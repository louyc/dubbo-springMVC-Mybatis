package com.semioe.web.Interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.semioe.web.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

public class AutorizadorInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(AutorizadorInterceptor.class);

	public static final String TOKEN = "token";
	
	public static final String USER_ID = "user_id";

	@Autowired
	private TokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {

		logger.debug("--------------拦截器开始----------");
        // 获取cookie的token
        String token = getCookie(request, TOKEN);
        String userCookId = getCookie(request, USER_ID);
        logger.debug("--------------token----------"+token);
//        token = StringUtils.isNotBlank(token) ? token : request.getParameter(TOKEN);
        token = StringUtils.isNotBlank(token) ? token : request.getHeader("X-Auth-Token");
        logger.debug("--------------token----------"+token);
        
        JSONObject tokenData = null;  //token中存储的数据对象
        String userId = null;         //用户ID
        
        boolean goFlag = false;
        
        // 没有获取到cookie的token
        if (StringUtils.isNotBlank(token)) {
        	//获取TOKEN数据对象
        	tokenData = tokenService.readToken(token);
       
        	//验证用户账号是否一致
        	//验证TOKEN，生成新的token
            if (null != tokenData) {
            	userId = tokenData.getString("data"); //获取用户ID
            	if(userId != null && userId.equals(userCookId)){
            		logger.debug("--------------userEntity isnot null----------");
                    //保存数据到Cookie
                    // tokenService.setCookieByUserId(response, userId);
                    //设置验证标识
                    goFlag = true;
            	}
            }
        }

        //判断是否验证通过
        if (!goFlag) {
        	logger.error("--------------您还未登录或者已经登录超时----------");
        	String img = "您还未登录或者已经登录超时";
            throw new Exception(new String(img.getBytes("UTF-8"),"UTF-8"));  
            // new String(sb.toString().getBytes("UTF-8"));
        } 
        
        return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) throws Exception {
		// TODO Auto-generated method stub
	}

	
	/**
     * 根据传入KEY,获取cookie中的数据
     *
     * @param request
     * @return
     */
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        logger.debug("--------------拦截器开始获取cookie----------");
        if (null != cookies && StringUtils.isNotBlank(key)) {
            for (Cookie cookie : cookies) {
                logger.debug("--------------key----------"+key);
                logger.debug("--------------cookie.getName----------"+cookie.getName());
                if (key.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
	
}
