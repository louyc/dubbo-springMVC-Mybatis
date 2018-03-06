package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.GoodsInfo;
import com.semioe.api.entity.ImplementSet;
import com.semioe.api.vo.ImplementSetVO;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.GoodsInfoMapper;
import com.semioe.dubbo.dao.ImplementSetDetailMapper;
import com.semioe.dubbo.dao.ImplementSetMapper;
import com.semioe.dubbo.dao.PersonalTailorMapper;
import com.semioe.dubbo.dao.ServiceInfoMapper;
import com.semioe.dubbo.service.ImplementSetService;
import com.semioe.util.MapToObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ImplementSetServiceImpl implements ImplementSetService {

	private static final Logger logger = LoggerFactory.getLogger(ImplementSetServiceImpl.class);

	private static final int IN_USE = 1;
	private static final int NOT_USE = 0;
	public static ResultCode MysqlWrong = new ResultCode("MYSQL_WRONG", "操作数据库出错");
	public static ResultCode ImplementUpdateFail = new ResultCode("IMPLEMENT_UPDATE_FAIL",
			"更新数据为0条，请确认此数据有效");
	public static ResultCode ImplementAddFail = new ResultCode("IMPLEMENT_ADD_FAIL", "数据库添加数据为0条");
	public static ResultCode ImplementDeleteFail = new ResultCode("IMPLEMENT_ADD_FAIL",
			"数据库删除数据为0条");

	@Autowired
	private ImplementSetMapper implementSetMapper;
	@Autowired
	private PersonalTailorMapper personMapper;
	@Autowired
	private ImplementSetDetailMapper implentDetailMapper;
	@Autowired
	private ServiceInfoMapper serviceMapper;
	@Autowired
	private GoodsInfoMapper goodsMapper;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getImplementSetInfoByType(String type) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<ImplementSetVO> listImple = new ArrayList<ImplementSetVO>();
		try {
			listImple = implementSetMapper.selectByType(Integer.parseInt(type));
			for (ImplementSetVO imple : listImple) {
				if (!StringUtil.isEmpty(imple.getName()) && imple.getName().split("=").length > 1
						&& imple.getName().contains("?") && imple.getName().contains("/")
						&& MapToObject.isNumeric(imple.getName().split("=")[1])) {
					String urlName = imple.getName();
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
			result.setResultCode(new ResultCode("SUCCESS", "运营设置查询成功！"));
			result.setData(listImple);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result implementSetInfoCreate(ImplementSet implementSet) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			implementSet.setInUse(IN_USE);
			implementSet.setCreateTime(new Date());
			int i = implementSetMapper.insert(implementSet);
			if (i > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "运营设置添加成功！"));
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
	public Result implementSetInfoUpdate(ImplementSet implementSet) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			implementSet.setUpdateTime(new Date());
			int i = implementSetMapper.updateByPrimaryKeySelective(implementSet);
			if (i > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "运营设置更新成功！"));
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
	public Result implementSetInfoRemove(String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put("id", id);
			updateMap.put("inUse", 0);
			updateMap.put("updateTime", new Date());
			int i = implementSetMapper.updateInUse(updateMap);
			// personMapper.updateInUse(updateMap);
			// implentDetailMapper.updateInUse(updateMap);
			if (i > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "运营设置删除成功！"));
			} else {
				result = new Result(StatusCodes.OK, false, ImplementDeleteFail);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			return result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getImplementFind(String type) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<ImplementSetVO> listImple = new ArrayList<ImplementSetVO>();
		try {
			listImple = implementSetMapper.selectByType(Integer.parseInt(type));
			/*
			 * for (ImplementSetVO imple : listImple) { if
			 * (!StringUtil.isEmpty(imple.getName()) &&
			 * imple.getName().split("=").length > 1 &&
			 * imple.getName().contains("?") && imple.getName().contains("/") &&
			 * MapToObject.isNumeric(imple.getName().split("=")[1])) { String
			 * urlName = imple.getName(); if
			 * (urlName.split("=")[0].contains("service")) {// 服务
			 * List<ServiceInfoVO> serviceList = new ArrayList<ServiceInfoVO>();
			 * serviceList.add(serviceMapper
			 * .selectVOByPrimaryKey(Integer.parseInt(urlName.split("=")[1])));
			 * imple.setServiceList(serviceList); } else if
			 * (urlName.split("=")[0].contains("goodsInfo")) {// 商品
			 * List<GoodsInfo> goodsList = new ArrayList<GoodsInfo>();
			 * goodsList.add(goodsMapper
			 * .selectByPrimaryKey(Integer.parseInt(urlName.split("=")[1])));
			 * imple.setGoodsList(goodsList); } } }
			 */
			result.setResultCode(new ResultCode("SUCCESS", "运营设置查询成功！"));
			result.setData(listImple);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getImplementFindDetail(ImplementSet implementSet) {
		Result result = new Result<>(StatusCodes.OK, true);
		ImplementSetVO implement = new ImplementSetVO();
		List<ImplementSetVO> listImple = new ArrayList<ImplementSetVO>();
		List<ImplementSetVO> listImpleReturn = new ArrayList<ImplementSetVO>();
		try {
			listImple = implementSetMapper.selectByEntity(implementSet);
			for (ImplementSetVO imple : listImple) {
				if (imple.getParentId() == 0) {
					implement.setName(imple.getName());
					implement.setId(imple.getId());
					implement.setDescription(imple.getDescription());
					implement.setImgUrl(imple.getImgUrl());
					continue;
				}
				listImpleReturn.add(imple);
				if (imple.getParentId() > 0 && !StringUtil.isEmpty(imple.getName())
						&& imple.getName().split("=").length > 1 && imple.getName().contains("?")
						&& imple.getName().contains("/")
						&& MapToObject.isNumeric(imple.getName().split("=")[1])) {
					String urlName = imple.getName();
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
			implement.setImplementList(listImpleReturn);
			result.setResultCode(new ResultCode("SUCCESS", "运营设置查询成功！"));
			result.setData(implement);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result addImplementFind(ImplementSet implementSet) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			implementSet.setInUse(IN_USE);
			implementSet.setType(1);
			implementSet.setCreateTime(new Date());
			int i = implementSetMapper.insertSelective(implementSet);
			if (i > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "运营设置添加成功！"));
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
	public Result addImplementDetailFind(ImplementSetVO implementSet) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			implementSet.setUpdateTime(new Date());
			if (null != implementSet.getImplementList()
					&& implementSet.getImplementList().size() > 0) {
				for (ImplementSetVO imp : implementSet.getImplementList()) {
					imp.setInUse(1);
					imp.setType(1);
					imp.setCreateTime(new Date());
					imp.setParentId(implementSet.getId());
				}
			}

			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put("parentId", implementSet.getId());
			updateMap.put("inUse", 0);
			updateMap.put("updateTime", new Date());
			int i = implementSetMapper.updateVOSelective(implementSet);
			int update = implementSetMapper.updateInUseByParentId(updateMap);
			if (null != implementSet.getImplementList()
					&& implementSet.getImplementList().size() > 0) {
				implementSetMapper.insertListVO(implementSet.getImplementList());
			}
			if (i >= 0 && update >= 0) {
				result.setResultCode(new ResultCode("SUCCESS", "运营设置添加成功！"));
			} else {
				result = new Result(StatusCodes.OK, false, ImplementUpdateFail);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			return result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

}
