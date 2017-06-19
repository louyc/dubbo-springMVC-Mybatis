package com.semioe.dubbo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.semioe.common.tools.util.JWTTokenUtil;
import com.semioe.common.tools.util.Md5Util;
import com.semioe.dubbo.service.JWTTokenService;

@Service
public class JWTTokenServiceImpl implements JWTTokenService{
	
	private static final Logger logger = LoggerFactory.getLogger(JWTTokenServiceImpl.class);
	
    public String createTokenStrService(String userId){
        //生成TOKEN内容
        String object = JWTTokenUtil.createTokenBody(userId);
        String backCode = null;
		try {
			String bodyStr;
			bodyStr = Md5Util.encryptBASE64(object);
			String str = JWTTokenUtil.createAskKey(object);
			
			backCode = bodyStr +"."+str;
			backCode = backCode.replace("\r\n","");
			
		} catch (Exception e) {
			logger.error("生成 token 失败：{}", e);
		}
		return backCode;
    }
    
    public JSONObject readTokenCanUseService(String tokenCode){

        String jwtBody = null;
        //判断TOKEN格式是否正确
        if(tokenCode.indexOf(".") == -1){
            return null;
        }
        // 根据内容信息转换密钥
        String bodyCode = tokenCode.substring(0,tokenCode.indexOf("."));
        String sayCode = null;
        try{
            jwtBody = Md5Util.decryptBASE64(bodyCode);
            sayCode = JWTTokenUtil.createAskKey(jwtBody);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        // 获取token 密钥，比较密钥是否正确
        String messCode = tokenCode.substring(tokenCode.indexOf(".")+1,tokenCode.length());
        if(!messCode.equals(sayCode)){
            return null;
        }
        // TOKEN 内容转换 JSON 串
        JSONObject jwtBodyJson = JSONObject.parseObject(jwtBody).getJSONObject("load");

        //返回内容
        return jwtBodyJson;
    }
}
