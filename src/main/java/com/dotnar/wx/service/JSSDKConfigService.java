package com.dotnar.wx.service;

import com.dotnar.bean.js.JSInitialize;
import com.dotnar.bean.js.JSInitializeResult;
import com.dotnar.client.HttpClientFactory;
import com.dotnar.client.LocalHttpClient;
import com.dotnar.contant.WXPayConfigure;
import com.dotnar.exception.WXPayExceptioin;
import com.dotnar.support.TicketManager;
import com.dotnar.support.TokenManager;
import com.dotnar.util.JsUtil;
import com.dotnar.util.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

/**
 * 初始化jssdk
 * Created by chovans on 15/7/18.
 */
@Service
public class JSSDKConfigService {

    /**
     * jssdk中wx的初始化参数
     * 参数有：  appid       第三方用户唯一凭证
     *          secret      第三方用户唯一凭证密钥，即appsecret
     *          url         访问网页的URL，不包含#及其后面部分
     *
     * @param jsonObj
     * @return
     */
    public static String jsConfig(String jsonObj) {

        JSInitialize jsInitialize = JsonUtil.parseObject(jsonObj, JSInitialize.class);
        String ticket = TicketManager.getTicket(jsInitialize.getAppid());
        JSInitializeResult result = new JSInitializeResult();
        try {
            if (WXPayConfigure.IS_DEBUG) {//调试状态，从服务端取ticket
                StringWriter writer = new StringWriter();

                HttpUriRequest httpUriRequest = RequestBuilder.post()
                        .setHeader(WXPayConfigure.xmlHeader)
                        .setUri(WXPayConfigure.DEBUG_URL + "getTicket.do?appid=wxcf74f930098faee1")
                        .build();
                HttpResponse response = LocalHttpClient.execute(httpUriRequest);
                IOUtils.copy(response.getEntity().getContent(), writer, "UTF-8");
                ticket = writer.toString();

                result.setAppId(jsInitialize.getAppid());
                result.setNonceStr(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20));
                result.setTimestamp(String.valueOf(new Date().getTime() / 1000));

                System.out.println("ticket = " +ticket);

                String sign = JsUtil.generateConfigSignature(result.getNonceStr(), ticket,
                        result.getTimestamp(), jsInitialize.getUrl());
                result.setSignature(sign);

            } else {
                if (StringUtils.isEmpty(ticket)) {

                    TokenManager.init(jsInitialize.getAppid(), jsInitialize.getSecret());
                    while (TokenManager.getToken(jsInitialize.getAppid()) == null) {
                        Thread.sleep(100);
                    }
                    TicketManager.init(jsInitialize.getAppid());

                    while (TicketManager.getTicket(jsInitialize.getAppid()) == null) {
                        Thread.sleep(100);
                    }

                }

                result.setAppId(jsInitialize.getAppid());
                result.setNonceStr(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20));
                result.setTimestamp(String.valueOf(new Date().getTime() / 1000));

                String sign = JsUtil.generateConfigSignature(result.getNonceStr(), TicketManager.getTicket(jsInitialize.getAppid()),
                        result.getTimestamp(), jsInitialize.getUrl());
                result.setSignature(sign);
            }


        } catch (Exception e) {
            return JsonUtil.toJSONString(new WXPayExceptioin(e.getMessage()));
        }

        return JsonUtil.toJSONString(result);

    }
}
