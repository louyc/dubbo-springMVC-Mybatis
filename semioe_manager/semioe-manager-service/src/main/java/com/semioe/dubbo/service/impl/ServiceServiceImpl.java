package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.GoodsInfo;
import com.semioe.api.entity.JyOrgDocRel;
import com.semioe.api.entity.Keywords;
import com.semioe.api.entity.KeywordsRel;
import com.semioe.api.entity.ServiceGoodsRel;
import com.semioe.api.entity.ServiceInfo;
import com.semioe.api.entity.UserShareService;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.GoodsInfoMapper;
import com.semioe.dubbo.dao.JyOrgDocRelMapper;
import com.semioe.dubbo.dao.KeywordsMapper;
import com.semioe.dubbo.dao.KeywordsRelMapper;
import com.semioe.dubbo.dao.ServiceGoodsRelMapper;
import com.semioe.dubbo.dao.ServiceInfoMapper;
import com.semioe.dubbo.dao.UserShareServiceMapper;
import com.semioe.dubbo.service.ServiceService;
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
public class ServiceServiceImpl implements ServiceService {

	private static final Logger logger = LoggerFactory.getLogger(ServiceServiceImpl.class);

	public static ResultCode MysqlWrong = new ResultCode("MYSQL_WRONG", "操作数据库出错");
	public static ResultCode ServiceStatusFail = new ResultCode("SERVICE_STATUS_FAIL", "上架和待审核状态，不允许修改");
	public static ResultCode ServiceUpdateFail = new ResultCode("SERVICE_UPDATE_FAIL", "更新数据为0条，请确认此数据有效");
	public static ResultCode ServiceAddFail = new ResultCode("SERVICE_ADD_FAIL", "数据库添加数据为0条");
	public static ResultCode ServiceDeleteFail = new ResultCode("SERVICE_ADD_FAIL", "数据库删除数据为0条");

