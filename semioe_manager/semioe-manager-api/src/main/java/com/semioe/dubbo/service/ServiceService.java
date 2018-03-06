package com.semioe.dubbo.service;

import com.semioe.api.entity.ServiceInfo;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import java.util.List;

public interface ServiceService {

	/**
	 * getServiceInfo 查询服务信息
	 * 
	 * @param queryMap
	 * @return
	 */
	Result getServiceInfoListPage(ServiceInfoVO serviceVO);

	/**
	 * 根据关键字查询服务
	 * 
	 * @author quanlj
	 * @param serviceVO
	 * @return
	 */
	PaginatedResult<ServiceInfoVO> getServiceInfoVoByKeywords(ServiceInfoVO serviceVO);

	/**
	 * getServiceInfo 查询单个服务信息
	 * 
	 * @param queryMap
	 * @return
	 */
	Result queryGoodsById(String id);

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

	/**
	 * serviceInfoRemove 服务信息删除
	 * 
	 * @param id
	 * @return
	 */
	Result serviceInfoRemove(String id);

	/**
	 * serviceInfoExamine 服务信息审核
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	Result serviceInfoExamine(String id, String status, String opinion);
	/**
	 * keyUpdate 标签修改
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	Result keyUpdate(String id,String keywordsIdList);

	/**
	 * queryAllServiceInfo 查询用户下所有服务信息
	 * 
	 * @return
	 */
	Result queryAllServiceInfo(String createUserId);

	List<ServiceInfo> selectByParameter(ServiceInfoVO service);

	// 20171031

	Result shareServiceInfo(String id, String serviceShare);

	Result getAllotServices(ServiceInfoVO service);

	Result getAllShareServices(ServiceInfoVO service);

}