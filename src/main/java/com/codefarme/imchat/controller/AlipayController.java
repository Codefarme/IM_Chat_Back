package com.codefarme.imchat.controller;

import com.codefarme.imchat.alipay.AlipayUtil;
import com.codefarme.imchat.alipay.OrderStatusEnum;
import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.pojo.*;
import com.codefarme.imchat.service.*;
import com.codefarme.imchat.utils.DateUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>类  说  明: TODO
 * <p>创建时间: 2017年11月7日 上午9:40:54
 * <p>创  建  人: geYang
 **/
@RestController
@RequestMapping("/alipay")
public class AlipayController {
    @Autowired
    private ProductService productService;

    @Autowired
    private OrdersService orderService;

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private UserVipService vipUserSerivce;

    @Autowired
    private SystemService systemService;

    @Autowired
    private GiftService giftService;


    @Autowired
    private Sid sid;

    /* *//**
     * 把产品返回给前台,供前台UI显示商品的名称和价格
     *
     * @return
     * @throws Exception
     *//*
    @RequestMapping("/products.action")
    @ResponseBody
    public Result<Map<String, Object>> products(HttpServletRequest request) throws Exception {

        // 声明返回值
        Result<Map<String, Object>> rr;


        Map<String, Object> map = new HashMap<>();

        List<Product> pList = productService.getProducts();


        if (pList != null && !pList.isEmpty()) {
            Product product = pList.get(0);
            map.put("id", product.getId());
            map.put("name", product.getName());
            map.put("price", product.getPrice());
            rr = new Result<Map<String, Object>>(0, "ok", map);
        } else {
            rr = new Result<Map<String, Object>>(1, "failure", null);
        }

        return rr;
    }*/


