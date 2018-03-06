package com.semioe.dubbo.service;

import com.semioe.api.entity.OrgCommunityInfo;

import java.util.List;

/**
 * Created by kwinxu on 2017/12/22.
 */
public interface OrgCommunityInfoService {


    /**
     * 获取所有管理社区
     * @param orgId
     * @return
     */
    List<OrgCommunityInfo> getAllOrgCommInfo(String orgId);


    /**
     * 添加管理社区
     * @param orgCommunityInfo
     * @return
     */
    int addOrgCommInfo(OrgCommunityInfo orgCommunityInfo) throws Exception;

}
