package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.PersonalTailor;
import com.semioe.api.vo.PersonalTailorVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.PersonalTailorMapper;
import com.semioe.dubbo.service.PersonalTailorService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PersonalTailorServiceImpl implements PersonalTailorService {

	private static final Logger logger = LoggerFactory.getLogger(PersonalTailorServiceImpl.class);

	private static final int IN_USE = 1;
	private static final int NOT_USE = 0;
	public static ResultCode MysqlWrong = new ResultCode("MYSQL_WRONG", "操作数据库出错");
	public static ResultCode ImplementUpdateFail = new ResultCode("IMPLEMENT_UPDATE_FAIL",
			"更新数据为0条，请确认此数据有效");
	public static ResultCode ImplementAddFail = new ResultCode("IMPLEMENT_ADD_FAIL", "数据库添加数据为0条");
	public static ResultCode ImplementDeleteFail = new ResultCode("IMPLEMENT_ADD_FAIL",
			"数据库删除数据为0条");

	@Autowired
	private PersonalTailorMapper personalTailorMapper;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getPersonalTailor(String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<PersonalTailor> listImple = new ArrayList<PersonalTailor>();
		try {
			PersonalTailorVO personalTailorVO = new PersonalTailorVO();
			personalTailorVO.setId(Integer.parseInt(id));
			listImple = personalTailorMapper.selectByEntity(personalTailorVO);
			result.setResultCode(new ResultCode("SUCCESS", "内容推荐查询成功！"));
			result.setData(listImple);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result getPersonalTailor(PersonalTailorVO personalTailorVO) {
		PaginatedResult<PersonalTailorVO> pa = null;
		List<PersonalTailorVO> listImple = new ArrayList<PersonalTailorVO>();
		List<PersonalTailorVO> listImpleSize = new ArrayList<PersonalTailorVO>();
		try {
			if (personalTailorVO.getShowCount() < 1) {
				personalTailorVO.setShowCount(1000);
			}
			if (!StringUtil.isEmpty(personalTailorVO.getTitle())) {
				personalTailorVO.setTitle(personalTailorVO.getTitle() + "%");
			}
			if (personalTailorVO.getApplyType() != null && personalTailorVO.getApplyType() < 1) {
				personalTailorVO.setApplyType(-1);
			}
			listImple = personalTailorMapper.selectEntityByConditionListPage(personalTailorVO);
			listImpleSize = personalTailorMapper.selectEntityByCondition(personalTailorVO);
			pa = new PaginatedResult<PersonalTailorVO>(listImple, personalTailorVO.getCurrentPage(),
					personalTailorVO.getShowCount(), listImpleSize.size());
			pa.setSuccessful(true);
			pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return pa;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result personalTailorCreate(PersonalTailor personalTailor) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			personalTailor.setInUse(IN_USE);
			personalTailor.setCreateTime(new Date());
			int i = personalTailorMapper.insert(personalTailor);
			if (i > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "私人订制添加成功！"));
			} else {
				result = new Result(StatusCodes.OK, false, ImplementUpdateFail);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			return result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result personalTailorUpdate(PersonalTailor personalTailor) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			personalTailor.setUpdateTime(new Date());
			int i = personalTailorMapper.updateByPrimaryKeySelective(personalTailor);
			if (i > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "私人定制更新成功！"));
			} else {
				result = new Result(StatusCodes.OK, false, ImplementUpdateFail);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			return result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result personalTailorRemove(String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			PersonalTailor personalTailor = new PersonalTailor();
			personalTailor.setInUse(0);
			personalTailor.setId(Integer.parseInt(id));
			personalTailor.setUpdateTime(new Date());
			int i = personalTailorMapper.updateByPrimaryKeySelective(personalTailor);
			if (i > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "私人定制删除成功！"));
			} else {
				result = new Result(StatusCodes.OK, false, ImplementDeleteFail);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			return result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

}
