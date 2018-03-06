package com.semioe.dubbo.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.Keywords;
import com.semioe.api.entity.KeywordsExample;
import com.semioe.api.entity.KeywordsRel;
import com.semioe.api.vo.RelationVO;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.KeywordsMapper;
import com.semioe.dubbo.dao.KeywordsRelMapper;
import com.semioe.dubbo.service.KeywordsService;

@Service
public class KeywordsServiceImpl implements KeywordsService{
	
	@Autowired
	private KeywordsMapper keywordsMapper;
	@Autowired
	private KeywordsRelMapper keywordsRelMapper;

	/**
	 * 创建标签
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result createKeywords(String name) {
		Result result = new Result<>(StatusCodes.OK, true);
		
		if("".equals(name) || null == name) {
			result.setResultCode(new ResultCode("FALSE", "参数为空！"));
			return result;
		}
		
		Keywords keywords = new Keywords();
		
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		keywords.setName(name.trim());
		keywords.setCreateTime(now);
		keywords.setUpdateTime(now);
		keywords.setInUse(1);
		
		//判断是否存在重复名称
		int count_num = keywordsMapper.getObjectCountByName(name.trim());
		
		if(count_num > 0) {
			result.setResultCode(new ResultCode("FALSE", "已经存在相同名称的标签！"));
			return result;
		}
		
		int num = keywordsMapper.insertSelective(keywords);
		
		if( num != 0 ) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
		}else {
			result.setResultCode(new ResultCode("FALSE", "添加失败！"));
		}
		return result;
	}
	
	/**
	 * 删除标签
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result deleteKeywords(Integer id) {
		Result result = new Result<>(StatusCodes.OK, true);
		
		if("".equals(id)) {
			result.setResultCode(new ResultCode("FALSE", "参数不正确！"));
			return result;
		}
		
		int num = keywordsMapper.deleteByPrimaryKey(id);
		
		if( num != 0 ) {
			result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
		}else {
			result.setResultCode(new ResultCode("FALSE", "删除失败！"));
		}
		return result;
	}

	/**
	 * 修改标签
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result updateKeywords(Keywords keywords) {
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(keywords == null ) {
			result.setResultCode(new ResultCode("FALSE", "参数为空！"));
			return result;
		}
		
		Keywords ke = keywordsMapper.getKeywordsBy(keywords);
		
		if(null != ke) {
			result.setResultCode(new ResultCode("FALSE", "已经存在相同名称的标签！"));
			return result;
		}
		
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		keywords.setUpdateTime(now);
		
		int num = keywordsMapper.updateByPrimaryKey(keywords);
		
		if( num != 0 ) {
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		}else {
			result.setResultCode(new ResultCode("FALSE", "修改失败！"));
		}
		return result;
	}

	/**
	 * 查询标签
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result selectKeywordsListPage(Keywords keywords) {
		List<Keywords> keywordsList = keywordsMapper.selectKeywordsListPage(keywords);
		
		//总页数
		int totalCount = keywords.getTotalResult();
		
		PaginatedResult<Keywords> pa = new PaginatedResult<Keywords>(keywordsList, keywords.getCurrentPage(), keywords.getShowCount(), totalCount);
		
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}
	
	/**
	 * 查询标签 For 流程
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getAllKeywordsForFlow() {
		Result result = new Result<>(StatusCodes.OK, true);
		
		KeywordsExample example = new KeywordsExample();
		example.createCriteria().andInUseEqualTo(1);
		
		List<Keywords> keywordsList = keywordsMapper.selectByExample(example);

		result.setData(keywordsList);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}
	/**
	 * 查询标签 根据类型
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getKeywordsByType(Keywords keywords) {
		Result result = new Result<>(StatusCodes.OK, true);
		
		List<Keywords> keywordsList = keywordsMapper.selectKeywordsByType(keywords);
		
		result.setData(keywordsList);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getRecommends(String keywords) {
		Result result = new Result<>(StatusCodes.OK, true);
		
		String[] keys = keywords.split(";");
		List<KeywordsRel> keywordsList = keywordsRelMapper.selectByKeywords(keys);
		result.setData(keywordsList);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		
		return result;
	}
	
	@Override
	public PaginatedResult<RelationVO> getRelationVoByKeywords(ServiceInfoVO serviceVO) {
		PaginatedResult<RelationVO> result = null;
		List<RelationVO> relationList = new ArrayList<>();
		// 分页查询数据
		if (0 < serviceVO.getCurrentPage() && 0 < serviceVO.getShowCount()) {
			// 查询总页数
			Integer totalCount = (int) keywordsMapper.countRelationByKeywords(serviceVO);
			// 计算分页
			int beginSize = (serviceVO.getCurrentPage() - 1) * serviceVO.getShowCount();
			serviceVO.setCurrentResult(beginSize);
			// 查询数据列表
			relationList = keywordsMapper.getRelationByKeywords(serviceVO);
			result = new PaginatedResult<>(relationList, serviceVO.getCurrentPage(),
					serviceVO.getShowCount(), totalCount);
		} else {
			relationList = keywordsMapper.getRelationByKeywords(serviceVO);
			result = new PaginatedResult<>(relationList, -1, -1, relationList.size());
		}
		return result;
	}
	
}