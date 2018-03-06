package com.semioe.web.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.DeviceUserRel;
import com.semioe.api.vo.ApiContractedUserVO;
import com.semioe.api.vo.ApiUserInfoVO;
import com.semioe.common.result.Result;
import com.semioe.dubbo.service.ApiContractedUserService;
import com.semioe.dubbo.service.ApiUserInfoService;
import com.semioe.dubbo.service.DeviceBindingUserService;
import com.semioe.dubbo.service.DeviceInfoService;

@Controller
@RequestMapping("/deviceProvide")
public class DeviceProvideController {

	@Reference
	private DeviceInfoService deviceInfoService;
	@Reference
	private DeviceBindingUserService deviceBindingUserService;
	@Reference
	private ApiUserInfoService apiUserInfoService;
	@Reference
	private ApiContractedUserService apiContractedUserService;
	
	/*  思创接口 START  */
    /**
     * 获取设备二维码接口
     */
    @RequestMapping("/getQRImg")
    @ResponseBody
    public Map<String, Object> getQRImg(String deviceCode, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        if(null == deviceCode || "".equals(deviceCode)){
            result.put("message", "deviceCode为空");
            result.put("error_code", "-1");
            return result;
        }

        StringBuffer path = request.getRequestURL();  
        String tempContextUrl = path.delete(path.length() - request.getRequestURI().length(), path.length()).append("/").toString();
        String url = tempContextUrl +  "ytjQrcode.jsp?deviceCode=" + deviceCode.substring(deviceCode.trim().lastIndexOf(".")+1, deviceCode.length());
        
        result.put("url", url);
        result.put("message", "查询成功");
        result.put("code", "0");
        return result;
    }
    
