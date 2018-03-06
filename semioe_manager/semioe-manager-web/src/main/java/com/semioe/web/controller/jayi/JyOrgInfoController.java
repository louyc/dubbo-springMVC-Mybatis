package com.semioe.web.controller.jayi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.JyOrgDocRel;
import com.semioe.api.entity.JyOrgInfo;
import com.semioe.api.entity.Message;
import com.semioe.api.vo.JyOrgInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.JyDoctorInfoService;
import com.semioe.dubbo.service.JyOrgDocRelService;
import com.semioe.dubbo.service.JyOrgInfoService;
import com.semioe.web.util.YtxSms;

@Controller
@RequestMapping("/jyOrgInfo")
public class JyOrgInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(JyOrgInfoController.class);
	
	@Reference
	private JyOrgInfoService jyOrgInfoService;
	
	@Reference
	private JyOrgDocRelService jyOrgDocRelService;
	
	@RequestMapping("/getJyDoctorSignOrg")
	@ResponseBody
	public PaginatedResult<JyOrgInfoVO> getJyDoctorSignOrg(@RequestBody JyOrgInfo entity) throws Exception {
		return jyOrgInfoService.getJyDoctorSignOrg(entity);
	}
	
	@RequestMapping("/getJyOrgInfoList")
	@ResponseBody
	public PaginatedResult<JyOrgInfoVO> getJyOrgInfoList(@RequestBody JyOrgInfo entity) throws Exception {
		return jyOrgInfoService.getJyOrgInfoList(entity);
	}
	
	@RequestMapping("/inviteDoctor")
	@ResponseBody
	public Result<Message> inviteDoctor(@RequestBody JyOrgDocRel entity) throws Exception {
		Result<Message> result = jyOrgDocRelService.inviteDoctor(entity);
		
		if("SUCCESS".equals(result.getResultCode().getCode())){
			//发送短信
			Message sendData = result.getData();
			logger.info("发送短信,手机号："+ sendData.getSendMobile() +"; 内容:"+sendData.getContent());
			YtxSms.sendContentMessage(sendData.getSendMobile(), YtxSms.SMS_ACCOUNT_JY_ORGSEND , sendData.getDxModel());
		}
		return result;
	}
	
}
