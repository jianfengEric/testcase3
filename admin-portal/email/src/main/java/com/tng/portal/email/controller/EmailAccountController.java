package com.tng.portal.email.controller;

import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.email.service.EmailAccountService;
import com.tng.portal.email.vo.EmailHostAccountDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Dell on 2017/10/31.
 */
@Controller
@RequestMapping("emailAccount")
public class EmailAccountController {


    @Autowired
    private EmailAccountService emailAccountService;

    @ApiOperation(value="Query Email host list", notes="")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<PageDatas> listAccount(@ApiParam(value="current page number")@RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                                   @ApiParam(value="page size")@RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                   @ApiParam(value="search value")@RequestParam(required = false) String search,
                                                   @ApiParam(value="sort by")@RequestParam(required = false) String sortBy,
                                                   @ApiParam(value="true--ascend or false--descend")@RequestParam(required = false) Boolean isAscending){

        return emailAccountService.listAccount(pageNo, pageSize, search, sortBy, isAscending);
    }


    @ApiOperation(value="Add email host info", notes="")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody RestfulResponse<Long> addAccount(@ApiParam(value="account dto") @RequestBody EmailHostAccountDto emailHostAccountDto) {
        return emailAccountService.addAccount(emailHostAccountDto);
    }

    @ApiOperation(value="Update email host info", notes="")
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody RestfulResponse<Long> updateAccount(@ApiParam(value="account dto")@RequestBody EmailHostAccountDto emailHostAccountDto)  {
        return emailAccountService.updateAccount(emailHostAccountDto);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public @ResponseBody RestfulResponse<Long> deleteAccount(@ApiParam(value="ref. email account.id")@PathVariable("id") Long id)  {
        return emailAccountService.deleteAccount(id);
    }

}
