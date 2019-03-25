package com.tng.portal.email.service;

import com.tng.portal.common.vo.email.EMailInput;
import com.tng.portal.email.entity.EmailMessage;
import com.tng.portal.email.vo.EmailAccountDto;
import java.util.List;

/**
 * Created by Dell on 2016/11/17.
 */
public interface EmailService {

    List<EmailMessage> getEmailMessages(String job);

    EmailAccountDto getEmailAccount(String hostCode,Long hostSizeLimit);

    void sendEmail(EMailInput emailInput);

    void sendEmailByMQ(EMailInput emailInput);

    void sendEmails();
}
