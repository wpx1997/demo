###配置

####application.properties文件配置
- 将spring.datasource.url    值后的 *** 改为本项目的数据库明
- 配置相应的数据库操作账号和密码
- mybatis.type-aliases-package  的 joinwe.demo  改为本项目路径<br><br>
支付信息配置（如需调用支付接口）：
- wechat.app-id    配置当前的appid
- wechat.mch-id    配置当前的商户号
- wechat.unified-callback-url   统一下单回调地址（注意自己写回调接口接收回调信息）
- wechat.refund-callback-url    退款回调地址（自己写回调接口接收回调信息）
- wechat.server-ip  当前服务器ip
- wechat.pay.key    当前商户号支付密钥
- wechat.pay-ssl-path ssl证书路径（/static/ssl/apiclient_cert.p12）

####generatorConfig.xml文件配置
- jdbcConnection标签里的connectionURL属性的***改为本项目数据库名并配置相应的数据库操作账号和密码
- javaModelGenerator标签里的targetPackage属性里的joinwe.demo配置为当前项目的路径
- javaClientGenerator标签里的joinwe.demo配置为当前项目的路径
- table标签里的tableName属性为数据表名称，domainObjectName属性为数据库表对应生成的model文件（建议使用对应的数据库表名的驼峰形式命名）
- table标签包含的generatedKey标签作用为返回的该表的插入操作后的id
######配置完成后在Terminal运行以下命令生成model和mapper文件
```sql
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```

###相关问题说明
####支付接口调用
- 设置调用方法所需的参数应先创建对应的参数对象
- 小程序付款和企业付款传参应该创建WeChatPayParameter的对象，应传参数为details（订单说明，类型为String），price（订单金额，类型为Double），openId
- 退款传参应该创建WeChatRefundParameter的对象，应传参数为tradeNo（退款订单的订单号），totalFee（退款订单的订单金额），refundFee（退款的金额）<br><br>
- 小程序付款调用WeChatPayProcessing类的统一下单入口方法unifiedOrder(weChatPayParameter)
- 企业付款调用WeChatPayProcessing类的企业付款入口方法mchPay(weChatPayParameter)
- 退款调用WeChatPayProcessing类的退款入口方法payRefund(weChatRefundParameter)
####支付结果返回
- 所有的支付结果都以WeChatPayResult类的对象返回
- WeChatPayResult类有6个属性
- returnCode属性类型为Boolean，是发起支付的通信标识
- resultCode属性类型为Boolean，是发起支付是否成功的标识
- message属性类型为String，是调用接口的信息
- resultMap属性类型为Map，是发起支付请求的结果集
- paymentMap属性类型为Map，是用于返回前端调起密码输入框的参数集，仅当调用方法为统一下单且成功时才有返回，否则为null
- TradeNo属性类型为String，是发起支付的订单号

####登录接口调用
- 小程序登录调用 WeChatAppletLogin 的获取openId和session_key的方法getOpenIdAndSessionKeyByJsCode(String jsCode)
- 小程序获取手机号码调用 WeChatAppletLogin 的解析手机号码的方法phoneDecrypt(String encryptedData, String iv, String sessionKey)
####返回结果
- 登录接口返回结果集为AppletLoginResult（包含openId和sessionKey）
- 获取到session_key后不要传到前端，存入数据库，需要的时候从数据库取出
- 获取手机号码接口的返回为 AppletPhoneResult类

###相关文档
[MyBatis-generator](http://mybatis.org/generator/)<br>
[Spring Boot](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/)