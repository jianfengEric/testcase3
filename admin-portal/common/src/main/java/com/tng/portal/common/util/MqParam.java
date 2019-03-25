package com.tng.portal.common.util;

public class MqParam{
	
	private Class clazz;
	
	private Object value;
	
	public static MqParam gen(Object value){
		return new MqParam(value);
	}
	
	public static MqParam gen(Class clazz, Object value){
		return new MqParam(clazz,value);
	}
	
	public static MqParam gen(Class clazz){
		return new MqParam(clazz);
	}

	public MqParam(Object value) {
		super();
		if(value == null){
			throw new IllegalArgumentException("value is null!");
		}
		this.value = value;
		this.clazz = value.getClass();
	}

	public MqParam(Class clazz, Object value) {
		super();
		this.clazz = clazz;
		this.value = value;
	}
	
	public MqParam(Class clazz) {
		super();
		this.clazz = clazz;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
}