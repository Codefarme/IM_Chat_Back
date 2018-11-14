package com.codefarme.imchat.pojo;

import java.io.Serializable;

public class Bottles implements Serializable {
    private Integer id;

    private String account;

    private String bottletext;

    private String addtime;

    private String status;

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

    public String getBottletext() {
        return bottletext;
    }

    public void setBottletext(String bottletext) {
        this.bottletext = bottletext == null ? null : bottletext.trim();
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime == null ? null : addtime.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}