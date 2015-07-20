package com.dotnar.controller;

import com.dotnar.util.SMSUtil;
import com.google.gson.Gson;

public class SMSController {
	private static Gson gson = new Gson();
	
	private SMSUtil smsUtil = new SMSUtil();
	
	public String sendSMS(String phone,String code){
		System.out.println(phone + code);
		return gson.toJson(smsUtil.sendSMS(phone, code)).toString();
	}
} 