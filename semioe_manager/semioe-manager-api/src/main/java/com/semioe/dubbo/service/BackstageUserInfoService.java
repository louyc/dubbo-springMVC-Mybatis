package com.semioe.dubbo.service;

import com.semioe.api.entity.BackstageUserInfo;
import com.semioe.api.vo.BackstageUserInfoVO;
import com.semioe.api.vo.DateConditionsVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import java.util.Map;

public interface BackstageUserInfoService {

	/**
	 * getBackstageUserInfoByMobile:根据用户名查询用户信息. <br/>
	 * 
	 * @param userName
	 * @return BackstageUserInfo
	 * @author hua
	 */
	BackstageUserInfo getBackstageUserInfoByUserName(String userName);

	/**
	 * login:用户登录. <br/>
	 * 
	 * @param id
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings("rawtypes")
	Result backstageUserLogin(String id);

	/**
	 * regist:用户注册. <br/>
	 * 
	 * @param info
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings("rawtypes")
	Result backstageUserRegist(BackstageUserInfo info);

	/**
	 * 创建用户
	 * 
	 * @description
	 * @author King L
	 * @createTime 2017年7月12日
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result insertBackstageUserInfo(BackstageUserInfo backstageUserInfo);

	/**
	 * 修改用户信息
	 * 
	 * @description
	 * @author King L
	 * @createTime 2017年7月12日
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result updateBackstageUserInfo(BackstageUserInfo backstageUserInfo);

	/**
	 * 删除用户
	 * 
	 * @description 把用户状态改为不可用
	 * @author King L
	 * @createTime 2017年7月12日
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result deleteBackstageUserInfo(String managerId);

	/**
	 * 查询所有用户
	 * 
	 * @description 根据条件查询所有用户
	 * @author King L
	 * @createTime 2017年7月12日
	 * @version 1.0
	 */
	Result getAllBackstageUserInfo(Map<String, Object> queryMap);

	/**
	 * 分配用户角色 验证逻辑：角色为医生、或机构的不能修改角色
	 * 
	 * @author quanlj
	 * @createTime 2017年7月14日
	 * @version 1.0
	 */
	Result deployUserRole(String userId, int roleId);

	/**
	 * 查询单用户
	 * 
	 * @param managerId
	 * @return
	 */
	Result getOneUser(String managerId);

	/**
	 * 查询单用户
	 * 
	 * @param managerId
	 * @return
	 */
	Result getOneUserAndDepts(String managerId);

	/**
	 * 查询用户 根据角色
	 * 
	 * @param managerId
	 * @return
	 */
	Result queryByRole(BackstageUserInfoVO back);

	/**
	 * @Description: TODO 运营数据统计 @author xuyuxing @createTime
	 *               2017年7月29日下午12:09:04 @Title:
	 *               selectRegisterNumberListPage @param
	 *               DateConditionsVO @return PaginateResult @throws
	 */
	PaginatedResult selectRegisterNumberListPage(DateConditionsVO conditionsVO);

	PaginatedResult queryUserListPage(BackstageUserInfoVO backstageUserInfoVO);

}