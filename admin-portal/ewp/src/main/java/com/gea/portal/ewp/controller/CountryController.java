package com.gea.portal.ewp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gea.portal.ewp.dto.CountryDto;
import com.gea.portal.ewp.service.CountryService;
import com.tng.portal.common.vo.rest.RestfulResponse;

@RestController
@RequestMapping("country/v1")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @PostMapping("find-all")
    @ResponseBody
    public RestfulResponse<List<CountryDto>> findAll(){
    	List<CountryDto> list = countryService.findAll();
        return RestfulResponse.ofData(list);
    }

}
