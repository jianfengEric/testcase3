package com.gea.portal.dpy.controller;

import com.gea.portal.dpy.entity.DeployQueue;
import com.gea.portal.dpy.service.DeployQueueService;
import com.tng.portal.common.enumeration.DeployType;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Eric on 2018/9/12.
 */

@RestController
@RequestMapping("remote/v1")
public class RemoteController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private DeployQueueService deployQueueService;

    @PostMapping("deploy-pt")
    @ResponseBody
    public RestfulResponse<String> deployPt(@ApiParam(value="deploy detail")@RequestBody(required = false)String deploydetail){
    	Long res = deployQueueService.deploy(deploydetail);
        return RestfulResponse.ofData(res.toString());
    }
    
    @PostMapping("deploy-mp")
    @ResponseBody
    public RestfulResponse<String> deployMp(@ApiParam(value="deploy detail")@RequestBody(required = false)String deploydetail){
    	Long res = deployQueueService.deployMp(deploydetail);
        return RestfulResponse.ofData(res.toString());
    }
    
    @PostMapping("deploy-srv")
    @ResponseBody
    public RestfulResponse<String> deploySrv(@ApiParam(value="deploy detail")@RequestBody(required = false)String deploydetail){
    	Long res = deployQueueService.deploySrv(deploydetail);
    	return RestfulResponse.ofData(res.toString());
    }
    
    @PostMapping("deploy-tre")
    @ResponseBody
    public RestfulResponse<String> deployTre(@ApiParam(value="deploy detail")@RequestBody(required = false)String deploydetail){
    	Long res = deployQueueService.deployTre(deploydetail);
    	return RestfulResponse.ofData(res.toString());
    }

    @GetMapping("deploy-task")
    @ResponseBody
    public RestfulResponse<String> deployTask(){
        List<DeployQueue> dqList = deployQueueService.findByScheduleDeployDate(new Timestamp(new Date().getTime()));
        logger.error(" Current timing task processing deploy number : {}", dqList.size());
        for(DeployQueue dq : dqList){
            deployQueueService.sendToGEA(dq);
        }
        return RestfulResponse.nullData();
    }
    
    @GetMapping("is-deploy")
    @ResponseBody
    public RestfulResponse<Boolean> isDeploy(@RequestParam String geaParticipantRefId,@RequestParam Instance deployEnvir){
        Boolean res = deployQueueService.isDeploy(geaParticipantRefId, deployEnvir);
        return RestfulResponse.ofData(res);
    }

    @GetMapping("get-deployment-date")
    @ResponseBody
    public RestfulResponse<String> getDeploymentDate(@RequestParam String geaParticipantRefId, @RequestParam Instance deployEnvir){
        String name = deployQueueService.getDeploymentDate(geaParticipantRefId, deployEnvir);
        return RestfulResponse.ofData(name);
    }

    @GetMapping("get-deployment-status")
    @ResponseBody
    public RestfulResponse<String> getDeploymentStatus(@RequestParam String geaParticipantRefId, @RequestParam Instance deployEnvir){
        String name = deployQueueService.getDeploymentStatus(geaParticipantRefId, deployEnvir);
        return RestfulResponse.ofData(name);
    }

    @GetMapping("get-deployment-info")
    @ResponseBody
    public RestfulResponse<Map<Long, Map<String, String>>> getDeploymentInfo(@RequestParam List<Long> ids){
        Map<Long, Map<String, String>> info = deployQueueService.getDeploymentInfo(ids);
        return RestfulResponse.ofData(info);
    }

    @GetMapping("has-pending")
    @ResponseBody
    public RestfulResponse<Integer> hasPending(@RequestParam DeployType deployType,@RequestParam Instance deployEnvir){
    	Integer res = deployQueueService.hasPending(deployType, deployEnvir);
        return RestfulResponse.ofData(res);
    }

}
