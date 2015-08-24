package com.dotnar.controller;

import com.dotnar.contant.TCPConfig;
import com.dotnar.filter.MongoFilter;
import com.dotnar.mongo.service.MongoService;
import hprose.server.HproseTcpServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author chovans on 15/8/22.
 */
@Controller
public class MongoDBController  implements InitializingBean {
    private static HproseTcpServer server = null;

    @Override
    public void afterPropertiesSet() throws Exception {

        try {
            if (server == null) {
                //Hprose
                server = new HproseTcpServer(TCPConfig.MONGODBTCP);
                server.add(MongoService.class,true);
                server.setEnabledThreadPool(true);
                server.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
