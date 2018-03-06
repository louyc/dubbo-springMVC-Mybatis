package com.semioe.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.OrgCommunityInfo;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.service.OrgCommunityInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by kwinxu on 2017/12/22.
 */

@Controller
@RequestMapping("/orgCommInfo")
public class OrgCommunityInfoController {

    private static final Logger logger = LoggerFactory.getLogger(OrgCommunityInfoController.class);

    @Reference
    private OrgCommunityInfoService orgCommunityInfoService;

    /**
     * 获取所有管理社区
     *
     * @param request
     * @param response
     * @param orgId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllOrgCommInfo", method = RequestMethod.GET)
    public Result getAllOrgCommInfo(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam String orgId) {

        logger.info("OrgCommunityInfoController.getAllOrgCommInfo start orgId = " + orgId);

        Result result = new Result(StatusCodes.OK, true);

        if (null == orgId || StringUtil.isEmpty(orgId)) {
            result.setResultCode(new ResultCode("FAIL", "机构 ID 不能为空！"));
            return result;
        }

        List<OrgCommunityInfo> communityInfoList = orgCommunityInfoService.getAllOrgCommInfo(orgId);

        result.setResultCode(new ResultCode("SUCCESS", "SUCCESS"));
        result.setData(communityInfoList);

        return result;
    }


    /**
     * 添加管理社区
     *
     * @param request
     * @param response
     * @param orgCommunityInfo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addOrgCommInfo", method = RequestMethod.POST)
    public Result addOrgCommInfo(HttpServletRequest request, HttpServletResponse response,
                                 @RequestBody OrgCommunityInfo orgCommunityInfo) {

        logger.info("OrgCommunityInfoController.addOrgCommInfo start ");

        Result result = new Result(StatusCodes.OK, true);

        try {
            int row = orgCommunityInfoService.addOrgCommInfo(orgCommunityInfo);
            if (row == 1) {
                result.setResultCode(new ResultCode("SUCCESS", "添加成功！"));
            }
        } catch (Exception e) {
            if (e.getMessage().indexOf("！") != -1) {
                result.setResultCode(new ResultCode("FAIL", e.getMessage()));
            } else {
                e.printStackTrace();
                result.setResultCode(new ResultCode("FAIL", "服务器错误！"));
            }
        } finally {
            return result;
        }
    }

}
