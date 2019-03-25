package com.tng.portal.email.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EMAIL_ACCOUNT_QUOTA")
public class EmailAccountQuota {
    @Id
    @Column(name = "email_account_id")
    private Long email_account_id;

    @OneToOne
    @JoinColumn(name = "email_account_id", referencedColumnName = "id")
    private EmailAccount emailAccount;

    @Column(name = "quota_period")
    private String quota_period;

    @Column(name = "send_quota")
    private Long send_quota;

    @Column(name = "send_counter")
    private Long send_counter;

    @Column(name = "update_date")
    private Date update_date;

    @Version
    @Column(name = "optlock_ver")
    private long optlock_ver;

    public Long getEmail_account_id() {
        return email_account_id;
    }

    public void setEmail_account_id(Long email_account_id) {
        this.email_account_id = email_account_id;
    }

    public String getQuota_period() {
        return quota_period;
    }

    public void setQuota_period(String quota_period) {
        this.quota_period = quota_period;
    }

    public Long getSend_quota() {
        return send_quota;
    }

    public void setSend_quota(Long send_quota) {
        this.send_quota = send_quota;
    }

    public Long getSend_counter() {
        return send_counter;
    }

    public void setSend_counter(Long send_counter) {
        this.send_counter = send_counter;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public long getOptlock_ver() {
        return optlock_ver;
    }

    public void setOptlock_ver(long optlock_ver) {
        this.optlock_ver = optlock_ver;
    }

    public EmailAccount getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(EmailAccount emailAccount) {
        this.emailAccount = emailAccount;
    }
}
