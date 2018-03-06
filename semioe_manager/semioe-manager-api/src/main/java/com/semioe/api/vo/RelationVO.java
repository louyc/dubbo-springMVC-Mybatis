package com.semioe.api.vo;

import java.io.Serializable;

/**
 * 商品&服务展示对象
 * @author puanl
 *
 */
public class RelationVO implements Serializable {

	/** 服务或商品 id */
	private Integer relationId;
	
	/** 服务或商品 名称 */
	private String relationName;
	
	/** 服务或商品 描述 */
	private String relationDesc;
	
	/** 服务或商品 供应商名称 */
	private String relationSupplier;
	
	/** 服务或商品 供应商id */
	private String relationSupplierId;
	
	/** 服务或商品 缩略图 */
	private String relationImgUrl;
	
	/** 服务 流程id */
	private String relationCode;
	
	/** 服务 流程名称 */
	private String relationCodeName;
	
	/** 商品或服务 单价 */
	private String relationPrice;
	
	/** 服务或商品类型，0：商品，1：服务 */
	private String type;

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

	public String getRelationDesc() {
		return relationDesc;
	}

	public void setRelationDesc(String relationDesc) {
		this.relationDesc = relationDesc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRelationPrice() {
		return relationPrice;
	}

	public void setRelationPrice(String relationPrice) {
		this.relationPrice = relationPrice;
	}
	
}
