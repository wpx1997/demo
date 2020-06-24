package com.joinwe.demo.wechatapplet.constant;

import com.joinwe.demo.wechatapplet.WeChatAppletProperties;
import lombok.Getter;

/**
 * @author 不会飞的小鹏
 * create on 2020/6/22 20:09
 * @Description WeChatConstant is
 */
public final class WeChatConstant {

    private static final WeChatAppletProperties weChatAppletProperties = WeChatAppletProperties.getBean(WeChatAppletProperties.class);

    /**
     * 配置文件
     */

    public static final String WE_CHAT_UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String WE_CHAT_MCH_PAY_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
    public static final String WE_CHAT_PAY_REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    public static final String WE_CHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    public static final String APP_ID = weChatAppletProperties.getAppId();
    public static final String APP_SECRET = weChatAppletProperties.getSecret();
    public static final String MCH_ID = weChatAppletProperties.getMchId();
    public static final String PAY_KEY = weChatAppletProperties.getPayKey();
    public static final String PAY_SSL_FILE_PATH = weChatAppletProperties.getPaySslPath();
    public static final String UNIFIED_CALLBACK_URL = weChatAppletProperties.getUnifiedCallbackUrl();
    public static final String REFUND_CALLBACK_URL = weChatAppletProperties.getRefundCallbackUrl();
    public static final String SERVER_IP = weChatAppletProperties.getServerIp();

    /**
     * 类型常量
     */

    public static final String METHOD_TYPE_POST = "POST";
    public static final String METHOD_TYPE_GET = "GET";
    public static final String TRADE_TYPE = "JSAPI";
    public static final String CHECK_NAME_TYPE = "NO_CHECK";

    /**
     * 登录模块常量
     */

    public static final String GRANT_TYPE = "authorization_code";

    public static final String AES = "AES";

    public static final String AES_CBC_PADDING = "AES/CBC/PKCS7Padding";

}
