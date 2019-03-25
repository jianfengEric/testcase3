package com.tng.portal.common.util;

import org.apache.commons.lang.StringUtils;

/***
 * 
 * @author christ
 * @time 2016-07-11
 * @desc the thrid call api logger
 *
 */
public class Log4jFormatUtil {

	private static final String IDENFYING="||";
	private static final String SUCCESS="success";
	private static final String SEND="send";
	private static final String FAIL="failure";
	private static final String REQUEST="request: ";
	private static final String RESPONSE="response: ";
	
	/***
	 * //TOMCATID||SUCCESS||requestUrl||METHOD||INFO||
	 * request:xx
	 * response:xxx
	 * @param requestUrl
	 * @param responseMsg
	 * @return
	 */
	/***
	 * 
	 * @param requestUrl
	 * @param request
	 * @param method
	 * @param responseMsg
	 * @return
	 */
	public static String logInfoMessage(String requestUrl,String request,String method,
			String responseMsg) {
		StringBuilder loggDer = new StringBuilder();
		if(StringUtils.isNotEmpty(request)&&null==responseMsg)
		{
			loggDer.append(SEND);
		}else{
			loggDer.append(SUCCESS);	
		}
		loggDer.append(IDENFYING);
		if(StringUtils.isNotEmpty(requestUrl)){
			loggDer.append(requestUrl);
			loggDer.append(IDENFYING);
		}
		if(StringUtils.isNotEmpty(method)){
			loggDer.append(method);
			loggDer.append(IDENFYING);
		}
		if(StringUtils.isNotEmpty(request)){
			loggDer.append(REQUEST+request);
			loggDer.append(IDENFYING);
		}
		if(StringUtils.isNotEmpty(responseMsg)){
			loggDer.append(RESPONSE+responseMsg);
		}
		return loggDer.toString();
	}
	/*** //TOMCATID||SUCCESS||requestUrl||METHOD||ERROR
	 * request:
	 * response:
	 * exception:
	 * @param requestUrl 
	 * @param request
	 * @param method
	 * @param responseMsg
	 * @param exception
	 * @return
	 */
	public static String logErrorMessage(String requestUrl,String request,String method,String responseMsg,String exception) {
		StringBuilder loggDer = new StringBuilder();
		loggDer.append(FAIL);
		loggDer.append(IDENFYING);
		if(StringUtils.isNotEmpty(requestUrl)){
			loggDer.append(requestUrl);
			loggDer.append(IDENFYING);
		}
		if(StringUtils.isNotEmpty(method)){
			loggDer.append(method);
			loggDer.append(IDENFYING);
		}
		if(StringUtils.isNotEmpty(request)){
			loggDer.append(REQUEST+request);
			loggDer.append(IDENFYING);
		}
		if(StringUtils.isNotEmpty(responseMsg)){
			loggDer.append(RESPONSE+responseMsg);
			loggDer.append(IDENFYING);
		}
		if(StringUtils.isNotEmpty(exception)){
			loggDer.append(exception);
		}
		return loggDer.toString();
	}

}
