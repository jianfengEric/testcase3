package com.gea.portal.dpy.util;

import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.ServiceName;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GEA Module Mapping util
 *
 */
public class GeaModuleMappingUtil {

	private static Map<String,String> geaApiMap = null;

	private static Map<String,String> geaModuleMap=null;
	
	private static Map<String,String> geaServerMap = null;

	private static int maxRetryCount = 3;
	
	private static String apiKey;

	static {
		geaApiMap = new HashMap<>();
		geaApiMap.put("PROD_MTH_SYNC", PropertiesUtil.getAppValueByKey("gea.api.PROD.MTH.SYNC"));
		geaApiMap.put("PROD_MTH_GET", PropertiesUtil.getAppValueByKey("gea.api.PROD.MTH.GET"));
		geaApiMap.put("PROD_MP_SYNC", PropertiesUtil.getAppValueByKey("gea.api.PROD.MP.SYNC"));
		geaApiMap.put("PROD_MP_GET", PropertiesUtil.getAppValueByKey("gea.api.PROD.MP.GET"));
		geaApiMap.put("PROD_MP_ADJUSTMENT", PropertiesUtil.getAppValueByKey("gea.api.PROD.MP.ADJUSTMENT"));
		geaApiMap.put("PROD_MS_SYNC", PropertiesUtil.getAppValueByKey("gea.api.PROD.MS.SYNC"));
		geaApiMap.put("PROD_MS_GET", PropertiesUtil.getAppValueByKey("gea.api.PROD.MS.GET"));
		geaApiMap.put("PROD_RATE_ENGINE_SYNC", PropertiesUtil.getAppValueByKey("gea.api.PROD.RATE_ENGINE.SYNC"));
		geaApiMap.put("PROD_RATE_ENGINE_GET", PropertiesUtil.getAppValueByKey("gea.api.PROD.RATE_ENGINE.GET"));
		geaApiMap.put("PROD_RATE_ENGINE_MARKUP", PropertiesUtil.getAppValueByKey("gea.api.PROD.RATE_ENGINE.MARKUP"));
		geaApiMap.put("PROD_RATE_ENGINE_RATE", PropertiesUtil.getAppValueByKey("gea.api.PROD.RATE_ENGINE.RATE"));
		geaApiMap.put("PROD_RATE_ENGINE_GETRATE", PropertiesUtil.getAppValueByKey("gea.api.PROD.RATE_ENGINE.GETRATE"));
		geaApiMap.put("PROD_SS_SYNC", PropertiesUtil.getAppValueByKey("gea.api.PROD.SS.SYNC"));
		geaApiMap.put("PROD_SS_GET", PropertiesUtil.getAppValueByKey("gea.api.PROD.SS.GET"));
		geaApiMap.put("PROD_NS_SYNC", PropertiesUtil.getAppValueByKey("gea.api.PROD.NS.SYNC"));
		geaApiMap.put("PROD_NS_GET", PropertiesUtil.getAppValueByKey("gea.api.PROD.NS.GET"));
		
		geaApiMap.put("PRE_MTH_SYNC", PropertiesUtil.getAppValueByKey("gea.api.PRE.MTH.SYNC"));
		geaApiMap.put("PRE_MTH_GET", PropertiesUtil.getAppValueByKey("gea.api.PRE.MTH.GET"));
		geaApiMap.put("PRE_MP_SYNC", PropertiesUtil.getAppValueByKey("gea.api.PRE.MP.SYNC"));
		geaApiMap.put("PRE_MP_GET", PropertiesUtil.getAppValueByKey("gea.api.PRE.MP.GET"));
		geaApiMap.put("PRE_MP_ADJUSTMENT", PropertiesUtil.getAppValueByKey("gea.api.PRE.MP.ADJUSTMENT"));
		geaApiMap.put("PRE_MS_SYNC", PropertiesUtil.getAppValueByKey("gea.api.PRE.MS.SYNC"));
		geaApiMap.put("PRE_MS_GET", PropertiesUtil.getAppValueByKey("gea.api.PRE.MS.GET"));
		geaApiMap.put("PRE_RATE_ENGINE_SYNC", PropertiesUtil.getAppValueByKey("gea.api.PRE.RATE_ENGINE.SYNC"));
		geaApiMap.put("PRE_RATE_ENGINE_GET", PropertiesUtil.getAppValueByKey("gea.api.PRE.RATE_ENGINE.GET"));
		geaApiMap.put("PRE_RATE_ENGINE_MARKUP", PropertiesUtil.getAppValueByKey("gea.api.PRE.RATE_ENGINE.MARKUP"));
		geaApiMap.put("PRE_RATE_ENGINE_RATE", PropertiesUtil.getAppValueByKey("gea.api.PRE.RATE_ENGINE.RATE"));
		geaApiMap.put("PRE_RATE_ENGINE_GETRATE", PropertiesUtil.getAppValueByKey("gea.api.PRE.RATE_ENGINE.GETRATE"));
		geaApiMap.put("PRE_SS_SYNC", PropertiesUtil.getAppValueByKey("gea.api.PRE.SS.SYNC"));
		geaApiMap.put("PRE_SS_GET", PropertiesUtil.getAppValueByKey("gea.api.PRE.SS.GET"));
		geaApiMap.put("PRE_NS_SYNC", PropertiesUtil.getAppValueByKey("gea.api.PRE.NS.SYNC"));
		geaApiMap.put("PRE_NS_GET", PropertiesUtil.getAppValueByKey("gea.api.PRE.NS.GET"));
		
		geaModuleMap = new HashMap<>();
		geaModuleMap.put("participant", PropertiesUtil.getAppValueByKey("GEA.module.mapping.participant"));
		geaModuleMap.put("adjustment", PropertiesUtil.getAppValueByKey("GEA.module.mapping.adjustment"));
		geaModuleMap.put("deposit", PropertiesUtil.getAppValueByKey("GEA.module.mapping.deposit"));
		geaModuleMap.put("cashout", PropertiesUtil.getAppValueByKey("GEA.module.mapping.cashout"));
		geaModuleMap.put("serviceMarkup", PropertiesUtil.getAppValueByKey("GEA.module.mapping.serviceMarkup"));
		geaModuleMap.put("exchangeRate", PropertiesUtil.getAppValueByKey("GEA.module.mapping.exchangeRate"));
		
		geaServerMap = new HashMap<>();
		geaServerMap.put("MTH", PropertiesUtil.getAppValueByKey("GEA.server.mapping.MTH"));
		geaServerMap.put("MP", PropertiesUtil.getAppValueByKey("GEA.server.mapping.MP"));
		geaServerMap.put("MS", PropertiesUtil.getAppValueByKey("GEA.server.mapping.MS"));
		geaServerMap.put("RATE_ENGINE", PropertiesUtil.getAppValueByKey("GEA.server.mapping.RATE_ENGINE"));
		geaServerMap.put("SS", PropertiesUtil.getAppValueByKey("GEA.server.mapping.SS"));
		geaServerMap.put("NS", PropertiesUtil.getAppValueByKey("GEA.server.mapping.NS"));

		apiKey = PropertiesUtil.getAppValueByKey("gea.api.api_key");
		maxRetryCount = Integer.parseInt(PropertiesUtil.getAppValueByKey("gea.api.maxRetryCount"));

	}

