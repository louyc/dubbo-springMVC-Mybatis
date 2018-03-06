package com.semioe.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.exception.WeixinPayException;
import com.foxinmy.weixin4j.http.weixin.JsonResult;
import com.foxinmy.weixin4j.model.Button;
import com.foxinmy.weixin4j.model.WeixinAccount;
import com.foxinmy.weixin4j.model.WeixinPayAccount;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.api.MenuApi;
import com.foxinmy.weixin4j.mp.api.OauthApi;
import com.foxinmy.weixin4j.mp.api.TmplApi;
import com.foxinmy.weixin4j.mp.message.TemplateMessage;
import com.foxinmy.weixin4j.mp.model.OauthToken;
import com.foxinmy.weixin4j.mp.model.User;
import com.foxinmy.weixin4j.payment.WeixinPayProxy;
import com.foxinmy.weixin4j.payment.mch.MchPayPackage;
import com.foxinmy.weixin4j.payment.mch.PrePay;
import com.foxinmy.weixin4j.token.RedisTokenStorager;
import com.foxinmy.weixin4j.token.TokenHolder;
import com.foxinmy.weixin4j.type.ButtonType;
import com.foxinmy.weixin4j.type.TicketType;
import com.foxinmy.weixin4j.type.TradeType;
import com.foxinmy.weixin4j.util.DigestUtil;
import com.foxinmy.weixin4j.util.MapUtil;
import com.foxinmy.weixin4j.util.RandomUtil;
import com.foxinmy.weixin4j.util.StringUtil;
import com.foxinmy.weixin4j.util.Weixin4jSettings;
import com.semioe.api.entity.ApiUserInfo;
import com.semioe.api.entity.BackstageUserInfo;
import com.semioe.api.entity.OrderInfo;
import com.semioe.api.entity.QrcodeInfo;
import com.semioe.api.entity.QrcodeRel;
import com.semioe.api.vo.OrderInfoVO;
import com.semioe.common.result.Result;
import com.semioe.common.tools.util.ConnUtil;
import com.semioe.common.tools.util.DateTimeUtil;
import com.semioe.common.tools.util.Md5Util;
import com.semioe.common.tools.util.SignUtil;
import com.semioe.common.tools.util.XmlUtils;
import com.semioe.dubbo.service.ApiUserInfoService;
import com.semioe.dubbo.service.BackstageUserInfoService;
import com.semioe.dubbo.service.DeviceBindingUserService;
import com.semioe.dubbo.service.DeviceInfoService;
import com.semioe.dubbo.service.OrderInfoService;
import com.semioe.dubbo.service.QrcodeInfoService;
import com.semioe.dubbo.service.QrcodeRelService;
import com.semioe.web.util.NetworkUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Controller
@RequestMapping("/api/WeiXin")
public class WeixinController {

	private static final Logger logger = LoggerFactory.getLogger(WeixinController.class);

	private WeixinProxy weixinProxy;

	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;
	@Autowired
	private JedisPoolConfig jedisPoolConfig;

	@Value("#{settings['weixin_Appid']}")
	private String weixin_Appid;
	@Value("#{settings['weixin_Secret']}")
	private String weixin_Secret;
	@Value("#{settings['weixin_Paysignkey']}")
	private String weixin_Paysignkey;
	@Value("#{settings['weixin_Mchid']}")
	private String weixin_Mchid;
	@Value("#{settings['template_id']}")
	private String template_id; // 推送 模板ID

	// 微信页面地址
	@Value("#{settings['weixin_Host']}")
	private String weixin_Host;

	// 订单处理服务
	@Reference
	private OrderInfoService orderInfoService;
	// 前台用户对象
	@Reference
	private ApiUserInfoService apiUserInfoService;
	// 后台用户数据
	@Reference
	private BackstageUserInfoService backUserInfoService;
	// 推广码信息表
	@Reference
	private QrcodeInfoService qrcodeInfoService;
	// 推广码 关系表
	@Reference
	private QrcodeRelService qrcodeRelService;
	// 设备操作
	@Reference
	private DeviceBindingUserService deviceBindingUserService;
	@Reference
	private DeviceInfoService deviceInfoService;

	/**
	 * 获取access_token
	 * 
	 **/
	@RequestMapping(value = "/weixin_get_access_token.action", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void getAccessToken(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("获取access_token");
		WeixinProxy wxProxy = getWeixinProxy();
		// logger.info(wxProxy.getTokenHolder().getToken());
		PrintWriter out = response.getWriter();
		out.print(wxProxy.getTokenHolder().getToken());
		out.close();
		out = null;
	}

	public WeixinProxy getWeixinProxy() {

		if (weixinProxy != null) {
			return weixinProxy;
		} else {
			JedisPool jedisPool = new JedisPool(jedisPoolConfig,
					jedisConnectionFactory.getHostName(), jedisConnectionFactory.getPort(),
					jedisConnectionFactory.getTimeout(), jedisConnectionFactory.getPassword());
			RedisTokenStorager rts = new RedisTokenStorager(jedisPool);
			// RedisTokenStorager rts = new
			// RedisTokenStorager(jedisConnectionFactory.getHostName(),
			// jedisConnectionFactory.getPort(), jedisPoolConfig);

			WeixinPayAccount wxpayAccount = null;
			// 建一个表来获取微信的全部信息。包含微信支付、imweixin
			wxpayAccount = new WeixinPayAccount(weixin_Appid.trim(), weixin_Secret.trim(),
					weixin_Paysignkey.trim(), weixin_Mchid.trim(), null, null, null, null, null);
			Weixin4jSettings settings = new Weixin4jSettings(wxpayAccount);
			settings.setTokenStorager(rts);
			WeixinProxy proxy = new WeixinProxy(settings);
			this.weixinProxy = proxy;
			return proxy;
		}
	}

	/**
	 * 生成菜单
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/gen_weixiaoche_menu.action", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void genWeiXiaoCheMenu(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("菜单生成");

		WeixinProxy weixinProxy = getWeixinProxy();

		TokenHolder tokenHolder = weixinProxy.getTokenHolder();

		MenuApi menuApi = new MenuApi(tokenHolder);

		JsonResult result = menuApi.deleteMenu();
		logger.info("删除所有菜单返回:" + result.getCode());

		List<Button> btnList = new ArrayList<Button>();
		// TODO
		Button b1 = new Button("健康档案", weixin_Host + "m/healthRecords.html", ButtonType.view);
		btnList.add(b1);
		Button b2 = new Button("首页", weixin_Host + "m/index.html", ButtonType.view);
		btnList.add(b2);
		Button b3 = new Button("个人中心", weixin_Host + "m/personal.html", ButtonType.view);
		btnList.add(b3);
		
		// 子菜单
		/*List<Button> b3list = new ArrayList<Button>();
		
		Button b3ch1 = new Button("扫码测量", weixin_Host + "m/equipment.html", ButtonType.view);
		b3list.add(b3ch1);
		Button b3ch2 = new Button("我的服务", weixin_Host + "m/myService.html", ButtonType.view);
		b3list.add(b3ch2);
		Button b3ch3 = new Button("个人中心", weixin_Host + "m/personal.html", ButtonType.view);
		b3list.add(b3ch3);

		b3.setSubs(b3list);*/

		JsonResult ret = menuApi.createMenu(btnList);

		logger.info("添加菜单返回值：" + ret);

		/*
		 * PrintWriter out = response.getWriter();
		 * out.print(JSON.toJSONString(ret)); out.close(); out = null;
		 */

		/*
		 * 示例代码
		 * 
		 * List<Button> btnList = new ArrayList<Button>(); //3、更多 Button b3 =
		 * new Button("更多服务", null,null); List<Button> subBtnList = new
		 * ArrayList<Button>(); //更多-校车加盟 Button subbtn1 = new Button("公司加盟",
		 * "http://api.wexiaoche.com/front/page/xiaochejiaru.html",
		 * ButtonType.view); subBtnList.add(subbtn1);
		 * 
		 * b3.setSubs(subBtnList); btnList.add(b3);
		 * 
		 */
	}

