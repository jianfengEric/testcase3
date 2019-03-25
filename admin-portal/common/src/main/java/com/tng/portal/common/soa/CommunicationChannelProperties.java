package com.tng.portal.common.soa;

import java.util.ArrayList;
import java.util.Collection;


public class CommunicationChannelProperties {
	private ChannelType channelType;
	private String host;
	private int port;
	private String username;
	private String password;
	private String virtualHost;
	private Collection<String> interestedTopics = new ArrayList<>();
	private String exchangeName = "";
	private String queueName;
	
	public void addInterestedTopic(String topic)
	{
		interestedTopics.add(topic);
	}
	
	public void removeInterestedTopic(String topic)
	{
		interestedTopics.remove(topic);
	}
	
	public enum ChannelType{
		RABBIT_MQ;
	}

	public ChannelType getChannelType() {
		return channelType;
	}

	public void setChannelType(ChannelType channelType) {
		this.channelType = channelType;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	public Collection<String> getInterestedTopics() {
		return interestedTopics;
	}

	public void setInterestedTopics(Collection<String> interestedTopics) {
		this.interestedTopics = interestedTopics;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
}
