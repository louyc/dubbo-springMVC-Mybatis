package com.semioe.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.Keywords;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.KeywordsService;

@Controller
@RequestMapping("/keywords")
public class KeywordsController {
	
	private static final Logger logger = LoggerFactory.getLogger(KeywordsController.class);

	@Reference
	private KeywordsService keywordsService;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/createKeywords")
	@ResponseBody
	public Result createKeywords(@RequestParam("name") String name) {
		return keywordsService.createKeywords(name);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/deleteKeywords")
	@ResponseBody
	public Result deleteKeywords(@RequestParam("id") Integer id) {
		return keywordsService.deleteKeywords(id);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateKeywords")
	@ResponseBody
	public Result updateKeywords(@RequestBody Keywords keywords) {
		return keywordsService.updateKeywords(keywords);
	}
	
	/**
	 * 分页 样例
	 * pageSize 分页大小  currentsPage 当前页数
	 * 
	 * selectKeywordsListPage  （分页方法必须以 *****ListPage  为准）
	 * 实体类需继承 PageInfo
	 * 
	 * @param name
	 * @param pageSize
	 * @param currentsPage
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getAllKeywords")
	@ResponseBody
	public Result getAllKeywords(@RequestParam("name") String name, @RequestParam(value = "pageSize") String pageSize,
			@RequestParam("currentsPage") String currentsPage) {
		
		int currentPage = Integer.parseInt(currentsPage);
		
		if (currentPage <= 0){
			currentPage = 1;
		}
		
		Keywords keywords = new Keywords();
		
		keywords.setShowCount(Integer.parseInt(pageSize));
		keywords.setCurrentPage(currentPage);
		
		keywords.setName(name);
		
		return keywordsService.selectKeywordsListPage(keywords);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getAllKeywordsForFlow")
	@ResponseBody
	public Result getAllKeywordsForFlow() {
		return keywordsService.getAllKeywordsForFlow();
	}
	/**
	 * 标签查询 根据类型
	 * @param keywords
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getKeywordsByType")
	@ResponseBody
	public Result getKeywordsByType(@RequestBody Keywords keywords) {
		return keywordsService.getKeywordsByType(keywords);
	}
	
	/**
	 * getRecommends:根据标签获取推荐. <br/> 
	 * @param keywords
	 * @return 
	 * Result
	 * @author hua
	*/
	@RequestMapping("/recommend")
	@ResponseBody
	public Result getRecommends(@RequestParam("keywords") String keywords) {
		return keywordsService.getRecommends(keywords);
	}
	
	
	/**
	 * 根据关键字查询所有商品服务
	 * @param currentPage
	 * @param showCount
	 * @param keywordsId
	 * @param keywordsName
	 * @param type
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/findServiceByKeywords")
	@ResponseBody
	public Result findServiceByKeywords(
			@RequestParam(value="currentPage", required = false) String currentPage,
			@RequestParam(value="showCount", required = false) String showCount,
			@RequestParam(value="keywordsId", required = false) String keywordsId,
			@RequestParam(value="keywordsName", required = false) String keywordsName) {
		
		logger.info("根据关键字查询，参数[ currentPage="+currentPage+" showCount="+showCount+" keywordsId="+keywordsId+" keywordsName="+keywordsName+"]");
		
		Result result = new Result<>(false);
		
		//查询服务
		ServiceInfoVO serviceVO = new ServiceInfoVO();
		if(!StringUtil.isEmpty(currentPage)){
			serviceVO.setCurrentPage(Integer.parseInt(currentPage));
		}
		if(!StringUtil.isEmpty(showCount)){
			serviceVO.setShowCount(Integer.parseInt(showCount));
		}
		if(!StringUtil.isEmpty(keywordsId)){
			serviceVO.setKeywordsIdList(keywordsId);
		}
		
		if(!StringUtil.isEmpty(keywordsName)){
			//判断关键字是否为多个的情况
			int i = keywordsName.indexOf(";");
			if(i >= 0){
				String[] names = keywordsName.split(";");
				List list = java.util.Arrays.asList(names);
				serviceVO.setKeywordsNameList(list);
			}else{
				serviceVO.setKeywordsName(keywordsName);
			}
		}
		result = keywordsService.getRelationVoByKeywords(serviceVO);
		
		return result;
	}
}