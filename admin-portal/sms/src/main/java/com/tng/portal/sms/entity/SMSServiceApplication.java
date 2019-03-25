package com.tng.portal.sms.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tng.portal.common.entity.AnaApplication;

@Entity
@Table(name = "SMS_SERVICE_APPLICATION")
@JsonInclude(Include.NON_NULL)
public class SMSServiceApplication {
	@EmbeddedId
    private SMSServiceApplicationPk smsServiceApplicationPk;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "APPLICATION_CODE",referencedColumnName = "code",insertable = false, updatable = false)
    private AnaApplication anaApplication;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sms_provider_id",referencedColumnName = "id",insertable = false, updatable = false)
    private SMSProvider smsProvider;
	
    @Column(name = "priority", length = 2)
	private int priority;

	public SMSServiceApplicationPk getSmsServiceApplicationPk() {
		return smsServiceApplicationPk;
	}

	public void setSmsServiceApplicationPk(SMSServiceApplicationPk smsServiceApplicationPk) {
		this.smsServiceApplicationPk = smsServiceApplicationPk;
	}

	public AnaApplication getAnaApplication() {
		return anaApplication;
	}

	public void setAnaApplication(AnaApplication anaApplication) {
		this.anaApplication = anaApplication;
	}

	public SMSProvider getSmsProvider() {
		return smsProvider;
	}

	public void setSmsProvider(SMSProvider smsProvider) {
		this.smsProvider = smsProvider;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
