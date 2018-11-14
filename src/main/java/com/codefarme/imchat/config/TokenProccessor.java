package com.codefarme.imchat.config;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 生成Token的工具类
 * @author zhous
 * @since 2018-2-23 13:59:27
 *
 */
public class TokenProccessor {

    private TokenProccessor(){};
    private static final TokenProccessor instance = new TokenProccessor();

    public static TokenProccessor getInstance() {
        return instance;
    }

    /**
     * 生成Token
     * @return
     */
    public String makeToken() {
        String token = (System.currentTimeMillis() + new Random().nextInt(999999999)) + "";
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte md5[] =  md.digest(token.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(md5);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}