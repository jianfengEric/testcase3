package com.tng.portal.common.constant;

/**
 *  Corresponding ANA_PERMISSION Table ID field 
 * <p>
 * Created by Jimmy on 2017/10/16.
 */
public class PermissionId {
	
	private PermissionId(){}

    public static final int CREATE = 1;
    public static final int VIEW = 2;
    public static final int EDIT = 4;
    public static final int DELETE = 8;
    public static final int APPROVAL = 16;
    public static final int WITHDRAWAL = 32;
    public static final int ASSIGN_USER_ROLE = 64;
    public static final int SSO_CONECTION = 128;
    public static final int CHANGE_STATUS = 256;
    public static final int RESET_PASSWORD = 512;
    public static final int RE_SEND_ACTIVATIOR_EMAIL = 1024;
    public static final int RE_SUBMIT = 2048;
    public static final int CHANGE_SALES_PERSON = 4096;
    public static final int DISCARD_DRAFT = 8192;
    public static final int REJECT = 32768;
    public static final int CANCEL = 65536;
    public static final int VIEW_ALL_MERCHANT = 131072;

}
