package com.tng.portal.common.dto.mp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jimmy on 2018/9/6.
 */
public class MpDetailDto implements Serializable {

    private MoneyPoolDto moneyPoolDto;
    private List<PoolAdjustmentDto> poolAdjustmentDtos;
    private List<PoolDepositCashOutDto> poolDepositCashOutDtos;
    private List<MpChangeReqDto> mpChangeReqDto;
    
    public List<MpChangeReqDto> getMpChangeReqDto() {
        return mpChangeReqDto;
    }

    public void setMpChangeReqDto(List<MpChangeReqDto> mpChangeReqDto) {
        this.mpChangeReqDto = mpChangeReqDto;
    }
    public List<PoolAdjustmentDto> getPoolAdjustmentDtos() {
        return poolAdjustmentDtos;
    }

    public void setPoolAdjustmentDtos(List<PoolAdjustmentDto> poolAdjustmentDtos) {
        this.poolAdjustmentDtos = poolAdjustmentDtos;
    }

    public List<PoolDepositCashOutDto> getPoolDepositCashOutDtos() {
        return poolDepositCashOutDtos;
    }

    public void setPoolDepositCashOutDtos(List<PoolDepositCashOutDto> poolDepositCashOutDtos) {
        this.poolDepositCashOutDtos = poolDepositCashOutDtos;
    }

    public MoneyPoolDto getMoneyPoolDto() {
        return moneyPoolDto;
    }

    public void setMoneyPoolDto(MoneyPoolDto moneyPoolDto) {
        this.moneyPoolDto = moneyPoolDto;
    }
}
