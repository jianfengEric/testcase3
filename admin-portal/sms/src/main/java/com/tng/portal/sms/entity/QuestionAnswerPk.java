package com.tng.portal.sms.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class QuestionAnswerPk implements Serializable{
    @Basic(optional = false)
    @Column(name = "question_id")
    private Long questionId;
    
    @Basic(optional = false)
    @Column(name = "language_id")
    private Long languageId;

    public QuestionAnswerPk() {

    }
    public QuestionAnswerPk(Long questionId, Long languageId) {
        this.questionId = questionId;
        this.languageId = languageId;
    }

    public Long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	public Long getLanguageId() {
		return languageId;
	}
	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}
	@Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj != null && obj.getClass()==QuestionAnswerPk.class){
            QuestionAnswerPk pk = (QuestionAnswerPk) obj;
            return pk.getQuestionId()==getQuestionId()
                    && pk.getLanguageId() == getLanguageId();

        }
        return false;
    }

    @Override
    public int hashCode() {
        return getQuestionId().hashCode()*31 + getLanguageId().hashCode();
    }
}
