package com.semioe.dubbo.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.BackstageUserInfo;
import com.semioe.api.entity.JyOrgDocRel;
import com.semioe.api.entity.Message;
import com.semioe.api.entity.OrderInfo;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.BackstageUserInfoMapper;
import com.semioe.dubbo.dao.JyOrgDocRelMapper;
import com.semioe.dubbo.service.JyOrgDocRelService;
import com.semioe.dubbo.service.MessageService;

@Service
public class JyOrgDocRelServiceImpl implements JyOrgDocRelService {
	
	private static final Logger logger = LoggerFactory.getLogger(JyOrgDocRelServiceImpl.class);

	@Autowired
	private JyOrgDocRelMapper jyOrgDocRelMapper;
	
	@Autowired
	private BackstageUserInfoMapper backstageUserInfoMapper;
	
	@Autowired
	private MessageService messageService;

	@Override
	public Result<Message> inviteDoctor(JyOrgDocRel entity) {
		Result<Message> result = null;
		// 获取机构对象
		BackstageUserInfo org = backstageUserInfoMapper.selectByPrimaryKey(entity.getOrgId());
		if(org != null && org.getType() != null ){
			// 根据机构类型判断邀请类型
			if("4".equals(org.getType()) ){
				// 邀请家庭医生
				result = JYInviteDoctor(entity);
			}else{
				// 邀请普通医生
				result = orgInviteDoctor(entity);
			}
		}else{	
			result = new Result<>(StatusCodes.OK, true);
			logger.info("机构邀请权限,邀请验证不通过 ！");
			result.setResultCode(new ResultCode("fell", "您没有邀请医生权限！"));
		}
		return result;
	}

	/**
	 * 家庭医生，机构邀请医生方法
	 * @param entity
	 * @return
	 */
	public Result<Message> JYInviteDoctor(JyOrgDocRel entity){
		Result<Message> result = new Result<>(StatusCodes.OK, true);
		//判断有效期内医生是否签约
		JyOrgDocRel query = new JyOrgDocRel();
		query.setValidTime(new Date());           // 有时间
		query.setDoctorId(entity.getDoctorId());  // 医生id
		query.setSignType(1);                     // 签约状态为已签约
		List<JyOrgDocRel> isSign = jyOrgDocRelMapper.selectJyOrgDocRel(query);
		if(isSign != null && isSign.size() > 0){
			logger.info("邀请验证不通过 ！");
			result.setResultCode(new ResultCode("fell", "邀请失败，医生已签约机构不能邀请。"));
			return result;
		}
		//是否邀请
		query.setValidTime(null); 
		query.setOrgId(entity.getOrgId());  //机构
		query.setSignType(0);               //签约状态为，待通过
		List<JyOrgDocRel> hasRel = jyOrgDocRelMapper.selectJyOrgDocRel(query);
		if(hasRel != null && hasRel.size() > 0){
			logger.info("医生已邀请");
			result.setResultCode(new ResultCode("fell", "邀请失败，医生已被邀请"));
			return result;
		}
		//创建签约信息
		entity.setCreateTime(new Date());
		entity.setSignType(0);
		int num = jyOrgDocRelMapper.insertSelective(entity);
		if(num > 0){
			logger.info("邀请成功 ！");
			//发送站内消息
			sendMessage(entity,result);
			result.setResultCode(new ResultCode("SUCCESS", "邀请成功。"));
		}else{
			logger.info("邀请保存失败！");
			result.setResultCode(new ResultCode("fell", "邀请失败。"));
		}
		return result;		
	}
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public Result<Message> orgInviteDoctor(JyOrgDocRel entity){
		Result<Message> result = new Result<>(StatusCodes.OK, true);
		//判断有效期内医生是否签约
		JyOrgDocRel query = new JyOrgDocRel();
		query.setDoctorId(entity.getDoctorId());  // 医生id
		query.setOrgId(entity.getOrgId());        // 机构 id
		query.setSignType(1);                     // 签约状态为已签约
		List<JyOrgDocRel> isSign = jyOrgDocRelMapper.selectJyOrgDocRel(query);
		if(isSign != null && isSign.size() > 0){
			logger.info("邀请验证不通过 ！医生机构已签约");
			result.setResultCode(new ResultCode("fell", "邀请失败，该医生已经是您的签约医生，不能重复签约。"));
			return result;
		}
		//是否邀请
		query.setDoctorId(entity.getDoctorId());  // 医生 id
		query.setOrgId(entity.getOrgId());        // 机构 id
		query.setSignType(0);               //签约状态为，待通过
		List<JyOrgDocRel> hasRel = jyOrgDocRelMapper.selectJyOrgDocRel(query);
		if(hasRel != null && hasRel.size() > 0){
			logger.info("邀请验证不通过 ！机构邀请已存在，等待医生通过");
			result.setResultCode(new ResultCode("fell", "邀请失败，您的经邀请已经发送医生，请耐心等待回复"));
			return result;
		}
		//创建签约信息
		entity.setCreateTime(new Date());
		entity.setSignType(0);
		int num = jyOrgDocRelMapper.insertSelective(entity);
		if(num > 0){
			logger.info("邀请成功 ！");
			//发送站内消息
			sendMessage(entity,result);
			result.setResultCode(new ResultCode("SUCCESS", "邀请成功。"));
		}else{
			logger.info("邀请保存失败！");
			result.setResultCode(new ResultCode("fell", "邀请失败。"));
		}
		return result;		
	}
	
