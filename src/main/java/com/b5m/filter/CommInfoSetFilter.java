package com.b5m.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.b5m.sys.ContextUtils;

/**
 * @description
 * 
 * @author echo
 * @time 2014年7月9日
 * @mail wuming@b5m.com
 */
public class CommInfoSetFilter implements Filter {
	private static final Log LOG = LogFactory.getLog(CommInfoSetFilter.class);
	private static final String DEFAULT_COMMINFO_PATH = "config.properties";

	public void destroy(){
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		filterChain.doFilter(req, res);
	}

	public void init(FilterConfig arg0) throws ServletException{
		try {
			initSearchContext(arg0);
		} catch (Exception e) {
			if (LOG.isErrorEnabled())
				LOG.debug("---------------------init exception exception type is[" + e.getClass().getSimpleName()
						+ "], error message is [" + e.getMessage() + "]-------------------------");
		}
	}
	
	private void initSearchContext(FilterConfig arg0) throws ServletException, IOException{
		ContextUtils.setVersion();
		ContextUtils.init(initSysProp());
	}

	private Properties initSysProp() throws IOException {
		String commInfoProPath = DEFAULT_COMMINFO_PATH;
		InputStream inputStream = CommInfoSetFilter.class.getClassLoader().getResourceAsStream(commInfoProPath);
		Properties properties = new Properties();
		properties.load(inputStream);
		return properties;
	}
	
}