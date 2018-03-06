package com.semioe.api.vo;

import java.io.Serializable;

public class UserVO implements Serializable {

	private static final long serialVersionUID = -5147850115984184705L;

	private String doctor;

	private String customer;

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

}
