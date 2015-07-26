package com.dotnar.service;

import com.dotnar.util.CheckCodeUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 * Created by chovans on 15/7/14.
 */
@Service
@EnableAsync
public class CheckCodeService {
    //验证码生成工具类
    private static CheckCodeUtil checkCodeUtil =null;
    static {
        checkCodeUtil = new CheckCodeUtil();
    }
    /**
     * 获取验证码图片
     * @param code 验证码
     * @return
     */
    @Async
    public static String getCheckCode(String code) {
        return checkCodeUtil.crimg(code);
    }
}
