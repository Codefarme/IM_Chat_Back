package com.codefarme.imchat.pojo;

import java.io.Serializable;

public class UserBrowse implements Serializable {
    private Integer id;

    private String account;

    private String browseAccount;

    private String browseTime;

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

    public String getBrowseAccount() {
        return browseAccount;
    }

    public void setBrowseAccount(String browseAccount) {
        this.browseAccount = browseAccount == null ? null : browseAccount.trim();
    }

    public String getBrowseTime() {
        return browseTime;
    }

    public void setBrowseTime(String browseTime) {
        this.browseTime = browseTime == null ? null : browseTime.trim();
    }
}