package com.semioe.dubbo.service;

import com.semioe.api.entity.GoodsInfo;
import com.semioe.api.vo.GoodsInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;

public interface GoodsInfoService {

	/* 查询商品数据列表 */
	PaginatedResult<GoodsInfoVO> getGoodsInfoArray(GoodsInfo goodsInfo);
	
	/* 根据关键字查询数据列表 */
	PaginatedResult<GoodsInfoVO> getGoodsInfoByKeywords(GoodsInfo goodsInfo);
	
	/* 添加商品 */
	Result addGoodsInfo(GoodsInfo goodsInfo);
	
	/* 修改商品 */
	Result updateGoodsInfo(GoodsInfo goodsInfo);
	
	/* 根据id 删除商品 */
	Result deleteGoodsInfo(Integer id);
	
	/* 根据id 恢复商品 */
	Result recoverDeviceType(Integer id);
	
	/* 根据id 查询商品详情 */
	Result getGoodsInfoById(Integer id);
	
}
