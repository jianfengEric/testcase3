package com.tng.portal.common.soa;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class RabbitMQ implements CommunicationChannel {
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQ.class);
	private CommunicationChannelProperties props;
	private Connection connection;
	private ExecutorService workingThread = null;
	private Channel recvChannel;
	private Channel sendChannel;
	private QueueingConsumer callbackConsumer;	
	private String callbackQueueName;
	private volatile boolean listening = false;
	private ConcurrentHashMap<MessageHandler, MessageHandler> messageHandlers = new ConcurrentHashMap<>();
	private ReentrantLock lock = new ReentrantLock();
	
	public RabbitMQ(CommunicationChannelProperties props) {
		this.props = props;
	}

	public Status getConnectionStatus() {
		if(connection == null){
			return Status.DISCONNECTED;
		}else{
			if(connection.isOpen()){
				return Status.CONNECTED;
			}else{
				return Status.CONNECTING;
			}
		}
	}

	/**
	 * Producer/Consumer connect to MQ
	 */
	public void connect() {
		Address[] addrArr = null;
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(props.getHost());
	    factory.setPort(props.getPort());
	    factory.setUsername(props.getUsername());
	    factory.setPassword(props.getPassword());
	    factory.setVirtualHost(props.getVirtualHost());

	    factory.setAutomaticRecoveryEnabled(true);
	    factory.setNetworkRecoveryInterval(6000);//ms
	    factory.setTopologyRecoveryEnabled(true);
	    factory.setRequestedHeartbeat(60);//s
	    factory.setConnectionTimeout(3000);//ms
	    
	    logger.info("props.getHost:{} {}",props.getHost(),props.getHost().contains(","));
	    if(props.getHost().contains(",")) {
	    	String[] hosts = props.getHost().split(",");
	    	addrArr = new Address[hosts.length];
	    	for(int i=0; i < hosts.length; i++){
	    		addrArr[i] = new Address(hosts[i], props.getPort());
	    	}
	    }
	    try {
	    	if(null!=addrArr){
	    		logger.debug("using addrArr without workingThread ");
	    		connection = factory.newConnection(addrArr);
	    	}else{
	    		logger.debug("using props.getHost ");
	    		connection = factory.newConnection();
	    	}
		} catch (Exception e) {
			logger.error("connection rabbitmq error", e);
			return;
		}
	}
	
	/**
	 * Producer/Consumer disconnect to MQ
	 */
	@Override
	public void disconnect() {
		lock.lock();
		try {
			if (sendChannel != null && sendChannel.isOpen()) {
				try {
					sendChannel.close();
					sendChannel = null;
				} catch (Exception e) {
					logger.error("RabbitMQ disconnect sendChannel Close Error:" + sendChannel, e);
				}
			}
			if (recvChannel != null && recvChannel.isOpen()) {
				try {
					recvChannel.close();
					sendChannel = null;
				} catch (Exception e) {
					logger.error("RabbitMQ disconnect recvChannel Error:" + recvChannel, e);
				}
			}
			if (connection != null && connection.isOpen()) {
				try {
					connection.close();
					connection = null;
				} catch (Exception e) {
					logger.error("RabbitMQ disconnect connection Error:" + connection, e);
				}
			}
		}finally {
			lock.unlock();
		}
	}
	/**
	 * Consumer start listening
	 */
	@Override
	public void startListening(){
		logger.info("startListening called connection:"+connection);
		try {
			if(workingThread!=null){
				workingThread.shutdownNow();
				workingThread = null;
			}

			if(recvChannel == null || !recvChannel.isOpen()){
				recvChannel = null;
				if(connection==null||!connection.isOpen()){
					long i = 0;
					while(true){
						connection = getConnetcion();
						if(i>60){
							logger.error("RabbitMQ connection error,Listener boot failure!");
							return;
						}
						if(connection == null){
							i++;
							logger.warn("RabbitMQ connection error,{} seconds later try again!",5*i);
							Thread.sleep(5000*i);
						}else{
							break;
						}
					}
				}
				recvChannel = connection.createChannel();
			}
			workingThread = Executors.newSingleThreadExecutor();
			String exchangeName = props.getExchangeName();
			if(!"".equals(exchangeName)){
				recvChannel.exchangeDeclare(exchangeName, "topic");
			}
			String queueName = props.getQueueName();
			if(queueName == null){
				queueName = recvChannel.queueDeclare().getQueue();
			} else {
				recvChannel.queueDeclare(queueName, true, false, false, null);
			}
			for(String topic : props.getInterestedTopics()){
				recvChannel.queueBind(queueName, exchangeName, topic);
			}
			recvChannel.basicQos(1);
			listening = true;
			workingThread.submit(new ListeningTask(queueName));
		} catch (Exception e) {
			logger.error("RabbitMQ connection error", e);
			listening = false;
		}
	}
	public void stopListening() {
		listening = false;
		try{
			workingThread.shutdownNow();
			workingThread = null;
		}catch(Exception e){
			logger.error("ignore worker shutdownNow",e);
		}
	}

	public Connection getConnetcion(){
		if(null != connection && !connection.isOpen()){
			try{
				connection.close();
			}catch (Exception e){
				logger.warn("RabbitMQ connection close error!",e);
			}
			connection = null;
			connect();
		}
		return connection;
	}

	/**
	 * Producer send MQ message, without reply
	 * @param message   MQ message
	 */
	public void send(Message message){
		lock.lock();
		try {
			if(sendChannel == null||!sendChannel.isOpen()){
				sendChannel = null;
				if(connection==null||!connection.isOpen()){
					connection = getConnetcion();
					if(connection == null){
						logger.error("RabbitMQ connection error,Please try again!");
						return;
					}
				}
				sendChannel = connection.createChannel();
			}
			String exchangeName = props.getExchangeName();
			logger.debug("exchangeName:{} props {}",exchangeName,props.toString());
			if(!"".equals(exchangeName)){
				sendChannel.exchangeDeclare(exchangeName, "topic");
			}
			String routingKey = message.getTopic();
			String targetQueue = message.getTargetQueue();
			if(targetQueue != null && !targetQueue.equals(""))
			{
				exchangeName = "";
				routingKey = targetQueue;
			}
			String contentAsString = message.getContentAsString(); 
			byte[] content = contentAsString == null?null:contentAsString.getBytes(Message.ENCODING);
			String correlationID = message.getCorrelationId();
			BasicProperties basicProps = new BasicProperties
                    .Builder()
					.deliveryMode(2) //persistent
                    .correlationId(correlationID)
                    .build();
			sendChannel.basicPublish(exchangeName, routingKey, basicProps, content);
		} catch (Exception e) {
			logger.error("RabbitMQ send error", e);
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 *  Producer send MQ message, waiting for reply for "timeout" Millisecond 
	 * @param message    MQ message
	 * @return String
	 */
	public String sendAndWait(Message message){
		return sendAndWait(message, Long.MAX_VALUE);
	}
	
	/**
	 *  Producer send message, waiting for reply for "timeout" Millisecond 
	 */
	public String sendAndWait(Message message, long timeout){
		String response = null;
		lock.lock();
		try {
			if(sendChannel == null || !sendChannel.isOpen()){
				sendChannel = null;
				if(connection==null||!connection.isOpen()){
					connection = getConnetcion();
					if(connection == null){
						logger.warn("RabbitMQ connection error,Please try again!");
						return null;
					}
				}
				sendChannel = connection.createChannel();
				callbackQueueName = null;
			}
			String exchangeName = props.getExchangeName();
			logger.debug("exchangeName:"+exchangeName+" props "+props.toString());
			if(!"".equals(exchangeName)){
				sendChannel.exchangeDeclare(exchangeName, "topic");
			}
			String routingKey = message.getTopic();
			String targetQueue = message.getTargetQueue();
			if(targetQueue != null && !targetQueue.equals("")){
				exchangeName = "";
				routingKey = targetQueue;
			}
			byte[] content = message.getContentAsString().getBytes(Message.ENCODING);
			String correlationID = UUID.randomUUID().toString();
			message.setCorrelationId(correlationID);
			if(callbackQueueName == null){
				callbackQueueName = sendChannel.queueDeclare().getQueue();
			}
			String replyTo = callbackQueueName;
			BasicProperties basicProps = new BasicProperties
                    .Builder()
					.deliveryMode(2) //persistent
					.expiration(String.valueOf(timeout))
                    .correlationId(correlationID)
                    .replyTo(replyTo)
                    .build();
			sendChannel.basicPublish(exchangeName, routingKey, basicProps, content);
			logger.debug("basicPublish exchangeName:{} routingKey {} basicProps {}  callbackQueueName:{}",exchangeName,routingKey,basicProps,callbackQueueName);
			//RPC wait for response
			if(replyTo != null){
				if(callbackConsumer == null||(!callbackConsumer.getChannel().equals(sendChannel))){
					callbackConsumer = null;
					callbackConsumer = new QueueingConsumer(sendChannel);
				}
				logger.debug("sendChannel:{} callbackQueueName {} callbackConsumer {}",sendChannel,callbackQueueName,callbackConsumer);
				sendChannel.basicConsume(callbackQueueName, true, callbackConsumer);
				while (true) {
					logger.debug("in rabbitmq loop!!!");
			        QueueingConsumer.Delivery delivery = callbackConsumer.nextDelivery(timeout);
			        if(delivery == null){
						logger.warn("RabbitMQ send RPC timeouted");
			        	break;
			        }
			        if (delivery.getProperties().getCorrelationId().equals(correlationID)) {
			            response = new String(delivery.getBody(), Message.ENCODING);
			            break;
			        }
			        logger.debug("in rabbitmq loop end!!!");
			    }
			}
		}catch (Exception e) {
			logger.error("RabbitMQ send error", e);
		} finally {
			lock.unlock();
		}
		return response;
	}
	
	/**
	 * Consumer listening task
	 *
	 */
	private class ListeningTask implements Runnable {
		private String queueName;
		private QueueingConsumer consumer = null;
		
		private void setConsumer() throws IOException {
			consumer = new QueueingConsumer(recvChannel);
			recvChannel.basicConsume(queueName, false, consumer);
		}
		
		private QueueingConsumer.Delivery getNextDelivery(QueueingConsumer consumer){
			QueueingConsumer.Delivery delivery = null;
			try {
				delivery = consumer.nextDelivery();
			} catch (Exception e) {
				logger.error("ListeningTask.getNextDelivery error: "+e);
				startListening();
			}
			return delivery;
		}
		
		public ListeningTask(String queueName)
		{
			this.queueName = queueName;
		}
		
		public void run() {
			logger.info("Rabbitmq RPCServer Is Listened , Startup Successful!");
			try {
				setConsumer() ;
			} catch (Exception e) {
				logger.error("RabbitMQ listening error", e);
			}
			while (listening) {
				try {
					QueueingConsumer.Delivery delivery = getNextDelivery(consumer);
					if(delivery == null){
						return;
					}
					BasicProperties basicProps = delivery.getProperties();
					String routingKey = delivery.getEnvelope().getRoutingKey();
					String data = new String(delivery.getBody(), Message.ENCODING);
					logger.info("RabbitMQ received:{} data:{}",routingKey,data);
					for(MessageHandler handler : messageHandlers.values()){
	            		logger.debug("handler.handleMessageData");
	            		handler.handleMessageData(routingKey, basicProps.getReplyTo(), basicProps.getCorrelationId(), data);
		            }
		            recvChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
	
				} catch (Exception e) {
					logger.error("RabbitMQ listening error", e);
				}
	        }
			logger.debug("ListeningTask.run exited ,listening: {}" ,listening);
		}
		
	}

	public Collection<MessageHandler> getMessageHandlers() {
		return messageHandlers.values();
	}
	
	 /**
	 * Consumer, add messageHandler
	 */	
	public void addMessageHandler(MessageHandler handler) {
		messageHandlers.putIfAbsent(handler, handler);
	}
	
}
