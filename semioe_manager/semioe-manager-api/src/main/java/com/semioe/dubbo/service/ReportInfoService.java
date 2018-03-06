package com.semioe.dubbo.service;

import com.semioe.api.entity.ReportInfo;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;

public interface ReportInfoService {

	/** 根据订单id查询报表列表  */
	PaginatedResult<ReportInfo> getReportListByOrderId(ReportInfo reportInfo);
	
	/** 保存报表数据 */
	Result<ReportInfo> addReportInfo(ReportInfo reportInfo);
	
}
