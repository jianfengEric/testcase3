package com.tng.portal.common.enumeration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owen on 2018/8/24.
 */
public enum RequestApprovalStatus {
	
    PENDING_FOR_APPROVAL("PENDING_FOR_APPROVAL","Pending Approval","PendingForApproval"),
    APPROVED ("APPROVED","Editable","Approved"),
    REJECT("REJECT","Editable","Reject");
	
	public static final String ST="1st pre-production deployment";
	
	private String value;

	private String listView;
	
	private String apvListView;
	

	private RequestApprovalStatus(String value,String listView,String apvListView) {
        this.value = value;
        this.listView = listView;
        this.apvListView = apvListView;
    }
	
	public static RequestApprovalStatus findByValue(String value){
		for(RequestApprovalStatus item : RequestApprovalStatus.values()){
			if(item.getValue().equals(value)){
				return item;
			}
		}
		return null;
	}

	public static List<RequestApprovalStatus> findByListView(String listView){
		List<RequestApprovalStatus> list = new ArrayList<>();
		for(RequestApprovalStatus item : RequestApprovalStatus.values()){
			if(item.getListView().equals(listView)){
				list.add(item);
			}
		}
		return list;
	}

    public String getValue() {
        return value;
    }

	public String getListView() {
		return listView;
	}

	public String getApvListView() {
		return apvListView;
	}

}
