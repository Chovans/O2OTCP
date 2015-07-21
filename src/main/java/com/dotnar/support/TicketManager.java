package com.dotnar.support;

import com.dotnar.api.TicketAPI;
import com.dotnar.bean.Ticket;

import java.util.*;

/**
 * TicketManager ticket 自动刷新
 * @author LiYi
 *
 */
public class TicketManager {

	private static Map<String,String> ticketMap = new LinkedHashMap<String,String>();

	private static Map<String,Timer> timerMap = new HashMap<String, Timer>();


	/**
	 * 初始化ticket 刷新，每20分钟刷新一次。
	 * 依赖TokenManager
	 * @param appid
	 */
	public static void init(final String appid){
		if(!timerMap.containsKey(appid)){
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					String access_token = TokenManager.getToken(appid);
					Ticket ticket = null;
					try {
						ticket = TicketAPI.ticketGetticket(access_token);
					} catch (Exception e) {
						e.printStackTrace();
						try {
							throw new Exception(e.getMessage());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					ticketMap.put(appid,ticket.getTicket());
				}
			},1000,1000*60*20);
			timerMap.put(appid,timer);
		}
	}

	/**
	 * 取消 ticket 刷新
	 */
	public static void destroyed(){
		for(Timer timer : timerMap.values()){
			timer.cancel();
		}
	}

	/**
	 * 获取 jsapi ticket
	 * @param appid
	 * @return
	 */
	public static String getTicket(final String appid){
		return ticketMap.get(appid);
	}

	/**
	 * 获取第一个appid 的  jsapi ticket
	 * 适用于单一微信号
	 * @param appid
	 * @return
	 */
	public static String getDefaultTicket(){
		Object[] objs = ticketMap.values().toArray();
		return objs.length>0?objs[0].toString():null;
	}

}
