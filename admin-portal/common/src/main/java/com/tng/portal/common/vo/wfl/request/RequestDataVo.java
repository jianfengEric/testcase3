package com.tng.portal.common.vo.wfl.request;

import java.util.List;

/**
 * Created by Owen on 2016/11/18.
 */
public class RequestDataVo {
    private String processName;
    private String accountNo;
    private String roleName;
    private String ownerName;// Founder 
    private String actionName;
    private String remark;
    private Long processInstanceId;
    private Long formInstanceId;

    private List<RequestFormDataVo> requestFormDataList;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Long getFormInstanceId() {
        return formInstanceId;
    }

    public void setFormInstanceId(Long formInstanceId) {
        this.formInstanceId = formInstanceId;
    }

    public List<RequestFormDataVo> getRequestFormDataList() {
        return requestFormDataList;
    }

    public void setRequestFormDataList(List<RequestFormDataVo> requestFormDataList) {
        this.requestFormDataList = requestFormDataList;
    }

    @Override
    public String toString() {
        return "RequestDataVo{" +
                "processName='" + processName + '\'' +
                ", accountNo='" + accountNo + '\'' +
                ", roleName='" + roleName + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", actionName='" + actionName + '\'' +
                ", remark='" + remark + '\'' +
                ", processInstanceId=" + processInstanceId +
                ", formInstanceId=" + formInstanceId +
                ", requestFormDataList=" + requestFormDataList +
                '}';
    }
}
