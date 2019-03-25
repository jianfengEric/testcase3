package com.test.testcase;

import com.gea.portal.ewp.Application;
import com.gea.portal.ewp.entity.EwpDeployment;
import com.gea.portal.ewp.entity.RequestApproval;
import com.gea.portal.ewp.service.EwalletParticipantService;
import com.gea.portal.ewp.service.EwpBaseService;
import com.gea.portal.ewp.service.RequestApprovalService;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.ewp.EwpDetailDto;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.ParticipantStatus;
import com.tng.portal.common.testUtils.CsvUtils;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;


@SpringBootTest(classes = { Application.class })
public class RemoteControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private EwalletParticipantService ewalletParticipantService;

    @Autowired
    private RequestApprovalService requestApprovalService;

    @Autowired
    private EwpBaseService ewpBaseService;

    @Autowired
    private AccountService accountService;

    @Value("${current.environment}")
    private String currentEnvironment;

    @DataProvider(name="participantData")
    public Iterator<Object[]> participantData() throws IOException {
        String path=".testData."+currentEnvironment+"TestData.";
        return (Iterator<Object[]>)new CsvUtils("participantData.csv",path);
    }

    @DataProvider(name="synchDeploymentData")
    public Iterator<Object[]> synchDeploymentData() throws IOException {
        String path=".testData."+currentEnvironment+"TestData.";
        return (Iterator<Object[]>)new CsvUtils("synchDeploymentData.csv",path);
    }

    @DataProvider(name="ewpMoneypoolData")
    public Iterator<Object[]> ewpMoneypoolData() throws IOException {
        String path=".testData."+currentEnvironment+"TestData.";
        return (Iterator<Object[]>)new CsvUtils("ewpMoneypoolData.csv",path);
    }


    @Test(dataProvider="participantData")
    public void testGetParticipantByNameOrIdList(Map<String, String> data) throws IOException {
        RestfulResponse<List<ParticipantDto>> restfulResponse = new RestfulResponse<>();
        List<ParticipantDto> participantDtoList = ewalletParticipantService.getParticipantByNameOrIdList(data.get("geaParticipantRefId"), data.get("participantName"),data.get("serviceId"), Instance.valueOf(data.get("instance")));
        restfulResponse.setData(participantDtoList);
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="participantData")
    public void testGetDetail(Map<String, String> data) throws IOException {
        RestfulResponse<EwpDetailDto> restfulResponse = ewalletParticipantService.getDetail("HKGA001", Instance.valueOf(data.get("instance")));
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="participantData")
    public void testGetRequestApproval(Map<String, String> data) throws IOException {
        List<RequestApprovalDto> participantDtoList = requestApprovalService.getRequestApproval(data.get("instance"));
        Assert.assertNotNull(participantDtoList, "response");
    }

    @Test(dataProvider="participantData")
    public void testGetParticipantCurrency(Map<String, String> data) throws IOException {
        List<String> participantDtoList = ewpBaseService.getParticipantCurrency("HKGA001", Instance.valueOf(data.get("instance")));
        Assert.assertNotNull(participantDtoList, "response");
    }

    @Test(description = "approval")
    public void testApproval() throws IOException {
        RestfulResponse<RequestApprovalDto> restfulResponse=new RestfulResponse<>();
        RequestApproval requestApproval = requestApprovalService.approvalStatus(Long.valueOf(83),"4028816958dd6d840158e1a181f90099");
        RequestApprovalDto dto = new RequestApprovalDto();
        BeanUtils.copyProperties(requestApproval, dto);
        dto.setParticipantId(requestApproval.getEwalletParticipant().getId().toString());
        dto.setGeaParticipantRefId(requestApproval.getEwalletParticipant().getGeaRefId());
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setId(requestApproval.getId().toString());
        dto.setCreateBy(requestApproval.getCreateBy());
        dto.setCreateUserName(accountService.getAccountName(requestApproval.getCreateBy()));
        dto.setCreateDate(requestApproval.getCreateDate());
        restfulResponse.setData(dto);
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(description = "reject")
    public void testReject() throws IOException {
        RestfulResponse<RequestApprovalDto> restfulResponse=new RestfulResponse<>();
        RequestApproval requestApproval = requestApprovalService.rejectApproval(Long.valueOf(84), ParticipantStatus.REJECTED,"4028816958dd6d840158e1a181f90099");
        RequestApprovalDto dto = new RequestApprovalDto();
        BeanUtils.copyProperties(requestApproval, dto);
        dto.setParticipantId(requestApproval.getEwalletParticipant().getId().toString());
        dto.setGeaParticipantRefId(requestApproval.getEwalletParticipant().getGeaRefId());
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setId(requestApproval.getId().toString());
        dto.setCreateBy(requestApproval.getCreateBy());
        dto.setCreateUserName(accountService.getAccountName(requestApproval.getCreateBy()));
        dto.setCreateDate(requestApproval.getCreateDate());
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="participantData")
    public void testHasPending(Map<String, String> data) throws IOException {
        RestfulResponse<Boolean> restfulResponse = new RestfulResponse<>();
        Boolean hasPendingStatus = ewalletParticipantService.hasPending("HKGA001", Instance.valueOf(data.get("instance")), Long.valueOf(84));
        restfulResponse.setData(hasPendingStatus);
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(description = "get-approval-info")
    public void testGetRequestApprovalInfo() throws IOException {
        RestfulResponse<String> restfulResponse = new RestfulResponse<>();
        restfulResponse.setData(requestApprovalService.getRequestApprovalInfo("84"));
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(description = "get-request-approval")
    public void testGetApproval() throws IOException {
        RestfulResponse<RequestApprovalDto> restfulResponse=new RestfulResponse<>();
        RequestApproval requestApproval = requestApprovalService.getApproval(Long.valueOf(84));
        RequestApprovalDto dto = new RequestApprovalDto();
        dto.setParticipantId(requestApproval.getEwalletParticipant().getId().toString());
        dto.setGeaParticipantRefId(requestApproval.getEwalletParticipant().getGeaRefId());
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setId(requestApproval.getId().toString());
        dto.setCreateBy(requestApproval.getCreateBy());
        dto.setCreateUserName(accountService.getAccountName(requestApproval.getCreateBy()));
        dto.setCreateDate(requestApproval.getCreateDate());
        restfulResponse.setData(dto);
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="participantData")
    public void testIsNeedDeploy(Map<String, String> data) throws IOException {
        Boolean restfulResponse=ewalletParticipantService.isNeedDeploy("HKGA001", Instance.valueOf(data.get("instance")), Long.valueOf(84), Long.valueOf(84));
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="participantData")
    public void testSaveDeployment(Map<String, String> data) throws IOException {
        RestfulResponse<String> restfulResponse=new RestfulResponse<>();
        EwpDeployment ewpDeployment=ewalletParticipantService.saveDeployment("HKGA001", null, Instance.valueOf(data.get("instance")));
        restfulResponse.setData(ewpDeployment.getId().toString());
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="participantData")
    public void testGetParticipantName(Map<String, String> data) throws IOException {
        RestfulResponse<Map<String,String>> restfulResponse=new RestfulResponse<>();
        List<String> geaParticipantRefId=new ArrayList<>();
        geaParticipantRefId.add("HKGA001");
        Map<String,String> name = ewalletParticipantService.getParticipantName(geaParticipantRefId, Instance.valueOf(data.get("instance")));
        restfulResponse.setData(name);
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="synchDeploymentData")
    public void testSynchDeployment(Map<String, String> data) throws IOException {
        RestfulResponse<String> restfulResponse=new RestfulResponse<>();
        ewalletParticipantService.synchDeployment(Long.valueOf(data.get("deployRefId")), DeployStatus.valueOf(data.get("status")),new Date(),data.get("deployVersionNo"));
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="participantData")
    public void testGetParticipantList(Map<String, String> data) throws IOException {
        List<ParticipantDto> list = ewalletParticipantService.getParticipantList("HKGA001", Instance.valueOf(data.get("instance")), ParticipantStatus.ACTIVE);
        Assert.assertNotNull(list, "response");
    }

    @Test(dataProvider="participantData")
    public void testGetParticipantByIds(Map<String, String> data) throws IOException {
        List<String> geaParticipantRefId=new ArrayList<>();
        geaParticipantRefId.add("HKGA001");
        Map<String,ParticipantDto> map = ewalletParticipantService.getParticipantList(geaParticipantRefId, Instance.valueOf(data.get("instance")));
        Assert.assertNotNull(map, "response");
    }

    @Test(dataProvider="ewpMoneypoolData")
    public void testGetRelatedServicesByMp(Map<String, String> data) throws IOException {
        List<String> geaMpRefIds=new ArrayList<>();
        geaMpRefIds.add(data.get("geaMpRefId"));
        Map<String,String> name = ewalletParticipantService.getRelatedServicesByMp(geaMpRefIds,Instance.valueOf(data.get("instance")));
        Assert.assertNotNull(name, "response");
    }

}
