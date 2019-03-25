package com.tng.portal.sms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "TEST_MOBILE_NUMBER")
@JsonInclude(Include.NON_NULL)
public class TestMobileNumber {
	@Id
	@Column(name="ID", unique=true, nullable=false)
	@GeneratedValue(generator="seq_test_mobile_number", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="seq_test_mobile_number", sequenceName="seq_test_mobile_number", allocationSize=1)
	private Long id;
	
	@Column(name="MOBILE_NUMBERS", nullable=false, length=1000)
	private String mobileNumbers;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMobileNumbers() {
		return mobileNumbers;
	}
	public void setMobileNumbers(String mobileNumbers) {
		this.mobileNumbers = mobileNumbers;
	}
}
