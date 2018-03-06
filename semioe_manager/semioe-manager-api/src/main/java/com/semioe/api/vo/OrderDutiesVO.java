/**
 * 订单履约情况数据展示对象
 */
package com.semioe.api.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OrderDutiesVO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 签约关系ID
	 */
	private Integer relId ;
	/**
	 * 签约账户ID
	 */
	private String userId ;
	/**
	 * 签约账户名称
	 */
	private String userName ;
	/**
	 * 签约账户电话
	 */
	private String userMobile ;
	/**
	 * 签约用户名称
	 */
	private String signUserName ;
	/**
	 * 签约医生ID
	 */
	private String doctorId ;
	/**
	 * 签约医生名称
	 */
	private String doctorName ;
	/**
	 * 签约医生电话
	 */
	private String doctorMobile ;
	/**
	 * 签约机构ID
	 */
	private String orgId ;
	/**
	 * 签约类型： 1.家庭医生  ， 2.孕产妇
	 */
	private Integer buildType ;
	
	private String buildTypeDesc;
	/**
	 * 订单ID
	 */
	private String orderId ;
	/**
	 * 订单编号
	 */
	private String orderCode ;
	/**
	 * 服务ID
	 */
	private Integer servieId ;
	/**
	 * 服务名称
	 */
	private String serviceName ;
	/**
	 * 履约次数（订单使用次数）
	 */
	private Integer reportCount ;
	/**
	 * 创建时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date createTime ;
	/**
	 * 查询类型: 1.公卫服务  ， 2.其他服务
	 */
	private Integer keywordsType ;
	public Integer getRelId() {
		return relId;
	}
	public void setRelId(Integer relId) {
		this.relId = relId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	public String getSignUserName() {
		return signUserName;
	}
	public void setSignUserName(String signUserName) {
		this.signUserName = signUserName;
	}
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getDoctorMobile() {
		return doctorMobile;
	}
	public void setDoctorMobile(String doctorMobile) {
		this.doctorMobile = doctorMobile;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public Integer getBuildType() {
		return buildType;
	}
	public void setBuildType(Integer buildType) {
		this.buildType = buildType;
		// 1.家庭医生  ， 2.孕产妇
		switch (this.buildType) {
		case 1:this.buildTypeDesc = "家庭医生"; break;
		case 2:this.buildTypeDesc = "孕产妇"; break;
		}
	}
	public String getBuildTypeDesc() {
		return buildTypeDesc;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public Integer getServieId() {
		return servieId;
	}
	public void setServieId(Integer servieId) {
		this.servieId = servieId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Integer getReportCount() {
		return reportCount;
	}
	public void setReportCount(Integer reportCount) {
		this.reportCount = reportCount;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getKeywordsType() {
		return keywordsType;
	}
	public void setKeywordsType(Integer keywordsType) {
		this.keywordsType = keywordsType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
