package com.tng.portal.email.exception;

/**
 * Created by Owen on 2017/10/23.
 */
public enum ErrorCode {
    EMAIL_ACCOUNT_NOT_EXIST_ERROR(103001),
    EMAIL_GATEWAY_NOT_EXIST_ERROR(103002),
    EMAIL_ATTACHMENTS_MAX_SIZE_ERROR(103003),
    INVALID_PARAMETER(103004),
    EMAIL_SEND_ERROR(103005)
    ;

    private Integer code;

    ErrorCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
