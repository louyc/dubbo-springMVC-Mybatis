package com.semioe.dubbo.service;

import com.semioe.api.entity.PersonalTailor;
import com.semioe.api.vo.PersonalTailorVO;
import com.semioe.common.result.Result;

public interface PersonalTailorService {

	/**
	 * getPersonalTailor:查询私人订制. <br/>
	 * 
	 * @param id
	 *            主键id
	 * @return Result
	 * @author lyc
	 */
	Result getPersonalTailor(String id);

	/**
	 * getPersonalTailor:查询私人订制. <br/>
	 * 
	 * @param personalTailorVO
	 * @return Result
	 * @author lyc
	 */
	Result getPersonalTailor(PersonalTailorVO personalTailorVO);

	/**
	 * personalTailorCreate 添加私人订制
	 * 
	 * @param personalTailor
	 * @return
	 */
	Result personalTailorCreate(PersonalTailor personalTailor);

	/**
	 * personalTailorUpdate 更新私人订制
	 * 
	 * @param personalTailor
	 * @return
	 */
	Result personalTailorUpdate(PersonalTailor personalTailor);

	/**
	 * personalTailorRemove 删除私人订制
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	Result personalTailorRemove(String id);

}