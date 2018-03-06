package com.semioe.web.controller;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.dto.ApiMotherhoodUserDTO;
import com.semioe.api.entity.ApiMotherhoodUser;
import com.semioe.api.entity.UserDoctorRel;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.service.ApiMotherhoodUserService;
import com.semioe.dubbo.service.UserDoctorRelService;

/**
 * Created by kwinxu on 2017/12/18.
 */
@Controller
@RequestMapping("/motherhood")
public class ApiMotherhoodUserController {

	private static final Logger logger = LoggerFactory.getLogger(ApiMotherhoodUserController.class);

	@Reference
	private ApiMotherhoodUserService apiMotherhoodUserService;

	@Reference
	private UserDoctorRelService userDoctorRelService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Result add(HttpServletRequest request, HttpServletResponse response,
			@RequestBody ApiMotherhoodUserDTO apiMotherhoodUser) {

		logger.info("ApiMotherhoodUserController.add start");

		Result result = new Result(StatusCodes.OK, true);

		if (null == apiMotherhoodUser) {
			result.setResultCode(new ResultCode("FAIL", "缺少签约档案信息！"));
			return result;
		}

		if (null == apiMotherhoodUser.getDoctorId()
				&& StringUtil.isEmpty(apiMotherhoodUser.getDoctorId())) {
			result.setResultCode(new ResultCode("FAIL", "缺少签约医生信息！"));
			return result;
		}

		if (null == apiMotherhoodUser.getManagerId()
				&& StringUtil.isEmpty(apiMotherhoodUser.getManagerId())) {
			result.setResultCode(new ResultCode("FAIL", "缺少签约用户信息！"));
			return result;
		}

		// 核实后孕产期加一个月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(apiMotherhoodUser.getPregnancyBirth());
		calendar.add(Calendar.MONTH, 1);
		apiMotherhoodUser.setExpirationDate(calendar.getTime());

		try {
			int id = apiMotherhoodUserService.add(apiMotherhoodUser);
			UserDoctorRel userDoctorRel = new UserDoctorRel();
			userDoctorRel.setBuildType(3);
			userDoctorRel.setCreateTime(new Date());
			userDoctorRel.setContractedUserId(id);
			userDoctorRel.setDoctorId(apiMotherhoodUser.getDoctorId());
			userDoctorRel.setManagerId(apiMotherhoodUser.getManagerId());
			userDoctorRel.setExpirationDate(apiMotherhoodUser.getExpirationDate());
			int row = userDoctorRelService.add(userDoctorRel);
			if (row == 1) {
				result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
			} else {
				result.setResultCode(new ResultCode("FAIL", "FAIL"));
			}
			return result;
		} catch (Exception e) {
			if (e.getMessage().indexOf("！") != -1) {
				result.setResultCode(new ResultCode("FAIL", e.getMessage()));
			} else {
				e.printStackTrace();
			}
		} finally {
			return result;
		}
	}

	/**
	 * 根据 id 获取孕产妇档案
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMotherhoodRecordById", method = RequestMethod.GET)
	public Result getMotherhoodRecordById(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String id) {
		Result result = new Result(StatusCodes.OK, true);
		try {
			ApiMotherhoodUser apiMotherhoodUser = apiMotherhoodUserService.findById(id);
			result.setData(apiMotherhoodUser);
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} catch (Exception e) {
			if (e.getMessage().indexOf("！") != -1) {
				result.setResultCode(new ResultCode("FAIL", e.getMessage()));
			} else {
				e.printStackTrace();
			}
		} finally {
			return result;
		}
	}

	/**
	 * 修改孕产妇档案
	 *
	 * @param request
	 * @param response
	 * @param apiMotherhoodUser
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/modifyMotherhoodRecord", method = RequestMethod.POST)
	public Result modifyMotherhoodRecordById(HttpServletRequest request,
			HttpServletResponse response, @RequestBody ApiMotherhoodUser apiMotherhoodUser) {

		Result result = new Result(StatusCodes.OK, true);
		try {
			int row = apiMotherhoodUserService.modifyMotherhoodRecord(apiMotherhoodUser);
			if (row == 1) {
				result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
			}
		} catch (Exception e) {
			if (e.getMessage().indexOf("！") != -1) {
				result.setResultCode(new ResultCode("FAIL", e.getMessage()));
			} else {
				e.printStackTrace();
			}
		} finally {
			return result;
		}
	}

}
