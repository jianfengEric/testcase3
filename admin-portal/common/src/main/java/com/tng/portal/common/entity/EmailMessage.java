package com.tng.portal.common.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Owen on 2016/12/1.
 */
@Entity
@Table(name = "email_message")
public class EmailMessage {

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    @GeneratedValue(generator="SEQ_EMAIL_MESSAGE_ID",strategy= GenerationType.SEQUENCE)
//    @SequenceGenerator(name="SEQ_EMAIL_MESSAGE_ID",sequenceName="SEQ_EMAIL_MESSAGE_ID", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job")
    private String job;

    @Column(name = "subject")
    private String subject;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receivers")
    private String receivers;

    @Column(name = "message")
    private String message;

    @Column(name = "send_time")
    private Date sendTime;

    @Column(name = "status")
    private Integer status;
    
    @Column(name = "SENDER_ID")
    private String senderId;

    @Column(name = "RECEIVERS_ID")
    private String receiversId;

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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiversId() {
		return receiversId;
	}

	public void setReceiversId(String receiversId) {
		this.receiversId = receiversId;
	}
}
