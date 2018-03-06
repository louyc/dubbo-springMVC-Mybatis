package com.semioe.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.ImplementSetDetail;
import com.semioe.api.vo.ImplementSetDetailVO;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.service.ImplementSetDetailService;
import com.semioe.dubbo.service.ServiceService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/implementDetail")
public class ImplementSetDetailController {

	private static final Logger logger = LoggerFactory
			.getLogger(ImplementSetDetailController.class);

	@Reference
	private ImplementSetDetailService implementDetailService;
	@Reference
	ServiceService serviceService;

	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryById")
	@ResponseBody
	public Result queryById(
			@RequestParam(value = "implementId", required = false) String implementId) {
		return implementDetailService.getImplementDetail(implementId);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryByEntity")
	@ResponseBody
	public Result queryByEntity(@RequestBody ImplementSetDetailVO implementSet) {
		return implementDetailService.getImplementDetail(implementSet);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryServiceAndImplement")
	@ResponseBody
	public Result queryServiceAndImplement(HttpServletResponse response,
			ImplementSetDetailVO implementSet) {
		ServiceInfoVO serviceVO = new ServiceInfoVO();
		if (null != implementSet.getServiceType() && implementSet.getServiceType() > 0) {
			serviceVO.setServiceType(implementSet.getServiceType());
		}
		if (implementSet.getShowCount() < 1) {
			implementSet.setShowCount(1000);
		}
		if (!StringUtil.isEmpty(implementSet.getTitle())) {
			implementSet.setTitle(implementSet.getTitle() + "%");
			serviceVO.setName(implementSet.getTitle() + "%");
		}
		serviceVO.setCurrentPage(implementSet.getCurrentPage());
		serviceVO.setShowCount(implementSet.getShowCount());
		serviceVO.setInUse(1);
		List<Integer> listStatus = new ArrayList<Integer>();
		listStatus.add(2);
		serviceVO.setStatusList(listStatus);
		serviceVO.setServiceAttribute(1);
		Result result = new Result<>(StatusCodes.OK, true);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		Map<String, Object> map = new HashMap<String, Object>();
		PaginatedResult<ImplementSetDetailVO> resultImplement = (PaginatedResult<ImplementSetDetailVO>) implementDetailService
				.queryServiceAndImplement(implementSet);
		PaginatedResult<ServiceInfoVO> resultService = (PaginatedResult<ServiceInfoVO>) serviceService
				.getServiceInfoListPage(serviceVO);
		if (null != resultImplement.getItems()) {
			map.put("implement", resultImplement.getItems());
		} else {
			map.put("implement", "");
		}
		if (null != resultImplement.getItems()) {
			map.put("service", resultService.getItems());
		} else {
			map.put("service", "");
		}
		map.put("totalItemsCount", resultService.getTotalItemsCount());
		map.put("pageNumber", resultService.getPageNumber());
		map.put("pagesCount", resultService.getPagesCount());
		result.setData(map);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/addDetail")
	@ResponseBody
	public Result addDetail(@RequestBody ImplementSetDetail implementSet) {
		return implementDetailService.implementDetailCreate(implementSet);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateDetail")
	@ResponseBody
	public Result updateDetail(@RequestBody ImplementSetDetail implementSet) {
		return implementDetailService.implementDetailUpdate(implementSet);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/removeDetail")
	@ResponseBody
	public Result removeDetail(@RequestParam("id") String id) {
		return implementDetailService.implementDetailRemove(id);
	}

}