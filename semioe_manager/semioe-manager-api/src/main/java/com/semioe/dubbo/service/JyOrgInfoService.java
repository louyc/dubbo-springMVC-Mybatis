package com.semioe.dubbo.service;

import com.semioe.api.entity.JyOrgInfo;
import com.semioe.api.vo.JyOrgInfoVO;
import com.semioe.common.result.PaginatedResult;

/**
 * （家庭医生） 机构服务
 * @author puanl
 *
 */
public interface JyOrgInfoService {

	/**
	 * 查询医生签约机构，医生ID必填
	 * @param entity
	 * @return
	 */
	PaginatedResult<JyOrgInfoVO> getJyDoctorSignOrg(JyOrgInfo entity);
	
	/**
	 * 查询所有家庭医生机构
	 * @param entity
	 * @return
	 */
	PaginatedResult<JyOrgInfoVO> getJyOrgInfoList(JyOrgInfo entity);
	
}
