package com.semioe.web.service;

import com.alibaba.fastjson.JSONObject;
import com.semioe.common.tools.RedisTool;
import com.semioe.common.tools.util.JWTTokenUtil;
import com.semioe.web.Interceptor.AutorizadorInterceptor;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by puanl on 2017/5/25.
 */
@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private RedisTool redisTool;

    /**
     * 生成TOKEN，并保存到Redis中。
     * @param userId
     * @return
     */
    public String createToken(String userId) {
        String token = null;
        try {
            token = JWTTokenUtil.createTokenStr(userId);
            //redisTool.put(userId, token , 15, TimeUnit.MINUTES); //定时
            redisTool.put(userId, token);
        } catch (Exception e) {
            logger.error("生成 token 失败：{}", e);
        }
        return token;
    }

    /**
     * 读取Redis中TOKEN的方法。
     * @param tokenCode
     * @return
     */
    public JSONObject readToken(String tokenCode) {
        JSONObject json = null;
        try {
            json = JWTTokenUtil.readTokenCanUse(tokenCode);
            String userId = json.getString("data");
            String redisToken = redisTool.get(userId);
            if (!tokenCode.equals(redisToken)) {
            	logger.error("token 比对失败，\n tokenCode：["+tokenCode+"]\n redisToken:["+redisToken+"]");
                return null;
            }
        } catch (Exception e) {
            logger.error("读取 token 失败：{}", e);
            return null;
        }

        return json;
    }

    /**
     * 根据用户ID，将初始化数据添加到cookie中
     * @param response
     * @param userId
     */
    public void setCookieByUserId(HttpServletResponse response,String userId){
    	
    	//通过用户ID生成新的TOKEN
        String token = createToken(userId);
        
        // 设置token的cookie
        Cookie cookie = new Cookie(AutorizadorInterceptor.TOKEN, token);
        cookie.setPath("/");
        response.addCookie(cookie);

        logger.info("TokenService.setCookieByUserId , token= "+token);
        
        //用户ID
        cookie = new Cookie(AutorizadorInterceptor.USER_ID, userId);
        cookie.setPath("/");
        response.addCookie(cookie);
        
        logger.info("TokenService.setCookieByUserId , userId= "+userId);
    	
    }
    
}
