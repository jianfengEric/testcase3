package com.gea.portal.order.entity.production.moneyPool;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "MONEY_POOL")
public class ProMoneyPool  implements Serializable {


  @Id
  private Long id;


  @Column(name = "REF_ID")
  private String refId;


  @Column(name = "EWALLET_PARTICIPANT_ID")
  private Long ewalletParticipantId;


  @Column(name = "DESCRIPTION")
  private String description;


  @Column(name = "CURRENCY_TYPE")
  private String currencyType;


  @Column(name = "STATUS")
  private String status;


  @Column(name = "CREATE_DATETIME")
  private Date createDatetime;


  @Column(name = "ALTER_LINE")
  private Double alterLine;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getRefId() {
    return refId;
  }

  public void setRefId(String refId) {
    this.refId = refId;
  }


  public Long getEwalletParticipantId() {
    return ewalletParticipantId;
  }

  public void setEwalletParticipantId(Long ewalletParticipantId) {
    this.ewalletParticipantId = ewalletParticipantId;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public String getCurrencyType() {
    return currencyType;
  }

  public void setCurrencyType(String currencyType) {
    this.currencyType = currencyType;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public Date getCreateDatetime() {
    return createDatetime;
  }

  public void setCreateDatetime(Date createDatetime) {
    this.createDatetime = createDatetime;
  }


  public Double getAlterLine() {
    return alterLine;
  }

  public void setAlterLine(Double alterLine) {
    this.alterLine = alterLine;
  }

}
