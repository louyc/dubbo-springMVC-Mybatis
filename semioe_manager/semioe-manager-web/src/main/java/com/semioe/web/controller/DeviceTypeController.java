package com.semioe.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.DeviceType;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.DeviceTypeService;

@Controller
@RequestMapping("/deviceType")
public class DeviceTypeController {

	@Reference
	private DeviceTypeService deviceTypeService;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getAllDeviceType")
	@ResponseBody
	public Result getAllDeviceType(@RequestBody DeviceType deviceType) {
		return deviceTypeService.getAllDeviceType(deviceType);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/insertDeviceType")
	@ResponseBody
	public Result insertDeviceType(@RequestBody DeviceType deviceType) {
		return deviceTypeService.insertDeviceType(deviceType);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateDeviceType")
	@ResponseBody
	public Result updateDeviceType(@RequestBody DeviceType deviceType) {
		return deviceTypeService.updateDeviceType(deviceType);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/deleteDeviceType")
	@ResponseBody
	public Result deleteDeviceType(@RequestParam("id") Integer id) {
		return deviceTypeService.deleteDeviceType(id);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/recoverDeviceType")
	@ResponseBody
	public Result recoverDeviceType(@RequestParam("id") Integer id) {
		return deviceTypeService.recoverDeviceType(id);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getDeviceTypeById")
	@ResponseBody
	public Result getDeviceTypeById(@RequestParam("id") Integer id) {
		return deviceTypeService.getDeviceTypeById(id);
	}
}