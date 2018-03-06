package com.semioe.dubbo.service;

import com.semioe.api.entity.QrcodeRel;
import com.semioe.api.vo.ApiUserInfoVO;
import com.semioe.api.vo.OrderInfoVO;
import com.semioe.common.result.Result;

public interface QrcodeRelService {

	/** 添加关系数据 */
	Result<QrcodeRel> addQrcodeRel(QrcodeRel qrcodeRel);
	
	/** 用户首次注册，更新数据表 */
	Result<QrcodeRel> firstLoginUser(ApiUserInfoVO user);
	
	/** 用户首次下单，更新数据表 */
	Result<QrcodeRel> firstPayOrderUser(OrderInfoVO order);
}
