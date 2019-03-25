package com.gea.portal.ewp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the base_rate database table.
 * 
 */
@Entity
@Table(name="base_rate")
@NamedQuery(name="BaseRate.findAll", query="SELECT b FROM BaseRate b")
public class BaseRate implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="curr_from")
	private BigDecimal currFrom;

	@Column(name="curr_to")
	private BigDecimal currTo;

	private BigDecimal rate;

	private String status;

	public BaseRate() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public BigDecimal getCurrFrom() {
		return this.currFrom;
	}

	public void setCurrFrom(BigDecimal currFrom) {
		this.currFrom = currFrom;
	}

	public BigDecimal getCurrTo() {
		return this.currTo;
	}

	public void setCurrTo(BigDecimal currTo) {
		this.currTo = currTo;
	}

	public BigDecimal getRate() {
		return this.rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}