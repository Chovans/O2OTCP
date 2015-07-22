package com.dotnar.wx.service;

import com.dotnar.api.PayMchAPI;
import com.dotnar.bean.paymch.Closeorder;
import com.dotnar.bean.paymch.MchBaseResult;
import com.dotnar.exception.WXPayException;
import com.dotnar.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 关闭订单
 * Created by chovans on 15/7/18.
 */
@Service
public class CloseOrderService {

    /**
     * 关闭订单
     * 参数有：appid,mcu_id,out_trade_no
     * @param jsonObj
     * @param key
     * @return
     */
    public static String closeOrder(String jsonObj,String key){

        MchBaseResult result = null;
        try{
            Closeorder closeorder = JsonUtil.parseObject(jsonObj,Closeorder.class);
            closeorder.setNonce_str(UUID.randomUUID().toString().replaceAll("-","").substring(0,20));

            result = PayMchAPI.payCloseorder(closeorder,key);
        }catch (Exception e){
            return JsonUtil.toJSONString(new WXPayException(e.getMessage()));
        }


        return JsonUtil.toJSONString(result);

    }
}
