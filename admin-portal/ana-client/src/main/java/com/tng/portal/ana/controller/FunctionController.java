package com.tng.portal.ana.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tng.portal.ana.service.FunctionService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.ana.vo.FunctionPermissionDto;
import com.tng.portal.ana.vo.FunctionPostDto;
import com.tng.portal.ana.vo.FunctionUpdateDto;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by Zero on 2016/11/10.
 */
@RestController
@RequestMapping("function")
public class FunctionController {
    @Autowired
    private FunctionService functionService;

    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;

    /**
     * Query ANA function list 
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
    @ApiOperation(value="Query ANA function list ", notes="")
    @RequestMapping(method = RequestMethod.GET)
    public RestfulResponse<PageDatas> listFunctions(@ApiParam(value="page number")@RequestParam(required = false) Integer pageNo,
										    		@ApiParam(value="page size")@RequestParam(required = false) Integer pageSize,
										    		@ApiParam(value="sort by")@RequestParam(required = false) String sortBy,
										    		@ApiParam(value="true--ascend or false--descend")@RequestParam(required = false) Boolean isAscending){
        return functionService.listFunctions(pageNo, pageSize,sortBy,isAscending);
    }

    /**
     * Create ANA function info
     * 
     * @param request
     * @param postDto
     * 			new function info
     * 
     * @return
     *
     */
    @ApiOperation(value="Create ANA function info", notes="")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    RestfulResponse<String> createFunction(HttpServletRequest request,@ApiParam(value="function dto")@RequestBody FunctionPostDto postDto)  {
        return functionService.createFunction(postDto);
    }
    
    /**
     * update function info
     * 
     * @param request
     * @param updateDto
     * 			updated function info
     * @return
     *
     */
    @ApiOperation(value="update function info", notes="")
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody RestfulResponse<String> updateFunction(HttpServletRequest request,
    		@ApiParam(value="function dto")@RequestBody FunctionUpdateDto updateDto)  {
        return functionService.updateFunction(updateDto);
    }
    
    /**
     * Delete function info by function code
     * 
     * @param request
     * @param code
     * 			ANA_FUNCTION.CODE
     * 
     * @return
     *
     */
    @ApiOperation(value="Delete function info by function code", notes="")
    @RequestMapping(value = "/{code}",method = RequestMethod.DELETE)
    public @ResponseBody RestfulResponse<String> deleteFunction(HttpServletRequest request,@ApiParam(value="ANA_FUNCTION.CODE")@PathVariable("code") String code)  {
        return functionService.deleteFunction(code);
    }

    /**
     * Query current account's function permission
     * 
     * @return
     *
     */
    @ApiOperation(value="Query current account's function permission", notes="")
    @RequestMapping(value = "permession",method = RequestMethod.GET)
    public RestfulResponse<List<FunctionPermissionDto>> getFunctionPermession()  {
        String account = userService.getCurrentAccount();
        return functionService.findFunctionPermissionByAccount(account);
    }

}
