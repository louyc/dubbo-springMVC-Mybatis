package com.semioe.dubbo.service;

import com.semioe.api.entity.JyOrgDocRel;
import com.semioe.api.entity.Message;
import com.semioe.common.result.Result;
/**
 * （家庭医生）医生机构关系服务
 * @author puanl
 *
 */
public interface JyOrgDocRelService {
	
	/**
	 * 邀请医生
	 * @param orgId
	 * @param doctorId
	 * @return
	 */
	Result<Message> inviteDoctor(JyOrgDocRel entity);
	
	/**
	 * 同意邀请
	 * @param entity
	 * @return
	 */
	Result<Message> agreeInvite(Integer relId);
	
	/**
	 * 拒绝邀请
	 * @param entity
	 * @return
	 */
	Result<Message> refuseInvite(Integer relId);
	
}
