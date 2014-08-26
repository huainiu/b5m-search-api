package com.b5m.controller.request;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.b5m.annotations.Jsonp;
import com.b5m.annotations.Param;
import com.b5m.bean.Msg;
import com.b5m.exception.InvokeException;
import com.b5m.service.hbase.bean.PricePerDay;
import com.b5m.service.hbase.bean.PriceTrend;
import com.b5m.service.pricetrend.PriceTrendService;
import com.b5m.service.pricetrend.PriceTrendUtils;
import com.b5m.service.sf1.SF1QueryService;

@Repository("ontime_price")
@Scope("prototype")
@Jsonp
public class PriceTrendInfoRequestBean extends RequestBean{
    @Param
	private String docId;
    
    @Param
	private String source;
    
    @Param
	private Boolean fill;
    
    @Param
	private String price;
    
    @Param
    private Integer day;
    
    @Reference(version = "0.0.1")
    private PriceTrendService priceTrendService;
    
    @Reference(version = "0.0.1")
    private SF1QueryService sf1QueryService;
	
	@Override
	public Msg invoke(HttpServletRequest req) throws InvokeException {
		if(day == null) day = 90;
		PriceTrend priceTrend = priceTrendService.singlePriceTrend(day, docId, fill, price);
		if (fill == null) fill = false;
		Map<String, String> docidSourceMapping = new HashMap<String, String>();
		docidSourceMapping.put(docId, source);
		if (priceTrend == null){
			return Msg.newFailedInstance("price trend is empty");
		}
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(PriceTrendUtils.convert(priceTrend, source));
		jsonObject.put("averiage", jsonArray);
		JSONArray priceArray = PriceTrendUtils.convert(jsonArray.getJSONObject(0).getJSONArray("prices"), "MM");
		JSONObject rs = sf1QueryService.forecastPrice(priceArray);
		if (rs.getString("forecast_result") != null && !StringUtils.isEmpty(price)) {
			BigDecimal forecast = new BigDecimal(rs.getString("forecast_result"));
			BigDecimal nowPrice = new BigDecimal(price);
			jsonObject.put("forecastTrend", forecast.compareTo(nowPrice));
			jsonObject.put("forecast", forecast.setScale(2, RoundingMode.HALF_EVEN));
		}
		jsonObject.put("averiageType", PriceTrendUtils.getPriceType(priceTrend));
		PricePerDay pricePerDay = PriceTrendUtils.getPrePrice(priceTrend, priceTrend.getPricePerDays().get(priceTrend.getPricePerDays().size() - 1));
		jsonObject.put("changePrice", pricePerDay);
		jsonObject.put("nowPrice", priceTrend.getPricePerDays().get(priceTrend.getPricePerDays().size() - 1));
		return Msg.newSuccInstance(jsonObject);
	}

	@Override
	public void checkParam() throws InvokeException {
		if(StringUtils.isEmpty(docId)) throw new InvokeException("docId is empty");
	}

}
