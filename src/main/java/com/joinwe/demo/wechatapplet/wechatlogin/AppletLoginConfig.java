package com.joinwe.demo.wechatapplet.wechatlogin;

import lombok.Data;

/**
 * @author 小case
 * @date 2020/6/16 10:46
 * @Description LoginConfig is
 */
@Data
public class AppletLoginConfig {

    /**
     * @author: 小case
     * @date: 2020/6/16 15:55
     * @description: 小程序appId
     */
    private String appid;

    /**
     * @author: 小case
     * @date: 2020/6/16 15:57
     * @description: 小程序appSecret
     */
    private String secret;

    /**
     * @author: 小case
     * @date: 2020/6/16 15:57
     * @description: 小程序用户登录时获取的code
     */
    private String js_code;

    /**
     * @author: 小case
     * @date: 2020/6/16 15:58
     * @description: 授权类型
     */
    private String grant_type;

}
