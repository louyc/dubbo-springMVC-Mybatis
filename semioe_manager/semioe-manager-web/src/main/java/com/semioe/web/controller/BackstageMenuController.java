package com.semioe.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.BackstageMenuInfo;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.service.BackstageMenuInfoService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/menu")
public class BackstageMenuController {

	private static final Logger logger = LoggerFactory.getLogger(BackstageMenuController.class);

	@Reference
	private BackstageMenuInfoService backstageMenuInfoService;

	/**
	 * addMenu:菜单添加<br/>
	 * 
	 * @param request
	 * @param name
	 *            菜单描述
	 * @param url
	 *            菜单url
	 * @param parentId
	 *            父节点
	 * @param isParent
	 *            是否父节点 1：是 0 ：否
	 * @return Result
	 * @author lyc
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/add")
	@ResponseBody
	public Result addMenu(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("name") String name, @RequestParam("url") String url,
			@RequestParam("parentId") String parentId, @RequestParam("isParent") String isParent) {
		logger.info("BackstageMenuController.addMenu start,Parameter::",
				name + " " + url + " " + parentId + " " + isParent);
		BackstageMenuInfo menu = null;
		// 判断菜单是否已存在
		if (isParent.equals("1")) {
			menu = backstageMenuInfoService.getBackstageMenuInfoByName(name);
		} else {
			menu = backstageMenuInfoService.getBackstageMenuInfoByNameAndParent(name, parentId);
		}
		if (null == menu) {
			return backstageMenuInfoService.backstageMenuCreate(name, url,
					Integer.parseInt(parentId));
		} else {
			return new Result(StatusCodes.OK, false, new ResultCode("MENU_FAIL", "菜单已存在！"));
		}
	}

	/**
	 * modifyMenu:菜单修改<br/>
	 * 
	 * @param request
	 * @param name
	 * @param url
	 * @param parentId
	 * @param id
	 * @return Result
	 * @author lyc
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/modify")
	@ResponseBody
	public Result modifyMenu(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("name") String name, @RequestParam("url") String url,
			@RequestParam("parentId") String parentId, @RequestParam("id") String id) {
		logger.info("BackstageMenuController.modifyMenu start,Parameter::",
				name + " " + url + " " + parentId + " " + id);
		return backstageMenuInfoService.backstageMenuUpdate(name, url, Integer.parseInt(parentId),
				Integer.parseInt(id));
	}

	/**
	 * removeMenu:菜单删除<br/>
	 * 
	 * @param request
	 * @param name
	 * @param id
	 * @return Result
	 * @author lyc
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/remove")
	@ResponseBody
	public Result removeMenu(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam("id") String id) {
		logger.info("BackstageMenuController.removeMenu start,name={}", name);
		return backstageMenuInfoService.backstageMenuRemove(name, Integer.parseInt(id));
	}

	/**
	 * queryMenu:菜单查询<br/>
	 * 
	 * @param request
	 * @param roleId
	 * @return Result
	 * @author lyc
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/query")
	@ResponseBody
	public Result queryMenu(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "roleId", required = false) String roleId) {
		logger.info("BackstageMenuController.queryMenu start,roleId={}", roleId);
		Result res = backstageMenuInfoService.getBackstageMenuInfoByRoleId(roleId);
		return res;
	}

	/**
	 * addMenuRoleRel:菜单角色关系添加<br/>
	 * 
	 * @param request
	 * @param roleId
	 * @param menuId
	 * @return Result
	 * @author lyc
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/addrel")
	@ResponseBody
	public Result addMenuRoleRel(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("roleId") String roleId,
			@RequestParam("menuIdList") List<Object> menuIdList) {
		logger.info("BackstageMenuController.addMenuRoleRel start,roleId={}", roleId);
		return backstageMenuInfoService.backstageRoleMenuRelCreate(menuIdList, roleId);
	}

	/**
	 * queryMenuRoleRel:菜单角色关系查询<br/>
	 * 
	 * @param request
	 * @param roleId
	 * @param menuId
	 * @return Result
	 * @author lyc
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryrel")
	@ResponseBody
	public Result queryMenuRoleRel(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "roleId", required = false) String roleId,
			@RequestParam(value = "menuId", required = false) String menuId) {
		logger.info("BackstageMenuController.queryMenuRoleRel start,roleId={}", roleId);
		return backstageMenuInfoService.backstageRoleMenuRelQuery(menuId, roleId);
	}

}