package com.semioe.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.common.tools.util.ConnUtil;

@Controller
@RequestMapping("/api/knowledge")
public class ApiKnoweController {

	private static final Logger logger = LoggerFactory.getLogger(ApiKnoweController.class);
	
	@Value("#{settings['knowledge_host']}")
	private String knowledge_host;
	
	/**
	 * 知识接口调用1
	 * @author quanlj
	 * @param str
	 * @return
	 */
	@RequestMapping("/getByKeywords")
	@ResponseBody
	public Result<String> getByKeywords(@RequestParam("str") String str ){
		
		Result<String> result = null;
		ResultCode code = null;
		
		Date startTime = null;
		Date endTime = null;
		
		if(str != null && !str.isEmpty()){
			Map<String,Object> urlData = new HashMap<>();
			urlData.put("str", str);

			try{
				startTime = new Date();
				logger.info(" ApiKnoweController.getByKeywords , start");
				logger.info("请求接口：["+knowledge_host + "/onto/getByKeyWords] , 参数：["+str+"] "+startTime);
				
				//请求返回参数
				String back = ConnUtil.sendPostString(knowledge_host + "/onto/getByKeyWords", urlData);
				
				endTime = new Date();
				logger.info("接口返回：["+back+"] "+endTime);
				long time = endTime.getTime() - startTime.getTime();
				logger.info("耗时：["+time+"] ");
				
				//转换返回参数
				JSONObject json = JSONObject.parseObject(back);
				code = new ResultCode(json.getString("error_code"), json.getString("message"));
				result = new Result<String>(StatusCodes.OK, true, code, json.getString("result"));
			}catch (Exception e) {
				// TODO: handle exception
				code = new ResultCode("false","数据获取失败");
				result = new Result<String>(StatusCodes.INTERNAL_SERVER_ERROR,false,code,e.getMessage());
			}
		
		}else{
			code = new ResultCode("false","请输入查询数据");
			result = new Result<String>(StatusCodes.UNSUPPORTED_MEDIA_TYPE,false,code);
		}
		
		return result;
	}
	
	
	/**
	 * 根据URL查询知识
	 * @author quanlj
	 * @param url
	 * @return
	 */
	@RequestMapping("/getByUrl")
	@ResponseBody
	public Result<String> getByUrl(@RequestParam("url") String url ){
		Result<String> result = null;
		ResultCode code = null;
		
		Date startTime = null;
		Date endTime = null;
		
		if(url != null && !url.isEmpty()){
			Map<String,Object> urlData = new HashMap<>();
			urlData.put("xstr", url);
			try{
				
				startTime = new Date();
				logger.info(" ApiKnoweController.getByUrl , start");
				logger.info("请求接口：["+knowledge_host + "/onto/getByURI] , 参数：["+url+"] "+startTime);
				
				//请求返回参数
				String back = ConnUtil.sendPostString(knowledge_host + "/onto/getByURI", urlData);
				
				endTime = new Date();
				logger.info("接口返回：["+back+"] "+endTime);
				long time = endTime.getTime() - startTime.getTime();
				logger.info("耗时：["+time+"] ");
				
				//转换返回参数
				JSONObject json = JSONObject.parseObject(back);
				code = new ResultCode(json.getString("error_code"), json.getString("message"));
				result = new Result<String>(StatusCodes.OK, true, code, json.getString("result"));
			}catch (Exception e) {
				// TODO: handle exception
				code = new ResultCode("false","数据获取失败");
				result = new Result<String>(StatusCodes.INTERNAL_SERVER_ERROR,false,code,e.getMessage());
			}
		
		}else{
			code = new ResultCode("false","请输入查询数据");
			result = new Result<String>(StatusCodes.UNSUPPORTED_MEDIA_TYPE,false,code);
		}
		
		return result;
	}
	
}
