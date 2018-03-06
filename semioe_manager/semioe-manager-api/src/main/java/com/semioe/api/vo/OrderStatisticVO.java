/**
 * 订单统计数据返回对象
 */
package com.semioe.api.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OrderStatisticVO  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String orderCode;
	private Integer relationId;
	private String relationName;
	private String relationSupplier;
	private String relationSupplierId;
	private String relationImgUrl;
	private String relationCode;
	private String relationCodeName;
	private Double relationPrice;
	private String type;
	private String typeDesc;
	private Double price;
	private Integer orderCount;
	private Integer payStatus;
	private String userId;
	private String userName;
	private String userMobile;
	private String backManagerId;	
	private String backUserName;
	private String backUserMobile;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date createTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date updateTime;
	private Integer inUse;
	
	private Integer keywordsType;
	
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
		this.orderCode = orderCode;
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
		this.relationName = relationName;
	}
	public String getRelationSupplier() {
		return relationSupplier;
	}
	public void setRelationSupplier(String relationSupplier) {
		this.relationSupplier = relationSupplier;
	}
	public String getRelationSupplierId() {
		return relationSupplierId;
	}
	public void setRelationSupplierId(String relationSupplierId) {
		this.relationSupplierId = relationSupplierId;
	}
	public String getRelationImgUrl() {
		return relationImgUrl;
	}
	public void setRelationImgUrl(String relationImgUrl) {
		this.relationImgUrl = relationImgUrl;
	}
	public String getRelationCode() {
		return relationCode;
	}
	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}
	public String getRelationCodeName() {
		return relationCodeName;
	}
	public void setRelationCodeName(String relationCodeName) {
		this.relationCodeName = relationCodeName;
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
		this.type = type;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
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
	public String getBackManagerId() {
		return backManagerId;
	}
	public void setBackManagerId(String backManagerId) {
		this.backManagerId = backManagerId;
	}
	public String getBackUserName() {
		return backUserName;
	}
	public void setBackUserName(String backUserName) {
		this.backUserName = backUserName;
	}
	public String getBackUserMobile() {
		return backUserMobile;
	}
	public void setBackUserMobile(String backUserMobile) {
		this.backUserMobile = backUserMobile;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getKeywordsType() {
		return keywordsType;
	}
	public void setKeywordsType(Integer keywordsType) {
		this.keywordsType = keywordsType;
	}
	
}
