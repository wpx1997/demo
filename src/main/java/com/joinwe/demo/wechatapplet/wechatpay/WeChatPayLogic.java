package com.joinwe.demo.wechatapplet.wechatpay;

import com.joinwe.demo.wechatapplet.WeChatAppletRequest;
import com.joinwe.demo.wechatapplet.WeChatAppletUtils;
import com.joinwe.demo.wechatapplet.constant.WeChatConstant;
import com.joinwe.demo.wechatapplet.wechatpay.config.MchPayConfig;
import com.joinwe.demo.wechatapplet.wechatpay.config.PayRefundConfig;
import com.joinwe.demo.wechatapplet.wechatpay.config.UnifiedOrderConfig;
import com.joinwe.demo.wechatapplet.wechatpay.parameter.WeChatPayParameter;
import com.joinwe.demo.wechatapplet.wechatpay.parameter.WeChatPayResult;
import com.joinwe.demo.wechatapplet.wechatpay.parameter.WeChatRefundParameter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 小case
 * created on 2020/5/26 10:54
 */
@Validated
public class WeChatPayLogic {

    /**
     * unifiedOrder 微信小程序支付统一下单入口
     *
     * @param weChatPayParameter
     * @author: 小case
     * @date: 2020/6/15 11:22
     * @return: WeChatPayResult
     */
    public static WeChatPayResult unifiedOrder(@Valid WeChatPayParameter weChatPayParameter) throws Exception {

        /**
         *
         * unifiedOrder 对传入价格进行转换处理
         *
         * @author: 小case
         * @date: 2020/6/15 11:22
         * @param weChatPayParameter
         * @return: WeChatPayResult
         */
        BigDecimal originalTotalFee = new BigDecimal(weChatPayParameter.getPrice().toString());
        BigDecimal totalFee = originalTotalFee.movePointRight(2);

        // 通过处理后的参数发起微信支付请求
        WeChatPayResult weChatPayResult = arouseOrder(weChatPayParameter.getDetails(), totalFee, weChatPayParameter.getOpenId());

        // 对微信支付请求结果集进行解析
        resultConfirm(weChatPayResult);

        // 根据解析结果处理后续步骤
        if (weChatPayResult.getReturnCode() && weChatPayResult.getResultCode()) {
            handleResult(weChatPayResult);
        }

        return weChatPayResult;
    }

    /**
     * mchPay 微信企业付款入口
     *
     * @param weChatPayParameter
     * @author: 小case
     * @date: 2020/6/15 11:22
     * @return: WeChatPayResult
     */
    public static WeChatPayResult mchPay(@Valid WeChatPayParameter weChatPayParameter) throws Exception {

        // 对传入参数进行处理
        BigDecimal preAmount = new BigDecimal(weChatPayParameter.getPrice().toString());
        BigDecimal amount = preAmount.movePointRight(2);

//        通过处理后的参数发起微信企业付款请求
        WeChatPayResult weChatPayResult = arouseMchPay(weChatPayParameter.getDetails(), amount, weChatPayParameter.getOpenId());

//        对微信企业付款请求结果集进行解析
        resultConfirm(weChatPayResult);

        return weChatPayResult;
    }

    /**
     * payRefund 微信小程序支付退款入口
     *
     * @param weChatRefundParameter
     * @author: 小case
     * @date: 2020/6/15 11:22
     * @return: WeChatPayResult
     */
    public static WeChatPayResult payRefund(@Valid WeChatRefundParameter weChatRefundParameter) throws Exception {

        // 对传入参数进行处理
        BigDecimal preTotalFee = new BigDecimal(weChatRefundParameter.getTotalFee().toString());
        BigDecimal totalFee = preTotalFee.movePointRight(2);
        BigDecimal preRefundFee = new BigDecimal(weChatRefundParameter.getRefundFee().toString());
        BigDecimal refundFee = preRefundFee.movePointRight(2);

        // 将传入后的参数进行校验
        if (totalFee.compareTo(refundFee) < 0) {
            WeChatPayResult weChatPayResult = new WeChatPayResult();
            weChatPayResult.setReturnCode(false);
            weChatPayResult.setResultCode(false);
            weChatPayResult.setMessage("退款金额不能大于订单金额");

            return weChatPayResult;
        }

        // 通过处理并校验后的参数发起微信支付退款请求
        WeChatPayResult weChatPayResult = arouseRefund(weChatRefundParameter.getTradeNo(), totalFee, refundFee);

        // 对微信支付退款请求结果集进行解析
        resultConfirm(weChatPayResult);

        return weChatPayResult;
    }

