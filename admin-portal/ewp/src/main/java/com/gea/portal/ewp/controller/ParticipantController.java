package com.gea.portal.ewp.controller;

import com.gea.portal.ewp.service.EwalletParticipantService;
import com.gea.portal.ewp.service.MpCallerService;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.constant.PermissionId;
import com.tng.portal.common.dto.ewp.*;
import com.tng.portal.common.dto.mp.MoneyPoolListDto;
import com.tng.portal.common.enumeration.*;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Owen on 2018/8/24.
 */
@RestController
@RequestMapping("participant/v1")
public class ParticipantController {
    private static final Logger logger = LoggerFactory.getLogger(ParticipantController.class);
    @Autowired
    private EwalletParticipantService ewalletParticipantService;

    @Autowired
    private MpCallerService mpCallerService;

    @Value("${material.dir}")
    private String materialDir;

    @Value("${file.size}")
    private String fileSize;

    @GetMapping("get-participant-by-name-or-id-list")
    @ResponseBody
    @PreAuthorize("hasPermission('PARTICIPANT_LIST',"+ PermissionId.VIEW +")")
    public RestfulResponse<List<ParticipantDto>> getParticipantByNameOrIdList(@RequestParam Map<String,String> map) {
        RestfulResponse<List<ParticipantDto>> restfulResponse = new RestfulResponse<>();
        List<ParticipantDto> participantDtoList = ewalletParticipantService.getParticipantByNameOrIdList(map.get("geaParticipantRefId"), map.get("participantName"),null, Instance.valueOf(map.get("instance")));
        restfulResponse.setData(participantDtoList);
        return restfulResponse;
    }

    @GetMapping("get-participant-page")
    @ResponseBody
    @PreAuthorize("hasPermission('PARTICIPANT_LIST',"+ PermissionId.VIEW+")")
    public RestfulResponse<PageDatas<ParticipantDto>> getList(
    		@ApiParam(value="current page number")@RequestParam(required = false) Integer pageNo,
    		@ApiParam(value="page size")@RequestParam(required = false) Integer pageSize,
			@ApiParam(value="Participant ID")@RequestParam(required = false) String geaParticipantRefId,
			@ApiParam(value="Participant Name")@RequestParam(required = false) String participantName,
            @ApiParam(value="Participant Status")@RequestParam(required = false) String participantStatus,
            @ApiParam(value="approval Status")@RequestParam(required = false) String approvalStatus,
			@ApiParam(value="Instance PRE_PROD or PROD")@RequestParam(required = true) Instance instance,
    		@ApiParam(value="sort by")@RequestParam(required = false) String sortBy,
    		@ApiParam(value="is ascending")@RequestParam(required = false) Boolean isAscending){
        RestfulResponse<PageDatas<ParticipantDto>> restResponse = new RestfulResponse<>();
        PageDatas<ParticipantDto> pageData = ewalletParticipantService.listParticipant(pageNo, pageSize, geaParticipantRefId, participantName, participantStatus,approvalStatus,instance,sortBy,isAscending);
        restResponse.setData(pageData);
        restResponse.setSuccessStatus();
        return restResponse;
    }

    @GetMapping("get-participant-list")
    @ResponseBody
    @PreAuthorize("hasPermission('PARTICIPANT_LIST',"+ PermissionId.VIEW+")")
    public List<ParticipantDto> getParticipantByNameOrIdList(@ApiParam(value="geaParticipantRefId")@RequestParam(required = false) String geaParticipantRefId,
                                                              @ApiParam(value="participantName")@RequestParam(required = false) String participantName,
                                                              @ApiParam(value="instance")@RequestParam(required = false) Instance instance){
        return ewalletParticipantService.getParticipantByNameOrIdList(geaParticipantRefId,participantName,null, instance);
    }

    @PostMapping("update-participant-status")
    @ResponseBody
    @PreAuthorize("hasPermission('PARTICIPANT_LIST',"+ PermissionId.EDIT+")")
    public RestfulResponse<Map<String,Object>> uploadParticipantStatus(@RequestBody StatusChangeDto statusChangeDto){
    	Map<String,Object> map = new HashMap<>();
    	try{
        	Long res = ewalletParticipantService.updateParticipantStatus(statusChangeDto);
        	map.put("id", res);
        	return RestfulResponse.ofData(map);
    	}catch(BusinessException e){
    		if(e.getErrorcode()==SystemMsg.EwpErrorMsg.INCOMPLETE_DATA.getErrorCode()){
    			map.put("model", e.getTemplateInput());
    			return RestfulResponse.ofData(map);
    		}else{
    			throw e;
    		}
    	}
    }

    @PostMapping("update-gateway")
    @ResponseBody
    @PreAuthorize("hasPermission('PARTICIPANT_API_GATEWAY_SETTING',"+ PermissionId.EDIT +")")
    public RestfulResponse<String> updateApiGatewaySetting(@RequestBody ApiGatewaySettingDto postDto){
        return ewalletParticipantService.updateApiGatewaySetting(postDto);
    }

