package com.tng.portal.sms.constant;

/**
 * Created by Zero on 2016/11/24.
 */
public interface SystemMsg {
    String getCode();
    Integer getErrorCode();
    enum ServerErrorMsg implements SystemMsg {
    	SERVER_ERROR("105000"),
    	QUESTION_TYPE_EXISTS_ERROR("105001"),
    	QUESTION_TEMPLATE_EXISTS_ERROR("105002"),
    	NOT_EXISTS_QUESTION_TYPE("105003"),
    	NOT_EXISTS_MERGE_QUESTION_TYPE("105004"),
    	NOT_EXISTS_LANGUAGE("105005"),
    	LANGUAGE_EXISTS_ERROR("105006"),
    	SEQUENCE_EXISTS_ERROR("105007"),
    	NULL_FILE_ERROR("105008"),
    	UPLOAD_FILE_ERROR("105009"),
    	CSV_FILE_PARSE_ERROR("105010"),
    	MOBILE_NUMBER_REQUIRED("105011"),
    	MOBILE_NUMBER_EXCEEDING("105012"),
    	REPEATED_MOBILE_NUMBER("105013"),
    	INVALID_FILE_FORMAT("105014"),
    	INVALID_MOBILE_INPUT_FORMAT("105015");

        private String code;

        @Override
        public String getCode() {
            return this.code;
        }

        private ServerErrorMsg(String code){
            this.code = code;
        }
        @Override
        public Integer getErrorCode() {
            return Integer.valueOf(this.code);
        }
    }
    
    enum SMSServerErrorMsg implements SystemMsg {
    	SMS_SERVER_CONNECT_ERROR("105100"),
    	SMS_SERVER_RETURN_ERROR("105101"),
    	JOB_ALREADY_COMPLETED("105102");

        private String code;

        @Override
        public String getCode() {
            return this.code;
        }
        @Override
        public Integer getErrorCode() {
            return Integer.valueOf(this.code);
        }

        private SMSServerErrorMsg(String code){
            this.code = code;
        }
    }
    
}
