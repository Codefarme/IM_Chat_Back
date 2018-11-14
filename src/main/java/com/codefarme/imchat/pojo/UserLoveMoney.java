package com.codefarme.imchat.pojo;

import java.io.Serializable;

public class UserLoveMoney implements Serializable {
    private Integer id;

    private String account;

    private Integer loveMoney;

    private Integer num;

    private String updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public Integer getLoveMoney() {
        return loveMoney;
    }

    public void setLoveMoney(Integer loveMoney) {
        this.loveMoney = loveMoney;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}