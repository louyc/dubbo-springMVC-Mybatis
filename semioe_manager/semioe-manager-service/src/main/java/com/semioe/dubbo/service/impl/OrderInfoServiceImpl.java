package com.semioe.dubbo.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.GoodsInfo;
import com.semioe.api.entity.Message;
import com.semioe.api.entity.OrderInfo;
import com.semioe.api.vo.GoodsInfoVO;
import com.semioe.api.vo.OrderCountVO;
import com.semioe.api.vo.OrderInfoVO;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.common.tools.util.UUIDUtil;
import com.semioe.dubbo.dao.ApiUserInfoMapper;
import com.semioe.dubbo.dao.GoodsInfoMapper;
import com.semioe.dubbo.dao.OrderInfoMapper;
import com.semioe.dubbo.dao.ServiceInfoMapper;
import com.semioe.dubbo.service.MessageService;
import com.semioe.dubbo.service.OrderInfoService;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

	private static final Logger logger = LoggerFactory.getLogger(OrderInfoServiceImpl.class);
	
	@Autowired
	public OrderInfoMapper orderInfoMapper;
	
	@Autowired
	public ServiceInfoMapper serviceInfoMapper;
	
	@Autowired
	public GoodsInfoMapper goodsInfoMapper;
	
	@Autowired
	public ApiUserInfoMapper apiUserInfoMapper;
	
	@Autowired
	public MessageService messageService;
	
	@Override
	public PaginatedResult<OrderInfoVO> getGoodsInfoArray(OrderInfo orderInfo) {
		//创建返回对象
		PaginatedResult<OrderInfoVO> result = null;
		List<OrderInfoVO> selectList = new ArrayList<>();
		//分页查询数据
		if (0 < orderInfo.getCurrentPage() && 0 < orderInfo.getShowCount() ) {
			// 查询总页数
			Integer totalCount = (int) orderInfoMapper.countByEntity(orderInfo);
			//计算分页
			int beginSize = (orderInfo.getCurrentPage()-1) * orderInfo.getShowCount();
			orderInfo.setCurrentResult(beginSize);
			//查询列表
			selectList = orderInfoMapper.selectVoByEntity(orderInfo);
			result = new PaginatedResult<>(selectList,orderInfo.getCurrentPage(), orderInfo.getShowCount(),totalCount);
		}else{
			selectList = orderInfoMapper.selectVoByEntity(orderInfo);
			result = new PaginatedResult<>(selectList,-1, -1,selectList.size());
		}
		
		return result;
	}
	
	@Override
	public PaginatedResult<OrderInfoVO> getOrderReportCount(OrderInfo orderInfo) {
		//创建返回对象
		PaginatedResult<OrderInfoVO> result = null;
		List<OrderInfoVO> selectList = new ArrayList<>();
		//分页查询数据
		if (0 < orderInfo.getCurrentPage() && 0 < orderInfo.getShowCount() ) {
			// 查询总页数
			Integer totalCount = (int) orderInfoMapper.countByEntity(orderInfo);
			//计算分页
			int beginSize = (orderInfo.getCurrentPage()-1) * orderInfo.getShowCount();
			orderInfo.setCurrentResult(beginSize);
			//查询列表
			selectList = orderInfoMapper.selectVoReportCount(orderInfo);
			result = new PaginatedResult<>(selectList,orderInfo.getCurrentPage(), orderInfo.getShowCount(),totalCount);
		}else{
			selectList = orderInfoMapper.selectVoReportCount(orderInfo);
			result = new PaginatedResult<>(selectList,-1, -1,selectList.size());
		}
		return result;
	}
	
	@Override
	public List<OrderInfoVO> getAllGoodsInfoArray(OrderInfo orderInfo){
		return orderInfoMapper.selectVoByEntity(orderInfo);
	}
	
	/**
	 * 处理分享相关数据
	 * 
	 *  sourceType : 0   广告位
	 *	sourceType : 1  医生主页
	 *	sourceType : 2  推荐服务
	 *		sourceFromType: 1 , 流程编辑器推荐 
	 *			sourceOrderId:  推荐订单id
	 *		sourceFromType: 2 , 医生后台推荐服务
	 *			sourceUserId: 推荐人ID
	 *		sourceFromType: 3 , 用户分享推荐
	 *			sourceUserId: 推荐人ID
	 *			
	 * @param orderInfo
	 */
	private void handleSourceData(OrderInfo orderInfo){
		String sourceType = orderInfo.getSourceType();
		String sourceFromType = orderInfo.getSourceFromType();
		Integer sourceOrderId = orderInfo.getSourceOrderId();
		String sourceUserId = orderInfo.getSourceUserId();
		
		logger.info("配置订单分享数据，参数： sourceType="+sourceType+" ,sourceFromType="+sourceFromType+
				" ,sourceOrderId="+sourceOrderId+", sourceUserId="+sourceUserId);
		
		// 根据不同类型，保存不同参数
		if("0".equals(sourceType)){  // 广告位
			// 订单提供者为，服务或者商品的创建者。 
			orderInfo.setBackManagerId(orderInfo.getRelationSupplierId());
		}else if("1".equals(sourceType)){  // 医生主页
			// 订单提供者为，服务或者商品的创建者。  推荐人为服务创建者
			orderInfo.setSourceUserId(orderInfo.getRelationSupplierId());  
			orderInfo.setBackManagerId(orderInfo.getRelationSupplierId());
		}else if("2".equals(sourceType)){
			switch (sourceFromType) {
			case "1":  // 流程编辑器推荐 
				// 根据订单ID查询推荐订单
				OrderInfo sourceOrder = orderInfoMapper.selectByPrimaryKey(sourceOrderId);
				logger.info("流程编辑器分享，获取分享订单：sourceOrder {}",sourceOrder.toString());
				// 订单推荐人为，推荐订单提供者
				if(sourceOrder != null){
					orderInfo.setSourceUserId(sourceOrder.getBackManagerId());
				}
				// 订单提供者为，服务或者商品的创建者。 
				orderInfo.setBackManagerId(orderInfo.getRelationSupplierId());
				break;
			case "2":  // 医生后台推荐服务
				logger.info("医生后台推荐服务，订单提供者：sourceUserId {}",sourceUserId);
				// 订单提供者为，推荐服务的医生
				orderInfo.setBackManagerId(sourceUserId);
				break;
			case "3":  // 用户分享推荐
				logger.info(" 用户分享推荐服务，订单提供者：sourceUserId {}",sourceUserId);
				// 订单提供者为，服务或者商品的创建者。 
				orderInfo.setBackManagerId(orderInfo.getRelationSupplierId());
				break;
			default:
				orderInfo.setBackManagerId(orderInfo.getRelationSupplierId());
				break;
			}
		}else{
			orderInfo.setBackManagerId(orderInfo.getRelationSupplierId());
		}
		
	}
	
	@Override
	public Result<OrderInfo> addOrderInfo(OrderInfo orderInfo) {
		Result<OrderInfo> result = new Result<>(StatusCodes.OK, true);
		
		logger.info("OrderInfoServiceImpl.addOrderInfo , start");
		
		if( null == orderInfo || StringUtil.isEmpty(orderInfo.getType())){
			logger.error("订单保存失败！ orderInfo == null");
			result.setResultCode(new ResultCode("fell", "订单保存失败！ orderInfo == null"));
			return result;
		}
		
		GoodsInfoVO goodsInfo = null;
		ServiceInfoVO serviceInfo = null;
		
		/* 
		 * 1.根据类型判断购买服务，或购买商品
		 * 2.验证购买数量，判断是否可以购买，查看商品库存数量是否大于等于购买数量。
		 * 3.修改商品库存数量 
		 */
		switch (orderInfo.getType()) {
		case "0":  //商品
			goodsInfo = goodsInfoMapper.selectVoByPrimaryKey(orderInfo.getRelationId());
			
			if(goodsInfo == null || goodsInfo.getInUse() == null || goodsInfo.getInUse() != 1){
				logger.error("商品查询失败！未查询到商品");
				result.setResultCode(new ResultCode("fell", "商品查询失败！未查询到商品"));
				return result;
			}
			
			if(goodsInfo.getType() == null || goodsInfo.getType() != 1 ){
				logger.error("未上架商品，不能购买！");
				result.setResultCode(new ResultCode("fell", "未上架商品，不能购买！"));
				return result;
			}
			
			//验证购买数量
			if( goodsInfo.getStock() >= orderInfo.getOrderCount() ){
				//修改库存
				GoodsInfo updateGoods = new GoodsInfo();
				updateGoods.setId(goodsInfo.getId());
				updateGoods.setUpdateTime(new Date());
				updateGoods.setStock(goodsInfo.getStock() - orderInfo.getOrderCount() );  
				goodsInfoMapper.updateByPrimaryKeySelective(updateGoods);
			}else{
				logger.error("订单保存失败！库存商品数量不足");
				result.setResultCode(new ResultCode("fell", "订单保存失败！ 库存商品数量不足"));
				return result;
			}
			break;
		case "1":  //服务
			serviceInfo = serviceInfoMapper.selectVOByPrimaryKey(orderInfo.getRelationId());
			
			if(serviceInfo == null || serviceInfo.getInUse() == null || serviceInfo.getInUse() != 1){
				logger.error("服务查询失败！未查询到服务");
				result.setResultCode(new ResultCode("fell", "服务查询失败！未查询到服务"));
				return result;
			}
			
			if(serviceInfo.getStatus() == null || serviceInfo.getStatus() != 2 ){
				logger.error("服务未上架，服务购买失败");
				result.setResultCode(new ResultCode("fell", "服务未上架，服务购买失败！"));
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
			break;
		default:
			logger.error("订单保存失败！ orderInfo.type 不存在");
			result.setResultCode(new ResultCode("fell", "订单保存失败！ orderInfo.type 不存在"));
			return result;
		}
		/* 4.将商品或服务转换为订单 */
		if(goodsInfo != null ){ 
			//商品对象转换为订单对象
			orderInfo.setRelationName(goodsInfo.getGoodsName());  //商品名称
			orderInfo.setRelationSupplierId(goodsInfo.getCreateUser());  //商品提供人ID
			orderInfo.setRelationSupplier(goodsInfo.getSupplierName()); //商品供应商
			orderInfo.setRelationImgUrl(goodsInfo.getImgUrl()); //商品缩略图URL
			orderInfo.setRelationPrice(goodsInfo.getPrice());   //商品单价
            orderInfo.setRelationDesc(goodsInfo.getGoodsDesc());   //描述
		}else if(serviceInfo != null){ 
			//服务对象转换为订单对象
			orderInfo.setRelationName(serviceInfo.getName());  //服务名称
			if(orderInfo.getRelationSupplierId() == null || "".equals(orderInfo.getRelationSupplierId())){
				orderInfo.setRelationSupplierId(serviceInfo.getCreateUserId());  //服务提供人ID
			}
			orderInfo.setRelationImgUrl(serviceInfo.getImgUrl());            // 服务的图片
			orderInfo.setRelationSupplier(serviceInfo.getCreateName());      // 服务提供人名称
			orderInfo.setRelationCode(serviceInfo.getProcedureId());         // 流程id
			orderInfo.setRelationCodeName(serviceInfo.getProcedureName());   // 流程名称
			orderInfo.setRelationPrice(serviceInfo.getPrice());   //服务单价
            orderInfo.setRelationDesc(serviceInfo.getDescription());   //描述
		}
		
		//  计算订单总价
		DecimalFormat df = new DecimalFormat("######0.00");
		double orderprice = Double.valueOf(df.format((orderInfo.getRelationPrice() * orderInfo.getOrderCount())));
		orderInfo.setPrice(orderprice);           
		/** 公共属性添加 */
		orderInfo.setOrderCode(UUIDUtil.uuid());             //生成订单编号
		orderInfo.setCreateTime(new Date());                 //创建时间
		
		// 设置超时时间
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.HOUR_OF_DAY, 6);   //6小时过期 （生产使用）
//		ca.add(Calendar.MINUTE, 5);   //5分钟过期 （测试使用）
		orderInfo.setOutTime(ca.getTime());    
		
		// 处理分享数据
		handleSourceData(orderInfo);
		
		/** 保存数据 */
		orderInfoMapper.insertSelective(orderInfo);
		//返回保存结果
		if(orderInfo != null && orderInfo.getId() != null){
			result.setData(orderInfo);
			result.setResultCode(new ResultCode("success", "订单生成成功！"));
		}else{
			result.setResultCode(new ResultCode("fell", "订单生成失败！"));
		}
		return result;
	}
	
	@Override
	public Result<OrderInfoVO> repeatPayOrder(String orderCode){
		Result<OrderInfoVO> result = new Result<>(StatusCodes.OK, true);
		//重新支付订单接口
		OrderInfoVO orderVo = orderInfoMapper.selectVoByOrderCode(orderCode);
		if(orderVo != null && orderVo.getPayStatus() == 0){  //支付状态必须为，待支付
			OrderInfo updateOrder = new OrderInfo();
			updateOrder.setId(orderVo.getId()); 
			updateOrder.setOrderCode(UUIDUtil.uuid());  //订单编号
			updateOrder.setUpdateTime(new Date());      //修改时间
			Integer num = orderInfoMapper.updateByPrimaryKeySelective(updateOrder); //修改订单
			if(1 <= num){
				orderVo.setOrderCode(updateOrder.getOrderCode());
				result.setData(orderVo);
				result.setResultCode(new ResultCode("SUCCESS", "订单修改成功！"));
			}else{
				result.setResultCode(new ResultCode("fell", "订单修改失败！"));
			}
		}else{
			result.setResultCode(new ResultCode("fell", "订单查询失败！"));
		}
		//返回结果
		return result;
	}
	
	@Override
	public Result<OrderInfo> payOrderCancal(OrderInfo orderInfo){
		Result<OrderInfo> result = new Result<>(StatusCodes.OK, true);
		
		logger.info("OrderInfoServiceImpl.payOrderCancal , start");
		//验证订单信息
		if( null == orderInfo || StringUtil.isEmpty(orderInfo.getType())){
			logger.error("订单取消失败！ orderInfo == null");
			result.setResultCode(new ResultCode("fell", "订单保存失败！ orderInfo == null"));
			return result;
		}
		//验证订单状态
		if( 0 != orderInfo.getPayStatus() ){
			logger.error("订单取消失败！ 订单状态值为：" + orderInfo.getPayStatus() );
			result.setResultCode(new ResultCode("fell", "订单取消失败！ 订单状态不为待支付"));
			return result;
		}
		//还原订单数量
		switch (orderInfo.getType()) {
		case "0":
			//查询商品
			GoodsInfoVO goodsInfo = goodsInfoMapper.selectVoByPrimaryKey(orderInfo.getRelationId());
			if(goodsInfo != null ){
				//修改库存
				GoodsInfo updateGoods = new GoodsInfo();
				updateGoods.setId(goodsInfo.getId());
				updateGoods.setUpdateTime(new Date());
				updateGoods.setStock(goodsInfo.getStock() + orderInfo.getOrderCount() );  //还原商品数量
				goodsInfoMapper.updateByPrimaryKeySelective(updateGoods);
			}else{
				logger.error("商品查询失败！未查询到商品");
				result.setResultCode(new ResultCode("fell", "商品查询失败！未查询到商品"));
				return result;
			}
			break;

		case "1":
			//查询服务
			ServiceInfoVO serviceInfo = serviceInfoMapper.selectVOByPrimaryKey(orderInfo.getRelationId());
			if(serviceInfo != null){
				//查询服务相关商品
				List<GoodsInfoVO> updateGoodsArray = goodsInfoMapper.selectByServiceId(serviceInfo.getId());  
				logger.info("查询服务相关商品，关联商品条数："+updateGoodsArray.size());
				//修改库存
				for(GoodsInfoVO goodsVO : updateGoodsArray){
					GoodsInfo updateGoods = new GoodsInfo();
					updateGoods.setId(goodsVO.getId());
					updateGoods.setUpdateTime(new Date());
					updateGoods.setStock(goodsVO.getStock() + orderInfo.getOrderCount() );  
					goodsInfoMapper.updateByPrimaryKeySelective(updateGoods);
				}
			}else{
				logger.error("服务查询失败！未查询到服务");
				result.setResultCode(new ResultCode("fell", "服务查询失败！未查询到服务"));
				return result;
			}
			break;
		}
		//修改订单状态
		OrderInfo updateOrder = new OrderInfo();
		updateOrder.setId(orderInfo.getId());
		updateOrder.setUpdateTime(new Date());
		updateOrder.setPayStatus(2);               //修改订单类型
		updateOrder.setInUse(0);                   //删除订单
		Integer num = orderInfoMapper.updateByPrimaryKeySelective(updateOrder);
		
		//返回结果
		if(1 >= num){
			result.setData(updateOrder);
			result.setResultCode(new ResultCode("success", "订单修改成功！"));
		}else{
			result.setResultCode(new ResultCode("fell", "订单修改失败！"));
		}
		return result;
	}
	
	
	@Override
	public Result updateOrderInfo(OrderInfo orderInfo) {
		Result result = new Result<>(StatusCodes.OK, true);
		orderInfo.setUpdateTime(new Date());
		Integer num = orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
		
		if(1 >= num){
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "FAILED"));
		}
		return result;
	}
	
	@Override
	public boolean updateOrder(OrderInfo orderInfo){
		orderInfo.setUpdateTime(new Date());
		Integer num = orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
		return 1 <= num;
	}

	@Override
	public Result deleteOrderInfo(Integer id){
		Result result = new Result<>(StatusCodes.OK, true);
		
		OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(id);
		orderInfo.setInUse(0);
		orderInfo.setUpdateTime(new Date());
		Integer num = orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
		
		if(1 <= num){
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "FAILED"));
		}
		return result;
	}
	
	@Override
	public Result<OrderInfoVO> getOrderInfoById(Integer id) {
		Result<OrderInfoVO> result = new Result<>(StatusCodes.OK, true);
		
		OrderInfoVO order = orderInfoMapper.selectVoByPrimaryKey(id);
		
		if(order != null){
			result.setData(order);
			result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "未查询到相应数据"));
		}
		
		return result;
	}
	
	@Override
	public Result<OrderInfoVO> getOrderDetailById(Integer id){
		Result<OrderInfoVO> result = new Result<>(StatusCodes.OK, true);
		//查询订单数据
		OrderInfoVO order = orderInfoMapper.selectVoByPrimaryKey(id);
		if(order != null){
			//查询关联用户信息
			order.setUserObject(apiUserInfoMapper.selectByPrimaryKey(order.getUserId()));
			if("0".equals(order.getType())){
				// TODO 查询商品信息，暂不实现
			}else if("1".equals(order.getType())){
				List<GoodsInfoVO> updateGoodsArray = goodsInfoMapper.selectByServiceId(order.getRelationId());  
				order.setGoodsList(updateGoodsArray);
			}
			result.setData(order);
			result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		}else{
			result.setResultCode(new ResultCode("FAILED", "未查询到相应数据"));
		}
		return result;
	}

	@Override
	public OrderInfoVO getOrderInfoByCode(String orderCode){
		// log
		return orderInfoMapper.selectVoByOrderCode(orderCode);
	}
	
	@Override
	public Result countTotalPrice(OrderInfo example) {
		Result result = new Result<>(StatusCodes.OK, true);
		
		Double count = orderInfoMapper.countTotalPriceByDate(example);
		
		result.setData(count);
		
		return result;
	}

	@Override
	public Result countPrice(OrderInfo example) {
		Result result = new Result<>(StatusCodes.OK, true);
		
		List<OrderCountVO> countArray = orderInfoMapper.countPriceByDate(example);
		
		result.setData(countArray);
		
		return result;
	}

	@Override
	public Result countTotalAmount(OrderInfo example) {
		Result result = new Result<>(StatusCodes.OK, true);
		
		Double count = orderInfoMapper.countTotalAmountByDate(example);
		
		result.setData(count);
		
		return result;
	}

	@Override
	public Result countAmount(OrderInfo example) {
		Result result = new Result<>(StatusCodes.OK, true);
		
		List<OrderCountVO> countArray = orderInfoMapper.countAmountByDate(example);
		
		result.setData(countArray);
		
		return result;
	}

	@Override
	public PaginatedResult<OrderCountVO> countOrderInfo(OrderInfo orderInfo) {
		//创建返回对象
		PaginatedResult<OrderCountVO> result = null;
		List<OrderCountVO> selectList = new ArrayList<>();
		
		//分页查询数据
		if (0 < orderInfo.getCurrentPage() && 0 < orderInfo.getShowCount() ) {
			// 查询总页数
			Integer totalCount = (int) orderInfoMapper.pageCountOrderInfoByEntity(orderInfo);
			//计算分页
			int beginSize = (orderInfo.getCurrentPage()-1) * orderInfo.getShowCount();
			orderInfo.setCurrentResult(beginSize);
			//查询数据
			selectList = orderInfoMapper.countOrderInfoByEntity(orderInfo);
			result = new PaginatedResult<>(selectList,orderInfo.getCurrentPage(), orderInfo.getShowCount(),totalCount);
		}else{
			selectList = orderInfoMapper.countOrderInfoByEntity(orderInfo);
			result = new PaginatedResult<>(selectList,-1, -1,selectList.size());
		}
		
		return result;
	}

	@Override
	public List<OrderInfo> getOutTimeOrders(){
		return orderInfoMapper.selectOutTimeOrders();
	}

	@Override
	public Result doctorRecService(String doctorId, List<String> userIds, List<Integer> serviceIds) {
		
		Result result = null;
		
		for(String userId : userIds){
			for(Integer serviceId : serviceIds){
				/*
				 * 1. 创建一条未支付订单
				 * 2. 给用户发送站内信，提示支付订单
				 */
				OrderInfo saveOrder = new OrderInfo();
				saveOrder.setType("1"); // 类型 :服务
				saveOrder.setUserId(userId); // 购买人ID
				saveOrder.setRelationId(serviceId); // 服务ID
				saveOrder.setOrderCount(1); // 购买数量
				saveOrder.setRelationSupplierId(doctorId);  // 服务提供医生ID
				
				/*
				    sourceType : 0 
					sourceType : 1
					sourceType : 2 , 推荐服务
						sourceFromType: 1 , 流程编辑器推荐 
							sourceOrderId:  推荐订单id
						sourceFromType: 2 , 医生后台推荐服务
							sourceUserId: 推荐人ID
						sourceFromType: 3 , 用户分享推荐
							sourceUserId: 推荐人ID
				*/
				saveOrder.setSourceType("2");  // 订单来源（推荐订单）
				saveOrder.setSourceFromType("2");
				saveOrder.setSourceUserId(doctorId);  //推荐人
				
				// 保存数据
				Result<OrderInfo> orderResult = addOrderInfo(saveOrder);
				
				if("success".equals(orderResult.getResultCode().getCode())){
					OrderInfo order = orderResult.getData();
					Message message = new Message();
					message.setMessageTo(userId);
					message.setType(2);  //类型为系统消息
					message.setStatus(0);  // 消息为未读消息
					message.setContent("您有一条未支付订单，请到我的订单中查看。 订单名称："+order.getRelationName());
					result = messageService.createMessage(message);
				}else{
					result = orderResult;
				}
			}
		}
		
		return result;
		
	}
	
}
