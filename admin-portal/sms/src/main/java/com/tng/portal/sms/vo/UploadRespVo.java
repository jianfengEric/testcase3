package com.tng.portal.sms.vo;

public class UploadRespVo {
	
	private int count;
	
	private String fileName;
	
	public UploadRespVo(int count, String fileName) {
		super();
		this.count = count;
		this.fileName = fileName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
