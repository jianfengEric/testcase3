package com.tng.portal.common.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.repository.AnaApplicationRepository;
import com.tng.portal.common.service.AnaApplicationService;

@Transactional
@Service("anaApplicationService")
public class AnaApplicationServiceImpl implements AnaApplicationService {

    //private static final Logger logger = LoggerFactory.getLogger(AnaApplicationServiceImpl.class);


    @Autowired
    private AnaApplicationRepository anaApplicationRepository;


	@Override
	public AnaApplication findByCode(String code) {
		return anaApplicationRepository.findByCode(code.toUpperCase());
	}

    
}
