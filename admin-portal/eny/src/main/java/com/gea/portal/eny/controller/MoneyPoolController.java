package com.gea.portal.eny.controller;

import com.gea.portal.eny.dto.MoneyPoolDto;
import com.gea.portal.eny.page.PageDatas;
import com.gea.portal.eny.rest.RestfulResponse;
import com.gea.portal.eny.service.MoneyPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dong on 2018/9/10.
 */
@RestController
@RequestMapping("eny/v1")
public class MoneyPoolController {

    @Autowired
    private MoneyPoolService moneyPoolService;

    @PostMapping("get_money_pool")
    public RestfulResponse<PageDatas<MoneyPoolDto>> getMoneyPool(@RequestBody(required = false) MoneyPoolDto dto) {
        return moneyPoolService.getMoneyPool(dto);
    }
}