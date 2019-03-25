package com.tng.portal.sms.service;

import com.tng.portal.sms.entity.SMSLanguage;
import com.tng.portal.sms.repository.PageDatas;

public interface LanguageService {

	PageDatas<SMSLanguage> getLanguagesByPage(Integer pageNo, Integer pageSize, String search, Boolean isAscending, String sortBy) ;

	Long deleteLanguage(Long id);

	Long updateLanguage(SMSLanguage type);

	Long addLanguage(SMSLanguage type);

}