    @PostMapping("create-company-info")
    @ResponseBody
    @PreAuthorize("hasPermission('PARTICIPANT_FULL_COMPANY_INFORMATION',"+ PermissionId.CREATE+")")
    public RestfulResponse<String> createFullCompanyInformation(@RequestBody FullCompanyInformationDto postDto){
        return ewalletParticipantService.createFullCompanyInformation(postDto);
    }

    @GetMapping("get-company-info")
    @ResponseBody
    @PreAuthorize("hasPermission('PARTICIPANT_FULL_COMPANY_INFORMATION',"+ PermissionId.VIEW+")")
    public RestfulResponse<FullCompanyInfoDto> getFullCompanyInfomation(@ApiParam(value="participant id")@RequestParam(required = true) String participantId,
                                                                                     @RequestParam(required = false) Instance instance){
        return ewalletParticipantService.getFullCompanyInfomation(participantId, instance == null ? Instance.PRE_PROD : instance);
    }

    @PostMapping("update-company-info")
    @ResponseBody
    @PreAuthorize("hasPermission('PARTICIPANT_FULL_COMPANY_INFORMATION',"+ PermissionId.EDIT +")")
    public RestfulResponse<String> updateFullCompanyInformation(@RequestBody FullCompanyInformationDto postDto){
        return ewalletParticipantService.updateFullCompanyInformation(postDto);
    }

	@GetMapping("get-gateway")
	@ResponseBody
    @PreAuthorize("hasPermission('PARTICIPANT_API_GATEWAY_SETTING',"+ PermissionId.VIEW+")")
    public RestfulResponse<GatewaySettingDto> getGatewaySetting(@ApiParam(value="participant id")@RequestParam(required = true) String participantId,
                                                                         @RequestParam(required = false) Instance instance){
	    return ewalletParticipantService.getGatewaySetting(participantId, instance == null ? Instance.PRE_PROD : instance);
	}

	@GetMapping("get-ServiceSetting")
	@ResponseBody
    @PreAuthorize("hasPermission('PARTICIPANT_SERVICE_SETTING',"+ PermissionId.VIEW+")")
    public RestfulResponse<ServiceSettingRequestDto> getServiceSetting(@ApiParam(value="participant id")@RequestParam(required = true) String participantId,
                                                                       @RequestParam(required = false) Instance instance){
	    return ewalletParticipantService.getServiceSetting(participantId, instance == null ? Instance.PRE_PROD : instance);
	}

    @PostMapping("update-service-setting")
    @ResponseBody
    @PreAuthorize("hasPermission('PARTICIPANT_SERVICE_SETTING',"+ PermissionId.EDIT +")")
    public RestfulResponse<String> updateServiceSetting(@RequestBody ServiceSettingRequestDto postDto){
        return ewalletParticipantService.updateServiceSetting(postDto);
    }

    @GetMapping("get-service-assignment")
    @ResponseBody
    @PreAuthorize("hasPermission('PARTICIPANT_SERVICE_ASSIGNMENT',"+ PermissionId.VIEW+")")
    public RestfulResponse<ServiceAssignmentDto> getServiceAssignment(@ApiParam(value="participant id")@RequestParam(required = true) String participantId,
                                                                      @RequestParam(required = false) Instance instance){
        return ewalletParticipantService.getServiceAssignment(participantId, instance == null ? Instance.PRE_PROD : instance);
    }

    @PostMapping("update-service-assignment")
    @ResponseBody
    @PreAuthorize("hasPermission('PARTICIPANT_SERVICE_ASSIGNMENT',"+ PermissionId.EDIT +")")
    public RestfulResponse<String> updateServiceAssignment(@RequestBody ServiceAssignmentDto postDto){
        return ewalletParticipantService.updateServiceAssignment(postDto);
    }

    @PostMapping("update-cut-off-time")
    @ResponseBody
    @PreAuthorize("hasPermission('SETTLEMENT_CUT_OFF_TIME_SETTING',"+ PermissionId.EDIT +")")
    public RestfulResponse<String> updateCutOffTime(@RequestParam String participantId, @RequestParam String cutOffTime,@RequestParam Instance instance,@RequestParam(required = false) String requestRemark){
        return ewalletParticipantService.updateCutOffTime(participantId, cutOffTime,instance,requestRemark);
    }

    @GetMapping("get-cut-off-time")
    @ResponseBody
    @PreAuthorize("hasPermission('SETTLEMENT_CUT_OFF_TIME_SETTING',"+ PermissionId.VIEW+")")
    public RestfulResponse<CutOffTimeDto> getCutOffTime(@RequestParam String participantId,
                                                 @RequestParam(required = false) Instance instance){
        return ewalletParticipantService.getCutOffTime(participantId, instance == null ? Instance.PRE_PROD : instance);
    }


    @PostMapping("deploy-to-production")
    @ResponseBody
    public RestfulResponse<String> deployToProduction(@RequestBody(required = false) ApiGatewaySettingDto postDto){
        return ewalletParticipantService.deployToProduction(postDto);
    }

