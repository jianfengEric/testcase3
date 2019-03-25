package com.tng.portal.email.service.impl;

import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.email.entity.EmailAccount;
import com.tng.portal.email.entity.EmailAccountQuota;
import com.tng.portal.email.entity.EmailHost;
import com.tng.portal.email.exception.ErrorCode;
import com.tng.portal.email.repository.EmailAccountQuotaRepository;
import com.tng.portal.email.repository.EmailAccountRepository;
import com.tng.portal.email.repository.EmailHostRepository;
import com.tng.portal.email.service.EmailAccountService;
import com.tng.portal.email.vo.EmailHostAccountDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Owen on 2017/10/31.
 */
@Service
@Transactional
public class EmailAccountServiceImpl implements EmailAccountService{

    @Autowired
    private EmailAccountRepository emailAccountRepository;

    @Autowired
    private EmailHostRepository emailHostRepository;

    @Autowired
    private EmailAccountQuotaRepository emailAccountQuotaRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public RestfulResponse<Long> deleteAccount(Long id) {
        EmailAccount emailAccount = emailAccountRepository.findOne(id);
        if(emailAccount == null){
            throw new BusinessException(ErrorCode.EMAIL_ACCOUNT_NOT_EXIST_ERROR.getCode());
        }
        emailAccount.setStatus("DEL");
        emailAccountRepository.save(emailAccount);

        RestfulResponse<Long> restfulResponse = new RestfulResponse<>();
        restfulResponse.setSuccessStatus();
        restfulResponse.setData(emailAccount.getId());
        return restfulResponse;
    }

    @Override
    public RestfulResponse<Long> updateAccount(EmailHostAccountDto emailHostAccountDto) {
        EmailAccount emailAccount = emailAccountRepository.findOne(emailHostAccountDto.getId());
        if(emailAccount == null){
            throw new BusinessException(ErrorCode.EMAIL_ACCOUNT_NOT_EXIST_ERROR.getCode());
        }
        boolean flag = false;
        if(StringUtils.isNotBlank(emailHostAccountDto.getAccount())){
            emailAccount.setAccount(emailHostAccountDto.getAccount());
            flag = true;
        }
        if(StringUtils.isNotBlank(emailHostAccountDto.getPassword())){
            emailAccount.setPassword(emailHostAccountDto.getPassword());
            flag = true;
        }
        if(StringUtils.isNotBlank(emailHostAccountDto.getDefaultSenderEmail())){
            emailAccount.setDefault_sender_email(emailHostAccountDto.getDefaultSenderEmail());
            flag = true;
        }
        if(null != emailHostAccountDto.getPriority()){
            emailAccount.setPriority(emailHostAccountDto.getPriority());
            flag = true;
        }
        if(StringUtils.isNotBlank(emailHostAccountDto.getStatus())){
            emailAccount.setStatus(emailHostAccountDto.getStatus());
            flag = true;
        }
        EmailAccountQuota emailAuota = emailAccount.getEmailAccountQuota();
        if(emailHostAccountDto.getQuotaCount() != 0){
            emailAuota.setSend_quota(emailHostAccountDto.getQuotaCount());
        }
        if(StringUtils.isNotBlank(emailHostAccountDto.getQuotaType())){
        	emailAuota.setQuota_period(emailHostAccountDto.getQuotaType());
        }
        emailAccount.setEmailAccountQuota(emailAuota);
        if(flag){
            emailAccountRepository.save(emailAccount);
        }
        RestfulResponse<Long> restfulResponse = new RestfulResponse<>();
        restfulResponse.setSuccessStatus();
        restfulResponse.setData(emailAccount.getId());
        return restfulResponse;
    }

