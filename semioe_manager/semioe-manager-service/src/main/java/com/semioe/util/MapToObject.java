package com.semioe.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapToObject {

	private static final Logger logger = LoggerFactory.getLogger(MapToObject.class);

	/**
	 * map 转成obj对象
	 * 
	 * @param map
	 * @param obj
	 * @return
	 */
	public static Object transMap2Bean(Map<String, Object> map, Object obj) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				if (map.containsKey(key)) {
					Object value = map.get(key);
					// 得到property对应的setter方法
					Method setter = property.getWriteMethod();
					setter.invoke(obj, value);
				}
			}
		} catch (Exception e) {
			logger.info("transMap2Bean Error " + e);
		}
		return obj;
	}

	/**
	 * 校验字符串是否是整型 (空校验有问题 自行判断)
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 转码base64
	 * 
	 * @param token
	 * @return
	 */
	public static String byteToBase64(String token) {
		byte[] encodeBase64 = null;
		try {
			encodeBase64 = Base64.encodeBase64(token.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new String(encodeBase64);
	}
}