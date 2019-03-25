package com.tng.portal.email.vo;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by Owen on 2016/11/18.
 */
public class EmailParameterVo {

    @NotNull
    private String job;

    private String subject;

    private String sender;

    private String receivers;

    Map<String, String> templateInput;

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

    public Map<String, String> getTemplateInput() {
        return templateInput;
    }

    public void setTemplateInput(Map<String, String> templateInput) {
        this.templateInput = templateInput;
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
}
