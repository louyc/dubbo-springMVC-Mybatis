package com.semioe.dubbo.service;

import com.semioe.api.entity.DepartmentDic;
import com.semioe.api.entity.DepartmentDicExample;
import com.semioe.common.result.Result;

public interface DepartmentDicService {

	/**
	 * 添加部门
	 * @param departmentDic
	 * @return
	 */
	Result insert(DepartmentDic departmentDic);
	
	
	/**
	 * 根据查询
	 * @param departmentDic
	 * @return
	 */
	Result getAllDept(int in_use);
	
	
	/**
	 * 修改部门名称
	 * @param departmentDic
	 * @return
	 */
	Result uodateById(DepartmentDic departmentDic, DepartmentDicExample departmentDicExample);
	
	
}
