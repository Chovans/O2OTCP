package com.dotnar.test;

import hprose.client.HproseHttpClient;

/**
 * 测试咯
 * Created by chovans on 15/7/23.
 */
public class ServletTest {
    public static void main(String[] args) throws Exception{
        HproseHttpClient client = new HproseHttpClient();
        client.useService("http://localhost:8080/checkCode");
        String result = (String) client.invoke("getCheckCode", new Object[] { "Hprose" });
        System.out.println(result);
    }
}
