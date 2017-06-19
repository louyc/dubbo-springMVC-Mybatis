package com.semioe.dubbo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.semioe.api.entity.ManagerInfo;
import com.semioe.api.entity.ManagerInfoExample;
import com.semioe.api.vo.ManagerInfoVO;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.common.tools.util.JWTTokenUtil;
import com.semioe.common.tools.util.Md5Util;
import com.semioe.common.tools.util.ValidateUtil;
import com.semioe.dubbo.dao.ManagerInfoMapper;
import com.semioe.dubbo.service.ManagerService;

@Service
public class ManagerServiceImpl implements ManagerService {

	private static final Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);

	public static ResultCode AccountNull = new ResultCode("ACCOUNT_NULL", "账号不存在！");
	public static ResultCode PasswordError = new ResultCode("PWD_ERROR", "密码错误！");
	public static ResultCode PermissionDenied = new ResultCode("PERMISSION_DENIED", "权限不足！");
	public static ResultCode ParamError = new ResultCode("PARAM_ERROR", "密码错误,参数不能为空！");
	public static ResultCode EmailExists = new ResultCode("EMAIL_EXISTS", "邮箱已注册！");
	public static ResultCode MobileExists = new ResultCode("MOBILE_EXISTS", "手机号已注册！");

	@Autowired
	private ManagerInfoMapper managerInfoMapper;

	public ManagerInfo getManagerInfoByPrimaryKey(String id) {
		ManagerInfo managerInfo = managerInfoMapper.selectByPrimaryKey(id);
		logger.info("开始查询用户信息，查询条件ID为:" + id);
		logger.info("查询结果：" + managerInfo.toString());
		return managerInfo;
	}

	public ManagerInfo getManagerInfoByMobile(String mobile) {
		logger.info("开始查询用户信息，查询条件手机号为:" + mobile);
		ManagerInfoExample example = new ManagerInfoExample();
		example.createCriteria().andMobileEqualTo(mobile);
		List<ManagerInfo> list = managerInfoMapper.selectByExample(example);
		if (list.size() == 0) {
			return null;
		}
		logger.info("查询结果：" + list.get(0).toString());
		return list.get(0);
	}

	public ManagerInfo getManagerInfoByEmail(String email) {
		logger.info("开始查询用户信息，查询条件邮箱为:" + email);
		ManagerInfoExample example = new ManagerInfoExample();
		example.createCriteria().andEmailEqualTo(email);
		List<ManagerInfo> list = managerInfoMapper.selectByExample(example);
		if (list.size() == 0) {
			return null;
		}
		logger.info("查询结果：" + list.get(0).toString());
		return list.get(0);
	}

	@SuppressWarnings("rawtypes")
	public Result login(String userName, String password) {
		Result result = new Result<>(StatusCodes.OK, true);

		ManagerInfo managerInfo = null;
		// 判断账户是邮箱还是手机号
		if (ValidateUtil.checkEmail(userName))
			managerInfo = this.getManagerInfoByEmail(userName);
		else
			managerInfo = this.getManagerInfoByMobile(userName);

		if (managerInfo == null) {// 密码错误
			return new Result(StatusCodes.OK, false, AccountNull);
		}
		if (!Md5Util.getBaseMDCode(password).equals(managerInfo.getPassword())) {// 密码错误
			return new Result(StatusCodes.OK, false, PasswordError);
		} else {
			result.setResultCode(new ResultCode("SUCCESS", "登录成功！"));
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	public Result regist(ManagerInfo info) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null != info) {

			// UUID 作为用户id
			info.setId(UUID.randomUUID().toString());
			// 设置注册时间
			info.setCreateTime(new Date());
			info.setPassword(Md5Util.getBaseMDCode(info.getPassword()));
			// 验证用户是否已注册
			ManagerInfo managerInfo = null;
			// 判断账户是邮箱还是手机号
			if (StringUtils.isNotEmpty(info.getEmail())) {
				managerInfo = this.getManagerInfoByEmail(info.getEmail());
				if (null != managerInfo) {
					return new Result<>(StatusCodes.OK, false, EmailExists);
				}
			}
			if (StringUtils.isNotEmpty(info.getMobile())) {
				managerInfo = this.getManagerInfoByMobile(info.getMobile());
				if (null != managerInfo) {
					return new Result<>(StatusCodes.OK, false, MobileExists);
				}
			}

			// 存库
			int line = managerInfoMapper.insertSelective(info);
			if (line > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "注册成功！"));
			}
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	public Result updatePassword(String id, String oldPassword, String newPassword) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null == id || StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
			return new Result<>(StatusCodes.OK, false, ParamError);
		}
		ManagerInfo managerInfo = this.getManagerInfoByPrimaryKey(id);
		if (null == managerInfo) {// 账号不存在
			return new Result<>(StatusCodes.OK, false, AccountNull);
		} else if (!Md5Util.getBaseMDCode(oldPassword).equals(managerInfo.getPassword())) {// 密码错误
			return new Result(StatusCodes.OK, false, PasswordError);
		} else {
			// 加密
			managerInfo.setPassword(Md5Util.getBaseMDCode(newPassword));
			managerInfo.setUpdateTime(new Date());
			int line = managerInfoMapper.updateByPrimaryKeySelective(managerInfo);
			if (line > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "密码修改成功！"));
			}
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	public Result forgetPassword(String userName, String password) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
			return new Result<>(StatusCodes.OK, false, ParamError);
		}
		ManagerInfo managerInfo = null;
		// 判断账户是邮箱还是手机号
		if (ValidateUtil.checkEmail(userName))
			managerInfo = this.getManagerInfoByEmail(userName);
		else
			managerInfo = this.getManagerInfoByMobile(userName);

		if (managerInfo == null) {// 账号不存在
			return new Result(StatusCodes.OK, false, AccountNull);
		}

		// 修改密码
		managerInfo.setPassword(Md5Util.getBaseMDCode(password));
		managerInfo.setUpdateTime(new Date());
		int line = managerInfoMapper.updateByPrimaryKeySelective(managerInfo);
		if (line > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "密码修改成功！"));
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	public Result update(ManagerInfo info) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null != info) {
			// 验证用户邮箱手机号的唯一性
			ManagerInfo managerInfo = null;
			if (StringUtils.isNotEmpty(info.getEmail())) {
				managerInfo = this.getManagerInfoByEmail(info.getEmail());
				if (null != managerInfo) {
					return new Result<>(StatusCodes.OK, false, EmailExists);
				}
			}
			if (StringUtils.isNotEmpty(info.getMobile())) {
				managerInfo = this.getManagerInfoByMobile(info.getMobile());
				if (null != managerInfo) {
					return new Result<>(StatusCodes.OK, false, MobileExists);
				}
			}

			// 存库
			info.setPassword(null);// 修改资料时，不能修改密码
			info.setUpdateTime(new Date());
			int line = managerInfoMapper.updateByPrimaryKeySelective(info);
			if (line > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
			}
		}

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Result getManagerInfoByToken(String token) {
		Result result = new Result<>(StatusCodes.OK, true);
		// 解析 token,获取用户id
		JSONObject jsonObject = JWTTokenUtil.readTokenCanUse(token);
		String id = jsonObject.getString("data");

		ManagerInfoVO managerInfoVO = managerInfoMapper.getUserInfo(id);
		if (null != managerInfoVO) {
			result.setData(managerInfoVO);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Result getManagerInfoById(String id) {

		Result result = new Result<>(StatusCodes.OK, true);
		ManagerInfoVO managerInfoVO = managerInfoMapper.getUserInfo(id);
		if (null != managerInfoVO) {
			result.setData(managerInfoVO);
		}

		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString());
	}

}