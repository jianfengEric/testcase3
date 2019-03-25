package com.tng.portal.email.util;

import com.tng.portal.common.constant.DateCode;
import com.tng.portal.common.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dell on 2017/10/19.
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static final String SAVE_PATH = PropertiesUtil.getAppValueByKey("download.file.path");

    /**
     *  From the network Url Download files 
     * @param urlStr
     * @throws IOException
     */
    public static File downLoadFromUrl(String urlStr) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat(DateCode.dateFormatMd);//sonar modify
        if (StringUtils.isBlank(urlStr)) {
            return null;
        }
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Set up over time. 3 second 
            conn.setConnectTimeout(3 * 1000);
            // Prevent shield program from grabbing and return 403 error 
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // Get input stream 
            InputStream inputStream = conn.getInputStream();
            // Get your own array 
            byte[] getData = readInputStream(inputStream);
            String path = SAVE_PATH + File.separator + sdf.format(new Date());
            // File save location 
            File saveDir = new File(path);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            String fileName = getFileNameFromUrl(urlStr);
            File file = new File(saveDir + File.separator + fileName);
            FileOutputStream fos =null;
            try {//sonar modify 
                fos = new FileOutputStream(file);
                fos.write(getData);
            }catch (Exception e){
                logger.error(e.getMessage(), e);//sonar modify
            }
            finally {
                if (fos != null) {
                    fos.close();
                }
                inputStream.close();
            }
            return file;
    }

    private static String getFileNameFromUrl(String url) {
        String name = new Long(System.currentTimeMillis()).toString() + ".X";
        int index = url.lastIndexOf("/");
        if (index > 0) {
            name = url.substring(index + 1);
            if (name.trim().length() > 0) {
                return name;
            }
        }
        return name;
    }


        /**
         *  Get byte array from input stream 
         * @param inputStream
         * @return
         * @throws IOException
         */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
