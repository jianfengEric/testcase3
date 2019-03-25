package com.gea.portal.dpy.service.impl;

import com.gea.portal.dpy.entity.DeployQueue;
import com.gea.portal.dpy.entity.DeploySyncMapResult;
import com.gea.portal.dpy.enumeration.DeploySyncType;
import com.gea.portal.dpy.enumeration.MapDeployStatus;
import com.gea.portal.dpy.enumeration.MapServerAckStatus;
import com.gea.portal.dpy.repository.DeployQueueRepository;
import com.gea.portal.dpy.repository.DeploySyncMapResultRepository;
import com.gea.portal.dpy.service.*;
import com.gea.portal.dpy.util.GeaModuleMappingUtil;
import com.gea.portal.dpy.util.GeaServiceName;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.tng.portal.common.constant.DateCode;
import com.tng.portal.common.dto.DeployDetailDto;
import com.tng.portal.common.dto.DeployDetailEntry;
import com.tng.portal.common.dto.DeployDetailEntry.Detail;
import com.tng.portal.common.dto.gea.GeaBaseResponse;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.DeployType;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.ServiceName;
import com.tng.portal.common.service.EmailService;
import com.tng.portal.common.util.*;
import com.tng.portal.common.vo.rest.EmailParameterVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Created by Eric on 2018/9/12.
 */

