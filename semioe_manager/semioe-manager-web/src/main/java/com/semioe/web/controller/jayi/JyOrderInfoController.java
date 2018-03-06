package com.semioe.web.controller.jayi;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.vo.OrderInfoVO;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.JyOrderInfoService;

@Controller
@RequestMapping("/jyOrderInfo")
public class JyOrderInfoController {

	@Reference
	private JyOrderInfoService jyOrderInfoService;
	
	@RequestMapping("/getJyValidUserOrder")
	@ResponseBody
	public Result<List<OrderInfoVO>> getJyValidUserOrder(
			@RequestParam(value="userId",required=true) String userId ,
			@RequestParam(value="doctorId",required=true) String doctorId ,
			@RequestParam(value="serviceName",required=false) String serviceName ,
			@RequestParam(value="buyStartTime",required=false) String buyStartTime ,
			@RequestParam(value="buyEndTime",required=false) String buyEndTime ) throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		
		Map<String, Object> query = new HashMap<String, Object>();
		
		if( null!= userId && !userId.isEmpty() ){
			query.put("userId", userId);
		}
		if( null!= doctorId && !doctorId.isEmpty() ){
			query.put("doctorId", doctorId);
		}
		if( null!= serviceName && !serviceName.isEmpty() ){
			query.put("serviceName", serviceName);
		}
		
		if( null!= buyStartTime && !buyStartTime.isEmpty() ){
			query.put("buyStartTime", sdf.parse( buyStartTime ));
		}
		
		if( null!= buyEndTime && !buyEndTime.isEmpty() ){
			query.put("buyEndTime", sdf.parse( buyEndTime ));
		}
		
		return jyOrderInfoService.getJyValidUserOrder(query);
	}
	
	@RequestMapping("/getBackValidUserOrder")
	@ResponseBody
	public Result<List<OrderInfoVO>> getBackValidUserOrder(
			@RequestParam(value="userId",required=true) String userId ,
			@RequestParam(value="doctorId",required=true) String doctorId ,
			@RequestParam(value="serviceName",required=false) String serviceName ,
			@RequestParam(value="buyStartTime",required=false) String buyStartTime ,
			@RequestParam(value="buyEndTime",required=false) String buyEndTime ) throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		
		Map<String, Object> query = new HashMap<String, Object>();
		
		if( null!= userId && !userId.isEmpty() ){
			query.put("userId", userId);
		}
		if( null!= doctorId && !doctorId.isEmpty() ){
			query.put("doctorId", doctorId);
		}
		if( null!= serviceName && !serviceName.isEmpty() ){
			query.put("serviceName", serviceName);
		}
		
		if( null!= buyStartTime && !buyStartTime.isEmpty() ){
			query.put("buyStartTime", sdf.parse( buyStartTime ));
		}
		
		if( null!= buyEndTime && !buyEndTime.isEmpty() ){
			query.put("buyEndTime", sdf.parse( buyEndTime ));
		}
		
		return jyOrderInfoService.getBackValidUserOrder(query);
	}
	
}
