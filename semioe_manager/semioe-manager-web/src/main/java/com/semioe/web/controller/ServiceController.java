package com.semioe.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.GoodsInfo;
import com.semioe.api.entity.ServiceInfo;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.service.GoodsInfoService;
import com.semioe.dubbo.service.KeywordsService;
import com.semioe.dubbo.service.ServiceService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/service")
public class ServiceController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

	@Reference
	private ServiceService serviceService;
	@Reference
	private KeywordsService keywordsService;
	@Reference
	private GoodsInfoService goodsInfoService;

	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/query")
	@ResponseBody
	public Result getServiceInfoByType(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "createName", required = false) String createName,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "serviceShare", required = false) String serviceShare,
			@RequestParam(value = "serviceAttribute", required = false) String serviceAttribute,
			@RequestParam(value = "serviceType", required = false) String serviceType) {
		ServiceInfoVO serviceVO = new ServiceInfoVO();
		if (name != null && !name.isEmpty()) {
			serviceVO.setName(name + "%");
		}
		if (id != null && !id.isEmpty()) {
			serviceVO.setCreateUserId(id);
		}
		if (serviceShare != null && !serviceShare.isEmpty()) {
			serviceVO.setServiceShare(Integer.parseInt(serviceShare));
		}
		if (serviceAttribute != null && !serviceAttribute.isEmpty()) {
			serviceVO.setServiceAttribute(Integer.parseInt(serviceAttribute));
		}
		if (serviceType != null && !serviceType.isEmpty()
				&& !String.valueOf(serviceType).equals("0")
				&& !String.valueOf(serviceType).equals("all")) {
			serviceVO.setServiceType(Integer.parseInt(serviceType));
		}
		if (status != null && !status.isEmpty()) {
			List<Integer> listStatus = new ArrayList<Integer>();
			for (String s : status.split(",")) {
				listStatus.add(Integer.parseInt(s));
			}
			serviceVO.setStatusList(listStatus);
		}
		if (createName != null && !createName.isEmpty()) {
			serviceVO.setCreateName(createName + "%");
		}
		if (pageSize != null && !pageSize.isEmpty()) {
			int currentPage = Integer.parseInt(pageNumber);
			if (currentPage <= 0) {
				currentPage = 1;
			}
			int currentResult = (currentPage - 1) * Integer.parseInt(pageSize);

			serviceVO.setShowCount(Integer.parseInt(pageSize));
			serviceVO.setCurrentResult(currentResult);
			serviceVO.setCurrentPage(currentPage);
		} else {
			serviceVO.setCurrentPage(-1);
		}
		serviceVO.setInUse(1);
		Result result = serviceService.getServiceInfoListPage(serviceVO);
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/queryGoodsById")
	@ResponseBody
	public Result queryGoodsById(@RequestParam(value = "id", required = false) String id) {
		Result result = serviceService.queryGoodsById(id);
		return result;
	}

	/**
	 * 新增服务
	 * 
	 * @param serviceInfoVO
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/add")
	@ResponseBody
	public Result insertServiceInfo(@RequestBody ServiceInfoVO serviceInfoVO) {

		if (StringUtils.isEmpty(serviceInfoVO.getProcedureId())) {
			return new Result(StatusCodes.OK, false, new ResultCode("PARAM_ERROR", "参数错误！工作流不能为空"));
		}

		if (StringUtils.isEmpty(serviceInfoVO.getName())) {
			return new Result(StatusCodes.OK, false,
					new ResultCode("PARAM_ERROR", "参数错误！服务名称不能为空"));
		}
		return serviceService.serviceInfoCreate(serviceInfoVO);
	}

	/**
	 * 修改服务
	 * 
	 * @param serviceInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/update")
	@ResponseBody
	public Result updateServiceInfo(@RequestBody ServiceInfoVO serviceInfo) {
		return serviceService.serviceInfoUpdate(serviceInfo);
	}

	/**
	 * 删除服务
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/remove")
	@ResponseBody
	public Result deleteServiceInfo(@RequestParam("id") String id) {
		return serviceService.serviceInfoRemove(id);
	}

	/**
	 * 审核服务 上架 下架 通过 退回
	 * 
	 * @param id
	 * @param status
	 * @param opinion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/examine")
	@ResponseBody
	public Result examineServiceInfo(@RequestParam("id") String id,
			@RequestParam("status") String status,
			@RequestParam(value = "opinion", required = false) String opinion) {
		return serviceService.serviceInfoExamine(id, status, opinion);
	}
	
	/**
	 * 标签修改 
	 * 
	 * @param id
	 * @param status
	 * @param opinion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/keyUpdate")
	@ResponseBody
	public Result keyUpdate(@RequestParam("id") String id,
			@RequestParam(value = "keywordsIdList", required = false) String keywordsIdList) {
		return serviceService.keyUpdate(id,keywordsIdList);
	}

	/**
	 * 查看流程是否绑定服务
	 * 
	 * @param id
	 * @param status
	 * @param opinion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/checkProcedureService")
	@ResponseBody
	public Result checkProcedureService(@RequestParam("procedureId") String procedureId) {

		logger.info("查看流程是否绑定服务，入参 procedureId=" + procedureId);

		// 返回对象
		Result<Integer> result = new Result<>(StatusCodes.OK, true);
		// 根据流程id查询
		ServiceInfoVO query = new ServiceInfoVO();
		query.setProcedureId(procedureId);
		List<ServiceInfo> serviceList = serviceService.selectByParameter(query);

		if (serviceList != null && serviceList.size() > 0) {
			result.setData(1);
			result.setResultCode(new ResultCode("SUCCESS", "查询到绑定服务"));
			logger.info("查询到绑定服务，共 " + serviceList.size() + " 条数据");
		} else {
			result.setData(0);
			result.setResultCode(new ResultCode("FALSE", "未查询到相关服务"));
			logger.info("未查询到相关服务");
		}

		return result;
	}

	/**
	 * 查询用户下所有服务
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryAll")
	@ResponseBody
	public Result queryAllServiceInfo(@RequestParam("createUserId") String createUserId) {
		return serviceService.queryAllServiceInfo(createUserId);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/findServiceByKeywords")
	@ResponseBody
	public Result findServiceByKeywords(
			@RequestParam(value = "currentPage", required = false) String currentPage,
			@RequestParam(value = "showCount", required = false) String showCount,
			@RequestParam(value = "ids", required = false) String ids,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "keywordsId", required = false) String keywordsId,
			@RequestParam(value = "keywordsName", required = false) String keywordsName,
			@RequestParam(value = "type", required = true) String type) {

		logger.info("根据关键字查询，参数[type=" + type + " currentPage=" + currentPage + " showCount="
				+ showCount + " keywordsId=" + keywordsId + " keywordsName=" + keywordsName
				+ " name=" + name + " ids=" + ids + "]");

		Result result = new Result<>(false);
		switch (type) {
		case "0": // 商品
			GoodsInfo goodsInfo = new GoodsInfo();
			goodsInfo.setType(1); // 查询上架的数据
			if (!StringUtil.isEmpty(name)) {
				goodsInfo.setGoodsName(name);
			}
			if (!StringUtil.isEmpty(currentPage)) {
				goodsInfo.setCurrentPage(Integer.parseInt(currentPage));
			}
			if (!StringUtil.isEmpty(showCount)) {
				goodsInfo.setShowCount(Integer.parseInt(showCount));
			}
			if (!StringUtil.isEmpty(keywordsId)) {
				goodsInfo.setKeywordsIds(keywordsId);
			}
			if (!StringUtil.isEmpty(keywordsName)) {
				// 判断关键字是否为多个的情况
				int i = keywordsName.indexOf(";");
				if (i >= 0) {
					String[] names = keywordsName.split(";");
					List<String> list = java.util.Arrays.asList(names);
					goodsInfo.setKeywordsNameList(list);
				} else {
					goodsInfo.setKeywordsNames(keywordsName);
				}
			}
			// id查询
			if (!StringUtil.isEmpty(ids)) {
				int i = ids.indexOf(",");
				if (i >= 0) {
					String[] idsArr = ids.split(",");
					List<Integer> idsList = new ArrayList<>();
					for (String id : idsArr) {
						idsList.add(Integer.parseInt(id));
					}
					goodsInfo.setIdList(idsList);
				} else {
					goodsInfo.setId(Integer.parseInt(ids));
				}
			}
			result = goodsInfoService.getGoodsInfoByKeywords(goodsInfo);
			break;
		case "1": // 服务
			// 查询服务
			ServiceInfoVO serviceVO = new ServiceInfoVO();
			if (!StringUtil.isEmpty(name)) {
				serviceVO.setName(name);
			}
			if (!StringUtil.isEmpty(currentPage)) {
				serviceVO.setCurrentPage(Integer.parseInt(currentPage));
			}
			if (!StringUtil.isEmpty(showCount)) {
				serviceVO.setShowCount(Integer.parseInt(showCount));
			}
			if (!StringUtil.isEmpty(keywordsId)) {
				serviceVO.setKeywordsIdList(keywordsId);
			}
			if (!StringUtil.isEmpty(keywordsName)) {
				// 判断关键字是否为多个的情况
				int i = keywordsName.indexOf(";");
				if (i >= 0) {
					String[] names = keywordsName.split(";");
					List<String> list = java.util.Arrays.asList(names);
					serviceVO.setKeywordsNameList(list);
				} else {
					serviceVO.setKeywordsName(keywordsName);
				}
			}
			// id查询
			if (!StringUtil.isEmpty(ids)) {
				int i = ids.indexOf(",");
				if (i >= 0) {
					String[] idsArr = ids.split(",");
					List<Integer> idsList = new ArrayList<>();
					for (String id : idsArr) {
						idsList.add(Integer.parseInt(id));
					}
					serviceVO.setIdList(idsList);
				} else {
					serviceVO.setId(Integer.parseInt(ids));
				}
			}
			result = serviceService.getServiceInfoVoByKeywords(serviceVO);
			break;
		}

		return result;
	}

	/**
	 * 共享 取消共享 20171031
	 * 
	 * @param id
	 * @param status
	 * @param opinion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/share")
	@ResponseBody
	public Result shareServiceInfo(@RequestParam("id") String id,
			@RequestParam("serviceShare") String serviceShare) {
		return serviceService.shareServiceInfo(id, serviceShare);
	}

	/**
	 * 查询当前医生可分配服务 20171031
	 * 
	 * 分配的服务包括：医生自创服务、所属机构选择的共享服务、所属机构自创的服务
	 * 
	 * @param id
	 * @param status
	 * @param opinion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getAllotServices")
	@ResponseBody
	public Result getAllotServices(@RequestBody ServiceInfoVO serviceInfo) {
		int currentResult = (serviceInfo.getCurrentPage() - 1) * serviceInfo.getShowCount();
		serviceInfo.setCurrentResult(currentResult);
		if (serviceInfo.getName() != null && !serviceInfo.getName().isEmpty()) {
			serviceInfo.setName(serviceInfo.getName() + "%");
		}
		return serviceService.getAllotServices(serviceInfo);
	}

	/**
	 * 查询所有共享服务 20171031
	 * 
	 * @param id
	 * @param status
	 * @param opinion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getAllShareServices")
	@ResponseBody
	public Result getAllShareServices(@RequestBody ServiceInfoVO serviceInfo) {
		int currentResult = (serviceInfo.getCurrentPage() - 1) * serviceInfo.getShowCount();
		serviceInfo.setCurrentResult(currentResult);
		if (serviceInfo.getCreateName() != null && !serviceInfo.getCreateName().isEmpty()) {
			serviceInfo.setCreateName(serviceInfo.getCreateName() + "%");
		}
		if (serviceInfo.getName() != null && !serviceInfo.getName().isEmpty()) {
			serviceInfo.setName(serviceInfo.getName() + "%");
		}
		return serviceService.getAllShareServices(serviceInfo);
	}

}