package com.tng.portal.ana.service.impl;


import com.tng.portal.ana.service.ApplicationService;
import com.tng.portal.ana.vo.ApplicationDto;
import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.repository.AnaApplicationRepository;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Zero on 2016/11/10.
 */
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private AnaApplicationRepository anaApplicationRepository;

    /**
     * Query ANA application list by page
     * @param pageNo current page number
     * @param pageSize page size
     * @return
     */
    @Override
    public RestfulResponse<PageDatas> listApplications(Integer pageNo,Integer pageSize) {
        RestfulResponse<PageDatas> restResponse = new RestfulResponse<>();
        PageDatas<ApplicationDto> pageDatas = new PageDatas<>(pageNo,pageSize);
        List<AnaApplication> list = pageDatas.findAll(anaApplicationRepository);
        List<ApplicationDto> applicationDtos = list.stream().filter(AnaApplication::getDisplay).map(item -> {
            ApplicationDto dto = new ApplicationDto(item.getCode(), item.getName(), item.getDescription(), item.getInternalEndpoint());
            dto.setIsDisplay(item.getIsDisplay());
            return dto;
        }).collect(Collectors.toList());
        pageDatas.setList(applicationDtos);
        restResponse.setData(pageDatas);
        return restResponse;
    }

    @Override
    public List<AnaApplication> listApplications() {
        return anaApplicationRepository.findAll();
    }
}
