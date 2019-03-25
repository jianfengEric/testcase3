package com.tng.portal.common.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Zero on 2016/11/4.
 */
@Entity
@Table(name = "ana_application")
public class AnaApplication {
    @Id
    @Column(name = "code", columnDefinition="CHAR")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "is_displayed")
    private Boolean isDisplay;

    @Version
    @Column(name = "optimisticLockVersion")
    private long optimisticLockVersion; //throws OptimisticLockException
    
    @Column(name = "external_endpoint")
    private String externalEndpoint;
    
    @Column(name = "internal_endpoint")
    private String internalEndpoint;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(Boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

    public Boolean getDisplay() {
        return isDisplay;
    }

    public void setDisplay(Boolean display) {
        isDisplay = display;
    }

    public long getOptimisticLockVersion() {
        return optimisticLockVersion;
    }

    public void setOptimisticLockVersion(long optimisticLockVersion) {
        this.optimisticLockVersion = optimisticLockVersion;
    }

	public String getExternalEndpoint() {
		return externalEndpoint;
	}

	public void setExternalEndpoint(String externalEndpoint) {
		this.externalEndpoint = externalEndpoint;
	}

	public String getInternalEndpoint() {
		return internalEndpoint;
	}

	public void setInternalEndpoint(String internalEndpoint) {
		this.internalEndpoint = internalEndpoint;
	}
}
