package com.gea.portal.ewp.dto;

import java.io.Serializable;


/**
 * The persistent class for the country database table.
 * 
 */
public class CountryDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String nameEn;
	
	private String nameZhHk;
	
	private String nameZhCn;
	
	private String countryCodeAlpha2;
	
	private String countryCodeQlpha3;
	
	private String dialingCode;
	
	private String currencyCode;
	
	private String nationality;
	
	private String nationalityCode;
	
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