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
import com.semioe.api.dto.OrderDutiesDTO;
import com.semioe.api.dto.OrderStatisticDTO;
import com.semioe.api.vo.OrderCountVO;
import com.semioe.api.vo.OrderDutiesVO;
import com.semioe.api.vo.OrderPurchaseVO;
import com.semioe.api.vo.OrderStatisticVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.OrderStatisticService;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Controller
@RequestMapping("/orderStatistic")
public class OrderStatisticController {

	private static final Logger logger = LoggerFactory.getLogger(OrderStatisticController.class);
	
	@Reference
	private OrderStatisticService orderStatisticService;
	
	/**
	 * 获取订单统计数据列表
	 * @param entity
	 * @return
	 */
	@RequestMapping("/getOrderPurchaseArray")
	@ResponseBody
	public PaginatedResult<OrderPurchaseVO> getOrderPurchaseArray(@RequestBody OrderStatisticDTO entity){
		logger.info("OrderStatisticController.getOrderInfoArray start, data="+entity);
		// 开始结束时间处理
		if(entity != null && entity.getStartTime() != null){
			entity.getStartTime().setHours(0);
			entity.getStartTime().setMinutes(0);
			entity.getStartTime().setSeconds(0);
		}
		if(entity != null && entity.getEndTime() != null){
			entity.getEndTime().setHours(23);
			entity.getEndTime().setMinutes(59);
			entity.getEndTime().setSeconds(59);
		}
		// 查询结果
		return orderStatisticService.orderPurchaseCount(entity);
	}
	
	/**
	 * 订单统计详情数据查询
	 * @param entity
	 * @return
	 */
	@RequestMapping("/getPurchaseDetailArray")
	@ResponseBody
	public PaginatedResult<OrderStatisticVO> getPurchaseDetailArray(@RequestBody OrderStatisticDTO entity){
		
		logger.info("OrderStatisticController.getPurchaseDetailArray start, data="+entity);
		
		// TODO 可以做一些接受参数验证
		
		if(entity != null){
			entity.setRelationName(null);     // 名称模糊查询  清空
			entity.setRelationSupplier(null); // 供应商模糊查询 清空
		}
		// 开始结束时间处理
		if(entity != null && entity.getStartTime() != null){
			entity.getStartTime().setHours(0);
			entity.getStartTime().setMinutes(0);
			entity.getStartTime().setSeconds(0);
		}
		if(entity != null && entity.getEndTime() != null){
			entity.getEndTime().setHours(23);
			entity.getEndTime().setMinutes(59);
			entity.getEndTime().setSeconds(59);
		}
		// 查询结果
		return orderStatisticService.orderPurchaseDetail(entity);
	}
	
	/**
	 * 查询订单合计数据
	 * @param entity
	 * @return
	 */
	@RequestMapping("/getPurchaseTotalCount")
	@ResponseBody
	public Result<OrderCountVO> getPurchaseTotalCount(@RequestBody OrderStatisticDTO entity){
		
		logger.info("OrderStatisticController.getPurchaseTotalCount start, data="+entity);
		// 开始结束时间处理
		if(entity != null && entity.getStartTime() != null){
			entity.getStartTime().setHours(0);
			entity.getStartTime().setMinutes(0);
			entity.getStartTime().setSeconds(0);
		}
		if(entity != null && entity.getEndTime() != null){
			entity.getEndTime().setHours(23);
			entity.getEndTime().setMinutes(59);
			entity.getEndTime().setSeconds(59);
		}
		// 查询结果
		return orderStatisticService.orderPurchaseTotalCount(entity);
	}
	
