package com.gea.portal.tre.service.impl;

import com.gea.portal.tre.dto.CsvDto;
import com.gea.portal.tre.dto.CurrencyCodes;
import com.gea.portal.tre.dto.GeaBaseRate;
import com.gea.portal.tre.dto.RateDetails;
import com.gea.portal.tre.entity.ExchangeRateDeployment;
import com.gea.portal.tre.entity.ExchangeRateFile;
import com.gea.portal.tre.entity.ExchangeRateRecord;
import com.gea.portal.tre.entity.RequestApproval;
import com.gea.portal.tre.repository.ExchangeRateDeploymentRepository;
import com.gea.portal.tre.repository.ExchangeRateFileRepository;
import com.gea.portal.tre.repository.ExchangeRateRecordRepository;
import com.gea.portal.tre.repository.RequestApprovalRepository;
import com.gea.portal.tre.service.RequestApprovalService;
import com.gea.portal.tre.service.TreasuryService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tng.portal.ana.bean.Account;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.constant.DateCode;
import com.tng.portal.common.dto.tre.CorrelationDataDto;
import com.tng.portal.common.dto.tre.ExchangeRateFileDto;
import com.tng.portal.common.dto.tre.ExchangeRateListDto;
import com.tng.portal.common.dto.tre.ExchangeRateRecordDto;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.RateFileStatus;
import com.tng.portal.common.enumeration.RequestApprovalStatus;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.CSVFileUtil;
import com.tng.portal.common.util.FileUpload;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Dell on 2018/9/14.
 */
@Service
@Transactional
public class TreasuryServiceImpl implements TreasuryService {
    @Value("${file.dir}")
    private String fileDir;
    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ExchangeRateFileRepository exchangeRateFileRepository;
    @Autowired
    private ExchangeRateRecordRepository exchangeRateRecordRepository;
    @Autowired
    private RequestApprovalRepository requestApprovalRepository;
    @Autowired
    private RequestApprovalService requestApprovalService;
    @Autowired
    private ExchangeRateDeploymentRepository exchangeRateDeploymentRepository;
    @Autowired
    private HttpClientUtils httpClientUtils;

    @Value("${baseRate.prod.url}")
    private String prodBaseRateUrl;
    @Value("${baseRate.pre.url}")
    private String preBaseRateUrl;
    @Value("${currencyCodes.prod.url}")
    private String prodCurrencyCodesUrl;
    @Value("${currencyCodes.pre.url}")
    private String preCurrencyCodesUrl;
    @Value("${baseRate.api.key}")
    private String baseRateApiKey;

    private String pattern ="-[0-9]+(.[0-9]+)?|[0-9]+(.[0-9]+)?";

    private static final Logger logger = LoggerFactory.getLogger(TreasuryServiceImpl.class);

    private DateFormat bf = new SimpleDateFormat(DateCode.dateFormatSs);
    private String missingPairsKey = "MISSING_PAIRS";//Lack of comparative databases
    private String duplicatePairsKey = "DUPLICATE_PAIRS";
    private String unfilledItemsKey = "UNFILLED_ITEMS";
    private String missingHeadKey = "MISSING_HEAD_TITLE";
    private String wrongCurrencyKey = "WRONG_CURRENCY";
    private String digitLimitKey = "DIGIT_LIMIT";
    private String titles = "From,To,BaseRate";

