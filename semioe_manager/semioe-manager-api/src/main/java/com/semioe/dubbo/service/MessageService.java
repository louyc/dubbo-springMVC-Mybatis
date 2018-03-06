package com.semioe.dubbo.service;

import java.util.List;

import com.semioe.api.entity.Message;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;

public interface MessageService {

	/**
	 * 创建消息
	 * 
	 * @param message
	 * @return
	 */
	Result createMessage(Message message);
	
	/**
	 * 删除消息
	 * 
	 * @param id
	 * @return
	 */
	Result deleteMessage(String id);

	/**
	 * 修改消息
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	Result updateMessage(Message Message);

	/**
	 * 查询消息
	 * 
	 * @param name
	 * @return
	 */
	PaginatedResult<Message> selectMessageListPage(Message Message);
	
	/**
	 * 医生向用户发送单向消息接口
	 * @param doctorId
	 * @param toUserId
	 * @param content
	 * @return
	 */
	Result sendDoctorMessage(String doctorId , String toUserId , String content);
	
	/**
	 * 医生向用户批量发送单向消息接口
	 * @param doctorId
	 * @param toUserIds
	 * @param content
	 * @return
	 */
	Result sendDoctorMessageList(String doctorId , List<String> toUserIds , String content );
	
	/**
	 * 设置消息已读
	 * @param messageFromId
	 * @param userId
	 * @param type （预留）
	 * @return
	 */
	Result readOverMessage(Message message);
	
	/**
	 * 查询未读消息统计列表
	 * @param message
	 * @return
	 */
	PaginatedResult<Message> countUnReadMessageList(Message message);
	
	/**
	 * 查询所有未读消息统计
	 * @param message
	 * @return
	 */
	Result countAllUnReadMessage(Message message);
	
	
}