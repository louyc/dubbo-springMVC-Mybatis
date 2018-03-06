package com.semioe.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.GoodsInfo;
import com.semioe.api.vo.GoodsInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.GoodsInfoService;

@Controller
@RequestMapping("/goodsInfo")
public class GoodsInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(GoodsInfoController.class);
	
	@Reference
	private GoodsInfoService goodsInfoService;

	@RequestMapping("/getGoodsInfoArray")
	@ResponseBody
	public PaginatedResult<GoodsInfoVO> getGoodsInfoArray(@RequestBody GoodsInfo goodsInfo){
		// log
		logger.info("GoodsInfoController.getGoodsInfoArray start, select");
		return goodsInfoService.getGoodsInfoArray(goodsInfo);
	}
	
	@RequestMapping("/addGoodsInfo")
	@ResponseBody
	public Result addGoodsInfo(@RequestBody GoodsInfo goodsInfo){
		// log
		logger.info("GoodsInfoController.addGoodsInfo start,goodsInfo = {}",goodsInfo.toString());
		return goodsInfoService.addGoodsInfo(goodsInfo);
	}
	
	@RequestMapping("/updateGoodsInfo")
	@ResponseBody
	public Result updateGoodsInfo(@RequestBody GoodsInfo goodsInfo){
		// log
		logger.info("GoodsInfoController.updateGoodsInfo start,goodsInfo = {}",goodsInfo.toString());
		return goodsInfoService.updateGoodsInfo(goodsInfo);
	}
	
	@RequestMapping("/deleteGoodsInfo")
	@ResponseBody
	public Result deleteGoodsInfo(@RequestParam("id") Integer id){
		// log
		logger.info("GoodsInfoController.deleteGoodsInfo start,id = {}",id);
		return goodsInfoService.deleteGoodsInfo(id);
	}
	
	@RequestMapping("/recoverDeviceType")
	@ResponseBody
	public Result recoverDeviceType(@RequestParam("id") Integer id){
		// log
		logger.info("GoodsInfoController.recoverDeviceType start,id = {}",id);
		return goodsInfoService.recoverDeviceType(id);
	}
	
	@RequestMapping("/getGoodsInfoById")
	@ResponseBody
	public Result getGoodsInfoById(@RequestParam("id") Integer id){
		// log
		logger.info("GoodsInfoController.getGoodsInfoById start,id = {}",id);
		return goodsInfoService.getGoodsInfoById(id);
	}
	
}
