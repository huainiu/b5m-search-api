package com.b5m.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.b5m.base.common.utils.CollectionTools;
import com.b5m.base.common.utils.StringTools;
import com.b5m.service.sf1.SF1QueryService;
import com.b5m.service.sf1.bean.AutoFillInfo;

/**
 * @author echo
 */
@Controller
public class AutoFillController {
	public static final int PAGE_SIZE = 10;

	@Reference(version = "0.0.1")
	private SF1QueryService sf1Search;

	private static String[] collections = new String[]{"b5mo", "hotel", "ticketp", "tourguide", "tourp", "tuanm", "zdm", "she", "haiwaip", "haiwaiinfo", "guang", "doctor", "usa", "korea", "koreainfo"};
	
	@RequestMapping("/allAutoFill")
	public void allAutoFill(Model model, @RequestParam(value = "keyWord") String keyWord, String city, HttpServletRequest request, HttpServletResponse response) throws Exception{
		city = createCityString(request);
		Map<String, List<AutoFillInfo>> autoFillInfoMaps = sf1Search.autoFillSearch(keyWord, PAGE_SIZE, city);
        String str = request.getParameter("jsoncallback");
        String returnJson = JSON.toJSONString(autoFillInfoMaps);
        if(str!= null && !"".equals(str)){
            response.setContentType("text/javascript;charset=utf-8");  
            returnJson = str + "(" + returnJson + ")";  
        }else{
        	response.setContentType("application/json");
        }
        response.getWriter().write(returnJson);  
        response.getWriter().flush(); 
        response.getWriter().close();
	}
	
	@RequestMapping("/mautofill")
	public void mobileAutoFill(Model model, @RequestParam(value = "wd") String key, HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, List<AutoFillInfo>> autoFillInfoMaps = sf1Search.autoFillSearch(key, PAGE_SIZE, null);
		List<AutoFillInfo> autoFillInfos = autoFillInfoMaps.get("b5mo");
		JSONObject result = new JSONObject();
		result.put("q", key);
		result.put("p", false);
		JSONArray keys = new JSONArray();
		result.put("s", keys);
		if(autoFillInfos != null){
			for (AutoFillInfo autoFillInfo : autoFillInfos) {
				keys.add(autoFillInfo.getValue());
			}
		}
		String returnJson = result.toJSONString();
		String str = request.getParameter("cb");
		response.setContentType("text/javascript;charset=utf-8");  
		returnJson = str + "(" + returnJson + ")";  
		response.getWriter().write(returnJson);  
        response.getWriter().flush(); 
        response.getWriter().close();
	}
	
	private String createCityString(HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		jsonObject.put("collections", jsonArray);
		for(String collection : collections){
			String city = request.getParameter(collection + "city");
			JSONObject jsonCollection = new JSONObject();
			jsonCollection.put("collection", getName(collection));
			jsonArray.add(jsonCollection);
			if(StringTools.isEmpty(city)) {
				continue;
			}
			JSONArray cityArray = new JSONArray();
			jsonCollection.put("cities", cityArray);
			if(city.indexOf("@") < 0){
				cityArray.add(city);
			}else{
				cityArray.addAll(CollectionTools.newList(StringTools.split(city, "@")));
			}
		}
		return jsonObject.toString();
	}
	
	protected String getName(String collection){
		if("b5mo".equals(collection)) return "b5mp";
		if("ticketp".equals(collection)) return "ticket";
		if("tourp".equals(collection)) return "tour";
		if("tuanm".equals(collection)) return "tuan";
		return collection;
	}
	
}
