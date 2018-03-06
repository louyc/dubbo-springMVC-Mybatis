package com.semioe.web.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.dto.SbOrderStatisticDTO;
import com.semioe.api.vo.SbOrderDutiesVO;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.SbOrderStatisticService;

@Controller
@RequestMapping("/sbOrderStatistic")
public class SbOrderStatisticController {

	private static final Logger logger = LoggerFactory.getLogger(SbOrderStatisticController.class);
	
	@Reference
	private SbOrderStatisticService orderStatisticService;
	
	/**
	 * 获取履约情况统计列表
	 * @param entity
	 * @return
	 */
	@RequestMapping("/getSbDutiesCount")
	@ResponseBody
	public Result<Map<String, Object>> getSbDutiesCount(){
		
		logger.info("OrderStatisticController.getSbDutiesCount start, select");
		
		return orderStatisticService.sbOrderDutiesCount();
	}
	
	/**
	 * 机构维度 履约用户统计
	 * @param keywordsId
	 * @return
	 */
	@RequestMapping("/getSbOrgDutiesCount")
	@ResponseBody
	public Result<List<SbOrderDutiesVO>> getSbOrgDutiesCount(@RequestParam(value="keywordsId",required=false) Integer keywordsId){
		
		logger.info("OrderStatisticController.getSbOrgDutiesCount start,  keywordsId="+keywordsId);
		
		SbOrderStatisticDTO entity = new SbOrderStatisticDTO();
		entity.setKeywordsId(keywordsId);
		
		return orderStatisticService.sbOrgDutiesCount(entity);
	}
	
	/**
	 * 机构维度 签约用户统计
	 * @return
	 */
	@RequestMapping("/getSbOrgUserCount")
	@ResponseBody
	public Result<List<SbOrderDutiesVO>> getSbOrgUserCount(){
		
		logger.info("OrderStatisticController.getSbOrgUserCount start, select");
		
		return orderStatisticService.sbOrgUserCount();
	}
	
	/**
	 * 机构签约履约情况统计
	 * @return
	 */
	@RequestMapping("/getSbOrgStatisticCount")
	@ResponseBody
	public Result<List<Map<String, Object>>> getSbOrgStatisticCount(){
		
		logger.info("OrderStatisticController.getSbOrgStatisticCount start, select");
		
		return orderStatisticService.sbOrgStatisticCount();
	}
	
}
