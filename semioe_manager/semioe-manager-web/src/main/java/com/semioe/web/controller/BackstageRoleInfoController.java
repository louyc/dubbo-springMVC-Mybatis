package com.semioe.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.BackstageRoleInfo;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.BackstageRoleInfoService;

@Controller
@RequestMapping("/role")
public class BackstageRoleInfoController {

	private static final Logger logger = LoggerFactory.getLogger(BackstageRoleInfoController.class);
	
	@Reference
	private BackstageRoleInfoService backstageRoleInfoService;
	
	/**
	 * 获取角色管理列表（all）
	 * @param roleInfo
	 * @return
	 */
	@RequestMapping("/getRoleInfo")
	@ResponseBody
	public Result getRoleInfo(HttpServletRequest request, HttpServletResponse response){
		//loge
		
		return backstageRoleInfoService.getRoleInfoArray();
	}
	
	/**
	 * 添加新角色
	 * @param roleInfo
	 * @return
	 */
	@RequestMapping("/addRoleInfo")
	@ResponseBody
	public Result addRoleInfo(@RequestBody BackstageRoleInfo roleInfo){
		//loge
		
		return backstageRoleInfoService.addRoleInfo(roleInfo);
	}
	
	/**
	 * 修改角色
	 * @param roleInfo
	 * @return
	 */
	@RequestMapping("/updateRoleInfo")
	@ResponseBody
	public Result updateRoleInfo(@RequestBody BackstageRoleInfo roleInfo){
		//loge
		
		return backstageRoleInfoService.updateRoleInfo(roleInfo);
	}
	
	/**
	 * 删除角色（未实现）
	 * @param roleInfo
	 * @return
	 */
	@RequestMapping("/deleteRoleInfo")
	@ResponseBody
	public Result deleteRoleInfo(@RequestBody BackstageRoleInfo roleInfo){
		//loge
		
		return backstageRoleInfoService.deleteRoleInfo(roleInfo);
	}
	
	/**
	 * 根据id查询角色信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/getRoleInfoById")
	@ResponseBody
	public Result getRoleInfoById(@RequestParam("id") Integer id){
		System.out.println(id);
		return backstageRoleInfoService.getRoleInfoById(id);
	}
	
	/**
	 * 用户管理中获取角色列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getRoleInfoListByUserManage")
	@ResponseBody
	public Result selectListByUserManage(HttpServletRequest request, HttpServletResponse response){
		return backstageRoleInfoService.selectListByUserManage();
	}
}
