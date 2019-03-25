package com.tng.portal.common.soa;

public class Message {
	public static final String ENCODING = "utf-8";
	
	private String targetQueue;
	private String topic;
	private String contentAsString;
	private String correlationId;

	public String getTargetQueue() {
		return targetQueue;
	}

	public void setTargetQueue(String targetQueue) {
		this.targetQueue = targetQueue;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getContentAsString() {
		return contentAsString;
	}

	public void setContentAsString(String contentAsString) {
		this.contentAsString = contentAsString;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
}
