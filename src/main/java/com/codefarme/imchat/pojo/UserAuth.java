package com.codefarme.imchat.pojo;

import java.io.Serializable;

public class UserAuth implements Serializable {
    private Integer id;

    private String account;

    private String authAvatar;

    private String authVideo;

    private String authVideoThumb;

    private String addTime;

    private Integer status;

    private String failureReason;

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

    public String getAuthAvatar() {
        return authAvatar;
    }

    public void setAuthAvatar(String authAvatar) {
        this.authAvatar = authAvatar == null ? null : authAvatar.trim();
    }

    public String getAuthVideo() {
        return authVideo;
    }

    public void setAuthVideo(String authVideo) {
        this.authVideo = authVideo == null ? null : authVideo.trim();
    }

    public String getAuthVideoThumb() {
        return authVideoThumb;
    }

    public void setAuthVideoThumb(String authVideoThumb) {
        this.authVideoThumb = authVideoThumb == null ? null : authVideoThumb.trim();
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

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason == null ? null : failureReason.trim();
    }
}