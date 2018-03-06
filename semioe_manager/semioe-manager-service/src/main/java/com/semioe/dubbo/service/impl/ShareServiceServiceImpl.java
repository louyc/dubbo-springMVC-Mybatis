package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.UserShareService;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.api.vo.UserShareServiceVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.ServiceInfoMapper;
import com.semioe.dubbo.dao.UserShareServiceMapper;
import com.semioe.dubbo.service.ShareServiceService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ShareServiceServiceImpl implements ShareServiceService {

	private static final Logger logger = LoggerFactory.getLogger(ShareServiceServiceImpl.class);

	public static ResultCode MysqlWrong = new ResultCode("MYSQL_WRONG", "操作数据库出错");

	@Autowired
	private UserShareServiceMapper userShareServiceMapper;
	@Autowired
	private ServiceInfoMapper serviceInfoMapper;

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result getShareServices(UserShareServiceVO shareService) {
		List<UserShareService> listService = new ArrayList<UserShareService>();
		PaginatedResult<UserShareService> pa = null;
		int totalCount = 0;
		try {
			List<UserShareService> shareService1 = userShareServiceMapper
					.selectByEntity(shareService);
			totalCount = shareService1.size();
			if (shareService.getCurrentPage() > 0) {
				listService = userShareServiceMapper.selectByEntityListPage(shareService);
			} else {
				listService = shareService1;
			}
			pa = new PaginatedResult<UserShareService>(listService, shareService.getCurrentPage(),
					shareService.getShowCount(), totalCount);
			pa.setSuccessful(true);
			pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} catch (Exception e) {
			logger.info(e.getMessage());
			pa = new PaginatedResult<>(shareService.getCurrentPage(), shareService.getShowCount());
			pa.setSuccessful(false);
			pa.setResultCode(MysqlWrong);
		}
		return pa;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result removeShareServices(UserShareServiceVO shareService) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			shareService.setInUse(0);
			userShareServiceMapper.updateInuseByPrimaryKey(shareService);
			result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.info(e.getMessage());
			result.setSuccessful(false);
			result.setResultCode(MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result addShareServices(UserShareServiceVO shareService) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			for (String s : shareService.getIdList().split(",")) {
				UserShareService us = new UserShareService();
				ServiceInfoVO service = serviceInfoMapper.selectVOByPrimaryKey(Integer.parseInt(s));
				if (null != service) {
					List<UserShareService> list = new ArrayList<UserShareService>();
					shareService.setServiceId(Integer.parseInt(s));
					list = userShareServiceMapper.selectByEntity(shareService);
					if (null == list || list.size() < 1) {
						us.setCreateTime(new Date());
						us.setDescription(service.getDescription());
						us.setInUse(1);
						us.setOrgId(shareService.getOrgId());
						us.setServiceName(service.getName());
						us.setShareId(service.getCreateUserId());
						us.setShareName(service.getCreateName());
						us.setServiceId(Integer.parseInt(s));
						userShareServiceMapper.insert(us);
					}
				}
			}
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
		} catch (Exception e) {
			logger.info(e.getMessage());
			result.setSuccessful(false);
			result.setResultCode(MysqlWrong);
		}
		return result;
	}
}
