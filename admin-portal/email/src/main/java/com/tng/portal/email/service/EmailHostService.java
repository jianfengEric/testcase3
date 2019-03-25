package com.tng.portal.email.service;

import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.email.vo.EmailHostDto;

import java.util.List;

/**
 * Created by Owen on 2017/10/31.
 */
public interface EmailHostService {

    RestfulResponse<PageDatas> listHost(Integer pageNo, Integer pageSize,String search,String sortBy,Boolean isAscending);

    RestfulResponse<String> addHost(EmailHostDto emailHostDto);

    RestfulResponse<String> updateHost(EmailHostDto emailHostDto);

    RestfulResponse<List<EmailHostDto>> listHost();

    RestfulResponse<String> deleteHost(String code);
}
