package com.semioe.web.service;

import com.alibaba.fastjson.JSONObject;
import com.semioe.common.tools.RedisTool;
import com.semioe.common.tools.util.JWTTokenUtil;
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

    public String createToken(String userId) {
        String token = null;
        try {
            token = JWTTokenUtil.createTokenStr(userId);
            redisTool.put(userId, token);
        } catch (Exception e) {
            logger.error("生成 token 失败：{}", e);
        }
        return token;
    }

    public JSONObject readToken(String tokenCode) {
        JSONObject json = null;
        try {
            json = JWTTokenUtil.readTokenCanUse(tokenCode);
            String userId = json.getString("data");
            String redisToken = redisTool.get(userId);
            if (!tokenCode.equals(redisToken)) {
                return null;
            }
        } catch (Exception e) {
            logger.error("读取 token 失败：{}", e);
        }

        return json;
    }

}
