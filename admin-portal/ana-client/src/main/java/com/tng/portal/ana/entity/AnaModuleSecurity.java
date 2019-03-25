package com.tng.portal.ana.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 */
@Entity
@Table(name = "ana_module_security")
public class AnaModuleSecurity{
	
	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "consumer")
	private String consumer;

	@Column(name = "api_key")
	private String apiKey;

	@Column(name = "api_url_path")
	private String apiUrlPath;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConsumer() {
		return consumer;
	}

	public void setConsumer(String consumer) {
		this.consumer = consumer;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiUrlPath() {
		return apiUrlPath;
	}

	public void setApiUrlPath(String apiUrlPath) {
		this.apiUrlPath = apiUrlPath;
	}
	
}
