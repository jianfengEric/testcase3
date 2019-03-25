package com.tng.portal.common.soa;

public class CommunicationChannelFactory {
	
	private CommunicationChannelFactory(){}
	
	public static CommunicationChannel createCommunicationChannel(CommunicationChannelProperties props)
	{
		
		return new RabbitMQ(props);
	}
}
