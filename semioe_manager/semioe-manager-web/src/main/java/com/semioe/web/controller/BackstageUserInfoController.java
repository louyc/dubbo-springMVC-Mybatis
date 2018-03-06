package com.semioe.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.ApiUserInfo;
import com.semioe.api.entity.BackstageUserInfo;
import com.semioe.api.entity.ManagerInfo;
import com.semioe.api.entity.Message;
import com.semioe.api.vo.BackstageUserInfoVO;
import com.semioe.api.vo.UserVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.service.ApiUserInfoService;
import com.semioe.dubbo.service.BackstageUserInfoService;
import com.semioe.dubbo.service.DepartmentRelService;
import com.semioe.dubbo.service.ManagerService;
import com.semioe.dubbo.service.MessageService;
import com.semioe.dubbo.service.TagsRelService;
import com.semioe.web.util.YtxSms;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/backstage")
public class BackstageUserInfoController {

	private static final Logger logger = LoggerFactory.getLogger(BackstageUserInfoController.class);

	@Reference
	private BackstageUserInfoService backstageUserInfoService;

	@Reference
	private ManagerService managerService;
	@Reference
	private MessageService messageService;

	@Reference
	private ApiUserInfoService apiUserInfoService;

	@Reference
	private DepartmentRelService departmentRelService;

	@Reference
	private TagsRelService tagsRelService;

