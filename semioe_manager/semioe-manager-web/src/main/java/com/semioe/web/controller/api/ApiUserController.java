package com.semioe.web.controller.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.ApiUserInfo;
import com.semioe.api.entity.ManagerInfo;
import com.semioe.api.vo.ApiUserInfoVO;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.common.tools.RedisTool;
import com.semioe.common.tools.util.DateTimeUtil;
import com.semioe.dubbo.service.ApiUserInfoService;
import com.semioe.dubbo.service.ManagerService;
import com.semioe.dubbo.service.QrcodeRelService;
import com.semioe.web.service.TokenService;
import com.semioe.web.util.RandomCode;
import com.semioe.web.util.YtxSms;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;
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
@RequestMapping("/apiUser")
public class ApiUserController {

	private static final Logger logger = LoggerFactory.getLogger(ApiUserController.class);

	@Reference
	private ManagerService managerService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private RedisTool redisTool;
	@Autowired
	private RandomCode randomCode;

	@Reference
	private ApiUserInfoService apiUserInfoService;

	@Reference
	private QrcodeRelService qrcodeRelService;

	/**
	 * login:用户登录 <br/>
	 * 
	 * @param request
	 * @param userName
	 * @return Result
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/login")
	public void login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "url", required = false) String url,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "openId", required = false) String openId,
			@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam(value = "randCode", required = false) String randCode,
			@RequestParam(value = "headimgurl", required = false) String headimgurl)
			throws IOException {
		logger.info("ApiUserController.login start,openId={}",
				openId + " mobile:" + mobile + "  city:" + city);
		if (!StringUtil.isEmpty(city)) {
			city = URLDecoder.decode(city);
			logger.info("  city:" + city);
		}
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			String randCodeRedis = redisTool.getMapField("apiUserCertified", mobile, String.class);
			if (randCodeRedis == null || !randCode.equals(randCodeRedis)) {
				response.sendRedirect("/m/login.html?mobile=" + mobile + "&message="
						+ URLEncoder.encode("验证码错误！") + "&randCode=" + randCode);
				return;
			}

			String managerId = "";
			ManagerInfo manager = managerService.getManagerInfoByMobile(mobile);
			if (null != manager && manager.getId() != null) { // 已在账户中心注册
				managerId = manager.getId();
				result = apiUserInfoService.getApiUserInfoById(manager.getId(), mobile, openId,
						headimgurl, url, city);
				// cookie 中保存用户id
				tokenService.setCookieByUserId(response, managerId);
				logger.info("保存结果：{}", result.getData());
				// 用户注册更新关注数据
			} else {
				manager = new ManagerInfo();
				manager.setMobile(mobile);
				manager.setPassword("000000");
				result = managerService.regist(manager);
				if (result.getStatus() == StatusCodes.OK) {
					// 账户中心注册成功,微信后台增加用户
					managerId = result.getData().toString();
					result = apiUserInfoService.getApiUserInfoById(managerId, mobile, openId,
							headimgurl, url, city);
					// cookie 中保存用户id
					tokenService.setCookieByUserId(response, managerId);
					logger.info("保存结果：{}", result.getData());
					// 用户注册更新关注数据
				} else {
					response.sendRedirect("/m/login.html?mobile=" + mobile + "&message="
							+ URLEncoder.encode("账户中心注册失败"));
					return;
				}
			}
			if (url.contains("?")) {
				url = url + "&managerId=" + managerId;
			} else {
				url = url + "?managerId=" + managerId;
			}
			ApiUserInfoVO userVO = (ApiUserInfoVO) result.getData();
			userVO.setManagerId(managerId);
			userVO.setOpenId(openId);
			qrcodeRelService.firstLoginUser(userVO);
		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.error("{}", e);
			response.sendRedirect(
					"/m/login.html?mobile=" + mobile + "&message=" + URLEncoder.encode("登录失败"));
			return;
		}
		response.sendRedirect("/m/jump.html?url=" + url);
		return;
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
	public Result update(HttpServletRequest request, @RequestBody ApiUserInfoVO info,
			HttpServletResponse response) {
		Result result = new Result<>(StatusCodes.OK, true);
		if (!StringUtil.isEmpty(info.getMobile())) {
			if (!StringUtil.isEmpty(info.getRandCode())) {
				ApiUserInfo ui = apiUserInfoService.getCurrentUserInfo(info.getManagerId());
				if (!ui.getMobile().equals(info.getMobile())) {
					String randCodeRedis = redisTool.getMapField("apiUserUpdateMobile",
							info.getMobile(), String.class);
					String randCode = info.getRandCode();
					if (randCodeRedis == null || !randCode.equals(randCodeRedis)) {
						result = new Result(StatusCodes.OK, false,
								new ResultCode("FAIL", "验证码校验失败"));
						return result;
					}
				}
			}
			String managerId = "";
			ManagerInfo manager = managerService.getManagerInfoByMobile(info.getMobile());
			if (null != manager && manager.getId() != null) { // 已在账户中心注册
				managerId = manager.getId();
				logger.info("保存结果：{}", managerId);
				// cookie 中保存用户id
				tokenService.setCookieByUserId(response, managerId);
			} else {
				manager = new ManagerInfo();
				manager.setMobile(info.getMobile());
				manager.setPassword("000000");
				result = managerService.regist(manager);
				if (result.getStatus() == StatusCodes.OK) {
					// 账户中心注册成功,微信后台增加用户
					managerId = result.getData().toString();
					// cookie 中保存用户id
					tokenService.setCookieByUserId(response, managerId);
					logger.info("保存结果：{}", result.getData());
				} else {
					result = new Result(StatusCodes.OK, false,
							new ResultCode("FAIL", "账户系统注册用户失败"));
					return result;
				}
			}
			info.setAccountManagerId(managerId);
			result = apiUserInfoService.updateApiUserInfo(info);
			return result;
		}
		return apiUserInfoService.updateApiUserInfo(info);
	}

	/**
	 * 微信端用户登录 发送验证码
	 * 
	 * @param request
	 * @param mobile
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/randCode")
	@ResponseBody
	public Result sendRandomCode(HttpServletRequest request,
			@RequestParam("mobile") String mobile) {
		Result result = new Result(StatusCodes.OK, true);
		// 从缓存中获取验证码，如果已存在，不再发送验证码
		String randCode = redisTool.getMapField("apiUserCertified", mobile, String.class);
		result.setResultCode(new ResultCode("CODE_SENT", "验证码已发送，不再重复发送！"));
		if (StringUtils.isEmpty(randCode)) {
			// 生成随机验证码
			randCode = randomCode.createRandomByFlag(true, 6, true);
			// send
			YtxSms senSms = new YtxSms();
			senSms.sendCode(mobile, randCode);
			// 存入缓存
			// 设置失效时间，验证码当天有效
			redisTool.addMap("apiUserCertified", mobile, randCode, DateTimeUtil.getNextFirstTime());
			result.setResultCode(new ResultCode("SUCCESS", "验证码发送成功,当天有效！"));
		}
		return result;
	}

	/**
	 * 微信端用户修改电话号 发送验证码
	 * 
	 * @param request
	 * @param mobile
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/updateRandCode")
	@ResponseBody
	public Result sendUpdateMobileRandomCode(HttpServletRequest request,
			@RequestParam("mobile") String mobile) {
		Result result = new Result(StatusCodes.OK, true);
		// 从缓存中获取验证码，如果已存在，不再发送验证码
		String randCode = redisTool.getMapField("apiUserUpdateMobile", mobile, String.class);
		result.setResultCode(new ResultCode("CODE_SENT", "验证码已发送，不再重复发送！"));
		if (StringUtils.isEmpty(randCode)) {
			// 生成随机验证码
			randCode = randomCode.createRandomByFlag(true, 6, true);
			// send
			YtxSms senSms = new YtxSms();
			senSms.sendCode(mobile, randCode);
			// 存入缓存
			// 设置失效时间，验证码5分钟内有效
			redisTool.addMap("apiUserUpdateMobile", mobile, randCode,
					DateTimeUtil.modifyMinutes(new Date(), 1));
			result.setResultCode(new ResultCode("SUCCESS", "验证码发送成功,1分钟内有效！"));
		}
		return result;
	}

	/**
	 * 添加子账户
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/addSubAccount")
	@ResponseBody
	public Result createSubAccount(HttpServletRequest request,
			@RequestBody ApiUserInfoVO userInfo) {
		Result result = new Result<>(StatusCodes.OK, true);
		String managerId = "";
		if (!StringUtil.isEmpty(userInfo.getMobile())) {
			if (!StringUtil.isEmpty(userInfo.getRandCode())) {
				ApiUserInfo ui = apiUserInfoService.getCurrentUserInfo(userInfo.getManagerId());
				if (!ui.getMobile().equals(userInfo.getMobile())) {
					String randCodeRedis = redisTool.getMapField("apiUserUpdateMobile",
							userInfo.getMobile(), String.class);
					String randCode = userInfo.getRandCode();
					if (randCodeRedis == null || !randCode.equals(randCodeRedis)) {
						result = new Result(StatusCodes.OK, false,
								new ResultCode("FAIL", "验证码校验失败"));
						return result;
					}
				}
			}
			ManagerInfo manager = managerService.getManagerInfoByMobile(userInfo.getMobile());
			if (null != manager && manager.getId() != null) { // 已在账户中心注册
				managerId = manager.getId();
				return apiUserInfoService.addSubAccount(userInfo, managerId);
			} else {
				manager = new ManagerInfo();
				manager.setMobile(userInfo.getMobile());
				manager.setPassword("000000");
				result = managerService.regist(manager);
				if (result.getStatus() == StatusCodes.OK) {
					// 账户中心注册成功,微信后台增加用户
					managerId = result.getData().toString();
					return apiUserInfoService.addSubAccount(userInfo, managerId);
				} else {
					return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "账户中心注册失败"));
				}
			}
		} else {
			managerId = UUID.randomUUID().toString();
			return apiUserInfoService.addSubAccount(userInfo, managerId);
		}

	}

	/**
	 * 查询所有相关账户
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/querySubAccount")
	@ResponseBody
	public Result queryAllSubAccount(HttpServletRequest request,
			@RequestParam("managerId") String managerId) {

		return apiUserInfoService.getAllApiUserInfo(managerId);
	}

	/**
	 * 查询父账户
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryParentAccount")
	@ResponseBody
	public Result queryParentAccount(HttpServletRequest request,
			@RequestParam("managerId") String managerId) {

		return apiUserInfoService.getParentUserInfo(managerId);
	}

	/**
	 * 查询当前账户信息
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/queryAccount")
	@ResponseBody
	public Result queryAccount(HttpServletRequest request,
			@RequestParam("managerId") String managerId) {
		Result result = new Result<>(StatusCodes.OK, true);
		ApiUserInfo user = apiUserInfoService.getCurrentUserInfo(managerId);
		result.setData(user);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		return result;

	}

	/**
	 * 切换当前账户信息
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/changeAccount")
	@ResponseBody
	public Result changeAccount(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("managerId") String managerId) {
		Result result = new Result<>(StatusCodes.OK, true);
		ApiUserInfo user = apiUserInfoService.changeAccount(managerId);
		tokenService.setCookieByUserId(response, managerId);
		result.setData(user);
		result.setToken(redisTool.get(managerId));
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		return result;

	}

	/**
	 * 删除子账户
	 * 
	 * @param request
	 * @param roleInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/delSubAccount")
	@ResponseBody
	public Result removeSubAccount(HttpServletRequest request,
			@RequestParam("managerId") String managerId) {
		return apiUserInfoService.deleteApiUserInfo(managerId);
	}

	/**
	 * 查询所有角色关系
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryRole")
	@ResponseBody
	public Result queryAllUserRole(HttpServletRequest request) {

		return apiUserInfoService.queryAllApiRoleInfo();
	}

}
