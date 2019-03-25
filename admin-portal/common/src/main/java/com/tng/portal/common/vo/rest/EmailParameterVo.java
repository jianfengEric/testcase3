package com.tng.portal.common.vo.rest;

import java.util.Map;

/**
 * Created by Owen on 2016/11/18.
 */
public class EmailParameterVo {


    private String job;

    private String subject;

    private String sender;

    private String receivers;

    private String message;
    
    private String senderId;
    
    private String receiversId;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EmailParameterVo{" +
                "job='" + job + '\'' +
                ", subject='" + subject + '\'' +
                ", sender='" + sender + '\'' +
                ", receivers='" + receivers + '\'' +
                ", message='" + message + '\'' +
                ", templateInput=" + templateInput +
                '}';
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
