package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.GoodsInfo;
import com.semioe.api.entity.Keywords;
import com.semioe.api.entity.KeywordsRel;
import com.semioe.api.entity.ServiceGoodsRel;
import com.semioe.api.entity.ServiceInfo;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.GoodsInfoMapper;
import com.semioe.dubbo.dao.KeywordsRelMapper;
import com.semioe.dubbo.dao.ServiceGoodsRelMapper;
import com.semioe.dubbo.dao.ServiceInfoMapper;
import com.semioe.dubbo.service.JyServiceService;
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
public class JyServiceServiceImpl implements JyServiceService {

	private static final Logger logger = LoggerFactory.getLogger(JyServiceServiceImpl.class);

	public static ResultCode MysqlWrong = new ResultCode("MYSQL_WRONG", "操作数据库出错");
	public static ResultCode ServiceStatusFail = new ResultCode("SERVICE_STATUS_FAIL",
			"上架和待审核状态，不允许修改");
	public static ResultCode ServiceUpdateFail = new ResultCode("SERVICE_UPDATE_FAIL",
			"更新数据为0条，请确认此数据有效");
	public static ResultCode ServiceAddFail = new ResultCode("SERVICE_ADD_FAIL", "数据库添加数据为0条");
	public static ResultCode ServiceDeleteFail = new ResultCode("SERVICE_ADD_FAIL", "数据库删除数据为0条");

