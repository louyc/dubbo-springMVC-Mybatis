/**
 * 订单统计接口
 */
package com.semioe.dubbo.dao;

import java.util.List;

import com.semioe.api.dto.OrderDutiesDTO;
import com.semioe.api.dto.OrderStatisticDTO;
import com.semioe.api.vo.OrderCountVO;
import com.semioe.api.vo.OrderDutiesVO;
import com.semioe.api.vo.OrderPurchaseVO;
import com.semioe.api.vo.OrderStatisticVO;

public interface OrderStatisticMapper {

	List<OrderPurchaseVO> orderPurchaseInfoListPage(OrderStatisticDTO entity);
	
	List<OrderPurchaseVO> orderPurchaseInfo(OrderStatisticDTO entity);
	
	List<OrderStatisticVO> orderPurchaseDetailListPage(OrderStatisticDTO entity);
	
	List<OrderStatisticVO> orderPurchaseDetail(OrderStatisticDTO entity);
	
	List<OrderDutiesVO> orderDutiesListPage(OrderDutiesDTO entity);
	
	List<OrderDutiesVO> orderDuties(OrderDutiesDTO entity);
	
	OrderCountVO orderPurchaseTotalCount(OrderStatisticDTO entity);
}
