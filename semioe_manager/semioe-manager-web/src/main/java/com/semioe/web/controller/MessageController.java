package com.semioe.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.Message;
import com.semioe.api.entity.OrderInfo;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.service.BackstageUserInfoService;
import com.semioe.dubbo.service.MessageService;
import com.semioe.dubbo.service.OrderInfoService;
import com.semioe.dubbo.service.ServiceService;

@Controller
@RequestMapping("/message")
public class MessageController {

	@Reference
	private MessageService messageService;

	@RequestMapping("/create")
	@ResponseBody
	public Result createKeywords(@RequestBody Message message) {
		return messageService.createMessage(message);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Result deleteKeywords(@RequestParam("id") String id) {
		return messageService.deleteMessage(id);
	}

	@RequestMapping("/update")
	@ResponseBody
	public Result updateKeywords(@RequestBody Message message) {
		return messageService.updateMessage(message);
	}

	/**
	 * 发送医生消息
	 * @return
	 */
	@RequestMapping(value = "/sendDoctorMessage", method = RequestMethod.POST)
	@ResponseBody
	public Result sendDoctorMessage(@RequestParam("doctorId") String doctorId , 
			@RequestParam("toUserId") String toUserId , @RequestParam("context") String context){
		return messageService.sendDoctorMessage(doctorId , toUserId , context);
	}
	
	@RequestMapping(value = "/sendDoctorMessageList", method = RequestMethod.POST)
	@ResponseBody
	public Result sendDoctorMessageList(@RequestParam("doctorId") String doctorId , 
			@RequestParam("toUserIds") List<String> toUserIds , @RequestParam("context") String content ){
		return messageService.sendDoctorMessageList(doctorId,toUserIds,content);
	}
	
	/**
	 * 分页 样例 pageSize 分页大小 currentsPage 当前页数
	 * 
	 * selectMessageListPage
	 * 
	 * @param message
	 * @return
	 */
	@RequestMapping("/getPageMessages")
	@ResponseBody
	public Result getPageMessages(@RequestBody Message message) {
		return messageService.selectMessageListPage(message);
	}
	
	@Reference
	private BackstageUserInfoService backstageUserInfoService;  //用户
	
	@Reference
	private OrderInfoService orderInfoService;   //订单
	
	@Reference
	private ServiceService serviceService;   //服务
	
	/**
	 * 代办事项处理
	 * @return
	 */
	@RequestMapping("/scheduleCountMessages")
	@ResponseBody
	public Result scheduleCountMessages(){
		
		Result<Object> result = new Result<>(StatusCodes.OK, true);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 统计当天订单数量
		OrderInfo orderQuery = new OrderInfo();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		orderQuery.setStartDate(sdf.format(new Date()));
		orderQuery.setEndDate(sdf.format(new Date()));
		Result orderRE = orderInfoService.countTotalAmount(orderQuery);
		if(orderRE != null && orderRE.getData() != null){
			returnMap.put("orderCount", orderRE.getData());
		}else{
			returnMap.put("orderCount", 0);
		}
		
		// 查询待审批用户数量
		Map<String, Object> userQuery = new HashMap<String, Object>();
		userQuery.put("userStatus", 2);  //待审批
		userQuery.put("inUse", 1);       //可用
		//分页参数（必填）
		userQuery.put("pageSize", 2);
		userQuery.put("pageNumber", 1);
		userQuery.put("beginSize", 0);
		Result userRE = backstageUserInfoService.getAllBackstageUserInfo(userQuery);
		if(userRE.getData() != null ){
			returnMap.put("userCount", ((Map<String, Object>) userRE.getData()).get("infoCount"));
		}else{
			returnMap.put("userCount", 0);
		}
		
		// 查询待审批服务数量
		ServiceInfoVO serviceVO = new ServiceInfoVO();
		//待审核
		List<Integer> listStatus = new ArrayList<Integer>();
		listStatus.add(1);
		serviceVO.setStatusList(listStatus);  
		serviceVO.setInUse(1);         //可用
		serviceVO.setCurrentPage(-1);  //无分页
		PaginatedResult<ServiceInfoVO> serviceER = (PaginatedResult<ServiceInfoVO>) serviceService.getServiceInfoListPage(serviceVO);
		if(serviceER != null){
			returnMap.put("serviceCount", serviceER.getTotalItemsCount());
		}else{
			returnMap.put("serviceCount", 0);
		}

		result.setData(returnMap);
		
		return result;
	}
	
	/**
	 * 设置所有状态为已读
	 */
	@RequestMapping("/readOverMessage")
	@ResponseBody
	public Result readOverMessage(@RequestParam(value="messageId", required = false) String messageId ,
			@RequestParam(value="userId", required = true) String userId ,
			@RequestParam(value="messageFromId", required = false) String messageFromId , 
			@RequestParam(value="type", required = true) Integer type ) {
		
		Message message = new Message();
		message.setId(messageId);
		message.setMessageFrom(messageFromId);   // 消息来源
		message.setMessageTo(userId);            // 用户
		message.setType(type);                   // 消息类型 ： 2,站内信; 3,医生消息
			
		return messageService.readOverMessage(message);
	}
	
	/**
	 * 查询未读消息数量统计
	 * 
	 * 
	 */
	@RequestMapping("/countUnReadMessageList")
	@ResponseBody
	public PaginatedResult<Message> countUnReadMessageList(
			@RequestParam(value="showCount", required = true) Integer showCount ,
			@RequestParam(value="currentPage", required = true) Integer currentPage ,
			@RequestParam(value="userId", required = true) String userId ,
			@RequestParam(value="doctorId", required = false) String doctorId ) {
		
		Message message = new Message();
		message.setShowCount(showCount);
		message.setCurrentPage(currentPage);
		message.setMessageFrom(doctorId);   // 消息来源
		message.setMessageTo(userId);       // 用户
		message.setType(3);                 // 消息类型 ： 2,站内信; 3,医生消息
		
		return messageService.countUnReadMessageList(message);
	}
	
	/**
	 * 查询
	 */
	@RequestMapping("/countAllUnReadMessage")
	@ResponseBody
	public Result countAllUnReadMessage(
			@RequestParam(value="userId", required = true) String userId) {
		
		Message message = new Message();
		message.setMessageTo(userId);
		
		return messageService.countAllUnReadMessage(message);
	}
	
}