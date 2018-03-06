package com.semioe.dubbo.service;

import com.semioe.api.dto.ApiMotherhoodUserDTO;
import com.semioe.api.entity.ApiMotherhoodUser;

/**
 * Created by kwinxu on 2017/12/18.
 */
public interface ApiMotherhoodUserService {

    /**
     * 创建孕产妇档案
     *
     * @param apiMotherhoodUser
     * @return
     */
    int add(ApiMotherhoodUserDTO apiMotherhoodUser) throws Exception;


    /**
     * 查询孕产妇档案
     * @param id
     * @return
     * @throws Exception
     */
    ApiMotherhoodUser findById(String id) throws Exception;


    /**
     * 修改该孕产妇档案
     * @param apiMotherhoodUser
     * @return
     * @throws Exception
     */
    int modifyMotherhoodRecord(ApiMotherhoodUser apiMotherhoodUser) throws Exception;
}
