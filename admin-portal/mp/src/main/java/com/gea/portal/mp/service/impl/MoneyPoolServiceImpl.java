package com.gea.portal.mp.service.impl;

import com.gea.portal.mp.dto.MoneyPoolDetailDto;
import com.gea.portal.mp.entity.*;
import com.gea.portal.mp.repository.*;
import com.gea.portal.mp.service.DpyCallerService;
import com.gea.portal.mp.service.EwpCallerService;
import com.gea.portal.mp.service.MoneyPoolService;
import com.gea.portal.mp.service.RequestApprovalService;
import com.tng.portal.ana.bean.Account;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.dto.ewp.EwpDetailDto;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.dto.ewp.ServiceSettingDto;
import com.tng.portal.common.dto.mp.*;
import com.tng.portal.common.enumeration.*;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by Jimmy on 2018/9/3.
 */
@Service
@Transactional
public class MoneyPoolServiceImpl implements MoneyPoolService {

    private static final Logger logger = LoggerFactory.getLogger(MoneyPoolServiceImpl.class);
    @Value("${gea.api.api_key}")
    private String apiKey;

    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;

    @Autowired
    private RequestApprovalService requestApprovalService;

    @Autowired
    private EwpMoneyPoolRepository ewpMoneyPoolRepository;
    @Autowired
    private EwpPoolAdjustmentRepository ewpPoolAdjustmentRepository;
    @Autowired
    private EwpMaterialRepository ewpMaterialRepository;
    @Autowired
    private EwpPoolDepositCashOutRepository ewpPoolDepositCashOutRepository;
    @Autowired
    private EwpMpChangeReqRepository ewpMpChangeReqRepository;
    @Autowired
    private EwpRecordEnvMapRepository ewpRecordEnvMapRepository;
    @Autowired
    private RequestApprovalRepository requestApprovalRepository;
    @Autowired
    private EwpPoolDeploymentRepository ewpPoolDeploymentRepository;
    @Autowired
    private EwpCallerService ewpCallerService;
    @Autowired
    private DpyCallerService dpyCallerService;

    @Autowired
    @Qualifier("httpClientUtils")
    private HttpClientUtils httpClientUtils;

    @Value("${pre.gea.balance.url}")
    private String preGeaBalanceUrl;

    @Value("${pro.gea.balance.url}")
    private String proGeaBalanceUrl;

