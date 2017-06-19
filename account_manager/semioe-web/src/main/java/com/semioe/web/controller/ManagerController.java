package com.semioe.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.semioe.api.entity.ManagerInfo;
import com.semioe.common.result.Result;
import com.semioe.common.result.StatusCodes;
import com.semioe.common.tools.util.JWTTokenUtil;
import com.semioe.dubbo.service.ManagerService;

@Controller
@RequestMapping("/manager")
public class ManagerController {

	private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

	@Reference
	private ManagerService managerService;

	@RequestMapping("/")
	public String goIndex() {
		return "index";
	}

	/**
	 * getManagerInfoById:根据 id 查询用户信息 <br/>
	 * 
	 * @param request
	 * @param id
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/one")
	@ResponseBody
	public Result getManagerInfoById(HttpServletRequest request, @RequestParam("id") String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		String token = request.getHeader("X-Auth-Token");
		result = managerService.getManagerInfoById(id);
		if (StringUtils.isEmpty(result.getToken()) || !result.isSuccessful()) {
			result.setToken(token);
		}
		return result;
	}

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
	@RequestMapping("/login/submit")
	@ResponseBody
	public Result login(HttpServletRequest request, @RequestParam("userName") String userName,
			@RequestParam("password") String password) {
		logger.info("ManagerController.login start,userName={}", userName);
		return managerService.login(userName, password);
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
	@RequestMapping(value = "/regist/submit", method = RequestMethod.POST)
	@ResponseBody
	public Result regist(HttpServletRequest request, @RequestBody ManagerInfo info) {
		return managerService.regist(info);
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
		String token = request.getHeader("X-Auth-Token");
		JSONObject jsonObject = JWTTokenUtil.readTokenCanUse(token);
		String id = jsonObject.getString("data");
		result = managerService.updatePassword(id, oldPassword, newPassword);

		if (StringUtils.isEmpty(result.getToken()) || !result.isSuccessful()) {
			result.setToken(token);
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
	public Result forgetPassword(HttpServletRequest request,
			@RequestParam("userName") String userName, @RequestParam("password") String password) {
		return managerService.forgetPassword(userName, password);
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
	 * update:修改用户信息 <br/>
	 * 
	 * @param request
	 * @param info
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Result update(HttpServletRequest request, @RequestBody ManagerInfo info) {

		Result result = new Result<>(StatusCodes.OK, true);
		String token = request.getHeader("X-Auth-Token");
		JSONObject jsonObject = JWTTokenUtil.readTokenCanUse(token);
		String id = jsonObject.getString("data");
		info.setId(id);
		result = managerService.update(info);
		return result;
	}

	/**
	 * currentManager:根据 token 查询用户信息 <br/>
	 * 
	 * @param request
	 * @return Result
	 * @author hua
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/current", method = RequestMethod.POST)
	@ResponseBody
	public Result currentManager(HttpServletRequest request) {
		String token = request.getHeader("X-Auth-Token");
		return managerService.getManagerInfoByToken(token);
	}


}