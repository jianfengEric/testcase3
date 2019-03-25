package com.tng.portal.common.dto.ewp;

import java.io.Serializable;

/**
 * Created by Jimmy on 2018/8/30.
 */
public class DisputeContactDto implements Serializable {

    private String ewpFormId;
    private String roleName;
    private String departmentName;
    private String nameEn;
    private String nameNls;
    private String contactPersonType;
    private String email;
    private String phoneOffice;
    private String phoneMobile;
    private Boolean mobileSms;
    private MaterialDto materialDto;

    public String getEwpFormId() {
        return ewpFormId;
    }

    public void setEwpFormId(String ewpFormId) {
        this.ewpFormId = ewpFormId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameNls() {
        return nameNls;
    }

    public void setNameNls(String nameNls) {
        this.nameNls = nameNls;
    }

    public String getContactPersonType() {
        return contactPersonType;
    }

    public void setContactPersonType(String contactPersonType) {
        this.contactPersonType = contactPersonType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneOffice() {
        return phoneOffice;
    }

    public void setPhoneOffice(String phoneOffice) {
        this.phoneOffice = phoneOffice;
    }

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public Boolean getMobileSms() {
        return mobileSms;
    }

    public void setMobileSms(Boolean mobileSms) {
        this.mobileSms = mobileSms;
    }

    public MaterialDto getMaterialDto() {
        return materialDto;
    }

    public void setMaterialDto(MaterialDto materialDto) {
        this.materialDto = materialDto;
    }
}
