package com.gea.portal.order.entity.production.order;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;


/**
 * The persistent class for the gea_tx_queue database table.
 * 
 */
@Entity
@Table(name="GEA_TX_QUEUE")
public class ProGeaTxQueue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name="B_SCREENING_REF_ID")
	private String bScreeningRefId;

	@Column(name="CREATE_DATETIME")
	private Timestamp createDatetime;

	@Column(name="CS_AGENT_ID")
	private String csAgentId;

	@Column(name="CS_REJECT_REASON_ID")
	private BigDecimal csRejectReasonId;

	@Column(name="CS_REMARK")
	private String csRemark;

	@Column(name="GEA_REF_ID")
	private String geaRefId;

	@Column(name="GEA_STATUS")
	private String geaStatus;

	@Column(name="GEA_STATUS_CODE")
	private String geaStatusCode;

	@Column(name="INSTANCE_ID")
	private int instanceId;

	@Column(name="IS_ALREADY_REMIND")
	private BigDecimal isAlreadyRemind;

	@Column(name="IS_TNG_CANCEL")
	private BigDecimal isTngCancel;

	@Column(name="IS_TNG_CHANGE_NAME")
	private BigDecimal isTngChangeName;

	@Column(name="IS_TRANGLO_CHANGE_NAME")
	private BigDecimal isTrangloChangeName;

	@Column(name="QUOTATION_ID")
	private BigInteger quotationId;

	@Column(name="QUOTATION_REF_ID")
	private String quotationRefId;

	@Column(name="RECORD_ID")
	private int recordId;

	@Column(name="RECORD_TYPE")
	private String recordType;

	@Column(name="REMINDED_TYPE")
	private String remindedType;

	@Column(name="REQUEST_RECORD_ID")
	private BigDecimal requestRecordId;

	@Column(name="S_SCREENING_REF_ID")
	private String sScreeningRefId;

	@Column(name="SCREENING_RESULT")
	private String screeningResult;

	@Column(name="SCREENING_SERVER_REF_ID")
	private String screeningServerRefId;

	@Column(name="sequence")
	private int sequence;

	@Column(name="SERVICE_ID" )
	private String serviceId;

	@Column(name="SR_REASON_EN")
	private String srReasonEn;

	@Column(name="SR_REASON_ZH_CN")
	private String srReasonZhCn;

	@Column(name="SR_REASON_ZH_HK")
	private String srReasonZhHk;

	@Column(name="STATUS")
	private String status;

	@Column(name="SUPPLEMENT_ID")
	private BigDecimal supplementId;

	@Column(name="TNG_TX_NO")
	private String tngTxNo;

	@Column(name="UPDATE_DATETIME")
	private Timestamp updateDatetime;

	@NotFound(action= NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "P_ID")
	private ProEwalletParticipant participant;

	@NotFound(action= NotFoundAction.IGNORE)
	@OneToOne()
	@JoinColumn(name = "QUOTATION_ID", insertable=false, updatable=false)
	private ProQuotation quotation;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBScreeningRefId() {
		return this.bScreeningRefId;
	}

	public void setBScreeningRefId(String bScreeningRefId) {
		this.bScreeningRefId = bScreeningRefId;
	}

	public Timestamp getCreateDatetime() {
		return this.createDatetime;
	}

	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getCsAgentId() {
		return this.csAgentId;
	}

	public void setCsAgentId(String csAgentId) {
		this.csAgentId = csAgentId;
	}

	public BigDecimal getCsRejectReasonId() {
		return this.csRejectReasonId;
	}

	public void setCsRejectReasonId(BigDecimal csRejectReasonId) {
		this.csRejectReasonId = csRejectReasonId;
	}

	public String getCsRemark() {
		return this.csRemark;
	}

	public void setCsRemark(String csRemark) {
		this.csRemark = csRemark;
	}

	public String getGeaRefId() {
		if(StringUtils.isBlank(this.geaRefId)){
			this.geaRefId = "";
		}
		return this.geaRefId;
	}

	public void setGeaRefId(String geaRefId) {
		this.geaRefId = geaRefId;
	}

	public String getGeaStatus() {
		return this.geaStatus;
	}

	public void setGeaStatus(String geaStatus) {
		this.geaStatus = geaStatus;
	}

	public String getGeaStatusCode() {
		return this.geaStatusCode;
	}

	public void setGeaStatusCode(String geaStatusCode) {
		this.geaStatusCode = geaStatusCode;
	}

	public int getInstanceId() {
		return this.instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	public BigDecimal getIsAlreadyRemind() {
		return this.isAlreadyRemind;
	}

	public void setIsAlreadyRemind(BigDecimal isAlreadyRemind) {
		this.isAlreadyRemind = isAlreadyRemind;
	}

	public BigDecimal getIsTngCancel() {
		return this.isTngCancel;
	}

	public void setIsTngCancel(BigDecimal isTngCancel) {
		this.isTngCancel = isTngCancel;
	}

	public BigDecimal getIsTngChangeName() {
		return this.isTngChangeName;
	}

	public void setIsTngChangeName(BigDecimal isTngChangeName) {
		this.isTngChangeName = isTngChangeName;
	}

	public BigDecimal getIsTrangloChangeName() {
		return this.isTrangloChangeName;
	}

	public void setIsTrangloChangeName(BigDecimal isTrangloChangeName) {
		this.isTrangloChangeName = isTrangloChangeName;
	}

	public BigInteger getQuotationId() {
		return this.quotationId;
	}

	public void setQuotationId(BigInteger quotationId) {
		this.quotationId = quotationId;
	}

	public String getQuotationRefId() {
		return this.quotationRefId;
	}

	public void setQuotationRefId(String quotationRefId) {
		this.quotationRefId = quotationRefId;
	}

	public int getRecordId() {
		return this.recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public String getRecordType() {
		if(StringUtils.isBlank(this.recordType)){
			this.recordType = "";
		}
		return this.recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getRemindedType() {
		return this.remindedType;
	}

	public void setRemindedType(String remindedType) {
		this.remindedType = remindedType;
	}

	public BigDecimal getRequestRecordId() {
		return this.requestRecordId;
	}

	public void setRequestRecordId(BigDecimal requestRecordId) {
		this.requestRecordId = requestRecordId;
	}

	public String getSScreeningRefId() {
		return this.sScreeningRefId;
	}

	public void setSScreeningRefId(String sScreeningRefId) {
		this.sScreeningRefId = sScreeningRefId;
	}

	public String getScreeningResult() {
		return this.screeningResult;
	}

	public void setScreeningResult(String screeningResult) {
		this.screeningResult = screeningResult;
	}

	public String getScreeningServerRefId() {
		return this.screeningServerRefId;
	}

	public void setScreeningServerRefId(String screeningServerRefId) {
		this.screeningServerRefId = screeningServerRefId;
	}

	public int getSequence() {
		return this.sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getSrReasonEn() {
		return this.srReasonEn;
	}

	public void setSrReasonEn(String srReasonEn) {
		this.srReasonEn = srReasonEn;
	}

	public String getSrReasonZhCn() {
		return this.srReasonZhCn;
	}

	public void setSrReasonZhCn(String srReasonZhCn) {
		this.srReasonZhCn = srReasonZhCn;
	}

	public String getSrReasonZhHk() {
		return this.srReasonZhHk;
	}

	public void setSrReasonZhHk(String srReasonZhHk) {
		this.srReasonZhHk = srReasonZhHk;
	}

	public String getStatus() {
		if(StringUtils.isBlank(this.status)){
			this.status = "";
		}
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getSupplementId() {
		return this.supplementId;
	}

	public void setSupplementId(BigDecimal supplementId) {
		this.supplementId = supplementId;
	}

	public String getTngTxNo() {
		return this.tngTxNo;
	}

	public void setTngTxNo(String tngTxNo) {
		this.tngTxNo = tngTxNo;
	}

	public Timestamp getUpdateDatetime() {
		return this.updateDatetime;
	}

	public void setUpdateDatetime(Timestamp updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public ProEwalletParticipant getParticipant() {
		return participant;
	}

	public void setParticipant(ProEwalletParticipant participant) {
		this.participant = participant;
	}

	public ProQuotation getQuotation() {
		return quotation;
	}

	public void setQuotation(ProQuotation quotation) {
		this.quotation = quotation;
	}
}