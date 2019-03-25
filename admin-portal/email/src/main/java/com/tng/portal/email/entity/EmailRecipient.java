package com.tng.portal.email.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Owen on 2017/10/17.
 */
@Entity
@Table(name = "EMAIL_RECIPIENT")
public class EmailRecipient {
    @Id
    @Column(name = "id")
//    @GeneratedValue(generator="seq_email_recipient",strategy= GenerationType.SEQUENCE)
//    @SequenceGenerator(name="seq_email_recipient",sequenceName="seq_email_recipient", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
//    email_content_id
    @Column(name = "recipient_email")
    private String recipientEmail;
    @Column(name = "send_type")
    private String sendType;
    @Column(name = "server_response_code")
    private String serverResponseCode;
    @Column(name = "server_response_msg")
    private String serverResponseMsg;
    @Column(name = "server_response_timestamp")
    private Date serverResponseTimestamp;
    @Column(name = "resend_count")
    private int resendCount;
    @Column(name = "prev_attempt_sent")
    private Date prevAttemptSent;
    @Column(name = "sent_time")
    private Date sentTime;
    @Column(name = "status")
    private String status;
    @Column(name = "status_chg_time")
    private Date statusChgTime;
    @Column(name = "update_date")
    private Date updateDate;
    @Version
    @Column(name = "optlock_ver")
    private long optlock_ver;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "email_content_id")
    private EmailContent emailContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getServerResponseCode() {
        return serverResponseCode;
    }

    public void setServerResponseCode(String serverResponseCode) {
        this.serverResponseCode = serverResponseCode;
    }

    public String getServerResponseMsg() {
        return serverResponseMsg;
    }

    public void setServerResponseMsg(String serverResponseMsg) {
        this.serverResponseMsg = serverResponseMsg;
    }

    public Date getServerResponseTimestamp() {
        return serverResponseTimestamp;
    }

    public void setServerResponseTimestamp(Date serverResponseTimestamp) {
        this.serverResponseTimestamp = serverResponseTimestamp;
    }

    public int getResendCount() {
        return resendCount;
    }

    public void setResendCount(int resendCount) {
        this.resendCount = resendCount;
    }

    public Date getPrevAttemptSent() {
        return prevAttemptSent;
    }

    public void setPrevAttemptSent(Date prevAttemptSent) {
        this.prevAttemptSent = prevAttemptSent;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStatusChgTime() {
        return statusChgTime;
    }

    public void setStatusChgTime(Date statusChgTime) {
        this.statusChgTime = statusChgTime;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public EmailContent getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(EmailContent emailContent) {
        this.emailContent = emailContent;
    }

    public long getOptlock_ver() {
        return optlock_ver;
    }

    public void setOptlock_ver(long optlock_ver) {
        this.optlock_ver = optlock_ver;
    }
}
