package com.gea.portal.ewp.service.impl;

import com.gea.portal.ewp.entity.*;
import com.gea.portal.ewp.entity.enums.ParticipantViewStatus;
import com.gea.portal.ewp.repository.*;
import com.gea.portal.ewp.service.*;
import com.tng.portal.ana.bean.Account;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.ana.util.DateUtil;
import com.tng.portal.common.constant.DateCode;
import com.tng.portal.common.dto.ewp.*;
import com.tng.portal.common.dto.gea.GeaBaseResponse;
import com.tng.portal.common.dto.srv.BaseServiceDto;
import com.tng.portal.common.enumeration.*;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.*;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Owen on 2018/8/24.
 */
@Service
@Transactional
public class EwalletParticipantServiceImpl implements EwalletParticipantService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EwalletParticipantRepository ewalletParticipantRepository;

    @Autowired
    private EwpServiceMoneyPoolRepository ewpMoneyPoolRepository;

    @Autowired
    private EwpCompanyFormRepository ewpCompanyFormRepository;

    @Autowired
    private EwpOwnerRepository ewpOwnerRepository;

    @Autowired
    private EwpKeyPersonRepository ewpKeyPersonRepository;

    @Autowired
    private EwpGatewayConfigRepository ewpGatewayConfigRepository;

    @Autowired
    private EwpServiceRepository ewpServiceRepository;

    @Autowired
    private EwpMaterialRepository ewpMaterialRepository;
    @Autowired
    private EwpDisputeContactRepository ewpDisputeContactRepository;
    @Autowired
    private EwpStatusChangeRepository ewpStatusChangeRepository;
    @Autowired
    private EwpRecordEnvMapRepository ewpRecordEnvMapRepository;

    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;

    @Autowired
    private RequestApprovalService requestApprovalService;

    @Autowired
    private EwpServiceSettlementRepository ewpServiceSettlementRepository;
    @Autowired
    private EwpServiceConfigRepository ewpServiceConfigRepository;
    @Autowired
    private EwpServiceCurrConfigRepository ewpServiceCurrConfigRepository;
    @Autowired
    private EwpServiceMoneyPoolRepository ewpServiceMoneyPoolRepository;

    @Autowired
    private RequestApprovalRepository requestApprovalRepository;
    @Autowired
    private EwpDeploymentRepository ewpDeploymentRepository;
    @Autowired
    private EwpSysConfigRepository ewpSysConfigRepository;

    @Autowired
    private MpCallerService mpCallerService;
    @Autowired
    private DpyCallerService dpyCallerService;
    @Autowired
    private SrvCallerService srvCallerService;
    
    @Autowired
    private HttpClientUtils httpClientUtils;


    @Value("${material.dir}")
    private String materialDir;
    @Autowired
    private JpaUtil jpaUtil;

    private String genGeaId(String area){
        String currentId = ewalletParticipantRepository.findMaxGeaRefId(area+"%");
        if(StringUtils.isBlank(currentId)){
            return area + "A001";
        }
        String sequence = currentId.replace(area, "");
        // Create next sequence 
        String front = sequence.substring(0, 1);
        String back = sequence.substring(1,4);
        int i = front.charAt(0);
        Integer currentNum = Integer.valueOf(i + back);
        if(currentNum >= 90999){  // More than Z999
            throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }
        String nextNum = String.valueOf(currentNum + 1);
        String front1 = nextNum.substring(0,2);
        String back1 = nextNum.substring(2, 5);
        if("000".equals(back1)){
            back1 = "001";
        }
        char c = (char)(Integer.parseInt(front1));
        return area + c + back1;
    }
    
    private Long genGeaSysPid(Instance instance) {
        Long geaSysPid = null;
        if(instance == Instance.PRE_PROD){
        	geaSysPid = ewalletParticipantRepository.findMaxGeaPrePid();
        }else{
        	geaSysPid = ewalletParticipantRepository.findMaxGeaPrdPid();
        }
        if(geaSysPid == null){
            try {
            	String url = PropertiesUtil.getAppValueByKey("gea.api."+(instance==Instance.PRE_PROD?"PRE":"PRD")+".getMaxPid")+"?api_key="+PropertiesUtil.getAppValueByKey("gea.api.api_key");
				GeaBaseResponse geaRes = httpClientUtils.httpGet(url, new ParameterizedTypeReference<GeaBaseResponse>() {}, null);
				if(!geaRes.getStatus().equals("success")){
					throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
				}
				geaSysPid = Long.parseLong(geaRes.getParticipantEntry().getDeployDetail().get(0).getDetails().get(0).getGeaSysPid());
				return geaSysPid + 50;  //预留空位
			} catch (Exception e) {
				logger.error("请求gea 失败",e);
				throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
			}
        }
        return geaSysPid + 1;
    }


    @Override
    public PageDatas<ParticipantDto> listParticipant(Integer pageNo, Integer pageSize, String geaParticipantRefId, String participantName, String participantStatus,String approvalStatus,Instance instance,String sortBy,Boolean isAscending) {
        String geaRefId = "gea_ref_id";
        String statusField = instance == Instance.PRE_PROD ? "pre_status" : "prod_status";
    	List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id,p.gea_ref_id,p."+statusField+" par_status,p.create_by,IFNULL(c_atv.participant_name_en,c_pdg.participant_name_en) pit_name,");
        sql.append("   IF(count(distinct ser_atv.id)=0,count(distinct ser_pdg.id),count(distinct ser_atv.id)) ser_no,ra.status ");
        sql.append(" from ewallet_participant p ");

        sql.append("  LEFT JOIN ewp_company_form c_atv on (c_atv.participant_id=p.id and c_atv.current_envir='"+instance.getValue()+"' and c_atv.status='ACTIVE' ) ");
        sql.append("  left join ewp_company_form c_pdg on (c_pdg.participant_id=p.id and c_pdg.current_envir='"+instance.getValue()+"' and c_pdg.status='PENDING_FOR_PROCESS') ");

        sql.append("  left join ewp_service ser_atv on (ser_atv.participant_id=p.id and ser_atv.current_envir='"+instance.getValue()+"' and ser_atv.status='ACTIVE')  ");
        sql.append("  left join ewp_service ser_pdg on (ser_pdg.participant_id=p.id and ser_pdg.current_envir='"+instance.getValue()+"' and ser_pdg.status='PENDING_FOR_PROCESS')  ");

        sql.append(" left join ( ");
        sql.append(" 	select a.* ");
        sql.append(" 	from (select * from request_approval where current_envir='"+instance.getValue()+"') a  ");
        sql.append(" 	left join (select * from request_approval where current_envir='"+instance.getValue()+"') b on a.participant_id=b.participant_id and a.create_date<b.create_date  ");
        sql.append(" 	group by a.id ");
        sql.append(" 	having count(b.id)<1 ");
        sql.append(" 	order by a.participant_id,a.create_date ");
        sql.append(" ) ra on (ra.participant_id=p.id) ");

        sql.append(" where p."+statusField+" not in ('"+ParticipantStatus.REJECTED.getValue()+"') ");
            
        if(geaParticipantRefId != null){
            sql.append(" and p.gea_ref_id  like ? ");
            params.add("%"+geaParticipantRefId+"%");
        }
        if(participantStatus != null){
            sql.append(" and p."+statusField+"  = ? ");
            params.add(participantStatus);
        }
        if(approvalStatus != null){
        	if(instance==Instance.PROD){
        		if(approvalStatus.equals(RequestApprovalStatus.ST)){
        			sql.append(" and p.id not in (select participant_id from ewp_deployment where deploy_envir='PROD' and STATUS='DEPLOYED') ");
        		}else {
        			sql.append(" and (ra.status in ? and p.id in (select participant_id from ewp_deployment where deploy_envir='PROD' and STATUS='DEPLOYED')) ");
        			List<String> list = RequestApprovalStatus.findByListView(approvalStatus).stream().map(RequestApprovalStatus::getValue).collect(Collectors.toList());
            		params.add(list);
        		}
        	}else{
        		sql.append(" and ra.status in ? ");
        		List<String> list = RequestApprovalStatus.findByListView(approvalStatus).stream().map(RequestApprovalStatus::getValue).collect(Collectors.toList());
        		params.add(list);
        	}
        }
        if(StringUtils.isNotBlank(participantName)){
            sql.append(" and ( c_atv.participant_name_en like ? or c_pdg.participant_name_en like ? ) ");
            params.add("%"+participantName+"%");
            params.add("%"+participantName+"%");
        }
        Account accountInfo = userService.getCurrentAccountInfo();
        Long merchantId = accountInfo.getMerchantId();
        Boolean internal = accountInfo.getInternal();
        if(merchantId!=null && !internal){
            sql.append(" and p.id  = ? ");
            params.add(merchantId);
        }

        sql.append(" GROUP BY p.id ");
        //排序
        String sortName = "p.create_date";
        String sortType = "desc";
        if("geaParticipantRefId".equals(sortBy)){
        	sortName = "p.gea_ref_id";
        }else if("participantName".equals(sortBy)){
        	sortName = "pit_name";
        }else if("serviceNo".equals(sortBy)){
        	sortName = "ser_no";
        }else if("participantStatus".equals(sortBy)){
	    	sortName = "p."+statusField;
	    }else if("approvalStatus".equals(sortBy)){
	    	sortName = "ra.status";
	    }
        if(Boolean.TRUE.equals(isAscending)){
        	sortType = "ASC";
        }else if(Boolean.FALSE.equals(isAscending)){
        	sortType = "DESC";
        }
        sql.append(" order by "+sortName+" "+sortType+" ");

        PageDatas<ParticipantDto> pageDatas = new PageDatas<>(null==pageNo?1:pageNo,null==pageSize?10:pageSize);
        Page<Map<String, Object>> page = jpaUtil.pageBySql(sql.toString(), pageDatas.pageRequest(), params.toArray());
        // mp total
        Map<String, Long> mpList = new HashMap<>();
        try {
        	ArrayList<String> geaIdList = (ArrayList<String>)page.getContent().stream().map(item -> (String)item.get(geaRefId)).collect(Collectors.toList());
        	ArrayList<String> mpStatus = (ArrayList<String>)MoneyPoolStatus.findValidStatus().stream().map(item -> item.getValue()).collect(Collectors.toList());
        	if(CollectionUtils.isNotEmpty(geaIdList)){
        		mpList = mpCallerService.callFindMoneyPoolCount(geaIdList, mpStatus, instance).getData();
        	}
        } catch (Exception e) {
            logger.error(" Connect mp fail ", e);
        }
        
        List<ParticipantDto> list = new ArrayList<>();
        for(Map<String, Object> item : page.getContent()){
            ParticipantDto dto = new ParticipantDto();
            dto.setParticipantId(item.get("id").toString());
            dto.setGeaParticipantRefId((String) item.get(geaRefId));
            dto.setParticipantName((String) item.get("pit_name"));
            dto.setServiceNo(item.get("ser_no").toString());
            dto.setParticipantStatus(ParticipantViewStatus.participantStatusToView((String) item.get("par_status")).getValue());
            dto.setApprovalStatus(RequestApprovalStatus.findByValue((String)item.get("status")).getListView());
            // First approval for production   A ApprovalStatus display
            if(instance == Instance.PROD && !dpyCallerService.callIsDeploy((String) item.get(geaRefId), instance).getData()){
                dto.setApprovalStatus(RequestApprovalStatus.ST);
            }
            //mp total
            dto.setMoneyPoolNo(mpList.get(item.get(geaRefId))==null?"0":mpList.get(item.get(geaRefId)).toString());
            list.add(dto);

        }
        pageDatas.initDatas(list, page.getTotalElements(), page.getTotalPages());
        return pageDatas;


    }

    @Override
    public RestfulResponse<String> updateApiGatewaySetting(ApiGatewaySettingDto postDto) {
        String EWALLET_PARTICIPANT = "ewalletParticipant";
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        if(null == postDto || null == postDto.getParticipantId()){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ApiGatewaySettingDto or ParticipantId"});
        }
        Instance instance = Instance.valueOf(postDto.getInstance());
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(Long.valueOf(postDto.getParticipantId()));
        if(null == ewalletParticipant){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{EWALLET_PARTICIPANT});
        }
        /*if(hasPending(ewalletParticipant.getGeaRefId(), instance,null)){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        if(mpCallerService.callHasPending(ewalletParticipant.getGeaRefId(), instance,null).getData()){
            throw new BusinessException(SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }*/

        EwpGatewayConfig ewpGatewayConfig = new EwpGatewayConfig();
        BeanUtils.copyProperties(postDto, ewpGatewayConfig, new String[]{"id"});
        ewpGatewayConfig.setEwalletParticipant(ewalletParticipant);
        ewpGatewayConfig.setCurrentEnvir(instance);
        ewpGatewayConfig.setStatus(ParticipantStatus.PENDING_FOR_PROCESS);
        ewpGatewayConfig.setCreateDate(new Date());
        ewpGatewayConfig.setCreateBy(userService.getLoginAccountId());
        ewpGatewayConfigRepository.save(ewpGatewayConfig);

        requestApprovalService.save(ewalletParticipant, "", "", "", "", String.valueOf(ewpGatewayConfig.getId()), "", instance, ApprovalType.API_GATEWAY_SETTING,postDto.getRequestRemark());

        restResponse.setData(String.valueOf(ewpGatewayConfig.getId()));
        return restResponse;
    }

    @Override
    public RestfulResponse<String> createFullCompanyInformation(FullCompanyInformationDto postDto) {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        if(null == postDto){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"FullCompanyInformationDto"});
        }
        postDto.setCurrentEnvir(Instance.PRE_PROD.getValue());

        Date createDate = new Date();
        EwalletParticipant ewalletParticipant = new EwalletParticipant();
        ewalletParticipant.setGeaRefId(this.genGeaId(postDto.getCountry()));
        ewalletParticipant.setGeaPrePid(this.genGeaSysPid(Instance.PRE_PROD));
        ewalletParticipant.setGeaPrdPid(this.genGeaSysPid(Instance.PROD));
        ewalletParticipant.setPreStatus(ParticipantStatus.PENDING_FOR_PROCESS);
        ewalletParticipant.setProdStatus(null);
        ewalletParticipant.setCreateBy(userService.getLoginAccountId());
        ewalletParticipant.setCreateDate(createDate);

        EwpCompanyForm ewpCompanyForm = parseEwpCompanyForm(ewalletParticipant, postDto);
        List<EwpCompanyForm> list = new ArrayList<>();
        list.add(ewpCompanyForm);
        ewalletParticipant.setEwpCompanyForms(list);

        ewalletParticipantRepository.save(ewalletParticipant);

        restResponse.setData(String.valueOf(ewalletParticipant.getId()));


        requestApprovalService.save(ewalletParticipant, String.valueOf(ewpCompanyForm.getId()), "", "", "", "", "", Instance.PRE_PROD, ApprovalType.CREATE_PARTICIPANT,postDto.getRequestRemark());
        return restResponse;
    }

    @Override
    public RestfulResponse<FullCompanyInfoDto> getFullCompanyInfomation(String participantId, Instance instance) {
        RestfulResponse<FullCompanyInfoDto> restResponse = new RestfulResponse<>();
        if(StringUtils.isBlank(participantId)){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"participantId"});
        }
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(Long.valueOf(participantId));
        if(null == ewalletParticipant){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ewalletParticipant"});
        }
        List<FullCompanyInformationDto> listDto = new ArrayList<>();
        List<FullCompanyInformationDto> oldListDto = new ArrayList<>();
        ewalletParticipant.getEwpCompanyForms().stream()/*.sorted(Comparator.comparing(EwpCompanyForm::getCreateDate).reversed())*/
                .filter(item -> item.getCurrentEnvir().equals(instance) && (item.getStatus().equals(ParticipantStatus.ACTIVE) || item.getStatus().equals(ParticipantStatus.PENDING_FOR_PROCESS)))
                .forEach(item -> {
                    FullCompanyInformationDto dto = new FullCompanyInformationDto();
                    getDetail(dto, item);
                    if (item.getCurrentEnvir().equals(Instance.PROD)) {
                        dto.setParticipantRecordStatus(ewalletParticipant.getProdStatus().getValue());
                        dto.setParticipantStatus(ewalletParticipant.getProdStatus().getShowView());
                    } else {
                        dto.setParticipantRecordStatus(ewalletParticipant.getPreStatus().getValue());
                        dto.setParticipantStatus(ewalletParticipant.getPreStatus().getShowView());
                    }
                    dto.setFullCompanyStatus(item.getStatus().getValue());
                    dto.setParticipantId(ewalletParticipant.getId().toString());
                    dto.setCurrentEnvir(instance.getValue());
                    if (item.getStatus().equals(ParticipantStatus.ACTIVE)) {
                        oldListDto.add(dto);
                    } else {
                        listDto.add(dto);
                    }
                });
        FullCompanyInfoDto fullCompanyInfoDto = new FullCompanyInfoDto();
        fullCompanyInfoDto.setFullCompanyInformationDto(listDto);
        fullCompanyInfoDto.setOldFullCompanyInformationDto(oldListDto);
        fullCompanyInfoDto.setIsDpyToProd(this.isNeedClone(ewalletParticipant.getGeaRefId())); // Is it allowed clone
        fullCompanyInfoDto.setGeaSysPid(instance==Instance.PRE_PROD?ewalletParticipant.getGeaPrePid():ewalletParticipant.getGeaPrdPid());

        restResponse.setData(fullCompanyInfoDto);
        return restResponse;
    }

    private void getDetail(FullCompanyInformationDto dto,EwpCompanyForm ewpCompanyForm){
        dto.setFullCompanyName(ewpCompanyForm.getFullCompanyName());
        dto.setParticipantName(ewpCompanyForm.getParticipantNameEn());
        dto.setCountry(ewpCompanyForm.getCountry());
        dto.setAddress(ewpCompanyForm.getCompanyAddress());
        dto.setRegistrationNo(ewpCompanyForm.getBdRegistrationNo());
        if(ewpCompanyForm.getBdRegistrationExpiryDate()!=null){
            dto.setRegistrationDate(DateUtils.formatDate(ewpCompanyForm.getBdRegistrationExpiryDate(), DateCode.dateFormatMd));
        }
        dto.setNotificationEmail(ewpCompanyForm.getNotificationEmail());
        List<EwpMaterial> formMaterial = ewpMaterialRepository.findByEwpFormId(ewpCompanyForm.getId());
        if(formMaterial!=null && !formMaterial.isEmpty()) {
            List<MaterialDto> materialDtoList = formMaterial.stream().map(item->getMaterialDto(item)).collect(Collectors.toList());
            materialDtoList.stream().findFirst().ifPresent(item -> {
                dto.setMaterialDto(item);
            });
        }

        List<EwpOwner> ewpOwnerList = ewpOwnerRepository.findByEwpCompanyForm(ewpCompanyForm);
        List<OwnerDetailsDto> ownerDetailsDtoList = new ArrayList<>();
        for(EwpOwner ewpOwner:ewpOwnerList){
            OwnerDetailsDto ownerDetailsDto = new OwnerDetailsDto();
            ownerDetailsDto.setRole(ewpOwner.getRoleName());
            ownerDetailsDto.setName(ewpOwner.getNameEn());
            ownerDetailsDto.setMobileNumber(ewpOwner.getPhoneMobile());
            ownerDetailsDto.setEmail(ewpOwner.getEmail());
            ownerDetailsDto.setDirectLine(ewpOwner.getPhoneOffice());

            List<EwpMaterial> materialList = ewpMaterialRepository.findByEwpOwnerId(ewpOwner.getId());
            if(materialList!=null && !materialList.isEmpty()) {
                List<MaterialDto> materialDtoList = materialList.stream().map(item->getMaterialDto(item)).collect(Collectors.toList());

                StringBuilder materialFilename = new StringBuilder();
                StringBuilder filePath = new StringBuilder();
                for (MaterialDto materialDto : materialDtoList) {
                    materialFilename.append(","+materialDto.getMaterialFilename());
                    filePath.append(","+materialDto.getFilePath());
                }
                String finalMaterialFilename = materialFilename.substring(1,materialFilename.length());
                String finalFilePath = filePath.substring(1,filePath.length());
                materialDtoList.stream().findFirst().ifPresent(item -> {
                    item.setFilePath(finalFilePath);
                    item.setMaterialFilename(finalMaterialFilename);
                    ownerDetailsDto.setMaterialDto(item);
                });
            }
            ownerDetailsDtoList.add(ownerDetailsDto);
        }
        dto.setOwnerDetailsDto(ownerDetailsDtoList);

        List<EwpKeyPerson> ewpKeyPersonList = ewpKeyPersonRepository.findByEwpCompanyForm(ewpCompanyForm);
        List<KeyPersonInformationDto> keyPersonInformationDtoList = new ArrayList<>();
        for(EwpKeyPerson ewpKeyPerson:ewpKeyPersonList){
            KeyPersonInformationDto keyPersonInformationDto = new KeyPersonInformationDto();
            keyPersonInformationDto.setRole(ewpKeyPerson.getRoleName());
            keyPersonInformationDto.setDepartment(ewpKeyPerson.getDepartmentName());
            keyPersonInformationDto.setName(ewpKeyPerson.getPersonNameEn());
            keyPersonInformationDto.setMobileNumber(ewpKeyPerson.getPhoneMobile());
            keyPersonInformationDto.setEmail(ewpKeyPerson.getEmail());
            keyPersonInformationDto.setDirectLine(ewpKeyPerson.getPhoneOffice());
            keyPersonInformationDto.setReceiveSms(ewpKeyPerson.getMobileSms());

            List<EwpMaterial> materialList = ewpMaterialRepository.findByEwpKeyPersonId(ewpKeyPerson.getId());
            if(materialList!=null && !materialList.isEmpty()) {
                List<MaterialDto> materialDtoList = materialList.stream().map(item->getMaterialDto(item)).collect(Collectors.toList());
                materialDtoList.stream().findFirst().ifPresent(item -> {
                    keyPersonInformationDto.setMaterialDto(item);
                });
            }
            keyPersonInformationDtoList.add(keyPersonInformationDto);
        }
        dto.setKeyPersonInformationDto(keyPersonInformationDtoList);

        List<EwpDisputeContact> ewpDisputeContactList = ewpDisputeContactRepository.findByEwpCompanyForm(ewpCompanyForm);
        List<DisputeContactDto> disputeContactDtoList = new ArrayList<>();
        for (EwpDisputeContact ewpDisputeContact : ewpDisputeContactList) {
            DisputeContactDto disputeContactDto = new DisputeContactDto();
            disputeContactDto.setRoleName(ewpDisputeContact.getRoleName());
            disputeContactDto.setDepartmentName(ewpDisputeContact.getDepartmentName());
            disputeContactDto.setNameEn(ewpDisputeContact.getContactName());
            disputeContactDto.setNameNls(ewpDisputeContact.getContactNameNls());
            disputeContactDto.setContactPersonType(ewpDisputeContact.getContactPersonType());
            disputeContactDto.setPhoneOffice(ewpDisputeContact.getPhoneOffice());
            disputeContactDto.setPhoneMobile(ewpDisputeContact.getPhoneMobile());
            disputeContactDto.setMobileSms(ewpDisputeContact.getMobileSms());
            disputeContactDto.setEmail(ewpDisputeContact.getEmail());

            List<EwpMaterial> materialList = ewpMaterialRepository.findByEwpDisputeContactId(ewpDisputeContact.getId());
            if(materialList!=null && !materialList.isEmpty()) {
                List<MaterialDto> materialDtoList = materialList.stream().map(item->getMaterialDto(item)).collect(Collectors.toList());
                materialDtoList.stream().findFirst().ifPresent(item -> {
                    disputeContactDto.setMaterialDto(item);
                });
            }
            disputeContactDtoList.add(disputeContactDto);
        }
        dto.setDisputeContactDto(disputeContactDtoList);
    }

    private MaterialDto getMaterialDto(EwpMaterial material){
        MaterialDto materialDto = new MaterialDto();
        materialDto.setMaterialType(material.getMaterialType());
        materialDto.setMaterialFilename(material.getMaterialFilename());
        materialDto.setFilePath(material.getFilePath());
        materialDto.setMaterialDesc(material.getMaterialDesc());
        return materialDto;
    }

    @Override
    public RestfulResponse<ServiceSettingRequestDto> getServiceSetting(String participantId, Instance instance) {
        if(StringUtils.isBlank(participantId)){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"participantId"});
        }
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(Long.valueOf(participantId));
        if(null == ewalletParticipant){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ewalletParticipant"});
        }
        List<BaseServiceDto> baseServiceList = srvCallerService.getAllService();
        ServiceSettingRequestDto dto = new ServiceSettingRequestDto();
        List<ServiceSettingDto> serviceSettingDtoList = new ArrayList<>();
        List<ServiceSettingDto> oldServiceSettingDtoList = new ArrayList<>();
        ewalletParticipant.getEwpServices().stream()/*.sorted(Comparator.comparing(EwpService::getCreateDate).reversed())*/
                .filter(item -> item.getCurrentEnvir().equals(instance)
                        && (item.getStatus().equals(ParticipantStatus.ACTIVE) || item.getStatus().equals(ParticipantStatus.PENDING_FOR_PROCESS))).forEach(ewpService -> {
            ServiceSettingDto serviceSettingDto = new ServiceSettingDto();
            serviceSettingDto.setMarkup(ewpService.getMarkUp().stripTrailingZeros().toPlainString());
            if (CollectionUtils.isNotEmpty(baseServiceList)) {
                BaseServiceDto bsd = baseServiceList.stream().filter(o -> o.getServiceId().equals(ewpService.getBaseServiceId())).findFirst().orElse(null);
                serviceSettingDto.setServiceId(bsd.getServiceId().toString());
                serviceSettingDto.setName(bsd.getServiceName());
                serviceSettingDto.setServiceCode(bsd.getServiceCode());
            }
            serviceSettingDto.setServiceStatus(ewpService.getServiceStatus());
            serviceSettingDto.setStatus(ewpService.getStatus().toString());
            serviceSettingDto.setEwpServiceId(ewpService.getId().toString());
            List<ServiceFromCurrencyDto> serviceFromCurrencyDtoList = new ArrayList<>();
            for (EwpServiceConfig ewpServiceConfig : ewpService.getEwpServiceConfig()) {
                ServiceFromCurrencyDto serviceFromCurrencyDto = new ServiceFromCurrencyDto();
                serviceFromCurrencyDto.setEnable(ewpServiceConfig.getEnable());
                serviceFromCurrencyDto.setCurrency(ewpServiceConfig.getCurrency());
                List<ServiceToCurrencyDto> serviceToCurrencyDtoList = new ArrayList<>();
                for (EwpServiceCurrConfig ewpServiceCurrConfig : ewpServiceConfig.getEwpServiceCurrConfig()) {
                    ServiceToCurrencyDto serviceToCurrencyDto = new ServiceToCurrencyDto();
                    serviceToCurrencyDto.setAdminFee(ewpServiceCurrConfig.getAdminFee());
                    serviceToCurrencyDto.setChangeNameAdminFee(ewpServiceCurrConfig.getChangeNameAdminFee());
                    serviceToCurrencyDto.setCancelAdminFee(ewpServiceCurrConfig.getCancelAdminFee());
                    serviceToCurrencyDto.setCurrency(ewpServiceCurrConfig.getCurrency());
                    serviceToCurrencyDto.setEnable(ewpServiceCurrConfig.getEnable());
                    serviceToCurrencyDtoList.add(serviceToCurrencyDto);
                }
                serviceFromCurrencyDto.setToCurrencyDto(serviceToCurrencyDtoList);
                serviceFromCurrencyDtoList.add(serviceFromCurrencyDto);
            }
            serviceSettingDto.setFromCurrencyDto(serviceFromCurrencyDtoList);
            if (ewpService.getStatus().equals(ParticipantStatus.PENDING_FOR_PROCESS)) {
                serviceSettingDtoList.add(serviceSettingDto);
            } else {
                oldServiceSettingDtoList.add(serviceSettingDto);
            }
        });
        dto.setServiceSettingDtoList(serviceSettingDtoList);
        dto.setOldServiceSettingDtoList(oldServiceSettingDtoList);
        dto.setParticipantId(participantId);
        return RestfulResponse.ofData(dto);
    }

    @Override
    public RestfulResponse<GatewaySettingDto> getGatewaySetting(String participantId, Instance instance) {
        if(StringUtils.isBlank(participantId)){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"participantId"});
        }
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(Long.valueOf(participantId));
        if(null == ewalletParticipant){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ewalletParticipant"});
        }
        List<ApiGatewaySettingDto> listDto = new ArrayList<>();
        List<ApiGatewaySettingDto> oldListDto = new ArrayList<>();
        ewalletParticipant.getEwpGatewayConfigs().stream()/*.sorted(Comparator.comparing(EwpGatewayConfig::getCreateDate).reversed())*/
                .filter(item -> item.getCurrentEnvir().equals(instance) && (item.getStatus().equals(ParticipantStatus.ACTIVE) || item.getStatus().equals(ParticipantStatus.PENDING_FOR_PROCESS)))
                .forEach(item -> {
                    ApiGatewaySettingDto dto = new ApiGatewaySettingDto();
                    dto.setParticipantId(ewalletParticipant.getId());
                    BeanUtils.copyProperties(item, dto);
                    dto.setStatus(item.getStatus().getValue());
                    dto.setInstance(item.getCurrentEnvir().getValue());
                    if (item.getStatus().equals(ParticipantStatus.ACTIVE)) {
                        oldListDto.add(dto);
                    } else {
                        listDto.add(dto);
                    }
                });

        GatewaySettingDto gatewaySettingDto = new GatewaySettingDto();
        gatewaySettingDto.setApiGatewaySettingDto(listDto);
        gatewaySettingDto.setOldApiGatewaySettingDto(oldListDto);
        // Default value 
        gatewaySettingDto.setDefApiGatewaySettingDto(new ApiGatewaySettingDto());
        List<EwpSysConfig> ewpSysConfigList = ewpSysConfigRepository.findByCategory("EwpGatewayConfig");
        for(EwpSysConfig item : ewpSysConfigList){
        	try {
				org.apache.commons.beanutils.BeanUtils.setProperty(gatewaySettingDto.getDefApiGatewaySettingDto(), item.getCode(), item.getValue());
			} catch (Exception e) {
				logger.error("copy bean property error:{}  {}",item.getCode(), item.getValue(),e);
			}
        }
        // Is it the first time? deployment from pre-production,production A API gateway setting Request
        gatewaySettingDto.setIsFirst(Boolean.FALSE);
        if(instance == Instance.PROD){
        	List<RequestApproval> prodAppList = ewalletParticipant.getRequestApproval().stream().filter(i->i.getCurrentEnvir()==Instance.PROD && i.getApprovalType()==ApprovalType.API_GATEWAY_SETTING).collect(Collectors.toList());
        	if(!prodAppList.stream().anyMatch(i -> i.getStatus()==RequestApprovalStatus.APPROVED)){
        		 gatewaySettingDto.setIsFirst(Boolean.TRUE);
        	}
        }
        return RestfulResponse.ofData(gatewaySettingDto);
    }

    @Override
    public RestfulResponse<ServiceAssignmentDto> getServiceAssignment(String participantId, Instance instance) {
        String EWALLETPA_RTICIPANT="EwalletParticipant";
        if(StringUtils.isBlank(participantId)){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"participantId"});
        }
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(Long.valueOf(participantId));
        if(null == ewalletParticipant){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{EWALLETPA_RTICIPANT});
        }

        List<MoneyPoolDto> moneyPoolDtoList = new ArrayList<>();
        List<MoneyPoolDto> oldMoneyPoolDtoList = new ArrayList<>();
        List<com.tng.portal.common.dto.mp.MoneyPoolDto> mpList = null;
        try {
            mpList = mpCallerService.callGetParticipantMoneyPool(ewalletParticipant.getGeaRefId(),instance).getData();
        } catch (Exception e) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }
        for(com.tng.portal.common.dto.mp.MoneyPoolDto mp : mpList){
        	if(mp.getStatus().equals("PENDING_FOR_PROCESS") || mp.getStatus().equals("REJECTED")){
        		continue;
        	}
            MoneyPoolDto moneyPoolDto = new MoneyPoolDto();
            moneyPoolDto.setGeaMoneyPoolRefId(mp.getGeaMoneyPoolRefId());
            moneyPoolDto.setCurrency(mp.getBaseCurrency());
            moneyPoolDto.setBalance(null == mp.getBalance() ? null : mp.getBalance().toString());
            moneyPoolDto.setAlertLevel(null == mp.getAlertLine() ? null : mp.getAlertLine().toString());
            moneyPoolDto.setStatus(mp.getStatus());
            moneyPoolDto.setServiceSettingDtoList(new ArrayList<>());
            moneyPoolDtoList.add(moneyPoolDto);

            MoneyPoolDto oldMoneyPoolDto = new MoneyPoolDto();
            oldMoneyPoolDto.setGeaMoneyPoolRefId(mp.getGeaMoneyPoolRefId());
            oldMoneyPoolDto.setCurrency(mp.getBaseCurrency());
            oldMoneyPoolDto.setBalance(null == mp.getBalance() ? null : mp.getBalance().toString());
            oldMoneyPoolDto.setAlertLevel(null == mp.getAlertLine() ? null : mp.getAlertLine().toString());
            oldMoneyPoolDto.setStatus(mp.getStatus());
            oldMoneyPoolDto.setServiceSettingDtoList(new ArrayList<>());
            oldMoneyPoolDtoList.add(oldMoneyPoolDto);
        }

        List<BaseServiceDto> baseServiceList = srvCallerService.getAllService();
        for(EwpServiceMoneyPool smp : ewalletParticipant.getEwpServiceMoneyPool()){
            MoneyPoolDto moneyPoolDto = null;
            if(smp.getStatus() == ParticipantStatus.ACTIVE && smp.getCurrentEnvir()==instance){
                for(MoneyPoolDto mpDto : oldMoneyPoolDtoList){
                    if(smp.getGeaMoneyPoolRefId().equals(mpDto.getGeaMoneyPoolRefId())){
                        moneyPoolDto = mpDto;
                        break;
                    }
                }
            }else if(smp.getStatus() == ParticipantStatus.PENDING_FOR_PROCESS && smp.getCurrentEnvir()==instance){
                for(MoneyPoolDto mpDto : moneyPoolDtoList){
                    if(smp.getGeaMoneyPoolRefId().equals(mpDto.getGeaMoneyPoolRefId())){
                        moneyPoolDto = mpDto;
                        break;
                    }
                }
            }else{
                continue;
            }

            EwpService ewpService = smp.getEwpService();
            ServiceSettingDto serviceSettingDto = new ServiceSettingDto();
            serviceSettingDto.setEwpServiceId(ewpService.getId().toString());
            serviceSettingDto.setServiceStatus(ewpService.getServiceStatus());
            if (CollectionUtils.isNotEmpty(baseServiceList)) {
                BaseServiceDto bsd = baseServiceList.stream().filter(o -> o.getServiceId().equals(ewpService.getBaseServiceId())).findFirst().orElse(null);
                if(bsd == null){
                	throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"BaseService"});
                }
                serviceSettingDto.setServiceId(bsd.getServiceId().toString());
                serviceSettingDto.setName(bsd.getServiceName());
                serviceSettingDto.setServiceCode(bsd.getServiceCode());
            }
            if(null != moneyPoolDto && null != moneyPoolDto.getServiceSettingDtoList()){
                moneyPoolDto.getServiceSettingDtoList().add(serviceSettingDto);
            }
        }


        // Return to the result 
        RestfulResponse<ServiceAssignmentDto> restResponse = new RestfulResponse<>();
        ServiceAssignmentDto serviceAssignmentDto = new ServiceAssignmentDto();
        serviceAssignmentDto.setMoneyPoolDtoList(moneyPoolDtoList);
        serviceAssignmentDto.setOldMoneyPoolDtoList(oldMoneyPoolDtoList);
        restResponse.setData(serviceAssignmentDto);
        return restResponse;
    }


    @Override
    public RestfulResponse<String> updateCutOffTime(String participantId, String cutOffTime,Instance instance, String requestRemark) {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        if(StringUtils.isBlank(participantId) || StringUtils.isBlank(cutOffTime)){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"participantId or cutOffTime"});
        }
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(Long.valueOf(participantId));
        if(null == ewalletParticipant){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwalletParticipant"});
        }
        if(CollectionUtils.isEmpty(ewalletParticipant.getEwpServices())){
            throw new BusinessException(SystemMsg.EwpErrorMsg.NO_SERVICE.getErrorCode());
        }
        /*if(hasPending(ewalletParticipant.getGeaRefId(), instance,null)){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }*/
        if(instance==Instance.PROD && !dpyCallerService.callIsDeploy(ewalletParticipant.getGeaRefId(), instance).getData()){
        	throw new BusinessException(SystemMsg.EwpErrorMsg.NO_API_GATEWAY.getErrorCode());
        }
        /*if(mpCallerService.callHasPending(ewalletParticipant.getGeaRefId(), instance,null).getData()){
            throw new BusinessException(SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }*/

        Date date = new Date();
        String accountId = userService.getLoginAccountId();
        StringBuilder ids = new StringBuilder();
        for(EwpService es : ewalletParticipant.getEwpServices()){
        	if(es.getStatus()==ParticipantStatus.ACTIVE && es.getCurrentEnvir()==instance){
	            EwpServiceSettlement ess = new EwpServiceSettlement();
	            ess.setParticipantId(Long.valueOf(participantId));
	            ess.setEwpServiceId(es.getId());
	            ess.setCutoffTime(cutOffTime);
	            ess.setStatus(ParticipantStatus.PENDING_FOR_PROCESS);
	            ess.setCurrentEnvir(es.getCurrentEnvir());
	            ess.setCreateDate(date);
	            ess.setCreateBy(accountId);
	            ewpServiceSettlementRepository.save(ess);
	            ids.append(ess.getId()+",");
        	}
        }
        ids = ids.deleteCharAt(ids.length()-1);
        requestApprovalService.save(ewalletParticipant, "", "", ids.toString(), "", "", "", instance, ApprovalType.SETTLEMENT_CUT_OFF_TIME,requestRemark);
        return restResponse;
    }

    @Override
    public RestfulResponse<String> updateFullCompanyInformation(FullCompanyInformationDto postDto) {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        if(null == postDto || StringUtils.isBlank(postDto.getParticipantId())){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"FullCompanyInformationDto or ParticipantId"});
        }
        Instance instance = Instance.valueOf(postDto.getCurrentEnvir());
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(Long.valueOf(postDto.getParticipantId()));
        if(null == ewalletParticipant){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwalletParticipant"});
        }
        /*if(hasPending(ewalletParticipant.getGeaRefId(), instance, null)){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }*/
        if(instance==Instance.PROD && !dpyCallerService.callIsDeploy(ewalletParticipant.getGeaRefId(), instance).getData()){
        	throw new BusinessException(SystemMsg.EwpErrorMsg.NO_API_GATEWAY.getErrorCode());
        }
        /*if(mpCallerService.callHasPending(ewalletParticipant.getGeaRefId(), instance,null).getData()){
            throw new BusinessException(SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }*/

        List<EwpCompanyForm> companyFormList = ewalletParticipant.getEwpCompanyForms();

        EwpCompanyForm ewpCompanyForm = parseEwpCompanyForm(ewalletParticipant, postDto);
        companyFormList.add(ewpCompanyForm);
        ewalletParticipant.setEwpCompanyForms(companyFormList);
        ewalletParticipantRepository.save(ewalletParticipant);

        requestApprovalService.save(ewalletParticipant, ewpCompanyForm.getId().toString(), "", "", "", "", "", instance, ApprovalType.FULL_COMPANY_INFORMATION,postDto.getRequestRemark());

        restResponse.setData(String.valueOf(ewalletParticipant.getId()));
        return restResponse;
    }

    private EwpCompanyForm parseEwpCompanyForm(EwalletParticipant ewalletParticipant, FullCompanyInformationDto postDto){
        Account loginAccount = userService.getCurrentAccountInfo();
        Date createDate = new Date();
        EwpCompanyForm ewpCompanyForm = new EwpCompanyForm();
        ewpCompanyForm.setCountry(postDto.getCountry());
        ewpCompanyForm.setCompanyAddress(postDto.getAddress());
        ewpCompanyForm.setFullCompanyName(postDto.getFullCompanyName());
        ewpCompanyForm.setCreateBy(loginAccount.getAccountId());
        ewpCompanyForm.setBdRegistrationNo(postDto.getRegistrationNo());
        if(StringUtils.isNotBlank(postDto.getRegistrationDate())){
            ewpCompanyForm.setBdRegistrationExpiryDate(DateUtil.parseDate(DateCode.dateFormatMd, postDto.getRegistrationDate()));
        }
        ewpCompanyForm.setStatus(ParticipantStatus.PENDING_FOR_PROCESS);
        ewpCompanyForm.setCurrentEnvir(Instance.valueOf(postDto.getCurrentEnvir()));
        ewpCompanyForm.setCreateBy(loginAccount.getAccountId());
        ewpCompanyForm.setCreateDate(createDate);
        ewpCompanyForm.setParticipantNameEn(postDto.getParticipantName());
        ewpCompanyForm.setNotificationEmail(postDto.getNotificationEmail());
        ewpCompanyForm.setEwalletParticipant(ewalletParticipant);
        if(postDto.getMaterialDto()!=null) {
            ewpCompanyFormRepository.save(ewpCompanyForm);

            List<EwpMaterial> ewpMaterials = getEwpMaterial(postDto.getMaterialDto(), loginAccount);
            for (EwpMaterial ewpMaterial : ewpMaterials) {
                ewpMaterial.setEwpFormId(ewpCompanyForm.getId());
            }
            ewpMaterialRepository.save(ewpMaterials);
        }

        if(null != postDto.getOwnerDetailsDto()){
            List<EwpOwner> ewpOwnerList = postDto.getOwnerDetailsDto().stream().map(item -> {
                EwpOwner ewpOwner = new EwpOwner();
                ewpOwner.setEmail(item.getEmail());
                ewpOwner.setNameEn(item.getName());
                ewpOwner.setRoleName(item.getRole());
                ewpOwner.setPhoneOffice(item.getDirectLine());
                ewpOwner.setPhoneMobile(item.getMobileNumber());
                ewpOwner.setCreateBy(loginAccount.getAccountId());
                ewpOwner.setCreateDate(createDate);
                ewpOwner.setEwpCompanyForm(ewpCompanyForm);
                if (item.getMaterialDto() != null) {
                    ewpOwnerRepository.save(ewpOwner);

                    List<EwpMaterial> ewpMaterials = getEwpMaterial(item.getMaterialDto(), loginAccount);
                    for (EwpMaterial ewpMaterial : ewpMaterials) {
                        ewpMaterial.setEwpOwnerId(ewpOwner.getId());
                    }
                    ewpMaterialRepository.save(ewpMaterials);
                }
                return ewpOwner;
            }).collect(Collectors.toList());
            ewpCompanyForm.setEwpOwners(ewpOwnerList);
        }
        if(null != postDto.getKeyPersonInformationDto()){
            List<EwpKeyPerson> ewpKeyPersonList = postDto.getKeyPersonInformationDto().stream().map(item -> {
                EwpKeyPerson ewpKeyPerson = new EwpKeyPerson();
                ewpKeyPerson.setCreateBy(loginAccount.getAccountId());
                ewpKeyPerson.setRoleName(item.getRole());
                ewpKeyPerson.setPersonNameEn(item.getName());
                ewpKeyPerson.setPhoneOffice(item.getDirectLine());
                ewpKeyPerson.setDepartmentName(item.getDepartment());
                ewpKeyPerson.setEmail(item.getEmail());
                ewpKeyPerson.setPhoneMobile(item.getMobileNumber());
                ewpKeyPerson.setMobileSms(item.getReceiveSms());
                ewpKeyPerson.setCreateDate(createDate);
                ewpKeyPerson.setEwpCompanyForm(ewpCompanyForm);
                if (item.getMaterialDto() != null) {
                    ewpKeyPersonRepository.save(ewpKeyPerson);
                    List<EwpMaterial> ewpMaterials = getEwpMaterial(item.getMaterialDto(), loginAccount);
                    for (EwpMaterial ewpMaterial : ewpMaterials) {
                        ewpMaterial.setEwpKeyPersonId(ewpKeyPerson.getId());
                    }
                    ewpMaterialRepository.save(ewpMaterials);
                }
                return ewpKeyPerson;
            }).collect(Collectors.toList());
            ewpCompanyForm.setEwpKeyPersons(ewpKeyPersonList);
        }
        if(postDto.getDisputeContactDto()!=null){
            List<EwpDisputeContact> ewpDisputeContactList= postDto.getDisputeContactDto().stream().map(item -> {
                EwpDisputeContact ewpDisputeContact = new EwpDisputeContact();
                ewpDisputeContact.setRoleName(item.getRoleName());
                ewpDisputeContact.setDepartmentName(item.getDepartmentName());
                ewpDisputeContact.setContactName(item.getNameEn());
                ewpDisputeContact.setContactNameNls(item.getNameNls());
                ewpDisputeContact.setContactPersonType(item.getContactPersonType());
                ewpDisputeContact.setPhoneOffice(item.getPhoneOffice());
                ewpDisputeContact.setPhoneMobile(item.getPhoneMobile());
                ewpDisputeContact.setMobileSms(item.getMobileSms());
                ewpDisputeContact.setCreateBy(loginAccount.getAccountId());
                ewpDisputeContact.setCreateDate(createDate);
                ewpDisputeContact.setEmail(item.getEmail());
                ewpDisputeContact.setEwpCompanyForm(ewpCompanyForm);
                if (item.getMaterialDto() != null) {
                    ewpDisputeContactRepository.save(ewpDisputeContact);

                    List<EwpMaterial> ewpMaterials = getEwpMaterial(item.getMaterialDto(), loginAccount);
                    for (EwpMaterial ewpMaterial : ewpMaterials) {
                        ewpMaterial.setEwpDisputeContactId(ewpDisputeContact.getId());
                    }
                    ewpMaterialRepository.save(ewpMaterials);
                }
                return ewpDisputeContact;
            }).collect(Collectors.toList());
            ewpCompanyForm.setEwpDisputeContacts(ewpDisputeContactList);
        }

        return ewpCompanyForm;
    }

    private List<EwpMaterial> getEwpMaterial(MaterialDto materialDto, Account account){
        List<EwpMaterial> ewpMaterials=new ArrayList<>();
        if(materialDto.getFilePath()!=null){
            String[] filePaths =materialDto.getFilePath().split(",");
            String[] fileNames =materialDto.getMaterialFilename().split(",");
            if (filePaths.length>0){
                for(int i=0;i<filePaths.length;i++){
                    EwpMaterial ewpMaterial = new EwpMaterial();
                    ewpMaterial.setMaterialFilename(fileNames[i]);
                    ewpMaterial.setMaterialDesc(materialDto.getMaterialDesc());
                    ewpMaterial.setFilePath(filePaths[i]);
                    ewpMaterial.setCreateBy(account.getAccountId());
                    ewpMaterial.setCreateDate(new Date());
                    int one = filePaths[i].lastIndexOf('.');
                    String type=filePaths[i].substring((one + 1), filePaths[i].length());
                    ewpMaterial.setMaterialType(type.toUpperCase());
                    ewpMaterials.add(ewpMaterial);
                }
            }
        }
        else{
            EwpMaterial ewpMaterial = new EwpMaterial();
            ewpMaterial.setMaterialFilename(materialDto.getMaterialFilename());
            ewpMaterial.setMaterialDesc(materialDto.getMaterialDesc());
            ewpMaterial.setFilePath(materialDto.getFilePath());
            ewpMaterial.setCreateBy(account.getAccountId());
            ewpMaterial.setCreateDate(new Date());
            String filePath=materialDto.getFilePath();
            int one = filePath.lastIndexOf('.');
            String type=filePath.substring((one + 1), filePath.length());
            ewpMaterial.setMaterialType(type.toUpperCase());
            ewpMaterials.add(ewpMaterial);
        }
        return ewpMaterials;

    }

    @Override
    public RestfulResponse<String> updateServiceSetting(ServiceSettingRequestDto postDto) {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        if(null == postDto || CollectionUtils.isEmpty(postDto.getServiceSettingDtoList()) || StringUtils.isBlank(postDto.getParticipantId())){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"Service or Participant Id"});
        }
        Instance instance = Instance.valueOf(postDto.getInstance());
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(Long.valueOf(postDto.getParticipantId()));
        if(null == ewalletParticipant){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwalletParticipant"});
        }
        /*if(hasPending(ewalletParticipant.getGeaRefId(), instance,null)){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }*/
        if(instance==Instance.PROD && !dpyCallerService.callIsDeploy(ewalletParticipant.getGeaRefId(), instance).getData()){
        	throw new BusinessException(SystemMsg.EwpErrorMsg.NO_API_GATEWAY.getErrorCode());
        }
        /*if(mpCallerService.callHasPending(ewalletParticipant.getGeaRefId(), instance,null).getData()){
            throw new BusinessException(SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }*/
        List<BaseServiceDto> list = srvCallerService.getAllService();
        if(CollectionUtils.isEmpty(list)){
            throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }
        List<String> postList = postDto.getServiceSettingDtoList().stream().map(ServiceSettingDto::getServiceId).distinct().collect(Collectors.toList());
        List<String> baseList = list.stream().map(item-> item.getServiceId().toString()).distinct().collect(Collectors.toList());
        if(!baseList.containsAll(postList)){
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }

        List<EwpService> ewpServiceList = parseEwpServiceList(ewalletParticipant, postDto.getServiceSettingDtoList(), instance);
        if(null == ewpServiceList){
            return restResponse;
        }
        ewpServiceRepository.save(ewpServiceList);

        String ids = ewpServiceList.stream().map(item -> String.valueOf(item.getId())).reduce((o1, o2) -> o1.concat(",").concat(o2)).orElse("");
        requestApprovalService.save(ewalletParticipant, "", ids,"", "", "", "", instance, ApprovalType.SERVICE_SETTING,postDto.getRequestRemark());
        return restResponse;
    }
    
    private boolean checkBigDecimalLength(BigDecimal bigDecimal,Integer integerLength,Integer digitLength){
    	if(bigDecimal == null){
    		return true;
    	}
    	//整数长度
    	if(bigDecimal.abs().stripTrailingZeros().toPlainString().split("\\.")[0].length() > integerLength){
    		return false;
    	}
    	//小数长度
    	if(bigDecimal.stripTrailingZeros().toPlainString().indexOf(".") >= 0){
    		if(bigDecimal.stripTrailingZeros().toPlainString().split("\\.")[1].length() > digitLength){
        		return false;
        	}
    	}
    	return true;
    }

    private List<EwpService> parseEwpServiceList(EwalletParticipant ewalletParticipant, List<ServiceSettingDto> serviceSettingDtoList,Instance currentEnvir){
        Date createDate = new Date();
        String accountId = userService.getLoginAccountId();
        return serviceSettingDtoList.stream().map(item -> {
            if (!this.checkBigDecimalLength(new BigDecimal(item.getMarkup()),2,12)) {
                throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode(), new String[]{"Markup"});
            }
            EwpService ewpService = new EwpService();
            ewpService.setCreateDate(createDate);
            ewpService.setCreateBy(accountId);
            ewpService.setMarkUp(new BigDecimal(item.getMarkup()));
            ewpService.setCurrentEnvir(currentEnvir);
            ewpService.setStatus(ParticipantStatus.PENDING_FOR_PROCESS);
            ewpService.setBaseServiceId(Long.valueOf(item.getServiceId()));
            int versionNo = ewalletParticipant.getEwpServices().stream().filter(vo -> null != vo.getChangeVersionNo()).map(EwpService::getChangeVersionNo).max((vo1, vo2) -> vo1.compareTo(vo2)).orElse(0);
            ewpService.setChangeVersionNo(++versionNo);
            ewpService.setServiceStatus(StringUtils.isBlank(item.getServiceStatus()) ? ParticipantStatus.SUSPEND.getValue() : item.getServiceStatus());
            ewpService.setEwalletParticipant(ewalletParticipant);

            List<EwpServiceConfig> serviceConfigList = item.getFromCurrencyDto().stream().map(i1 -> {
                EwpServiceConfig ewpServiceConfig = new EwpServiceConfig();
                ewpServiceConfig.setCreateDate(createDate);
                ewpServiceConfig.setEnable(i1.getEnable());
                ewpServiceConfig.setCurrency(i1.getCurrency());
                ewpServiceConfig.setEwpService(ewpService);

                List<ServiceToCurrencyDto> toCurrencyDtoList = i1.getToCurrencyDto();
                List<EwpServiceCurrConfig> serviceCurrConfigList = toCurrencyDtoList.stream().map(i2 -> {
                	if(!this.checkBigDecimalLength(i2.getAdminFee(), 15, 2) || !this.checkBigDecimalLength(i2.getChangeNameAdminFee(), 15, 2) || !this.checkBigDecimalLength(i2.getCancelAdminFee(), 15, 2)){
                		throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode(), new String[]{"Markup"});
                	}
                    EwpServiceCurrConfig ewpServiceCurrConfig = new EwpServiceCurrConfig();
                    ewpServiceCurrConfig.setCurrency(i2.getCurrency());
                    ewpServiceCurrConfig.setEnable(i2.getEnable());
                    ewpServiceCurrConfig.setAdminFee(i2.getAdminFee());
                    ewpServiceCurrConfig.setChangeNameAdminFee(i2.getChangeNameAdminFee());
                    ewpServiceCurrConfig.setCancelAdminFee(i2.getCancelAdminFee());
                    ewpServiceCurrConfig.setCreateDate(createDate);
                    ewpServiceCurrConfig.setEwpServiceConfig(ewpServiceConfig);
                    return ewpServiceCurrConfig;
                }).collect(Collectors.toList());

                ewpServiceConfig.setEwpServiceCurrConfig(serviceCurrConfigList);
                return ewpServiceConfig;
            }).collect(Collectors.toList());

            ewpService.setEwpServiceConfig(serviceConfigList);
            return ewpService;
        }).collect(Collectors.toList());
    }

    @Override
    public RestfulResponse<String> updateServiceAssignment(ServiceAssignmentDto postDto) {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        if(null == postDto || null == postDto.getMoneyPoolDtoList() || StringUtils.isBlank(postDto.getParticipantId())){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"Service or Participant Id"});
        }
        Instance instance = Instance.valueOf(postDto.getInstance());
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(Long.valueOf(postDto.getParticipantId()));
        if(null == ewalletParticipant){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwalletParticipant"});
        }
        /*if(hasPending(ewalletParticipant.getGeaRefId(), instance,null)){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }*/
        if(instance==Instance.PROD && !dpyCallerService.callIsDeploy(ewalletParticipant.getGeaRefId(), instance).getData()){
        	throw new BusinessException(SystemMsg.EwpErrorMsg.NO_API_GATEWAY.getErrorCode());
        }
        /*if(mpCallerService.callHasPending(ewalletParticipant.getGeaRefId(), instance,null).getData()){
            throw new BusinessException(SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }*/
        for(MoneyPoolDto item : postDto.getMoneyPoolDtoList()){
        	Map<String, Long> tempMap = item.getServiceSettingDtoList().stream().collect(Collectors.groupingBy(ServiceSettingDto::getServiceId,Collectors.counting()));
        	if(tempMap.entrySet().stream().anyMatch(i -> i.getValue()>1L)){
        		throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        	}
        }
        String accountId = userService.getLoginAccountId();
        Date date = new Date();

        StringBuilder  ids = new StringBuilder ();
        postDto.getMoneyPoolDtoList().stream().forEach(item -> {
            List<EwpServiceMoneyPool> ewpMoneyPoolList = new ArrayList<>();
            item.getServiceSettingDtoList().stream().forEach(i1 -> {
                EwpServiceMoneyPool ewpMoneyPool = new EwpServiceMoneyPool();
                ewpMoneyPool.setCreateBy(accountId);
                ewpMoneyPool.setCreateDate(date);
                ewpMoneyPool.setStatus(ParticipantStatus.PENDING_FOR_PROCESS);
                ewpMoneyPool.setCurrentEnvir(instance);
                ewpMoneyPool.setGeaMoneyPoolRefId(item.getGeaMoneyPoolRefId());
                EwpService ewpService = ewpServiceRepository.findOne(Long.valueOf(i1.getEwpServiceId()));
                ewpMoneyPool.setEwpService(ewpService);
                ewpMoneyPool.setEwalletParticipant(ewalletParticipant);
                ewpMoneyPoolList.add(ewpMoneyPool);
            });
            ewpMoneyPoolRepository.save(ewpMoneyPoolList);
            if(ids.length()>0) ids.append(",");
            ids.append(ewpMoneyPoolList.stream().map(i2 -> String.valueOf(i2.getId())).reduce((o1, o2) -> o1.concat(",").concat(o2)).orElse(""));
        });

        requestApprovalService.save(ewalletParticipant, "", "","", ids.toString(), "", "", instance, ApprovalType.SERVICE_POOL_ASSIGNMENT,postDto.getRequestRemark());
        return restResponse;
    }

    @Override
    public List<EwpCompanyForm> updateCompanyInfoStatus(String ewpCompanyFormId, ParticipantStatus status) {
        if (StringUtils.isBlank(ewpCompanyFormId) || status == null) {
            logger.error("StringUtils.isBlank(ewpCompanyFormId) || status == null");
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"ewpCompanyFormId or status"});
        }
        String[] split = ewpCompanyFormId.split(",");
        List<Long> idList = Arrays.asList(split).stream().map(item -> Long.valueOf(item)).collect(Collectors.toList());
        List<EwpCompanyForm> ewpCompanyFormList = ewpCompanyFormRepository.findAll(idList);
        if(ewpCompanyFormList.isEmpty()){
            logger.error("ewpCompanyFormList.isEmpty()");
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ewpCompanyFormList"});
        }
        String accountId = userService.getLoginAccountId();
        Date date = new Date();
        for (EwpCompanyForm ewpCompanyForm : ewpCompanyFormList) {
            ewpCompanyForm.setStatus(status);
            ewpCompanyForm.setUpdateBy(accountId);
            ewpCompanyForm.setUpdateDate(date);
        }
        return ewpCompanyFormRepository.save(ewpCompanyFormList);
    }

    @Override
    public List<EwpService> updateServiceSettingStatus(String ewpServiceId, ParticipantStatus status) {
        if (StringUtils.isBlank(ewpServiceId) || status == null) {
            logger.error("StringUtils.isBlank(ewpServiceId) || status == null");
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"ewpServiceId or status"});
        }
        String[] split = ewpServiceId.split(",");
        List<Long> idList = Arrays.asList(split).stream().map(item -> Long.valueOf(item)).collect(Collectors.toList());
        List<EwpService> ewpServiceList = ewpServiceRepository.findAll(idList);
        if(ewpServiceList.isEmpty()){
            logger.error("ewpServiceList.isEmpty()");
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ewpServiceList"});
        }
        String accountId = userService.getLoginAccountId();
        Date date = new Date();
        for (EwpService ewpService : ewpServiceList) {
            ewpService.setStatus(status);
            ewpService.setUpdateBy(accountId);
            ewpService.setUpdateDate(date);
        }
        return ewpServiceRepository.save(ewpServiceList);
    }

    @Override
    public List<EwpServiceSettlement> updateServiceSettlementStatus(String ids, ParticipantStatus status) {
        if (StringUtils.isBlank(ids) || status == null) {
            logger.error("StringUtils.isBlank(ewpServiceId) || status == null");
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"ids or ParticipantStatus"});
        }
        String[] split = ids.split(",");
        List<Long> idList = Arrays.asList(split).stream().map(item -> Long.valueOf(item)).collect(Collectors.toList());
        List<EwpServiceSettlement> essList = ewpServiceSettlementRepository.findAll(idList);
        if(essList.isEmpty()){
            logger.error("ewpServiceList.isEmpty()");
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwpServiceSettlement List"});
        }
        String accountId = userService.getLoginAccountId();
        Date date = new Date();
        for (EwpServiceSettlement es : essList) {
            es.setStatus(status);
            es.setUpdateBy(accountId);
            es.setUpdateDate(date);
        }
        return ewpServiceSettlementRepository.save(essList);
    }

    @Override
    public List<EwpServiceMoneyPool> updateServiceAssignmentStatus(String ewpMoneyPoolId, ParticipantStatus status) {
        if (StringUtils.isBlank(ewpMoneyPoolId) || status == null) {
            logger.error("StringUtils.isBlank(ewpMoneyPoolId) || status == null");
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"ewpMoneyPoolId or ParticipantStatus"});
        }
        String[] split = ewpMoneyPoolId.split(",");
        List<Long> idList = Arrays.asList(split).stream().map(item -> Long.valueOf(item)).collect(Collectors.toList());
        List<EwpServiceMoneyPool> ewpMoneyPoolList = ewpMoneyPoolRepository.findAll(idList);
        if(ewpMoneyPoolList.isEmpty()){
            logger.error("ewpMoneyPoolList.isEmpty()");
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ewpMoneyPoolList"});
        }
        String accountId = userService.getLoginAccountId();
        Date date = new Date();
        for (EwpServiceMoneyPool ewpMoneyPool : ewpMoneyPoolList) {
            ewpMoneyPool.setStatus(status);
            ewpMoneyPool.setUpdateBy(accountId);
            ewpMoneyPool.setUpdateDate(date);
        }
        return ewpMoneyPoolRepository.save(ewpMoneyPoolList);
    }

    @Override
    public List<EwpGatewayConfig> updateApiGatewaySettingStatus(String ewpGatewayConfigId, ParticipantStatus status) {
        if (StringUtils.isBlank(ewpGatewayConfigId) || status == null) {
            logger.error("StringUtils.isBlank(ewpGatewayConfigId) || status == null");
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"ewpGatewayConfigId or status"});
        }
        String[] split = ewpGatewayConfigId.split(",");
        List<Long> idList = Arrays.asList(split).stream().map(item -> Long.valueOf(item)).collect(Collectors.toList());
        List<EwpGatewayConfig> ewpGatewayConfigList = ewpGatewayConfigRepository.findAll(idList);
        if(ewpGatewayConfigList.isEmpty()){
            logger.error("ewpGatewayConfigList.isEmpty()");
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ewpGatewayConfigList"});
        }
        String accountId = userService.getLoginAccountId();
        Date date = new Date();
        for (EwpGatewayConfig ewpGatewayConfig : ewpGatewayConfigList) {
            ewpGatewayConfig.setStatus(status);
            ewpGatewayConfig.setUpdateBy(accountId);
            ewpGatewayConfig.setUpdateDate(date);
        }
        return ewpGatewayConfigRepository.save(ewpGatewayConfigList);
    }

    @Override
    public RestfulResponse<CutOffTimeDto> getCutOffTime(String participantId, Instance instance) {
        if(StringUtils.isBlank(participantId)){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"participantId"});
        }
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(Long.valueOf(participantId));
        if(null == ewalletParticipant){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwalletParticipant"});
        }
        CutOffTimeDto dto = new CutOffTimeDto();
        dto.setParticipantId(ewalletParticipant.getId().toString());
        List<EwpServiceSettlement>  setlist = ewpServiceSettlementRepository.findByParticipantId(Long.valueOf(participantId));
        for(EwpServiceSettlement item : setlist){
            if(item.getStatus()==ParticipantStatus.ACTIVE && item.getCurrentEnvir()==instance){
                dto.setOldCutOffTime(item.getCutoffTime());
            }
            if(item.getStatus()==ParticipantStatus.PENDING_FOR_PROCESS && item.getCurrentEnvir()==instance){
                dto.setCutOffTime(item.getCutoffTime());
            }
        }
        return RestfulResponse.ofData(dto);
    }

    @Override
    public RestfulResponse<StatusChangeDto> getStatusChange(String participantId, Instance instance) {
        if(StringUtils.isBlank(participantId)){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"participantId"});
        }
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(Long.valueOf(participantId));
        if(null == ewalletParticipant){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwalletParticipant"});
        }
        StatusChangeDto dto = new StatusChangeDto();
        dto.setParticipantId(ewalletParticipant.getId().toString());
        List<EwpStatusChange>  setlist = ewalletParticipant.getEwpStatusChanges();
        for(EwpStatusChange item : setlist){
            if(item.getStatus().equals(ParticipantStatus.PENDING_FOR_PROCESS)){
                BeanUtils.copyProperties(item, dto);
            }
        }
        return RestfulResponse.ofData(dto);
    }


    @Override
    public RestfulResponse<String> deployToProduction(ApiGatewaySettingDto postDto) {
        if(postDto == null){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ApiGatewaySettingDto"});
        }
        Long participantId = postDto.getParticipantId();
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(participantId);
        if(null == ewalletParticipant){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwalletParticipant"});
        }
       /* if(!this.isNeedClone(ewalletParticipant.getGeaRefId())){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }*/
        String accountId = userService.getLoginAccountId();
        Date createDate = new Date();
        //  copy ewp_company_form
        ewalletParticipant.getEwpCompanyForms().stream().filter(item -> item.getStatus().equals(ParticipantStatus.ACTIVE)).forEach(item -> {
            EwpCompanyForm ewpCompanyForm = new EwpCompanyForm();

            BeanUtils.copyProperties(item, ewpCompanyForm, new String[]{"createBy", "createDate", "currentEnvir", "ewalletParticipant", "ewpDisputeContacts", "ewpKeyPersons", "ewpOwners", "id", "status", "updateBy", "updateDate"});
            ewpCompanyForm.setCreateBy(accountId);
            ewpCompanyForm.setCreateDate(createDate);
            ewpCompanyForm.setCurrentEnvir(Instance.PROD);
            ewpCompanyForm.setEwalletParticipant(ewalletParticipant);
            ewpCompanyForm.setStatus(ParticipantStatus.ACTIVE);
            ewpCompanyFormRepository.save(ewpCompanyForm);
            //copy ewp_material
            List<EwpMaterial> ewpMaterialList = ewpMaterialRepository.findByEwpFormId(item.getId()).stream().map(material -> {
                EwpMaterial ewpMaterial = new EwpMaterial();
                BeanUtils.copyProperties(material, ewpMaterial, new String[]{"createBy", "createDate", "ewpDisputeContactId", "ewpFormId", "ewpKeyPersonId", "ewpOwnerId", "id", "updateBy", "updateDate"});
                ewpMaterial.setCreateBy(accountId);
                ewpMaterial.setCreateDate(createDate);
                ewpMaterial.setEwpFormId(ewpCompanyForm.getId());
                return ewpMaterial;
            }).collect(Collectors.toList());
            ewpMaterialRepository.save(ewpMaterialList);
            // copy ewp_dispute_contact
            List<EwpDisputeContact> contactList = item.getEwpDisputeContacts().stream().map(i1 -> {
                EwpDisputeContact ewpDisputeContact = new EwpDisputeContact();
                BeanUtils.copyProperties(i1, ewpDisputeContact, new String[]{"createBy", "createDate", "ewpCompanyForm", "id", "updateBy", "updateDate"});
                ewpDisputeContact.setCreateBy(accountId);
                ewpDisputeContact.setCreateDate(createDate);
                ewpDisputeContact.setEwpCompanyForm(ewpCompanyForm);
                ewpDisputeContactRepository.save(ewpDisputeContact);
                return ewpDisputeContact;
            }).collect(Collectors.toList());
            // copy ewp_key_person
            List<EwpKeyPerson> personList = item.getEwpKeyPersons().stream().map(i1 -> {
                EwpKeyPerson ewpKeyPerson = new EwpKeyPerson();
                BeanUtils.copyProperties(i1, ewpKeyPerson, new String[]{"createBy", "createDate", "ewpCompanyForm", "id", "updateBy", "updateDate"});
                ewpKeyPerson.setCreateBy(accountId);
                ewpKeyPerson.setCreateDate(createDate);
                ewpKeyPerson.setEwpCompanyForm(ewpCompanyForm);
                ewpKeyPersonRepository.save(ewpKeyPerson);
                return ewpKeyPerson;
            }).collect(Collectors.toList());
            // copy ewp_owner
            List<EwpOwner> ownerList = item.getEwpOwners().stream().map(i1 -> {
                EwpOwner ewpOwner = new EwpOwner();
                BeanUtils.copyProperties(i1, ewpOwner, new String[]{"createBy", "createDate", "ewpCompanyForm", "id", "updateBy", "updateDate"});
                ewpOwner.setCreateBy(accountId);
                ewpOwner.setCreateDate(createDate);
                ewpOwner.setEwpCompanyForm(ewpCompanyForm);
                ewpOwnerRepository.save(ewpOwner);
                //copy ewp_material
                List<EwpMaterial> ownMaterialList = ewpMaterialRepository.findByEwpOwnerId(i1.getId()).stream().map(material -> {
                    EwpMaterial ewpMaterial = new EwpMaterial();
                    BeanUtils.copyProperties(material, ewpMaterial, new String[]{"createBy", "createDate", "ewpDisputeContactId", "ewpFormId", "ewpKeyPersonId", "ewpOwnerId", "id", "updateBy", "updateDate"});
                    ewpMaterial.setCreateBy(accountId);
                    ewpMaterial.setCreateDate(createDate);
                    ewpMaterial.setEwpOwnerId(ewpOwner.getId());
                    return ewpMaterial;
                }).collect(Collectors.toList());
                ewpMaterialRepository.save(ownMaterialList);
                return ewpOwner;
            }).collect(Collectors.toList());
            ewpCompanyForm.setEwpKeyPersons(personList);
            ewpCompanyForm.setEwpOwners(ownerList);
            ewpCompanyForm.setEwpDisputeContacts(contactList);
            ewpCompanyFormRepository.save(ewpCompanyForm);

        });

        // save ewp_gateway_config
        EwpGatewayConfig ewpGatewayConfig = new EwpGatewayConfig();
        BeanUtils.copyProperties(postDto, ewpGatewayConfig,new String[]{"createBy","createDate","currentEnvir","id","participantId","status","updateBy","updateDate"});
        ewpGatewayConfig.setEwalletParticipant(ewalletParticipant);
        ewpGatewayConfig.setCreateBy(accountId);
        ewpGatewayConfig.setCreateDate(createDate);
        ewpGatewayConfig.setCurrentEnvir(Instance.PROD);
        ewpGatewayConfig.setStatus(ParticipantStatus.PENDING_FOR_PROCESS);
        ewpGatewayConfigRepository.save(ewpGatewayConfig);

        //copy money pool data
        Map<String,String> mpMapping = null;// old and new mp data  gea id mapping 
        try {
            RestfulResponse<Map<String,String>> callMpRes = mpCallerService.callDeployToProd(ewalletParticipant.getGeaRefId());
            if(!"success".equals(callMpRes.getStatus())){
                throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
            }
            mpMapping = callMpRes.getData();
        } catch (Exception e) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }

        //copy ewp_service
        List<EwpService> ewpServiceList = new ArrayList<>();
        for(EwpService srcService : ewalletParticipant.getEwpServices()){
            if(srcService.getStatus() != ParticipantStatus.ACTIVE){
                continue;
            }
            EwpService tagService = new EwpService();
            BeanUtils.copyProperties(srcService, tagService, new String[]{"createBy","createDate","currentEnvir","ewpMoneyPool","ewpServiceConfig","id","status","updateBy","updateDate"});
            tagService.setCreateBy(accountId);
            tagService.setCreateDate(createDate);
            tagService.setCurrentEnvir(Instance.PROD);
            tagService.setEwpMoneyPool(new ArrayList<>());
            tagService.setEwpServiceConfig(new ArrayList<>());
            tagService.setStatus(ParticipantStatus.ACTIVE);
            ewpServiceRepository.save(tagService);
            ewpServiceList.add(tagService);
            //copy ewp_service_config
            for(EwpServiceConfig srcServiceConfig : srcService.getEwpServiceConfig()){
                EwpServiceConfig tagServiceConfig = new EwpServiceConfig();
                BeanUtils.copyProperties(srcServiceConfig, tagServiceConfig, new String[]{"createDate","ewpService","ewpServiceCurrConfig","id","updateDate"});
                tagServiceConfig.setCreateDate(createDate);
                tagServiceConfig.setEwpService(tagService);
                tagServiceConfig.setEwpServiceCurrConfig(new ArrayList<>());
                ewpServiceConfigRepository.save(tagServiceConfig);
                tagService.getEwpServiceConfig().add(tagServiceConfig);
                //copy ewp_service_curr_config
                for(EwpServiceCurrConfig srcServiceCurrConfig : srcServiceConfig.getEwpServiceCurrConfig()){
                    EwpServiceCurrConfig tagServiceCurrConfig = new EwpServiceCurrConfig();
                    BeanUtils.copyProperties(srcServiceCurrConfig, tagServiceCurrConfig, new String[]{"createDate","ewpServiceConfig","id","updateDate"});
                    tagServiceCurrConfig.setCreateDate(createDate);
                    tagServiceCurrConfig.setEwpServiceConfig(tagServiceConfig);
                    ewpServiceCurrConfigRepository.save(tagServiceCurrConfig);
                    tagServiceConfig.getEwpServiceCurrConfig().add(tagServiceCurrConfig);
                }
            }
            //copy ewp_service_money_pool
            for(EwpServiceMoneyPool srcSmp : srcService.getEwpMoneyPool()){
            	if(srcSmp.getStatus() != ParticipantStatus.ACTIVE){
            		continue;
            	}
                EwpServiceMoneyPool tagSmp = new EwpServiceMoneyPool();
                BeanUtils.copyProperties(srcSmp, tagSmp, new String[]{"createBy","createDate","currentEnvir","ewpService","geaMoneyPoolRefId","id","status","updateBy","updateDate"});
                tagSmp.setCreateBy(accountId);
                tagSmp.setCreateDate(createDate);
                tagSmp.setCurrentEnvir(Instance.PROD);
                tagSmp.setEwpService(tagService);
                tagSmp.setGeaMoneyPoolRefId(mpMapping.get(srcSmp.getGeaMoneyPoolRefId()));
                tagSmp.setStatus(ParticipantStatus.ACTIVE);
                ewpServiceMoneyPoolRepository.save(tagSmp);
            }
            //copy ewp_service_settlement
            List<EwpServiceSettlement> srcServiceSettlementList = ewpServiceSettlementRepository.findByEwpServiceId(srcService.getId());
            for(EwpServiceSettlement srcServiceSettlement : srcServiceSettlementList){
            	if(srcServiceSettlement.getStatus() != ParticipantStatus.ACTIVE){
            		continue;
            	}
                EwpServiceSettlement tagServiceSettlement = new EwpServiceSettlement();
                BeanUtils.copyProperties(srcServiceSettlement, tagServiceSettlement, new String[]{"createBy","createDate","currentEnvir","ewpServiceId","id","status","updateBy","updateDate"});
                tagServiceSettlement.setCreateBy(accountId);
                tagServiceSettlement.setCreateDate(createDate);
                tagServiceSettlement.setCurrentEnvir(Instance.PROD);
                tagServiceSettlement.setEwpServiceId(tagService.getId());
                tagServiceSettlement.setStatus(ParticipantStatus.ACTIVE);
                ewpServiceSettlementRepository.save(tagServiceSettlement);
            }
        }
        ewpServiceRepository.save(ewpServiceList);

        //save request_approval
        requestApprovalService.save(ewalletParticipant, "", "","", "", ewpGatewayConfig.getId().toString(), "", Instance.PROD, ApprovalType.API_GATEWAY_SETTING,postDto.getRequestRemark());
        //ewalletParticipant -> ACTIVE
        ewalletParticipant.setProdStatus(ParticipantStatus.ACTIVE);
        ewalletParticipantRepository.save(ewalletParticipant);

        return RestfulResponse.nullData();
    }

    @Override
    public RestfulResponse<String> uploadMaterial(MultipartFile file) {
        String fileName = "";
        try {
            FileUpload.createDir(materialDir);
            fileName = FileUpload.upload(file, materialDir, null, true);
        } catch (Exception e) {
            throw new BusinessException(SystemMsg.ErrorMsg.UPLOAD_FILE_ERROR.getErrorCode());
        }
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        restResponse.setData(String.valueOf(fileName));
        return restResponse;
    }

    @Override
    public Long updateParticipantStatus(StatusChangeDto statusChangeDto) {
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(Long.valueOf(statusChangeDto.getParticipantId()));
        if(ewalletParticipant == null){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwalletParticipant"});
        }
        Instance instance  = Instance.valueOf(statusChangeDto.getCurrentEnvir());
        /*if(hasPending(ewalletParticipant.getGeaRefId(), instance,null)){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }*/
        if(instance==Instance.PROD && !dpyCallerService.callIsDeploy(ewalletParticipant.getGeaRefId(), instance).getData()){
        	throw new BusinessException(SystemMsg.EwpErrorMsg.NO_API_GATEWAY.getErrorCode());
        }
        /*if(mpCallerService.callHasPending(ewalletParticipant.getGeaRefId(), Instance.PRE_PROD,null).getData()){
            throw new BusinessException(SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }*/
        // Modify it to ACTIVE Full information is required. 
        if(statusChangeDto.getToStatus().equals("ACTIVE")){
        	Map<ApprovalType, Boolean> comMap = this.isCompleteData(ewalletParticipant.getId(), instance, null);
        	String str = comMap.entrySet().stream().filter(item->!item.getValue()).map(item->item.getKey().getValue()).collect(Collectors.joining(","));
        	if(StringUtils.isNotBlank(str)){
        		throw new BusinessException(SystemMsg.EwpErrorMsg.INCOMPLETE_DATA.getErrorCode(),new String[]{str});
        	}
        }

        String accountId = userService.getLoginAccountId();
        EwpStatusChange ewpStatusChange = new EwpStatusChange();
        ewpStatusChange.setEwalletParticipant(ewalletParticipant);
        if (instance == Instance.PRE_PROD) {
            ewpStatusChange.setFromStatus(ewalletParticipant.getPreStatus().getValue());
        } else if (instance == Instance.PROD){
            ewpStatusChange.setFromStatus(ewalletParticipant.getProdStatus().getValue());
        }
        ewpStatusChange.setToStatus(statusChangeDto.getToStatus());
        ewpStatusChange.setChangeReason(statusChangeDto.getChangeReason());
        ewpStatusChange.setCurrentEnvir(instance);
        ewpStatusChange.setStatus(ParticipantStatus.PENDING_FOR_PROCESS);
        ewpStatusChange.setCreateBy(accountId);
        ewpStatusChange.setCreateDate(new Date());
        ewpStatusChangeRepository.save(ewpStatusChange);
        requestApprovalService.save(ewalletParticipant,"","","","","",String.valueOf(ewpStatusChange.getId()),instance, ApprovalType.PARTICIPANT_CHANGE_STATUS,statusChangeDto.getRequestRemark());
        return ewpStatusChange.getId();
    }

    @Override
    public List<EwpStatusChange> updateStatusChangeStatus(String ewpStatusChangeId, ParticipantStatus status) {
        if (StringUtils.isBlank(ewpStatusChangeId) || status == null) {
            logger.error("StringUtils.isBlank(ewpGatewayConfigId) || status == null");
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"ewpStatusChangeId or ParticipantStatus"});
        }
        String[] split = ewpStatusChangeId.split(",");
        List<Long> idList = Arrays.asList(split).stream().map(item -> Long.valueOf(item)).collect(Collectors.toList());
        List<EwpStatusChange> ewpStatusChangeList = ewpStatusChangeRepository.findAll(idList);
        if(ewpStatusChangeList.isEmpty()){
            logger.error("ewpGatewayConfigList.isEmpty()");
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ewpStatusChangeList"});
        }
        String accountId = userService.getLoginAccountId();
        Date date = new Date();
        for (EwpStatusChange ewpStatusChange : ewpStatusChangeList) {
            ewpStatusChange.setStatus(status);
            ewpStatusChange.setUpdateBy(accountId);
            ewpStatusChange.setUpdateDate(date);
        }
        return ewpStatusChangeRepository.save(ewpStatusChangeList);
    }

    @Override
    public RestfulResponse<EwpDetailDto> getDetail(String geaParticipantRefId, Instance instance) {
        if(StringUtils.isBlank(geaParticipantRefId) || instance == null){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"geaParticipantRefId or Instance"});
        }
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findByGeaRefId(geaParticipantRefId);
        if(ewalletParticipant == null){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwalletParticipant"});
        }

        EwpDetailDto detailDto = new EwpDetailDto();
        String participantId = String.valueOf(ewalletParticipant.getId());

        RestfulResponse<FullCompanyInfoDto> fullCompanyInfo = getFullCompanyInfomation(participantId, instance);
        detailDto.setFullCompanyInfoDto(fullCompanyInfo.getData());

        RestfulResponse<ServiceSettingRequestDto> serviceSetting = getServiceSetting(participantId, instance);
        detailDto.setServiceSettingRequestDto(serviceSetting.getData());

        RestfulResponse<ServiceAssignmentDto> serviceAssignment = getServiceAssignment(participantId, instance);
        detailDto.setServiceAssignmentDto(serviceAssignment.getData());

        RestfulResponse<GatewaySettingDto> gatewaySetting = getGatewaySetting(participantId, instance);
        detailDto.setGatewaySettingDto(gatewaySetting.getData());

        RestfulResponse<CutOffTimeDto> cutOffTime = getCutOffTime(participantId, instance);
        detailDto.setCutOffTimeDto(cutOffTime.getData());

        RestfulResponse<StatusChangeDto> statusChange = getStatusChange(participantId, instance);
        detailDto.setStatusChangeDto(statusChange.getData());

        return RestfulResponse.ofData(detailDto);
    }

    @Override
    public List<ParticipantDto> getParticipantByNameOrIdList(String geaParticipantRefId, String participantName, String serviceId, Instance instance) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder hql = new StringBuilder("select e FROM EwalletParticipant e left join e.ewpCompanyForms com left join e.ewpServices es where 1=1");
        hql.append(" and com.status=:status and com.currentEnvir=:instance");
        hql.append(" and es.status=:status and es.currentEnvir=:instance");
        params.put("status", ParticipantStatus.ACTIVE);
        params.put("instance", instance);
        if(StringUtils.isNotBlank(participantName)){
            hql.append(" and (com.fullCompanyName  like '%"+ participantName +"%' or com.participantNameEn like '%"+ participantName +"%')");
        }
        if(StringUtils.isNotBlank(geaParticipantRefId)){
            hql.append(" and e.geaRefId like '%"+ geaParticipantRefId +"%'");
        }
        if(StringUtils.isNotBlank(serviceId)){
            hql.append(" and es.baseServiceId ="+ serviceId);
        }
        hql.append("  GROUP BY e.id");
        List<EwalletParticipant> ewalletParticipantList = jpaUtil.list(hql.toString(), params);

        List<BaseServiceDto> baseServiceList = srvCallerService.getAllService();
        List<ParticipantDto> participantDtoList = new ArrayList<>();
        for(EwalletParticipant ewalletParticipant : ewalletParticipantList){
            ParticipantDto participantDto = new ParticipantDto();
            participantDto.setGeaParticipantRefId(ewalletParticipant.getGeaRefId());
            ewalletParticipant.getEwpCompanyForms().stream().filter(item -> item.getCurrentEnvir().equals(instance) ).findFirst().ifPresent(item -> {
                participantDto.setParticipantName(item.getParticipantNameEn());
            });
            if (CollectionUtils.isNotEmpty(baseServiceList)) {
                String serviceNo = ewalletParticipant.getEwpServices().stream().filter(item -> item.getCurrentEnvir() == instance && item.getStatus() == ParticipantStatus.ACTIVE).map(item -> {
                    return baseServiceList.stream().filter(o -> o.getServiceId().equals(item.getBaseServiceId())).map(BaseServiceDto::getServiceName).findFirst().orElse("");
                }).collect(Collectors.joining(","));
                participantDto.setServiceNo(serviceNo);
            }


            Map<String, List<EwpServiceMoneyPool>> keyMap = ewalletParticipant.getEwpServiceMoneyPool().stream().filter(item -> item.getCurrentEnvir() == instance && item.getStatus() == ParticipantStatus.ACTIVE).collect(Collectors.groupingBy(EwpServiceMoneyPool::getGeaMoneyPoolRefId));
            List<MoneyPoolServiceDto> moneyPoolServices =  keyMap.entrySet().stream().map(item->{
                MoneyPoolServiceDto moneyPoolServiceDto = new MoneyPoolServiceDto();
                moneyPoolServiceDto.setGeaMoneyPoolRefId(item.getKey());
                List<Long> ids = item.getValue().stream().map(i -> i.getEwpService().getBaseServiceId()).distinct().collect(Collectors.toList());
                moneyPoolServiceDto.setServiceIds(ids);
                if (CollectionUtils.isNotEmpty(baseServiceList)) {
                    String names = ids.stream().map(i -> {
                        return baseServiceList.stream().filter(o -> o.getServiceId().equals(i)).map(BaseServiceDto::getServiceName).findFirst().orElse("");
                    }).distinct().collect(Collectors.joining(","));
                    moneyPoolServiceDto.setMoneyPoolServices(names);
                }
                return moneyPoolServiceDto;
            }).collect(Collectors.toList());
            participantDto.setMoneyPoolServices(moneyPoolServices);


            participantDtoList.add(participantDto);
        }

        return participantDtoList;
    }


    @Override
    public Integer checkEdit(String participantId, Instance instance,String type) {
        if(StringUtils.isBlank(participantId) || instance==null){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"participantId or instance"});
        }
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(Long.valueOf(participantId));
        if(ewalletParticipant == null){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwalletParticipant"});
        }
        if(hasPending(ewalletParticipant.getGeaRefId(), instance, null)){
            return SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode();
        }
        if(mpCallerService.callHasPending(ewalletParticipant.getGeaRefId(), instance,null).getData()){
            return SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode();
        }
        if(!"PARTICIPANT_API_GATEWAY_SETTING".equals(type) && instance==Instance.PROD
                && !dpyCallerService.callIsDeploy(ewalletParticipant.getGeaRefId(), instance).getData()){
            return SystemMsg.EwpErrorMsg.NO_API_GATEWAY.getErrorCode();
        }
        return 0;
    }

    /**
     *  Is there a need for approval? 
     * @param requestApprovalId  This time Approval
     */
    @Override
    public Boolean hasPending(String geaParticipantRefId, Instance instance,Long requestApprovalId){
        RequestApproval approval = requestApprovalId==null ? null : requestApprovalRepository.findOne(requestApprovalId);
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findByGeaRefId(geaParticipantRefId);
        if(CollectionUtils.isNotEmpty(ewalletParticipant.getRequestApproval())){
            for(RequestApproval item : ewalletParticipant.getRequestApproval()){
                if(approval!=null && approval.getId().equals(item.getId())){ // Ignore this Approval
                    continue;
                }
                if(item.getCurrentEnvir()==instance && item.getStatus()==RequestApprovalStatus.PENDING_FOR_APPROVAL){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *  Whether it can be or not?  clone
     */
    @Override
    public Boolean isNeedClone(String geaParticipantRefId) {
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findByGeaRefId(geaParticipantRefId);
        if(!ewalletParticipant.getPreStatus().equals(ParticipantStatus.ACTIVE)){
            return false;
        }
        if(this.hasPending(ewalletParticipant.getGeaRefId(), Instance.PRE_PROD,null)){
            return false;
        }
        for(EwpCompanyForm item : ewalletParticipant.getEwpCompanyForms()){
            if(item.getCurrentEnvir() == Instance.PROD){
                return false;
            }
        }
        if(mpCallerService.callHasPending(geaParticipantRefId, Instance.PRE_PROD,null).getData()){   // call mp Interface view mp Whether it can be or not? clone
            return false;
        }
        //pre-prod Has it been deployed to GEA
        boolean flag = dpyCallerService.callIsDeploy(geaParticipantRefId, Instance.PRE_PROD).getData();
        if(!flag){
            return false;
        }
        return true;
    }

    /**
     *  Is it possible to deploy? 
     * @param ewpRequestApprovalId  This time Approval
     * @param mpRequestApprovalId  This time Approval - MP
     */
    @Override
    public Boolean isNeedDeploy(String geaParticipantRefId,Instance instance, Long ewpRequestApprovalId, Long mpRequestApprovalId) {
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findByGeaRefId(geaParticipantRefId);
        //participant  State judgment 
        if(!this.isParticipantActive(ewalletParticipant.getId(), instance, ewpRequestApprovalId)){
            return false;
        }
        // Is the data complete? 
        Map<ApprovalType, Boolean> map = this.isCompleteData(ewalletParticipant.getId(), instance, ewpRequestApprovalId);
        if(map.values().stream().anyMatch(item -> !item)){
            return false;
        }

        if(this.hasPending(ewalletParticipant.getGeaRefId(), instance, ewpRequestApprovalId)){
            return false;
        }
        boolean flag = mpCallerService.callHasPending(ewalletParticipant.getGeaRefId(), instance,mpRequestApprovalId).getData();
        if(flag){   // call mp Interface view mp Whether it can be or not? Deploy
            return false;
        }
        return true;
    }

    /**
     * Participant  Whether or not? active
     * @param requestApprovalId   This time Approval
     */
    private Boolean isParticipantActive(Long participantId,Instance instance,Long requestApprovalId){
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(participantId);
        // If this time approval  Change the status to  Active  Return true
        if(requestApprovalId != null){
            RequestApproval approval = requestApprovalRepository.findOne(requestApprovalId);
            ApprovalType approvalType = approval.getApprovalType();
            if(approvalType==ApprovalType.PARTICIPANT_CHANGE_STATUS){
                EwpStatusChange ewpStatusChange =ewpStatusChangeRepository.findOne(Long.valueOf(approval.getEwpStatusChangeId()));
                if(ewpStatusChange.getToStatus().equals("ACTIVE")){
                    return true;
                }
            }
        }
        // first Deploy ParticipantStatus  Must be active
        if(!dpyCallerService.callIsDeploy(ewalletParticipant.getGeaRefId(), instance).getData()){
            if(instance==Instance.PRE_PROD && !ewalletParticipant.getPreStatus().equals(ParticipantStatus.ACTIVE)){
                return false;
            }
            if(instance==Instance.PROD && !ewalletParticipant.getProdStatus().equals(ParticipantStatus.ACTIVE)){
                return false;
            }
        }
        return true;
    }

    /**
     * participant  Is the data complete? 
     * @param requestApprovalId  This time Approval
     */
    @Override
    public Map<ApprovalType,Boolean> isCompleteData(Long participantId,Instance instance,Long requestApprovalId){
    	Map<ApprovalType,Boolean> map = new HashMap<>();
    	map.put(ApprovalType.FULL_COMPANY_INFORMATION, true);
    	map.put(ApprovalType.SERVICE_SETTING, true);
    	map.put(ApprovalType.SETTLEMENT_CUT_OFF_TIME, true);
    	map.put(ApprovalType.SERVICE_POOL_ASSIGNMENT, true);
    	map.put(ApprovalType.API_GATEWAY_SETTING, true);
    	
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findOne(participantId);
        ApprovalType approvalType = null;
        if(requestApprovalId != null){
            RequestApproval approval = requestApprovalRepository.findOne(requestApprovalId);
            approvalType = approval.getApprovalType();
        }

        if(approvalType!=ApprovalType.FULL_COMPANY_INFORMATION){
            List<EwpCompanyForm> comList = ewalletParticipant.getEwpCompanyForms();
            boolean flag = comList.stream().filter(item->item.getCurrentEnvir().equals(instance)).anyMatch(item -> item.getStatus().equals(ParticipantStatus.ACTIVE));
            if(!flag){
            	map.put(ApprovalType.FULL_COMPANY_INFORMATION, false);
            }
        }
        if(approvalType!=ApprovalType.SERVICE_SETTING){
            List<EwpService> serList = ewalletParticipant.getEwpServices();
            boolean flag = serList.stream().filter(item->item.getCurrentEnvir().equals(instance)).anyMatch(item -> item.getStatus().equals(ParticipantStatus.ACTIVE));
            if(!flag){
            	map.put(ApprovalType.SERVICE_SETTING, false);
            }
        }
        if(approvalType!=ApprovalType.SETTLEMENT_CUT_OFF_TIME){
            List<EwpServiceSettlement> setList = ewpServiceSettlementRepository.findByParticipantId(ewalletParticipant.getId());
            boolean flag = setList.stream().filter(item->item.getCurrentEnvir().equals(instance)).anyMatch(item -> item.getStatus().equals(ParticipantStatus.ACTIVE));
            if(!flag){
            	map.put(ApprovalType.SETTLEMENT_CUT_OFF_TIME, false);
            }
        }
        if(approvalType!=ApprovalType.SERVICE_POOL_ASSIGNMENT){
            List<EwpServiceMoneyPool> mopList = ewalletParticipant.getEwpServiceMoneyPool();
            boolean flag = mopList.stream().filter(item->item.getCurrentEnvir().equals(instance)).anyMatch(item -> item.getStatus().equals(ParticipantStatus.ACTIVE));
            if(!flag){
            	map.put(ApprovalType.SERVICE_POOL_ASSIGNMENT, false);
            }
        }
        if(approvalType!=ApprovalType.API_GATEWAY_SETTING){
            List<EwpGatewayConfig> gatList = ewalletParticipant.getEwpGatewayConfigs();
            boolean flag = gatList.stream().filter(item->item.getCurrentEnvir().equals(instance)).anyMatch(item -> item.getStatus().equals(ParticipantStatus.ACTIVE));
            if(!flag){
            	map.put(ApprovalType.API_GATEWAY_SETTING, false);
            }
        }
        return map;
    }

    @Override
    public EwpDeployment saveDeployment(String geaParticipantRefId, String createBy, Instance instance) {
        if(StringUtils.isBlank(geaParticipantRefId)){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"geaParticipantRefId"});
        }
        EwalletParticipant par = ewalletParticipantRepository.findByGeaRefId(geaParticipantRefId);
        jpaUtil.refresh(par);
        if(null == par){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"EwalletParticipant"});
        }
        RequestApproval requestApproval = null;
        for(RequestApproval item : par.getRequestApproval()){
            if(item.getCurrentEnvir()==instance){
                requestApproval = item;
                break;
            }
        }
        if(null == requestApproval){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"RequestApproval"});
        }
        Date createDate = new Date();
        EwpDeployment ewpDeployment = new EwpDeployment();
        ewpDeployment.setCreateBy(createBy);
        ewpDeployment.setCreateDate(createDate);
        ewpDeployment.setStatus(DeployStatus.PENDING_FOR_DEPLOY);
        ewpDeployment.setRequestApprovalId(requestApproval.getId());
        ewpDeployment.setParticipantId(requestApproval.getEwalletParticipant().getId());
        ewpDeployment.setDeployEnvir(requestApproval.getCurrentEnvir());

        EwpRecordEnvMap ewpRecordEnvMap = new EwpRecordEnvMap();
        ewpRecordEnvMap.setCreateDate(createDate);
        ewpRecordEnvMap.setCreateBy(createBy);
        if(requestApproval.getCurrentEnvir() == Instance.PRE_PROD){
            ewpRecordEnvMap.setPreprodRequestApproval(requestApproval);
        }else {
            ewpRecordEnvMap.setProdRequestApproval(requestApproval);
        }
        ewpRecordEnvMap.setEwpDeployToProd(ewpDeployment);
        ewpRecordEnvMap.setParticipantId(requestApproval.getEwalletParticipant().getId());
        ewpRecordEnvMapRepository.save(ewpRecordEnvMap);

        return ewpDeployment;
    }

    @Override
    public String getParticipantName(String geaParticipantRefId, Instance instance) {
        EwalletParticipant ewalletParticipant = ewalletParticipantRepository.findByGeaRefId(geaParticipantRefId);
        if(Objects.isNull(ewalletParticipant)){
            return "";
        }
        String participantNameAtv = "";
        String participantNamePdg = "";
        String participantNameOth = "";
        for(EwpCompanyForm company : ewalletParticipant.getEwpCompanyForms()){
            if(company.getCurrentEnvir()!=instance){
                continue;
            }
            if(company.getStatus()==ParticipantStatus.ACTIVE){
                participantNameAtv = company.getParticipantNameEn();
            }
            if(company.getStatus()==ParticipantStatus.PENDING_FOR_PROCESS){
                participantNamePdg = company.getParticipantNameEn();
            }
            participantNameOth = company.getParticipantNameEn();
        }
        String participantName = StringUtils.isNotBlank(participantNameAtv)?participantNameAtv:participantNamePdg;
        if(StringUtils.isBlank(participantName)){
        	participantName = participantNameOth;
        }
        return participantName;
    }
    
    @Override
    public Map<String,String> getParticipantName(List<String> geaParticipantRefId, Instance instance) {
    	geaParticipantRefId = geaParticipantRefId.stream().filter(item -> StringUtils.isNotBlank(item)).collect(Collectors.toList());
    	geaParticipantRefId = geaParticipantRefId.stream().distinct().collect(Collectors.toList());
    	Map<String,String> map = new HashMap<>();
    	for(String item : geaParticipantRefId){
    		String name = this.getParticipantName(item, instance);
    		map.put(item, name);
    	}
    	return map;
    }

    @Override
    public void synchDeployment(Long deployRefId,DeployStatus status,Date scheduleDeployDate,String deployVersionNo) {
        EwpDeployment ewpDeployment = ewpDeploymentRepository.findOne(deployRefId);
        ewpDeployment.setStatus(status);
        ewpDeployment.setUpdateDate(new Date());
        ewpDeployment.setScheduleDeployDate(scheduleDeployDate);
        ewpDeployment.setDeployVersionNo(deployVersionNo);
        ewpDeploymentRepository.save(ewpDeployment);
    }

    @Override
    public List<ParticipantDto> getParticipantList(String geaParticipantRefId, Instance instance, ParticipantStatus status) {
        Specifications<EwalletParticipant> where = Specifications.where((root, criteriaQuery, criteriaBuilder) -> root.get("id").isNotNull());
        if(StringUtils.isNotBlank(geaParticipantRefId)){
            where = where.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("geaRefId"), geaParticipantRefId));
        }
        if(Objects.nonNull(instance) && Objects.nonNull(status)){
            if(instance == Instance.PRE_PROD){
                where = where.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("preStatus"), status));
            }else {
                where = where.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("prodStatus"), status));
            }
        }

        List<BaseServiceDto> baseServiceList = srvCallerService.getAllService();
        List<EwalletParticipant> list = ewalletParticipantRepository.findAll(where);
        return list.stream().map(ewalletParticipant -> {
            ParticipantDto participantDto = new ParticipantDto();
            participantDto.setParticipantId(ewalletParticipant.getId().toString());
            participantDto.setGeaParticipantRefId(ewalletParticipant.getGeaRefId());
            ewalletParticipant.getEwpCompanyForms().stream().filter(item -> item.getCurrentEnvir().equals(instance) && item.getStatus().equals(ParticipantStatus.ACTIVE))
                    .max(Comparator.comparing(EwpCompanyForm::getId)).ifPresent(item -> {
                participantDto.setParticipantName(item.getParticipantNameEn());
            });
            if (CollectionUtils.isNotEmpty(baseServiceList)) {
                String serviceNo = ewalletParticipant.getEwpServices().stream().filter(item -> item.getCurrentEnvir() == instance && item.getStatus() == ParticipantStatus.ACTIVE).map(item -> {
                    return baseServiceList.stream().filter(o -> o.getServiceId().equals(item.getBaseServiceId())).map(BaseServiceDto::getServiceName).findFirst().orElse("");
                }).collect(Collectors.joining(","));
                participantDto.setServiceNo(serviceNo);
            }

            return participantDto;
        }).collect(Collectors.toList());
    }
    
    @Override
    public Map<String,ParticipantDto> getParticipantList(List<String> geaParticipantRefId, Instance instance) {
    	List<EwalletParticipant> list = ewalletParticipantRepository.findByGeaRefId(geaParticipantRefId);
    	List<ParticipantDto> result = list.stream().map(ewalletParticipant -> {
            ParticipantDto participantDto = new ParticipantDto();
            participantDto.setParticipantId(ewalletParticipant.getId().toString());
            participantDto.setGeaParticipantRefId(ewalletParticipant.getGeaRefId());
            ewalletParticipant.getEwpCompanyForms().stream().filter(item -> item.getCurrentEnvir().equals(instance) && item.getStatus().equals(ParticipantStatus.ACTIVE))
                    .max(Comparator.comparing(EwpCompanyForm::getId)).ifPresent(item -> {
                participantDto.setParticipantName(item.getParticipantNameEn());
            });
            return participantDto;
        }).collect(Collectors.toList());
    	
    	Map<String,ParticipantDto> map = new HashMap<>();
    	for(ParticipantDto item : result){
    		map.put(item.getGeaParticipantRefId(), item);
    	}
    	
        return map;
    }

    
    @Override
    public Map<String,String> getRelatedServicesByMp(List<String> geaMpRefIds, Instance instance) {
    	geaMpRefIds = geaMpRefIds.stream().filter(item -> StringUtils.isNotBlank(item)).collect(Collectors.toList());
    	geaMpRefIds = geaMpRefIds.stream().distinct().collect(Collectors.toList());
    	
    	List<BaseServiceDto> baseServiceList = srvCallerService.getAllService();
    	Map<String,String> map = new HashMap<>();
    	for(String item : geaMpRefIds){
    		List<EwpServiceMoneyPool> list = ewpServiceMoneyPoolRepository.findByGeaMoneypoolRefId(item,instance);
    		List<Long> serviceIdList = list.stream().filter(i->i.getStatus()==ParticipantStatus.ACTIVE).map(i -> i.getEwpService().getBaseServiceId()).collect(Collectors.toList());
    		List<String> serviceList = serviceIdList.stream().map(i-> {
    			return baseServiceList.stream().filter(i1->i1.getServiceId().equals(i)).findFirst().orElse(null).getServiceName();
    		}).collect(Collectors.toList());;
    		map.put(item, StringUtils.join(serviceList.toArray(), ","));
    	}
    	return map;
    }
}
