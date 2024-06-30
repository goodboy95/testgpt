package com.seekerhut.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Optional.empty;

public class LoginInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		var rawCookies = request.getCookies();
		Cookie nullCookie = null;
		var userCookie = rawCookies == null
				? Optional.ofNullable(nullCookie)
				: Arrays.stream(rawCookies).filter(c -> c.getName().equalsIgnoreCase("userId")).findFirst();
//		if (!userCookie.isEmpty()) {
//			var userId = Long.parseLong(userCookie.get().getValue());
//			return true;
//		} else {
//			response.sendRedirect("/api/auth/user_login");
//			return false;
//		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	
	}
}