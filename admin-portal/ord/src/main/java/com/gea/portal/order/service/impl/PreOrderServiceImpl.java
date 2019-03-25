package com.gea.portal.order.service.impl;

import com.gea.portal.order.dto.GeaTxQueueDto;
import com.gea.portal.order.dto.GeaTxQueueInDto;
import com.gea.portal.order.dto.OrderDetailDto;
import com.gea.portal.order.entity.preProduction.order.*;
import com.gea.portal.order.repository.preProduction.moneyPool.PreMoneyPoolCashFlowRepository;
import com.gea.portal.order.repository.preProduction.order.PreGeaTxQueueRepository;
import com.gea.portal.order.service.OrderService;
import com.google.gson.annotations.SerializedName;
import com.tng.portal.common.constant.DateCode;
import com.tng.portal.common.util.DateUtils;
import com.tng.portal.common.vo.PageDatas;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

@Service
public class PreOrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(PreOrderServiceImpl.class);

    @Autowired
    public PreGeaTxQueueRepository preGeaTxQueueRepository;

    @Autowired
    public PreMoneyPoolCashFlowRepository preMoneyPoolCashFlowRepository;

    @Override
    @Transactional(value="transactionManagerPreProduction", propagation = Propagation.REQUIRES_NEW)
    public PageDatas<GeaTxQueueDto> getOrders(GeaTxQueueInDto geaTxQueueInDto, Integer pageNo,
                                              Integer pageSize, String sortBy, Boolean isAscending) {
        //default return data
        PageDatas<GeaTxQueueDto> pageDatas = new PageDatas<>(pageNo, pageSize);


        Sort.Direction direction = isAscending ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (null == sortBy || sortBy.isEmpty()) {
            sortBy = "submissionDateTime";
        }
        //converts display fields to entity fields
        try {
            Field field = GeaTxQueueInDto.class.getDeclaredField(sortBy);
            if(field != null){
                SerializedName serializedName = field.getAnnotation(SerializedName.class);
                sortBy = serializedName.value();
            }
        } catch (NoSuchFieldException e) {
            logger.error("error",e);
        }
        Sort sort = new Sort(direction, sortBy);
        //spring paging mechanism start from zero,so pageNo need minus 1
        PageRequest pageRequest = new PageRequest(pageNo-1, pageSize, sort);
        Specification<PreGeaTxQueue> specification = new Specification<PreGeaTxQueue>() {
            public Predicate toPredicate(Root<PreGeaTxQueue> preGeaTxQueue, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                String participantId = geaTxQueueInDto.getParticipantId();
                String participantIdByLogin = geaTxQueueInDto.getParticipantIdByLogin();
                String serviceType = geaTxQueueInDto.getServiceType();
                String status = geaTxQueueInDto.getStatus();
                String beginSubmissionDateTime = geaTxQueueInDto.getBeginSubmissionDateTime();
                String endSubmissionDateTime = geaTxQueueInDto.getEndSubmissionDateTime();
                String beginUpdateTime = geaTxQueueInDto.getBeginUpdateTime();
                String endUpdateTime = geaTxQueueInDto.getEndUpdateTime();
                String submissionDateTime = geaTxQueueInDto.getSubmissionDateTime();
                String updateTime = geaTxQueueInDto.getUpdateTime();
                String transferId = geaTxQueueInDto.getTransferId();
                String name = geaTxQueueInDto.getParticipantName();
                String remark = geaTxQueueInDto.getRemark();
                String geaStatusCode = geaTxQueueInDto.getGeaStatus();
                ArrayList<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotBlank(serviceType)) {
                    Join<PreGeaTxQueue, PreQuotation> joinQuotation = preGeaTxQueue.join("quotation", JoinType.LEFT);
                    Join<PreGeaTxQueue, PreTngIssuer> joinTngIssuer = joinQuotation.join("tngIssuer", JoinType.LEFT);
                    Join<PreGeaTxQueue, PreService> joinService = joinTngIssuer.join("service", JoinType.LEFT);
                    predicates.add(criteriaBuilder.equal(joinService.get("id"),serviceType));
                }
                if (StringUtils.isNotBlank(status)) {
                    predicates.add(criteriaBuilder.equal(preGeaTxQueue.get("status").as(String.class), status));
                }
                if (StringUtils.isNotBlank(geaStatusCode)) {
                    predicates.add(criteriaBuilder.equal(preGeaTxQueue.get("geaStatusCode").as(String.class), geaStatusCode));
                }
                if (StringUtils.isNotBlank(transferId)) {
                    predicates.add(criteriaBuilder.like(preGeaTxQueue.get("geaRefId").as(String.class), "%"+transferId+"%"));
                }
                if (StringUtils.isNotBlank(beginSubmissionDateTime)) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(preGeaTxQueue.get("createDatetime").as(String.class), beginSubmissionDateTime));
                }
                if (StringUtils.isNotBlank(endSubmissionDateTime)) {
                    predicates.add(criteriaBuilder.lessThan(preGeaTxQueue.get("createDatetime").as(String.class), endSubmissionDateTime+":59"));
                }
                if (StringUtils.isNotBlank(beginUpdateTime)) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(preGeaTxQueue.get("updateDatetime").as(String.class), beginUpdateTime));
                }
                if (StringUtils.isNotBlank(endUpdateTime)) {
                    predicates.add(criteriaBuilder.lessThan(preGeaTxQueue.get("updateDatetime").as(String.class), endUpdateTime+":59"));
                }
                if (StringUtils.isNotBlank(submissionDateTime)) {
                    predicates.add(criteriaBuilder.between(preGeaTxQueue.get("createDatetime"), DateUtils.parseDate(submissionDateTime+" 00:00:00", DateCode.dateFormatSs)
                        ,DateUtils.parseDate(submissionDateTime+" 23:59:59", DateCode.dateFormatSs)));
                }
                if (StringUtils.isNotBlank(updateTime)) {
                    predicates.add(criteriaBuilder.between(preGeaTxQueue.get("updateDatetime"), DateUtils.parseDate(updateTime+" 00:00:00", DateCode.dateFormatSs)
                            ,DateUtils.parseDate(updateTime+" 23:59:59", DateCode.dateFormatSs)));
                }
                if (StringUtils.isNotBlank(remark)) {
                    predicates.add(criteriaBuilder.like(preGeaTxQueue.get("csRemark").as(String.class), "%"+remark+"%"));
                }

                if (StringUtils.isNotBlank(name) || StringUtils.isNotBlank(participantId) || StringUtils.isNotBlank(participantIdByLogin)) {
                    String refId = "refId";
                    Join<PreGeaTxQueue, PreEwalletParticipant> joinParticipant = preGeaTxQueue.join("participant", JoinType.LEFT);
                    if(StringUtils.isNotBlank(name)){
                        predicates.add(criteriaBuilder.like(joinParticipant.get("name"),"%" + name + "%"));
                    }
                    if(StringUtils.isNotBlank(participantId) && StringUtils.isNotBlank(participantIdByLogin)){
                        predicates.add(criteriaBuilder.and(criteriaBuilder.like(joinParticipant.get(refId), "%" + participantId + "%"),
                                criteriaBuilder.equal(joinParticipant.get(refId), participantIdByLogin)));
                    } else if(StringUtils.isNotBlank(participantId)){
                        predicates.add(criteriaBuilder.like(joinParticipant.get(refId), "%" + participantId + "%"));
                    } else if(StringUtils.isNotBlank(participantIdByLogin)){
                        predicates.add(criteriaBuilder.equal(joinParticipant.get(refId), participantIdByLogin));
                    }
                }
                Predicate[] pre = new Predicate[predicates.size()];
                return query.where(predicates.toArray(pre)).getRestriction();
            }
        };
        Page<PreGeaTxQueue> page = preGeaTxQueueRepository.findAll(specification, pageRequest);
        Iterator<PreGeaTxQueue> preGeaTxQueueListIterator = page.getContent().iterator();
        ArrayList<GeaTxQueueDto> listDto = new ArrayList<>();
        while (preGeaTxQueueListIterator.hasNext()){
            PreGeaTxQueue item = preGeaTxQueueListIterator.next();
            PreEwalletParticipant participant = item.getParticipant();
            GeaTxQueueDto dto = new GeaTxQueueDto();
            if(null != participant){
                dto.setParticipantId(participant.getRefId());
                dto.setParticipantName(participant.getName());
            }
            if(null != item.getQuotation() && null != item.getQuotation().getTngIssuer() && null != item.getQuotation().getTngIssuer().getService()){
                dto.setServiceType(item.getQuotation().getTngIssuer().getService().getDescEn());
            }
            dto.setTransferId(item.getGeaRefId());
            if(null != item.getCreateDatetime()){
                dto.setSubmissionDateTime(DateFormatUtils.format(item.getCreateDatetime().getTime(), DateCode.dateFormatSs));
            }
            if(null != item.getUpdateDatetime()){
                dto.setUpdateTime(DateFormatUtils.format(item.getUpdateDatetime().getTime(), DateCode.dateFormatSs));
            }
            dto.setStatus(item.getStatus());
            dto.setGeaStatus(item.getGeaStatus());
            dto.setRemark(StringUtils.isBlank(item.getCsRemark())?"":item.getCsRemark());
            if(StringUtils.isNotBlank(item.getGeaRefId())){
                try{
                    String moneyPoolRefId = preMoneyPoolCashFlowRepository.getMoneyPoolRefIdByGeaRefId(item.getGeaRefId());
                    dto.setMoneyPoolId(moneyPoolRefId);
                } catch(Exception e){
                    logger.error("error",e);
                }
            }
            listDto.add(dto);
        }
        pageDatas.initDatas(listDto, page.getTotalElements(), page.getTotalPages());
        return pageDatas;
    }

    @Override
    @Transactional(value="transactionManagerPreProduction", propagation = Propagation.REQUIRES_NEW)
    public OrderDetailDto getOrderDetail(String transferId, String participantId) {
        OrderDetailDto order = null;
        Specification<PreGeaTxQueue> specification = new Specification<PreGeaTxQueue>() {
            public Predicate toPredicate(Root<PreGeaTxQueue> preGeaTxQueue, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                Predicate condition1 = null;
                if (!StringUtils.isEmpty(transferId)) {
                    condition1 = criteriaBuilder.equal(preGeaTxQueue.get("geaRefId"), transferId);
                    query.where(condition1);
                }
                return criteriaBuilder.and(condition1);
            }
        };

        PreGeaTxQueue preGeaTxQueue = preGeaTxQueueRepository.findOne(specification);
        if(null != preGeaTxQueue){
            PreEwalletParticipant participant = preGeaTxQueue.getParticipant();
            order = new OrderDetailDto();
            String geaRefId = preGeaTxQueue.getGeaRefId();
            order.setTransferId(geaRefId);
            if(null != participant){
                order.setParticipantId(participant.getRefId());
                order.setParticipantName(participant.getName());
            }
            if(null != preGeaTxQueue.getQuotation() && null != preGeaTxQueue.getQuotation().getTngIssuer() && null != preGeaTxQueue.getQuotation().getTngIssuer().getService()){
                order.setServiceType(preGeaTxQueue.getQuotation().getTngIssuer().getService().getDescEn());
            }
            if(null != preGeaTxQueue.getCreateDatetime()){
                order.setSubmissionDateTime(DateFormatUtils.format(preGeaTxQueue.getCreateDatetime().getTime(), DateCode.dateFormatSs));
            }
            if(null != preGeaTxQueue.getUpdateDatetime()){
                order.setUpdateTime(DateFormatUtils.format(preGeaTxQueue.getUpdateDatetime().getTime(), DateCode.dateFormatSs));
            }
            order.setStatus(preGeaTxQueue.getStatus());
            order.setRemark(StringUtils.isBlank(preGeaTxQueue.getCsRemark())?"":preGeaTxQueue.getCsRemark());
            PreQuotation preQuotation = preGeaTxQueue.getQuotation();
            if(null != preQuotation){
                order.setExchangeRate(preQuotation.getExchangeRate());
                order.setFromAmount(preQuotation.getFromAmount());
                order.setFromCountry(preQuotation.getFromCurrency());
                order.setToAmount(preQuotation.getToAmount());
                order.setToCountry(preQuotation.getToCurrency());
            }
            if(StringUtils.isNotBlank(geaRefId)){
                try{
                    String moneyPoolRefId = preMoneyPoolCashFlowRepository.getMoneyPoolRefIdByGeaRefId(geaRefId);
                    order.setMoneyPoolId(moneyPoolRefId);
                } catch(Exception e){
                    logger.error("error",e);
                }
            }
        }
        return order;
    }
}