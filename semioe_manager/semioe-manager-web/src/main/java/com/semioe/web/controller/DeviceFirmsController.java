package com.semioe.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.DeviceFirms;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.DeviceFirmsService;

@Controller
@RequestMapping("/deviceFirms")
public class DeviceFirmsController {

	@Reference
	private DeviceFirmsService deviceFirmsService;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/selectDeviceFirmsListPage")
	@ResponseBody
	public Result selectDeviceFirmsListPage(@RequestParam("firmName") String firmName, @RequestParam(value = "pageSize", required = true) String pageSize,
			@RequestParam(value = "currentsPage", required = true) String currentsPage) {
		
		DeviceFirms deviceFirms = new DeviceFirms();
		
		if (pageSize != null && !pageSize.isEmpty()) {
			deviceFirms.setShowCount(Integer.parseInt(pageSize));
		}
		
		if (currentsPage != null && !currentsPage.isEmpty()) {
			int currentPage = Integer.parseInt(currentsPage);
			
			if (currentPage <= 0){
				currentPage = 1;
			}
			
			int currentResult = (currentPage-1) * Integer.parseInt(pageSize);
			
			deviceFirms.setCurrentPage(currentPage);
			deviceFirms.setCurrentResult(currentResult);
		}
		
		deviceFirms.setFirmName(firmName);
		
		return deviceFirmsService.selectDeviceFirmsListPage(deviceFirms);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/insertDeviceFirm")
	@ResponseBody
	public Result insertDeviceFirm(@RequestBody DeviceFirms deviceFirms) {
		return deviceFirmsService.insertDeviceFirm(deviceFirms);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateDeviceFirm")
	@ResponseBody
	public Result updateDeviceFirm(@RequestBody DeviceFirms deviceFirms) {
		return deviceFirmsService.updateDeviceFirm(deviceFirms);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/deleteDeviceFirm")
	@ResponseBody
	public Result deleteDeviceFirm(@RequestParam("id") Integer id) {
		return deviceFirmsService.deleteDeviceFirm(id);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/recoverDeviceFirm")
	@ResponseBody
	public Result recoverDeviceFirm(@RequestParam("id") Integer id) {
		return deviceFirmsService.recoverDeviceFirm(id);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getDeviceFirmById")
	@ResponseBody
	public Result getDeviceFirmById(@RequestParam("id") Integer id) {
		return deviceFirmsService.getDeviceFirmById(id);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getDeviceFirms")
	@ResponseBody
	public Result getDeviceFirms() {
		return deviceFirmsService.getDeviceFirms();
	}
}