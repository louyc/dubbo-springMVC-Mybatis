package com.semioe.web.controller.jayi;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.JyDoctorInfo;
import com.semioe.api.entity.Message;
import com.semioe.api.vo.JyDoctorInfoVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.JyDoctorInfoService;
import com.semioe.dubbo.service.JyOrgDocRelService;
import com.semioe.web.util.YtxSms;

@Controller
@RequestMapping("/jyDoctorInfo")
public class JyDoctorInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(JyDoctorInfoController.class);
	
	@Reference
	private JyDoctorInfoService jyDoctorInfoService;
	
	@Reference
	private JyOrgDocRelService jyOrgDocRelService;
	
	
	@RequestMapping("/findJyDoctorInfo")
	@ResponseBody
	public Result<List<JyDoctorInfoVO>> findJyDoctorInfo(@RequestBody JyDoctorInfo entity) throws Exception {
		return jyDoctorInfoService.findJyDoctorInfo(entity);
	}
	
	@RequestMapping("/getJySignDoctorInfo")
	@ResponseBody
	public PaginatedResult<JyDoctorInfoVO> getJySignDoctorInfo(@RequestBody JyDoctorInfo entity) throws Exception {
		if(entity.getValidTime() != null){
			entity.setValidTime(new Date());
		}
		return jyDoctorInfoService.getJySignDoctorInfo(entity);
	}
	
	@RequestMapping("/agreeInvite")
	@ResponseBody
	public Result<Message> agreeInvite(@RequestParam("relId") Integer relId) throws Exception {
		Result<Message> result = jyOrgDocRelService.agreeInvite(relId);
		
		if("SUCCESS".equals(result.getResultCode().getCode())){
			//发送短信
			Message sendData = result.getData();
			logger.info("发送短信,手机号："+ sendData.getSendMobile() +"; 内容:"+sendData.getContent());
			YtxSms.sendContentMessage(sendData.getSendMobile(), YtxSms.SMS_ACCOUNT_JY_DOCTORIN ,sendData.getDxModel());
		}
		
		return result;
	}
	
	@RequestMapping("/refuseInvite")
	@ResponseBody
	public Result<Message> refuseInvite(@RequestParam("relId") Integer relId) throws Exception {
		Result<Message> result = jyOrgDocRelService.refuseInvite(relId);
		
		if("SUCCESS".equals(result.getResultCode().getCode())){
			//发送短信
//			Message sendData = result.getData();
//			logger.info("发送短信,手机号："+ sendData.getSendMobile() +"; 内容:"+sendData.getContent());
//			YtxSms.sendContentMessage(sendData.getSendMobile(), YtxSms.SMS_ACCOUNT_JY_DOCTOROUT , sendData.getDxModel());
		}
		return result;
	}

	/**
	 * 机构修改家医类型
	 * @param signId
	 * @param types
	 * @return
	 */
	@RequestMapping("/changeSignType")
	@ResponseBody
	public Result<Object> changeSignType(@RequestParam("signId") Integer signId ,
			@RequestParam("types") List<Integer> types){
		
		logger.info("JyDoctorInfoController.changeSignType  signId="+signId+" types="+types);
		
		return jyDoctorInfoService.changeSignType(signId, types);
	}
	
}
