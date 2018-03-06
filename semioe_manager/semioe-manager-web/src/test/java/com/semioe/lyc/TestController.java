package com.semioe.lyc;

import com.semioe.dubbo.service.BackstageMenuInfoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestController {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring-context.xml", "spring-mvc.xml", "spring-redis.xml" });

		BackstageMenuInfoService bmi = (BackstageMenuInfoService) context
				.getBean("BackstageMenuInfoService");
		bmi.getBackstageMenuInfoByRoleId("1");
	}
}