	@Autowired
	private ServiceInfoMapper serviceInfoMapper;
	@Autowired
	private ServiceGoodsRelMapper serviceGoodsRelMapper;
	@Autowired
	private KeywordsRelMapper keywordsRelMapper;
	@Autowired
	private KeywordsMapper keywordsMapper;
	@Autowired
	private GoodsInfoMapper goodsInfoMapper;
	@Autowired
	private JyOrgDocRelMapper jyOrgDocMapper;
	@Autowired
	private UserShareServiceMapper userShareServiceMapper;

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result getServiceInfoListPage(ServiceInfoVO serviceVO) {
		List<ServiceInfoVO> listService = new ArrayList<ServiceInfoVO>();
		PaginatedResult<ServiceInfoVO> pa = null;
		try {
			listService = serviceInfoMapper.selectByConditionListPage(serviceVO);
			for (ServiceInfoVO service : listService) {
				String keywordsName = "";
				String keywordsIdList = "";
				// 服务关联的所有标签name信息
				// List<Map<String, Object>> listKeywords = keywordsRelMapper
				// .selectByRelationId(Integer.parseInt(service.getId().toString()),
				// 1);
				// 服务关联的所有商品信息
				List<GoodsInfo> goodsInfoList = serviceGoodsRelMapper
						.selectGoodsById(Integer.parseInt(service.getId().toString()));
				// 服务关联的所有标签信息
				List<Keywords> keywordsList = keywordsRelMapper
						.selectListByRelationId(Integer.parseInt(service.getId().toString()), 1);
				service.setGoodsInfoList(goodsInfoList);
				service.setKeywordsList(keywordsList);
				if (null != keywordsList && keywordsList.size() > 0) {
					for (Keywords keywords : keywordsList) {
						keywordsName = keywordsName + "," + keywords.getName();
						keywordsIdList = keywordsIdList + "," + keywords.getId();
					}
					service.setKeywordsName(keywordsName.substring(1, keywordsName.length()));
					service.setKeywordsIdList(keywordsIdList.substring(1, keywordsIdList.length()));
				}
				// listServiceVO.add(
				// (ServiceInfoVO) MapToObject.transMap2Bean(serviceMap,
				// returnServiceVO));
			}
			pa = new PaginatedResult<ServiceInfoVO>(listService, serviceVO.getCurrentPage(), serviceVO.getShowCount(),
					serviceVO.getTotalResult());
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

	/**
	 * 根据标签查询服务
	 * 
	 * @author quanlj
	 * @param serviceVO
	 * @return
	 */
	@Override
	public PaginatedResult<ServiceInfoVO> getServiceInfoVoByKeywords(ServiceInfoVO serviceVO) {
		PaginatedResult<ServiceInfoVO> result = null;
		List<ServiceInfoVO> serviceList = new ArrayList<>();
		// 分页查询数据
		if (0 < serviceVO.getCurrentPage() && 0 < serviceVO.getShowCount()) {
			// 查询总页数
			Integer totalCount = (int) serviceInfoMapper.countServiceInfoVoByKeywords(serviceVO);
			// 计算分页
			int beginSize = (serviceVO.getCurrentPage() - 1) * serviceVO.getShowCount();
			serviceVO.setCurrentResult(beginSize);
			// 查询数据列表
			serviceList = serviceInfoMapper.getServiceInfoVoByKeywords(serviceVO);
			result = new PaginatedResult<>(serviceList, serviceVO.getCurrentPage(), serviceVO.getShowCount(),
					totalCount);
		} else {
			serviceList = serviceInfoMapper.getServiceInfoVoByKeywords(serviceVO);
			result = new PaginatedResult<>(serviceList, -1, -1, serviceList.size());
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result queryGoodsById(String id) {
		ServiceInfoVO serviceVO = new ServiceInfoVO();
		serviceVO.setInUse(1);
		serviceVO.setId(Integer.parseInt(id));
		List<Map<String, Object>> listService = new ArrayList<Map<String, Object>>();
		List<ServiceInfoVO> listServiceVO = new ArrayList<ServiceInfoVO>();
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			String keywordsName = "";
			String keywordsIdList = "";
			String goodsIdList = "";
			listService = serviceInfoMapper.selectById(serviceVO);
			for (Map<String, Object> serviceMap : listService) {
				// 服务关联的所有商品信息
				List<GoodsInfo> goodsInfoList = serviceGoodsRelMapper.selectGoodsById(Integer.parseInt(id));
				if (null != goodsInfoList && goodsInfoList.size() > 0) {
					for (GoodsInfo goods : goodsInfoList) {
						goodsIdList = goodsIdList + "," + goods.getId();
					}
					serviceMap.put("goodsIdList", goodsIdList.substring(1, goodsIdList.length()));
				}
				// 服务关联的所有标签信息
				List<Keywords> keywordsList = keywordsRelMapper.selectListByRelationId(Integer.parseInt(id), 1);
				if (null != keywordsList && keywordsList.size() > 0) {
					for (Keywords keywords : keywordsList) {
						keywordsName = keywordsName + "," + keywords.getName();
						keywordsIdList = keywordsIdList + "," + keywords.getId();
					}
					serviceMap.put("keywordsName", keywordsName.substring(1, keywordsName.length()));
					serviceMap.put("keywordsIdList", keywordsIdList.substring(1, keywordsIdList.length()));
				}
				serviceMap.put("goodsInfoList", goodsInfoList);
				serviceMap.put("keywordsList", keywordsList);
				ServiceInfoVO returnServiceVO = new ServiceInfoVO();
				listServiceVO.add((ServiceInfoVO) MapToObject.transMap2Bean(serviceMap, returnServiceVO));
			}
			result.setData(listServiceVO);
			result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} catch (Exception e) {
			logger.error("查询异常 {}", e);
			result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result serviceInfoCreate(ServiceInfoVO serviceInfoVO) {
		Result result = new Result<>(StatusCodes.OK, true);
		Double goodsSum = 0.0;
		serviceInfoVO.setId(-1);
		ServiceInfo serviceInfo = new ServiceInfo();
		List<ServiceInfo> listService = new ArrayList<ServiceInfo>();
		try {
			listService = serviceInfoMapper.selectServiceByName(serviceInfoVO);
			if (listService.size() > 0) {
				result.setSuccessful(false);
				result.setResultCode(new ResultCode("NOT_UNIQUE", "已有相同服务名称数据，请修改服务名"));
				return result;
			}
			serviceInfo.setCreateTime(new Date());
			serviceInfo.setStatus(0); // 初始状态
			serviceInfo.setServiceShare(0);// 服务共享 初始状态20171031
			serviceInfo.setDescription(serviceInfoVO.getDescription());
			serviceInfo.setCreateUserId(serviceInfoVO.getCreateUserId());
			serviceInfo.setInUse(1);
			serviceInfo.setName(serviceInfoVO.getName());
			serviceInfo.setOpinion(serviceInfoVO.getOpinion());
			serviceInfo.setPrice(serviceInfoVO.getPrice());
			serviceInfo.setProcedureId(serviceInfoVO.getProcedureId());
			serviceInfo.setProcedureName(serviceInfoVO.getProcedureName());
			serviceInfo.setServiceAttribute(serviceInfoVO.getServiceAttribute());
			serviceInfo.setServiceShare(serviceInfoVO.getServiceShare());
			serviceInfo.setServiceType(serviceInfoVO.getServiceType());
			serviceInfo.setImgUrl(serviceInfoVO.getImgUrl());
			String goodsId = serviceInfoVO.getGoodsIdList();
			String keywordsIdList = serviceInfoVO.getKeywordsIdList();
			List<ServiceGoodsRel> listServiceRel = new ArrayList<ServiceGoodsRel>();
			List<KeywordsRel> listKeywordsRel = new ArrayList<KeywordsRel>();
			int num = serviceInfoMapper.insert(serviceInfo);
			if (!StringUtil.isEmpty(goodsId) && goodsId.split(",").length > 0) {
				String[] goodsArray = goodsId.split(",");
				for (int i = 0; i < goodsArray.length; i++) {
					ServiceGoodsRel sgr = new ServiceGoodsRel();
					sgr.setServiceId(serviceInfo.getId());
					sgr.setGoodsId(Integer.parseInt(goodsArray[i]));
					goodsSum = goodsSum
							+ goodsInfoMapper.selectByPrimaryKey(Integer.parseInt(goodsArray[i])).getPrice();
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

	@SuppressWarnings("rawtypes")
	@Override
	public Result serviceInfoUpdate(ServiceInfoVO service) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			List<ServiceInfo> listService = new ArrayList<ServiceInfo>();
			ServiceInfo serviceInfo = serviceInfoMapper.selectByPrimaryKey(service.getId());
			// 初始 下架 审核不通过的 才能修改
			if (serviceInfo.getStatus() != 0 && serviceInfo.getStatus() != 3 && serviceInfo.getStatus() != 4) {
				result = new Result(StatusCodes.OK, false, ServiceStatusFail);
				return result;
			}
			listService = serviceInfoMapper.selectServiceByName(service);
			if (listService.size() > 0) {
				result.setSuccessful(false);
				result.setResultCode(new ResultCode("NOT_UNIQUE", "已有相同服务名称数据，请修改服务名"));
				return result;
			}
			service.setProcedureName(serviceInfo.getProcedureName());
			service.setProcedureId(serviceInfo.getProcedureId());
			service.setCreateTime(serviceInfo.getCreateTime());
			service.setUpdateTime(new Date());
			service.setStatus(serviceInfo.getStatus());
			
			// result = serviceInfoRemove(String.valueOf(service.getId()));
			// 删除服务商品关系
			result = serviceGoodsReldelete(String.valueOf(service.getId()));
			// 删除服务标签
			if (result.isSuccessful()) {// 1 普通标签
				keywordsRelMapper.deleteByRelationId(service.getId(), 1);
			}
			Double goodsSum = 0.0;
			String goodsId = service.getGoodsIdList();
			String keywordsIdList = service.getKeywordsIdList();
			List<ServiceGoodsRel> listServiceRel = new ArrayList<ServiceGoodsRel>();
			List<KeywordsRel> listKeywordsRel = new ArrayList<KeywordsRel>();
			if (!StringUtil.isEmpty(goodsId) && goodsId.split(",").length > 0) {
				String[] goodsArray = goodsId.split(",");
				for (int j = 0; j < goodsArray.length; j++) {
					ServiceGoodsRel sgr = new ServiceGoodsRel();
					sgr.setServiceId(serviceInfo.getId());
					sgr.setGoodsId(Integer.parseInt(goodsArray[j]));
					goodsSum = goodsSum
							+ goodsInfoMapper.selectByPrimaryKey(Integer.parseInt(goodsArray[j])).getPrice();
					listServiceRel.add(sgr);
				}
			}
			if (service.getPrice() < goodsSum) {
				result.setSuccessful(false);
				result.setResultCode(new ResultCode("FALSE", "服务价格不能小于商品价格之和！"));
				return result;
			}
			if (!StringUtil.isEmpty(keywordsIdList) && keywordsIdList.split(",").length > 0) {
				String[] keywordsArray = keywordsIdList.split(",");
				for (int k = 0; k < keywordsArray.length; k++) {
					KeywordsRel kr = new KeywordsRel();
					kr.setRelationId(serviceInfo.getId());
					kr.setKeywordsId(Integer.parseInt(keywordsArray[k]));
					kr.setType(1);
					listKeywordsRel.add(kr);
				}
			}
			if (listKeywordsRel.size() > 0) {
				keywordsRelMapper.insertList(listKeywordsRel);
			}
			if (listServiceRel.size() > 0) {
				result = serviceGoodsRelCreate(listServiceRel);
			}
			if (result.isSuccessful()) {
				serviceInfoMapper.updateByPrimaryKeySelective(service);
				result.setResultCode(new ResultCode("SUCCESS", "服务信息修改成功！"));
			} else {
				result.setResultCode(new ResultCode("FALSE", "服务信息修改失败！"));
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	/**
	 * 删除服务信息
	 */
	@SuppressWarnings("rawtypes")
	@Override
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
				// 删除服务商品关系
				// result = serviceGoodsReldelete(id);
				// 删除服务标签
				// if (result.isSuccessful()) {
				// keywordsRelMapper.deleteByRelationId(Integer.parseInt(id));
				// }
			} else {
				result = new Result(StatusCodes.OK, false, ServiceDeleteFail);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result serviceInfoExamine(String id, String status, String opinion) {
		Result result = new Result<>(StatusCodes.OK, true);
		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("id", id);
		updateMap.put("status", status);
		updateMap.put("updateTime", new Date());
		if (null != opinion) {
			updateMap.put("opinion", opinion);
		}

		// 0：初始，1：待审核，2：上架，3，下架，4，审核不通过
		try {
			int i = serviceInfoMapper.updateStatus(updateMap);
			if (i > 0) {
				String message = "操作成功!";
				if (status.equals("1")) {
					message = "服务进入待审核状态！";
				} else if (status.equals("2")) {
					message = "服务审核通过，上架成功！";
				} else if (status.equals("3")) {
					message = "服务下架成功！";
				}
				result.setResultCode(new ResultCode("SUCCESS", message));
			} else {
				result = new Result(StatusCodes.OK, false, ServiceUpdateFail);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result keyUpdate(String id, String keywordsIdList) {
		Result result = new Result<>(StatusCodes.OK, true);
		if (!StringUtil.isEmpty(keywordsIdList) && keywordsIdList.split(",").length > 0) {
			keywordsRelMapper.deleteByRelationId(Integer.parseInt(id), 1);
			List<KeywordsRel> listKeywordsRel = new ArrayList<KeywordsRel>();
			String[] keywordsArray = keywordsIdList.split(",");
			for (int i = 0; i < keywordsArray.length; i++) {
				KeywordsRel kr = new KeywordsRel();
				kr.setRelationId(Integer.parseInt(id));
				kr.setKeywordsId(Integer.parseInt(keywordsArray[i]));
				kr.setType(1);
				Keywords key = keywordsMapper.selectByPrimaryKey(Integer.parseInt(keywordsArray[i]));
				kr.setKeyType(key.getKeyType());
				listKeywordsRel.add(kr);
			}
			if (listKeywordsRel.size() > 0) {
				keywordsRelMapper.insertList(listKeywordsRel);
			}
		}
		result.setResultCode(new ResultCode("SUCCESS", "修改成功"));
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

	/**
	 * 删除服务商品关系
	 * 
	 * @param listServiceRel
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Result serviceGoodsReldelete(String serviceId) {
		int refNum = serviceGoodsRelMapper.deleteByServiceId(Integer.parseInt(serviceId));
		Result result = new Result<>(StatusCodes.OK, true);
		if (refNum != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "删除服务商品关系成功！"));
		} else {
			result.setResultCode(new ResultCode("FALSE", "删除服务商品关系失败！"));
		}
		return result;
	}

	/**
	 * 查询用户下所有上架服务
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Result queryAllServiceInfo(String createUserId) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<ServiceInfo> listServiceInfos = new ArrayList<ServiceInfo>();
		ServiceInfoVO serviceInfo = new ServiceInfoVO();
		// serviceInfo.setCreateUserId(createUserId);
		serviceInfo.setStatus(2);// 0：初始，1：待审核，2：上架，3，下架，4，审核不通过
		listServiceInfos = serviceInfoMapper.selectByUserIdAndStatus(serviceInfo);
		result.setResultCode(new ResultCode("SUCCESS", "查询服务信息成功！"));
		result.setData(listServiceInfos);
		return result;
	}

	/**
	 * 根据服务属性 查询服务
	 */
	public List<ServiceInfo> selectByParameter(ServiceInfoVO serviceInfo) {
		List<ServiceInfo> listService = new ArrayList<ServiceInfo>();
		listService = serviceInfoMapper.selectByParameter(serviceInfo);
		return listService;
	}

	// 20171031
	@SuppressWarnings("rawtypes")
	@Override
	public Result shareServiceInfo(String id, String serviceShare) {
		Result result = new Result<>(StatusCodes.OK, true);
		Map<String, Object> updateMap = new HashMap<String, Object>();
		ServiceInfo service = serviceInfoMapper.selectByPrimaryKey(Integer.parseInt(id));
		if (serviceShare.equals("1")) { // 上架并公开购买的 才能共享
			// 共享状态 0：初始状态 1：共享 2：取消共享
			// 上架状态 0：初始，1：待审核，2：上架，3，下架，4，审核不通过
			// 服务属性 1：公开购买 2：私人推荐
			if (service.getStatus() == 2 && service.getServiceAttribute() == 1) {
				updateMap.put("serviceShare", 1);
			} else {
				result.setSuccessful(false);
				result.setResultCode(new ResultCode("FAIL", "当前服务不符合共享条件：上架且能公开购买"));
				return result;
			}
		} else if (serviceShare.equals("2")) {
			updateMap.put("serviceShare", 2);
		}
		updateMap.put("id", id);
		updateMap.put("updateTime", new Date());
		try {
			int i = serviceInfoMapper.updateStatus(updateMap);
			if (i > 0) {
				result.setResultCode(new ResultCode("SUCCESS", "操作成功"));
			} else {
				result = new Result(StatusCodes.OK, false, ServiceUpdateFail);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result getAllotServices(ServiceInfoVO serviceVO) {
		List<ServiceInfoVO> listService = new ArrayList<ServiceInfoVO>();
		PaginatedResult<ServiceInfoVO> pa = null;
		try {
			JyOrgDocRel jyorg = new JyOrgDocRel();
			jyorg.setDoctorId(serviceVO.getCreateUserId());
			List<JyOrgDocRel> jys = jyOrgDocMapper.selectJyOrgDocRel(jyorg);
			if (jys != null && jys.size() > 0) {
				listService = serviceInfoMapper.selectAllListPage(serviceVO);
			} else {
				listService = serviceInfoMapper.selectByConditionListPage(serviceVO);
			}
			for (ServiceInfoVO service : listService) {
				String keywordsName = "";
				// 服务关联的所有标签name信息
				// List<Map<String, Object>> listKeywords = keywordsRelMapper
				// .selectByRelationId(Integer.parseInt(service.getId().toString()),
				// 1);
				// 服务关联的所有商品信息
				List<GoodsInfo> goodsInfoList = serviceGoodsRelMapper
						.selectGoodsById(Integer.parseInt(service.getId().toString()));
				// 服务关联的所有标签信息
				List<Keywords> keywordsList = keywordsRelMapper
						.selectListByRelationId(Integer.parseInt(service.getId().toString()), 1);
				service.setGoodsInfoList(goodsInfoList);
				service.setKeywordsList(keywordsList);
				if (null != keywordsList && keywordsList.size() > 0) {
					for (Keywords keywords : keywordsList) {
						keywordsName = keywordsName + "," + keywords.getName();
					}
					service.setKeywordsName(keywordsName.substring(1, keywordsName.length()));
				}
			}
			pa = new PaginatedResult<ServiceInfoVO>(listService, serviceVO.getCurrentPage(), serviceVO.getShowCount(),
					serviceVO.getTotalResult());
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

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result getAllShareServices(ServiceInfoVO serviceVO) {
		List<ServiceInfoVO> listService = new ArrayList<ServiceInfoVO>();
		List<UserShareService> listShareService = new ArrayList<UserShareService>();
		PaginatedResult<UserShareService> pa = null;
		try {
			listService = serviceInfoMapper.selectByConditionListPage(serviceVO);
			for (ServiceInfoVO service : listService) {
				UserShareService usService = new UserShareService();
				usService.setDescription(String.valueOf(service.getDescription()));
				usService.setId(service.getId());
				usService.setServiceName(String.valueOf(service.getName()));
				usService.setShareId(String.valueOf(service.getCreateUserId()));
				usService.setShareName(String.valueOf(service.getCreateName()));
				listShareService.add(usService);
			}
			pa = new PaginatedResult<UserShareService>(listShareService, serviceVO.getCurrentPage(),
					serviceVO.getShowCount(), serviceVO.getTotalResult());
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

}
