# O2OTCP
点纳科技—微信后端


==============模板部分=================

访问地址：www.domain.com:7070 

请求地址：www.domain.com:7070/getInfoByTemplateName/{templateName} 

hook地址：www.domain.com:7070/projectName/{templateName} 


==============Mongo Support部分=========

dbName:数据库名称
documentName:Collection名称

var client = hprose.Client.create('tcp://localhost:7074/', []); 

//插入数据,jsonObj中需要含有_id

insert(String dbName, String documentName, String jsonObj)

findById(String dbName, String documentName, String id)

//查找数据，jsonObj中包含搜索条件

findOne(String dbName, String documentName, String jsonObj)

findList(String dbName, String documentName, String num, String page)

findAll(String dbName, String documentName)

//jsonObj中有更新的数据，有则修改，没有则添加

update(String dbName, String documentName, String id, String jsonObj)

remove(String dbName, String documentName, String id)


==============微信部分=================

初始化客户端

	var client = hprose.Client.create('tcp://localhost:xxxx/', []); 	
	var proxy = client.useService(['payRequestJson','jsConfig']);


初始化jssdk（jsConfig）

    /**
     * jssdk中wx的初始化参数
     * 参数：   appid       第三方用户唯一凭证
     *          secret      第三方用户唯一凭证密钥，即appsecret
     *          url         访问网页的URL，不包含#及其后面部分
     *
     * @param jsonObj
     * @return appId,timestamp,nonceStr,signature;
     */
    
    
预支付订单（payRequestJson）

    /**
     * 微信支付申请（兼容js和微信扫码模式2）
     * 对应内容文档中所有信息
     * 省略：nonce_str,sign,time_start,time_expire,notify_url
     *
     * @param objectInfo
     * @param key
     * @param security_key 识别标志，支付成功回调函数中使用
     * @return 支付请求的所有参数{appId,timeStamp,nonceStr,package,signType,paySin}
     */
     
     
关闭订单（closeOrder）

    /**
     * 关闭订单
     * 参数：appid,mcu_id,out_trade_no
     * @param jsonObj
     * @param key
     * @return return_code,return_msg,appid,mch_id,nonce_str,sign,result_code,err_code,err_code_des;
     */
     
     
下载对账单（download）

    /**
     * 下载对账单
     * 参数：appid
     *      mch_id
     *      device_info     微信支付分配的终端设备号，填写此字段，只下载该设备号的对账单
     *      //out_trade_no    暂不需要
     *      bill_date       下载对账单的日期，格式：20140603
     *      bill_type       ALL，返回当日所有订单信息，默认值
     *                      SUCCESS，返回当日成功支付的订单
     *                      REFUND，返回当日退款订单
     *                      REVOKED，已撤销的订单
     * @param jsonObj
     * @param key
     * @return
     */
     
查询订单（queryOrder）

    /**
     * 查询订单，包括参数
     * appid,mch_id,transaction_id,out_trade_no
     * @param jsonObj
     * @param key
     * @return return_code,return_msg,appid,mch_id,nonce_str,sign,result_code,err_code,err_code_des,trade_state,device_info,openid,is_subscribe,trade_type,bank_type,total_fee,fee_type,cash_fee,cash_fee_type,coupon_fee,coupon_count,transaction_id,out_trade_no,attach,time_end,trade_state_desc;
     */
     
     
申请退款（refund）

    /**
     * 申请退款
     * 参数：    appid
     *          mch_id
     *          device_info
     *          transaction_id  微信订单号
     *          out_trade_no    商户系统内部的订单号
     *          out_refund_no   商户系统内部的退款单号
     *          total_fee       订单总金额
     *          refund_fee      退款总金额
     *          refund_fee_type 默认人民币：CNY
     *          op_used_id      操作员帐号
     *
     * @param jsonObj
     * @param key
     * @return 查询订单（queryOrder）
     */
     
     
退款查询（refundQuery）

    /**
     * 查询退款
     * 提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款20分钟内到账，银行卡支付的退款3个工作日后重新查询退款状态。
     * 参数：appid
     *      mch_id
     *      device_info
     *      transaction_id  微信订单号
     *      out_trade_no    商户系统内部的订单号
     *      out_refund_no   商户退款单号
     *      refund_id       微信退款单号
     * @param jsonObj
     * @param key
     * @return 查询订单（queryOrder）
     */
     
根据code获取用户openid等（oauth2AccessToken）

    /**
     * 根据code获取openid等参数
     *  e.g. {"code":"xxxxxx"}
     * @param jsonObj
     * @return access_token,expires_in,refresh_token,openid,scope;
     */



=================其他工具=====================

获取图片验证码（getCheckCode）

    /**
     * 获取验证码图片
     * @param code 验证码
     * @return base64-PNG
     */
     
 发送短信（sendSMS）
     
     /**
     * 发送短信
     * @param phone
     * @param templateId
     * @param params 字符串数组  e.g: arg1,arg2,arg3
     * @return 服务商返回的json字符串
     */
     
     test
