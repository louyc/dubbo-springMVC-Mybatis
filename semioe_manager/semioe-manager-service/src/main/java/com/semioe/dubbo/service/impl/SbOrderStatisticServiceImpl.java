/**
 * 订单统计服务接口实现
 */
package com.semioe.dubbo.service.impl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.dto.SbOrderStatisticDTO;
import com.semioe.api.vo.SbOrderDutiesVO;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.SbOrderStatisticMapper;
import com.semioe.dubbo.service.SbOrderStatisticService;

@Service
public class SbOrderStatisticServiceImpl implements SbOrderStatisticService {

	private static final Logger logger = LoggerFactory.getLogger(SbOrderStatisticServiceImpl.class);
	
	@Autowired
	private SbOrderStatisticMapper sbOrderStatisticMapper;

	@Override
	public Result<Map<String, Object>> sbOrderDutiesCount() {
		// TODO Auto-generated method stub
		Result<Map<String, Object>> result = new Result<>(StatusCodes.OK, true);
		
		Map<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("dutiesList", sbOrderStatisticMapper.sbKeywordsDutiesList());
		
		resultMap.put("signCount", sbOrderStatisticMapper.signUserCount());
		
		resultMap.put("dutiesCount", sbOrderStatisticMapper.dutiesUserCount());
		
		result.setData(resultMap);
		result.setResultCode(new ResultCode("SUCCESS", "成功！"));
		
		return result;
	}

	@Override
	public Result<List<SbOrderDutiesVO>> sbOrgDutiesCount(SbOrderStatisticDTO entity) {
		// TODO Auto-generated method stub
		Result<List<SbOrderDutiesVO>> result = new Result<>(StatusCodes.OK, true);
		
		result.setData(sbOrderStatisticMapper.sbOrgDutiesList(entity));
		result.setResultCode(new ResultCode("SUCCESS", "成功！"));
		return result;
	}

	@Override
	public Result<List<SbOrderDutiesVO>> sbOrgUserCount() {
		// TODO Auto-generated method stub
		Result<List<SbOrderDutiesVO>> result = new Result<>(StatusCodes.OK, true);
		
		result.setData(sbOrderStatisticMapper.signOrgUserList());
		result.setResultCode(new ResultCode("SUCCESS", "成功！"));
		return result;
	}

	@Override
	public Result<List<Map<String, Object>>> sbOrgStatisticCount() {
		// TODO Auto-generated method stub
		Result<List<Map<String, Object>>> result = new Result<>(StatusCodes.OK, true);
		// 返回结果集
		List<Map<String, Object>> resultList = new ArrayList<>();
		result.setData(resultList);
		
		// 机构 签约统计
		List<SbOrderDutiesVO> signList = sbOrderStatisticMapper.signOrgUserList();
		// 机构 履约统计
		List<SbOrderDutiesVO> dutiesList = sbOrderStatisticMapper.sbOrgDutiesList(new SbOrderStatisticDTO());
		
		DecimalFormat df = new DecimalFormat("0%");
		df.setMaximumFractionDigits(2);
		df.setRoundingMode(RoundingMode.HALF_UP); 
		
		if(dutiesList != null && dutiesList.size() > 0 
				&& signList != null && signList.size() > 0 ){
			
			Integer totalSign = 0 ;
			Integer totalDuties = 0 ;
			
			for(SbOrderDutiesVO sign : signList){
				for(SbOrderDutiesVO duties : dutiesList){
					// 按照机构ID合并数据
					if(duties.getDutiesKey().equals(sign.getDutiesKey())  ){
						
						Map<String, Object> resultMap = new HashMap<>();
						resultMap.put("type", 0);
						resultMap.put("orgName", sign.getDutiesName()); // 机构名称
						resultMap.put("signCount", sign.getDutiesCount()); // 签约数量
						resultMap.put("dutiesCount", duties.getDutiesCount()); // 履约数量
						
						if(sign.getDutiesCount() > 0 && duties.getDutiesCount() > 0 ){
							resultMap.put("ratio", df.format(duties.getDutiesCount()*1.0 / sign.getDutiesCount()*1.0) ); // 比率
						}else{
							resultMap.put("ratio", "0%" ); // 比率
						}
						
						resultList.add(resultMap);
						
						totalSign += sign.getDutiesCount();
						totalDuties += duties.getDutiesCount();
						
						dutiesList.remove(duties);
						break;
					}
				}
			}
			
			// 添加合计值
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("type", 1);
			resultMap.put("orgName", ""); // 机构名称
			resultMap.put("signCount", totalSign); // 签约数量
			resultMap.put("dutiesCount", totalDuties); // 履约数量
			
			if(totalSign > 0 && totalDuties > 0 ){
				resultMap.put("ratio", df.format(totalDuties*1.0 / totalSign*1.0) ); // 比率
			}else{
				resultMap.put("ratio", "0%" ); // 比率
			}
			
			resultList.add(resultMap);
			
			result.setResultCode(new ResultCode("SUCCESS", "成功！"));
		}else{
			logger.error("统计机构履约失败   dutiesList="+dutiesList + " signList=" +signList );
			result.setResultCode(new ResultCode("FALL", "查询失败"));
		}
		
		return result;
	}
	
}
