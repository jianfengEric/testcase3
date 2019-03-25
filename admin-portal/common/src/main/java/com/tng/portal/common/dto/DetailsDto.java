package com.tng.portal.common.dto;

import java.io.Serializable;

import com.tng.portal.common.dto.ewp.EwpDetailDto;
import com.tng.portal.common.dto.mp.MpDetailDto;
import com.tng.portal.common.dto.srv.ServiceBatchChgReqDto;
import com.tng.portal.common.dto.tre.ExchangeRateFileDto;

/**
 * Created by Owen on 2018/9/13.
 */
public class DetailsDto implements Serializable{
    private EwpDetailDto ewpDetailDto;
    private MpDetailDto mpDetailDto;
    private ServiceBatchChgReqDto serviceBatchChgReqDto;
    private ExchangeRateFileDto exchangeRateFileDto;

    public EwpDetailDto getEwpDetailDto() {
        return ewpDetailDto;
    }

    public void setEwpDetailDto(EwpDetailDto ewpDetailDto) {
        this.ewpDetailDto = ewpDetailDto;
    }

    public MpDetailDto getMpDetailDto() {
        return mpDetailDto;
    }

    public void setMpDetailDto(MpDetailDto mpDetailDto) {
        this.mpDetailDto = mpDetailDto;
    }

	public ServiceBatchChgReqDto getServiceBatchChgReqDto() {
		return serviceBatchChgReqDto;
	}

	public void setServiceBatchChgReqDto(ServiceBatchChgReqDto serviceBatchChgReqDto) {
		this.serviceBatchChgReqDto = serviceBatchChgReqDto;
	}

	public ExchangeRateFileDto getExchangeRateFileDto() {
		return exchangeRateFileDto;
	}

	public void setExchangeRateFileDto(ExchangeRateFileDto exchangeRateFileDto) {
		this.exchangeRateFileDto = exchangeRateFileDto;
	}

}
