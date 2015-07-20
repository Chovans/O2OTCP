package com.dotnar.util;

import com.dotnar.bean.paymch.MchPayNotify;
import com.dotnar.contant.WXPayConfigure;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 回调工具
 * Created by chovans on 15/7/20.
 */
public class NotifyUtil {

    @SuppressWarnings("NullArgumentToVariableArgMethod")
    public static void sendToJS(MchPayNotify payNotify)throws Exception{
        Map<String, String> map = MapUtil.objectToMap(payNotify, null);

        URL url = new URL(WXPayConfigure.NOTIFY_TO_JS);

        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {

            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            sb.append(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
            sb.append("&");
        }
        wr.write(sb.toString());
        wr.flush();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
        wr.close();
        rd.close();
    }
}
