package com.gea.portal.ewp.service.impl;

import com.gea.portal.ewp.entity.EwalletParticipant;
import com.gea.portal.ewp.entity.EwpService;
import com.gea.portal.ewp.entity.EwpServiceConfig;
import com.gea.portal.ewp.repository.EwalletParticipantRepository;
import com.gea.portal.ewp.service.EwpBaseService;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EwpBaseServiceImpl implements EwpBaseService {

	@Autowired
	private EwalletParticipantRepository ewalletParticipantRepository;

	@Override
	public List<String> getParticipantCurrency(String geaParticipantRefId, Instance instance) {
		if(StringUtils.isBlank(geaParticipantRefId)){
			return Collections.emptyList();
		}
		EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findByGeaRefId(geaParticipantRefId);
		if(null == ewalletParticipant){
			throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"ewalletParticipant"});
		}
		List<String> list = new ArrayList<>();
		ewalletParticipant.getEwpServices().stream().filter(item->instance.equals(item.getCurrentEnvir())).map(EwpService::getEwpServiceConfig).forEach(item -> {
			List<String> l1 = item.stream().map(EwpServiceConfig::getCurrency).collect(Collectors.toList());
			list.addAll(l1);
		});
		return list.stream().distinct().collect(Collectors.toList());
	}

}
