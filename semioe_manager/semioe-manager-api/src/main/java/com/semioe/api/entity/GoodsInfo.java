package com.semioe.api.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.semioe.common.pageUtil.PageInfo;

public class GoodsInfo extends PageInfo {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column goods_info.id
	 * @mbg.generated
	 */
	private Integer id;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column goods_info.goods_name
	 * @mbg.generated
	 */
	private String goodsName;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column goods_info.supplier_name
	 * @mbg.generated
	 */
	private String supplierName;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column goods_info.price
	 * @mbg.generated
	 */
	private Double price;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column goods_info.unit
	 * @mbg.generated
	 */
	private String unit;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column goods_info.stock
	 * @mbg.generated
	 */
	private Integer stock;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column goods_info.type
	 * @mbg.generated
	 */
	private Integer type;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column goods_info.create_user
	 * @mbg.generated
	 */
	private String createUser;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column goods_info.create_time
	 * @mbg.generated
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date createTime;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column goods_info.update_time
	 * @mbg.generated
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date updateTime;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column goods_info.in_use
	 * @mbg.generated
	 */
	private Integer inUse;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column goods_info.keywords_ids
	 * @mbg.generated
	 */
	private String keywordsIds;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column goods_info.keywords_names
	 * @mbg.generated
	 */
	private String keywordsNames;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column goods_info.goods_type
	 * @mbg.generated
	 */
	private String goodsType;

	/**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column goods_info.goods_desc
    *
    * @mbg.generated
    */
   private String goodsDesc;

   /**
    *
    * This field was generated by MyBatis Generator.
    * This field corresponds to the database column goods_info.img_url
    *
    * @mbg.generated
    */
   private String imgUrl;
   
   private List<Integer> idList;
   
   public List<Integer> getIdList() {
	return idList;
	}
	
	public void setIdList(List<Integer> idList) {
		this.idList = idList;
	}

/**
    * This method was generated by MyBatis Generator.
    * This method returns the value of the database column goods_info.goods_desc
    *
    * @return the value of goods_info.goods_desc
    *
    * @mbg.generated
    */
   public String getGoodsDesc() {
       return goodsDesc;
   }

   /**
    * This method was generated by MyBatis Generator.
    * This method sets the value of the database column goods_info.goods_desc
    *
    * @param goodsDesc the value for goods_info.goods_desc
    *
    * @mbg.generated
    */
   public void setGoodsDesc(String goodsDesc) {
       this.goodsDesc = goodsDesc == null ? null : goodsDesc.trim();
   }

   /**
    * This method was generated by MyBatis Generator.
    * This method returns the value of the database column goods_info.img_url
    *
    * @return the value of goods_info.img_url
    *
    * @mbg.generated
    */
   public String getImgUrl() {
       return imgUrl;
   }

