package com.tng.portal.ana.vo;

import java.util.List;

/**
 * Created by Dell on 2017/8/29.
 */
public class MamAddAccountPostDto extends AddAccountPostDto{

    private String userId;

    private String mamUserId;

    private Long departmentId;

    private String applicationCode;

    private List<AnaAccountApplicationDto> bindingData;

    public List<AnaAccountApplicationDto> getBindingData() {
        return bindingData;
    }

    public void setBindingData(List<AnaAccountApplicationDto> bindingData) {
        this.bindingData = bindingData;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMamUserId() {
        return mamUserId;
    }

    public void setMamUserId(String mamUserId) {
        this.mamUserId = mamUserId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    @Override
    public String toString() {
        return "MamAddAccountPostDto{" +
                "super.toString()='" + super.toString() +
                ", userId='" + userId + '\'' +
                ", mamUserId='" + mamUserId + '\'' +
                ", departmentId=" + departmentId +
                ", applicationCode='" + applicationCode + '\'' +
                ", bindingData=" + bindingData +
                '}';
    }

}
