package com.gea.portal.tre.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gea.portal.tre.entity.ExchangeRateRecord;
import com.tng.portal.common.dto.tre.ExchangeRateFileDto;
import com.tng.portal.common.dto.tre.ExchangeRateListDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 * Created by Dell on 2018/9/14.
 */
public interface TreasuryService {
    RestfulResponse<ExchangeRateListDto> getPageData(Integer pageNo, Integer pageSize, String status, String sortBy, Instance instance, Boolean isAscending) throws IllegalAccessException, InvocationTargetException;
    
    ExchangeRateListDto getListData(Instance instance) throws IllegalAccessException, InvocationTargetException;
    
    RestfulResponse<ExchangeRateFileDto> loadExchangeRateFile(Long exchRateFileId, Instance instance);
    ExchangeRateFileDto getDetail(Long exchRateFileId, Instance instance);
    RestfulResponse<List<ExchangeRateRecord>> saveExchangeRateRecord(String exchRateFileId, Instance instance,String requestRemark);
    Boolean checkStatus(Instance instance);
    RestfulResponse<String> uploadExchangeRateFile(MultipartFile file, Instance instance) throws Exception;
}
