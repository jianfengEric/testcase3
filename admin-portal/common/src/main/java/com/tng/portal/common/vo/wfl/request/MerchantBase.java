package com.tng.portal.common.vo.wfl.request;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@MappedSuperclass
public class MerchantBase {
	
	@Column(name = "MERCHANT_TYPE", length=10, nullable = false)
	protected String merchantType;

	@Column(name = "REGISTER_NAME_EN", length=500)
	protected String registerNameEN;

	@Column(name = "REGISTER_NAME_NLS", length=500)
	protected String registerNameNLS;
	
	@Column(name = "TRADING_NAME_EN", length=500)
	protected String tradingNameEN;

	@Column(name = "TRADING_NAME_NLS", length=500)
	protected String tradingNameNLS;
	
	@Temporal(TemporalType.DATE) 
	@Column(name = "REGISTRATION_DATE")
	protected Date registrationDate;
	
	@Column(name = "REGISTRATION_PLACE", length=200)
	protected String registrationPlace;
	
	@Column(name = "REGISTERED_ADDR_LINE1", length=200, nullable = false)
	protected String registeredAddrLine1;
	
	@Column(name = "REGISTERED_ADDR_LINE2", length=200, nullable = false)
	protected String registeredAddrLine2;
	
	@Column(name = "REGISTERED_ADDR_LINE3", length=200, nullable = false)
	protected String registeredAddrLine3;
	
	@Column(name = "BUSINESS_ADDR_LINE1", length=200, nullable = false)
	protected String businessAddrLine1;
	
	@Column(name = "BUSINESS_ADDR_LINE2", length=200, nullable = false)
	protected String businessAddrLine2;
	
	@Column(name = "BUSINESS_ADDR_LINE3", length=200, nullable = false)
	protected String businessAddrLine3;
	
	@Column(name = "COMPANY_EMAIL", length=200)
	protected String companyEmail;
	
	@Column(name = "REGISTRATION_DOC", length=4000)
	protected String registrationDoc;
	
	@Column(name = "LISTED_COMPANY_NO", length=100)
	protected String listedCompanyNo;
	
	@Column(name = "LISTED_PLACE", length=200)
	protected String listedPlace;
	
	@Column(name = "INDUSTRY", length=100)
	protected String industry;
	
	@Column(name = "PRODUCTS_SERVICES", length=200)
	protected String productsOrServices;
	
	@Column(name = "ANNUAL_TURNOVER", length=2)
	protected String annualTurnover;
	
	@Column(name = "EXPECT_FUND_SOURCE", length=200)
	protected String expectFundSource;
	
	@Column(name = "COUNTRY_OF_FUND_SOURCE", length=200)
	protected String countryOfFundSource;
	
	@Column(name = "ORGANISATION_CHART_PATH", length=500)
	protected String organisationChartPath;
	
	@Column(name = "WALLET_RELATED_MATERIAL", length=2000)
	protected String walletRelatedMaterial;

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}

	public String getRegisterNameEN() {
		return registerNameEN;
	}

	public void setRegisterNameEN(String registerNameEN) {
		this.registerNameEN = registerNameEN;
	}

	public String getRegisterNameNLS() {
		return registerNameNLS;
	}

	public void setRegisterNameNLS(String registerNameNLS) {
		this.registerNameNLS = registerNameNLS;
	}

	public String getTradingNameEN() {
		return tradingNameEN;
	}

	public void setTradingNameEN(String tradingNameEN) {
		this.tradingNameEN = tradingNameEN;
	}

	public String getTradingNameNLS() {
		return tradingNameNLS;
	}

	public void setTradingNameNLS(String tradingNameNLS) {
		this.tradingNameNLS = tradingNameNLS;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getRegistrationPlace() {
		return registrationPlace;
	}

	public void setRegistrationPlace(String registrationPlace) {
		this.registrationPlace = registrationPlace;
	}

	public String getRegisteredAddrLine1() {
		return registeredAddrLine1;
	}

	public void setRegisteredAddrLine1(String registeredAddrLine1) {
		this.registeredAddrLine1 = registeredAddrLine1;
	}

	public String getRegisteredAddrLine2() {
		return registeredAddrLine2;
	}

	public void setRegisteredAddrLine2(String registeredAddrLine2) {
		this.registeredAddrLine2 = registeredAddrLine2;
	}

	public String getRegisteredAddrLine3() {
		return registeredAddrLine3;
	}

	public void setRegisteredAddrLine3(String registeredAddrLine3) {
		this.registeredAddrLine3 = registeredAddrLine3;
	}

	public String getBusinessAddrLine1() {
		return businessAddrLine1;
	}

	public void setBusinessAddrLine1(String businessAddrLine1) {
		this.businessAddrLine1 = businessAddrLine1;
	}

	public String getBusinessAddrLine2() {
		return businessAddrLine2;
	}

	public void setBusinessAddrLine2(String businessAddrLine2) {
		this.businessAddrLine2 = businessAddrLine2;
	}

	public String getBusinessAddrLine3() {
		return businessAddrLine3;
	}

	public void setBusinessAddrLine3(String businessAddrLine3) {
		this.businessAddrLine3 = businessAddrLine3;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getRegistrationDoc() {
		return registrationDoc;
	}

	public void setRegistrationDoc(String registrationDoc) {
		this.registrationDoc = registrationDoc;
	}

	public String getListedCompanyNo() {
		return listedCompanyNo;
	}

	public void setListedCompanyNo(String listedCompanyNo) {
		this.listedCompanyNo = listedCompanyNo;
	}

	public String getListedPlace() {
		return listedPlace;
	}

	public void setListedPlace(String listedPlace) {
		this.listedPlace = listedPlace;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getProductsOrServices() {
		return productsOrServices;
	}

	public void setProductsOrServices(String productsOrServices) {
		this.productsOrServices = productsOrServices;
	}

	public String getAnnualTurnover() {
		return annualTurnover;
	}

	public void setAnnualTurnover(String annualTurnover) {
		this.annualTurnover = annualTurnover;
	}

	public String getExpectFundSource() {
		return expectFundSource;
	}

	public void setExpectFundSource(String expectFundSource) {
		this.expectFundSource = expectFundSource;
	}

	public String getCountryOfFundSource() {
		return countryOfFundSource;
	}

	public void setCountryOfFundSource(String countryOfFundSource) {
		this.countryOfFundSource = countryOfFundSource;
	}

	public String getOrganisationChartPath() {
		return organisationChartPath;
	}

	public void setOrganisationChartPath(String organisationChartPath) {
		this.organisationChartPath = organisationChartPath;
	}

	public String getWalletRelatedMaterial() {
		return walletRelatedMaterial;
	}

	public void setWalletRelatedMaterial(String walletRelatedMaterial) {
		this.walletRelatedMaterial = walletRelatedMaterial;
	}
	
}