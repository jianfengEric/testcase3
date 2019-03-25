package com.gea.portal.eny.entity.pre;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "EWALLET_PARTICIPANT")
public class EwalletParticipant {
    private int id;
    private String code;
    private String name;
    private String apiKey;
    private String secretKey;
    private String apiGatewayUrl;
    private int active;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CODE", nullable = false, length = 255)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "NAME", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "API_KEY", nullable = false, length = 255)
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Basic
    @Column(name = "SECRET_KEY", nullable = false, length = 255)
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Basic
    @Column(name = "API_GATEWAY_URL", nullable = true, length = 255)
    public String getApiGatewayUrl() {
        return apiGatewayUrl;
    }

    public void setApiGatewayUrl(String apiGatewayUrl) {
        this.apiGatewayUrl = apiGatewayUrl;
    }

    @Basic
    @Column(name = "ACTIVE", nullable = false)
    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EwalletParticipant that = (EwalletParticipant) o;
        return id == that.id &&
                active == that.active &&
                Objects.equals(code, that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(apiKey, that.apiKey) &&
                Objects.equals(secretKey, that.secretKey) &&
                Objects.equals(apiGatewayUrl, that.apiGatewayUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, apiKey, secretKey, apiGatewayUrl, active);
    }
}
