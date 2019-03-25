package com.tng.portal.sms.server.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Zero on 2016/11/4.
 */
@Embeddable
public class SMSServiceApplicationPk implements Serializable{
    @Basic(optional = false)
    @Column(name = "application_code")
    private String applicationCode;
    
    @Basic(optional = false)
    @Column(name = "sms_provider_id")
    private String smsProviderId;

    public SMSServiceApplicationPk() {

    }
    public SMSServiceApplicationPk(String applicationCode, String smsProviderId) {
        this.applicationCode = applicationCode;
        this.smsProviderId = smsProviderId;
    }

    public String getApplicationCode() {
		return applicationCode;
	}
	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}
	public String getSmsProviderId() {
		return smsProviderId;
	}
	public void setSmsProviderId(String smsProviderId) {
		this.smsProviderId = smsProviderId;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj != null && obj.getClass()==SMSServiceApplicationPk.class){
            SMSServiceApplicationPk pk = (SMSServiceApplicationPk) obj;
            return pk.getApplicationCode()==getApplicationCode()
                    && pk.getSmsProviderId() == getSmsProviderId();

        }
        return false;
    }

    @Override
    public int hashCode() {
        return getApplicationCode().hashCode()*31 + getApplicationCode().hashCode();
    }
}
