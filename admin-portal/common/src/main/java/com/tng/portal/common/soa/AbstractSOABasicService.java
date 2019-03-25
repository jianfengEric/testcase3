package com.tng.portal.common.soa;

import com.google.gson.Gson;
import com.tng.portal.common.soa.CommunicationChannelProperties.ChannelType;
import com.tng.portal.common.util.MethodParameters;
import com.tng.portal.common.util.RabbitMQUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Properties;

public abstract class AbstractSOABasicService implements MessageHandler {
	private static final Logger logger = LoggerFactory.getLogger(AbstractSOABasicService.class);
    protected CommunicationChannel commChannel;
    
    /**
     * Consumer connect to MQ
     */
    public void connectToMQ(){ 
    	try{
	    	Properties props = new Properties();
			props.load(RabbitMQUtil.class.getClassLoader().getResourceAsStream("rabbitMQ.properties"));
			String host = props.getProperty("rabbitmq.host");
			int port = Integer.parseInt(props.getProperty("rabbitmq.port"));
			String username = props.getProperty("rabbitmq.username");
			String password = props.getProperty("rabbitmq.password");
			String vhost = props.getProperty("rabbitmq.vhost");
	    	CommunicationChannelProperties commProps = new CommunicationChannelProperties();
			commProps.setChannelType(ChannelType.RABBIT_MQ);
			commProps.setHost(host);
			commProps.setPort(port);
			commProps.setUsername(username);
			commProps.setPassword(password);
			commProps.setVirtualHost(vhost);
			commProps.setExchangeName("TNG");
			commProps.addInterestedTopic(this.getServiceName() + ".*");
			commProps.setQueueName(this.getServiceName());
		
			commChannel = CommunicationChannelFactory.createCommunicationChannel(commProps);
	    	commChannel.connect();
	    	commChannel.addMessageHandler(this);
	    	commChannel.startListening();	
		}catch(Exception e){
			logger.error("connection rabbitmq error!",e);
		}
    }
   
    /**
     * Consumer handle the receive date
     */
    @Override
	public void handleMessageData(String routingKey, String replyTo, String correlationId, String data) {
    	logger.info("handleMessageData\t{}\t{}\t{}\t{}\t{}",this,routingKey,replyTo,correlationId,data);

		String returnResult = null;
		
		String methodName = routingKey.split("\\.")[1];
		MethodParameters params = MethodParameters.fromJson(data);
		Object[] parameters = params.getParameters();
		try {
			Object handleInstance = getHandleInstance();
			
			Class<?>[] paramTypes = params.getParameterTypes();
			Method method = handleInstance.getClass().getDeclaredMethod(methodName, paramTypes);
			Object ret = method.invoke(handleInstance, parameters);
								
			if(ret != null)
			{
				Gson gson = new Gson();
				returnResult =  gson.toJson(ret, method.getReturnType());
				if(replyTo != null){
			    	Message message = new Message();
					message.setTargetQueue(replyTo);
					message.setCorrelationId(correlationId);
					message.setContentAsString(returnResult);
					commChannel.send(message);
					logger.debug("send reply done!!!");
		    	}
			}
		}catch(Exception e){
			logger.error(getServiceName()+" failed to invoke method "+methodName,e);
		}
	}    
    
    public abstract String getServiceName();
    
    public abstract Object getHandleInstance();
}
