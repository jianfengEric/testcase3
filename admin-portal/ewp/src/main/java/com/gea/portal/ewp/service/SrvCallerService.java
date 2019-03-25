package com.gea.portal.ewp.service;

import com.tng.portal.common.dto.srv.BaseServiceDto;
import com.tng.portal.common.vo.rest.RestfulResponse;

import java.util.List;

/**
 *  Used for calling mp Interface to other modules 
 */
public interface SrvCallerService {
	List<BaseServiceDto> getAllService();
	RestfulResponse<List<BaseServiceDto>> listBaseServiceAll();
}