	/**
	 * 接入验证
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/weixin_check.action", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void check(HttpServletRequest request, HttpServletResponse response) {
		try {
			boolean isGet = request.getMethod().toLowerCase().equals("get");
			logger.info("[isGet]：" + isGet);
			if (isGet) {
				logger.info("首次微信接入验证");
				// 微信加密签名
				String signature = request.getParameter("signature");
				// 时间戳
				String timestamp = request.getParameter("timestamp");
				// 随机数
				String nonce = request.getParameter("nonce");
				// 随机字符串
				String echostr = request.getParameter("echostr");

				logger.info(signature + "---------signature--------");
				logger.info(timestamp + "---------timestamp--------");
				logger.info(nonce + "---------nonce--------");
				PrintWriter out = response.getWriter();
				// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
				if (SignUtil.checkSignature(signature, timestamp, nonce)) {
					out.print(echostr);
				}
				out.close();
				out = null;

			} else {
				logger.info("微信接入验证  else");
				// 接收菜单点击消息处理
				StringBuffer sb = new StringBuffer();
				InputStream is = request.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
				while ((s = br.readLine()) != null) {
					sb.append(s);
				}
				String xml = sb.toString();
				logger.info("xml:" + xml);
				Map<String, String> msgMap = XmlUtils.xmlBody2map(xml, "xml");
				String msgType = msgMap.get("MsgType");
				String eventType2 = msgMap.get("Event");
				logger.info("==========" + msgType + "======" + eventType2);

				// 时间消息
				if (msgType.equals("event")) {
					String eventType = msgMap.get("Event");
					// 点击菜单
					if (eventType.equals("CLICK")) {
						String eventKey = msgMap.get("EventKey");
						// if ("lianxi".equals(eventKey)) {// 点击联系方式
						logger.info("点击菜单-联系方式");
						PrintWriter out = response.getWriter();

						Map<String, Object> respMsgMap = new HashMap<String, Object>();
						respMsgMap.put("ToUserName", msgMap.get("FromUserName"));
						respMsgMap.put("FromUserName", msgMap.get("ToUserName"));
						respMsgMap.put("CreateTime", new Date().getTime() + "");
						respMsgMap.put("MsgType", "text");
						respMsgMap.put("Content", eventKey);

						out.print(XmlUtils.map2xmlBody(respMsgMap, "xml"));
						out.close();
						out = null;
						// }
					} else if ("subscribe".equals(eventType)) {

						logger.info("go to case [subscribe]");

						// 关注自动回复
						String openId = msgMap.get("FromUserName"); // 用户openid
						// 邀请码
						String EventKey = null;

						// 根据业务需要处理 EventKey
						if (null != msgMap.get("EventKey") && !"".equals(msgMap.get("EventKey"))
								&& msgMap.get("EventKey").length() > 8) {
							logger.info("====EventKey: " + msgMap.get("EventKey"));
							EventKey = msgMap.get("EventKey").substring(8,
									msgMap.get("EventKey").length());
						}

						PrintWriter out = response.getWriter();
						Map<String, Object> respMsgMap = new HashMap<String, Object>();
						String retXml = "";
						if (StringUtils.isNotEmpty(EventKey)) {

							logger.info("处理EvenKey逻辑，EventKey= " + EventKey);
							// 解析事件参数
							String[] eventData = EventKey.split(",");

							switch (eventData[0]) {
							case "1": // 推广码关注 参数格式： 1,IsPromotionName,ManagerId
								String personName = eventData[1];
								String doctorId = eventData[2];
								// 查询推广码关系表
								QrcodeInfo qrcode = qrcodeInfoService
										.getQrcodeByQrPerson(personName, doctorId);
								// 添加一条关注用户
								QrcodeRel qrcodeRel = new QrcodeRel();
								qrcodeRel.setQrcodeId(qrcode.getId()); // 推广表id
								qrcodeRel.setPromotionName(qrcode.getIsPromotionName()); // 推广人名称
								qrcodeRel.setDoctorId(qrcode.getManagerId()); // 医生ID
								qrcodeRel.setOpenId(openId); // 用户openid
								logger.info("添加关系：" + qrcodeRel.toString());

								Result<QrcodeRel> result = qrcodeRelService.addQrcodeRel(qrcodeRel);

								logger.info(result.getResultCode().getMessage());

								// 返回处理结果
								out.print(result.getResultCode().getMessage());
								out.close();
								out = null;

								break;
							case "2": // TODO 推广码分享 2,type,id

								String inTyep = eventData[1]; // 查询类型
								String caseId = eventData[2]; // 查询Id
								String sourceUserId = eventData[3]; // 分享用户Id
								
								String tital = "";
								String desc = "";

								String url = weixin_Host; // 跳转url
								switch (inTyep) {
								case "1": // 服务
									url += "m/serviceIndex.html?serviceId=" + caseId +"&sourceType=2&sourceFromType=3&sourceUserId="+sourceUserId;
									tital = "点击打开服务详情";
									desc = "点击打开服务详情，获取智能服务，持续关爱您的健康！";
									break;
								case "2": // 医生
									url += "m/doctorIndex.html?md=" + caseId;
									tital = "点击打开医生主页";
									desc = "点击这里打开医生主页，获取智能服务，持续关爱您的健康！";
									break;
								case "3": // 机构
									url += "m/orgIndex.html?md=" + caseId;
									tital = "点击打开机构主页";
									desc = "点击这里打开机构主页，获取智能服务，持续关爱您的健康！";
									break;
								case "4": // 商品
									url += "m/equipmentIndex.html?isBuy=1&goodId=" + caseId +"&sourceType=2&sourceFromType=3&sourceUserId="+sourceUserId;
									tital = "点击打开商品详情";
									desc = "点击打开商品详情，获取智能服务，持续关爱您的健康！";
									break;
								}
								
								logger.info("分享跳转URL: " + url);
								
								// 参数说明： 1.title 2.Description 3.PicUrl 4.Url
								// 5.openid 6.FromUserName
								retXml = tuwenxmlBody(tital, desc,
										"https://mmbiz.qlogo.cn/mmbiz_jpg/AgDXIA3oXdLibqs96ywvDC7CjAWXLo1wqcQDJdKiasBoecqeuxSOYKcvVCTYvEgWZ5wadkCpol3Cl9gjibp6h3f4g/0?wx_fmt=jpeg",
										url, openId, msgMap.get("ToUserName"));
								logger.info("-----------respMsgMap----------------" + retXml);
								out.print(retXml);
								out.close();
								out = null;

								break;
							}
						} else {
							// 发送图文消息
							/*retXml = tuwenxmlBody("欢迎关注小语健康",
									"签约家庭医生、建立电子档案、数据便携检测、定制健康服务、看病方便舒心。",
									"https://mmbiz.qpic.cn/mmbiz_jpg/AgDXIA3oXdInemX9eicKUhNUL3IhwV6M3xlOu3akpicNNgSQFALVOWpcd3TiagmRW83dyJMLFp8aVu3hJjhibJ91rg/0?wx_fmt=jpeg",
									weixin_Host + "m/myFamilyDoctor.html", openId,
									msgMap.get("ToUserName"));*/
							
