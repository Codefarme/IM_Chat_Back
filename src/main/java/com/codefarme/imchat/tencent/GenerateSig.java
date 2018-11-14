package com.codefarme.imchat.tencent;


import com.codefarme.imchat.config.ZxfConstans;
import com.codefarme.imchat.utils.PropertiesUtil;
import com.tls.sigcheck.tls_sigcheck;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

// 由于生成 sig 和校验 sig 的接口使用方法类似，故此处只是演示了生成 sig 的接口调用

// 使用的编译命令是
// javac -encoding utf-8 GenerateSig.java
// 使用的运行命令是
// java GenerateSig

public class GenerateSig {

    public static  String SDK_APPID;

    static {
        Properties properties = PropertiesUtil.getDefaultProperties();

        SDK_APPID = properties.getProperty("tencent.im.appid");
    }

    public static Map<String,Object> generateSigByAccount(String account) throws Exception {

        tls_sigcheck demo = new tls_sigcheck();
        // 使用前请修改动态库的加载路径
       demo.loadJniLib(ZxfConstans.JNISIGCHECK_PATH);

        File priKeyFile = new File(ZxfConstans.EC_KEY_PATH);
        StringBuilder strBuilder = new StringBuilder();
        String s = "";
        Map<String,Object> map = new HashMap<>();


        BufferedReader br = new BufferedReader(new FileReader(priKeyFile));
        while ((s = br.readLine()) != null) {
            strBuilder.append(s + '\n');
        }
        br.close();
        String priKey = strBuilder.toString();
		int ret = demo.tls_gen_signature_ex2(SDK_APPID,account, priKey);

        if (0 != ret) {
            System.out.println("ret " + ret + " " + demo.getErrMsg());
        }
        else
        {
            //把sig字符串添加到Map集合中，只要map不为空，并且sig也不为空，就代表sig生成成功
            String sig = demo.getSig();
            System.out.println("sig:\n" + sig);
            map.put("sig",sig);
        }

        File pubKeyFile = new File(ZxfConstans.PUBLIC_PATH);
        br = new BufferedReader(new FileReader(pubKeyFile));
		strBuilder.setLength(0);
        while ((s = br.readLine()) != null) {
            strBuilder.append(s + '\n');
        }
        br.close();
        String pubKey = strBuilder.toString();
		ret = demo.tls_check_signature_ex2(demo.getSig(), pubKey, SDK_APPID, account);
        if (0 != ret) {
            System.out.println("ret " + ret + " " + demo.getErrMsg());
        }
        else
        {
            int init =  demo.getInitTime();
            int expire =  demo.getExpireTime();
            map.put("init",init);
            map.put("expire",expire);
            System.out.println("--\nverify ok -- expire time " + expire + " -- init time " + init );
        }


        return map;

    }
}
