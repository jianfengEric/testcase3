package com.tng.portal.sms.controller;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tng.portal.ana.util.ToolUtil;
import com.tng.portal.ana.vo.SMSJobQueryVo;
import com.tng.portal.ana.vo.SMSQueryParamVo;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.FileUpload;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.common.vo.wfl.request.SMSJobInputVo;
import com.tng.portal.sms.constant.SystemMsg;
import com.tng.portal.sms.service.JobService;
import com.tng.portal.sms.vo.UploadRespVo;

@Controller
@RequestMapping("/job")
public class JobController {
	
	@Autowired
	private JobService jobService;

	private static final String FILE_PATH = PropertiesUtil.getAppValueByKey("file.path");
	
	/**
	 * Query sms job list
	 * @param vo sms query param vo
	 * @return
	 */
    @RequestMapping(value = "query",method = RequestMethod.POST)
    @ResponseBody
    public RestfulResponse<PageDatas<SMSJobQueryVo>> listJobsByPage(@RequestBody SMSQueryParamVo vo){
    	RestfulResponse<PageDatas<SMSJobQueryVo>> restResponse = new RestfulResponse<>();
    	PageDatas<SMSJobQueryVo> pageDatas = jobService.getJobsByPage(vo);
    	restResponse.setData(pageDatas);
        restResponse.setSuccessStatus();
        return restResponse;
    }

	/**
	 * add sms job
	 * @param vo sms job info
	 * @return
	 */
	@RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    public RestfulResponse<Boolean> addJob(@RequestBody SMSJobInputVo vo) {
    	RestfulResponse<Boolean> restResponse = new RestfulResponse<>();
    	jobService.addJob(vo);
    	restResponse.setData(true);
        restResponse.setSuccessStatus();
        return restResponse;
    }
	
	@RequestMapping(value = "/uploadMobileNumberFile", method = RequestMethod.POST)
	public @ResponseBody RestfulResponse<UploadRespVo> uploadMobileNumberFile(@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request) {
		RestfulResponse<UploadRespVo> restResponse = new RestfulResponse<>();
		if (file == null || file.isEmpty()) 
			throw new BusinessException(SystemMsg.ServerErrorMsg.NULL_FILE_ERROR.getErrorCode());
		
		if(!ToolUtil.isCsvFile(file.getOriginalFilename()))
			throw new BusinessException(SystemMsg.ServerErrorMsg.INVALID_FILE_FORMAT.getErrorCode());
		
		String fileName = "";
		try {
			fileName = FileUpload.upload(file, FILE_PATH, null, true);
		} catch (Exception e) {
			throw new BusinessException(SystemMsg.ServerErrorMsg.UPLOAD_FILE_ERROR.getErrorCode());
		}
		
		SMSJobInputVo vo = new SMSJobInputVo();
		vo.setUploadFileName(fileName);
		restResponse.setSuccessStatus();
		restResponse.setData(new UploadRespVo(jobService.assembMobileNumber(vo).split(",").length, fileName));
		return restResponse;
	}
	
	@RequestMapping(value = "/terminate",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<Boolean> terminateJob(Long id) {
    	RestfulResponse<Boolean> restResponse = new RestfulResponse<>();
    	jobService.terminateJob(id);
        restResponse.setSuccessStatus();
        return restResponse;
    }
	
	@RequestMapping(value = "/mobile",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<String> findMobile(@RequestParam(required = true) Long id,
    		@RequestParam(required = true) String status,
    		@RequestParam(required = true) String mobile) {
    	RestfulResponse<String> restResponse = new RestfulResponse<>();
    	restResponse.setData(jobService.findMobile(id, status, mobile));
        restResponse.setSuccessStatus();
        return restResponse;
    }
	
	@RequestMapping(value = "/resend",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<Boolean> resend(@RequestParam(required = true) String type, //JobId/Date/Month/Sender/SMS provider
    		@RequestParam(required = true) String key) {
    	RestfulResponse<Boolean> restResponse = new RestfulResponse<>();
    	jobService.resend(type, key);
    	restResponse.setData(true);
        restResponse.setSuccessStatus();
        return restResponse;
    }
	
}
