package com.dotnar.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dotnar.bean.mongo.MongoResult;
import org.bson.types.ObjectId;
import org.springframework.util.*;

import java.util.Date;

public class JsonUtil {

    private JsonUtil() {
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public static String toJSONString(Object object) {
        System.out.println("==== json("+new Date()+"):" + JSON.toJSONString(object) + "====");
        return JSON.toJSONString(object);
    }

    /**
     * 解决mongo中存在2中主键id的情况。ObjectId和Id（String）
     * @param mongoResult
     * @return
     */
    public static String mongoResultToString(MongoResult mongoResult) {

        try {
            JSONArray array = JSONArray.parseArray(JsonUtil.toJSONString(mongoResult.getContent()));
            //if array
            if (array != null)
                for (int i = 0; i < array.size(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    if (item.get("_id") != null && !(item.get("_id") instanceof String)) {
                        item.put("_id", JsonUtil.parseObject(item.get("_id").toString(), ObjectId.class).toHexString());
                    }
                }
            mongoResult.setContent(array);
        } catch (Exception e) {
            //if single
            JSONObject jsonObject = JSONObject.parseObject(JsonUtil.toJSONString(mongoResult.getContent()));
            if (jsonObject != null && jsonObject.get("_id") != null &&
                    !(jsonObject.get("_id") instanceof String)) {
                jsonObject.put("_id", JsonUtil.parseObject(jsonObject.get("_id").toString(), ObjectId.class).toHexString());
            }
            mongoResult.setContent(jsonObject);
        }

        return JSON.toJSONString(mongoResult);
    }

    /**
     * 检测json是否含有error_code和error_msg
     * 若error_code有且大于0 报错
     *
     * @param json
     * @return true 微信服务端报错
     * false 数据正常
     */
    public static Boolean checkIsError(String json) {
        System.out.println("==== 检验json: " + json + " ====");
        JSONObject jsonObject = JSON.parseObject(json);

        try {
            Integer errorCode = jsonObject.getInteger("errcode");
            String errormessage = jsonObject.getString("errmsg");

            if (errorCode == 0 || errormessage.equals("ok")) {
                System.out.println("==== 见到到errorcode字段，但可以忽略 ====");
                return false;
            }

            return true;
        } catch (Exception e) {
            System.out.println("==== 没有error信息 ====");
            return false;
        }

    }
}
