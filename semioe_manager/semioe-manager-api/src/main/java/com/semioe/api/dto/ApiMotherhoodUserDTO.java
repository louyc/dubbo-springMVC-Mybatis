package com.semioe.api.dto;

import com.semioe.api.entity.ApiMotherhoodUser;

import java.io.Serializable;

/**
 * Created by kwinxu on 2017/12/18.
 */
public class ApiMotherhoodUserDTO extends ApiMotherhoodUser implements Serializable{

    private String doctorId;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

}
