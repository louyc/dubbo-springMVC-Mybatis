package com.semioe.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.PersonalTailor;
import com.semioe.api.vo.PersonalTailorVO;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.PersonalTailorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/personalTailor")
public class PersonalTailorController {

	@Reference
	private PersonalTailorService personalTailorService;

	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryById")
	@ResponseBody
	public Result queryById(@RequestParam(value = "id", required = false) String id) {
		return personalTailorService.getPersonalTailor(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryByEntity")
	@ResponseBody
	public Result queryByEntity(@RequestBody PersonalTailorVO personalTailorVO) {
		return personalTailorService.getPersonalTailor(personalTailorVO);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/addPerson")
	@ResponseBody
	public Result addDetail(@RequestBody PersonalTailor personalTailor) {
		return personalTailorService.personalTailorCreate(personalTailor);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/updatePerson")
	@ResponseBody
	public Result updateDetail(@RequestBody PersonalTailor personalTailor) {
		return personalTailorService.personalTailorUpdate(personalTailor);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/removePerson")
	@ResponseBody
	public Result removeDetail(@RequestParam("id") String id) {
		return personalTailorService.personalTailorRemove(id);
	}

}