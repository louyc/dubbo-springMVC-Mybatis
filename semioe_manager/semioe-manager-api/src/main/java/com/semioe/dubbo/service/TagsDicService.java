package com.semioe.dubbo.service;

import com.semioe.api.entity.TagsDic;
import com.semioe.api.entity.TagsDicExample;
import com.semioe.common.result.Result;

public interface TagsDicService {

	/**
	 * 添加部门
	 * 
	 * @param tagsDic
	 * @return
	 */
	Result insert(TagsDic tagsDic);

	/**
	 * 查询标签
	 * 
	 * @param tagsDicExample
	 * @return
	 */
	Result getAllTags(int in_use, String name);

	/**
	 * 删除标签
	 * 
	 * @param tagId
	 * @return
	 */
	Result remove(int tagId);

	/**
	 * 修改标签
	 * 
	 * @param tagId
	 * @param name
	 * @return
	 */
	Result modify(int tagId, String name);

}
