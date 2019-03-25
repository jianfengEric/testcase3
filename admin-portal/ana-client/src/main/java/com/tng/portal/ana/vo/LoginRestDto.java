package com.tng.portal.ana.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tng.portal.ana.entity.AnaAccountApplication;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zero on 2016/11/14.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRestDto implements Serializable {
    private String accountId;
    private String username;
    private String accessToken;
    private Long expriesIn;
    private String phoneNumber;
    private String language;
    private List<String> roles;
    private Boolean internal;
    private String externalGroupId;
    private List<SystemUrlDto> systemUrl = new ArrayList<>();
    private boolean firstLogin;
    private String userType;
    private String merchantType;
    private String merchantStatus;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isRestPwd;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public LoginRestDto() {
    }

    public LoginRestDto(String username) {
        this.username = username;
    }

    public LoginRestDto(String username, String accessToken, Long expriesIn) {
        this.username = username;
        this.accessToken = accessToken;
        this.expriesIn = expriesIn;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpriesIn() {
        return expriesIn;
    }

    public void setExpriesIn(Long expriesIn) {
        this.expriesIn = expriesIn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Boolean getInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    public String getExternalGroupId() {
        return externalGroupId;
    }

    public void setExternalGroupId(String externalGroupId) {
        this.externalGroupId = externalGroupId;
    }

    public List<SystemUrlDto> getSystemUrl() {
        return systemUrl;
    }

    public void setSystemUrl(List<AnaAccountApplication> anaAccountApplications) {
        if(CollectionUtils.isNotEmpty(anaAccountApplications)){
            for(AnaAccountApplication application:anaAccountApplications){
                if(application.getAnaApplication().getDisplay()) {
                    systemUrl.add(new SystemUrlDto(application.getAnaApplication().getName().toLowerCase(), application.getAnaApplication().getExternalEndpoint()));
                }
            }
        }
    }
    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getMerchantStatus() {
        return merchantStatus;
    }

    public void setMerchantStatus(String merchantStatus) {
        this.merchantStatus = merchantStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean getIsRestPwd() {
        return isRestPwd;
    }

    public void setIsRestPwd(boolean isRestPwd) {
        this.isRestPwd = isRestPwd;
    }
}
