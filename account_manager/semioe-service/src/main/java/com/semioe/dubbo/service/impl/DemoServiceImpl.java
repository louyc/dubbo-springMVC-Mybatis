package com.semioe.dubbo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.ManagerInfo;
import com.semioe.dubbo.dao.ManagerInfoMapper;
import com.semioe.dubbo.service.DemoService;

@Service
public class DemoServiceImpl implements DemoService {

	private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

	@Autowired
	private ManagerInfoMapper managerInfoMapper;

	public String sayHello(String name) {

		ManagerInfo managerInfo = managerInfoMapper.selectByPrimaryKey(name);

		System.out.println("**********" + name);
		logger.debug("name:::::" + name);
		System.out.println(managerInfo.getUserName());
		return "Hello " + managerInfo.getUserName();
	}

}
