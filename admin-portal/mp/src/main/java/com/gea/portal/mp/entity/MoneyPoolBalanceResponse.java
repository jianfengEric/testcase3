package com.gea.portal.mp.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Eric on 2018/11/9.
 */
public class MoneyPoolBalanceResponse {
    private List<MoneyPoolBalanceEntry> moneyPoolBalances;

    public List<MoneyPoolBalanceEntry> getMoneyPoolBalances() {
        return moneyPoolBalances;
    }

    public void setMoneyPoolBalances(List<MoneyPoolBalanceEntry> moneyPoolBalances) {
        this.moneyPoolBalances = moneyPoolBalances;
    }

    public static class MoneyPoolBalanceEntry {
        private String moneyPoolRefId;
        private BigDecimal balance;
        private String lastUpdateTime;

        public String getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(String lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }

        public String getMoneyPoolRefId() {
            return moneyPoolRefId;
        }

        public void setMoneyPoolRefId(String moneyPoolRefId) {
            this.moneyPoolRefId = moneyPoolRefId;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }
    }
}
