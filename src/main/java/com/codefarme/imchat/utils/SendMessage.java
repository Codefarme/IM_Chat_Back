package com.codefarme.imchat.utils;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;

import java.io.IOException;
import java.util.Properties;

public class SendMessage {

    static int appid; //  短信应用SDK AppID 1400开头

    static String appkey;    // 短信应用SDK AppKey

    static int templateId; // 短信模板ID，需要在短信应用中申请 需要在短信控制台中申请

    static String smsSign; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`


    static {
        Properties properties = PropertiesUtil.getDefaultProperties();

        appid = Integer.parseInt(properties.getProperty("tencent.appid"));
        templateId = Integer.parseInt(properties.getProperty("tencent.templateId"));
        appkey = properties.getProperty("tencent.appkey");
//        smsSign = properties.getProperty("tencent.smsSign");
        smsSign = "";
    }

    /*
     * 发送短信方法
     */
    public static int sendSMS(String number, String[] phoneNumbers) {
        SmsSingleSenderResult result = null;
        try {
            String[] params = {number, "5"};
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            result = ssender.sendWithParam("86", phoneNumbers[0],
                    templateId, params, smsSign, "", "");
            System.out.println(result);
        } catch (HTTPException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }

        return result.result;

    }

}
