package com.tng.portal.ana.service.impl;


import com.tng.portal.ana.authentication.AnaPrincipalAuthenticationToken;
import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.constant.AccountStatus;
import com.tng.portal.ana.constant.AccountType;
import com.tng.portal.ana.entity.*;
import com.tng.portal.ana.repository.*;
import com.tng.portal.ana.service.RoleService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.ana.util.StringUtil;
import com.tng.portal.ana.vo.*;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.repository.AnaApplicationRepository;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Zero on 2016/11/10.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    @Autowired
    private AnaRoleRepository roleRepository;
    @Autowired
    private AnaFunctionRepository functionRepository;
    @Autowired
    private AnaApplicationRepository applicationRepository;
    @Autowired
    private AnaRoleFunctionPermissionRepository roleFunctionPermissionRepository;
    @Autowired
    private AnaAccountRepository anaAccountRepository;

    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;
    @Autowired
    private AnaPermissionRepository permissionRepository;
    @Autowired
    private AnaRoleFunctionRepository roleFunctionRepository;
    @Autowired
    private AnaRolePermissionRepository rolePermissionRepository;

    private static final String COPY=" copy";

    /**
     * Create new ANA role info
     * @param remoteAddr
     * @param postDto new role info
     * @return
     */
    @Override
    public RestfulResponse<Long> createRole(String remoteAddr, AnaRoleFunctionPermissionDto postDto)  {
        RestfulResponse<Long> restResponse = new RestfulResponse<>();
        AnaPrincipalAuthenticationToken authentication =  (AnaPrincipalAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Account loginAccount = null;
        if(authentication!=null){
            loginAccount =  authentication.getAccount();
        }
        AnaAccount account = anaAccountRepository.findOne(loginAccount.getAccountId());
        AnaRole anaRole = new AnaRole();
        if(StringUtils.isNotBlank(postDto.getExternalGroupId())&&!"tng".equalsIgnoreCase(postDto.getExternalGroupId())){
            List<AnaRole> existAnaRoles = roleRepository.findByNameAndMid(postDto.getName(),account.getExternalGroupId());
            if (!existAnaRoles.isEmpty()){
                throw new BusinessException(SystemMsg.ServerErrorMsg.exist_role.getErrorCode());
            }
            anaRole.setType("E");
            anaRole.setMid(postDto.getExternalGroupId());
        }else{
            List<AnaRole> existAnaRoles = roleRepository.findByNameAndMidIsNull(postDto.getName());
            if (!existAnaRoles.isEmpty()){
                throw new BusinessException(SystemMsg.ServerErrorMsg.exist_role.getErrorCode());
            }
            anaRole.setType("I");
        }
        AnaApplication anaApplication = applicationRepository.findOne(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName));
        if (anaApplication == null) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_application.getErrorCode());
        }
        anaRole.setName(postDto.getName());
        anaRole.setDescription(postDto.getDescription());
        anaRole.setCreateDate(new Date());
        anaRole.setLastModifyDate(new Date());
        anaRole.setIsActive(true);
        anaRole.setAnaApplication(anaApplication);
        anaRole.setUpdatedBy(account.getAccount());
        anaRole.setUpdatedTime(new Date());
        anaRole.setIpAddress(remoteAddr);
        anaRole.setIsdefault("N");
        roleRepository.saveAndFlush(anaRole);
        Long roleId = anaRole.getId();
        List<AnaRoleFunctionPermissionDto.Function> functionList = postDto.getFunctionList();
        if (functionList != null) {
            for (AnaRoleFunctionPermissionDto.Function fpDto : functionList) {
                if (fpDto.getPermissionSum()<=0){
                    continue;
                }
                AnaRoleFunctionPermission anaRoleFunctionPermissions = new AnaRoleFunctionPermission();
                AnaRoleFunctionPk pk = new AnaRoleFunctionPk();
                pk.setRoleId(roleId);
                pk.setFunctionCode(fpDto.getCode());
                anaRoleFunctionPermissions.setAnaRoleFunctionPk(pk);
                anaRoleFunctionPermissions.setPermissionsSum(fpDto.getPermissionSum());
                roleFunctionPermissionRepository.save(anaRoleFunctionPermissions);

            }
        }

        for(AnaFunctionDto function:postDto.getFunctions()){
            AnaRoleFunction roleFunction = new AnaRoleFunction();
            roleFunction.setAccessRight(function.getAccessRight());
            roleFunction.setCreateDate(new Date());
            roleFunction.setFunctionCode(function.getCode());
            roleFunction.setRoleId(roleId);
            roleFunction.setBefore(function.getBefore());
            roleFunction.setAfter(function.getAfter());
            roleFunction.setCreatorAccountid(account.getId());
            roleFunctionRepository.save(roleFunction);
            for(AnaPermissionDto permission:function.getPermissions()) {
                AnaRolePermission rolePermission = new AnaRolePermission();
                rolePermission.setAccessRight(permission.getAccessRight());
                rolePermission.setCreateDate(new Date());
                rolePermission.setPermissionId(permission.getId());
                rolePermission.setPermissionName(permission.getName());
                rolePermission.setRoleId(roleId);
                rolePermission.setFunctionCode(function.getCode());
                rolePermission.setCreatorAccountid(account.getId());
                rolePermission.setBefore(permission.getBefore());
                rolePermission.setAfter(permission.getAfter());
                rolePermissionRepository.save(rolePermission);
            }
        }
        restResponse.setData(anaRole.getId());
        restResponse.setSuccessStatus();
        return restResponse;
    }

    /**
     * update ANA role info
     * @param remoteAddr client address
     * @param postDto updated role info
     * @return
     */
    @Override
    public RestfulResponse<Long> updateRole(String remoteAddr,AnaRoleFunctionPermissionDto postDto)  {
        RestfulResponse<Long> restResponse = new RestfulResponse<>();
        AnaPrincipalAuthenticationToken authentication =  (AnaPrincipalAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Account loginAccount = null;
        if(authentication!=null){
            loginAccount =  authentication.getAccount();
        }
        AnaAccount account = anaAccountRepository.findOne(loginAccount.getAccountId());
        AnaRole anaRole = roleRepository.findOne(postDto.getId());
        if (anaRole == null) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_role.getErrorCode());
        }
        if (!anaRole.getName().equals(postDto.getName())){
            if(StringUtils.isNotBlank(account.getUserType())&&StringUtils.isNotBlank(postDto.getExternalGroupId())){
                List<AnaRole> existAnaRoles = roleRepository.findByNameAndMid(postDto.getName(),postDto.getExternalGroupId());
                if (!existAnaRoles.isEmpty()){
                    throw new BusinessException(SystemMsg.ServerErrorMsg.exist_role.getErrorCode());
                }
            }else{
                List<AnaRole> existAnaRoles = roleRepository.findByNameAndMidIsNull(postDto.getName());
                if (!existAnaRoles.isEmpty()){
                    throw new BusinessException(SystemMsg.ServerErrorMsg.exist_role.getErrorCode());
                }
            }

        }
        AnaApplication anaApplication = applicationRepository.findOne(postDto.getApplicationCode());
        if (anaApplication == null) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_application.getErrorCode());
        }
        if (!com.tng.portal.ana.util.StringUtil.isEmpty(postDto.getName())) {
            anaRole.setName(postDto.getName());
        }
        anaRole.setDescription(postDto.getDescription());
        anaRole.setLastModifyDate(new Date());
        anaRole.setAnaApplication(anaApplication);
        anaRole.setUpdatedBy(loginAccount.getUsername());
        anaRole.setUpdatedTime(new Date());
        anaRole.setIpAddress(remoteAddr);
        String type = postDto.getType();
        if(!com.tng.portal.ana.util.StringUtil.isEmpty(type)){
            anaRole.setType(type);
        }

        // Delete all RoleFunctionPermission
        roleFunctionPermissionRepository.delectByRoleId(anaRole.getId());
        List<AnaRoleFunctionPermissionDto.Function> functionList = postDto.getFunctionList();
        if (functionList != null) {
            for (AnaRoleFunctionPermissionDto.Function fpDto : functionList) {
                AnaRoleFunctionPermission anaRoleFunctionPermissions = new AnaRoleFunctionPermission();
                AnaRoleFunctionPk pk = new AnaRoleFunctionPk();
                pk.setRoleId(anaRole.getId());
                pk.setFunctionCode(fpDto.getCode());
                anaRoleFunctionPermissions.setAnaRoleFunctionPk(pk);
                anaRoleFunctionPermissions.setPermissionsSum(fpDto.getPermissionSum());
                roleFunctionPermissionRepository.save(anaRoleFunctionPermissions);
            }
        }
        for(AnaFunctionDto function:postDto.getFunctions()){
            AnaRoleFunction roleFunction = roleFunctionRepository.findOne(new AnaRoleFunctionKey(anaRole.getId(),function.getCode()));
            roleFunction.setAccessRight(function.getAccessRight());
            roleFunction.setUpdateDate(new Date());
            roleFunction.setUpdateAccountid(account.getId());
            roleFunction.setBefore(function.getBefore());
            roleFunction.setAfter(function.getAfter());
            roleFunctionRepository.save(roleFunction);
            for(AnaPermissionDto permission:function.getPermissions()) {
                AnaRolePermission rolePermission = rolePermissionRepository.findOne(new AnaRolePermissionKey(anaRole.getId(),permission.getId(),function.getCode()));
                rolePermission.setAccessRight(permission.getAccessRight());
                rolePermission.setBefore(permission.getBefore());
                rolePermission.setAfter(permission.getAfter());
                rolePermission.setUpdateDate(new Date());
                rolePermission.setUpdateAccountid(account.getId());
                rolePermissionRepository.save(rolePermission);
            }
        }
        restResponse.setData(anaRole.getId());
        restResponse.setSuccessStatus();
        return restResponse;
    }

    /**
     * delete role info by role id
     * @param request HttpServletRequest
     * @param id ANA_ROLE.ID
     * @return
     * @
     */
    @Override
    public RestfulResponse<Long> deleteRole(HttpServletRequest request,long id)  {
        RestfulResponse<Long> restResponse = new RestfulResponse<>();
        AnaRole anaRole = roleRepository.findOne(id);
        if (anaRole == null) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_role.getErrorCode());
        }
        List<AnaAccount> list = anaAccountRepository.findByAnaRoles(anaRole);
        if (!list.isEmpty()){
            throw new BusinessException(SystemMsg.ServerErrorMsg.delete_role_error.getErrorCode());
        }
        Account loginAccount = userService.getCurrentAccountInfo();

        anaRole.setIsActive(false);
        anaRole.upOperation(request, loginAccount);
        roleFunctionPermissionRepository.delectByRoleId(anaRole.getId());

        restResponse.setData(id);
        restResponse.setSuccessStatus();
        return restResponse;
    }


    /**
     * Query ANA role list
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
    public RestfulResponse<PageDatas> listRoles(Integer pageNo, Integer pageSize,String sortBy,Boolean isAscending,String nameSearch,String externalGroupIdSearch) {
        RestfulResponse<PageDatas> restResponse = new RestfulResponse<>();
        PageDatas<RoleDto> pageDatas = new PageDatas<>(pageNo,pageSize);
        Account account = userService.getCurrentAccountInfo();
        Sort sort = null;
        if(sortBy == null){
        	sort = new Sort(Direction.DESC,"createDate");
        }else{
            if("externalGroupId".equals(sortBy)){
                sortBy = "mid";
            }
        	sort = new Sort(isAscending ? Direction.ASC : Direction.DESC, sortBy);
        }
        List<AnaRole> list = new ArrayList<>();
        Page<AnaRole> page = null;
        logger.info(AccountType.ROOT.getCode()+" * "+account.getUserType()+" - "+AccountType.ROOT.getCode().equals(account.getUserType()));
        Boolean internal = account.getInternal();
        if((internal || StringUtils.isBlank(account.getExternalGroupId()))
                && (AccountType.equals(AccountType.ROOT, account.getUserType()) || AccountType.equals(AccountType.Admin, account.getUserType()))){
            if (pageDatas.isAll()){
                list = roleRepository.findByIsActiveIsTrue(sort);
                if(!StringUtil.isEmpty(nameSearch)){
                    list = roleRepository.findByIsActiveIsTrueAndNameLike(sort,"%"+nameSearch+ "%");
                }
                if(!StringUtil.isEmpty(externalGroupIdSearch)){
                    list = roleRepository.findByIsActiveIsTrueAndMidLike("%"+externalGroupIdSearch+ "%",pageDatas.pageRequest(sort)).getContent();
                }
            }else{
                page = roleRepository.findByIsActiveIsTrue(pageDatas.pageRequest(sort));
                if(!StringUtil.isEmpty(nameSearch)){
                    page = roleRepository.findByIsActiveIsTrueAndNameLike(pageDatas.pageRequest(sort),"%"+nameSearch+ "%");
                }
                if(!StringUtil.isEmpty(externalGroupIdSearch)){
                    page = roleRepository.findByIsActiveIsTrueAndMidLike("%"+externalGroupIdSearch+ "%",pageDatas.pageRequest(sort));
                }
                list=page.getContent();
                pageDatas.initPageParam(page);
            }

        }else {
            if (StringUtils.isNotBlank(account.getExternalGroupId())) {
                if (pageDatas.isAll()){
                    list = roleRepository.findByIsActiveIsTrueAndMid(account.getExternalGroupId(),sort);
                    if(!StringUtil.isEmpty(nameSearch)){
                        list = roleRepository.findByIsActiveIsTrueAndMidAndNameLike(account.getExternalGroupId(),sort,"%"+nameSearch+ "%");
                    }
                }else{
                    page = roleRepository.findByIsActiveIsTrueAndMid(account.getExternalGroupId(), pageDatas.pageRequest(sort));
                    if(!StringUtil.isEmpty(nameSearch)){
                        page = roleRepository.findByIsActiveIsTrueAndMidAndNameLike(account.getExternalGroupId(),pageDatas.pageRequest(sort),"%"+nameSearch+ "%");
                    }
                    list = page.getContent();
                    pageDatas.initPageParam(page);
                }
            } else {
                if (pageDatas.isAll()){
                    list = roleRepository.findByIsActiveIsTrueAndMidIsNull(sort);
                    if(!StringUtil.isEmpty(nameSearch)){
                        list = roleRepository.findByIsActiveIsTrueAndMidIsNullAndNameLike(sort,"%"+nameSearch+ "%");
                    }
                }else{
                    page = roleRepository.findByIsActiveIsTrueAndTypeAndMidIsNull("I",pageDatas.pageRequest(sort));
                    if(!StringUtil.isEmpty(nameSearch)){
                        page = roleRepository.findByIsActiveIsTrueAndTypeAndMidIsNullAndNameLike("I",pageDatas.pageRequest(sort),"%"+nameSearch+ "%");
                    }
                    list = page.getContent();
                    pageDatas.initPageParam(page);
                }

            }
        }
        List<RoleDto> roleDtos = list.stream().map(item -> {
            AnaApplication anaApplication = item.getAnaApplication();
            RoleDto dto = new RoleDto();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dto.setCreateDate(item.getCreateDate());
            dto.setLastModifyDate(item.getLastModifyDate());
            dto.setAnaApplication(anaApplication.getCode());
            dto.setType(item.getType());
            dto.setExternalGroupId(item.getMid());
            if("Y".equalsIgnoreCase(item.getIsdefault())){
                dto.setIsDefault(true);
            }else if("N".equalsIgnoreCase(item.getIsdefault())){
                dto.setIsDefault(false);
            }
            String type = item.getType();
            if(null!=type){
                if ("I".equals(type)){
                    dto.setInternal(true);
                }else {
                    dto.setInternal(false);
                }
            }else {
                dto.setInternal(true);
            }
            dto.setRoleType(item.getRoleType());
            return dto;
        }).collect(Collectors.toList());
        pageDatas.setList(roleDtos);
        restResponse.setData(pageDatas);
        restResponse.setSuccessStatus();
        return restResponse;
    }

    /**
     * Query all roles
     * @return
     */
    @Override
    public RestfulResponse<List<RoleDto>> listAllRoles() {
        RestfulResponse<List<RoleDto>> restResponse = new RestfulResponse<>();
        List<AnaRole> list = roleRepository.findAll();
        List<RoleDto> roleDtos = list.stream().map(item -> {
            RoleDto dto = new RoleDto();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dto.setCreateDate(item.getCreateDate());
            dto.setLastModifyDate(item.getLastModifyDate());
            dto.setAnaApplication(item.getAnaApplication().getCode());
            dto.setExternalGroupId(item.getMid());
            return dto;
        }).collect(Collectors.toList());
        restResponse.setData(roleDtos);
        return restResponse;
    }

    /**
     * Query role detail by role id
     * 
     * @param roleId
     * 			ANA_ROLE.ID
     * 
     * @return
     * @
     */
    @Override
    public RestfulResponse<RoleDetailDto> getRoleDetail(Long roleId)  {
        RestfulResponse<RoleDetailDto> restResponse = new RestfulResponse<>();
        AnaRole anaRole = roleRepository.findOne(roleId);
        if (anaRole == null) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_role.getErrorCode());
        }

        RoleDetailDto roleDetail = new RoleDetailDto();
        roleDetail.setId(anaRole.getId());
        roleDetail.setName(anaRole.getName());
        roleDetail.setDescription(anaRole.getDescription());

        AnaApplication anaApplication = anaRole.getAnaApplication();
        ApplicationDto application = new ApplicationDto();
        application.setCode(anaApplication.getCode());
        application.setName(anaApplication.getName());

        roleDetail.setApplication(application);

        roleDetail.setType(anaRole.getType());

        List<AnaRoleFunctionPermission> anaRoleFunctionPermissions = anaRole.getAnaRoleFunctions();
        List<RoleFunctionPermissionDto> rfpList = anaRoleFunctionPermissions.stream().map(item -> {
            RoleFunctionPermissionDto dto = new RoleFunctionPermissionDto();
            dto.setRoleId(item.getAnaRoleFunctionPk().getRoleId());
            dto.setCode(item.getAnaRoleFunctionPk().getFunctionCode());
            dto.setPermissionSum(item.getPermissionsSum());
            return dto;
        }).collect(Collectors.toList());

        roleDetail.setFunctionList(rfpList);
        restResponse.setData(roleDetail);
        restResponse.setSuccessStatus();
        return restResponse;
    }

    /**
     * Query all roles of current account
     * 
     * @param account
     * 			ANA_ACCOUNT.ACCOUNT
     * 
     * @return
     * @
     */
    @Override
    public RestfulResponse<List<RoleDto>> listRolesByAccount(String account)  {
        RestfulResponse<List<RoleDto>> restResponse = new RestfulResponse<>();
        if (account==null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_available_token.getErrorCode());
        }
        AnaAccount anaAccount = anaAccountRepository.findByAccountAndStatusNot(account, AccountStatus.Terminated.getCode());
        if (anaAccount == null) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        List<AnaRole> list = anaAccount.getAnaRoles();
        List<RoleDto> roleDtos = list.stream().map(item -> {
            RoleDto dto = new RoleDto();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dto.setExternalGroupId(item.getMid());
            dto.setAnaApplication(item.getAnaApplication().getCode());
            return dto;
        }).collect(Collectors.toList());
        restResponse.setData(roleDtos);
        return restResponse;
    }

    @Override
    public RestfulResponse<AnaRoleFunctionPermissionDto> newRole() {
        Sort sort = new Sort(Direction.ASC, "code");
        Account loginAccount = userService.getCurrentAccountInfo();
        Specifications<AnaFunction> where = Specifications.where((root, query, builder)-> builder.isNotNull(root.get("code")));
        if(Objects.nonNull(loginAccount) && StringUtils.isNotBlank(loginAccount.getExternalGroupId())){
            where = where.and((root, query, builder)-> builder.isTrue(root.get("external").as(Boolean.class)));
        }
        List<AnaFunction> functions =  functionRepository.findAll(where, sort);
        List<AnaPermission> permissions = permissionRepository.findAll();
        List<AnaFunctionDto> functionsResult =  new ArrayList<>();
        for(AnaFunction function:functions){
            AnaFunctionDto functionDto = new AnaFunctionDto();
            functionDto.setCode(function.getCode());
            functionDto.setDescription(function.getDescription());
            functionDto.setAccessRight("");
            List<AnaPermissionDto> permissionDtos = new ArrayList<>();
            for(AnaPermission permission:permissions){
                int resultPermission = function.getPermissionSum() & permission.getId();
                if (resultPermission == permission.getId()) {
                    AnaPermissionDto permissionDto = new AnaPermissionDto();
                    permissionDto.setId(permission.getId());
                    permissionDto.setName(permission.getName());
                    permissionDto.setAccessRight("");
                    permissionDtos.add(permissionDto);
                }
            }
            functionDto.setPermissions(permissionDtos);
            functionsResult.add(functionDto);
        }
        AnaRoleFunctionPermissionDto dto = new AnaRoleFunctionPermissionDto();
        dto.setFunctions(functionsResult);
        RestfulResponse response = new RestfulResponse();
        response.setData(dto);
        response.setSuccessStatus();
        return response;
    }

    @Override
    public RestfulResponse<AnaRoleFunctionPermissionDto> editRole(Long roleId) {
        List<AnaRoleFunction> roleFunctions = roleFunctionRepository.findByRoleId(roleId);
        List<AnaRolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
        AnaRole role = roleRepository.findOne(roleId);
        /*Sort sort = new Sort(Direction.DESC, "code");
        List<AnaFunction> functions =  functionRepository.findAll(sort);
        List<AnaPermission> permissions = permissionRepository.findAll();
        Map<String,AnaRoleFunctionPermission> mappedFunctionPermission = new HashMap<>();
        for(AnaRoleFunctionPermission anaRoleFunctionPermission:role.getAnaRoleFunctions()){
            mappedFunctionPermission.put(anaRoleFunctionPermission.getAnaFunction().getCode(),anaRoleFunctionPermission);
        }
        Map<String,AnaRoleFunction> roleFunctionsMap = roleFunctions.stream().collect(Collectors.toMap(AnaRoleFunction::getFunctionCode, (p) -> p));
        Map<String,AnaRolePermission> rolePermissionsMap = new HashMap<>();
        for(AnaRolePermission anaRolePermission:rolePermissions){
            rolePermissionsMap.put(anaRolePermission.getFunctionCode()+"#"+anaRolePermission.getPermissionId(),anaRolePermission);
        }
        //init role >> function
//        if(roleFunctions==null || roleFunctions.size()==0){
            for(AnaFunction function:functions){
                if(roleFunctionsMap.get(function.getCode())==null) {
                    AnaRoleFunction roleFunction = new AnaRoleFunction();
                    if (mappedFunctionPermission.get(function.getCode()) != null) {
                        roleFunction.setAccessRight("RW");
                    } else {
                        roleFunction.setAccessRight("");
                    }
                    roleFunction.setCreateDate(new Date());
                    roleFunction.setFunctionCode(function.getCode());
                    roleFunction.setRoleId(roleId);
                    roleFunctionRepository.save(roleFunction);
                }
                for(AnaPermission permission:permissions) {
                    int resultPermission = function.getPermissionSum() & permission.getId();
                    if (resultPermission == permission.getId() && rolePermissionsMap.get(function.getCode()+"#"+permission.getId())==null) {
                        AnaRolePermission rolePermission = new AnaRolePermission();
                        if(mappedFunctionPermission.get(function.getCode())==null){
                            rolePermission.setAccessRight("");
                        }else{
                            resultPermission = mappedFunctionPermission.get(function.getCode()).getPermissionsSum() & permission.getId();
                            if (resultPermission == permission.getId()) {
                                rolePermission.setAccessRight("E");
                            }else{
                                rolePermission.setAccessRight("");
                            }
                        }
                        rolePermission.setCreateDate(new Date());
                        rolePermission.setPermissionId(permission.getId());
                        rolePermission.setPermissionName(permission.getName());
                        rolePermission.setFunctionCode(function.getCode());
                        rolePermission.setRoleId(roleId);
                        rolePermissionRepository.save(rolePermission);
                    }
                }
            }*/
//        }
//        roleFunctions = roleFunctionRepository.findByRoleId(roleId);
//        rolePermissions = rolePermissionRepository.findByRoleId(roleId);
        List<AnaFunctionDto> functionsResult =  new ArrayList<>();
        for(AnaRoleFunction roleFunction:roleFunctions){
            Account account = userService.getCurrentAccountInfo();
            AnaAccount anaAccount = anaAccountRepository.findById(account.getAccountId());
            AnaFunction anaFunction = functionRepository.findOne(roleFunction.getFunctionCode());
            if(hasfunction(anaAccount,anaFunction)){
                AnaFunctionDto functionDto = new AnaFunctionDto();
                functionDto.setCode(roleFunction.getFunctionCode());
                functionDto.setAccessRight(StringUtils.trimToEmpty(roleFunction.getAccessRight()));
                functionDto.setBefore(StringUtils.trimToEmpty(roleFunction.getBefore()));
                functionDto.setAfter(StringUtils.trimToEmpty(roleFunction.getAfter()));
                List<AnaPermissionDto> permissionDtos = new ArrayList<>();
                for(AnaRolePermission permission:rolePermissions){
                    if(roleFunction.getFunctionCode().equals(permission.getFunctionCode())) {
                        AnaPermissionDto permissionDto = new AnaPermissionDto();
                        permissionDto.setId(permission.getPermissionId());
                        permissionDto.setName(permission.getPermissionName());
                        permissionDto.setAccessRight(StringUtils.trimToEmpty(permission.getAccessRight()));
                        permissionDto.setBefore(StringUtils.trimToEmpty(permission.getBefore()));
                        permissionDto.setAfter(StringUtils.trimToEmpty(permission.getAfter()));
                        permissionDtos.add(permissionDto);
                    }
                }
                functionDto.setPermissions(permissionDtos);
                functionsResult.add(functionDto);
            }
        }
        AnaRoleFunctionPermissionDto dto = new AnaRoleFunctionPermissionDto();
        dto.setFunctions(functionsResult);
        dto.setApplicationCode(role.getAnaApplication().getCode());
        dto.setDescription(role.getDescription());
        dto.setExternalGroupId(role.getMid());
        dto.setName(role.getName());
        if("Y".equalsIgnoreCase(role.getIsdefault())){
            dto.setIsDefault(true);
        }else if("N".equalsIgnoreCase(role.getIsdefault())){
            dto.setIsDefault(false);
        }
        dto.setId(role.getId());
        RestfulResponse response = new RestfulResponse();
        response.setData(dto);
        response.setSuccessStatus();
        return response;
    }

    /**
     * Check if internal/external user have function code
     * @param anaAccount
     * @param anaFunction
     * @return
     */
    private Boolean hasfunction(AnaAccount anaAccount,AnaFunction anaFunction){
        Boolean internal = anaAccount.getInternal();
        Boolean external = anaFunction.getExternal();
        return internal || external;
    }

    @Override
    public List<AnaRole> listRoles() {
        Account account = userService.getCurrentAccountInfo();
        List<AnaRole> anaRoles = new ArrayList<>();
        if(StringUtils.isNotBlank(account.getExternalGroupId())){
            anaRoles = roleRepository.findByMidAndIsActiveIsTrue(account.getExternalGroupId(),new Sort(Direction.DESC,"name"));
        }else {
            anaRoles = roleRepository.findByIsActiveIsTrue(new Sort(Direction.DESC,"name"));
        }
        return anaRoles;
    }

    /**
     * Query roles and permission by ANA account
     * 
     * @param account
     * 			ANA_ACCOUNT.ACCOUNT
     * 
     * @return
     * @
     */
    /*public RestResponse<List<RoleDetailDto>> listRoles_PermissionByAccount(String account)  {
        RestResponse<List<RoleDetailDto>> restResponse = new RestResponse<>();
        if (account==null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_available_token.getErrorCode());
        }
//        AnaAccount anaAccount = anaAccountRepository.findByAccount(account);
        AnaAccount anaAccount = anaAccountRepository.findByAccountAndStatusNot(account, AccountStatus.Terminated.getCode());
        if (anaAccount == null) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        List<AnaRole> list = anaAccount.getAnaRoles();
        List<RoleDetailDto> roleDtos = list.stream().map(item -> {
            RoleDetailDto dto = new RoleDetailDto();
            dto.setId(item.getId());
            dto.setName(item.getName());
            AnaApplication anaApplication = item.getAnaApplication();
            ApplicationDto applicationDto = new ApplicationDto(anaApplication.getCode(),anaApplication.getName());
            dto.setApplication(applicationDto);

            List<AnaRoleFunctionPermission> list1 = item.getAnaRoleFunctions();
            List<RoleFunctionPermissionDto> list2 = list1.stream().map(it -> {
                RoleFunctionPermissionDto dto1 = new RoleFunctionPermissionDto();
                dto1.setRoleId(it.getAnaRoleFunctionPk().getRoleId());
                dto1.setCode(it.getAnaRoleFunctionPk().getFunctionCode());
                return dto1;
            }).collect(Collectors.toList());
            dto.setFunctionList(list2);
            return dto;
        }).collect(Collectors.toList());

        restResponse.setData(roleDtos);
        return restResponse;
    }*/

    /**
     * filter roles by current account application
     * 
     * @param anaRoles
     * 			ANA role list
     * @return
     */
    public List<AnaRole> filterRolesByCurrentAcountApplication(List<AnaRole> anaRoles){
        String account = userService.getCurrentAccount();
        AnaAccount anaAccount = anaAccountRepository.findByAccountAndStatusNot(account, AccountStatus.Terminated.getCode());
        List<AnaRole> anaAccountAnaRoles = anaAccount.getAnaRoles();
        List<String> applicationCodes = anaAccountAnaRoles.stream().map(item -> item.getAnaApplication().getCode()).distinct().collect(Collectors.toList());
        return anaRoles.stream().filter(role -> applicationCodes.contains(role.getAnaApplication().getCode())).collect(Collectors.toList());
    }

	@Override
	public Long copyRole(Long roleId) {
		AnaRole role = roleRepository.findOne(roleId);
        AnaRole newRole = new AnaRole();
        BeanUtils.copyProperties(role,newRole);
        List<AnaRole> existsCopyRoles = roleRepository.findByMidAndNameStartingWith(role.getMid(), role.getName() + COPY) ;
        if(existsCopyRoles == null || existsCopyRoles.isEmpty()){
        	newRole.setName(role.getName() + " copy1");
        }else{
        	int maxIndex = 0;
        	int index = 0;
        	for(AnaRole r : existsCopyRoles){
        		int count = 0;
                Pattern pattern = Pattern.compile(COPY);
                Matcher m = pattern.matcher(r.getName().substring(role.getName().length()));
                while (m.find()) {
                	count++;
                }
                if(count == 1){
                	index = Integer.parseInt(r.getName().substring((role.getName() + COPY).length()));
                	if(index > maxIndex){
                		maxIndex = index;
                	}
                }
        	}
        	newRole.setName(role.getName() + COPY + (maxIndex + 1));
        	
        }
        if(newRole.getName().length()>100){
            throw new BusinessException(SystemMsg.ServerErrorMsg.ROLE_NAME_TOO_LONG.getErrorCode());
        }
        newRole.setId(null);
        newRole.setAnaAccounts(null);
        newRole.setAnaRoleFunctions(null);
        newRole.setIsdefault("N");
        newRole.setCreateDate(new Date());
        roleRepository.saveAndFlush(newRole);
        List<AnaRoleFunctionPermission> anaRoleFunctionPermissions = new ArrayList<>();
        for(AnaRoleFunctionPermission arfp:role.getAnaRoleFunctions()){
            AnaRoleFunctionPermission anaRoleFunctionPermission = new AnaRoleFunctionPermission();
            BeanUtils.copyProperties(arfp,anaRoleFunctionPermission);
            anaRoleFunctionPermission.setAnaRoleFunctionPk(new AnaRoleFunctionPk(newRole.getId(), arfp.getAnaFunction().getCode()));
            anaRoleFunctionPermission.setAnaRole(newRole);
            anaRoleFunctionPermissions.add(anaRoleFunctionPermission);
        }

        newRole.setAnaRoleFunctions(anaRoleFunctionPermissions);


        List<AnaRoleFunction> roleFunctions = roleFunctionRepository.findByRoleId(role.getId());
        List<AnaRolePermission> rolePermissions = rolePermissionRepository.findByRoleId(role.getId());

        for(AnaRoleFunction function:roleFunctions){
            AnaRoleFunction anaRoleFunction = new AnaRoleFunction();
            BeanUtils.copyProperties(function,anaRoleFunction);
            anaRoleFunction.setRoleId(newRole.getId());
            roleFunctionRepository.save(anaRoleFunction);
        }
        for(AnaRolePermission permission:rolePermissions){
            AnaRolePermission anaRolePermission = new AnaRolePermission();
            BeanUtils.copyProperties(permission,anaRolePermission);
            anaRolePermission.setRoleId(newRole.getId());
            rolePermissionRepository.save(anaRolePermission);
        }
        return newRole.getId();
	}



    @Override
    public void autoCreateRole(String mid){
        //create role
        if(StringUtils.isBlank(mid)){
            return;
        }
        List<AnaRole> roles = roleRepository.findByMid(mid);
        if(CollectionUtils.isEmpty(roles)){
            roles = roleRepository.findByTypeAndIsdefaultAndMidIsNull("E", "Y");
            Date createDate = new Date();
            for(AnaRole role:roles){
                AnaRole newRole = new AnaRole();
                BeanUtils.copyProperties(role, newRole, new String[]{"id", "anaAccounts", "anaRoleFunctions", "updatedBy", "updatedTime"});
                newRole.setId(null);
                newRole.setAnaAccounts(null);
                newRole.setMid(mid);
                newRole.setType("E");
                newRole.setAnaRoleFunctions(null);
                newRole.setIsdefault("Y");
                newRole.setCreateDate(createDate);
                newRole.setLastModifyDate(createDate);
                roleRepository.saveAndFlush(newRole);
                List<AnaRoleFunctionPermission> anaRoleFunctionPermissions = new ArrayList<>();
                for(AnaRoleFunctionPermission arfp:role.getAnaRoleFunctions()){
                    AnaRoleFunctionPermission anaRoleFunctionPermission = new AnaRoleFunctionPermission();
                    BeanUtils.copyProperties(arfp,anaRoleFunctionPermission);
                    anaRoleFunctionPermission.setAnaRoleFunctionPk(new AnaRoleFunctionPk(newRole.getId(), arfp.getAnaFunction().getCode()));
                    anaRoleFunctionPermission.setAnaRole(newRole);
                    anaRoleFunctionPermissions.add(anaRoleFunctionPermission);
                }
                newRole.setAnaRoleFunctions(anaRoleFunctionPermissions);
                List<AnaRoleFunction> roleFunctions = roleFunctionRepository.findByRoleId(role.getId());
                List<AnaRolePermission> rolePermissions = rolePermissionRepository.findByRoleId(role.getId());
                for(AnaRoleFunction function:roleFunctions){
                    AnaRoleFunction anaRoleFunction = new AnaRoleFunction();
                    BeanUtils.copyProperties(function,anaRoleFunction, new String[]{"creatorAccountid", "updateAccountid", "updateDate"});
                    anaRoleFunction.setRoleId(newRole.getId());
                    anaRoleFunction.setCreateDate(createDate);
                    roleFunctionRepository.save(anaRoleFunction);
                }
                for(AnaRolePermission permission:rolePermissions){
                    AnaRolePermission anaRolePermission = new AnaRolePermission();
                    BeanUtils.copyProperties(permission,anaRolePermission, new String[]{"creatorAccountid", "updateAccountid", "updateDate"});
                    anaRolePermission.setRoleId(newRole.getId());
                    anaRolePermission.setCreateDate(createDate);
                    rolePermissionRepository.save(anaRolePermission);
                }
            }
        }
    }

}
