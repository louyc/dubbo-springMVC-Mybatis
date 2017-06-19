package com.semioe.dubbo.service;

import com.alibaba.fastjson.JSONObject;

public interface JWTTokenService {

	/**
     * 生成完整Token方法
     * @return
     * @throws Exception
     */
	String createTokenStrService(String userId);
	
	/**
     * 验证 TOKEN 信息是否可用（返回load内容）
     * @param tokenCode
     * @return
     */
	JSONObject readTokenCanUseService(String tokenCode);
}
