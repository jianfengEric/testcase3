package com.tng.portal.email.entity;

import javax.persistence.*;

/**
 * Created by Owen on 2017/10/17.
 */
@Entity
@Table(name = "EMAIL_ATTACHMENT")
public class EmailAttachment {
    @Id
    @Column(name = "id")
//    @GeneratedValue(generator="seq_email_attachment",strategy= GenerationType.SEQUENCE)
//    @SequenceGenerator(name="seq_email_attachment",sequenceName="seq_email_attachment", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "attachment_name")
    private String attachmentName;
    @Column(name = "attachment_path")
    private String attachmentPath;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.LAZY)
    @JoinColumn(name = "email_content_id")
    private EmailContent emailContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public EmailContent getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(EmailContent emailContent) {
        this.emailContent = emailContent;
    }
}
