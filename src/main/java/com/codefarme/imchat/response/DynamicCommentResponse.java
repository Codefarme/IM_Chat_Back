package com.codefarme.imchat.response;

import com.codefarme.imchat.pojo.DynamicComment;
import com.codefarme.imchat.pojo.UserInfo;

import java.io.Serializable;

public class DynamicCommentResponse implements Serializable {

    public DynamicComment comment;

    public UserInfo userInfo;

    public String dynamicContent;
}
