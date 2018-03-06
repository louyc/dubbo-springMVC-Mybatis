package com.semioe.dubbo.service;

import java.util.List;

import com.semioe.api.entity.OrderInfo;
import com.semioe.api.vo.OrderCountVO;
import com.semioe.api.vo.OrderInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;

public interface OrderInfoService {

	/* 查询订单数据列表 */
	PaginatedResult<OrderInfoVO> getGoodsInfoArray(OrderInfo orderInfo);
	
	/* 查询订单列表及报告数量 */
	PaginatedResult<OrderInfoVO> getOrderReportCount(OrderInfo orderInfo);
	
	/* 查询所有订单数据列表 */
	List<OrderInfoVO> getAllGoodsInfoArray(OrderInfo orderInfo);
	
	/* 添加订单 */
	Result<OrderInfo> addOrderInfo(OrderInfo orderInfo);
	
	/* 重新支付订单 */
	Result<OrderInfoVO> repeatPayOrder(String orderCode);
	
	/* 取消支付订单 */
	Result payOrderCancal(OrderInfo orderInfo);
	
	/* 修改订单 */
	Result updateOrderInfo(OrderInfo orderInfo);
	
	/* 修改订单，返回成功失败 */
	boolean updateOrder(OrderInfo orderInfo);
	
	/* 删除订单 */
	Result deleteOrderInfo(Integer id);
	
	/* 根据id 查询订单详情 */
	Result getOrderInfoById(Integer id);
	
	/* 查询订单详细信息（含对象） */
	Result<OrderInfoVO> getOrderDetailById(Integer id);
	
	/*  查询订单详情 */
	OrderInfoVO getOrderInfoByCode(String orderCode);
	
	/* 销售总金额统计 */
	Result countTotalPrice(OrderInfo example);
	
	/* 销售金额 按日期统计 */
	Result countPrice(OrderInfo example);
	
	/* 销售总数量统计 */
	Result countTotalAmount(OrderInfo example);
	
	/* 销售数量 按日期统计 */
	Result countAmount(OrderInfo example);
	
	/* 订单数量&金额 数据统计 */
	PaginatedResult<OrderCountVO> countOrderInfo(OrderInfo orderInfo);
	
	/* 获取过期订单数据 */
	List<OrderInfo> getOutTimeOrders();
	
	/* 医生推荐服务 */
	Result doctorRecService(String doctorId , List<String> userId , List<Integer> serviceId);
	
}
