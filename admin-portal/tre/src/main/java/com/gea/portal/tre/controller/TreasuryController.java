package com.gea.portal.tre.controller;

import com.gea.portal.tre.entity.ExchangeRateRecord;
import com.gea.portal.tre.service.TreasuryService;
import com.tng.portal.common.constant.PermissionId;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.dto.tre.ExchangeRateFileDto;
import com.tng.portal.common.dto.tre.ExchangeRateListDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Dell on 2018/9/14.
 */
@RestController
@RequestMapping("treasury/v1")
public class TreasuryController {

    @Autowired
    private TreasuryService treasuryService;

    @GetMapping("get-list")
    @ResponseBody
    @PreAuthorize("hasPermission('EXCHANGE_RATE_LISTING',"+ PermissionId.VIEW +")")
    public RestfulResponse<ExchangeRateListDto> getList(@ApiParam(value = "current page number") @RequestParam(required = false) Integer pageNo,
                                                                   @ApiParam(value = "page size") @RequestParam(required = false) Integer pageSize,
                                                                   @ApiParam(value = "status") @RequestParam(required = false) String status,
                                                                   @ApiParam(value = "sort by") @RequestParam(required = false) String sortBy,
                                                                   @ApiParam(value = "Instance") @RequestParam Instance instance,
                                                                   @ApiParam(value = "true--ascend or false--descend") @RequestParam(required = false) Boolean isAscending) throws IllegalAccessException, InvocationTargetException {

        return treasuryService.getPageData(pageNo, pageSize, status, sortBy, instance, isAscending);
    }

    @PostMapping("upload-file")
    @ResponseBody
    @PreAuthorize("hasPermission('UPLOAD_EXCHANGE_RATE_CSV',"+ PermissionId.CREATE +")")
    public RestfulResponse<String> uploadExchangeRateFile(@RequestParam MultipartFile file,
                                                                 @RequestParam Instance instance) throws Exception{
        if (file == null || file.isEmpty()) {
            throw new BusinessException(SystemMsg.ErrorMsg.UPLOAD_FILE_EMPTY.getErrorCode());
        }
        return treasuryService.uploadExchangeRateFile(file,instance);
    }

    @GetMapping("load-exchange-rate")
    @ResponseBody
    @PreAuthorize("hasPermission('EXCHANGE_RATE_LISTING',"+ PermissionId.VIEW +")")
    public RestfulResponse<ExchangeRateFileDto> loadExchangeRateFile(@RequestParam String exchRateFileId,
                                                                     @RequestParam Instance instance) {
        if(StringUtils.isBlank(exchRateFileId)){
            throw new BusinessException(SystemMsg.ErrorMsg.PLEASE_UPLOAD_CSV_FILE.getErrorCode());
        }
        return treasuryService.loadExchangeRateFile(Long.parseLong(exchRateFileId),instance);
    }


    @PostMapping("save-record")
    @ResponseBody
    @PreAuthorize("hasPermission('NEW_EXCHANGE_RATE',"+ PermissionId.CREATE +")")
    public RestfulResponse<List<ExchangeRateRecord>> saveRecord(@RequestParam String exchRateFileId,
                                                                @RequestParam Instance instance,
                                                                @RequestParam(required = false) String requestRemark) {
        if (StringUtils.isBlank(exchRateFileId)) {
            throw new BusinessException(SystemMsg.ErrorMsg.UPLOAD_FILE_EMPTY.getErrorCode());
        }
        return treasuryService.saveExchangeRateRecord(exchRateFileId,instance,requestRemark);
    }

    @GetMapping("check-status")
    @ResponseBody
    @PreAuthorize("hasPermission('NEW_EXCHANGE_RATE',"+ PermissionId.VIEW +")")
    public RestfulResponse<Boolean> checkStatus(@RequestParam Instance instance) {
        return RestfulResponse.ofData(treasuryService.checkStatus(instance));
    }

}
