package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.BackstageMenuInfo;
import com.semioe.api.entity.BackstageRoleMenuRel;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.BackstageMenuInfoMapper;
import com.semioe.dubbo.dao.BackstageRoleMenuRelMapper;
import com.semioe.dubbo.service.BackstageMenuInfoService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class BackstageMenuInfoserviceImpl implements BackstageMenuInfoService {

	private static final Logger logger = LoggerFactory
			.getLogger(BackstageMenuInfoserviceImpl.class);

	private static final int IN_USE = 1;
	private static final int NOT_USE = 0;
	public static ResultCode RoleMenuNotNull = new ResultCode("ROLE_MENU_NOT_NULL", "角色菜单关系已存在！");
	public static ResultCode RoleMenuIsNull = new ResultCode("ROLE_MENU_IS_NULL", "角色菜单关系不存在！");
	public static ResultCode RoleMenuDeleteFail = new ResultCode("ROLE_MENU_DELETE_FAIL",
			"角色菜单关系删除失败！");
	public static ResultCode RoleMenuUpdateFail = new ResultCode("ROLE_MENU_UPDATE_NULL",
			"角色菜单关系修改失败！");
	public static ResultCode ParamError = new ResultCode("PARAM_ERROR", "参数不能为空！");
	public static ResultCode MenuInUse = new ResultCode("MENU_IN_USE", "菜单已存在");
	public static ResultCode MenuAddFail = new ResultCode("MENU_ADD_FAIL", "菜单添加失败！");
	public static ResultCode MenuUpdateFail = new ResultCode("MENU_UPDATE_FAIL", "菜单修改失败！");
	public static ResultCode MenuDeleteFail = new ResultCode("MENU_DELETE_FAIL", "菜单删除失败！");

	@Autowired
	private BackstageMenuInfoMapper backstageMenuInfoMapper;
	@Autowired
	private BackstageRoleMenuRelMapper backstageRoleMenuRelMapper;

	@SuppressWarnings("rawtypes")
	@Override
	public Result getBackstageMenuInfoByRoleId(String roleId) {
		logger.info("根据角色查询对应菜单信息::" + roleId);
		Result result = new Result<>(StatusCodes.OK, true);
		// 查询所有二级菜单
		List<BackstageMenuInfo> listChildrenMenu = backstageMenuInfoMapper.selectChildren();
		// 查询所有一级菜单
		List<BackstageMenuInfo> listParentMenu = backstageMenuInfoMapper.selectAll();
		List<BackstageMenuInfo> listMenu = new ArrayList<BackstageMenuInfo>();
		if (!"undefined".equals(roleId) && StringUtils.isNotEmpty(roleId)) {
			// 查询角色下所有二级菜单
			List<BackstageRoleMenuRel> ListRel = backstageRoleMenuRelMapper
					.selectByRoleId(Integer.parseInt(roleId));
			// 标识角色选中的二级菜单
			for (BackstageRoleMenuRel menurel : ListRel) {
				for (BackstageMenuInfo menuVo : listChildrenMenu) {
					if (menurel.getMenuId() == menuVo.getId()) {
						menuVo.setType("1");
					} else {
						if (null != menuVo.getType() && !menuVo.getType().equals("1")) {
							menuVo.setType("0");
						}
					}
				}
			}
		} else {
			for (BackstageMenuInfo menuVo : listChildrenMenu) {
				menuVo.setType("0");
			}
		}
		// 二级菜单和一级菜单做关联
		for (BackstageMenuInfo menuParent : listParentMenu) {
			List<BackstageMenuInfo> meunchileList = new ArrayList<BackstageMenuInfo>();
			for (BackstageMenuInfo mi : listChildrenMenu) {
				if (mi.getParentId() == menuParent.getId()) {
					meunchileList.add(mi);
				}
			}
			menuParent.setChildren(meunchileList);
			listMenu.add(menuParent);
		}
		result.setResultCode(new ResultCode("SUCCESS", "查询菜单成功！"));
		result.setData(listMenu);
		return result;
	}

	@Override
	public BackstageMenuInfo getBackstageMenuInfoByName(String name) {
		// TODO Auto-generated method stub
		return backstageMenuInfoMapper.selectByName(name);
	}

	@Override
	public BackstageMenuInfo getBackstageMenuInfoByNameAndParent(String name, String parentId) {
		// TODO Auto-generated method stub
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("name", name);
		queryMap.put("parentId", parentId);
		return backstageMenuInfoMapper.selectByNameAndParent(queryMap);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result backstageMenuCreate(String name, String url, int parentId) {
		Result result = new Result<>(StatusCodes.OK, true);
		BackstageMenuInfo menuInfo = new BackstageMenuInfo();
		menuInfo.setName(name);
		menuInfo.setUrl(url);
		menuInfo.setParentId(parentId);
		menuInfo.setCreateTime(new Date());
		menuInfo.setInUse(IN_USE);
		int line = backstageMenuInfoMapper.insertSelective(menuInfo);
		if (line > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加菜单成功！"));
		} else {
			result = new Result(StatusCodes.OK, false, MenuAddFail);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result backstageMenuUpdate(String name, String url, int parentId, int id) {
		Result result = new Result<>(StatusCodes.OK, true);
		BackstageMenuInfo menuInfo = backstageMenuInfoMapper.selectByPrimaryKey(id);
		menuInfo.setId(id);
		menuInfo.setName(name);
		menuInfo.setParentId(parentId);
		menuInfo.setUrl(url);
		menuInfo.setUpdateTime(new Date());
		menuInfo.setInUse(IN_USE);
		int line = backstageMenuInfoMapper.updateByPrimaryKey(menuInfo);
		if (line > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "修改菜单成功！"));
			result.setData(menuInfo);
		} else {
			result = new Result(StatusCodes.OK, false, MenuUpdateFail);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result backstageMenuRemove(String name, int id) {
		Result result = new Result<>(StatusCodes.OK, true);
		BackstageMenuInfo info = new BackstageMenuInfo();
		info.setId(id);
		if (null != name && !name.isEmpty()) {
			info.setName(name);
		}
		info.setUpdateTime(new Date());
		info.setInUse(NOT_USE);
		int line = backstageMenuInfoMapper.updateByIdOrName(info);
		if (line > 0) {
			backstageRoleMenuRelMapper.deleteByMenuId(id);
			result.setResultCode(new ResultCode("SUCCESS", "菜单删除成功！"));
		} else {
			result = new Result(StatusCodes.OK, false, MenuDeleteFail);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result backstageRoleMenuRelCreate(List<Object> menuIdList, String roleId) {
		Result result = new Result<>(StatusCodes.OK, true);
		backstageRoleMenuRelMapper.deleteByRoleId(Integer.parseInt(roleId));
		if (null != menuIdList && menuIdList.size() > 0) {
			for (Object s : menuIdList) {
				BackstageRoleMenuRel ref = new BackstageRoleMenuRel();
				ref.setMenuId(Integer.parseInt(s.toString()));
				ref.setRoleId(Integer.parseInt(roleId));
				backstageRoleMenuRelMapper.insert(ref);
			}
		}
		result.setResultCode(new ResultCode("SUCCESS", "关系添加成功！"));
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result backstageRoleMenuRelModify(String id, String menuId, String roleId) {
		Result result = new Result<>(StatusCodes.OK, true);
		BackstageRoleMenuRel ref = backstageRoleMenuRelMapper
				.selectByPrimaryKey(Integer.parseInt(id));
		if (null != ref) {
			ref.setMenuId(Integer.parseInt(menuId));
			ref.setRoleId(Integer.parseInt(roleId));
			backstageRoleMenuRelMapper.updateByPrimaryKey(ref);
			result.setResultCode(new ResultCode("SUCCESS", "关系修改成功！"));
			result.setData(ref);
		} else {
			result = new Result(StatusCodes.OK, false, RoleMenuIsNull);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result backstageRoleMenuRelQuery(String menuId, String roleId) {
		Result result = new Result<>(StatusCodes.OK, true);
		Map<String, Object> queryMap = new HashMap<String, Object>();
		if (null != menuId && !menuId.isEmpty()) {
			queryMap.put("menuId", menuId);
		}
		if (null != roleId && !roleId.isEmpty()) {
			queryMap.put("roleId", roleId);
		}
		List<BackstageRoleMenuRel> listRef = backstageRoleMenuRelMapper
				.selectByRoleAndMenu(queryMap);
		// 查询父节点id
		for (BackstageRoleMenuRel rel : listRef) {
			BackstageMenuInfo menu = backstageMenuInfoMapper.selectByPrimaryKey(rel.getMenuId());
			rel.setParentId(menu.getParentId());
		}
		result.setResultCode(new ResultCode("SUCCESS", "关系查询成功！"));
		result.setData(listRef);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result backstageRoleMenuRelRemove(String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		int i = backstageRoleMenuRelMapper.deleteByPrimaryKey(Integer.parseInt(id));
		if (i > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "关系删除成功！"));
		} else {
			result = new Result(StatusCodes.OK, false, RoleMenuDeleteFail);
		}
		return result;
	}

}
