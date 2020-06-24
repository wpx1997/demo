package com.joinwe.demo.wechatapplet.wechatlogin;

import com.alibaba.fastjson.JSONArray;
import com.joinwe.demo.wechatapplet.WeChatAppletRequest;
import com.joinwe.demo.wechatapplet.WeChatAppletUtils;
import com.joinwe.demo.wechatapplet.constant.WeChatConstant;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Base64;

/**
 * @author 小case
 * @date 2020/6/16 10:45
 * @Description WeChatLogin is
 */
@Component
public class WeChatAppletLogin {

    /**
     * login 微信小程序获取用户openId和session_key入口
     *
     * @param jsCode
     * @author: 小case
     * @date: 2020/6/16 11:31
     * @return: void
     */
    public static AppletLoginResult getOpenIdAndSessionKeyByJsCode(String jsCode) throws NoSuchFieldException, IllegalAccessException {

        AppletLoginConfig appletLoginConfig = setAppletLoginConfig(jsCode);
        String loginParameter = WeChatAppletUtils.concatenateString(appletLoginConfig);

        String result = WeChatAppletRequest.weChatRequest(WeChatConstant.WE_CHAT_LOGIN_URL, loginParameter, WeChatConstant.METHOD_TYPE_GET);

        // 将json字符串转为 AppletLoginResult 对象
        AppletLoginResult appletLoginResult = JSONArray.parseObject(result, AppletLoginResult.class);

        return appletLoginResult;
    }

    /**
     * setAppletLoginConfig 配置AppletLoginConfig对象
     *
     * @param jsCode
     * @author: 小case
     * @date: 2020/6/16 11:33
     * @return: AppletLoginConfig
     */
    private static AppletLoginConfig setAppletLoginConfig(String jsCode) {

        AppletLoginConfig appletLoginConfig = new AppletLoginConfig();
        appletLoginConfig.setAppid(WeChatConstant.APP_ID);
        appletLoginConfig.setSecret(WeChatConstant.APP_SECRET);
        appletLoginConfig.setJs_code(jsCode);
        appletLoginConfig.setGrant_type(WeChatConstant.GRANT_TYPE);

        return appletLoginConfig;
    }

    /**
     * phoneDecrypt 对手机号码进行解密
     *
     * @param encryptedData 加密数据
     * @param iv            加密算法的初始向量
     * @param sessionKey    解密密钥
     * @author: 不会飞的小鹏
     * @date: 2020/6/22 15:24
     * @return: String
     */
    public static AppletPhoneResult phoneDecrypt(String encryptedData, String iv, String sessionKey) throws InvalidParameterSpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        byte[] encryptedByte = Base64.getDecoder().decode(encryptedData);
        byte[] ivByte = Base64.getDecoder().decode(iv);
        byte[] sessionKeyByte = Base64.getDecoder().decode(sessionKey);
        init();
        AlgorithmParameters ivAlgorithmParameters = generateVI(ivByte);
        byte[] resultByte = decrypt(encryptedByte, sessionKeyByte, ivAlgorithmParameters);
        AppletPhoneResult appletPhoneResult = JSONArray.parseObject(resultByte,AppletPhoneResult.class);

        return appletPhoneResult;
    }

    /**
     * init 初始化密钥
     *
     * @param
     * @author: 不会飞的小鹏
     * @date: 2020/6/22 15:45
     * @return: void
     */
    private static void init() throws NoSuchAlgorithmException {

        BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
        Security.addProvider(bouncyCastleProvider);
        KeyGenerator.getInstance(WeChatConstant.AES).init(128);

    }

    /**
     * decrypt 解密操作
     *
     * @param encryptedByte         加密数据的64位编码数组
     * @param sessionKeyByte        加密算法的解密密钥
     * @param ivAlgorithmParameters 加密算法初始向量的编码参数
     * @author: 不会飞的小鹏
     * @date: 2020/6/22 15:37
     * @return: byte[]
     */
    private static byte[] decrypt(byte[] encryptedByte, byte[] sessionKeyByte, AlgorithmParameters ivAlgorithmParameters) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Key key = new SecretKeySpec(sessionKeyByte, WeChatConstant.AES);
        Cipher cipher = Cipher.getInstance(WeChatConstant.AES_CBC_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, key, ivAlgorithmParameters);
        byte[] resultByte = cipher.doFinal(encryptedByte);

        return resultByte;
    }

    /**
     * generateVI 生成初始向量的编码参数
     *
     * @param ivByte 初始向量64位编码数组
     * @author: 不会飞的小鹏
     * @date: 2020/6/22 15:48
     * @return: AlgorithmParameters
     */
    private static AlgorithmParameters generateVI(byte[] ivByte) throws NoSuchAlgorithmException, InvalidParameterSpecException {

        AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance(WeChatConstant.AES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivByte);
        algorithmParameters.init(ivParameterSpec);

        return algorithmParameters;
    }


}
