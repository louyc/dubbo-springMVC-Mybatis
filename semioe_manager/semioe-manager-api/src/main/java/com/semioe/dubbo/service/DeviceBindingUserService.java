package com.semioe.dubbo.service;

import com.semioe.api.entity.DeviceUserRel;
import com.semioe.common.result.Result;

public interface DeviceBindingUserService {

	@SuppressWarnings("rawtypes")
	Result deviceBindingUser(String managerId, Integer deviceId);

	@SuppressWarnings("rawtypes")
	Result getDeviceBindingList(String managerId);

	@SuppressWarnings("rawtypes")
	Result relieveBinding(String managerId, String deviceId, String type, String netWorking, String hostName, int port, String Appid, String Secret,
			String Paysignkey, String Mchid, String deviceCode);

	@SuppressWarnings("rawtypes")
	Result changeNetworking(String managerId, Integer deviceId, String deviceNetType);

	@SuppressWarnings("rawtypes")
	Result deleteBingingUser(Integer id);

	DeviceUserRel getDeviceIsBindingByDeviceId(Integer deviceId);

	DeviceUserRel checkDeviceIsBinding(Integer deviceId);

	DeviceUserRel checkDeviceIsBinding(String managerId, Integer deviceId);
}
