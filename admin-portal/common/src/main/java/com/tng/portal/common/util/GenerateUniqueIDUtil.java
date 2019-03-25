package com.tng.portal.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Eric on 2018/9/19.
 */
public class GenerateUniqueIDUtil {
    private static String dpyStr="DEPLOY-";
    private static String serverBatchNoStr="SRV-";

    
    private GenerateUniqueIDUtil(){}

    public static String generateDeployUniqueId(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateNowStr = sdf.format(d);
        String random=String.format("%04d",new Random().nextInt(10000));
        return dpyStr+dateNowStr+random;
    }
    
    public static String generateSrvUniqueId(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateNowStr = sdf.format(d);
        String random=String.format("%04d",new Random().nextInt(10000));
        return serverBatchNoStr+dateNowStr+random;
    }
}
