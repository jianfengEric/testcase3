package com.tng.portal.email.controller;


import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.email.service.MSGLogService;
import com.tng.portal.email.vo.ReportDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by dong on 2017/10/27.
 */
@RestController
@RequestMapping("msgLog")
public class MSGLogController {
    @Autowired
    private MSGLogService msgLogService;

    private final Logger logger = LoggerFactory.getLogger(getClass());//sonar modify 
    /**
     * 
     * @param pageNo current page number
     * @param pageSize page size
     * @return
     */
    @ApiOperation(value="all the email sending history", notes="")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<PageDatas> listApplications(@ApiParam(value="page number")@RequestParam(required = false) Integer pageNo,
            @ApiParam(value="page size")@RequestParam(required = false) Integer pageSize,
            @ApiParam(value="sort by")@RequestParam(required = false) String sortBy,
            @ApiParam(value="true--ascend or false--descend")@RequestParam(required = false) Boolean isAscending){
        return msgLogService.listMSGLogService(pageNo,pageSize,sortBy,isAscending);
    }
    
    
    /**
     * Export the execution record list
     * @param response http servlet response
     * @param docType Export file type csv/pdf
     */
    @ApiOperation("Export the record list")
    @RequestMapping(value = "reportMSG", method = RequestMethod.GET)
    public void export(HttpServletResponse response,
                       @ApiParam("Export file type csv/pdf")@RequestParam String docType) {
        try {
            ReportDto report = msgLogService.report(docType);
            response.setHeader("Content-Disposition", "attachment; filename=" + report.getFileName());
            response.setHeader(HttpHeaders.CONTENT_TYPE, report.getContentType());
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(report.getContent().length));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(report.getContent());
            response.setStatus(HttpServletResponse.SC_OK);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    @ApiOperation("Download ATTACHMENT")
    @RequestMapping(value = "downloadAttachment", method = RequestMethod.GET)
    public void downloadAttachment(HttpServletResponse response,
                                   @ApiParam("file path")@RequestParam String filePath,
                                   @ApiParam("file name")@RequestParam String fileName) {
        ServletOutputStream servletOutputStream = null;
        try {
            String filePaths = new String(filePath.getBytes("ISO8859-1"), "UTF-8");
            File downloadFile=new File(filePaths);
            if (downloadFile.exists()) {
                response.setContentType("application/octet-stream");
                Long length=downloadFile.length();
                response.setContentLength(length.intValue());
                fileName = URLEncoder.encode(downloadFile.getName(), "UTF-8");
                response.addHeader("Content-Disposition", "attachment; filename=" + fileName);

                servletOutputStream=response.getOutputStream();
                try (FileInputStream fileInputStream=new FileInputStream(downloadFile);
                     BufferedInputStream bufferedInputStream =new BufferedInputStream(fileInputStream)) {
                    int size=0;
                    byte[] b=new byte[1024];
                    while ((size=bufferedInputStream.read(b))!=-1) {
                        servletOutputStream.write(b, 0, size);
                    }
                    response.setStatus(HttpServletResponse.SC_OK);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if(servletOutputStream!=null){
                    servletOutputStream.flush();
                    servletOutputStream.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
}
