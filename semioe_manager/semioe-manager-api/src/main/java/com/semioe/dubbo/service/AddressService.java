package com.semioe.dubbo.service;

import java.util.List;

import com.semioe.api.entity.AddressCity;
import com.semioe.api.entity.AddressProvince;
import com.semioe.api.entity.AddressTown;
import com.semioe.common.result.Result;

public interface AddressService {
	
	/**
	 * 获取所有省份
	 * @return
	 */
	Result<List<AddressProvince>> getAllProvince();
	
	/**
	 * 根据省份查询城市
	 * @param provinceCode
	 * @return
	 */
	Result<List<AddressCity>> getCityByProvinceCode(String provinceCode);
	
	/**
	 * 根据城市查询区县
	 * @param cityCode
	 * @return
	 */
	Result<List<AddressTown>> getTownByTownCode(String cityCode);

}
