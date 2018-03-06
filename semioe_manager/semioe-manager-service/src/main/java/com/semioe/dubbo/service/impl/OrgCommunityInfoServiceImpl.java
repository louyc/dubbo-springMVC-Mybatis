package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.OrgCommunityInfo;
import com.semioe.api.entity.OrgCommunityInfoExample;
import com.semioe.dubbo.dao.OrgCommunityInfoMapper;
import com.semioe.dubbo.service.OrgCommunityInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by kwinxu on 2017/12/22.
 */
@Service
public class OrgCommunityInfoServiceImpl implements OrgCommunityInfoService {

    private static final Logger logger = LoggerFactory.getLogger(OrgCommunityInfoServiceImpl.class);

    @Autowired
    private OrgCommunityInfoMapper orgCommunityInfoMapper;

    /**
     * 获取所有管理社区
     *
     * @param orgId
     * @return
     */
    @Override
    public List<OrgCommunityInfo> getAllOrgCommInfo(String orgId) {

        logger.info("OrgCommunityInfoServiceImpl.getAllOrgCommInfo start orgId = " + orgId);

        OrgCommunityInfoExample example = new OrgCommunityInfoExample();
        example.createCriteria().andOrgIdEqualTo(orgId).andInUseEqualTo(1);

        List<OrgCommunityInfo> communityInfoList = orgCommunityInfoMapper.selectByExample(example);

        return communityInfoList;
    }

    /**
     * 添加管理社区
     *
     * @param orgCommunityInfo
     * @return
     */
    @Override
    public int addOrgCommInfo(OrgCommunityInfo orgCommunityInfo) throws Exception {

        logger.info("OrgCommunityInfoServiceImpl.addOrgCommInfo start orgId = " + orgCommunityInfo.getOrgId() + " communityName = " + orgCommunityInfo.getCommunityName());

        if (null == orgCommunityInfo.getCommunityName() || StringUtil.isEmpty(orgCommunityInfo.getCommunityName())) {
            throw new Exception("社区名不能为空！");
        }

        OrgCommunityInfoExample example = new OrgCommunityInfoExample();
        example.createCriteria().andCommunityNameEqualTo(orgCommunityInfo.getCommunityName());
        long row = orgCommunityInfoMapper.countByExample(example);
        if (row > 0) {
            throw new Exception("已添加该社区！");
        }
        return orgCommunityInfoMapper.insertSelective(orgCommunityInfo);
    }
}
