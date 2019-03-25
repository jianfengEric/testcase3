package com.tng.portal.email.service;

import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.email.vo.ReportDto;

/**
 * Created by dong on 2017/10/27.
 */
public interface MSGLogService {

    RestfulResponse<PageDatas> listMSGLogService(Integer pageNo, Integer pageSize, String sortBy, Boolean isAscending);
    
    ReportDto report(String type);
}