    @Override
    public RestfulResponse<ExchangeRateListDto> getPageData(Integer pageNo, Integer pageSize, String status, String sortBy,Instance instance, Boolean isAscending) throws IllegalAccessException, InvocationTargetException {
        List<ExchangeRateDeployment> dePloyList = exchangeRateDeploymentRepository.findAllExchangeRateDeployment(instance);
        ExchangeRateListDto dto = new ExchangeRateListDto();
        PageDatas<ExchangeRateRecord> pageDatas = new PageDatas<>(pageNo,pageSize);
        Sort sort = new Sort("currFrom","currTo");
        if(StringUtils.isNotEmpty(sortBy)){
            sort = pageDatas.pageSort(sortBy, isAscending);
        }
        if(CollectionUtils.isNotEmpty(dePloyList)){
            ExchangeRateDeployment deploy = dePloyList.get(0);
            RequestApproval approval = requestApprovalRepository.findOne(deploy.getRequestApprovalId());
            ExchangeRateFile exchangeRateFile = exchangeRateFileRepository.findOne(Long.parseLong(approval.getExchRateFileId()));
            dto.setFileName(exchangeRateFile.getFileName());
            DateFormat dateFormat = new SimpleDateFormat(DateCode.dateFormatSs);
            dto.setLastUploadDateTime(dateFormat.format(exchangeRateFile.getCreateDate()));
            AnaAccount account = accountService.getAccount(exchangeRateFile.getCreateBy());
            dto.setLastUploadUserId(account.getFullName());
            long fileId = Long.parseLong(approval.getExchRateFileId());
            Page<ExchangeRateRecord> page =  exchangeRateRecordRepository.findByExchRateFileId(fileId,pageDatas.pageRequest(sort));
            List<ExchangeRateRecordDto> list = new ArrayList<>();
            for(ExchangeRateRecord record : page){
                ExchangeRateRecordDto recordDto = new ExchangeRateRecordDto();
                BeanUtils.copyProperties(recordDto, record);
                AnaAccount accountCreate = accountService.getAccount(record.getCreateBy());
                recordDto.setCreateBy(accountCreate.getFullName());
                list.add(recordDto);
            }
            pageDatas.initPageParam(page);
            pageDatas.setList(page.getContent());
            dto.setData(list);
            dto.setPageNo(pageDatas.getPageNo());
            dto.setPageSize(pageDatas.getPageSize());
            dto.setTotal(pageDatas.getTotal());
            dto.setTotalPages(pageDatas.getTotalPages());
            return RestfulResponse.ofData(dto);
        }
        return RestfulResponse.ofData(dto);
    }
    
    @Override
    public ExchangeRateListDto getListData(Instance instance) throws IllegalAccessException, InvocationTargetException {
        List<RequestApproval> requestApproval = requestApprovalRepository.findAllApprovalRecord(instance);
        ExchangeRateListDto dto = new ExchangeRateListDto();
        if(CollectionUtils.isNotEmpty(requestApproval)){
            for(RequestApproval approval : requestApproval){
                List<ExchangeRateDeployment> dePloyList = exchangeRateDeploymentRepository.findAllExchangeRateDeployment(instance);
                for (ExchangeRateDeployment deploy : dePloyList){
                    if(deploy.getStatus().equals(DeployStatus.DEPLOYED.getValue())){
                        ExchangeRateFile exchangeRateFile = exchangeRateFileRepository.findOne(Long.parseLong(approval.getExchRateFileId()));
                        dto.setFileName(exchangeRateFile.getFileName());
                        DateFormat dateFormat = new SimpleDateFormat(DateCode.dateFormatSs);
                        dto.setLastUploadDateTime(dateFormat.format(exchangeRateFile.getCreateDate()));
                        AnaAccount account = accountService.getAccount(exchangeRateFile.getCreateBy());
                        dto.setLastUploadUserId(account.getFullName());
                        long fileId = Long.parseLong(approval.getExchRateFileId());
                        List<ExchangeRateRecord> page =  exchangeRateRecordRepository.findExchangeRateRecord(fileId);
                        List<ExchangeRateRecordDto> list = new ArrayList<>();
                        for(ExchangeRateRecord record : page){
                            ExchangeRateRecordDto recordDto = new ExchangeRateRecordDto();
                            BeanUtils.copyProperties(recordDto, record);
                            AnaAccount accountCreate = accountService.getAccount(record.getCreateBy());
                            recordDto.setCreateBy(accountCreate.getFullName());
                            list.add(recordDto);
                        }
                        dto.setData(list);
                        return dto;
                    }
                }
            }
        }
        return dto;
    }

