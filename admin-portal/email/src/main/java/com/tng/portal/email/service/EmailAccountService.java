package com.tng.portal.email.service;

import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.email.entity.EmailAccount;
import com.tng.portal.email.vo.EmailHostAccountDto;

/**
 * Created by Owen on 2017/10/31.
 */
public interface EmailAccountService {

    RestfulResponse<PageDatas> listAccount(Integer pageNo, Integer pageSize, String search, String sortBy, Boolean isAscending);

    RestfulResponse<Long> addAccount(EmailHostAccountDto emailHostAccountDto);

    RestfulResponse<Long> updateAccount(EmailHostAccountDto emailHostAccountDto);

    RestfulResponse<Long> deleteAccount(Long id);

    EmailAccount getEmailAccount(String hostCode, Long hostSizeLimit);

}
