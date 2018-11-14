package com.codefarme.imchat.pojo;

import java.io.Serializable;

public class DynamicShare implements Serializable {
    private Integer id;

    private Integer cid;

    private String shareAccount;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getShareAccount() {
        return shareAccount;
    }

    public void setShareAccount(String shareAccount) {
        this.shareAccount = shareAccount == null ? null : shareAccount.trim();
    }
}