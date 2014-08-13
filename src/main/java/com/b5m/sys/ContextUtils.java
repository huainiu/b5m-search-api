package com.b5m.sys;

import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.b5m.base.common.utils.DateTools;

/**
 * @description
 * 初始化上下文
 * @author echo
 * @time 2014年6月9日
 * @mail wuming@b5m.com
 */
public class ContextUtils {
	private static Properties properties;
	
	public static void init(Properties properties){
		SearchContext.getInstance();
		setVersion();
		ContextUtils.properties = properties;
	}
	
	public static void setVersion(){
		SearchContext.getInstance().setVersion(DateTools.formate(DateTools.now(), "yyyyMMddHHmmss"));
	}
	
	public static String getVersion(){
		return SearchContext.getInstance().getVersion();
	}
	
	public static String getProp(String name){
		return properties.getProperty(name);
	}
	
	public static Integer getIntProp(String key){
        Object o = properties.get(key);
        if(null == o) return null;
        return Integer.parseInt(o.toString());
    }
    
    public static Long getLongProp(String key){
    	Object o = properties.get(key);
        if(null == o) return null;
        return Long.parseLong(o.toString());
    }
    
    public static boolean checkToken(String token) throws Exception{
    	if(StringUtils.isEmpty(token)) return false;
    	Class<?> cls = Class.forName("com.b5m.search.sys.Tokens");
    	Object o = cls.newInstance();
    	Method method = cls.getMethod("check", String.class);
    	Object flag = method.invoke(o, token);
    	return Boolean.valueOf(flag.toString());
    }
    
    public static String generatorToken(String token) throws Exception{
    	if(StringUtils.isEmpty(token)) return "";
    	Class<?> cls = Class.forName("com.b5m.search.sys.Tokens");
    	Object o = cls.newInstance();
    	Method method = cls.getMethod("generator", String.class);
    	Object tokenStr = method.invoke(o, token);
    	return tokenStr.toString();
    }
    
}
