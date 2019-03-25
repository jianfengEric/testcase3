package com.tng.portal.email.service.impl;

import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.email.entity.EmailHost;
import com.tng.portal.email.exception.ErrorCode;
import com.tng.portal.email.repository.EmailHostRepository;
import com.tng.portal.email.service.EmailHostService;
import com.tng.portal.email.vo.EmailHostDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Owen on 2017/10/31.
 */
@Service
@Transactional
public class EmailHostServiceImpl implements EmailHostService{

    @Autowired
    private EmailHostRepository emailHostRepository;

    @Override
    public RestfulResponse<String> deleteHost(String code) {
        EmailHost emailHost = emailHostRepository.findOne(code);
        if(emailHost == null){
            throw new BusinessException(ErrorCode.EMAIL_ACCOUNT_NOT_EXIST_ERROR.getCode());
        }
        emailHost.setStatus("DEL");
        emailHostRepository.save(emailHost);
        RestfulResponse<String> restfulResponse = new RestfulResponse<>();
        restfulResponse.setSuccessStatus();
        restfulResponse.setData(emailHost.getCode());
        return restfulResponse;
    }

    @Override
    public RestfulResponse<List<EmailHostDto>> listHost() {
        RestfulResponse<List<EmailHostDto>> restResponse = new RestfulResponse<>();
        Sort sort = new Sort(Sort.Direction.ASC, "code");
        List<EmailHost> list =  emailHostRepository.findByStatus("ACT", sort);

        restResponse.setSuccessStatus();
        restResponse.setData(parseToDto(list));
        return restResponse;
    }

    @Override
    public RestfulResponse<String> updateHost(EmailHostDto emailHostDto) {
        EmailHost emailHost = emailHostRepository.findOne(emailHostDto.getCode());
        if(emailHost == null){
            throw new BusinessException(ErrorCode.EMAIL_ACCOUNT_NOT_EXIST_ERROR.getCode());
        }
        boolean flag = false;
        if(StringUtils.isNotBlank(emailHostDto.getProvider())){
            emailHost.setPriority(emailHostDto.getPriority());
            flag = true;
        }
        if(StringUtils.isNotBlank(emailHostDto.getHost1())){
            emailHost.setHost1(emailHostDto.getHost1());
            flag = true;
        }
        if(StringUtils.isNotBlank(emailHostDto.getHost2())){
            emailHost.setHost2(emailHostDto.getHost2());
            flag = true;
        }
        if(StringUtils.isNotBlank(emailHostDto.getStatus())){
            emailHost.setStatus(emailHostDto.getStatus());
            flag = true;
        }
        if(null != emailHostDto.getPort()){
            emailHost.setPort(emailHostDto.getPort());
            flag = true;
        }
        if(null != emailHostDto.getPriority()){
            emailHost.setPriority(emailHostDto.getPriority());
            flag = true;
        }
        if(null != emailHostDto.getEmailSizeLimit()){
            emailHost.setEmailSizeLimit(emailHostDto.getEmailSizeLimit());
            flag = true;
        }
        if(flag){
            emailHostRepository.save(emailHost);
        }
        RestfulResponse<String> restfulResponse = new RestfulResponse<>();
        restfulResponse.setSuccessStatus();
        restfulResponse.setData(emailHost.getCode());
        return restfulResponse;
    }

    @Override
    public RestfulResponse<String> addHost(EmailHostDto emailHostDto) {
        if(null == emailHostDto){
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode());
        }
        EmailHost emailHost = new EmailHost();
        BeanUtils.copyProperties(emailHostDto, emailHost);
        emailHost.setStatus("ACT");
        emailHost.setProvider(emailHost.getCode());
        emailHost.setRequire_auth(1L);
        emailHost = emailHostRepository.save(emailHost);
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        restResponse.setData(emailHost.getCode());
        restResponse.setSuccessStatus();
        return restResponse;
    }

    @Override
    public RestfulResponse<PageDatas> listHost(Integer pageNo, Integer pageSize, String search, String sortBy, Boolean isAscending) {
        RestfulResponse<PageDatas> restResponse = new RestfulResponse<>();
        PageDatas<EmailHostDto> pageData = new PageDatas<>(pageNo,pageSize);
        Sort sort = pageData.pageSort(sortBy, isAscending, "code");
        Specifications<EmailHost> where = Specifications.where((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("status").as(String.class), "DEL"));
        Page<EmailHost> page = emailHostRepository.findAll(where, pageData.pageRequest(sort));
        pageData.initPageParam(page);

        pageData.setList(parseToDto(page.getContent()));
        restResponse.setData(pageData);
        restResponse.setSuccessStatus();

        return restResponse;
    }

    private List<EmailHostDto> parseToDto(List<EmailHost> list){
        if(null == list || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<EmailHostDto> result = list.stream().map(item->{
            EmailHostDto dto = new EmailHostDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());

        return result;
    }
}
