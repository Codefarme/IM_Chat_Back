package com.codefarme.imchat.limit;


import com.codefarme.imchat.config.ZxfConstans;
import com.codefarme.imchat.pojo.UserVip;
import com.codefarme.imchat.service.UserVipService;
import com.codefarme.imchat.utils.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@Aspect
@Component
public class RequestLimitContract {
    private static final Logger logger = LoggerFactory.getLogger("RequestLimitLogger");

    @Autowired
    private UserVipService userVipService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Before("within(@org.springframework.web.bind.annotation.RestController *) && @annotation(limit)")
    public void requestLimit(final JoinPoint joinPoint, RequestLimit limit) throws RequestLimitException {

        logger.info("走了");
        System.out.println("走了");
        try {
            Object[] args = joinPoint.getArgs();
            HttpServletRequest request = null;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof HttpServletRequest) {
                    request = (HttpServletRequest) args[i];
                    break;
                }
            }
            if (request == null) {
                throw new RequestLimitException("方法中缺失HttpServletRequest参数");
            }
            String account = request.getParameter("account");//获取请求的账号
            System.out.println("account:"+account);
            String requestURI = request.getRequestURI();

            //TODO 获取请求接口的URL 根据不同的请求接口配置不同的限制策略
            String inter = requestURI.substring(requestURI.lastIndexOf("/")+1);
            String key = "req_limit_".concat(inter+"_").concat(account);


            long count = redisTemplate.opsForValue().increment(key, 1);//
            if (count == 1) {

                //当天时间
                Date date = getExpireDate();
                redisTemplate.expireAt(key, date);//零点过期
            }


            //VIP用户不做限制，普通用户接口做限制
            UserVip vipUser = userVipService.getVipByAccount(account);
            if (vipUser == null) {
                //普通用户
                if (count > limit.count()) {//用户请求已经超过了次数
                    logger.info("用户账号[" + account + "]超过了限定的次数[" + limit.count() + "]");
                    request.setAttribute(ZxfConstans.ISREQUEST, ZxfConstans.REQ_FORBIDDEN);
                }

                return;
            } else {
                String fishtime = vipUser.getFishtime();
                String currentTime = DateUtil.getCurrentTime();
                long value = DateUtil.getBetweenDate(fishtime, currentTime);
                if (value > 0) {//代表当前用户属于普通用户
                    if (count > limit.count()) {//用户请求已经超过了次数
                        logger.info("用户账号[" + account + "]超过了限定的次数[" + limit.count() + "]");
                        request.setAttribute(ZxfConstans.ISREQUEST, ZxfConstans.REQ_FORBIDDEN);
                    }
                }
            }


        } catch (RequestLimitException e) {
            throw e;
        } catch (Exception e) {
            logger.error("发生异常: ", e);
        }
    }

    private Date getExpireDate() {
        Date date = new Date();
        //当天零点
        date = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        //第二天零点
        date = DateUtils.addDays(date, +1);
        return date;
    }


    //获取当前小时距离24点的时间的毫秒值
    public static long getExpireTime() {

        int hourOfDay = Calendar.HOUR_OF_DAY;//获取当前的小时数

        int hour = 24 - hourOfDay;

        return getHourTime(hour);
    }

    public static long getHourTime(int hour) {
        return hour * 3600000;
    }


    public static void main(String[] args) {

        Jedis jedis = new Jedis("154.8.168.183", 6379);
        System.out.println(jedis.ping());
        Transaction t = jedis.multi();
        t.exec();

    }
}