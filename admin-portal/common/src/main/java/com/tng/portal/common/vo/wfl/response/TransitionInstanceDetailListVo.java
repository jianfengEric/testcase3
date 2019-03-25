package com.tng.portal.common.vo.wfl.response;

/**
 * Created by Owen on 2016/11/8.
 */
public class TransitionInstanceDetailListVo {
    private Long formInstanceId;
    private String actionName;

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
}
