package com.semioe.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.semioe.common.pageUtil.PageInfo;

import java.util.Date;

public class JyDoctorInfo extends PageInfo {

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
		（家医）医生绑定状态：0：已绑定，1：未绑定
	 */
	private Integer doctorSignType;
	/**

	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date doctorOutTime;
	
	/**
     */
	private Integer signType;
	
	/**
	 * 关联机构ID
	 */
	private String signOrgId;
	
	/**
	 * 有效时间
	 */
	private Date validTime;
	
	/**
	 * 家医签约类型：1. 家庭医生，3. 孕产
	 */
	private String jySignType;
	
	public String getJySignType() {
		return jySignType;
	}

	public void setJySignType(String jySignType) {
		this.jySignType = jySignType;
	}

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
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

	public Integer getDoctorSignType() {
		return doctorSignType;
	}
	
	public void setDoctorSignType(Integer doctorSignType) {
		this.doctorSignType = doctorSignType;
	}

	public Date getDoctorOutTime() {
		return doctorOutTime;
	}

	public void setDoctorOutTime(Date doctorOutTime) {
		this.doctorOutTime = doctorOutTime;
	}

	public Integer getSignType() {
		return signType;
	}

	public void setSignType(Integer signType) {
		this.signType = signType;
	}

	public String getSignOrgId() {
		return signOrgId;
	}

	public void setSignOrgId(String signOrgId) {
		this.signOrgId = signOrgId;
	}
	

}