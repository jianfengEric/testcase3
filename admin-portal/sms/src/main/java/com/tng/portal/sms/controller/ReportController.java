package com.tng.portal.sms.controller;

import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.common.vo.sms.ReportDataVto;
import com.tng.portal.common.vo.sms.SmsSendDetail;
import com.tng.portal.sms.service.ReportService;
import com.tng.portal.sms.vo.ReportDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Owen on 2017/6/15.
 */
@Controller
@RequestMapping("report")
public class ReportController {

    private static Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    @Qualifier("smsReportService")
    private ReportService reportService;

    /**
     * Query SMS report data
     *
     * @param pageNo
     * 			current page number
     *
     * @param pageSize
     * 			page size
     *
     * @param searchBy (daily / monthly / sender / jobId / provider)
     * 			search field
     *
     * @param search
     * 			search value(null / 20170608 / 20170608-20170610)
     *
     * @param sortBy
     * 			sort by (sendTime / sender / jobId / provider)
     *
     * @param isAscending
     * 			true--ascend or false--descend
     *
     * @return
     */
    @ApiOperation("Query report data")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    RestfulResponse<ReportDataVto> reportData(@ApiParam(value="page number")@RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                              @ApiParam(value="page size")@RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                              @ApiParam(value="search by")@RequestParam(required = false) String searchBy,
                                              @ApiParam(value="search value")@RequestParam(required = false) String search,
                                              @ApiParam(value="sort by")@RequestParam(required = false, defaultValue = "date") String sortBy,
                                              @ApiParam(value="true--ascend or false--descend")@RequestParam(required = false, defaultValue = "true") Boolean isAscending,
                                              @ApiParam(value="application code")@RequestParam(required = false) String appCode,
                                              @ApiParam(value="provider id")@RequestParam(required = false) String providerId) {
        RestfulResponse<ReportDataVto> response = new RestfulResponse<>();
        ReportDataVto data = reportService.getReportData(pageNo, pageSize, searchBy, search, sortBy, isAscending, appCode, providerId);
        response.setData(data);
        response.setSuccessStatus();
        return response;
    }


