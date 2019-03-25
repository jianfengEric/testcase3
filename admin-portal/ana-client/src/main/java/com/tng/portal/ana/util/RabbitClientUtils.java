package com.tng.portal.ana.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tng.portal.ana.dto.MerchantDto;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.util.CoderUtil;
import com.tng.portal.common.util.MqParam;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.util.RabbitMQUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 * Created by Roger on 2017/8/17.
 */
public class RabbitClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(RabbitClientUtils.class);

    private static final String SOA_MAM = PropertiesUtil.getPropertyValueByKey("mam.service.name");
    private static final String SOA_EWP = PropertiesUtil.getPropertyValueByKey("ewp.service.name");
    private static final String GET_MERCHANT = PropertiesUtil.getPropertyValueByKey("mam.method.getMerchant");

    public static MerchantDto getMerchantByMid(String mid) {
        try {
            RestfulResponse<MerchantDto> response = RabbitMQUtil.sendRequestToSOAService(
                    SOA_MAM,
                    GET_MERCHANT,
                    MerchantDto.class,
                    MqParam.gen(CoderUtil.encrypt(String.valueOf(mid))));
            logger.info("getMerchantByMid() mid:{} restfulResponse:{}", mid, response);
            if(response!=null && response.hasSuccessful()){
                return response.getData();
            }
        } catch (Exception e) {
            logger.error("mid:{}", mid, e);
        }
        return null;
    }

    /**
     * Find Merchant By merchantId Or mid
     * @param merchantId  Encrypted merchantId
     * @param mid  Encrypted mid
     * @return
     */
    public static MerchantDto getMerchant(String merchantId, String mid) {
        /*if (StringUtils.isBlank(merchantId) && StringUtils.isBlank(mid)) {
            logger.warn("getMerchant() isBlank(merchantId) && isBlank(mid) merchantId:{} mid:{}", merchantId, mid);
            return null;
        }
        MerchantDto merchantDto = null;
        try {
            RestfulResponse<MerchantDto> restfulResponse = RabbitMQUtil.sendRequestToSOAService(
                    SOA_MAM,
                    getMerchant,
                    MerchantDto.class,
                    merchantId,mid);
//            logger.info("getMerchant() merchantId:{} mid:{} restfulResponse:{}", CoderUtil.decrypt(merchantId), CoderUtil.decrypt(mid), restfulResponse);
            if (restfulResponse != null && restfulResponse.hasSuccessful()) {
                merchantDto = restfulResponse.getData();
            } else {
                logger.warn("get merchant dto fail !");
            }
        } catch (Exception e) {
            logger.error("get merchant dto error!", e);
        }
        return merchantDto;*/
    	return null;
    }

    /**
     *
     * @param geaParticipantRefId
     * @return
     */
    public static ParticipantDto getParticipant(String geaParticipantRefId, Instance instance) {
        if (StringUtils.isBlank(geaParticipantRefId)) {
            logger.warn("getParticipant() isBlank(mid) mid:{}", geaParticipantRefId);
            return null;
        }
        ParticipantDto merchantDto = null;
        try {
            RestfulResponse<ParticipantDto> restfulResponse = RabbitMQUtil.sendRequestToSOAService(
                    SOA_EWP, "getParticipantByGeaRefId", ParticipantDto.class ,MqParam.gen(geaParticipantRefId), MqParam.gen(instance));
            logger.info("getParticipant()  mid:{} restfulResponse:{}", geaParticipantRefId, restfulResponse);
            if (restfulResponse != null && restfulResponse.hasSuccessful()) {
                merchantDto = restfulResponse.getData();
            } else {
                logger.warn("get Participant dto fail !");
            }
        } catch (Exception e) {
            logger.error("get Participant dto error!", e);
        }
        return merchantDto;
    }

}
