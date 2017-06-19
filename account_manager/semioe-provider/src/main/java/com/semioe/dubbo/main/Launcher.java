package com.semioe.dubbo.main;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher {

	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring-context.xml", "spring-mybatis.xml", "spring-dubbo.xml",
						"spring-redis.xml" });
		context.start();

		System.in.read(); // 按任意键退出
	}
}