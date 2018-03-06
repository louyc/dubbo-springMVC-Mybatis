package com.semioe.dubbo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.ReportInfo;
import com.semioe.api.vo.OrderInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.OrderInfoMapper;
import com.semioe.dubbo.dao.ReportInfoMapper;
import com.semioe.dubbo.service.ReportInfoService;

@Service
public class ReportInfoServiceImpl implements ReportInfoService {

	@Autowired
	private ReportInfoMapper reportInfoMapper;
	
	@Autowired
	private OrderInfoMapper orderInfoMapper;
	
	@Override
	public PaginatedResult<ReportInfo> getReportListByOrderId(ReportInfo reportInfo){
		
		PaginatedResult<ReportInfo> result = null;
		List<ReportInfo> reportList = new ArrayList<>();
		// 分页查询数据
		if (0 < reportInfo.getCurrentPage() && 0 < reportInfo.getShowCount() ) {
			//查询总数量
			Integer totalCount = (int) reportInfoMapper.countByOrderId(reportInfo);
			//计算分页信息
			int beginSize = (reportInfo.getCurrentPage() - 1) * reportInfo.getShowCount();
			reportInfo.setCurrentResult(beginSize);
			//查询数据
			reportList = reportInfoMapper.selectByOrderId(reportInfo);
			result = new PaginatedResult<ReportInfo>(reportList, reportInfo.getCurrentPage(),
					reportInfo.getShowCount(), totalCount);
		} else {
			reportList = reportInfoMapper.selectByOrderId(reportInfo);
			result = new PaginatedResult<ReportInfo>(reportList, -1, -1, reportList.size());
		}
		
		return result;
	}

	@Override
	public Result<ReportInfo> addReportInfo(ReportInfo reportInfo) {
		Result<ReportInfo> result = new Result<>(StatusCodes.OK, true);
		//根据订单ID查询订单
		if(reportInfo == null ){
			result.setResultCode(new ResultCode("fell", "保存对象不能为空！"));
			return result;
		}
		//查询订单数据
		OrderInfoVO orderInfo = orderInfoMapper.selectVoByPrimaryKey(reportInfo.getOrderId());
		if(orderInfo == null){
			result.setResultCode(new ResultCode("fell", "订单不存在"));
			return result;
		}
		reportInfo.setCreateTime(new Date());
		int num = reportInfoMapper.insert(reportInfo);   //保存数据
		
		if(num >= 1){
			result.setData(reportInfo);
			result.setResultCode(new ResultCode("SUCCESS", "报告保存成功！"));
		}else{
			result.setResultCode(new ResultCode("SUCCESS", "报告保存失败！"));
		}
		
		return result;
	}
	
}
