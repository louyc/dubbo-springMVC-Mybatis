package com.semioe.web.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.semioe.common.tools.util.SignUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/deviceDebug")
public class DeviceDebugController {

	private static final Logger logger = LoggerFactory.getLogger(DeviceDebugController.class);
	
	public static final String DEVICE_TEXT = new StringBuilder()
			.append("<xml>")
			.append("<ToUserName><![CDATA[%s]]></ToUserName>")
			.append("<FromUserName><![CDATA[%s]]></FromUserName>")
			.append("<CreateTime>%s</CreateTime>")
			.append("<MsgType><![CDATA[%s]]></MsgType>")
			.append("<DeviceType><![CDATA[%s]]></DeviceType>")
			.append("<DeviceID><![CDATA[%s]]></DeviceID>")
			.append("<SessionID>%s</SessionID>")
			.append("<Content><![CDATA[%s]]></Content>")
			.append("</xml>").toString();
	
	/**
	 * 构造设备消息响应
	 */
	public static final String buildDeviceText(String fromUser, String toUser,
			String deviceType, String deviceID, String content, String sessionID) {
		return String.format(DEVICE_TEXT, fromUser, toUser, time(),
				"device_text", deviceType, deviceID, sessionID, content);
	}
	
	/**
	 * 秒级时间戳
	 */
	private static String time(){
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
	
	/**
	 * 接口体脂称上传数据
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/weixin_debug_check.action", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void check(HttpServletRequest request, HttpServletResponse response){
		try{
			logger.info("---- weixin_tzc_check.action 请求参数 START ----");
			
			logger.info("[URL]："+request.getRequestURL());
			Enumeration enu = request.getParameterNames();
			while(enu.hasMoreElements()){
				String paraName=(String)enu.nextElement();
				logger.info("["+paraName+"]: "+request.getParameter(paraName));
			}
			
			logger.info("---- weixin_tzc_check.action 请求参数 END ----");
			
			boolean isGet = request.getMethod().toLowerCase().equals("get");
			logger.info("[isGet]："+isGet );
			
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
				// 接收菜单点击消息处理
				InputStream is = request.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				String s = br.readLine();
				
				logger.info("s :" + s );
				
				JSONObject jb = JSONObject.fromObject(s);
				
				PrintWriter out = response.getWriter();

//				Map<String, Object> respMsgMap = new HashMap<String, Object>();
//				respMsgMap.put("ToUserName", "");
//				respMsgMap.put("FromUserName", "");
//				respMsgMap.put("CreateTime", new Date().getTime() + "");
//				respMsgMap.put("MsgType", "device_text");
//				respMsgMap.put("DeviceType", jb.get("device_type"));
//				respMsgMap.put("DeviceID", jb.get("device_id"));
//				respMsgMap.put("SessionID", jb.get("session_id").toString());
//				respMsgMap.put("Content", Base64Utils.encodeToString("0".getBytes()));
				
				String xml = buildDeviceText(jb.get("open_id").toString(), "", jb.get("device_type").toString(), 
						jb.get("device_id").toString(), Base64Utils.encodeToString("0".getBytes()), jb.get("session_id").toString());
				
				out.print(xml);
				out.close();
				out = null;
				
				logger.info(xml + "---++++++------respMsgMap---+++++++-----");
			}
			
		}catch (Exception e) {
			logger.error("{}", e);
			e.printStackTrace();
		}
	}
}