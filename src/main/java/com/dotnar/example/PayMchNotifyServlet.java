package com.dotnar.example;

import com.dotnar.bean.paymch.MchNotifyXml;
import com.dotnar.bean.paymch.MchPayNotify;
import com.dotnar.util.ExpireSet;
import com.dotnar.util.SignatureUtil;
import com.dotnar.util.XMLConverUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 支付回调通知
 * @author LiYi
 *
 */
public class PayMchNotifyServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String key;	//mch key

	//重复通知过滤  时效60秒
    private static ExpireSet<String> expireSet = new ExpireSet<String>(60);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取请求数据
		MchPayNotify payNotify = null;
		try {
			payNotify = XMLConverUtil.convertToObject(MchPayNotify.class, request.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//已处理 去重
		if(expireSet.contains(payNotify.getTransaction_id())){
			return;
		}
		//签名验证
		try {
			if(SignatureUtil.validateAppSignature(payNotify,key)){
                expireSet.add(payNotify.getTransaction_id());
                MchNotifyXml baseResult = new MchNotifyXml();
                baseResult.setReturn_code("SUCCESS");
                baseResult.setReturn_msg("OK");
                response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
            }else{
                MchNotifyXml baseResult = new MchNotifyXml();
                baseResult.setReturn_code("FAIL");
                baseResult.setReturn_msg("ERROR");
                response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
