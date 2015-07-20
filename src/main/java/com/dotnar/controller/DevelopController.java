package com.dotnar.controller;

import com.dotnar.support.TicketManager;
import com.dotnar.support.TokenManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 开发所需接口
 * 获取当前服务器保存的token和ticket
 * Created by chovans on 15/7/20.
 */
@Controller
public class DevelopController {

    @RequestMapping("getToken.do")
    @ResponseBody
    public String getToken(String appid){
        return TokenManager.getToken(appid);
    }
    @RequestMapping("getTicket.do")
    @ResponseBody
    public String getTicket(String appid){
        return TicketManager.getTicket(appid);
    }
}
