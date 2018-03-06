package com.semioe.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.DeviceDefinition;
import com.semioe.api.vo.DeviceDefinitionVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.DeviceDefinitionService;

@Controller
@RequestMapping("/deviceDefinition")
public class DeviceDefinitionController {

	@Reference
	private DeviceDefinitionService deviceDefinitionService;
	
	@RequestMapping("/getDeviceDefinitionList")
	@ResponseBody
	public PaginatedResult<DeviceDefinitionVO> getDeviceDefinitionList(@RequestBody DeviceDefinition entity){
		return deviceDefinitionService.getDeviceDefinitionList(entity);
	}
	
	@RequestMapping("/addDeviceDefinition")
	@ResponseBody
	public Result<DeviceDefinition> addDeviceDefinition(@RequestBody DeviceDefinition entity){
		return deviceDefinitionService.addDeviceDefinition(entity);
	}
	
	@RequestMapping("/updateDeviceDefinition")
	@ResponseBody
	public Result<DeviceDefinition> updateDeviceDefinition(@RequestBody DeviceDefinition entity){
		return deviceDefinitionService.updateDeviceDefinition(entity);
	}
	
	@RequestMapping("/getDeviceDefinitionById")
	@ResponseBody
	public Result<DeviceDefinitionVO> getDeviceDefinitionById( @RequestParam("id") Integer id){
		return deviceDefinitionService.getDeviceDefinitionById(id);
	}
	
	@RequestMapping("/deleteDeviceDefinition")
	@ResponseBody
	public Result deleteDeviceDefinition( @RequestParam("id") Integer id){
		return deviceDefinitionService.deleteDeviceDefinition(id);
	}
	
}
