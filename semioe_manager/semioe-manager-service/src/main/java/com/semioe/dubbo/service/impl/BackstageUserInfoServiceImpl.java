package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.dto.BackstageUserInfoDTO;
import com.semioe.api.dto.Statistics;
import com.semioe.api.entity.BackstageUserInfo;
import com.semioe.api.entity.BackstageUserInfoExample;
import com.semioe.api.entity.DepartmentDic;
import com.semioe.api.vo.BackstageUserInfoVO;
import com.semioe.api.vo.DateConditionsVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.BackstageRoleInfoMapper;
import com.semioe.dubbo.dao.BackstageUserInfoMapper;
import com.semioe.dubbo.service.BackstageUserInfoService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class BackstageUserInfoServiceImpl implements BackstageUserInfoService {

	private static final Logger logger = LoggerFactory
			.getLogger(BackstageUserInfoServiceImpl.class);

	public static ResultCode AccountNull = new ResultCode("ACCOUNT_NULL", "账号不存在！");
	public static ResultCode AccountDisabled = new ResultCode("ACCOUNT_DISABLED", "账号被禁用！");
	public static ResultCode ParamError = new ResultCode("PARAM_ERROR", "密码错误,参数不能为空！");
	public static ResultCode UserExists = new ResultCode("USER_EXISTS", "用户已注册！");
	public static ResultCode RoleExists = new ResultCode("Role_EXISTS", "该用户角色不能被修改！");

	public static ResultCode UserAddFail = new ResultCode("USER_ADD_FAIL", "用户添加失败！");
	public static ResultCode UserUpdateFail = new ResultCode("USER_UPDATE_FAIL", "用户修改失败！");

	@Autowired
	private BackstageUserInfoMapper backstageUserInfoMapper;
	@Autowired
	private BackstageRoleInfoMapper backstageRoleInfoMapper;

	public BackstageUserInfo getBackstageUserInfoByUserName(String userName) {
		logger.info("开始查询用户信息，查询条件用户名为:" + userName);
		BackstageUserInfoExample example = new BackstageUserInfoExample();
		example.createCriteria().andNameEqualTo(userName);
		List<BackstageUserInfo> list = backstageUserInfoMapper.selectByExample(example);
		if (list.size() == 0) {
			return null;
		}
		logger.info("查询结果：" + list.get(0).toString());
		return list.get(0);
	}

	@SuppressWarnings("rawtypes")
	public Result backstageUserLogin(String id) {
		Result result = new Result<>(StatusCodes.OK, true);

		BackstageUserInfo backstageUserInfo = null;
		backstageUserInfo = backstageUserInfoMapper.selectByPrimaryKey(id);
		result.setData(backstageUserInfo);

		if (backstageUserInfo == null) {// 账户不存在
			return new Result(StatusCodes.OK, false, AccountNull);
		} else if (backstageUserInfo.getInUse() == 0) {
			result.setSuccessful(false);
			result.setResultCode(AccountDisabled);
		} else if (backstageUserInfo.getUserStatus() != BackstageUserInfo.UserStatus.PASS
				.getValue()) {
			result.setSuccessful(false);
			BackstageUserInfo.UserStatus us = BackstageUserInfo.UserStatus
					.values()[backstageUserInfo.getUserStatus()];
			result.setResultCode(new ResultCode(us.name(), us.getName()));
		} else {
			result.setResultCode(new ResultCode("SUCCESS", "登录成功！"));
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	public Result backstageUserRegist(BackstageUserInfo info) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null != info) {

			// 设置注册时间
			info.setCreateTime(new Date());
			// 设置初始审核状态
			info.setUserStatus(BackstageUserInfo.UserStatus.INIT.getValue());
			// 验证用户是否已注册
			BackstageUserInfo BackstageUserInfo = null;
			if (StringUtils.isNotEmpty(info.getManagerId())) {
				BackstageUserInfo = backstageUserInfoMapper.selectByPrimaryKey(info.getManagerId());
				if (null != BackstageUserInfo) {
					return new Result<>(StatusCodes.OK, false, UserExists);
				}
			}

			// 存库
			int line = backstageUserInfoMapper.insertSelective(info);
			if (line > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "注册成功！"));
			}
		}

		return result;
	}

	/**
	 * 创建用户
	 * 
	 * @description
	 * @author King L
	 * @createTime 2017年7月12日
	 * @version 1.0
	 */
	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	public Result insertBackstageUserInfo(BackstageUserInfo backstageUserInfo) {
		if ("".equals(backstageUserInfo) || null == backstageUserInfo) {
			Result result = new Result<>(StatusCodes.OK, false);
			return result;
		}
		backstageUserInfo.setCreateTime(new Date());
		backstageUserInfo.setUserStatus(BackstageUserInfo.UserStatus.PASS.getValue());
		int insertCount = backstageUserInfoMapper.insertSelective(backstageUserInfo);

		Result result = null;
		if (1 != insertCount) {
			return new Result(StatusCodes.OK, false, UserAddFail);
		} else {
			result = new Result<>(StatusCodes.OK, true);
			result.setResultCode(new ResultCode("SUCCESS", "添加用户成功！"));
		}

		return result;
	}

	/**
	 * 修改用户信息
	 * 
	 * @description
	 * @author King L
	 * @createTime 2017年7月12日
	 * @version 1.0
	 */
	@SuppressWarnings({ "rawtypes" })
	public Result updateBackstageUserInfo(BackstageUserInfo backstageUserInfo) {
		backstageUserInfo.setUpdateTime(new Date());
		int updateCount = backstageUserInfoMapper.updateByPrimaryKeySelective(backstageUserInfo);

		Result result = null;

		if (1 != updateCount) {
			return new Result(StatusCodes.OK, false, UserUpdateFail);
		} else {
			result = new Result<>(StatusCodes.OK, true);
			result.setResultCode(new ResultCode("SUCCESS", "修改用户成功！"));
		}

		return result;
	}

	/**
	 * 删除用户
	 * 
	 * @description 把用户状态改为不可用
	 * @author King L
	 * @createTime 2017年7月12日
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	public Result deleteBackstageUserInfo(String managerId) {
		if ("".equals(managerId)) {
			Result result = new Result<>(StatusCodes.OK, false);
			return result;
		}

		BackstageUserInfo backstageUserInfo = new BackstageUserInfo();
		backstageUserInfo.setInUse(0);

		BackstageUserInfoExample example = new BackstageUserInfoExample();
		example.createCriteria().andManagerIdEqualTo(managerId);

		int deleteCount = backstageUserInfoMapper.updateByExampleSelective(backstageUserInfo,
				example);
		Result result = null;
		if (1 != deleteCount) {
			result = new Result<>(StatusCodes.OK, false);
		} else {
			result = new Result<>(StatusCodes.OK, true);
		}
		return result;
	}

	/**
	 * 查询某一个用户
	 * 
	 * @description
	 * @author King L
	 * @createTime 2017年7月12日
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	public Result getOneUser(String managerId) {
		BackstageUserInfo backstageUserInfo = new BackstageUserInfo();
		backstageUserInfo = backstageUserInfoMapper.selectByPrimaryKey(managerId);
		Result result = null;

		if (null != backstageUserInfo) {
			result = new Result<>(StatusCodes.OK, true);
			result.setData(backstageUserInfo);
		} else {
			result = new Result<>(StatusCodes.OK, false);

		}
		return result;
	}

	/**
	 * 查询某一个用户和标签信息
	 * 
	 * @description
	 * @author King L
	 * @createTime 2017年7月12日
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	public Result getOneUserAndDepts(String managerId) {
		BackstageUserInfoVO backstageUserInfo = new BackstageUserInfoVO();
		List<DepartmentDic> listDepart = new ArrayList<DepartmentDic>();
		List<BackstageUserInfoVO> list = backstageUserInfoMapper
				.selectInfoVOByPrimaryKey(managerId);
		Result result = null;

		if (null != list && list.size() > 0) {
			for (BackstageUserInfoVO vo : list) {
				if (null != vo.getDeptId()) {
					DepartmentDic de = new DepartmentDic();
					de.setId(Integer.parseInt(vo.getDeptId()));
					de.setName(vo.getDeptName());
					listDepart.add(de);
				}
			}
			JSONObject jsonObject = JSONObject.fromObject(list.get(0));
			backstageUserInfo = (BackstageUserInfoVO) JSONObject.toBean(jsonObject,
					BackstageUserInfoVO.class);
			backstageUserInfo.setListDepart(listDepart);
		}

		if (null != backstageUserInfo) {
			result = new Result<>(StatusCodes.OK, true);
			result.setData(backstageUserInfo);
		} else {
			result = new Result<>(StatusCodes.OK, false);

		}
		return result;
	}

	/**
	 * 查询用户根据角色
	 * 
	 * @description
	 * @author King L
	 * @createTime 2017年7月12日
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	public Result queryByRole(BackstageUserInfoVO back) {
		List<BackstageUserInfoVO> backstageUserInfo = new ArrayList<BackstageUserInfoVO>();
		List<BackstageUserInfoVO> listAllService = new ArrayList<BackstageUserInfoVO>();
		PaginatedResult<BackstageUserInfoVO> pa = null;
		int totalCount = 0;
		listAllService = backstageUserInfoMapper.selectByConditionList(back);
		totalCount = listAllService.size();
		backstageUserInfo = backstageUserInfoMapper.selectByConditionListPage(back);
		for (BackstageUserInfoVO user : backstageUserInfo) {
			if (StringUtil.isEmpty(user.getAddress())) {
				user.setAddress("");
			}
		}
		pa = new PaginatedResult<BackstageUserInfoVO>(backstageUserInfo, back.getCurrentPage(),
				back.getShowCount(), totalCount);
		pa.setSuccessful(true);
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

	/**
	 * 查询所有用户
	 * 
	 * @description 根据条件查询所有用户
	 * @author King L
	 * @createTime 2017年7月12日
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	public Result getAllBackstageUserInfo(Map<String, Object> queryMap) {

		List<BackstageUserInfoVO> userInfoAllList = backstageUserInfoMapper
				.selectByCondition(queryMap);
		List<BackstageUserInfoVO> backstageUserInfoList = backstageUserInfoMapper
				.selectByConditionLimit(queryMap);
		Result result = new Result<>(StatusCodes.OK, true);
		int pageSuM = 0; // 页码数
		int count = 0; // 总条数
		if (null != userInfoAllList && userInfoAllList.size() > 0) {
			count = userInfoAllList.size();
			int pageSize = Integer.parseInt(queryMap.get("pageSize").toString());
			pageSuM = count % pageSize == 0 ? count / pageSize : (count / pageSize + 1);
		}
		if (null != backstageUserInfoList && backstageUserInfoList.size() > 0) {
			for (BackstageUserInfoVO userinfo : backstageUserInfoList) {
				if (null != userinfo.getRoleId()) {
					userinfo.setRoleName(backstageRoleInfoMapper
							.selectByPrimaryKey(userinfo.getRoleId()).getItemName());
				}
			}
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("list", backstageUserInfoList);
		returnMap.put("pageSum", pageSuM);
		returnMap.put("pageNumber", queryMap.get("pageNumber"));
		returnMap.put("infoCount", count);
		result.setData(returnMap);
		return result;
	}

	/**
	 * 分配用户角色 验证逻辑：角色为医生、或机构的不能修改角色
	 * 
	 * @author quanlj
	 * @createTime 2017年7月14日
	 * @version 1.0
	 */
	public Result deployUserRole(String userId, int roleId) {

		// 根据用户ID获取用户角色
		BackstageUserInfo userInfo = backstageUserInfoMapper.selectByPrimaryKey(userId);
		// 验证用户及角色
		if (userInfo == null) {
			return new Result(StatusCodes.OK, false, AccountNull);
		}
		if (userInfo.getRoleId() != null
				&& (1 == userInfo.getRoleId() || 2 == userInfo.getRoleId())) {
			return new Result(StatusCodes.OK, false, RoleExists);
		}
		// 修改用户角色
		userInfo.setRoleId(roleId);
		int insertCount = backstageUserInfoMapper.updateByPrimaryKey(userInfo);

		Result result = null;
		if (1 != insertCount) {
			result = new Result<>(StatusCodes.OK, false);
		} else {
			result = new Result<>(StatusCodes.OK, true);
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public PaginatedResult selectRegisterNumberListPage(DateConditionsVO dateConditionsVO) {
		System.out.println(
				dateConditionsVO.getStartDate() != null && dateConditionsVO.getStartDate() != "");
		System.out.println(
				dateConditionsVO.getEndDate() != null && dateConditionsVO.getEndDate() != "");
		List<Statistics> statisticsList = backstageUserInfoMapper
				.selectRegisterNumberListPage(dateConditionsVO);
		int totalCount = dateConditionsVO.getTotalResult();
		PaginatedResult<Statistics> pr = new PaginatedResult<Statistics>(statisticsList,
				dateConditionsVO.getCurrentPage(), dateConditionsVO.getShowCount(), totalCount);
		pr.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		return pr;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public PaginatedResult queryUserListPage(BackstageUserInfoVO backstageUserInfoVO) {
		List<BackstageUserInfoDTO> dataList = backstageUserInfoMapper
				.queryUserListPage(backstageUserInfoVO);
		int totalCount = backstageUserInfoVO.getTotalResult();
		PaginatedResult<BackstageUserInfoDTO> pr = new PaginatedResult<BackstageUserInfoDTO>(
				dataList, backstageUserInfoVO.getCurrentPage(), backstageUserInfoVO.getShowCount(),
				totalCount);
		return pr;
	}

}