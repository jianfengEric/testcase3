package com.tng.portal.ana.vo;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Created by Zero on 2016/11/30.
 */
public class SendEmailDto implements Serializable{
    private String job;
    private String subject;
    private String sender;
    private String receivers;
    private String message;
    private LinkedHashMap<String,String> templateInput;

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

    public LinkedHashMap<String, String> getTemplateInput() {
        return templateInput;
    }

    public void setTemplateInput(LinkedHashMap<String, String> templateInput) {
        this.templateInput = templateInput;
    }
}
