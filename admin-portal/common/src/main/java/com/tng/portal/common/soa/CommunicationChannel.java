package com.tng.portal.common.soa;

import java.util.Collection;

public interface CommunicationChannel {
	public Status getConnectionStatus();
	public void connect();
	public void disconnect();
	public void startListening();
	public void stopListening();
	public void send(Message message);
	public String sendAndWait(Message message);
	public String sendAndWait(Message message, long timeout);
	public Collection<MessageHandler> getMessageHandlers();
	public void addMessageHandler(MessageHandler handler);
	
	public enum Status
	{
		DISCONNECTED, CONNECTING, CONNECTED;
	}
}
