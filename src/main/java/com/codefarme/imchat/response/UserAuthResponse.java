package com.codefarme.imchat.response;

import com.codefarme.imchat.pojo.UserAuth;

import java.io.Serializable;

public class UserAuthResponse extends UserAuth implements Serializable {
    private String username;

    private String avatar;
    private String sign;
    private String sex;
    private String height;
    private String weight;
    private String province;
    private String city;
    private Integer ethnicity;
    private String loginTime;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(Integer ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}
