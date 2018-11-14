package com.codefarme.imchat.service.impl;


import com.alipay.api.internal.util.AlipaySignature;
import com.codefarme.imchat.mapper.FlowMapper;
import com.codefarme.imchat.mapper.OrdersMapper;
import com.codefarme.imchat.mapper.UserOrderMapper;
import com.codefarme.imchat.service.OrdersService;
import com.codefarme.imchat.alipay.AlipayConfig;
import com.codefarme.imchat.alipay.AlipayUtil;
import com.codefarme.imchat.alipay.OrderStatusEnum;
import com.codefarme.imchat.pojo.Flow;
import com.codefarme.imchat.pojo.Orders;
import com.codefarme.imchat.pojo.Product;
import com.codefarme.imchat.pojo.UserOrder;
import com.codefarme.imchat.utils.DateUtil;
import net.sf.json.JSONObject;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserOrderMapper userOrderMapper;//用户和订单的关联表

    @Autowired
    private FlowMapper flowMapper;

    @Autowired
    private Sid sid;




    @Override
    public Orders selectUserByOutTrade(String out_trade_no) {
        return ordersMapper.selectUserByOutTrade(out_trade_no);
    }


    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public Map<String, Object> saveOrder(Product product, Orders order, String account) throws Exception {

        ordersMapper.insert(order);
        String id = order.getId();
        UserOrder userOrder = new UserOrder();
        userOrder.setOrderNum(order.getId());
        userOrder.setOrderUser(account);
        userOrder.setProductId(product.getId());
        userOrder.setProductType(product.getType());
        userOrderMapper.insert(userOrder);

        return payment(order, product.getName());
    }

    @Override
    public Orders getOrderById(String orderId) {
        return ordersMapper.selectByPrimaryKey(orderId);
    }




    @Override
    public void updateOrderStatus(String orderId, String alpayFlowNum, String paidAmount) {

        Orders order = getOrderById(orderId);
        if (order.getOrderStatus().equals(OrderStatusEnum.WAIT_PAY.key)) {
            order = new Orders();
            order.setId(orderId);
            order.setOrderStatus(OrderStatusEnum.PAID.key);
            order.setPaidTime(new Date());
            order.setPaidAmount(paidAmount);

            ordersMapper.updateByPrimaryKeySelective(order);

            order = getOrderById(orderId);

            String flowId = sid.nextShort();
            Flow flow = new Flow();
            flow.setId(flowId);
            flow.setFlowNum(alpayFlowNum);
            flow.setBuyCounts(order.getBuyCounts());
            flow.setCreateTime(new Date());
            flow.setOrderNum(orderId);
            flow.setPaidAmount(paidAmount);
            flow.setPaidMethod(1);
            flow.setProductId(order.getProductId());

            flowMapper.insertSelective(flow);
        }

    }


    /**
     * 封装信息请求支付[真正的调用支付宝接口返回给前端展示的方法]
     *
     * @param order
     * @param
     * @return
     * @throws Exception
     */
    private Map<String, Object> payment(Orders order, String  productName) throws Exception {

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = order.getId();
        //付款金额，必填
        String total_amount = order.getOrderAmount();
        //订单名称，必填
        String subject = productName;
        //商品描述，可空
        String body = "";

        // 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
        String timeout_express = "1c";


        ////////////////////////====================
        String nowTime = DateUtil.getCurrentTime();       //当前时间
        //公共参数:
        Map<String, String> publicParam = new HashMap<String, String>();
        publicParam.put("app_id", AlipayConfig.APP_ID);      //支付宝分配给开发者的应用ID
        publicParam.put("method", "alipay.trade.app.pay");   // 接口名称
        publicParam.put("format", "json");
        publicParam.put("charset", AlipayConfig.CHARSET);
        publicParam.put("sign_type", AlipayConfig.SIGN_TYPE); //商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
        publicParam.put("timestamp", nowTime);
        publicParam.put("version", "1.0");  //调用的接口版本，固定为：1.0
        publicParam.put("notify_url", AlipayConfig.NOTIFY_URL);//支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https

        //业务参数:
        Map<String, String> payParam = new HashMap<String, String>();
        payParam.put("body", body);       //对一笔交易的具体描述信息
        payParam.put("subject", subject); //商品的标题/交易标题/订单标题/订单关键字等。
        payParam.put("out_trade_no", out_trade_no);    //商户网站唯一订单号
        payParam.put("timeout_express", timeout_express);   //该笔订单允许的最晚付款时间，逾期将关闭交易
        payParam.put("total_amount", total_amount);//订单总金额，单位为元，精确到小数点后两位
//        payParam.put("seller_id", AlipayConfig.SELLER_ID);  //收款支付宝用户ID。 如果该值为空，则默认为商户签约账号对应的支付宝用户ID
        payParam.put("product_code", "QUICK_MSECURITY_PAY");//销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY


        JSONObject bizcontentJson = JSONObject.fromObject(payParam);
        System.out.println("支付宝业务参数==" + bizcontentJson);

        //业务请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
        publicParam.put("biz_content", bizcontentJson.toString());
        System.out.println("支付宝请求参数==" + publicParam);

        //RSA签名
        String rsaSign = AlipayUtil.rsaSign(publicParam);
        System.out.println("支付宝签名参数==" + rsaSign);

        Map<String, String> codeParam = new HashMap<String, String>();
        codeParam.put("app_id", AlipayConfig.APP_ID);
        codeParam.put("method", "alipay.trade.app.pay");
        codeParam.put("format", "json");
        codeParam.put("charset", AlipayConfig.CHARSET);
        codeParam.put("sign_type", AlipayConfig.SIGN_TYPE);
        codeParam.put("timestamp", URLEncoder.encode(nowTime, "UTF-8"));
        codeParam.put("version", "1.0");
        codeParam.put("notify_url", URLEncoder.encode(AlipayConfig.NOTIFY_URL, "UTF-8"));  //通知接口

        //最后对请求字符串的所有一级value（biz_content作为一个value）进行encode，编码格式按请求串中的charset为准，没传charset按UTF-8处理
        codeParam.put("biz_content", URLEncoder.encode(bizcontentJson.toString(), "UTF-8"));
        String data = AlipaySignature.getSignContent(codeParam);   //拼接后的字符串
        data = data + "&sign=" + URLEncoder.encode(rsaSign, "UTF-8");
        System.out.println("支付宝支付参数==" + data);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("data", data);

        return dataMap; //返回给前端处理
    }

}
