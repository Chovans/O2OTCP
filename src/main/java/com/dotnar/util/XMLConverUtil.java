package com.dotnar.util;

import com.dotnar.bean.BaseResult;
import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import org.springframework.util.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * XML 数据接收对象转换工具类
 * @author LiYi
 *
 */
public class XMLConverUtil{

	private static Map<Class<?>,Unmarshaller> uMap = new HashMap<Class<?>,Unmarshaller>();
	private static Map<Class<?>,Marshaller> mMap = new HashMap<Class<?>,Marshaller>();

	/**
	 * 检验返回的xml中error_code和error_msg是否正常
	 * @param xml
	 * @return 	true 微信端返回错误信息
	 * 			false 微信端返回信息正常
	 */
	public static Boolean checkIsError(String xml){
		try {
			BaseResult baseResult = convertToObject(BaseResult.class,xml);

			//检验返回xml中是否含有error字段，若没有则返回false
			if(org.springframework.util.StringUtils.isEmpty(baseResult.getErrcode())){
				return false;
			}
			//检验若返回的xml中，error_code=0或者error_msg=“ok”则表明正常，无错，返回false
			if(baseResult.getErrcode().equals("0") || baseResult.getErrmsg().equals("ok")){
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//在解析过程中没用解析到error字段可能报错，表明没有error，返回false
			return false;
		}
		//执行到此表明微信服务端返回错误信息，返回true
		return true;
	}

	/**
	 * XML to Object
	 * @param <T>
	 * @param clazz
	 * @param xml
	 * @return
	 */
	public static <T> T convertToObject(Class<T> clazz,String xml) throws Exception {
		return convertToObject(clazz,new StringReader(xml));
	}

	/**
	 * XML to Object
	 * @param <T>
	 * @param clazz
	 * @param inputStream
	 * @return
	 */
	public static <T> T convertToObject(Class<T> clazz,InputStream inputStream) throws Exception {
		return convertToObject(clazz,new InputStreamReader(inputStream));
	}

	/**
	 * XML to Object
	 * @param <T>
	 * @param clazz
	 * @param reader
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertToObject(Class<T> clazz,Reader reader) throws Exception {
		try {
			if(!uMap.containsKey(clazz)){
				JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				uMap.put(clazz,unmarshaller);
			}
			return (T)uMap.get(clazz).unmarshal(reader);
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Object to XML
	 * @param object
	 * @return
	 */
	public static String convertToXML(Object object) throws Exception {
		try {
			if(!mMap.containsKey(object.getClass())){
				JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	            marshaller.setProperty(CharacterEscapeHandler.class.getName(), new CharacterEscapeHandler() {
	                public void escape(char[] ac, int i, int j, boolean flag,Writer writer) throws IOException {
	                writer.write( ac, i, j ); }
	            });
				mMap.put(object.getClass(), marshaller);
			}
			StringWriter stringWriter = new StringWriter();
			mMap.get(object.getClass()).marshal(object,stringWriter);
			return stringWriter.getBuffer().toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
}