	@Autowired
	private ServiceInfoMapper serviceInfoMapper;
	@Autowired
	private ServiceGoodsRelMapper serviceGoodsRelMapper;
	@Autowired
	private KeywordsRelMapper keywordsRelMapper;
	@Autowired
	private GoodsInfoMapper goodsInfoMapper;

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result getServiceInfoListPage(ServiceInfoVO serviceVO) {
		List<Map<String, Object>> listService = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listAllService = new ArrayList<Map<String, Object>>();
		List<ServiceInfoVO> listServiceVO = new ArrayList<ServiceInfoVO>();
		PaginatedResult<ServiceInfoVO> pa = null;
		int totalCount = 0;
		try {
			listAllService = serviceInfoMapper.selectJyConditionList(serviceVO);
			totalCount = listAllService.size();
			if (serviceVO.getCurrentPage() > 0) {
				listService = serviceInfoMapper.selectJyByConditionListPage(serviceVO);
			} else {
				listService = listAllService;
			}
			for (Map<String, Object> serviceMap : listService) {
				String keywordsName = "";
				// 服务关联的所有标签name信息
				// List<Map<String, Object>> listKeywords = keywordsRelMapper
				// .selectByRelationId(Integer.parseInt(serviceMap.get("id").toString()),
				// 1);
				// 服务关联的所有商品信息
				List<GoodsInfo> goodsInfoList = serviceGoodsRelMapper
						.selectGoodsById(Integer.parseInt(serviceMap.get("id").toString()));
				// 服务关联的所有标签信息
				List<Keywords> keywordsList = keywordsRelMapper.selectListByRelationId(
						Integer.parseInt(serviceMap.get("id").toString()), 1);
				serviceMap.put("goodsInfoList", goodsInfoList);
				serviceMap.put("keywordsList", keywordsList);
				if (null != keywordsList && keywordsList.size() > 0) {
					for (Keywords keywords : keywordsList) {
						keywordsName = keywordsName + "," + keywords.getName();
					}
					serviceMap.put("keywordsName",
							keywordsName.substring(1, keywordsName.length()));
				}
				ServiceInfoVO returnServiceVO = new ServiceInfoVO();
				listServiceVO.add(
						(ServiceInfoVO) MapToObject.transMap2Bean(serviceMap, returnServiceVO));
			}
			pa = new PaginatedResult<ServiceInfoVO>(listServiceVO, serviceVO.getCurrentPage(),
					serviceVO.getShowCount(), totalCount);
			pa.setSuccessful(true);
			pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} catch (Exception e) {
			logger.info(e.getMessage());
			pa = new PaginatedResult<>(serviceVO.getCurrentPage(), serviceVO.getShowCount());
			pa.setSuccessful(false);
			pa.setResultCode(MysqlWrong);
		}
		return pa;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result serviceInfoCreate(ServiceInfoVO serviceInfoVO) {
		Result result = new Result<>(StatusCodes.OK, true);
		Double goodsSum = 0.0;
		serviceInfoVO.setId(-1);
		List<ServiceInfo> listService = new ArrayList<ServiceInfo>();
		ServiceInfo serviceInfo = new ServiceInfo();
		try {
			listService = serviceInfoMapper.selectServiceByName(serviceInfoVO);
			if (listService.size() > 0) {
				result.setSuccessful(false);
				result.setResultCode(new ResultCode("NOT_UNIQUE", "已有相同服务名称数据，请修改服务名"));
				return result;
			}
			serviceInfo.setCreateTime(new Date());
			serviceInfo.setStatus(2); // 家医上架状态 20170926
			serviceInfo.setDescription(serviceInfoVO.getDescription());
			serviceInfo.setCreateUserId(serviceInfoVO.getCreateUserId());
			serviceInfo.setInUse(1);
			serviceInfo.setName(serviceInfoVO.getName());
			serviceInfo.setOpinion(serviceInfoVO.getOpinion());
			serviceInfo.setPrice(serviceInfoVO.getPrice());
			serviceInfo.setProcedureId(serviceInfoVO.getProcedureId());
			serviceInfo.setProcedureName(serviceInfoVO.getProcedureName());
			serviceInfo.setBuildType(2);
			String goodsId = serviceInfoVO.getGoodsIdList();
			String keywordsIdList = serviceInfoVO.getKeywordsIdList();
			List<ServiceGoodsRel> listServiceRel = new ArrayList<ServiceGoodsRel>();
			List<KeywordsRel> listKeywordsRel = new ArrayList<KeywordsRel>();
			int num = serviceInfoMapper.insertJy(serviceInfo);
			if (!StringUtil.isEmpty(goodsId) && goodsId.split(",").length > 0) {
				String[] goodsArray = goodsId.split(",");
				for (int i = 0; i < goodsArray.length; i++) {
					ServiceGoodsRel sgr = new ServiceGoodsRel();
					sgr.setServiceId(serviceInfo.getId());
					sgr.setGoodsId(Integer.parseInt(goodsArray[i]));
					goodsSum = goodsSum + goodsInfoMapper
							.selectByPrimaryKey(Integer.parseInt(goodsArray[i])).getPrice();
					listServiceRel.add(sgr);
				}
			}
			if (serviceInfoVO.getPrice() < goodsSum) {
				serviceInfoRemove(String.valueOf(serviceInfo.getId()));
				result.setSuccessful(false);
				result.setResultCode(new ResultCode("FALSE", "服务价格不能小于商品价格之和！"));
				return result;
			}
			if (!StringUtil.isEmpty(keywordsIdList) && keywordsIdList.split(",").length > 0) {
				String[] keywordsArray = keywordsIdList.split(",");
				for (int i = 0; i < keywordsArray.length; i++) {
					KeywordsRel kr = new KeywordsRel();
					kr.setRelationId(serviceInfo.getId());
					kr.setKeywordsId(Integer.parseInt(keywordsArray[i]));
					kr.setType(1);
					listKeywordsRel.add(kr);
				}
			}
			if (num != 0) {
				if (listKeywordsRel.size() > 0) {
					keywordsRelMapper.insertList(listKeywordsRel);
					result.setResultCode(new ResultCode("SUCCESS", "标签修改成功！"));
				}
				if (listServiceRel.size() > 0) {
					result = serviceGoodsRelCreate(listServiceRel);
				}
			} else {
				result.setResultCode(new ResultCode("FALSE", "添加失败！"));
			}
		} catch (Exception e) {
			serviceInfoRemove(String.valueOf(serviceInfo.getId()));
			logger.info(e.getMessage());
			result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	/**
	 * 删除服务信息
	 */
	@SuppressWarnings("rawtypes")
	public Result serviceInfoRemove(String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put("id", id);
			updateMap.put("inUse", 0);
			updateMap.put("updateTime", new Date());
			int i = serviceInfoMapper.updateInUse(updateMap);
			if (i > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "删除服务成功！"));
			} else {
				result = new Result(StatusCodes.OK, false, ServiceDeleteFail);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	/**
	 * 批量添加服务商品关系
	 * 
	 * @param listServiceRel
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Result serviceGoodsRelCreate(List<ServiceGoodsRel> listServiceRel) {
		int refNum = serviceGoodsRelMapper.insertList(listServiceRel);
		Result result = new Result<>(StatusCodes.OK, true);
		if (refNum != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加服务商品关系成功！"));
		} else {
			result.setResultCode(new ResultCode("FALSE", "添加服务商品关系失败！"));
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result serviceInfoUpdate(ServiceInfoVO service) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			List<ServiceInfo> listService = new ArrayList<ServiceInfo>();
			ServiceInfo serviceInfo = serviceInfoMapper.selectByPrimaryKey(service.getId());
			// 初始 下架 审核不通过的 才能修改
			if (serviceInfo.getStatus() != 0 && serviceInfo.getStatus() != 3
					&& serviceInfo.getStatus() != 4) {
				result = new Result(StatusCodes.OK, false, ServiceStatusFail);
				return result;
			}
			listService = serviceInfoMapper.selectServiceByName(service);
			if (listService.size() > 0) {
				result.setSuccessful(false);
				result.setResultCode(new ResultCode("NOT_UNIQUE", "已有相同服务名称数据，请修改服务名"));
				return result;
			}
			service.setCreateTime(serviceInfo.getCreateTime());
			service.setUpdateTime(new Date());
			service.setStatus(serviceInfo.getStatus());
			result = serviceInfoRemove(String.valueOf(service.getId()));
			if (result.isSuccessful()) {
				result = serviceInfoCreate(service);
				if (result.isSuccessful()) {
					result.setResultCode(new ResultCode("SUCCESS", "服务信息修改成功！"));
				} else {
					result.setResultCode(new ResultCode("FALSE", "服务信息修改失败！"));
				}
			} else {
				result.setResultCode(new ResultCode("FALSE", "服务信息修改失败！"));
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}
}
