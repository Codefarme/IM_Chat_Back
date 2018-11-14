package com.codefarme.imchat.alipay;


import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.methods.multipart.FilePartSource;
import org.apache.commons.httpclient.methods.multipart.PartSource;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 *<p>类  说  明: TODO
 *<p>创建时间: 2017年11月7日 上午9:38:02
 *<p>创  建  人: geYang
 **/
public class AlipayUtil {

    /**
     * 支付宝消息验证地址
     */
    private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

    /**
     *<p>方法说明: TODO 签名验证
     *<p>参数说明: @param params 通知返回来的参数数组
     *<p>参数说明: @param sign   比对的签名结果
     *<p>参数说明: @throws AlipayApiException
     *<p>返回说明: boolean 签名验证结果
     *<p>创建时间: 2017年11月1日 下午2:19:18
     *<p>创  建  人: geYang
     **/
    public static boolean rsaCheck(Map<String, String> params) throws AlipayApiException {
        return AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGN_TYPE);
    }

    /**
     *<p>方法说明: TODO 进行签名
     *<p>参数说明: @param params 需要签名的参数
     *<p>参数说明: @throws AlipayApiException
     *<p>返回说明: String 签名字符串
     *<p>创建时间: 2017年11月1日 下午2:29:01
     *<p>创  建  人: geYang
     **/
    public static String rsaSign (Map<String, String> params) throws AlipayApiException{
        String content = AlipaySignature.getSignCheckContentV2(params);
        return AlipaySignature.rsaSign(content, AlipayConfig.PRIVATE_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGN_TYPE);
    }

    /**
     *<p>方法说明: TODO 解密
     *<p>参数说明: @param params 密文参数
     *<p>参数说明: @throws AlipayApiException
     *<p>返回说明: String 解密字符串
     *<p>创建时间: 2017年11月1日 下午3:34:02
     *<p>创  建  人: geYang
     **/
    public static String rsaDecrypt(Map<String, String> params) throws AlipayApiException{
        return AlipaySignature.checkSignAndDecrypt(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.PRIVATE_KEY, true, true, AlipayConfig.SIGN_TYPE);
    }
    /**
     * 获取远程服务器ATN结果,验证返回URL
     *
     * @param notifyId 通知校验ID
     * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
     *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    public static String verifyResponse(String notifyId) {
        // 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
        String urlValue = HTTPS_VERIFY_URL + "partner=" + AlipayConfig.SELLER_ID + "&notify_id=" + notifyId;
        return checkUrl(urlValue);
    }

    /**
     * 获取远程服务器ATN结果
     *
     * @param urlValue 指定URL路径地址
     * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
     *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    private static String checkUrl(String urlValue) {
        String inputLine = "";
        try {
            URL url = new URL(urlValue);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            inputLine = in.readLine().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputLine;
    }

    /**
     *<p>方法说明: TODO 获取支付宝回调地址
     *<p>创建时间: 2017年11月2日 下午1:21:05
     *<p>创  建  人: geYang
     **/
    public static String getNotifyUrl(HttpServletRequest request){
        StringBuffer requestURL = request.getRequestURL();
        return requestURL.substring(0,requestURL.indexOf("/front/"))+AlipayConfig.NOTIFY_URL;
    }

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param word 要写入日志里的文本内容
     */
    public static void logResult(String word) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(AlipayConfig.LOG_PATH + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(word);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 生成文件摘要
     * @param strFilePath 文件路径
     * @param fileDigestType 摘要算法
     * @return 文件摘要结果
     */
    public static String getAbstract(String strFilePath, String fileDigestType) throws IOException {
        PartSource file = new FilePartSource(new File(strFilePath));
        if("MD5".equals(fileDigestType)){
            return DigestUtils.md5Hex(file.createInputStream());
        }
        else if("SHA".equals(fileDigestType)) {
            return DigestUtils.sha256Hex(file.createInputStream());
        }
        else {
            return "";
        }
    }






}