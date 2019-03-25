package com.tng.portal.sms.service.impl;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.repository.AnaAccountRepository;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.ana.vo.SMSJobQueryVo;
import com.tng.portal.ana.vo.SMSJobResponse;
import com.tng.portal.ana.vo.SMSQueryParamVo;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.MqParam;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.util.RabbitMQUtil;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.common.vo.wfl.request.SMSJobInputVo;
import com.tng.portal.sms.constant.SystemMsg;
import com.tng.portal.sms.service.JobService;
import com.tng.portal.sms.util.StringUtil;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class JobServiceImpl implements JobService{
    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);
    
    @Qualifier("anaUserService")
	@Autowired
	private UserService userService;

	@Autowired
	private AccountService accountService;

	@Autowired
	@Qualifier("httpClientUtils")
	private HttpClientUtils httpClientUtils;
	
	@Autowired
	private AnaAccountRepository anaAccountRepository;
	
    private static final String SERVICE_NAME = PropertiesUtil.getAppValueByKey("sms.server.service.name");
    
    private static final String APPLICATION_CODE = PropertiesUtil.getAppValueByKey("application");
    
    private static final String JOB_CREATE_METHOD = PropertiesUtil.getAppValueByKey("job.create.method");
    
    private static final String COMMUNICATION_STYPE = PropertiesUtil.getAppValueByKey("communication.style");
    
    private static final String FILE_PATH = PropertiesUtil.getAppValueByKey("file.path");
    
    /**
	 * Query sms job list 
	 * 
	 * @param vo
	 * 			sms query param vo
	 * 
	 * @return
	 */
	@Override
	@Transactional
	public PageDatas<SMSJobQueryVo> getJobsByPage(SMSQueryParamVo vo){
		Account account = userService.getCurrentAccountInfo();
		
		vo.setApplicationCode(APPLICATION_CODE);
			List<AnaAccount> anaAccounts = anaAccountRepository.findByExternalGroupId(account.getExternalGroupId());
	    	if(anaAccounts != null && !anaAccounts.isEmpty())
	    		vo.setAccountIds(anaAccounts.stream().map(item -> {return item.getId();}).collect(Collectors.toList()));
		SMSJobResponse resp = null;
		try {
			resp = httpClientUtils.httpPost(PropertiesUtil.getAppValueByKey("sms.server.query.api"), vo, SMSJobResponse.class, null,null);
		}catch(Exception e){
        	throw new BusinessException(SystemMsg.SMSServerErrorMsg.SMS_SERVER_CONNECT_ERROR.getErrorCode());
        }
		
		if(resp == null 
				|| "fail".equalsIgnoreCase(resp.getStatus())){
        	throw new BusinessException(SystemMsg.SMSServerErrorMsg.SMS_SERVER_RETURN_ERROR.getErrorCode());
		}

		PageDatas<SMSJobQueryVo> pData = resp.getData();
		List<SMSJobQueryVo> voList = pData.getList();
		voList.stream().map(item->{
			SMSJobQueryVo queryVo = item;
			if(StringUtils.isNotBlank(queryVo.getSenderId())){
				AnaAccount anaAccount = accountService.getAccount(queryVo.getSenderId());
				if(null != anaAccount)	
					queryVo.setSenderName(anaAccount.getFullName());
			}
			return queryVo;
		}).collect(Collectors.toList());

		pData.setList(voList);
		return pData;
	}

	/**
	 * add sms job 
	 * 
	 * @param vo
	 * 			sms job info
	 * 
	 * @return
	 * @throws Exception 
	 */
	@Override
	@Transactional
	public void addJob(SMSJobInputVo vo){
		Account account = userService.getCurrentAccountInfo();
		
		if(StringUtils.isNotBlank(vo.getUploadFileName())){
			vo.setMobileNumbers(assembMobileNumber(vo));
		}
		
		if(StringUtils.isBlank(vo.getMobileNumbers())){
			return;
		}
		
		vo.setCreateBy(account.getAccountId());
		vo.setApplicationCode(APPLICATION_CODE);
		
		RestfulResponse<Long> response = null;
		try{
			if("mq".equals(COMMUNICATION_STYPE)){
				response = RabbitMQUtil.sendRequestToSOAService( SERVICE_NAME, JOB_CREATE_METHOD, Long.class, MqParam.gen(vo));
			}else if("http".equals(COMMUNICATION_STYPE)){
            	response = httpClientUtils.httpPost(PropertiesUtil.getAppValueByKey("sms.server.send.api"), vo, RestfulResponse.class, userService.getToken(),null);
			}
		}catch(Exception e){
        	throw new BusinessException(SystemMsg.SMSServerErrorMsg.SMS_SERVER_CONNECT_ERROR.getErrorCode());
        }
		
		if(response == null 
				|| "fail".equalsIgnoreCase(response.getStatus())){
        	throw new BusinessException(SystemMsg.SMSServerErrorMsg.SMS_SERVER_RETURN_ERROR.getErrorCode());
		}
	}

	@Override
	public String assembMobileNumber(SMSJobInputVo vo) {
		String numbers = vo.getMobileNumbers() == null ? "" : vo.getMobileNumbers();
		if(StringUtils.isNotBlank(vo.getUploadFileName())){
			File file = new File(FILE_PATH + File.separator + vo.getUploadFileName());
			Pattern pattern = Pattern.compile("[0-9]*");
			CSVReader reader = null;
			List<String[]> rows = null;
			
			try{
		        FileReader fReader = new FileReader(file);  
		        reader = new CSVReader(fReader);  
		        rows = reader.readAll();
			}catch(Exception e){
				throw new BusinessException(SystemMsg.ServerErrorMsg.CSV_FILE_PARSE_ERROR.getErrorCode());
			}finally {
				try {
					reader.close();
				} catch (IOException e) {} 
			}
			
	        for(String[] row : rows){
	            for(String mobile : row){  
	            	mobile = mobile.trim();
	                if(StringUtils.isNotBlank(mobile)) {
	                	Matcher match = pattern.matcher(mobile);
	                	if(!match.matches())
	                		throw new BusinessException(SystemMsg.ServerErrorMsg.INVALID_MOBILE_INPUT_FORMAT.getErrorCode());
	                	numbers += StringUtils.isBlank(numbers) ? mobile : "," + mobile;
	                }
	            }
	        }
		}
		
		String [] mobiles = numbers.trim().split(",");
		if(mobiles.length == 0)
			throw new BusinessException(SystemMsg.ServerErrorMsg.MOBILE_NUMBER_REQUIRED.getErrorCode());
		else if(mobiles.length > 1000)
			throw new BusinessException(SystemMsg.ServerErrorMsg.MOBILE_NUMBER_EXCEEDING.getErrorCode());
		
		StringBuilder builder = new StringBuilder();
		StringBuilder repeatBuilder = new StringBuilder();
		int i = 0;
        for(String mobile : mobiles) {  
            if(builder.indexOf(","+mobile+",") > -1) {
            	switch(i){
            	case 0 :
            	case 1 :
            	case 2 :
            	case 3 : repeatBuilder.append(mobile).append(","); break;
            	case 4 : repeatBuilder.append(mobile); break;
            	case 5 : repeatBuilder.append("..."); break;
            	}
            	i++;
            } else {  
                builder.append(",").append(mobile).append(",");  
            }  
        }
        
        String repeatStr = repeatBuilder.toString();
        if(repeatStr.endsWith(","))
        	repeatStr = repeatStr.substring(0, repeatStr.length()-1);
        
        if(repeatStr.length() != 0)
        	throw new BusinessException(SystemMsg.ServerErrorMsg.REPEATED_MOBILE_NUMBER.getErrorCode(), new String[]{repeatStr});
        
		return numbers;
	}

	@Override
	public void terminateJob(Long id) {
		RestfulResponse<String> response = null;
		try{
			Map<String, String> params = new HashMap<>();
            params.put("id", id.toString());
			response = httpClientUtils.httpGet(PropertiesUtil.getAppValueByKey("sms.server.terminate.api"), RestfulResponse.class, params);
		}catch(Exception e){
        	throw new BusinessException(SystemMsg.SMSServerErrorMsg.SMS_SERVER_CONNECT_ERROR.getErrorCode());
        }
		
		if(response == null 
				|| "fail".equalsIgnoreCase(response.getStatus())){
        	throw new BusinessException(SystemMsg.SMSServerErrorMsg.SMS_SERVER_RETURN_ERROR.getErrorCode());
		}else if("1001".equalsIgnoreCase(response.getData())){
        	throw new BusinessException(SystemMsg.SMSServerErrorMsg.JOB_ALREADY_COMPLETED.getErrorCode());
        }
	}

	@Override
	public String findMobile(Long id, String status, String mobile) {
		RestfulResponse<String> resp = null;
		try{
			Map<String, String> params = new HashMap<>();
            params.put("id", id.toString());
            params.put("status", status);
            params.put("mobile", mobile);
			resp = httpClientUtils.httpGet(PropertiesUtil.getAppValueByKey("sms.server.findMobile.api"), RestfulResponse.class, params);
		}catch(Exception e){
        	throw new BusinessException(SystemMsg.SMSServerErrorMsg.SMS_SERVER_CONNECT_ERROR.getErrorCode());
        }
		
		if(resp == null 
				|| "fail".equalsIgnoreCase(resp.getStatus())){
        	throw new BusinessException(SystemMsg.SMSServerErrorMsg.SMS_SERVER_RETURN_ERROR.getErrorCode());
		}
		
		return resp.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void resend(String type, String key){
		Account account = userService.getCurrentAccountInfo();
		Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("key", key);
		params.put("senderId", null==account?"":account.getAccountId());
		
		RestfulResponse<Long> response = null;
		try{
			response = httpClientUtils.httpGet(PropertiesUtil.getAppValueByKey("sms.server.resend.api"), RestfulResponse.class, params);
		}catch(Exception e){
			logger.error("sms server error", e);
        	throw new BusinessException(SystemMsg.SMSServerErrorMsg.SMS_SERVER_CONNECT_ERROR.getErrorCode());
        }
		
		if(response == null 
				|| "fail".equalsIgnoreCase(response.getStatus())){
        	throw new BusinessException(SystemMsg.SMSServerErrorMsg.SMS_SERVER_RETURN_ERROR.getErrorCode());
		}
	}

}