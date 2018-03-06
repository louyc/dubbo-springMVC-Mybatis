package com.semioe.dubbo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.DepartmentRel;
import com.semioe.api.entity.DepartmentRelExample;
import com.semioe.dubbo.dao.DepartmentRelMapper;
import com.semioe.dubbo.service.DepartmentRelService;

@Service
public class DepartmentRelServiceImpl implements DepartmentRelService {

	private static final Logger logger = LoggerFactory.getLogger(DepartmentRelServiceImpl.class);

	@Autowired
	private DepartmentRelMapper departmentRelMapper;

	/**
	 * 批量新增科室关系
	 */
	@Override
	public int insertDepts(String[] deptArr, String managerId) {
		List<DepartmentRel> deptRelList = new ArrayList<DepartmentRel>();
		for (int i = 0, length = deptArr.length; i < length; i++) {
			DepartmentRel departmentRel = new DepartmentRel();
			departmentRel.setDeptId(Integer.valueOf(deptArr[i]));
			departmentRel.setManagerId(managerId);
			deptRelList.add(departmentRel);
		}
		return departmentRelMapper.insertDeptRels(deptRelList);
	}

	/**
	 * 删除科室关系
	 */
	@Override
	public int removeRel(String managerId) {
		DepartmentRelExample example = new DepartmentRelExample();
		example.createCriteria().andManagerIdEqualTo(managerId);
		return departmentRelMapper.deleteByExample(example);
	}

}
