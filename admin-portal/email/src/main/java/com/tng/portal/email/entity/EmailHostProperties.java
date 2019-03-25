package com.tng.portal.email.entity;

import javax.persistence.*;

/**
 * Created by Owen on 2016/11/18.
 */
@Entity
@Table(name = "email_host_properties")
public class EmailHostProperties {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "email_host_id")
    private EmailHost emailHost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public EmailHost getEmailHost() {
        return emailHost;
    }

    public void setEmailHost(EmailHost emailHost) {
        this.emailHost = emailHost;
    }
}
