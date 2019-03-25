package com.gea.portal.ewp.controller;

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
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.Map.Entry;


/**
 * Created by Owen on 2018/9/6.
 */
@RestController
@RequestMapping("dictionary")
public class DictionaryController {

    
    @Autowired
    private TreCallerService treCallerService;
    @Autowired
    private SrvCallerService srvCallerService;

    @GetMapping("base-service")
    @ResponseBody
    public RestfulResponse<List<BaseServiceDto>> listBaseService(){
        return srvCallerService.listBaseServiceAll();
    }

    @GetMapping("base-currency")
    @ResponseBody
    public RestfulResponse<List<CurrencyDto>> listBaseCurrency(@RequestParam Instance instance){
		RestfulResponse<ExchangeRateListDto> resp = treCallerService.callGetListData(instance);
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
    	for(Entry<String,List<String>> entry : map.entrySet()){
    		CurrencyDto cd = new CurrencyDto(entry.getKey(), new ArrayList<>());
    		for(String currto : entry.getValue()){
    			Map<String,String> m = new HashMap<>();
    			m.put("currTo", currto);
    			cd.getCurrToList().add(m);
    		}
    		list.add(cd);
    	}
    	
        return RestfulResponse.ofData(list);
    }
}
