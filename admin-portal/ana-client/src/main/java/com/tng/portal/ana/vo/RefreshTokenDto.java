package com.tng.portal.ana.vo;

import java.io.Serializable;

/**
 * Created by Zero on 2016/11/18.
 */
public class RefreshTokenDto implements Serializable {
    private String accessToken;
    private Long  expriesIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpriesIn() {
        return expriesIn;
    }

    public void setExpriesIn(Long expriesIn) {
        this.expriesIn = expriesIn;
    }
}
