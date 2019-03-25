package com.tng.portal.common.soa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tng.portal.common.vo.rest.EmailParameterVo;


@Service("CommonRPCSoaAppDemo")
public class CommonRPCSoaAppDemo  extends AbstractSOABasicService{
	
	 private static final Logger logger = LoggerFactory.getLogger(CommonRPCSoaAppDemo.class);

	 public String getServiceName(){
		 return "Common.RPCSoaAppDemo";
	 }
			 
			
	 
	 public EmailParameterVo sendEmailTest(String subject,String receivers,String message){
		 
		 logger.info("send email success!!!! subject:{} receivers:{}  message:{}",subject,receivers,message);
		 EmailParameterVo emailParameterVo=new EmailParameterVo();
		 emailParameterVo.setJob("call back jobbb!");
		 emailParameterVo.setSubject(subject);
		 emailParameterVo.setReceivers(receivers);
		 emailParameterVo.setSender(message);
		 
		 return emailParameterVo;
		 
	 }
	 
	 
	 
	 
	 public Object getHandleInstance(){
		 
		 return this;
	 }
	 
}
