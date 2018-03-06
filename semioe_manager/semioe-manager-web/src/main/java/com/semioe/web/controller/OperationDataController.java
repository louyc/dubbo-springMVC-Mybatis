package com.semioe.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.vo.ApiUserInfoVO;
import com.semioe.api.vo.QrcodeInfoVO;
import com.semioe.api.vo.UserCountVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.dubbo.service.OperationDataService;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Controller
@RequestMapping("/oprationDate")
public class OperationDataController {

	private static final Logger logger = LoggerFactory.getLogger(OperationDataController.class);

	@Reference
	private OperationDataService operationService;

	/* 注册统计 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/regist")
	@ResponseBody
	public PaginatedResult registCount(@RequestBody ApiUserInfoVO userInfo) {
		logger.info("OperationDataController.registCount start, userInfo = {}",
				userInfo.toString());
		if (userInfo.getCurrentPage() < 1) {
			userInfo.setCurrentPage(1);
		}
		int currentResult = (userInfo.getCurrentPage() - 1) * userInfo.getShowCount();

		userInfo.setCurrentResult(currentResult);
		return operationService.countRegist(userInfo);
	}

	/* 注册查看明细 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/detaile")
	@ResponseBody
	public PaginatedResult registDetaile(@RequestBody ApiUserInfoVO userInfo) {
		logger.info("OperationDataController.registCount start, userInfo = {}",
				userInfo.toString());
		if (userInfo.getCurrentPage() < 1) {
			userInfo.setCurrentPage(1);
		}
		int currentResult = (userInfo.getCurrentPage() - 1) * userInfo.getShowCount();

		userInfo.setCurrentResult(currentResult);
		return operationService.registDetaile(userInfo);
	}

	/* 用户分析 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/userAnalysis")
	@ResponseBody
	public Result userAnalysis(@RequestBody ApiUserInfoVO userInfo) {
		logger.info("OperationDataController.userAnalysis start, userInfo = {}",
				userInfo.toString());
		if (userInfo.getCurrentPage() < 1) {
			userInfo.setCurrentPage(1);
		}
		return operationService.userAnalysis(userInfo);
	}

	/* 查询省份 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryProvince")
	@ResponseBody
	public Result queryProvince(@RequestParam("id") String id) {
		logger.info("OperationDataController.queryProvince start, id = {}", id);
		return operationService.queryProvince(id);
	}

	/* 转化率统计 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/conversion")
	@ResponseBody
	public PaginatedResult conversionCount(@RequestBody ApiUserInfoVO userInfo) {
		logger.info("OperationDataController.countOrderInfo start, userInfo = {}",
				userInfo.toString());
		if (userInfo.getCurrentPage() < 1) {
			userInfo.setCurrentPage(1);
		}
		return operationService.countconversion(userInfo);
	}

	/* 推广码统计 */
	@RequestMapping("/qrcode")
	@ResponseBody
	public PaginatedResult<QrcodeInfoVO> qrcodeCount(@RequestBody QrcodeInfoVO orCodeInfo) {
		logger.info("OperationDataController.qrcodeCount start, userInfo = {}",
				orCodeInfo.toString());
		if (orCodeInfo.getCurrentPage() < 1) {
			orCodeInfo.setCurrentPage(1);
		}
		return operationService.countQrcode(orCodeInfo);
	}