							retXml = tuwenxmlBody("欢迎关注小语健康",
									"签约家庭医生、建立电子档案、数据便携检测、定制健康服务、看病方便舒心。",
									"https://mmbiz.qpic.cn/mmbiz_jpg/qOxX57IEy9Xl3CB6uxfSwoQsibECc2xxtxibKfTqtWFlX6qns9icq81UGWMSmyRrrzYGrelqibmjKo29Dib6v9wSKag/0?wx_fmt=jpeg",
									weixin_Host + "m/myFamilyDoctors.html", openId,
									msgMap.get("ToUserName"));
							
							logger.info("-----------respMsgMap----------------" + retXml);
							out.print(retXml);
							out.close();
							out = null;
						}

					} else if ("SCAN".equals(eventType)) {// 已经关注又扫二维码
						// 项目邀请码：
						String openId = msgMap.get("FromUserName");

						String EventKey = msgMap.get("EventKey");

						PrintWriter out = response.getWriter();
						String retXml = "";
						if (StringUtils.isNotEmpty(EventKey)) {
							logger.info("处理EvenKey逻辑，EventKey= " + EventKey);
							// 解析事件参数
							String[] eventData = EventKey.split(",");

							switch (eventData[0]) {
							case "2": // TODO 推广码分享 2,type,id

								String inTyep = eventData[1]; // 查询类型
								String caseId = eventData[2]; // 查询Id
								String sourceUserId = eventData[3]; // 分享用户Id
								
								String tital = "";
								String desc = "";

								String url = weixin_Host; // 跳转url
								switch (inTyep) {
								case "1": // 服务
									url += "m/serviceIndex.html?serviceId=" + caseId +"&sourceType=2&sourceFromType=3&sourceUserId="+sourceUserId;
									tital = "点击打开服务详情";
									desc = "点击打开服务详情，获取智能服务，持续关爱您的健康！";
									break;
								case "2": // 医生
									url += "m/doctorIndex.html?md=" + caseId;
									tital = "点击打开医生主页";
									desc = "点击这里打开医生主页，获取智能服务，持续关爱您的健康！";
									break;
								case "3": // 机构
									url += "m/orgIndex.html?md=" + caseId;
									tital = "点击打开机构主页";
									desc = "点击这里打开机构主页，获取智能服务，持续关爱您的健康！";
									break;
								case "4": // 商品
									url += "m/equipmentIndex.html?isBuy=1&goodId=" + caseId +"&sourceType=2&sourceFromType=3&sourceUserId="+sourceUserId;
									tital = "点击打开商品详情";
									desc = "点击打开商品详情，获取智能服务，持续关爱您的健康！";
									break;
								}
								
								logger.info("分享跳转URL: " + url);
								
								// 参数说明： 1.title 2.Description 3.PicUrl 4.Url
								// 5.openid 6.FromUserName
								retXml = tuwenxmlBody(tital, desc,
										"https://mmbiz.qlogo.cn/mmbiz_jpg/AgDXIA3oXdLibqs96ywvDC7CjAWXLo1wqcQDJdKiasBoecqeuxSOYKcvVCTYvEgWZ5wadkCpol3Cl9gjibp6h3f4g/0?wx_fmt=jpeg",
										url, openId, msgMap.get("ToUserName"));
								break;
							}
						} else {
							// 发送图文消息
							/*retXml = tuwenxmlBody("欢迎关注小语健康",
									"在这里你可以进行自我健康管理，还可以关联医生的看护服务，进行智能健康咨询！",
									"https://mmbiz.qlogo.cn/mmbiz_jpg/AgDXIA3oXdLibqs96ywvDC7CjAWXLo1wqcQDJdKiasBoecqeuxSOYKcvVCTYvEgWZ5wadkCpol3Cl9gjibp6h3f4g/0?wx_fmt=jpeg",
									weixin_Host + "/m/myFamilyDoctor.html", openId,
									msgMap.get("ToUserName"));*/
							
							retXml = tuwenxmlBody("欢迎关注小语健康",
									"签约家庭医生、建立电子档案、数据便携检测、定制健康服务、看病方便舒心。",
									"https://mmbiz.qpic.cn/mmbiz_jpg/qOxX57IEy9Xl3CB6uxfSwoQsibECc2xxtxibKfTqtWFlX6qns9icq81UGWMSmyRrrzYGrelqibmjKo29Dib6v9wSKag/0?wx_fmt=jpeg",
									weixin_Host + "m/myFamilyDoctors.html", openId,
									msgMap.get("ToUserName"));
							
						}
						logger.info("-----------respMsgMap----------------" + retXml);
						out.print(retXml);
						out.close();
						out = null;
					} else if ("unsubscribe".equals(eventType)) {// 取消关注

					} else if ("TEMPLATESENDJOBFINISH".equals(eventType)) {

					}
				} else if (msgType.equals("text")) {

				} else if (msgType.equals("device_event")) { // 设备关联事件
					PrintWriter out = response.getWriter();
					String retXml = "";

					if (eventType2.equals("bind")) { // 设备绑定事件处理
						retXml = deviceEventBind(msgMap);
					}

					logger.info("-----------respMsgMap----------------" + retXml);
					out.print(retXml);
					out.close();
					out = null;

				} else {
					PrintWriter out = response.getWriter();
					out.print("success");
					out.close();
					out = null;
				}
			}
		} catch (Exception e) {
			logger.error("{}", e);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param msgMap
	 * @return
	 * @throws Exception
	 */
	public String deviceEventBind(Map<String, String> msgMap ) throws Exception{
		
		//解析 XML 传入数据
		String toUserName = msgMap.get("ToUserName") ;
		String fromUserName = msgMap.get("FromUserName") ;
		String event = msgMap.get("Event") ;
		String deviceType = msgMap.get("DeviceType") ;
		String deviceCode = msgMap.get("DeviceID") ;
		String sessionId = msgMap.get("SessionID") ;
		String openId = msgMap.get("OpenID") ;

		logger.info("-------------deviceEventBind_解析参数------------------");
		logger.info("  toUserName：" + toUserName);
		logger.info("  fromUserName：" + fromUserName);
		logger.info("  event：" + event);
		logger.info("  deviceType：" + deviceType);
		logger.info("  deviceCode：" + deviceCode);
		logger.info("  sessionId：" + sessionId);
		logger.info("  openId：" + openId);

		// 根据openID ,获取managerID
		String managerId = "";
		ApiUserInfo apiUser = apiUserInfoService.getApiUserByOpenId(openId);
		if (apiUser != null) {
			managerId = apiUser.getManagerId();
		} else {
			throw new Exception("userNotFound");
		}
		logger.info("根据【" + openId + "】获取 用户ID：" + managerId);
		// 根据传入 设备信息获取 deviceId (截取最后一个下划线后的id串)  取消该逻辑  2017-10-12
		/* int index = deviceCode.lastIndexOf("_");
		String deviceCodeFind = deviceCode.substring(index + 1, deviceCode.length()); 
		logger.info("解析deviceCode：" + deviceCodeFind); */
		Integer deviceId = deviceInfoService.getDeviceIdByDeviceCode(deviceCode);
		logger.info("获取 设备ID：" + deviceId);

		// 调用绑定接口
		Result result = deviceBindingUserService.deviceBindingUser(managerId, deviceId);
		if(result != null && result.getResultCode() != null){
			logger.info(result.getResultCode().toString());
		}else{
			logger.info("返回值异常：{}",result);
		}

		// 推送图文消息跳转页面
		/*String tital = "设备绑定";
		String desc = "点击绑定设备";
		String url = weixin_Host + "m/lanyaTest.html";*/   //TODO 测试跳转页面
		
		// 返回微信
		String retXml = deviceBackXmlBody(fromUserName, toUserName, 
				event, deviceType, deviceCode, sessionId ,"test");

		return retXml;
	}

	/**
	 * 返回设备通信信息XML
	 * 
	 * 参数说明 
	 *	ToUserName ：接收方（微信用户）的user name 
	 *	FromUserName ：发送方（公众号）的user name 
	 *	CreateTime ：消息创建时间，第三方自己取当前秒级时间戳 
	 *	MsgType ：消息类型（同请求参数）：device_event 
	 *	Event ：事件类型（同请求参数） 
	 *	DeviceType ：设备类型（同请求参数） 
	 *	DeviceID ：设备ID （同请求参数） 
	 *	SessionID ：微信客户端生成的session id（同请求参数） 
	 *	Content ：消息内容，BASE64编码 
	 * 
	 * @return
	 */
	public String deviceBackXmlBody(String ToUserName, String FromUserName, String Event, String DeviceType,
			String DeviceID, String SessionID, String Content) {

		org.dom4j.Document doc = DocumentHelper.createDocument();
		Element body = DocumentHelper.createElement("xml");
		doc.add(body);
		body.addElement("ToUserName").addText(ToUserName);
		body.addElement("FromUserName").addText(FromUserName);
		body.addElement("CreateTime").addText(new Date().getTime() + "");
		body.addElement("MsgType").addText("device_event");
		body.addElement("Event").addText(Event);
		body.addElement("DeviceType").addText(DeviceType);
		body.addElement("DeviceID").addText(DeviceID);
		body.addElement("SessionID").addText(SessionID);
		try{
			body.addElement("Content").addText( Md5Util.encryptBASE64(Content));
		}catch (Exception e) {
			logger.error("base64转换异常，转换参数："+Content);
			logger.error("{}",e);
			body.addElement("Content").addText("");
		}
		return doc.asXML();
	}
	
	/**
	 * 返回图文信息XML
	 * 
	 * @return
	 */
	public String tuwenxmlBody(String title, String Description, String PicUrl, String Url,
			String openid, String FromUserName) {

		org.dom4j.Document doc = DocumentHelper.createDocument();
		Element body = DocumentHelper.createElement("xml");
		doc.add(body);
		Element item = DocumentHelper.createElement("item");
		item.addElement("Title").addText(title);
		item.addElement("Description").addText(Description);
		item.addElement("PicUrl").addText(PicUrl);
		item.addElement("Url").addText(Url);
		body.addElement("ToUserName").addText(openid);
		body.addElement("FromUserName").addText(FromUserName);
		body.addElement("CreateTime").addText(new Date().getTime() + "");
		body.addElement("MsgType").addText("news");
		body.addElement("ArticleCount").addText("1");
		body.addElement("Articles").add(item);

		return doc.asXML();
	}

	// 如果要使用JSSDK，获得需要获取配置的参数
	/**
	 * wx.config({ debug: true, //
	 * 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开
	 * ，参数信息会通过log打出，仅在pc端时才会打印。 appId: '', // 必填，公众号的唯一标识 timestamp: , //
	 * 必填，生成签名的时间戳 nonceStr: '', // 必填，生成签名的随机串 signature: '',// 必填，签名，见附录1
	 * jsApiList: [] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2 });
	 */
	@RequestMapping(value = "/jssdkconfig.action", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void jssdkconfig(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("JSSDK 参数获取");

		String url = request.getParameter("url"); // 获取URL
		logger.info("参数 url=" + url);

		WeixinProxy wxProxy = getWeixinProxy();
		// weixin = weixinService.selectWeixinByDomainName();

		WeixinAccount weixinAccount = new WeixinAccount(weixin_Appid.trim(), weixin_Secret.trim());

		TokenHolder tokenHolder = wxProxy.getTicketHolder(TicketType.jsapi);

		logger.info(tokenHolder.getToken().toString());// 获取js token

		Map<String, String> map = new HashMap();

		// map.put("timeStamp", DateUtil.timestamp2string());
		map.put("timeStamp", DateTimeUtil.getFormatDateTime(new Date()));

		map.put("nonceStr", RandomUtil.generateString(16));
		map.put("url", request.getParameter("url"));
		map.put("jsapi_ticket", tokenHolder.getToken().getAccessToken());

		String sign = DigestUtil.SHA1(MapUtil.toJoinString(map, false, true));

		map.put("signature", sign);
		map.put("appId", weixinAccount.getId());
		map.put("link", weixin_Host);

		PrintWriter out = response.getWriter();
		out.print(JSON.toJSONString(map));
		out.close();
		out = null;

		logger.info("返回结果：" + JSON.toJSONString(map));

		/**
		 * 返回值： appId: 'wxacd70a44b6877ae9', timestamp: res.timestamp, nonceStr:
		 * res.nonceStr, signature: res.signature,
		 */

	}

	/**
	 * 微信支付调用
	 * 
	 * @return
	 */
	private JSONObject orderWeixinPay(HttpServletRequest request, String description,
			String outTradeNo, Double totalFee, String openid) {
		// 返回信息对象
		JSONObject jsondataObject = new JSONObject();

		WeixinPayAccount wxpayAccount = new WeixinPayAccount(weixin_Appid.trim(),
				weixin_Secret.trim(), weixin_Paysignkey.trim(), weixin_Mchid.trim(), null, null,
				null, null, null);

		WeixinPayProxy PAY = new WeixinPayProxy(new Weixin4jSettings(wxpayAccount));
		// 构建微信结构
		try {
			logger.info("支付请求： body=" + description + " outTradeNo= " + outTradeNo + ", totalFee= "
					+ totalFee);
			logger.info(" notifyUrl=" + weixin_Host + "api/WeiXin/recharge_notify.action"
					+ ", openid=" + openid);

			MchPayPackage payPackageV3 = new MchPayPackage(description, outTradeNo, totalFee,
					weixin_Host + "api/WeiXin/recharge_notify.action",
					NetworkUtil.getIpAddress(request), TradeType.JSAPI, openid, null, null, null);

			PrePay prePay = null;
			try {
				prePay = PAY.createPrePay(payPackageV3);
				jsondataObject = JSONObject.fromObject(prePay);
			} catch (WeixinPayException e) {
				logger.error("{}", e);
				jsondataObject.put("ret_code", 1);
				jsondataObject.put("ret_msg", "调用微信支付失败！支付异常码[[" + e.getErrorCode() + "]");
				jsondataObject.put("ret_error", e);
			}
			String timestamp = DateTimeUtil.getFormatDateTime(new Date());
			Map<String, String> map = new HashMap<String, String>();
			map.put("appId", jsondataObject.getString("appId"));
			map.put("timeStamp", timestamp);
			map.put("nonceStr", prePay.getNonceStr());
			map.put("package", "prepay_id=" + jsondataObject.getString("prepayId"));
			map.put("signType", "MD5");
			String sign = DigestUtil.MD5(
					MapUtil.toJoinString(map, false, false) + "&key=" + weixin_Paysignkey.trim())
					.toUpperCase();
			jsondataObject.put("sign1", sign);
			jsondataObject.put("timeStamp", timestamp);
			jsondataObject.put("ret_code", 0);
			jsondataObject.put("ret_msg", "微信支付调用成功！");

		} catch (WeixinException e) {
			logger.error("{}", e);
			jsondataObject.put("ret_code", 1);
			jsondataObject.put("ret_msg", "调用微信支付失败！支付异常码[[" + e.getErrorCode() + "]");
			jsondataObject.put("ret_error", e);

		} catch (IOException e) {
			logger.error("{}", e);
			jsondataObject.put("ret_code", 1);
			jsondataObject.put("ret_msg", "消息通讯失败！");
			jsondataObject.put("ret_error", e);
		}

		return jsondataObject;
	}

	/**
	 * 订单预生成
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/wx_project_pay.action", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public void wx_order_recharge(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("预订单下单接口");

		String userId = request.getParameter("userId"); // 用户ID
		String relationId = request.getParameter("goodsId"); // 商品或服务ID
		String type = request.getParameter("type"); // 类型
		String buyCount = request.getParameter("buyCount"); // 购买数量
		
		// TODO
		String sourceType = request.getParameter("sourceType");          // 推荐来源类型：0，广告位推荐；1，主页推荐；2
		String sourceFromType = request.getParameter("sourceFromType");  //
		String sourceOrderId = request.getParameter("sourceOrderId");    // 流程推荐订单ID
		String sourceUserId = request.getParameter("sourceUserId"); 	 // 推荐人id
		
		JSONObject jsondataObject = new JSONObject();

		logger.info("接受参数，userId=" + userId + ", relationId=" + relationId + ", type=" + type
				+ ", buyCount=" + buyCount);
		
		logger.info("配置订单分享数据，参数： sourceType="+sourceType+" ,sourceFromType="+sourceFromType+
				" ,sourceOrderId="+sourceOrderId+", sourceUserId="+sourceUserId);

		// 根据用户id，查询用户信息
		ApiUserInfo user = apiUserInfoService.getCurrentUserInfo(userId);

		// 判断用户及OpenId是否存在
		if (user != null && user.getManagerId() != null) {
			// 生成订单
			OrderInfo saveOrder = new OrderInfo();
			saveOrder.setType(type); // 类型
			saveOrder.setUserId(userId); // 购买人ID
			saveOrder.setRelationId(Integer.parseInt(relationId)); // 商品或服务ID
			saveOrder.setOrderCount(Integer.parseInt(buyCount)); // 购买数量
			// 推荐来源参数设置
			if (!StringUtil.isEmpty(sourceType)) {
				saveOrder.setSourceType(sourceType);
			}
			if (!StringUtil.isEmpty(sourceFromType)) {
				saveOrder.setSourceFromType(sourceFromType);
			}
			if (!StringUtil.isEmpty(sourceOrderId)) {
				saveOrder.setSourceOrderId(Integer.valueOf(sourceOrderId));
			}
			if (!StringUtil.isEmpty(sourceUserId)) {
				saveOrder.setSourceUserId(sourceUserId);
			}
			
			Result<OrderInfo> resule = orderInfoService.addOrderInfo(saveOrder);

			if (null != resule && null != resule.getData() && null != resule.getData().getId()) {

				OrderInfo order = resule.getData();
				logger.info("订单创建成功。   订单编号：" + order.getOrderCode());
				String openid = user.getOpenId(); // 用户OpenId
				logger.info("订单创建成功。   openid：" + openid);
				String description = order.getRelationName(); // 商品名称
				logger.info("订单创建成功。   description：" + description);
				logger.info("订单创建成功。   order.getPrice()：" + order.getPrice()); // 订单价格

				// 当订单价格为0时直接支付
				if (order.getPrice() != 0) {
					// 调用微信支付接口
					jsondataObject = orderWeixinPay(request, description, order.getOrderCode(),
							order.getPrice(), openid);
				} else {
					OrderInfoVO orderVo = orderInfoService.getOrderInfoByCode(order.getOrderCode()); // 根据订单编号查询订单
					if (orderVo != null && orderVo.getPayStatus() == 0) {
						OrderInfo upOrder = new OrderInfo();
						upOrder.setId(orderVo.getId());
						upOrder.setPayStatus(1); // 支付状态，0：未支付，1：已支付
						boolean uptype = orderInfoService.updateOrder(upOrder);
						logger.info("修改支付状态：" + uptype);
						// 查看推广码关联用户表，更新首次购买数据
						qrcodeRelService.firstPayOrderUser(orderVo);
					}
					jsondataObject.put("ret_code", 3);
					jsondataObject.put("ret_msg", "订单购买成功！");
				}
			} else if (null != resule.getResultCode()
					&& null != resule.getResultCode().getMessage()) {
				jsondataObject.put("ret_code", 1);
				jsondataObject.put("ret_msg", resule.getResultCode().getMessage());
			} else {
				jsondataObject.put("ret_code", 1);
				jsondataObject.put("ret_msg", "订单生成失败！");
			}
		} else {
			jsondataObject.put("ret_code", 1);
			jsondataObject.put("ret_msg", "用户获取失败，请重新登录！");
		}
		logger.info(jsondataObject.toString());
		// return jsondataObject;
		PrintWriter out = response.getWriter();
		out.print(jsondataObject.toString());
		logger.info(jsondataObject.toString());
		out.close();
		out = null;
	}

	/**
	 * 订单预生成（重复下单）
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/wx_project_repeatpay.action", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public void wx_order_repeatpay(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("订单重新支付接口");

		String orderCode = request.getParameter("orderCode"); // 订单编号
		JSONObject jsondataObject = new JSONObject();

		logger.info("订单编号：" + orderCode);

		// 重新支付订单
		Result<OrderInfoVO> resule = orderInfoService.repeatPayOrder(orderCode);
		if (null != resule && null != resule.getData() && null != resule.getData().getId()) {
			OrderInfoVO order = resule.getData();
			// 根据用户id，查询用户信息
			ApiUserInfo user = apiUserInfoService.getCurrentUserInfo(order.getUserId());
			// 判断用户及OpenId是否存在
			if (user != null && user.getManagerId() != null) {
				logger.info("订单修改编号成功。   订单编号：" + order.getOrderCode());
				String openid = user.getOpenId(); // 用户OpenId
				logger.info(" openid：" + openid);
				String description = order.getRelationName(); // 商品名称
				logger.info(" description：" + description);
				// 建一个表来获取微信的全部信息。包含微信支付、imweixin
				logger.info(" order.getPrice()：" + order.getPrice());

				if (order.getPrice() != 0) {
					// 调用微信支付接口
					jsondataObject = orderWeixinPay(request, description, order.getOrderCode(),
							order.getPrice(), openid);
				} else {
					OrderInfoVO orderVo = orderInfoService.getOrderInfoByCode(order.getOrderCode()); // 根据订单编号查询订单
					if (orderVo != null && orderVo.getPayStatus() == 0) {
						OrderInfo upOrder = new OrderInfo();
						upOrder.setId(orderVo.getId());
						upOrder.setPayStatus(1); // 支付状态，0：未支付，1：已支付
						boolean uptype = orderInfoService.updateOrder(upOrder);
						logger.info("修改支付状态：" + uptype);
						// 查看推广码关联用户表，更新首次购买数据
						qrcodeRelService.firstPayOrderUser(orderVo);
					}

					jsondataObject.put("ret_code", 3);
					jsondataObject.put("ret_msg", "订单购买成功！");
				}

			} else {
				jsondataObject.put("ret_code", 1);
				jsondataObject.put("ret_msg", "订单生成失败！");
			}

		} else if (null != resule.getResultCode() && null != resule.getResultCode().getMessage()) {
			jsondataObject.put("ret_code", 1);
			jsondataObject.put("ret_msg", resule.getResultCode().getMessage());
		} else {
			jsondataObject.put("ret_code", 1);
			jsondataObject.put("ret_msg", "订单生成失败！");
		}

		logger.info(jsondataObject.toString());
		PrintWriter out = response.getWriter();
		out.print(jsondataObject.toString());
		logger.info(jsondataObject.toString());
		out.close();
		out = null;
	}

	/**
	 * 回调地址
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/recharge_notify.action", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void recharge_notify(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("回调接口");
		// https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7

		StringBuffer sb = new StringBuffer();
		InputStream is = request.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String s = "";
		while ((s = br.readLine()) != null) {
			sb.append(s);
		}
		String xml = sb.toString();

		logger.info("xml:" + xml);

		// 转化成map
		Map msgMap = XmlUtils.xmlBody2map(xml, "xml");
		// out_trade_no result_code
		String result_code = msgMap.get("result_code").toString();
		String out_trade_no = msgMap.get("out_trade_no").toString();
		Map returnMap = new HashMap();
		if (result_code.equals("SUCCESS")) {
			logger.info("支付成功，更新订单状态。   订单编号：" + out_trade_no);
			// 更新订单状态
			OrderInfoVO orderVo = orderInfoService.getOrderInfoByCode(out_trade_no); // 根据订单编号查询订单
			if (orderVo != null && orderVo.getPayStatus() == 0) {
				OrderInfo order = new OrderInfo();
				order.setId(orderVo.getId());
				order.setPayStatus(1); // 支付状态，0：未支付，1：已支付
				boolean uptype = orderInfoService.updateOrder(order);
				logger.info("修改支付状态：" + uptype);
				// 查看推广码关联用户表，更新首次购买数据
				qrcodeRelService.firstPayOrderUser(orderVo);
			}
		} else {
			// 订单支付失败， 更新状态为失败状态
			logger.info("支付失败！   订单编号：" + out_trade_no);
			OrderInfoVO orderVo = orderInfoService.getOrderInfoByCode(out_trade_no);
			if (orderVo != null) {
				OrderInfo orderInfo = new OrderInfo();
				orderInfo.setId(orderVo.getId()); // 订单id
				orderInfo.setOrderCount(orderVo.getOrderCount()); // 订单数量
				orderInfo.setType(orderVo.getType()); // 订单类型
				orderInfo.setPayStatus(orderVo.getPayStatus()); // 支付状态
				orderInfo.setRelationId(orderVo.getRelationId()); // 商品服务id
				orderInfoService.payOrderCancal(orderInfo);
			}
		}
		returnMap.put("return_code", "SUCCESS");
		returnMap.put("return_msg", "OK");
		String retStr = XmlUtils.map2xmlBody(returnMap, "xml");
		PrintWriter out = response.getWriter();
		out.print(retStr);
		logger.info(retStr);
		out.close();
		out = null;

	}

	/**
	 * 授权接口 登录授权
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/auth.action", method = { RequestMethod.GET, RequestMethod.POST })
	public void Auth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("授权页面");
		try {
			//
			String domainname = request.getServerName();
			//
			String type = request.getParameter("type");

			if (!StringUtil.isEmpty(type)) {
				// 1. 登陆
				switch (type) {
				case "1": // 登陆
					// 跳转参数（给前台）
					String url = request.getParameter("url");
					// 业务逻辑参数

					String mobile = request.getParameter("mobile");

					String randCode = request.getParameter("randCode");

					// response.setHeader("Access-Control-Allow-Origin","*");

					logger.info("auth ==========" + "http://" + domainname
							+ "/api/WeiXin/doauth.action?type=" + type + "&url=" + url + "&mobile="
							+ mobile + "&randCode=" + randCode);
					// 带参数跳转
					response.sendRedirect(new OauthApi().getAuthorizeURL(weixin_Appid.trim(),
							"http://" + domainname + "/api/WeiXin/doauth.action?type=" + type
									+ "&mobile=" + mobile + "&randCode=" + randCode + "&url=" + url,
							"state", "snsapi_userinfo"));
					break;
				}

			} else {
				response.sendRedirect("/mobile/auth_error.html");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/mobile/auth_error.html");
		}
	}

	/**
	 * oauth授权页面,改用页面实现。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/doauth.action", method = { RequestMethod.GET, RequestMethod.POST })
	public void doAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			logger.info("doAuth 授权页面");
			// 业务类型
			String type = request.getParameter("type");
			logger.info("doAuth 授权页面type" + type);
			if (!StringUtil.isEmpty(type)) {
				// 微信获取数据
				WeixinProxy wxProxy = getWeixinProxy();
				OauthApi oauthApi = new OauthApi();
				String code = request.getParameter("code");

				String headimgurl = "";
				String city = "";
				if (StringUtils.isNotEmpty(code)) {

					logger.info("wxProxy is " + wxProxy.getWeixinAccount());

					logger.info("wxProxy.id is " + wxProxy.getWeixinAccount().getId());

					// 获取用户信息
					OauthToken oauthToken = oauthApi.getOauthToken(code,
							wxProxy.getWeixinAccount().getId(),
							wxProxy.getWeixinAccount().getSecret());
					if (oauthToken == null) {
						// 授权失败返回首页
						response.sendRedirect("/mobile/auth_error.html");
					} else {
						// 获取用户
						User user = oauthApi.getUser(oauthToken);
						if (null != user) {
							headimgurl = user.getHeadimgurl();
							city = user.getProvince();
							logger.info("headimgurl" + user.getHeadimgurl());
							logger.info("city==" + user.getProvince());
						}
						// 根据类型处理业务
						switch (type) {
						case "1": // 登陆

							// 跳转页面
							String url = request.getParameter("url");
							// 业务逻辑参数

							String mobile = request.getParameter("mobile");

							String randCode = request.getParameter("randCode");

							logger.info("请求登陆 URL：" + "/apiUser/login?mobile=" + mobile
									+ "&randCode=" + randCode + "&city=" + URLEncoder.encode(city)
									+ "&url=" + url + "&openId=" + oauthToken.getOpenId()
									+ "&headimgurl=" + headimgurl);

							// 跳转登录页（传OpenID）
							response.sendRedirect("/apiUser/login?mobile=" + mobile + "&randCode="
									+ randCode + "&city=" + URLEncoder.encode(city) + "&openId="
									+ oauthToken.getOpenId() + "&headimgurl=" + headimgurl + "&url="
									+ url);
							break;
						}
					}
				}
			} else {
				response.sendRedirect("/mobile/auth_error.html");
			}
		} catch (Exception e) {
			logger.error("{}", e);
			e.printStackTrace();
			response.sendRedirect("/mobile/none.action");
		}
	}

	/**
	 * 微信模板推送接口
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendTuisong.action", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void sendTuisong(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("微信模板推送接口 开始");

		JSONObject jsondataObject = new JSONObject(); // 返回结果对象

		String userId = request.getParameter("userId"); // t USERID 查询用户， 获取
														// openid
		String doctorId = request.getParameter("doctorId"); // 医生id 查询医生
		String remark = request.getParameter("remark"); // 描述 显示内容
		String url = request.getParameter("url"); // 跳转URL地址

		logger.info("参数值：userId=" + userId + ",doctorId=" + doctorId + ",remark=" + remark + ",url="
				+ url);

		boolean goflage = true;
		// 根据用户id，查询用户信息
		ApiUserInfo user = apiUserInfoService.getCurrentUserInfo(userId);
		if (user == null || StringUtil.isEmpty(user.getOpenId())) {
			logger.error("用户查询失败，或用户openID不存在。 用户id：" + userId);
			jsondataObject.put("ret_code", 600);
			jsondataObject.put("ret_msg", "用户查询失败，用户id：" + userId);
			goflage = false;
		}
		// 获取医生数据
		Result backResult = backUserInfoService.getOneUser(doctorId);
		if (goflage && (backResult == null || backResult.getData() == null)) {
			logger.error("医生查询失败，医生id：" + userId);
			jsondataObject.put("ret_code", 600);
			jsondataObject.put("ret_msg", "用户查询失败，用户id：" + userId);
			goflage = false;
		}

		if (goflage) {
			BackstageUserInfo doctor = (BackstageUserInfo) backResult.getData();

			// 推送openid
			String touser = user.getOpenId();

			// 时间
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateTime = formatter.format(new Date());

			// 创建链接微信
			WeixinProxy wxProxy = getWeixinProxy();
			TmplApi tmplApi = new TmplApi(wxProxy.getTokenHolder());

			logger.info("创建微信模板：touser=" + touser + " template_id=" + template_id + " url=" + url);
			// 微信定义模板
			TemplateMessage tplMessage = new TemplateMessage(touser, template_id, url);
			tplMessage.pushItem("first", "您好，您的诊后问卷发送成功！");
			tplMessage.pushItem("keyword1", user.getName()); // 患者名称
			tplMessage.pushItem("keyword2", doctor.getName()); // 医生名称
			tplMessage.pushItem("keyword3", dateTime); // 时间
			tplMessage.pushItem("remark", remark);

			logger.info("发送微信消息 ：{}", tplMessage);

			try {
				JsonResult result = tmplApi.sendTmplMessage(tplMessage);
				logger.info("微信返回  result=" + result.getCode());

				if (result.getCode() == 0) {
					// 发送成功
					jsondataObject.put("ret_code", 200);
					jsondataObject.put("ret_msg", "消息发送成功！");
				} else {
					jsondataObject.put("ret_code", 600);
					jsondataObject.put("ret_msg", "微信消息发送失败！");
				}
			} catch (Exception e) {
				logger.error("消息发送失败， {}", e);
				jsondataObject.put("ret_code", 40001);
				jsondataObject.put("ret_msg", "微信消息发送失败！");
			}
		}

		logger.info(jsondataObject.toString());
		PrintWriter out = response.getWriter();
		out.print(jsondataObject.toString());
		logger.info(jsondataObject.toString());
		out.close();
		out = null;
		logger.info("微信模板推送接口 结束");
	}

	// public static void main(String[] args) {
	// try {
	// wx_device(null, null);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	/**
	 * 绑定设备授权
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/wx_device.action", method = { RequestMethod.GET, RequestMethod.POST })
	public void wx_device(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("设备授权开始");
		JSONObject jsondataObject = new JSONObject(); // 返回结果对象 WeixinProxy

		WeixinProxy weixinProxy = getWeixinProxy();
		TokenHolder tokenHolder = weixinProxy.getTokenHolder();
		String token = tokenHolder.getAccessToken();

		String productId = request.getParameter("productId");
		String mac = request.getParameter("mac");
		if (StringUtil.isEmpty(productId) || StringUtil.isEmpty(mac)) {
			jsondataObject.put("errcode", "200");
			jsondataObject.put("errmsg", "参数为空，请重新校验");
			logger.info("返回数据：" + jsondataObject.toString());
			PrintWriter out = response.getWriter();
			out.print(jsondataObject.toString());
			out.close();
			out = null;
			logger.info("设备授权 结束");
			return;
		}

		// String productId = "41005";
		// String mac = "23451234";
		// String token =
		// "UbPbhsvpRPxFP-TY6XyddDNrGXrnu0GOKutJ1uo53xtw8xCpJBlnpRj1_R8gU-aaDejUskgaI6NewKQ34O7gsUmUPWHtqXkS18IleD3hRwoGRLfADAOZT";
		String url = "https://api.weixin.qq.com/device/getqrcode?access_token=" + token
				+ "&product_id=" + productId;
		String shouQuanUrl = "https://api.weixin.qq.com/device/authorize_device?access_token="
				+ token;
		// 请求返回参数
		logger.info("请求授权地址：： " + url);
		logger.info("请求更新地址：： " + shouQuanUrl);
		String back = "";
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setRequestMethod("GET"); // 以Post方式提交表单，默认get方式
		con.setDoInput(true);
		con.setDoOutput(true);
		// 设置请求头信息
		con.setRequestProperty("Accept", "application/json");
		// 获取所有响应头字段
		Map<String, List<String>> map = con.getHeaderFields();
		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream(), "UTF-8"));
		String line;
		while ((line = in.readLine()) != null) {
			back += line;
		}
		String deviceid = "";
		String qrticket = "";
		// 转换返回参数
		com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(back);
		Map resultMap = (Map) json;
		logger.info("获取授权 微信返回信息： " + resultMap.entrySet());
		if (null != resultMap.get("errcode") && !resultMap.get("errcode").equals("0")) {
			jsondataObject.put("errcode", resultMap.get("errcode"));
			jsondataObject.put("errmsg", resultMap.get("errmsg").toString());
		} else {
			String code = resultMap.get("base_resp").toString();
			JSONObject jasonObject = JSONObject.fromObject(code);
			Map resultCodeMap = (Map) jasonObject;
			String retCode = resultCodeMap.get("errcode").toString();
			if (retCode.equals("0")) {
				logger.info("获取设备id成功： " + resultMap.entrySet());
				deviceid = resultMap.get("deviceid").toString();
				qrticket = resultMap.get("qrticket").toString();
				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("device_num", "1");
				requestMap.put("op_type", "1");
				List<Map> deviceList = new ArrayList<Map>();
				Map<String, Object> bodyMap = new HashMap<String, Object>();
				bodyMap.put("id", deviceid);
				bodyMap.put("mac", mac);
				bodyMap.put("connect_protocol", "3");
				bodyMap.put("auth_key", "");
				bodyMap.put("close_strategy", "1");
				bodyMap.put("conn_strategy", "1");
				bodyMap.put("crypt_method", "0");
				bodyMap.put("auth_ver", "0");
				bodyMap.put("manu_mac_pos", "-1");
				bodyMap.put("ser_mac_pos", "-2");
				bodyMap.put("ble_simple_protocol", "0");
				deviceList.add(bodyMap);
				requestMap.put("device_list", deviceList);
				String jsonString = JSON.toJSONString(requestMap);
				String sqBack = ConnUtil.sendPostWithOutHeaderAndJson(shouQuanUrl, jsonString);
				com.alibaba.fastjson.JSONObject sqJson = com.alibaba.fastjson.JSONObject
						.parseObject(sqBack);
				Map resultSQMap = (Map) sqJson;
				logger.info("设备更新返回：" + resultSQMap.entrySet());
				logger.info(resultSQMap.get("resp").toString());
				List list = (List) resultSQMap.get("resp");
				Map reMap = (Map) list.get(0);
				logger.info(reMap + "");
				if (reMap.get("errcode").toString().equals("0")) {
					jsondataObject.put("errcode", reMap.get("errcode"));
					jsondataObject.put("errmsg", reMap.get("errmsg").toString());
					logger.info("设备授权成功：");
				} else {
					jsondataObject.put("errcode", reMap.get("errcode"));
					jsondataObject.put("errmsg", reMap.get("errmsg").toString());
					logger.info("设备授权失败：");
				}
			} else {
				jsondataObject.put("errcode", retCode);
				jsondataObject.put("errmsg", resultCodeMap.get("errmsg").toString());
				logger.info("获取设备id失败： " + resultMap.entrySet());
			}
			// if (deviceid.split("_").length > 1) {
			// jsondataObject.put("deviceid", deviceid.split("_")[2]);
			// jsondataObject.put("qrticket", qrticket);
			// } else {
			// }
		}
		jsondataObject.put("deviceid", deviceid);
		jsondataObject.put("qrticket", qrticket);
		logger.info("返回数据：" + jsondataObject.toString());
		PrintWriter out = response.getWriter();
		out.print(jsondataObject.toString());
		out.close();
		out = null;
		logger.info("设备授权 结束");
	}

}
