package com.tng.portal.sms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "SMS_SERVICE_PROVIDER")
@JsonInclude(Include.NON_NULL)
public class SMSServiceProvider {
	@Id
	@Column(name="CODE", nullable=false, unique=true)
	private String code;
	
	@Column(name="NAME", unique=true, nullable=false, length=100)
	private String name;
	
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
	
}
