package com.tng.portal.sms.server.constant;

/**
 * Created by Zero on 2016/11/24.
 */
public interface SystemMsg {
    String getCode();
    Integer getErrorCode();
    enum ServerErrorMsg implements SystemMsg {
    	SERVER_ERROR("106000"),
    	SMS_SERVICE_APPLICATION_NOT_EXISTS_ERROR("106001");

        private String code;

        @Override
        public String getCode() {
            return this.code;
        }
        @Override
        public Integer getErrorCode() {
            return Integer.valueOf(this.code);
        }
        private ServerErrorMsg(String code){
            this.code = code;
        }
    }
    
}
