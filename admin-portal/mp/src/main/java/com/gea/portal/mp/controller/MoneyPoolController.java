package com.gea.portal.mp.controller;

import com.gea.portal.mp.dto.MoneyPoolDetailDto;
import com.gea.portal.mp.dto.MoneyPoolDetailPageDto;
import com.gea.portal.mp.dto.MoneyPoolTransactionDto;
import com.gea.portal.mp.entity.EwpMoneyPool;
import com.gea.portal.mp.entity.EwpMpChangeReq;
import com.gea.portal.mp.entity.EwpPoolAdjustment;
import com.gea.portal.mp.entity.EwpPoolDepositCashOut;
import com.gea.portal.mp.service.EnyCallerService;
import com.gea.portal.mp.service.EwpCallerService;
import com.gea.portal.mp.service.MoneyPoolService;
import com.gea.portal.mp.service.SrvCallerService;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.constant.PermissionId;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.dto.mp.*;
import com.tng.portal.common.dto.srv.BaseServiceDto;
import com.tng.portal.common.enumeration.ApprovalStatus;
import com.tng.portal.common.enumeration.FileTypes;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.MoneyPoolStatus;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.FileUpload;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@RequestMapping("money-pool/v1")
public class MoneyPoolController {

    @Autowired
    private MoneyPoolService moneyPoolService;
    @Autowired
    private EwpCallerService ewpCallerService;
    @Autowired
    private EnyCallerService enyCallerService;
    @Autowired
    private SrvCallerService srvCallerService;
    
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${material.dir}")
    private String materialDir;
    @Value("${file.size}")
    private String fileSize;

    @PostMapping("create-money-pool")
    @ResponseBody
    @PreAuthorize("hasPermission('MONEY_POOL_LISTING',"+ PermissionId.CREATE +")")
    public RestfulResponse<String> createMoneyPool(@RequestBody MoneyPoolDto moneyPoolDto) {
        EwpMoneyPool ewpMoneyPool = moneyPoolService.createMoneyPool(moneyPoolDto, Instance.stringToInstance(moneyPoolDto.getCurrentEnvir()));
        return RestfulResponse.ofData(String.valueOf(ewpMoneyPool.getId()));
    }


    @GetMapping("get-money-pool")
    @ResponseBody
    @PreAuthorize("hasPermission('MONEY_POOL_LISTING',"+ PermissionId.VIEW +")")
    public RestfulResponse<MoneyPoolDto> getMoneyPool(@RequestParam String moneyPoolId) {
        MoneyPoolDto ewpMoneyPool = moneyPoolService.getMoneyPool(moneyPoolId);
        return RestfulResponse.ofData(ewpMoneyPool);
    }

    @PostMapping("update-money-pool-status")
    @ResponseBody
    @PreAuthorize("hasPermission('MONEY_POOL_DETAIL_TRANSACTION',"+ PermissionId.EDIT +")")
    public RestfulResponse<String> updateMoneyPoolStatus(@RequestBody MpChangeReqDto mpChangeReqDto) {
        EwpMpChangeReq ewpMpChangeReq = moneyPoolService.updateMoneyPoolStatus(mpChangeReqDto, Instance.stringToInstance(mpChangeReqDto.getCurrentEnvir()));
        return RestfulResponse.ofData(String.valueOf(ewpMpChangeReq.getId()));
    }

    @PostMapping("create-pool-adjustment")
    @ResponseBody
    @PreAuthorize("hasPermission('ADJUSTMENT',"+ PermissionId.CREATE +")")
    public RestfulResponse<String> createPoolAdjustment(@RequestBody PoolAdjustmentDto poolAdjustmentDto) {
        EwpPoolAdjustment ewpPoolAdjustment = moneyPoolService.createPoolAdjustment(poolAdjustmentDto, Instance.stringToInstance(poolAdjustmentDto.getCurrentEnvir()));
        return RestfulResponse.ofData(String.valueOf(ewpPoolAdjustment.getId()));
    }

