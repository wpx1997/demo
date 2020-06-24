package com.joinwe.demo.wechatapplet.wechatpay.parameter;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * created by 小case on 2020/6/3 18:40
 */
@Data
public class WeChatRefundParameter {

    @NotBlank(message = "退款订单的订单号不能为空!")
    private String tradeNo;

    @Min(value = 0,message = "退款订单的订单金额不能为负!")
    private Double totalFee;

    @Min(value = 0,message = "退款金额不能为负!")
    public Double refundFee;

}