    /**
     * 前台
     * 生成订单，返回给安卓端  【安卓端请求的方法】
     *
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public Result<Map<String, Object>> createOrde2r(HttpServletRequest request) {

        // 声明返回值
        Result<Map<String, Object>> rr;
        Map<String, Object> payment = null;
        try {
            String account = request.getParameter("account");
            String productId = request.getParameter("id");//id 1代表是会员产品

            Product p = productService.getProductById(productId);
            p.setType(1);//代表是会员充值服务

            System.out.println("开始请求支付" + account);
            //生成订单并且把封装好的支付宝参数返回给前端

            payment = createOrderAndPay(account, p, 1);

            if (payment == null) {
                rr = new Result<>(1, "failure", null);
            } else {
                rr = new Result<>(0, "ok", payment);
            }


        } catch (Exception e) {
            rr = new Result<>(1, "failure", null);
        }

        return rr;
    }

    /**
     * 前台
     * 生成购买礼物订单，返回给安卓端  【安卓端请求的方法】
     *
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/gift_pay", method = RequestMethod.POST)
    public Result<Map<String, Object>> createGiftOrder(HttpServletRequest request) {

        // 声明返回值
        Result<Map<String, Object>> rr;
        Map<String, Object> payment = null;
        try {
            String account = request.getParameter("account");
            String gid = request.getParameter("gid");//id 礼物的id
            String count = request.getParameter("count");//购买的数量


            System.out.println("开始请求支付" + account);

            SystemConfigGift gift = systemService.selectConfigGiftByGid(Integer.valueOf(gid));
            Product p = new Product();
            p.setId(gift.getId());
            p.setName(gift.getName() + "礼物购买业务");//展示在前端的订单名称
            p.setPrice(gift.getPrice());
            p.setType(2);//礼物的产品类型是2

            payment = createOrderAndPay(account, p, Integer.valueOf(count));

            if (payment == null) {
                rr = new Result<>(1, "failure", null);
            } else {
                rr = new Result<>(0, "ok", payment);
            }


        } catch (Exception e) {
            rr = new Result<>(1, "failure", null);
        }

        return rr;
    }

    /**
     * 前台
     * 生成C币充值订单，返回给安卓端  【安卓端请求的方法】
     *
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/love_money_pay", method = RequestMethod.POST)
    public Result<Map<String, Object>> createLoveMoneyOrder(HttpServletRequest request) {

        // 声明返回值
        Result<Map<String, Object>> rr;
        Map<String, Object> payment = null;
        try {
            //C币充值订单
            String account = request.getParameter("account");
            String amount = request.getParameter("amount");//充值的金额  1爱币=1人民币


            Integer payAmount = Integer.valueOf(amount);
            if (payAmount <= 0 || payAmount > 2000) {//最多充值2000  校验充值范围是否合理
                return new Result<>(1, "failure", null);
            }
            Product p = productService.getProductById("2");//代表是爱币
            p.setPrice(payAmount);//金额
            p.setType(3);//3代表产品类型是爱币
            payment = createOrderAndPay(account, p, payAmount);

            if (payment == null) {
                rr = new Result<>(1, "failure", null);
            } else {
                rr = new Result<>(0, "ok", payment);
            }


        } catch (Exception e) {
            rr = new Result<>(1, "failure", null);
        }

        return rr;
    }

    private Map<String, Object> createOrderAndPay(String account, Product p, Integer buyCounts) {
        Map<String, Object> payment = null;
        Orders order = new Orders();
        String orderId = sid.nextShort();
        order.setId(orderId);
        order.setOrderNum(orderId);
        order.setCreateTime(new Date());
        order.setBuyCounts(buyCounts);//购买的数量
        order.setOrderAmount(String.valueOf(Float.valueOf(p.getPrice()) * order.getBuyCounts()));
        order.setOrderStatus(OrderStatusEnum.WAIT_PAY.key);

        //订单表对应的产品id和产品类型，用于做付款成功后的业务区分
        order.setProductId(p.getId() + "");
        order.setProductType(p.getType());

        try {
            payment = orderService.saveOrder(p, order, account);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return payment;
    }


    /**
     * <p>方法说明: TODO 支付同步验证结果
     * <p>参数说明: String data 成功为9000
     * <p>参数说明: @return 支付状态  成功为1
     **/
    @RequestMapping(value = "/alipayVerification.action")
    @ResponseBody
    public Result<Map<String, Object>> alipayVerification(String data) {

        // 声明返回值
        Result<Map<String, Object>> responseResult;
        System.out.println("支付结果" + data);

        if ("9000".equals(data)) {
            responseResult = new Result<>(0, "支付成功", null);
            return responseResult;

        } else if ("8000".equals(data)) {
            responseResult = new Result<>(2, "正在处理中", null);
            return responseResult;

        } else if ("4000".equals(data)) {

            responseResult = new Result<>(3, "订单支付失败", null);
            return responseResult;


        } else if ("5000".equals(data)) {
            responseResult = new Result<>(4, "重复请求", null);
            return responseResult;

        } else if ("6001".equals(data)) {

            responseResult = new Result<>(5, "用户中途取消", null);
            return responseResult;

        } else if ("6002".equals(data)) {
            responseResult = new Result<>(6, "网络连接出错", null);
            return responseResult;

        } else if ("6004".equals(data)) {
            responseResult = new Result<>(7, "支付结果未知", null);
            return responseResult;

        } else {
            responseResult = new Result<>(8, "其他未知错误", null);
            return responseResult;
        }
    }