    @GetMapping("get-pool-adjustment")
    @ResponseBody
    @PreAuthorize("hasPermission('ADJUSTMENT',"+ PermissionId.VIEW +")")
    public RestfulResponse<List<PoolAdjustmentDto>> getPoolAdjustment(@RequestParam String moneyPoolId) {
        List<PoolAdjustmentDto> ewpPoolAdjustments = moneyPoolService.getPoolAdjustment(moneyPoolId);
        return RestfulResponse.ofData(ewpPoolAdjustments);
    }

    @PostMapping("create-pool-deposit-cash-out")
    @ResponseBody
    @PreAuthorize("hasPermission('DEPOSIT_CASH_OUT',"+ PermissionId.CREATE +")")
    public RestfulResponse<String> createPoolDepositCashOut(@RequestBody PoolDepositCashOutDto poolDepositCashOutDto) {
        EwpPoolDepositCashOut ewpPoolDepositCashOut = moneyPoolService.createPoolDepositCashOut(poolDepositCashOutDto, Instance.stringToInstance(poolDepositCashOutDto.getCurrentEnvir()));
        return RestfulResponse.ofData(String.valueOf(ewpPoolDepositCashOut.getId()));
    }

    @GetMapping("get-pool-deposit-cash-out")
    @ResponseBody
    @PreAuthorize("hasPermission('DEPOSIT_CASH_OUT',"+ PermissionId.VIEW +")")
    public RestfulResponse<List<PoolDepositCashOutDto>> getPoolDepositCashOut(@RequestParam String moneyPoolId) {
        List<PoolDepositCashOutDto> poolDepositCashOutDtos = moneyPoolService.getPoolDepositCashOut(moneyPoolId);
        return RestfulResponse.ofData(poolDepositCashOutDtos);
    }

    @GetMapping("get-detail-money-pool")
    @ResponseBody
    @PreAuthorize("hasPermission('MONEY_POOL_LISTING',"+ PermissionId.VIEW +")")
    public RestfulResponse<MoneyPoolDetailDto> getDetailMoneyPool(@RequestParam String moneyPoolId,
                                                                  @RequestParam Instance instance) {
        MoneyPoolDetailDto ewpMoneyPool = moneyPoolService.getDetailMoneyPool(moneyPoolId, instance);
        return RestfulResponse.ofData(ewpMoneyPool);
    }

    @GetMapping("get-money-pool-List")
    @ResponseBody
    @PreAuthorize("hasPermission('MONEY_POOL_LISTING',"+ PermissionId.VIEW +")")
    public RestfulResponse<PageDatas<MoneyPoolListDto>> getMoneyPoolList(@ApiParam(value = "geaMoneyPoolRefId") @RequestParam(required = false) String geaMoneyPoolRefId,
                                                                         @ApiParam(value = "geaParticipantRefId") @RequestParam(required = false) String geaParticipantRefId,
                                                                         @ApiParam(value = "currency") @RequestParam(required = false) String currency,
                                                                         @ApiParam(value = "Balance") @RequestParam(required = false) String balance,
                                                                         @ApiParam(value = "Money Pool Status") @RequestParam(required = false) MoneyPoolStatus moneyPoolStatus,
                                                                         @ApiParam(value = "Group") @RequestParam(required = false) String group,
                                                                         @ApiParam(value = "current page number") @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                                                         @ApiParam(value = "page size") @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                         @ApiParam(value = "sort by") @RequestParam(required = false) String sortBy,
                                                                         @ApiParam(value = "true--ascend or false--descend") @RequestParam(required = false) Boolean isAscending,
                                                                         @RequestParam Instance instance,
                                                                         @RequestParam(required = false) String approvalStatus) {
        RestfulResponse<PageDatas<MoneyPoolListDto>> restResponse = new RestfulResponse<>();
        PageDatas<MoneyPoolListDto> pageData = moneyPoolService.getMoneyPoolList(geaMoneyPoolRefId, geaParticipantRefId, currency, balance, moneyPoolStatus, group, pageNo, pageSize, sortBy, isAscending, instance, approvalStatus);
        restResponse.setData(pageData);
        restResponse.setSuccessStatus();
        return restResponse;
    }



