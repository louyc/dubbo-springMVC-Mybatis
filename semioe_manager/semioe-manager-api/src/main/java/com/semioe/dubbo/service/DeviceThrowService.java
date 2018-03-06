package com.semioe.dubbo.service;

import com.semioe.common.result.Result;

public interface DeviceThrowService {

	@SuppressWarnings("rawtypes")
	Result insertDeviceThrow(String managerId, Integer deviceId, String deviceLocation);

}