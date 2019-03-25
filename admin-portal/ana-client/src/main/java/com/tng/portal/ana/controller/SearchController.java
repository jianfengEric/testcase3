package com.tng.portal.ana.controller;


import com.tng.portal.ana.entity.AnaRole;
import com.tng.portal.ana.service.ApplicationService;
import com.tng.portal.ana.service.RoleService;
import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.vo.FieldDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zero on 2016/11/8.
 */
@RestController
@RequestMapping("search")
public class SearchController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private ApplicationService applicationService;

    @ApiOperation(value="GET Field", notes="")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<FieldDatas> getFields(@RequestParam(required = true) String function_code){

        RestfulResponse<FieldDatas> restResponse = new RestfulResponse<>();
        Map<String,List<String>> values = new HashMap<>();

        if ("USER_ACCOUNT".equalsIgnoreCase(function_code)) {
            List<String> roles = new ArrayList<>();
            for(AnaRole role :roleService.listRoles())
                roles.add(role.getName());
            values.put("roles",roles);
            List<String> applications = new ArrayList<>();
            for(AnaApplication anaApplication:applicationService.listApplications())
                applications.add(anaApplication.getCode());
            values.put("applications",applications);
        }
        FieldDatas fieldDatas = FieldDatas.getInstance(function_code,values);
        restResponse.setData(fieldDatas);
        restResponse.setSuccessStatus();
        return restResponse;
    }

}