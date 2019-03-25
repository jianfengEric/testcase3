package com.tng.portal.sms.server.entity;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by Jimmy on 2018/7/19.
 */
//@Entity
public class Sms {

    @Id
    private Long id;
    @Column(name = "reference_id")
    private String jobReferenceId;
    @Column(name = "sender_account_id")
    private String senderAccountId;
    @Column(name = "create_date")
    private String createDate;
    @Column(name = "mobile_number")
    private String mobileNumber;
    @Column(name = "status")
    private String status;
    @Column(name = "sms_provider_id")
    private String providerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobReferenceId() {
        return jobReferenceId;
    }

    public void setJobReferenceId(String jobReferenceId) {
        this.jobReferenceId = jobReferenceId;
    }

    public String getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(String senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    @Override
    public String toString() {
        return "Sms{" +
                "id=" + id +
                ", jobReferenceId='" + jobReferenceId + '\'' +
                ", senderAccountId='" + senderAccountId + '\'' +
                ", createDate='" + createDate + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", status='" + status + '\'' +
                ", providerId='" + providerId + '\'' +
                '}';
    }
}
