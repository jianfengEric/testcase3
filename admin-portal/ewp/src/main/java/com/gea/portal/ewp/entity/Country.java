package com.gea.portal.ewp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the country database table.
 * 
 */
@Entity
@Table(name="country")
public class Country implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="name_en")
	private String nameEn;
	
	@Column(name="name_zh_hk")
	private String nameZhHk;
	
	@Column(name="name_zh_cn")
	private String nameZhCn;
	
	@Column(name="country_code_alpha_2")
	private String countryCodeAlpha2;
	
	@Column(name="country_code_alpha_3")
	private String countryCodeQlpha3;
	
	@Column(name="dialing_code")
	private String dialingCode;
	
	@Column(name="currency_code")
	private String currencyCode;
	
	@Column(name="nationality")
	private String nationality;
	
	@Column(name="nationality_code")
	private String nationalityCode;
	
	@Column(name="currency_dp")
	private Integer currencyDp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameZhHk() {
		return nameZhHk;
	}

	public void setNameZhHk(String nameZhHk) {
		this.nameZhHk = nameZhHk;
	}

	public String getNameZhCn() {
		return nameZhCn;
	}

	public void setNameZhCn(String nameZhCn) {
		this.nameZhCn = nameZhCn;
	}

	public String getCountryCodeAlpha2() {
		return countryCodeAlpha2;
	}

	public void setCountryCodeAlpha2(String countryCodeAlpha2) {
		this.countryCodeAlpha2 = countryCodeAlpha2;
	}

	public String getCountryCodeQlpha3() {
		return countryCodeQlpha3;
	}

	public void setCountryCodeQlpha3(String countryCodeQlpha3) {
		this.countryCodeQlpha3 = countryCodeQlpha3;
	}

	public String getDialingCode() {
		return dialingCode;
	}

	public void setDialingCode(String dialingCode) {
		this.dialingCode = dialingCode;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getNationalityCode() {
		return nationalityCode;
	}

	public void setNationalityCode(String nationalityCode) {
		this.nationalityCode = nationalityCode;
	}

	public Integer getCurrencyDp() {
		return currencyDp;
	}

	public void setCurrencyDp(Integer currencyDp) {
		this.currencyDp = currencyDp;
	}

}