package com.semioe.dubbo.service;

import com.semioe.common.result.Result;

public interface SourceService {

	/**
	 * getSourceInfoListPage 查询素材信息
	 * 
	 * @param procedureSystemUrl
	 * @param type
	 * @param token
	 * @param pageNumber
	 * @return
	 */
	Result getSourceInfoListPage(String procedureSystemUrl, String type, String token,
			String pageNumber, String pageSize);

	/**
	 * sourceInfoRemove 删除素材信息
	 * 
	 * @param url
	 * @param token
	 * @param id
	 * @return
	 */

	Result sourceInfoRemove(String url, String token, String id);

}