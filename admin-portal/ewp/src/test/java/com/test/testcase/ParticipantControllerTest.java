package com.test.testcase;

import com.gea.portal.ewp.Application;
import com.gea.portal.ewp.service.EwalletParticipantService;
import com.gea.portal.ewp.service.MpCallerService;
import com.tng.portal.ana.authentication.AnaPrincipalAuthenticationToken;
import com.tng.portal.ana.bean.UserDetails;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.dto.ewp.*;
import com.tng.portal.common.dto.mp.MoneyPoolListDto;
import com.tng.portal.common.enumeration.*;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.testUtils.CsvUtils;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.multipart.MultipartFile;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootTest(classes = { Application.class })
public class ParticipantControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private EwalletParticipantService ewalletParticipantService;

    @Autowired
    private MpCallerService mpCallerService;

    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;

    @Value("${current.environment}")
    private String currentEnvironment;

    @Value("${file.size}")
    private String fileSize;

    @Value("${material.dir}")
    private String materialDir;

    @DataProvider(name="pageData")
    public Iterator<Object[]> pageData() throws IOException {
        String path=".testData."+currentEnvironment+"TestData.";
        return (Iterator<Object[]>)new CsvUtils("pageData.csv",path);
    }

    @DataProvider(name="participantData")
    public Iterator<Object[]> participantData() throws IOException {
        String path=".testData."+currentEnvironment+"TestData.";
        return (Iterator<Object[]>)new CsvUtils("participantData.csv",path);
    }

    @DataProvider(name="fullCompanyInformationData")
    public Iterator<Object[]> fullCompanyInformationDto() throws IOException {
        String path=".testData."+currentEnvironment+"TestData.";
        return (Iterator<Object[]>)new CsvUtils("fullCompanyInformationData.csv",path);
    }

    @DataProvider(name="statusChangeDtoData")
    public Iterator<Object[]> statusChangeDtoData() throws IOException {
        String path=".testData."+currentEnvironment+"TestData.";
        return (Iterator<Object[]>)new CsvUtils("statusChangeDtoData.csv",path);
    }

    @DataProvider(name="fileData")
    public Iterator<Object[]> fileData() throws IOException {
        String path=".testData."+currentEnvironment+"TestData.";
        return (Iterator<Object[]>)new CsvUtils("fileData.csv",path);
    }

    @DataProvider(name="serviceSettingDto")
    public Iterator<Object[]> serviceSettingDtoData() throws IOException {
        String path=".testData."+currentEnvironment+"TestData.";
        return (Iterator<Object[]>)new CsvUtils("serviceSettingDto.csv",path);
    }

    @DataProvider(name="serviceAssignmentDto")
    public Iterator<Object[]> serviceAssignmentDtoData() throws IOException {
        String path=".testData."+currentEnvironment+"TestData.";
        return (Iterator<Object[]>)new CsvUtils("serviceAssignmentDto.csv",path);
    }

    @DataProvider(name="updateCutOffTimeData")
    public Iterator<Object[]> updateCutOffTimeData() throws IOException {
        String path=".testData."+currentEnvironment+"TestData.";
        return (Iterator<Object[]>)new CsvUtils("updateCutOffTimeData.csv",path);
    }

    @Test(dataProvider="pageData")
    public void testListParticipant(Map<String, String> data) throws IOException {
        String token=data.get("token");
        UserDetails userDetails = userService.getUserDetailByToken(token);
        if(null!=userDetails){
            AnaPrincipalAuthenticationToken authentication = new AnaPrincipalAuthenticationToken(userDetails,token,"");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        RestfulResponse<PageDatas<ParticipantDto>> restResponse = new RestfulResponse<>();
        Integer pageNo = Integer.valueOf(data.get("pageNo"));
        Integer pageSize = 10;
        String instance=data.get("instance");
        String isAscending=data.get("isAscending");
        PageDatas<ParticipantDto> pageData = ewalletParticipantService.listParticipant(pageNo, pageSize, data.get("geaParticipantRefId"), data.get("participantName"), data.get("participantStatus"),data.get("approvalStatus"), Instance.valueOf(instance),data.get("sortBy"),Boolean.valueOf(isAscending));
        restResponse.setData(pageData);
        restResponse.setSuccessStatus();
        Assert.assertNotNull(restResponse, "response");
    }

    @Test(dataProvider="fullCompanyInformationData")
    public void testCreateFullCompanyInformation(Map<String, String> data) throws IOException {
        String token=data.get("token");
        UserDetails userDetails = userService.getUserDetailByToken(token);
        if(null!=userDetails){
            AnaPrincipalAuthenticationToken authentication = new AnaPrincipalAuthenticationToken(userDetails,token,"");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        FullCompanyInformationDto postDto=new FullCompanyInformationDto();
        postDto=getPostDtoData(postDto,data);
        RestfulResponse<String> restResponse=ewalletParticipantService.createFullCompanyInformation(postDto);
        Assert.assertNotNull(restResponse, "response");
    }

    @Test(dataProvider="pageData")
    public void testGetServiceAssignment(Map<String, String> data) throws IOException {
        String instance=data.get("instance");
        RestfulResponse<ServiceAssignmentDto> restResponse= ewalletParticipantService.getServiceAssignment(data.get("participantId"), Instance.valueOf(instance) == null ? Instance.PRE_PROD : Instance.valueOf(instance));
        Assert.assertNotNull(restResponse, "response");
    }

    @Test(dataProvider="participantData")
    public void testGetParticipantByNameOrIdList(Map<String, String> data) throws IOException {
        RestfulResponse<List<ParticipantDto>> restfulResponse = new RestfulResponse<>();
        List<ParticipantDto> participantDtoList = ewalletParticipantService.getParticipantByNameOrIdList(data.get("geaParticipantRefId"), data.get("participantName"),data.get("serviceId"), Instance.valueOf(data.get("instance")));
        restfulResponse.setData(participantDtoList);
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="pageData")
    public void testCheckEdit(Map<String, String> data) throws IOException {
        Integer restfulResponse = ewalletParticipantService.checkEdit(data.get("participantId"), Instance.valueOf(data.get("instance")),data.get("type"));
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="pageData")
    public void testGetAllMoneyPoolList(Map<String, String> data) throws IOException {
        List<MoneyPoolListDto> list =mpCallerService.callGetAllMoneyPoolList(data.get("geaParticipantRefId"), Instance.valueOf(data.get("instance"))).getData();
        Assert.assertNotNull(list, "response");
    }

    @Test(dataProvider="pageData")
    public void testGetFullCompanyInfomation(Map<String, String> data) throws IOException {
        RestfulResponse<FullCompanyInfoDto> response=ewalletParticipantService.getFullCompanyInfomation(data.get("participantId"), Instance.valueOf(data.get("instance")) == null ? Instance.PRE_PROD : Instance.valueOf(data.get("instance")));
        Assert.assertNotNull(response, "response");
    }

    @Test(dataProvider="pageData")
    public void testGetGatewaySetting(Map<String, String> data) throws IOException {
        RestfulResponse<GatewaySettingDto> response=ewalletParticipantService.getGatewaySetting(data.get("participantId"), Instance.valueOf(data.get("instance"))  == null ? Instance.PRE_PROD : Instance.valueOf(data.get("instance")) );
        Assert.assertNotNull(response, "response");
    }

    @Test(dataProvider="pageData")
    public void testGetServiceSetting(Map<String, String> data) throws IOException {
        RestfulResponse<ServiceSettingRequestDto> response=ewalletParticipantService.getServiceSetting(data.get("participantId"), Instance.valueOf(data.get("instance")) == null ? Instance.PRE_PROD : Instance.valueOf(data.get("instance")));
        Assert.assertNotNull(response, "response");
    }

    @Test(dataProvider="pageData")
    public void testGetCutOffTime(Map<String, String> data) throws IOException {
        RestfulResponse<CutOffTimeDto> response=ewalletParticipantService.getCutOffTime(data.get("participantId"), Instance.valueOf(data.get("instance")) == null ? Instance.PRE_PROD : Instance.valueOf(data.get("instance")));
        Assert.assertNotNull(response, "response");
    }

    @Test(description = "get-Participant-Status-list")
    public void testGetParticipantStatusList() throws IOException {
        RestfulResponse<List<String>> restfulResponse = new RestfulResponse<>();
        List<String> list = new ArrayList<>();
        ParticipantStatus[] s = ParticipantStatus.values();
        for(int i = 0; i< s.length; i++){
            if(!s[i].equals(ParticipantStatus.PENDING_FOR_PROCESS) && !s[i].equals(ParticipantStatus.REJECTED)){
                list.add(s[i].name());
            }
        }
        restfulResponse.setData(list);
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="pageData")
    public void testGetApprovalStatusList(Map<String, String> data) throws IOException {
        RestfulResponse<List<String>> restfulResponse = new RestfulResponse<>();
        List<String> list = Stream.of(RequestApprovalStatus.values()).map(RequestApprovalStatus::getListView).distinct().collect(Collectors.toList());
        if(Instance.valueOf(data.get("instance")) == Instance.PROD){
            list.add(RequestApprovalStatus.ST);
        }
        restfulResponse.setData(list);
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(description = "gen-api-gateway-key")
    public void testGenApiGatewayKey() throws IOException {
        RestfulResponse<String> restfulResponse = new RestfulResponse<>();
        String key = UUID.randomUUID().toString();
        restfulResponse.setData(key);
        Assert.assertNotNull(restfulResponse, "response");
    }


    @Test(dataProvider="pageData")
    public void testIsCompleteData(Map<String, String> data) throws IOException {
        Map<ApprovalType,Boolean> map =  ewalletParticipantService.isCompleteData(Long.valueOf(data.get("participantId")), Instance.valueOf(data.get("instance")),null);
        Assert.assertNotNull(map, "response");
    }

    @Test(dataProvider="statusChangeDtoData")
    public void testUpdateParticipantStatus(Map<String, String> data) throws IOException {
        Map<String,Object> map = new HashMap<>();
        StatusChangeDto statusChangeDto=new StatusChangeDto();
        statusChangeDto=getStatusChangeDto(statusChangeDto,data);
        Long res = ewalletParticipantService.updateParticipantStatus(statusChangeDto);
        map.put("id", res);
        Assert.assertNotNull(map, "response");
    }

    private StatusChangeDto getStatusChangeDto(StatusChangeDto statusChangeDto, Map<String, String> data) {
        statusChangeDto.setParticipantId(data.get("participantId"));
        statusChangeDto.setChangeReason(data.get("changeReason"));
        statusChangeDto.setCurrentEnvir(data.get("currentEnvir"));
        statusChangeDto.setFromStatus(data.get("fromStatus"));
        statusChangeDto.setRequestRemark(data.get("requestRemark"));
        statusChangeDto.setStatus(data.get("status"));
        statusChangeDto.setToStatus(data.get("toStatus"));
        return statusChangeDto;
    }

    @Test(description = "update-api-gateway-setting")
    public void testUpdateApiGatewaySetting() throws IOException {
        ApiGatewaySettingDto postDto=new ApiGatewaySettingDto();
        postDto=getApiGatewaySettingDto(postDto);
        RestfulResponse<String> response=ewalletParticipantService.updateApiGatewaySetting(postDto);;
        Assert.assertNotNull(response, "response");
    }

    @Test(dataProvider="pageData")
    public void testDeployToProduction(Map<String, String> data) throws IOException {
        String token=data.get("token");
        UserDetails userDetails = userService.getUserDetailByToken(token);
        if(null!=userDetails){
            AnaPrincipalAuthenticationToken authentication = new AnaPrincipalAuthenticationToken(userDetails,token,"");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        ApiGatewaySettingDto postDto=new ApiGatewaySettingDto();
        postDto=getApiGatewaySettingDto(postDto);
        RestfulResponse<String> response=ewalletParticipantService.deployToProduction(postDto);
        Assert.assertNotNull(response, "response");
    }

    @Test(dataProvider="fileData")
    public void testUploadMaterial(Map<String, String> data) throws IOException {
        File pdfFile = new File(data.get("pathname"));
        FileInputStream fileInputStream = new FileInputStream(pdfFile);
        MultipartFile file = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

        RestfulResponse<String> restfulResponse=new RestfulResponse<>();
        if (file == null || file.isEmpty()) {
            throw new BusinessException(SystemMsg.ErrorMsg.UPLOAD_FILE_EMPTY.getErrorCode());
        }
        if(file.getSize()>Long.valueOf(fileSize)){
            throw new BusinessException(SystemMsg.ErrorMsg.UPLOAD_FILE_MULTIPART.getErrorCode());
        }
        String name=file.getOriginalFilename();
        int one = name.lastIndexOf('.');
        String type=name.substring((one + 1), name.length());
        FileTypes[] s = FileTypes.values();
        for(int i = 0; i< s.length; i++){
            if(s[i].getValue().equalsIgnoreCase(type)){
                restfulResponse=ewalletParticipantService.uploadMaterial(file);
            }
        }
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="fileData")
    public void testLoadMaterialFile(Map<String, String> data) throws IOException {
        HttpServletResponse response = getHttpServletResponse();
        String filePath=data.get("filePath");
        String fileName=data.get("fileName");
        String code = "/";
        String filePaths = materialDir + code +filePath;
        String fileNames = null;
        try (InputStream is = new FileInputStream(new File(filePaths))){
            int one = filePaths.lastIndexOf('.');
            String type=filePaths.substring((one + 1), filePaths.length());

            String[] strs = filePaths.split("/");
            if(StringUtils.isBlank(fileName)){
                fileNames = strs[strs.length - 1];
            }else{
                fileNames = decodingFileName(fileName, "UTF-8");
            }
            if(type.equalsIgnoreCase(FileTypes.PDF.getValue())){
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline;fileName=" + fileNames);
            }else{
                response.setContentType("image/"+type);
                response.setHeader("Content-Disposition", "attachment;fileName=" + fileNames);
            }

            response.setContentLength(is.available());
            ServletOutputStream out = response.getOutputStream();
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
        } catch (Exception e) {
            logger.error("loadMaterialFile",e);
        }
        RestfulResponse<String> restfulResponse=new RestfulResponse<>();
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="fullCompanyInformationData")
    public void testUpdateFullCompanyInformation(Map<String, String> data) throws IOException {
        FullCompanyInformationDto postDto=new FullCompanyInformationDto();
        postDto=getPostDtoData(postDto,data);
        RestfulResponse<String> restfulResponse=ewalletParticipantService.updateFullCompanyInformation(postDto);
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="serviceSettingDto")
    public void testUpdateServiceSetting(Map<String, String> data) throws IOException {
        ServiceSettingRequestDto serviceSettingDto=new ServiceSettingRequestDto();
        serviceSettingDto=getServiceSettingDto(serviceSettingDto,data);
        RestfulResponse<String> restfulResponse=ewalletParticipantService.updateServiceSetting(serviceSettingDto);
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="serviceAssignmentDto")
    public void testUpdateServiceAssignment(Map<String, String> data) throws IOException {
        ServiceAssignmentDto serviceAssignmentDto=new ServiceAssignmentDto();
        serviceAssignmentDto=getServiceAssignmentDto(serviceAssignmentDto,data);
        RestfulResponse<String> restfulResponse=ewalletParticipantService.updateServiceAssignment(serviceAssignmentDto);;
        Assert.assertNotNull(restfulResponse, "response");
    }

    @Test(dataProvider="updateCutOffTimeData")
    public void testUpdateCutOffTime(Map<String, String> data) throws IOException {
        RestfulResponse<String> restfulResponse=ewalletParticipantService.updateCutOffTime(data.get("participantId"), data.get("cutOffTime"),Instance.valueOf(data.get("instance")),data.get("requestRemark"));
        Assert.assertNotNull(restfulResponse, "response");
    }

    private ServiceAssignmentDto getServiceAssignmentDto(ServiceAssignmentDto serviceAssignmentDto, Map<String, String> data) {
        serviceAssignmentDto.setInstance(data.get("instance"));
        serviceAssignmentDto.setParticipantId(data.get("participantId"));
        serviceAssignmentDto.setRequestRemark(data.get("requestRemark"));
        List<MoneyPoolDto> moneyPoolDtoList=new ArrayList<>();
        MoneyPoolDto moneyPoolDto=new MoneyPoolDto();
        moneyPoolDto.setAlertLevel("11");
        moneyPoolDto.setCurrency("USD");
        moneyPoolDto.setGeaMoneyPoolRefId("CHNA008-USD01");
        moneyPoolDto.setStatus("ACTIVE");
        List<ServiceSettingDto> serviceSettingDtoList=new ArrayList<>();
        ServiceSettingDto settingDto=new ServiceSettingDto();
        settingDto.setEwpServiceId("1");
        settingDto.setName("Transfer-Out Bank Transfer (T + 1)");
        settingDto.setServiceCode("GR_BANK_TRANSFER");
        settingDto.setServiceId("1");
        settingDto.setServiceStatus("ACTIVE");
        serviceSettingDtoList.add(settingDto);
        moneyPoolDto.setServiceSettingDtoList(serviceSettingDtoList);
        moneyPoolDtoList.add(moneyPoolDto);
        serviceAssignmentDto.setMoneyPoolDtoList(moneyPoolDtoList);
        return serviceAssignmentDto;
    }

    private FullCompanyInformationDto getPostDtoData(FullCompanyInformationDto postDto, Map<String, String> data) {
        postDto.setParticipantId(data.get("participantId"));
        postDto.setRegistrationDate(data.get("registrationDate"));
        postDto.setAddress(data.get("address"));
        postDto.setCountry(data.get("country"));
        postDto.setFullCompanyName(data.get("fullCompanyName"));
        postDto.setNotificationEmail(data.get("notificationEmail"));
        postDto.setParticipantName(data.get("participantName"));
        postDto.setRequestRemark(data.get("requestRemark"));
        postDto.setRegistrationNo(data.get("registrationNo"));
        postDto.setCurrentEnvir("PRE_PROD");
        //
        List<DisputeContactDto> disputeContactDtoList=new ArrayList<>();
        DisputeContactDto disputeContactDto=new DisputeContactDto();
        disputeContactDto.setRoleName("testnum5");
        disputeContactDto.setPhoneOffice("13214236554");
        disputeContactDto.setPhoneMobile("13214236554");
        disputeContactDto.setNameEn("testnum5");
        disputeContactDto.setMobileSms(true);
        disputeContactDto.setEmail("testnum5@qq.com");
        disputeContactDto.setDepartmentName("testnum5");
        disputeContactDtoList.add(disputeContactDto);
        postDto.setDisputeContactDto(disputeContactDtoList);
        //
        KeyPersonInformationDto keyPersonInformationDto=new KeyPersonInformationDto();
        keyPersonInformationDto.setDepartment("testnum5");
        keyPersonInformationDto.setDirectLine("13214236554");
        keyPersonInformationDto.setEmail("testnum5@qq.com");
        keyPersonInformationDto.setMobileNumber("13214236554");
        keyPersonInformationDto.setName("testnum5");
        keyPersonInformationDto.setReceiveSms(true);
        keyPersonInformationDto.setRole("testnum5");
        List<KeyPersonInformationDto> keyPersonInformationDtoList=new ArrayList<>();
        keyPersonInformationDtoList.add(keyPersonInformationDto);
        postDto.setKeyPersonInformationDto(keyPersonInformationDtoList);
        //
        MaterialDto materialDto=new MaterialDto();
        materialDto.setFilePath("46160833_e5a7_4b58_8018_3224075f7fcf.png");
        materialDto.setMaterialFilename("u=415293130 - 副本.png");
        postDto.setMaterialDto(materialDto);
        //
        OwnerDetailsDto ownerDetailsDto=new OwnerDetailsDto();
        ownerDetailsDto.setDirectLine("13214236554");
        ownerDetailsDto.setEmail("testnum5@qq.com");
        ownerDetailsDto.setMobileNumber("13214236554");
        MaterialDto materialDto1=new MaterialDto();
        materialDto1.setFilePath("464fcc91_7244_4cf5_9171_070f6068fea7.png");
        materialDto1.setMaterialFilename("u=415293130 - 副本.png");
        ownerDetailsDto.setMaterialDto(materialDto1);
        ownerDetailsDto.setName("testnum5");
        ownerDetailsDto.setRole("testnum5");
        List<OwnerDetailsDto> ownerDetailsDtoList=new ArrayList<>();
        ownerDetailsDtoList.add(ownerDetailsDto);
        postDto.setOwnerDetailsDto(ownerDetailsDtoList);
        return postDto;
    }

    private ServiceSettingRequestDto getServiceSettingDto(ServiceSettingRequestDto serviceSettingDto, Map<String, String> data) {
        serviceSettingDto.setInstance(data.get("instance"));
        serviceSettingDto.setParticipantId(data.get("participantId"));
        serviceSettingDto.setRequestRemark(data.get("hjkhjk"));
        List<ServiceSettingDto> serviceSettingDtoList=new ArrayList<>();
        ServiceSettingDto settingDto=new ServiceSettingDto();
        settingDto.setEwpServiceId("68");
        settingDto.setMarkup("3");
        settingDto.setName("Transfer-Out Bank Transfer (T + 1)");
        settingDto.setServiceCode("GR_BANK_TRANSFER");
        settingDto.setServiceId("1");
        settingDto.setServiceStatus("ACTIVE");
        settingDto.setStatus("ACTIVE");
        List<ServiceFromCurrencyDto> fromCurrencyDto=new ArrayList<>();
        ServiceFromCurrencyDto serviceFromCurrencyDto=new ServiceFromCurrencyDto();
        serviceFromCurrencyDto.setCurrency("USD");
        List<ServiceToCurrencyDto> toCurrencyDto=new ArrayList<>();
        ServiceToCurrencyDto serviceToCurrencyDto=new ServiceToCurrencyDto();
        serviceToCurrencyDto.setAdminFee(new BigDecimal("1"));
        serviceToCurrencyDto.setCancelAdminFee(new BigDecimal("1"));
        serviceToCurrencyDto.setChangeNameAdminFee(new BigDecimal("1"));
        serviceToCurrencyDto.setCurrency("CAD");
        serviceToCurrencyDto.setEnable(true);
        toCurrencyDto.add(serviceToCurrencyDto);
        serviceFromCurrencyDto.setToCurrencyDto(toCurrencyDto);
        fromCurrencyDto.add(serviceFromCurrencyDto);
        settingDto.setFromCurrencyDto(fromCurrencyDto);
        serviceSettingDtoList.add(settingDto);
        serviceSettingDto.setServiceSettingDtoList(serviceSettingDtoList);
        return serviceSettingDto;
    }

    private ApiGatewaySettingDto getApiGatewaySettingDto(ApiGatewaySettingDto postDto) {
        postDto.setApiGatewayUrl("dfgdfgdfgdf");
        postDto.setCreateBy("402881616975b5b3016994113cdb0007");
        postDto.setCreateDate(new Date());
        postDto.setInstance("PRE_PROD");
        postDto.setEndpointsHealthSensitive(true);
        postDto.setEndpointsInfoSensitive(true);
        postDto.setEndpointsRestartEnable(true);
        postDto.setId(Long.valueOf(1));
        postDto.setMgtContextPath("/healthcheck");
        postDto.setMgtHealthRefreshEnable(true);
        postDto.setMgtSecurityEnable(true);
        postDto.setParticipantId(Long.valueOf(1));
        postDto.setRequestRemark("dfgdfg");
        postDto.setSecurityBasicEnable(true);
        postDto.setSecurityBasicPath("/healthcheck/**,/log/**");
        postDto.setSecurityUserName("dfgdfgdfgdff");
        postDto.setSecurityUserPwd("dfgdfgdfgdf");
        postDto.setServerApiKey("65b8ad7d-2458-4bca-ba84-2171888a53f7");
        postDto.setServerConnectionTimeout(Long.valueOf(60000));
        postDto.setServerHealcheckEndpoint("dfgdfgdfgdf");
        postDto.setServerHealthThreshold("5368709120");
        postDto.setServerLogEnableEncrypt(true);
        postDto.setServerLogEncryptionKey("dfgdfgdfgdf");
        postDto.setServerLogMaxHistory(Long.valueOf(7));
        postDto.setServerLogPath("/opt/api_gateway/logs");
        postDto.setServerLogPattern("%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %replace(%caller{1}){'\t|Caller.{1}0|\r\n', ''} %clr(:){faint} %m%n");
        postDto.setServerLogTotalSize(Long.valueOf(92160));
        postDto.setServerMessageEndpoint("dfgdfgdfgdf");
        postDto.setServerPort("8092");
        postDto.setServerRequestTimeout(Long.valueOf(60000));
        postDto.setServerRetryTimes(Long.valueOf(3));
        postDto.setServerRoutesMeta("https://geaaux-pp-8093.globalewalletalliance.com");
        postDto.setServerRoutesMth("https://geamain-pp-2046.globalewalletalliance.com");
        postDto.setServerRoutesSr("https://geamain-pp-8098.globalewalletalliance.com");
        postDto.setServerSecretKey("1654b1ad-50b8-4511-b791-006b8ffc4220");
        postDto.setServerZipCompressionLevel(Long.valueOf(5));
        postDto.setServerZipKey("dfgdfgdfgdf");
        postDto.setStatus("ACTIVE");
        postDto.setUpdateDate(new Date());
        return postDto;
    }

    private static String decodingFileName(String fileName,String encoding){
        try {
            return new String(fileName.getBytes(encoding), "iso8859-1");
        }catch (UnsupportedEncodingException e) {
            return fileName;
        }}

    public HttpServletResponse getHttpServletResponse() {
        return new HttpServletResponse() {
            @Override
            public void addCookie(Cookie cookie) {

            }

            @Override
            public boolean containsHeader(String s) {
                return false;
            }

            @Override
            public String encodeURL(String s) {
                return null;
            }

            @Override
            public String encodeRedirectURL(String s) {
                return null;
            }

            @Override
            public String encodeUrl(String s) {
                return null;
            }

            @Override
            public String encodeRedirectUrl(String s) {
                return null;
            }

            @Override
            public void sendError(int i, String s) throws IOException {

            }

            @Override
            public void sendError(int i) throws IOException {

            }

            @Override
            public void sendRedirect(String s) throws IOException {

            }

            @Override
            public void setDateHeader(String s, long l) {

            }

            @Override
            public void addDateHeader(String s, long l) {

            }

            @Override
            public void setHeader(String s, String s1) {

            }

            @Override
            public void addHeader(String s, String s1) {

            }

            @Override
            public void setIntHeader(String s, int i) {

            }

            @Override
            public void addIntHeader(String s, int i) {

            }

            @Override
            public void setStatus(int i) {

            }

            @Override
            public void setStatus(int i, String s) {

            }

            @Override
            public int getStatus() {
                return 0;
            }

            @Override
            public String getHeader(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaders(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaderNames() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return null;
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return null;
            }

            @Override
            public void setCharacterEncoding(String s) {

            }

            @Override
            public void setContentLength(int i) {

            }

            @Override
            public void setContentLengthLong(long l) {

            }

            @Override
            public void setContentType(String s) {

            }

            @Override
            public void setBufferSize(int i) {

            }

            @Override
            public int getBufferSize() {
                return 0;
            }

            @Override
            public void flushBuffer() throws IOException {

            }

            @Override
            public void resetBuffer() {

            }

            @Override
            public boolean isCommitted() {
                return false;
            }

            @Override
            public void reset() {

            }

            @Override
            public void setLocale(Locale locale) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }
        };
    }
}
