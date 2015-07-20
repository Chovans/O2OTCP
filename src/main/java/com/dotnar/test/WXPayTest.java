package com.dotnar.test;

import com.dotnar.contant.TCPConfig;
import com.dotnar.contant.WXPayConfigure;
import hprose.client.HproseTcpClient;

/**
 * payRequestJson
 * Created by chovans on 15/7/13.
 */
public class WXPayTest {
    public static void main(String[] args) {
        final HproseTcpClient client = new HproseTcpClient(WXPayConfigure.WXPAY);
        try {
            String json = "{" +
                    "                'appid': 'wxcf74f930098faee1'," +
                    "                    'mch_id': '1233472902'," +
                    "                    'device_info': 'device_info'," +
                    "                    'body': 'test body'," +
                    "                    'detail': 'test detail'," +
                    "                    'attach': 'test attach'," +
                    "                    'out_trade_no': 'test-out-trade-no-1'," +
                    "                    'fee_type': ''," +
                    "                    'total_fee': '1'," +
                    "                    'spbill_create_ip': '127.0.0.0'," +
                    "                    'goods_tag': 'test-goods-tag'," +
                    "                    'trade_type': 'JSAPI'," +
                    "                    'product_id': 'test-product-id'," +
                    "                    'openid': 'o_QrEjjIkQ9URa1YnZ8gWi6G2AWs'" +
                    "            }";
            String key = "HXCU71nGNU5O4hvQlGSXEuMEMTWQs0HW";
            Object object = client.invoke("payRequestJson", new Object[]{json,key});
            System.out.println(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
