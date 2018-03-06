package com.semioe.dubbo.service;

import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.Result;

public interface JyServiceService {

	/**
	 * getServiceInfo 查询服务信息
	 * 
	 * @param queryMap
	 * @return
	 */
	Result getServiceInfoListPage(ServiceInfoVO serviceVO);

	/**
	 * serviceInfoCreate 服务信息添加
	 * 
	 * @param service
	 * @return
	 */
	Result serviceInfoCreate(ServiceInfoVO serviceInfoVO);

	/**
	 * serviceInfoUpdate 服务信息修改
	 * 
	 * @param service
	 * @return
	 */
	Result serviceInfoUpdate(ServiceInfoVO service);

}