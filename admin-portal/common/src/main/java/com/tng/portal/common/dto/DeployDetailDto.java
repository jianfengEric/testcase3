package com.tng.portal.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owen on 2018/9/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeployDetailDto implements Serializable {
    private Long deployRefId;
    private String deployEnvir;
    private String deployScheduleDate;
    private String geaParticipantRefId;
    private List<DeployDetailEntry> deployDetail;
    
    public DeployDetailEntry getEntryByServiceName(String serviceName){
    	for(DeployDetailEntry entry : this.getDeployDetail()){
    		if(entry.getServiceName().equals(serviceName)){
    			return entry;
    		}
    	}
    	return null;
    }

    public Long getDeployRefId() {
        return deployRefId;
    }

    public void setDeployRefId(Long deployRefId) {
        this.deployRefId = deployRefId;
    }

    public DeployDetailDto() {
    }

    public DeployDetailDto(String deployEnvir, String deployScheduleDate) {
        this.deployEnvir = deployEnvir;
        this.deployScheduleDate = deployScheduleDate;
        this.deployDetail = new ArrayList<>();
    }

    public void addDeployDetailEntry(DeployDetailEntry deployDetailEntry){
        this.getDeployDetail().add(deployDetailEntry);
    }

    public List<DeployDetailEntry> getDeployDetail() {
        return deployDetail;
    }

    public void setDeployDetail(List<DeployDetailEntry> deployDetail) {
        this.deployDetail = deployDetail;
    }

    public String getDeployEnvir() {
        return deployEnvir;
    }

    public void setDeployEnvir(String deployEnvir) {
        this.deployEnvir = deployEnvir;
    }

    public String getDeployScheduleDate() {
        return deployScheduleDate;
    }

    public void setDeployScheduleDate(String deployScheduleDate) {
        this.deployScheduleDate = deployScheduleDate;
    }

    @Override
    public String toString() {
        return "DeployDetailDto{" +
                "deployDetail=" + deployDetail +
                '}';
    }

	public String getGeaParticipantRefId() {
		return geaParticipantRefId;
	}

	public void setGeaParticipantRefId(String geaParticipantRefId) {
		this.geaParticipantRefId = geaParticipantRefId;
	}

}
