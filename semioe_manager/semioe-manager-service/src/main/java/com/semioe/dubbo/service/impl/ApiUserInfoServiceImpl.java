package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.ApiRoleInfo;
import com.semioe.api.entity.ApiRoleInfoExample;
import com.semioe.api.entity.ApiUserInfo;
import com.semioe.api.entity.BackstageUserInfo;
import com.semioe.api.vo.ApiUserInfoVO;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.ApiRoleInfoMapper;
import com.semioe.dubbo.dao.ApiUserInfoMapper;
import com.semioe.dubbo.dao.BackstageUserInfoMapper;
import com.semioe.dubbo.service.ApiUserInfoService;
import com.semioe.util.MapToObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ApiUserInfoServiceImpl implements ApiUserInfoService {

	private static final Logger logger = LoggerFactory.getLogger(ImplementSetServiceImpl.class);
	public static ResultCode MysqlWrong = new ResultCode("MYSQL_WRONG", "操作数据库出错");
	public static ResultCode UserInfoComplete = new ResultCode("USERINFO_COMPLETE", "请完善用户信息");
	public static ResultCode UserInfoUpdateFail = new ResultCode("USERINFO_UPDATE_FAIL", "完善用户出错");
	public static ResultCode UserInfoAddFail = new ResultCode("USERINFO_ADD_FAIL", "添加字用户出错");
	public static ResultCode UserInfoDelFail = new ResultCode("USERINFO_DEL_FAIL", "删除字用户出错");

	@Autowired
	private ApiUserInfoMapper apiUserInfoMapper;
	@Autowired
	private ApiRoleInfoMapper apiRoleInfoMapper;
	@Autowired
	private BackstageUserInfoMapper backstageUser;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getApiUserInfoById(String managerId, String mobile, String openId,
			String headimgurl, String url, String city) {
		Result result = new Result<>(StatusCodes.OK, true);
		// ApiUserInfo user = apiUserInfoMapper.selectByPrimaryKey(managerId);
		ApiUserInfo user = new ApiUserInfo();
		user.setManagerId(managerId);
		user.setInUse(-1); // 查询managerId 对应的数据
		user = apiUserInfoMapper.selectByEntity(user);
		if (null == user) {
			BackstageUserInfo backUser = backstageUser.selectByPrimaryKey(managerId);
			user = new ApiUserInfo();
			user.setManagerId(managerId);
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			user.setInUse(1);
			user.setMobile(mobile);
			user.setOpenId(openId);
			if (!StringUtil.isEmpty(city)) {
				user.setCity(city);
			}
			user.setSex("0");
			user.setImageUrl(headimgurl);
			if (null != backUser) {
				// user.setAddress(backUser.getAddress());
				user.setCompany(backUser.getCompany());
				user.setEmail(backUser.getEmail());
				user.setName(backUser.getName());
			}
			result.setStatus(1); // 0：登录成功 1：完善用户信息
			result.setResultCode(UserInfoComplete);
			apiUserInfoMapper.insertSelective(user);
		} else {
			user.setOpenId(openId);
			user.setCity(city);
			user.setInUse(1);
			user.setImageUrl(headimgurl);
			user.setUpdateTime(new Date());
			apiUserInfoMapper.updateByPrimaryKeySelective(user);
			apiUserInfoMapper.updateSubUser(user);
			result.setStatus(0);
			result.setResultCode(new ResultCode("SUCCESS", "登录成功！"));
		}
		// String infoJosn = JSONObject.toJSONString(user);
		logger.info("*****************" + JSONObject.fromObject(user).toString());
		// ApiUserInfoVO apiUser = JSONObject.parseObject(infoJosn,
		// ApiUserInfoVO.class);
		JSONObject jsonObject = JSONObject.fromObject(user);
		ApiUserInfoVO apiUser = (ApiUserInfoVO) JSONObject.toBean(jsonObject, ApiUserInfoVO.class);
		apiUser.setUrl(url);
		result.setData(apiUser);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result updateApiUserInfo(ApiUserInfoVO apiUserInfo) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			apiUserInfo.setUpdateTime(new Date());
			int i = apiUserInfoMapper.updateByApiUserinfoVO(apiUserInfo);
			if (i > 0) {
				apiUserInfo.setManagerId(apiUserInfo.getAccountManagerId());
				result.setResultCode(new ResultCode("SUCCESS", "完善资料成功！"));
				result.setData(apiUserInfo);
			} else {
				result = new Result(StatusCodes.OK, false, UserInfoUpdateFail);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result deleteApiUserInfo(String managerId) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put("managerId", managerId);
			updateMap.put("inUse", 0);
			updateMap.put("updateTime", new Date());
			int i = apiUserInfoMapper.updateInUse(updateMap);
			if (i > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "子账户删除成功！"));
			} else {
				result = new Result(StatusCodes.OK, false, UserInfoDelFail);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Result getAllApiUserInfo(String managerId) {
		Result result = new Result<>(StatusCodes.OK, true);
		ApiUserInfo user = new ApiUserInfo();
		user = apiUserInfoMapper.selectByPrimaryKey(managerId);
		List<ApiUserInfoVO> listUserVO = new ArrayList<ApiUserInfoVO>();
		if (StringUtil.isEmpty(user.getParentId())) { // 父账户
			List<Map<String, Object>> listUser = apiUserInfoMapper.selectSubUserInfo(managerId);
			for (Map<String, Object> userInfo : listUser) {
				ApiUserInfoVO apiUserInfoVO = new ApiUserInfoVO();
				listUserVO.add((ApiUserInfoVO) MapToObject.transMap2Bean(userInfo, apiUserInfoVO));
			}
		} else { // 子账户
			ApiUserInfo parentUser = apiUserInfoMapper.selectByPrimaryKey(user.getParentId());
			List<Map<String, Object>> listUser = apiUserInfoMapper
					.selectSubUserInfo(user.getParentId());
			for (Map<String, Object> userInfo : listUser) {
				ApiUserInfoVO apiUserInfoVO = new ApiUserInfoVO();
				listUserVO.add((ApiUserInfoVO) MapToObject.transMap2Bean(userInfo, apiUserInfoVO));
			}
			user = parentUser;
		}
		JSONObject jsonObject = JSONObject.fromObject(user);
		listUserVO.add((ApiUserInfoVO) JSONObject.toBean(jsonObject, ApiUserInfoVO.class));
		result.setData(listUserVO);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Result getParentUserInfo(String managerId) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<Map<String, Object>> listUser = apiUserInfoMapper.selectParentUserInfo(managerId);
		if (listUser.size() > 0) {
			ApiUserInfoVO apiUserInfoVO = new ApiUserInfoVO();
			apiUserInfoVO = (ApiUserInfoVO) MapToObject.transMap2Bean(listUser.get(0),
					apiUserInfoVO);
			result.setData(apiUserInfoVO);
			result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} else {
			result = new Result(StatusCodes.FAILED_DEPENDENCY, false,
					new ResultCode("QUERY_ZERO", "父账户未找到"));
		}
		return result;
	}

	@Override
	public ApiUserInfo getCurrentUserInfo(String managerId) {
		ApiUserInfo user = new ApiUserInfo();
		user = apiUserInfoMapper.selectByPrimaryKey(managerId);
		return user;
	}

	@Override
	public ApiUserInfo changeAccount(String managerId) {
		ApiUserInfo user = new ApiUserInfo();
		user = apiUserInfoMapper.selectByPrimaryKey(managerId);
		if (!StringUtil.isEmpty(user.getParentId())) {// 切换帐号 要更新成父账户openId
			ApiUserInfo parentUser = apiUserInfoMapper.selectByPrimaryKey(user.getParentId());
			user.setOpenId(parentUser.getOpenId());
			apiUserInfoMapper.updateByPrimaryKeySelective(user);
		}
		return user;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result addSubAccount(ApiUserInfoVO userInfo, String managerId) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			ApiUserInfo sonUser = new ApiUserInfo();
			sonUser.setManagerId(managerId);
			sonUser.setInUse(-1);
			sonUser = apiUserInfoMapper.selectByEntity(sonUser);

			ApiUserInfo parentUser = apiUserInfoMapper.selectByPrimaryKey(userInfo.getManagerId());

			if (null != sonUser) {
				sonUser.setName(userInfo.getName());
				sonUser.setParentId(userInfo.getManagerId());
				sonUser.setOpenId(parentUser.getOpenId());
				sonUser.setUpdateTime(new Date());
				sonUser.setRoleId(userInfo.getRoleId());
				sonUser.setInUse(1);
				apiUserInfoMapper.updateByPrimaryKeySelective(sonUser);
				result.setResultCode(new ResultCode("SUCCESS", "关系添加成功！"));
			} else {
				userInfo.setInUse(1);
				// if (StringUtil.isEmpty(userInfo.getMobile())) {
				// return new Result(StatusCodes.OK, false, new
				// ResultCode("FAIL", "手机号为空"));
				// }
				userInfo.setOpenId(parentUser.getOpenId());
				userInfo.setParentId(userInfo.getManagerId());
				userInfo.setCreateTime(new Date());
				userInfo.setUpdateTime(new Date());
				userInfo.setSex("0");
				userInfo.setManagerId(managerId);
				int i = apiUserInfoMapper.insertInfoVoSelective(userInfo);
				if (i > 0) {
					result.setResultCode(new ResultCode("SUCCESS", "关系添加成功！"));
				} else {
					result = new Result(StatusCodes.OK, false, UserInfoAddFail);
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@Override
	public Result createApiUserRoleInfo(ApiUserInfo roleInfo) {

		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result queryAllApiRoleInfo() {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			ApiRoleInfoExample example = new ApiRoleInfoExample();
			example.createCriteria().andInUseEqualTo(1);
			List<ApiRoleInfo> listRole = apiRoleInfoMapper.selectByExample(example);
			result.setData(listRole);
			result.setResultCode(new ResultCode("SUCCESS", "查询关系成功！"));
		} catch (Exception e) {
			logger.info(e.getMessage());
			result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@Override
	public Result updateAllApiRoleInfo(ApiRoleInfo roleInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result removeApiRoleInfo(String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put("id", id);
			updateMap.put("inUse", 0);
			updateMap.put("updateTime", new Date());
			int i = apiRoleInfoMapper.updateInUse(updateMap);
			if (i > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "运营设置删除成功！"));
			} else {
				result = new Result(StatusCodes.OK, false, UserInfoDelFail);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@Override
	public String getManagerIdByMobile(String mobile) {
		return apiUserInfoMapper.getManagerIdByMobile(mobile);
	}

	@Override
	public ApiUserInfo getApiUserByOpenId(String openId) {

		return apiUserInfoMapper.getApiUserByOpenId(openId);
	}
}
