package com.gea.portal.mp.entity;

import com.tng.portal.common.enumeration.MoneyPoolStatus;
import com.tng.portal.common.enumeration.Instance;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Entity
@Table(name = "ewp_pool_adjustment")
public class EwpPoolAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "money_pool_id")
    private EwpMoneyPool ewpMoneyPool;

    @Column(name = "adjust_type")
    private Integer adjustType;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MoneyPoolStatus status;

    @Column(name = "current_envir")
    @Enumerated(EnumType.STRING)
    private Instance currentEnvir;
    
    @Column(name = "gea_tx_ref_no")
    private String geaTxRefNo;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EwpMoneyPool getEwpMoneyPool() {
        return ewpMoneyPool;
    }

    public void setEwpMoneyPool(EwpMoneyPool ewpMoneyPool) {
        this.ewpMoneyPool = ewpMoneyPool;
    }

    public Integer getAdjustType() {
        return adjustType;
    }

    public void setAdjustType(Integer adjustType) {
        this.adjustType = adjustType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public MoneyPoolStatus getStatus() {
        return status;
    }

    public void setStatus(MoneyPoolStatus status) {
        this.status = status;
    }

    public Instance getCurrentEnvir() {
        return currentEnvir;
    }

    public void setCurrentEnvir(Instance currentEnvir) {
        this.currentEnvir = currentEnvir;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

	public String getGeaTxRefNo() {
		return geaTxRefNo;
	}

	public void setGeaTxRefNo(String geaTxRefNo) {
		this.geaTxRefNo = geaTxRefNo;
	}
}
