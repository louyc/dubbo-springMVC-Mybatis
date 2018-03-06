package com.semioe.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.BackstageUserInfo;
import com.semioe.api.entity.ManagerInfo;
import com.semioe.api.vo.DateConditionsVO;
import com.semioe.api.vo.UserInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.common.tools.RedisTool;
import com.semioe.common.tools.util.DateTimeUtil;
import com.semioe.common.tools.util.JWTTokenUtil;
import com.semioe.common.tools.util.ValidateUtil;
import com.semioe.dubbo.service.BackstageUserInfoService;
import com.semioe.dubbo.service.DepartmentRelService;
import com.semioe.dubbo.service.ManagerService;
import com.semioe.dubbo.service.TagsRelService;
import com.semioe.web.Interceptor.AutorizadorInterceptor;
import com.semioe.web.controller.limit.RequestLimit;
import com.semioe.web.service.TokenService;
import com.semioe.web.util.RandomCode;
import com.semioe.web.util.YtxSms;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class BackstageUserController {

	private static final Logger logger = LoggerFactory.getLogger(BackstageUserController.class);

	@Reference
	private ManagerService managerService;
	@Reference
	private BackstageUserInfoService backstageUserInfoService;
	@Reference
	private DepartmentRelService departmentRelService;
	@Reference
	private TagsRelService tagsRelService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private RedisTool redisTool;
	@Autowired
	private RandomCode randomCode;

	/**
	 * login:用户登录 <br/>
	 * 
	 * @param request
	 * @param userName
	 * @param password
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings("rawtypes")
	@RequestLimit(count = 100, time = 60000)
	@RequestMapping("/login/submit")
	@ResponseBody
	public Result login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("userName") String userName, @RequestParam("password") String password) {
		logger.info("BackstageUserController.login start,userName={}", userName);

		// 判断账户是否为手机号
		if (!ValidateUtil.checkMobileNumber(userName)) {
			return new Result(StatusCodes.OK, false, new ResultCode("ACCOUNT_NULL", "账号不存在！"));
		}

		Result result = managerService.login(userName, password);
		if (result.isSuccessful()) { // 已在账户中心注册
			String userId = result.getData().toString();
			Result backstageResult = backstageUserInfoService.backstageUserLogin(userId);
			if (!backstageResult.isSuccessful() && backstageResult.getData() == null) {
				BackstageUserInfo backstageUserInfo = new BackstageUserInfo();
				backstageUserInfo.setMobile(userName);
				backstageUserInfo.setManagerId(userId);
				// 初始化信息
				backstageUserInfoService.backstageUserRegist(backstageUserInfo);
				backstageResult
						.setResultCode(new ResultCode("ACCOUNT_EXISTS", "已在账户中心注册,完善信息直接登录！"));
				backstageResult.setData(backstageUserInfo);
			}
			// cookie 中保存用户id
			tokenService.setCookieByUserId(response, userId);
			return backstageResult;
		}
		return result;
	}

	/**
	 * regist:用户注册 <br/>
	 * 
	 * @param request
	 * @param info
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings("rawtypes")
	@RequestLimit(count = 100, time = 60000)
	@RequestMapping(value = "/regist/submit", method = RequestMethod.POST)
	@ResponseBody
	public Result regist(HttpServletRequest request, @RequestBody UserInfoVO info) {

		// 参数校验
		if (StringUtils.isEmpty(info.getMobile()) || StringUtils.isEmpty(info.getRandCode())) {
			return new Result(StatusCodes.OK, false,
					new ResultCode("PARAM_ERROR", "参数错误！手机号或验证码不能为空"));
		}
		String randCode = redisTool.getMapField("accountCertified", info.getMobile(), String.class);
		if (randCode == null || !info.getRandCode().equals(randCode)) {
			return new Result(StatusCodes.OK, false, new ResultCode("RANDCODE_ERROR", "验证码错误！"));
		}
		// 调用账户中心接口，为用户注册
		ManagerInfo managerInfo = new ManagerInfo();
		String infoJosn = JSONObject.toJSONString(info);
		managerInfo = JSONObject.parseObject(infoJosn, ManagerInfo.class);
		Result result = managerService.regist(managerInfo);
		BackstageUserInfo backstageUserInfo = new BackstageUserInfo();
		backstageUserInfo.setName(info.getUserName());
		// 用户中心注册成功或者已注册，完善信息
		if (result.getStatus() == StatusCodes.OK) {

			Result backstageResult = new Result(StatusCodes.OK, true);
			// 账户中心注册成功，完善信息
			if (result.isSuccessful()) {
				backstageUserInfo.setManagerId(result.getData().toString());
				backstageUserInfo.setMobile(info.getMobile());
				backstageUserInfo.setEmail(info.getEmail());
				// 完善信息
				backstageResult = backstageUserInfoService.backstageUserRegist(backstageUserInfo);
				backstageResult.setData(backstageUserInfo.getManagerId());
				return backstageResult;
			} else {
				backstageResult = new Result(StatusCodes.OK, false,
						new ResultCode("ACCOUNT_REGISTERED", "账户已注册，请直接登录完善信息！"));
				backstageResult.setData(backstageUserInfo.getManagerId());
				return backstageResult;
			}
		} else { // 失败，返回错误结果
			return result;
		}
	}

	/**
	 * updatePassword:用户修改密码 <br/>
	 * 
	 * @param request
	 * @param oldPassword
	 * @param newPassword
	 * @return Result
	 * @exception @author
	 *                hua
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/password/update")
	@ResponseBody
	public Result updatePassword(HttpServletRequest request,
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword) {

		Result result = new Result<>(StatusCodes.OK, true);
		// String token = request.getHeader("X-Auth-Token");
		// 从COOKIE中获取用户信息，修改密码
		String token = AutorizadorInterceptor.getCookie(request, AutorizadorInterceptor.TOKEN);
		JSONObject jsonObject = JWTTokenUtil.readTokenCanUse(token);
		String id = jsonObject.getString("data");
		result = managerService.updatePassword(id, oldPassword, newPassword);

		if (StringUtils.isEmpty(result.getToken()) || !result.isSuccessful()) {
			result.setToken(token);
		}

		return result;
	}

	/**
	 * 发送密码找回验证码
	 * 
	 * @param request
	 * @param mobile
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestLimit(count = 100, time = 60000)
	@RequestMapping("/passwrod/randCode")
	@ResponseBody
	public Result sendPasswordRandomCode(HttpServletRequest request, @RequestParam String mobile) {
		Result result = new Result(StatusCodes.OK, true);
		mobile = request.getParameter("mobile");
		logger.info("sendPasswordRandomCode() start, mobile = {}", mobile);
		// 从缓存中获取验证码，如果已存在，不再发送验证码
		String randCode = redisTool.getMapField("accountPassword", mobile, String.class);
		result.setResultCode(new ResultCode("CODE_SENT", "验证码已发送，不再重复发送！"));
		if (StringUtils.isEmpty(randCode)) {
			// 生成随机验证码
			randCode = randomCode.createRandomByFlag(true, 6, true);
			// 存入缓存
			// 设置失效时间，验证码当前一分钟有效
			logger.info("save into redis, mobile = {}", mobile);
			redisTool.addMap("accountPassword", mobile, randCode,
					DateTimeUtil.modifyMinutes(new Date(), 5));
			// send
			YtxSms senSms = new YtxSms();
			logger.info("sends message to {}", mobile);
			senSms.sendCode(mobile, randCode);
			result.setResultCode(new ResultCode("SUCCESS", "验证码发送成功,5分钟内有效！"));
		}

		return result;
	}

	/**
	 * forgetPassword:忘记密码 <br/>
	 * 
	 * @param request
	 * @param userName
	 * @param password
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/password/forget")
	@ResponseBody
	public Result forgetPassword(HttpServletRequest request, @RequestParam("mobile") String mobile,
			@RequestParam("password") String password, @RequestParam("randCode") String randCode) {
		// 参数校验
		if (StringUtils.isEmpty(randCode) || StringUtils.isEmpty(mobile)) {
			return new Result(StatusCodes.OK, false,
					new ResultCode("PARAM_ERROR", "参数错误！手机号或验证码不能为空"));
		}
		String redisRandCode = redisTool.getMapField("accountPassword", mobile, String.class);
		if (redisRandCode == null || !randCode.equals(redisRandCode)) {
			return new Result(StatusCodes.OK, false, new ResultCode("RANDCODE_ERROR", "验证码错误！"));
		}
		return managerService.forgetPassword(mobile, password);
	}

	/**
	 * forgetPassword:重置密码 <br/>
	 * 
	 * @param request
	 * @param userName
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/password/reset")
	@ResponseBody
	public Result resetPassword(HttpServletRequest request,
			@RequestParam("userName") String userName) {

		// 重置默认密码为：123456
		return managerService.forgetPassword(userName, "123456");

	}

	/**
	 * update:完善用户信息 <br/>
	 * 
	 * @param request
	 * @param info
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Result update(HttpServletRequest request, @RequestBody BackstageUserInfo info) {

		Result result = new Result<>(StatusCodes.OK, true);
		String[] deptArr = new String[] {};
		String[] tagArr = new String[] {};
		if (!StringUtil.isEmpty(info.getDepts()) && info.getDepts().length() > 0
				&& info.getDepts().indexOf("$") != -1) {
			deptArr = info.getDepts().split("\\$");
		} else if (!StringUtil.isEmpty(info.getDepts()) && info.getDepts().indexOf("$") == -1) {
			deptArr = new String[] { info.getDepts() };
		}
		info.setDepts(null);
		if (!StringUtil.isEmpty(info.getTags()) && info.getTags().length() > 0
				&& info.getTags().indexOf("$") != -1) {
			tagArr = info.getTags().split("\\$");
		} else if (!StringUtil.isEmpty(info.getTags()) && info.getTags().indexOf("$") == -1) {
			tagArr = new String[] { info.getTags() };
		}
		info.setTags(null);
		tagsRelService.removeRel(info.getManagerId());
		departmentRelService.removeRel(info.getManagerId());
		if (deptArr.length != 0) {
			departmentRelService.insertDepts(deptArr, info.getManagerId());
		}
		if (tagArr.length != 0) {
			tagsRelService.insertTags(tagArr, info.getManagerId());
		}
		// 设置初始审核状态
		info.setUserStatus(BackstageUserInfo.UserStatus.PEDING.getValue());
		result = backstageUserInfoService.updateBackstageUserInfo(info);
		if (result.isSuccessful()) {
			result.setData(info);
			result.setResultCode(new ResultCode("SUCCESS", "完善资料成功！"));
		} else {
			result.setResultCode(new ResultCode("UPDATE_FAILED", "完善资料失败！"));
		}
		return result;
	}

	/**
	 * 分配用户角色
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "/deployUserRole", method = RequestMethod.POST)
	@ResponseBody
	public Result deployUserRole(@RequestParam("userId") String userId,
			@RequestParam("roleId") int roleId) {
		// log
		logger.info("BackstageUserController.deployUserRole start,userId={},roleId={}", userId,
				roleId);
		return backstageUserInfoService.deployUserRole(userId, roleId);
	}

	/* 获取字符验证码 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestLimit(count = 100, time = 60000)
	@RequestMapping("/randCode")
	@ResponseBody
	public Result sendRandomCode(HttpServletRequest request, @RequestParam String mobile) {
		Result result = new Result(StatusCodes.OK, true);
		mobile = request.getParameter("mobile");
		logger.info("sendRandomCode() start, mobile = {}", mobile);
		// 从缓存中获取验证码，如果已存在，不再发送验证码
		String randCode = redisTool.getMapField("accountCertified", mobile, String.class);
		result.setResultCode(new ResultCode("CODE_SENT", "验证码已发送，不再重复发送！"));
		if (StringUtils.isEmpty(randCode)) {
			// 生成随机验证码
			randCode = randomCode.createRandomByFlag(true, 6, true);
			// 存入缓存
			// 设置失效时间，验证码当天有效
			logger.info("save into redis, mobile = {}", mobile);
			redisTool.addMap("accountCertified", mobile, randCode, DateTimeUtil.getNextFirstTime());
			// send
			YtxSms senSms = new YtxSms();
			logger.info("sends message to {}", mobile);
			senSms.sendCode(mobile, randCode);
			result.setResultCode(new ResultCode("SUCCESS", "验证码发送成功,当天有效！"));
		}

		return result;
	}

	/**
	 * @Description: TODO 用户注册统计 @author xuyuxing @createTime
	 *               2017年7月29日下午2:39:56 @Title:
	 *               getRegisteredUserNumberListByDate @param startDate @param
	 *               endDate @param pageSize @param currentPage @return
	 *               Result @throws
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/countRegisterUser")
	public PaginatedResult getRegisteredUserNumberListByDate(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "currentPage", required = false) String currentPage) {

		int _pageSize = Integer.parseInt(pageSize);
		_pageSize = _pageSize < 1 ? 1 : _pageSize;
		int _currentPage = Integer.parseInt(currentPage);
		_currentPage = _currentPage < 1 ? 10 : _currentPage;
		int _pageSkip = (_currentPage - 1) * _pageSize;

		DateConditionsVO dateConditionsVO = new DateConditionsVO();
		dateConditionsVO.setStartDate(startDate);
		dateConditionsVO.setEndDate(endDate);
		dateConditionsVO.setShowCount(_pageSize);
		dateConditionsVO.setCurrentPage(_currentPage);
		dateConditionsVO.setCurrentResult(_pageSkip);
		return backstageUserInfoService.selectRegisterNumberListPage(dateConditionsVO);
	}
}