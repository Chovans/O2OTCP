package com.dotnar.wx.service;

import com.dotnar.api.SnsAPI;
import com.dotnar.bean.SnsToken;
import com.dotnar.bean.User;
import com.dotnar.bean.sns.Oauth2;
import com.dotnar.dao.OauthRepository;
import com.dotnar.exception.WXPayException;
import com.dotnar.support.Oauth2Manager;
import com.dotnar.util.JsUtil;
import com.dotnar.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 授权服务类
 * Created by chovans on 15/7/20.
 */
@Service
public class Oauth2Service {

    private static Logger logger = LoggerFactory.getLogger(Oauth2Service.class);

    private static OauthRepository oauthRepository = null;

    @Autowired
    public Oauth2Service(@Qualifier("OauthRepository") OauthRepository oauthRepository) {
        this.oauthRepository = oauthRepository;
    }

    /**
     * 通过code换取网页授权access_token
     *
     * @param appid
     * @param appSecret
     * @param code
     * @return
     */
    public static String oauth2Code(String appid, String appSecret, String code) {

        try {
            if (JsUtil.isEmpty(appid, appSecret, code) != null) {
                return JsonUtil.toJSONString(new WXPayException(JsUtil.isEmpty(appid, appSecret, code) + "is null"));
            }

            logger.info("==== 获取用户信息：收到的appid=" + appid + " =====");
            logger.info("==== 获取用户信息：收到的appSecret=" + appSecret + " =====");
            logger.info("==== 获取用户信息：收到的code=" + code + " =====");

            SnsToken snsToken = SnsAPI.oauth2AccessToken(appid, appSecret, code);

            logger.info("==== 获取用户信息：收到的snstoken=" + JsonUtil.toJSONString(snsToken) + " =====");

            //如果没报错，则存入refreshToken和access_Token
            if (StringUtils.isEmpty(snsToken.getErrmsg())) {
                /**
                 *  openid = refreshToken
                 *  openid = accessToken
                 */
                logger.info("==== 存入token ====");
                Oauth2Manager.setRefreshToken(snsToken.getOpenid(), snsToken.getRefresh_token());
                Oauth2Manager.setAccessToken(snsToken.getOpenid(), snsToken.getRefresh_token());
                //Oauth2Manager.setUnionId(snsToken.getOpenid(), snsToken.getUnionid());

                //存入数据库
                Oauth2 oauth2 = new Oauth2();
                BeanUtils.copyProperties(snsToken, oauth2);
                oauth2.setAppid(appid);
                oauthRepository.save(oauth2);

            }

            return JsonUtil.toJSONString(snsToken);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.toJSONString(new WXPayException(e.getMessage()));
        }
    }

    /**
     * 根据appid,openid获取用户个人信息
     * 需scope为 snsapi_userinfo
     *
     * @param appid
     * @return
     */
    public static String oauth2AccessToken(String appid, String openid) {

        try {
            if (JsUtil.isEmpty(appid, openid) != null) {
                return JsonUtil.toJSONString(new WXPayException(JsUtil.isEmpty(appid, openid) + "is null"));
            }

            logger.info("==== appid:" + appid + " =====");
            logger.info("==== openid:" + openid + " =====");

            //获取accessToken
            String accessToken = null;
            try {
                accessToken = Oauth2Manager.getAccessToken(appid, openid);
                logger.info("==== Oauth2获取的Token:" + accessToken + " ====");
            } catch (Exception e) {
                e.printStackTrace();
                return JsonUtil.toJSONString(new WXPayException("get access_token fail"));
            }

            if (accessToken == null) {
                logger.info("refresh_token time out");
                return JsonUtil.toJSONString(new WXPayException("refresh_token time out"));
            }

            User user = SnsAPI.userinfo(accessToken, openid, "zh_CN");
//            user.setUnionid(Oauth2Manager.getUnionId(user.getOpenid()));
            Oauth2Manager.setUnionId(user.getOpenid(), user.getUnionid());

            logger.info("==== 获取用户信息：" + JsonUtil.toJSONString(user) + " ====");

            if (!StringUtils.isEmpty(openid)) {
                logger.info("==== 更新授权ACCESS_TOKEN ====");

                Oauth2 oauth2 = oauthRepository.findOne(openid);
                oauth2.setAccess_token(accessToken);
                if(StringUtils.isEmpty(oauth2.getUnionid())){
                    oauth2.setUnionid(user.getUnionid());
                }
                oauthRepository.save(oauth2);
            }
            return JsonUtil.toJSONString(user);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.toJSONString(new WXPayException(e.getMessage()));
        }
    }
}
