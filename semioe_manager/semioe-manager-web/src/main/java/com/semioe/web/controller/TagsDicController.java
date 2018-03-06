package com.semioe.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.TagsDic;
import com.semioe.api.entity.TagsDicExample;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.service.TagsDicService;

@Controller
@RequestMapping("/tagsdic")
public class TagsDicController {

	private static final Logger logger = LoggerFactory.getLogger(TagsDicController.class);

	@Reference
	private TagsDicService tagsDicService;

	/**
	 * 添加标签
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Result insert(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "name", required = true) String name) {
		logger.info("TagsDicController.insert parament:", name);
		TagsDic tagsDic = new TagsDic();
		tagsDic.setName(name);
		tagsDic.setCreateTime(new Date());
		Result result = tagsDicService.insert(tagsDic);
		return result;
	}

	/**
	 * 获取标签
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	@ResponseBody
	public Result getAllDept(HttpServletRequest request, HttpServletResponse response) {
		String in_use = request.getParameter("in_use");
		String name = request.getParameter("name");
		if (name == null) {
			name = "";
		}
		Result result = tagsDicService.getAllTags(in_use != null ? Integer.valueOf(in_use) : 1, name);
		return result;
	}

	/**
	 * 删除标签
	 * 
	 * @param request
	 * @param response
	 * @param tagId
	 * @return
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public Result remove(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "tagId") int tagId) {
		Result result = tagsDicService.remove(tagId);
		return result;
	}

	/**
	 * 修改标签
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "modify", method = RequestMethod.POST)
	@ResponseBody
	public Result modify(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "tagId") int tagId, @RequestParam(value = "name") String name) {
		Result result = tagsDicService.modify(tagId, name);
		return result;
	}
}
