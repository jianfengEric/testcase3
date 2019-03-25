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
import com.tng.portal.sms.entity.QuestionTemplate;
import com.tng.portal.sms.repository.PageDatas;
import com.tng.portal.sms.service.QuestionService;
import com.tng.portal.sms.vo.QuestionTemplateDto;
import com.tng.portal.sms.vo.QuestionTemplateVo;

@Controller
@RequestMapping("/question")
public class QuestionController {
	
	@Autowired
	private QuestionService questionService;

	/**
	 * Query question template list
	 * @param pageNo current page number
	 * @param pageSize page size
	 * @param questionTypeIdSearch searched field
	 * @param questionSearch search value
	 * @param language
	 * @param creatorIdSearch
	 * @param sortBy sort by
	 * @param isAscending ascending or descending
	 * @return
	 */
    @RequestMapping(value = "",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<PageDatas<QuestionTemplateDto>> listQuestions(@RequestParam(required = false, defaultValue = "1") Integer pageNo,
    		@RequestParam(required = false, defaultValue = "50") Integer pageSize,
    		@RequestParam(required = false) String questionTypeIdSearch,
    		@RequestParam(required = false) String questionSearch,
    		@RequestParam(required = false) String language,
    		@RequestParam(required = false) String creatorIdSearch,
    		@RequestParam(required = false) String sortBy,
    		@RequestParam(required = false) boolean isAscending) {
    	RestfulResponse<PageDatas<QuestionTemplateDto>> restResponse = new RestfulResponse<>();
    	PageDatas<QuestionTemplateDto> pageDatas = questionService.getQuestionsByPage(pageNo, pageSize, questionTypeIdSearch, questionSearch, language, creatorIdSearch, sortBy, isAscending);
    	restResponse.setData(pageDatas);
        restResponse.setSuccessStatus();
        return restResponse;
    }

	/**
	 * Save question template
	 * @param vo question template data vo
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
    public RestfulResponse<Long> add(@RequestBody QuestionTemplateVo vo){
		RestfulResponse<Long> restResponse = new RestfulResponse<>();
		Long id = this.questionService.addQuestionTemplate(vo);
		restResponse.setData(id);
        restResponse.setSuccessStatus();
        return restResponse;
    }

	/**
	 * update question template
	 * @param vo question template info
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.PUT)
	@ResponseBody
    public RestfulResponse<Long> update(@RequestBody QuestionTemplateVo vo){
		RestfulResponse<Long> restResponse = new RestfulResponse<>();
		Long id = this.questionService.updateQuestion(vo);
		restResponse.setData(id);
        restResponse.setSuccessStatus();
        return restResponse;
    }

	/**
	 * Delete question template by question ids
	 * @param ids question template ids
	 * @return
	 */
	@RequestMapping(value = "/{ids}",method = RequestMethod.DELETE)
    @ResponseBody
    public RestfulResponse<String> deleteQuestionTemplates(@PathVariable("ids") String ids) {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
		String id = this.questionService.deleteQuestionTemplates(ids);
		restResponse.setData(id);
        restResponse.setSuccessStatus();
        return restResponse;
    }

	/**
	 * Query question template by question id
	 * @param id question template id
	 * @return
	 */
	@RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<QuestionTemplateVo> getQuestionTemplate(@PathVariable("id") Long id) {
        RestfulResponse<QuestionTemplateVo> restResponse = new RestfulResponse<>();
        QuestionTemplateVo qt = this.questionService.getQuestionTemplate(id);
		restResponse.setData(qt);
        restResponse.setSuccessStatus();
        return restResponse;
    }

	/**
	 * Query question template by type id
	 * @param typeId question type id
	 * @return
	 */
	@RequestMapping(value = "/byType/{typeId}",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<List<QuestionTemplate>> getQuestionByTypeId(@PathVariable("typeId") Long typeId){
        RestfulResponse<List<QuestionTemplate>> restResponse = new RestfulResponse<>();
        List<QuestionTemplate> questions = this.questionService.getQuestionByTypeId(typeId);
		restResponse.setData(questions);
        restResponse.setSuccessStatus();
        return restResponse;
    }

}
