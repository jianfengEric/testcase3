package com.gea.portal.mp.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Entity
@Table(name = "ewp_record_env_map")
public class EwpRecordEnvMap {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "gea_participant_ref_id")
    private String geaParticipantRefId;

    @OneToOne
    @JoinColumn(name = "prod_req_apv_id")
    private RequestApproval prodRequestApproval;

    @OneToOne
    @JoinColumn(name = "preprod_req_apv_id")
    private RequestApproval preprodRequestApproval;

    @OneToOne
    @JoinColumn(name = "deploy_to_prod_id")
    private EwpPoolDeployment ewpPoolDeployment;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGeaParticipantRefId() {
        return geaParticipantRefId;
    }

    public void setGeaParticipantRefId(String geaParticipantRefId) {
        this.geaParticipantRefId = geaParticipantRefId;
    }

    public RequestApproval getProdRequestApproval() {
        return prodRequestApproval;
    }

    public void setProdRequestApproval(RequestApproval prodRequestApproval) {
        this.prodRequestApproval = prodRequestApproval;
    }

    public RequestApproval getPreprodRequestApproval() {
        return preprodRequestApproval;
    }

    public void setPreprodRequestApproval(RequestApproval preprodRequestApproval) {
        this.preprodRequestApproval = preprodRequestApproval;
    }

    public EwpPoolDeployment getEwpDeployToProd() {
        return ewpPoolDeployment;
    }

    public void setEwpDeployToProd(EwpPoolDeployment ewpPoolDeployment) {
        this.ewpPoolDeployment = ewpPoolDeployment;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
