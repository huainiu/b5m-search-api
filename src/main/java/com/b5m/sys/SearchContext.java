package com.b5m.sys;


/**
 * @description
 * 搜索上下文
 * @author echo
 * @time 2014年6月6日
 * @mail wuming@b5m.com
 */
public class SearchContext {
	private static SearchContext instance;
	private String version;
	private String tokenKey;
	
	private SearchContext(){}
	
	public static SearchContext getInstance(){
		//tomcat启动时 初始化 所以没有并发问题
		if(instance == null){
			instance = new SearchContext();
		}
		return instance;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

	public String getTokenKey() {
		return tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}

}