@Service
@Transactional
public class DeployQueueServiceImpl implements DeployQueueService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DeployQueueRepository deployQueueRepository;
    @Autowired
    private DeploySyncMapResultRepository deploySyncMapResultRepository;
    
    @Autowired
    @Qualifier("httpClientUtils")
    private HttpClientUtils httpClientUtils;

    @Qualifier("commonEmailService")
    @Autowired
    private EmailService commonEmailService;

    @Autowired
    private EwpCallerService ewpCallerService;

    @Autowired
    private MpCallerService mpCallerService;
    
    @Autowired
    private SrvCallerService srvCallerService;
    
    @Autowired
    private TreCallerService treCallerService;

    @Value("${deploy.fail.notify.recipients}")
    private String notifyRecipients;

    @Autowired
    private JpaUtil jpaUtil;
    
    @Override
    public Long deploy(String deploydetail) {
        // analysis deployDetail A json Character string 
        Gson gson = new Gson();
        DeployDetailDto deployDetailDto = gson.fromJson(deploydetail, DeployDetailDto.class);
        Instance instance = Instance.valueOf(deployDetailDto.getDeployEnvir());
        Long deployRefId = deployDetailDto.getDeployRefId();
        String deployScheduleDate = deployDetailDto.getDeployScheduleDate();
        deployDetailDto.setDeployEnvir(null);
        deployDetailDto.setDeployRefId(null);
        deployDetailDto.setDeployScheduleDate(null);
        // Create the total deploy_queue Table database , And set data 
        DeployQueue deployQueue=new DeployQueue();
        deployQueue.setDeployVersionNo(GenerateUniqueIDUtil.generateDeployUniqueId());
        deployQueue.setCreateDate(new Date());
        deployQueue.setDeployFullData(gson.toJson(deployDetailDto));
        deployQueue.setDeployType(DeployType.PARTICIPANT);
        deployQueue.setDeployEnvir(instance);
        if( StringUtils.isNotBlank(deployScheduleDate)){
            Date date = DateUtils.parseDate(deployScheduleDate, DateCode.dateFormatMm);
            deployQueue.setScheduleDeployDate(date);
        }
        deployQueue.setStatus(DeployStatus.PENDING_FOR_DEPLOY);
        deployQueue.setDeployRefId(deployRefId);// Set up deployQueue A deploy_ref_id data 
        String geaParticipantRefId = null;   // take  geaParticipantRefId
        for (DeployDetailEntry deployDetail : deployDetailDto.getDeployDetail()) {
            List<DeployDetailEntry.Detail> details = deployDetail.getDetails();
            if(StringUtils.isBlank(geaParticipantRefId)){
            	geaParticipantRefId = details.stream().map(DeployDetailEntry.Detail::getGeaParticipantRefId).findFirst().orElse("");
                if(StringUtils.isNotBlank(geaParticipantRefId)) {
                    deployQueue.setGeaParticipantRefId(geaParticipantRefId);
                }
            }
        }
        deployQueueRepository.saveAndFlush(deployQueue);
        // Store the acquired data deploy_sync_map_result Table database 
        Map<String, String> mth = GeaModuleMappingUtil.getGEAModuleMap("participant", instance,"SYNC");
        for (Entry<String, String> entry : mth.entrySet()) {
            String serviceName=GeaServiceName.findBySortName(entry.getKey()).getValue();
            saveDeploySyncMapResult(deployQueue,serviceName,null,null,entry.getValue());
        }

        if(Objects.isNull(deployQueue.getScheduleDeployDate())) {
            // According to demand , Send to Gea
            sendToGEA(deployQueue);
        }

        return deployQueue.getId();
    }
    

	@Override
	public Long deployMp(String deploydetail) {
        // analysis deployDetail A json Character string 
        Gson gson = new Gson();
        DeployDetailDto deployDetailDto = gson.fromJson(deploydetail, DeployDetailDto.class);
        // The modified object , Re turn json object , And set up deployQueue Table FinalDeployData field 
        Instance deployEnvir = Instance.valueOf(deployDetailDto.getDeployEnvir());
        Long deployRefId = deployDetailDto.getDeployRefId();
        String deployScheduleDate = deployDetailDto.getDeployScheduleDate();
        
        deployDetailDto.setDeployEnvir(null);
        deployDetailDto.setDeployRefId(null);
        deployDetailDto.setDeployScheduleDate(null);
        deploydetail = gson.toJson(deployDetailDto);
        
        DeployType deployType = null;
        String serviceName = deployDetailDto.getDeployDetail().get(0).getServiceName();
        if("adjustment".equals(serviceName)){
        	deployType = DeployType.ADJUSTMENT;
        }else if("deposit".equals(serviceName)){
        	deployType = DeployType.DEPOSIT;
        }else if("cashout".equals(serviceName)){
        	deployType = DeployType.CASH_OUT;
        }
        // Create the total deploy_queue Table database , And set data 
        DeployQueue deployQueue=new DeployQueue();
        deployQueue.setDeployVersionNo(GenerateUniqueIDUtil.generateDeployUniqueId());
        deployQueue.setGeaParticipantRefId(deployDetailDto.getGeaParticipantRefId());
        deployQueue.setDeployFullData(deploydetail);
        if( StringUtils.isNotBlank(deployScheduleDate)){
        	Date date = DateUtils.parseDate(deployScheduleDate, DateCode.dateFormatMm);
        	deployQueue.setScheduleDeployDate(date);
        }
        deployQueue.setDeployEnvir(deployEnvir);
        deployQueue.setStatus(DeployStatus.PENDING_FOR_DEPLOY);
        deployQueue.setCreateDate(new Date());
        deployQueue.setDeployType(deployType);
        deployQueue.setDeployRefId(deployRefId);
        deployQueueRepository.save(deployQueue);

        // Store the acquired data into the sub deploy_sync_map_result Table database 
        for (DeployDetailEntry deployDetail : deployDetailDto.getDeployDetail()){
            Map<String, String> mapp = GeaModuleMappingUtil.getGEAModuleMap(deployDetail.getServiceName(), deployEnvir,"ADJUSTMENT");
            for (Map.Entry<String, String> entry : mapp.entrySet()) {
                saveDeploySyncMapResult(deployQueue, GeaServiceName.findBySortName(entry.getKey()).getValue(),null,deploydetail,entry.getValue());
            }
        }

        if(StringUtils.isBlank(deployScheduleDate)) {
             // According to demand , Send to Gea
             sendToGEA(deployQueue);
        }

        return deployQueue.getId();
	}
	
	@Override
	public Long deploySrv(String deploydetail) {
        // analysis deployDetail A json Character string 
        Gson gson = new Gson();
        DeployDetailDto deployDetailDto = gson.fromJson(deploydetail, DeployDetailDto.class);
        // The modified object , Re turn json object , And set up deployQueue Table FinalDeployData field 
        Instance deployEnvir = Instance.valueOf(deployDetailDto.getDeployEnvir());
        Long deployRefId = deployDetailDto.getDeployRefId();
        String deployScheduleDate = deployDetailDto.getDeployScheduleDate();
        
        deployDetailDto.setDeployEnvir(null);
        deployDetailDto.setDeployRefId(null);
        deployDetailDto.setDeployScheduleDate(null);
        deploydetail = gson.toJson(deployDetailDto);
        
        // Create the total deploy_queue Table database , And set data 
        DeployQueue deployQueue=new DeployQueue();
        deployQueue.setDeployVersionNo(GenerateUniqueIDUtil.generateDeployUniqueId());
        deployQueue.setGeaParticipantRefId(deployDetailDto.getGeaParticipantRefId());
        deployQueue.setDeployFullData(deploydetail);
        if( StringUtils.isNotBlank(deployScheduleDate)){
        	Date date = DateUtils.parseDate(deployScheduleDate, DateCode.dateFormatMm);
        	deployQueue.setScheduleDeployDate(date);
        }
        deployQueue.setDeployEnvir(deployEnvir);
        deployQueue.setStatus(DeployStatus.PENDING_FOR_DEPLOY);
        deployQueue.setCreateDate(new Date());
        deployQueue.setDeployType(DeployType.SERVICE_MARKUP);
        deployQueue.setDeployRefId(deployRefId);
        deployQueueRepository.save(deployQueue);

        // Store the acquired data into the sub deploy_sync_map_result Table database 
        for (DeployDetailEntry deployDetail : deployDetailDto.getDeployDetail()){
            Map<String, String> mapp = GeaModuleMappingUtil.getGEAModuleMap(deployDetail.getServiceName(), deployEnvir,"MARKUP");
            for (Map.Entry<String, String> entry : mapp.entrySet()) {
                saveDeploySyncMapResult(deployQueue, GeaServiceName.findBySortName(entry.getKey()).getValue(),null,deploydetail,entry.getValue());
            }
        }

        if(StringUtils.isBlank(deployScheduleDate)) {
             // According to demand , Send to Gea
             sendToGEA(deployQueue);
        }

        return deployQueue.getId();
	}
	
	@Override
	public Long deployTre(String deploydetail) {
		// analysis deployDetail A json Character string 
        Gson gson = new Gson();
        DeployDetailDto deployDetailDto = gson.fromJson(deploydetail, DeployDetailDto.class);
        // The modified object , Re turn json object , And set up deployQueue Table FinalDeployData field 
        Instance deployEnvir = Instance.valueOf(deployDetailDto.getDeployEnvir());
        Long deployRefId = deployDetailDto.getDeployRefId();
        String deployScheduleDate = deployDetailDto.getDeployScheduleDate();
        
        deployDetailDto.setDeployEnvir(null);
        deployDetailDto.setDeployRefId(null);
        deployDetailDto.setDeployScheduleDate(null);
        deploydetail = gson.toJson(deployDetailDto);
        
        // Create the total deploy_queue Table database , And set data 
        DeployQueue deployQueue=new DeployQueue();
        deployQueue.setDeployVersionNo(GenerateUniqueIDUtil.generateDeployUniqueId());
        deployQueue.setGeaParticipantRefId(deployDetailDto.getGeaParticipantRefId());
        deployQueue.setDeployFullData(deploydetail);
        if( StringUtils.isNotBlank(deployScheduleDate)){
        	Date date = DateUtils.parseDate(deployScheduleDate, DateCode.dateFormatMm);
        	deployQueue.setScheduleDeployDate(date);
        }
        deployQueue.setDeployEnvir(deployEnvir);
        deployQueue.setStatus(DeployStatus.PENDING_FOR_DEPLOY);
        deployQueue.setCreateDate(new Date());
        deployQueue.setDeployType(DeployType.EXCHANGE_RATE);
        deployQueue.setDeployRefId(deployRefId);
        deployQueueRepository.save(deployQueue);

        // Store the acquired data into the sub deploy_sync_map_result Table database 
        for (DeployDetailEntry deployDetail : deployDetailDto.getDeployDetail()){
            Map<String, String> mapp = GeaModuleMappingUtil.getGEAModuleMap(deployDetail.getServiceName(), deployEnvir,"RATE");
            for (Map.Entry<String, String> entry : mapp.entrySet()) {
                saveDeploySyncMapResult(deployQueue, GeaServiceName.findBySortName(entry.getKey()).getValue(),null,deploydetail,entry.getValue());
            }
        }

        if(StringUtils.isBlank(deployScheduleDate)) {
             // According to demand , Send to Gea
             sendToGEA(deployQueue);
        }

        return deployQueue.getId();
	}

    
    private void compareAndSetSyncType(DeployDetailDto apvData,DeployDetailDto geaData){
    	for(DeployDetailEntry apvEntry : apvData.getDeployDetail()){
    		for(DeployDetailEntry.Detail apvDetail :apvEntry.getDetails()){
    			if(geaData == null){
    				apvDetail.setSyncType(DeploySyncType.ADD.getValue());
    				continue;
    			}
    			DeployDetailEntry geaEntry = geaData.getEntryByServiceName(apvEntry.getServiceName());
    			if(geaEntry == null){
    				apvDetail.setSyncType(DeploySyncType.ADD.getValue());
    				continue;
    			}
    			// Whether or not to add  ->apv Existence gea Nonexistent data 
    			Map<String,String> field = apvDetail.getCompareField(apvEntry.getServiceName());
    			DeployDetailEntry.Detail geaDetail = geaEntry.findDetailByField(field);
    			if(geaDetail == null){
    				apvDetail.setSyncType(DeploySyncType.ADD.getValue());
    				continue;
    			}
    			// yes update  still none
    			if(apvDetail.compareGea(ServiceName.valueOf(apvEntry.getServiceName()), geaDetail)){
    				apvDetail.setSyncType(DeploySyncType.NONE.getValue());
    				continue;
    			}else{
    				apvDetail.setSyncType(DeploySyncType.UPDATE.getValue());
    				continue;
    			}
    		}
    	}
    	// Whether or not to delete  ->gea Existence apv Nonexistent data 
    	if(geaData != null){
    		for(DeployDetailEntry geaEntry : geaData.getDeployDetail()){
    			for(DeployDetailEntry.Detail geaDetail : geaEntry.getDetails()){
    				DeployDetailEntry apvEntry = apvData.getEntryByServiceName(geaEntry.getServiceName());
					Map<String,String> field = geaDetail.getCompareField(geaEntry.getServiceName());
					DeployDetailEntry.Detail apvDetail = apvEntry.findDetailByField(field);
					if(apvDetail == null){
	    				geaDetail.setSyncType(DeploySyncType.DELETE.getValue());
	    				apvEntry.addDetail(geaDetail);
	    				continue;
	    			}
    			}
    		}
    	}
    }
    
    private boolean compareExchangeRate(DeployDetailDto apvData,DeployDetailDto geaData){
    	DeployDetailEntry apvEntry = apvData.getDeployDetail().stream().filter(item -> item.getServiceName().equals(ServiceName.exchangeRate.getValue())).findFirst().orElse(null);
    	DeployDetailEntry geaEntry = geaData.getDeployDetail().stream().filter(item -> item.getServiceName().equals(ServiceName.exchangeRate.getValue())).findFirst().orElse(null);
    	if(apvEntry == null || geaEntry == null){
    		return false;
    	}
    	if(apvEntry.getDetails().size() != geaEntry.getDetails().size()){
    		return false;
    	}
    	for(DeployDetailEntry.Detail apvDetail : apvEntry.getDetails()){//sonarmodify
    		Optional<Detail> geaDetail = geaEntry.getDetails().stream().filter(item -> item.getCurrencyFrom().equals(apvDetail.getCurrencyFrom()) && item.getCurrencyTo().equals(apvDetail.getCurrencyTo())).findFirst();
    		if(geaDetail.isPresent()){
                if(!apvDetail.compareGea(ServiceName.exchangeRate, geaDetail.get())){
                    return false;
                }
            }else{
                return false;
            }
    	}
    	return true;
    }
    
    
    private void synchronDeployment(DeployQueue deployQueue) {
        try {
            if (DeployType.PARTICIPANT == deployQueue.getDeployType()) {
                ewpCallerService.callDeployment(deployQueue.getDeployRefId(), deployQueue.getStatus(),deployQueue.getUpdateDate(),deployQueue.getDeployVersionNo());
            } else if (DeployType.ADJUSTMENT==deployQueue.getDeployType() || DeployType.DEPOSIT==deployQueue.getDeployType() || DeployType.CASH_OUT==deployQueue.getDeployType()) {
                mpCallerService.callDeployment(deployQueue.getDeployRefId(), deployQueue.getStatus(),deployQueue.getUpdateDate(),deployQueue.getDeployVersionNo());
            } else if (DeployType.SERVICE_MARKUP==deployQueue.getDeployType()){
            	srvCallerService.callDeployment(deployQueue.getDeployRefId(), deployQueue.getStatus(),deployQueue.getUpdateDate(),deployQueue.getDeployVersionNo());
            } else if (DeployType.EXCHANGE_RATE==deployQueue.getDeployType()){
            	treCallerService.callDeployment(deployQueue.getDeployRefId(), deployQueue.getStatus(),deployQueue.getUpdateDate(),deployQueue.getDeployVersionNo());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);//sonar modify
        }
    }

    private void saveDeploySyncMapResult(DeployQueue deployQueue, String geaServerName,String geaCurrentData, String objectStr,  String apiUrl) {
        DeploySyncMapResult deploySyncMapResult=new DeploySyncMapResult();
        deploySyncMapResult.setDeployQueueId(deployQueue.getId());
        deploySyncMapResult.setGeaServerName(geaServerName);
        deploySyncMapResult.setCreateDate(new Date());
        deploySyncMapResult.setGeaCurrentData(geaCurrentData);
        deploySyncMapResult.setReqParameters(objectStr);
        deploySyncMapResult.setRetryCount(0);
        deploySyncMapResult.setApiUrl(apiUrl);
        deploySyncMapResultRepository.save(deploySyncMapResult);
    }

    /**
     * find By schedule_deploy_date
     */
    @Override
    public List<DeployQueue> findByScheduleDeployDate(Date scheduleDeployDate){
        List<DeployQueue> scheduleList = deployQueueRepository.findByScheduleDeployDate(new Date(), DeployStatus.PENDING_FOR_DEPLOY);
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.MINUTE, minute - 2);
        Date timestamp = new Date(calendar.getTimeInMillis());
        List<DeployQueue> nowList = deployQueueRepository.findByCreateDateLessThanAndStatusAndScheduleDeployDateIsNullOrderByCreateDateAsc(timestamp, DeployStatus.PENDING_FOR_DEPLOY);
        scheduleList.addAll(nowList);
        return scheduleList;
    }


    /**
     *  Send to gea
     */
    @Override
    public Boolean sendToGEA(DeployQueue dq) {
    	jpaUtil.refresh(dq);
    	String logPre = "dqid:"+dq.getId();
        if(dq.getStatus() != DeployStatus.PENDING_FOR_DEPLOY){
        	logger.error("{} The state is as follows: {}  Do not deal with" ,logPre, dq.getStatus() );
            return false;
        }
        if(dq.getDeployType()==DeployType.PARTICIPANT){
        	this.updateFailureData(dq);// Cancel invalid task 
        }
        Boolean synchronStatus=true;
    	List<DeploySyncMapResult> mapResultList = deploySyncMapResultRepository.findByDeployQueueId(dq.getId());
    	logger.error("{} Common requests: {}" , logPre, mapResultList.size());
    	for(DeploySyncMapResult mapResult : mapResultList){
    		logger.info("{} Is being processed: {}", logPre, mapResult.getId());
    		if(MapServerAckStatus.OK == mapResult.getServerAckStatus() && MapDeployStatus.SUC == mapResult.getDeployStatus()){  // Successful communication and business success 
    			logger.error("{}  {}  Has been sent successfully, no longer processed. ", logPre, mapResult.getId());
    			continue;
    		}
    		if(/*MapServerAckStatus.FAIL == mapResult.getServerAckStatus() &&*/ mapResult.getRetryCount()>=GeaModuleMappingUtil.getMaxRetryCount()){
    			logger.error("{}  {}  The maximum number of retry has been reached, marked as failure. ", logPre ,mapResult.getId());
                sendEmail(dq.getDeployVersionNo(),dq.getGeaParticipantRefId(),dq.getDeployEnvir(), mapResult.getGeaServerName());
    			dq.setStatus(DeployStatus.FAIL);
                dq.setUpdateDate(new Date());
    			deployQueueRepository.save(dq);
    			synchronStatus=false;
    			break;
    		}
    		// Start the request GEA
    		try{
    			dq.setUpdateDate(new Date());
    			mapResult.setRetryCount(mapResult.getRetryCount()+1);
    			if(dq.getDeployType()==DeployType.PARTICIPANT){
	        		// request GEA take gea_current_data
    	        	String url = GeaModuleMappingUtil.getApiUrl(GeaServiceName.findByValue(mapResult.getGeaServerName()).getSortName(),dq.getDeployEnvir(),"GET");
	        		Map<String, String> param = new HashMap<>();
	        		param.put("geaParticipantRefId", dq.getGeaParticipantRefId());
	        		param.put("api_key", GeaModuleMappingUtil.getApiKey());
	        		String restfulResponse = httpClientUtils.httpGet(url, String.class, param);
	        		mapResult.setGeaCurrentData(restfulResponse);
	        		GeaBaseResponse response = new Gson().fromJson(restfulResponse, new TypeToken<GeaBaseResponse>() {}.getType());
	        		if(!"success".equals(response.getStatus())){
	        			throw new Exception("查询gea数据时出错:"+ restfulResponse);
	        		}
	        		DeployDetailDto geaCurrentData = response.getParticipantEntry();
	        	    // Contrast differences, mark to  deployDetailDto
	        		DeployDetailDto deployDetailDto = new Gson().fromJson(dq.getDeployFullData(), DeployDetailDto.class);
	        		Iterator<DeployDetailEntry> entryIt = deployDetailDto.getDeployDetail().iterator();
	        		List<ServiceName> snList = GeaModuleMappingUtil.getGEAServerMap(mapResult.getGeaServerName());
	        		while (entryIt.hasNext()) {
	        			DeployDetailEntry e = entryIt.next();
	        			if(!snList.stream().anyMatch(item -> item.getValue().equals(e.getServiceName()))){
	        				entryIt.remove();
	        			}
					}
	        		this.compareAndSetSyncType(deployDetailDto, geaCurrentData);
	        		// Store in database 
	        		mapResult.setReqParameters(new Gson().toJson(deployDetailDto));
	        		deploySyncMapResultRepository.saveAndFlush(mapResult);
    	        }else if(dq.getDeployType()==DeployType.EXCHANGE_RATE){
    	        	String url = GeaModuleMappingUtil.getApiUrl(GeaServiceName.findByValue(mapResult.getGeaServerName()).getSortName(),dq.getDeployEnvir(),"GETRATE");
    				Map<String, String> p = new HashMap<>();
            		p.put("api_key", GeaModuleMappingUtil.getApiKey());
    				String restfulResponse = httpClientUtils.httpGet(url, String.class,p);
    				GeaBaseResponse response = new Gson().fromJson(restfulResponse, new TypeToken<GeaBaseResponse>() {}.getType());
	        		DeployDetailEntry geaEntry = response.getParticipantEntry().getDeployDetail().get(0);
	        		DeployDetailEntry reqEntry = new Gson().fromJson(mapResult.getReqParameters(), DeployDetailDto.class).getDeployDetail().get(0);
	        		for(Detail getDetail : geaEntry.getDetails()){
	        			if(!reqEntry.getDetails().stream().anyMatch(i->i.getCurrencyFrom().equals(getDetail.getCurrencyFrom())&&i.getCurrencyTo().equals(getDetail.getCurrencyTo()))){
	        				throw new Exception("tre数据比gea当前的数据少:"+ restfulResponse);
	        			}
	        		}
    	        }
    			String reqParameters = mapResult.getReqParameters();
                DeployDetailDto deployDetailDto = new Gson().fromJson(reqParameters, DeployDetailDto.class);
	    		String resStr =  httpClientUtils.postSendJson(mapResult.getApiUrl()+"?api_key="+GeaModuleMappingUtil.getApiKey(), String.class, deployDetailDto);
	    		mapResult.setServerAckStatus(MapServerAckStatus.OK);

                JsonParser parser = new JsonParser();
	    		JsonElement resEle = parser.parse(resStr);
	    		JsonObject resJson = resEle.getAsJsonObject();
	    		mapResult.setRespCode(resJson.get("errorCode")==null?"":resJson.get("errorCode").getAsString()); 
	    		mapResult.setRespCodeDesc(resStr);
	    		if(!"success".equals(resJson.get("status").getAsString())){   // Business failure 
	    			mapResult.setDeployStatus(MapDeployStatus.FAIL);
	    			deploySyncMapResultRepository.save(mapResult);
	    			dq.setStatus(DeployStatus.FAIL);
	    			deployQueueRepository.save(dq);
                    synchronStatus=false;
	    		}else{
	    			mapResult.setDeployStatus(MapDeployStatus.SUC);
	    			deploySyncMapResultRepository.save(mapResult);
	    			
	    			if(dq.getDeployType()==DeployType.EXCHANGE_RATE){
	    				// 二次检查
	    				String url = GeaModuleMappingUtil.getApiUrl(GeaServiceName.findByValue(mapResult.getGeaServerName()).getSortName(),dq.getDeployEnvir(),"GETRATE");
	    				Map<String, String> p = new HashMap<>();
	            		p.put("api_key", GeaModuleMappingUtil.getApiKey());
	    				String restfulResponse = httpClientUtils.httpGet(url, String.class,p);
	    				GeaBaseResponse response = new Gson().fromJson(restfulResponse, new TypeToken<GeaBaseResponse>() {}.getType());
		        		DeployDetailDto geaCurrentData = response.getParticipantEntry();
		        		DeployDetailDto reqData = new Gson().fromJson(reqParameters, DeployDetailDto.class);
		        		logger.error("restfulResponse" + restfulResponse);
		        		if(!this.compareExchangeRate(reqData, geaCurrentData)){
		        			mapResult.setDeployStatus(MapDeployStatus.FAIL);
			    			deploySyncMapResultRepository.save(mapResult);
			    			synchronStatus = false;
		        		}
	    			}
	    		}

    		}catch(RestClientException e){
    			synchronStatus=false;
    			logger.error("{}  {}  synchronization GEA Failure! ", logPre ,mapResult.getId(), e);
    			mapResult.setServerAckStatus(MapServerAckStatus.FAIL);
    			deploySyncMapResultRepository.save(mapResult);
    			continue;
    		}catch (Exception e) {
    			synchronStatus=false;
    			logger.error("{}  {}  synchronization GEA Failure! ", logPre ,mapResult.getId() , e);
    			mapResult.setServerAckStatus(MapServerAckStatus.FAIL);
    			deploySyncMapResultRepository.save(mapResult);
    			dq.setStatus(DeployStatus.FAIL);
                deployQueueRepository.save(dq);
			}

    	}
        if(synchronStatus){
            dq.setStatus(DeployStatus.DEPLOYED);
            deployQueueRepository.save(dq);
        }
        
        synchronDeployment(dq); // Synchronous corresponding module deployment
        return synchronStatus;
    }

    /**
     * CANCEL  fall deploy Time in  dq And then create time. dq Previous  DeployQueue
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateFailureData(DeployQueue dq){
    	Date now = new Date();
        List<DeployQueue> list = deployQueueRepository.findByCreateDate(dq.getCreateDate(), DeployStatus.PENDING_FOR_DEPLOY, DeployType.PARTICIPANT, dq.getGeaParticipantRefId(), dq.getId(), dq.getDeployEnvir());
        list.stream().forEach(item->{
            item.setStatus(DeployStatus.CANCEL);
            item.setUpdateDate(now);
            synchronDeployment(item); // Synchronous corresponding module deployment
        });
        deployQueueRepository.save(list);
    }

    private void sendEmail(String deployVersionNo,String geaParticipantRefId,Instance instance, String geaServerName){
        EmailParameterVo emailParameterVo = new EmailParameterVo();
        emailParameterVo.setSender(PropertiesUtil.getMailValueByKey("email.sender.account"));
        emailParameterVo.setReceivers(notifyRecipients);
        emailParameterVo.setSubject("Synchronization Failure");
        emailParameterVo.setMessage("["+deployVersionNo+"]["+instance.getValue()+"]["+geaParticipantRefId+"]:  Failed to synchronize information to GEA ["+geaServerName+"]");
        try {
            commonEmailService.sendByHttp(emailParameterVo,null);
        }catch (Exception e){
            logger.error("Synchronization Failure Email", e);
        }
    }


    /**
     *  Is it already Deploy
     */
	@Override
	public Boolean isDeploy(String geaParticipantRefId, Instance deployEnvir) {
		List<DeployQueue> list = deployQueueRepository.findByGeaParticipantRefId(geaParticipantRefId, DeployType.PARTICIPANT, deployEnvir);
		for(DeployQueue item : list){
			if(DeployStatus.DEPLOYED == item.getStatus()){
				return true;
			}
		}
		return false;
	}

    @Override
    public String getDeploymentDate(String geaParticipantRefId, Instance deployEnvir) {
        final String[] deploymentDate = {""};
        List<DeployQueue> deployQueues = deployQueueRepository.findByGeaParticipantRefIdAndDeployEnvir(geaParticipantRefId, deployEnvir);
        deployQueues.stream().findFirst().ifPresent(item -> {
            deploymentDate[0] =item.getScheduleDeployDate()==null ? "" :item.getScheduleDeployDate().toString();
        });
        return deploymentDate[0];
    }

    @Override
    public String getDeploymentStatus(String geaParticipantRefId, Instance deployEnvir) {
        final String[] deploymentStatus = {""};
        List<DeployQueue> deployQueues = deployQueueRepository.findByGeaParticipantRefIdAndDeployEnvir(geaParticipantRefId, deployEnvir);
        deployQueues.stream().findFirst().ifPresent(item -> {
            deploymentStatus[0] =item.getStatus()==null ? "" : item.getStatus().toString();
        });
        return deploymentStatus[0];
    }

    @Override
    public Map<Long,Map<String, String>> getDeploymentInfo(List<Long> ids) {
        List<DeployQueue> deployQueueList = deployQueueRepository.findAll(ids);
        Map<Long,Map<String, String>> res = new HashMap<>();
        for(DeployQueue deployQueue : deployQueueList){
        	Map<String, String> map = new HashMap<>();
        	map.put("deploymentStatus", deployQueue.getStatus().getValue());
        	if(Objects.nonNull(deployQueue.getScheduleDeployDate())){
        		map.put("scheduleDeployDate", DateUtils.formatDate(new Date(deployQueue.getScheduleDeployDate().getTime()), DateCode.dateFormatMm));
        	}
        	if(deployQueue.getStatus()==DeployStatus.DEPLOYED && Objects.nonNull(deployQueue.getUpdateDate())) {
        		map.put("updateDate", DateUtils.formatDate(new Date(deployQueue.getUpdateDate().getTime()), DateCode.dateFormatMm));
        	}
        	res.put(deployQueue.getId(), map);
        }
        return res;
    }


