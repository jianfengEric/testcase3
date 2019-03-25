package com.gea.portal.order.entity.production.order;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "EWALLET_PARTICIPANT")
public class ProEwalletParticipant implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ID")
  private Long id;
  
  
  @Column(name = "CODE")
  private String code;
  
  
  @Column(name = "NAME")
  private String name;
  
  
  @Column(name = "API_KEY")
  private String apiKey;
  
  
  @Column(name = "SECRET_KEY")
  private String secretKey;
  
  
  @Column(name = "ACTIVE")
  private Long active;
  
  
  @Column(name = "API_GATEWAY")
  private String apiGateway;
  
  
  @Column(name = "REF_ID")
  private String refId;
  
  
  @Column(name = "FULL_COMPANY_NAME")
  private String fullCompanyName;
  
  
  @Column(name = "ADDRESS")
  private String address;
  
  
/*  @Column(name = "ROLLING_SETTLEMENT_TIME")
  private String rollingSettlementTime;
  
  
  @Column(name = "SETTLEMENT_DATE_ALIGN")
  private Long settlementDateAlign;*/


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }


  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }


  public Long getActive() {
    return active;
  }

  public void setActive(Long active) {
    this.active = active;
  }


  public String getApiGateway() {
    return apiGateway;
  }

  public void setApiGateway(String apiGateway) {
    this.apiGateway = apiGateway;
  }


  public String getRefId() {
    return refId;
  }

  public void setRefId(String refId) {
    this.refId = refId;
  }


  public String getFullCompanyName() {
    return fullCompanyName;
  }

  public void setFullCompanyName(String fullCompanyName) {
    this.fullCompanyName = fullCompanyName;
  }


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }


}
