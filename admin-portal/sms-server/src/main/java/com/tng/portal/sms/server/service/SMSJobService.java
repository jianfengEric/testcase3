package com.tng.portal.sms.server.service;

import com.tng.portal.ana.vo.SMSJobQueryVo;
import com.tng.portal.ana.vo.SMSQueryParamVo;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.wfl.request.SMSJobInputVo;

public interface SMSJobService {

	PageDatas<SMSJobQueryVo> getJobsByPage(SMSQueryParamVo vo);

	void addJob(SMSJobInputVo vo);

	String terminateJob(Long id);

	String findMobile(Long id, String status, String mobile);

	void resend(String type, String key, String senderId);

}
