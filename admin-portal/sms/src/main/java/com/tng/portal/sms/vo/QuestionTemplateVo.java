package com.tng.portal.sms.vo;

import java.util.List;
import java.util.Map;

public class QuestionTemplateVo {
	
	private Long id;
	
	private List<Long> typeIds;
	
	private String question;
	
	private Map<Long, String> answerMap;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Long> getTypeIds() {
		return typeIds;
	}

	public void setTypeIds(List<Long> typeIds) {
		this.typeIds = typeIds;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Map<Long, String> getAnswerMap() {
		return answerMap;
	}

	public void setAnswerMap(Map<Long, String> answerMap) {
		this.answerMap = answerMap;
	}
	
}
