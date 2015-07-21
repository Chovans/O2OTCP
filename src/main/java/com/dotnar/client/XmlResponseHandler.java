package com.dotnar.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import com.dotnar.util.XMLConverUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XmlResponseHandler{

	private static Map<String, ResponseHandler<?>> map = new HashMap<String, ResponseHandler<?>>();

	@SuppressWarnings("unchecked")
	public static <T> ResponseHandler<T> createResponseHandler(final Class<T> clazz)throws Exception{
		if(map.containsKey(clazz.getName())){
			return (ResponseHandler<T>)map.get(clazz.getName());
		}else{
			ResponseHandler<T> responseHandler = new ResponseHandler<T>() {
				@Override
				public T handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
	                if (status >= 200 && status < 300) {
	                    HttpEntity entity = response.getEntity();
	                    String str = EntityUtils.toString(entity);
						System.out.println("==== 从服务器获取xml：" + str +" ====");
						try{

	                   		return XMLConverUtil.convertToObject(clazz,new String(str.getBytes("iso-8859-1"),"utf-8"));
						}catch (Exception e){
							try {
								throw new Exception(e.getMessage());
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
	                } else {
	                    throw new ClientProtocolException("Unexpected response status: " + status);
	                }

					return null;
				}
			};
			map.put(clazz.getName(), responseHandler);
			return responseHandler;
		}
	}

}
