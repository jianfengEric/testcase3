package com.tng.portal.email.soa;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tng.portal.common.soa.AbstractMQBasicService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.email.EMailInput;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.email.entity.EmailMessage;
import com.tng.portal.email.exception.InvalidInputException;
import com.tng.portal.email.service.EmailService;


@Service("EmailSOAService")
public class EmailSOAService  extends AbstractMQBasicService{
	private static final Logger logger = LoggerFactory.getLogger(EmailSOAService.class);

	@Value("${email.service.name}")
	private String serviceName;

	@Autowired
	@Qualifier("emailService")
	private EmailService emailService;
	
	@PostConstruct
	public void initConnectToMQ(){
		if(ApplicationContext.Env.integrated_client.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))){
			logger.info("EmailSOAService connectToMQ start!!");
			startListening();
			logger.info("EmailSOAService connectToMQ end!!");
		}else{
			logger.info("EmailSOAService will not run!!");
		}

	}
	
	 public String getServiceName(){
		 return serviceName;
	 }


	public void sendEmail(EMailInput emailInput){
		try {
			emailService.sendEmail(emailInput);
		}catch (InvalidInputException e) {
			logger.error("send email error:" + e.getMessage());
		}catch (Exception e){
			logger.error("Exception",e);
		}
	}


	/**
	 * According to the job query has been sent mailing information list
	 * @param job email template code
	 * @return
	 */
	public RestfulResponse<List<EmailMessage>> getEmailMessages(String job){
		try {
			RestfulResponse<List<EmailMessage>> restResponse = new RestfulResponse<>();
			restResponse.setData(emailService.getEmailMessages(job));
			restResponse.setSuccessStatus();
			return restResponse;
		}catch (InvalidInputException e) {
			logger.error("send email error:" + e.getMessage());
			return getErrorResponse(e);
		}catch (Exception e){
			logger.error("Exception",e);
			return null;
		}
	}

	private RestfulResponse getErrorResponse(InvalidInputException e) {
		RestfulResponse restResponse = new RestfulResponse();
		restResponse.setFailStatus();
		restResponse.setErrorCode(e.getErrorCode().toString());
		restResponse.setMessageEN(e.getErrorMsg());
		restResponse.setMessageZhCN(e.getErrorMsg());
		restResponse.setMessageZhHK(e.getErrorMsg());
		return restResponse;
	}

	public Object getHandleInstance(){
		 return this;
	 }
	 
}