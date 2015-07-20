package com.dotnar.wx.service;

import com.dotnar.api.PayMchAPI;
import com.dotnar.bean.paymch.MchBaseResult;
import com.dotnar.bean.paymch.MchDownloadbill;
import com.dotnar.exception.WXPayExceptioin;
import com.dotnar.util.JsonUtil;
import net.sf.json.util.JSONUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 下载对账单
 * Created by chovans on 15/7/18.
 */
@Service
public class DownloadBillService {

    /**
     * 下载对账单
     * 参数：appid
     *      mch_id
     *      device_info     微信支付分配的终端设备号，填写此字段，只下载该设备号的对账单
     *      //out_trade_no    暂不需要
     *      bill_date       下载对账单的日期，格式：20140603
     *      bill_type       ALL，返回当日所有订单信息，默认值
     *                      SUCCESS，返回当日成功支付的订单
     *                      REFUND，返回当日退款订单
     *                      REVOKED，已撤销的订单
     * @param jsonObj
     * @param key
     * @return
     */
    public static String download(String jsonObj,String key){
        MchDownloadbill mchDownloadbill = JsonUtil.parseObject(jsonObj, MchDownloadbill.class);
        mchDownloadbill.setNonce_str(UUID.randomUUID().toString().replaceAll("-","").substring(0,20));
        MchBaseResult mchBaseResult = null;
        try{
            mchBaseResult = PayMchAPI.payDownloadbill(mchDownloadbill, key);
        }catch (Exception e){
            return JsonUtil.toJSONString(new WXPayExceptioin(e.getMessage()));
        }
        return JsonUtil.toJSONString(mchBaseResult);
    }
}
