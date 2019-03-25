package com.gea.portal.srv.service.impl;

import com.gea.portal.srv.entity.BaseService;
import com.gea.portal.srv.entity.ServiceBatchChgReq;
import com.gea.portal.srv.entity.ServiceChangeDetail;
import com.gea.portal.srv.entity.ServiceConfig;
import com.gea.portal.srv.repository.BaseServiceRepository;
import com.gea.portal.srv.repository.ServiceBatchChgReqRepository;
import com.gea.portal.srv.repository.ServiceChangeDetailRepository;
import com.gea.portal.srv.repository.ServiceConfigRepository;
import com.gea.portal.srv.service.RequestApprovalService;
import com.gea.portal.srv.service.SrvBaseService;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.dto.srv.BaseServiceDto;
import com.tng.portal.common.dto.srv.ServiceBatchChgReqDto;
import com.tng.portal.common.dto.srv.ServiceBatchDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.GenerateUniqueIDUtil;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Transactional
public class SrvBaseServiceImpl implements SrvBaseService {
    @Autowired
    private ServiceConfigRepository serviceConfigRepository;
    @Autowired
    private ServiceBatchChgReqRepository serviceBatchChgReqRepository;
    @Autowired
    private ServiceChangeDetailRepository serviceChangeDetailRepository;
    @Autowired
    private BaseServiceRepository baseServiceRepository;
    @Autowired
    private RequestApprovalService requestApprovalService;

    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;

    @Override
    public RestfulResponse<PageDatas> getServiceBaseMarkups(Integer pageNo, Integer pageSize, String sortBy, Boolean isAscending,Instance instance) {
        RestfulResponse<PageDatas> restResponse = new RestfulResponse<>();
        PageDatas<BaseServiceDto> pageDatas = new PageDatas<>(pageNo,pageSize);
        List<ServiceConfig> list;
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        if(checkSort(ServiceConfig.class, sortBy)){
            sort = pageDatas.pageSort(sortBy, isAscending, "createdTime");
        }
        Specifications<ServiceConfig> where = Specifications.where((root, query, builder)-> builder.isNotNull(root.get("id")));
        if(Objects.nonNull(instance)){
            where = where.and((root, query, builder)-> builder.equal(root.get("currentEnvir"), instance));
        }
        if (pageDatas.isAll()){
            list = serviceConfigRepository.findAll(where, sort);
        }else {
            Page<ServiceConfig> page = serviceConfigRepository.findAll(where, pageDatas.pageRequest(sort));
            list = page.getContent();
            pageDatas.initPageParam(page);
        }
        List<BaseServiceDto> baseServiceDtos = list.stream().map(item -> {
            BaseServiceDto dto=new BaseServiceDto();
            dto.setServiceId(item.getBaseService().getId());
            dto.setMarkup(new BigDecimal(item.getMarkup()));
            dto.setServiceCode(item.getBaseService().getServiceCode());
            dto.setServiceName(item.getBaseService().getNameEn());
            return dto;
        }).collect(Collectors.toList());
        pageDatas.setList(baseServiceDtos);
        restResponse.setData(pageDatas);
        restResponse.setSuccessStatus();
        return restResponse;
    }

