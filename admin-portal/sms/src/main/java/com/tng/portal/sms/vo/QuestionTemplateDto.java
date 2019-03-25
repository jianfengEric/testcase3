package com.tng.portal.sms.vo;

import java.io.Serializable;
import java.util.List;

import com.tng.portal.sms.entity.QuestionAnswer;
import com.tng.portal.sms.entity.QuestionType;

public class QuestionTemplateDto implements Serializable {
	private Long id;
	
	private String question;
	
	private String creatorName;
	
    private transient List<QuestionType> questionTypes;
    
    private transient List<QuestionAnswer> questionAnswers;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public List<QuestionType> getQuestionTypes() {
		return questionTypes;
	}
	public void setQuestionTypes(List<QuestionType> questionTypes) {
		this.questionTypes = questionTypes;
	}
	public List<QuestionAnswer> getQuestionAnswers() {
		return questionAnswers;
	}
	public void setQuestionAnswers(List<QuestionAnswer> questionAnswers) {
		this.questionAnswers = questionAnswers;
	}
}
