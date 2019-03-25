package com.tng.portal.common.util;

import com.tng.portal.common.soa.CommunicationChannel;
import com.tng.portal.common.soa.CommunicationChannelFactory;
import com.tng.portal.common.soa.CommunicationChannelProperties;
import com.tng.portal.common.vo.rest.RestfulResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RabbitMQUtil {
	
	private static CommunicationChannel commChannel = null;
	
	private RabbitMQUtil(){}
	
	/**
	 * Producer get RabbitMQ connect channel
	 */
	public static synchronized CommunicationChannel getCommChannel() throws IOException {
		if(commChannel == null) {
			Properties props = new Properties();
			props.load(RabbitMQUtil.class.getClassLoader().getResourceAsStream("rabbitMQ.properties"));
			String host = props.getProperty("rabbitmq.host");
			int port = Integer.parseInt(props.getProperty("rabbitmq.port"));
			String username = props.getProperty("rabbitmq.username");
			String password = props.getProperty("rabbitmq.password");
			String vhost = props.getProperty("rabbitmq.vhost");
			
			CommunicationChannelProperties commProps = new CommunicationChannelProperties();
			commProps.setChannelType(CommunicationChannelProperties.ChannelType.RABBIT_MQ);
			commProps.setHost(host);
			commProps.setPort(port);
			commProps.setUsername(username);
			commProps.setPassword(password);
			commProps.setVirtualHost(vhost);
			commProps.setExchangeName("TNG");
			
			commChannel = CommunicationChannelFactory.createCommunicationChannel(commProps);
	    	commChannel.connect();
		}
		return commChannel;
	}
	
	/**
	 * 
	 * @param wait         when wait == 0, send without reply; when wait >0, will wait for reply for "wait" Millisecond 
	 * @param serviceName  the serviceName of the class that implements AbstractSOABasicService
	 * @param methodName   the callback method
	 * @param arguments    the callback method arguments
	 * @return HashMap
	 * @throws IOException 
	 * @throws Exception
	 */
	/*@SuppressWarnings("rawtypes")
	public static Map sendRequestToSOAService(long wait, String serviceName, String methodName, MqParam... mqParams) throws IOException {

			if(serviceName == null)
			{
				throw new IllegalArgumentException("sendRequestToSOAService::serviceName is null!");
			}else if(methodName == null)
			{
				throw new IllegalArgumentException("sendRequestToSOAService::methodName is null!");
			}else if(mqParams == null)
			{
				throw new IllegalArgumentException("sendRequestToSOAService::arguments is null!");
			}
			
			String[] paramTypes = new String[mqParams.length];
			Object[] arguments = new String[mqParams.length];
			for(int i = 0; i < paramTypes.length; i++)
			{
				paramTypes[i] = mqParams[i].getClazz().getName();
				arguments[i] = (Object)mqParams[i].getValue();
			}
				
			MethodParameters params = new MethodParameters();
			params.setParameterTypes(paramTypes);
			params.setParameters(arguments);
			Message message = new Message();
			message.setTopic(String.format("%s.%s", serviceName, methodName));
	        message.setContentAsString(params.toJson());
	        CommunicationChannel comm = getCommChannel();
	        String response = "";	            
			if(wait == 0){
				comm.send(message);
			}else{
				response = comm.sendAndWait(message, wait);
			}
			Gson gson = new Gson();
			return gson.fromJson(response, HashMap.class);
	}*/


	public static void sendRequestToSOAService(String serviceName, String methodName, MqParam... mqParams) throws IOException {

		if(serviceName == null)
		{
			throw new IllegalArgumentException("sendRequestToSOAService::serviceName is null!");
		}else if(methodName == null)
		{
			throw new IllegalArgumentException("sendRequestToSOAService::methodName is null!");
		}else if(mqParams == null)
		{
			throw new IllegalArgumentException("sendRequestToSOAService::arguments is null!");
		}

		List<String> paramTypes = new ArrayList<>();
		List<Object> arguments = new ArrayList<>();
		for(int i = 0; i < mqParams.length; i++)
		{
			paramTypes.add(mqParams[i].getClazz().getName());
			arguments.add(mqParams[i].getValue());
		}

		MethodParameters params = new MethodParameters();
		params.setParameterTypes(paramTypes.stream().toArray(String[]::new));
		params.setParameters(arguments.stream().toArray(Object[]::new));
		params.setMethodName(methodName);
		RabbitUtil.send(serviceName, params);
	}

	/**
	 * 
	 * @param wait          when wait == 0, send without reply; when wait >0, will wait for reply for "wait" Millisecond 
	 * @param serviceName   the serviceName of the class that implements AbstractSOABasicService
	 * @param methodName    the callback method
	 * @param responseClass the class that you want to response
	 * @param arguments     the callback method arguments
	 * @return responseClass
	 * @throws Exception
	 */
	/*@SuppressWarnings("rawtypes")
	public static <T> RestfulResponse sendRequestToSOAService(long wait, String serviceName, String methodName, Class<T> responseClass, MqParam... mqParams) throws Exception {

		if(serviceName == null)
		{
			throw new IllegalArgumentException("sendRequestToSOAService::serviceName is null!");
		}else if(methodName == null)
		{
			throw new IllegalArgumentException("sendRequestToSOAService::methodName is null!");
		}else if(mqParams == null)
		{
			throw new IllegalArgumentException("sendRequestToSOAService::arguments is null!");
		}

		List<String> paramTypes = new ArrayList<>();
		List<Object> arguments = new ArrayList<>();
		for(int i = 0; i < mqParams.length; i++)
		{
			paramTypes.add(mqParams[i].getClazz().getName());
			arguments.add(mqParams[i].getValue());
		};

		MethodParameters params = new MethodParameters();
		params.setParameterTypes(paramTypes.stream().toArray(String[]::new));
		params.setParameters(arguments.stream().toArray(Object[]::new));
		Message message = new Message();
		message.setTopic(String.format("%s.%s", serviceName, methodName));
		message.setContentAsString(params.toJson());
		CommunicationChannel comm = getCommChannel();
		String response = "";
		if(wait == 0){
			comm.send(message);
		}else{
			response = comm.sendAndWait(message, wait);
		}

		if(null == response){
			return null;
		}

		Gson gson = new Gson();
		RestfulResponse restResponse = gson.fromJson(response, RestfulResponse.class);

		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(response);
		JsonObject object =	jsonElement.getAsJsonObject();
		if(object.has("data") && null != responseClass){
			JsonElement je = object.get("data");
			if(je.isJsonObject()){
				T t = gson.fromJson(je, responseClass);
				restResponse.setData(t);
			}else if (je.isJsonArray()){
				List<T> list = new ArrayList<>();
				JsonArray array = je.getAsJsonArray();
				Iterator<JsonElement> it=array.iterator();
				while(it.hasNext()){
					T t= gson.fromJson(it.next(), responseClass);
					list.add(t);
				}
				restResponse.setData(list);
			}
		}

		return restResponse;
	}*/


	public static <T> RestfulResponse sendRequestToSOAService(String serviceName, String methodName, Class<T> responseClass, MqParam... mqParams) throws Exception {
		if(serviceName == null)
		{
			throw new IllegalArgumentException("sendRequestToSOAService::serviceName is null!");
		}else if(methodName == null)
		{
			throw new IllegalArgumentException("sendRequestToSOAService::methodName is null!");
		}else if(mqParams == null)
		{
			throw new IllegalArgumentException("sendRequestToSOAService::arguments is null!");
		}

		List<String> paramTypes = new ArrayList<>();
		List<Object> arguments = new ArrayList<>();
		for(int i = 0; i < mqParams.length; i++)
		{
			paramTypes.add(mqParams[i].getClazz().getName());
			arguments.add(mqParams[i].getValue());
		}

		MethodParameters params = new MethodParameters();
		params.setParameterTypes(paramTypes.stream().toArray(String[]::new));
		params.setParameters(arguments.stream().toArray(Object[]::new));
		params.setMethodName(methodName);
		return RabbitUtil.send(serviceName, params, RestfulResponse.class);
	}
}
