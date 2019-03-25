package com.tng.portal.sms.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tng.portal.ana.vo.SMSJobQueryVo;
import com.tng.portal.ana.vo.SMSJobResponse;
import com.tng.portal.ana.vo.SMSQueryParamVo;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.common.vo.sms.ReportDataVto;
import com.tng.portal.common.vo.sms.SmsSendDetail;
import com.tng.portal.sms.server.service.ReportService;
import com.tng.portal.sms.server.service.SMSJobService;
import com.tng.portal.sms.server.vo.ReportDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/remote")
public class RemoteSMSController {

	private static Logger logger = LoggerFactory.getLogger(ReportController.class);
	
	@Autowired
	private SMSJobService jobService;

	@Autowired
	@Qualifier("smsReportService")
	private ReportService reportService;

	/**
	 * Query sms job list 
	 * 
	 * @param vo
	 * 			sms query param vo
	 * 
	 * @return
	 */
    @RequestMapping(value = "queryJob",method = RequestMethod.POST)
    @ResponseBody
    public SMSJobResponse listJobsByPage(@RequestBody SMSQueryParamVo vo) {
    	SMSJobResponse response = new SMSJobResponse();
    	PageDatas<SMSJobQueryVo> pageDatas = null;
    	try{
    		pageDatas = jobService.getJobsByPage(vo);
    	}catch(Exception e){
    		response.setFailStatus();
    		throw e;
    	}
    	response.setData(pageDatas);
    	response.setSuccessStatus();
        return response;
    }

	/**
	 * the SMS sent history report
	 * @param startDate Export the data start time
	 * @param endDate Export the data end time
	 * @param docType Export file type csv/pdf
	 * @param searchBy  group by (daily / monthly / sender / jobId / provider)
	 */
	@ApiOperation("Export the execution record list")
	@RequestMapping(value = "report/exportData", method = RequestMethod.GET)
	public @ResponseBody RestfulResponse<ReportDto> exportData(@ApiParam("Export the record start time")@RequestParam String startDate,
															   @ApiParam("Export the record end time") @RequestParam String endDate,
															   @ApiParam("Export file type csv/pdf")@RequestParam String docType,
															   @ApiParam("Export file type csv/pdf")@RequestParam String searchBy,
															   @ApiParam(value="sort by")@RequestParam(required = false, defaultValue = "date") String sortBy,
															   @ApiParam(value="true--ascend or false--descend")@RequestParam(required = false, defaultValue = "true") Boolean isAscending,
															   @ApiParam(value="application code")@RequestParam(required = false) String appCode) {
		RestfulResponse restfulResponse = new RestfulResponse();
		ReportDto report = reportService.report(docType, searchBy, startDate, endDate, sortBy, isAscending, appCode);
		restfulResponse.setData(report);
		restfulResponse.setSuccessStatus();
		return restfulResponse;
	}
	
