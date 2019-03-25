package com.gea.portal.order.entity.production.order;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "SERVICE")
public class ProService implements Serializable {

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "DESC_EN")
  private String descEn;

  @Column(name = "DESC_ZH_HK")
  private String descZhHk;

  @Column(name = "DESC_ZH_CN")
  private String descZhCn;

  @Column(name = "CREATE_DATETIME")
  private Timestamp createDatetime;

  @Column(name = "UPDATE_DATETIME")
  private Timestamp updateDatetime;

  @Column(name = "SERVICE_CODE")
  private String serviceCode;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getDescEn() {
    return descEn;
  }

  public void setDescEn(String descEn) {
    this.descEn = descEn;
  }


  public String getDescZhHk() {
    return descZhHk;
  }

  public void setDescZhHk(String descZhHk) {
    this.descZhHk = descZhHk;
  }


  public String getDescZhCn() {
    return descZhCn;
  }

  public void setDescZhCn(String descZhCn) {
    this.descZhCn = descZhCn;
  }


  public Timestamp getCreateDatetime() {
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


  public String getServiceCode() {
    return serviceCode;
  }

  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }

}
