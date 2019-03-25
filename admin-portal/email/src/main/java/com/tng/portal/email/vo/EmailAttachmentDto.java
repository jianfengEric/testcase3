package com.tng.portal.email.vo;

import java.io.Serializable;

/**
 * Created by Owen on 2017/10/23.
 */
public class EmailAttachmentDto  implements Serializable{
    private String fileName;
    private String filePath;
    private long fileSize;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
