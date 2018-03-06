package com.semioe.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.semioe.api.entity.ApiUserInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderInfoVO implements Serializable {

	private Integer id;
	/** 订单编号 */
	private String orderCode;
	/** 服务或商品 id */
	private Integer relationId;
	/** 服务或商品 名称 */
	private String relationName;
	/** 服务或商品 供应商名称 */
	private String relationSupplier;
	/** 服务或商品 供应商id */
	private String relationSupplierId;
	/** 服务或商品 缩略图 */
	private String relationImgUrl;
	/** 服务或商品 流程id */
	private String relationCode;
	/** 服务或商品 流程名称 */
	private String relationCodeName;

	private String relationDesc;
	/** 服务或商品 单价 */
	private Double relationPrice;
	/** 订单类型，0：商品，1：服务 */
	private String type;
	/** 单价 */
	private Double price;
	/** 订单数量 */
	private Integer orderCount;
	/** 支付状态，0：未支付，1：已支付 */
	private Integer payStatus;
	/** 购买人id */
	private String userId;
	/** 购买人名称 */
	private String userName;
	/** 超时时间 */
	private Date outTime;
	/** 推荐来源类型：0，广告位推荐；1，服务推荐； */
	private String sourceType;
	/** 推荐用户id */
	private String sourceUserId;
	/** 推荐用户名 */ 
	private String sourceUser;
	
	/** 用户对象 */
	private ApiUserInfo userObject;
	
	/** 关联商品集合 */
	private List<GoodsInfoVO> goodsList;
	
	/** 报告是否存在 */
	private Integer reportCount;
	
	/** 用户收货地址 */
	private String userAddress;
	
	/** 医生所在医院 */
	private String doctorCompany;
	
	/** 医生职称 */
	private String doctorTital;
	
	/**
	 * 服务或商品提供给者id
	 */
	private String backManagerId;
	
	private String sourceFromType;
	
	public String getSourceFromType() {
		return sourceFromType;
	}

	public void setSourceFromType(String sourceFromType) {
		this.sourceFromType = sourceFromType;
	}
	
	public String getBackManagerId() {
		return backManagerId;
	}

	public void setBackManagerId(String backManagerId) {
		this.backManagerId = backManagerId;
	}
	
	public String getDoctorCompany() {
		return doctorCompany;
	}

	public void setDoctorCompany(String doctorCompany) {
		this.doctorCompany = doctorCompany;
	}

	public String getDoctorTital() {
		return doctorTital;
	}

	public void setDoctorTital(String doctorTital) {
		this.doctorTital = doctorTital;
	}

	public String getRelationDesc() {
		return relationDesc;
	}

	public void setRelationDesc(String relationDesc) {
		this.relationDesc = relationDesc;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date createTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date updateTime;

	private Integer inUse;
	
	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode == null ? null : orderCode.trim();
	}

	public Integer getRelationId() {
		return relationId;
	}

	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName == null ? null : relationName.trim();
	}

	public String getRelationSupplier() {
		return relationSupplier;
	}

	public void setRelationSupplier(String relationSupplier) {
		this.relationSupplier = relationSupplier == null ? null : relationSupplier.trim();
	}

	public String getRelationSupplierId() {
		return relationSupplierId;
	}

	public void setRelationSupplierId(String relationSupplierId) {
		this.relationSupplierId = relationSupplierId == null ? null : relationSupplierId.trim();
	}

	public String getRelationImgUrl() {
		return relationImgUrl;
	}

	public void setRelationImgUrl(String relationImgUrl) {
		this.relationImgUrl = relationImgUrl == null ? null : relationImgUrl.trim();
	}

	public String getRelationCode() {
		return relationCode;
	}

	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode == null ? null : relationCode.trim();
	}

	public Double getRelationPrice() {
		return relationPrice;
	}

	public void setRelationPrice(Double relationPrice) {
		this.relationPrice = relationPrice;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
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

	public String getRelationCodeName() {
		return relationCodeName;
	}

	public void setRelationCodeName(String relationCodeName) {
		this.relationCodeName = relationCodeName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<GoodsInfoVO> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsInfoVO> goodsList) {
		this.goodsList = goodsList;
	}

	public ApiUserInfo getUserObject() {
		return userObject;
	}

	public void setUserObject(ApiUserInfo userObject) {
		this.userObject = userObject;
	}

	public Integer getReportCount() {
		return reportCount;
	}

	public void setReportCount(Integer reportCount) {
		this.reportCount = reportCount;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceUserId() {
		return sourceUserId;
	}

	public void setSourceUserId(String sourceUserId) {
		this.sourceUserId = sourceUserId;
	}

	public String getSourceUser() {
		return sourceUser;
	}

	public void setSourceUser(String sourceUser) {
		this.sourceUser = sourceUser;
	}
	

}