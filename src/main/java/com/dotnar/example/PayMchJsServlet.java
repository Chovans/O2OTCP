package com.dotnar.example;

import com.dotnar.api.PayMchAPI;
import com.dotnar.bean.paymch.Unifiedorder;
import com.dotnar.bean.paymch.UnifiedorderResult;
import com.dotnar.util.PayUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 生成WEB JS 支付请求json
 * @author LiYi
 *
 */
public class PayMchJsServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String appid;			//appid
	private String mch_id;      	//微信支付商户号
	private String key;				//API密钥

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//payPackage 的商品信息，总价可以通过前端传入

		Unifiedorder unifiedorder = new Unifiedorder();
		unifiedorder.setAppid(appid);
		unifiedorder.setMch_id(mch_id);
		unifiedorder.setNonce_str(UUID.randomUUID().toString());

		unifiedorder.setBody("商品信息");
		unifiedorder.setOut_trade_no("123456");
		unifiedorder.setTotal_fee("1");//单位分
		unifiedorder.setSpbill_create_ip(request.getRemoteAddr());//IP
		unifiedorder.setNotify_url("http://mydomain.com/test/notify");
		unifiedorder.setTrade_type("JSAPI");//JSAPI，NATIVE，APP，WAP

		UnifiedorderResult unifiedorderResult = null;
		try {
			unifiedorderResult = PayMchAPI.payUnifiedorder(unifiedorder, key);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String json = null;
		try {
			json = PayUtil.generateMchPayJsRequestJson(unifiedorderResult.getPrepay_id(), appid, key,null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//将json 传到jsp 页面
		request.setAttribute("json", json);
		//示例jsp
		request.getRequestDispatcher("pay_example.jsp").forward(request,response);
	}


}
