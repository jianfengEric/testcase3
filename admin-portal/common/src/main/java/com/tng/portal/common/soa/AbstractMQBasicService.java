package com.tng.portal.common.soa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpTimeoutException;

import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.MethodParameters;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.util.RabbitUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;

public abstract class AbstractMQBasicService {
	private static final Logger logger = LoggerFactory.getLogger(AbstractMQBasicService.class);

	long i = 0;
	protected void startListening() {
		if(!ApplicationContext.Communication.MQ.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.communicationStyle))){
			return;
		}
		String serviceName = getServiceName();
		logger.info("Start MQ listening\t{}", serviceName);
		try {
			RabbitUtil.startListening(getHandleInstance(), serviceName);
		}catch (Exception e) {
			if (e instanceof AmqpConnectException || e instanceof AmqpTimeoutException) {
				if (i > 60) {
					logger.error("RabbitMQ connection error,Listener boot failure!");
					return;
				} else {
					i++;
					logger.warn("RabbitMQ connection error,{} seconds later try again!", 5 * i);
					try {
						TimeUnit.SECONDS.sleep(5);
					} catch (InterruptedException e1) {
						logger.error(e1.getMessage());
					}

					startListening();
				}
			} else {
				logger.error("Start MQ listening error", e);
			}
		}
	}

    /**
     * Consumer handle the receive date
     */
	public Object handleMessage(String data) {
    	logger.info("handleMessageData\t{}\t{}",this,data);

		MethodParameters params = MethodParameters.fromJson(data);
		Object[] parameters = params.getParameters();
		try {
			Object handleInstance = getHandleInstance();
			
			Class<?>[] paramTypes = params.getParameterTypes();
			Method method = handleInstance.getClass().getDeclaredMethod(params.getMethodName(), paramTypes);
			Object ret = this.methodInvoke(method, handleInstance, parameters);   //method.invoke(handleInstance, parameters);
			logger.info("resp data {}", ret);
			return ret;
		}catch(Exception e){
			logger.error("failed to invoke method {}", params.getMethodName(), e);
		}

		return null;
	}
	
	private Object methodInvoke(Method method,Object handleInstance, Object[] parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		try{	
			return method.invoke(handleInstance, parameters);
		}catch(InvocationTargetException e){
			if(e.getTargetException() instanceof BusinessException){
				logger.error("mq invoke error 1",e);
				BusinessException be = (BusinessException)e.getTargetException();
				RestfulResponse resp = RestfulResponse.failData();
				resp.setErrorCode(String.valueOf(be.getErrorcode()));
				return resp;
			}else{
				logger.error("mq invoke error 2",e);
				return RestfulResponse.nullData();
			}
		}
	}

	public abstract String getServiceName();

    public abstract Object getHandleInstance();


}
