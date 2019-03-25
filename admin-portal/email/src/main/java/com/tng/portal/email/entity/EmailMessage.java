package com.tng.portal.email.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Owen on 2016/12/1.
 */
@Entity
@Table(name = "email_message")
public class EmailMessage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    @GeneratedValue(generator="SEQ_EMAIL_MESSAGE_ID",strategy= GenerationType.SEQUENCE)
//    @SequenceGenerator(name="SEQ_EMAIL_MESSAGE_ID",sequenceName="SEQ_EMAIL_MESSAGE_ID", allocationSize = 1)
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
    private LocalDateTime sendTime;

    @Column(name = "status")
    private int status;


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

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
