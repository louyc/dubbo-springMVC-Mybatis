package com.semioe.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.ImplementSet;
import com.semioe.api.vo.ImplementSetVO;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.ImplementSetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/implement")
public class ImplementSetController {

	@Reference
	private ImplementSetService implementService;

	@SuppressWarnings("rawtypes")
	@RequestMapping("/query")
	@ResponseBody
	public Result getImplementSetInfoByType(
			@RequestParam(value = "type", required = false) String type) {
		if (type == null || type.isEmpty()) {
			type = "-1";
		}
		return implementService.getImplementSetInfoByType(type);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/add")
	@ResponseBody
	public Result insertImplementSetInfo(@RequestBody ImplementSet implementSet) {
		return implementService.implementSetInfoCreate(implementSet);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/update")
	@ResponseBody
	public Result updateImplementSetInfo(@RequestBody ImplementSet implementSet) {
		return implementService.implementSetInfoUpdate(implementSet);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/remove")
	@ResponseBody
	public Result deleteImplementSetInfo(@RequestParam("id") String id) {
		return implementService.implementSetInfoRemove(id);
	}

	// 20171031

	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryFind")
	@ResponseBody
	public Result getImplementFind(@RequestParam(value = "type", required = false) String type) {
		if (type == null || type.isEmpty()) {
			type = "-1";
		}
		return implementService.getImplementFind(type);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryDetailFind")
	@ResponseBody
	public Result queryFindDetailFind(@RequestBody ImplementSet implementSet) {
		return implementService.getImplementFindDetail(implementSet);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/addFind")
	@ResponseBody
	public Result addImplementFind(@RequestBody ImplementSet implementSet) {
		return implementService.addImplementFind(implementSet);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/addDetailFind")
	@ResponseBody
	public Result addImplementDetailFind(@RequestBody ImplementSetVO implementSet) {
		return implementService.addImplementDetailFind(implementSet);
	}

}