package com.gea.portal.srv.service;

import com.gea.portal.srv.entity.ServiceBatchChgReq;
import com.tng.portal.common.dto.srv.BaseServiceDto;
import com.tng.portal.common.dto.srv.ServiceBatchChgReqDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;

import java.util.List;

public interface SrvBaseService {

    RestfulResponse<PageDatas> getServiceBaseMarkups(Integer pageNo, Integer pageSize, String sortBy, Boolean isAscending,Instance instance);

    ServiceBatchChgReq submitServiceBaseMarkupData(ServiceBatchChgReqDto serviceBatchChgReqDto);

    ServiceBatchChgReqDto getDetail(Long batchId);

    Boolean checkEdit(Instance instance);

    RestfulResponse<List<BaseServiceDto>> queryAll();
}