   /**
    * This method was generated by MyBatis Generator.
    * This method sets the value of the database column goods_info.img_url
    *
    * @param imgUrl the value for goods_info.img_url
    *
    * @mbg.generated
    */
   public void setImgUrl(String imgUrl) {
       this.imgUrl = imgUrl == null ? null : imgUrl.trim();
   }

	
	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column goods_info.id
	 * @return  the value of goods_info.id
	 * @mbg.generated
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column goods_info.id
	 * @param id  the value for goods_info.id
	 * @mbg.generated
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column goods_info.goods_name
	 * @return  the value of goods_info.goods_name
	 * @mbg.generated
	 */
	public String getGoodsName() {
		return goodsName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column goods_info.goods_name
	 * @param goodsName  the value for goods_info.goods_name
	 * @mbg.generated
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName == null ? null : goodsName.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column goods_info.supplier_name
	 * @return  the value of goods_info.supplier_name
	 * @mbg.generated
	 */
	public String getSupplierName() {
		return supplierName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column goods_info.supplier_name
	 * @param supplierName  the value for goods_info.supplier_name
	 * @mbg.generated
	 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName == null ? null : supplierName.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column goods_info.price
	 * @return  the value of goods_info.price
	 * @mbg.generated
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column goods_info.price
	 * @param price  the value for goods_info.price
	 * @mbg.generated
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column goods_info.unit
	 * @return  the value of goods_info.unit
	 * @mbg.generated
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column goods_info.unit
	 * @param unit  the value for goods_info.unit
	 * @mbg.generated
	 */
	public void setUnit(String unit) {
		this.unit = unit == null ? null : unit.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column goods_info.stock
	 * @return  the value of goods_info.stock
	 * @mbg.generated
	 */
	public Integer getStock() {
		return stock;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column goods_info.stock
	 * @param stock  the value for goods_info.stock
	 * @mbg.generated
	 */
	public void setStock(Integer stock) {
		this.stock = stock;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column goods_info.type
	 * @return  the value of goods_info.type
	 * @mbg.generated
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column goods_info.type
	 * @param type  the value for goods_info.type
	 * @mbg.generated
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column goods_info.create_user
	 * @return  the value of goods_info.create_user
	 * @mbg.generated
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column goods_info.create_user
	 * @param createUser  the value for goods_info.create_user
	 * @mbg.generated
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser == null ? null : createUser.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column goods_info.create_time
	 * @return  the value of goods_info.create_time
	 * @mbg.generated
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column goods_info.create_time
	 * @param createTime  the value for goods_info.create_time
	 * @mbg.generated
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column goods_info.update_time
	 * @return  the value of goods_info.update_time
	 * @mbg.generated
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column goods_info.update_time
	 * @param updateTime  the value for goods_info.update_time
	 * @mbg.generated
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column goods_info.in_use
	 * @return  the value of goods_info.in_use
	 * @mbg.generated
	 */
	public Integer getInUse() {
		return inUse;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column goods_info.in_use
	 * @param inUse  the value for goods_info.in_use
	 * @mbg.generated
	 */
	public void setInUse(Integer inUse) {
		this.inUse = inUse;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column goods_info.keywords_ids
	 * @return  the value of goods_info.keywords_ids
	 * @mbg.generated
	 */
	public String getKeywordsIds() {
		return keywordsIds;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column goods_info.keywords_ids
	 * @param keywordsIds  the value for goods_info.keywords_ids
	 * @mbg.generated
	 */
	public void setKeywordsIds(String keywordsIds) {
		this.keywordsIds = keywordsIds == null ? null : keywordsIds.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column goods_info.keywords_names
	 * @return  the value of goods_info.keywords_names
	 * @mbg.generated
	 */
	public String getKeywordsNames() {
		return keywordsNames;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column goods_info.keywords_names
	 * @param keywordsNames  the value for goods_info.keywords_names
	 * @mbg.generated
	 */
	public void setKeywordsNames(String keywordsNames) {
		this.keywordsNames = keywordsNames == null ? null : keywordsNames.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column goods_info.goods_type
	 * @return  the value of goods_info.goods_type
	 * @mbg.generated
	 */
	public String getGoodsType() {
		return goodsType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column goods_info.goods_type
	 * @param goodsType  the value for goods_info.goods_type
	 * @mbg.generated
	 */
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType == null ? null : goodsType.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table goods_info
	 * @mbg.generated
	 */
	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (that == null) {
			return false;
		}
		if (getClass() != that.getClass()) {
			return false;
		}
		GoodsInfo other = (GoodsInfo) that;
		return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
				&& (this.getGoodsName() == null ? other.getGoodsName() == null
						: this.getGoodsName().equals(other.getGoodsName()))
				&& (this.getSupplierName() == null ? other.getSupplierName() == null
						: this.getSupplierName().equals(other.getSupplierName()))
				&& (this.getPrice() == null ? other.getPrice() == null : this.getPrice().equals(other.getPrice()))
				&& (this.getUnit() == null ? other.getUnit() == null : this.getUnit().equals(other.getUnit()))
				&& (this.getStock() == null ? other.getStock() == null : this.getStock().equals(other.getStock()))
				&& (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
				&& (this.getCreateUser() == null ? other.getCreateUser() == null
						: this.getCreateUser().equals(other.getCreateUser()))
				&& (this.getCreateTime() == null ? other.getCreateTime() == null
						: this.getCreateTime().equals(other.getCreateTime()))
				&& (this.getUpdateTime() == null ? other.getUpdateTime() == null
						: this.getUpdateTime().equals(other.getUpdateTime()))
				&& (this.getInUse() == null ? other.getInUse() == null : this.getInUse().equals(other.getInUse()))
				&& (this.getKeywordsIds() == null ? other.getKeywordsIds() == null
						: this.getKeywordsIds().equals(other.getKeywordsIds()))
				&& (this.getKeywordsNames() == null ? other.getKeywordsNames() == null
						: this.getKeywordsNames().equals(other.getKeywordsNames()))
				&& (this.getGoodsType() == null ? other.getGoodsType() == null
						: this.getGoodsType().equals(other.getGoodsType()));
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table goods_info
	 * @mbg.generated
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getGoodsName() == null) ? 0 : getGoodsName().hashCode());
		result = prime * result + ((getSupplierName() == null) ? 0 : getSupplierName().hashCode());
		result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
		result = prime * result + ((getUnit() == null) ? 0 : getUnit().hashCode());
		result = prime * result + ((getStock() == null) ? 0 : getStock().hashCode());
		result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
		result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
		result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
		result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
		result = prime * result + ((getInUse() == null) ? 0 : getInUse().hashCode());
		result = prime * result + ((getKeywordsIds() == null) ? 0 : getKeywordsIds().hashCode());
		result = prime * result + ((getKeywordsNames() == null) ? 0 : getKeywordsNames().hashCode());
		result = prime * result + ((getGoodsType() == null) ? 0 : getGoodsType().hashCode());
		return result;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table goods_info
	 * @mbg.generated
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", goodsName=").append(goodsName);
		sb.append(", supplierName=").append(supplierName);
		sb.append(", price=").append(price);
		sb.append(", unit=").append(unit);
		sb.append(", stock=").append(stock);
		sb.append(", type=").append(type);
		sb.append(", createUser=").append(createUser);
		sb.append(", createTime=").append(createTime);
		sb.append(", updateTime=").append(updateTime);
		sb.append(", inUse=").append(inUse);
		sb.append(", keywordsIds=").append(keywordsIds);
		sb.append(", keywordsNames=").append(keywordsNames);
		sb.append(", goodsType=").append(goodsType);
		sb.append("]");
		return sb.toString();
	}

	private List<String> keywordsNameList;
	
	/** 数量大于参数 */
	private Integer goodsCountGt;

	public List<String> getKeywordsNameList() {
		return keywordsNameList;
	}

	public void setKeywordsNameList(List<String> keywordsNameList) {
		this.keywordsNameList = keywordsNameList;
	}

	public Integer getGoodsCountGt() {
		return goodsCountGt;
	}

	public void setGoodsCountGt(Integer goodsCountGt) {
		this.goodsCountGt = goodsCountGt;
	}	
	
}