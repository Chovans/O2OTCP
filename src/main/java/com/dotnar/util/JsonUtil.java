package com.dotnar.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

	private JsonUtil(){}

	public static <T> T parseObject(String json,Class<T> clazz){
		return JSON.parseObject(json, clazz);
	}

	public static String toJSONString(Object object){
		return JSON.toJSONString(object);
	}

	/**
	 * 检测json是否含有error_code和error_msg
	 * 若error_code有且大于0 报错
	 * @param json
	 * @return 	true 微信服务端报错
	 * 			false 数据正常
	 */
	public static Boolean checkIsError(String json){
		System.out.println("==== 检验json: "+ json +" ====");
		JSONObject jsonObject = JSON.parseObject(json);

		try{
			Integer errorCode = jsonObject.getInteger("errcode");
			String errormessage = jsonObject.getString("errmsg");

			if(errorCode == 0 || errormessage.equals("ok")){
				System.out.println("==== 检测错误信息可忽略 ====");
				return false;
			}

			return true;
		}catch (Exception e){
			System.out.println("==== 无法获取error信息，忽略 ====");
			return false;
		}

	}
}
