package com.tng.portal.sms.service;

import java.util.List;

import com.tng.portal.sms.entity.QuestionType;
import com.tng.portal.sms.repository.PageDatas;
import com.tng.portal.sms.vo.QuestionTypeVo;

public interface QuestionTypeService {

	PageDatas<QuestionTypeVo> getQuestionTypesByPage(Integer pageNo, Integer pageSize, String nameSearch, String creatorIdSearch) ;

	Long deleteQuestionType(Long id);

	Long updateQuestionType(QuestionType type);

	Long addQuestionType(QuestionType type);

	List<QuestionType> getQuestionTypes();

}