	/**
	 * 获取履约情况统计列表
	 * @param entity
	 * @return
	 */
	@RequestMapping("/getDutiesCountArray")
	@ResponseBody
	public PaginatedResult<OrderDutiesVO> getDutiesCountArray(@RequestBody OrderDutiesDTO entity){
		
		logger.info("OrderStatisticController.getDutiesCountArray start, data="+entity);
		// 开始结束时间处理
		if(entity != null && entity.getStartTime() != null){
			entity.getStartTime().setHours(0);
			entity.getStartTime().setMinutes(0);
			entity.getStartTime().setSeconds(0);
		}
		if(entity != null && entity.getEndTime() != null){
			entity.getEndTime().setHours(23);
			entity.getEndTime().setMinutes(59);
			entity.getEndTime().setSeconds(59);
		}
		// 查询结果
		return orderStatisticService.orderDutiesCount(entity);
	}
	
	/**
	 * 订单统计数据列表  导出
	 * @param response
	 * @param jsonQuery
	 */
	@RequestMapping("/exportOrderPurchaseArray")
	public void exportOrderPurchaseArray(HttpServletResponse response , @RequestParam("jsonQuery") String jsonQuery){
		
		logger.info("OrderStatisticController.exportOrderPurchaseArray export, data=" + jsonQuery);
		
		OrderStatisticDTO entity = new OrderStatisticDTO();
		entity.setJsonData(jsonQuery);
		// 开始结束时间处理
		if(entity.getStartTime() != null){
			entity.getStartTime().setHours(0);
			entity.getStartTime().setMinutes(0);
			entity.getStartTime().setSeconds(0);
		}
		if(entity.getEndTime() != null){
			entity.getEndTime().setHours(23);
			entity.getEndTime().setMinutes(59);
			entity.getEndTime().setSeconds(59);
		}
		// 查询数据
		List<OrderPurchaseVO> itemList = orderStatisticService.orderPurchaseCountList(entity);
		// 文件名
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "商品服务统计" + sdf.format(new Date()) + ".xls"; 
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名
		try {
			// 创建excel文件
			WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
			// 居中样式
			WritableCellFormat wf = new WritableCellFormat();
			wf.setAlignment(Alignment.CENTRE);
			// 1.添加shift页
			WritableSheet sheet = book.createSheet("服务&商品统计",0);
			// 2.添加表头数据
			sheet.addCell(new Label(0, 0, "服务&商品名称", wf));
			sheet.addCell(new Label(0, 1, "供应商&创建者", wf));
			sheet.addCell(new Label(0, 2, "购买数量", wf));
			sheet.addCell(new Label(0, 3, "购买金额", wf));
			
			// 3.历史数据，业务数据，不用关注
			for (int i = 0; i < itemList.size(); i++) {
				OrderPurchaseVO item = itemList.get(i);
				// 服务&商品名称
				if( item!=null && item.getRelationName() !=null ) {
					sheet.addCell(new Label(0, i + 1, item.getRelationName() + "", wf));
				}else{
					sheet.addCell(new Label(0, i + 1, "", wf));
				}
				// 供应商&创建者
				if( item!=null && item.getRelationSupplier() !=null ) {
					sheet.addCell(new Label(1, i + 1, item.getRelationSupplier() + "", wf));
				}else{
					sheet.addCell(new Label(1, i + 1, "", wf));
				}
				// 购买数量
				if( item!=null && item.getBuyCount() !=null ) {
					sheet.addCell(new Label(2, i + 1, item.getBuyCount() + "", wf));
				}else{
					sheet.addCell(new Label(2, i + 1, "", wf));
				}
				// 购买金额
				if( item!=null && item.getPriceCount() !=null ) {
					sheet.addCell(new Label(3, i + 1, item.getPriceCount() + "", wf));
				}else{
					sheet.addCell(new Label(3, i + 1, "", wf));
				}
			}
			// 4.写入excel并关闭
			book.write();
			book.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 订单购买数据统计详情 导出
	 * @param response
	 * @param jsonQuery
	 */
	@RequestMapping("/exportPurchaseDetailArray")
	public void exportPurchaseDetailArray(HttpServletResponse response , @RequestParam("jsonQuery") String jsonQuery){
		
		logger.info("OrderStatisticController.exportPurchaseDetailArray export, data=" + jsonQuery);
		
		OrderStatisticDTO entity = new OrderStatisticDTO();
		entity.setJsonData(jsonQuery);
		entity.setRelationName(null);     // 名称模糊查询  清空
		entity.setRelationSupplier(null); // 供应商模糊查询 清空
		if(entity.getStartTime() != null){
			entity.getStartTime().setHours(0);
			entity.getStartTime().setMinutes(0);
			entity.getStartTime().setSeconds(0);
		}
		if(entity.getEndTime() != null){
			entity.getEndTime().setHours(23);
			entity.getEndTime().setMinutes(59);
			entity.getEndTime().setSeconds(59);
		}
		// 查询数据
		List<OrderStatisticVO> itemList = orderStatisticService.orderPurchaseDetailList(entity);
		// 文件名
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "商品服务统计详情" + sdf.format(new Date()) + ".xls"; 
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名
		try {
			// 创建excel文件
			WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
			// 居中样式
			WritableCellFormat wf = new WritableCellFormat();
			wf.setAlignment(Alignment.CENTRE);
			// 1.添加shift页
			WritableSheet sheet = book.createSheet("服务&商品统计详情",0);
			// 2.添加表头数据
			sheet.addCell(new Label(0, 0, "服务&商品名称", wf));
			sheet.addCell(new Label(1, 0, "购买人账号", wf));
			sheet.addCell(new Label(2, 0, "购买人", wf));
			sheet.addCell(new Label(3, 0, "订单号", wf));
			sheet.addCell(new Label(4, 0, "购买数量", wf));
			sheet.addCell(new Label(5, 0, "购买金额", wf));
			sheet.addCell(new Label(6, 0, "购买时间", wf));
			
			// 3.历史数据，业务数据，不用关注
			for (int i = 0; i < itemList.size(); i++) {
				OrderStatisticVO item = itemList.get(i);
				// 服务&商品名称
				if( item!=null && item.getRelationName() !=null ) {
					sheet.addCell(new Label(0, i + 1, item.getRelationName()  + "", wf));
				}else{
					sheet.addCell(new Label(0, i + 1, "", wf));
				}
				// 购买人账号
				if( item!=null && item.getUserMobile() !=null ) {
					sheet.addCell(new Label(1, i + 1, item.getUserMobile()  + "", wf));
				}else{
					sheet.addCell(new Label(1, i + 1, "", wf));
				}
				// 购买人
				if( item!=null && item.getUserName() !=null ) {
					sheet.addCell(new Label(2, i + 1, item.getUserName()  + "", wf));
				}else{
					sheet.addCell(new Label(2, i + 1, "", wf));
				}
				// 订单号
				if( item!=null && item.getOrderCode() !=null ) {
					sheet.addCell(new Label(3, i + 1, item.getOrderCode()  + "", wf));
				}else{
					sheet.addCell(new Label(3, i + 1, "", wf));
				}
				// 购买数量
				if( item!=null && item.getOrderCount() !=null ) {
					sheet.addCell(new Label(4, i + 1, item.getOrderCount()  + "", wf));
				}else{
					sheet.addCell(new Label(4, i + 1, "", wf));
				}
				// 购买金额
				if( item!=null && item.getPrice() !=null ) {
					sheet.addCell(new Label(5, i + 1, item.getPrice()  + "", wf));
				}else{
					sheet.addCell(new Label(5, i + 1, "", wf));
				}
				// 购买时间
				if( item!=null && item.getCreateTime() !=null ) {
					sheet.addCell(new Label(6, i + 1, item.getCreateTime() + "", wf));
				}else{
					sheet.addCell(new Label(6, i + 1, "", wf));
				}
			}
			// 4.写入excel并关闭
			book.write();
			book.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 履约情况 导出
	 * @param response
	 * @param entity
	 */
	@RequestMapping("/exportDutiesCountArray")
	public void exportDutiesCountArray(HttpServletResponse response , @RequestParam("jsonQuery") String jsonQuery){
		
		logger.info("OrderStatisticController.exportDutiesCountArray export, data=" + jsonQuery);
		
		OrderDutiesDTO entity = new OrderDutiesDTO();
		entity.setJsonData(jsonQuery);
		if(entity.getStartTime() != null){
			entity.getStartTime().setHours(0);
			entity.getStartTime().setMinutes(0);
			entity.getStartTime().setSeconds(0);
		}
		if(entity.getEndTime() != null){
			entity.getEndTime().setHours(23);
			entity.getEndTime().setMinutes(59);
			entity.getEndTime().setSeconds(59);
		}
		// 查询数据
		List<OrderDutiesVO> itemList = orderStatisticService.orderDutiesCountList(entity);
		// 文件名
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "履约情况" + sdf.format(new Date()) + ".xls"; 
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名
		try {
			// 创建excel文件
			WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
			// 居中样式
			WritableCellFormat wf = new WritableCellFormat();
			wf.setAlignment(Alignment.CENTRE);
			// 1.添加shift页
			WritableSheet sheet = book.createSheet("履约情况",0);
			// 2.添加表头数据
			sheet.addCell(new Label(0, 0, "签约账户", wf));
			sheet.addCell(new Label(1, 0, "签约人", wf));
			sheet.addCell(new Label(2, 0, "签约服务", wf));
			sheet.addCell(new Label(3, 0, "订单编号", wf));
			sheet.addCell(new Label(4, 0, "完成次数", wf));
			sheet.addCell(new Label(5, 0, "签约类型", wf));
			sheet.addCell(new Label(6, 0, "签约医生", wf));
			// 3.历史数据，业务数据，不用关注
			for (int i = 0; i < itemList.size(); i++) {
				OrderDutiesVO item = itemList.get(i);
				// 签约账户
				if( item!=null && item.getUserMobile()!=null ) {
					sheet.addCell(new Label(0, i + 1, item.getUserMobile() + "", wf));
				}else{
					sheet.addCell(new Label(0, i + 1, "", wf));
				}
				// 签约人
				if( item!=null && item.getSignUserName()!=null ) {
					sheet.addCell(new Label(1, i + 1, item.getSignUserName() + "", wf));
				}else{
					sheet.addCell(new Label(1, i + 1, "", wf));
				}
				// 签约服务
				if( item!=null && item.getServiceName() !=null ) {
					sheet.addCell(new Label(2, i + 1, item.getServiceName() + "", wf));
				}else{
					sheet.addCell(new Label(2, i + 1, "", wf));
				}
				// 订单编号
				if( item!=null && item.getOrderCode() !=null ) {
					sheet.addCell(new Label(3, i + 1, item.getOrderCode() + "", wf));
				}else{
					sheet.addCell(new Label(3, i + 1, "", wf));
				}
				// 完成次数
				if( item!=null && item.getReportCount() !=null ) {
					sheet.addCell(new Label(4, i + 1, item.getReportCount() + "", wf));
				}else{
					sheet.addCell(new Label(4, i + 1, "", wf));
				}
				// 签约类型
				if( item!=null && item.getBuildTypeDesc() !=null ) {
					sheet.addCell(new Label(5, i + 1, item.getBuildTypeDesc() + "", wf));
				}else{
					sheet.addCell(new Label(5, i + 1, "", wf));
				}
				// 签约医生
				if( item!=null && item.getDoctorName() !=null ) {
					sheet.addCell(new Label(6, i + 1, item.getDoctorName() + "", wf));
				}else{
					sheet.addCell(new Label(6, i + 1, "", wf));
				}
			}
			// 4.写入excel并关闭
			book.write();
			book.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
}
