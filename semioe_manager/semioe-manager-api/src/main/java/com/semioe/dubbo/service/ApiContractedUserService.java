package com.semioe.dubbo.service;

import com.semioe.api.entity.UserDoctorRel;
import com.semioe.api.vo.ApiContractedUserVO;
import com.semioe.api.vo.ApiUserInfoVO;
import com.semioe.common.result.Result;
import java.util.List;

public interface ApiContractedUserService {

	/**
	 * queryDictionary:查询字典表
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result queryDictionary(String typeId);

	/**
	 * 根据用户id查询签约用户档案信息
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result queryUserArchives(String managerId, String id);

	/**
	 * 查询签约用户档案信息
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result querySignUserByEntity(ApiContractedUserVO apiUserInfo);

	/**
	 * addUserArchives：添加签约用户
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result addUserArchives(ApiContractedUserVO apiUserInfo);

	/**
	 * deleteUserArchives：删除签约用户
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result deleteUserArchives(ApiContractedUserVO apiUserInfo);

	/**
	 * updateUserArchives：修改签约用户
	 * 
	 * @description
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	Result updateUserArchives(ApiContractedUserVO apiUserInfo);

	/* 用户分析 */
	Result userAnalysis(ApiUserInfoVO userInfo);

	Result getAllContractedUser(ApiContractedUserVO userInfo);

	Result queryDoctors(String managerId);

	Result queryRel(UserDoctorRel udRel);

	List<UserDoctorRel> queryDocRel(String managerId, String doctorId);
}