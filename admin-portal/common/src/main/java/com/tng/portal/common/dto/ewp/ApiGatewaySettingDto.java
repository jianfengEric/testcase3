package com.tng.portal.common.dto.ewp;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dell on 2018/8/27.
 */
public class ApiGatewaySettingDto implements Serializable{
	private Long id;
	
	private Long participantId;
	
	private String status;
	
	private String instance;
	
	private Boolean securityBasicEnable;
	
	private String securityBasicPath;
	
	private String securityUserName;
	
	private String securityUserPwd;
	
	private Boolean mgtSecurityEnable;
	
	private String mgtContextPath;
	
	private Boolean mgtHealthRefreshEnable;
	
	private Boolean endpointsInfoSensitive;
	
	private Boolean endpointsHealthSensitive;
	
	private Boolean endpointsRestartEnable;
	
	private String serverPort;

	private String serverApiKey;

	private String serverSecretKey ;

	private String apiGatewayUrl ;

	private String serverLogPath;
	
	private String serverLogEncryptionKey;
	
	private Long serverLogTotalSize;
	
	private String sizeUnit;
	
	private Long serverLogMaxHistory;
	
	private String serverLogPattern;
	
	private String serverHealthThreshold;
	
	private String serverZipKey;
	
	private Long serverZipCompressionLevel;
	
	private String serverRoutesMth;
	
	private String serverRoutesSr;
	
	private String serverRoutesMeta;
	
	private Long serverConnectionTimeout;
	
	private Long serverRequestTimeout;
	
	private Long serverRetryTimes;
	
	private String serverHealcheckEndpoint;
	
	private String serverMessageEndpoint;
	
	private Boolean serverLogEnableEncrypt;
	
	private String createBy;
	
	private Date createDate;
	
	private String updateBy;
	
	private Date updateDate;
	
	private String requestRemark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getSecurityBasicEnable() {
		return securityBasicEnable;
	}

	public void setSecurityBasicEnable(Boolean securityBasicEnable) {
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

	public Boolean getMgtSecurityEnable() {
		return mgtSecurityEnable;
	}

	public void setMgtSecurityEnable(Boolean mgtSecurityEnable) {
		this.mgtSecurityEnable = mgtSecurityEnable;
	}

	public String getMgtContextPath() {
		return mgtContextPath;
	}

	public void setMgtContextPath(String mgtContextPath) {
		this.mgtContextPath = mgtContextPath;
	}

	public Boolean getMgtHealthRefreshEnable() {
		return mgtHealthRefreshEnable;
	}

	public void setMgtHealthRefreshEnable(Boolean mgtHealthRefreshEnable) {
		this.mgtHealthRefreshEnable = mgtHealthRefreshEnable;
	}

	public Boolean getEndpointsInfoSensitive() {
		return endpointsInfoSensitive;
	}

	public void setEndpointsInfoSensitive(Boolean endpointsInfoSensitive) {
		this.endpointsInfoSensitive = endpointsInfoSensitive;
	}

	public Boolean getEndpointsHealthSensitive() {
		return endpointsHealthSensitive;
	}

	public void setEndpointsHealthSensitive(Boolean endpointsHealthSensitive) {
		this.endpointsHealthSensitive = endpointsHealthSensitive;
	}

	public Boolean getEndpointsRestartEnable() {
		return endpointsRestartEnable;
	}

	public void setEndpointsRestartEnable(Boolean endpointsRestartEnable) {
		this.endpointsRestartEnable = endpointsRestartEnable;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerApiKey() {
		return serverApiKey;
	}

	public void setServerApiKey(String serverApiKey) {
		this.serverApiKey = serverApiKey;
	}

	public String getServerSecretKey() {
		return serverSecretKey;
	}

	public void setServerSecretKey(String serverSecretKey) {
		this.serverSecretKey = serverSecretKey;
	}

	public String getApiGatewayUrl() {
		return apiGatewayUrl;
	}

	public void setApiGatewayUrl(String apiGatewayUrl) {
		this.apiGatewayUrl = apiGatewayUrl;
	}

	public String getServerLogPath() {
		return serverLogPath;
	}

	public void setServerLogPath(String serverLogPath) {
		this.serverLogPath = serverLogPath;
	}

	public String getServerLogEncryptionKey() {
		return serverLogEncryptionKey;
	}

	public void setServerLogEncryptionKey(String serverLogEncryptionKey) {
		this.serverLogEncryptionKey = serverLogEncryptionKey;
	}

	public Long getServerLogTotalSize() {
		return serverLogTotalSize;
	}

	public void setServerLogTotalSize(Long serverLogTotalSize) {
		this.serverLogTotalSize = serverLogTotalSize;
	}

	public String getSizeUnit() {
		return sizeUnit;
	}

	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}

	public Long getServerLogMaxHistory() {
		return serverLogMaxHistory;
	}

	public void setServerLogMaxHistory(Long serverLogMaxHistory) {
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

	public Long getServerZipCompressionLevel() {
		return serverZipCompressionLevel;
	}

	public void setServerZipCompressionLevel(Long serverZipCompressionLevel) {
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

	public Long getServerConnectionTimeout() {
		return serverConnectionTimeout;
	}

	public void setServerConnectionTimeout(Long serverConnectionTimeout) {
		this.serverConnectionTimeout = serverConnectionTimeout;
	}

	public Long getServerRequestTimeout() {
		return serverRequestTimeout;
	}

	public void setServerRequestTimeout(Long serverRequestTimeout) {
		this.serverRequestTimeout = serverRequestTimeout;
	}

	public Long getServerRetryTimes() {
		return serverRetryTimes;
	}

	public void setServerRetryTimes(Long serverRetryTimes) {
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

	public Boolean getServerLogEnableEncrypt() {
		return serverLogEnableEncrypt;
	}

	public void setServerLogEnableEncrypt(Boolean serverLogEnableEncrypt) {
		this.serverLogEnableEncrypt = serverLogEnableEncrypt;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}


}
