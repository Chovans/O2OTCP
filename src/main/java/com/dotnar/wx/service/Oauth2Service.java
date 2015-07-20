package com.dotnar.wx.service;

import com.dotnar.api.SnsAPI;
import com.dotnar.bean.SnsToken;
import com.dotnar.bean.sns.Code;
import com.dotnar.contant.WXPayConfigure;
import com.dotnar.exception.WXPayExceptioin;
import com.dotnar.util.JsonUtil;
import org.springframework.stereotype.Service;

/**
 * 授权服务类
 * Created by chovans on 15/7/20.
 */
@Service
public class Oauth2Service {

    /**
     * 根据code获取openid等参数
     *  e.g. {"code":"xxxxxx"}
     * @param jsonObj
     * @return
     */
    public static String oauth2AccessToken(String jsonObj){
        Code code = JsonUtil.parseObject(jsonObj,Code.class);
        if(org.springframework.util.StringUtils.isEmpty(code.getCode())){
            return JsonUtil.toJSONString(new WXPayExceptioin("code is empty!!"));
        }

        System.out.println("==== 获取用户信息：收到的code="+code.getCode()+" =====");

        try {
            SnsToken snsToken = SnsAPI.oauth2AccessToken(WXPayConfigure.DEFAULT_APPID, WXPayConfigure.DEFAULT_SECRET, code.getCode());
            System.out.println(JsonUtil.toJSONString(snsToken));
            return JsonUtil.toJSONString(snsToken);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.toJSONString(new WXPayExceptioin(e.getMessage()));
        }
    }
}
