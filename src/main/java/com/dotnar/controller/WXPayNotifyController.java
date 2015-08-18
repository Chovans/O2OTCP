package com.dotnar.controller;

import com.dotnar.bean.paymch.UnifiedorderKey;
import com.dotnar.contant.WXPayConfigure;
import com.dotnar.util.JsonUtil;
import com.dotnar.wx.service.UnifiedorderService;
import hprose.client.HproseTcpClient;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

/**
 * 微信支付异步回调函数
 * Created by chovans on 15/7/18.
 */
@Controller
public class WXPayNotifyController {

    public static HproseTcpClient client = new HproseTcpClient(WXPayConfigure.NOTIFY_TO_JS);

    @RequestMapping("/notify.do")
    public static void notify(HttpServletRequest request,HttpServletResponse response) throws Exception {
        StringWriter writer = new StringWriter();
        IOUtils.copy(request.getInputStream(), writer, "UTF-8");

        UnifiedorderKey unifiedorderKey = UnifiedorderService.payRequestResultJson(writer.toString(), response);
        //若取得返回值，直接返回给js客户端
        if(unifiedorderKey != null){
//            client.invoke(WXPayConfigure.NOTIFY_MOTHOD,new Object[]{JsonUtil.toJSONString(unifiedorderKey)});
        }
    }
}
