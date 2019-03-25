package com.tng.portal.common.vo.wfl.response;

/**
 * Created by Owen on 2016/11/30.
 */
public class StateListVo {
    private Long currentStateId;
    private String currentStateName;
    private Long nextStateId;
    private String actionName;

    public Long getCurrentStateId() {
        return currentStateId;
    }

    public void setCurrentStateId(Long currentStateId) {
        this.currentStateId = currentStateId;
    }

    public String getCurrentStateName() {
        return currentStateName;
    }

    public void setCurrentStateName(String currentStateName) {
        this.currentStateName = currentStateName;
    }

    public Long getNextStateId() {
        return nextStateId;
    }

    public void setNextStateId(Long nextStateId) {
        this.nextStateId = nextStateId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
