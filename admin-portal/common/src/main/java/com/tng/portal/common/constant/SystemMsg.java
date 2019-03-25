package com.tng.portal.common.constant;

/**
 * Created by Zero on 2016/11/24.
 */
public interface SystemMsg {
	
    String getCode();
    
    int getErrorCode();
    
    
    enum LoginMsg implements SystemMsg {
        LOGIN_ACCOUNT_ERROR("102001"),
        LOGIN_PASSWORD_ERROR("102002"),
        LOGIN_MER_ERROR("102113");

        private String code;

        private LoginMsg(String code) {
            this.code = code;
        }
        @Override
        public String getCode() {
            return this.code;
        }

        @Override
        public int getErrorCode(){
            return Integer.valueOf(this.code);
        }
    }

    enum ServerErrorMsg implements SystemMsg {
        SERVER_ERROR("102000"),

        VALID_CODE_SUCCESS("102600"),
        VALID_LINK_ERROR("102022"),
        VALID_LINK_EEPIRED("102602"),
        VALID_CODE_EMAIL_SEND_FAILED("102023"),

        exist_account("102101"),
        not_exist_account("102003"),
        sure_password("102004"),
        mobile_not_available("102104"),
        original_password_error("102005"),
        empty_password_error("102006"),
        password_not_eauals_repassword("102007"),
        exist_login_id("102008"),
        exceed_external_registered_count("102009"),
        account_has_been_occupied("102008"),
        the_code_not_exists("102111"),

        exist_role("102012"),
        not_exist_role("102013"),
        delete_role_error("102014"),

        exist_function("102015"),
        not_exist_function("102016"),
        delete_function_error("102017"),

        exist_application("102401"),
        not_exist_application("102018"),

        exist_permission("102019"),
        not_exist_permission("102020"),
        delete_permission_error("102021"),

        exist_token_account("102901"),
        not_exist_token_account("102025"),
        not_available_token("102025"),
        CHECK_TOKEN_ERROR("102025"),
        ACCOUT_MID_EMPTY("109020"),
        REFRESH_TOKEN_FAILED("102025"),
        AGENT_NOT_FOUND("102029"),
        BINDING_ERROR_LOGIN_ID("109022"),
        LOGIN_ID_NOT_EXISTS("102001"),
        ACCOUNT_ALREADY_BE_BOUND("102031"),
        account_status_error("102033"),
        NOTFINDMERCHANT("102011"),
        exist_merchant_agent("102024"),
    	account_not_exist("102104"),
        account_status_not_verified("102030"),
    	FIRST_NAME_OR_LAST_NAME_NOT_EXIST("102110"),
    	ACCOUNT_HAS_BEEN_ACTIVATED("102116"),
        link_failure("102128"),
        ROLE_NAME_TOO_LONG("102129"),
        ;

        private String code;

        @Override
        public String getCode() {
            return this.code;
        }

        private ServerErrorMsg(String code){
            this.code = code;
        }

        @Override
        public int getErrorCode(){
            return Integer.valueOf(this.code);
        }
    }

    enum ErrorMsg implements SystemMsg {

        ErrorAccessingAccountInformation("102100"),
        ErrorSsoAccessingAccountInformation("102101"),
        IncorrectAccountStatus("102102"),
        ErrorUploadingParameter("102103"),
        SuccessCreateSsoAccountConnect("102105"),
        ErrorCreateSsoAccountConnect("102106"),
        AgentAccountExist("102107"),
        EnterSameMidAccountSso("102108"),
        HasSsoLinkAccount("102109"),
        UPLOAD_FILE_EMPTY("102111"),
        UPLOAD_FILE_ERROR("102112"),
        UPLOAD_FILE_FORMAT_ERROR("102114"),
        client_not_exist_account("102115"),
        CAN_NOT_BE_EMPTY("102117"),
        IS_NULL("102118"),
        UPLOAD_FILE_MULTIPART("102119"),
        UPLOAD_FILE_TYPE_ERROR("102120"),
        FILE_TYPE_ERROR("102121"),
        NO_COLUMN_FIELD_NAME("102122"),
        IS_NOT_NUMBER("102123"),
        HAVE_DUPLICATE_ROWS("102124"),
        CURRENCY_TYPE_UNAVAILABLE("102125"),
        FILES_IS_TOO_BIG("102126"),
        PLEASE_UPLOAD_CSV_FILE("102127")
        ;

        private String code;

        ErrorMsg(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return this.code;
        }

        @Override
        public int getErrorCode(){
            return Integer.valueOf(this.code);
        }

    }
    
    enum EwpErrorMsg implements SystemMsg {
    	
    	HAS_PENDING_FOR_APPROVAL("110100"),
    	NO_SERVICE("110101"),
    	INCOMPLETE_DATA("110102"),
    	NO_ACTIVATION("110103"),
    	MORE_THAN_ONCE_TO_PRODUCTION("110104"),
    	NO_API_GATEWAY("110105"),
        REPEATED_SERVICE_ASSIGNMENT("110106"),
        NOT_BASE_CURRENCY("110107"),
    	;

        private String code;

        @Override
        public String getCode() {
            return this.code;
        }
        @Override
        public int getErrorCode() {
            return Integer.valueOf(this.code);
        }

        private EwpErrorMsg(String code){
            this.code = code;
        }
    }
    
    
    enum MpErrorMsg implements SystemMsg {

    	HAS_PENDING_FOR_APPROVAL("120100"),
    	NO_DEPLOY("120101"),
    	CANONT_ADJUSTMENT("120102"),
        ;

        private String code;

        @Override
        public String getCode() {
            return this.code;
        }
        @Override
        public int getErrorCode() {
            return Integer.valueOf(this.code);
        }

        private MpErrorMsg(String code){
            this.code = code;
        }
    }
    
    enum ApvErrorMsg implements SystemMsg {

    	HAS_BEEN_APPROVED("130100")
    	;

        private String code;

        @Override
        public String getCode() {
            return this.code;
        }
        @Override
        public int getErrorCode() {
            return Integer.valueOf(this.code);
        }

        private ApvErrorMsg(String code){
            this.code = code;
        }
    }
    
}
