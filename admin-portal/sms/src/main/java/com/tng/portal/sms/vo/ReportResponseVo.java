package com.tng.portal.sms.vo;

import com.tng.portal.common.vo.rest.BaseRestfulResponse;
import com.tng.portal.common.vo.sms.ReportDataVo;
import com.tng.portal.sms.repository.PageDatas;

/**
 * Created by Owen on 2017/8/4.
 */
public class ReportResponseVo extends BaseRestfulResponse{
    private transient PageDatas<ReportDataVo> data;

    public PageDatas<ReportDataVo> getData() {
        return data;
    }

    public void setData(PageDatas<ReportDataVo> data) {
        this.data = data;
    }
}