    private Map<String,CorrelationDataDto> correlationData(List<CsvDto> newExchangeFileList,ExchangeRateFileDto exchangeRateFileDto,Instance instance) throws Exception{
        Map<String,CorrelationDataDto> map = new HashMap<>();
        for(CsvDto dto : newExchangeFileList){
            if(null != dto.getCurrFrom() && null !=dto.getCurrTo() && null != dto.getOfferRate()){
                CorrelationDataDto rate = new CorrelationDataDto();
                rate.setCurrFrom(dto.getCurrFrom());
                rate.setCurrTo(dto.getCurrTo());
                BigDecimal newRete = new BigDecimal(dto.getOfferRate()).setScale(12, BigDecimal.ROUND_DOWN);
                rate.setNewExchangeRate(newRete.toString());
                map.put(dto.getCurrFrom()+dto.getCurrTo(),rate);
            }
        }
        List<ExchangeRateRecord> exchangeFileList = new ArrayList<>();
        ExchangeRateFile exchangeRateFile = null;
        List<ExchangeRateDeployment> dePloyList = exchangeRateDeploymentRepository.findAllExchangeRateDeployment(instance);
        for (ExchangeRateDeployment deploy : dePloyList) {
            if (deploy.getStatus().equals(DeployStatus.DEPLOYED.getValue())) {
                RequestApproval approval = requestApprovalRepository.findOne(deploy.getRequestApprovalId());
                exchangeRateFile = exchangeRateFileRepository.findOne(Long.parseLong(approval.getExchRateFileId()));
                exchangeRateFileDto.setCurrentFileName(exchangeRateFile.getFileName());
                exchangeRateFileDto.setLastDateTime(bf.format(exchangeRateFile.getCreateDate()));
                AnaAccount accountNew = accountService.getAccount(exchangeRateFile.getCreateBy());
                exchangeRateFileDto.setLastUserId(accountNew.getFullName());
                exchangeRateFileDto.setExchangeRateFileId(exchangeRateFile.getId().toString());
                exchangeFileList.addAll(exchangeRateRecordRepository.findExchangeRateRecord(exchangeRateFile.getId()));
                break;
            }
        }
        if(CollectionUtils.isEmpty(exchangeFileList)){
            String url = prodBaseRateUrl;
            if(instance.equals(Instance.PRE_PROD)){
                url = preBaseRateUrl;
            }
            HashMap<String,String>  prams = new HashMap<>();
            prams.put("api_key",baseRateApiKey);
            String json =  httpClientUtils.httpGet(url,String.class,prams);
            GeaBaseRate baseRate = new Gson().fromJson(json, new TypeToken<GeaBaseRate>() {}.getType());
            List<RateDetails> rateList = baseRate.getParticipantEntry().getDeployDetail().get(0).getDetails();
            for(RateDetails record : rateList){
                if(map.containsKey(record.getCurrencyFrom()+record.getCurrencyTo())){
                    BigDecimal currencyExchangeRate = new BigDecimal(record.getRate()).setScale(12, BigDecimal.ROUND_DOWN);
                    map.get(record.getCurrencyFrom()+record.getCurrencyTo()).setCurrencyExchangeRate(currencyExchangeRate.toString());
                    if(null != record.getRate() && null != map.get(record.getCurrencyFrom()+record.getCurrencyTo()).getNewExchangeRate()){
                        BigDecimal newExchangeRete = (BigDecimal.valueOf(Double.parseDouble(map.get(record.getCurrencyFrom()+record.getCurrencyTo()).getNewExchangeRate())));
                        BigDecimal oldExchangeRete = (BigDecimal.valueOf(Double.parseDouble(record.getRate())));
                        BigDecimal divisor = (newExchangeRete.subtract(oldExchangeRete));
                        BigDecimal percentageChange = divisor.divide(oldExchangeRete, 14, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(12);
                        map.get(record.getCurrencyFrom()+record.getCurrencyTo()).setPercentageChange(percentageChange.toPlainString()+"%");
                    }
                } else {
                    CorrelationDataDto rate = new CorrelationDataDto();
                    rate.setCurrFrom(record.getCurrencyFrom());
                    rate.setCurrTo(record.getCurrencyTo());
                    BigDecimal currencyExchangeRate = new BigDecimal(record.getRate()).setScale(12, BigDecimal.ROUND_DOWN);
                    rate.setCurrencyExchangeRate(currencyExchangeRate.toString());
                    rate.setNewExchangeRate(null);
                    map.put(record.getCurrencyFrom()+record.getCurrencyTo(),rate);
                }
            }
        } else {
            for(ExchangeRateRecord record : exchangeFileList){
                if(map.containsKey(record.getCurrFrom()+record.getCurrTo())){
                    map.get(record.getCurrFrom()+record.getCurrTo()).setCurrencyExchangeRate(record.getOfferRate().toString());
                    if(null != record.getOfferRate() && null != map.get(record.getCurrFrom()+record.getCurrTo()).getNewExchangeRate()){
                        BigDecimal newExchangeRete = (BigDecimal.valueOf(Double.parseDouble(map.get(record.getCurrFrom()+record.getCurrTo()).getNewExchangeRate())));
                        BigDecimal oldExchangeRete = record.getOfferRate();
                        if(Double.parseDouble(record.getOfferRate().toPlainString())!=0){
                            BigDecimal divisor = (newExchangeRete.subtract(oldExchangeRete));
                            BigDecimal percentageChange = divisor.divide(oldExchangeRete, 14, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(12);
                            map.get(record.getCurrFrom()+record.getCurrTo()).setPercentageChange(percentageChange.toPlainString()+"%");
                        } else {
                            map.get(record.getCurrFrom()+record.getCurrTo()).setPercentageChange(String.valueOf(0.0)+"%");
                        }
                    }
                } else {
                    CorrelationDataDto rate = new CorrelationDataDto();
                    rate.setCurrFrom(record.getCurrFrom());
                    rate.setCurrTo(record.getCurrTo());
                    rate.setCurrencyExchangeRate(record.getOfferRate().toString());
                    rate.setNewExchangeRate(null);
                    map.put(record.getCurrFrom()+record.getCurrTo(),rate);
                }
            }
        }
        return map;
    }

    public ExchangeRateFileDto getDetail(Long exchRateFileId, Instance instance) {
        List<ExchangeRateRecord> exchangeFileList = new ArrayList<>();
        List<CsvDto> newExchangeFileList = new ArrayList<>();
        exchangeFileList.addAll(exchangeRateRecordRepository.findExchangeRateRecord(exchRateFileId));
        ExchangeRateFileDto exchangeRateFileDto = new ExchangeRateFileDto();
        ExchangeRateFile newExchangeRateFile = exchangeRateFileRepository.findOne(exchRateFileId);
        exchangeRateFileDto.setNewFileName(newExchangeRateFile.getFileName());
        exchangeRateFileDto.setNewDateTime(bf.format(newExchangeRateFile.getCreateDate()));
        AnaAccount account = accountService.getAccount(newExchangeRateFile.getCreateBy());
        exchangeRateFileDto.setNewUserId(account.getFullName());
        List<CorrelationDataDto> listNew = new ArrayList<>();

        for(ExchangeRateRecord record : exchangeFileList){
            CsvDto dto = new CsvDto();
            dto.setOfferRate(record.getOfferRate().stripTrailingZeros().toPlainString());
            dto.setCurrTo(record.getCurrTo());
            dto.setCurrFrom(record.getCurrFrom());
            CorrelationDataDto cdd = new CorrelationDataDto();
            cdd.setCurrTo(record.getCurrTo());
            cdd.setCurrFrom(record.getCurrFrom());
            cdd.setCurrencyExchangeRate(record.getOfferRate().stripTrailingZeros().toPlainString());
            listNew.add(cdd);
            newExchangeFileList.add(dto);
            exchangeRateFileDto.setNewData(listNew);
        }
        List<CorrelationDataDto> listAll = new ArrayList<>();
        try {
            Map<String,CorrelationDataDto> map = correlationData(newExchangeFileList,exchangeRateFileDto,instance);
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                listAll.add((CorrelationDataDto)entry.getValue());
            }
        } catch (Exception e) {
            logger.error(TreasuryServiceImpl.class.getName(),e);
        }
        exchangeRateFileDto.setData(listAll);
        return exchangeRateFileDto;
    }


    @Override
    public RestfulResponse<ExchangeRateFileDto> loadExchangeRateFile(Long exchRateFileId, Instance instance) {
        ExchangeRateFileDto exchangeRateFileDto = new ExchangeRateFileDto();
        List<CorrelationDataDto> listAll = new ArrayList<>();
        List<CorrelationDataDto> listNew = new ArrayList<>();
        List<CsvDto> newExchangeFileList = new ArrayList<>();
        ExchangeRateFile newExchangeRateFile = exchangeRateFileRepository.findOne(exchRateFileId);
        exchangeRateFileDto.setNewFileName(newExchangeRateFile.getFileName());
        exchangeRateFileDto.setNewDateTime(bf.format(newExchangeRateFile.getCreateDate()));
        AnaAccount account = accountService.getAccount(newExchangeRateFile.getCreateBy());
        exchangeRateFileDto.setNewUserId(account.getFullName());
        File newExchangeFile = new File(newExchangeRateFile.getFilePath());
        try {
            newExchangeFileList.addAll(CSVFileUtil.parseCSVFile(CsvDto.class, newExchangeFile, null));
            if(CollectionUtils.isNotEmpty(newExchangeFileList)){
                if((StringUtils.isNotEmpty(newExchangeFileList.get(0).getCurrFrom()) && titles.contains(newExchangeFileList.get(0).getCurrFrom()))
                        || (StringUtils.isNotEmpty(newExchangeFileList.get(0).getCurrTo()) && titles.contains(newExchangeFileList.get(0).getCurrTo()))
                        || (StringUtils.isNotEmpty(newExchangeFileList.get(0).getOfferRate()) && titles.contains(newExchangeFileList.get(0).getOfferRate()))){
                    newExchangeFileList.remove(0);
                }
            }
            Map<String,CorrelationDataDto> map = correlationData(newExchangeFileList,exchangeRateFileDto,instance);
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                listAll.add((CorrelationDataDto)entry.getValue());
                if(null ==((CorrelationDataDto) entry.getValue()).getNewExchangeRate()){
                    listNew.add((CorrelationDataDto)entry.getValue());
                }
            }
        } catch (Exception e) {
            logger.error(TreasuryServiceImpl.class.getName(),e);
        }
        Map<String,StringBuffer> maps = checkData(newExchangeFile,instance);
        if(CollectionUtils.isNotEmpty(listNew)){
            StringBuffer sb = new StringBuffer();
            for(CorrelationDataDto dto : listNew){
                if(StringUtils.isNotEmpty(dto.getCurrFrom()) && StringUtils.isNotEmpty(dto.getCurrTo())){
                    BigDecimal currencyRate = BigDecimal.valueOf(Double.parseDouble(dto.getCurrencyExchangeRate()));
                    sb.append(dto.getCurrFrom()+","+dto.getCurrTo()+","+currencyRate.stripTrailingZeros().toPlainString()+"\n");
                }
            }
            maps.put(missingPairsKey,sb);
        }
        if(StringUtils.isNotEmpty(maps.get(missingPairsKey).toString()) || StringUtils.isNotEmpty(maps.get(duplicatePairsKey).toString())
                || StringUtils.isNotEmpty(maps.get(unfilledItemsKey).toString()) || StringUtils.isNotEmpty(maps.get(missingHeadKey).toString())
                || StringUtils.isNotEmpty(maps.get(wrongCurrencyKey).toString()) || StringUtils.isNotEmpty(maps.get(digitLimitKey).toString())) {
            exchangeRateFileDto.setErrorData(maps);
            exchangeRateFileDto.setStatus("FAIL");
        }else {
            exchangeRateFileDto.setData(listAll);
            exchangeRateFileDto.setStatus("SUCCESS");
        }
        return RestfulResponse.ofData(exchangeRateFileDto);
    }