	public Result<Message> JYAgreeInvite(JyOrgDocRel entity){
		Result<Message> result = new Result<>(StatusCodes.OK, true);
		//判断有效期内医生是否签约
		JyOrgDocRel query = new JyOrgDocRel();
		query.setValidTime(new Date());           // 有时间
		query.setDoctorId(entity.getDoctorId());  // 医生id
		query.setSignType(1);                     // 签约状态为已签约
		List<JyOrgDocRel> isSign = jyOrgDocRelMapper.selectJyOrgDocRel(query);
		if(isSign != null && isSign.size() > 0){
			logger.info("邀请验证不通过 ！");
			result.setResultCode(new ResultCode("fell", "签约失败，医生已签约机构不能重复签约。"));
			return result;
		}
		//查询所有签约邀请
		query.setValidTime(null);          
		query.setSignType(0);                     // 签约状态为 邀请签约
		List<JyOrgDocRel> inviteSignList = jyOrgDocRelMapper.selectJyOrgDocRel(query);
		try{
			for(JyOrgDocRel item : inviteSignList){
				if(item.getId() == entity.getId()){
					// 签约医生，修改状态
					item.setSignType(1);
					item.setUpdateTime(new Date());
					item.setSignTime(new Date());
					// 签约结束时间
					Calendar ca = Calendar.getInstance();
					ca.setTime(new Date());
					ca.add(Calendar.YEAR, 1);   
					item.setOutTime(ca.getTime());
					// 修改签约信息
					jyOrgDocRelMapper.updateByPrimaryKeySelective(item);
					// 同步医生表冗余字段
					jyOrgDocRelMapper.updateUserInfoType(item);
					//发送消息
					sendMessage(item,result);
				}else{
					// 拒绝其他机构
					item.setSignType(2);
					item.setUpdateTime(new Date());
					item.setSignTime(null);  
					item.setOutTime(null);
					// 修改签约信息
					jyOrgDocRelMapper.updateByPrimaryKeySelective(item);
					//发送站内消息
					sendInsideMessage(item);
				}
			}
			logger.info("签约成功 ！");
			result.setResultCode(new ResultCode("SUCCESS", "签约成功。"));
		}catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("签约保存失败！");
			result.setResultCode(new ResultCode("fell", "签约失败。"));
		}
		
