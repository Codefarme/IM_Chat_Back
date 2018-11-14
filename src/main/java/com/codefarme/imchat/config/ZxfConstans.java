package com.codefarme.imchat.config;

/**
 * Created by Administrator on 2018/9/1/001.
 */

public class ZxfConstans {

    public static final String DATA_TYPE = "type";
    public static final String DATA_MSM = "msm";
    public static final String DATA_ACCOUNT = "account";
    public static final String DATA_PHONE = "phone";


    //windows平台的存放路径
   /* public static final String EC_KEY_PATH = "C:\\Users\\Administrator\\Desktop\\windows\\ec_key.pem";
    public static final String PUBLIC_PATH = "C:\\Users\\Administrator\\Desktop\\windows\\public.pem";
    public static final String JNISIGCHECK_PATH = "C:\\Users\\Administrator\\Desktop\\windows\\jnisigcheck.dll";*/

    //线上平台的地址
    public static final String EC_KEY_PATH = "/usr/local/java/tencent/ec_key.pem";
    public static final String PUBLIC_PATH = "/usr/local/java/tencent/public.pem";
    public static final String JNISIGCHECK_PATH = "/usr/local/java/tencent/jnisigcheck.so";



    public static final String ISREQUEST = "isrequest";//是否禁止请求对应url
    public static final int REQ_FORBIDDEN = 3;//请求次数超过限制 禁止再请求




    //=================普通用户请求限制的常量========================
    public static final int PUBLISH = 3;//24小时内最多可以发表3次
    public static final int PICKUPBOTTLES = 10;//24小时内捡瓶子次数
    public static final int ADD_FRIEND_COUNT = 3;//添加好友的次数



    //==================浏览足迹=============================================================
    public static final Integer IS_VIP = 2000;//当前用户是VIP
    public static final Integer NOT_VIP = 2001;//当前用户不是VIP

    public static final Integer IS_VIP_AND_NOT_QUALITY = 2003;//是VIP但是信用分小于80

    public static final Integer IS_VIP_AND_IS_QUALITY = 2004;//是VIP并且信用分大于80



}
