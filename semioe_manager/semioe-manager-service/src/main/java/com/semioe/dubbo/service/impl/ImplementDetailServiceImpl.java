package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.GoodsInfo;
import com.semioe.api.entity.ImplementSetDetail;
import com.semioe.api.vo.ImplementSetDetailVO;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.GoodsInfoMapper;
import com.semioe.dubbo.dao.ImplementSetDetailMapper;
import com.semioe.dubbo.dao.ServiceInfoMapper;
import com.semioe.dubbo.service.ImplementSetDetailService;
import com.semioe.util.MapToObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ImplementDetailServiceImpl implements ImplementSetDetailService {

	private static final Logger logger = LoggerFactory.getLogger(ImplementDetailServiceImpl.class);

	private static final int IN_USE = 1;
	private static final int NOT_USE = 0;
	public static ResultCode MysqlWrong = new ResultCode("MYSQL_WRONG", "操作数据库出错");
	public static ResultCode ImplementUpdateFail = new ResultCode("IMPLEMENT_UPDATE_FAIL",
			"更新数据为0条，请确认此数据有效");
	public static ResultCode ImplementAddFail = new ResultCode("IMPLEMENT_ADD_FAIL", "数据库添加数据为0条");
	public static ResultCode ImplementDeleteFail = new ResultCode("IMPLEMENT_ADD_FAIL",
			"数据库删除数据为0条");

	@Autowired
	private ImplementSetDetailMapper ImplementSetDetailMapper;
	@Autowired
	private ServiceInfoMapper serviceMapper;
	@Autowired
	private ServiceInfoMapper serviceInfoMapper;
	@Autowired
	private GoodsInfoMapper goodsMapper;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getImplementDetail(String implementId) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<ImplementSetDetail> listImple = new ArrayList<ImplementSetDetail>();
		try {
			ImplementSetDetailVO implementSetDetail = new ImplementSetDetailVO();
			implementSetDetail.setImplementId(Integer.parseInt(implementId));
			listImple = ImplementSetDetailMapper.selectByEntity(implementSetDetail);
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
	public Result getImplementDetail(ImplementSetDetailVO implementSet) {
		PaginatedResult<ImplementSetDetailVO> pa = null;
		List<ImplementSetDetailVO> listImple = new ArrayList<ImplementSetDetailVO>();
		List<ImplementSetDetailVO> listImpleSize = new ArrayList<ImplementSetDetailVO>();
		try {
			if (implementSet.getServiceType() != null && implementSet.getServiceType() < 1) {
				implementSet.setServiceType(-1);
			}
			if (implementSet.getShowCount() < 1) {
				implementSet.setShowCount(1000);
			}
			if (!StringUtil.isEmpty(implementSet.getTitle())) {
				implementSet.setTitle(implementSet.getTitle() + "%");
			}
			listImple = ImplementSetDetailMapper.selectEntityByConditionListPage(implementSet);
			listImpleSize = ImplementSetDetailMapper.selectEntityByCondition(implementSet);
			for (ImplementSetDetailVO imple : listImple) {
				if (!StringUtil.isEmpty(imple.getContentUrl())
						&& imple.getContentUrl().split("=").length > 1
						&& imple.getContentUrl().contains("?")
						&& imple.getContentUrl().contains("/")
						&& MapToObject.isNumeric(imple.getContentUrl().split("=")[1])) {
					String urlName = imple.getContentUrl();
					if (urlName.split("=")[0].contains("service")) {// 服务
						List<ServiceInfoVO> serviceList = new ArrayList<ServiceInfoVO>();
						serviceList.add(serviceMapper
								.selectVOByPrimaryKey(Integer.parseInt(urlName.split("=")[1])));
						imple.setServiceList(serviceList);
					} else if (urlName.split("=")[0].contains("goodsInfo")) {// 商品
						List<GoodsInfo> goodsList = new ArrayList<GoodsInfo>();
						goodsList.add(goodsMapper
								.selectByPrimaryKey(Integer.parseInt(urlName.split("=")[1])));
						imple.setGoodsList(goodsList);
					}
				}
			}
			pa = new PaginatedResult<ImplementSetDetailVO>(listImple, implementSet.getCurrentPage(),
					implementSet.getShowCount(), listImpleSize.size());
			pa.setSuccessful(true);
			pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return pa;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result queryServiceAndImplement(ImplementSetDetailVO implementSet) {
		PaginatedResult<ImplementSetDetailVO> pa = null;
		List<ImplementSetDetailVO> listImple = new ArrayList<ImplementSetDetailVO>();
		// List<ImplementSetDetailVO> listImpleSize = new
		// ArrayList<ImplementSetDetailVO>();
		try {
			listImple = ImplementSetDetailMapper.selectEntityByConditionListPage(implementSet);
			// listImpleSize =
			// ImplementSetDetailMapper.selectEntityByCondition(implementSet);
			for (ImplementSetDetailVO imple : listImple) {
				if (!StringUtil.isEmpty(imple.getContentUrl())
						&& imple.getContentUrl().split("=").length > 1
						&& imple.getContentUrl().contains("?")
						&& imple.getContentUrl().contains("/")
						&& MapToObject.isNumeric(imple.getContentUrl().split("=")[1])) {
					String urlName = imple.getContentUrl();
					if (urlName.split("=")[0].contains("service")) {// 服务
						List<ServiceInfoVO> serviceList = new ArrayList<ServiceInfoVO>();
						serviceList.add(serviceMapper
								.selectVOByPrimaryKey(Integer.parseInt(urlName.split("=")[1])));
						imple.setServiceList(serviceList);
					} else if (urlName.split("=")[0].contains("goodsInfo")) {// 商品
						List<GoodsInfo> goodsList = new ArrayList<GoodsInfo>();
						goodsList.add(goodsMapper
								.selectByPrimaryKey(Integer.parseInt(urlName.split("=")[1])));
						imple.setGoodsList(goodsList);
					}
				}
			}
			pa = new PaginatedResult<ImplementSetDetailVO>(listImple, implementSet.getCurrentPage(),
					implementSet.getShowCount(), implementSet.getTotalResult());
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
	public Result implementDetailCreate(ImplementSetDetail implementSet) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			implementSet.setInUse(IN_USE);
			implementSet.setCreateTime(new Date());
			int i = ImplementSetDetailMapper.insert(implementSet);
			if (i > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "内容推荐添加成功！"));
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
	public Result implementDetailUpdate(ImplementSetDetail implementSet) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			implementSet.setUpdateTime(new Date());
			int i = ImplementSetDetailMapper.updateByPrimaryKeySelective(implementSet);
			if (i > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "内容推荐更新成功！"));
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
	public Result implementDetailRemove(String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			ImplementSetDetail implementSet = new ImplementSetDetail();
			implementSet.setInUse(0);
			implementSet.setId(Integer.parseInt(id));
			implementSet.setUpdateTime(new Date());
			int i = ImplementSetDetailMapper.updateByPrimaryKeySelective(implementSet);
			if (i > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "内容推荐删除成功！"));
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
