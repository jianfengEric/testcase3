package com.gea.portal.order.controller;

import com.tng.portal.ana.authentication.AnaPrincipalAuthenticationToken;
import com.tng.portal.ana.service.EwpCallerService;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class BaseController {

    @Qualifier("anaEwpCallerService")
    @Autowired
    private EwpCallerService ewpCallerService;

    public String getOwnParticipantId(){
        AnaPrincipalAuthenticationToken auth = (AnaPrincipalAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        if (null != auth && null != auth.getAccount() && StringUtils.isNotBlank(auth.getAccount().getExternalGroupId())) {
            ParticipantDto participantDto = ewpCallerService.getParticipant(auth.getAccount().getExternalGroupId());
            if (Objects.nonNull(participantDto)) {
                return participantDto.getGeaParticipantRefId();// find ewallet_participant  ref_id
            }
        }
        return null;
    }
}
