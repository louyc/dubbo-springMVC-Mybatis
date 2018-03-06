package com.semioe.dubbo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.TagsRel;
import com.semioe.api.entity.TagsRelExample;
import com.semioe.dubbo.dao.TagsRelMapper;
import com.semioe.dubbo.service.TagsRelService;

@Service
public class TagsRelServiceImpl implements TagsRelService {

	private static final Logger logger = LoggerFactory.getLogger(TagsRelServiceImpl.class);

	@Autowired
	private TagsRelMapper tagsRelMapper;

	/**
	 * 批量新增标签
	 */
	@Override
	public int insertTags(String[] tagArr, String managerId) {
		if (tagArr.length == 0) {
			return 0;
		}
		List<TagsRel> tagsRelList = new ArrayList<TagsRel>();
		for (int i = 0, length = tagArr.length; i < length; i++) {
			TagsRel tagsRel = new TagsRel();
			tagsRel.setTagId(Integer.valueOf(tagArr[i]));
			tagsRel.setManagerId(managerId);
			tagsRel.setType(1);
			tagsRelList.add(tagsRel);
		}
		return tagsRelMapper.insertTagsRels(tagsRelList);
	}

	/**
	 * 删除标签关系
	 */
	@Override
	public int removeRel(String managerId) {
		TagsRelExample example = new TagsRelExample();
		example.createCriteria().andManagerIdEqualTo(managerId);
		return tagsRelMapper.deleteByExample(example);
	}

}
