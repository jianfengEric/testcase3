package com.gea.portal.mp.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gea.portal.mp.dto.MoneyPoolDetailDto;
import com.gea.portal.mp.entity.EwpMoneyPool;
import com.gea.portal.mp.entity.EwpMpChangeReq;
import com.gea.portal.mp.entity.EwpPoolAdjustment;
import com.gea.portal.mp.entity.EwpPoolDeployment;
import com.gea.portal.mp.entity.EwpPoolDepositCashOut;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.dto.mp.MoneyPoolDto;
import com.tng.portal.common.dto.mp.MoneyPoolListDto;
import com.tng.portal.common.dto.mp.MpChangeReqDto;
import com.tng.portal.common.dto.mp.MpDetailDto;
import com.tng.portal.common.dto.mp.PoolAdjustmentDto;
import com.tng.portal.common.dto.mp.PoolDepositCashOutDto;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.MoneyPoolStatus;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 * Created by Jimmy on 2018/9/3.
 */
public interface MoneyPoolService {

    EwpMoneyPool createMoneyPool(MoneyPoolDto moneyPoolDto, Instance instance);
    List<MoneyPoolDto> getParticipantMoneyPool(String geaParticipantRefId, Instance instance);
    MoneyPoolDto getMoneyPool(String moneyPoolId);
    EwpMpChangeReq updateMoneyPoolStatus(MpChangeReqDto mpChangeReqDto, Instance instance);

    EwpPoolAdjustment createPoolAdjustment(PoolAdjustmentDto poolAdjustmentDto, Instance instance);
    List<PoolAdjustmentDto> getPoolAdjustment(String moneyPoolId);

    EwpPoolDepositCashOut createPoolDepositCashOut(PoolDepositCashOutDto poolDepositCashOutDto, Instance instance);
    List<PoolDepositCashOutDto> getPoolDepositCashOut(String moneyPoolId);

    public RestfulResponse<Map<String,String>> deployToProduction(String geaParticipantRefId);

    PageDatas<MoneyPoolListDto> getMoneyPoolList(String moneyPoolId, String participantId ,String currency,
                                                 String balance, MoneyPoolStatus moneyPoolStatus, String group ,
                                                 Integer pageNo, Integer pageSize, String sortBy, Boolean isAscending,
                                                 Instance instance, String approvalStatus);

    MpDetailDto getDetail(String geaMoneyPoolRefId, Instance instance);
    List<MoneyPoolListDto> getAllMoneyPool(String geaParticipantRefId, List<MoneyPoolStatus> status, Instance instance);
    List<ParticipantDto> getAllParticipantList(Instance instance);

    MoneyPoolDto getMoneyPoolByRefId(String geaMoneyPoolRefId, Instance currentEnvir);

    /**
     *  Is there a need for approval? 
     * @param requestApprovalId  This time Approval
     */
    public Boolean hasPending(String geaMoneyPoolRefId, Instance instance,Long requestApprovalId);
    
    public void synchDeployment(Long deployRefId, DeployStatus status,Date scheduleDeployDate,String deployVersionNo);


    public PoolAdjustmentDto getAdjustment(Long id);

    public PoolDepositCashOutDto getDepositCashOut(Long id);
    
    public EwpPoolDeployment saveDeployment(Long requestApprovalId);
    
    public MoneyPoolDetailDto getDetailMoneyPool(String moneyPoolId, Instance instance);
    
	public Map<String,Long> findMoneyPoolCount(List<String> geaParticipantRefId, List<MoneyPoolStatus> status,Instance instance);

}
