package com.tng.portal.ana.service.impl;


import com.tng.portal.ana.entity.AnaFunction;
import com.tng.portal.ana.entity.AnaPermission;
import com.tng.portal.ana.repository.AnaFunctionRepository;
import com.tng.portal.ana.repository.AnaPermissionRepository;
import com.tng.portal.ana.service.PermissionService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.ana.vo.PermissionDto;
import com.tng.portal.ana.vo.PermissionPostDto;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Zero on 2016/11/10.
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);
    @Autowired
    private AnaPermissionRepository anaPermissionRepository;
    @Autowired
    private AnaFunctionRepository functionRepository;
    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;

    /**
     * Create new ANA permission info
     * 
     * @param postDto
     * 			new permission info
     * 
     * @return
     * @
     */
    @Override
    public RestfulResponse<Integer> createPermission(PermissionPostDto postDto)  {
        RestfulResponse<Integer> restResponse = new RestfulResponse<>();
        String name = postDto.getName();
        List<AnaPermission> list = anaPermissionRepository.findByName(name);
        if (!list.isEmpty()){
            throw new BusinessException(SystemMsg.ServerErrorMsg.exist_permission.getErrorCode());
        }
        AnaPermission maxIdAnaPermission = anaPermissionRepository.findMaxIdAnaPermission();
        int newMaxId = 1;
        if (maxIdAnaPermission != null){
            newMaxId = maxIdAnaPermission.getId()*2;
        }
        AnaPermission anaPermission = new AnaPermission();
        anaPermission.setId(newMaxId);
        anaPermission.setName(name);
        anaPermission.setDescription(postDto.getDescription());

        anaPermissionRepository.save(anaPermission);
        restResponse.setSuccessStatus();
        restResponse.setData(anaPermission.getId());
        restResponse.setSuccessStatus();
        return restResponse;
    }

    /**
     * Update ANA permission info
     * 
     * @param postDto
     * 			updated permission info
     * 
     * @return
     * @
     */
    @Override
    public RestfulResponse<Integer> updatePermission(PermissionPostDto postDto)  {

        RestfulResponse<Integer> restResponse = new RestfulResponse<>();
        AnaPermission anaPermission = anaPermissionRepository.findOne(postDto.getId());
        if (anaPermission == null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_permission.getErrorCode());
        }
        if (!anaPermission.getName().equals(postDto.getName())){
            List<AnaPermission> list = anaPermissionRepository.findByName(postDto.getName());
            if (!list.isEmpty()){
                throw new BusinessException(SystemMsg.ServerErrorMsg.exist_permission.getErrorCode());
            }
            anaPermission.setName(postDto.getName());
        }
        anaPermission.setDescription(postDto.getDescription());

        restResponse.setData(anaPermission.getId());
        restResponse.setSuccessStatus();
        return restResponse;
    }

    /**
     * Delete ANA permission by permission id
     * 
     * @param id
     * 			ANA_PERMISSION.ID
     * 
     * @return
     * @
     */
    @Override
    public RestfulResponse<Integer> deletePermission(Integer id)  {
        RestfulResponse<Integer> restResponse = new RestfulResponse<>();
        AnaPermission anaPermission = anaPermissionRepository.findOne(id);
        if (anaPermission == null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_permission.getErrorCode());
        }
        List<AnaFunction> list = functionRepository.findAll();
        int total = 0;
        for (AnaFunction function : list){
            total = total | function.getPermissionSum();
        }
        int m = id & total;
        // Occupied? 
        if (m==id){
            throw new BusinessException(SystemMsg.ServerErrorMsg.delete_permission_error.getErrorCode());
        }

        anaPermissionRepository.delete(anaPermission);
        restResponse.setData(id);
        restResponse.setSuccessStatus();
        return restResponse;
    }

    /**
     * Query ANA permission list
     * 
     * @param pageNo
	 * 			current page number
	 * 
	 * @param pageSize
	 * 			page size
	 * 
	 * @param sortBy
	 * 			sort by
	 * 
	 * @param isAscending
	 * 			true--ascend or false--descend
	 * 
     * @return
     */
    @Override
    public RestfulResponse<PageDatas> listPermissions(Integer pageNo, Integer pageSize,String sortBy,Boolean isAscending) {
        RestfulResponse<PageDatas> restResponse = new RestfulResponse<>();
        PageDatas<PermissionDto> pageDatas = new PageDatas<>(pageNo,pageSize);
        List<AnaPermission> list = pageDatas.findAll(anaPermissionRepository,sortBy,isAscending,"name");
        List<PermissionDto> permissionDtos = list.stream().map(item -> {
            PermissionDto dto = new PermissionDto();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dto.setDescription(item.getDescription());
            return dto;
        }).collect(Collectors.toList());
        pageDatas.setList(permissionDtos);
        restResponse.setData(pageDatas);
        restResponse.setSuccessStatus();
        return restResponse;
    }
    
}
