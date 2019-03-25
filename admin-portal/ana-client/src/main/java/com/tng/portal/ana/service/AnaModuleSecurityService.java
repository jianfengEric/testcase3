package com.tng.portal.ana.service;


import com.tng.portal.ana.entity.AnaModuleSecurity;

/**
 */
public interface AnaModuleSecurityService {

	 /**
     * find By consumer
     */
    public AnaModuleSecurity findByConsumer(String consumer);
}
