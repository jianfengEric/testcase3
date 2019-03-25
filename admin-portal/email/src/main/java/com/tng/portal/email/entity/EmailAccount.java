package com.tng.portal.email.entity;

import javax.persistence.*;

/**
 * Created by Owen on 2016/11/18.
 */
@Entity
@Table(name = "EMAIL_ACCOUNT")
public class EmailAccount {

    @Id
    @Column(name = "id")
//    @GeneratedValue(generator = "seq_email_account", strategy = GenerationType.SEQUENCE)
//    @SequenceGenerator(name = "seq_email_account", sequenceName = "seq_email_account",allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "email_host_code", referencedColumnName = "code")
    private EmailHost emailHost;

    @Column(name = "account")
    private String account;

    @Column(name = "password")
    private String password;

    @Column(name = "priority")
    private Long priority;

    @Column(name = "default_sender_email")
    private String default_sender_email;

    @Column(name = "status")
    private String status;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "email_account_id")
    private EmailAccountQuota emailAccountQuota;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmailHost getEmailHost() {
        return emailHost;
    }

    public void setEmailHost(EmailHost emailHost) {
        this.emailHost = emailHost;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getDefault_sender_email() {
        return default_sender_email;
    }

    public void setDefault_sender_email(String default_sender_email) {
        this.default_sender_email = default_sender_email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EmailAccountQuota getEmailAccountQuota() {
        return emailAccountQuota;
    }

    public void setEmailAccountQuota(EmailAccountQuota emailAccountQuota) {
        this.emailAccountQuota = emailAccountQuota;
    }

    @Override
    public String toString() {
        return "EmailAccount{" +
                "id=" + id +
                ", emailHost=" + emailHost +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", priority=" + priority +
                ", default_sender_email='" + default_sender_email + '\'' +
                ", status='" + status + '\'' +
                ", emailAccountQuota=" + emailAccountQuota +
                '}';
    }
}
