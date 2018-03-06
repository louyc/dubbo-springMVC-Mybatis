package com.semioe.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.semioe.api.entity.DeviceInfo;
import com.semioe.api.entity.DeviceUserRel;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.service.DeviceBindingUserService;
import com.semioe.dubbo.service.DeviceInfoService;
import com.semioe.dubbo.service.DeviceSyncService;

/**
 * 设备同步接口 
 */
@Controller
@RequestMapping("/deviceSync")
public class DeviceSyncController {
	
	@Reference
	private DeviceSyncService deviceSyncService;
	@Reference
	private DeviceInfoService deviceInfoService;
	@Reference
	private DeviceBindingUserService deviceBindingUserService;
	
	/**
	 * 设备同步接口 主要同步 卡片机 手环 血糖仪
	 * @param type 设备类型      KPJ : 卡片机   XTY : 血糖仪  SH : 蓝牙手环  TWJ : 体温计 KT:康泰血压计
	 * @param data 数据JSON
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	@RequestMapping("/deviceSyncInit")
	@ResponseBody
	public Result deviceSyncInit(@RequestParam("type") String type, @RequestParam("data") String data) throws IllegalStateException, IOException {
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(null == type || "".equals(type)) {
			result.setResultCode(new ResultCode("FALSE", "type为空"));
			return result;
		}
		
		if(null == data || "".equals(data)) {
			result.setResultCode(new ResultCode("FALSE", "data为空"));
			return result;
		}
		
		JSONObject jb = JSONObject.parseObject(data.trim());
		
		// 根据设备 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(jb.getString("deviceCode"));
		// deviceId 不存在 说明设备没有加入后台
		if(null == deviceId || "".equals(deviceId)) {
			result.setResultCode(new ResultCode("FALSE", "未找到该设备"));
			return result;
		}
		// 数据同步实现
		return deviceSyncService.deviceSyncInit(type, deviceId, data);
	}
	
	/**
     * 设备同步接口 仅支持一体机同步
     */
	@SuppressWarnings({ "unlikely-arg-type", "rawtypes" })
	@RequestMapping("/deviceSyncByYtj")
    @ResponseBody
    public Map<String, Object> deviceSyncByYtj(String deviceCode, String type, String data, String userMobile, String time, HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        if(null == deviceCode || "".equals(deviceCode)){
            result.put("message", "deviceCode为空");
            result.put("error_code", "-1");
            return result;
        }
        if(null == type || "".equals(type)){
            result.put("message", "type为空");
            result.put("error_code", "-1");
            return result;
        }
        if(null == data || "".equals(data)){
            result.put("message", "data为空");
            result.put("error_code", "-1");
            return result;
        }
        if(null == userMobile || "".equals(userMobile)){
            result.put("message", "userMobile为空");
            result.put("error_code", "-1");
            return result;
        }
        
        // 截取 deviceCode 获取 设备码
        String code = deviceCode.substring(deviceCode.lastIndexOf(".") + 1, deviceCode.length());
        
        // 根据 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(code.trim());
				
		if(null == deviceId || "".equals(deviceId)) {
			result.put("message", "未找到该设备");
			result.put("error_code", "-1");
			return result;
		}
		// 根据 deviceId 校验 该设备是否投放
		Result resultData = deviceInfoService.getDeviceInfoById(deviceId);
		DeviceInfo deviceInfo = (DeviceInfo)resultData.getData();
		
		if(null == deviceInfo.getManagerId() || "".equals(deviceInfo.getManagerId())) {
			result.put("message", "该设备未投放");
			result.put("error_code", "-1");
			return result;
		}
		// 设备投放地 ID
		String locationId = deviceInfo.getManagerId();
		
		// 根据 userId（managerId） 设备ID 获取当前绑定关系
		DeviceUserRel deviceUserRel = deviceBindingUserService.checkDeviceIsBinding(userMobile, deviceId);

        // 判断设备是否绑定
        if(null == deviceUserRel || "".equals(deviceUserRel) || "F".equals(deviceUserRel.getIsBinding())){
            result.put("code", "-1");
            result.put("message", "设备未绑定");
            return result;
        }
        
        return deviceSyncService.deviceSyncByYtj(deviceId, locationId, type, data, deviceUserRel.getManagerId(), time);
    }
	
	/**
	 * 设备同步接口 主要同步 免疫定量分析仪
	 * @param type 设备类型
	 * @param data 数据JSON
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	@RequestMapping("/deviceSyncByImmunoassay")
	@ResponseBody
	public Result deviceSyncByImmunoassay(@RequestParam("deviceCode") String deviceCode, @RequestParam("data") String data, @RequestParam("type") String type) throws IllegalStateException, IOException {
		Result result = new Result<>(StatusCodes.OK, true);
		
		if(null == deviceCode || "".equals(deviceCode)) {
			result.setResultCode(new ResultCode("FALSE", "deviceCode为空"));
			return result;
		}
		
		if(null == data || "".equals(data)) {
			result.setResultCode(new ResultCode("FALSE", "data为空"));
			return result;
		}
		
		if(null == type || "".equals(type)) {
			result.setResultCode(new ResultCode("FALSE", "type为空"));
			return result;
		}
		
		// 截取 deviceCode 获取 设备码
        String code = deviceCode.substring(deviceCode.lastIndexOf(".") + 1, deviceCode.length());
        
        // 根据 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(code.trim());
				
		if(null == deviceId || "".equals(deviceId)) {
			result.setResultCode(new ResultCode("FALSE", "未找到该设备"));
		}
		// 根据 deviceId 校验 该设备是否投放
//		Result resultData = deviceInfoService.getDeviceInfoById(deviceId);
//		DeviceInfo deviceInfo = (DeviceInfo)resultData.getData();
//		
//		if(null == deviceInfo.getManagerId() || "".equals(deviceInfo.getManagerId())) {
//			result.setResultCode(new ResultCode("FALSE", "该设备未投放"));
//			return result;
//		}
		
		// 根据 userId（managerId） 设备ID 获取当前绑定关系
		DeviceUserRel deviceUserRel = deviceBindingUserService.checkDeviceIsBinding(deviceId);

        // 判断设备是否绑定
        if(null == deviceUserRel || "".equals(deviceUserRel) || "F".equals(deviceUserRel.getIsBinding())){
        	result.setResultCode(new ResultCode("FALSE", "设备未绑定"));
        }
		
		// 数据同步实现   deviceSyncService.deviceSyncByImmunoassay(deviceId, data)
		return deviceSyncService.deviceSyncByImmunoassay(deviceId, deviceUserRel.getManagerId(), data, type);
	}
}