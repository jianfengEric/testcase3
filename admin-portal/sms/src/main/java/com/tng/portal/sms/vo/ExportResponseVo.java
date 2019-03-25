package com.tng.portal.sms.vo;

import com.tng.portal.common.vo.rest.BaseRestfulResponse;

/**
 * Created by Owen on 2017/8/4.
 */
public class ExportResponseVo extends BaseRestfulResponse{
    private ReportDto data;

    public ReportDto getData() {
        return data;
    }

    public void setData(ReportDto data) {
        this.data = data;
    }
}
