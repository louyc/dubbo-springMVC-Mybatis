package com.semioe.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.semioe.api.entity.QrcodeInfo;
import com.semioe.common.result.Result;
import com.semioe.common.tools.RedisTool;
import com.semioe.dubbo.service.QrcodeInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

@Controller
@RequestMapping("/qrcode")
public class QrcodeInfoController {

    private static final Logger logger = LoggerFactory.getLogger(QrcodeInfoController.class);

    @Reference
    private QrcodeInfoService qrcodeInfoService;

    @Value("${redis.host}")
    private String redis_host;
    @Value("${redis.port}")
    private String redis_port;

    @SuppressWarnings("unused")
    @Autowired
    private RedisTool redisTool;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;
    @SuppressWarnings("unused")
    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    /**
     * weixin.getAppid().trim(), weixin.getSecret().trim(),
     * weixin.getPaysignkey().trim(), weixin.getMchid().trim() private Im_weixin
     * weixin ;
     */

    @Value("#{settings['weixin_Appid']}")
    private String weixin_Appid;
    @Value("#{settings['weixin_Secret']}")
    private String weixin_Secret;
    @Value("#{settings['weixin_Paysignkey']}")
    private String weixin_Paysignkey;
    @Value("#{settings['weixin_Mchid']}")
    private String weixin_Mchid;

    @SuppressWarnings("rawtypes")
    @RequestMapping("/createQrcodeInfo")
    @ResponseBody
    public Result createQrcodeInfo(@RequestBody QrcodeInfo qrcodeInfo) {
        return qrcodeInfoService.createQrcodeInfo(qrcodeInfo, jedisConnectionFactory.getHostName(),
                jedisConnectionFactory.getPort(), weixin_Appid.trim(), weixin_Secret.trim(), weixin_Paysignkey.trim(),
                weixin_Mchid.trim());
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("/getAllQrcodeInfoListPage")
    @ResponseBody
    public Result getAllQrcodeInfoListPage(@RequestParam("managerId") String managerId,
                                           @RequestParam(value="qrType",required = false) Integer qrType,
                                           @RequestParam("pageSize") String pageSize, @RequestParam("currentsPage") String currentsPage) {

        int currentPage = Integer.parseInt(currentsPage);

        if (currentPage <= 0) {
            currentPage = 1;
        }

        QrcodeInfo qrcodeInfo = new QrcodeInfo();

        qrcodeInfo.setShowCount(Integer.parseInt(pageSize));
        qrcodeInfo.setCurrentPage(currentPage);

        qrcodeInfo.setManagerId(managerId);
        if (qrType != null) {
            qrcodeInfo.setQrType(qrType);
        }

        return qrcodeInfoService.getAllQrcodeInfoListPage(qrcodeInfo);
    }

    @RequestMapping("/download")
    @ResponseBody
    public void createQrcodeInfo(@RequestParam("ticket") String ticket, @RequestParam("name") String name, HttpServletRequest request,
                                 HttpServletResponse response) {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;
        OutputStream fs = null;
        InputStream inStream = null;

        try {
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("QrCode" + name + ".jpg", "UTF-8"));
            URL url = new URL("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket);

            URLConnection conn = url.openConnection();
            inStream = conn.getInputStream();

            byte[] buffer = new byte[1204];
            fs = response.getOutputStream();
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 用户服务分享功能
     *
     * @param paramsId
     * @param type
     * @return
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("/getShareServiceCode")
    @ResponseBody
    public Result getShareServiceCode(@RequestParam("paramsId") String paramsId,
                                      @RequestParam("type") String type, @RequestParam("sourceUserId") String sourceUserId) {

        logger.info("QrcodeInfoController.getShareServiceCode start, type=" + type + ", paramsId=" + paramsId + ", sourceUserId=" + sourceUserId);

        return qrcodeInfoService.shareServiceCode(type, paramsId, sourceUserId, jedisConnectionFactory.getHostName(),
                jedisConnectionFactory.getPort(), weixin_Appid.trim(), weixin_Secret.trim(), weixin_Paysignkey.trim(),
                weixin_Mchid.trim());
    }


    @ResponseBody
    @RequestMapping(value = "/getQRType", method = RequestMethod.GET)
    public Result getQRType(HttpServletRequest request, HttpServletResponse response) {
        logger.info("QrcodeInfoController.getQRType start itemType=32");

        return qrcodeInfoService.getQRType();
    }
}