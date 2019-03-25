package com.gea.portal.apv.entity;

import com.tng.portal.common.enumeration.ApprovalType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "approval_category_item")
public class ApprovalCategoryItem {
    @Id
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "code", nullable = true, length = 45)
    @Enumerated(EnumType.STRING)
    private ApprovalType code;
    @Basic
    @Column(name = "desc", nullable = true, length = 150)
    private String desc;
    @Basic
    @Column(name = "status", nullable = true, length = 45)
    private String status;
    @Basic
    @Column(name = "create_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Basic
    @Column(name = "create_by", nullable = true, length = 45)
    private String createBy;
    @Basic
    @Column(name = "update_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Basic
    @Column(name = "update_by", nullable = true, length = 45)
    private String updateBy;
    @OneToMany(mappedBy = "approvalCategoryItem")
    private List<ApprovalResult> approvalResult;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ApprovalType getCode() {
        return code;
    }

    public void setCode(ApprovalType code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }


    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApprovalCategoryItem that = (ApprovalCategoryItem) o;
        return id == that.id &&
                Objects.equals(code, that.code) &&
                Objects.equals(desc, that.desc) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(createBy, that.createBy) &&
                Objects.equals(updateDate, that.updateDate) &&
                Objects.equals(updateBy, that.updateBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, desc, status, createDate, createBy, updateDate, updateBy);
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public List<ApprovalResult> getApprovalResult() {
        return approvalResult;
    }

    public void setApprovalResult(List<ApprovalResult> approvalResult) {
        this.approvalResult = approvalResult;
    }
}