	/* 推广码统计导出 */
	@RequestMapping("/export")
	public void exportQrcodeCount(HttpServletResponse response,
			@RequestParam(value = "showCount", required = false) String showCount,
			@RequestParam(value = "currentPage", required = false) String currentPage,
			@RequestParam(value = "registStartTime", required = false) String registStartTime,
			@RequestParam(value = "registEndTime", required = false) String registEndTime,
			@RequestParam(value = "payStartTime", required = false) String payStartTime,
			@RequestParam(value = "payEndTime", required = false) String payEndTime,
			@RequestParam(value = "doctorName", required = false) String doctorName,
			@RequestParam(value = "promotionName", required = false) String promotionName) {
		logger.info("OperationDataController.exportQrcodeCount start, exportQrcodeCount = {}");
		QrcodeInfoVO qrCode = new QrcodeInfoVO();
		if (!StringUtil.isEmpty(showCount)) {
			qrCode.setShowCount(Integer.parseInt(showCount));
		}
		if (!StringUtil.isEmpty(currentPage)) {
			qrCode.setCurrentPage(Integer.parseInt(currentPage));
		}
		if (!StringUtil.isEmpty(registStartTime)) {
			qrCode.setRegistStartTime(registStartTime);
		}
		if (!StringUtil.isEmpty(registEndTime)) {
			qrCode.setRegistEndTime(registEndTime);
		}
		if (!StringUtil.isEmpty(payStartTime)) {
			qrCode.setPayStartTime(payStartTime);
		}
		if (!StringUtil.isEmpty(payEndTime)) {
			qrCode.setPayEndTime(payEndTime);
		}
		if (!StringUtil.isEmpty(doctorName)) {
			qrCode.setDoctorName(doctorName);
		}
		if (!StringUtil.isEmpty(promotionName)) {
			qrCode.setPromotionName(promotionName);
		}
		PaginatedResult<QrcodeInfoVO> pa = operationService.countQrcode(qrCode);
		List<QrcodeInfoVO> itemList = pa.getItems();

		CellView navCellView = new CellView();
		navCellView.setAutosize(true); // 设置自动大小
		// 文件名
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "Qrcode_" + sdf.format(new Date()) + ".xls";
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名

		try {
			// 1.创建excel文件
			WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
			// 居中样式
			WritableCellFormat wf = new WritableCellFormat();
			wf.setAlignment(Alignment.CENTRE);

			WritableSheet sheet = book.createSheet("推广码统计", 0);
			// sheet.setColumnView(0, navCellView); // 设置col显示样式
			// 2.添加表头数据
			sheet.addCell(new Label(0, 0, "注册用户", wf));
			sheet.addCell(new Label(1, 0, "推广人", wf));
			sheet.addCell(new Label(2, 0, "注册时间", wf));
			sheet.addCell(new Label(3, 0, "订单号", wf));
			sheet.addCell(new Label(4, 0, "购买时间", wf));
			sheet.addCell(new Label(5, 0, "所购服务或商品", wf));
			sheet.addCell(new Label(6, 0, "订单金额", wf));
			Double sumPrice = 0.0;
			for (int i = 0; i < itemList.size(); i++) {
				sheet.addCell(new Label(0, i + 1, itemList.get(i).getUserId() + "", wf));
				sheet.addCell(new Label(1, i + 1, itemList.get(i).getPromotionName() + "", wf));
				sheet.addCell(new Label(2, i + 1, itemList.get(i).getLoginTime() + "", wf));
				sumPrice = sumPrice + Double.valueOf(itemList.get(i).getOrderPrice());
				sheet.addCell(new Label(3, i + 1, itemList.get(i).getOrderCode() + "", wf));
				sheet.addCell(new Label(4, i + 1, itemList.get(i).getPayTime() + "", wf));
				sheet.addCell(new Label(5, i + 1, itemList.get(i).getOrderName() + "", wf));
				sheet.addCell(new Label(6, i + 1, itemList.get(i).getOrderPrice() + "", wf));
			}
			sheet.addCell(new Label(0, itemList.size() + 1, "合计", wf));
			sheet.addCell(new Label(1, itemList.size() + 1, itemList.size() + "", wf));
			sheet.addCell(new Label(6, itemList.size() + 1, sumPrice + "", wf));
			// 4.写入excel并关闭
			book.write();
			book.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 签约统计
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/sign")
	@ResponseBody
	public PaginatedResult signCount(@RequestBody ApiUserInfoVO userInfo) {
		logger.info("OperationDataController.signCount start, userInfo = {}",
				userInfo.toString());
		if (userInfo.getCurrentPage() < 1) {
			userInfo.setCurrentPage(1);
		}
		return operationService.signCount(userInfo);
	}
	
	/**
	 * 签约统计 查看明细
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/signDetaile")
	@ResponseBody
	public Result signDetaile(@RequestBody ApiUserInfoVO userInfo) {
		logger.info("OperationDataController.signDetaile start, userInfo = {}",
				userInfo.toString());
		if (userInfo.getCurrentPage() < 1) {
			userInfo.setCurrentPage(1);
		}
		return operationService.signDetaile(userInfo);
	}
	
	/**
	 * 签约统计  分析
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/signAnalysis")
	@ResponseBody
	public Result signAnalysis(@RequestBody ApiUserInfoVO userInfo) {
		logger.info("OperationDataController.signDetaile start, userInfo = {}",
				userInfo.toString());
		if (userInfo.getCurrentPage() < 1) {
			userInfo.setCurrentPage(1);
		}
		if(StringUtil.isEmpty(userInfo.getBuildType())) {
			return new Result(0, false, new ResultCode("FAIL", "签约类型不能为空"));
		}
		return operationService.signAnalysis(userInfo);
	}
	/**
	 * 签约统计  家庭组 明细
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/familyDetail")
	@ResponseBody
	public Result familyDetail(@RequestBody UserCountVO userInfo) {
		logger.info("OperationDataController.familyDetail start, userInfo = {}",
				userInfo.toString());
		return operationService.familyDetail(userInfo);
	}
	/**
	 * 家庭组 统计
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/familyAnalysis")
	@ResponseBody
	public Result familyAnalysis(@RequestBody ApiUserInfoVO userInfo) {
		logger.info("OperationDataController.familyDetail start, userInfo = {}",
				userInfo.toString());
		return operationService.familyAnalysis(userInfo);
	}
	
}
