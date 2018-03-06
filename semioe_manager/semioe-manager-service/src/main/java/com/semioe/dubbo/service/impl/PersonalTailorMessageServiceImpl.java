package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.PersonalTailorMessage;
import com.semioe.api.vo.PersonalTailorMessageVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.PersonalTailorMessageMapper;
import com.semioe.dubbo.service.PersonalTailorMessageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
public class PersonalTailorMessageServiceImpl implements PersonalTailorMessageService {

	@Autowired
	private PersonalTailorMessageMapper personalTailorMessageMapper;

	/**
	 * 添加
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result add(PersonalTailorMessage personalTailorMessage) {
		Result result = new Result<>(StatusCodes.OK, true);
		ResultCode resultCode = null;
		if (personalTailorMessage.getApplyType() == null || StringUtil.isEmpty(personalTailorMessage.getPersonalId())
				|| StringUtil.isEmpty(personalTailorMessage.getName())) {
			resultCode = new ResultCode("FAIL", "请完善信息！");
			result.setResultCode(resultCode);
			return result;
		}
		personalTailorMessage.setCreateTime(new Date());
		personalTailorMessage.setIsRead(0);
		personalTailorMessage.setIsHandle(0);
		int row = personalTailorMessageMapper.insert(personalTailorMessage);
		if (row > 0) {
			resultCode = new ResultCode("SUCCESS", "申请成功！");
		} else {
			resultCode = new ResultCode("FAIL", "申请失败！");
		}
		result.setResultCode(resultCode);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public PaginatedResult queryByConditions(PersonalTailorMessageVO personalTailorMessageVO) {
		List<PersonalTailorMessage> messageVOList = personalTailorMessageMapper.queryPTMListPage(personalTailorMessageVO);
		int totalCount = personalTailorMessageVO.getTotalResult();
		PaginatedResult<PersonalTailorMessage> pr = new PaginatedResult<PersonalTailorMessage>(messageVOList,
				personalTailorMessageVO.getCurrentPage(), personalTailorMessageVO.getShowCount(), totalCount);
		pr.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pr;
	}

	/**
	 * 阅读
	 */
	@Override
	public Result read(Integer id) {
		Result result = new Result<>(StatusCodes.OK, true);
		if (id == null) {
			result.setResultCode(new ResultCode("FAIL", "数据出错！"));
			return result;
		}
		PersonalTailorMessage message = new PersonalTailorMessage();
		message.setId(id);
		message.setUpdateTime(new Date());
		message.setIsRead(1);
		int row = personalTailorMessageMapper.updateByPrimaryKeySelective(message);
		if (row == 1) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FAIL", "FAIL"));
		}
		return result;
	}

	@Override
	public Result handle(Integer id) {
		Result result = new Result<>(StatusCodes.OK, true);
		if (id == null) {
			result.setResultCode(new ResultCode("FAIL", "数据出错！"));
			return result;
		}
		PersonalTailorMessage message = new PersonalTailorMessage();
		message.setId(id);
		message.setUpdateTime(new Date());
		message.setIsHandle(1);
		int row = personalTailorMessageMapper.updateByPrimaryKeySelective(message);
		if (row == 1) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FAIL", "FAIL"));
		}
		return result;
	}

}
