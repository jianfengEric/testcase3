/*
package com.tng.portal.common.soa;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.RabbitMQUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NioSocketAcceptor extends Thread{
	private static transient Logger log = LoggerFactory.getLogger(NioSocketAcceptor.class);
	private IoHandler ioHandler;
	private String type;
	private static Properties pros = new Properties();
	static {
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("rabbitMQ.properties"));
		} catch (Exception e) {
			log.error("Can't loading rabbit.properties file ...", e);
		}
	}
	public static final String TOPIC = "topic";
	public static final String FANOUT = "fanout";
	public NioSocketAcceptor(IoHandlerAdapter ioHandler, String type){
		this.ioHandler = ioHandler;
		this.type = type;
		init();
	}
	private Connection conn = null;
	private ExecutorService executor = null;

	private void init(){
		try{
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(pros.getProperty("rabbitmq.host"));
			factory.setPort(Integer.valueOf(pros.getProperty("rabbitmq.port")));
			factory.setUsername(pros.getProperty("rabbitmq.username"));
			factory.setPassword(pros.getProperty("rabbitmq.password"));
			factory.setVirtualHost(pros.getProperty("rabbitmq.vhost"));
			factory.setAutomaticRecoveryEnabled(Boolean.valueOf(pros.getProperty("rabbitmq.automaticrecoveryenabled")));
			factory.setNetworkRecoveryInterval(Long.valueOf(pros.getProperty("rabbitmq.networkrecoveryinterval")));//ms
			factory.setTopologyRecoveryEnabled(Boolean.valueOf(pros.getProperty("rabbitmq.topologyrecoveryenabled")));
			factory.setRequestedHeartbeat(Integer.valueOf(pros.getProperty("rabbitmq.requestedheartbeat")));//s
			factory.setConnectionTimeout(Integer.valueOf(pros.getProperty("rabbitmq.connectiontimeout")));//ms

			String[] hosts = pros.getProperty("rabbitmq.host").split(",");
			Address[] addresses = new Address[hosts.length];
			for (int i = 0; i < hosts.length; i++) {
				addresses[i] = new Address(hosts[i]);
			}
			conn = factory.newConnection(addresses);
		}catch (Exception e) {
			ioHandler.exceptionCaught(e);
		}
	}

	public void execute(){
		if(executor!=null){
			executor.shutdownNow();
			executor = null;
		}
		executor = Executors.newSingleThreadExecutor();
		executor.submit(this);
		log.info("NioSocketAcceptor(MQ"+pros.getProperty("rabbitmq.host")+") Startup Successful !");
	}
	@Override
	public void run(){ 
		try{
			if(FANOUT.equals(this.type)) {
				String exchange = pros.getProperty("rabbitmq.exchange");
				Channel channel = conn.createChannel();
				channel.exchangeDeclare(exchange, FANOUT);
				String queueName = channel.queueDeclare().getQueue();
				channel.queueBind(queueName, exchange, "");
				channel.basicConsume(queueName, true, "commonConsumerTag",
						new DefaultConsumer(channel) {
							@Override
							public void handleDelivery(String consumerTag,
													   Envelope envelope,
													   AMQP.BasicProperties properties,
													   byte[] body) throws IOException {
								String routingKey = envelope.getRoutingKey();
								String contentType = properties.getContentType();
								long deliveryTag = envelope.getDeliveryTag();
								String message = new String(body, "utf-8");
								log.debug("routingKey = " + routingKey + "; contentType = " + contentType + "; deliveryTag = " + deliveryTag + "; message = " + message);
								ioHandler.messageReceived(message);
							}
						});
			}else if(TOPIC.equals(this.type)){
				Channel channel = conn.createChannel();
				channel.exchangeDeclare("TNG", TOPIC);

				String serviceName = MessageFormat.format(ApplicationContext.get(ApplicationContext.Key.serviceNameTemplate),ApplicationContext.get(ApplicationContext.Key.serviceName));
				String queueName = serviceName + "_CLIENT";
				channel.queueDeclare(queueName, true, false, false, null);

				String topic = queueName + ".*";
				channel.queueBind(queueName, "TNG", topic);

				channel.basicQos(1);
				channel.basicConsume(queueName, false, "commonConsumerTag",
						new DefaultConsumer(channel) {
							@Override
							public void handleDelivery(String consumerTag,
													   Envelope envelope,
													   AMQP.BasicProperties properties,
													   byte[] body) throws IOException {
								String routingKey = envelope.getRoutingKey();
								String contentType = properties.getContentType();
								long deliveryTag = envelope.getDeliveryTag();
								String message = new String(body, "utf-8");
								log.debug("routingKey = " + routingKey + "; contentType = " + contentType + "; deliveryTag = " + deliveryTag + "; message = " + message);
								ioHandler.messageReceived(message);
								channel.basicAck(envelope.getDeliveryTag(), false);
							}
						});
			}
		}catch (Exception e) {
			ioHandler.exceptionCaught(e);
		}
	}
}
*/
