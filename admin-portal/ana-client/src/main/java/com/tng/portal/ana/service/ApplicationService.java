package com.tng.portal.ana.service;

import java.util.List;

import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 * Created by Zero on 2016/11/10.
 */
public interface ApplicationService {

    RestfulResponse<PageDatas> listApplications(Integer pageNo, Integer pageSize);
    List<AnaApplication> listApplications();
}
