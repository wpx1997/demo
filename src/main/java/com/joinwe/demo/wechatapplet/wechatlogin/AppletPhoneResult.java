package com.joinwe.demo.wechatapplet.wechatlogin;

import lombok.Data;

/**
 * @author 不会飞的小鹏
 * create on 2020/6/24 10:36
 * @Description AppletPhoneResult is
 */
@Data
public class AppletPhoneResult {

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/6/24 10:38
     * @description:  用户绑定的手机号（国外手机号会有区号）
     */
    private String phoneNumber;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/6/24 10:39
     * @description: 没有区号的手机号
     */
    private String purePhoneNumber;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/6/24 10:40
     * @description: 区号
     */
    private String countryCode;
}