    @GetMapping("get-all-money-pool-list")
    @ResponseBody
    @PreAuthorize("hasPermission('MONEY_POOL_LISTING',"+ PermissionId.VIEW +")")
    public RestfulResponse<List<MoneyPoolListDto>> getAllMoneyPoolList(@ApiParam(value = "geaParticipantRefId") @RequestParam String geaParticipantRefId,
                                                                       @ApiParam(value = "instance") @RequestParam(required = false) Instance instance,
                                                                       @ApiParam(value = "status") @RequestParam(required = false) String status
                                                                       ) {
    	List<MoneyPoolStatus> statusList = new ArrayList<>();
    	if(StringUtils.isNotBlank(status)){
	    	for(String item : Arrays.asList(status.split(","))){
	    		statusList.add(MoneyPoolStatus.findByValue(item));
	    	}
	    }
    	List<MoneyPoolListDto> pageData = moneyPoolService.getAllMoneyPool(geaParticipantRefId, statusList, instance);
        
        
        RestfulResponse<List<MoneyPoolListDto>> restResponse = new RestfulResponse<>();
        if(null != pageData){
            restResponse.setData(pageData);
        }
        return restResponse;
    }

    @GetMapping("get-all-Participant-list")
    @ResponseBody
    @PreAuthorize("hasPermission('MONEY_POOL_LISTING',"+ PermissionId.VIEW +")")
    public RestfulResponse<List<ParticipantDto>> getAllParticipantList(@ApiParam(value = "status") @RequestParam(required = false) MoneyPoolStatus status,
                                                                       @ApiParam(value = "instance") @RequestParam(required = false) Instance instance) {
        RestfulResponse<List<ParticipantDto>> restResponse = new RestfulResponse<>();
        List<ParticipantDto> pageData = moneyPoolService.getAllParticipantList(instance);
        if(null != pageData){
            restResponse.setData(pageData);
        }
        return restResponse;
    }


    @GetMapping("get-money-pool-processing-status-list")
    @ResponseBody
    @PreAuthorize("hasPermission('MONEY_POOL_LISTING',"+ PermissionId.VIEW +")")
    public RestfulResponse<PageDatas<MoneyPoolListDto>> listMoneyPoolForProcessingStatus(@ApiParam(value = "Money Pool ID") @RequestParam(required = false) String moneyPoolId,
                                                                                         @ApiParam(value = "Participant ID") @RequestParam(required = false) String participantId,
                                                                                         @ApiParam(value = "Participant Name") @RequestParam(required = false) String participantName,
                                                                                         @ApiParam(value = "current page number") @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                                                                         @ApiParam(value = "page size") @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                                         @ApiParam(value = "sort by") @RequestParam(required = false) String sortBy,
                                                                                         @ApiParam(value = "true--ascend or false--descend") @RequestParam(required = false) Boolean isAscending) {
        RestfulResponse<PageDatas<MoneyPoolListDto>> restResponse = new RestfulResponse<>();
        PageDatas<MoneyPoolListDto> pageData = enyCallerService.listMoneyPoolForProcessingStatus(moneyPoolId, participantId, participantName, pageNo, pageSize, sortBy, isAscending, Instance.PRE_PROD);
        restResponse.setData(pageData);
        restResponse.setSuccessStatus();
        return restResponse;
    }

