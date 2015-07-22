package com.dotnar.bean;

public class SnsToken extends BaseResult {

    private String access_token;

    private Integer expires_in;

    private String refresh_token;

    private String openid;

    private String unionid;

    private String scope;

    private String errcode;

    private String errmsg;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String accessToken) {
        access_token = accessToken;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expiresIn) {
        expires_in = expiresIn;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refreshToken) {
        refresh_token = refreshToken;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Override
    public String toString() {
        return "SnsToken{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", refresh_token='" + refresh_token + '\'' +
                ", openid='" + openid + '\'' +
                ", unionid='" + unionid + '\'' +
                ", scope='" + scope + '\'' +
                ", errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
