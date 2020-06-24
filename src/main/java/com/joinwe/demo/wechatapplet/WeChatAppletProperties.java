package com.joinwe.demo.wechatapplet;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 不会飞的小鹏
 * created on 2020/5/29 12:21
 * 微信小程序参数配置
 */
@Component
@ConfigurationProperties(prefix = WeChatAppletProperties.MESSAGE_PREFIX)
@Data
public class WeChatAppletProperties implements ApplicationContextAware {
    public static final String MESSAGE_PREFIX = "wechat";
    private String appId;
    private String mchId;
    private String secret;
    private String payKey;
    private String paySslPath;
    private String unifiedCallbackUrl;
    private String refundCallbackUrl;
    private String serverIp;

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WeChatAppletProperties.applicationContext = applicationContext;
    }

    public static <T>T getBean(Class<T> tClass){
        return applicationContext != null ? applicationContext.getBean(tClass) : null;
    }

}
