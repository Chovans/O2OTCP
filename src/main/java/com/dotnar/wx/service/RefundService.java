package com.dotnar.wx.service;

import com.alibaba.fastjson.JSON;
import com.dotnar.api.PayMchAPI;
import com.dotnar.bean.paymch.Refundquery;
import com.dotnar.bean.paymch.RefundqueryResult;
import com.dotnar.bean.paymch.SecapiPayRefund;
import com.dotnar.bean.paymch.SecapiPayRefundResult;
import com.dotnar.dao.SecapiPayRefundRepository;
import com.dotnar.exception.WXPayExceptioin;
import com.dotnar.util.JsonUtil;
import net.sf.json.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 退款相关服务
 * Created by chovans on 15/7/18.
 */
@Service
public class RefundService {

    private static SecapiPayRefundRepository secapiPayRefundRepository;

    @Autowired
    public RefundService(@Qualifier("SecapiPayRefundRepository")SecapiPayRefundRepository secapiPayRefundRepository) {
        this.secapiPayRefundRepository = secapiPayRefundRepository;
    }

    /**
     * 申请退款
     * 参数：    appid
     *          mch_id
     *          device_info
     *          transaction_id  微信订单号
     *          out_trade_no    商户系统内部的订单号
     *          out_refund_no   商户系统内部的退款单号
     *          total_fee       订单总金额
     *          refund_fee      退款总金额
     *          refund_fee_type 默认人民币：CNY
     *          op_used_id      操作员帐号
     *
     * @param jsonObj
     * @param key
     * @return
     */
    public static String refund(String jsonObj,String key){
        SecapiPayRefund secapiPayRefund = JsonUtil.parseObject(jsonObj,SecapiPayRefund.class);
        secapiPayRefund.setNonce_str(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20));

        SecapiPayRefundResult secapiPayRefundResult = null;
        try{
            secapiPayRefundResult = PayMchAPI.secapiPayRefund(secapiPayRefund,key);
            //记录退款申请
            secapiPayRefundRepository.save(secapiPayRefund);
        }catch (Exception e){
            return JsonUtil.toJSONString(new WXPayExceptioin(e.getMessage()));
        }

        return JsonUtil.toJSONString(secapiPayRefundResult);
    }

    /**
     * 查询退款
     * 提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款20分钟内到账，银行卡支付的退款3个工作日后重新查询退款状态。
     * 参数：appid
     *      mch_id
     *      device_info
     *      transaction_id  微信订单号
     *      out_trade_no    商户系统内部的订单号
     *      out_refund_no   商户退款单号
     *      refund_id       微信退款单号
     * @param jsonObj
     * @param key
     * @return
     */
    public static String refundQuery(String jsonObj,String key){
        Refundquery refundquery = JsonUtil.parseObject(jsonObj,Refundquery.class);
        refundquery.setNonce_str(UUID.randomUUID().toString().replaceAll("-", ""));

        RefundqueryResult refundqueryResult = null;
        try{
            refundqueryResult  = PayMchAPI.payRefundquery(refundquery,key);
        }catch (Exception e){
            return JsonUtil.toJSONString(new WXPayExceptioin(e.getMessage()));
        }

        return JsonUtil.toJSONString(refundqueryResult);
    }

    /**
     * 撤销订单？？？没有查到相关API
     *
     *
     * @param jsonObj
     * @param key
     * @return
     */
    public static String reverse(String jsonObj,String key){
        return null;
    }
}