    /**
     * arouseOrder 微信小程序统一下单并返回结果集和订单号
     *
     * @param body
     * @param totalFee
     * @param openId
     * @author: 小case
     * @date: 2020/6/15 11:23
     * @return: WeChatPayResult
     */
    private static WeChatPayResult arouseOrder(String body, BigDecimal totalFee, String openId) throws Exception {

        WeChatPayResult weChatPayResult = new WeChatPayResult();

        // 配置并返回UnifiedOrderConfig类的对象
        UnifiedOrderConfig unifiedOrderConfig = setUnifiedOrderConfig(body, totalFee, openId);

        // 返回商户订单号
        weChatPayResult.setTradeNo(unifiedOrderConfig.getOut_trade_no());

        // 将要发起微信支付请求的参数转换成xml字符串
        String unifiedOrderConfigXml = WeChatAppletUtils.getObjectConfigXml(unifiedOrderConfig);

        // 发起微信支付请求并返回结果集
        String result = WeChatAppletRequest.weChatRequest(WeChatConstant.WE_CHAT_UNIFIED_ORDER_URL, unifiedOrderConfigXml, "POST");

        // 将返回的结果集转换成Map
        Map resultMap = WeChatAppletUtils.resultXmlToMap(result);

        // 返回resultMap
        weChatPayResult.setResultMap(resultMap);

        return weChatPayResult;
    }

    /**
     * arouseMchPay 微信企业付款返回结果集和订单号
     *
     * @param desc
     * @param amount
     * @param openId
     * @author: 小case
     * @date: 2020/6/15 11:23
     * @return: WeChatPayResult
     */
    private static WeChatPayResult arouseMchPay(String desc, BigDecimal amount, String openId) throws Exception {

        WeChatPayResult weChatPayResult = new WeChatPayResult();
        MchPayConfig mchPayConfig = setMchPayConfig(desc, amount, openId);
        weChatPayResult.setTradeNo(mchPayConfig.getPartner_trade_no());
        String mchPayConfigXml = WeChatAppletUtils.getObjectConfigXml(mchPayConfig);
        String result = WeChatAppletRequest.WeChatSslRequest(WeChatConstant.WE_CHAT_MCH_PAY_URL, mchPayConfigXml, WeChatConstant.PAY_SSL_FILE_PATH, WeChatConstant.MCH_ID);
        Map resultMap = WeChatAppletUtils.resultXmlToMap(result);
        weChatPayResult.setResultMap(resultMap);

        return weChatPayResult;
    }

    /**
     * arouseRefund 微信支付退款返回结果集和退款单号
     *
     * @param tradeNo
     * @param totalFee
     * @param refundFee
     * @author: 小case
     * @date: 2020/6/15 11:24
     * @return: WeChatPayResult
     */
    private static WeChatPayResult arouseRefund(String tradeNo, BigDecimal totalFee, BigDecimal refundFee) throws Exception {

        WeChatPayResult weChatPayResult = new WeChatPayResult();
        PayRefundConfig payRefundConfig = setPayRefundConfig(tradeNo, totalFee, refundFee);
        weChatPayResult.setTradeNo(payRefundConfig.getOut_refund_no());
        String payRefundConfigXml = WeChatAppletUtils.getObjectConfigXml(payRefundConfig);
        String result = WeChatAppletRequest.WeChatSslRequest(WeChatConstant.WE_CHAT_PAY_REFUND_URL, payRefundConfigXml, WeChatConstant.PAY_SSL_FILE_PATH, WeChatConstant.MCH_ID);
        Map resultMap = WeChatAppletUtils.resultXmlToMap(result);
        weChatPayResult.setResultMap(resultMap);

        return weChatPayResult;
    }

    /**
     * setUnifiedOrderConfig 配置微信小程序统一下单的UnifiedOrderConfig类
     *
     * @param body
     * @param totalFee
     * @param openId
     * @author: 小case
     * @date: 2020/6/15 11:24
     * @return: UnifiedOrderConfig
     */
    private static UnifiedOrderConfig setUnifiedOrderConfig(String body, BigDecimal totalFee, String openId) throws NoSuchFieldException, IllegalAccessException {

        UnifiedOrderConfig unifiedOrderConfig = new UnifiedOrderConfig();
        String notifyUrl = WeChatConstant.UNIFIED_CALLBACK_URL;
        String tradeType = WeChatConstant.TRADE_TYPE;
        unifiedOrderConfig.setAppid(WeChatConstant.APP_ID);
        unifiedOrderConfig.setMch_id(WeChatConstant.MCH_ID);
        unifiedOrderConfig.setNonce_str(WeChatAppletUtils.getNonceStr());
        unifiedOrderConfig.setBody(body);
        unifiedOrderConfig.setOut_trade_no(WeChatAppletUtils.getTradeNo());
        unifiedOrderConfig.setTotal_fee(totalFee);
        unifiedOrderConfig.setSpbill_create_ip(WeChatConstant.SERVER_IP);
        unifiedOrderConfig.setNotify_url(notifyUrl);
        unifiedOrderConfig.setTrade_type(tradeType);
        unifiedOrderConfig.setOpenid(openId);

        // 在WeChatPayUtils工具类中生成签名Sign
        String sign = WeChatAppletUtils.generateSign(unifiedOrderConfig, WeChatConstant.PAY_KEY);
        unifiedOrderConfig.setSign(sign);

        return unifiedOrderConfig;
    }

