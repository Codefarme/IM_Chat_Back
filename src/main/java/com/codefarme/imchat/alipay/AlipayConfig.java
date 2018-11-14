package com.codefarme.imchat.alipay;

/**
 * 说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 * 商户配置文件:
 */
public class AlipayConfig {

    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://openhome.alipay.com/platform/keyManage.htm?keyType=partner
     */
    public static final String SELLER_ID = "你的SELLER_ID";

    /**
     * 商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
     */
    public static final String PRIVATE_KEY = "你的PRIVATE_KEY";

    /**
     * 支付宝的公钥，查看地址：https://openhome.alipay.com/platform/keyManage.htm?keyType=partner
     */
    public static final String ALIPAY_PUBLIC_KEY = "你的ALIPAY_PUBLIC_KEY";

    /**
     * 签名方式
     */
    public static final String SIGN_TYPE = "RSA2";
    /**
     * 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法
     */
    public static final String LOG_PATH = "/usr/local/java/tomcat8/";
    /**
     * 字符编码格式 目前支持 gbk 或 utf-8
     */
    public static final String CHARSET = "utf-8";
    /**
     * 接收通知的接口名(回调地址)
     */
    public static final String NOTIFY_URL = "你的NOTIFY_URL";
    /**
     * APPID
     */
    public static final String APP_ID = "你的APP_ID";

    //↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

}