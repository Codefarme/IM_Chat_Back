package com.codefarme.imchat.oss;


import com.aliyun.oss.OSSClient;
import com.codefarme.imchat.utils.PropertiesUtil;

import java.io.ByteArrayInputStream;
import java.util.Properties;

public class OSUtils {
    private static final String cdns = "https://zxf-an.oss-cn-qingdao.aliyuncs.com/";

    private static String ak;

    private static String sk;

    private static OSSClient ossClient;

    private static String bucketName = "zxf-an";
    private static final String endpoint = "http://oss-cn-qingdao.aliyuncs.com";


    private static final String CONFIG_AK="oss.accesskey";
    private static final String CONFIG_SK="oss.secretkey";

    static {
        Properties properties = PropertiesUtil.getDefaultProperties();
        ak = properties.getProperty(CONFIG_AK);
        sk = properties.getProperty(CONFIG_SK);

    }

    /**
     * @param buff
     * @param isImage 上传的文件是否是图片
     * @return
     */
    public static String upload(byte[] buff, boolean isImage, String account) {

        ossClient = new OSSClient(endpoint, ak, sk);

        String key = KeyGenerator.generateOSKey().concat("-" + account);
        if (isImage) {
            key = key.concat(".jpg");
        }
        ossClient.putObject(bucketName, key, new ByteArrayInputStream(buff));
        ossClient.shutdown();

        return key;
    }

    /**
     * 获取图片链接
     *
     * @param key
     * @return
     */
    public static String getUrl(String key) {

        return cdns.concat(key);
    }
}