    @PostMapping("upload-material")
    @ResponseBody
    public RestfulResponse<String> uploadMaterial(@RequestParam MultipartFile file){
    	if (file == null || file.isEmpty()) {
    		throw new BusinessException(SystemMsg.ErrorMsg.UPLOAD_FILE_EMPTY.getErrorCode());
    	}
        if(file.getSize()>Long.valueOf(fileSize)){
            throw new BusinessException(SystemMsg.ErrorMsg.UPLOAD_FILE_MULTIPART.getErrorCode());
        }
        String name=file.getOriginalFilename();
        int one = name.lastIndexOf('.');
        String type=name.substring((one + 1), name.length());
        FileTypes[] s = FileTypes.values();
        for(int i = 0; i< s.length; i++){
            if(s[i].getValue().equalsIgnoreCase(type)){
                return ewalletParticipantService.uploadMaterial(file);
            }
        }
        throw new BusinessException(SystemMsg.ErrorMsg.UPLOAD_FILE_TYPE_ERROR.getErrorCode());
    }

    @GetMapping("download-material")
    @ResponseBody
    public RestfulResponse<String> loadMaterialFile(@RequestParam String filePath,
                                                    @RequestParam(required = false) String fileName,
                                                    HttpServletResponse response) {
        String code = "/";
        String filePaths = materialDir + code +filePath;
        String fileNames = null;
        try (InputStream is = new FileInputStream(new File(filePaths))){
            int one = filePaths.lastIndexOf('.');
            String type=filePaths.substring((one + 1), filePaths.length());

            String[] strs = filePaths.split("/");
            if(StringUtils.isBlank(fileName)){
                fileNames = strs[strs.length - 1];
            }else{
                fileNames = decodingFileName(fileName, "UTF-8");
            }
            if(type.equalsIgnoreCase(FileTypes.PDF.getValue())){
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline;fileName=" + fileNames);
            }else{
                response.setContentType("image/"+type);
                response.setHeader("Content-Disposition", "attachment;fileName=" + fileNames);
            }

            response.setContentLength(is.available());
            ServletOutputStream out = response.getOutputStream();
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
        } catch (Exception e) {
            logger.error("loadMaterialFile",e);
        }
        return null;
    }

    private static String decodingFileName(String fileName,String encoding){
        try {
            return new String(fileName.getBytes(encoding), "iso8859-1");
        }catch (UnsupportedEncodingException e) {
            return fileName;
        }}

    @GetMapping("check-edit")
    @ResponseBody
    public RestfulResponse<Boolean> checkEdit(@ApiParam(value="participant id")@RequestParam String participantId,
                                                                 @RequestParam(required = false) Instance instance,
                                                                 @RequestParam(required = false) String type) {
        Integer response = ewalletParticipantService.checkEdit(participantId, instance,type);
        if(response != 0){
        	throw new BusinessException(response);
        }
        return RestfulResponse.nullData();
    }

    @GetMapping("get-all-money-pool-list")
    @ResponseBody
    public RestfulResponse<List<MoneyPoolListDto>> getAllMoneyPoolList(@ApiParam(value = "geaParticipantRefId") @RequestParam(required = false) String geaParticipantRefId,
                                                                       @ApiParam(value = "instance") @RequestParam(required = false) Instance instance) {
        List<MoneyPoolListDto> list = null;
		try {
			list = mpCallerService.callGetAllMoneyPoolList(geaParticipantRefId, instance).getData();
		} catch (Exception e) {
			throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
		}
        return RestfulResponse.ofData(list);
    }

    @GetMapping("get-Participant-Status-list")
    @ResponseBody
    public RestfulResponse<List<String>> getParticipantStatusList() {
        RestfulResponse<List<String>> restfulResponse = new RestfulResponse<>();
        List<String> list = new ArrayList<>();
        ParticipantStatus [] s = ParticipantStatus.values();
        for(int i = 0; i< s.length; i++){
            if(!s[i].equals(ParticipantStatus.PENDING_FOR_PROCESS) && !s[i].equals(ParticipantStatus.REJECTED)){
                list.add(s[i].name());
            }
        }
        restfulResponse.setData(list);
        return restfulResponse;
    }

    @GetMapping("get-Approval-Status-list")
    @ResponseBody
    public RestfulResponse<List<String>> getApprovalStatusList(Instance instance) {
        List<String> list = Stream.of(RequestApprovalStatus.values()).map(RequestApprovalStatus::getListView).distinct().collect(Collectors.toList());
        if(instance == Instance.PROD){
        	list.add(RequestApprovalStatus.ST);
        }
        return RestfulResponse.ofData(list);
    }
    
    @GetMapping("gen-api-gateway-key")
    @ResponseBody
    public RestfulResponse<String> genApiGatewayKey() {
    	String key = UUID.randomUUID().toString();
        return RestfulResponse.ofData(key);
    }
    
    @GetMapping("is-complete-data")
    @ResponseBody
    public RestfulResponse<Map<ApprovalType,Boolean>> isCompleteData(@RequestParam(required = true) Long participantId,@RequestParam(required = false) Instance instance){
    	Map<ApprovalType,Boolean> map =  ewalletParticipantService.isCompleteData(participantId, instance,null);
    	return RestfulResponse.ofData(map);
    }

}
