package com.codefarme.imchat.tencent;

import com.codefarme.imchat.utils.PropertiesUtil;
import com.tencent.xinge.Message;
import com.tencent.xinge.XingeApp;
import org.json.JSONObject;

import java.util.Properties;

public class XGUtils {


    private static XingeApp xinge;

    private static final String CONFIG_AD = "tencent.accessId";
    private static final String CONFIG_SK = "tencent.secretKey";

    static {
        Properties properties = PropertiesUtil.getDefaultProperties();

        xinge = new XingeApp(Long.valueOf(properties.getProperty(CONFIG_AD)), properties.getProperty(CONFIG_SK));
    }


    /**
     * @param title
     * @param content
     * @param exPireTime 单位秒
     * @param token
     * @return
     */
    public static JSONObject pushSingleDeviceMessage(String title, String content, int exPireTime, String token) {
        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        message.setType(Message.TYPE_MESSAGE);
        message.setExpireTime(exPireTime);
        JSONObject ret = xinge.pushSingleDevice(token, message);

        return ret;
    }

    //下发单个账号
    public static JSONObject pushSingleAccount(String title, String content, int exPireTime, String account) {
        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        message.setType(Message.TYPE_MESSAGE);
        message.setExpireTime(exPireTime);
        JSONObject ret = xinge.pushSingleAccount(0, account, message);
        return ret;
    }
}
