package com.joinwe.demo.wechatapplet.wechatpay.config;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 小case
 * created on 2020/5/29 10:45
 */
@Data
public class MchPayConfig {

   /**
    * @author: 小case
    * @date: 2020/6/16 16:02
    * @description: 商户appId
    */
   private String mch_appid;

   /**
    * @author: 小case
    * @date: 2020/6/16 16:02
    * @description: 商户mchId
    */
   private String mchid;

   /**
    * @author: 小case
    * @date: 2020/6/16 16:03
    * @description: 不长于32位的随机字符串
    */
   private String nonce_str;

   /**
    * @author: 小case
    * @date: 2020/6/16 16:06
    * @description: 企业付款签名
    */
   private String sign;

   /**
    * @author: 小case
    * @date: 2020/6/16 16:06
    * @description: 商户订单号，需保持唯一性(只能是字母或者数字，不能包含有其它字符)
    */
   private String partner_trade_no;

   /**
    * @author: 小case
    * @date: 2020/6/16 16:06
    * @description: 商户appId下的用户的openid
    */
   private String openid;

   /**
    * @author: 小case
    * @date: 2020/6/16 16:07
    * @description: NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
    */
   private String check_name;

   /**
    * @author: 小case
    * @date: 2020/6/16 16:08
    * @description: 企业付款金额，单位为分
    */
   private BigDecimal amount;

   /**
    * @author: 小case
    * @date: 2020/6/16 16:08
    * @description: 企业付款备注
    */
   private String desc;

}
