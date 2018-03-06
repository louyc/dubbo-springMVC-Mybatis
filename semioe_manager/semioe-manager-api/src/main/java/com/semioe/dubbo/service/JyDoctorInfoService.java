package com.semioe.dubbo.service;

import java.util.List;

import com.semioe.api.entity.JyDoctorInfo;
import com.semioe.api.vo.JyDoctorInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;

/**
 * （家庭医生）医生服务
 * @author puanl
 *
 */
public interface JyDoctorInfoService {
	
	/**
	 * 查询所有医生，带签约状态，
	 * @param entity
	 * @return
	 */
	Result<List<JyDoctorInfoVO>> findJyDoctorInfo(JyDoctorInfo entity);
	
	/**
	 * 根据机构id，查询所有历史签约医生
	 * 根据机构id，查询当前有效签约医生
	 * @param entity
	 * @return
	 */
	PaginatedResult<JyDoctorInfoVO> getJySignDoctorInfo(JyDoctorInfo entity);
	
	/**
	 * 修改家庭医生签约类型
	 * @param relId
	 * @param types
	 * @return
	 */
	Result<Object> changeSignType(Integer signId , List<Integer> types); 
	
}
