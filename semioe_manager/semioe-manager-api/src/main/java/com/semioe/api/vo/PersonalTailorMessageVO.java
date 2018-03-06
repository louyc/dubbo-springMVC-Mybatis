package com.semioe.api.vo;

import com.semioe.common.pageUtil.PageInfo;

public class PersonalTailorMessageVO extends PageInfo{

	private static final long serialVersionUID = 1008422564785313837L;
	
	private Integer id;
	
	private Integer applyType;
	
	private String title;
	
	private String name;
	
	private String mobile;
	
	private Integer isRead;
	
	private Integer isHandle;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApplyType() {
		return applyType;
	}

	public void setApplyType(Integer applyType) {
		this.applyType = applyType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public Integer getIsHandle() {
		return isHandle;
	}

	public void setIsHandle(Integer isHandle) {
		this.isHandle = isHandle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	


}
