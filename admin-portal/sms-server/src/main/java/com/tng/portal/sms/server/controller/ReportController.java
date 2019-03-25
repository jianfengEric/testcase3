package com.tng.portal.sms.server.controller;

import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.common.vo.sms.ReportDataVto;
import com.tng.portal.sms.server.service.ReportService;
import com.tng.portal.sms.server.vo.ReportDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
                       @ApiParam(value="true--ascend or false--descend")@RequestParam(required = false, defaultValue = "true") Boolean isAscending){
        try {
            ReportDto report = reportService.report(docType, searchBy, startDate, endDate, sortBy, isAscending, null);
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
            logger.error("startDate:{} endDate:{} docType:{} searchBy:{} sortBy:{} isAscending:{}",startDate,endDate,docType,searchBy,sortBy,isAscending,e);
        }
    }
}
