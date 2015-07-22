package com.dotnar.wx.service;

import com.dotnar.api.PayMchAPI;
import com.dotnar.bean.paymch.MchOrderInfoResult;
import com.dotnar.bean.paymch.MchOrderquery;
import com.dotnar.exception.WXPayException;
import com.dotnar.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 查询订单
 * Created by chovans on 15/7/18.
 */
@Service
public class OrderQueryService {
    /**
     * 查询订单，包括参数
     * appid,mch_id,transaction_id,out_trade_no
     * @param jsonObj
     * @param key
     * @return
     */
    public static String queryOrder(String jsonObj,String key){
        MchOrderquery orderquery = JsonUtil.parseObject(jsonObj,MchOrderquery.class);
        orderquery.setNonce_str(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20));

        MchOrderInfoResult result = null;

        try{
            result = PayMchAPI.payOrderquery(orderquery,key);
        }catch (Exception e){
            return JsonUtil.toJSONString(new WXPayException(e.getMessage()));
        }

        return JsonUtil.toJSONString(result);
    }
}
