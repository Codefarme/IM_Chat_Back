package com.codefarme.imchat.utils;



import com.codefarme.imchat.config.ZxfConstans;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import net.sf.json.JSONObject;
/**
 * 通用的方法
 *
 * @author Administrator
 */
public class CommonUtils {

    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");


    /**
     * 校验用户对于该接口是否有权限
     *
     * @param request
     * @return false 没权限
     */
    public static boolean checkPer(HttpServletRequest request) {
        Object object = request.getAttribute(ZxfConstans.ISREQUEST);
        if (null != object && (int) object == ZxfConstans.REQ_FORBIDDEN) {
            return false;
        } else {
            return true;
        }

    }


    /**
     * 生成随机数范围（A-Z,0-9,a-z）
     *
     * @param length 生成随机数的长度
     * @return
     */
    public static String getRandomString(int length) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int number = 0;
        for (int i = 0; i < length; i++) {
            number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成随机数范围（0-9）
     *
     * @param length 生成随机数的长度
     * @return
     */
    public static String getnumberString(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int number = 0;
        for (int i = 0; i < length; i++) {
            number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String addpath(HttpServletRequest request, String path) {
        //获取项目根目录地址
        String string = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        path = string + path;
        return path;

    }

    /**
     * 将实体类变为返回的字符串（object类型）
     *
     * @param object
     * @return
     */
    public static String getReturnString(Object object) {
        List<Object> list = new ArrayList<Object>();
        list.add(object);
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("list", list);
        System.out.println(jsonobject.toString());
        return jsonobject.toString();

    }

    /**
     * 将实体类变为返回的字符串(list类型)
     *
     * @param
     * @return
     */
    public static String getReturnStringbylist(List<Object> list) {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("list", list);
        System.out.println(jsonobject.toString());
        return jsonobject.toString();

    }

    /**
     * 将实体类变为返回的字符串(list类型)
     *
     * @param
     * @return
     */
    public static String getReturnStringbymap(Map<Object, Object> map) {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("Map", map);
        System.out.println(jsonobject.toString());
        return jsonobject.toString();

    }

    /**
     * 计算两个坐标间的距离
     *
     * @param
     * @param
     * @return
     */
    public static double latAndLnt(String bonusesplace, String publishplace) {
        DecimalFormat df = new DecimalFormat("######0.00");
        String[] latLng = bonusesplace.split(",");
        String[] latLng2 = publishplace.split(",");
        Double lat2 = Double.parseDouble(latLng[0]);
        Double lng2 = Double.parseDouble(latLng[1]);
        Double lat1 = Double.parseDouble(latLng[0]);
        Double lng1 = Double.parseDouble(latLng2[1]);
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double km = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        km = km * EARTH_RADIUS;
        String km2 = df.format(km);
        double d = Double.parseDouble(km2);
        return d;

    }

    //地球半径
    private static double EARTH_RADIUS = 6371.393;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算红包的剩余金额和本次抢到的钱数
     *
     * @param total 剩余金额
     * @param num   总个数
     * @param min   最小数值
     * @param i     第几个红包
     */
    public static double hongbao(double total, int num, double min, int i) {
        System.out.println("第几个红包" + i);
        double returnmoney = 0;
        if (num > i) {
            double safe_total = (total - (num - i) * min) / (num - i);
            double money = Math.random() * (safe_total - min) + min;
            BigDecimal money_bd = new BigDecimal(money);
            money = money_bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            total = total - money;
            BigDecimal total_bd = new BigDecimal(total);
            total = total_bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            System.out.println("第" + i + "个红包：" + money + ",余额为:" + total + "元");
            returnmoney = money;
        } else {
            System.out.println("进了吗");
            System.out.println("第" + i + "个红包：" + total + ",余额为:" + 0 + "元");
            returnmoney = total;
        }
        return returnmoney;
    }


    public static boolean isRequest(HttpServletRequest request) {
        Object o = request.getAttribute(ZxfConstans.ISREQUEST);
        if (o != null) {
            int code = (int) o;
            if (code == 3) {
                return false;//不允许再请求了
            }
        }
        return true;//允许请求
    }


    public static String getUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


}
