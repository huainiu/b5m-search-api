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
import com.b5m.service.ontime.OntimeService;
import com.b5m.service.ontime.bean.SkuBean;
import com.b5m.service.ontime.bean.SkuRequest;

@Repository("ontime_sku")
@Scope("prototype")
@Jsonp
public class OntimeSkuRequestBean extends RequestBean {
	@Param
	private String url;
	@Param
	private String docId;
	@Param
	private String isDetail;
	@Param
	private String nosku;
	
	@Reference(version = "0.0.1")
	private OntimeService ontimeService;

	@Override
	public Msg invoke(HttpServletRequest req) throws InvokeException {
		SkuRequest skuRequest = new SkuRequest(docId, url);
		if(!StringUtils.isEmpty(isDetail)){
			skuRequest.addKeys("Source","NoSKU","OriginalPicture","Url");
		}else if(!StringUtils.isEmpty(nosku)){
			skuRequest.addKeys("NoSKU");
		}
		SkuBean skuBean = ontimeService.querySkuProp(skuRequest, docId);
		return Msg.newSuccInstance(skuBean);
	}

	@Override
	public void checkParam() throws InvokeException {
		if(StringUtils.isEmpty(url)) throw new InvokeException("url is empty");
	}

}