    public RestfulResponse<String> uploadExchangeRateFile(MultipartFile file, Instance instance) throws Exception{
        List<RequestApproval> list = requestApprovalRepository.findByStatusAndCurrentEnvir(RequestApprovalStatus.PENDING_FOR_APPROVAL, instance);
        RestfulResponse<String> response = new RestfulResponse<>();
        if(CollectionUtils.isEmpty(list)){
            String data = saveExchangeRateFile(file,instance);
            response.setData(data);
        }
        return response;
    }


    public String saveExchangeRateFile(MultipartFile file,Instance instance) throws Exception{
        String fileName = "";
        fileName = fileDir + File.separator + FileUpload.upload(file, fileDir, null, true);
        String originalName = file.getOriginalFilename();
        String suffix=originalName.substring(originalName.lastIndexOf('.')+1, originalName.length());
        if(file.getSize()>1000000){
            throw new BusinessException(SystemMsg.ErrorMsg.FILES_IS_TOO_BIG.getErrorCode());
        }
        if(!suffix.equalsIgnoreCase("csv")){
            throw new BusinessException(SystemMsg.ErrorMsg.FILE_TYPE_ERROR.getErrorCode());
        }
        Account loginAccount = userService.getCurrentAccountInfo();
        ExchangeRateFile exchangeRateFile = new ExchangeRateFile();
        exchangeRateFile.setCreateBy(null==loginAccount?null:loginAccount.getAccountId());
        exchangeRateFile.setCreateDate(new Date());
        exchangeRateFile.setCurrentEnvir(instance);
        exchangeRateFile.setFileName(originalName);
        exchangeRateFile.setFilePath(fileName);
        exchangeRateFile.setFileType(suffix);
        exchangeRateFile.setStatus(RateFileStatus.PENDING_FOR_PROCESS.getValue());
        exchangeRateFile.setPreRateFileId(exchangeRateFile.getId());
        exchangeRateFileRepository.save(exchangeRateFile);
        return exchangeRateFile.getId().toString();
    }

