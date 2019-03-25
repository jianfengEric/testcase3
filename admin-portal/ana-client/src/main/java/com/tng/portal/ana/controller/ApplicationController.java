package com.tng.portal.ana.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tng.portal.ana.service.ApplicationService;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by Zero on 2016/11/8.
 */
@RestController
@RequestMapping("application")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    /**
     * Query ANA application list
     * @param pageNo current page number
     * @param pageSize page size
     * @return
     */
    @ApiOperation(value="Query ANA application list", notes="")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<PageDatas> listApplications(@ApiParam(value="page number")@RequestParam(required = false) Integer pageNo,
    												   @ApiParam(value="page size")@RequestParam(required = false) Integer pageSize){
        return applicationService.listApplications(pageNo,pageSize);
    }
}
