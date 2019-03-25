package com.tng.portal.common.vo.jir;

import java.io.Serializable;

public class JirCallbackDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long jobId;
	private String projectKey;
	private String issueKey;
	private String modifiedUserEmail;
	
	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	public String getProjectKey() {
		return projectKey;
	}
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}
	public String getIssueKey() {
		return issueKey;
	}
	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}
	public String getModifiedUserEmail() {
		return modifiedUserEmail;
	}
	public void setModifiedUserEmail(String modifiedUserEmail) {
		this.modifiedUserEmail = modifiedUserEmail;
	}
	@Override
    public String toString() {
        return "jobId:" + jobId +
        		"projectKey:" + projectKey + 
                "; issueKey:" + issueKey + 
                "; modifiedUserEmail:" + modifiedUserEmail;
    }
}
