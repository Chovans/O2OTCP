package com.dotnar.util;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;
import org.omg.CORBA.BAD_CONTEXT;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chovans on 15/8/22.
 */
public class MongoUtil {

    /**
     * 将json字符串转换成数据库对象（_id）
     *
     * @param jsonObj
     * @return
     */
    public static BasicDBObject transProperties(String jsonObj) {
        BasicDBObject basicDbObject = (BasicDBObject) JSON.parse(jsonObj);

        //若有id，转换成id对象重新复制(暂用字符串)
//        if (basicDbObject.get("_id") != null)
//            basicDbObject.append("_id", new ObjectId(basicDbObject.get("_id").toString()));

        return basicDbObject;
    }

}
