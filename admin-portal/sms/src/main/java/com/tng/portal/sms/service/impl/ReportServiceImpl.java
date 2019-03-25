package com.tng.portal.sms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.repository.AnaAccountRepository;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.common.vo.sms.ReportDataVo;
import com.tng.portal.common.vo.sms.ReportDataVto;
import com.tng.portal.common.vo.sms.SmsSendDetail;
import com.tng.portal.sms.service.ReportService;
import com.tng.portal.sms.util.StringUtil;
import com.tng.portal.sms.vo.ExportResponseVo;
import com.tng.portal.sms.vo.ReportDto;

/**
 * Created by Owen on 2017/6/12.
 */
@Service("smsReportService")
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    @Qualifier("httpClientUtils")
    private HttpClientUtils httpClientUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AnaAccountRepository anaAccountRepository;

    @Override
    public ReportDataVto getReportData(Integer pageNo, Integer pageSize, String searchBy, String search, String sortBy, Boolean isAscending, String appCode, String providerId) {
        Map<String, String> pram = new HashMap<>();
        pram.put("pageNo", null==pageNo?null:pageNo.toString());
        pram.put("pageSize",  null==pageSize?null:pageSize.toString());
        pram.put("searchBy", searchBy);
        pram.put("search", search);
        pram.put("sortBy", sortBy);
        pram.put("isAscending", null==isAscending?null:isAscending.toString());
        pram.put("appCode", appCode);
        pram.put("providerId", providerId);
        ReportDataVto reportDataVto=new ReportDataVto();
        try {
            RestfulResponse<ReportDataVto> restfulResponse = httpClientUtils.httpGet(
            		PropertiesUtil.getAppValueByKey("sms.server.report.api"),
                    new ParameterizedTypeReference<RestfulResponse<ReportDataVto>>() {},
                    pram,userService.getToken());
            if(null != restfulResponse && restfulResponse.getStatus().equals("success")){
                reportDataVto = restfulResponse.getData();
                List<ReportDataVo> list = reportDataVto.getList();
                reportDataVto.setList(list);
            }
        } catch (Exception e) {
           logger.error("Exception",e);
        }
        return reportDataVto;
    }

    @Override
    public PageDatas<SmsSendDetail> getReportItemData(String searchType, String searchTime, String itemValue, String sendResult, String appCode, Boolean isAscending, Integer pageNumber, Integer pageSize) {
        Map<String, String> pram = new HashMap<>();
        pram.put("pageNumber", null==pageNumber?null:pageNumber.toString());
        pram.put("pageSize",  null==pageSize?null:pageSize.toString());
        pram.put("searchType", searchType);
        pram.put("searchTime", searchTime);
        pram.put("isAscending", null==isAscending?null:isAscending.toString());
        pram.put("appCode", appCode);
        pram.put("itemValue", itemValue);
        pram.put("sendResult", sendResult);
        PageDatas<SmsSendDetail> reportDataVto=new PageDatas<>();
        try {
            RestfulResponse<PageDatas<SmsSendDetail>> restfulResponse = httpClientUtils.httpGet(
            		PropertiesUtil.getAppValueByKey("sms.server.reportItem.api"),
                    new ParameterizedTypeReference<RestfulResponse<PageDatas<SmsSendDetail>>>() {},
                    pram,userService.getToken());
            logger.info("getReportItemData() restfulResponse "+restfulResponse);
            if(null != restfulResponse && restfulResponse.getStatus().equals("success")){
                reportDataVto = restfulResponse.getData();
            }
        } catch (Exception e) {
            logger.error("Exception",e);
        }
        return reportDataVto;
    }

    @Override
    public PageDatas<SmsSendDetail> searchReportItemData(String searchType, String searchTime, String itemValue, String sendResult, String searchNumber, String appCode, Boolean isAscending, Integer pageNumber, Integer pageSize) {
        Map<String, String> pram = new HashMap<>();
        pram.put("pageNumber", null==pageNumber?null:pageNumber.toString());
        pram.put("pageSize",  null==pageSize?null:pageSize.toString());
        pram.put("searchType", searchType);
        pram.put("searchTime", searchTime);
        pram.put("isAscending", null==isAscending?null:isAscending.toString());
        pram.put("appCode", appCode);
        pram.put("itemValue", itemValue);
        pram.put("sendResult", sendResult);
        pram.put("searchNumber", searchNumber);
        PageDatas<SmsSendDetail> reportDataVto=new PageDatas<>();
        try {
            RestfulResponse<PageDatas<SmsSendDetail>> restfulResponse = httpClientUtils.httpGet(
            		PropertiesUtil.getAppValueByKey("sms.server.reportItemSearch.api"),
                    new ParameterizedTypeReference<RestfulResponse<PageDatas<SmsSendDetail>>>() {},
                    pram,userService.getToken());
            logger.info("getReportItemData() restfulResponse "+restfulResponse);
            if(null != restfulResponse && restfulResponse.getStatus().equals("success")){
                reportDataVto = restfulResponse.getData();
            }
        } catch (Exception e) {
            logger.error("Exception",e);
        }
        return reportDataVto;
    }


    @Override
    public ReportDto report(String docType, String searchBy, String startDate, String endDate, String sortBy, Boolean isAscending, String appCode) {
        Map<String, String> pram = new HashMap<>();
        pram.put("docType", docType);
        pram.put("searchBy", searchBy);
        pram.put("startDate", startDate);
        pram.put("endDate", endDate);
        pram.put("sortBy", sortBy);
        pram.put("isAscending", null==isAscending?null:isAscending.toString());
        pram.put("appCode", appCode);
        ExportResponseVo responseVo;
        try {
            responseVo = httpClientUtils.getObject(PropertiesUtil.getAppValueByKey("sms.server.export.api"), ExportResponseVo.class,pram, userService.getToken());
            if(null != responseVo && responseVo.getStatus().equals("success")){
                return responseVo.getData();
            }
        } catch (Exception e) {
            logger.error("Exception",e);
        }
        return null;
    }

    @Override
    public Map<String, String> getAllUser() {
        List<AnaAccount> accounts = anaAccountRepository.findAll();
        Map<String, String> all = accounts.stream().collect(Collectors.toMap(AnaAccount::getId, AnaAccount::getFullName));
        return all;
    }
}
