package com.dotnar.controller;

import com.dotnar.bean.paymch.Closeorder;
import com.dotnar.contant.WXPayConfigure;
import com.dotnar.filter.TCPFilter;
import com.dotnar.support.TicketManager;
import com.dotnar.support.TokenManager;
import com.dotnar.wx.service.*;
import hprose.server.HproseTcpServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;

/**
 * 微信支付初始化
 * Created by chovans on 15/7/17.
 */
@Controller
public class WXPayController implements InitializingBean {
    private Log logger = LogFactory.getLog(WXPayController.class);
    public static HproseTcpServer server = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            if (server == null) {
                //Hprose
                server = new HproseTcpServer(WXPayConfigure.WXPAY);
                //jssdk初始化累
                server.add(JSSDKConfigService.class,true);
                //预支付服务类
                server.add(UnifiedorderService.class,true);
                //查询订单服务类
                server.add(OrderQueryService.class,true);
                //退款服务类
                server.add(RefundService.class,true);
                //关闭订单服务类
                server.add(Closeorder.class,true);
                //下载对账单服务类
                server.add(DownloadBillService.class,true);
                //授权服务类
                server.add(Oauth2Service.class,true);
                //添加拦截器
                //server.setFilter(new TCPFilter());
                server.setEnabledThreadPool(true);
                server.start();
                System.out.println("===== 微信支付监听端口开启 =====");
                logger.info("===== 微信支付监听端口开启 =====");


                long begin = System.currentTimeMillis();
                //获取当前账号下的token和ticket
                TokenManager.init(WXPayConfigure.DEFAULT_APPID, WXPayConfigure.DEFAULT_SECRET);
                while (TokenManager.getToken(WXPayConfigure.DEFAULT_APPID) == null) {
                    Thread.sleep(100);
                }
                TicketManager.init(WXPayConfigure.DEFAULT_APPID);

                while (TicketManager.getTicket(WXPayConfigure.DEFAULT_APPID) == null) {
                    Thread.sleep(100);
                }
                long end = System.currentTimeMillis();
                System.out.println("===== 获取token和ticket共耗时："+(end-begin)+"毫秒 ====");
                System.out.println("===== token=" + TokenManager.getToken(WXPayConfigure.DEFAULT_APPID) + " ====");
                System.out.println("===== ticket="+ TicketManager.getTicket(WXPayConfigure.DEFAULT_APPID)+" ====");System.out.println("===== 获取token和ticket共耗时："+(end-begin)+"毫秒 ====");
                logger.info("===== 获取token和ticket共耗时：" + (end - begin) + "毫秒 ====");
                logger.info("===== token=" + TokenManager.getToken(WXPayConfigure.DEFAULT_APPID) + " ====");
                logger.info("===== ticket="+ TicketManager.getTicket(WXPayConfigure.DEFAULT_APPID) + " ====");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
