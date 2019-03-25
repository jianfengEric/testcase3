package com.tng.portal.sms.service;

import com.tng.portal.ana.vo.SMSJobQueryVo;
import com.tng.portal.ana.vo.SMSQueryParamVo;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.wfl.request.SMSJobInputVo;

public interface JobService {

	PageDatas<SMSJobQueryVo> getJobsByPage(SMSQueryParamVo vo);

	void addJob(SMSJobInputVo vo);

	String assembMobileNumber(SMSJobInputVo vo);

	void terminateJob(Long id);

	String findMobile(Long id, String status, String mobile);

	void resend(String type, String key);
}
