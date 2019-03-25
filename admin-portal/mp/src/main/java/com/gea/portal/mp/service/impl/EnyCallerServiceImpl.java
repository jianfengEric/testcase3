package com.gea.portal.mp.service.impl;

import com.gea.portal.mp.dto.MoneyPoolDetailPageDto;
import com.gea.portal.mp.dto.MoneyPoolTransactionDto;
import com.gea.portal.mp.entity.EwpMoneyPool;
import com.gea.portal.mp.entity.EwpPoolAdjustment;
import com.gea.portal.mp.entity.EwpPoolDepositCashOut;
import com.gea.portal.mp.repository.EwpMoneyPoolRepository;
import com.gea.portal.mp.repository.EwpPoolAdjustmentRepository;
import com.gea.portal.mp.repository.EwpPoolDepositCashOutRepository;
import com.gea.portal.mp.service.EnyCallerService;
import com.gea.portal.mp.service.EwpCallerService;
import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.dto.mp.MoneyPoolListDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.RequestApprovalStatus;
import com.tng.portal.common.enumeration.ServiceType;
import com.tng.portal.common.enumeration.TransactionType;
import com.tng.portal.common.service.BaseCallerService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *  Used for calling ewp Interface to other modules 
 */
@Service
@Transactional
public class EnyCallerServiceImpl extends BaseCallerService implements EnyCallerService {




    @Autowired
    private HttpClientUtils httpClientUtils;
    @Autowired
    private AccountService accountService;
    @Autowired
    private EwpMoneyPoolRepository ewpMoneyPoolRepository;
    @Autowired
    private EwpCallerService ewpCallerService;
    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;
    @Autowired
    private EwpPoolAdjustmentRepository ewpPoolAdjustmentRepository;
    @Autowired
    private EwpPoolDepositCashOutRepository ewpPoolDepositCashOutRepository;

    @Value("${eny.url}")
    private String enyUrl;
    @Value("${pre.gea.transaction.url}")
    private String preTransactionUrl;
    @Value("${pro.gea.transaction.url}")
    private String proTransactionUrl;

 	@Override
	protected String getTargetModule() {
		return ApplicationContext.Modules.ENY;
	}

