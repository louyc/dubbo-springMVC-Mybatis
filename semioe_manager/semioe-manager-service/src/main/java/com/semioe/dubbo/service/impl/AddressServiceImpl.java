package com.semioe.dubbo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.AddressCity;
import com.semioe.api.entity.AddressCityExample;
import com.semioe.api.entity.AddressProvince;
import com.semioe.api.entity.AddressProvinceExample;
import com.semioe.api.entity.AddressTown;
import com.semioe.api.entity.AddressTownExample;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.AddressCityMapper;
import com.semioe.dubbo.dao.AddressProvinceMapper;
import com.semioe.dubbo.dao.AddressTownMapper;
import com.semioe.dubbo.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {
	
	private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

	@Autowired
	private AddressProvinceMapper addressProvinceMapper;
	@Autowired
	private AddressCityMapper addressCityMapper;
	@Autowired
	private AddressTownMapper addressTownMapper;
	
	@Override
	public Result<List<AddressProvince>> getAllProvince() {
		
		logger.info("AddressServiceImpl.getAllProvince START");
		
		Result<List<AddressProvince>> result = new Result<>(StatusCodes.OK, true);
		List<AddressProvince> provinceList = addressProvinceMapper.selectByExample(new AddressProvinceExample());
		
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		result.setData(provinceList);
		
		return result;
	}

	@Override
	public Result<List<AddressCity>> getCityByProvinceCode(String provinceCode) {
		
		logger.info("AddressServiceImpl.getAllProvince START provinceCode="+provinceCode);
		
		Result<List<AddressCity>> result = new Result<>(StatusCodes.OK, true);
		
		AddressCityExample example = new AddressCityExample();
		example.createCriteria().andProvincecodeEqualTo(provinceCode);
		List<AddressCity> cityList = addressCityMapper.selectByExample(example);
		
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		result.setData(cityList);
		
		return result;
	}

	@Override
	public Result<List<AddressTown>> getTownByTownCode(String cityCode) {

		logger.info("AddressServiceImpl.getAllProvince START cityCode="+cityCode);
		
		Result<List<AddressTown>> result = new Result<>(StatusCodes.OK, true);
		
		AddressTownExample example = new AddressTownExample();
		example.createCriteria().andCitycodeEqualTo(cityCode);
		List<AddressTown> townList = addressTownMapper.selectByExample(example);
		
		result.setResultCode(new ResultCode("SUCCESS", "查询成功"));
		result.setData(townList);
		
		return result;
	}

}
