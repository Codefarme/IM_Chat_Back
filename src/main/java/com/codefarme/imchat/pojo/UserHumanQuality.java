package com.codefarme.imchat.pojo;

import java.io.Serializable;

public class UserHumanQuality implements Serializable {
    private Integer id;

    private String account;

    private Integer humanQuality;

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

    public Integer getHumanQuality() {
        return humanQuality;
    }

    public void setHumanQuality(Integer humanQuality) {
        this.humanQuality = humanQuality;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }
}