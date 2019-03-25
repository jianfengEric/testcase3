package com.tng.portal.common.vo.sms;

import com.tng.portal.common.vo.PageDatas;

/**
 *  Compared to ReportDataVo Add longitudinal summation statistics. 
 * Created by Jimmy on 2017/10/24.
 */
public class ReportDataVto extends PageDatas<ReportDataVo> {

    private int jobCount;
    private int successCount;
    private int failedCount;
    private int totalCount;

    public int getJobCount() {
        return jobCount;
    }

    public void setJobCount(int jobCount) {
        this.jobCount = jobCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

}
