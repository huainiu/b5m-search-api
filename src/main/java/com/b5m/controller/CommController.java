package com.b5m.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.b5m.annotations.Jsonp;
import com.b5m.annotations.Param;
import com.b5m.base.common.spring.utils.ApplicationContextUtils;
import com.b5m.base.common.utils.BeanTools;
import com.b5m.base.common.utils.DateTools;
import com.b5m.bean.Msg;
import com.b5m.controller.request.RequestBean;
import com.b5m.exception.InvokeException;

/**
 * @description
 * 
 * @author echo
 * @time 2014年7月28日
 * @mail wuming@b5m.com
 */
@Controller
public class CommController {
	
	@RequestMapping("/api")
	@ResponseBody
	public void invoke(HttpServletRequest req, HttpServletResponse res) throws IOException {
		handle(req, res);
	}
	
	protected void handle(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String resultJson = null;
		try {
			RequestBean requestBean = getRequestBean(req);
			Msg msg = invoke(req, requestBean);
			Jsonp jsonp = requestBean.getClass().getAnnotation(Jsonp.class);
			String jsoncall = getCallbackStr(req);
			resultJson = JSON.toJSONString(msg);
			if(jsonp != null && !StringUtils.isEmpty(jsoncall)){
				resultJson = jsoncall + "(" + resultJson + ")"; 
				res.setContentType("text/javascript;charset=utf-8");
			}else{
				res.setContentType("application/json");
			}
		} catch (Exception e) {
			resultJson = JSON.toJSONString(Msg.newFailedInstance(e.getMessage()));
		}
		res.getWriter().write(resultJson);
		res.getWriter().flush(); 
		res.getWriter().close();
	}
	
	protected String getCallbackStr(HttpServletRequest req){
		String call = req.getParameter("jsoncallback");
		if(StringUtils.isEmpty(call)){
			call = req.getParameter("jsonCallback");
		}
		return call;
	}
	
	protected Msg invoke(HttpServletRequest req, RequestBean requestBean) throws Exception{
		try {
			setParamsValue(req, requestBean);
			requestBean.checkParam();
			return requestBean.invoke(req);
		} catch (Exception e) {
			return Msg.newFailedInstance(e.getMessage());
		}
	}
	
	private RequestBean getRequestBean(HttpServletRequest req){
		String method = req.getParameter("method");
		checkParams(method);
		RequestBean requestBean = ApplicationContextUtils.getBean(method, RequestBean.class);
		if(requestBean == null) throw new InvokeException("method is illegal");
		return requestBean;
	}
	
	private void checkParams(String method) throws InvokeException{
		if(StringUtils.isEmpty(method)) 
			throw new InvokeException("method is empty");
	}
	
	private void setParamsValue(HttpServletRequest req, RequestBean requestBean) throws Exception{
		List<Field> fields = BeanTools.getDeclearField(requestBean.getClass(), Object.class);
		for(Field field : fields){
			if(!isParam(field)) continue;
			String name = getParamName(field);
			String value = req.getParameter(name);
			if(StringUtils.isEmpty(value)) continue;
			setFieldValue(field, requestBean, value);
		}
	}
	
	private boolean isParam(Field field){
		return null != field.getAnnotation(Param.class);
	}
	
	private String getParamName(Field field){
		Param param = field.getAnnotation(Param.class);
		String name = param.value();
		if(!StringUtils.isEmpty(name)) return name;
		return field.getName();
	}
	
	private void setFieldValue(Field field, RequestBean requestBean, String value) throws Exception{
		Class<?> type = field.getType();
		if(Integer.class.equals(type) || int.class.equals(type)) {
			field.setAccessible(true);
			field.set(requestBean, Integer.valueOf(value));
			return;
		}
		if(Long.class.equals(type) || long.class.equals(type)) {
			field.setAccessible(true);
			field.set(requestBean, Long.valueOf(value));
			return;
		}
		if(Boolean.class.equals(type) || boolean.class.equals(type)) {
			field.setAccessible(true);
			field.set(requestBean, Boolean.valueOf(value));
			return;
		}
		if(Double.class.equals(type) || double.class.equals(type)) {
			field.setAccessible(true);
			field.set(requestBean, Double.valueOf(value));
			return;
		}
		if(Float.class.equals(type) || float.class.equals(type)) {
			field.setAccessible(true);
			field.set(requestBean, Float.valueOf(value));
			return;
		}
		if(BigDecimal.class.equals(type)) {
			field.setAccessible(true);
			field.set(requestBean, new BigDecimal(value));
			return;
		}
		if(Date.class.equals(type)) {
			field.setAccessible(true);
			field.set(requestBean, DateTools.toDate(value));
			return;
		}
		field.setAccessible(true);
		field.set(requestBean, value);
	}
	
}
