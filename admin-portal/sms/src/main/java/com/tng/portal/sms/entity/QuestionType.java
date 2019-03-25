package com.tng.portal.sms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tng.portal.ana.entity.AnaAccount;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "QUESTION_TYPE")
@JsonInclude(Include.NON_NULL)
public class QuestionType {
	@Id
	@Column(name="ID", unique=true, nullable=false)
	@GeneratedValue(generator="seq_question_type", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="seq_question_type", sequenceName="seq_question_type", allocationSize=1)
	private Long id;
	
	@Column(name="NAME", unique=true, nullable=false, length=50)
	private String name;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="CREATOR_ID")
	private AnaAccount anaAccount;

	@Column(name="MID")
	private String mid;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "questionTypes", fetch = FetchType.LAZY)
    private List<QuestionTemplate> questionTemplates;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AnaAccount getAnaAccount() {
		return anaAccount;
	}
	public void setAnaAccount(AnaAccount anaAccount) {
		this.anaAccount = anaAccount;
	}
	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}
	public List<QuestionTemplate> getQuestionTemplates() {
		return questionTemplates;
	}
	public void setQuestionTemplates(List<QuestionTemplate> questionTemplates) {
		this.questionTemplates = questionTemplates;
	}
}
