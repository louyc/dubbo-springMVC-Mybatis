package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.semioe.api.entity.UserDoctorRel;
import com.semioe.dubbo.dao.UserDoctorRelMapper;
import com.semioe.dubbo.service.UserDoctorRelService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by kwinxu on 2017/12/18.
 */
@Service
public class UserDoctorRelServiceImpl implements UserDoctorRelService {

    @Autowired
    private UserDoctorRelMapper userDoctorRelMapper;

    /**
     * 添加孕产妇签约信息
     *
     * @param userDoctorRel
     * @return
     */
    @Override
    public int add(UserDoctorRel userDoctorRel) throws Exception {
        if (null == userDoctorRel.getContractedUserId()) {
            throw new Exception("缺失孕产妇档案！");
        }
        if (null == userDoctorRel.getManagerId()) {
            throw new Exception("缺少用户关联！");
        }
        if (null == userDoctorRel.getDoctorId()) {
            throw new Exception("缺少医生信息！");
        }
        userDoctorRel.setInUse(1);
        userDoctorRel.setBuildType(3);
        userDoctorRel.setCreateTime(new Date());
        return userDoctorRelMapper.insertSelective(userDoctorRel);
    }
}
