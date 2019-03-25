package com.tng.portal.common.vo.wfl.response;

/**
 * Created by Dell on 2016/11/8.
 */
public class TransitionInstanceVo {
    private Long formInstanceId;
    private String actionName;
    private String description;
    private String activity;
    private String roleName;
    private String stateName;
    private String actionUserAccount;

    public Long getFormInstanceId() {
        return formInstanceId;
    }

    public void setFormInstanceId(Long formInstanceId) {
        this.formInstanceId = formInstanceId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getActionUserAccount() {
        return actionUserAccount;
    }

    public void setActionUserAccount(String actionUserAccount) {
        this.actionUserAccount = actionUserAccount;
    }
}
