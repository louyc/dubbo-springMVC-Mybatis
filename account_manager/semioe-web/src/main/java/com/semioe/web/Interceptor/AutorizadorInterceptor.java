package com.semioe.web.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.semioe.web.service.TokenService;

public class AutorizadorInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(AutorizadorInterceptor.class);

	public static final String TOKEN = "token";

	@Autowired
	private TokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {

		String token = request.getHeader("X-Auth-Token");
		logger.info("request url:{}", request.getRequestURI());
		logger.info("token::", token);
		if (1 == 1) {
			return true;
		}
		if (StringUtils.isEmpty(token)) {
			return false;
		}
		// JSONObject jsonObject = JWTTokenUtil.readTokenCanUse(token);
		JSONObject jsonObject = tokenService.readToken(token);

		if (null == jsonObject) {
			return false;
		} else {
			logger.info("User Authentication Passed:{}", jsonObject);
			return true;
		}

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) throws Exception {
		// TODO Auto-generated method stub
	}

}
