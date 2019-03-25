package com.tng.portal.common.vo.wfl.response;

/**
 * Created by Owen on 2016/11/8.
 */
public class TransitionInstanceListVo {
    private Long formInstanceId;

    public TransitionInstanceListVo(){

    }

    public TransitionInstanceListVo(Long formInstanceId){
        this.formInstanceId = formInstanceId;
    }

    public Long getFormInstanceId() {
        return formInstanceId;
    }

    public void setFormInstanceId(Long formInstanceId) {
        this.formInstanceId = formInstanceId;
    }


}
