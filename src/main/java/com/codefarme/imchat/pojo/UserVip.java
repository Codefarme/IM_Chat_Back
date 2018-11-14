package com.codefarme.imchat.pojo;

import java.io.Serializable;

public class UserVip implements Serializable {
    private Integer id;

    private String account;

    private String vipstate;

    private String starttime;

    private String fishtime;

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

    public String getVipstate() {
        return vipstate;
    }

    public void setVipstate(String vipstate) {
        this.vipstate = vipstate == null ? null : vipstate.trim();
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime == null ? null : starttime.trim();
    }

    public String getFishtime() {
        return fishtime;
    }

    public void setFishtime(String fishtime) {
        this.fishtime = fishtime == null ? null : fishtime.trim();
    }
}