    /**
     * the SMS sent history report
     * @param response http servlet response
     * @param startDate Export the data start time
     * @param endDate Export the data end time
     * @param docType Export file type csv/pdf
     * @param searchBy  group by (daily / monthly / sender / jobId / provider)
     */
    @ApiOperation("Export the execution record list")
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void export(HttpServletResponse response,
                       @ApiParam("Export the record start time")@RequestParam String startDate,
                       @ApiParam("Export the record end time") @RequestParam String endDate,
                       @ApiParam("Export file type csv/pdf")@RequestParam String docType,
                       @ApiParam("Export file type csv/pdf")@RequestParam String searchBy,
                       @ApiParam(value="sort by")@RequestParam(required = false, defaultValue = "date") String sortBy,
                       @ApiParam(value="true--ascend or false--descend")@RequestParam(required = false, defaultValue = "true") Boolean isAscending,
                       @ApiParam(value="application code")@RequestParam(required = false) String appCode){
        try {
            ReportDto report = reportService.report(docType, searchBy, startDate, endDate, sortBy, isAscending, appCode);
            if(null == report){
                return;
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + report.getFileName());
            response.setHeader(HttpHeaders.CONTENT_TYPE, report.getContentType());
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(report.getContent().length));
            ServletOutputStream outputStream = response.getOutputStream();
            if("csv".equals(docType)){
                outputStream.write(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF });
            }
            outputStream.write(report.getContent());
            response.setStatus(HttpServletResponse.SC_OK);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            logger.error("startDate:{} endDate:{} docType:{} searchBy:{} sortBy:{} isAscending:{} appCode:{}",startDate,endDate,docType,searchBy,sortBy,isAscending,appCode,e);
        }
    }
    
    private String getReplaceNumber(String mobileNumbers){
        String numbers = mobileNumbers.replace("[", "").replace("]", "").replace(" ", "");
        String[] split = numbers.split(",");
        for (int i=split.length-1;i>=0;i--) {
            if(split[i]!=null && split[i].length() >= 7) {
                split[i]=new StringBuilder(split[i]).replace(3, 7, "XXXX").toString();
            }
        }
        List<String> numberList = Arrays.asList(split);
        return numberList.toString();
    }

    @RequestMapping(value = "allUser", method = RequestMethod.GET)
    public @ResponseBody Map<String, String> getAllUser(){
        return reportService.getAllUser();
    }

    @ApiOperation("Query report item data")
    @RequestMapping(value = "/item",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<PageDatas<SmsSendDetail>> getReportItemData(@ApiParam(value="search type : jobId,daily,monthly,sender,provider")@RequestParam() String searchType,
                                                                       @ApiParam(value="search time : yyyyMMdd(-yyyyMMdd)")@RequestParam() String searchTime,
                                                                       @ApiParam(value="item value ")@RequestParam() String itemValue,
                                                                       @ApiParam(value="send result : SUCCESS or FAIL ")@RequestParam() String sendResult,
                                                                       @ApiParam(value="application code : SMS")@RequestParam(required = false) String appCode,
                                                                       @ApiParam(value="sort by ")@RequestParam(required = false, defaultValue = "date") String sortBy,
                                                                       @ApiParam(value="true--ascend or false--descend")@RequestParam(required = false, defaultValue = "true") Boolean isAscending,
                                                                       @ApiParam(value="page number ")@RequestParam(required = false,defaultValue = "1") Integer pageNumber,
                                                                       @ApiParam(value="page size ")@RequestParam(required = false,defaultValue = "20") Integer pageSize){
        logger.info("getReportItemData() searchType:{} searchTime:{} itemValue:{} sendResult:{} appCode:{} sortBy:{} isAscending:{} pageNumber:{} pageSize:{} " ,
                searchType, searchTime,itemValue, sendResult, appCode, sortBy, isAscending, pageNumber, pageSize);
        RestfulResponse<PageDatas<SmsSendDetail>> response = new RestfulResponse<>();
        PageDatas<SmsSendDetail> data = reportService.getReportItemData(searchType,searchTime,itemValue,sendResult,appCode,isAscending,pageNumber,pageSize);
        replaceNumber(data.getList());
        response.setData(data);
        response.setSuccessStatus();
        return response;
    }

    @ApiOperation("Query report item data")
    @RequestMapping(value = "/itemSearch",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<PageDatas<SmsSendDetail>> searchReportItemData(@ApiParam(value="search type ")@RequestParam() String searchType,
                                                                          @ApiParam(value="search time ")@RequestParam() String searchTime,
                                                                          @ApiParam(value="item value ")@RequestParam() String itemValue,
                                                                          @ApiParam(value="send result,SUCCESS or FAIL ")@RequestParam() String sendResult,
                                                                          @ApiParam(value="search number ")@RequestParam() String searchNumber,
                                                                          @ApiParam(value="application code")@RequestParam(required = false) String appCode,
                                                                          @ApiParam(value="sort by ")@RequestParam(required = false, defaultValue = "date") String sortBy,
                                                                          @ApiParam(value="true--ascend or false--descend")@RequestParam(required = false, defaultValue = "true") Boolean isAscending,
                                                                          @ApiParam(value="page number ")@RequestParam(required = false,defaultValue = "1") Integer pageNumber,
                                                                          @ApiParam(value="page size ")@RequestParam(required = false,defaultValue = "20") Integer pageSize){
        logger.info("searchReportItemData() searchType:{} searchTime:{} itemValue:{} sendResult:{} searchNumber:{} appCode:{} sortBy:{} isAscending:{} pageNumber:{} pageSize:{} ",
                searchType, searchTime, appCode, sortBy, isAscending, pageNumber, pageSize,itemValue, sendResult, searchNumber);
        RestfulResponse<PageDatas<SmsSendDetail>> response = new RestfulResponse<>();
        PageDatas<SmsSendDetail> data = reportService.searchReportItemData(searchType,searchTime,itemValue,sendResult,searchNumber,appCode,isAscending,null,null);
        response.setData(data);
        response.setSuccessStatus();
        return response;
    }

    private List<SmsSendDetail> replaceNumber(List<SmsSendDetail> list){
        if(list != null && !list.isEmpty()){//mask the 4th to 7th digits by X
            for(SmsSendDetail detail : list){
                String mobile = detail.getMobile();
                if(!StringUtils.isBlank(mobile) && mobile.length()>7){
                    detail.setMobile(new StringBuffer(mobile).replace(3, 7, "XXXX").toString());
                }
            }
        }
        return list;
    }

    @RequestMapping(value = "exportMobileNumber", method = RequestMethod.GET)
    public void exportSuccessNumber(HttpServletResponse response,
                                    @RequestParam String category,
                                    @ApiParam(value="search type ")@RequestParam() String searchType,
                                    @ApiParam(value="search time ")@RequestParam() String searchTime,
                                    @ApiParam(value="item value ")@RequestParam() String itemValue,
                                    @ApiParam(value="send result,SUCCESS or FAIL ")@RequestParam() String sendResult,
                                    @ApiParam(value="application code")@RequestParam(required = false) String appCode,
                                    @ApiParam(value="sort by ")@RequestParam(required = false, defaultValue = "date") String sortBy,
                                    @ApiParam(value="true--ascend or false--descend")@RequestParam(required = false, defaultValue = "true") Boolean isAscending){
        try {
            PageDatas<SmsSendDetail> data = reportService.getReportItemData(searchType,searchTime,itemValue,sendResult,appCode,isAscending,null,null);
            int count = 0;
            String successMobileNumber = "";
            if(data!=null && data.getList()!=null){
                List<SmsSendDetail> smsSendDetails = data.getList();
                count = smsSendDetails.size();
                Optional<String> stringOptional = smsSendDetails.stream().map(item -> item.getMobile()).reduce((i1, i2) -> i1 + "\n" + i2);
                if(stringOptional.isPresent()){
                    successMobileNumber=stringOptional.get();
                }
            }
            String head = "Mobile No.,Success\n";
            String result = "success";
            if("FAIL".equals(sendResult)){
                head = "Mobile No.,Failed\n";
                result = "fail";
            }
        	String body = successMobileNumber + "," + count;
            response.setHeader("Content-Disposition", "attachment; filename=" + category + "_" + result + ".csv");
            response.setHeader(HttpHeaders.CONTENT_TYPE, "CSV");
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF });
            outputStream.write(head.getBytes());
            outputStream.write(body.getBytes());
            response.setStatus(HttpServletResponse.SC_OK);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            logger.error("Export error!");
        }
    }
    
}
