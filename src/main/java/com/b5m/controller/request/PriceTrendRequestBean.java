package com.b5m.controller.request;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.alibaba.dubbo.config.annotation.Reference;
import com.b5m.annotations.Jsonp;
import com.b5m.annotations.Param;
import com.b5m.bean.Msg;
import com.b5m.exception.InvokeException;
import com.b5m.service.hbase.bean.PriceTrend;
import com.b5m.service.pricetrend.PriceTrendService;

@Repository("ontime_price")
@Scope("prototype")
@Jsonp
public class PriceTrendRequestBean extends RequestBean{
	@Param
	private String docId;
	@Param
	private Integer day;
	
	@Reference(version = "0.0.1")
	private PriceTrendService priceTrendService;
	
	@Override
	public Msg invoke(HttpServletRequest req) throws InvokeException {
		if(day == null) day = 90;
		PriceTrend priceTrend = priceTrendService.singlePriceTrend(day, docId, false, null);
		return Msg.newSuccInstance(priceTrend);
	}

	@Override
	public void checkParam() throws InvokeException {
		if(StringUtils.isEmpty(docId)) throw new InvokeException("docId is empty");
	}

}
