package com.tng.portal.common.dto.ewp;

import java.io.Serializable;

/**
 * Created by dong on 2018/8/28.
 */
public class BaseServiceDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -8579233950972819289L;
	private String id;
	private String nameEn;
    private String nameZhCn;
    private String nameZhHk;
    private String serviceCode;
    private String status;

    public BaseServiceDto() {
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameZhCn() {
		return nameZhCn;
	}

	public void setNameZhCn(String nameZhCn) {
		this.nameZhCn = nameZhCn;
	}

	public String getNameZhHk() {
		return nameZhHk;
	}

	public void setNameZhHk(String nameZhHk) {
		this.nameZhHk = nameZhHk;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
}