    @Override
    public RestfulResponse<PageDatas<MoneyPoolTransactionDto>> getMoneyPoolDetailPageList(MoneyPoolDetailPageDto detailPageDto,Instance instance,Integer pageNo, Integer pageSize, String sortBy, Boolean isAscending) {
    	Pattern pattern = Pattern.compile("^\\w+$");
    	if(StringUtils.isNotBlank(detailPageDto.getGeaMoneyTransferId()) && !pattern.matcher(detailPageDto.getGeaMoneyTransferId()).find()){
        	return RestfulResponse.nullData();
        }
    	if(StringUtils.isNotBlank(detailPageDto.getCurrency()) && !pattern.matcher(detailPageDto.getCurrency()).find()){
    		return RestfulResponse.nullData();
        }
        HashMap<String,String>  prams = new HashMap<>();
        if(StringUtils.isNotBlank(detailPageDto.getMoneyPoolId())){
            EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findOne(Long.valueOf(detailPageDto.getMoneyPoolId()));
            if(Objects.nonNull(ewpMoneyPool)){
                prams.put("moneyPoolRefId", ewpMoneyPool.getGeaMoneyPoolRefId());
            }
        }
        Account accountInfo = userService.getCurrentAccountInfo();
        String participantRefId = accountInfo.getExternalGroupId();
        Boolean internal = accountInfo.getInternal();
        if(StringUtils.isNotBlank(participantRefId) && !internal && StringUtils.isBlank(detailPageDto.getMoneyPoolId())){
            List<EwpMoneyPool> list = ewpMoneyPoolRepository.findByGeaParticipantRefId(participantRefId, instance);
            String refIds = list.stream().map(EwpMoneyPool::getGeaMoneyPoolRefId).collect(Collectors.joining(","));
            prams.put("moneyPoolRefIds", refIds);
        }
        if(StringUtils.isNotBlank(detailPageDto.getMoneyPoolRefId())){
        	prams.put("moneyPoolRefIdLike", detailPageDto.getMoneyPoolRefId());
        }

        if(StringUtils.isNotBlank(detailPageDto.getGeaMoneyTransferId())){
            prams.put("geaMoneyTransferId",detailPageDto.getGeaMoneyTransferId());
        }
        if(StringUtils.isNotBlank(detailPageDto.getBeginTransactionDateTime())){
            prams.put("beginTransactionDateTime",detailPageDto.getBeginTransactionDateTime());
        }
        if(StringUtils.isNotBlank(detailPageDto.getEndTransactionDateTime())){
            prams.put("endTransactionDateTime",detailPageDto.getEndTransactionDateTime());
        }
        if(StringUtils.isNotBlank(detailPageDto.getServiceType())){
            prams.put("serviceId",String.valueOf(ServiceType.findByValue(detailPageDto.getServiceType()).getNo()));
        }
        if(StringUtils.isNotBlank(detailPageDto.getTransactionType())){
            prams.put("transactionType",TransactionType.findByValue(detailPageDto.getTransactionType()).getKey());
        }
        if(StringUtils.isNotBlank(detailPageDto.getCurrency())){
            prams.put("currency",detailPageDto.getCurrency());
        }
        if(StringUtils.isNotBlank(detailPageDto.getMinimumAmount())){
            prams.put("minimumAmount",detailPageDto.getMinimumAmount());
        }
        if(StringUtils.isNotBlank(detailPageDto.getLargestAmount())){
            prams.put("largestAmount",detailPageDto.getLargestAmount());
        }
        if(StringUtils.isNotBlank(detailPageDto.getMinimumBalanceBeforeTransaction())){
            prams.put("minimumBalanceBeforeTransaction",detailPageDto.getMinimumBalanceBeforeTransaction());
        }
        if(StringUtils.isNotBlank(detailPageDto.getLargestBalanceBeforeTransaction())){
            prams.put("largestBalanceBeforeTransaction",detailPageDto.getLargestBalanceBeforeTransaction());
        }
        if(StringUtils.isNotBlank(detailPageDto.getMinimumBalanceAfterTransaction())){
            prams.put("minimumBalanceAfterTransaction",detailPageDto.getMinimumBalanceAfterTransaction());
        }
        if(StringUtils.isNotBlank(detailPageDto.getLargestBalanceAfterTransaction())){
            prams.put("largestBalanceAfterTransaction",detailPageDto.getLargestBalanceAfterTransaction());
        }
        if(StringUtils.isNotBlank(detailPageDto.getRemark())){
            prams.put("remark",detailPageDto.getRemark());
        }
        if(null !=instance){
            prams.put("instance",instance.getValue());
        } else {
            prams.put("instance","PRE_PROD");
        }
        prams.put("pageNo", pageNo + "");
        prams.put("pageSize",pageSize+"");
        prams.put("sortBy",StringUtils.isNotBlank(sortBy)?sortBy:"");
        prams.put("isAscending",isAscending==null?"":isAscending.toString());
        RestfulResponse<PageDatas<MoneyPoolTransactionDto>> moneyPoolDto = new RestfulResponse<>();
        moneyPoolDto.setSuccessStatus();
        String url = this.getTargetUrl().concat("/eny/v1/get_money_pool");
        try {
        	moneyPoolDto =  httpClientUtils.postSendJson(url, new ParameterizedTypeReference<RestfulResponse<PageDatas<MoneyPoolTransactionDto>>>(){}, prams);
        }catch (Exception e) {
        	logger.error("请求eny失败",e);
        }
        for(MoneyPoolTransactionDto mpDto : moneyPoolDto.getData().getList()){
            int item = Integer.parseInt(mpDto.getServiceId());
            TransactionType transactionType = TransactionType.valueOf(mpDto.getTransactionType());
            mpDto.setServiceId(ServiceType.findByNo(item)==null?"":ServiceType.findByNo(item).getValue());
            mpDto.setGeaUserId(getRequestUserName(transactionType, mpDto.getRefNo(), instance));
            mpDto.setTransactionType(transactionType==null?"":transactionType.getValue());
        }
        return moneyPoolDto;
    }

