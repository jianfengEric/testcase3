package com.tng.portal.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Owen on 2016/11/17.
 */
@Entity
@Table(name = "EMAIL_TEMPLATE")
public class EmailTemplate {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "job")
    private String job;

    @Column(name = "subject")
    private String subject;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receivers")
    private String receivers;

    @Column(name = "email_template")
    private String emailTemplate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public String getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(String emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
