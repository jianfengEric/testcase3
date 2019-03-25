package com.tng.portal.sms.service;

import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.sms.ReportDataVto;
import com.tng.portal.common.vo.sms.SmsSendDetail;
import com.tng.portal.sms.vo.ReportDto;

import java.util.Map;

/**
 * Created by Owen on 2017/6/12.
 */
public interface ReportService {

    ReportDataVto getReportData(Integer pageNo, Integer pageSize, String searchBy, String search, String sortBy, Boolean isAscending, String appCode, String providerId);
    PageDatas<SmsSendDetail> getReportItemData(String searchType, String searchTime, String itemValue, String sendResult, String appCode, Boolean isAscending, Integer pageNumber, Integer pageSize);
    PageDatas<SmsSendDetail> searchReportItemData(String searchType, String searchTime, String itemValue, String sendResult, String searchNumber, String appCode, Boolean isAscending, Integer pageNumber, Integer pageSize);

    ReportDto report(String docType, String searchBy, String startDate, String endDate, String sortBy, Boolean isAscending, String appCode);

    Map<String, String> getAllUser();
}
