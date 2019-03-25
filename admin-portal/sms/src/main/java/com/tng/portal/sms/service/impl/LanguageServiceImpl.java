package com.tng.portal.sms.service.impl;


import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.repository.AnaAccountRepository;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.sms.constant.SystemMsg;
import com.tng.portal.sms.entity.SMSLanguage;
import com.tng.portal.sms.repository.LanguageRepository;
import com.tng.portal.sms.repository.PageDatas;
import com.tng.portal.sms.service.LanguageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class LanguageServiceImpl implements LanguageService{
    @Autowired
	private LanguageRepository languageRepository;
    
    @Qualifier("anaUserService")
	@Autowired
	private UserService userService;

	@Autowired
	private AnaAccountRepository anaAccountRepository;
    
    /**
	 * Query language list 
	 * 
	 * @param pageNo
	 * 			current page number
	 * 
	 * @param pageSize
	 * 			page size
	 * 
	 * @return
	 */
	@Override
	@Transactional
	public PageDatas<SMSLanguage> getLanguagesByPage(Integer pageNo, Integer pageSize, String search, Boolean isAscending, String sortBy) {
		PageDatas<SMSLanguage> pageDatas = new PageDatas<>(pageNo, pageSize);
		Sort sort = new Sort(Direction.ASC, "sequence");
		String searchs = search;
		if (StringUtils.isBlank(searchs)){
			searchs = "%%";
		} else {
			searchs = searchs.replaceAll("_","\\\\_");
			searchs = searchs.replaceAll("%","\\\\%");
			searchs = "%"+searchs+"%";
		}
		if(null != isAscending && StringUtils.isNotBlank(sortBy)){
			sort = new Sort(isAscending?Direction.ASC:Direction.DESC, getSort(sortBy));
		}
		AnaAccount anaAccount = anaAccountRepository.findOne(userService.getCurrentAccountInfo().getAccountId());
		Page<SMSLanguage> page =null;
		String mid = anaAccount.getExternalGroupId();
		if(StringUtils.isNotBlank(mid)){
			page = languageRepository.findLanguagesByMid(mid, searchs, pageDatas.pageRequest(sort));
		}else {
			page = languageRepository.findLanguages(searchs, pageDatas.pageRequest(sort));
		}

    	pageDatas.initDatas(page.getContent(), page.getTotalElements(), page.getTotalPages());
        return pageDatas;
	}

	private String getSort(String sortBy){
		Map<String, String> sortMap = new HashMap<>();
		sortMap.put("sequence","sequence");
		sortMap.put("language","language");
		sortMap.put("mandatory","isMandatory");//sonar modify 

		String sort = sortMap.get(sortBy);
		if(StringUtils.isBlank(sort)){
			return "sequence";
		}
		return sort;
	}

	/**
     * Delete language by id
     * 
     * @param id
     * 			language id
     * 
     * @return
	 * @throws BusinessException 
     */
	@Override
	@Transactional
	public Long deleteLanguage(Long id){
		SMSLanguage language = this.languageRepository.findOne(id);
		if(language == null)		
			throw new BusinessException(SystemMsg.ServerErrorMsg.NOT_EXISTS_LANGUAGE.getErrorCode());
		
		this.languageRepository.delete(language);
		return id;
	}

	/**
	 * update language
	 * 
	 * @param language 
	 * 			language info
	 * 
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	@Transactional
	public Long updateLanguage(SMSLanguage language){
		AnaAccount anaAccount = anaAccountRepository.findOne(userService.getCurrentAccountInfo().getAccountId());
		String mid = anaAccount.getExternalGroupId();
		SMSLanguage sameNameLanguage = this.languageRepository.findByLanguageAndMid(language.getLanguage(), mid);
		if(sameNameLanguage == null){//sonar modify
			throw new BusinessException(SystemMsg.ServerErrorMsg.NOT_EXISTS_LANGUAGE.getErrorCode());
		}
		if(sameNameLanguage.getId() != language.getId())
			throw new BusinessException(SystemMsg.ServerErrorMsg.LANGUAGE_EXISTS_ERROR.getErrorCode());
		if(language.getSequence()!=sameNameLanguage.getSequence()){
			throw new BusinessException(SystemMsg.ServerErrorMsg.SEQUENCE_EXISTS_ERROR.getErrorCode());
		}
		
		return this.languageRepository.save(language).getId();
	}
	
	/**
	 * add language
	 * 
	 * @param language 
	 * 			language info
	 * 
	 * @return
	 * throws BusinessException
	 */
	@Override
	@Transactional
	public Long addLanguage(SMSLanguage language){
		AnaAccount anaAccount = anaAccountRepository.findOne(userService.getCurrentAccountInfo().getAccountId());
		String mid = anaAccount.getExternalGroupId();
		SMSLanguage sameNameLanguage = this.languageRepository.findByLanguageAndMid(language.getLanguage(), mid);
		if(sameNameLanguage != null)
			throw new BusinessException(SystemMsg.ServerErrorMsg.LANGUAGE_EXISTS_ERROR.getErrorCode());
		
		SMSLanguage sameSeqLanguage = this.languageRepository.findBySequenceAndMid(language.getSequence(), mid);
		if(sameSeqLanguage != null)
			throw new BusinessException(SystemMsg.ServerErrorMsg.SEQUENCE_EXISTS_ERROR.getErrorCode());
		language.setMid(mid);
		return this.languageRepository.save(language).getId();
	}

}