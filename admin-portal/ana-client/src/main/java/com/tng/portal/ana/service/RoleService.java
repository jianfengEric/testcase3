package com.tng.portal.ana.service;


import com.tng.portal.ana.entity.AnaRole;
import com.tng.portal.ana.vo.AnaRoleFunctionPermissionDto;
import com.tng.portal.ana.vo.RoleDetailDto;
import com.tng.portal.ana.vo.RoleDto;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Zero on 2016/11/10.
 */
public interface RoleService {

    @PreAuthorize("hasPermission('USER_ROLE',1)")
    RestfulResponse<Long> createRole(String remoteAddr, AnaRoleFunctionPermissionDto postDto) ;
    @PreAuthorize("hasPermission('USER_ROLE',4)")
    RestfulResponse<Long> updateRole(String remoteAddr,AnaRoleFunctionPermissionDto postDto) ;
    @PreAuthorize("hasPermission('USER_ROLE',8)")
    RestfulResponse<Long> deleteRole(HttpServletRequest request, long id) ;

    //@PreAuthorize("hasPermission('USER_ROLE',2)")
    RestfulResponse<PageDatas> listRoles(Integer pageNo, Integer pageSize, String sortBy, Boolean isAscending,String nameSearch,String externalGroupIdSearch);

    RestfulResponse<RoleDetailDto> getRoleDetail(Long roleId) ;

    RestfulResponse<List<RoleDto>> listAllRoles();

    RestfulResponse<List<RoleDto>> listRolesByAccount(String account) ;

    RestfulResponse<AnaRoleFunctionPermissionDto> newRole();

    RestfulResponse<AnaRoleFunctionPermissionDto> editRole(Long roleId);

    List<AnaRole> listRoles();
    
	Long copyRole(Long roleId);

    void autoCreateRole(String mid);
}