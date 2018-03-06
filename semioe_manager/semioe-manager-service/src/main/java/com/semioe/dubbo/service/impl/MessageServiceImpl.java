package com.semioe.dubbo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.Message;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.MessageMapper;
import com.semioe.dubbo.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageMapper messageMapper;

	/**
	 * 创建消息
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result<?> createMessage(Message message) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (null == message) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		message.setId(UUID.randomUUID().toString());
		message.setCreateTime(new Date());
		message.setUpdateTime(new Date());
		int num = messageMapper.insertSelective(message);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "添加失败！"));
		}
		return result;
	}

	/**
	 * 删除消息
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Result<?> deleteMessage(String id) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (StringUtils.isEmpty(id)) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		int num = messageMapper.deleteByPrimaryKey(id);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "删除成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "删除失败！"));
		}
		return result;
	}

	/**
	 * 修改消息
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public Result<?> updateMessage(Message message) {
		Result<?> result = new Result<>(StatusCodes.OK, true);

		if (message == null) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}

		message.setUpdateTime(new Date());

		int num = messageMapper.updateByPrimaryKey(message);

		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "修改失败！"));
		}
		return result;
	}

	/**
	 * 查询消息
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public PaginatedResult<Message> selectMessageListPage(Message message) {

		List<Message> MessageList = messageMapper.selectMessageListPage(message);

		// 总页数
		int totalCount = message.getTotalResult();

		PaginatedResult<Message> pa = new PaginatedResult<Message>(MessageList,
				message.getCurrentPage(), message.getShowCount(), totalCount);

		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

	@Override
	public Result<?> sendDoctorMessage(String doctorId, String toUserId, String content) {
		// 创建发送消息
		Message message = new Message();
		message.setType(3); //医生消息类型
		message.setStatus(0);  //设置消息未读
		message.setMessageFrom(doctorId);
		message.setMessageTo(toUserId);
		message.setContent(content);
		// 保存消息
		return createMessage(message);
	}

	@Override
	public Result<?> sendDoctorMessageList(String doctorId, List<String> toUserIds, String content) {
		Result<?> result = new Result<>(StatusCodes.OK, true);
		
		int successCount = 0;
		
		for(String toUserId : toUserIds){
			Message message = new Message();
			message.setType(3); //医生消息类型
			message.setStatus(0);  //设置消息未读
			message.setMessageFrom(doctorId);
			message.setMessageTo(toUserId);
			message.setContent(content);
			
			Result<?> create = createMessage(message);
			
			if("SUCCESS".equals(create.getResultCode().getCode())){
				successCount++;
			}
		}
		if(successCount == toUserIds.size()){
			result.setResultCode(new ResultCode("SUCCESS", "消息批量发送成功"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "消息批量发送失败"));
		}
		
		return result;
	}

	@Override
	public Result<?> readOverMessage(Message message) {
		Result<?> result = new Result<>(StatusCodes.OK, true);
		if (message == null) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("PARAM_NULL", "参数不能为空！"));
		}
		message.setUpdateTime(new Date());

		int num = messageMapper.updateStatusByEntity(message);
		if (num != 0) {
			result.setResultCode(new ResultCode("SUCCESS", "修改成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "修改失败！"));
		}
		return result;
	}

	@Override
	public PaginatedResult<Message> countUnReadMessageList(Message message) {
		List<Message> MessageList = messageMapper.countMessageStatusListPage(message);
		// 总页数
		int totalCount = message.getTotalResult();
		PaginatedResult<Message> pa = new PaginatedResult<Message>(MessageList,
				message.getCurrentPage(), message.getShowCount(), totalCount);

		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

	@Override
	public Result<Message> countAllUnReadMessage(Message message) {
		Result<Message> result = new Result<>(StatusCodes.OK, true);
		
		Message readCount = messageMapper.countUserMessageStatus(message);
		
		if (readCount != null) {
			result.setData(readCount);
			result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} else {
			result.setResultCode(new ResultCode("FAILED", "查询失败！"));
		}
		
		return result;
	}

}