package com.semioe.web.util;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.cloopen.rest.sdk.CCPRestSmsSDK;

/**
 * Created by King L on 2017/4/22.
 */
public class YtxSms {

    private static final Logger logger = LoggerFactory.getLogger(YtxSms.class);

    /**
     * accountSid 云通讯Sid
     */
    public static final String SMS_ACCOUNT_SID = "8a216da855e8eb7b0155ec96b51701ea";
    /**
     * accountToken 云通讯Token
     */
    public static final String SMS_ACCOUNT_TOKEN = "098b2e30083540ce9d81539784593627";
    /**
     * AccountAppId 云通讯应用Id
     */
    public static final String SMS_ACCOUNT_APPID = "8a216da855e8eb7b0155ec96b57d01f0";
    /**
     * 模板ID  验证码模板
     */
    public static final String SMS_ACCOUNT_TEMPLATE = "106298";
    
    /**
     * 模板ID  账户审批提示
     */
    public static final String SMS_ACCOUNT_TEMPLATE_EXAMINE = "192240";
    
    /**
     * 模板ID  家庭医生，机构申请
     */
    public static final String SMS_ACCOUNT_JY_ORGSEND = "207316";
    
    /**
     * 模板ID  家庭医生，医生同意
     */
    public static final String SMS_ACCOUNT_JY_DOCTORIN = "207310";
    
    /**
     * 模板ID  家庭医生，医生拒绝
     */
    public static final String SMS_ACCOUNT_JY_DOCTOROUT = "207262";

    /**
     * 发送短信验证码接口（注册验证码）
     */
    public static boolean sendCode(String phone, String code){
        HashMap<String, Object> result = null;
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        restAPI.init("app.cloopen.com", "8883");
        restAPI.setAccount(SMS_ACCOUNT_SID, SMS_ACCOUNT_TOKEN);
        restAPI.setAppId(SMS_ACCOUNT_APPID);
        result = restAPI.sendTemplateSMS(phone, SMS_ACCOUNT_TEMPLATE, new String[]{code, "10"});
        if ("000000".equals(result.get("statusCode"))) {
            logger.debug("---------YtxSms-----------sendCode-------发送短信成功------------");
            return true;
        }else{
            logger.debug("-------验证码发送失败-------错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
        return false;
    }
    
    
    /**
     * 发送短信验证码接口（账号审批消息）
     */
    public static boolean sendExamineMessage(String phone, String code){
        HashMap<String, Object> result = null;
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        restAPI.init("app.cloopen.com", "8883");
        restAPI.setAccount(SMS_ACCOUNT_SID, SMS_ACCOUNT_TOKEN);
        restAPI.setAppId(SMS_ACCOUNT_APPID);
        result = restAPI.sendTemplateSMS(phone, SMS_ACCOUNT_TEMPLATE_EXAMINE, new String[]{code, "10"});
        if ("000000".equals(result.get("statusCode"))) {
            logger.debug("---------YtxSms-----------sendExamineMessage-------发送短信成功------------");
            return true;
        }else{
            logger.debug("-------验证码发送失败-------错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
        return false;
    }
    
    
    /**
     * 发送短信验证码接口（家庭医生消息）
     * @param phone  发送手机号
     * @param type   短信模板id
     * @param code   短信内容
     * @return
     */
    public static boolean sendContentMessage(String phone, String type ,String[] code){
    	
    	logger.info("发送短信：phone="+phone+"; type="+type+"; code="+code);
    	
        HashMap<String, Object> result = null;
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        restAPI.init("app.cloopen.com", "8883");
        restAPI.setAccount(SMS_ACCOUNT_SID, SMS_ACCOUNT_TOKEN);
        restAPI.setAppId(SMS_ACCOUNT_APPID);
        result = restAPI.sendTemplateSMS(phone, type, code);
        if ("000000".equals(result.get("statusCode"))) {
            logger.info("---------YtxSms-----------sendContentMessage-------发送短信成功------------");
            return true;
        }else{
            logger.info("-------验证码发送失败-------错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
        return false;
    }
    
}