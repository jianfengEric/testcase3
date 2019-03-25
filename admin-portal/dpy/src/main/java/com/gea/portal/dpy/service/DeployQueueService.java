package com.gea.portal.dpy.service;

import com.gea.portal.dpy.entity.DeployQueue;
import com.tng.portal.common.enumeration.DeployType;
import com.tng.portal.common.enumeration.Instance;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Eric on 2018/9/12.
 */
public interface DeployQueueService {
	
    public Long deploy(String deploydetail);
    
    public Long deployMp(String deploydetail);
    
    public Long deploySrv(String deploydetail);
    
    public Long deployTre(String deploydetail);
    
    /**
     * find By schedule_deploy_date
     */
    public List<DeployQueue> findByScheduleDeployDate(Date scheduleDeployDate);
    
    /**
     *  Send to time gea
     */
    public Boolean sendToGEA(DeployQueue dq);
    
    /**
     *  Is it already Deploy
     */
    public Boolean isDeploy(String geaParticipantRefId,Instance instance);
    

    public String getDeploymentDate(String geaParticipantRefId, Instance deployEnvir);

    public String getDeploymentStatus(String geaParticipantRefId, Instance deployEnvir);

    Map<Long,Map<String, String>> getDeploymentInfo(List<Long> ids);

    public Integer hasPending(DeployType deployType,Instance deployEnvir);
}
