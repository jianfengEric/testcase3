package com.tng.portal.common.constant;

/**
 * Created by Jimmy on 2017/10/16.
 */
public class Url {
	
	//Add a private constructor to hide the implicit public one.
	private Url(){}

    /**
     *  query Merchant Id Interface 
     */
    public static final String MAM_SALES_PERSION_MERCHANT_ID = "/salesPerson/merchantId";

    /**
     *  query Permission Interface 
     */
    public static final String MAM_ROLE_MANAGER_PERMISSION = "/roleManager/permission";

    /**
     *  Inquiry non sso Account number 
     */
    public static final String ACCOUNT_WITHOUT_SSO = "/accountWithoutSso";

    /**
     *  Query other module accounts 
     */
    public static final String ACCOUNT_CLIENT_LIST = "/mamAccount/clientList";

    /**
     *  Establish ana Account number, and bind the account of other modules, create SSO
     */
    public static final String ANA_ACCOUNT_ADD_BIND_SSO = "/accountWithoutSso/addBind";




    /**
     *  Establish ANA Account, bind other module accounts. SSO The menu), 
     *  Original interface address: /mamAccount/mamAdd
     */
    public static final String REMOTE_ANA_ADD_ACCOUNT="/remote/account/addAna";
    /**
     *  Create module account 
     *  Original interface address: /mamAccount/mamClientAdd
     */
    public static final String REMOTE_ADD_ACCOUNT="/remote/account/add";
    /**
     *  query SSO Account number 
     *  Original interface address: /mamAccount
     */
    public static final String REMOTE_ACCOUNT_SSO="/remote/account/sso";
    /**
     *  Query account information 
     *  Original interface address: /oauth/username
     */
    public static final String REMOTE_ACCOUNT_INFO="/remote/account/info";
    /**
     *  Query module account by condition 
     *  Original interface address: /mamAccount/clientList
     */
    public static final String REMOTE_ACCOUNT_QUERY="/remote/account/query";
    /**
     *  Original interface address: SSO Account number 
     *  Original interface address: /accountWithoutSso
     */
    public static final String REMOTE_ACCOUNT_WITHOUT_SSO = "/remote/account/withoutSso";
    /**
     *  Original interface address: ANA Account, bind other module accounts (non SSO Menu) 
     *  Original interface address: /accountWithoutSso/addBind
     */
    public static final String REMOTE_ANA_ADD_BIND_ACCOUNT = "/remote/account/addAnaBind";
    /**
     *  Original interface address: ANA Account, bind other module accounts (non SSO Menu) 
     *  Original interface address: /mamAccount/bindingAccounts
     */
    public static final String REMOTE_ACCOUNT_QUERY_BIND = "/remote/account/queryBind";
    /**
     *  Edit local account 
     *  Original interface address: /mamAccount/editLocalUser
     */
    public static final String REMOTE_ACCOUNT_EDIT_LOCAL = "/remote/account/editLocal";
    /**
     *  Resend mail 
     *  Original interface address: /mamAccount/reSendEmail
     */
    public static final String REMOTE_ACCOUNT_EMAIL = "/remote/account/email";
    /**
     *  Edit account number 
     *  Original interface address: /mamAccount/editUser
     */
    public static final String REMOTE_ACCOUNT_EDIT = "/remote/account/edit";
    /**
     *  Update account status 
     *  Original interface address: /mamAccount/updateStatus
     */
    public static final String REMOTE_ACCOUNT_UPDATE_STATUS = "/remote/account/updateStatus";
    /**
     *  Bind account 
     *  Original interface address: /mamAccount/mamBindingAccount
     */
    public static final String REMOTE_ACCOUNT_BIND = "/remote/account/bind";
    /**
     *  query SSO account information 
     *  Original interface address: /mamAccount/ssoAccount
     */
    public static final String REMOTE_ACCOUNT_SSO_INFO = "/remote/account/ssoInfo";
    /**
     *  Update local account status 
     *  Original interface address: /mamAccount/updateLocalStatus
     */
    public static final String REMOTE_ACCOUNT_UPDATE_LOCAL_STATUS = "/remote/account/updateLocalStatus";

}