    private String getRequestUserName(TransactionType transactionType, String getGeaMoneyTransferId, Instance instance) {
        if (StringUtils.isBlank(getGeaMoneyTransferId) || Objects.isNull(transactionType)) {
            return null;
        }
        String accountId = "";
        if (transactionType == TransactionType.ADJUSTMENT_TOP_UP || transactionType == TransactionType.ADJUSTMENT_WITH_DRAWAL) {
            EwpPoolAdjustment ewpPoolAdjustment = ewpPoolAdjustmentRepository.findByGeaTxRefNoAndCurrentEnvir(getGeaMoneyTransferId, instance);
            if (Objects.nonNull(ewpPoolAdjustment)) {
                accountId = ewpPoolAdjustment.getCreateBy();
            }
        } else if (transactionType == TransactionType.TOP_UP || transactionType == TransactionType.WITH_DRAWAL) {
            EwpPoolDepositCashOut ewpPoolDepositCashOut = ewpPoolDepositCashOutRepository.findByGeaTxRefNoAndCurrentEnvir(getGeaMoneyTransferId, instance);
            if (Objects.nonNull(ewpPoolDepositCashOut)) {
                accountId = ewpPoolDepositCashOut.getCreateBy();
            }
        }
        if (StringUtils.isNotBlank(accountId)) {
            AnaAccount anaAccount = accountService.getAccount(accountId);
            if (Objects.nonNull(anaAccount)) {
                return anaAccount.getAccount();
            }
        }
        return null;
    }

    @Override
    public PageDatas<MoneyPoolListDto> listMoneyPoolForProcessingStatus(String moneyPoolId, String participantId , String participantName, Integer pageNo, Integer pageSize, String sortBy, Boolean isAscending,
                                                                        Instance instance) {
        try{
            PageDatas<MoneyPoolListDto> pageDatas = new PageDatas<>(1, 10);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            RequestApprovalStatus status = RequestApprovalStatus.PENDING_FOR_APPROVAL;
            List<ParticipantDto> dtoList = ewpCallerService.callGetParticipantByNameOrIdList(participantId, participantName,null,instance).getData();
            ArrayList<Long> idList = new ArrayList<>();
            for(ParticipantDto dtos : dtoList){
                idList.add(Long.parseLong(dtos.getParticipantId()));
            }

            Page<EwpMoneyPool> pageData = ewpMoneyPoolRepository.listMoneyPoolForProcessingStatus(status,instance,pageDatas.pageRequest(sort));
            List<MoneyPoolListDto> list = pageData.getContent().stream().map(item -> {
                MoneyPoolListDto dto = new MoneyPoolListDto();
                for(ParticipantDto participantDto: dtoList){
                    if(item.getGeaParticipantRefId().equals(participantDto.getParticipantId())){
                        dto.setParticipantName(participantDto.getParticipantName());
                        dto.setRelatedServices(participantDto.getServiceNo());
                    }
                }
                dto.setMoneyPoolId(item.getGeaMoneyPoolRefId());
                dto.setParticipantId(item.getGeaParticipantRefId());
                dto.setAlertLine(item.getAlertLine());
                dto.setCurrency(item.getBaseCurrency());
                dto.setMoneyPoolStatus(item.getStatus().toString());
                return dto;
            }).collect(Collectors.toList());
            pageDatas.initDatas(list, pageData.getTotalElements(), pageData.getTotalPages());
            return pageDatas;
        }catch(Exception e){
            logger.error(" request ewp fail ",e);
            return null;
        }
    }

    @Override
    public RestfulResponse<List<String>> getEnyServiceList() {
        RestfulResponse<List<String>> restfulResponse = new RestfulResponse<>();
        List<String> list = new ArrayList<>();
        ServiceType[] s = ServiceType.values();
        for(int i = 0; i< s.length; i++){
            list.add(s[i].getValue());
        }
        restfulResponse.setData(list);
        return restfulResponse;
    }

    @Override
    public RestfulResponse<List<String>> getEnyTransactionType() {
        RestfulResponse<List<String>> restfulResponse = new RestfulResponse<>();
        List<String> list = new ArrayList<>();
        TransactionType[] s = TransactionType.values();
        for(int i = 0; i< s.length; i++){
            list.add(s[i].getValue());
        }
        restfulResponse.setData(list);
        return restfulResponse;
    }
}
