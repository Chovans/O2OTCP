package com.dotnar.service;

import com.dotnar.util.SMSTCPUtil;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

/**
 * Created by chovans on 15/7/14.
 */
@Service
public class SMSService {

    private static Gson gson = new Gson();

    //短信工具类
    public static SMSTCPUtil smsUtil;
    static {
        smsUtil = new SMSTCPUtil();
    }
    /**
     * 发送短信
     * @param phone
     * @param templateId
     * @param params 字符串数组  e.g: arg1,arg2,arg3ß
     * @return
     */
    public static String sendSMS(String phone,String templateId, String params){
        String result = gson.toJson(smsUtil.sendSMS(phone, templateId, params.split(",")));
        return result;
    }
}
