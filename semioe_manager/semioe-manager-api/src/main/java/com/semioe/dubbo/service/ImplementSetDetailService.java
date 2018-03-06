package com.semioe.dubbo.service;

import com.semioe.api.entity.ImplementSetDetail;
import com.semioe.api.vo.ImplementSetDetailVO;
import com.semioe.common.result.Result;

public interface ImplementSetDetailService {

	/**
	 * getImplementSetInfoByType:根据类型查询运行设置信息. <br/>
	 * 
	 * @param implementId
	 *            运营设置id
	 * @return Result
	 * @author lyc
	 */
	Result getImplementDetail(String implementId);

	/**
	 * 查询服务和健康管理
	 * 
	 * @return
	 */
	Result queryServiceAndImplement(ImplementSetDetailVO implementSet);

	/**
	 * getImplementDetail:查询内容推荐详情. <br/>
	 * 
	 * @param type
	 * @return Result
	 * @author lyc
	 */
	Result getImplementDetail(ImplementSetDetailVO implementSetDetail);

	/**
	 * implementDetailCreate 添加内容推荐详情
	 * 
	 * @param implementSet
	 * @return
	 */
	Result implementDetailCreate(ImplementSetDetail implementSet);

	/**
	 * implementDetailUpdate 更新内容推荐详情
	 * 
	 * @param implementSet
	 * @return
	 */
	Result implementDetailUpdate(ImplementSetDetail implementSet);

	/**
	 * implementDetailRemove 删除内容推荐详情
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	Result implementDetailRemove(String id);

}