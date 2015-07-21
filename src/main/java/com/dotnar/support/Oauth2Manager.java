package com.dotnar.support;

import com.dotnar.api.SnsAPI;
import com.dotnar.bean.BaseResult;
import com.dotnar.bean.SnsToken;
import com.dotnar.bean.sns.Oauth2;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 网页授权access_token
 * Created by chovans on 15/7/21.
 */
public class Oauth2Manager {
    private static Map<String,String> refreshTokenMap = new LinkedHashMap<String,String>();
    private static Map<String,String> accessTokenMap = new LinkedHashMap<String,String>();
    private static Map<String,String> openIdUnionIdMap = new HashMap<String, String>();

    public static void setRefreshToken(String openid,String refreshToken){
        refreshTokenMap.put(openid,refreshToken);
    }
    public static void setAccessToken(String openid,String accessToken){
        accessTokenMap.put(openid,accessToken);
    }
    public static void setUnionId(String openid,String unionId){
        openIdUnionIdMap.put(openid,unionId);
    }
    public static String getUnionId(String openid){
        return openIdUnionIdMap.get(openid);
    }

    /**
     * 程序启动时从数据库存入内存
     * @param oauth2s
     */
    public static void init(List<Oauth2> oauth2s){
        System.out.println("==== Oauth2长度：" + oauth2s.size() +" ====");
        for(Oauth2 oauth2:oauth2s){
            refreshTokenMap.put(oauth2.getOpenid(),oauth2.getRefresh_token());
            accessTokenMap.put(oauth2.getOpenid(),oauth2.getAccess_token());
            openIdUnionIdMap.put(oauth2.getOpenid(),oauth2.getUnionid());
        }
    }

    /**
     * @param appid
     * @param openid
     * @return
     */
    public static String getAccessToken(String appid,String openid) throws Exception {

        if(accessTokenMap.get(openid) == null)
            return null;

        //检验是否可用
        BaseResult result = SnsAPI.checkOauth2AccessToken(accessTokenMap.get(openid),openid);

        //accessToken可用时
        if(result.getErrmsg().equals("ok")){
            return accessTokenMap.get(openid);
        }
        //accessToken不可用时
        else
        {
            //重新获取accessToken
            SnsToken snsToken = SnsAPI.refreshOauth2AccessToken(appid, refreshTokenMap.get(openid));

            //如果获取到accessToken的情况下，则表明refreshToken未过期
            if(!StringUtils.isEmpty(snsToken.getAccess_token())){
                accessTokenMap.put(openid,snsToken.getAccess_token());
                return snsToken.getAccess_token();
            }
        }

        //refreshToken过期，返回null
        return null;
    }
}
