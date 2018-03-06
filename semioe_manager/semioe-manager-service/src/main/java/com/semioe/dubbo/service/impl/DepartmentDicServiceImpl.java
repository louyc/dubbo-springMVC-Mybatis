package com.semioe.dubbo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.DepartmentDic;
import com.semioe.api.entity.DepartmentDicExample;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.DepartmentDicMapper;
import com.semioe.dubbo.service.DepartmentDicService;

@Service
public class DepartmentDicServiceImpl implements DepartmentDicService {

	@Autowired
	private DepartmentDicMapper departmentDicMapper;
	
	
	/**
	 * 添加
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result insert(DepartmentDic departmentDic) {
		Result result = new Result<>(StatusCodes.OK, true);
		int row = departmentDicMapper.insert(departmentDic);
		if (row == 1) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FAIL", "FAIL"));
		}
		return result;
	}

	/**
	 * 获取部门列表
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result getAllDept(int in_use) {
		Result result = new Result<>(StatusCodes.OK, true);
		DepartmentDicExample departmentDicExample = new DepartmentDicExample();
		departmentDicExample.or().andInUseEqualTo(in_use);
		List<DepartmentDic> departmentDics = departmentDicMapper.selectByExample(departmentDicExample);
		result.setData(departmentDics);
		result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		return result;
	}

	/**
	 * 修改部门
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result uodateById(DepartmentDic departmentDic, DepartmentDicExample departmentDicExample) {
		Result result = new Result<>(StatusCodes.OK, true);
		int row = departmentDicMapper.updateByExample(departmentDic, departmentDicExample);
		if (row == 1) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FAIL", "FAIL"));
		}
		return result;
	}

}
