package com.semioe.dubbo.service;

import com.semioe.api.entity.UserDoctorRel;

/**
 * Created by kwinxu on 2017/12/18.
 */
public interface UserDoctorRelService {


    /**
     * 添加孕产妇签约信息
     * @param userDoctorRel
     * @return
     */
    int add(UserDoctorRel userDoctorRel) throws Exception;
}
