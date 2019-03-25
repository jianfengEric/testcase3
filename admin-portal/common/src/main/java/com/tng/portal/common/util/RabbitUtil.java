package com.tng.portal.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.messaging.converter.GenericMessageConverter;

public class RabbitUtil {
    private static final Logger logger = LoggerFactory.getLogger(RabbitUtil.class);

    private static final String HOST = PropertiesUtil.getMQValueByKey("rabbitmq.host");
    private static final int PORT = Integer.parseInt(PropertiesUtil.getMQValueByKey("rabbitmq.port"));
    private static final String USERNAME = PropertiesUtil.getMQValueByKey("rabbitmq.username");
    private static final String PASSWORD = PropertiesUtil.getMQValueByKey("rabbitmq.password");
    private static final String VHOST = PropertiesUtil.getMQValueByKey("rabbitmq.vhost");
    private static final long REPLY_TIME_OUT = Long.parseLong(PropertiesUtil.getMQValueByKey("rabbitmq.replytimeout"));

    private static ConnectionFactory connectionFactory;

    static {
        connectionFactory = RabbitUtil.connectionFactory();
    }

    public static void send(String serviceName, MethodParameters parameters){
        logger.info("Send message by MQ to {}", serviceName);
        RabbitMessagingTemplate template = RabbitUtil.messagingTemplate(connectionFactory, serviceName);
        template.convertAndSend(getExchange(serviceName), getRoutingKey(serviceName), parameters.toJson());
    }

    public static <T> T send(String serviceName, MethodParameters parameters, Class<T> targetClass){
        logger.info("Send message by MQ to {}", serviceName);
        RabbitMessagingTemplate template = RabbitUtil.messagingTemplate(connectionFactory, serviceName);
        return template.convertSendAndReceive(getExchange(serviceName), getRoutingKey(serviceName), parameters.toJson(), targetClass);
    }

    private static ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setVirtualHost(VHOST);
        factory.setAddresses(HOST);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        return factory;
    }

    private static RabbitMessagingTemplate messagingTemplate(ConnectionFactory connectionFactory, String queueName) {
//        rabbitAdmin(connectionFactory, queueName);
        return RabbitUtil.simpleMessageTemplate(connectionFactory);
    }

    /**
     * init RabbitMessagingTemplate
     *
     * @param connectionFactory
     * @return
     */
    private static RabbitMessagingTemplate simpleMessageTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setReplyTimeout(REPLY_TIME_OUT);
        template.setReceiveTimeout(REPLY_TIME_OUT);
        RabbitMessagingTemplate rabbitMessagingTemplate = new RabbitMessagingTemplate();
        rabbitMessagingTemplate.setMessageConverter(new GenericMessageConverter());
        rabbitMessagingTemplate.setRabbitTemplate(template);
        return rabbitMessagingTemplate;
    }

    public static void startListening(Object receiver, String queueName){
        SimpleMessageListenerContainer listenerContainer = RabbitUtil.listenerContainer(connectionFactory, receiver, queueName);
        if(!listenerContainer.isRunning()){
            listenerContainer.start();
        }
    }

    private static SimpleMessageListenerContainer listenerContainer(ConnectionFactory connectionFactory, Object receiver, String queueName) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setRabbitAdmin(rabbitAdmin(connectionFactory, queueName));
        container.setMessageListener(new MessageListenerAdapter(receiver));
        return container;
    }

    private static RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory, String queueName){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        DirectExchange exchange = new DirectExchange(getExchange(queueName), true, false);
        rabbitAdmin.declareExchange(exchange);
        Queue queue = new Queue(queueName, true, false, false);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(getRoutingKey(queueName)));
        return rabbitAdmin;
    }

    private static String getExchange(String queueName) { return "EXCHANGE_".concat(queueName); }

    private static String getRoutingKey(String queueName) { return "ROUTING_KEY_".concat(queueName); }

}
