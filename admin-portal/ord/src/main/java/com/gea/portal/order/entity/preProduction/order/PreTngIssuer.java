package com.gea.portal.order.entity.preProduction.order;


import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "TNG_ISSUER")
public class PreTngIssuer implements Serializable {

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "DISPLAY_NAME")
  private String displayName;
  
  @Column(name = "COUNTRY_ID")
  private Double countryId;

  @Column(name = "SERVICE_ID")
  private Double serviceId;

  @Column(name = "USING_VENDOR_CODE")
  private String usingVendorCode;

  @Column(name = "CREATE_DATETIME")
  private Timestamp createDatetime;
  
  @Column(name = "UPDATE_DATETIME")
  private Timestamp updateDatetime;

  @Column(name = "IS_VALID")
  private Double isValid;
  
  @Column(name = "ENABLE")
  private Double enable;
  
  @Column(name = "ISSUER_CODE")
  private String issuerCode;

  @NotFound(action= NotFoundAction.IGNORE)
  @ManyToOne
  @JoinColumn(name = "SERVICE_ID", insertable=false, updatable=false)
  private PreService service;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }


  public Double getCountryId() {
    return countryId;
  }

  public void setCountryId(Double countryId) {
    this.countryId = countryId;
  }


  public Double getServiceId() {
    return serviceId;
  }

  public void setServiceId(Double serviceId) {
    this.serviceId = serviceId;
  }


  public String getUsingVendorCode() {
    return usingVendorCode;
  }

  public void setUsingVendorCode(String usingVendorCode) {
    this.usingVendorCode = usingVendorCode;
  }


  public java.util.Date getCreateDatetime() {
    return createDatetime;
  }

  public void setCreateDatetime(Timestamp createDatetime) {
    this.createDatetime = createDatetime;
  }


  public Timestamp getUpdateDatetime() {
    return updateDatetime;
  }

  public void setUpdateDatetime(Timestamp updateDatetime) {
    this.updateDatetime = updateDatetime;
  }


  public Double getIsValid() {
    return isValid;
  }

  public void setIsValid(Double isValid) {
    this.isValid = isValid;
  }


  public Double getEnable() {
    return enable;
  }

  public void setEnable(Double enable) {
    this.enable = enable;
  }


  public String getIssuerCode() {
    return issuerCode;
  }

  public void setIssuerCode(String issuerCode) {
    this.issuerCode = issuerCode;
  }

  public PreService getService() {
    return service;
  }

  public void setService(PreService service) {
    this.service = service;
  }
}
