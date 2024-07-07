package com.seekerhut.interceptors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seekerhut.mySqlMapper.UserDAO;
import com.seekerhut.utils.JedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginInterceptor implements HandlerInterceptor {

	private final UserDAO userMapper;

	@Autowired
	public LoginInterceptor(UserDAO userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 获取请求中的Cookie
		Cookie[] cookies = request.getCookies();
		String userqq = null;
		String logintoken = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("userqq".equals(cookie.getName())) {
					userqq = cookie.getValue();
				}
				if ("logintoken".equals(cookie.getName())) {
					logintoken = cookie.getValue();
				}
			}
		}
		response.setHeader("Access-Control-Allow-Origin", "*");
		var userInfo = JedisHelper.get("logintoken:" + userqq);
		if (userInfo == null || userInfo.isEmpty() || !userInfo.equals(logintoken)) {
			// 登录失败，返回错误信息
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"code\":-401, \"message\":\"login failed\", \"data\": \"/login.html\"}");
			return false;
		} else {
			// 登录成功，继续执行后续的处理
			return true;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// postHandle方法在请求处理之后，视图渲染之前执行
		response.setHeader("Access-Control-Allow-Origin", "*");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// afterCompletion方法在整个请求结束之后执行
		response.setHeader("Access-Control-Allow-Origin", "*");
	}
}
