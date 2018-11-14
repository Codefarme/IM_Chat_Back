package com.codefarme.imchat.pojo;

import java.io.Serializable;

public class UserSmsMsg implements Serializable {
    private Integer id;

    private String account;

    private String securityCode;

    private String time;

    private String timeLimit;

    private Integer todyCount;

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

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode == null ? null : securityCode.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit == null ? null : timeLimit.trim();
    }

    public Integer getTodyCount() {
        return todyCount;
    }

    public void setTodyCount(Integer todyCount) {
        this.todyCount = todyCount;
    }
}