    @GetMapping("get-money-pool-detail-page-list")
    @ResponseBody
    @PreAuthorize("hasPermission('MONEY_POOL_DETAIL_TRANSACTION',"+ PermissionId.VIEW +")")
    public RestfulResponse<PageDatas<MoneyPoolTransactionDto>> listMoneyPoolDetailPageList(@ApiParam(value = "moneyPoolId") @RequestParam(required = true) String moneyPoolId,
           @ApiParam(value = "portalUserId") @RequestParam(required = false) String portalUserId,
           @ApiParam(value = "geaMoneyTransferId") @RequestParam(required = false) String geaMoneyTransferId,
           @RequestParam(required = false) String moneyPoolRefId,
           @RequestParam(required = false) String beginTransactionDateTime,
   	       @RequestParam(required = false) String endTransactionDateTime,
           @RequestParam(required = false) String minimumAmount,
           @RequestParam(required = false) String largestAmount,
           @RequestParam(required = false) String minimumBalanceAfterTransaction,
           @RequestParam(required = false) String largestBalanceAfterTransaction,
           @RequestParam(required = false) String minimumBalanceBeforeTransaction,
           @RequestParam(required = false) String largestBalanceBeforeTransaction,
           @ApiParam(value = "currency") @RequestParam(required = false) String currency,
           @ApiParam(value = "serviceType") @RequestParam(required = false) String serviceId,
           @ApiParam(value = "transactionType") @RequestParam(required = false) String transactionType,
           @ApiParam(value = "page size") @RequestParam(required = false, defaultValue = "10") Integer pageSize,
           @ApiParam(value = "current page number") @RequestParam(required = false, defaultValue = "1") Integer pageNo,
           @ApiParam(value = "instance") @RequestParam(required = false) Instance instance,
           @ApiParam(value = "sort By") @RequestParam(required = false) String sortBy,
           @ApiParam(value = "true--ascend or false--descend") @RequestParam(required = false) Boolean isAscending) {
        MoneyPoolDetailPageDto moneyPoolDetailPageDto = new MoneyPoolDetailPageDto();
        moneyPoolDetailPageDto.setMoneyPoolId(moneyPoolId);
        moneyPoolDetailPageDto.setMoneyPoolRefId(moneyPoolRefId);
        moneyPoolDetailPageDto.setPortalUserId(portalUserId);
        moneyPoolDetailPageDto.setGeaMoneyTransferId(geaMoneyTransferId);
        moneyPoolDetailPageDto.setBeginTransactionDateTime(beginTransactionDateTime);
        moneyPoolDetailPageDto.setEndTransactionDateTime(endTransactionDateTime);
        moneyPoolDetailPageDto.setCurrency(currency);
        moneyPoolDetailPageDto.setServiceType(serviceId);
        moneyPoolDetailPageDto.setTransactionType(transactionType);
        moneyPoolDetailPageDto.setMinimumAmount(minimumAmount);
        moneyPoolDetailPageDto.setLargestAmount(largestAmount);
        moneyPoolDetailPageDto.setMinimumBalanceBeforeTransaction(minimumBalanceBeforeTransaction);
        moneyPoolDetailPageDto.setLargestBalanceBeforeTransaction(largestBalanceBeforeTransaction);
        moneyPoolDetailPageDto.setMinimumBalanceAfterTransaction(minimumBalanceAfterTransaction);
        moneyPoolDetailPageDto.setLargestBalanceAfterTransaction(largestBalanceAfterTransaction);
        RestfulResponse<PageDatas<MoneyPoolTransactionDto>> restResponse;
        restResponse = enyCallerService.getMoneyPoolDetailPageList(moneyPoolDetailPageDto,instance,pageNo, pageSize,sortBy,isAscending);
        return restResponse;
    }

