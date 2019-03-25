package com.tng.portal.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.sms.entity.SMSLanguage;
import com.tng.portal.sms.repository.PageDatas;
import com.tng.portal.sms.service.LanguageService;

@Controller
@RequestMapping("/language")
public class LanguageController {
	
	@Autowired
	private LanguageService languageService;

	/**
	 * Query language list
	 * @param pageNo current page number
	 * @param pageSize page size
	 * @return
	 */
    @RequestMapping(value = "",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<PageDatas<SMSLanguage>> listLanguages(@RequestParam(required = false, defaultValue = "1") Integer pageNo,
																 @RequestParam(required = false, defaultValue = "50") Integer pageSize,
																 @RequestParam(required = false) String search,
																 @RequestParam(required = false) Boolean isAscending,
																 @RequestParam(required = false) String sortBy) {
    	RestfulResponse<PageDatas<SMSLanguage>> restResponse = new RestfulResponse<>();
    	PageDatas<SMSLanguage> pageDatas = languageService.getLanguagesByPage(pageNo, pageSize, search, isAscending, sortBy);
    	restResponse.setData(pageDatas);
        restResponse.setSuccessStatus();
        return restResponse;
    }

	/**
	 * Delete language by id
	 * @param id sms language id
	 * @return
	 */
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public RestfulResponse<Long> deleteLanguage(@PathVariable("id") Long id){
        RestfulResponse<Long> restResponse = new RestfulResponse<>();
		Long deleteId = this.languageService.deleteLanguage(id);
		restResponse.setData(deleteId);
        restResponse.setSuccessStatus();
        return restResponse;
    }

	/**
	 * update language
	 * @param language language info
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.PUT)
	@ResponseBody
    public RestfulResponse<Long> update(@RequestBody SMSLanguage language){
		RestfulResponse<Long> restResponse = new RestfulResponse<>();
		Long id = this.languageService.updateLanguage(language);
		restResponse.setData(id);
        restResponse.setSuccessStatus();
        return restResponse;
    }

	/**
	 * add language
	 * @param language language info
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
    public RestfulResponse<Long> add(@RequestBody SMSLanguage language){
		RestfulResponse<Long> restResponse = new RestfulResponse<>();
		Long id = this.languageService.addLanguage(language);
		restResponse.setData(id);
        restResponse.setSuccessStatus();
        return restResponse;
    }

}
