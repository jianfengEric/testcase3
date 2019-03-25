package com.tng.portal.common.vo.jir;

import java.io.Serializable;

public class JirRequestBody implements Serializable{

	private static final long serialVersionUID = 1L;	
	
	//for create
    private String module;
    private String component;
    private String reportBy;
    private String assign2Role;
    private String title;
    private String description;
    private long jobId;
    
    //for others
    private String caseId;
    private String actionName;
    private String approvedBy;
    private String rejectedBy;
    private String completedBy;
    private String deleteBy;
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getReportBy() {
		return reportBy;
	}
	public void setReportBy(String reportBy) {
		this.reportBy = reportBy;
	}
	public String getAssign2Role() {
		return assign2Role;
	}
	public void setAssign2Role(String assign2Role) {
		this.assign2Role = assign2Role;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
		
	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public String getRejectedBy() {
		return rejectedBy;
	}
	public void setRejectedBy(String rejectedBy) {
		this.rejectedBy = rejectedBy;
	}
	public String getCompletedBy() {
		return completedBy;
	}
	public void setCompletedBy(String completedBy) {
		this.completedBy = completedBy;
	}
	public String getDeleteBy() {
		return deleteBy;
	}
	public void setDeleteBy(String deleteBy) {
		this.deleteBy = deleteBy;
	}     
	
	@Override
    public String toString() {
        return "requestId:" + jobId + 
                "; actionName:" + actionName + 
                "; caseId:" + caseId + 
                "; reportBy:" + reportBy + 
                "; title:" + title + 
                "; description:" + description + 
                "; module:" + module;
    }

}
