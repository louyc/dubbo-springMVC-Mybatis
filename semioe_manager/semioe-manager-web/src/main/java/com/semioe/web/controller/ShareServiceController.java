package com.semioe.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.vo.UserShareServiceVO;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.ShareServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/shareService")
public class ShareServiceController {

	private static final Logger logger = LoggerFactory.getLogger(ShareServiceController.class);

	@Reference
	private ShareServiceService shareServiceService;

	/**
	 * 创建机构用户共享服务 20171031
	 * 
	 * @param id
	 * @param status
	 * @param opinion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/addShareServices")
	@ResponseBody
	public Result addShareServices(@RequestBody UserShareServiceVO shareService) {
		logger.debug("机构选择共享服务:" + shareService.getIdList() + " 当前用户id：" + shareService.getOrgId());
		return shareServiceService.addShareServices(shareService);
	}

	/**
	 * 查询机构用户共享服务 20171031
	 * 
	 * @param id
	 * @param status
	 * @param opinion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getShareServices")
	@ResponseBody
	public Result getShareServices(@RequestBody UserShareServiceVO shareService) {
		int currentResult = (shareService.getCurrentPage() - 1) * shareService.getShowCount();
		shareService.setCurrentResult(currentResult);
		if (shareService.getShareName() != null && !shareService.getShareName().isEmpty()) {
			logger.debug("查询共享服务：姓名：" + shareService.getShareName());
			shareService.setShareName(shareService.getShareName() + "%");
		}
		if (shareService.getServiceName() != null && !shareService.getServiceName().isEmpty()) {
			logger.debug("查询共享服务：服务名称：" + shareService.getServiceName());
			shareService.setServiceName(shareService.getServiceName() + "%");
		}
		return shareServiceService.getShareServices(shareService);
	}

	/**
	 * 删除机构用户共享服务 20171031
	 * 
	 * @param id
	 * @param status
	 * @param opinion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/removeShareServices")
	@ResponseBody
	public Result removeShareServices(@RequestBody UserShareServiceVO shareService) {
		logger.debug("要删除的服务id：" + shareService.getId());
		return shareServiceService.removeShareServices(shareService);
	}

}