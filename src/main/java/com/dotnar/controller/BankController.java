package com.dotnar.controller;

import com.dotnar.contant.TCPConfig;
import com.dotnar.mongo.service.MongoReflectService;
import com.dotnar.mongo.service.MongoService;
import hprose.server.HproseTcpServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;

/**
 * @author chovans on 15/8/22.
 */
@Controller
public class BankController implements InitializingBean {
    private static HproseTcpServer server = null;

    @Override
    public void afterPropertiesSet() throws Exception {

        try {
            if (server == null) {
                //Hprose
                server = new HproseTcpServer(TCPConfig.BANKTCP);
//                server.add(MongoService.class,true);
//                server.setEnabledThreadPool(true);
//                server.start();
                System.out.println("==== bank support start ====");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
