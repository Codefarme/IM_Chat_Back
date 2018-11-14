package com.codefarme.imchat.response;

import java.io.Serializable;

public class CommonMessage implements Serializable {
    private String likeAccount;//点赞人的账号
    private String likeNickName;//点赞人的昵称
    private String likeHeadImg;//点赞人的头像地址
    private String likeCid;//点赞帖子的id()

    public String getLikeAccount() {
        return likeAccount;
    }

    public void setLikeAccount(String likeAccount) {
        this.likeAccount = likeAccount;
    }

    public String getLikeNickName() {
        return likeNickName;
    }

    public void setLikeNickName(String likeNickName) {
        this.likeNickName = likeNickName;
    }

    public String getLikeHeadImg() {
        return likeHeadImg;
    }

    public void setLikeHeadImg(String likeHeadImg) {
        this.likeHeadImg = likeHeadImg;
    }

    public String getLikeCid() {
        return likeCid;
    }

    public void setLikeCid(String likeCid) {
        this.likeCid = likeCid;
    }

    @Override
    public String toString() {
        return "CommonMessage{" +
                "likeAccount='" + likeAccount + '\'' +
                ", likeNickName='" + likeNickName + '\'' +
                ", likeHeadImg='" + likeHeadImg + '\'' +
                ", likeCid='" + likeCid + '\'' +
                '}';
    }
}
