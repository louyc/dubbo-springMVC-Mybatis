package com.semioe.dubbo.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.model.WeixinPayAccount;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.token.RedisTokenStorager;
import com.foxinmy.weixin4j.token.TokenHolder;
import com.foxinmy.weixin4j.util.Weixin4jSettings;
import com.semioe.api.entity.ApiUserInfo;
import com.semioe.api.entity.BackstageUserInfo;
import com.semioe.api.entity.DeviceUserRel;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.common.tools.util.ConnUtil;
import com.semioe.dubbo.dao.ApiUserInfoMapper;
import com.semioe.dubbo.dao.BackstageUserInfoMapper;
import com.semioe.dubbo.dao.DeviceUserRelMapper;
import com.semioe.dubbo.service.DeviceBindingUserService;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class DeviceBindingUserServiceImpl implements DeviceBindingUserService {

	private static final Logger logger = LoggerFactory.getLogger(DeviceBindingUserServiceImpl.class);
	
	@Autowired
	private DeviceUserRelMapper deviceUserRelMapper;
	@Autowired
	private BackstageUserInfoMapper backstageUserInfoMapper;
	@Autowired
	private ApiUserInfoMapper apiUserInfoMapper;
	
	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	
	private WeixinProxy weixinProxy;
	
	/**
	 * 设备绑定 设备ID 用户ID
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result deviceBindingUser(String managerId, Integer deviceId) {
		Result result = new Result<>(StatusCodes.OK, true);
		Date date = new Date();

		DeviceUserRel deviceUserRel = new DeviceUserRel();

		deviceUserRel.setDeviceId(deviceId);
		deviceUserRel.setUpdateTime(date);
		// 设备之前绑定解绑
		deviceUserRelMapper.updateRelNotBinding(deviceUserRel);

		deviceUserRel.setManagerId(managerId);
		// 设备 + 当前用户 是否有绑定关系
		DeviceUserRel device = deviceUserRelMapper.getDeviceUserRelByMaDe(deviceUserRel);
		// 是否设备管理员
		BackstageUserInfo user = backstageUserInfoMapper.selectByPrimaryKey(managerId);
		if (null != user && user.getUserStatus() == 1 && user.getRoleId() == 5) { // 设备管理员
			deviceUserRel.setIsBinding("A");
		} else {
			deviceUserRel.setIsBinding("T");
		}
		deviceUserRel.setDeviceNetType("F");
		int num = 0;
		if (null == device) {
			deviceUserRel.setCreateTime(date);
			num = deviceUserRelMapper.insertSelective(deviceUserRel);
		} else {
			num = deviceUserRelMapper.relieveBinding(deviceUserRel);
		}
		if (num > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
		}

		return result;
	}

	/**
	 * 根据 用户ID 获取改用户已绑定设备
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result getDeviceBindingList(String managerId) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null == managerId || "".equals(managerId)) {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
			return result;
		}

		List<DeviceUserRel> deviceUserRelList = deviceUserRelMapper.getDeviceBindingList(managerId);

		result.setData(deviceUserRelList);
		result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));

		return result;
	}

	/**
	 * 根据 设备id 获取用户信息
	 */
	@Override
	public DeviceUserRel getDeviceIsBindingByDeviceId(Integer deviceId) {

		DeviceUserRel deviceUser = deviceUserRelMapper.getDeviceIsBindingByDeviceId(deviceId);

		return deviceUser;
	}

	/**
	 * 取消绑定
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result relieveBinding(String managerId, String deviceId, String type, String netWorking, String hostName, int port, String Appid, String Secret,
			String Paysignkey, String Mchid, String deviceCode) {
		Result result = new Result<>(StatusCodes.OK, true);

		if (null == managerId || "".equals(managerId)) {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
			return result;
		}

		if (null == deviceId || "".equals(deviceId)) {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
			return result;
		}

		if (null == type || "".equals(type)) {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
			return result;
		}

		if (!"T".equals(type) && !"F".equals(type)) {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
			return result;
		}

		DeviceUserRel deviceUserRel = new DeviceUserRel();
		deviceUserRel.setManagerId(managerId);
		deviceUserRel.setDeviceId(Integer.parseInt(deviceId));
		deviceUserRel.setIsBinding(type);
		deviceUserRel.setUpdateTime(new Date());

		int num = deviceUserRelMapper.relieveBinding(deviceUserRel);

		if("0".equals(netWorking)) {
			logger.info("蓝牙设备解绑返回Start" );
			
			ApiUserInfo apiUserInfo = apiUserInfoMapper.selectByPrimaryKey(managerId);
			if(null != apiUserInfo) {
				try {
					weixinProxy = getWeixinProxy(hostName, port,  Appid,  Secret, Paysignkey,  Mchid);
					TokenHolder tokenHolder = weixinProxy.getTokenHolder();
	
					String token = tokenHolder.getAccessToken();
					String url = "https://api.weixin.qq.com/device/compel_unbind?access_token=" + token;
					
					String sqBack = ConnUtil.sendPostWithOutHeaderAndJson(url, "{\"device_id\":\""+ deviceCode +"\",\"openid\":\""+ apiUserInfo.getOpenId() +"\"}");
					
					JSONObject jb = JSONObject.parseObject(sqBack.trim());
					
					logger.info("蓝牙设备解绑返回JSON：" + jb);
					
				} catch (WeixinException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		if (num > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
		}

		return result;
	}

	/**
	 * 根据 设备ID 用户ID 修改联网状态
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result changeNetworking(String managerId, Integer deviceId, String deviceNetType) {
		Result result = new Result<>(StatusCodes.OK, true);

		DeviceUserRel deviceUserRel = new DeviceUserRel();

		deviceUserRel.setDeviceId(deviceId);
		deviceUserRel.setManagerId(managerId);
		deviceUserRel.setNetworkingType(deviceNetType);
		deviceUserRel.setUpdateTime(new Date());

		int num = deviceUserRelMapper.changeNetworking(deviceUserRel);

		if (num > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
		}

		return result;
	}

	/**
	 * 解除用户设备关系
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result deleteBingingUser(Integer id) {
		Result result = new Result<>(StatusCodes.OK, true);

		DeviceUserRel deviceUserRel = new DeviceUserRel();

		deviceUserRel.setDeviceId(id);
		deviceUserRel.setIsBinding("F");
		deviceUserRel.setUpdateTime(new Date());

		int num = deviceUserRelMapper.updateByPrimaryKeySelective(deviceUserRel);

		if (num > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
		} else {
			result.setResultCode(new ResultCode("FALSE", "FALSE"));
		}

		return result;
	}

	/**
	 * 验证此设备号是否绑定用户
	 */
	@Override
	public DeviceUserRel checkDeviceIsBinding(Integer deviceId) {
		return deviceUserRelMapper.checkDeviceIsBinding(deviceId);
	}

	/**
	 * 验证此设备号是否绑定用户
	 */
	@Override
	public DeviceUserRel checkDeviceIsBinding(String userMobile, Integer deviceId) {
		DeviceUserRel deviceUserRel = new DeviceUserRel();
		String managerId = apiUserInfoMapper.getManagerIdByMobile(userMobile);
		deviceUserRel.setManagerId(managerId);
		deviceUserRel.setDeviceId(deviceId);
		return deviceUserRelMapper.checkDeviceIsBindingByManagerId(deviceUserRel);
	}
	
	public WeixinProxy getWeixinProxy(String hostName, int port, String Appid, String Secret,
			String Paysignkey, String Mchid) {

		if (weixinProxy != null) {
			return weixinProxy;
		} else {
			JedisPool jedisPool = new JedisPool(jedisPoolConfig,
					jedisConnectionFactory.getHostName(), jedisConnectionFactory.getPort(),
					jedisConnectionFactory.getTimeout(), jedisConnectionFactory.getPassword());
			RedisTokenStorager rts = new RedisTokenStorager(jedisPool);

			WeixinPayAccount wxpayAccount = null;
			
			//建一个表来获取微信的全部信息。包含微信支付、imweixin
			wxpayAccount = new WeixinPayAccount(Appid, Secret,
					Paysignkey, Mchid, null, null, null, null, null);
			Weixin4jSettings settings = new Weixin4jSettings(wxpayAccount);

			settings.setTokenStorager(rts);

			WeixinProxy proxy = new WeixinProxy(settings);
			
			return proxy;
		}
	}
}