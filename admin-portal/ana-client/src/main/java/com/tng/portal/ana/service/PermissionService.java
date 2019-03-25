package com.tng.portal.ana.service;


import org.springframework.security.access.prepost.PreAuthorize;

import com.tng.portal.ana.vo.PermissionPostDto;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 * Created by Zero on 2016/11/10.
 */
public interface PermissionService {

    @PreAuthorize("hasPermission('USER_PERMISSION',1)")
    RestfulResponse<Integer> createPermission(PermissionPostDto postDto) ;
    @PreAuthorize("hasPermission('USER_PERMISSION',4)")
    RestfulResponse<Integer> updatePermission(PermissionPostDto postDto) ;
    @PreAuthorize("hasPermission('USER_PERMISSION',8)")
    RestfulResponse<Integer> deletePermission(Integer id) ;
    @PreAuthorize("hasPermission('USER_PERMISSION',2)")
    RestfulResponse<PageDatas> listPermissions(Integer pageNo, Integer pageSize, String sortBy, Boolean isAscending);

}
