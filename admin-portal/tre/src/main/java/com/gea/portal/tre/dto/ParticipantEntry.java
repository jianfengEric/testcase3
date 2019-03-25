package com.gea.portal.tre.dto;

import java.io.Serializable;
import java.util.List;

public class ParticipantEntry implements Serializable {

    private List<DeployDetail> deployDetail;

    public List<DeployDetail> getDeployDetail() {
        return deployDetail;
    }

    public void setDeployDetail(List<DeployDetail> deployDetail) {
        this.deployDetail = deployDetail;
    }
}
