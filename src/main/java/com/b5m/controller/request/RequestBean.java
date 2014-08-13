package com.b5m.controller.request;

import javax.servlet.http.HttpServletRequest;

import com.b5m.bean.Msg;
import com.b5m.exception.InvokeException;

public abstract class RequestBean {
	
	abstract public Msg invoke(HttpServletRequest req) throws InvokeException;
	
	abstract public void checkParam() throws InvokeException;
	
}
