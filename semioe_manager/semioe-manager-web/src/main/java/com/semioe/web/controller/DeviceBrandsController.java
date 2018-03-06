package com.semioe.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.DeviceBrands;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.DeviceBrandsService;
import com.semioe.dubbo.service.DeviceFirmsService;

@Controller
@RequestMapping("/deviceBrands")
public class DeviceBrandsController {

	@Reference
	private DeviceBrandsService deviceBrandsService;
	@Reference
	private DeviceFirmsService deviceFirmsService;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/selectDeviceBrandsListPage")
	@ResponseBody
	public Result selectDeviceBrandsListPage(@RequestParam(value = "brandName", required = false) String brandName, 
			@RequestParam(value = "firmName", required = false) String firmName, 
			@RequestParam(value = "firmId", required = false) Integer firmId,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "currentsPage", required = false) String currentsPage) {
		
		DeviceBrands deviceBrands = new DeviceBrands();
		
		if (pageSize != null && !pageSize.isEmpty()) {
			deviceBrands.setShowCount(Integer.parseInt(pageSize));
		}
		
		if (currentsPage != null && !currentsPage.isEmpty()) {
			
			int currentPage = Integer.parseInt(currentsPage);
			
			if (currentPage <= 0){
				currentPage = 1;
			}
			int currentResult = (currentPage-1) * Integer.parseInt(pageSize);
			
			deviceBrands.setCurrentPage(currentPage);
			deviceBrands.setCurrentResult(currentResult);
		}
		
		if(brandName!=null){
			deviceBrands.setBrandName(brandName);
		}
		
		if(firmName != null){
			deviceBrands.setFirmName(firmName);
		}
		
		if(firmId != null){
			deviceBrands.setDeviceFirmid(firmId);
		}
		
		return deviceBrandsService.selectDeviceBrandsListPage(deviceBrands);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/insertDeviceBrand")
	@ResponseBody
	public Result insertDeviceBrand(@RequestBody DeviceBrands deviceBrands) {
		return deviceBrandsService.insertDeviceBrand(deviceBrands);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateDeviceBrand")
	@ResponseBody
	public Result updateDeviceBrand(@RequestBody DeviceBrands deviceBrands) {
		return deviceBrandsService.updateDeviceBrand(deviceBrands);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/deleteDeviceBrand")
	@ResponseBody
	public Result deleteDeviceBrand(@RequestParam("id") Integer id) {
		return deviceBrandsService.deleteDeviceBrand(id);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/recoverDeviceBrand")
	@ResponseBody
	public Result recoverDeviceBrand(@RequestParam("id") Integer id) {
		return deviceBrandsService.recoverDeviceBrand(id);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getDeviceBrandById")
	@ResponseBody
	public Result getDeviceBrandById(@RequestParam("id") Integer id) {
		return deviceBrandsService.getDeviceBrandById(id);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getDeviceBrands")
	@ResponseBody
	public Result getDeviceBrands() {
		return deviceBrandsService.getDeviceBrands();
	}
}