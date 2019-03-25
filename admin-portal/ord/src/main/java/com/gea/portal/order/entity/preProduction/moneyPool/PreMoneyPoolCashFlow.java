package com.gea.portal.order.entity.preProduction.moneyPool;


import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "MONEY_POOL_CASH_FLOW")
public class PreMoneyPoolCashFlow  implements Serializable {

  
  @Id
  private Long id;

  @Column(name = "ACTION")
  private String action;
  
  
  @Column(name = "PREVIOUS_TOTAL_RESERVED")
  private Double previousTotalReserved;
  
  
  @Column(name = "PREVIOUS_TOTAL_AVAILABLE")
  private Double previousTotalAvailable;
  
  
  @Column(name = "PREVIOUS_TOTAL_BALANCE")
  private Double previousTotalBalance;
  
  
  @Column(name = "CHANGE_RESERVATION")
  private Double changeReservation;
  
  
  @Column(name = "CHANGE_BALANCE")
  private Double changeBalance;
  
  
  @Column(name = "LAST_TOTAL_RESERVED")
  private Double lastTotalReserved;
  
  
  @Column(name = "LAST_TOTAL_AVAILABLE")
  private Double lastTotalAvailable;
  
  
  @Column(name = "LAST_TOTAL_BALANCE")
  private Double lastTotalBalance;
  
  
  @Column(name = "REF_NO")
  private String refNo;
  
  
  @Column(name = "TOPUP_CHANNEL")
  private String topupChannel;
  
  
  @Column(name = "DESCRIPTION")
  private String description;
  
  
  @Column(name = "REMARK")
  private String remark;
  
  
  @Column(name = "MODULE_CODE")
  private String moduleCode;
  
  
  @Column(name = "SERVICE_ID")
  private String serviceId;
  
  
  @Column(name = "MP_CF_REF_NO")
  private String mpCfRefNo;
  
  
  @Column(name = "CREATE_DATETIME")
  private Date createDatetime;
  
  
  @Column(name = "TYPE")
  private String type;
  
  
  @Column(name = "PARENT_ID")
  private Long parentId;
  
  
  @Column(name = "EWALLET_PARTICIPANT_ID")
  private Long ewalletParticipantId;
  
  
  @Column(name = "EWP_TX_REF_NO")
  private String ewpTxRefNo;
  
  
  @Column(name = "GEA_REF_ID")
  private String geaRefId;
  
  
  @Column(name = "SETTLEMENT_DATE")
  private String settlementDate;

  @NotFound(action= NotFoundAction.IGNORE)
  @ManyToOne
  @JoinColumn(name = "MONEY_POOL_ID")
  private PreMoneyPool moneyPool;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }


  public Double getPreviousTotalReserved() {
    return previousTotalReserved;
  }

  public void setPreviousTotalReserved(Double previousTotalReserved) {
    this.previousTotalReserved = previousTotalReserved;
  }


  public Double getPreviousTotalAvailable() {
    return previousTotalAvailable;
  }

  public void setPreviousTotalAvailable(Double previousTotalAvailable) {
    this.previousTotalAvailable = previousTotalAvailable;
  }


  public Double getPreviousTotalBalance() {
    return previousTotalBalance;
  }

  public void setPreviousTotalBalance(Double previousTotalBalance) {
    this.previousTotalBalance = previousTotalBalance;
  }


  public Double getChangeReservation() {
    return changeReservation;
  }

  public void setChangeReservation(Double changeReservation) {
    this.changeReservation = changeReservation;
  }


  public Double getChangeBalance() {
    return changeBalance;
  }

  public void setChangeBalance(Double changeBalance) {
    this.changeBalance = changeBalance;
  }


  public Double getLastTotalReserved() {
    return lastTotalReserved;
  }

  public void setLastTotalReserved(Double lastTotalReserved) {
    this.lastTotalReserved = lastTotalReserved;
  }


  public Double getLastTotalAvailable() {
    return lastTotalAvailable;
  }

  public void setLastTotalAvailable(Double lastTotalAvailable) {
    this.lastTotalAvailable = lastTotalAvailable;
  }


  public Double getLastTotalBalance() {
    return lastTotalBalance;
  }

  public void setLastTotalBalance(Double lastTotalBalance) {
    this.lastTotalBalance = lastTotalBalance;
  }


  public String getRefNo() {
    return refNo;
  }

  public void setRefNo(String refNo) {
    this.refNo = refNo;
  }


  public String getTopupChannel() {
    return topupChannel;
  }

  public void setTopupChannel(String topupChannel) {
    this.topupChannel = topupChannel;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


  public String getModuleCode() {
    return moduleCode;
  }

  public void setModuleCode(String moduleCode) {
    this.moduleCode = moduleCode;
  }


  public String getServiceId() {
    return serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }


  public String getMpCfRefNo() {
    return mpCfRefNo;
  }

  public void setMpCfRefNo(String mpCfRefNo) {
    this.mpCfRefNo = mpCfRefNo;
  }


  public Date getCreateDatetime() {
    return createDatetime;
  }

  public void setCreateDatetime(Date createDatetime) {
    this.createDatetime = createDatetime;
  }


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }


  public Long getEwalletParticipantId() {
    return ewalletParticipantId;
  }

  public void setEwalletParticipantId(Long ewalletParticipantId) {
    this.ewalletParticipantId = ewalletParticipantId;
  }


  public String getEwpTxRefNo() {
    return ewpTxRefNo;
  }

  public void setEwpTxRefNo(String ewpTxRefNo) {
    this.ewpTxRefNo = ewpTxRefNo;
  }


  public String getGeaRefId() {
    return geaRefId;
  }

  public void setGeaRefId(String geaRefId) {
    this.geaRefId = geaRefId;
  }


  public String getSettlementDate() {
    return settlementDate;
  }

  public void setSettlementDate(String settlementDate) {
    this.settlementDate = settlementDate;
  }

  public PreMoneyPool getMoneyPool() {
    return moneyPool;
  }

  public void setMoneyPool(PreMoneyPool moneyPool) {
    this.moneyPool = moneyPool;
  }

}
