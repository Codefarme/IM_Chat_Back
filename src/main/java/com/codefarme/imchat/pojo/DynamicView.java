package com.codefarme.imchat.pojo;

import java.io.Serializable;

public class DynamicView implements Serializable {
    private Integer id;

    private Integer cid;

    private String viewAccount;

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

    public String getViewAccount() {
        return viewAccount;
    }

    public void setViewAccount(String viewAccount) {
        this.viewAccount = viewAccount == null ? null : viewAccount.trim();
    }
}