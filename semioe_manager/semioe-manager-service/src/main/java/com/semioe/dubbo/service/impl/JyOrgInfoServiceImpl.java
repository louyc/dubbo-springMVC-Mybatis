package com.semioe.dubbo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.JyOrgInfo;
import com.semioe.api.vo.JyOrgInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.dubbo.dao.JyOrgDocRelMapper;
import com.semioe.dubbo.service.JyOrgInfoService;

@Service
public class JyOrgInfoServiceImpl implements JyOrgInfoService {

	@Autowired
	private JyOrgDocRelMapper jyOrgDocRelMapper;

	@Override
	public PaginatedResult<JyOrgInfoVO> getJyDoctorSignOrg(JyOrgInfo entity) {
		//创建返回对象
		PaginatedResult<JyOrgInfoVO> result = null;
		List<JyOrgInfoVO> orgInfoArray = new ArrayList<>();
		//分页查询数据
		if (0 < entity.getCurrentPage() && 0 < entity.getShowCount() ) {
			// 查询总页数
			Integer totalCount = (int) jyOrgDocRelMapper.countJyOrgInfo(entity);
			//计算分页
			int beginSize = (entity.getCurrentPage()-1) * entity.getShowCount();
			entity.setCurrentResult(beginSize);
			//查询列表
			orgInfoArray = jyOrgDocRelMapper.selectJyOrgInfo(entity);
			result = new PaginatedResult<JyOrgInfoVO>(orgInfoArray,
					entity.getCurrentPage(), entity.getShowCount(), totalCount);
		}else{
			orgInfoArray = jyOrgDocRelMapper.selectJyOrgInfo(entity);
			result = new PaginatedResult<>(orgInfoArray,-1, -1,orgInfoArray.size());
		}
		
		return result;
	}
	
	@Override
	public PaginatedResult<JyOrgInfoVO> getJyOrgInfoList(JyOrgInfo entity) {
		//创建返回对象
		PaginatedResult<JyOrgInfoVO> result = null;
		List<JyOrgInfoVO> orgInfoArray = new ArrayList<>();
		//分页查询数据
		if (0 < entity.getCurrentPage() && 0 < entity.getShowCount() ) {
			// 查询总页数
			Integer totalCount = (int) jyOrgDocRelMapper.countOrgInfoList(entity);
			//计算分页
			int beginSize = (entity.getCurrentPage()-1) * entity.getShowCount();
			entity.setCurrentResult(beginSize);
			//查询列表
			orgInfoArray = jyOrgDocRelMapper.selectOrgInfoList(entity);
			result = new PaginatedResult<JyOrgInfoVO>(orgInfoArray,
					entity.getCurrentPage(), entity.getShowCount(), totalCount);
		}else{
			orgInfoArray = jyOrgDocRelMapper.selectOrgInfoList(entity);
			result = new PaginatedResult<>(orgInfoArray,-1, -1,orgInfoArray.size());
		}
		
		return result;
	}
	
}