	/**
	 * get api url
	 */
	public static String getApiUrl(String key,Instance instance,String fun){
		if(instance == Instance.PROD){
			key = "PROD_"+key;
		}else{
			key="PRE_"+key;
		}
		key = key+"_"+fun;
		String url = geaApiMap.get(key);
		if(StringUtils.isBlank(url)){
			throw new BusinessException(" Nonexistent key");
		}
		return url;
	}

	/**
	 *  Take the interface address of the map. 
	 */
	public static Map<String,String> getGEAModuleMap(String key,Instance instance,String fun){
		String mapStr = geaModuleMap.get(key);
		if(StringUtils.isBlank(mapStr)){
			throw new BusinessException(" Nonexistent key");
		}
		String[] mapStrArr = mapStr.split(",");
		Map<String,String> map = new HashMap<>();
		for(String item : mapStrArr){
			map.put(item, getApiUrl(item,instance,fun));
		}

		return map;
	}
	
	public static List<ServiceName> getGEAServerMap(GeaServiceName geaServiceName){
		List<ServiceName> list = new ArrayList<>();
		String[] serviceArr = geaServerMap.get(geaServiceName.getSortName()).split(",");
		for(String item : serviceArr){
			ServiceName sn = ServiceName.findByValue(item);
			list.add(sn);
		}
		
		return list;
	}
	
	public static List<ServiceName> getGEAServerMap(String geaServiceName){
		return getGEAServerMap(GeaServiceName.findByValue(geaServiceName));
	}

	/**
	 *  Maximum retransmission times 
	 */
	public static int getMaxRetryCount() {
		return maxRetryCount;
	}

	public static String getApiKey() {
		return apiKey;
	}
}
