/**
 * 订单购买统计页面展示对象
 */
package com.semioe.api.vo;

import java.io.Serializable;

public class OrderPurchaseVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 服务&商品 ID
	 */
	private String relationId;
	/**
	 * 服务&商品名称
	 */
	private String relationName;
	/**
	 * 服务&商品供应商 ID
	 */
	private String relationSupplierId;
	/**
	 * 服务&商品供应商
	 */
	private String relationSupplier;
	
	/**
	 * 订单类型：  0.商品订单 ， 1.服务订单
	 */
	private Integer type;
	/**
	 * 订单类型描述
	 */
	private String typeDesc;
	/**
	 * 购买数量（orderCount）
	 */
	private Double buyCount;
	/**
	 * 购买价格（price）
	 */
	private Double priceCount;
	
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	public String getRelationSupplierId() {
		return relationSupplierId;
	}
	public void setRelationSupplierId(String relationSupplierId) {
		this.relationSupplierId = relationSupplierId;
	}
	public String getRelationSupplier() {
		return relationSupplier;
	}
	public void setRelationSupplier(String relationSupplier) {
		this.relationSupplier = relationSupplier;
	}
	public Double getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(Double buyCount) {
		this.buyCount = buyCount;
	}
	public Double getPriceCount() {
		return priceCount;
	}
	public void setPriceCount(Double priceCount) {
		this.priceCount = priceCount;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	
}
