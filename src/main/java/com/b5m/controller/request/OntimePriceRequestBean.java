package com.b5m.controller.request;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.b5m.annotations.Jsonp;
import com.b5m.annotations.Param;
import com.b5m.base.common.utils.StringTools;
import com.b5m.bean.Msg;
import com.b5m.exception.InvokeException;
import com.b5m.service.ontime.OntimeService;
import com.b5m.service.ontime.bean.OntimePriceBean;

@Repository("ontime_price")
@Scope("prototype")
@Jsonp
public class OntimePriceRequestBean extends RequestBean{
	private static Log LOG = LogFactory.getLog(OntimePriceRequestBean.class);
	@Param
	private String docId; 
	
	@Param
	private String url;
	
	@Reference(timeout = 5000, version = "0.0.1")
	private OntimeService ontimeService;
	
	@Override
	public Msg invoke(HttpServletRequest request) throws InvokeException{
		OntimePriceBean ontimePriceBean = new OntimePriceBean();
		ontimePriceBean.setFlag(1);
		ontimePriceBean.addDocId(docId, url);
		LOG.debug("start invoke query price----------------------------------");
		JSONArray jsonArray = ontimeService.queryPrices(ontimePriceBean);
		if(jsonArray == null || jsonArray.size() < 1){
			return Msg.newFailedInstance("error get price");
		}
		LOG.debug("end invoke query price----------------------------------");
		return Msg.newSuccInstance(jsonArray.get(0));
	}

	@Override
	public void checkParam() throws InvokeException {
		if(StringTools.isEmpty(url)) throw new InvokeException("url is empty");
	}
	
}
