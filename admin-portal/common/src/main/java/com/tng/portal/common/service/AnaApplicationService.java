package com.tng.portal.common.service;


import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.entity.EmailMessage;
import com.tng.portal.common.vo.rest.EmailParameterVo;

import java.util.Date;
import java.util.List;

/**
 * Created by Dell on 2016/11/17.
 */
public interface AnaApplicationService {
	
	public AnaApplication findByCode(String code);
	
}
