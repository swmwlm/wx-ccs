package com.github.wx.ccs.utils;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cch
 * @date 2018年8月13日 下午12:07:14
 *
 */
public class Util {

	/**
	 * 判断对象是否为空
	 * 
	 * @author cch
	 * @param o
	 * @return 对象为空：<code>true</code>,对象不为空<code>false</code>
	 */
	public static final boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		}
		if ((o instanceof String)) {
			return ((String) o).trim().length() == 0;
		}
		if ((o instanceof List)) {
			return ((List) o).size() == 0;
		}
		if ((o instanceof Set)) {
			return ((Set) o).size() == 0;
		}
		if ((o instanceof Map)) {
			return ((Map) o).size() == 0;
		}
		if ((o instanceof Object[])) {
			return ((Object[]) o).length == 0;
		}

		return false;
	}

	public static final boolean isNotEmpty(Object o) {

		return !isEmpty(o);
	}

	/**
	 * 多个对象判断是否有对象为空
	 * 
	 * @author cch
	 * @param o
	 * @return 任一对象为空：返回 true。
	 */
	public static final boolean isAnyEmpty(Object... o) {
		for (int i = 0; i < o.length; i++) {
			Object obj = o[i];
			if (isEmpty(obj)) {
				return true;
			}
		}
		return false;
	}

	public static final boolean isNotAnyEmpty(Object... o) {
		return !isAnyEmpty(o);
	}

	/**
	 * 首字母转化为小写
	 * 
	 * @author cch
	 * @param s
	 * @return
	 */
	public static final String lowerCaseFirstChar(String s) {
		if (isEmpty(s)) {
			return "";
		}
		return s.substring(0, 1).toString().toLowerCase() + s.substring(1).toString();
	}

	/**
	 * 首字母转化为大写
	 * 
	 * @author cch
	 * @param s
	 * @return
	 */
	public static final String upperCaseFirstChar(String s) {
		if (isEmpty(s)) {
			return "";
		}
		return s.substring(0, 1).toString().toUpperCase() + s.substring(1).toString();
	}

	/**
	 * @author cch
	 * @return 返回当前时间（毫秒级别）+UUID随机数（去‘-’字符）
	 */
	public static final String getUUID() {
		return System.nanoTime() + UUID.randomUUID().toString().replaceAll("\\-", "");
	}
	
	/**
	 * 验证正则表达式
	 */
	public static final boolean RegeExValid(String str, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		boolean flag = matcher.matches();
		return flag;
	}
	
	/**
	 * 检查是否小数类型
	 */
	public static final boolean isBigDecimal(Object o) {
		try {
			BigDecimal d =  new BigDecimal(o + "");
			System.out.println(d);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static Map<String, Object> listToMap(List<Map<String, Object>> list, String paramKey){
		Map<String, Object> map = new HashMap<>(list.size());
		for(Map<String, Object> l : list) {
			if(isEmpty(l.get(paramKey))) {
				break;
			}
			if(!map.containsKey(l.get(paramKey).toString())) {
				map.put(l.get(paramKey).toString(), l);
			}
		}
		return map;
	}

	
}
