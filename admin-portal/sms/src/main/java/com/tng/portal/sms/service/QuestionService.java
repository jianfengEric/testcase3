package com.tng.portal.sms.service;

import java.util.List;

import com.tng.portal.sms.entity.QuestionTemplate;
import com.tng.portal.sms.repository.PageDatas;
import com.tng.portal.sms.vo.QuestionTemplateDto;
import com.tng.portal.sms.vo.QuestionTemplateVo;

public interface QuestionService {

	PageDatas<QuestionTemplateDto> getQuestionsByPage(Integer pageNo, Integer pageSize, String questionTypeIdSearch, String questionSearch, String language, String creatorIdSearch, String sortBy, boolean isAscending);

	Long addQuestionTemplate(QuestionTemplateVo vo);
	
	Long updateQuestion(QuestionTemplateVo vo);

	String deleteQuestionTemplates(String ids);

	QuestionTemplateVo getQuestionTemplate(Long id);

	List<QuestionTemplate> getQuestionByTypeId(Long typeId);
}
