package com.semioe.dubbo.service;

import com.semioe.api.vo.DeviceDataVO;
import com.semioe.common.result.Result;

public interface DeviceDataService {

	@SuppressWarnings("rawtypes")
	Result getMeasureData(String managerId, String time);

	@SuppressWarnings("rawtypes")
	Result getHistoryMeasureData(String managerId, String time);

	@SuppressWarnings("rawtypes")
	Result getMeasureDataByType(String managerId, String time, String type);

	@SuppressWarnings("rawtypes")
	Result insertMeasureData(String managerId, String type, String data);

	@SuppressWarnings("rawtypes")
	Result getMeasureDataByMidType(String managerId, String time, String type);

	@SuppressWarnings("rawtypes")
	Result getAllDeviceBindingUser(DeviceDataVO deviceData);

	@SuppressWarnings("rawtypes")
	Result queryUserInfo(DeviceDataVO deviceData);

	@SuppressWarnings("rawtypes")
	Result queryDeviceItems();
	
	@SuppressWarnings("rawtypes")
	Result queryCheckDeviceItems();
}