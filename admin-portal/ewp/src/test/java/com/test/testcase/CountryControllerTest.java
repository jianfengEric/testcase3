package com.test.testcase;

import com.gea.portal.ewp.Application;
import com.gea.portal.ewp.dto.CountryDto;
import com.gea.portal.ewp.service.CountryService;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;


@SpringBootTest(classes = { Application.class })
public class CountryControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private CountryService countryService;

    @Test(description = "find-all-country")
    public void testFindAllCountry() throws IOException {
        List<CountryDto> list = countryService.findAll();
        RestfulResponse.ofData(list);
        Assert.assertNotNull(list, "response");
    }
}
