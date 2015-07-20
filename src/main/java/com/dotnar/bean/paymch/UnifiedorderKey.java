package com.dotnar.bean.paymch;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 记录统一下单和key的关系（保证多个系统能使用相同的支付模块）
 * 使用条件：
 * appid，mchid，outtradeno，openid
 * 四个参数可保证唯一性
 */
@Entity
@Table
public class UnifiedorderKey {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;
    private String appId;
    private String mchId;
    private String outTradeNo;
    private String openId;
    private String _key;
    private String securityKey;  //与js端的识别标志，支付成功回调函数中需要一同传回

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }
}
