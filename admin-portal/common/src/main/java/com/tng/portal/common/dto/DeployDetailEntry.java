package com.tng.portal.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tng.portal.common.enumeration.ServiceName;
import com.tng.portal.common.exception.BusinessException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by Dell on 2018/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeployDetailEntry implements Serializable {

    private String serviceName;
    private List<Detail> details;

    public DeployDetailEntry() {

    }

    public DeployDetailEntry(ServiceName serviceName) {
        this.serviceName = serviceName.getValue();
        this.details = new ArrayList<>();
    }
    
    public DeployDetailEntry.Detail findDetailByField(Map<String,String> field) {
	    try{
    		for(DeployDetailEntry.Detail detail : this.getDetails()){
    			boolean flag = false;
	    		for(Entry<String,String> en : field.entrySet()){
	    			String value = BeanUtils.getProperty(detail, en.getKey());
	    			flag = value.equals(en.getValue());
	    			if(!flag){
	    				break;
	    			}
	    		}
	    		if(flag){
	    			return detail;
	    		}
	    	}
	    	return null;
	    }catch(Exception e){
	    	throw new BusinessException("findByField err",e);
	    }
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public void addDetail(Detail detail) {
        getDetails().add(detail);
    }

    public void addParticipantDetails(String participantStatus, String syncType, String geaParticipantRefId, String participantName, String address, String serverApiKey, String serverLogEncryptionKey) {
        getDetails().add(new Detail(participantStatus, syncType, geaParticipantRefId, participantName, address, serverApiKey, serverLogEncryptionKey));
    }

    @Override
    public String toString() {
        return "DeployDetailEntry{" +
                "serviceName='" + serviceName + '\'' +
                ", details=" + details +
                '}';
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Detail implements Serializable{
        private String syncType;

        private String geaParticipantRefId;
        private String participantName;
        private String participantStatus;
        private String geaSysPid;

        private String serverApiKey;
        private String serverSecretKey;
        private String apiGatewayUrl;

        private String securityBasicEnable;
        private String securityBasicPath;
        private String securityUserName;
        private String securityUserPwd;
        private String mgtSecurityEnable;
        private String mgtContextPath;
        private String mgtHealthRefreshEnable;
        private String endpointsInfoSensitive;
        private String endpointsHealthSensitive;
        private String endpointsRestartEnable;
        private String serverPort;
        private String serverLogPath;
        private String serverLogEncryptionKey;
        private String serverLogTotalSize;
        private String sizeUnit;
        private String serverLogMaxHistory;
        private String serverLogPattern;
        private String serverHealthThreshold;
        private String serverZipKey;
        private String serverZipCompressionLevel;
        private String serverRoutesMth;
        private String serverRoutesSr;
        private String serverRoutesMeta;
        private String serverConnectionTimeout;
        private String serverRequestTimeout;
        private String serverRetryTimes;
        private String serverHealcheckEndpoint;
        private String serverMessageEndpoint;
        private String serverLogEnableEncrypt;

        private String geaServiceRefId;
        private String markup;
        private String serviceCode;

        private String geaMoneyPoolRefId;
        private String moneyPoolStatus;
        private String alertLine;
        private String adjustType;
        private String amount;
        private String currency;
        private String geaTxRefNo;
        private String remark;

        private String currencyFrom;
        private String currencyTo;
        private String enable;
        private String adminFee;
        private String changeNameAdminFee;
        private String cancelAdminFee;

        private String dailySettlementCutOffTime;
        
        private String rate;

        public Detail() {

        }

        public Detail(String participantStatus, String syncType, String geaParticipantRefId, String participantName, String address, String serverApiKey, String serverLogEncryptionKey) {
            this.participantStatus = participantStatus;
            this.syncType = syncType;
            this.geaParticipantRefId = geaParticipantRefId;
            this.participantName = participantName;
            this.serverApiKey = serverApiKey;
            this.serverLogEncryptionKey = serverLogEncryptionKey;
        }
        
        /**
         *  Remove contrast fields 
         */
        public Map<String,String> getCompareField(String serviceName){
        	Map<String,String> field = new HashMap<>();
			if(ServiceName.participant.getValue().equals(serviceName)) {
				field.put("geaParticipantRefId", this.getGeaParticipantRefId());
            
			}else if(ServiceName.ewpServiceMarkupSetting.getValue().equals(serviceName)){
            	field.put("geaServiceRefId", this.getGeaServiceRefId());

            }else if(ServiceName.ewpMoneypool.getValue().equals(serviceName)){
            	field.put("geaMoneyPoolRefId", this.getGeaMoneyPoolRefId());

            }else if(ServiceName.ewpMoneypoolServiceMap.getValue().equals(serviceName)){
            	field.put("geaMoneyPoolRefId", this.getGeaMoneyPoolRefId());
            	field.put("geaServiceRefId", this.getGeaServiceRefId());

            }else if(ServiceName.ewpServiceCurrencySetting.getValue().equals(serviceName)){
            	field.put("geaServiceRefId", this.getGeaServiceRefId());
                field.put("currencyFrom", this.getCurrencyFrom());
                field.put("currencyTo", this.getCurrencyTo());

            }else if(ServiceName.ewpProgramSetting.getValue().equals(serviceName)){
            	field.put("geaParticipantRefId", this.getGeaParticipantRefId());

            }else if(ServiceName.ewpApiGatewayConfig.getValue().equals(serviceName)){
            	field.put("geaParticipantRefId", this.getGeaParticipantRefId());

            }else if(ServiceName.settlementCutOffTimeSetting.getValue().equals(serviceName)){
            	field.put("geaParticipantRefId", this.getGeaParticipantRefId());

            }else{
            	throw new BusinessException(" Illegal ServiceName");
            }
			return field;
        }
        
        public boolean compareGea(ServiceName serviceName, DeployDetailEntry.Detail geaDetail){
        	// For different ServiceName Make a comparison 
			if(ServiceName.participant==serviceName) {
				return this.equalsParticipant(geaDetail);
			}else if(ServiceName.ewpServiceMarkupSetting==serviceName){
				return this.equalsEwpServiceMarkupSetting(geaDetail);
            }else if(ServiceName.ewpMoneypool==serviceName){
            	return this.equalsEwpMoneypool(geaDetail);
            }else if(ServiceName.ewpMoneypoolServiceMap==serviceName){
            	return this.equalsEwpMoneypoolServiceMap(geaDetail);
            }else if(ServiceName.ewpServiceCurrencySetting==serviceName){
            	return this.equalsEwpServiceCurrencySetting(geaDetail);
            }else if(ServiceName.ewpProgramSetting==serviceName){
            	return this.equalsEwpProgramSetting(geaDetail);
            }else if(ServiceName.ewpApiGatewayConfig==serviceName){
            	return this.equalsEwpApiGatewayConfig(geaDetail);
            }else if(ServiceName.settlementCutOffTimeSetting==serviceName){
            	return this.equalsSettlementCutOffTimeSetting(geaDetail);
            }else if(ServiceName.exchangeRate==serviceName){
            	return this.equalsExchangeRate(geaDetail);
            }else{
            	throw new BusinessException(" Illegal ServiceName");
            }
        }
        
        /**
         *  and GEA Conduct Participant Data comparison 
         */
        private boolean equalsParticipant(DeployDetailEntry.Detail geaDetail){
        	if (StringUtils.equals(this.getGeaParticipantRefId(),geaDetail.getGeaParticipantRefId())
                    && StringUtils.equals(this.getParticipantName(),geaDetail.getParticipantName())
                    && StringUtils.equals(this.getParticipantStatus(),geaDetail.getParticipantStatus())
                    ) {
                return true;
            }
            return false;
        }
        
        /**
         *  and GEA Conduct ProgramSetting Data comparison 
         */
        private boolean equalsEwpProgramSetting(DeployDetailEntry.Detail geaDetail){
            if (StringUtils.equals(this.getGeaParticipantRefId(),geaDetail.getGeaParticipantRefId())
                    && StringUtils.equals(this.getServerApiKey(),geaDetail.getServerApiKey())
                    && StringUtils.equals(this.getServerSecretKey(),geaDetail.getServerSecretKey())
                    && StringUtils.equals(this.getApiGatewayUrl(),geaDetail.getApiGatewayUrl())
                    ) {
                return true;
            }
            return false;
        }

        /**
         *  and GEA Conduct GeaGateway Data comparison 
         */
        private boolean equalsEwpApiGatewayConfig(DeployDetailEntry.Detail geaDetail){
            if (StringUtils.equals(this.getGeaParticipantRefId(),geaDetail.getGeaParticipantRefId())
                    && StringUtils.equals(this.getSecurityBasicEnable(),geaDetail.getSecurityBasicEnable())
                    && StringUtils.equals(this.getSecurityBasicPath(),geaDetail.getSecurityBasicPath())
                    && StringUtils.equals(this.getSecurityUserName(),geaDetail.getSecurityUserName())
                    && StringUtils.equals(this.getSecurityUserPwd(),geaDetail.getSecurityUserPwd())
                    && StringUtils.equals(this.getMgtSecurityEnable(),geaDetail.getMgtSecurityEnable())
                    && StringUtils.equals(this.getMgtContextPath(),geaDetail.getMgtContextPath())
                    && StringUtils.equals(this.getMgtHealthRefreshEnable(),geaDetail.getMgtHealthRefreshEnable())
                    && StringUtils.equals(this.getEndpointsInfoSensitive(),geaDetail.getEndpointsInfoSensitive())
                    && StringUtils.equals(this.getEndpointsHealthSensitive(),geaDetail.getEndpointsHealthSensitive())
                    && StringUtils.equals(this.getEndpointsRestartEnable(),geaDetail.getEndpointsRestartEnable())
                    && StringUtils.equals(this.getServerPort(),geaDetail.getServerPort())
                    && StringUtils.equals(this.getServerApiKey(),geaDetail.getServerApiKey())
                    && StringUtils.equals(this.getServerLogPath(),geaDetail.getServerLogPath())
                    && StringUtils.equals(this.getServerLogEncryptionKey(),geaDetail.getServerLogEncryptionKey())
                    && StringUtils.equals(this.getServerLogTotalSize(),geaDetail.getServerLogTotalSize())
                    && StringUtils.equals(this.getSizeUnit(),geaDetail.getSizeUnit())
                    && StringUtils.equals(this.getServerLogMaxHistory(),geaDetail.getServerLogMaxHistory())
                    && StringUtils.equals(this.getServerLogPattern(),geaDetail.getServerLogPattern())
                    && StringUtils.equals(this.getServerHealthThreshold(),geaDetail.getServerHealthThreshold())
                    && StringUtils.equals(this.getServerZipKey(),geaDetail.getServerZipKey())
                    && StringUtils.equals(this.getServerZipCompressionLevel(),geaDetail.getServerZipCompressionLevel())
                    && StringUtils.equals(this.getServerRoutesMth(),geaDetail.getServerRoutesMth())
                    && StringUtils.equals(this.getServerRoutesSr(),geaDetail.getServerRoutesSr())
                    && StringUtils.equals(this.getServerRoutesMeta(),geaDetail.getServerRoutesMeta())
                    && StringUtils.equals(this.getServerConnectionTimeout(),geaDetail.getServerConnectionTimeout())
                    && StringUtils.equals(this.getServerRequestTimeout(),geaDetail.getServerRequestTimeout())
                    && StringUtils.equals(this.getServerRetryTimes(),geaDetail.getServerRetryTimes())
                    && StringUtils.equals(this.getServerHealcheckEndpoint(),geaDetail.getServerHealcheckEndpoint())
                    && StringUtils.equals(this.getServerMessageEndpoint(),geaDetail.getServerMessageEndpoint())
                    && StringUtils.equals(this.getServerLogEnableEncrypt(),geaDetail.getServerLogEnableEncrypt())
                    ) {
                return true;
            }
            return false;
        }
        
        /**
         *  and GEA Conduct settlementCutOffTimeSetting Data comparison 
         */
        private boolean equalsSettlementCutOffTimeSetting(DeployDetailEntry.Detail geaDetail){
        	if (StringUtils.equals(this.getGeaParticipantRefId(),geaDetail.getGeaParticipantRefId())
                    && StringUtils.equals(this.getDailySettlementCutOffTime(),geaDetail.getDailySettlementCutOffTime())
                    ) {
                return true;
            }
            return false;
        }
        
        /**
         *  and GEA Conduct Service Data comparison 
         */
        private boolean equalsEwpServiceMarkupSetting(DeployDetailEntry.Detail geaDetail){
            if (StringUtils.equals(this.getGeaServiceRefId(),geaDetail.getGeaServiceRefId())
                    && StringUtils.equals(this.getServiceCode(),geaDetail.getServiceCode())
                    && Objects.equals(Double.valueOf(this.getMarkup()),Double.valueOf(geaDetail.getMarkup()))
                    ) {
                return true;
            }
            return false;
        }
        
        /**
         *  and GEA Conduct Moneypool Data comparison 
         */
        private boolean equalsEwpMoneypool(DeployDetailEntry.Detail geaDetail){
            if (StringUtils.equals(this.getGeaParticipantRefId(),geaDetail.getGeaParticipantRefId())
                    && StringUtils.equals(this.getGeaMoneyPoolRefId(),geaDetail.getGeaMoneyPoolRefId())
                    && StringUtils.equals(this.getMoneyPoolStatus(),geaDetail.getMoneyPoolStatus())
                    && StringUtils.equals(this.getCurrency(),geaDetail.getCurrency())
                    && Objects.equals(Double.valueOf(this.getAlertLine()),Double.valueOf(geaDetail.getAlertLine()))
                    ) {
                return true;
            }
            return false;
        }

        /**
         *  and GEA Conduct MoneypoolServiceMap Data comparison 
         */
        private boolean equalsEwpMoneypoolServiceMap(DeployDetailEntry.Detail geaDetail){
            if (StringUtils.equals(this.getGeaMoneyPoolRefId(),geaDetail.getGeaMoneyPoolRefId())
                    && StringUtils.equals(this.getGeaServiceRefId(),geaDetail.getGeaServiceRefId())
                    && StringUtils.equals(this.getServiceCode(),geaDetail.getServiceCode())
                    ) {
                return true;
            }
            return false;
        }
        
        /**
         *  and GEA Conduct Currency Data comparison 
         */
        private boolean equalsEwpServiceCurrencySetting(DeployDetailEntry.Detail geaDetail){
            if (StringUtils.equals(this.getGeaServiceRefId(),geaDetail.getGeaServiceRefId())
                    && StringUtils.equals(this.getCurrencyFrom(),geaDetail.getCurrencyFrom())
                    && StringUtils.equals(this.getCurrencyTo(),geaDetail.getCurrencyTo())
                    && StringUtils.equals(this.getEnable(),geaDetail.getEnable())
                    && this.equalsDouble(this.getAdminFee(), geaDetail.getAdminFee())
                    && this.equalsDouble(this.getChangeNameAdminFee(),geaDetail.getChangeNameAdminFee())
                    && this.equalsDouble(this.getCancelAdminFee(),geaDetail.getCancelAdminFee())
                    ) {
                return true;
            }
            return false;
        }
        
        /**
         *  与GEA exchange Rate 数据作比较
         */
        private boolean equalsExchangeRate(DeployDetailEntry.Detail geaDetail){
            if (StringUtils.equals(this.getCurrencyFrom(),geaDetail.getCurrencyFrom())
                    && StringUtils.equals(this.getCurrencyTo(),geaDetail.getCurrencyTo())
                    && Objects.equals(Double.valueOf(this.getRate()),Double.valueOf(geaDetail.getRate()))
                    ) {
                return true;
            }
            return false;
        }
        
        private boolean equalsDouble(String d1,String d2){
        	if(d1 == null && d2 == null){
        		return true;
        	}else if(d1 != null && d2 == null){
        		return false;
        	}else if(d1 == null && d2 != null){
        		return false;
        	}else{
        		return Objects.equals(Double.valueOf(d1),Double.valueOf(d2));
        	}
        }
        
        
        public String getSyncType() {
            return syncType;
        }

        public void setSyncType(String syncType) {
            this.syncType = syncType;
        }

        public String getGeaParticipantRefId() {
            return geaParticipantRefId;
        }

        public void setGeaParticipantRefId(String geaParticipantRefId) {
            this.geaParticipantRefId = geaParticipantRefId;
        }

        public String getParticipantName() {
            return participantName;
        }

        public void setParticipantName(String participantName) {
            this.participantName = participantName;
        }

        public String getServerApiKey() {
            return serverApiKey;
        }

        public void setServerApiKey(String serverApiKey) {
            this.serverApiKey = serverApiKey;
        }

        public String getServerLogEncryptionKey() {
            return serverLogEncryptionKey;
        }

        public void setServerLogEncryptionKey(String serverLogEncryptionKey) {
            this.serverLogEncryptionKey = serverLogEncryptionKey;
        }

        public String getParticipantStatus() {
            return participantStatus;
        }

        public void setParticipantStatus(String participantStatus) {
            this.participantStatus = participantStatus;
        }

        public String getGeaServiceRefId() {
            return geaServiceRefId;
        }

        public void setGeaServiceRefId(String geaServiceRefId) {
            this.geaServiceRefId = geaServiceRefId;
        }

        public String getMarkup() {
            return markup;
        }

        public void setMarkup(String markup) {
            this.markup = markup;
        }

        public String getGeaMoneyPoolRefId() {
            return geaMoneyPoolRefId;
        }

        public void setGeaMoneyPoolRefId(String geaMoneyPoolRefId) {
            this.geaMoneyPoolRefId = geaMoneyPoolRefId;
        }

        public String getMoneyPoolStatus() {
            return moneyPoolStatus;
        }

        public void setMoneyPoolStatus(String moneyPoolStatus) {
            this.moneyPoolStatus = moneyPoolStatus;
        }

        public String getAlertLine() {
            return alertLine;
        }

        public void setAlertLine(String alertLine) {
            this.alertLine = alertLine;
        }

        public String getCurrencyFrom() {
            return currencyFrom;
        }

        public void setCurrencyFrom(String currencyFrom) {
            this.currencyFrom = currencyFrom;
        }

        public String getCurrencyTo() {
            return currencyTo;
        }

        public void setCurrencyTo(String currencyTo) {
            this.currencyTo = currencyTo;
        }

        public String getEnable() {
            return enable;
        }

        public void setEnable(String enable) {
            this.enable = enable;
        }

        public String getAdminFee() {
            return adminFee;
        }

        public void setAdminFee(String adminFee) {
            this.adminFee = adminFee;
        }

        public String getServerSecretKey() {
            return serverSecretKey;
        }

        public void setServerSecretKey(String serverSecretKey) {
            this.serverSecretKey = serverSecretKey;
        }

        public String getServiceCode() {
            return serviceCode;
        }

        public void setServiceCode(String serviceCode) {
            this.serviceCode = serviceCode;
        }

        public String getChangeNameAdminFee() {
            return changeNameAdminFee;
        }

        public void setChangeNameAdminFee(String changeNameAdminFee) {
            this.changeNameAdminFee = changeNameAdminFee;
        }

        public String getCancelAdminFee() {
            return cancelAdminFee;
        }

        public void setCancelAdminFee(String cancelAdminFee) {
            this.cancelAdminFee = cancelAdminFee;
        }

        public String getAdjustType() {
            return adjustType;
        }

        public void setAdjustType(String adjustType) {
            this.adjustType = adjustType;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getApiGatewayUrl() {
            return apiGatewayUrl;
        }

        public void setApiGatewayUrl(String apiGatewayUrl) {
            this.apiGatewayUrl = apiGatewayUrl;
        }

        @Override
        public String toString() {
            return "Detail{" +
                    "syncType='" + syncType + '\'' +
                    ", geaParticipantRefId='" + geaParticipantRefId + '\'' +
                    ", participantName='" + participantName + '\'' +
                    ", serverApiKey='" + serverApiKey + '\'' +
                    ", serverLogEncryptionKey='" + serverLogEncryptionKey + '\'' +
                    ", participantStatus='" + participantStatus + '\'' +
                    ", geaServiceRefId='" + geaServiceRefId + '\'' +
                    ", markup='" + markup + '\'' +
                    ", geaMoneyPoolRefId='" + geaMoneyPoolRefId + '\'' +
                    ", moneyPoolStatus='" + moneyPoolStatus + '\'' +
                    ", alertLine='" + alertLine + '\'' +
                    ", currencyFrom='" + currencyFrom + '\'' +
                    ", currencyTo='" + currencyTo + '\'' +
                    ", enable='" + enable + '\'' +
                    ", adminFee='" + adminFee + '\'' +
                    '}';
        }

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

        public String getSecurityBasicEnable() {
            return securityBasicEnable;
        }

        public void setSecurityBasicEnable(String securityBasicEnable) {
            this.securityBasicEnable = securityBasicEnable;
        }

        public String getSecurityBasicPath() {
            return securityBasicPath;
        }

        public void setSecurityBasicPath(String securityBasicPath) {
            this.securityBasicPath = securityBasicPath;
        }

        public String getSecurityUserName() {
            return securityUserName;
        }

        public void setSecurityUserName(String securityUserName) {
            this.securityUserName = securityUserName;
        }

        public String getSecurityUserPwd() {
            return securityUserPwd;
        }

        public void setSecurityUserPwd(String securityUserPwd) {
            this.securityUserPwd = securityUserPwd;
        }

        public String getMgtSecurityEnable() {
            return mgtSecurityEnable;
        }

        public void setMgtSecurityEnable(String mgtSecurityEnable) {
            this.mgtSecurityEnable = mgtSecurityEnable;
        }

        public String getMgtContextPath() {
            return mgtContextPath;
        }

        public void setMgtContextPath(String mgtContextPath) {
            this.mgtContextPath = mgtContextPath;
        }

        public String getMgtHealthRefreshEnable() {
            return mgtHealthRefreshEnable;
        }

        public void setMgtHealthRefreshEnable(String mgtHealthRefreshEnable) {
            this.mgtHealthRefreshEnable = mgtHealthRefreshEnable;
        }

        public String getEndpointsInfoSensitive() {
            return endpointsInfoSensitive;
        }

        public void setEndpointsInfoSensitive(String endpointsInfoSensitive) {
            this.endpointsInfoSensitive = endpointsInfoSensitive;
        }

        public String getEndpointsHealthSensitive() {
            return endpointsHealthSensitive;
        }

        public void setEndpointsHealthSensitive(String endpointsHealthSensitive) {
            this.endpointsHealthSensitive = endpointsHealthSensitive;
        }

        public String getEndpointsRestartEnable() {
            return endpointsRestartEnable;
        }

        public void setEndpointsRestartEnable(String endpointsRestartEnable) {
            this.endpointsRestartEnable = endpointsRestartEnable;
        }

        public String getServerPort() {
            return serverPort;
        }

        public void setServerPort(String serverPort) {
            this.serverPort = serverPort;
        }

        public String getServerLogPath() {
            return serverLogPath;
        }

        public void setServerLogPath(String serverLogPath) {
            this.serverLogPath = serverLogPath;
        }

        public String getServerLogTotalSize() {
            return serverLogTotalSize;
        }

        public void setServerLogTotalSize(String serverLogTotalSize) {
            this.serverLogTotalSize = serverLogTotalSize;
        }

        public String getSizeUnit() {
            return sizeUnit;
        }

        public void setSizeUnit(String sizeUnit) {
            this.sizeUnit = sizeUnit;
        }

        public String getServerLogMaxHistory() {
            return serverLogMaxHistory;
        }

        public void setServerLogMaxHistory(String serverLogMaxHistory) {
            this.serverLogMaxHistory = serverLogMaxHistory;
        }

        public String getServerLogPattern() {
            return serverLogPattern;
        }

        public void setServerLogPattern(String serverLogPattern) {
            this.serverLogPattern = serverLogPattern;
        }

        public String getServerHealthThreshold() {
            return serverHealthThreshold;
        }

        public void setServerHealthThreshold(String serverHealthThreshold) {
            this.serverHealthThreshold = serverHealthThreshold;
        }

        public String getServerZipKey() {
            return serverZipKey;
        }

        public void setServerZipKey(String serverZipKey) {
            this.serverZipKey = serverZipKey;
        }

        public String getServerZipCompressionLevel() {
            return serverZipCompressionLevel;
        }

        public void setServerZipCompressionLevel(String serverZipCompressionLevel) {
            this.serverZipCompressionLevel = serverZipCompressionLevel;
        }

        public String getServerRoutesMth() {
            return serverRoutesMth;
        }

        public void setServerRoutesMth(String serverRoutesMth) {
            this.serverRoutesMth = serverRoutesMth;
        }

        public String getServerRoutesSr() {
            return serverRoutesSr;
        }

        public void setServerRoutesSr(String serverRoutesSr) {
            this.serverRoutesSr = serverRoutesSr;
        }

        public String getServerRoutesMeta() {
            return serverRoutesMeta;
        }

        public void setServerRoutesMeta(String serverRoutesMeta) {
            this.serverRoutesMeta = serverRoutesMeta;
        }

        public String getServerConnectionTimeout() {
            return serverConnectionTimeout;
        }

        public void setServerConnectionTimeout(String serverConnectionTimeout) {
            this.serverConnectionTimeout = serverConnectionTimeout;
        }

        public String getServerRequestTimeout() {
            return serverRequestTimeout;
        }

        public void setServerRequestTimeout(String serverRequestTimeout) {
            this.serverRequestTimeout = serverRequestTimeout;
        }

        public String getServerRetryTimes() {
            return serverRetryTimes;
        }

        public void setServerRetryTimes(String serverRetryTimes) {
            this.serverRetryTimes = serverRetryTimes;
        }

        public String getServerHealcheckEndpoint() {
            return serverHealcheckEndpoint;
        }

        public void setServerHealcheckEndpoint(String serverHealcheckEndpoint) {
            this.serverHealcheckEndpoint = serverHealcheckEndpoint;
        }

        public String getServerMessageEndpoint() {
            return serverMessageEndpoint;
        }

        public void setServerMessageEndpoint(String serverMessageEndpoint) {
            this.serverMessageEndpoint = serverMessageEndpoint;
        }

        public String getServerLogEnableEncrypt() {
            return serverLogEnableEncrypt;
        }

        public void setServerLogEnableEncrypt(String serverLogEnableEncrypt) {
            this.serverLogEnableEncrypt = serverLogEnableEncrypt;
        }

		public String getDailySettlementCutOffTime() {
			return dailySettlementCutOffTime;
		}

		public void setDailySettlementCutOffTime(String dailySettlementCutOffTime) {
			this.dailySettlementCutOffTime = dailySettlementCutOffTime;
		}

		public String getGeaTxRefNo() {
			return geaTxRefNo;
		}

		public void setGeaTxRefNo(String geaTxRefNo) {
			this.geaTxRefNo = geaTxRefNo;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getRate() {
			return rate;
		}

		public void setRate(String rate) {
			this.rate = rate;
		}

		public String getGeaSysPid() {
			return geaSysPid;
		}

		public void setGeaSysPid(String geaSysPid) {
			this.geaSysPid = geaSysPid;
		}
    }
}