    @Override
    public ServiceBatchChgReq submitServiceBaseMarkupData(ServiceBatchChgReqDto serviceBatchChgReqDto) {
        if(null == serviceBatchChgReqDto || null == serviceBatchChgReqDto.getInstance() || null ==serviceBatchChgReqDto.getServiceBatchDtoList()){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ServiceBatchDtoList or instance"});
        }
        Instance instance = Instance.valueOf(serviceBatchChgReqDto.getInstance());
        if(requestApprovalService.hasPendingStatus(instance)){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        ServiceBatchChgReq serviceBatchChgReq=saveServiceBatchChgReq(serviceBatchChgReqDto,instance);
        List<ServiceBatchDto> serviceBatchDtoList = serviceBatchChgReqDto.getServiceBatchDtoList();
        if(!serviceBatchDtoList.isEmpty()){
            for (ServiceBatchDto serviceBatchDto : serviceBatchDtoList) {
                saveServiceChangeDetail(serviceBatchChgReq.getId(),serviceBatchDto);
            }
        }
        requestApprovalService.saveServiceChangeRequestApproval(serviceBatchChgReq,instance,serviceBatchChgReqDto.getRequestRemark());
        return serviceBatchChgReq;
    }

    @Override
    public ServiceBatchChgReqDto getDetail(Long batchId) {
        if(null == batchId){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"batchId"});
        }
        ServiceBatchChgReq serviceBatchChgReq = serviceBatchChgReqRepository.findOne(batchId);
        if(Objects.isNull(serviceBatchChgReq)){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"serviceBatchChgReq"});
        }
        List<ServiceChangeDetail> serviceBatchs=serviceChangeDetailRepository.findByServiceBatchChgReq(serviceBatchChgReq);
        ServiceBatchChgReqDto dto=new ServiceBatchChgReqDto();
        dto.setInstance(serviceBatchChgReq.getCurrentEnvir().getValue());
        List<ServiceBatchDto> serviceBatchDtoList = new ArrayList<>();
        List<ServiceBatchDto> oldServiceBatchDtoList = new ArrayList<>();
        if(!serviceBatchs.isEmpty()){
            serviceBatchs.stream().forEach(item -> {
                ServiceBatchDto serviceBatchDto=new ServiceBatchDto();
                serviceBatchDto.setServiceId(item.getBaseService().getId());
                serviceBatchDto.setMarkup(item.getToMarkUp());
                serviceBatchDto.setServerCode(item.getBaseService().getServiceCode());
                serviceBatchDto.setServerName(item.getBaseService().getNameEn());
                serviceBatchDtoList.add(serviceBatchDto);
            });
            dto.setServiceBatchDtoList(serviceBatchDtoList);
        }
        List<ServiceConfig> serviceConfigs = serviceConfigRepository.findByCurrentEnvir(serviceBatchChgReq.getCurrentEnvir());
        if(!serviceConfigs.isEmpty()){
            serviceConfigs.stream().forEach(item -> {
                ServiceBatchDto serviceBatchDto=new ServiceBatchDto();
                serviceBatchDto.setServiceId(item.getBaseService().getId());
                serviceBatchDto.setMarkup(item.getMarkup());
                serviceBatchDto.setServerCode(item.getBaseService().getServiceCode());
                serviceBatchDto.setServerName(item.getBaseService().getNameEn());
                oldServiceBatchDtoList.add(serviceBatchDto);
            });
            dto.setOldServiceBatchDtoList(oldServiceBatchDtoList);
        }
        return dto;
    }

    @Override
    public Boolean checkEdit(Instance instance) {
        if(requestApprovalService.hasPendingStatus(instance)){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        return true;
    }

    private void saveServiceChangeDetail(Long batchId, ServiceBatchDto serviceBatchDto) {
        if(null == serviceBatchDto || null == serviceBatchDto.getServiceId() || null ==serviceBatchDto.getMarkup() || null ==batchId){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"MarkUp or ServiceId or batchId"});
        }
        ServiceChangeDetail serviceChangeDetail=new ServiceChangeDetail();
        ServiceBatchChgReq serviceBatchChgReq = serviceBatchChgReqRepository.findOne(batchId);
        BaseService baseService = baseServiceRepository.findOne(serviceBatchDto.getServiceId());
        serviceChangeDetail.setServiceBatchChgReq(serviceBatchChgReq);
        serviceChangeDetail.setBaseService(baseService);
        serviceChangeDetail.setToMarkUp(serviceBatchDto.getMarkup());
        serviceChangeDetail.setCreateBy(userService.getLoginAccountId());
        serviceChangeDetail.setCreateDate(new Date());
        serviceChangeDetailRepository.save(serviceChangeDetail);
    }

    private ServiceBatchChgReq saveServiceBatchChgReq(ServiceBatchChgReqDto serviceBatchChgReqDto, Instance instance) {
        if(null == serviceBatchChgReqDto || null ==instance){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"instance"});
        }
        ServiceBatchChgReq serviceBatchChgReq=new ServiceBatchChgReq();
        serviceBatchChgReq.setBatchNo(GenerateUniqueIDUtil.generateSrvUniqueId());
        serviceBatchChgReq.setCurrentEnvir(instance);
        serviceBatchChgReq.setCreateBy(userService.getLoginAccountId());
        serviceBatchChgReq.setCreateDate(new Date());
        serviceBatchChgReqRepository.save(serviceBatchChgReq);
        return serviceBatchChgReq;
    }

    private boolean checkSort(Class clazz, String sort){
        return Stream.of(clazz.getDeclaredFields()).anyMatch(item -> item.getName().equals(sort));
    }

    @Override
    public RestfulResponse<List<BaseServiceDto>> queryAll() {
        List<BaseService> baseServiceList = baseServiceRepository.findActive();
        List<BaseServiceDto> list = new ArrayList<>();
        for(BaseService baseService : baseServiceList){
            BaseServiceDto dto = new BaseServiceDto();
            dto.setServiceId(baseService.getId());
            dto.setServiceCode(baseService.getServiceCode());
            dto.setServiceName(baseService.getNameEn());
            list.add(dto);
        }
        return RestfulResponse.ofData(list);
    }
}
