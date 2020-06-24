package com.joinwe.demo.wechatapplet;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 小case
 * created on 2020/6/3 9:44
 */
public class WeChatAppletUtils {

    /**
     * getObjectConfigXml 将Config对象转xml字符串
     *
     * @param objectConfig
     * @author: 小case
     * @date: 2020/6/15 11:19
     * @return: String
     */
    public static String getObjectConfigXml(Object objectConfig) throws NoSuchFieldException, IllegalAccessException {
        StringBuffer objectConfigXml = new StringBuffer();
        objectConfigXml.append("<xml>");
        Field[] fields = objectConfig.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            objectConfigXml.append("<")
                    .append(fieldName)
                    .append(">");
            Field fieldValue = objectConfig.getClass().getDeclaredField(fieldName);
            fieldValue.setAccessible(true);
            objectConfigXml.append(fieldValue.get(objectConfig));
            objectConfigXml.append("</")
                    .append(fieldName)
                    .append(">");
        }
        objectConfigXml.append("</xml>");

        return objectConfigXml.toString();
    }

    /**
     * resultXmlToMap 将支付请求返回的xml字符串转为map
     *
     * @param result
     * @author: 小case
     * @date: 2020/6/15 11:19
     * @return: Map
     */
    public static Map resultXmlToMap(String result) throws Exception {
        if (null == result || "".equals(result)) {
            return null;
        }

        Map resultMap = new HashMap();
        InputStream in = stringToInputStream(result);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            resultMap.put(k, v);
        }

        //关闭流
        in.close();

        return resultMap;
    }

    private static InputStream stringToInputStream(String result) {
        return new ByteArrayInputStream(result.getBytes());
    }

    private static String getChildrenText(List children) {
        StringBuffer resultBuffer = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                resultBuffer.append("<").append(name).append(">");
                if (!list.isEmpty()) {
                    resultBuffer.append(getChildrenText(list));
                }
                resultBuffer.append(value);
                resultBuffer.append("</").append(name).append(">");
            }
        }

        return resultBuffer.toString();
    }

    /**
     * generateSign 根据传入的对象和商家支付key生成数字签名Sign
     *
     * @param objectConfig
     * @param payKey
     * @author: 小case
     * @date: 2020/6/15 13:50
     * @return: String
     */
    public static String generateSign(Object objectConfig, String payKey) throws NoSuchFieldException, IllegalAccessException {

        // 拼接签名所需字符串
        String signFirstParameter = concatenateString(objectConfig);
        String signParameter = signFirstParameter + "&key=" + payKey;
        String sign = WeChatAppletUtils.generateFinalSign(signParameter, "utf-8").toUpperCase();

        return sign;
    }

    /**
     * concatenateString 利用反射得到对象的属性名,并将属性名和属性值拼接为生成签名所需的字符串参数
     *
     * @param objectConfig
     * @author: 小case
     * @date: 2020/6/16 11:21
     * @return: String
     */
    public static String concatenateString(Object objectConfig) throws NoSuchFieldException, IllegalAccessException {
        Field[] fields = objectConfig.getClass().getDeclaredFields();
        List<String> fieldNameList = new ArrayList();
        for (Field field : fields) {
            fieldNameList.add(field.getName());
        }
        fieldNameList.remove("sign");
        Collections.sort(fieldNameList);
        StringBuffer stringBuffer = new StringBuffer();
        for (String name : fieldNameList) {
            Field fieldValue = objectConfig.getClass().getDeclaredField(name);
            fieldValue.setAccessible(true);
            stringBuffer.append(name)
                    .append("=")
                    .append(fieldValue.get(objectConfig))
                    .append("&");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);

        return stringBuffer.toString();
    }

    /**
     * generateFinalSign 生成MD5签名
     *
     * @param signParameter
     * @param inputCharset
     * @author: 小case
     * @date: 2020/6/15 13:50
     * @return: String
     */
    public static String generateFinalSign(String signParameter, String inputCharset) {

        String sign = DigestUtils.md5DigestAsHex(getPreliminarySignBytes(signParameter, inputCharset));

        return sign;
    }

    /**
     * getPreliminarySignBytes 最终MD5签名
     *
     * @param signParameter
     * @param inputCharset
     * @author: 小case
     * @date: 2020/6/15 11:15
     * @return: byte[]
     */
    private static byte[] getPreliminarySignBytes(String signParameter, String inputCharset) {
        if (inputCharset == null || "".equals(inputCharset)) {
            return signParameter.getBytes();
        }
        try {
            return signParameter.getBytes(inputCharset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + inputCharset);
        }
    }

    /**
     * getNonceStr 生成32位的随机字符串
     *
     * @param
     * @author: 小case
     * @date: 2020/6/15 11:15
     * @return: String
     */
    public static String getNonceStr() {
        String nonceStr = UUID.randomUUID().toString().replace("-", "");
        return nonceStr;
    }


    /**
     * getTradeNo 根据时间戳和随机码生成商户订单号
     *
     * @param
     * @author: 小case
     * @date: 2020/6/15 11:16
     * @return: String
     */
    public static String getTradeNo() {
        String code = createCode(5);
        String formatTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String timestamp = String.valueOf(System.currentTimeMillis());
        String outTradeNo = formatTime + timestamp + code;
        return outTradeNo;
    }


    /**
     * createCode 商户订单号的随机码
     *
     * @param
     * @author: 小case
     * @date: 2020/6/15 11:17
     * @return: String
     */
    public static String createCode(int size) {
        StringBuffer codeBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            codeBuffer.append(random.nextInt(10));
        }
        return codeBuffer.toString();
    }


}