	/**
	 * addBackstageUser 添加后台用户
	 * 
	 * @param request
	 * @param response
	 * @param backstageUserInfo
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	@RequestMapping("/add")
	@ResponseBody
	public Result addBackstageUser(HttpServletRequest request, HttpServletResponse response,
			@RequestBody BackstageUserInfo backstageUserInfo) {
		logger.info("BackstageUserInfoController.addMenu start,Parameter::",
				backstageUserInfo.getName());
		// 调用账户中心接口，为用户注册
		ManagerInfo managerInfo = new ManagerInfo();
		managerInfo.setMobile(backstageUserInfo.getMobile());
		managerInfo.setUserName(backstageUserInfo.getName());
		managerInfo.setEmail(backstageUserInfo.getEmail());
		managerInfo.setPassword("000000");

		Result result = managerService.regist(managerInfo);
		// 用户中心注册成功或者已注册，完善信息
		if (result.getStatus() == StatusCodes.OK) {
			// 账户中心注册成功，完善信息
			if (result.isSuccessful()) {
				backstageUserInfo.setManagerId(result.getData().toString());
				return backstageUserInfoService.insertBackstageUserInfo(backstageUserInfo);
			} else {
				return new Result(StatusCodes.OK, false,
						new ResultCode("ACCOUNT_REGISTERED", "账户已注册，请直接登录完善信息！"));
			}
		} else { // 失败，返回错误结果
			return result;
		}
	}

	/**
	 * modifyMenu 修改后台用户
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	@ResponseBody
	public Result modifyUser(HttpServletRequest request, HttpServletResponse response,
			@RequestBody BackstageUserInfo backstageUserInfo
			/*@RequestParam(value = "address", required = true) String address,
			@RequestParam(value = "managerId", required = true) String managerId,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "imageUrl", required = false) String imageUrl,
			@RequestParam(value = "desc", required = false) String desc,
			@RequestParam(value = "depts", required = false) String depts,
			@RequestParam(value = "tags", required = false) String tags*/) {
		
		logger.info("BackstageUserInfoController.modifyUser start,Parameter::", backstageUserInfo);
		
		if (backstageUserInfo == null || backstageUserInfo.getManagerId() == null
				|| StringUtil.isEmpty(backstageUserInfo.getManagerId()) ) {
			Result result = new Result<>(StatusCodes.OK, false);
			result.setResultCode(new ResultCode("DATA_IS_NULL", "数据传输为空"));
			return result;
		}
		
		/* 该接口不处理用户部门等信息。
		 * 
		String[] deptArr = new String[] {};
		String[] tagArr = new String[] {};
		
		String depts = backstageUserInfo.getDepts();
		String tags = backstageUserInfo.getTags();
		
		if(depts != null && !"".equals(depts)){
			if (depts.length() > 0 && depts.indexOf("$") != -1) {
				deptArr = depts.split("\\$");
			} else if (depts.indexOf("$") == -1) {
				deptArr = new String[] { depts };
			}
		}
		
		if(tags != null && !"".equals(tags)){
			if (tags.length() > 0 && tags.indexOf("$") != -1) {
				tagArr = tags.split("\\$");
			} else if (tags.indexOf("$") == -1) {
				tagArr = new String[] { tags };
			}
		}

		tagsRelService.removeRel(backstageUserInfo.getManagerId());
		departmentRelService.removeRel(backstageUserInfo.getManagerId());
		if (deptArr.length != 0) {
			departmentRelService.insertDepts(deptArr, backstageUserInfo.getManagerId());
		}
		if (tagArr.length != 0) {
			tagsRelService.insertTags(tagArr, backstageUserInfo.getManagerId());
		}*/
		return backstageUserInfoService.updateBackstageUserInfo(backstageUserInfo);
	}
	
	/**
	 * removeMenu 删除用户
	 * 
	 * @param request
	 * @param response
	 * @param managerId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/remove")
	@ResponseBody
	public Result removeUser(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("managerId") String managerId) {
		logger.info("BackstageUserInfoController.removeUser start,managerId={}", managerId);
		return backstageUserInfoService.deleteBackstageUserInfo(managerId);
	}

	/**
	 * queryMenu 查询用户
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param roleId
	 * @param userStatus
	 * @param inUse
	 * @param pageSize
	 *            每页条数
	 * @param pageNumber
	 *            页数
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/query")
	@ResponseBody
	public Result queryUserList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "shieldJY", required = false) String shieldJY,
			@RequestParam(value = "managerId", required = false) String managerId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "roleId", required = false) String roleId,
			@RequestParam(value = "userStatus", required = false) String userStatus,
			@RequestParam(value = "inUse", required = false) String inUse,
			@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam(value = "deptId", required = false) String deptId,

			@RequestParam(value = "province", required = false) String province, // 省
			@RequestParam(value = "city", required = false) String city, // 市
			@RequestParam(value = "town", required = false) String town, // 县

			@RequestParam(value = "pageSize", required = true) String pageSize,
			@RequestParam(value = "pageNumber", required = true) String pageNumber) {
		logger.info("BackstageUserInfoController.queryUserList start,name={}", name);
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != name && !name.isEmpty()) {
			map.put("name", name + "%");
		}
		if (null != roleId && !roleId.isEmpty()) {
			map.put("roleId", roleId);
		}
		if (null != userStatus && !userStatus.isEmpty()) {
			map.put("userStatus", userStatus);
		}
		if (null != inUse && !inUse.isEmpty()) {
			map.put("inUse", inUse);
		}
		if (null != managerId && !managerId.isEmpty()) {
			map.put("managerId", managerId);
		}
		if (null != mobile && !mobile.isEmpty()) {
			map.put("mobile", mobile);
		}
		if (null != deptId && !deptId.isEmpty() && !deptId.equals("0")) {
			map.put("deptId", deptId);
		}
		if (null != shieldJY && !shieldJY.isEmpty()) {
			map.put("shieldJY", shieldJY);
		}

		if (null != province && !province.isEmpty()) {
			map.put("province", province);
		}
		if (null != city && !city.isEmpty()) {
			map.put("city", city);
		}
		if (null != town && !town.isEmpty()) {
			map.put("town", town);
		}
		int size = Integer.parseInt(pageSize);
		int num = Integer.parseInt(pageNumber);
		map.put("pageSize", size);
		map.put("pageNumber", pageNumber);
		map.put("beginSize", (num - 1) * size);
		return backstageUserInfoService.getAllBackstageUserInfo(map);
	}

	/**
	 * queryMenu 查询某个用户
	 * 
	 * @param request
	 * @param response
	 * @param managerId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryOne")
	@ResponseBody
	public Result queryOneUser(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "managerId", required = true) String managerId) {
		logger.info("BackstageUserInfoController.queryOneUser start,managerId={}", managerId);
		return backstageUserInfoService.getOneUserAndDepts(managerId);
	}

	/**
	 * queryByRole 查询用户根据角色
	 * 
	 * @param request
	 * @param response
	 * @param managerId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryByRole")
	@ResponseBody
	public Result queryByRole(HttpServletRequest request, HttpServletResponse response,
			@RequestBody BackstageUserInfoVO info) {
		logger.info("BackstageUserInfoController.queryByRole start,info={}", info.getRoleId());
		int currentResult = (info.getCurrentPage() - 1) * info.getShowCount();
		info.setCurrentResult(currentResult);
		if (info.getRoles() != null && !info.getRoles().isEmpty()) {
			List<Integer> listRole = new ArrayList<Integer>();
			for (String s : info.getRoles().split(",")) {
				listRole.add(Integer.parseInt(s));
			}
			info.setListRole(listRole);
		}
		info.setName(info.getName() + "%");
		return backstageUserInfoService.queryByRole(info);
	}

	/**
	 * 用户审核
	 * 
	 * @param request
	 * @param response
	 * @param managerId
	 *            用户id
	 * @param auditMessage
	 *            审核意见
	 * @param userStatus
	 *            审核状态
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/audit")
	@ResponseBody
	public Result auditUser(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "managerId", required = false) String managerId,
			@RequestParam(value = "auditMessage", required = false) String auditMessage,
			@RequestParam(value = "userStatus", required = false) String userStatus) {
		logger.info("BackstageUserInfoController.queryOneUser start,managerId={}",
				managerId + " " + auditMessage);
		if (StringUtils.isEmpty(managerId) || StringUtils.isEmpty(userStatus)) {
			return new Result(StatusCodes.OK, false,
					new ResultCode("PARAM_ERROR", "参数错误！用户id或者审核状态不能为空"));
		}
		BackstageUserInfo user = (BackstageUserInfo) backstageUserInfoService.getOneUser(managerId)
				.getData();
		String mobile = user.getMobile();
		int beforeStatus = user.getUserStatus();
		user = new BackstageUserInfo();
		user.setUserStatus(Integer.parseInt(userStatus));
		user.setManagerId(managerId);
		Result result = backstageUserInfoService.updateBackstageUserInfo(user);
		// 0.未完善，1.审核通过，2.待审核，3.驳回申请，4.禁用
		if (result.isSuccessful()) {
			YtxSms senSms = new YtxSms();
			if (StringUtils.isEmpty(auditMessage)) {
				if (userStatus.equals("1")) {
					auditMessage = "<审核通过>";
				} else if (userStatus.equals("3")) {
					auditMessage = "<被驳回>";
				}
			} else {
				if (userStatus.equals("3")) {
					auditMessage = "<被退回>" + " 退回原因：" + auditMessage;
				}
			}

			// 消息存库
			Message message = new Message();
			try {
				senSms.sendExamineMessage(mobile, auditMessage);

				message.setMessageTo(managerId);
				message.setType(Message.Type.SMS.getValue());
				message.setContent(auditMessage);
				message.setStatus(1);
				message.setUserStatus(Integer.parseInt(userStatus));
			} catch (Exception e) {
				logger.info(e.getMessage());
				message.setStatus(0);
				user.setUserStatus(beforeStatus);
				backstageUserInfoService.updateBackstageUserInfo(user);
				result.setSuccessful(false);
				result.setResultCode(new ResultCode("MESSAGE_SEND_FAIL", "手机短信发送失败"));
			}
			messageService.createMessage(message);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/queryUser")
	@ResponseBody
	public Result queryUser(HttpServletRequest request, @RequestParam("userId") String userId) {
		Result result = new Result<>(StatusCodes.OK, true);
		UserVO userVO = new UserVO();
		ApiUserInfo user = apiUserInfoService.getCurrentUserInfo(userId);
		BackstageUserInfo backstageUserInfo = (BackstageUserInfo) backstageUserInfoService
				.getOneUser(userId).getData();
		if (null != user && !StringUtil.isEmpty(user.getManagerId())) {
			String userInfoJosn = JSONObject.toJSONString(user);
			userVO.setCustomer(userInfoJosn);
		}
		if (null != backstageUserInfo) {
			String doctorInfoJosn = JSONObject.toJSONString(backstageUserInfo);
			userVO.setDoctor(doctorInfoJosn);
		}
		result.setData(userVO);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		return result;

	}

	@RequestMapping(value = "/managerList", method = RequestMethod.POST)
	@ResponseBody
	public PaginatedResult queryManagerList(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "roleId", required = false) String roleId,
			@RequestParam(value = "userStatus", required = false) String userStatus,
			@RequestParam(value = "inUse", required = false) String inUse,
			@RequestParam(value = "mobile", required = false) String mobile,

			@RequestParam(value = "province", required = false) String province, // 省
			@RequestParam(value = "city", required = false) String city, // 市
			@RequestParam(value = "town", required = false) String town, // 县

			@RequestParam(value = "pageSize", required = true) String pageSize,
			@RequestParam(value = "pageNumber", required = true) String pageNumber) {

		BackstageUserInfoVO backstageUserInfoVO = new BackstageUserInfoVO();

		if (null != name && !StringUtil.isEmpty(name)) {
			backstageUserInfoVO.setName(name);
		}
		if (null != roleId && !StringUtil.isEmpty(roleId)) {
			backstageUserInfoVO.setRoleId(Integer.valueOf(roleId));
		}
		if (null != userStatus && !StringUtil.isEmpty(userStatus)) {
			backstageUserInfoVO.setUserStatus(Integer.valueOf(userStatus));
		}
		if (null != inUse && !StringUtil.isEmpty(inUse)) {
			backstageUserInfoVO.setInUse(Integer.valueOf(inUse));
		}
		if (null != mobile && !StringUtil.isEmpty(mobile)) {
			backstageUserInfoVO.setMobile(mobile);
		}
		if (null != province && !StringUtil.isEmpty(province)) {
			backstageUserInfoVO.setProvinceCode(province);
		}
		if (null != city && !StringUtil.isEmpty(city)) {
			backstageUserInfoVO.setCityCode(city);
		}
		if (null != town && !StringUtil.isEmpty(town)) {
			backstageUserInfoVO.setTownCode(town);
		}
		if (null != pageSize && !StringUtil.isEmpty(pageSize)) {
			backstageUserInfoVO.setShowCount(Integer.valueOf(pageSize));
		}
		if (null != pageNumber && !StringUtil.isEmpty(pageNumber)) {
			backstageUserInfoVO.setCurrentPage(Integer.valueOf(pageNumber));
		}

		return backstageUserInfoService.queryUserListPage(backstageUserInfoVO);
	}

	/**
	 * 修改用户标签
	 * 
	 * @param request
	 * @param response
	 * @param managerId
	 * @param tags
	 * @return
	 */
	@RequestMapping(value = "/modifyTags", method = RequestMethod.POST)
	@ResponseBody
	public Result modifyTags(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "manager_id") String managerId,
			@RequestParam(value = "tags") String tags) {
		Result result = new Result<>(StatusCodes.OK, true);
		if (null == managerId || StringUtil.isEmpty(managerId)) {
			result.setResultCode(new ResultCode("FAIL", "请验证数据完整性！"));
			return result;
		}
		String[] tagArr = new String[] {};
		if (null != tags && !StringUtil.isEmpty(tags) && tags.indexOf("$") != -1) {
			tagArr = tags.split("\\$");
		} else if (null != tags && !StringUtil.isEmpty(tags)) {
			tagArr = new String[] { tags };
		}
		tagsRelService.removeRel(managerId);
		int row = tagsRelService.insertTags(tagArr, managerId);
		if (row == tagArr.length) {
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
			return result;
		} else {
			result.setResultCode(new ResultCode("FAIL", "修改失败！"));
		}
		return result;
	}
}
