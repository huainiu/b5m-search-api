package com.b5m.controller.request;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.alibaba.dubbo.config.annotation.Reference;
import com.b5m.annotations.Param;
import com.b5m.bean.Msg;
import com.b5m.exception.InvokeException;
import com.b5m.service.sf1.SF1QueryService;

@Repository("shop_item")
@Scope("prototype")
public class ShopItemRequestBean extends RequestBean{
	@Param
	private String docId;
	
	@Param
	private String col;
	
	@Reference(version = "0.0.1")
	private SF1QueryService sf1Search;

	@Override
	public Msg invoke(HttpServletRequest req) throws InvokeException {
		if(!StringUtils.isEmpty(col)){
			col = "b5mp";
		}
		try {
			return Msg.newSuccInstance(sf1Search.doGet(col, docId));
		} catch (Exception e) {
			return Msg.newSuccInstance(e.getMessage());
		}
	}

	@Override
	public void checkParam() throws InvokeException {
		if(StringUtils.isEmpty(docId)) throw new InvokeException("docId is empty");
	}

}