    private Map checkData(File file,Instance instance){
        Map<String,StringBuffer> mapData = new HashMap<>();
        String url = prodCurrencyCodesUrl;
        if(instance.equals(Instance.PRE_PROD)){
            url = preCurrencyCodesUrl;
        }
        HashMap<String,String>  prams = new HashMap<>();
        prams.put("api_key",baseRateApiKey);
        String json =  httpClientUtils.httpGet(url,String.class,prams);
        CurrencyCodes currencyCodes = new Gson().fromJson(json, new TypeToken<CurrencyCodes>() {}.getType());
        List<String> rateList = currencyCodes.getCurrencyCodes();
        StringBuffer missingPairs = new StringBuffer();
        StringBuffer missingHeadTitle = new StringBuffer();
        StringBuffer duplicatePairs = new StringBuffer();
        StringBuffer unfilledItems = new StringBuffer();
        StringBuffer wrongCurrency = new StringBuffer();
        StringBuffer digitLimit = new StringBuffer();
        try (InputStreamReader isr= new InputStreamReader(new FileInputStream(file), "UTF-8");BufferedReader reader =new BufferedReader(isr);){
            String title = reader.readLine().replace(" ", "");
            title = title.replaceAll("\t","");

            if(!title.equalsIgnoreCase(titles)){
                missingHeadTitle.append(titles);
            }
            String line;
            Map<String,String> map = new HashMap<>();
            while((line=reader.readLine()) !=null){
                line = line.replaceAll("\t","");
                String[] valueArr = line.split(",");
                if(valueArr.length ==3){
                    Pattern pattern = Pattern.compile(this.pattern);
                    String value1 = valueArr[0].replace(" ", "");
                    String value2 = valueArr[1].replace(" ", "");
                    String value3 = valueArr[2].replace(" ", "");
                    Matcher isNum = pattern.matcher(value3);
                    if(StringUtils.isEmpty(value1) || StringUtils.isEmpty(value2)){
                        unfilledItems.append(line+"\n");
                    }
                    if(!isNum.matches()){
                        unfilledItems.append(line+"\n");
                      }else if(new BigDecimal(value3).compareTo(BigDecimal.valueOf(999999999999.999999999999))>0){//sonar modify
                    	digitLimit.append(line+"\n");
                    }
                    if(!rateList.contains(value1) || !rateList.contains(value2)){
                        wrongCurrency.append(line+"\n");
                    }
                    if(!map.containsKey(value1+value2)){
                        map.put(value1+value2,value1+value2);
                    } else {
                        duplicatePairs.append(line+"\n");
                    }
                } else {
                    if(StringUtils.isNotEmpty(line)){
                        unfilledItems.append(line+"\n");
                    }
                }
            }
        } catch (Exception e){
            throw new BusinessException(SystemMsg.ErrorMsg.UPLOAD_FILE_ERROR.getErrorCode());
        } 
        mapData.put(missingPairsKey,missingPairs);
        mapData.put(missingHeadKey,missingHeadTitle);
        mapData.put(wrongCurrencyKey,wrongCurrency);
        mapData.put(unfilledItemsKey,unfilledItems);
        mapData.put(duplicatePairsKey,duplicatePairs);
        mapData.put(digitLimitKey,digitLimit);
        return mapData;
    }

