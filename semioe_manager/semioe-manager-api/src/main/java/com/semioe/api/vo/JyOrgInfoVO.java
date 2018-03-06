package com.semioe.api.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class JyOrgInfoVO implements Serializable {

	/**
	
	 */
	private String managerId;
	/**
	
	 */
	private String name;
	/**
	
	 */
	private String imageUrl;
	/**
	
	 */
	private String address;
	/**
	
	 */
	private String company;
	/**
	
	 */
	private String skills;
	/**
	
	 */
	private String title;
	/**
	
	 */
	private String certificationsUrl;
	/**
	
	 */
	private Integer userStatus;
	/**
	
	 */
	private String email;
	/**
	
	 */
	private String mobile;
	/**
	
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date createTime;
	/**
	
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date updateTime;
	/**
	
	 */
	private Integer inUse;
	/**
	
	 */
	private String desc;
	/**
	
	 */
	private String type;

	/**
	 */
	private Integer signId;

	/**
	 */
	private Integer signType;

	/**
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date signTime;

	/**
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date outTime;

	/**
	 * 医生ID
	 */
	private String signDoctorId;

	/**
	 * 省市信息
	 */
	private String provinceCode;
	private String provinceCodeDesc;

	private String cityCode;
	private String cityCodeDesc;

	private String townCode;
	private String townCodeDesc;

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceCodeDesc() {
		return provinceCodeDesc;
	}

	public void setProvinceCodeDesc(String provinceCodeDesc) {
		this.provinceCodeDesc = provinceCodeDesc;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityCodeDesc() {
		return cityCodeDesc;
	}

	public void setCityCodeDesc(String cityCodeDesc) {
		this.cityCodeDesc = cityCodeDesc;
	}

	public String getTownCode() {
		return townCode;
	}

	public void setTownCode(String townCode) {
		this.townCode = townCode;
	}

	public String getTownCodeDesc() {
		return townCodeDesc;
	}

	public void setTownCodeDesc(String townCodeDesc) {
		this.townCodeDesc = townCodeDesc;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date inviteTime;

	public Date getInviteTime() {
		return inviteTime;
	}

	public void setInviteTime(Date inviteTime) {
		this.inviteTime = inviteTime;
	}

	public String getSignDoctorId() {
		return signDoctorId;
	}

	public void setSignDoctorId(String signDoctorId) {
		this.signDoctorId = signDoctorId;
	}

	public Integer getSignId() {
		return signId;
	}

	public void setSignId(Integer signId) {
		this.signId = signId;
	}

	public Integer getSignType() {
		return signType;
	}

	public void setSignType(Integer signType) {
		this.signType = signType;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId == null ? null : managerId.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl == null ? null : imageUrl.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address == null ? null : address.trim();
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company == null ? null : company.trim();
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills == null ? null : skills.trim();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public String getCertificationsUrl() {
		return certificationsUrl;
	}

	public void setCertificationsUrl(String certificationsUrl) {
		this.certificationsUrl = certificationsUrl == null ? null : certificationsUrl.trim();
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getInUse() {
		return inUse;
	}

	public void setInUse(Integer inUse) {
		this.inUse = inUse;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc == null ? null : desc.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}