package com.gea.portal.tre.entity;

import com.tng.portal.common.enumeration.Instance;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Owen on 2018/9/14.
 */
@Table(name = "exchange_rate_file")
@Entity
public class ExchangeRateFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_type")
    private String fileType;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "remark")
    private String remark;
    @Column(name = "status")
    private String status;
    @Column(name = "pre_rate_file_id")
    private Long preRateFileId;
    @Column(name = "current_envir")
    @Enumerated(EnumType.STRING)
    private Instance currentEnvir;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by")
    private String updateBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPreRateFileId() {
        return preRateFileId;
    }

    public void setPreRateFileId(Long preRateFileId) {
        this.preRateFileId = preRateFileId;
    }

    public Instance getCurrentEnvir() {
        return currentEnvir;
    }

    public void setCurrentEnvir(Instance currentEnvir) {
        this.currentEnvir = currentEnvir;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
