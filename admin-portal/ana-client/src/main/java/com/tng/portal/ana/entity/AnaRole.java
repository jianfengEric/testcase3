package com.tng.portal.ana.entity;

import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.util.ToolUtil;
import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.entity.BaseAuditEntity;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by Zero on 2016/11/4.
 */
@Entity
@Table(name = "ana_role")
public class AnaRole extends BaseAuditEntity{
    @Id
    @Column(name = "id")
//    @GeneratedValue(generator = "SEQ_ANA_ROLE_ID", strategy = GenerationType.SEQUENCE)
//    @SequenceGenerator(name = "SEQ_ANA_ROLE_ID", sequenceName = "SEQ_ANA_ROLE_ID",allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "description")
    private String description;

    @Column(name = "MID")
    private String mid;

    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "last_modify_date")
    private Date lastModifyDate;
    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_code")
    private AnaApplication anaApplication;

    @OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "anaRole")
    private List<AnaRoleFunctionPermission> anaRoleFunctions;

    @ManyToMany(mappedBy = "anaRoles")
    private List<AnaAccount> anaAccounts;

    @Column(name = "type", columnDefinition="CHAR")
    private String type;

    @Version
    @Column(name = "optimisticLockVersion")
    private long optimisticLockVersion; //throws OptimisticLockException

    @Column(name = "is_default", columnDefinition="CHAR")
    private String isdefault;

    @Column(name = "role_type", columnDefinition="CHAR")
    private String roleType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return StringUtils.trimToEmpty(name);
    }

    public void setName(String name) {

        this.name = StringUtils.trimToEmpty(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public AnaApplication getAnaApplication() {
        return anaApplication;
    }

    public void setAnaApplication(AnaApplication anaApplication) {
        this.anaApplication = anaApplication;
    }

    public List<AnaRoleFunctionPermission> getAnaRoleFunctions() {
        return anaRoleFunctions;
    }

    public void setAnaRoleFunctions(List<AnaRoleFunctionPermission> anaRoleFunctions) {
        this.anaRoleFunctions = anaRoleFunctions;
    }

    public List<AnaAccount> getAnaAccounts() {
        return anaAccounts;
    }

    public void setAnaAccounts(List<AnaAccount> anaAccounts) {
        this.anaAccounts = anaAccounts;
    }

    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getLastModifyDate() {
        return lastModifyDate;
    }
    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public void upOperation(HttpServletRequest request,Account loginAccount){
        super.setUpdatedBy(loginAccount.getUsername());
        super.setUpdatedTime(new Date());
        super.setIpAddress(ToolUtil.getRemoteHost(request));
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getOptimisticLockVersion() {
        return optimisticLockVersion;
    }

    public void setOptimisticLockVersion(long optimisticLockVersion) {
        this.optimisticLockVersion = optimisticLockVersion;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
}
