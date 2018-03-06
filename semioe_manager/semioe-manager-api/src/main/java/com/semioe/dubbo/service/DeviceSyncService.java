package com.semioe.dubbo.service;

import java.util.Map;

import com.semioe.common.result.Result;

public interface DeviceSyncService {
	
	@SuppressWarnings("rawtypes")
	Result deviceSyncInit(String type, Integer deviceId, String data);
	
	Map<String, Object> deviceSyncByYtj(Integer deviceId, String locationId, String type, String data, String managerId, String time);
	
	@SuppressWarnings("rawtypes")
	Result deviceSyncByImmunoassay(Integer deviceId, String managerId, String data, String type);
}