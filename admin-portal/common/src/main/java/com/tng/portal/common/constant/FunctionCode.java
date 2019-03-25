package com.tng.portal.common.constant;

/**
 *  Corresponding ANA_FUNCTION Interior Code field 
 */
public class FunctionCode {
	
	private FunctionCode(){}

    /**
     * ANA,Account management
     */
    public static final String USER_ACCOUNT="USER_ACCOUNT";
    /**
     * MAM,User Department
     */
    public static final String USER_DEPARTMENT="USER_DEPARTMENT";
    /**
     * ANA,Permission management
     */
    public static final String USER_FUNCTION="USER_FUNCTION";
    /**
     * ANA,Function Management
     */
    public static final String USER_PERMISSION="USER_PERMISSION";
    /**
     * MAM,Merchant management( Corresponding interface Merchant List)
     */
    public static final String MERCHANT="MERCHANT";
    /**
     * MAM,Merchent account management (corresponding to the interface) Merchant Management The database is built in the table. MERCHENT ) 
     */
    public static final String MERCHENT_ACCOUNT="MERCHENT_ACCOUNT";
    /**
     * MAM,Job process
     */
    public static final String JOB_PROCESS="JOB_PROCESS";
    /**
     * ANA,ROLE management
     */
    public static final String USER_ROLE="USER_ROLE";
    /**
     * MAM,Merchent management account without sso
     */
    public static final String ACCOUNTS_WITHOUT_SSO="ACCOUNTS_WITHOUT_SSO";

}
