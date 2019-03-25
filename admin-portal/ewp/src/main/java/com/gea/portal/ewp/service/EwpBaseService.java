package com.gea.portal.ewp.service;

import com.tng.portal.common.enumeration.Instance;

import java.util.List;

public interface EwpBaseService {
	List<String> getParticipantCurrency(String geaParticipantRefId, Instance instance);
}
