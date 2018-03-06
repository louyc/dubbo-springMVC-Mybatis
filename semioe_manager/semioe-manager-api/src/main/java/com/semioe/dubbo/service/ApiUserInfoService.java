package com.semioe.dubbo.service;

import com.semioe.api.entity.ApiRoleInfo;
import com.semioe.api.entity.ApiUserInfo;
import com.semioe.api.vo.ApiUserInfoVO;
import com.semioe.common.result.Result;

public interface ApiUserInfoService {

	/**
	 * getApiUserInfoById:用户登录
	 * 
	 * @param managerId
	 * @return getApiUserInfoById
	 */
	@SuppressWarnings("rawtypes")
	Result getApiUserInfoById(String managerId, String mobile, String openId, String headimgurl,
			String url, String city);

	/**
	 * updateApiUserInfo：完善用户信息
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result updateApiUserInfo(ApiUserInfoVO apiUserInfo);

	/**
	 * 删除用户
	 * 
	 * @description 把用户状态改为不可用
	 * @author King L
	 * @createTime 2017年7月12日
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result deleteApiUserInfo(String managerId);

	/**
	 * 查询所有子用户
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result getAllApiUserInfo(String managerId);

	/**
	 * 查询所有父用户
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result getParentUserInfo(String managerId);

	/**
	 * 查询当前用户信息
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	ApiUserInfo getCurrentUserInfo(String managerId);

	/**
	 * 切换当前用户信息
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	ApiUserInfo changeAccount(String managerId);

	/**
	 * 添加子账户
	 * 
	 * @param apiUserInfo
	 * @return
	 */
	Result addSubAccount(ApiUserInfoVO apiUserInfo, String managerId);

	/**
	 * 创建用户、子用户关系
	 * 
	 * @param roleInfo
	 * @return
	 */
	Result createApiUserRoleInfo(ApiUserInfo roleInfo);

	/**
	 * 获取用户、子用户关系
	 * 
	 * @return
	 */
	Result queryAllApiRoleInfo();

	/**
	 * 修改用户、子用户关系
	 * 
	 * @param roleInfo
	 * @return
	 */
	Result updateAllApiRoleInfo(ApiRoleInfo roleInfo);

	/**
	 * 删除用户、子用户关系
	 * 
	 * @param id
	 * @return
	 */
	Result removeApiRoleInfo(String managerId);

	/**
	 * 根据用户手机号 获取managerId by j
	 */
	String getManagerIdByMobile(String mobile);

	ApiUserInfo getApiUserByOpenId(String openId);
}