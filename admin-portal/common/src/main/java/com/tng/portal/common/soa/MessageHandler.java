package com.tng.portal.common.soa;

public interface MessageHandler {
	public void handleMessageData(String routingKey, String replyTo, String correlationId, String data);
}