    /**
     * <p>方法说明: TODO 支付异步通知(支付宝后台的回调通知)
     * <p>返回说明: String SUCCESS/FAIL
     * <p>创建时间: 2017年10月31日 下午3:18:04
     * <p>创  建  人: geYang
     *
     * @throws Exception
     **/
    @RequestMapping(value = "/alipayNotify.action")
    @ResponseBody
    public String getPayNotify(HttpServletRequest request) throws Exception {
        System.out.println("==========  支付宝支付回调      ==========");
        //获取支付宝POST过来反馈信息
        Map<String, String> receiveMap = getReceiveMap(request);
        System.out.println("支付宝支付回调参数==" + receiveMap);
        // 获取支付宝的通知返回参数,可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)
        // 1,判断是否有异步通知
        if (receiveMap.get("notify_id") == null || receiveMap.get("notify_id").isEmpty()) {
            System.out.println("支付宝支付回调没有异步通知");
            return "no notify message";
        }
        //2,判断是否是支付宝发来的异步通知
        if (!AlipayUtil.verifyResponse(receiveMap.get("notify_id")).equals("true")) {
            System.out.println("支付宝支付回调通知错误");
            return ("response failure");
        }
        //3,判断是否是支付宝发来的异步通知
        if (!AlipayUtil.rsaCheck(receiveMap)) {
            System.out.println("支付宝支付回调验证签名失败");
            return "sign failure";
        }
        //4,判断交易状态
        String tradeStatus = receiveMap.get("trade_status");
        if ("TRADE_FINISHED".equals(tradeStatus) || "TRADE_SUCCESS".equals(tradeStatus)) {

            System.out.println("走到判断交易状态了");

            //这句话 报空指针不知道为什么
            // System.out.println("支付宝支付回调签名解码==" + URLDecoder.decode(receiveMap.get("sign"), "UTF-8"));


            String out_trade_no = receiveMap.get("out_trade_no");    // 商户订单号
            String trade_no = receiveMap.get("trade_no");//支付宝交易号
            String total_amount = receiveMap.get("total_amount");


            // 修改订单状态，改为 支付成功，已付款; 同时新增支付流水
            orderService.updateOrderStatus(out_trade_no, trade_no, total_amount);


            UserOrder userOrder = userOrderService.selectUserByOutTrade(out_trade_no);
            if (userOrder != null) {


                String account = userOrder.getOrderUser();
                //TODO 需要校验不同的产品
                if (userOrder.getProductType() == 1) {//VIP会员充值业务
                    //查询当前用户是否是VIP用户
                    UserVip vipUser = vipUserSerivce.getVipByAccount(account);
                    if (vipUser == null) {
                        // 非VIP用户
                        vipUser = new UserVip();
                        vipUser.setFishtime(DateUtil.getBackTime(30));
                        vipUser.setAccount(account);
                        vipUser.setStarttime(DateUtil.getCurrentDate());
                        vipUserSerivce.insert(vipUser);

                    } else {//之前是VIP用户

                        String fishtime = vipUser.getFishtime();
                        String currentTime = DateUtil.getCurrentTime();
                        long value = DateUtil.getBetweenDate(fishtime, currentTime);//当前时间减去会员结束时间

                        if (value == 0 || value > 0) {//表示今天是会员到期时间
                            vipUser.setFishtime(DateUtil.getBackTime(30));

                        } else {//代表用户|value|天后会员才到期
                            long abs = Math.abs(value);
                            vipUser.setFishtime(DateUtil.getBackTime((int) (30 + abs)));  //计算VIP结束时间和当前时间的差值天数，在差值天数的基础上再增加30天
                        }

                        vipUser.setAccount(account);
                        vipUser.setStarttime(DateUtil.getCurrentDate());
                        vipUserSerivce.updateByPrimaryKeySelective(vipUser);
                    }
                } else if (userOrder.getProductType() == 2) {//支付宝购买礼物业务[暂不支持]
                   /* //1.根据订单号在订单表查出对应的订单
                    Orders orders = orderService.selectUserByOutTrade(out_trade_no);

                    //2. 获取到礼物ID和对应的购买数量
                    String id = orders.getProductId();
                    Integer buyCounts = orders.getBuyCounts();


                    //3. 把对应的礼物ID和对应的购买数量增加到用户购买礼物记录表
                    //4. 再把用户拥有的礼物中寻找有没有该礼物，没有的话插入，有的话更新数量
                    giftService.upadeUserGift(account,id,buyCounts,out_trade_no);*/

                } else if (userOrder.getProductType() == 3) {//爱币充值业务
                    //1.根据订单号在订单表查出对应的订单
                    Orders orders = orderService.selectUserByOutTrade(out_trade_no);

                    //2. 获取到对应的购买金额
                    Integer buyCounts = orders.getBuyCounts();//这里的购买数量和爱币等值

                    //3. 再把从爱币表中寻找该用户，没有的话插入，有的话更新C币数量
                    giftService.upadeUserLoveMoney(account,buyCounts,out_trade_no);
                }

            }

            return "SUCCESS";
        }
        System.out.println("支付宝支付回调失败");
        return "failure";
    }


    /**
     * <p>方法说明: TODO 获取请求参数
     **/
    private static Map<String, String> getReceiveMap(HttpServletRequest request) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }


        return params;
    }


}