    private String genGeaId(String geaParticipantRefId,String baseCurrency,Instance currentEnvir){
        String prefix = geaParticipantRefId + "-" + baseCurrency;
        String currentId = ewpMoneyPoolRepository.findMaxGeaRefId(prefix+"%",currentEnvir);
        if(StringUtils.isBlank(currentId)){
            return prefix + "01";
        }
        String sequence = currentId.replace(prefix, "");
        // Create next sequence 
        Integer next = Integer.valueOf(sequence) + 1 ;
        if(next > 99){  // Exceed the upper limit 
            throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }
        return prefix + String.format("%02d", next);
    }
    @Override
    public EwpMoneyPool createMoneyPool(MoneyPoolDto moneyPoolDto, Instance instance) {
        if (moneyPoolDto == null
                || StringUtils.isBlank(moneyPoolDto.getGeaParticipantRefId())
                || StringUtils.isBlank(moneyPoolDto.getBaseCurrency())
                || moneyPoolDto.getAlertLine() == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"parameter"});        }

        if(this.hasPending(moneyPoolDto.getGeaParticipantRefId(), instance, null)){
            throw new BusinessException(SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        RestfulResponse<Boolean> response = ewpCallerService.callHasPending(moneyPoolDto.getGeaParticipantRefId(), instance.toString());
        if(response.getData()){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        if(instance==Instance.PROD && !dpyCallerService.callIsDeploy(moneyPoolDto.getGeaParticipantRefId(), instance).getData()){
        	throw new BusinessException(SystemMsg.MpErrorMsg.NO_DEPLOY.getErrorCode());
        }

        EwpMoneyPool ewpMoneyPool = new EwpMoneyPool();
        ewpMoneyPool.setGeaParticipantRefId(moneyPoolDto.getGeaParticipantRefId());
        ewpMoneyPool.setGeaMoneyPoolRefId(this.genGeaId(moneyPoolDto.getGeaParticipantRefId(), moneyPoolDto.getBaseCurrency(),instance));
        ewpMoneyPool.setBaseCurrency(moneyPoolDto.getBaseCurrency());
        ewpMoneyPool.setAlertLine(moneyPoolDto.getAlertLine());
        ewpMoneyPool.setStatus(MoneyPoolStatus.PENDING_FOR_PROCESS);
        ewpMoneyPool.setCurrentEnvir(instance);
        ewpMoneyPool.setCreateBy(userService.getLoginAccountId());
        ewpMoneyPool.setCreateDate(new Date());
        ewpMoneyPoolRepository.save(ewpMoneyPool);
        requestApprovalService.saveMoneyPoolRequestApproval(moneyPoolDto.getGeaParticipantRefId(), ewpMoneyPool, instance,moneyPoolDto.getRequestRemark());
        return ewpMoneyPool;
    }

    @Override
    public List<MoneyPoolDto> getParticipantMoneyPool(String geaParticipantRefId, Instance instance) {
        if (StringUtils.isBlank(geaParticipantRefId)) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"geaParticipantRefId"});
        }
        List<EwpMoneyPool> ewpMoneyPool = ewpMoneyPoolRepository.findByGeaParticipantRefId(geaParticipantRefId,instance);
        List<MoneyPoolDto> moneyPoolDtoList = new ArrayList<>();
        for(EwpMoneyPool item : ewpMoneyPool){
            MoneyPoolDto dto = getMoneyPoolDto(item);
            moneyPoolDtoList.add(dto);
        }
        return moneyPoolDtoList;
    }

    @Override
    public MoneyPoolDto getMoneyPool(String moneyPoolId) {
        if (StringUtils.isBlank(moneyPoolId)) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"moneyPoolId"});
        }
        EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findOne(Long.valueOf(moneyPoolId));
        return getMoneyPoolDto(ewpMoneyPool);
    }

    @Override
    public EwpMpChangeReq updateMoneyPoolStatus(MpChangeReqDto mpChangeReqDto, Instance instance) {
        if (mpChangeReqDto == null
                || StringUtils.isBlank(mpChangeReqDto.getMoneyPoolId())
                || StringUtils.isBlank(mpChangeReqDto.getToStatus())
                || mpChangeReqDto.getToAlertLine() == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"MpChangeReqDto or MoneyPoolId or ToStatus or AlertLine"});
        }
        EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findOne(Long.valueOf(mpChangeReqDto.getMoneyPoolId()));
        if (ewpMoneyPool == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwpMoneyPool"});
        }
        if(hasPending(ewpMoneyPool.getGeaParticipantRefId(), instance, null)){
            throw new BusinessException(SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        RestfulResponse<Boolean> response = ewpCallerService.callHasPending(ewpMoneyPool.getGeaParticipantRefId(), instance.toString());
        if(response.getData()){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        if(instance==Instance.PROD && !dpyCallerService.callIsDeploy(ewpMoneyPool.getGeaParticipantRefId(), instance).getData()){
        	throw new BusinessException(SystemMsg.MpErrorMsg.NO_DEPLOY.getErrorCode());
        }

        EwpMpChangeReq ewpMpChangeReq = new EwpMpChangeReq();
        ewpMpChangeReq.setEwpMoneyPool(ewpMoneyPool);
        ewpMpChangeReq.setToAlertLine(mpChangeReqDto.getToAlertLine());
        ewpMpChangeReq.setToStatus(mpChangeReqDto.getToStatus());
        ewpMpChangeReq.setStatus(MoneyPoolStatus.PENDING_FOR_PROCESS);
        ewpMpChangeReq.setCurrentEnvir(instance);
        ewpMpChangeReq.setCreateBy(userService.getLoginAccountId());
        ewpMpChangeReq.setCreateDate(new Date());
        ewpMpChangeReqRepository.save(ewpMpChangeReq);
        requestApprovalService.saveMpChangeRequestApproval(mpChangeReqDto.getGeaParticipantRefId(), ewpMpChangeReq, instance,mpChangeReqDto.getRequestRemark());
        return ewpMpChangeReq;
    }

    private MoneyPoolDto getMoneyPoolDto(EwpMoneyPool ewpMoneyPool) {
        MoneyPoolDto moneyPoolDto = new MoneyPoolDto();
        moneyPoolDto.setGeaParticipantRefId(ewpMoneyPool.getGeaParticipantRefId());
        moneyPoolDto.setBaseCurrency(ewpMoneyPool.getBaseCurrency());
        moneyPoolDto.setAlertLine(ewpMoneyPool.getAlertLine());
        moneyPoolDto.setStatus(ewpMoneyPool.getStatus().getValue());
        moneyPoolDto.setCurrentEnvir(ewpMoneyPool.getCurrentEnvir().getValue());
        moneyPoolDto.setGeaMoneyPoolRefId(ewpMoneyPool.getGeaMoneyPoolRefId());
        return moneyPoolDto;
    }

    @Override
    public EwpPoolAdjustment createPoolAdjustment(PoolAdjustmentDto poolAdjustmentDto, Instance instance) {
        if (poolAdjustmentDto == null
                || StringUtils.isBlank(poolAdjustmentDto.getMoneyPoolId())
                || poolAdjustmentDto.getAmount() == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"PoolAdjustmentDto or MoneyPoolId or Amount"});
        }
        EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findOne(Long.valueOf(poolAdjustmentDto.getMoneyPoolId()));
        if (ewpMoneyPool == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwpMoneyPool"});
        }
        if(ewpMoneyPool.getStatus()!=MoneyPoolStatus.ACTIVE){
        	throw new BusinessException(SystemMsg.MpErrorMsg.CANONT_ADJUSTMENT.getErrorCode());
        }
        if(hasPending(ewpMoneyPool.getGeaParticipantRefId(), instance, null)){
            throw new BusinessException(SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        RestfulResponse<Boolean> response = ewpCallerService.callHasPending(ewpMoneyPool.getGeaParticipantRefId(), instance.toString());
        if(response.getData()){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        if(!dpyCallerService.callIsDeploy(poolAdjustmentDto.getGeaParticipantRefId(), instance).getData()){
        	throw new BusinessException(SystemMsg.MpErrorMsg.NO_DEPLOY.getErrorCode());
        }

        EwpPoolAdjustment ewpPoolAdjustment = saveEwpPoolAdjustment(ewpMoneyPool, poolAdjustmentDto, instance);
        requestApprovalService.savePoolAdjustmentRequestApproval(poolAdjustmentDto.getGeaParticipantRefId(), ewpPoolAdjustment, instance, poolAdjustmentDto.getRequestRemark());
        return ewpPoolAdjustment;
    }

    @Override
    public List<PoolAdjustmentDto> getPoolAdjustment(String moneyPoolId) {
        if (StringUtils.isBlank(moneyPoolId)) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"moneyPoolId"});
        }
        EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findOne(Long.valueOf(moneyPoolId));
        if (ewpMoneyPool == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwpMoneyPool"});
        }
        List<EwpPoolAdjustment> poolAdjustments = ewpPoolAdjustmentRepository.findByEwpMoneyPool(ewpMoneyPool);
        return poolAdjustments.stream().map(item -> {
            PoolAdjustmentDto poolAdjustmentDto = new PoolAdjustmentDto();
            poolAdjustmentDto.setMoneyPoolId(String.valueOf(item.getEwpMoneyPool().getId()));
            poolAdjustmentDto.setAdjustType(item.getAdjustType());
            poolAdjustmentDto.setAmount(item.getAmount());
            poolAdjustmentDto.setStatus(item.getStatus().getValue());
            poolAdjustmentDto.setCurrentEnvir(item.getCurrentEnvir().getValue());
            List<EwpMaterial> ewpMaterialList = ewpMaterialRepository.findByEwpPoolAdjustmentId(item.getId());
            if (!ewpMaterialList.isEmpty()) {
                List<MaterialDto> materialDtoList = ewpMaterialList.stream().map(material -> {
                    MaterialDto materialDto = getMaterialDto(material);
                    materialDto.setEwpPoolAdjustmentId(String.valueOf(item.getId()));
                    return materialDto;
                }).collect(Collectors.toList());

                List<MaterialDto> materialDtos =new ArrayList<>();
                StringBuilder materialFilename = new StringBuilder();
                StringBuilder filePath = new StringBuilder();
                for (MaterialDto materialDto : materialDtoList) {
                    materialFilename.append(","+materialDto.getMaterialFilename());
                    filePath.append(","+materialDto.getFilePath());
                }
                String finalMaterialFilename = materialFilename.substring(1,materialFilename.length());
                String finalFilePath = filePath.substring(1,filePath.length());
                materialDtoList.stream().findFirst().ifPresent(ite -> {
                    ite.setFilePath(finalFilePath);
                    ite.setMaterialFilename(finalMaterialFilename);
                    materialDtos.add(ite);
                });
                poolAdjustmentDto.setMaterialDtos(materialDtos);
            }
            return poolAdjustmentDto;
        }).collect(Collectors.toList());
    }

    private MaterialDto getMaterialDto(EwpMaterial ewpMaterial) {
        MaterialDto materialDto = new MaterialDto();
        materialDto.setMaterialFilename(ewpMaterial.getMaterialFilename());
        materialDto.setMaterialType(ewpMaterial.getMaterialType());
        materialDto.setMaterialDesc(materialDto.getMaterialDesc());
        materialDto.setFilePath(ewpMaterial.getFilePath());
        return materialDto;
    }

    private EwpPoolAdjustment saveEwpPoolAdjustment(EwpMoneyPool ewpMoneyPool, PoolAdjustmentDto poolAdjustmentDto, Instance instance) {
        EwpPoolAdjustment ewpPoolAdjustment = new EwpPoolAdjustment();
        ewpPoolAdjustment.setEwpMoneyPool(ewpMoneyPool);
        ewpPoolAdjustment.setAdjustType(poolAdjustmentDto.getAdjustType());
        ewpPoolAdjustment.setAmount(poolAdjustmentDto.getAmount());
        ewpPoolAdjustment.setStatus(MoneyPoolStatus.PENDING_FOR_PROCESS);
        ewpPoolAdjustment.setCurrentEnvir(instance);
        ewpPoolAdjustment.setGeaTxRefNo(UUID.randomUUID().toString());
        ewpPoolAdjustment.setCreateBy(userService.getLoginAccountId());
        ewpPoolAdjustment.setCreateDate(new Date());
        ewpPoolAdjustmentRepository.save(ewpPoolAdjustment);
        List<MaterialDto> materialDtoList = poolAdjustmentDto.getMaterialDtos();
        if (materialDtoList != null && !materialDtoList.isEmpty()) {
            Account account = userService.getCurrentAccountInfo();
            List<EwpMaterial> ewpMaterialList=new ArrayList<>();
            for (MaterialDto materialDto : materialDtoList) {
                ewpMaterialList=getEwpMaterial(materialDto, account);
                for (EwpMaterial material : ewpMaterialList) {
                    material.setEwpPoolAdjustmentId(ewpPoolAdjustment.getId());
                }
            }
            ewpMaterialRepository.save(ewpMaterialList);
        }
        return ewpPoolAdjustment;
    }


    private List<EwpMaterial> getEwpMaterial(MaterialDto materialDto, Account account){
        List<EwpMaterial> ewpMaterials=new ArrayList<>();
        if(materialDto.getFilePath()!=null){
            String[] filePaths =materialDto.getFilePath().split(",");
            String[] fileNames =materialDto.getMaterialFilename().split(",");
            if (filePaths.length>0){
                for(int i=0;i<filePaths.length;i++){
                    EwpMaterial ewpMaterial = new EwpMaterial();
                    ewpMaterial.setMaterialFilename(fileNames[i]);
                    ewpMaterial.setMaterialDesc(materialDto.getMaterialDesc());
                    ewpMaterial.setFilePath(filePaths[i]);
                    ewpMaterial.setCreateBy(account.getAccountId());
                    ewpMaterial.setCreateDate(new Date());
                    int one = filePaths[i].lastIndexOf('.');
                    String type=filePaths[i].substring((one+1),filePaths[i].length());
                    ewpMaterial.setMaterialType(type.toUpperCase());
                    ewpMaterials.add(ewpMaterial);
                }
            }
        }
        else{
            EwpMaterial ewpMaterial = new EwpMaterial();
            ewpMaterial.setMaterialFilename(materialDto.getMaterialFilename());
            ewpMaterial.setMaterialDesc(materialDto.getMaterialDesc());
            ewpMaterial.setFilePath(materialDto.getFilePath());
            ewpMaterial.setCreateBy(account.getAccountId());
            ewpMaterial.setCreateDate(new Date());
            String filePath=materialDto.getFilePath();
            int one = filePath.lastIndexOf('.');
            String type=filePath.substring((one+1),filePath.length());
            ewpMaterial.setMaterialType(type.toUpperCase());
            ewpMaterials.add(ewpMaterial);
        }
        return ewpMaterials;
    }

    @Override
    public EwpPoolDepositCashOut createPoolDepositCashOut(PoolDepositCashOutDto poolDepositCashOutDto, Instance instance) {
        if (poolDepositCashOutDto == null
                || StringUtils.isBlank(poolDepositCashOutDto.getMoneyPoolId())
                || poolDepositCashOutDto.getAmount() == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"PoolDepositCashOutDto or MoneyPoolId or Amount"});
        }

        EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findOne(Long.valueOf(poolDepositCashOutDto.getMoneyPoolId()));
        if (ewpMoneyPool == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwpMoneyPool"});
        }
        if(ewpMoneyPool.getStatus()!=MoneyPoolStatus.ACTIVE){
        	throw new BusinessException(SystemMsg.MpErrorMsg.CANONT_ADJUSTMENT.getErrorCode());
        }
        if(hasPending(ewpMoneyPool.getGeaParticipantRefId(), instance, null)){
            throw new BusinessException(SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        RestfulResponse<Boolean> response = ewpCallerService.callHasPending(ewpMoneyPool.getGeaParticipantRefId(), instance.toString());
        if(response.getData()){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        if(!dpyCallerService.callIsDeploy(poolDepositCashOutDto.getGeaParticipantRefId(), instance).getData()){
        	throw new BusinessException(SystemMsg.MpErrorMsg.NO_DEPLOY.getErrorCode());
        }
        if(ewpMoneyPool.getRequestApprovals().stream().anyMatch(
                item -> item.getStatus().equals(RequestApprovalStatus.PENDING_FOR_APPROVAL))){
            throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }
        
        EwpPoolDepositCashOut ewpPoolDepositCashOut = saveEwpPoolDepositCashOut(ewpMoneyPool, poolDepositCashOutDto, instance);
        requestApprovalService.savePoolDepositCashOutRequestApproval(poolDepositCashOutDto.getGeaParticipantRefId(), ewpPoolDepositCashOut, instance,poolDepositCashOutDto.getRequestRemark());
        return ewpPoolDepositCashOut;
    }

    @Override
    public List<PoolDepositCashOutDto> getPoolDepositCashOut(String moneyPoolId) {
        if (StringUtils.isBlank(moneyPoolId)) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"moneyPoolId"});
        }
        EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findOne(Long.valueOf(moneyPoolId));
        if (ewpMoneyPool == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwpMoneyPool"});
        }
        List<EwpPoolDepositCashOut> poolDepositCashOuts = ewpPoolDepositCashOutRepository.findByEwpMoneyPool(ewpMoneyPool);
        return poolDepositCashOuts.stream().map(item -> {
            PoolDepositCashOutDto poolDepositCashOutDto = new PoolDepositCashOutDto();
            poolDepositCashOutDto.setMoneyPoolId(String.valueOf(item.getEwpMoneyPool().getId()));
            poolDepositCashOutDto.setType(item.getType());
            poolDepositCashOutDto.setAmount(item.getAmount());
            poolDepositCashOutDto.setStatus(item.getStatus().getValue());
            poolDepositCashOutDto.setCurrentEnvir(item.getCurrentEnvir().getValue());
            poolDepositCashOutDto.setCurrency(item.getEwpMoneyPool().getBaseCurrency());
            List<EwpMaterial> ewpMaterialList = ewpMaterialRepository.findByEwpPoolDepositCashOutId(item.getId());
            if (!ewpMaterialList.isEmpty()) {
                List<MaterialDto> materialDtoList = ewpMaterialList.stream().map(material -> {
                    MaterialDto materialDto = getMaterialDto(material);
                    materialDto.setEwpPoolDepositCashOutId(String.valueOf(item.getId()));
                    return materialDto;
                }).collect(Collectors.toList());

                List<MaterialDto> materialDtos =new ArrayList<>();
                StringBuilder materialFilename = new StringBuilder();
                StringBuilder filePath = new StringBuilder();
                for (MaterialDto materialDto : materialDtoList) {
                    materialFilename.append(","+materialDto.getMaterialFilename());
                    filePath.append(","+materialDto.getFilePath());
                }
                String finalMaterialFilename = materialFilename.substring(1,materialFilename.length());
                String finalFilePath = filePath.substring(1,filePath.length());
                materialDtoList.stream().findFirst().ifPresent(ite -> {
                    ite.setFilePath(finalFilePath);
                    ite.setMaterialFilename(finalMaterialFilename);
                    materialDtos.add(ite);
                });
                poolDepositCashOutDto.setMaterialDtos(materialDtos);
            }
            return poolDepositCashOutDto;
        }).collect(Collectors.toList());
    }

    private EwpPoolDepositCashOut saveEwpPoolDepositCashOut(EwpMoneyPool ewpMoneyPool, PoolDepositCashOutDto poolDepositCashOutDto, Instance instance) {
        EwpPoolDepositCashOut ewpPoolDepositCashOut = new EwpPoolDepositCashOut();
        ewpPoolDepositCashOut.setEwpMoneyPool(ewpMoneyPool);
        ewpPoolDepositCashOut.setType(poolDepositCashOutDto.getType());
        ewpPoolDepositCashOut.setAmount(poolDepositCashOutDto.getAmount());
        ewpPoolDepositCashOut.setStatus(MoneyPoolStatus.PENDING_FOR_PROCESS);
        ewpPoolDepositCashOut.setCurrentEnvir(instance);
        ewpPoolDepositCashOut.setGeaTxRefNo(UUID.randomUUID().toString());
        ewpPoolDepositCashOut.setCreateBy(userService.getLoginAccountId());
        ewpPoolDepositCashOut.setCreateDate(new Date());
        ewpPoolDepositCashOutRepository.save(ewpPoolDepositCashOut);
        List<MaterialDto> materialDtoList = poolDepositCashOutDto.getMaterialDtos();
        if (materialDtoList != null && !materialDtoList.isEmpty()) {
            Account account = userService.getCurrentAccountInfo();
            List<EwpMaterial> ewpMaterialList=new ArrayList<>();
            for (MaterialDto materialDto : materialDtoList) {
                ewpMaterialList=getEwpMaterial(materialDto, account);
                for (EwpMaterial material : ewpMaterialList) {
                    material.setEwpPoolDepositCashOutId(ewpPoolDepositCashOut.getId());
                }
            }
            ewpMaterialRepository.save(ewpMaterialList);
        }
        return ewpPoolDepositCashOut;
    }

    @Override
    public RestfulResponse<Map<String,String>> deployToProduction(String geaParticipantRefId) {
        String loginAccountId = userService.getLoginAccountId();
        Date createDate = new Date();
        if(StringUtils.isBlank(geaParticipantRefId)){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"geaParticipantRefId"});
        }
        if(this.hasPending(geaParticipantRefId, Instance.PRE_PROD, null)){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }

        Map<String,String> mpMapping = new HashMap<>();  // old and new mp data  gea id mapping 
        List<EwpMoneyPool> srcMpList = ewpMoneyPoolRepository.findByGeaParticipantRefId(geaParticipantRefId, Instance.PRE_PROD);
        for(EwpMoneyPool srcMp : srcMpList){
        	if(srcMp.getStatus()==MoneyPoolStatus.PENDING_FOR_PROCESS || srcMp.getStatus()==MoneyPoolStatus.REJECTED){
        		continue;
        	}

            //copy ewp_money_pool
            EwpMoneyPool tagMp = new EwpMoneyPool();
            BeanUtils.copyProperties(srcMp, tagMp, new String[]{"geaMoneyPoolRefId","createBy","createDate","currentEnvir","ewpMpChangeReqs","ewpPoolAdjustments","ewpPoolDepositCashOuts","id","requestApprovals","status","updateBy","updateDate"});
            tagMp.setGeaMoneyPoolRefId(this.genGeaId(geaParticipantRefId, tagMp.getBaseCurrency(), Instance.PROD));
            tagMp.setCreateBy(loginAccountId);
            tagMp.setCreateDate(createDate);
            tagMp.setCurrentEnvir(Instance.PROD);
            tagMp.setStatus(srcMp.getStatus());
            ewpMoneyPoolRepository.save(tagMp);
            mpMapping.put(srcMp.getGeaMoneyPoolRefId(), tagMp.getGeaMoneyPoolRefId());

        }
        return RestfulResponse.ofData(mpMapping);
    }

    @Override
    public PageDatas<MoneyPoolListDto> getMoneyPoolList(String geaMoneyPoolRefId, String geaParticipantRefId, String currency,
                                                        String balance, MoneyPoolStatus moneyPoolStatus, String group ,
                                                        Integer pageNo, Integer pageSize, String sortBy, Boolean isAscending,
                                                        Instance instance, String approvalStatus) {
        PageDatas<MoneyPoolListDto> pageDatas = new PageDatas<>(pageNo, pageSize);
        Sort sort = getSort(isAscending, sortBy);

        Specifications<EwpMoneyPool> where = Specifications.where((root, query, builder)-> builder.isNotNull(root.get("id")));
        if(StringUtils.isNotBlank(geaMoneyPoolRefId)){
            where = where.and((root, query, builder)-> builder.like(root.get("geaMoneyPoolRefId"), "%"+ geaMoneyPoolRefId +"%"));
        }
        if(StringUtils.isNotBlank(currency)){
            where = where.and((root, query, builder)-> builder.like(root.get("baseCurrency"), "%"+ currency +"%"));
        }
        if(Objects.nonNull(moneyPoolStatus)){
            where = where.and((root, query, builder)-> builder.equal(root.get("status"), moneyPoolStatus));
        }else {
            where = where.and((root, query, builder)-> root.get("status").in(Arrays.asList(MoneyPoolStatus.REJECTED)).not());
        }
        if(Objects.nonNull(instance)){
            where = where.and((root, query, builder)-> builder.equal(root.get("currentEnvir"), instance));
        }
        if(StringUtils.isNotBlank(approvalStatus)){
            where = where.and((root, query, builder)-> {
                query.distinct(true);
                Join<EwpMoneyPool, RequestApproval> join = root.join("requestApprovals", JoinType.LEFT);
                return join.get("status").in(RequestApprovalStatus.findByListView(approvalStatus));
            });
        }

        Account accountInfo = userService.getCurrentAccountInfo();
        String participantRefId = accountInfo.getExternalGroupId();
        Boolean internal = accountInfo.getInternal();
        if(StringUtils.isNotBlank(participantRefId) && !internal){
            geaParticipantRefId = participantRefId;
        }

        if(StringUtils.isNotBlank(geaParticipantRefId)){
            String search = "%"+ geaParticipantRefId +"%";
            where = where.and((root, query, builder)-> builder.like(root.get("geaParticipantRefId"), search));
        }

        Page<EwpMoneyPool> pageData = ewpMoneyPoolRepository.findAll(where, pageDatas.pageRequest(sort));
        //pt name
        Map<String,String> ptNameMap = new HashMap<>();
        try{
        	List<String> ptGeaIds = pageData.getContent().stream().map(item->item.getGeaParticipantRefId()).collect(Collectors.toList());
        	ptNameMap = ewpCallerService.callGetParticipantName(ptGeaIds, instance).getData();
        }catch(Exception e){
        	logger.error("访问 ewp 出错", e);
        }
        //relatedServices
        Map<String,String> relatedServicesMap = new HashMap<>();
        try{
        	List<String> mpGeaIds = pageData.getContent().stream().map(item->item.getGeaMoneyPoolRefId()).collect(Collectors.toList());
        	relatedServicesMap = ewpCallerService.callGetRelatedServicesByMp(mpGeaIds, instance).getData();
        }catch(Exception e){
        	logger.error("访问 ewp 出错", e);
        }

        MoneyPoolBalanceResponse restfulResponse=getMoneyPoolBalance(pageData.getContent().stream().map((EwpMoneyPool item) -> item.getGeaMoneyPoolRefId()).collect(Collectors.joining(",")), instance);

        List<MoneyPoolListDto> list = new ArrayList<>();
        for(EwpMoneyPool item : pageData.getContent()){
        	MoneyPoolListDto dto = new MoneyPoolListDto();
            if (Objects.nonNull(restfulResponse) && CollectionUtils.isNotEmpty(restfulResponse.getMoneyPoolBalances())) {
                List<MoneyPoolBalanceResponse.MoneyPoolBalanceEntry> moneyPoolBalances = restfulResponse.getMoneyPoolBalances();
                for (MoneyPoolBalanceResponse.MoneyPoolBalanceEntry moneyPoolBalance : moneyPoolBalances) {
                    if (moneyPoolBalance.getMoneyPoolRefId().equals(item.getGeaMoneyPoolRefId())) {
                        dto.setBalance(moneyPoolBalance.getBalance());
                        dto.setLastUpdateTime(moneyPoolBalance.getLastUpdateTime());
                    }
                }
            }
            dto.setMoneyPoolId(item.getId().toString());
            dto.setGeaMoneyPoolRefId(item.getGeaMoneyPoolRefId());
            dto.setGeaParticipantRefId(item.getGeaParticipantRefId());
            dto.setAlertLine(item.getAlertLine());
            dto.setCurrency(item.getBaseCurrency());
            if (MoneyPoolStatus.PENDING_FOR_PROCESS.equals(item.getStatus())) {
                dto.setMoneyPoolStatus(MoneyPoolStatus.SUSPEND.getValue());
            } else {
                dto.setMoneyPoolStatus(item.getStatus().toString());
            }
            EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findOne(item.getId());
            List<RequestApproval> requestApprovals = ewpMoneyPool.getRequestApprovals();
            if (requestApprovals.stream().anyMatch(i -> i.getStatus().equals(RequestApprovalStatus.PENDING_FOR_APPROVAL))) {
                dto.setApprovalStatus(ApprovalStatus.PENDING_APPROVAL.getValue());
            } else {
                dto.setApprovalStatus(ApprovalStatus.EDITABLE.getValue());
            }

            //pt name
            dto.setParticipantName(ptNameMap.get(item.getGeaParticipantRefId()));
            //relatedServices
            dto.setRelatedServices(relatedServicesMap.get(item.getGeaMoneyPoolRefId()));
            list.add(dto);
        }

        pageDatas.initDatas(list, pageData.getTotalElements(), pageData.getTotalPages());
        return pageDatas;
    }

    private Sort getSort(Boolean isAscending, String sortBy){
        Sort.Direction direction;
        if(null == isAscending){
            direction = Sort.Direction.DESC;
        }else {
            direction = isAscending?Sort.Direction.ASC:Sort.Direction.DESC;
        }
        String sortName = StringUtils.isBlank(sortBy)||StringUtils.isBlank(orderMap().get(sortBy))?orderMap().get("default"):orderMap().get(sortBy);
        return new Sort(direction, sortName);
    }

    private static Map<String,String> orderMap(){
        Map<String,String> orderMap = new HashMap<>();
        orderMap.put("default", "createDate");
        orderMap.put("geaMoneyPoolRefId","geaMoneyPoolRefId");
        orderMap.put("geaParticipantRefId","geaParticipantRefId");
        orderMap.put("currency","baseCurrency");
        orderMap.put("alertLine","alertLine");
        orderMap.put("moneyPoolStatus","status");
        return orderMap;
    }

    private MoneyPoolBalanceResponse getMoneyPoolBalance(String moneyPoolRefIds, Instance instance) {
    	try{
	        if(StringUtils.isBlank(moneyPoolRefIds)){
	            return null;
	        }
	        String url ;
	        if(instance==Instance.PRE_PROD){
	            url = preGeaBalanceUrl;
	        }else{
	            url = proGeaBalanceUrl;
	        }
	
	        Map<String,String> param = new HashMap<>();
	        param.put("moneyPoolRefIds", moneyPoolRefIds);
            param.put("api_key", apiKey);
	        return httpClientUtils.httpGet(url, MoneyPoolBalanceResponse.class, param);
    	}catch(Exception e){
    		logger.error("error",e);
    		return null;
    	}
    	
    }

    @Override
    public MpDetailDto getDetail(String geaMoneyPoolRefId, Instance instance) {
        if(StringUtils.isBlank(geaMoneyPoolRefId)){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"geaMoneyPoolRefId"});
        }
        EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findByGeaMoneyPoolRefId(geaMoneyPoolRefId,instance);
        if(ewpMoneyPool == null){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwpMoneyPool"});
        }
        MpDetailDto mpDetailDto = new MpDetailDto();

        MoneyPoolDto moneyPoolDto  = getMoneyPool(String.valueOf(ewpMoneyPool.getId()));
        mpDetailDto.setMoneyPoolDto(moneyPoolDto);

        List<MpChangeReqDto> mpChangeReqDtos=getMpChangeReq(String.valueOf(ewpMoneyPool.getId()));
        mpDetailDto.setMpChangeReqDto(mpChangeReqDtos);

        List<PoolAdjustmentDto> poolAdjustmentDtos = getPoolAdjustment(String.valueOf(ewpMoneyPool.getId()));
        List<PoolAdjustmentDto> poolAdjustmentDtoList = poolAdjustmentDtos.stream().filter(item ->
                item.getStatus().equals(MoneyPoolStatus.PENDING_FOR_PROCESS.getValue())
                        && item.getCurrentEnvir().equals(instance.getValue())).collect(Collectors.toList());
        mpDetailDto.setPoolAdjustmentDtos(poolAdjustmentDtoList);

        List<PoolDepositCashOutDto> poolDepositCashOutDtos = getPoolDepositCashOut(String.valueOf(ewpMoneyPool.getId()));
        List<PoolDepositCashOutDto> poolDepositCashOutDtoList = poolDepositCashOutDtos.stream().filter(item ->
                item.getStatus().equals(MoneyPoolStatus.PENDING_FOR_PROCESS.getValue())
                        && item.getCurrentEnvir().equals(instance.getValue())).collect(Collectors.toList());
        mpDetailDto.setPoolDepositCashOutDtos(poolDepositCashOutDtoList);
        return mpDetailDto;
    }

    private List<MpChangeReqDto> getMpChangeReq(String moneyPoolId) {
        if (StringUtils.isBlank(moneyPoolId)) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"moneyPoolId"});
        }
        List<MpChangeReqDto> mpChangeReqDtoList =new ArrayList<>();

        // Setting old data 
        EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findOne(Long.valueOf(moneyPoolId));
        MpChangeReqDto orginData=new MpChangeReqDto();
        orginData.setMoneyPoolId(String.valueOf(ewpMoneyPool.getId()));
        orginData.setToStatus(ewpMoneyPool.getStatus().getValue());
        orginData.setStatus(ewpMoneyPool.getStatus().getValue());
        orginData.setToAlertLine(ewpMoneyPool.getAlertLine());
        mpChangeReqDtoList.add(orginData);
        // Set new data 
        EwpMpChangeReq ewpMpChangeReq = ewpMpChangeReqRepository.findByMoneyPoolId(Long.valueOf(moneyPoolId));
        if(ewpMpChangeReq!=null){
            MpChangeReqDto newData=new MpChangeReqDto();
            newData.setMoneyPoolId(String.valueOf(ewpMpChangeReq.getEwpMoneyPool().getId()));
            newData.setToStatus(ewpMpChangeReq.getToStatus());
            newData.setStatus(ewpMpChangeReq.getStatus().getValue());
            newData.setToAlertLine(ewpMpChangeReq.getToAlertLine());
            mpChangeReqDtoList.add(newData);
        }
        return mpChangeReqDtoList;
    }


    @Override
    public List<MoneyPoolListDto> getAllMoneyPool(String geaParticipantRefId, List<MoneyPoolStatus> status, Instance instance) {
        if(CollectionUtils.isEmpty(status)){
            status = Arrays.asList(MoneyPoolStatus.ACTIVE,MoneyPoolStatus.SUSPEND);
        }
        if(null == instance){
            instance = Instance.PRE_PROD;
        }
        List<EwpMoneyPool> ewpMoneyPoolList = ewpMoneyPoolRepository.getAllMoneyPool(geaParticipantRefId, status, instance);
        List<MoneyPoolListDto> listDto = new ArrayList<>();
        for(EwpMoneyPool ewpMoneyPool:ewpMoneyPoolList){
            MoneyPoolListDto dto = new MoneyPoolListDto();
            dto.setMoneyPoolId(ewpMoneyPool.getId().toString());
            dto.setGeaMoneyPoolRefId(ewpMoneyPool.getGeaMoneyPoolRefId());
            dto.setMoneyPoolStatus(ewpMoneyPool.getStatus().toString());
            dto.setCurrency(ewpMoneyPool.getBaseCurrency());
            dto.setAlertLine(ewpMoneyPool.getAlertLine());
            listDto.add(dto);
        }
        return listDto;
    }

    @Override
    public List<ParticipantDto> getAllParticipantList(Instance instance) {
        Instance instances = instance;
        if(null == instances){
            instances = Instance.PRE_PROD;
        }
        List<ParticipantDto> dtoList = null;
        try {
            dtoList = ewpCallerService.callGetParticipantByNameOrIdList(null, null,null,instances).getData();
        } catch (Exception e) {
            logger.error(" request ewp fail ",e);
        }
        return dtoList;
    }

    @Override
    public MoneyPoolDto getMoneyPoolByRefId(String geaMoneyPoolRefId, Instance currentEnvir) {
        if (StringUtils.isBlank(geaMoneyPoolRefId)) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"geaMoneyPoolRefId"});
        }
        EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findByGeaMoneyPoolRefId(geaMoneyPoolRefId,currentEnvir);
        return getMoneyPoolDto(ewpMoneyPool);
    }

    /**
     *  Is there a need for approval? 
     * @param requestApprovalId  This time Approval
     */
    @Override
    public Boolean hasPending(String geaParticipantRefId, Instance instance,Long requestApprovalId){
        RequestApproval approval = requestApprovalId==null ? null : requestApprovalRepository.findOne(requestApprovalId);
        List<RequestApproval> requestApprovalList=requestApprovalRepository.findByGeaParticipantRefId(geaParticipantRefId);
        if(CollectionUtils.isNotEmpty(requestApprovalList)){
            for(RequestApproval item : requestApprovalList){
                if(approval!=null && approval.getId().equals(item.getId())){ // Ignore this Approval
                    continue;
                }
                if(item.getCurrentEnvir()==instance && item.getStatus()==RequestApprovalStatus.PENDING_FOR_APPROVAL){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void synchDeployment(Long deployRefId,DeployStatus status,Date scheduleDeployDate,String deployVersionNo) {
        EwpPoolDeployment ewpPoolDeployment = ewpPoolDeploymentRepository.findOne(deployRefId);
        ewpPoolDeployment.setStatus(status);
        ewpPoolDeployment.setUpdateDate(new Date());
        ewpPoolDeployment.setScheduleDeployDate(scheduleDeployDate);
        ewpPoolDeployment.setDeployVersionNo(deployVersionNo);
        ewpPoolDeploymentRepository.save(ewpPoolDeployment);
    }



    @Override
    public PoolAdjustmentDto getAdjustment(Long id) {
        EwpPoolAdjustment adj = ewpPoolAdjustmentRepository.findOne(id);
        PoolAdjustmentDto dto = new PoolAdjustmentDto();
        BeanUtils.copyProperties(adj, dto);
        return dto;
    }

    @Override
    public PoolDepositCashOutDto getDepositCashOut(Long id) {
        EwpPoolDepositCashOut cashOut = ewpPoolDepositCashOutRepository.findOne(id);
        PoolDepositCashOutDto dto = new PoolDepositCashOutDto();
        BeanUtils.copyProperties(cashOut, dto);
        return dto;
    }

    @Override
    public EwpPoolDeployment saveDeployment(Long requestApprovalId) {
        if(null == requestApprovalId){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"requestApprovalId"});
        }
        RequestApproval requestApproval = requestApprovalRepository.findOne(requestApprovalId);
        if(null == requestApproval){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"RequestApproval"});
        }
        Date createDate = new Date();
        EwpPoolDeployment ewpDeployment = new EwpPoolDeployment();
        ewpDeployment.setCreateBy(null);
        ewpDeployment.setCreateDate(createDate);
        ewpDeployment.setStatus(DeployStatus.PENDING_FOR_DEPLOY);
        ewpDeployment.setRequestApproval(requestApproval);
        ewpDeployment.setGeaParticipantRefId(requestApproval.getGeaParticipantRefId());
        ewpDeployment.setDeployEnvir(requestApproval.getCurrentEnvir().toString());
        ewpPoolDeploymentRepository.save(ewpDeployment);

        EwpRecordEnvMap ewpRecordEnvMap = new EwpRecordEnvMap();
        ewpRecordEnvMap.setCreateDate(createDate);
        ewpRecordEnvMap.setCreateBy(null);
        if(requestApproval.getCurrentEnvir() == Instance.PRE_PROD){
            ewpRecordEnvMap.setPreprodRequestApproval(requestApproval);
        }else {
            ewpRecordEnvMap.setProdRequestApproval(requestApproval);
        }
        ewpRecordEnvMap.setEwpDeployToProd(ewpDeployment);
        ewpRecordEnvMap.setGeaParticipantRefId(requestApproval.getGeaParticipantRefId());
        ewpRecordEnvMapRepository.save(ewpRecordEnvMap);

        return ewpDeployment;
    }


    @Override
    public MoneyPoolDetailDto getDetailMoneyPool(String moneyPoolId, Instance instance) {
    	try{
	        EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findOne(Long.valueOf(moneyPoolId));
	        MoneyPoolDetailDto moneyPoolDetailDto = new MoneyPoolDetailDto();
        	EwpDetailDto ewpDetailDto = ewpCallerService.callGetDetail(ewpMoneyPool.getGeaParticipantRefId(), instance);
            moneyPoolDetailDto.setParticipantId(ewpMoneyPool.getGeaParticipantRefId());
            moneyPoolDetailDto.setAlertLine(ewpMoneyPool.getAlertLine());
            moneyPoolDetailDto.setMoneyPoolId(ewpMoneyPool.getId().toString());
            moneyPoolDetailDto.setGeaMoneyPoolRefId(ewpMoneyPool.getGeaMoneyPoolRefId());
            moneyPoolDetailDto.setBaseCurrency(ewpMoneyPool.getBaseCurrency());
            moneyPoolDetailDto.setGeaParticipantRefId(ewpMoneyPool.getGeaParticipantRefId());
            moneyPoolDetailDto.setStatus(ewpMoneyPool.getStatus()==MoneyPoolStatus.PENDING_FOR_PROCESS?MoneyPoolStatus.SUSPEND.name():ewpMoneyPool.getStatus().name());
            if(Objects.nonNull(ewpDetailDto)) {
                moneyPoolDetailDto.setParticipantName(ewpDetailDto.getFullCompanyInfoDto().getOldFullCompanyInformationDto().get(0).getParticipantName());
                StringBuilder serviceNo = new StringBuilder();
                for (com.tng.portal.common.dto.ewp.MoneyPoolDto m : ewpDetailDto.getServiceAssignmentDto().getOldMoneyPoolDtoList()) {
                    if (m.getGeaMoneyPoolRefId().equals(ewpMoneyPool.getGeaMoneyPoolRefId())) {
                        for (ServiceSettingDto s : m.getServiceSettingDtoList()) {
                            serviceNo.append(s.getName() + ",");
                        }
                        break;
                    }
                }
                if(StringUtils.isNotBlank(serviceNo.toString())){
                	serviceNo = serviceNo.deleteCharAt(serviceNo.length()-1);
                }
                moneyPoolDetailDto.setServiceNo(serviceNo.toString());
            }
            MoneyPoolBalanceResponse balRes = this.getMoneyPoolBalance(moneyPoolDetailDto.getGeaMoneyPoolRefId(), instance);
            if(balRes != null && CollectionUtils.isNotEmpty(balRes.getMoneyPoolBalances())){
            	moneyPoolDetailDto.setBalance(balRes.getMoneyPoolBalances().get(0).getBalance());
            }
	        return moneyPoolDetailDto;
    	}catch(Exception e){
    		logger.error("error",e);
    		throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
    	}
    }
    
	@Override
	public Map<String,Long> findMoneyPoolCount(List<String> geaParticipantRefId, List<MoneyPoolStatus> status, Instance instance) {
		List<Map<String,Object>> list = ewpMoneyPoolRepository.findMoneyPoolCount(geaParticipantRefId, status, instance);
		Map<String,Long> map = new HashMap<>();
		for(Map<String,Object> item : list){
			if(item.get("geaParticipantRefId") != null){
				map.put(item.get("geaParticipantRefId").toString(), (Long)item.get("total"));
			}
		}
		return map;
	}
}
