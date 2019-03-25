package com.tng.portal.common.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


public class MethodParameters {
	private static final Logger logger = LoggerFactory.getLogger(MethodParameters.class);
	@Expose
	private String methodName;
	@Expose
	private String[] parameterTypes;
	@Expose
	private Object[] parameters;
	private static Gson gson = new Gson();
	
	public String toJson()
	{
		return gson.toJson(this, MethodParameters.class);
	}
	
	public static MethodParameters fromJson(String json)
	{
		MethodParameters methodParams = new MethodParameters();
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(json).getAsJsonObject();
		methodParams.setMethodName(obj.get("methodName").getAsString());
		JsonArray array = obj.get("parameterTypes").getAsJsonArray();
		String[] pTypes = new String[array.size()];
		for(int i = 0; i < pTypes.length; i++)
		{
			pTypes[i] = gson.fromJson(array.get(i), String.class);
		}
		methodParams.parameterTypes = pTypes;
		Class<?>[] paramTypes = methodParams.getParameterTypes();
		array = obj.get("parameters").getAsJsonArray();
		Object[] params = new Object[array.size()];
		for(int i = 0; i < params.length; i++)
		{
			params[i] = gson.fromJson(array.get(i), paramTypes[i]);
		}
		methodParams.parameters = params;
		return methodParams;
	}
	
	public Class<?>[] getParameterTypes()
	{
		if(parameterTypes == null)
		{
			return null;
		}
		
		Class<?>[] clazz = new Class<?>[parameterTypes.length];
		for(int i = 0; i < parameterTypes.length; i++)
		{
			try {
				clazz[i] = Class.forName(parameterTypes[i]);
			} catch (ClassNotFoundException e) {
				logger.debug(parameterTypes[i] + " class not found");
			}
		}
		return clazz;
	}

	public void setParameterTypes(String[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}


	public static MethodParameters convert(String methodName, Object...arguments){
		Assert.notNull(methodName, "Method name is null!");
		Assert.notEmpty(arguments, "Arguments is null!");
		String[] paramTypes = new String[arguments.length];
		for(int i = 0; i < paramTypes.length; i++)
		{
			paramTypes[i] = arguments[i].getClass().getName();
		}
		return new MethodParameters(methodName, paramTypes, arguments);
	}

	public MethodParameters() {
	}

	public MethodParameters(String methodName, String[] parameterTypes, Object[] parameters) {
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.parameters = parameters;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