    /**
     * setMchPayConfig 配置微信企业付款的MchPayConfig类
     *
     * @param desc
     * @param amount
     * @param openId
     * @author: 小case
     * @date: 2020/6/15 11:32
     * @return: MchPayConfig
     */
    private static MchPayConfig setMchPayConfig(String desc, BigDecimal amount, String openId) throws NoSuchFieldException, IllegalAccessException {

        MchPayConfig mchPayConfig = new MchPayConfig();
        mchPayConfig.setMch_appid(WeChatConstant.APP_ID);
        mchPayConfig.setMchid(WeChatConstant.MCH_ID);
        mchPayConfig.setNonce_str(WeChatAppletUtils.getNonceStr());
        mchPayConfig.setPartner_trade_no(WeChatAppletUtils.getTradeNo());
        mchPayConfig.setOpenid(openId);
        mchPayConfig.setCheck_name(WeChatConstant.CHECK_NAME_TYPE);
        mchPayConfig.setAmount(amount);
        mchPayConfig.setDesc(desc);

        // 在WeChatPayUtils工具类中生成签名Sign
        String sign = WeChatAppletUtils.generateSign(mchPayConfig, WeChatConstant.PAY_KEY);
        mchPayConfig.setSign(sign);

        return mchPayConfig;
    }

    /**
     * setPayRefundConfig 配置微信支付退款的PayRefundConfig类
     *
     * @param tradeNo
     * @param totalFee
     * @param refundFee
     * @author: 小case
     * @date: 2020/6/15 11:32
     * @return: PayRefundConfig
     */
    private static PayRefundConfig setPayRefundConfig(String tradeNo, BigDecimal totalFee, BigDecimal refundFee) throws NoSuchFieldException, IllegalAccessException {

        PayRefundConfig payRefundConfig = new PayRefundConfig();
        payRefundConfig.setAppid(WeChatConstant.APP_ID);
        payRefundConfig.setMch_id(WeChatConstant.MCH_ID);
        payRefundConfig.setNonce_str(WeChatAppletUtils.getNonceStr());
        payRefundConfig.setOut_trade_no(tradeNo);
        payRefundConfig.setOut_refund_no(WeChatAppletUtils.getTradeNo());
        payRefundConfig.setTotal_fee(totalFee);
        payRefundConfig.setRefund_fee(refundFee);
        payRefundConfig.setNotify_url(WeChatConstant.REFUND_CALLBACK_URL);

        // 在WeChatPayUtils工具类中生成签名
        String sign = WeChatAppletUtils.generateSign(payRefundConfig, WeChatConstant.PAY_KEY);
        payRefundConfig.setSign(sign);

        return payRefundConfig;
    }

    /**
     * resultConfirm 对支付请求的结果集进行校验和分析
     *
     * @param weChatPayResult
     * @author: 小case
     * @date: 2020/6/15 11:33
     * @return: void
     */
    private static void resultConfirm(WeChatPayResult weChatPayResult) {

        String checkSuccess = "SUCCESS";
        Map resultMap = weChatPayResult.getResultMap();
        String returnCode = (String) resultMap.get("return_cod");
        String resultCode = (String) resultMap.get("result_code");
        if (checkSuccess.equals(returnCode)) {
            weChatPayResult.setReturnCode(true);
        } else {
            weChatPayResult.setReturnCode(false);
            weChatPayResult.setResultCode(false);
            weChatPayResult.setMessage((String) resultMap.get("return_msg"));
            return;
        }
        if (checkSuccess.equals(resultCode)) {
            weChatPayResult.setResultCode(true);
        } else {
            weChatPayResult.setResultCode(false);
            weChatPayResult.setMessage((String) resultMap.get("err_code_des"));
        }

    }

    /**
     * handleResult 微信小程序支付统一下单成功后,封装调起密码输入框的签名和数据集合
     *
     * @param weChatPayResult
     * @author: 小case
     * @date: 2020/6/15 13:45
     * @return: void
     */
    private static void handleResult(WeChatPayResult weChatPayResult) {

        Map resultMap = weChatPayResult.getResultMap();
        Map paymentMap = new HashMap(16);
        String prepayId = (String) resultMap.get("prepay_id");
        paymentMap.put("nonceStr", resultMap.get("nonce_str"));
        paymentMap.put("package", "prepay_id=" + prepayId);
        Long timeStamp = System.currentTimeMillis() / 1000;
        paymentMap.put("timeStamp", timeStamp + "");
        paymentMap.put("signType", "MD5");

        // 拼接调起密码输入框的生成签名所需的字符串参数
        StringBuffer signBuffer = new StringBuffer();
        signBuffer.append("appId=")
                .append(WeChatConstant.APP_ID)
                .append("&nonceStr=")
                .append(resultMap.get("nonce_str"))
                .append("&package=prepay_id=")
                .append(prepayId)
                .append("&signType=")
                .append("MD5");
        signBuffer.append("&timeStamp=" + timeStamp);
        signBuffer.append("&key=" + WeChatConstant.PAY_KEY);
        String signParameter = signBuffer.toString();

        String paySign = WeChatAppletUtils.generateFinalSign(signParameter, "utf-8").toUpperCase();
        paymentMap.put("paySign", paySign);

        weChatPayResult.setPaymentMap(paymentMap);
    }

}
