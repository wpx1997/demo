package com.joinwe.demo.wechatapplet.wechatlogin;

import lombok.Data;

/**
 * @author 小case
 * @date 2020/6/16 15:06
 * @Description AppletLoginResult is
 */
@Data
public class AppletLoginResult {

    /**
     * @author: 小case
     * @date: 2020/6/16 15:58
     * @description: 根据返回的json字符串解析得到的session_key
     */
    private String session_key;

    /**
     * @author: 小case
     * @date: 2020/6/16 15:59
     * @description: 根据返回的json字符串解析得到的用户openId
     */
    private String openid;

}
