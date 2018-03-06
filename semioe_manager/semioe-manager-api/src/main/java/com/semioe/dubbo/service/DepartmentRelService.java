package com.semioe.dubbo.service;

public interface DepartmentRelService {

	/**
	 * 批量插入部门
	 * 
	 * @param departmentDics
	 * @return
	 */
	public int insertDepts(String[] deptArr, String managerId);

	/**
	 * 解除部门关系
	 * 
	 * @param managerId
	 * @param deptId
	 * @return
	 */
	public int removeRel(String managerId);
}