    @Override
    public RestfulResponse<Long> addAccount(EmailHostAccountDto emailHostAccountDto) {
        if(null == emailHostAccountDto){
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode());
        }
        EmailHost emailHost = emailHostRepository.findOne(emailHostAccountDto.getCode());
        if(emailHost == null){
            throw new BusinessException(ErrorCode.EMAIL_ACCOUNT_NOT_EXIST_ERROR.getCode());
        }
        EmailAccount emailAccount = new EmailAccount();
        emailAccount.setAccount(emailHostAccountDto.getAccount());
        emailAccount.setPassword(emailHostAccountDto.getPassword());
        emailAccount.setPriority(emailHostAccountDto.getPriority());
        emailAccount.setDefault_sender_email(emailHostAccountDto.getDefaultSenderEmail());
        emailAccount.setStatus("ACT");
        emailAccount.setEmailHost(emailHost);
        emailAccount = emailAccountRepository.save(emailAccount);

        EmailAccountQuota emailAccountQuota = new EmailAccountQuota();
        emailAccountQuota.setEmail_account_id(emailAccount.getId());
        emailAccountQuota.setEmailAccount(emailAccount);
        emailAccountQuota.setQuota_period(emailHostAccountDto.getQuotaType());
        emailAccountQuota.setSend_quota(emailHostAccountDto.getQuotaCount());
        emailAccountQuota.setSend_counter(0L);
        emailAccountQuotaRepository.save(emailAccountQuota);


        RestfulResponse<Long> restResponse = new RestfulResponse<>();
        restResponse.setData(emailAccount.getId());
        restResponse.setSuccessStatus();
        return restResponse;
    }

    @Override
    public RestfulResponse<PageDatas> listAccount(Integer pageNo, Integer pageSize, String search, String sortBy, Boolean isAscending) {
        RestfulResponse<PageDatas> restResponse = new RestfulResponse<>();
        PageDatas<EmailHostAccountDto> pageData = new PageDatas<>(pageNo,pageSize);
        Sort sort = pageData.pageSort(sortBy, isAscending, "priority");
        Specifications<EmailAccount> where = Specifications.where((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("status").as(String.class), "DEL"));
        Page<EmailAccount> page = emailAccountRepository.findAll(where, pageData.pageRequest(sort));
        pageData.initPageParam(page);

        pageData.setList(parseToDto(page.getContent()));
        restResponse.setData(pageData);
        restResponse.setSuccessStatus();

        return restResponse;
    }

    private List<EmailHostAccountDto> parseToDto(List<EmailAccount> list){
        if(null == list || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<EmailHostAccountDto> result = list.stream().map(item->{
            EmailHostAccountDto dto = new EmailHostAccountDto();
            dto.setId(item.getId());
            dto.setCode(item.getEmailHost().getCode());
            dto.setAccount(item.getAccount());
            dto.setPassword(item.getPassword());
            dto.setPriority(item.getPriority());
            dto.setDefaultSenderEmail(item.getDefault_sender_email());
            dto.setStatus(item.getStatus());
            dto.setQuotaType(item.getEmailAccountQuota().getQuota_period());
            dto.setQuotaCount(item.getEmailAccountQuota().getSend_quota());
            return dto;
        }).collect(Collectors.toList());

        return result;
    }

    public EmailAccount getEmailAccount(String hostCode, Long hostSizeLimit) {
        StringBuilder sb = new StringBuilder();
        sb.append("select a from EmailAccount a where a.status = 'ACT' and a.emailHost.status = 'ACT' ");
        Map<String, Object> parameters = new HashMap<>();
        if(StringUtils.isNotBlank(hostCode)){
            sb.append(" and a.emailHost.code = :hostCode ");
            parameters.put("hostCode", hostCode);
        }
        if(hostSizeLimit!=null && hostSizeLimit.longValue()>0){
            sb.append(" and a.emailHost.emailSizeLimit >= :hostSizeLimit ");
            parameters.put("hostSizeLimit", hostSizeLimit);
        }
        sb.append(" order by a.emailHost.priority asc,(coalesce(a.emailAccountQuota.send_quota,0)-coalesce(a.emailAccountQuota.send_counter,0)) desc,a.priority asc");
        Query query = entityManager.createQuery(sb.toString());
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setFirstResult(0);
        query.setMaxResults(1);
        List<EmailAccount> emailAccounts = query.getResultList();
        if(emailAccounts == null|| emailAccounts.isEmpty()){
            return null;
        }else{
            return emailAccounts.get(0);
        }
    }

}