		return result;
	}
	
	public Result<Message> doctorAgreeInvite(JyOrgDocRel entity){
		Result<Message> result = new Result<>(StatusCodes.OK, true);
		// 签约医生，修改状态
		entity.setSignType(1);
		entity.setUpdateTime(new Date());
		entity.setSignTime(new Date());
		// 修改签约信息
		jyOrgDocRelMapper.updateByPrimaryKeySelective(entity);
		//发送消息
		return sendMessage(entity,result);
	}
	
	@Override
	public Result<Message> agreeInvite(Integer relId) {
		Result<Message> result = new Result<>(StatusCodes.OK, true);
		// 查询关系信息
		JyOrgDocRel entity = jyOrgDocRelMapper.selectByPrimaryKey(relId);
		if(entity == null || (entity != null && entity.getSignType() != 0) ){
			logger.info("签约失败 ！");
			result.setResultCode(new ResultCode("fell", "签约失败，无签约邀请信息。"));
			return result;
		}
		BackstageUserInfo org = backstageUserInfoMapper.selectByPrimaryKey(entity.getOrgId());
		// 根据机构类型判断邀请类型
		if("4".equals(org.getType()) ){
			// 邀请家庭医生
			result = JYAgreeInvite(entity);
		}else{
			// 邀请普通医生
			result = doctorAgreeInvite(entity);
		}
		
		return result;
	}

	@Override
	public Result<Message> refuseInvite(Integer relId) {
		Result<Message> result = new Result<>(StatusCodes.OK, true);
		// 查询关系信息
		JyOrgDocRel entity = jyOrgDocRelMapper.selectByPrimaryKey(relId);
		if(entity == null || (entity != null && entity.getSignType() != 0) ){
			logger.info("签约失败 ！");
			result.setResultCode(new ResultCode("fell", "拒绝失败，无签约邀请信息。"));
			return result;
		}
		
		// 签约医生，修改状态
		entity.setSignType(2);
		entity.setUpdateTime(new Date());
		entity.setSignTime(null);  
		entity.setOutTime(null);
		// 修改签约信息
		int num1 = jyOrgDocRelMapper.updateByPrimaryKeySelective(entity);
		
		// 查询机构对象，判断机构类型
		BackstageUserInfo org = backstageUserInfoMapper.selectByPrimaryKey(entity.getOrgId());
		
		if("4".equals(org.getType()) ){ // 家庭医生
			// 同步医生表冗余字段
			jyOrgDocRelMapper.updateUserInfoType(entity);
		}
		
		if(num1 > 0){
			logger.info("拒绝成功 ！");
			//发送消息
			sendMessage(entity,result);
			result.setResultCode(new ResultCode("SUCCESS", "拒绝成功。"));
		}else{
			logger.info("拒绝保存失败！");
			result.setResultCode(new ResultCode("fell", "拒绝失败。"));
		}
		return result;
	}
	
	/**
	 * 发送站内消息
	 * @param entity
	 */
	private Message sendInsideMessage(JyOrgDocRel entity){
		
		// 站内消息对象
		Message message = new Message();
		
		//查找医生
		BackstageUserInfo doctor = backstageUserInfoMapper.selectByPrimaryKey(entity.getDoctorId());
		//查找机构
		BackstageUserInfo org = backstageUserInfoMapper.selectByPrimaryKey(entity.getOrgId());
		//消息内容
		String sendMessage = "";
		String sendMobile = "";
		
		String fromId = "";
		String toId = "";
		String[] dxModel = new String[2];
		
		switch (entity.getSignType()) {
		case 0:  //机构邀请医生  （发医生）
			sendMessage = org.getName() + "机构 邀请您签约医生。";
			sendMobile = doctor.getMobile();
			fromId = org.getManagerId();
			toId = doctor.getManagerId();
			//短信发送内容
			dxModel[0] = org.getName();
			dxModel[1] = "申请了";
			break;
		case 1:  //医生接受邀请  （发机构）
			sendMessage = doctor.getName() + "医生 接受了您的医生签约邀请。";
			sendMobile = org.getMobile();
			fromId = doctor.getManagerId();
			toId = org.getManagerId();
			//短信发送内容
			dxModel[0] = doctor.getName();
			dxModel[1] = "接受了";
			break;
		case 2:  //医生拒绝邀请  （发机构）
			sendMessage = doctor.getName() + "医生 拒绝了您的生签约邀请。";
			sendMobile = org.getMobile();
			fromId = doctor.getManagerId();
			toId = org.getManagerId();
			//短信发送内容
			dxModel[0] = doctor.getName();
			dxModel[1] = "拒绝了";
			break;
		}
		
		message.setMessageFrom(fromId);
		message.setMessageTo(toId);
		message.setSendMobile(sendMobile);
		message.setType(Message.Type.INTERNAL.getValue());
		message.setContent(sendMessage);
		message.setStatus(1);
		
		message.setDxModel(dxModel);
		
		//保存消息
		messageService.createMessage(message);
		
		logger.info("邀请，发送消息：" + sendMessage);
		
		return message;
		
	}
	
	/**
	 * 发送消息提示功能
	 * @param entity
	 */
	private Result<Message> sendMessage(JyOrgDocRel entity , Result<Message> result){
	
		// 发送站内消息
		Message message = sendInsideMessage(entity);
		
		//添加消息
		result.setData(message);
		return result;
	}
	
}
