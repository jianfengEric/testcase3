package com.tng.portal.sms.server.vo;

import com.tng.portal.sms.server.entity.SMSProvider;

/**
 * Created by Owen on 2017/7/28.
 */
public class SendSMSResponseVo {
    private String status;
    private String rspMsg;
    private String errorMsg;
    private SMSProvider smsProvider;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRspMsg() {
        return rspMsg;
    }

    public void setRspMsg(String rspMsg) {
        this.rspMsg = rspMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public SMSProvider getSmsProvider() {
        return smsProvider;
    }

    public void setSmsProvider(SMSProvider smsProvider) {
        this.smsProvider = smsProvider;
    }

    @Override
    public String toString() {
        return "SendSMSResponseVo{" +
                "status='" + status + '\'' +
                ", rspMsg='" + rspMsg + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", smsProvider=" + smsProvider +
                '}';
    }
}
