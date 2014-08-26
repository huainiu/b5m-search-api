package com.b5m.controller.request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.alibaba.dubbo.config.annotation.Reference;
import com.b5m.bean.Msg;
import com.b5m.bean.dto.SuiSearchDto;
import com.b5m.exception.InvokeException;
import com.b5m.service.search.SearchService;
import com.b5m.sf1api.dto.res.SearchDTO;

@Repository("search_result")
@Scope("prototype")
public class SearchResultRequestBean extends RequestBean{
	public static final String[] REQUEST_PARAMS = new String[]{"sort_field", "sort_type", "sources", "attrs", "sprice", "eprice","pageNo", "size", "free_delivery","cod","genuine","cat","key", "nocate", "nosource", "noattr"};
	
	@Reference(version = "0.0.1", timeout = 5000)
	private SearchService searchService;

	@Override
	public Msg invoke(HttpServletRequest req) throws InvokeException {
		SuiSearchDto dto = new SuiSearchDto();
		beforeSearch(dto, req);
		try {
			SearchDTO searchDTO = searchService.searchList(dto, getCol(req));
			return Msg.newSuccInstance(searchDTO);
		} catch (Exception e) {
			return Msg.newFailedInstance(e.getMessage());
		}
	}
	
	private String getCol(HttpServletRequest req){
		String col = req.getParameter("col");
		if(!StringUtils.isEmpty(col)){
			return "col";
		}
		return "aggregator";
	}

	/**
	 * @description 搜索前进行处理
	 * @param dto
	 * @author echo
	 * @since 2013-8-22
	 * @email echo.weng@b5m.com
	 */
	protected void beforeSearch(SuiSearchDto dto, HttpServletRequest req) {
		String categoryValue = dto.getCategoryValue();
		String[] categorys = null;
		if (!StringUtils.isEmpty(categoryValue)) {
			categorys = StringUtils.split(categoryValue, ">");
		}
		String[] params = REQUEST_PARAMS;
		if(!StringUtils.isEmpty(req.getParameter(params[7]))){
			dto.setPageSize(Integer.parseInt(req.getParameter(params[7])));
		}
		// 每页显示个数
		if(dto.getPageSize() == null || dto.getPageSize() <= 0){
			dto.setPageSize(36);
		}
		if(StringUtils.isEmpty(dto.getKeyword())){
			dto.setKeyword(req.getParameter(params[12]));
		}
		if(StringUtils.isEmpty(dto.getCategoryValue())){
			dto.setCategoryValue(req.getParameter(params[11]));
		}
		dto.setOrignKeyword(dto.getKeyword());
		//如果不用关键词进行查询 则将搜索的最后一个类目作为关键词进行页面显示
		if (StringUtils.isEmpty(dto.getKeyword())) {
			dto.setKeyword("*");
			if (!StringUtils.isEmpty(categoryValue)) {
				if (categorys != null && categorys.length > 0) {
					dto.setKeyword(categorys[categorys.length - 1]);
				}
			}
		}
		String pageNum = req.getParameter(params[6]);
		if(dto.getCurrPageNo() == null){
			dto.setCurrPageNo(1);
		}
		if(dto.getCurrPageNo() == 1){
			if(!StringUtils.isEmpty(pageNum)){
				Pattern pattern = Pattern.compile("(\\d)+"); 
				Matcher matcher = pattern.matcher(pageNum);
				if(matcher.matches()){
					dto.setCurrPageNo(Integer.valueOf(pageNum));
				}
			}
		}
		dto.setSortField(req.getParameter(params[0]));
		dto.setSortType(req.getParameter(params[1]));
		dto.setSourceValue(req.getParameter(params[2]));
		dto.setAttrs(req.getParameter(params[3]));
		dto.setPriceFrom(req.getParameter(params[4]));
		if(!StringUtils.isEmpty(req.getParameter(params[3]))){
			dto.setNoCategory(Boolean.parseBoolean(req.getParameter(params[3])));
		}
		if(!StringUtils.isEmpty(req.getParameter(params[4]))){
			dto.setNoSource(Boolean.parseBoolean(req.getParameter(params[4])));
		}
		if(!StringUtils.isEmpty(req.getParameter(params[5]))){
			dto.setNoattr(Boolean.parseBoolean(req.getParameter(params[5])));
		}
		if(StringUtils.isEmpty(dto.getPriceFrom())){
			dto.setPriceFrom("0");
		}
		dto.setPriceTo(req.getParameter(params[5]));
		if(StringUtils.isEmpty(dto.getPriceTo())){
			dto.setPriceTo("1000000");
		}
		String freeDelivery = req.getParameter(params[8]);
		if("1".equals(freeDelivery) || "0".equals(freeDelivery)){
			dto.setIsFreeDelivery(freeDelivery);
		}
		String cod = req.getParameter(params[9]);
		if("1".equals(cod) || "0".equals(cod)){
			dto.setIsCOD(cod);
		}
		String genuine = req.getParameter(params[10]);
		if("1".equals(genuine) || "0".equals(genuine)){
			dto.setIsGenuine(genuine);
		}
		for(int index = 0; index < REQUEST_PARAMS.length; index++){
			String param = REQUEST_PARAMS[index];
			req.getParameter(param);
		}
	}
	
	@Override
	public void checkParam() throws InvokeException {
	}

}
