package com.tng.portal.ana.bean;

import com.tng.portal.ana.constant.AccountStatus;
import com.tng.portal.ana.constant.AccountType;
import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.util.AnaBeanUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zero on 2016/11/10.
 */
public class Account implements Serializable{
    private String accountId;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Boolean locked;
    private Boolean enabled;
    private List<Role> roles;
    private Boolean internal;
    private String externalGroupId;
    private AccountStatus status;
    private String userType;
    private Long merchantId;

    private String merchantType;

    public void fill(AnaAccount anaAccount){
        setAccountId(anaAccount.getId());
        setUsername(anaAccount.getAccount());
        setFirstName(anaAccount.getFirstName());
        setLastName(anaAccount.getLastName());
        setPassword(anaAccount.getPassword());
        setEmail(anaAccount.getEmail());
        List<Role> roleList = AnaBeanUtils.toRoles(anaAccount.getAnaRoles());
        setRoles(roleList);
        setInternal(anaAccount.getInternal());
        setExternalGroupId(anaAccount.getExternalGroupId());
        setStatus(AccountStatus.parse(anaAccount.getStatus()));
        setUserType(AccountType.getName(anaAccount.getUserType()));
        setMerchantId(anaAccount.getMerchantId());
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getFullName(){
        String name = "";
        if(StringUtils.isNotBlank(firstName)){
            name = firstName;
        }
        if(StringUtils.isNotBlank(lastName)){
            name += " "+lastName;
        }
        return name;
    }

}
