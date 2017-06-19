package com.semioe.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.common.tools.RedisTool;
import com.semioe.dubbo.service.DemoService;

/*@RequestMapping("/semioe-web")*/
@Controller
public class DemoController {

	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

	@Reference
	private DemoService demoService;
	@Autowired
	private RedisTool redisTool;

	@RequestMapping("/hello")
	@ResponseBody
	public String getManagerInfoById(
			@RequestParam(value = "name", required = true, defaultValue = "zhang") String name) {

		logger.info("name::" + name);
		String result = demoService.sayHello(name);
		/*
		 * ModelAndView mv = new ModelAndView(); logger.info("resule::",
		 * result); mv.addObject("message", result);
		 * mv.setViewName("/WEB-INF/views/error_fileupload.jsp");
		 */
		logger.info("12323::::" + result);
		return result;
		// return result;
	}
}