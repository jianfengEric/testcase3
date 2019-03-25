package com.tng.portal.common.util;

/**
 * Created by Roger on 2017/9/15.
 */
public class ApplicationContext {
    
    private ApplicationContext(){}
    
    public static class Env{
        public static final String integrated = "integrated";
        public static final String integrated_client = "integrated.client";
        public static final String standalone = "standalone";
        
        private Env(){}
    }

    public static class Communication{
        public static final String HTTP = "http";
        public static final String MQ = "mq";
    }
    public static class Modules{
        public static final String ANA = "ANA";
        public static final String MAM = "MAM";
        public static final String BTU = "BTU";
        public static final String SMS = "SMS";
        public static final String SMM = "SMM";
        public static final String MSG = "MSG";
        public static final String EWP = "EWP";
        public static final String MP = "MP";
        public static final String APV = "APV";
        public static final String TRE = "TRE";
        public static final String DPY = "DPY";
        public static final String AGE = "AGE";
        public static final String ENY = "ENY";
        public static final String SRV = "SRV";
        public static final String ORD = "ORD";
    }
    
    public static class Key{
        public static final String integratedStyle = "integrated.style";
        public static final String communicationStyle = "communication.style";
        public static final String serviceName = "service.name";
        public static final String serviceNameTemplate = "service.name.template";
    }
    

}
