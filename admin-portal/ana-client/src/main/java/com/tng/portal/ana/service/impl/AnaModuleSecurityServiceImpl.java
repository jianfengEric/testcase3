package com.tng.portal.ana.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tng.portal.ana.entity.AnaModuleSecurity;
import com.tng.portal.ana.repository.AnaModuleSecurityRepository;
import com.tng.portal.ana.service.AnaModuleSecurityService;

/**
 */
@Transactional
@Service
public class AnaModuleSecurityServiceImpl implements AnaModuleSecurityService {

	@Autowired
    private AnaModuleSecurityRepository anaModuleSecurityRepository;
	
	/**
     * find By consumer
     */
	@Override
	public AnaModuleSecurity findByConsumer(String consumer) {
		return anaModuleSecurityRepository.findByConsumer(consumer);
	}
}
