package com.banksystem.application.entity;

import java.time.Instant;

public class Transaction {
    private Long id;
    private Long userId; // User ID
    private String transactionType; // Transaction type: deposit, withdrawal, transfer
    private Long amount; // Transaction amount
    private Long balanceAfter; // Balance after transaction
    private String description; // Transaction description
    private Boolean deleted; // Logical delete flag
    private Long createBy;
    private Long updateBy;
    private Instant createTime;
    private Instant updateTime;

    // Constructors
    public Transaction() {
    }

    public Transaction(Long id, Long userId, String transactionType, Long amount, Long balanceAfter, String description, Boolean deleted, Long createBy, Long updateBy, Instant createTime, Instant updateTime) {
        this.id = id;
        this.userId = userId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.deleted = deleted;
        this.createBy = createBy;
        this.updateBy = updateBy;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Long balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", userId=" + userId +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", balanceAfter=" + balanceAfter +
                ", description='" + description + '\'' +
                ", deleted=" + deleted +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}