/**
 * 订单统计服务
 */
package com.semioe.dubbo.service;

import java.util.List;
import java.util.Map;

import com.semioe.api.dto.SbOrderStatisticDTO;
import com.semioe.api.vo.SbOrderDutiesVO;
import com.semioe.common.result.Result;

public interface SbOrderStatisticService {

	/**
	 * 履约情况统计
	 * @param entity
	 * @return
	 */
	Result<Map<String, Object>> sbOrderDutiesCount();
	
	/**
	 * 机构维度  履约情况统计
	 * @return
	 */
	Result<List<SbOrderDutiesVO>> sbOrgDutiesCount(SbOrderStatisticDTO entity);
	
	/**
	 * 机构维度 签约情况统计
	 * @return
	 */
	Result<List<SbOrderDutiesVO>> sbOrgUserCount();
	
	/**
	 * 机构统计
	 * @return
	 */
	Result<List<Map<String, Object>>> sbOrgStatisticCount();
	
}
