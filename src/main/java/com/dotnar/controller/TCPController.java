package com.dotnar.controller;

import com.dotnar.contant.TCPConfig;
import com.dotnar.service.CheckCodeService;
import com.dotnar.service.SMSService;
import hprose.common.HproseContext;
import hprose.common.HproseFilter;
import hprose.server.HproseTcpServer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;

import java.nio.ByteBuffer;

/**
 * 工具（发送短信，获取验证码base64）初始化
 * Created by chovans on 15/7/14.
 */
@Controller
public class TCPController implements InitializingBean {

    private static Logger logger = Logger.getLogger(TCPController.class);
    public static HproseTcpServer server = null;

    @Override
    public void afterPropertiesSet() {
        try {
            if (server == null) {
                //Hprose
                server = new HproseTcpServer(TCPConfig.COMMONTCP);
                server.add(SMSService.class,true);
                server.add(CheckCodeService.class,true);
                server.setEnabledThreadPool(true);
                server.start();
                System.out.println("===== 工具监听端口开启 =====");
                logger.info("===== 工具监听端口开启 =====");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
