package com.semioe.web.controller.jayi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.service.JyServiceService;
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
@RequestMapping("/jyservice")
public class JyServiceController {

	private static final Logger logger = LoggerFactory.getLogger(JyServiceController.class);

	@Reference
	private JyServiceService jyServiceService;

	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/query")
	@ResponseBody
	public Result getServiceInfoByType(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "createName", required = false) String createName,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "id", required = false) String id) {
		ServiceInfoVO serviceVO = new ServiceInfoVO();
		if (name != null && !name.isEmpty()) {
			serviceVO.setName(name + "%");
		}
		if (id != null && !id.isEmpty()) {
			serviceVO.setCreateUserId(id);
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
		Result result = jyServiceService.getServiceInfoListPage(serviceVO);
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
		return jyServiceService.serviceInfoCreate(serviceInfoVO);
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
		logger.error("修改服务：：" + serviceInfo.getId());
		return jyServiceService.serviceInfoUpdate(serviceInfo);
	}

}