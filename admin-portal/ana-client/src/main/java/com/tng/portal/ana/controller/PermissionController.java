package com.tng.portal.ana.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tng.portal.ana.service.PermissionService;
import com.tng.portal.ana.vo.PermissionPostDto;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by Zero on 2016/11/10.
 */
@RestController
@RequestMapping("permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    /**
     * Query ANA permission list
     * 
     * @param pageNo
	 * 			current page number
	 * 
	 * @param pageSize
	 * 			page size
	 * 
	 * @param sortBy
	 * 			sort by
	 * 
	 * @param isAscending
	 * 			true--ascend or false--descend
	 * 
     * @return
     */
    @ApiOperation(value="Query ANA permission list", notes="")
    @RequestMapping(method = RequestMethod.GET)
    public RestfulResponse<PageDatas> listPermissions(@ApiParam(value="page number")@RequestParam(required = false) Integer pageNo,
    		@ApiParam(value="page size")@RequestParam(required = false) Integer pageSize,
    		@ApiParam(value="sort by")@RequestParam(required = false) String sortBy,
    		@ApiParam(value="true--ascend or false--descend")@RequestParam(required = false) Boolean isAscending){

        return permissionService.listPermissions(pageNo, pageSize,sortBy,isAscending);
    }

    /**
     * Add ANA permission info
     * 
     * @param request
     * @param postDto
     * 			new permission info
     * 
     * @return
     *
     */
    @ApiOperation(value="Add ANA permission info", notes="")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    RestfulResponse<Integer> addPermission(HttpServletRequest request,@ApiParam(value="permission dto")@RequestBody PermissionPostDto postDto)  {
        return permissionService.createPermission(postDto);
    }
    
    /**
     * Update ANA permission info
     * 
     * @param request
     * @param postDto
     * 			updated permission info
     * 
     * @return
     * @
     */
    @ApiOperation(value="Update ANA permission info", notes="")
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody RestfulResponse<Integer> updatePermission(HttpServletRequest request,@ApiParam(value="permission dto")@RequestBody PermissionPostDto postDto)  {
        return permissionService.updatePermission(postDto);
    }
    
    /**
     * Delete ANA permission by permission id
     * 
     * @param request
     * @param permissionId
     * 			ANA_PERMISSION.ID
     * 
     * @return
     * @
     */
    @ApiOperation(value="Delete ANA permission by permission id", notes="")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public @ResponseBody RestfulResponse<Integer> deletePermission(HttpServletRequest request,@ApiParam(value="ANA_PERMISSION.ID")@PathVariable("id") Integer permissionId)  {
        return permissionService.deletePermission(permissionId);
    }

}
