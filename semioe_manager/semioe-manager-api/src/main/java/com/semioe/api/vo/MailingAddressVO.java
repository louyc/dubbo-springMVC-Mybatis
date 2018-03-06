package com.semioe.api.vo;

import java.io.Serializable;

public class MailingAddressVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5160499949777175323L;

	private String name;
	private String mobile;
	private String address;
	private String zipCode;
	private String detailedAddress;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getDetailedAddress() {
		return detailedAddress;
	}

	public void setDetailedAddress(String detailedAddress) {
		this.detailedAddress = detailedAddress;
	}

}
