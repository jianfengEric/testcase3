/*
package com.gea.portal.ewp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gea.portal.ewp.service.EwpBaseService;
import com.tng.portal.common.dto.ewp.BaseServiceDto;
import com.tng.portal.common.vo.rest.RestfulResponse;

@RestController
@RequestMapping("baseService/v1")
public class BaseServiceController {

    @Autowired
    private EwpBaseService ewpBaseService;

    @PostMapping("get-baseService")
    @ResponseBody
    public RestfulResponse<List<BaseServiceDto>> listBaseServiceAll(){
        return ewpBaseService.listBaseServiceAll();
    }

}
*/