    /**
     * 验证 开/关屏
     */
    @SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/getScreenState")
    @ResponseBody
    public Map<String, Object> getScreenState(String deviceCode){
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> action = new HashMap<String, Object>();
        
        if(null == deviceCode || "".equals(deviceCode)){
            result.put("message", "deviceCode为空");
            result.put("error_code", "-1");
            return result;
        }
        
        // 截取deviceCode 
        String code = deviceCode.substring(deviceCode.lastIndexOf(".") + 1, deviceCode.length());
		
		// 根据 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(code.trim());
				
		if(null == deviceId || "".equals(deviceId)) {
			result.put("message", "此设备暂未投放");
            result.put("error_code", "-1");
            return result;
		}	
		
		// 根据 deviceId 获取已经绑定的用户关系
		DeviceUserRel deviceUserRel = deviceBindingUserService.checkDeviceIsBinding(deviceId);
		// 屏幕状态定义字段
		String str = null;
		
		// 关系为空 返回关屏
		if(null == deviceUserRel) {
			str = "closeTheScreen";
			
			action.put("action", str);
	        action.put("userId", "");
	        action.put("userName", "");
	        action.put("userSex", "");
	        action.put("userAge", "");
	        
	        result.put("result", action);
	        result.put("message", "查询成功");
	        result.put("error_code", "0");
	        return result;
        }
		
		// IsBinding 为 F 关屏  T 开屏 A 管理员
        if("F".equals(deviceUserRel.getIsBinding())){
            str = "closeTheScreen";
        }else if("T".equals(deviceUserRel.getIsBinding())){
        	str = "openTheScreen";
        }else if("A".equals(deviceUserRel.getIsBinding())) {
        	str = "allSystem";
        }
        action.put("action", str);
        action.put("userId", deviceUserRel.getUserMobile());
        action.put("userName", deviceUserRel.getUserName());
        
        String sex = null;
        if("1".equals(deviceUserRel.getUserSex())) {
        	sex = "男";
        }else if("2".equals(deviceUserRel.getUserSex())) {
        	sex = "女";
        }
        
        action.put("userSex", sex);
        try {
			action.put("userAge", getAge(deviceUserRel.getUserBirthday()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        result.put("result", action);
        result.put("message", "查询成功");
        result.put("error_code", "0");
        return result;
    }

    /**
     * 设备锁屏
     */
    @SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/closeDevice")
    @ResponseBody
    public Map<String, Object> closeDevice(String deviceCode, String userId){
        Map<String, Object> result = new HashMap<String, Object>();
        if(null == deviceCode || "".equals(deviceCode)){
            result.put("message", "deviceCode为空");
            result.put("error_code", "-1");
            return result;
        }
        if(null == userId || "".equals(userId)){
            result.put("message", "userId为空");
            result.put("error_code", "-1");
            return result;
        }

        String code = deviceCode.substring(deviceCode.lastIndexOf(".") + 1, deviceCode.length());
		
		// 根据 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(code);
				
		if(null == deviceId || "".equals(deviceId)) {
			result.put("message", "此设备暂未投放");
            result.put("error_code", "-1");
            return result;
		}	
		
		// 此 userId 为一体机传过来的参与  具体意义为 用户手机号
        String managerId = apiUserInfoService.getManagerIdByMobile(userId);
		
        deviceBindingUserService.relieveBinding(managerId, deviceId.toString(), "F", "", "", 0, "", "", "", "", "");

        result.put("message", "成功");
        result.put("error_code", "0");
        return result;
    }

    /**
     * 身份证开屏 
     */
    @SuppressWarnings({ "unlikely-arg-type", "rawtypes" })
	@RequestMapping("/openYtjByIdCard")
    @ResponseBody
    public Map<String, Object> openYtjByIdCard(String cardId, String deviceCode){
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> action = new HashMap<String, Object>();
        
        if(null == cardId || "".equals(cardId)){
            result.put("message", "cardId为空");
            result.put("error_code", "-1");
            return result;
        }
        
        if(null == deviceCode || "".equals(deviceCode)){
            result.put("message", "deviceCode为空");
            result.put("error_code", "-1");
            return result;
        }
        
        // 截取deviceCode 
        String code = deviceCode.substring(deviceCode.lastIndexOf(".") + 1, deviceCode.length());
		
		// 根据 deviceCode 获取 deviceId
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(code.trim());
				
		if(null == deviceId || "".equals(deviceId)) {
			result.put("message", "此设备暂未投放");
            result.put("error_code", "-1");
            return result;
		}	
		
		// 屏幕状态定义字段
		String str = null;
		// 根据身份证号 查询 managerId 不存在则关机 存在则开机
		ApiContractedUserVO apiUserInfo = new ApiContractedUserVO();
		apiUserInfo.setCardId(cardId);
		Result re = apiContractedUserService.querySignUserByEntity(apiUserInfo);
		
		if(null == re.getData()) {
			str = "closeTheScreen";
			
			action.put("action", str);
	        action.put("userId", "");
	        action.put("userName", "");
	        action.put("userSex", "");
	        action.put("userAge", "");
	        
	        result.put("result", action);
	        result.put("message", "查询成功");
	        result.put("error_code", "0");
	        return result;
		}else {
			ApiUserInfoVO auiv = (ApiUserInfoVO)re.getData();
			
			deviceBindingUserService.deviceBindingUser(auiv.getManagerId(), deviceId);
			
			str = "openTheScreen";
			action.put("action", str);
	        action.put("userId", auiv.getMobile());
	        action.put("userName", auiv.getName());
	        
	        String sex = null;
	        if("1".equals(auiv.getSex())) {
	        	sex = "男";
	        }else if("2".equals(auiv.getSex())) {
	        	sex = "女";
	        }
	        
	        action.put("userSex", sex);
	        
	        try {
				action.put("userAge", getAge(auiv.getBirthday()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        result.put("result", action);
	        result.put("message", "查询成功");
	        result.put("error_code", "0");
	        return result;
		}
    }
    /*  思创接口 END  */
    
    // 计算年龄
    private Integer getAge(Date birthDay) throws Exception {
    	if(null == birthDay || "".equals(birthDay)) {
    		return 0;
    	}
    	
        //获取当前系统时间
        Calendar cal = Calendar.getInstance();
        //如果出生日期大于当前时间，则抛出异常
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        //取出系统当前时间的年、月、日部分
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        //将日期设置为出生日期
        cal.setTime(birthDay);
        //取出出生日期的年、月、日部分
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        //当前年份与出生年份相减，初步计算年龄
        int age = yearNow - yearBirth;
        //当前月份与出生日期的月份相比，如果月份小于出生月份，则年龄上减1，表示不满多少周岁
        if (monthNow <= monthBirth) {
            //如果月份相等，在比较日期，如果当前日，小于出生日，也减1，表示不满多少周岁
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }
}