/*    @Override
    public List<Map<String, String>> getDeploymentInfoList(Instance deployEnvir, String deploymentStatus, String deploymentDate) {
        Specifications<DeployQueue> where = Specifications.where((root, query, builder) -> builder.equal(root.get("deployEnvir"), deployEnvir));
        if(StringUtils.isNotBlank(deploymentStatus)){
            where = where.and((root, query, builder) -> builder.equal(root.get("status"), DeployStatus.valueOf(deploymentStatus)));
        }
        if(StringUtils.isNotBlank(deploymentDate)){
            where = where.and((root, query, builder) -> builder.like(root.get("updateDate").as(String.class), "%" + deploymentDate + "%"));
        }
        List<DeployQueue> deployQueues = deployQueueRepository.findAll(where);

        List<Map<String, String>> list = deployQueues.stream().map(item -> {
            Map<String, String> map = new HashMap<>();
            map.put("geaParticipantRefId", item.getGeaParticipantRefId());
            map.put("deploymentStatus", null == item.getStatus() ? null : item.getStatus().getValue());
            map.put("deployRefId", String.valueOf(item.getId()));
            if (Objects.nonNull(item.getScheduleDeployDate())) {
                map.put("scheduleDeployDate", DateUtils.formatDate(new Date(item.getScheduleDeployDate().getTime()), DateCode.dateFormatMm));
            } 
            if (Objects.nonNull(item.getUpdateDate())) {
                map.put("updateDate", DateUtils.formatDate(new Date(item.getUpdateDate().getTime()), DateCode.dateFormatMm));
            }
            
            return map;
        }).collect(Collectors.toList());
        return list;
    }*/

	@Override
	public Integer hasPending(DeployType deployType, Instance deployEnvir) {
		List<DeployQueue> list = deployQueueRepository.findPending(deployType, deployEnvir);
		return list.size();
	}
}
