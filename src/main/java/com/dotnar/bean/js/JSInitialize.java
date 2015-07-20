package com.dotnar.bean.js;

/**
 * js要config时所传入的对象
 * Created by chovans on 15/7/18.
 */
public class JSInitialize {
    private String appid;
    private String secret;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "JSInitialize{" +
                "appid='" + appid + '\'' +
                ", secret='" + secret + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