    @RequestMapping(value = "terminateJob",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<String> terminateJob(@RequestParam Long id) {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
    	try{
    		restResponse.setData(jobService.terminateJob(id));
    	} catch(Exception e){
    		logger.error("Terminate job exception",e);
	        restResponse.setFailStatus();
	        return restResponse;
		}
        restResponse.setSuccessStatus();
        return restResponse;
    }
    
    @RequestMapping(value = "findMobile",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<String> findMobile(@RequestParam Long id,
    		@RequestParam String status,
    		@RequestParam String mobile) {
    	RestfulResponse<String> restResponse = new RestfulResponse<>();
    	try{
    		restResponse.setData(jobService.findMobile(id, status, mobile));
    	} catch(Exception e){
    		logger.error("find mobile exception",e);
	        restResponse.setFailStatus();
	        return restResponse;
		}
        restResponse.setSuccessStatus();
        return restResponse;
    }

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
	@RequestMapping(value = "/report",method = RequestMethod.GET)
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
	
	@RequestMapping(value = "resend",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<String> resend(@RequestParam(required = true) String type,
    		@RequestParam(required = true) String key, 
    		@RequestParam String senderId){
    	RestfulResponse<String> restResponse = new RestfulResponse<>();
    	try{
    		jobService.resend(type, key, senderId);
    	} catch(Exception e){
    		logger.error("resend error",e);
	        restResponse.setFailStatus();
	        return restResponse;
		}
        restResponse.setSuccessStatus();
        return restResponse;
    }

    @ApiOperation("Query report item data")
    @RequestMapping(value = "/reportItem",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<com.tng.portal.common.vo.PageDatas<SmsSendDetail>> getReportItemData(@ApiParam(value="search type ")@RequestParam() String searchType,
																								@ApiParam(value="search time ")@RequestParam() String searchTime,
																								@ApiParam(value="application code")@RequestParam(required = false) String appCode,
																								@ApiParam(value="sort by ")@RequestParam(required = false, defaultValue = "date") String sortBy,
																								@ApiParam(value="true--ascend or false--descend")@RequestParam(required = false, defaultValue = "true") Boolean isAscending,
																								@ApiParam(value="page number ")@RequestParam(required = false) Integer pageNumber,
																								@ApiParam(value="page size ")@RequestParam(required = false) Integer pageSize,
																								@ApiParam(value="item value ")@RequestParam() String itemValue,
																								@ApiParam(value="send result,SUCCESS or FAIL ")@RequestParam() String sendResult){
        logger.info("getReportItemData() searchType:{} searchTime:{} itemValue:{} sendResult:{} appCode:{} sortBy:{} isAscending:{} pageNumber:{} pageSize:{} ",
                searchType, searchTime, itemValue, sendResult, appCode, sortBy, isAscending, pageNumber, pageSize);
        RestfulResponse<com.tng.portal.common.vo.PageDatas<SmsSendDetail>> response = new RestfulResponse<>();
        com.tng.portal.common.vo.PageDatas<SmsSendDetail> data = reportService.getReportItemData(searchType, searchTime,itemValue, sendResult, appCode,
				sortBy, isAscending, pageNumber, pageSize);
        response.setData(data);
        response.setSuccessStatus();
        return response;
    }

    @ApiOperation("Search report item data")
    @RequestMapping(value = "/reportItemSearch",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<com.tng.portal.common.vo.PageDatas<SmsSendDetail>> searchReportItemData(@ApiParam(value="search type ")@RequestParam() String searchType,
																								   @ApiParam(value="search time ")@RequestParam() String searchTime,
																								   @ApiParam(value="item value ")@RequestParam() String itemValue,
																								   @ApiParam(value="send result,SUCCESS or FAIL ")@RequestParam() String sendResult,
																								   @ApiParam(value="search number ")@RequestParam() String searchNumber,
																								   @ApiParam(value="application code")@RequestParam(required = false) String appCode,
																								   @ApiParam(value="sort by ")@RequestParam(required = false, defaultValue = "date") String sortBy,
																								   @ApiParam(value="true--ascend or false--descend")@RequestParam(required = false, defaultValue = "true") Boolean isAscending,
																								   @ApiParam(value="page number ")@RequestParam(required = false) Integer pageNumber,
																								   @ApiParam(value="page size ")@RequestParam(required = false) Integer pageSize){
        logger.info("searchReportItemData() searchType:{} searchTime:{} itemValue:{} sendResult:{} searchNumber:{} appCode:{} sortBy:{} isAscending:{} pageNumber:{} pageSize:{} " ,
                searchType, searchTime,itemValue, sendResult, searchNumber, appCode, sortBy, isAscending, pageNumber, pageSize);
        RestfulResponse<com.tng.portal.common.vo.PageDatas<SmsSendDetail>> response = new RestfulResponse<>();
        com.tng.portal.common.vo.PageDatas<SmsSendDetail> data = reportService.searchReportItemData(searchType, searchTime,itemValue, sendResult,searchNumber, appCode,
				sortBy, isAscending, pageNumber, pageSize);
        response.setData(data);
        response.setSuccessStatus();
        return response;
    }

}
