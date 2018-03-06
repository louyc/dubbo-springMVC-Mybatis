package com.semioe.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.PersonalTailorMessage;
import com.semioe.api.vo.PersonalTailorMessageVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.common.tools.RedisTool;
import com.semioe.common.tools.util.DateTimeUtil;
import com.semioe.dubbo.service.PersonalTailorMessageService;
import com.semioe.web.util.RandomCode;
import com.semioe.web.util.YtxSms;

@Controller
@RequestMapping(value = "/ptmessage")
public class PersonalTailorMessageController {

	@Reference
	private PersonalTailorMessageService messageService;

	@Autowired
	private RedisTool redisTool;

	@Autowired
	private RandomCode randomCode;

	/**
	 * 分页条件查询
	 * 
	 * @param pageSize
	 * @param currentPage
	 * @param applyType
	 * @param title
	 * @param name
	 * @param isRead
	 * @param isHandle
	 * @return
	 */
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	public PaginatedResult query(@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "currentPage", required = false) String currentPage,
			@RequestParam(value = "applyType", required = false) Integer applyType,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "isRead", required = false) Integer isRead,
			@RequestParam(value = "isHandle", required = false) Integer isHandle) {

		int _pageSize = Integer.parseInt(pageSize);
		_pageSize = _pageSize < 1 ? 1 : _pageSize;
		int _currentPage = Integer.parseInt(currentPage);
		_currentPage = _currentPage < 1 ? 10 : _currentPage;
		int _pageSkip = (_currentPage - 1) * _pageSize;

		PersonalTailorMessageVO messageVO = new PersonalTailorMessageVO();
		messageVO.setShowCount(_pageSize);
		messageVO.setCurrentPage(_currentPage);
		messageVO.setCurrentResult(_pageSkip);
		if (applyType != null) {
			messageVO.setApplyType(applyType);
		}
		if (!StringUtil.isEmpty(title)) {
			messageVO.setTitle(title);
		}
		if (!StringUtil.isEmpty(name)) {
			messageVO.setName(name);
		}
		if (isRead != null) {
			messageVO.setIsRead(isRead);
		}
		if (isHandle != null) {
			messageVO.setIsHandle(isHandle);
		}
		return messageService.queryByConditions(messageVO);
	}

	/**
	 * 添加
	 * 
	 * @param message
	 * @param verifyCode
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Result add(@RequestBody PersonalTailorMessage message, @RequestParam String verifyCode) {
		Result result = new Result<>(StatusCodes.OK, true);
		if (StringUtil.isEmpty(message.getMobile())) {
			result.setResultCode(new ResultCode("FAIL", "数据非法！"));
			return result;
		}
		String mobile = message.getMobile();
		String randCode = redisTool.getMapField("PTMessageCode", mobile, String.class);
		if (StringUtil.isEmpty(randCode)) {
			result.setResultCode(new ResultCode("FAIL", "非法操作！"));
			return result;
		}
		if (!verifyCode.equals(randCode)) {
			result.setResultCode(new ResultCode("FAIL", "验证码错误！"));
			return result;
		}
		redisTool.delMapField("PTMessageCode", message.getMobile());
		return messageService.add(message);
	}

	/**
	 * 阅读
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/read", method = RequestMethod.POST)
	@ResponseBody
	public Result read(@RequestParam Integer id) {
		return messageService.read(id);
	}

	/**
	 * 处理
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/handle", method = RequestMethod.POST)
	@ResponseBody
	public Result handle(@RequestParam Integer id) {
		return messageService.handle(id);
	}

	/**
	 * 发送验证码
	 * 
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/sendVerifyCode", method = RequestMethod.POST)
	@ResponseBody
	public Result sendVerifyCode(@RequestParam("mobile") String mobile) {
		Result result = new Result(StatusCodes.OK, true);
		// 从缓存中获取验证码，如果已存在，不再发送验证码
		String randCode = redisTool.getMapField("PTMessageCode", mobile, String.class);
		if (!StringUtil.isEmpty(randCode)) {
			result.setResultCode(new ResultCode("CODE_SENT", "验证码已发送，不再重复发送！"));
			return result;
		}
		// 生成随机验证码
		randCode = randomCode.createRandomByFlag(true, 6, true);
		// send
		YtxSms senSms = new YtxSms();
		senSms.sendCode(mobile, randCode);
		// 存入缓存
		// 设置失效时间，验证码当天有效
		redisTool.addMap("PTMessageCode", mobile, randCode, DateTimeUtil.getNextFirstTime());
		result.setResultCode(new ResultCode("SUCCESS", "验证码发送成功,当天有效！"));
		return result;
	}
}
