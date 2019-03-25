package com.tng.portal.email.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Owen on 2017/10/17.
 */
@Entity
@Table(name = "EMAIL_CONTENT")
public class EmailContent {
    @Id
    @Column(name = "id")
//    @GeneratedValue(generator="seq_email_content",strategy= GenerationType.SEQUENCE)
//    @SequenceGenerator(name="seq_email_content",sequenceName="seq_email_content", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(name = "sender_email")
    private String senderEmail;
    @Column(name = "subject")
    private String subject;
    @Column(name = "schedule_send_time")
    private Date scheduleSendTime;
    @Column(name = "content")
    private String content;
    @Column(name = "status")
    private String status;
    @Column(name = "status_chg_time")
    private Date statusChgTime;
    @Column(name = "application_code")
    private String applicationCode;
    @Column(name = "creator_acc_id")
     private String  creatorAccId;
    @Column(name = "create_date")
    private Date createDate;
    @Version
    @Column(name = "optlock_ver")
    private long optlock_ver;

    @OneToMany(mappedBy = "emailContent", fetch = FetchType.LAZY)
    private List<EmailAttachment> emailAttachments;

    @OneToMany(mappedBy = "emailContent", fetch = FetchType.LAZY)
    private List<EmailRecipient> emailRecipients;

    @Column(name = "CONTENT_TYPE")
    private String contentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getScheduleSendTime() {
        return scheduleSendTime;
    }

    public void setScheduleSendTime(Date scheduleSendTime) {
        this.scheduleSendTime = scheduleSendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getCreatorAccId() {
        return creatorAccId;
    }

    public void setCreatorAccId(String creatorAccId) {
        this.creatorAccId = creatorAccId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<EmailAttachment> getEmailAttachments() {
        return emailAttachments;
    }

    public void setEmailAttachments(List<EmailAttachment> emailAttachments) {
        this.emailAttachments = emailAttachments;
    }

    public List<EmailRecipient> getEmailRecipients() {
        return emailRecipients;
    }

    public void setEmailRecipients(List<EmailRecipient> emailRecipients) {
        this.emailRecipients = emailRecipients;
    }

    public long getOptlock_ver() {
        return optlock_ver;
    }

    public void setOptlock_ver(long optlock_ver) {
        this.optlock_ver = optlock_ver;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