    @GetMapping("get-transaction-enquiry")
    @ResponseBody
    @PreAuthorize("hasPermission('ENQUIRY',"+ PermissionId.VIEW +")")
    public RestfulResponse<PageDatas<MoneyPoolTransactionDto>> getTransactionEnquiry(
    		@ApiParam(value = "GEA Money Record ID") @RequestParam(required = false) String geaMoneyRecordId,
    		@RequestParam(required = false) String moneyPoolRefId,
    		@RequestParam(required = false) String beginTransactionDateTime,
    		@RequestParam(required = false) String endTransactionDateTime,
            @RequestParam(required = false) String minimumAmount,
            @RequestParam(required = false) String largestAmount,
            @RequestParam(required = false) String minimumBalanceAfterTransaction,
            @RequestParam(required = false) String largestBalanceAfterTransaction,
            @RequestParam(required = false) String minimumBalanceBeforeTransaction,
            @RequestParam(required = false) String largestBalanceBeforeTransaction,
    		@ApiParam(value = "currency") @RequestParam(required = false) String currency,
    		@ApiParam(value = "serviceType") @RequestParam(required = false) String serviceId,
    		@ApiParam(value = "transactionType") @RequestParam(required = false) String transactionType,
    		@ApiParam(value = "GEA Money Transfer ID") @RequestParam(required = false) String geaMoneyTransferId,
    		@ApiParam(value = "instance") @RequestParam(required = false) Instance instance,
    		@ApiParam(value = "remark") @RequestParam(required = false) String remark,
    		@ApiParam(value = "current page number") @RequestParam(required = false, defaultValue = "1") Integer pageNo,
    		@ApiParam(value = "page size") @RequestParam(required = false, defaultValue = "10") Integer pageSize,
    		@ApiParam(value = "sort By") @RequestParam(required = false) String sortBy,
    		@ApiParam(value = "true--ascend or false--descend") @RequestParam(required = false) Boolean isAscending
    		) {
        MoneyPoolDetailPageDto moneyPoolDetailPageDto = new MoneyPoolDetailPageDto();
        moneyPoolDetailPageDto.setGeaMoneyTransferId(geaMoneyTransferId);
        moneyPoolDetailPageDto.setMoneyPoolId(geaMoneyRecordId);
        moneyPoolDetailPageDto.setMoneyPoolRefId(moneyPoolRefId);
        moneyPoolDetailPageDto.setBeginTransactionDateTime(beginTransactionDateTime);
        moneyPoolDetailPageDto.setEndTransactionDateTime(endTransactionDateTime);
        moneyPoolDetailPageDto.setCurrency(currency);
        moneyPoolDetailPageDto.setServiceType(serviceId);
        moneyPoolDetailPageDto.setTransactionType(transactionType);
        moneyPoolDetailPageDto.setMinimumAmount(minimumAmount);
        moneyPoolDetailPageDto.setLargestAmount(largestAmount);
        moneyPoolDetailPageDto.setMinimumBalanceBeforeTransaction(minimumBalanceBeforeTransaction);
        moneyPoolDetailPageDto.setLargestBalanceBeforeTransaction(largestBalanceBeforeTransaction);
        moneyPoolDetailPageDto.setMinimumBalanceAfterTransaction(minimumBalanceAfterTransaction);
        moneyPoolDetailPageDto.setLargestBalanceAfterTransaction(largestBalanceAfterTransaction);
        moneyPoolDetailPageDto.setRemark(remark);
        RestfulResponse<PageDatas<MoneyPoolTransactionDto>> restResponse = enyCallerService.getMoneyPoolDetailPageList(moneyPoolDetailPageDto, instance, pageNo, pageSize, sortBy, isAscending);
        return restResponse;
    }

    @PostMapping("upload-material")
    @ResponseBody
    public RestfulResponse<String> uploadMaterial(@RequestParam MultipartFile file) {
    	if (file == null || file.isEmpty()) {
    		throw new BusinessException(SystemMsg.ErrorMsg.UPLOAD_FILE_EMPTY.getErrorCode());
    	}
    	if(file.getSize()>Long.valueOf(fileSize)){
            throw new BusinessException(SystemMsg.ErrorMsg.UPLOAD_FILE_MULTIPART.getErrorCode());
        }
        String name=file.getOriginalFilename();
        int one = name.lastIndexOf('.');
        String type=name.substring((one+1),name.length());
        FileTypes[] s = FileTypes.values();
        for(int i = 0; i< s.length; i++){
            if(s[i].getValue().equalsIgnoreCase(type)){
                try {
                    String fileName = FileUpload.upload(file, materialDir, null, true);
                    return RestfulResponse.ofData(fileName);
                } catch (Exception e) {
                    throw new BusinessException(SystemMsg.ErrorMsg.UPLOAD_FILE_ERROR.getErrorCode());
                }
            }
        }
        throw new BusinessException(SystemMsg.ErrorMsg.UPLOAD_FILE_TYPE_ERROR.getErrorCode());
    }

