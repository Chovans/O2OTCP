package com.dotnar.mongo.service;

import com.alibaba.fastjson.JSONArray;
import com.dotnar.bean.mongo.MongoResult;
import com.dotnar.util.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chovans on 15/8/27.
 */
@Service
public class MongoReflectService {


    public static String reflectMethod(String funcName,String jsonObj){

        MongoResult mongoResult = new MongoResult();
        List<Object> objects = new ArrayList<>();

        try{
            Method[] methods = MongoService.class.getMethods();
            Method method = null;
            for(Method m:methods){
                if(m.getName().equals(funcName))
                    method = m;
            }

            if(!StringUtils.isEmpty(jsonObj)){
                JSONArray jsonArray = JSONArray.parseArray(jsonObj);
                for(int idx = 0 ;idx<jsonArray.size();idx++){

                    List<String> params = new ArrayList<>();
                    for(int param = 0;param< method.getParameterCount();param++){

                        params.add(jsonArray.getJSONArray(idx).getString(param));

                    }

                    objects.add(method.invoke(MongoService.class,params.toArray()));

                }
            }

            mongoResult.setContent(objects);

        }catch (Exception e){
            mongoResult.setResult("fail");
            mongoResult.setMsg(e.getMessage());
        }
        return JsonUtil.toJSONString(mongoResult);
    }

}
