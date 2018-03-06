package com.semioe.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.DeviceInfo;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.service.DeviceBindingUserService;
import com.semioe.dubbo.service.DeviceInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/deviceBinding")
public class DeviceBindingController {

	@Reference
	private DeviceBindingUserService deviceBindingUserService;
	@Reference
	private DeviceInfoService deviceInfoService;
	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;

	@Value("#{settings['weixin_Appid']}")
	private String weixin_Appid;
	@Value("#{settings['weixin_Secret']}")
	private String weixin_Secret;
	@Value("#{settings['weixin_Paysignkey']}")
	private String weixin_Paysignkey;
	@Value("#{settings['weixin_Mchid']}")
	private String weixin_Mchid;
	
	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	@RequestMapping("/deviceBindingUser")
	@ResponseBody
	public Result deviceBindingUser(@RequestParam("deviceCode") String deviceCode,
			@RequestParam("managerId") String managerId) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null == deviceCode || "".equals(deviceCode)) {
			result.setResultCode(new ResultCode("FALSE", "deviceCode为空"));
			return result;
		}

		if (null == managerId || "".equals(managerId)) {
			result.setResultCode(new ResultCode("FALSE", "managerId为空"));
			return result;
		}

		String deviceCodeCh = deviceCode.substring(deviceCode.lastIndexOf(".") + 1,
				deviceCode.length());

		// 根据 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(deviceCodeCh);

		if (null == deviceId || "".equals(deviceId)) {
			result.setResultCode(new ResultCode("FALSE", "没有查到该设备"));
			return result;
		}

		return deviceBindingUserService.deviceBindingUser(managerId, deviceId);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/getDeviceBindingList")
	@ResponseBody
	public Result getDeviceBindingList(@RequestParam("managerId") String managerId) {
		return deviceBindingUserService.getDeviceBindingList(managerId);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/relieveBinding")
	@ResponseBody
	public Result relieveBinding(@RequestParam("managerId") String managerId,
			@RequestParam("deviceId") String deviceId, @RequestParam("type") String type, 
			@RequestParam("netWorking") String netWorking) {
		
		String deviceCode = null;
		if("0".equals(netWorking)) {
			Result result = deviceInfoService.getDeviceInfoById(Integer.parseInt(deviceId));
			
			DeviceInfo deviceInfo = (DeviceInfo)result.getData();
			
			if(null != deviceInfo) {
				deviceCode = deviceInfo.getDeviceCode();
			}
		}
		
		return deviceBindingUserService.relieveBinding(managerId, deviceId, type, netWorking, jedisConnectionFactory.getHostName(),
				jedisConnectionFactory.getPort(), weixin_Appid.trim(), weixin_Secret.trim(), weixin_Paysignkey.trim(),
				weixin_Mchid.trim(), deviceCode);
	}

	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	@RequestMapping("/changeNetworking")
	@ResponseBody
	public Result changeNetworking(@RequestParam("managerId") String managerId,
			@RequestParam("deviceCode") String deviceCode,
			@RequestParam("deviceNetType") String deviceNetType) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null == deviceCode || "".equals(deviceCode)) {
			result.setResultCode(new ResultCode("FALSE", "deviceCode为空"));
			return result;
		}

		if (null == managerId || "".equals(managerId)) {
			result.setResultCode(new ResultCode("FALSE", "managerId为空"));
			return result;
		}

		if (null == deviceNetType || "".equals(deviceNetType)) {
			result.setResultCode(new ResultCode("FALSE", "deviceNetType为空"));
			return result;
		}

		String deviceCodeCh = deviceCode.substring(deviceCode.lastIndexOf(".") + 1,
				deviceCode.length());

		// 根据 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(deviceCodeCh);

		if (null == deviceId || "".equals(deviceId)) {
			result.setResultCode(new ResultCode("FALSE", "没有查到该设备"));
			return result;
		}

		return deviceBindingUserService.changeNetworking(managerId, deviceId, deviceNetType);
	}

}
