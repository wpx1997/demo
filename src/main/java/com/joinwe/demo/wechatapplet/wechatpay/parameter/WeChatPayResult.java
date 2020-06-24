package com.joinwe.demo.wechatapplet.wechatpay.parameter;

import lombok.Data;

import java.util.Map;

/**
 * created by 小case on 2020/6/2 10:05
 */
@Data
public class WeChatPayResult {
    private Boolean returnCode;
    private Boolean resultCode;
    private String message;
    private Map resultMap;
    private Map paymentMap;
    private String TradeNo;
}
