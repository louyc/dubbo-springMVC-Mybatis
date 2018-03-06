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
import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.OrderInfo;
import com.semioe.api.entity.ReportInfo;
import com.semioe.api.vo.OrderInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.OrderInfoService;
import com.semioe.dubbo.service.ReportInfoService;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Controller
@RequestMapping("/orderInfo")
public class OrderInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderInfoController.class);
	
	@Reference
	private OrderInfoService orderInfoService;
	
	@Reference
	private ReportInfoService reportInfoService;  //报表服务
	
	/* 查询订单数据列表 */
	@RequestMapping("/getOrderInfoArray")
	@ResponseBody
	public PaginatedResult getOrderInfoArray(@RequestBody OrderInfo orderInfo){
		logger.info("OrderInfoController.getOrderInfoArray start, select");
		return orderInfoService.getGoodsInfoArray(orderInfo);
	}
	
	/* 查询订单数据列表 */
	@RequestMapping("/getOrderReportCount")
	@ResponseBody
	public PaginatedResult getOrderReportCount(@RequestBody OrderInfo orderInfo){
		logger.info("OrderInfoController.getOrderReportCount start, select");
		return orderInfoService.getOrderReportCount(orderInfo);
	}
	
	/* 添加订单 */
	@RequestMapping("/addOrderInfo")
	@ResponseBody
	public Result addOrderInfo(@RequestBody OrderInfo orderInfo){
		logger.info("OrderInfoController.addOrderInfo start, goodsInfo = {}",orderInfo.toString());
		return orderInfoService.addOrderInfo(orderInfo);
	}
	
	/* 修改订单 */
	@RequestMapping("/updateOrderInfo")
	@ResponseBody
	public Result updateOrderInfo(@RequestBody OrderInfo orderInfo){
		logger.info("OrderInfoController.updateOrderInfo start, goodsInfo = {}",orderInfo.toString());
		return orderInfoService.updateOrderInfo(orderInfo);
	}
	
	/* 删除订单 */
	@RequestMapping("/deleteOrderInfo")
	@ResponseBody
	public Result deleteOrderInfo(@RequestParam("id") Integer id){
		logger.info("OrderInfoController.deleteOrderInfo start, id = {}",id);
		return orderInfoService.deleteOrderInfo(id);
	}
	
	/* 根据id 查询订单详情 */
	@RequestMapping("/getOrderInfoById")
	@ResponseBody
	public Result getOrderInfoById(@RequestParam("id") Integer id){
		logger.info("OrderInfoController.getOrderInfoById start, id = {}",id);
		return orderInfoService.getOrderInfoById(id);
	}
	
	/* 根据id 查询订单详情 */
	@RequestMapping("/getOrderDetailById")
	@ResponseBody
	public Result getOrderDetailById(@RequestParam("id") Integer id){
		logger.info("OrderInfoController.getOrderDetailById start, id = {}",id);
		return orderInfoService.getOrderDetailById(id);
	}
	
	/* 销售总金额统计 */
	@RequestMapping("/countTotalPrice")
	@ResponseBody
	public Result countTotalPrice(@RequestBody OrderInfo orderInfo){
		logger.info("OrderInfoController.countTotalPrice start, goodsInfo = {}",orderInfo.toString());
		return orderInfoService.countTotalPrice(orderInfo);
	}
	
	/* 销售金额 按日期统计 */
	@RequestMapping("/countPrice")
	@ResponseBody
	public Result countPrice(@RequestBody OrderInfo orderInfo){
		logger.info("OrderInfoController.countPrice start, goodsInfo = {}",orderInfo.toString());
		return orderInfoService.countPrice(orderInfo);
	}
	
	/* 销售总数量统计 */
	@RequestMapping("/countTotalAmount")
	@ResponseBody
	public Result countTotalAmount(@RequestBody OrderInfo orderInfo){
		logger.info("OrderInfoController.countTotalAmount start, goodsInfo = {}",orderInfo.toString());
		return orderInfoService.countTotalAmount(orderInfo);
	}
	
	/* 销售数量 按日期统计 */
	@RequestMapping("/countAmount")
	@ResponseBody
	public Result countAmount(@RequestBody OrderInfo orderInfo){
		logger.info("OrderInfoController.countAmount start, goodsInfo = {}",orderInfo.toString());
		return orderInfoService.countAmount(orderInfo);
	}
	
	/* 订单数量&金额 数据统计 */
	@RequestMapping("/countOrderInfo")
	@ResponseBody
	public PaginatedResult countOrderInfo(@RequestBody OrderInfo orderInfo){
		logger.info("OrderInfoController.countOrderInfo start, goodsInfo = {}",orderInfo.toString());
		return orderInfoService.countOrderInfo(orderInfo);
	}
	
	
	/**
	 * 导出EXCEL文件
	 * 
	 * @param response
	 */
	@RequestMapping(value = "/exportOrderInfo")
	public void exportOrderInfo(HttpServletResponse response , 
			@RequestParam(value = "orderCode", required = false) String orderCode ,
			@RequestParam(value = "relationName", required = false) String relationName ,
			@RequestParam(value = "relationSupplier", required = false) String relationSupplier ,
			@RequestParam(value = "startDate", required = false) String startDate ,
			@RequestParam(value = "endDate", required = false) String endDate ,
			@RequestParam(value = "type", required = false) String type ,
			@RequestParam(value = "payStatus", required = false) String payStatus) {
		
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setShowCount(-1);
		if (orderCode != null && !orderCode.isEmpty()) { orderInfo.setOrderCode(orderCode);}
		if (relationName != null && !relationName.isEmpty()) { orderInfo.setRelationName(relationName);}
		if (relationSupplier != null && !relationSupplier.isEmpty()) { orderInfo.setRelationSupplier(relationSupplier);}
		if (startDate != null && !startDate.isEmpty()) { orderInfo.setStartDate(startDate);}
		if (endDate != null && !endDate.isEmpty()) { orderInfo.setEndDate(endDate);}
		if (type != null && !type.isEmpty()) { orderInfo.setType(type);}
		if (payStatus != null && !payStatus.isEmpty()) { orderInfo.setPayStatus(Integer.parseInt(payStatus));}
		
		logger.info("OrderInfoController.exportOrderInfo start, goodsInfo = {}",orderInfo.toString());
		// 文件名
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = sdf.format(new Date()) + ".xls"; 

		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名

		//查询订单数据
		List<OrderInfoVO> itemList = orderInfoService.getAllGoodsInfoArray(orderInfo);
		
		try {
			// 1.创建excel文件
			WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
			// 居中样式
			WritableCellFormat wf = new WritableCellFormat();
			wf.setAlignment(Alignment.CENTRE);

			WritableSheet sheet = book.createSheet("订单",0);
			
			// 2.添加表头数据
			sheet.addCell(new Label(0, 0, "订单编号", wf));
			sheet.addCell(new Label(1, 0, "商品名称", wf));
			sheet.addCell(new Label(2, 0, "供应商", wf));
			sheet.addCell(new Label(3, 0, "单价", wf));
			sheet.addCell(new Label(4, 0, "数量", wf));
			sheet.addCell(new Label(5, 0, "金额", wf));
			sheet.addCell(new Label(6, 0, "用户", wf));
			sheet.addCell(new Label(7, 0, "状态", wf));
			sheet.addCell(new Label(8, 0, "订单时间", wf));
			
			sheet.addCell(new Label(9, 0, "收货人", wf));
			sheet.addCell(new Label(10, 0, "收获地址", wf));
			sheet.addCell(new Label(11, 0, "详细地址", wf));
			sheet.addCell(new Label(12, 0, "联系方式", wf));
			sheet.addCell(new Label(13, 0, "邮编", wf));
			
			sheet.addCell(new Label(14, 0, "订单来源", wf));
			sheet.addCell(new Label(15, 0, "推荐人", wf));
			
			// 3.历史数据，业务数据，不用关注
			for (int i = 0; i < itemList.size(); i++) {
				OrderInfoVO order = itemList.get(i);
				//订单编号
				if( order!=null && order.getOrderCode()!=null ) {
					sheet.addCell(new Label(0, i + 1, order.getOrderCode() + "", wf));
				}else{
					sheet.addCell(new Label(0, i + 1, "", wf));
				}
				//商品名称
				if( order!=null && order.getRelationName()!=null ){
					sheet.addCell(new Label(1, i + 1, order.getRelationName() + "", wf));
				}else{
					sheet.addCell(new Label(1, i + 1, "", wf));
				}
				//供应商
				if( order!=null && order.getRelationSupplier()!=null ){
					sheet.addCell(new Label(2, i + 1, order.getRelationSupplier() + "", wf));
				}else{
					sheet.addCell(new Label(2, i + 1, "", wf));
				}
				//单价
				if( order!=null && order.getRelationPrice()!=null ){	
					sheet.addCell(new Label(3, i + 1, order.getRelationPrice() + "", wf));
				}else{
					sheet.addCell(new Label(3, i + 1, "", wf));
				}
				//数量
				if( order!=null && order.getOrderCount()!=null ){
					sheet.addCell(new Label(4, i + 1, order.getOrderCount() + "", wf));
				}else{
					sheet.addCell(new Label(4, i + 1, "", wf));
				}
				//金额
				if( order!=null && order.getPrice()!=null ){
					sheet.addCell(new Label(5, i + 1, order.getPrice()  + "", wf));
				}else{
					sheet.addCell(new Label(5, i + 1, "", wf));
				}
				//用户名
				if( order!=null && order.getUserName()!=null ){
					sheet.addCell(new Label(6, i + 1, order.getUserName()+"", wf));  
				}else{
					sheet.addCell(new Label(6, i + 1, "", wf));  
				}
				//支付状态
				if( order!= null && order.getPayStatus() != null && order.getPayStatus()==0 ){
					sheet.addCell(new Label(7, i + 1, "未支付", wf));
				}else if( order!= null && order.getPayStatus() != null && order.getPayStatus()==1 ){
					sheet.addCell(new Label(7, i + 1, "已支付 ", wf));
				}else{
					sheet.addCell(new Label(7, i + 1, "", wf));
				}
				//订单时间
				if( order != null && order.getCreateTime() != null ){
					sheet.addCell(new Label(8, i + 1, sdf.format(order.getCreateTime()) + "", wf));
				}else{
					sheet.addCell(new Label(8, i + 1, "", wf));
				}
				//收货信息
				if( order != null && !StringUtil.isEmpty(order.getUserAddress()) ){
					try{
						JSONObject addressJs = JSONObject.parseObject(order.getUserAddress());
						
						sheet.addCell(new Label(9, i + 1, addressJs.getString("name") , wf));  //收货人
						sheet.addCell(new Label(10, i + 1, addressJs.getString("address") , wf));  //收获地址
						sheet.addCell(new Label(11, i + 1, addressJs.getString("detailedAddress") , wf));  //详细地址
						sheet.addCell(new Label(12, i + 1, addressJs.getString("mobile") , wf));  //联系方式
						sheet.addCell(new Label(13, i + 1, addressJs.getString("zipCode") , wf));  //邮编
						
					}catch (Exception e) {
						logger.error("收货信息："+order.getUserAddress());
						logger.error("转换收获信息异常，{}",e);
					}
				}
				//订单来源
				if( order!= null && order.getSourceType() != null && "0".equals(order.getSourceType()) ){
					sheet.addCell(new Label(14, i + 1, "广告位", wf));
				}else if( order!= null && order.getSourceType() != null && "1".equals(order.getSourceType()) ){
					sheet.addCell(new Label(14, i + 1, "医生主页", wf));
				}else if( order!= null && order.getSourceType() != null && "2".equals(order.getSourceType()) ){
					sheet.addCell(new Label(14, i + 1, "推荐服务", wf));
				}else{
					sheet.addCell(new Label(14, i + 1, "", wf));
				}
				
				//推荐人
				if( order != null && order.getSourceUser() != null ){
					sheet.addCell(new Label(15, i + 1, order.getSourceUser() + "", wf));
				}else{
					sheet.addCell(new Label(15, i + 1, "", wf));
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
	
	@RequestMapping("/getReportArray")
	@ResponseBody
	public PaginatedResult getReportArray(@RequestBody ReportInfo reportInfo){
		logger.info("OrderInfoController.getReportArray start, reportInfo = " + reportInfo.toString() );
		return reportInfoService.getReportListByOrderId(reportInfo);
	}
	
	@RequestMapping("/doctorRecService")
	@ResponseBody
	public Result doctorRecService(@RequestParam("doctorId") String doctorId , 
			@RequestParam("userIds") List<String> userIds , @RequestParam("serviceIds") List<Integer> serviceIds){
		logger.info("OrderInfoController.doctorRecService start, doctorId="+doctorId );
		return orderInfoService.doctorRecService(doctorId,userIds,serviceIds);
	}
	
}
