package com.semioe.dubbo.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.semioe.api.dto.DeviceItemEnum;
import com.semioe.api.entity.CardApparatusData;
import com.semioe.api.entity.DeviceData;
import com.semioe.api.entity.GlucometerData;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.common.tools.util.DateTimeUtil;
import com.semioe.dubbo.dao.CardApparatusDataMapper;
import com.semioe.dubbo.dao.DeviceDataMapper;
import com.semioe.dubbo.dao.DeviceUserRelMapper;
import com.semioe.dubbo.dao.GlucometerDataMapper;
import com.semioe.dubbo.service.DeviceSyncService;

/**
 * 设备同步接口实现
 */
@Service
public class DeviceSyncServiceImpl implements DeviceSyncService {
	
	@Autowired
	private CardApparatusDataMapper cardApparatusDataMapper;
	@Autowired
	private GlucometerDataMapper glucometerDataMapper;
	@Autowired
	private DeviceDataMapper deviceDataMapper;
	@Autowired
	private DeviceUserRelMapper deviceUserRelMapper;
	
	/**
	 * 设备同步接口 主要同步 卡片机 手环 血糖仪 康泰血压计
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result deviceSyncInit(String type, Integer deviceId, String data) {
		Result result = new Result<>(StatusCodes.OK, true);
		JSONObject jb = JSONObject.parseObject(data.trim());
		
		boolean bo = false;
		
		// KPJ:卡片机   XTY:血糖仪  SH:蓝牙手环  TWJ:体温计  KT:康泰血压计
		switch(type) {
			// 卡片机
			case "KPJ":
				bo = cardDataSync(deviceId, jb);
				break;
			// 血糖仪
			case "XTY":
				try {
					bo = glucometerDataSync(deviceId, jb);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			// 蓝牙手环
			case "SH":
				bo = braceletDataSync(deviceId, jb);
				break;
			// 体温计
			case "TWJ":
				try {
					bo = temperatureDataSync(deviceId, jb);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			// 康泰血压计
			case "KT":
				bo = ktDataSync(deviceId, jb);
				break;
			default:
				break;
		}

		
		if(bo) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功"));
		}else {
			result.setResultCode(new ResultCode("FAILED", "添加失败"));
		}
		return result;
	}
	
	/**
	 * 	卡片机数据处理 （主要用于测量心电数据）
	 * 
	 * 	deviceId			// 设备 id
		heartRate        	// 心率
		ecgResult		 	// 心电经算法库得出结论
		ecgImg			 	// 心电图图片   base64
	 * 
	 * @param jb
	 * @return
	 */
	private boolean ktDataSync(Integer deviceId, JSONObject jb) {
		DeviceData deviceData = new DeviceData();
		// 根据deviceId查询当前绑定的用户ID
		String managerId = deviceUserRelMapper.getManagerIdByDeviceId(deviceId);
		int id = 0;
		
		if(null != managerId && !"".equals(managerId)) {
			deviceData.setManagerId(managerId);
			deviceData.setDeviceId(deviceId);
			deviceData.setCreateTime(new Date());
			deviceData.setUpdateTime(new Date());
			deviceData.setDataFrom("KT");
			deviceData.setIsAffirm("T");
			
			deviceData.setDataType(DeviceItemEnum.BP.toString());
			
			JSONObject jo = new JSONObject();
			
			// 舒张压 diastolic_blood_pressure
        	String diastolicBloodPressure = jb.get("diastolic_blood_pressure").toString();
            // 收缩压 systolic_blood_pressure
        	String systolicBloodPressure = jb.get("systolic_blood_pressure").toString();
            // 脉率 pulse_rate
        	String pulseRate = jb.get("pulse_rate").toString();
        	
        	jo.put("dia", diastolicBloodPressure);
        	jo.put("sys", systolicBloodPressure);
        	jo.put("pr", pulseRate);
			
        	jo.put("value", systolicBloodPressure + "/" + diastolicBloodPressure);
        	jo.put("date", DateTimeUtil.getFormatDateTime(new Date()));
        	jo.put("units", DeviceItemEnum.BP.getUnit());
        	jo.put("name", DeviceItemEnum.BP.getName());
        	jo.put("dataType", DeviceItemEnum.BP.toString());
			
        	deviceData.setDataValue(jo.toString());
			
			id = deviceDataMapper.insertSelective(deviceData);
		}
		
		if(id > 0) {
			return true;
		}else {
			return false;
		}
	}
	
	
	/**
	 * 	卡片机数据处理 （主要用于测量心电数据）
	 * 
	 * 	deviceId			// 设备 id
		heartRate        	// 心率
		ecgResult		 	// 心电经算法库得出结论
		ecgImg			 	// 心电图图片   base64
	 * 
	 * @param jb
	 * @return
	 */
	private boolean cardDataSync(Integer deviceId, JSONObject jb) {
		CardApparatusData cardApparatusData = new CardApparatusData();
		cardApparatusData.setDeviceId(deviceId);
		cardApparatusData.setHeartRateValue(Float.valueOf(jb.getString("heartRate")));
		cardApparatusData.setEcgResult("");
		cardApparatusData.setEcgImgUrl(jb.getString("ecgImg"));
		cardApparatusData.setEcgData(jb.getString("ecgResult"));
		cardApparatusData.setCreateTime(new Date());
		
		int id = cardApparatusDataMapper.insertSelective(cardApparatusData);
		
		DeviceData deviceData = new DeviceData();
		// 根据deviceId查询当前绑定的用户ID
		String managerId = deviceUserRelMapper.getManagerIdByDeviceId(deviceId);
		
		if(null != managerId && !"".equals(managerId)) {
			deviceData.setManagerId(managerId);
			deviceData.setDeviceId(deviceId);
			deviceData.setCreateTime(new Date());
			deviceData.setUpdateTime(new Date());
			deviceData.setDataFrom(DeviceItemEnum.KPJ.toString());
			
			deviceData.setDataType(DeviceItemEnum.HR.toString());
			
			JSONObject jo = new JSONObject();
			
			jo.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jo.put("name", DeviceItemEnum.HR.getName());
			jo.put("dataType", DeviceItemEnum.HR.toString());
			jo.put("value", jb.getString("heartRate"));
			jo.put("cardEcgImgUrl", jb.getString("ecgImg"));
			jo.put("ecgResult", jb.getString("ecgResult"));
			jo.put("units", DeviceItemEnum.HR.getUnit());
			
			deviceData.setDataValue(jo.toString());
			
			deviceDataMapper.insertSelective(deviceData);
		}
		
		if(id > 0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 血糖仪设备
	 * 
	 * @param jb
	 * @return
	 * @throws ParseException 
	 */
	private boolean glucometerDataSync(Integer deviceId, JSONObject jb) throws ParseException {
		
		GlucometerData glucometerData = new GlucometerData();
		glucometerData.setDeviceId(deviceId);
		glucometerData.setBloodGlucoseValue(Float.valueOf(jb.getString("value")));
		glucometerData.setCreateTime(new Date());
		
		glucometerDataMapper.insertSelective(glucometerData);
		
		Integer id = null;
		
		DeviceData deviceData = new DeviceData();
		// 根据deviceId查询当前绑定的用户ID
		String managerId = deviceUserRelMapper.getManagerIdByDeviceId(deviceId);
		
		if(null != managerId && !"".equals(managerId)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");  
			Date date = sdf.parse(jb.getString("time"));  
			
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.add(Calendar.HOUR, 8);
	        Date nowTime = calendar.getTime();
			
			deviceData.setManagerId(managerId);
			deviceData.setDeviceId(deviceId);
			deviceData.setCreateTime(nowTime);
			deviceData.setUpdateTime(nowTime);
			deviceData.setDataFrom(DeviceItemEnum.XTY.toString());
			
			deviceData.setDataType(DeviceItemEnum.BG.toString());
			
			JSONObject jo = new JSONObject();
			
			jo.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jo.put("name", DeviceItemEnum.BG.getName());
			jo.put("dataType", DeviceItemEnum.BG.toString());
			jo.put("value", jb.getString("value"));
			jo.put("units", DeviceItemEnum.BG.getUnit());
			
			deviceData.setDataValue(jo.toString());
			
			id = deviceDataMapper.insertSelective(deviceData);
		}
		
		if(id > 0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 蓝牙手环设备
	 * 
	 * @param jb
	 * @return
	 */
	private boolean braceletDataSync(Integer deviceId, JSONObject jb) {
		return false;
	}
	
	/**
	 * 蓝牙体温计设备
	 * 
	 * @param jb
	 * @return
	 * @throws ParseException 
	 */
	private boolean temperatureDataSync(Integer deviceId, JSONObject jb) throws ParseException {
		
		Integer id = null;
		
		DeviceData deviceData = new DeviceData();
		// 根据deviceId查询当前绑定的用户ID
		String managerId = deviceUserRelMapper.getManagerIdByDeviceId(deviceId);
		
		if(null != managerId && !"".equals(managerId)) {
			Date date = new Date();
			
			deviceData.setManagerId(managerId);
			deviceData.setDeviceId(deviceId);
			deviceData.setCreateTime(date);
			deviceData.setUpdateTime(date);
			deviceData.setDataFrom(DeviceItemEnum.TWJ.toString());
			
			deviceData.setDataType(DeviceItemEnum.TE.toString());
			
			JSONObject jo = new JSONObject();
			
			jo.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			jo.put("name", DeviceItemEnum.TE.getName());
			jo.put("dataType", DeviceItemEnum.TE.toString());
			jo.put("value", jb.getString("value"));
			jo.put("units", DeviceItemEnum.TE.getUnit());
			
			deviceData.setDataValue(jo.toString());
			
			id = deviceDataMapper.insertSelective(deviceData);
		}
		
		if(id > 0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
     * 设备同步接口 仅支持一体机同步
     */
	@Override
	public Map<String, Object> deviceSyncByYtj(Integer deviceId, String locationId, String type, String data, String managerId, String time) {
		Map<String, Object> result = new HashMap<String, Object>();
        // 一体机数据转 JSON
        JSONObject jb = JSONObject.parseObject(data.trim());
        // 一体机数据整合处理 
        JSONObject jsonData = new JSONObject();
        
        DeviceData deviceData = new DeviceData();
        deviceData.setDeviceId(deviceId);
		deviceData.setManagerId(managerId);
		deviceData.setIsAffirm("T");
		deviceData.setDeviceLocationid(locationId);
		
		if("ECG_data".equals(type)) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				deviceData.setCreateTime(sdf.parse(time));
				deviceData.setUpdateTime(sdf.parse(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			deviceData.setCreateTime(new Date());
			deviceData.setUpdateTime(new Date());
		}
		deviceData.setDataFrom(DeviceItemEnum.YTJ.toString());
		
		// 十二导心电数据
        if("heart_exam".equals(type)) {
        	String imgUrl = jb.get("twelve_lead_electrocardiogram").toString();
        	
			DeviceData hrData = deviceDataMapper.getDataByUserTimeByMin(deviceData.getManagerId(), time, "HR");
			
			if(null != hrData) {
				
				JSONObject jo = JSONObject.parseObject(hrData.getDataValue().trim());
    			
				JSONObject jos = new JSONObject();
				
				jos.put("date", time);
				jos.put("name", DeviceItemEnum.HR.getName());
				jos.put("dataType", DeviceItemEnum.HR.toString());
				jos.put("value", jo.getString("value"));
				jos.put("cardEcgImgUrl", imgUrl);
				jos.put("ecgResult", "");
				jos.put("units", DeviceItemEnum.HR.getUnit());
    			
    			int count = deviceDataMapper.updateHrData(managerId, time, jos.toString());
    			
    			if(count > 0) {
    				result.put("message", "添加成功");
    	            result.put("error_code", "0");
    			}else {
    				result.put("message", "添加失败");
    	            result.put("error_code", "-1");
    			}
    			return result;
    		}else {
    			result.put("message", "添加失败");
	            result.put("error_code", "-1");
	            return result;
    		}
        }else if("body_composition_analysis".equals(type)) {
//          // height
//      	String height = jb.get("height").toString();
//          // weight
			String weight = jb.get("weight").toString();
//          // BMI
//      	String bmi = jb.get("BMI").toString();
//          // body_fat_percentage
			String bodyFatPercentage = jb.get("body_fat_percentage").toString();
//          // water_percentage
			String waterPercentage = jb.get("water_percentage").toString();
//          // muscle_content
			String muscleContent = jb.get("muscle_content").toString();
//          // fat_content
//      	String fatContent = jb.get("fat_content").toString();
//          // basic_metabolism_rate
			String basicMetabolismRate = jb.get("basic_metabolism_rate").toString();
//          // visceral_fat_index
			String visceralFatIndex = jb.get("visceral_fat_index").toString();
//          // standard_weight
//      	String standardWeight = jb.get("standard_weight").toString();
//          // fat_control
			String fatControl = jb.get("fat_control").toString();
//          // muscle_control
//			String muscleControl = jb.get("muscle_control").toString();
//          // weight_control
//      	String weightControl = jb.get("weight_control").toString();
//          // bone_weight
			String boneWeight = jb.get("bone_weight").toString();
//          // fat_level
//      	String fatLevel = jb.get("fat_level").toString();
//          // health_score
//      	String healthScore = jb.get("health_score").toString();
//          // degreased_body_weight
//      	String degreasedBodyWeight = jb.get("degreased_body_weight").toString();
//          // body_type
//      	String bodyType = jb.get("body_type").toString();
//          // protein_weight
			String proteinWeight = jb.get("protein_weight").toString();
      	
			deviceData.setDataValue(jb.toString());
			deviceData.setDataType(DeviceItemEnum.BODY.toString());
			deviceDataMapper.insertSelective(deviceData);
			
			jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
			
			// 水分比
			if(null != waterPercentage || !"".equals(waterPercentage)) {
				jsonData.put("value", waterPercentage);
				jsonData.put("units", DeviceItemEnum.WP.getUnit());
				jsonData.put("name", DeviceItemEnum.WP.getName());
				jsonData.put("dataType", DeviceItemEnum.WP.toString());
				
				deviceData.setDataValue(jsonData.toString());
				deviceData.setDataType(DeviceItemEnum.WP.toString());
				deviceDataMapper.insertSelective(deviceData);
			}
			
			// 肌肉
			if(null != muscleContent || !"".equals(muscleContent)) {
				jsonData.put("weight", weight);
				
				jsonData.put("value", muscleContent);
				jsonData.put("units", DeviceItemEnum.MUCALE.getUnit());
				jsonData.put("name", DeviceItemEnum.MUCALE.getName());
				jsonData.put("dataType", DeviceItemEnum.MUCALE.toString());
				
				deviceData.setDataValue(jsonData.toString());
				deviceData.setDataType(DeviceItemEnum.MUCALE.toString());
				deviceDataMapper.insertSelective(deviceData);
			}
			
			// 骨量
			if(null != boneWeight || !"".equals(boneWeight)) {
				jsonData.put("value", boneWeight);
				jsonData.put("units", DeviceItemEnum.BONE.getUnit());
				jsonData.put("name", DeviceItemEnum.BONE.getName());
				jsonData.put("dataType", DeviceItemEnum.BONE.toString());
				
				deviceData.setDataValue(jsonData.toString());
				deviceData.setDataType(DeviceItemEnum.BONE.toString());
				deviceDataMapper.insertSelective(deviceData);
			}
			
			// 基础代谢率
			if(null != basicMetabolismRate || !"".equals(basicMetabolismRate)) {
				jsonData.put("value", basicMetabolismRate);
				jsonData.put("units", DeviceItemEnum.BMR.getUnit());
				jsonData.put("name", DeviceItemEnum.BMR.getName());
				jsonData.put("dataType", DeviceItemEnum.BMR.toString());
				
				deviceData.setDataValue(jsonData.toString());
				deviceData.setDataType(DeviceItemEnum.BMR.toString());
				deviceDataMapper.insertSelective(deviceData);
			}
			
			// 蛋白质
			if(null != proteinWeight || !"".equals(proteinWeight)) {
				jsonData.put("weight", weight);
				
				jsonData.put("value", proteinWeight);
				jsonData.put("units", DeviceItemEnum.PIW.getUnit());
				jsonData.put("name", DeviceItemEnum.PIW.getName());
				jsonData.put("dataType", DeviceItemEnum.PIW.toString());
				
				deviceData.setDataValue(jsonData.toString());
				deviceData.setDataType(DeviceItemEnum.PIW.toString());
				deviceDataMapper.insertSelective(deviceData);
			}
			
			// 体脂
			if(null != bodyFatPercentage || !"".equals(bodyFatPercentage)) {
				jsonData.put("fc", fatControl);
				jsonData.put("vfp", visceralFatIndex);
				
				jsonData.put("value", bodyFatPercentage);
				jsonData.put("units", DeviceItemEnum.FP.getUnit());
				jsonData.put("name", DeviceItemEnum.FP.getName());
				jsonData.put("dataType", DeviceItemEnum.FP.toString());
				
				deviceData.setDataValue(jsonData.toString());
				deviceData.setDataType(DeviceItemEnum.FP.toString());
				deviceDataMapper.insertSelective(deviceData);
			}
			
			result.put("message", "添加成功");
            result.put("error_code", "0");
			return result;
			
		}else {
			jsonData = disposeData(deviceData, type, jb);
		}
		
		switch(type) {
			// 血压
			case "blood_pressure":
				deviceData.setDataType(DeviceItemEnum.BP.toString());
				break;
			// 血氧饱和度
			case "blood_oxygen":
				deviceData.setDataType(DeviceItemEnum.BO.toString());
				break;
			// 体温
			case "temperature":
				deviceData.setDataType(DeviceItemEnum.TE.toString());
				break;
			// 尿常规
			case "urine_routine":
				deviceData.setDataType(DeviceItemEnum.RU.toString());
				break;		
			// 血糖
			case "glucose_metabolism_analysis":
				deviceData.setDataType(DeviceItemEnum.BG.toString());
				break;	
			// 总胆固醇
			case "total_cholesterol_analysis":
				deviceData.setDataType(DeviceItemEnum.CHOL.toString());
				break;	
			//  血液尿酸
			case "renal_function":
				deviceData.setDataType(DeviceItemEnum.UA.toString());
				break;	
			//  血红蛋白
			case "BloodRoutineTest":
				deviceData.setDataType(DeviceItemEnum.BHB.toString());
				break;	
			//  ECG 数据上传
			case "ECG_data":
				deviceData.setDataType(DeviceItemEnum.ECG.toString());
				break;	
			//  体脂称 健康数据
			case "body_composition_analysis":
				//deviceData.setDataType(DeviceItemEnum.BODY.toString());
				break;	
			//  血脂
			case "blood_lipid_level_analysis":
				deviceData.setDataType(DeviceItemEnum.BF.toString());
				break;	
			//  血常规
			case "blood_routine":
				deviceData.setDataType(DeviceItemEnum.BR.toString());
				break;	
			//  十二导心电 heart_exam
			default:
				break;	
		}
		
        deviceData.setDataValue(jsonData.toString());
		
		int count = deviceDataMapper.insertSelective(deviceData);
		
		if(count > 0) {
			result.put("message", "添加成功");
            result.put("error_code", "0");
		}else {
			result.put("message", "添加失败");
            result.put("error_code", "-1");
		}
		return result;
	}
	
	/**
	 * 处理数据
	 * @param type
	 * @param jb
	 * @return
	 */
	public JSONObject disposeData(DeviceData deviceData, String type, JSONObject jb) {
		JSONObject jsonData = new JSONObject();
		switch (type) {
			// 血压数据
			case "blood_pressure":
				
				// 舒张压 diastolic_blood_pressure
	        	String diastolicBloodPressure = jb.get("diastolic_blood_pressure").toString();
	            // 收缩压 systolic_blood_pressure
	        	String systolicBloodPressure = jb.get("systolic_blood_pressure").toString();
	            // 脉率 pulse_rate
	        	String pulseRate = jb.get("pulse_rate").toString();
	        	
	        	jsonData.put("dia", diastolicBloodPressure);
	        	jsonData.put("sys", systolicBloodPressure);
	        	jsonData.put("pr", pulseRate);
				
	        	jsonData.put("value", systolicBloodPressure + "/" + diastolicBloodPressure);
	        	jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
	        	jsonData.put("units", DeviceItemEnum.BP.getUnit());
	        	jsonData.put("name", DeviceItemEnum.BP.getName());
	        	jsonData.put("dataType", DeviceItemEnum.BP.toString());
				
				break;
			// 血氧数据
			case "blood_oxygen":
				
				// 血氧饱和度 blood_oxygen
	        	String bloodOxygen = jb.get("blood_oxygen").toString();
	            // 脉率 pulse_rate
	            String pr = jb.get("pulse_rate").toString();
	            
	            jsonData.put("pr", pr);
	            
	            jsonData.put("value", bloodOxygen);
	            jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
	            jsonData.put("units", DeviceItemEnum.BO.getUnit());
	            jsonData.put("name", DeviceItemEnum.BO.getName());
	            jsonData.put("dataType", DeviceItemEnum.BO.toString());
				
				break;
			// 体温数据
			case "temperature":
				
				// 体温 temperature
	        	String temperatureValue = jb.get("temperature").toString();
	        	
				jsonData.put("value", temperatureValue);
				jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
				jsonData.put("units", DeviceItemEnum.TE.getUnit());
				jsonData.put("name", DeviceItemEnum.TE.getName());
				jsonData.put("dataType", DeviceItemEnum.TE.toString());
				
				break;
			// 尿常规数据
			case "urine_routine":
				
//	            // 尿胆原 urobilinogen
//	        	String urobilinogen = jb.get("urobilinogen").toString();
//	            // 尿潜血 urine_occult_blood
//	        	String urineOccultBlood = jb.get("urine_occult_blood").toString();
//	            // 尿胆红素 urine_bilirubin
//	        	String urineBilirubin = jb.get("urine_bilirubin").toString();
//	            // 尿酮体 urine_ketone
//	        	String urineKetone = jb.get("urine_ketone").toString();
//	            // 尿葡萄糖 urine_glucose
//	        	String urineGlucose = jb.get("urine_glucose").toString();
//	            // 尿蛋白 urine_protein
//	        	String urineProtein = jb.get("urine_protein").toString();
//	            // 尿酸碱度 urine_PH
//	        	String urinePh = jb.get("urine_PH").toString();
//	            // 亚硝酸盐 urine_nitrite
//	        	String urineNitrite = jb.get("urine_nitrite").toString();
//	            // 尿液白细胞 urine_leukocytes
//	        	String urineLeukocytes = jb.get("urine_leukocytes").toString();
//	            // 尿比重 urine_specific_gravity
//	        	String urineSpecificGravity = jb.get("urine_specific_gravity").toString();
//	            // 尿液维生素C urine_VitC
//	        	String urineVitc = jb.get("urine_VitC").toString();
	        	
	        	jsonData.put("value", jb);
	            jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
	            jsonData.put("units", DeviceItemEnum.RU.getUnit());
	            jsonData.put("name", DeviceItemEnum.RU.getName());
	            jsonData.put("dataType", DeviceItemEnum.RU.toString());
				
				break;
			// 血糖数据
			case "glucose_metabolism_analysis":
				
				// 血糖 blood_glucose
	        	String bloodGlucose = jb.get("blood_glucose").toString();
				
	        	jsonData.put("value", bloodGlucose);
	            jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
	            jsonData.put("units", DeviceItemEnum.BG.getUnit());
	            jsonData.put("name", DeviceItemEnum.BG.getName());
	            jsonData.put("dataType", DeviceItemEnum.BG.toString());
	            jsonData.put("flag", jb.get("flag").toString());
				
				break;
			// 总胆固醇数据
			case "total_cholesterol_analysis":
				
				// 总胆固醇 total_cholesterol
	        	String totalCholesterol = jb.get("total_cholesterol").toString();
	        	
	        	jsonData.put("value", totalCholesterol);
	            jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
	            jsonData.put("units", DeviceItemEnum.CHOL.getUnit());
	            jsonData.put("name", DeviceItemEnum.CHOL.getName());
	            jsonData.put("dataType", DeviceItemEnum.CHOL.toString());		
				
				break;
			// 血液尿酸数据
			case "renal_function":
				
				// 血液尿酸 serum_uric_acid
	        	String urineAcid = jb.get("urine_uric_acid").toString();
	        	
	        	jsonData.put("value", urineAcid);
	            jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
	            jsonData.put("units", DeviceItemEnum.UA.getUnit());
	            jsonData.put("name", DeviceItemEnum.UA.getName());
	            jsonData.put("dataType", DeviceItemEnum.UA.toString());
				
				break;
			// 血红蛋白数据
			case "BloodRoutineTest":
				
	            // 血红蛋白浓度(HGB) Hemoglobin
	        	String bloodHemoglobin = jb.get("Hemoglobin").toString();
	            // 红细比容(HCT) Hematocrit
	        	String bloodHematocrit = jb.get("Hematocrit").toString();
	        	
	        	jsonData.put("hct", bloodHematocrit);
	        	
	        	jsonData.put("hb", bloodHemoglobin);
	            jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
	            jsonData.put("units", DeviceItemEnum.BHB.getUnit());
	            jsonData.put("name", DeviceItemEnum.BHB.getName());
	            jsonData.put("dataType", DeviceItemEnum.BHB.toString());
				
				break;
			// ECG_data数据
			case "ECG_data":
				
//	            // ECG_rr
//	        	String rr = jb.get("ECG_rr").toString();
//	            // ECG_p_r
//	        	String pr = jb.get("ECG_p_r").toString();
//	            // ECG_qrs
//	        	String qrs = jb.get("ECG_qrs").toString();
//	            // ECG_qt
//	        	String qt = jb.get("ECG_qt").toString();
//	            // ECG_qtc
//	        	String qtc = jb.get("ECG_qtc").toString();
//	            // ECG_p_axis
//	        	String pAxis = jb.get("ECG_p_axis").toString();
//	            // ECG_qrs_axis
//	        	String qrsAxis = jb.get("ECG_qrs_axis").toString();
//	            // ECG_t_axis
//	        	String tAxis = jb.get("ECG_t_axis").toString();
//	            // ECG_rv5
//	        	String rv5 = jb.get("ECG_rv5").toString();
//	            // ECG_sv1
//	        	String sv1 = jb.get("ECG_sv1").toString();
//	            // ECG_rv5_svl
//	        	String rv5Svl = jb.get("ECG_rv5_svl").toString();
//	            // heart_rate
	        	String hr = jb.get("heart_rate").toString();
	        	
	        	jsonData.put("value", jb);
	            jsonData.put("date", DateTimeUtil.getFormatDateTime(deviceData.getCreateTime()));
	            jsonData.put("units", DeviceItemEnum.ECG.getUnit());
	            jsonData.put("name", DeviceItemEnum.ECG.getName());
	            jsonData.put("dataType", DeviceItemEnum.ECG.toString());
	            
	            if(null != hr) {
	    			deviceData.setDataType(DeviceItemEnum.HR.toString());
	    			
	    			JSONObject hrObject = new JSONObject();
	    			
	    			hrObject.put("date", DateTimeUtil.getFormatDateTime(new Date()));
	    			hrObject.put("name", DeviceItemEnum.HR.getName());
	    			hrObject.put("dataType", DeviceItemEnum.HR.toString());
	    			hrObject.put("units", DeviceItemEnum.HR.getUnit());
	    			hrObject.put("value", hr);
	    			hrObject.put("cardEcgImgUrl", "");
	    			hrObject.put("ecgResult", "");
	    			
	    			deviceData.setDataValue(hrObject.toString());
	    			deviceDataMapper.insertSelective(deviceData);
	            }
				break;
			// 体脂称数据
			case "body_composition_analysis":
				
				break;
			// 血脂数据
			case "blood_lipid_level_analysis":
				
	            // triglyceride 甘油三酯
	        	String TG = jb.get("triglyceride").toString();
	            // total_cholesterol 总胆固醇
	        	String CHOL = jb.get("total_cholesterol").toString();
	            // low_density_lipoprotein 低密度脂蛋白胆固醇
	        	String LDL = jb.get("low_density_lipoprotein").toString();
	            // high_density_lipoprotein 高密度脂蛋白胆固醇
	        	String HDL = jb.get("high_density_lipoprotein").toString();
	            // cholesterol_esters
	        	String cholesterolEsters = jb.get("cholesterol_esters").toString();
	        	
	        	jsonData.put("TG", TG);
	        	jsonData.put("LDL", LDL);
	        	jsonData.put("HDL", HDL);
	        	jsonData.put("CE", cholesterolEsters);
	        	
	        	jsonData.put("chol", CHOL);
	            jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
	            jsonData.put("units", "mmol/L");
	            jsonData.put("name", DeviceItemEnum.BF.getName());
	            jsonData.put("dataType", DeviceItemEnum.BF.toString());
				
				break;
			// 血常规数据		
			case "blood_routine":
				
//	            // WBC
//	            String WBC = jb.get("white_blood_cell_count").toString();
//	            if("****".equals(WBC)){
//	                WBC = null;
//	            }else{
//	            }
//	            // RBC
//	            String RBC = jb.get("red_blood_cell_count").toString();
//	            if("****".equals(RBC)){
//	                RBC = null;
//	            }else{
//	            }
//	            // HB
//	            String HB = jb.get("hemoglobin").toString();
//	            if("****".equals(HB)){
//	                HB = null;
//	            }else{
//	            }
//	            // HCT
//	            String HCT = jb.get("hematocrit").toString();
//	            if("****".equals(HCT)){
//	                HCT = null;
//	            }else{
//	            }
//	            // MCV
//	            String MCV = jb.get("red_blood_cell_mean_corpuscular_volume").toString();
//	            if("****".equals(MCV)){
//	                MCV = null;
//	            }else{
//	            }
//	            // MCH
//	            String MCH = jb.get("mean_corpuscular_hemoglobin").toString();
//	            if("****".equals(MCH)){
//	                MCH = null;
//	            }else{
//	            }
//	            // MCHC
//	            String MCHC = jb.get("mean_corpuscular_hemoglobin_concentration").toString();
//	            if("****".equals(MCHC)){
//	                MCHC = null;
//	            }else{
//	            }
//	            // PLT
//	            String PLT = jb.get("platelet_count").toString();
//	            if("****".equals(PLT)){
//	                PLT = null;
//	            }else{
//	            }
//	            // LYM_ratio
//	            String LYM_ratio = jb.get("lymphocyte_ratio").toString();
//	            if("****".equals(LYM_ratio)){
//	                LYM_ratio = null;
//	            }else{
//	            }
//	            // MID_ratio
//	            String MID_ratio = jb.get("intermediate_cell_ratio").toString();
//	            if("****".equals(MID_ratio)){
//	                MID_ratio = null;
//	            }else{
//	            }
//	            // GRAN_ratio
//	            String GRAN_ratio = jb.get("neutrophils_ratio").toString();
//	            if("****".equals(GRAN_ratio)){
//	                GRAN_ratio = null;
//	            }else{
//	            }
//	            // LYM_tatol
//	            String LYM_tatol = jb.get("lymphocytes").toString();
//	            if("****".equals(LYM_tatol)){
//	                LYM_tatol = null;
//	            }else{
//	            }
//	            // MID_tatol
//	            String MID_tatol = jb.get("intermediate_cell").toString();
//	            if("****".equals(MID_tatol)){
//	                MID_tatol = null;
//	            }else{
//	            }
//	            // GRAN_tatol
//	            String GRAN_tatol = jb.get("neutrophils").toString();
//	            if("****".equals(GRAN_tatol)){
//	                GRAN_tatol = null;
//	            }else{
//	            }
//	            // RDW_SD
//	            String RDW_SD = jb.get("red_blood_cell_volume_distributing_width_standard_deviation").toString();
//	            if("****".equals(RDW_SD)){
//	                RDW_SD = null;
//	            }else{
//	            }
//	            // RDW_CV
//	            String RDW_CV = jb.get("red_blood_cell_volume_distributing_width_coefficient_variation").toString();
//	            if("****".equals(RDW_CV)){
//	                RDW_CV = null;
//	            }else{
//	            }
//	            // PDW
//	            String PDW = jb.get("platelet_distribution_width").toString();
//	            if("****".equals(PDW)){
//	                PDW = null;
//	            }else{
//	            }
//	            // MPV
//	            String MPV = jb.get("mean_platelet_volume").toString();
//	            if("****".equals(MPV)){
//	                MPV = null;
//	            }else{
//	            }
//	            // PCT
//	            String PCT = jb.get("thrombocytocrit").toString();
//	            if("****".equals(PCT)){
//	                PCT = null;
//	            }else{
//	            }
//	            // P_LCR
//	            String P_LCR = jb.get("platelet_large_cell_ratio").toString();
//	            if("****".equals(P_LCR)){
//	                P_LCR = null;
//	            }else{
//	            }
//	            // P_LCC
//	            String P_LCC = jb.get("platelet_large_cell_count").toString();
//	            if("****".equals(P_LCC)){
//	                P_LCC = null;
//	            }else{
//	            }
	            
//	            jsonData.put("value", jb);
//	            jsonData.put("date", DateTimeUtil.getFormatDateTime(new Date()));
//	            jsonData.put("units", DeviceItemEnum.BR.getUnit());
//	            jsonData.put("name", DeviceItemEnum.BR.getName());
//	            jsonData.put("dataType", DeviceItemEnum.BR.toString());
				
				break;
			default:
				break;
			}
		return jsonData;
	}
	
	/**
	 * 设备同步接口 主要同步 免疫定量分析仪
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result deviceSyncByImmunoassay(Integer deviceId, String managerId, String data, String type) {
		Result result = new Result<>(StatusCodes.OK, true);
		
		JSONObject value = JSONObject.parseObject(data.trim());
		JSONObject jb = JSONObject.parseObject(value.getString("value").trim());

		DeviceData deviceData = new DeviceData();
		
		deviceData.setManagerId(managerId);
		deviceData.setDeviceId(deviceId);
		deviceData.setCreateTime(new Date());
		deviceData.setUpdateTime(new Date());
		deviceData.setDataFrom(DeviceItemEnum.MY.toString());
		
		JSONObject jo = new JSONObject();
		jo.put("date", DateTimeUtil.getFormatDateTime(new Date()));
		
		switch (type) {
			// cTnI 心肌肌钙蛋白I
			case "1":
				deviceData.setDataType(DeviceItemEnum.cTnI.toString());
				
				jo.put("name", DeviceItemEnum.cTnI.getName());
				jo.put("dataType", DeviceItemEnum.cTnI.toString());
				jo.put("value", jb.getString("cTnI"));
				jo.put("units", DeviceItemEnum.cTnI.getUnit());
				
				break;
			// NT-proBNP N-端脑利钠肽前体
			case "2":
				deviceData.setDataType(DeviceItemEnum.NT_proBNP.toString());
				
				jo.put("name", DeviceItemEnum.NT_proBNP.getName());
				jo.put("dataType", DeviceItemEnum.NT_proBNP.toString());
				jo.put("value", jb.getString("NT_proBNP"));
				jo.put("units", DeviceItemEnum.NT_proBNP.getUnit());
				
				break;
			// CRP  hs-CRP : 超敏C反应蛋白  CRP : C反应蛋白
			case "3": 
				deviceData.setDataType(DeviceItemEnum.CRP.toString());
				
				jo.put("name", DeviceItemEnum.hs_CRP.getName());
				jo.put("dataType", DeviceItemEnum.CRP.toString());
				jo.put("value", jb.getString("hs-CRP"));
				jo.put("CRP", jb.getString("CRP"));
				jo.put("units", DeviceItemEnum.hs_CRP.getUnit());
				
				break;
			// cTnI+NT-proBNP  cTnI : 心肌肌钙蛋白I  NT-proBNP : N-端脑利钠肽前体
			case "4":
				deviceData.setDataType(DeviceItemEnum.cTnI_NT_proBNP.toString());
				
				jo.put("name", DeviceItemEnum.cTnI.getName());
				jo.put("dataType", DeviceItemEnum.cTnI_NT_proBNP.toString());
				jo.put("value", jb.getString("cTnI"));
				jo.put("NT_proBNP", jb.getString("NT-proBNP"));
				jo.put("units", DeviceItemEnum.cTnI.getUnit());
				
				break;
			// cTnI+CKMB+Myo  CK-MB : 肌酸激酶同工酶  cTnI : 心肌肌钙蛋白I  Myo : 肌红蛋白
			case "5":
				deviceData.setDataType(DeviceItemEnum.cTnI_CKMB_Myo.toString());
				
				jo.put("name", DeviceItemEnum.CK_MB.getName());
				jo.put("dataType", DeviceItemEnum.cTnI_CKMB_Myo.toString());
				jo.put("value", jb.getString("CK-MB"));
				jo.put("cTnI", jb.getString("cTnI"));
				jo.put("Myo", jb.getString("Myo"));
				jo.put("units", DeviceItemEnum.CK_MB.getUnit());
				
				break;
			// D-Dimer D-二聚体
			case "6":
				deviceData.setDataType(DeviceItemEnum.D_Dimer.toString());
				
				jo.put("name", DeviceItemEnum.D_Dimer.getName());
				jo.put("dataType", DeviceItemEnum.D_Dimer.toString());
				jo.put("value", jb.getString("D-Dimer"));
				jo.put("units", DeviceItemEnum.D_Dimer.getUnit());
				
				break;
			// PCT 降钙素原
			case "7":
				deviceData.setDataType(DeviceItemEnum.PCT.toString());
				
				jo.put("name", DeviceItemEnum.PCT.getName());
				jo.put("dataType", DeviceItemEnum.PCT.toString());
				jo.put("value", jb.getString("PCT"));
				jo.put("units", DeviceItemEnum.PCT.getUnit());
				
				break;
			// mAlb 微量白蛋白
			case "8":
				deviceData.setDataType(DeviceItemEnum.mAlb.toString());
				
				jo.put("name", DeviceItemEnum.mAlb.getName());
				jo.put("dataType", DeviceItemEnum.mAlb.toString());
				jo.put("value", jb.getString("mAlb"));
				jo.put("units", DeviceItemEnum.mAlb.getUnit());
				
				break;
			// NGAL 中性粒细胞明胶酶相关脂质运载蛋白
			case "9":
				deviceData.setDataType(DeviceItemEnum.NGAL.toString());
				
				jo.put("name", DeviceItemEnum.NGAL.getName());
				jo.put("dataType", DeviceItemEnum.NGAL.toString());
				jo.put("value", jb.getString("NGAL"));
				jo.put("units", DeviceItemEnum.NGAL.getUnit());
				
				break;
			// CysC 胱抑素C
			case "10":
				deviceData.setDataType(DeviceItemEnum.CysC.toString());
				
				jo.put("name", DeviceItemEnum.CysC.getName());
				jo.put("dataType", DeviceItemEnum.CysC.toString());
				jo.put("value", jb.getString("CysC"));
				jo.put("units", DeviceItemEnum.CysC.getUnit());
				
				break;
			// β2-MG β2-微球蛋白
			case "11":
				deviceData.setDataType(DeviceItemEnum.β2_MG.toString());
				
				jo.put("name", DeviceItemEnum.β2_MG.getName());
				jo.put("dataType", DeviceItemEnum.β2_MG.toString());
				jo.put("value", jb.getString("β2-MG"));
				jo.put("units", DeviceItemEnum.β2_MG.getUnit());
				
				break;
			// hs-CRP 高敏C反应蛋白
			case "12":
				deviceData.setDataType(DeviceItemEnum.hs_CRP.toString());
				
				jo.put("name", DeviceItemEnum.hs_CRP.getName());
				jo.put("dataType", DeviceItemEnum.hs_CRP.toString());
				jo.put("value", jb.getString("hs-CRP"));
				jo.put("units", DeviceItemEnum.hs_CRP.getUnit());
				
				break;
			// HCG 人绒毛膜促性腺激素
			case "13":
				deviceData.setDataType(DeviceItemEnum.HCG.toString());
				
				jo.put("name", DeviceItemEnum.HCG.getName());
				jo.put("dataType", DeviceItemEnum.HCG.toString());
				jo.put("value", jb.getString("HCG"));
				jo.put("units", DeviceItemEnum.HCG.getUnit());
				
				break;
			// H-FABP 心型脂肪酸结合蛋白
			case "14":
				deviceData.setDataType(DeviceItemEnum.H_FABP.toString());
				
				jo.put("name", DeviceItemEnum.H_FABP.getName());
				jo.put("dataType", DeviceItemEnum.H_FABP.toString());
				jo.put("value", jb.getString("H-FABP "));
				jo.put("units", DeviceItemEnum.H_FABP.getUnit());
				
				break;
			// BNP 脑钠肽
			case "15":
				deviceData.setDataType(DeviceItemEnum.BNP.toString());
				
				jo.put("name", DeviceItemEnum.BNP.getName());
				jo.put("dataType", DeviceItemEnum.BNP.toString());
				jo.put("value", jb.getString("BNP"));
				jo.put("units", DeviceItemEnum.BNP.getUnit());
				
				break;
			// PCT/CRP  降钙素原 超敏C反应蛋白
			case "16":
				deviceData.setDataType(DeviceItemEnum.PCT_CRP.toString());
				
				jo.put("name", DeviceItemEnum.PCT_CRP.getName());
				jo.put("dataType", DeviceItemEnum.PCT_CRP.toString());
				jo.put("value", jb.getString("PCT"));
				jo.put("CRP", jb.getString("CRP"));
				jo.put("units", DeviceItemEnum.PCT_CRP.getUnit());
				
				break;
			// CK-MB/cTnI/H-FABP 肌酸激酶同工酶 心肌肌钙蛋白I 心型脂肪酸结合蛋白
			case "17":
				deviceData.setDataType(DeviceItemEnum.CK_MB_cTnI_H_FABP.toString());
				
				jo.put("name", DeviceItemEnum.CK_MB.getName());
				jo.put("dataType", DeviceItemEnum.CK_MB_cTnI_H_FABP.toString());
				jo.put("value", jb.getString("CK-MB"));
				jo.put("cTnI", jb.getString("cTnI"));
				jo.put("H_FABP", jb.getString("H-FABP"));
				jo.put("units", DeviceItemEnum.CK_MB.getUnit());
				
				break;
			// NT-proBNP/BNP N-端脑利钠肽前体 脑钠肽
			case "18":
				deviceData.setDataType(DeviceItemEnum.NT_proBNP_BNP.toString());
				
				jo.put("name", DeviceItemEnum.NT_proBNP.getName());
				jo.put("dataType", DeviceItemEnum.NT_proBNP.toString());
				jo.put("value", jb.getString("NT-proBNP"));
				jo.put("BNP", jb.getString("BNP"));
				jo.put("units", DeviceItemEnum.NT_proBNP.getUnit());
				
				break;
			// NGAL/mAlb 中性粒细胞明胶酶相关脂质运载蛋白 微量白蛋白
			case "19":
				deviceData.setDataType(DeviceItemEnum.NGAL_mAlb.toString());
				
				jo.put("name", DeviceItemEnum.NGAL.getName());
				jo.put("dataType", DeviceItemEnum.NGAL_mAlb.toString());
				jo.put("value", jb.getString("NGAL"));
				jo.put("mAlb", jb.getString("mAlb"));
				jo.put("units", DeviceItemEnum.NGAL.getUnit());
				
				break;
			// HbA1c 糖化血红蛋白
			case "20":
				deviceData.setDataType(DeviceItemEnum.HbA1c.toString());
				
				jo.put("name", DeviceItemEnum.HbA1c.getName());
				jo.put("dataType", DeviceItemEnum.HbA1c.toString());
				jo.put("value", jb.getString("HbA1c"));
				jo.put("units", DeviceItemEnum.HbA1c.getUnit());
				
				break;
			// h-cTnI 高敏心肌肌钙蛋白I
			case "21":
				deviceData.setDataType(DeviceItemEnum.h_cTnI.toString());
				
				jo.put("name", DeviceItemEnum.h_cTnI.getName());
				jo.put("dataType", DeviceItemEnum.h_cTnI.toString());
				jo.put("value", jb.getString("h-cTnI"));
				jo.put("units", DeviceItemEnum.h_cTnI.getUnit());
				
				break;	
			default:
				break;
		}
		
		deviceData.setDataValue(jo.toString());
		int num = deviceDataMapper.insertSelective(deviceData);
		if(num > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "添加成功"));
		}else {
			result.setResultCode(new ResultCode("FAILED", "添加失败"));
		}
		return result;
	}
}