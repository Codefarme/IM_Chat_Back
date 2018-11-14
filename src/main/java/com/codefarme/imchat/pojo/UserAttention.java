package com.codefarme.imchat.pojo;

import java.io.Serializable;

public class UserAttention implements Serializable {
    private Integer id;

    private String account;

    private String attentionAccount;

    private String attentionTime;

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

    public String getAttentionAccount() {
        return attentionAccount;
    }

    public void setAttentionAccount(String attentionAccount) {
        this.attentionAccount = attentionAccount == null ? null : attentionAccount.trim();
    }

    public String getAttentionTime() {
        return attentionTime;
    }

    public void setAttentionTime(String attentionTime) {
        this.attentionTime = attentionTime == null ? null : attentionTime.trim();
    }
}