    @Override
    public RestfulResponse<List<ExchangeRateRecord>> saveExchangeRateRecord(String exchRateFileId,Instance instance,String requestRemark){
        try {
            Account loginAccount = userService.getCurrentAccountInfo();
            ExchangeRateFile exchangeRateFile = exchangeRateFileRepository.findOne(Long.valueOf(exchRateFileId));
            if(null == exchangeRateFile){
                throw new BusinessException(SystemMsg.ErrorMsg.UPLOAD_FILE_ERROR.getErrorCode());
            }
            File file = new File(exchangeRateFile.getFilePath());
            List<CsvDto> list = CSVFileUtil.parseCSVFile(CsvDto.class, file, null);
            if(list.size()>=0){
                if((StringUtils.isNotEmpty(list.get(0).getCurrFrom()) && titles.contains(list.get(0).getCurrFrom()))
                        || (StringUtils.isNotEmpty(list.get(0).getCurrTo()) && titles.contains(list.get(0).getCurrTo()))
                        || (StringUtils.isNotEmpty(list.get(0).getOfferRate()) && titles.contains(list.get(0).getOfferRate()))){
                    list.remove(0);
                }
            }
            List<ExchangeRateRecord> recordList = list.stream().map(item -> {
                ExchangeRateRecord rateRecord = new ExchangeRateRecord();
                rateRecord.setCreateBy(null == loginAccount ? null : loginAccount.getAccountId());
                rateRecord.setCreateDate(new Date());
                rateRecord.setCurrFrom(item.getCurrFrom().trim());
                rateRecord.setCurrTo(item.getCurrTo().trim());
                rateRecord.setExchRateFileId(Long.valueOf(exchRateFileId));
                rateRecord.setOfferRate(new BigDecimal(item.getOfferRate()).setScale(12,BigDecimal.ROUND_DOWN));
                return rateRecord;
            }).collect(Collectors.toList());

            exchangeRateRecordRepository.save(recordList);

            requestApprovalService.save(exchRateFileId, instance,requestRemark);

            return RestfulResponse.ofData(recordList);
        } catch (Exception e) {
            logger.error("saveExchangeRateRecord",e);
        }
        return null;
    }

    @Override
    public Boolean checkStatus(Instance instance) {
        List<RequestApproval> list = requestApprovalRepository.findByStatusAndCurrentEnvir(RequestApprovalStatus.PENDING_FOR_APPROVAL, instance);
        if(CollectionUtils.isNotEmpty(list)){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        return true;
    }
    
}
