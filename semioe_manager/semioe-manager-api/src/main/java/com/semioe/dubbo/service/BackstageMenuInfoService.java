package com.semioe.dubbo.service;

import com.semioe.api.entity.BackstageMenuInfo;
import com.semioe.common.result.Result;
import java.util.List;

public interface BackstageMenuInfoService {

	/**
	 * getBackstageMenuInfoByName:根据角色查询对应菜单信息. <br/>
	 * 
	 * @param roleId
	 * @return List<BackstageMenuInfo>
	 * @author lyc
	 */
	Result getBackstageMenuInfoByRoleId(String roleId);

	/**
	 * getBackstageMenuInfoByName:根据菜单描述查询对应菜单信息. <br/>
	 * 
	 * @param name
	 * @return BackstageMenuInfo
	 * @author lyc
	 */
	BackstageMenuInfo getBackstageMenuInfoByName(String name);

	/**
	 * getBackstageMenuInfoByNameAndParent:根据菜单+父节点查询对应菜单信息. <br/>
	 * 
	 * @param name
	 * @param parentId
	 * @return BackstageMenuInfo
	 * @author lyc
	 */
	BackstageMenuInfo getBackstageMenuInfoByNameAndParent(String name, String parentId);

	/**
	 * backstageMenuCreate:添加菜单. <br/>
	 * 
	 * @param name
	 * @param url
	 * @param parentId
	 * @return Result
	 * @author lyc
	 */
	Result backstageMenuCreate(String name, String url, int parentId);

	/**
	 * backstageMenuUpdate:菜单更新 <br/>
	 * 
	 * @param info
	 * @return Result
	 * @author lyc
	 */
	Result backstageMenuUpdate(String name, String url, int parentId, int id);

	/**
	 * backstageMenuRemove:菜单删除
	 * 
	 * @param info
	 * @return Result
	 * @author lyc
	 */
	Result backstageMenuRemove(String name, int id);

	/**
	 * backstageRoleMenuRelCreate:角色和菜单关系绑定
	 * 
	 * @param menuId
	 * @param roleId
	 * @return Result
	 * @author lyc
	 */

	Result backstageRoleMenuRelCreate(List<Object> roleId, String menuId);

	/**
	 * backstageRoleMenuRelModify 修改角色 菜单关系
	 * 
	 * @param id
	 * @param menuId
	 * @param roleId
	 * @return Result
	 */

	Result backstageRoleMenuRelModify(String id, String menuId, String roleId);

	/**
	 * backstageRoleMenuRelQuery 查询角色菜单关系
	 * 
	 * @param menuId
	 * @param roleId
	 * @return
	 */
	Result backstageRoleMenuRelQuery(String menuId, String roleId);

	/**
	 * backstageRoleMenuRelRemove 删除角色菜单关系
	 * 
	 * @param id
	 * @return
	 */
	Result backstageRoleMenuRelRemove(String id);

}