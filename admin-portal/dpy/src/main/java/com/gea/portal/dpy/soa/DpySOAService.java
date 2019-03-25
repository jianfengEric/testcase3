package com.gea.portal.dpy.soa;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.gea.portal.dpy.service.DeployQueueService;
import com.tng.portal.common.enumeration.DeployType;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.soa.AbstractMQBasicService;
import com.tng.portal.common.soa.AbstractSOABasicService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;

@Service("dpySOAService")
public class DpySOAService extends AbstractMQBasicService {

    private transient Logger log = LoggerFactory.getLogger(AbstractSOABasicService.class);


    @Autowired
    private DeployQueueService deployQueueService;

    @PostConstruct
    public void initConnectToMQ(){
        if(ApplicationContext.Env.integrated.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))){
            log.info("DpySOAService connectToMQ start!!");
            startListening();
            log.info("DpySOAService connectToMQ end!!");
        }else{
            log.info("DpySOAService will not run!!");
        }

    }
    @Override
    public String getServiceName() {
        return MessageFormat.format(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceNameTemplate), PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName));
    }

    @Override
    public Object getHandleInstance() {
        return this;
    }

    public RestfulResponse<String> deployPt(String deploydetail) {
    	log.error("starttime:"+new Date());
    	Long res = deployQueueService.deploy(deploydetail);
    	log.error("starttime:"+new Date());
        return RestfulResponse.ofData(res.toString());
    }

    public RestfulResponse<String> deployMp(String deploydetail) {
    	Long res = deployQueueService.deployMp(deploydetail);
    	return RestfulResponse.ofData(res.toString());
    }
    
    public RestfulResponse<String> deploySrv(String deploydetail) {
    	Long res = deployQueueService.deploySrv(deploydetail);
    	return RestfulResponse.ofData(res.toString());
    }
    
    public RestfulResponse<String> deployTre(String deploydetail) {
    	Long res = deployQueueService.deployTre(deploydetail);
    	return RestfulResponse.ofData(res.toString());
    }

    public RestfulResponse<Boolean> isDeploy(String geaParticipantRefId,Instance deployEnvir) {
        Boolean res = deployQueueService.isDeploy(geaParticipantRefId, deployEnvir);
        return RestfulResponse.ofData(res);
    }

    public RestfulResponse<Map<Long, Map<String, String>>> getDeploymentInfo(List<Object> ids) {
    	List<Long> id = ids.stream().map(item-> ((Double)item).longValue()).collect(Collectors.toList());
    	Map<Long, Map<String, String>> info = deployQueueService.getDeploymentInfo(id);
        return RestfulResponse.ofData(info);
    }
    
    public RestfulResponse<Integer> hasPending(@RequestParam DeployType deployType,@RequestParam Instance deployEnvir){
    	Integer res = deployQueueService.hasPending(deployType, deployEnvir);
        return RestfulResponse.ofData(res);
    }

}
