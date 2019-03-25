package com.tng.portal.email.controller;

import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.email.service.EmailHostService;
import com.tng.portal.email.vo.EmailHostDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Dell on 2017/10/31.
 */
@Controller
@RequestMapping("emailHost")
public class EmailHostController {


    @Autowired
    private EmailHostService emailHostService;

    @ApiOperation(value="Query Email host list", notes="")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<PageDatas> listHost(@ApiParam(value="current page number")@RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                                   @ApiParam(value="page size")@RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                   @ApiParam(value="search value")@RequestParam(required = false) String search,
                                                   @ApiParam(value="sort by")@RequestParam(required = false) String sortBy,
                                                   @ApiParam(value="true--ascend or false--descend")@RequestParam(required = false) Boolean isAscending){

        return emailHostService.listHost(pageNo, pageSize, search, sortBy, isAscending);
    }


    @ApiOperation(value="Add email host info", notes="")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody RestfulResponse<String> addHost(@ApiParam(value="account dto") @RequestBody EmailHostDto emailHostDto) {
        return emailHostService.addHost(emailHostDto);
    }

    @ApiOperation(value="Update email host info", notes="")
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody RestfulResponse<String> updateHost(@ApiParam(value="account dto")@RequestBody EmailHostDto emailHostDto)  {
        return emailHostService.updateHost(emailHostDto);
    }

    @ApiOperation(value="list email host info", notes="")
    @RequestMapping(method = RequestMethod.GET, value = "list")
    public @ResponseBody RestfulResponse<List<EmailHostDto>> listHost()  {
        return emailHostService.listHost();
    }

    @RequestMapping(value = "/{code}",method = RequestMethod.DELETE)
    public @ResponseBody RestfulResponse<String> deleteHost(@ApiParam(value="ref. host.code")@PathVariable("code") String code)  {
        return emailHostService.deleteHost(code);
    }

}
