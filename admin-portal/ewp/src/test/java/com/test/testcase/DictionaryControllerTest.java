package com.test.testcase;

import com.gea.portal.ewp.Application;
import com.gea.portal.ewp.dto.CurrencyDto;
import com.gea.portal.ewp.service.SrvCallerService;
import com.gea.portal.ewp.service.TreCallerService;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.dto.srv.BaseServiceDto;
import com.tng.portal.common.dto.tre.ExchangeRateListDto;
import com.tng.portal.common.dto.tre.ExchangeRateRecordDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;


@SpringBootTest(classes = { Application.class })
public class DictionaryControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private TreCallerService treCallerService;
    @Autowired
    private SrvCallerService srvCallerService;

    @Test(description = "base-service")
    public void testListBaseService() throws IOException {
        RestfulResponse<List<BaseServiceDto>> restfulResponse= srvCallerService.listBaseServiceAll();
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(description = "base-service")
    public void testListBaseCurrency() throws IOException {
        RestfulResponse<ExchangeRateListDto> resp = treCallerService.callGetListData(Instance.PRE_PROD);
        if(null == resp || null == resp.getData() || null == resp.getData().getData()){
            throw new BusinessException(SystemMsg.EwpErrorMsg.NOT_BASE_CURRENCY.getErrorCode());
        }
        List<ExchangeRateRecordDto> data = resp.getData().getData();

        Map<String,List<String>> map = new TreeMap<>();
        for(ExchangeRateRecordDto dto : data){
            List<String> currToList = map.get(dto.getCurrFrom());
            if(map.get(dto.getCurrFrom()) == null){
                currToList = new ArrayList<>();
                map.put(dto.getCurrFrom(), currToList);
            }
            currToList.add(dto.getCurrTo());
        }
        List<CurrencyDto> list = new ArrayList<>();
        for(Map.Entry<String,List<String>> entry : map.entrySet()){
            CurrencyDto cd = new CurrencyDto(entry.getKey(), new ArrayList<>());
            for(String currto : entry.getValue()){
                Map<String,String> m = new HashMap<>();
                m.put("currTo", currto);
                cd.getCurrToList().add(m);
            }
            list.add(cd);
        }
        Assert.assertNotNull(list, "response");
    }

}
