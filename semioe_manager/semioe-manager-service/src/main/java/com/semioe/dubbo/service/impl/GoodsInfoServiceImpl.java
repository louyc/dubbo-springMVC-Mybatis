package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.GoodsInfo;
import com.semioe.api.entity.KeywordsRel;
import com.semioe.api.vo.GoodsInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.GoodsInfoMapper;
import com.semioe.dubbo.dao.KeywordsRelMapper;
import com.semioe.dubbo.service.GoodsInfoService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商品服务接口实现类
 * 
 * @author puanl
 *
 */
@Service
public class GoodsInfoServiceImpl implements GoodsInfoService {

	private static final Logger logger = LoggerFactory.getLogger(GoodsInfoServiceImpl.class);

	@Autowired
	public GoodsInfoMapper goodsInfoMapper;

	@Autowired
	public KeywordsRelMapper keywordsRelMapper;

	@Override
	public PaginatedResult<GoodsInfoVO> getGoodsInfoArray(GoodsInfo goodsInfo) {
		// 创建返回对象
		PaginatedResult<GoodsInfoVO> result = null;
		List<GoodsInfoVO> selectList = new ArrayList<>();
		// 分页查询数据
		if (0 < goodsInfo.getCurrentPage() && 0 < goodsInfo.getShowCount() ) {
			// 查询总页数
			Integer totalCount = (int) goodsInfoMapper.countByEntity(goodsInfo);
			//计算分页
			int beginSize = (goodsInfo.getCurrentPage() - 1) * goodsInfo.getShowCount();
			goodsInfo.setCurrentResult(beginSize);
			//查询数据
			selectList = goodsInfoMapper.selectByEntity(goodsInfo);
			result = new PaginatedResult<>(selectList, goodsInfo.getCurrentPage(),
					goodsInfo.getShowCount(), totalCount);
		} else {
			selectList = goodsInfoMapper.selectByEntity(goodsInfo);
			result = new PaginatedResult<>(selectList, -1, -1, selectList.size());
		}

		return result;
	}

	@Override
	public PaginatedResult<GoodsInfoVO> getGoodsInfoByKeywords(GoodsInfo goodsInfo){
		// 创建返回对象
		PaginatedResult<GoodsInfoVO> result = null;
		List<GoodsInfoVO> selectList = new ArrayList<>();
		// 分页查询数据
		if (0 < goodsInfo.getCurrentPage() && 0 < goodsInfo.getShowCount() ) {
			// 查询总页数
			Integer totalCount = (int) goodsInfoMapper.countByKeywords(goodsInfo);
			//计算分页
			int beginSize = (goodsInfo.getCurrentPage() - 1) * goodsInfo.getShowCount();
			goodsInfo.setCurrentResult(beginSize);
			//查询数据
			selectList = goodsInfoMapper.selectByKeywords(goodsInfo);
			result = new PaginatedResult<>(selectList, goodsInfo.getCurrentPage(),
					goodsInfo.getShowCount(), totalCount);
		} else {
			selectList = goodsInfoMapper.selectByKeywords(goodsInfo);
			result = new PaginatedResult<>(selectList, -1, -1, selectList.size());
		}

		return result;
	}
	
	@Override
	public Result addGoodsInfo(GoodsInfo goodsInfo) {
		Result result = new Result<>(StatusCodes.OK, true);
		// 新增商品数据
		goodsInfo.setCreateTime(new Date());
		List<GoodsInfoVO> listGoods = new ArrayList<GoodsInfoVO>();
		listGoods = goodsInfoMapper.selectByName(goodsInfo);
		if (listGoods.size() > 0) {
			result.setSuccessful(false);
			result.setResultCode(new ResultCode("NOT_UNIQUE", "已有相同商品名称数据，请修改商品名称"));
			return result;
		}
		goodsInfoMapper.insertSelective(goodsInfo);
		// 添加标签
		if (goodsInfo.getKeywordsIds() != null && !"".equals(goodsInfo.getKeywordsIds())) {
			List<KeywordsRel> rels = new ArrayList<KeywordsRel>();
			String[] keywords = goodsInfo.getKeywordsIds().split(","); // 拆分标签id
			for (String words : keywords) {
				KeywordsRel item = new KeywordsRel();
				item.setRelationId(goodsInfo.getId()); // 关联id
				item.setKeywordsId(Integer.parseInt(words)); // 标签id
				item.setType(0); // 添加类型，商品
				// 添加到集合
				rels.add(item);
			}
			// 批量添加关系表
			Integer num = keywordsRelMapper.insertList(rels);

			if (1 >= num) {
				result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
			} else {
				result.setResultCode(new ResultCode("FAILED", "FAILED"));
			}
		}
		return result;
	}

	
	@Override
	public Result updateGoodsInfo(GoodsInfo goodsInfo) {
		Result result = new Result<>(StatusCodes.OK, true);
		// 修改商品数据
		goodsInfo.setUpdateTime(new Date());
		goodsInfoMapper.updateByPrimaryKeySelective(goodsInfo);
		// 添加
		if (goodsInfo.getKeywordsIds() != null) {
			// 删除关系表数据
			keywordsRelMapper.deleteByRelationId(goodsInfo.getId(),0);
			// 保存标签
			if (!"".equals(goodsInfo.getKeywordsIds())) {
				List<KeywordsRel> rels = new ArrayList<KeywordsRel>();
				String[] keywords = goodsInfo.getKeywordsIds().split(","); // 拆分标签id
				for (String words : keywords) {
					KeywordsRel item = new KeywordsRel();
					item.setRelationId(goodsInfo.getId()); // 关联id
					item.setKeywordsId(Integer.parseInt(words)); // 标签id
					item.setType(0); // 添加类型，商品
					// 添加到集合
					rels.add(item);
				}
				// 批量添加关系表
				Integer num = keywordsRelMapper.insertList(rels);

				if (1 >= num) {
					result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
				} else {
					result.setResultCode(new ResultCode("FAILED", "FAILED"));
				}
			}
		}
		return result;
	}

	@Override
	public Result deleteGoodsInfo(Integer id) {
		Result result = new Result<>(StatusCodes.OK, true);
		// 查询数据
		GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(id);
		goodsInfo.setInUse(0);
		goodsInfo.setUpdateTime(new Date());
		// 修改数据
		Integer num = goodsInfoMapper.updateByPrimaryKeySelective(goodsInfo);

		if (1 == num) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "FAILED"));
		}
		return result;
	}

	@Override
	public Result recoverDeviceType(Integer id) {
		Result result = new Result<>(StatusCodes.OK, true);
		// 查询数据
		GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(id);
		goodsInfo.setInUse(1);
		goodsInfo.setUpdateTime(new Date());
		// 修改数据
		Integer num = goodsInfoMapper.updateByPrimaryKeySelective(goodsInfo);

		if (1 == num) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "FAILED"));
		}
		return result;
	}

	@Override
	public Result getGoodsInfoById(Integer id) {
		Result result = new Result<>(StatusCodes.OK, true);
		// 查询数据
		GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(id);
		// 返回结果
		if (goodsInfo != null) {
			result.setData(goodsInfo);
			result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "未查询到相应数据"));
		}

		return result;
	}

}
