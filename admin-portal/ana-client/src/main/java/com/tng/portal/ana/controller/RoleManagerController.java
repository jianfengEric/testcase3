package com.tng.portal.ana.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tng.portal.ana.service.RoleService;
import com.tng.portal.ana.vo.AnaRoleFunctionPermissionDto;
import com.tng.portal.ana.vo.RoleDetailDto;
import com.tng.portal.ana.vo.RoleDto;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by Zero on 2016/11/10.
 */
@RestController
@RequestMapping(value={"role"})
public class RoleManagerController {
    @Autowired
    private RoleService roleService;
    @Resource
    public HttpServletRequest request;
    /**
     * Query ANA role list
     * @param pageNo current page number
     * @param pageSize page size
     * @param sortBy sort by
     * @param isAscending true--ascend or false--descend
     * @return
     */
    @ApiOperation(value="Query ANA role list", notes="")
    @RequestMapping(method = RequestMethod.GET)
    public RestfulResponse<PageDatas> listRoles(@ApiParam(value="page number")@RequestParam(required = false) Integer pageNo,
									    	 @ApiParam(value="page size")@RequestParam(required = false) Integer pageSize,
									    	 @ApiParam(value="sort by")@RequestParam(required = false) String sortBy,
									    	 @ApiParam(value="true--ascend or false--descend")@RequestParam(required = false) Boolean isAscending,
                                             @RequestParam(value = "nameSearch", required = false) String nameSearch,
                                             @RequestParam(value = "externalGroupIdSearch", required = false) String externalGroupIdSearch){
        return roleService.listRoles(pageNo, pageSize,sortBy,isAscending,nameSearch,externalGroupIdSearch);
    }

    /**
     * Create ANA role info
     * 
     * @param request
     * @param postDto
     * 			new role info
     * 
     * @return
     *
     */
    @ApiOperation(value="Create ANA role info", notes="")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    RestfulResponse<Long> createRole(@ApiParam(value="role dto")@RequestBody AnaRoleFunctionPermissionDto postDto)  {
        return roleService.createRole(request.getRemoteAddr(),postDto);
    }

    /**
     * Query ANA role detail info by role id
     * 
     * @param roleId
     * 			ANA_ROLE.ID
     * 
     * @return
     * @
     */
    @ApiOperation(value="Query ANA role detail info by role id", notes="")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public @ResponseBody RestfulResponse<RoleDetailDto> detailRole(@ApiParam(value="ANA_ROLE.ID")@PathVariable("id") Long roleId)  {
        return roleService.getRoleDetail(roleId);
    }

    /**
     * update ANA role info
     * @param postDto updated role info
     * @return
     */
    @ApiOperation(value="update ANA role info", notes="")
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody RestfulResponse<Long> updateRole(@ApiParam(value="role dto")@RequestBody AnaRoleFunctionPermissionDto postDto)  {
        return roleService.updateRole(request.getRemoteAddr(), postDto);
    }

    /**
     * delete role info by role id
     * 
     * @param request
     * @param roleId
     * 			ANA_ROLE.ID
     * 
     * @return
     * @
     */
    @ApiOperation(value="delete role info by role id", notes="")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public @ResponseBody RestfulResponse<Long> deleteRole(HttpServletRequest request,@ApiParam(value="ANA_ROLE.ID")@PathVariable("id") Integer roleId)  {
        return roleService.deleteRole(request,roleId);
    }

    /**
     * Query ALL role list
     * @return
     */
    @ApiOperation(value="Query ANA role list", notes="")
    @RequestMapping(value = "/listAllRoles",method = RequestMethod.GET)
    public RestfulResponse<List<RoleDto>> listAllRoles(){
        return roleService.listAllRoles();
    }

    @ApiOperation(value="Create New Role View", notes="")
    @RequestMapping(value = "/newRole",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<AnaRoleFunctionPermissionDto> newRole(){
        return roleService.newRole();
    }

    @ApiOperation(value="Edit Role View", notes="")
    @RequestMapping(value = "/editRole",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<AnaRoleFunctionPermissionDto> editRole(Long roleId){
        return roleService.editRole(roleId);
    }
    
    @ApiOperation(value="copy role", notes="")
    @RequestMapping(value = "/copyRole",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<Long> copyRole(Long roleId){
    	RestfulResponse<Long> restResponse = new RestfulResponse<>();
    	restResponse.setData(roleService.copyRole(roleId));
    	return restResponse;
    }
}
