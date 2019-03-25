package com.tng.portal.ana.service;

import com.tng.portal.ana.vo.FunctionPermissionDto;
import com.tng.portal.ana.vo.FunctionPostDto;
import com.tng.portal.ana.vo.FunctionUpdateDto;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by Zero on 2016/11/10.
 */
public interface FunctionService {

    @PreAuthorize("hasPermission('USER_FUNCTION',1)")
    RestfulResponse<String> createFunction(FunctionPostDto postDto) ;
    @PreAuthorize("hasPermission('USER_FUNCTION',4)")
    RestfulResponse<String> updateFunction(FunctionUpdateDto updateDto) ;
    @PreAuthorize("hasPermission('USER_FUNCTION',8)")
    RestfulResponse<String> deleteFunction(String code) ;
    @PreAuthorize("hasPermission('USER_FUNCTION',14)")
    RestfulResponse<PageDatas> listFunctions(Integer pageNo, Integer pageSize, String sortBy, Boolean isAscending);

    RestfulResponse<List<FunctionPermissionDto>> findFunctionPermissionByAccount(String account) ;
}
