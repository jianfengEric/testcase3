package com.tng.portal.email.vo;

import java.io.Serializable;

/**
 * Created by Owen on 2017/1/11.
 */
public class ReportDto implements Serializable {
    private String contentType;
    private String fileName;
    private byte[] content;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
