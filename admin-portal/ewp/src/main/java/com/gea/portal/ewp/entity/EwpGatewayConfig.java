package com.gea.portal.ewp.entity;

import com.tng.portal.common.enumeration.ParticipantStatus;
import com.tng.portal.common.enumeration.Instance;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


@Entity
@Table(name="ewp_gateway_config")
@NamedQuery(name="EwpGatewayConfig.findAll", query="SELECT e FROM EwpGatewayConfig e")
public class EwpGatewayConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name="current_envir")
	@Enumerated(EnumType.STRING)
	private Instance currentEnvir;

	@Column(name="endpoints_health_sensitive")
	private Boolean endpointsHealthSensitive;

	@Column(name="endpoints_info_sensitive")
	private Boolean endpointsInfoSensitive;

	@Column(name="endpoints_restart_enable")
	private Boolean endpointsRestartEnable;

	@Column(name="mgt_context_path")
	private String mgtContextPath;

	@Column(name="mgt_health_refresh_enable")
	private Boolean mgtHealthRefreshEnable;

	@Column(name="mgt_security_enable")
	private Boolean mgtSecurityEnable;

	@Column(name="security_basic_enable")
	private Boolean securityBasicEnable;

	@Column(name="security_basic_path")
	private String securityBasicPath;

	@Column(name="security_user_name")
	private String securityUserName;

	@Column(name="security_user_pwd")
	private String securityUserPwd;

	@Column(name="server_api_key")
	private String serverApiKey;

	@Column(name="server_secret_key")
	private String serverSecretKey ;

	@Column(name="api_gateway_url")
	private String apiGatewayUrl;

	@Column(name="server_connection_timeout")
	private Long serverConnectionTimeout;

	@Column(name="server_healcheck_endpoint")
	private String serverHealcheckEndpoint;

	@Column(name="server_health_threshold")
	private String serverHealthThreshold;

	@Column(name="server_log_enable_encrypt")
	private Boolean serverLogEnableEncrypt;

	@Column(name="server_log_encryption_key")
	private String serverLogEncryptionKey;

	@Column(name="server_log_max_history")
	private Long serverLogMaxHistory;

	@Column(name="server_log_path")
	private String serverLogPath;

	@Column(name="server_log_pattern")
	private String serverLogPattern;

	@Column(name="server_log_total_size")
	private Long serverLogTotalSize;

	@Column(name="server_message_endpoint")
	private String serverMessageEndpoint;

	@Column(name="server_port")
	private String serverPort;

	@Column(name="server_request_timeout")
	private Long serverRequestTimeout;

	@Column(name="server_retry_times")
	private Long serverRetryTimes;

	@Column(name="server_routes_meta")
	private String serverRoutesMeta;

	@Column(name="server_routes_mth")
	private String serverRoutesMth;

	@Column(name="server_routes_sr")
	private String serverRoutesSr;

	@Column(name="server_zip_compression_level")
	private Long serverZipCompressionLevel;

	@Column(name="server_zip_key")
	private String serverZipKey;

	@Column(name="size_unit")
	private String sizeUnit;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ParticipantStatus status;

	//bi-directional many-to-one association to EwalletParticipant
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="participant_id")
	private EwalletParticipant ewalletParticipant;

	@Column(name="create_by")
	private String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="update_by")
	private String updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

	public EwpGatewayConfig() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instance getCurrentEnvir() {
		return currentEnvir;
	}

	public void setCurrentEnvir(Instance currentEnvir) {
		this.currentEnvir = currentEnvir;
	}

	public Boolean getEndpointsHealthSensitive() {
		return this.endpointsHealthSensitive;
	}

	public void setEndpointsHealthSensitive(Boolean endpointsHealthSensitive) {
		this.endpointsHealthSensitive = endpointsHealthSensitive;
	}

	public Boolean getEndpointsInfoSensitive() {
		return this.endpointsInfoSensitive;
	}

	public void setEndpointsInfoSensitive(Boolean endpointsInfoSensitive) {
		this.endpointsInfoSensitive = endpointsInfoSensitive;
	}

	public Boolean getEndpointsRestartEnable() {
		return this.endpointsRestartEnable;
	}

	public void setEndpointsRestartEnable(Boolean endpointsRestartEnable) {
		this.endpointsRestartEnable = endpointsRestartEnable;
	}

	public String getMgtContextPath() {
		return this.mgtContextPath;
	}

	public void setMgtContextPath(String mgtContextPath) {
		this.mgtContextPath = mgtContextPath;
	}

	public Boolean getMgtHealthRefreshEnable() {
		return this.mgtHealthRefreshEnable;
	}

	public void setMgtHealthRefreshEnable(Boolean mgtHealthRefreshEnable) {
		this.mgtHealthRefreshEnable = mgtHealthRefreshEnable;
	}

	public Boolean getMgtSecurityEnable() {
		return mgtSecurityEnable;
	}

	public void setMgtSecurityEnable(Boolean mgtSecurityEnable) {
		this.mgtSecurityEnable = mgtSecurityEnable;
	}

	public Boolean getSecurityBasicEnable() {
		return this.securityBasicEnable;
	}

	public void setSecurityBasicEnable(Boolean securityBasicEnable) {
		this.securityBasicEnable = securityBasicEnable;
	}

	public String getSecurityBasicPath() {
		return this.securityBasicPath;
	}

	public void setSecurityBasicPath(String securityBasicPath) {
		this.securityBasicPath = securityBasicPath;
	}

	public String getSecurityUserName() {
		return this.securityUserName;
	}

	public void setSecurityUserName(String securityUserName) {
		this.securityUserName = securityUserName;
	}

	public String getSecurityUserPwd() {
		return this.securityUserPwd;
	}

	public void setSecurityUserPwd(String securityUserPwd) {
		this.securityUserPwd = securityUserPwd;
	}

	public String getServerApiKey() {
		return this.serverApiKey;
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

	public Long getServerConnectionTimeout() {
		return this.serverConnectionTimeout;
	}

	public void setServerConnectionTimeout(Long serverConnectionTimeout) {
		this.serverConnectionTimeout = serverConnectionTimeout;
	}

	public String getServerHealcheckEndpoint() {
		return this.serverHealcheckEndpoint;
	}

	public void setServerHealcheckEndpoint(String serverHealcheckEndpoint) {
		this.serverHealcheckEndpoint = serverHealcheckEndpoint;
	}

	public String getServerHealthThreshold() {
		return this.serverHealthThreshold;
	}

	public void setServerHealthThreshold(String serverHealthThreshold) {
		this.serverHealthThreshold = serverHealthThreshold;
	}

	public Boolean getServerLogEnableEncrypt() {
		return this.serverLogEnableEncrypt;
	}

	public void setServerLogEnableEncrypt(Boolean serverLogEnableEncrypt) {
		this.serverLogEnableEncrypt = serverLogEnableEncrypt;
	}

	public String getServerLogEncryptionKey() {
		return this.serverLogEncryptionKey;
	}

	public void setServerLogEncryptionKey(String serverLogEncryptionKey) {
		this.serverLogEncryptionKey = serverLogEncryptionKey;
	}

	public Long getServerLogMaxHistory() {
		return this.serverLogMaxHistory;
	}

	public void setServerLogMaxHistory(Long serverLogMaxHistory) {
		this.serverLogMaxHistory = serverLogMaxHistory;
	}

	public String getServerLogPath() {
		return this.serverLogPath;
	}

	public void setServerLogPath(String serverLogPath) {
		this.serverLogPath = serverLogPath;
	}

	public String getServerLogPattern() {
		return this.serverLogPattern;
	}

	public void setServerLogPattern(String serverLogPattern) {
		this.serverLogPattern = serverLogPattern;
	}

	public Long getServerLogTotalSize() {
		return this.serverLogTotalSize;
	}

	public void setServerLogTotalSize(Long serverLogTotalSize) {
		this.serverLogTotalSize = serverLogTotalSize;
	}

	public String getServerMessageEndpoint() {
		return this.serverMessageEndpoint;
	}

	public void setServerMessageEndpoint(String serverMessageEndpoint) {
		this.serverMessageEndpoint = serverMessageEndpoint;
	}

	public String getServerPort() {
		return this.serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public Long getServerRequestTimeout() {
		return this.serverRequestTimeout;
	}

	public void setServerRequestTimeout(Long serverRequestTimeout) {
		this.serverRequestTimeout = serverRequestTimeout;
	}

	public Long getServerRetryTimes() {
		return this.serverRetryTimes;
	}

	public void setServerRetryTimes(Long serverRetryTimes) {
		this.serverRetryTimes = serverRetryTimes;
	}

	public String getServerRoutesMeta() {
		return this.serverRoutesMeta;
	}

	public void setServerRoutesMeta(String serverRoutesMeta) {
		this.serverRoutesMeta = serverRoutesMeta;
	}

	public String getServerRoutesMth() {
		return this.serverRoutesMth;
	}

	public void setServerRoutesMth(String serverRoutesMth) {
		this.serverRoutesMth = serverRoutesMth;
	}

	public String getServerRoutesSr() {
		return this.serverRoutesSr;
	}

	public void setServerRoutesSr(String serverRoutesSr) {
		this.serverRoutesSr = serverRoutesSr;
	}

	public Long getServerZipCompressionLevel() {
		return this.serverZipCompressionLevel;
	}

	public void setServerZipCompressionLevel(Long serverZipCompressionLevel) {
		this.serverZipCompressionLevel = serverZipCompressionLevel;
	}

	public String getServerZipKey() {
		return this.serverZipKey;
	}

	public void setServerZipKey(String serverZipKey) {
		this.serverZipKey = serverZipKey;
	}

	public String getSizeUnit() {
		return this.sizeUnit;
	}

	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}

	public ParticipantStatus getStatus() {
		return status;
	}

	public void setStatus(ParticipantStatus status) {
		this.status = status;
	}

	public EwalletParticipant getEwalletParticipant() {
		return this.ewalletParticipant;
	}

	public void setEwalletParticipant(EwalletParticipant ewalletParticipant) {
		this.ewalletParticipant = ewalletParticipant;
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

}