    @GetMapping("download-material")
    @ResponseBody
    public RestfulResponse<String> downloadMaterialFile(@RequestParam String filePath,
                                                        @RequestParam(required = false) String fileName,
                                                        HttpServletResponse response) {
        if (StringUtils.isBlank(filePath)) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        ServletOutputStream out = null;
        InputStream is = null;
        String filePaths = filePath;
        String fileNames = fileName;
        try {
            filePaths=materialDir + "/" +filePaths;
            String type=filePaths.substring(filePaths.indexOf(".") + 1);

            is = new FileInputStream(new File(filePaths));
            String[] strs = filePaths.split("/");
            if(StringUtils.isBlank(fileNames)){
                fileNames = strs[strs.length - 1];
            }else{
                fileNames = decodingFileName(fileNames, "UTF-8");
            }
            if(type.equalsIgnoreCase(FileTypes.PDF.getValue())){
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline;fileName=" + fileNames);
            }else{
                response.setContentType("image/"+type);
                response.setHeader("Content-Disposition", "attachment;fileName=" + fileNames);
            }

            response.setContentLength(is.available());
            out = response.getOutputStream();
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
        } catch (Exception e) {
            logger.info("downloadMaterialFile",e);
        } finally {
            try {
                if(null != is){
                    is.close();
                }
                if(null != out){
                    out.close();
                }
            } catch (IOException e) {
                logger.info("finally",e);
            }
        }
        return null;
    }

    private static String decodingFileName(String fileName,String encoding){
        try {
            return new String(fileName.getBytes(encoding), "iso8859-1");
        }catch (UnsupportedEncodingException e) {
            return fileName;
    }}

    @GetMapping("get-participant-currency")
    @ResponseBody
    public RestfulResponse<List<String>> getParticipantCurrency(@RequestParam String geaParticipantRefId,
                                                                @ApiParam(value = "instance") @RequestParam Instance instance){
        return ewpCallerService.callGetParticipantCurrency(geaParticipantRefId, instance);
    }

    @GetMapping("get-Money-Pool-Status-list")
    @ResponseBody
    public RestfulResponse<List<String>> getMoneyPoolStatusList() {
        RestfulResponse<List<String>> restfulResponse = new RestfulResponse<>();
        List<String> list = new ArrayList<>();
        MoneyPoolStatus[] s = MoneyPoolStatus.values();
        for(int i = 0; i< s.length; i++){
            if(s[i].name().equals(MoneyPoolStatus.PENDING_FOR_PROCESS.getValue()) || s[i].name().equals(MoneyPoolStatus.REJECTED.getValue())|| s[i].name().equals(MoneyPoolStatus.DORMANT.getValue())){
                continue;
            }
            list.add(s[i].name());
        }
        restfulResponse.setData(list);
        return restfulResponse;
    }

    @GetMapping("get-Approval-Status-list")
    @ResponseBody
    public RestfulResponse<List<String>> getApprovalStatusList() {
        List<String> list = Stream.of(ApprovalStatus.values()).map(ApprovalStatus::getValue).collect(Collectors.toList());
        return RestfulResponse.ofData(list);
    }

    @GetMapping("get-Service-list")
    @ResponseBody
    public RestfulResponse<List<BaseServiceDto>> getServiceList() {
        return srvCallerService.getAllService();
    }

    @GetMapping("get-eny-Service-list")
    @ResponseBody
    public RestfulResponse<List<String>> getEnyServiceList() {
        return enyCallerService.getEnyServiceList();
    }

    @GetMapping("get-eny-transaction-type")
    @ResponseBody
    public RestfulResponse<List<String>> getEnyTransactionType() {
        return enyCallerService.getEnyTransactionType();
    }

}
