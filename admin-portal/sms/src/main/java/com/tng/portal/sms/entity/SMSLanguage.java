package com.tng.portal.sms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "SMS_LANGUAGE")
@JsonInclude(Include.NON_NULL)
public class SMSLanguage {
	@Id
	@Column(name="ID", unique=true, nullable=false)
	@GeneratedValue(generator="seq_sms_language", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="seq_sms_language", sequenceName="seq_sms_language", allocationSize=1)
	private Long id;
	
	@Column(name="LANGUAGE", unique=true, nullable=false, length=20)
	private String language;
	
	@Column(name="IS_MANDATORY", nullable=false, length=1)
	private boolean isMandatory;
	
	@Column(name="DISPLAY_SEQ", unique=true, nullable=false, length=3)
	private int sequence;

	@Column(name="MID")
	private String mid;
	
	@OneToMany(cascade=CascadeType.REMOVE,mappedBy = "language")
    private List<QuestionAnswer> questionAnswers;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public boolean isMandatory() {
		return isMandatory;
	}
	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}
}