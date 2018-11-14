package com.codefarme.imchat.response;

import com.codefarme.imchat.pojo.DynamicPraise;
import com.codefarme.imchat.pojo.UserInfo;

import java.io.Serializable;

public class DynamicPraiseResponse implements Serializable {
    public DynamicPraise praise;

    public UserInfo userInfo;

    public String dynamicContent;

    public static final long serialVersionUID = 1L;


}