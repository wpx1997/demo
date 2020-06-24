package com.joinwe.demo.wechatapplet.wechatpay.config;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 小case
 * created on 2020/6/3 11:50
 */
@Data
public class PayRefundConfig {

    /**
     * @author: 小case
     * @date: 2020/6/16 16:09
     * @description: 公众号appId
     */
    private String appid;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:10
     * @description: 微信支付分配的商户号
     */
    private String mch_id;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:10
     * @description: 不长于32位的随机字符串
     */
    private String nonce_str;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:10
     * @description: 发起订单退款申请的签名
     */
    private String sign;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:11
     * @description: 发起退款申请的订单的订单号
     */
    private String out_trade_no;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:12
     * @description: 本次退款申请的退款单号
     */
    private String out_refund_no;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:12
     * @description: 发起退款申请的订单金额，单位为分
     */
    private BigDecimal total_fee;

    /**
     * @author: 小case
     * @date: 2020/6/16 16:13
     * @description: 发起退款的金额，需小于或等于订单金额，单位为分
     */
    private BigDecimal refund_fee;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/6/23 10:14
     * @description: 退款结果通知url
     */
    private String notify_url;

}
