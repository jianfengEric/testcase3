package com.tng.portal.sms.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "QUESTION_ANSWER")
@JsonInclude(Include.NON_NULL)
public class QuestionAnswer {
	@EmbeddedId
    private QuestionAnswerPk questionAnswerPk;
	
	@JsonIgnore
	@ManyToOne(optional = false)
    @JoinColumn(name = "question_id",referencedColumnName = "id",insertable = false, updatable = false)
	private QuestionTemplate questionTemplate;
	
	@JsonIgnore
	@ManyToOne(optional = false)
    @JoinColumn(name = "language_id",referencedColumnName = "id",insertable = false, updatable = false)
	private SMSLanguage language;
	
	@Column(name="answer", nullable=false, length=255)
	private String answer;

	public QuestionAnswerPk getQuestionAnswerPk() {
		return questionAnswerPk;
	}

	public void setQuestionAnswerPk(QuestionAnswerPk questionAnswerPk) {
		this.questionAnswerPk = questionAnswerPk;
	}

	public QuestionTemplate getQuestionTemplate() {
		return questionTemplate;
	}

	public void setQuestionTemplate(QuestionTemplate questionTemplate) {
		this.questionTemplate = questionTemplate;
	}

	public SMSLanguage getLanguage() {
		return language;
	}

	public void setLanguage(SMSLanguage language) {
		this.language = language;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
}