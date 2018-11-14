package com.codefarme.imchat.pojo;

import java.io.Serializable;

public class DynamicComment implements Serializable {
    private Integer id;

    private Integer cid;

    private String account;

    private String dynamicAccount;

    private String commentType;

    private String commentContent;

    private String addTime;

    private Integer status;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getDynamicAccount() {
        return dynamicAccount;
    }

    public void setDynamicAccount(String dynamicAccount) {
        this.dynamicAccount = dynamicAccount == null ? null : dynamicAccount.trim();
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType == null ? null : commentType.trim();
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent == null ? null : commentContent.trim();
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime == null ? null : addTime.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}