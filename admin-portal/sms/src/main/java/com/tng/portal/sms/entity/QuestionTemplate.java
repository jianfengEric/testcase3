package com.tng.portal.sms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tng.portal.ana.entity.AnaAccount;

@Entity
@Table(name = "QUESTION_TEMPLATE")
@JsonInclude(Include.NON_NULL)
public class QuestionTemplate {
	@Id
	@Column(name="ID", unique=true, nullable=false)
	@GeneratedValue(generator="seq_question_template", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="seq_question_template", sequenceName="seq_question_template", allocationSize=1)
	private Long id;
	
	@Column(name="QUESTION", unique=true, nullable=false, length=100)
	private String question;
	
	@ManyToOne
	@JoinColumn(name="CREATOR_ID")
	private AnaAccount anaAccount;

	@Column(name="MID")
	private String mid;
	
	@ManyToMany(cascade={CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "type_question_relation",
            joinColumns = {@JoinColumn(name = "question_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "type_id",referencedColumnName = "id")})
    private List<QuestionType> questionTypes;
    
	@Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "questionTemplate", orphanRemoval = true)
    private List<QuestionAnswer> questionAnswers;
	
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
	public List<QuestionType> getQuestionTypes() {
		return questionTypes;
	}
	public void setQuestionTypes(List<QuestionType> questionTypes) {
		this.questionTypes = questionTypes;
	}
	@JsonIgnore
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

	public List<QuestionAnswer> getQuestionAnswers() {
		return questionAnswers;
	}
	public void setQuestionAnswers(List<QuestionAnswer> questionAnswers) {
		this.questionAnswers = questionAnswers;
	}
}
