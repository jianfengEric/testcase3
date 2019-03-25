package com.gea.portal.ewp.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.gea.portal.ewp.entity.EwpCompanyForm;
import com.gea.portal.ewp.entity.EwpDeployment;
import com.gea.portal.ewp.entity.EwpGatewayConfig;
import com.gea.portal.ewp.entity.EwpService;
import com.gea.portal.ewp.entity.EwpServiceMoneyPool;
import com.gea.portal.ewp.entity.EwpServiceSettlement;
import com.gea.portal.ewp.entity.EwpStatusChange;
import com.tng.portal.common.dto.ewp.ApiGatewaySettingDto;
import com.tng.portal.common.dto.ewp.CutOffTimeDto;
import com.tng.portal.common.dto.ewp.EwpDetailDto;
import com.tng.portal.common.dto.ewp.FullCompanyInfoDto;
import com.tng.portal.common.dto.ewp.FullCompanyInformationDto;
import com.tng.portal.common.dto.ewp.GatewaySettingDto;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.dto.ewp.ServiceAssignmentDto;
import com.tng.portal.common.dto.ewp.ServiceSettingRequestDto;
import com.tng.portal.common.dto.ewp.StatusChangeDto;
import com.tng.portal.common.enumeration.ApprovalType;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.ParticipantStatus;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 * Created by Owen on 2018/8/24.
 */
public interface EwalletParticipantService {
	
	PageDatas<ParticipantDto> listParticipant(Integer pageNo, Integer pageSize, String geaParticipantRefId, String participantName, String participantStatus,String approvalStatus,Instance instance,String sortBy,Boolean isAscending);
    RestfulResponse<String> updateApiGatewaySetting(ApiGatewaySettingDto postDto);
    RestfulResponse<String> createFullCompanyInformation(FullCompanyInformationDto postDto);
    RestfulResponse<FullCompanyInfoDto> getFullCompanyInfomation(String participantId, Instance instance);
    RestfulResponse<GatewaySettingDto> getGatewaySetting(String participantId, Instance instance);
    RestfulResponse<ServiceSettingRequestDto> getServiceSetting(String participantId, Instance instance);
    RestfulResponse<ServiceAssignmentDto> getServiceAssignment(String participantId, Instance instance);
    RestfulResponse<String> updateCutOffTime(String participantId, String cutOffTime,Instance instance, String requestRemark);
    RestfulResponse<CutOffTimeDto> getCutOffTime(String participantId, Instance instance);
    
    RestfulResponse<StatusChangeDto> getStatusChange(String participantId, Instance instance);
    
    RestfulResponse<String> updateFullCompanyInformation(FullCompanyInformationDto postDto);
    RestfulResponse<String> updateServiceSetting(ServiceSettingRequestDto postDto);
    RestfulResponse<String> updateServiceAssignment(ServiceAssignmentDto postDto);

    List<EwpCompanyForm> updateCompanyInfoStatus(String ids, ParticipantStatus status);
    List<EwpService> updateServiceSettingStatus(String ids, ParticipantStatus status);
    
    List<EwpServiceSettlement> updateServiceSettlementStatus(String ids, ParticipantStatus status);
    
    List<EwpServiceMoneyPool> updateServiceAssignmentStatus(String ids, ParticipantStatus status);
    List<EwpGatewayConfig> updateApiGatewaySettingStatus(String ids, ParticipantStatus status);
    List<EwpStatusChange> updateStatusChangeStatus(String ewpStatusChangeId, ParticipantStatus status);

    RestfulResponse<String> deployToProduction(ApiGatewaySettingDto postDto);
    RestfulResponse<String> uploadMaterial(MultipartFile file);
    public Long updateParticipantStatus(StatusChangeDto statusChangeDto);
    RestfulResponse<EwpDetailDto> getDetail(String geaParticipantRefId, Instance instance);

    List<ParticipantDto> getParticipantByNameOrIdList(String participantId, String participantName, String serviceId ,Instance instance);
    Integer checkEdit(String participantId, Instance instance,String type);

    /**
     *  Is there a need for approval? 
     * @param requestApprovalId  This time Approval
     */
    Boolean hasPending(String geaParticipantRefId, Instance instance,Long requestApprovalId);
    
    /**
     *  Whether it can be or not?  clone
     */
    Boolean isNeedClone(String geaParticipantRefId);
    
    /**
     *  Is it possible to deploy? 
     * @param ewpRequestApprovalId  This time Approval
     * @param mpRequestApprovalId  This time Approval - MP
     */
    Boolean isNeedDeploy(String geaParticipantRefId,Instance instance, Long ewpRequestApprovalId, Long mpRequestApprovalId);
    
    EwpDeployment saveDeployment(String geaParticipantRefId, String createBy, Instance instance);
    
    String getParticipantName(String geaParticipantRefId, Instance instance);	
    
    Map<String,String> getParticipantName(List<String> geaParticipantRefId, Instance instance);

    void synchDeployment(Long deployRefId,DeployStatus status,Date scheduleDeployDate,String deployVersionNo);

    List<ParticipantDto> getParticipantList(String geaParticipantRefId, Instance instance, ParticipantStatus status);
    
    Map<String,ParticipantDto> getParticipantList(List<String> geaParticipantRefId, Instance instance);
    
    public Map<ApprovalType,Boolean> isCompleteData(Long participantId,Instance instance,Long requestApprovalId);
    
    Map<String,String> getRelatedServicesByMp(List<String> geaMpRefIds, Instance instance);
}
