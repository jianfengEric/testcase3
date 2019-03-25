package com.tng.portal.ana.dto;

import java.io.Serializable;

/**
 * Created by Jimmy on 2017/11/20.
 */
public class MerchantDto implements Serializable {

    /**
     *  Encrypted id
     */
    private String id;
    /**
     *  Encrypted mid
     */
    private String mid;
    private String merchantType;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MerchantDto{" +
                "id='" + id + '\'' +
                ", mid='" + mid + '\'' +
                ", merchantType='" + merchantType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}
