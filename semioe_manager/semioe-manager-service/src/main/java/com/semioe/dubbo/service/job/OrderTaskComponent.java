package com.semioe.dubbo.service.job;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.semioe.api.entity.OrderInfo;
import com.semioe.dubbo.service.OrderInfoService;

@Component
public class OrderTaskComponent {

	private static final Logger logger = LoggerFactory.getLogger(OrderTaskComponent.class);

	@Autowired
	private OrderInfoService orderInfoService;

	/**
	 * 定时处理订单
	 */
	@Scheduled(cron = "0 0 0/1 * * ?")
	public void runOrderTask() {
		if (orderInfoService == null) {
			return;
		}
		logger.info("OrderTaskComponent.runOrderTask Start ");
		// 查询过期订单
		List<OrderInfo> outTimeOrders = orderInfoService.getOutTimeOrders();
		// 判断是否存在过期订单
		if (outTimeOrders != null) {
			logger.info("  outTimeOrders size :" + outTimeOrders.size());
			// 循环取消订单
			for (OrderInfo outOrder : outTimeOrders) {
				logger.info("  Cancal outOrder :" + outOrder.toString());
				orderInfoService.payOrderCancal(outOrder); // 取消订单
			}
		}
		logger.info("OrderTaskComponent.runOrderTask End ");
	}

}
