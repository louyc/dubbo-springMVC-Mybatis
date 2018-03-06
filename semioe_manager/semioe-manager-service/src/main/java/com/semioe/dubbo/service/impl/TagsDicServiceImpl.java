package com.semioe.dubbo.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.DepartmentDic;
import com.semioe.api.entity.TagsDic;
import com.semioe.api.entity.TagsDicExample;
import com.semioe.api.entity.TagsRelExample;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.TagsDicMapper;
import com.semioe.dubbo.dao.TagsRelMapper;
import com.semioe.dubbo.service.TagsDicService;

@Service
public class TagsDicServiceImpl implements TagsDicService {

	@Autowired
	private TagsDicMapper tagsDicMapper;

	@Autowired
	private TagsRelMapper tagsRelMapper;

	@Override
	public Result insert(TagsDic tagsDic) {
		Result result = new Result<>(StatusCodes.OK, true);
		int row = tagsDicMapper.insert(tagsDic);
		if (row == 1) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FAIL", "FAIL"));
		}
		return result;
	}

	@Override
	public Result getAllTags(int in_use, String name) {
		Result result = new Result<>(StatusCodes.OK, true);
		TagsDicExample tagsDicExample = new TagsDicExample();
		tagsDicExample.createCriteria().andInUseEqualTo(in_use).andNameLike(name + "%");
		List<TagsDic> departmentDics = tagsDicMapper.selectByExample(tagsDicExample);
		result.setData(departmentDics);
		result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		return result;
	}

	@Override
	public Result remove(int tagId) {
		Result result = new Result<>(StatusCodes.OK, true);
		TagsRelExample example = new TagsRelExample();
		example.createCriteria().andTagIdEqualTo(tagId);
		long count = tagsRelMapper.countByExample(example);
		if (count > 0) {
			result.setResultCode(new ResultCode("FAIL", "删除失败，该标签已被引用！"));
			return result;
		}
		TagsDic tagsDic = new TagsDic();
		tagsDic.setId(tagId);
		tagsDic.setInUse(0);
		int row = tagsDicMapper.updateByPrimaryKeySelective(tagsDic);
		if (row == 1) {
			result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
		} else {
			result.setResultCode(new ResultCode("FAIL", "删除失败！"));
		}
		return result;
	}

	/**
	 * 修改标签
	 */
	@Override
	public Result modify(int tagId, String name) {
		Result result = new Result<>(StatusCodes.OK, true);
		TagsRelExample example = new TagsRelExample();
		example.createCriteria().andTagIdEqualTo(tagId);
		long count = tagsRelMapper.countByExample(example);
		if (count > 0) {
			result.setResultCode(new ResultCode("FAIL", "修改失败，该标签已被引用！"));
			return result;
		}
		TagsDic tagsDic = new TagsDic();
		tagsDic.setId(tagId);
		tagsDic.setName(name);
		tagsDic.setUpdateTime(new Date());
		int row = tagsDicMapper.updateByPrimaryKeySelective(tagsDic);
		if (row == 1) {
			result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
		} else {
			result.setResultCode(new ResultCode("FAIL", "删除失败！"));
		}
		return result;
	}

}
