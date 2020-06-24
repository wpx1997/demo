package com.joinwe.demo.wechatapplet.wechatpay.config;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 小case
 * created on 2020/5/25 15:44
 */
@Data
public class UnifiedOrderConfig {

    /**
     * @author: 小case
     * @date: 2020/6/16 16:16
     * @description: 商户appId
     */
    private String appid;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:16
     * @description: 商户macId
     */
    private String mch_id;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:17
     * @description: 不长于32位的随机字符串
     */
    private String nonce_str;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:17
     * @description: 发起统一下单请求的参数签名
     */
    private String sign;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:18
     * @description: 订单说明
     */
    private String body;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:18
     * @description: 订单号，唯一
     */
    private String out_trade_no;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:19
     * @description: 订单金额，单位为分
     */
    private BigDecimal total_fee;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:19
     * @description: 终端ip
     */
    private String spbill_create_ip;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:20
     * @description: 接收微信支付结果通知的回调地址
     */
    private String notify_url;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:21
     * @description: 交易类型
     */
    private String trade_type;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:21
     * @description: 用户openId
     */
    private String openid;

}
