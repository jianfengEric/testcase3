package com.gea.portal.apv.controller;

import com.tng.portal.ana.repository.AnaAccountAccessTokenRepository;
import com.tng.portal.ana.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Dell on 2018/10/11.
 */
@RestController
@RequestMapping("remote/v1")
public class RemoteController {
    @Autowired
    private AnaAccountAccessTokenRepository anaAccountAccessTokenRepository;


    /*@GetMapping("delete-token")
    @ResponseBody
    public void deleteToken(){
        String expiresMinus = StringUtil.getPropertyValueByKey("token.expires.mins");
        int minus = Integer.parseInt(expiresMinus);
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,-minus);
        date=calendar.getTime();
        anaAccountAccessTokenRepository.deleteByExpriedTime(date);
    }*/

}
