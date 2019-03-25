package com.tng.portal.sms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.sms.entity.QuestionType;
import com.tng.portal.sms.repository.PageDatas;
import com.tng.portal.sms.service.QuestionTypeService;
import com.tng.portal.sms.vo.QuestionTypeVo;

@Controller
@RequestMapping("/questionType")
public class QuestionTypeController {
	
	@Autowired
	private QuestionTypeService questionTypeService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<PageDatas<QuestionTypeVo>> listQuestionTypes(
    		@RequestParam(required = false) Integer pageNo,
    		@RequestParam(required = false) Integer pageSize,
    		@RequestParam(required = false) String nameSearch,
    		@RequestParam(required = false) String creatorIdSearch) {
    	RestfulResponse<PageDatas<QuestionTypeVo>> restResponse = new RestfulResponse<>();
    	PageDatas<QuestionTypeVo> pageDatas = questionTypeService.getQuestionTypesByPage(pageNo, pageSize, nameSearch, creatorIdSearch);
    	restResponse.setData(pageDatas);
        restResponse.setSuccessStatus();
        return restResponse;
    }
    
    /**
     * Delete question type by id
     * 
     * @param id
     * 			question type id
     * 
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public RestfulResponse<Long> deleteQuestionTemplates(@PathVariable("id") Long id){
        RestfulResponse<Long> restResponse = new RestfulResponse<>();
		Long deleteId = this.questionTypeService.deleteQuestionType(id);
		restResponse.setData(deleteId);
        restResponse.setSuccessStatus();
        return restResponse;
    }
    
	/**
	 * update question type
	 * 
	 * @param type 
	 * 			question type info
	 * 
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.PUT)
	@ResponseBody
    public RestfulResponse<Long> update(@RequestBody QuestionType type){
		RestfulResponse<Long> restResponse = new RestfulResponse<>();
		Long id = this.questionTypeService.updateQuestionType(type);
		restResponse.setData(id);
        restResponse.setSuccessStatus();
        return restResponse;
    }
	
	/**
	 * add question type
	 * 
	 * @param type 
	 * 			question type info
	 * 
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
    public RestfulResponse<Long> add(@RequestBody QuestionType type){
		RestfulResponse<Long> restResponse = new RestfulResponse<>();
		Long id = this.questionTypeService.addQuestionType(type);
		restResponse.setData(id);
        restResponse.setSuccessStatus();
        return restResponse;
    }

	/**
	 * Query question type list 
	 * 
	 * @return
	 */
    @RequestMapping(value = "all",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<List<QuestionType>> listQuestionTypes() {
    	RestfulResponse<List<QuestionType>> restResponse = new RestfulResponse<>();
    	List<QuestionType> questionTypes = questionTypeService.getQuestionTypes();
    	restResponse.setData(questionTypes);
        restResponse.setSuccessStatus();
        return restResponse;
    }

}
