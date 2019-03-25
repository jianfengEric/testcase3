package com.tng.portal.common.enumeration;

/**
 * Created by Eric on 2018/11/6.
 */
public enum FileTypes {
    PNG("png"),
    GIF("gif"),
    JPG("jpg"),
    BMP("bmp"),
    JPEG("jpeg"),
    JPE("jpe"),
    TIFF("tiff"),
    TIF("tif"),
    PDF("pdf"),
    ;


    FileTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String value;
}
