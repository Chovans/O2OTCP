package com.dotnar.client;

import com.dotnar.util.JsonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

public class LocalHttpClient {

	protected static HttpClient httpClient = HttpClientFactory.createHttpClient(100,10);

	private static Map<String,HttpClient> httpClient_mchKeyStore = new HashMap<String, HttpClient>();
	public static void init(int maxTotal,int maxPerRoute) throws Exception {
		httpClient = HttpClientFactory.createHttpClient(maxTotal,maxPerRoute);
	}

	/**
	 * 初始化   MCH HttpClient KeyStore
	 * @param keyStoreName  keyStore 名称
	 * @param keyStoreFilePath 私钥文件路径
	 * @param mch_id
	 * @param maxTotal
	 * @param maxPerRoute
	 */
	public static void initMchKeyStore(String keyStoreName,String keyStoreFilePath,String mch_id,int maxTotal,int maxPerRoute) throws Exception {
		try {
			KeyStore keyStore = KeyStore.getInstance(keyStoreName);
			 FileInputStream instream = new FileInputStream(new File(keyStoreFilePath));
			 keyStore.load(instream,mch_id.toCharArray());
			 instream.close();
			 HttpClient httpClient = HttpClientFactory.createKeyMaterialHttpClient(keyStore, mch_id, new String[]{"TLSv1"}, maxTotal, maxPerRoute);
			 httpClient_mchKeyStore.put(mch_id, httpClient);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}


	public static HttpResponse execute(HttpUriRequest request) throws Exception {
		try {
			return httpClient.execute(request);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public static <T> T execute(HttpUriRequest request,ResponseHandler<T> responseHandler) throws Exception {
		try {
			T t = httpClient.execute(request, responseHandler);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 数据返回自动JSON对象解析
	 * @param request
	 * @param clazz
	 * @return
	 */
	public static <T> T executeJsonResult(HttpUriRequest request,Class<T> clazz) throws Exception {
		return execute(request,JsonResponseHandler.createResponseHandler(clazz));
	}

	/**
	 * 数据返回自动XML对象解析
	 * @param request
	 * @param clazz
	 * @return
	 */
	public static <T> T executeXmlResult(HttpUriRequest request,Class<T> clazz) throws Exception {
		return execute(request,XmlResponseHandler.createResponseHandler(clazz));
	}

	/**
	 * MCH keyStore 请求 数据返回自动XML对象解析
	 * @param mch_id
	 * @param request
	 * @param clazz
	 * @return
	 */
	public static <T> T keyStoreExecuteXmlResult(String mch_id,HttpUriRequest request,Class<T> clazz) throws Exception {
		try {
			return httpClient_mchKeyStore.get(mch_id).execute(request,XmlResponseHandler.createResponseHandler(clazz));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
}
