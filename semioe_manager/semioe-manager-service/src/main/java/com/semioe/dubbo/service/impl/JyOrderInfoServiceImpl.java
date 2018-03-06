package com.semioe.dubbo.service.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.GoodsInfo;
import com.semioe.api.entity.OrderInfo;
import com.semioe.api.vo.GoodsInfoVO;
import com.semioe.api.vo.OrderInfoVO;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.common.tools.util.UUIDUtil;
import com.semioe.dubbo.dao.GoodsInfoMapper;
import com.semioe.dubbo.dao.OrderInfoMapper;
import com.semioe.dubbo.dao.ServiceInfoMapper;
import com.semioe.dubbo.service.JyOrderInfoService;

@Service
public class JyOrderInfoServiceImpl implements JyOrderInfoService {

	private static final Logger logger = LoggerFactory.getLogger(JyOrderInfoServiceImpl.class);
	
	@Autowired
	public OrderInfoMapper orderInfoMapper;
	
	@Autowired
	public ServiceInfoMapper serviceInfoMapper;
	
	@Autowired
	public GoodsInfoMapper goodsInfoMapper;
	
	/*@Override
	public Result<OrderInfo> addJyOrderInfo(Integer serviceId, String doctorId , String userId) {
		Result<OrderInfo> result = new Result<>(StatusCodes.OK, true);
		
		logger.info("JyOrderInfoServiceImpl.addJyOrderInfo , start");
		//创建基础订单数据
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setRelationId(serviceId);    // 关联服务ID
		orderInfo.setType("3");                // 添加订单状态为：家庭医生服务
		orderInfo.setSourceType("3");          // 订单来源：家庭医生
		orderInfo.setSourceUserId(doctorId);   // 家庭医生ID
		orderInfo.setUserId(userId);           // 患者ID
		orderInfo.setOrderCount(1);            // 购买数量
		orderInfo.setPayStatus(1);             // 支付成功
		
		 
		 * 1.根据类型判断购买服务，或购买商品
		 * 2.验证购买数量，判断是否可以购买，查看商品库存数量是否大于等于购买数量。
		 * 3.修改商品库存数量 
		 
		ServiceInfoVO serviceInfo = serviceInfoMapper.selectVOByPrimaryKey(orderInfo.getRelationId());
		
		if(serviceInfo == null){
			logger.error("服务查询失败！未查询到服务");
			result.setResultCode(new ResultCode("fell", "服务查询失败！未查询到服务"));
			return result;
		}
		// 根据服务id查询商品列表
		List<GoodsInfoVO> updateGoodsArray = goodsInfoMapper.selectByServiceId(serviceInfo.getId());  
		
		logger.info("查询服务相关商品，关联商品条数："+updateGoodsArray.size());
		
		//验证购买数量
		for(GoodsInfoVO goodsVO : updateGoodsArray){
			if( goodsVO.getStock() < orderInfo.getOrderCount() ){
				logger.error("订单保存失败！库存商品数量不足");
				result.setResultCode(new ResultCode("fell", "订单保存失败！ 库存商品数量不足"));
				return result;
			}
		}
		//修改库存
		for(GoodsInfoVO goodsVO : updateGoodsArray){
			GoodsInfo updateGoods = new GoodsInfo();
			updateGoods.setId(goodsVO.getId());
			updateGoods.setUpdateTime(new Date());
			updateGoods.setStock(goodsVO.getStock() - orderInfo.getOrderCount() );  
			goodsInfoMapper.updateByPrimaryKeySelective(updateGoods);
		}
		
		 4.将商品或服务转换为订单 
		if(serviceInfo != null){ 
			//服务对象转换为订单对象
			orderInfo.setRelationName(serviceInfo.getName());  //服务名称
			orderInfo.setRelationSupplierId(serviceInfo.getCreateUserId());  //服务提供人ID
			orderInfo.setRelationSupplier(serviceInfo.getCreateName());      //服务提供人名称
			orderInfo.setRelationCode(serviceInfo.getProcedureId());         //流程id
			orderInfo.setRelationCodeName(serviceInfo.getProcedureName());   //流程名称
			orderInfo.setRelationPrice(serviceInfo.getPrice());   //服务单价
            orderInfo.setRelationDesc(serviceInfo.getDescription());   //描述
		}
		//  计算订单总价
		DecimalFormat df = new DecimalFormat("######0.00");
		double orderprice = Double.valueOf(df.format((orderInfo.getRelationPrice() * orderInfo.getOrderCount())));
		orderInfo.setPrice(orderprice);           
		*//** 公共属性添加 *//*
		orderInfo.setOrderCode(UUIDUtil.uuid());             //生成订单编号
		orderInfo.setCreateTime(new Date());                 //创建时间 
		
		*//** 保存数据 *//*
		orderInfoMapper.insertSelective(orderInfo);
		//返回保存结果
		if(orderInfo != null && orderInfo.getId() != null){
			result.setData(orderInfo);
			result.setResultCode(new ResultCode("success", "订单生成成功！"));
		}else{
			result.setResultCode(new ResultCode("fell", "订单生成失败！"));
		}
		return result;
	}*/

	@Override
	public Result<List<OrderInfoVO>> getJyValidUserOrder(Map<String, Object> query) {

		Result<List<OrderInfoVO>> result = new Result<>(StatusCodes.OK, true);
		
		List<OrderInfoVO> orderInfoList = orderInfoMapper.selectJyOrderInfo(query);
		
		result.setData(orderInfoList);
		
		return result;
	}
	
	@Override
	public Result<List<OrderInfoVO>> getBackValidUserOrder(Map<String, Object> query) {

		Result<List<OrderInfoVO>> result = new Result<>(StatusCodes.OK, true);
		
		List<OrderInfoVO> orderInfoList = orderInfoMapper.selectBackOrderInfo(query);
		
		result.setData(orderInfoList);
		
		return result;
	}

}
