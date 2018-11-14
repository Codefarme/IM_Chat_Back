package com.codefarme.imchat.pojo;

import java.io.Serializable;

public class DynamicCommentReply implements Serializable {
    private Integer id;

    private Integer dynaId;

    private Integer cid;

    private String account;

    private String commentAccount;

    private String replyType;

    private String replyContent;

    private String addTime;

    private Integer status;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDynaId() {
        return dynaId;
    }

    public void setDynaId(Integer dynaId) {
        this.dynaId = dynaId;
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

    public String getCommentAccount() {
        return commentAccount;
    }

    public void setCommentAccount(String commentAccount) {
        this.commentAccount = commentAccount == null ? null : commentAccount.trim();
    }

    public String getReplyType() {
        return replyType;
    }

    public void setReplyType(String replyType) {
        this.replyType = replyType == null ? null : replyType.trim();
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent == null ? null : replyContent.trim();
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