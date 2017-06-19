package com.semioe.common.tools.util;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

/**
 * token服务类
 * @author puanl
 *
 */
public class JWTTokenUtil {


	public static Integer outTime = 10000 * 60 * 5;  //延期时间

    public static Integer tokenTime = 12345 * 30 * 5;  //加密时间

    /**
     * 创建 TOKEN 内容部分
     * @return
     */
    private static String createTokenBody( String userId){
        Date iat = new Date();
        //token
        JSONObject jwtBody = new JSONObject();
        JSONObject headBody = new JSONObject();
        JSONObject loadBody = new JSONObject();
        // 构建头信息
        jwtBody.put("head",headBody);
        headBody.put("alg","semioe");
        headBody.put("type","JWT");
        // 构建内容信息
        jwtBody.put("load",loadBody);
        loadBody.put("iss","www.semioe.account");   //签发者
        loadBody.put("iat",iat);   //签发时间
        loadBody.put("exp",new Date(iat.getTime()+ outTime));   //过期时间
        loadBody.put("data",userId);  //参数

        return jwtBody.toJSONString();
    }

    private static String createTokenBody(String fromIP, String userId){
        Date iat = new Date();
        //token
        JSONObject jwtBody = new JSONObject();
        JSONObject headBody = new JSONObject();
        JSONObject loadBody = new JSONObject();
        // 构建头信息
        jwtBody.put("head",headBody);
        headBody.put("alg","semioe");
        headBody.put("type","JWT");
        // 构建内容信息
        jwtBody.put("load",loadBody);
        loadBody.put("iss","www.semioe.account");   //签发者
        loadBody.put("iat",iat);   //签发时间
        loadBody.put("exp",new Date(iat.getTime()+ outTime));   //过期时间
        loadBody.put("from",fromIP);  //来源
        loadBody.put("data",userId);  //参数

        return jwtBody.toJSONString();
    }

    /**
     * 生成密钥的方法
     * @param bodyCode
     * @return
     */
    private static String createAskKey(String bodyCode) throws Exception{
        //字符串转换
        JSONObject jwtBody = JSONObject.parseObject(bodyCode);

        //解析头部
        String tokenDataStr = null;
        JSONObject headBody = jwtBody.getJSONObject("head");
        //头部String
        tokenDataStr = headBody.toJSONString();

        //解析内容
        JSONObject loadBody = jwtBody.getJSONObject("load");
        Date iatTime = loadBody.getDate("iat");
        loadBody.put("exp",new Date(iatTime.getTime() - tokenTime));  //修改截至时间
        //内容String
        tokenDataStr += "." + loadBody.toJSONString();
        //转换base64
        tokenDataStr = Md5Util.encryptBASE64(tokenDataStr);
        //MD5加密
        tokenDataStr = Md5Util.getBaseMDCode(tokenDataStr);
        return tokenDataStr;
    }

    /**
     * 生成完整Token方法
     * @return
     * @throws Exception
     */
    public static String createTokenStr(String userId) throws Exception{
        //生成TOKEN内容
        String object = createTokenBody(userId);
        String bodyStr = Md5Util.encryptBASE64(object);
        String str = createAskKey(object);

        String backCode = bodyStr +"."+str;
        backCode = backCode.replace("\r\n","");

        //显示生成TOKEN
        //System.out.println("token: "+backCode);

        return backCode;
    }

    /**
     * 生成Token方法（含来源ID）
     * @param fromIP
     * @param userId
     * @return
     */
    public static String createTokenStr(String fromIP , String userId) throws Exception{
        //生成TOKEN内容
        String object = createTokenBody(fromIP,userId);
        String bodyStr = Md5Util.encryptBASE64(object);
        String str = createAskKey(object);

        String backCode = bodyStr +"."+str;
        backCode = backCode.replace("\r\n","");

        return backCode;
    }

    /**
     * 验证 TOKEN 信息是否可用（返回load内容）
     * @param tokenCode
     * @return
     */
    public static JSONObject readTokenCanUse(String tokenCode){

        String jwtBody = null;
        //判断TOKEN格式是否正确
        if(tokenCode.indexOf(".") == -1){
            return null;
        }
        // 根据内容信息转换密钥
        String bodyCode = tokenCode.substring(0,tokenCode.indexOf("."));
        String sayCode = null;
        try{
            jwtBody = Md5Util.decryptBASE64(bodyCode);
            sayCode = createAskKey(jwtBody);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        // 获取token 密钥，比较密钥是否正确
        String messCode = tokenCode.substring(tokenCode.indexOf(".")+1,tokenCode.length());
        if(!messCode.equals(sayCode)){
            return null;
        }
        // TOKEN 内容转换 JSON 串
        JSONObject jwtBodyJson = JSONObject.parseObject(jwtBody).getJSONObject("load");

        //返回内容
        return jwtBodyJson;
    }

    /**
     * 验证来源
     * @param fromIP
     * @param tokenCode
     * @return
     */
    public static JSONObject readTokenCanUse(String fromIP, String tokenCode){
        JSONObject bodyJson = readTokenCanUse(tokenCode);
        if(fromIP.equals(bodyJson.getString("from"))){
            return bodyJson;
        }
        return null;
    }

    public static void main(String[] args) throws Exception{

    }
	
}
