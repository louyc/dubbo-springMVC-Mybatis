package com.semioe.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.SourceService;

@Controller
@RequestMapping("/source")
public class SourceController {

	@Value("#{settings['external_system']}")
	private String external_system;

	@Reference
	private SourceService sourceService;

	@SuppressWarnings({ "rawtypes" })
	@RequestMapping("/query")
	@ResponseBody
	public Result getSourceInfoByType(@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "token", required = false) String token) {
		int currentPage = Integer.parseInt(pageNumber);
		if (currentPage <= 0) {
			currentPage = 1;
		}
		Result result = sourceService.getSourceInfoListPage(external_system, type, token,
				pageNumber, pageSize);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/delete")
	@ResponseBody
	public Result deleteServiceInfo(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "token", required = false) String token) {
		return sourceService.sourceInfoRemove(external_system, token, id);
	}

}