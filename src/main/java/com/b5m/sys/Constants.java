package com.b5m.sys;

public final class Constants {
	
	/**
     * 请求参数中的多值的分隔符
     */
    public static final String REQUEST_PARAM_DELIMER = ",";
    
    public static final int KEYWORDS_LIMIT = 10;
    
    public static final String  CHAR_SET = "UTF-8";
    
    public static final String ISO8859 = "ISO8859-1";
    
    public static final String IMAGE_SHOW_TYPE = "image";
    
    public static final String CPS_SERVER_ADDRESS = "http://old.b5m.com/";
    
    //评论的分页大小
    public static final int COMMENT_PAGE_SIZE = 10;
    
    
    /**------------------web----------------**/
    /** 请求或发送数据时的默认编码,UTF-8 */
    public static String DEFAULT_ENCODING = "UTF-8";

    /** HTML类型文档的ContentType,text/html;charset=$default_encoding */
    public final static String HTML_CONTENT_TYPE = "text/html;charset="  + DEFAULT_ENCODING;

    public final static String XML_CONTENT_TYPE = "text/xml;charset="  + DEFAULT_ENCODING;

    /** application/x-json */
    public final static String JSON_CONTENT_TYPE = "application/x-json";
	
}
