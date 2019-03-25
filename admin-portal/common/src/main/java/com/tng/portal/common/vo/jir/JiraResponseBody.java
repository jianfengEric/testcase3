package com.tng.portal.common.vo.jir;

import java.io.Serializable;

public class JiraResponseBody implements Serializable{

	private static final long serialVersionUID = 1L;	
    
	private long jobId;
	private String issueKey;

	  
	public long getJobId() {
		return jobId;
	}


	public void setJobId(long jobId) {
		this.jobId = jobId;
	}


	public String getIssueKey() {
		return issueKey;
	}


	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}

	@Override
    public String toString() {
        return "jobId:" + jobId + 
                "; issueKey:" + issueKey;
    }

}
