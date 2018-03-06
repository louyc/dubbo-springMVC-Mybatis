package com.semioe.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.ReportInfo;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.tools.RedisTool;
import com.semioe.dubbo.service.ProceduresService;
import com.semioe.dubbo.service.ReportInfoService;

@Controller
@RequestMapping("/procedures")
public class ProceduresController {

	private static final Logger logger = LoggerFactory.getLogger(ProceduresController.class);

	@Reference
	private ProceduresService proceduresService;

	@Reference
	private ReportInfoService reportInfoService; // 检测报告

	@Autowired
	private RedisTool redisTool;

	@RequestMapping("/getProceduresByToken")
	@ResponseBody
	public PaginatedResult getProceduresByToken(
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@RequestParam(value = "pageSize", required = false) String pageSize) {

		logger.info("根据token查询流程列表：{}", token);
		return proceduresService.getProceduresByToken(token, title, pageNumber, pageSize);
	}

	/**
	 * 调用流程
	 * 
	 * @param token
	 * @param userId
	 * @param doctorId
	 * @param orderId
	 * @param procedureId
	 * @return
	 */
	@RequestMapping("/getProcedureDialog")
	@ResponseBody
	public Result getProcedureDialog(@RequestParam("userId") String userId,
			@RequestParam("doctorId") String doctorId, @RequestParam("orderId") String orderId,
			@RequestParam("procedureId") String procedureId) {

		logger.info("调用流程列表：userId=" + userId + " doctorId=" + doctorId + " orderId=" + orderId
				+ " procedureId=" + procedureId);
		// 从REDIS中获取用户TOKEN
		String token = redisTool.get(userId);
		logger.info("获取token：" + token);
		if (token == null) {
			return new Result<>(500, false, new ResultCode("fell", "用户token获取失败！ 用户id=" + userId));
		}
		return proceduresService.getProcedureDialog(token, userId, doctorId, orderId, procedureId);
	}

	@RequestMapping("/addReportResult")
	@ResponseBody
	public Result addReportResult(@RequestParam("orderId") String orderId, @RequestParam("dialogId") String dialogId ,
			@RequestParam("reportResult") String reportResult) {

		logger.info("ProceduresController.addReportResult , orderId=" + orderId + " reportResult="
				+ reportResult);

		ReportInfo reportInfo = new ReportInfo();
		reportInfo.setOrderId(Integer.parseInt(orderId)); // 订单id
		reportInfo.setReportDesc(reportResult);           // 报告结论
		reportInfo.setDialogId(dialogId);                 // 流程会话ID
		return reportInfoService.addReportInfo(reportInfo);
	}

}