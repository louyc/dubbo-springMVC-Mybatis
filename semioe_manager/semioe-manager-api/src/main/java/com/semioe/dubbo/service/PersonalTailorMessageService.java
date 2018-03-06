package com.semioe.dubbo.service;

import com.semioe.api.entity.PersonalTailorMessage;
import com.semioe.api.vo.PersonalTailorMessageVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;

public interface PersonalTailorMessageService {

	/**
	 * 添加
	 * @param personalTailorMessage
	 * @return
	 */
	Result add(PersonalTailorMessage personalTailorMessage);
	
	/**
	 * 查询
	 * @param personalTailorMessageVO
	 * @return
	 */
	PaginatedResult queryByConditions(PersonalTailorMessageVO personalTailorMessageVO);
	
	/**
	 *  阅读
	 * @param id
	 * @return
	 */
	Result read(Integer id);
	
	/**
	 * 处理
	 * @param id
	 * @return
	 */
	Result handle(Integer id);
}
