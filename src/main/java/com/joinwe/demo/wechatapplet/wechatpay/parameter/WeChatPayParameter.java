package com.joinwe.demo.wechatapplet.wechatpay.parameter;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author 小case
 * created on 2020/6/2 14:51
 */
@Data
public class WeChatPayParameter {

    @NotBlank(message = "唤起微信支付时，传入的付款详情不能为空！")
    private String details;

    @Min(value = 0,message = "唤起微信支付时，金额不能为负！")
    private Double price;

    @NotBlank(message = "唤起付款时，用户的openId不能为空！")
    private String openId;

}
