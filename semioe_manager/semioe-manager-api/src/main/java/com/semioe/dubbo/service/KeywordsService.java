package com.semioe.dubbo.service;

import com.semioe.api.entity.Keywords;
import com.semioe.api.vo.RelationVO;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;

public interface KeywordsService {

	/**
	 * 创建标签
	 * 
	 * @param name
	 * @return
	 */
	Result createKeywords(String name);
	
	/**
	 * 删除标签
	 * 
	 * @param id
	 * @return
	 */
	Result deleteKeywords(Integer id);

	/**
	 * 修改标签
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	Result updateKeywords(Keywords keywords);

	/**
	 * 查询标签
	 * 
	 * @param name
	 * @return
	 */
	Result selectKeywordsListPage(Keywords keywords);
	
	/**
	 * getRecommends:根据标签获取推荐
	 * @param keywords
	 * @return Result
	 * @author hua
	*/
	Result getRecommends(String keywords);
	
	/**
	 * 查询标签 For 流程
	 * 
	 * @param name
	 * @return
	 */
	Result getAllKeywordsForFlow();
	/**
	 * 查询标签根据类型
	 * 
	 * @param name
	 * @return
	 */
	Result getKeywordsByType(Keywords keywords);
	
	/**
	 * 根据关键字 联合查询 商品&服务
	 * @param serviceVO
	 * @return
	 */
	PaginatedResult<RelationVO> getRelationVoByKeywords(ServiceInfoVO serviceVO);
}