package com.dotnar.util;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.dotnar.contant.ContantString;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Set;


//@Component
public class SMSTCPUtil {
	
	@SuppressWarnings({ "unchecked", "unused" })
	public HashMap<String, Object> sendSMS(String phone,String templateId,String...params){

		if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(templateId))
			return null;

		HashMap<String, Object> result = null;

		//初始化SDK
		CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
		
		restAPI.init(ContantString.url, ContantString.port);
		restAPI.setAccount(ContantString.accountSid, ContantString.token);
		restAPI.setAppId(ContantString.appId);
		
		//******************************注释****************************************************************
		//*调用发送模板短信的接口发送短信                                                                  *
		//*参数顺序说明：                                                                                  *
		//*第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号                          *
		//*第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。    *
		//*系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
		//*第三个参数是要替换的内容数组。																														       *
		//**************************************************************************************************
		
		//**************************************举例说明***********************************************************************
		//*假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为           *
		//*result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});																		  *
		//*则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入     *
		//*********************************************************************************************************************

		result = restAPI.sendTemplateSMS(phone,templateId,params);

		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
			}
		}else{
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			return result;
		}
		return result;
	}
}
