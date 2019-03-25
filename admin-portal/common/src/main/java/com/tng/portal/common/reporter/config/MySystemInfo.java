package com.tng.portal.common.reporter.config;

import com.vimalselvam.testng.SystemInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class MySystemInfo implements SystemInfo {
    @Override
    public Map<String, String> getSystemInfo() {
        /*InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("env.properties");*/
        Properties properties = new Properties();
        Map<String, String> systemInfo = new HashMap<>();
        try {
            /*properties.load(inputStream);*/
            systemInfo.put("environment", "test-Environment");
            systemInfo.put("测试人员", "test");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return systemInfo;
    }
}
