package com.tng.portal.email.service.impl;


import com.tng.portal.common.constant.DateCode;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.email.entity.EmailAttachment;
import com.tng.portal.email.entity.EmailContent;
import com.tng.portal.email.entity.EmailRecipient;
import com.tng.portal.email.repository.EmailAttachmentRepository;
import com.tng.portal.email.repository.EmailContentRepository;
import com.tng.portal.email.repository.EmailRecipientRepository;
import com.tng.portal.email.service.MSGLogService;
import com.tng.portal.email.util.ReportUtils;
import com.tng.portal.email.vo.MSGLogDto;
import com.tng.portal.email.vo.ReportDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dong on 2017/10/27.
 */
@Service
@Transactional
public class MSGLogServiceImpl implements MSGLogService {

	  @Autowired
	  private EmailRecipientRepository recipientRepository;
	  @Autowired
	  private EmailContentRepository contentRepository;
	  @Autowired
	  private EmailAttachmentRepository attachmentRepository;
	  
	  
	  
	  private final SimpleDateFormat dateFma = new SimpleDateFormat("yyyy-MM-dd (EEE) HH:mm:ss", Locale.ENGLISH);//sonar modify

	  
    /**
     * all the email sending history
     * @param pageNo current page number
     * @param pageSize page size
     * @return
     */
    @Override
    public RestfulResponse<PageDatas> listMSGLogService(Integer pageNo,Integer pageSize, String sortBy, Boolean isAscending) {
    	PageDatas<MSGLogDto> pageDatas = new PageDatas<>(pageNo,pageSize);
    	Sort sort = pageDatas.pageSort(sortBy, isAscending, "id");
        Page<EmailRecipient> emailRecipient = recipientRepository.findAll(pageDatas.pageRequest(sort));
        RestfulResponse<PageDatas> restResponse = new RestfulResponse<>();
        
        List<MSGLogDto> msgLogDtos = emailRecipient.getContent().stream().map(item -> {
        	MSGLogDto dto = new MSGLogDto();
        	EmailContent emailContent= item.getEmailContent();
        	dto.setRequestId(emailContent.getId());
        	dto.setRequestReceivedDate(dateFma.format(item.getSentTime()));
        	dto.setRequestReceivedTime(dateFma.format(item.getSentTime()));
        	dto.setModule(emailContent.getApplicationCode());
        	dto.setLoginId("test");
        	dto.setLogin("tst");
        	dto.setGateway("getsss");
        	dto.setSender(emailContent.getSenderEmail());
        	dto.setRecipient(item.getRecipientEmail());
        	dto.setContent(ReportUtils.delHTMLTag(emailContent.getContent()));
        	List<EmailAttachment> emailAttachment = attachmentRepository.findByEmailContent(emailContent);
        	HashMap<String,String> emailAttachmentList = new HashMap<>();
        	for(int i=0; i<emailAttachment.size(); i++){
        		emailAttachmentList.put(emailAttachment.get(i).getAttachmentName(), emailAttachment.get(i).getAttachmentPath());
        	}
        	dto.setAttachment(emailAttachmentList);
        	dto.setStatus(emailContent.getStatus());
        	dto.setResponseCode(item.getServerResponseCode());
        	dto.setResponseContent(item.getServerResponseMsg());
        	dto.setResponseTime(dateFma.format(item.getServerResponseTimestamp()));
        	dto.setReSendRetryCount(item.getResendCount());
            return dto;
        }).collect(Collectors.toList());
        pageDatas.setTotal(Long.valueOf(emailRecipient.getSize()));
        pageDatas.setTotalPages(emailRecipient.getTotalPages());

        pageDatas.setList(msgLogDtos);
        restResponse.setData(pageDatas);
        return restResponse;
    }
    
    @Override
    public ReportDto report(String docType)  {
        Map<String, Object> prams = new HashMap<>();
    	List<EmailRecipient> emailRecipient = recipientRepository.findAll();
        List<MSGLogDto> msgLogDtos = emailRecipient.stream().map(item -> {
        	MSGLogDto dto = new MSGLogDto();
        	EmailContent emailContent= item.getEmailContent();
        	List<EmailAttachment> emailAttachment = attachmentRepository.findByEmailContent(emailContent);
        	HashMap<String,String> emailAttachmentList = new HashMap<>();
        	for(int i=0; i<emailAttachment.size(); i++){
        		emailAttachmentList.put(emailAttachment.get(i).getAttachmentName(), emailAttachment.get(i).getAttachmentPath());
        	}

        	if (null == prams || prams.isEmpty()) {
        		prams.put("reportDate", LocalDate.now().format(DateTimeFormatter.ofPattern(DateCode.dateFormatMd)));
            }
        	dto.setRequestId(emailContent.getId());
        	dto.setRequestReceivedDate(dateFma.format(item.getSentTime()));
        	dto.setRequestReceivedTime(dateFma.format(item.getSentTime()));
        	dto.setModule(emailContent.getApplicationCode());
        	dto.setLoginId("test");
        	dto.setLogin("tst");
        	dto.setGateway("getsss");
        	dto.setSender(emailContent.getSenderEmail());
        	dto.setRecipient(item.getRecipientEmail());
        	dto.setContent(ReportUtils.delHTMLTag(emailContent.getContent()));
        	if(emailAttachmentList.size()<1){
//        		dto.setAttachment("");
        	} else {
        		dto.setAttachment(emailAttachmentList);
        	}
        	dto.setStatus(emailContent.getStatus());
        	dto.setResponseCode(item.getServerResponseCode());
        	dto.setResponseContent(item.getServerResponseMsg());
        	dto.setResponseTime(dateFma.format(item.getServerResponseTimestamp()));
        	dto.setReSendRetryCount(item.getResendCount());
            return dto;
        }).collect(Collectors.toList());

        ReportUtils.DocType type = ReportUtils.getEnumDocType(docType);
        InputStream in =  this.getClass().getClassLoader().getResourceAsStream("jasper/msgLogReport.jrxml");
        ReportDto dto = new ReportDto();
        dto.setContentType(ReportUtils.getContentType(type));
        dto.setContent(ReportUtils.report(type, in, msgLogDtos, prams));
        String fileName = "MSG_LOg Report."+docType;
        dto.setFileName(fileName);
        return dto;
    }

}
