package com.tng.portal.sms.server.service.impl;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tng.portal.sms.server.entity.SystemParameter;
import com.tng.portal.sms.server.repository.SystemParameterRepository;
import com.tng.portal.sms.server.service.SystemParameterService;

@Service
public class SystemParameterServiceImpl implements SystemParameterService{
    @Autowired
	private SystemParameterRepository systemParameterRepository;
    
	@Override
	@Transactional
	public List<SystemParameter> getParamByCategory(String paramCat) {
		return systemParameterRepository.findByParamCat(paramCat);
	}

}