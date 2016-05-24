package com.momohelp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.momohelp.util.HttpUtil;

/**
 *
 * @author Administrator
 *
 */
public class PermitFilter implements Filter {
	private static final Logger logger = Logger.getLogger(PermitFilter.class);

	private String urlSuffix;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		urlSuffix = "," + filterConfig.getInitParameter("url-suffix") + ",";
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO
		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpServletResponse hres = (HttpServletResponse) response;

		// TODO
		if (!checkUrlSafe(hreq)) {
			chain.doFilter(request, response);
			return;
		}

		// 获取此次的请求路径
		String uri = hreq.getRequestURI();

		if ("/user/login".equals(uri)) {
			chain.doFilter(request, response);
			return;
		}

		HttpSession session = hreq.getSession();
		// 获取lv对象
		Object obj = session.getAttribute("session.lv");

		if (isManage(uri)) {
			if (null == obj) {
				hres.sendRedirect("/manage/login");
				return;
			}

			int lv = (Integer) obj;
			if (1 != lv) {
				hres.sendRedirect("/user/login");
				return;
			}
		} else {
			// 前台
			if (null == obj) {
				hres.sendRedirect("/user/login");
				return;
			}
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		logger.info("filter destroy");
	}

	/**
	 * 检测Url是否安全
	 *
	 * @param url
	 * @return 安全返回true
	 */
	private boolean checkUrlSafe(HttpServletRequest req) {
		String suffix = HttpUtil.getUrlSuffix(req);
		// TODO
		return (null == suffix) ? true : (urlSuffix.indexOf(","
				+ suffix.toLowerCase() + ",") == -1);
	}

	/**
	 * 判断是前台还是后台
	 */
	private boolean isManage(String uri) {
		return 0 == uri.indexOf("/manage/");
	}

}
