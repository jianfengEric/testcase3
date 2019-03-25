package com.tng.portal.sms.server.service;

import java.util.List;

import com.tng.portal.sms.server.entity.SystemParameter;

public interface SystemParameterService {

	List<SystemParameter> getParamByCategory(String paramCat);

}
