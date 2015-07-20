package com.dotnar.contant;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 14:40
 * 这里放置各种配置数据
 */
public class WXPayConfigure {
	//调试相关参数start
	public static Boolean IS_DEBUG = false;
	public static Header xmlHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_XML.toString());
	public static String DEBUG_URL = "http://121.40.72.93:7070/";
	//调试相关参数end

	//默认的appid（程序启动后自动获取token和ticket）
	public static final String DEFAULT_APPID = "wxcf74f930098faee1";
	//默认的secret（程序启动后自动获取token和ticket）
	public static final String DEFAULT_SECRET = "77ff4d4b3f861ebacfe1de20c497c411";


	//微信支付端口
	public static final String WXPAY = "tcp://0.0.0.0:7073";
	//微信回调地址
	public static final String NOTIFY_URL="http://www.dotnar.com:7070/notify.do";
	//微信支付回调端口
	public static final String NOTIFY_TO_JS = "http://api.dotnar.com/wx/success_order_notify/from/jjy/java_tools/weixin";
	//微信支付中订单有效时间，单位：分钟
	public static final Integer EFFECTIVE_TIME=10;

	//sdk的版本号
//	public static final String sdkVersion = "java sdk 1.0.1";

	//这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
	// 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
	// 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改

//	public static String key = "HXCU71nGNU5O4hvQlGSXEuMEMTWQs0HW";

	//微信分配的公众号ID（开通公众号之后可以获取到）
//	public static String appID = "wxcf74f930098faee1";

	//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
//	public static String mchID = "1233472902";

	//受理模式下给子商户分配的子商户号
//	public static String subMchID = "";

	//HTTPS证书的本地路径
//	public static String certLocalPath = "/root/O2O/my_modules/wx/.pem/apiclient_cert.p12";
	public static String certLocalPath = "C://apiclient_cert.p12";
//	public static String certLocalPath = "/Users/chovans/Desktop/temp/apiclient_cert.p12";

	//HTTPS证书密码，默认密码等于商户号MCHID
	public static String certPassword = "1233472902";

	//是否使用异步线程的方式来上报API测速，默认为异步模式
//	public static boolean useThreadToDoReport = true;

	//机器IP
	public static String ip = "121.40.72.93";

	public static String UNI_FIED_ORDER="https://api.mch.weixin.qq.com/pay/unifiedorder";



}
