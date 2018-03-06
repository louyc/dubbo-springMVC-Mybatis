package com.semioe.api.vo;

import com.semioe.api.entity.GoodsInfo;
import com.semioe.common.pageUtil.PageInfo;
import java.util.Date;
import java.util.List;

public class ImplementSetDetailVO extends PageInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4853593308877059427L;

	private Integer id;

	private Integer implementId;

	private Integer serviceType;

	private String serviceTypeDesc;

	private List<ServiceInfoVO> serviceList;

	private List<GoodsInfo> goodsList;

	public List<ServiceInfoVO> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<ServiceInfoVO> serviceList) {
		this.serviceList = serviceList;
	}

	public List<GoodsInfo> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsInfo> goodsList) {
		this.goodsList = goodsList;
	}

	public String getServiceTypeDesc() {
		return serviceTypeDesc;
	}

	public void setServiceTypeDesc(String serviceTypeDesc) {
		this.serviceTypeDesc = serviceTypeDesc;
	}

	private String imgUrl;

	private String title;

	private String description;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column implement_set_detail.display_order
	 *
	 * @mbg.generated
	 */
	private Integer displayOrder;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column implement_set_detail.content_url
	 *
	 * @mbg.generated
	 */
	private String contentUrl;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column implement_set_detail.create_time
	 *
	 * @mbg.generated
	 */
	private Date createTime;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column implement_set_detail.update_time
	 *
	 * @mbg.generated
	 */
	private Date updateTime;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column implement_set_detail.in_use
	 *
	 * @mbg.generated
	 */
	private Integer inUse;

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column implement_set_detail.id
	 *
	 * @return the value of implement_set_detail.id
	 *
	 * @mbg.generated
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column implement_set_detail.id
	 *
	 * @param id
	 *            the value for implement_set_detail.id
	 *
	 * @mbg.generated
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column implement_set_detail.implement_id
	 *
	 * @return the value of implement_set_detail.implement_id
	 *
	 * @mbg.generated
	 */
	public Integer getImplementId() {
		return implementId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column implement_set_detail.implement_id
	 *
	 * @param implementId
	 *            the value for implement_set_detail.implement_id
	 *
	 * @mbg.generated
	 */
	public void setImplementId(Integer implementId) {
		this.implementId = implementId == null ? null : implementId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column implement_set_detail.service_type
	 *
	 * @return the value of implement_set_detail.service_type
	 *
	 * @mbg.generated
	 */
	public Integer getServiceType() {
		return serviceType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column implement_set_detail.service_type
	 *
	 * @param serviceType
	 *            the value for implement_set_detail.service_type
	 *
	 * @mbg.generated
	 */
	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column implement_set_detail.img_url
	 *
	 * @return the value of implement_set_detail.img_url
	 *
	 * @mbg.generated
	 */
	public String getImgUrl() {
		return imgUrl;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column implement_set_detail.img_url
	 *
	 * @param imgUrl
	 *            the value for implement_set_detail.img_url
	 *
	 * @mbg.generated
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl == null ? null : imgUrl.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column implement_set_detail.title
	 *
	 * @return the value of implement_set_detail.title
	 *
	 * @mbg.generated
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column implement_set_detail.title
	 *
	 * @param title
	 *            the value for implement_set_detail.title
	 *
	 * @mbg.generated
	 */
	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column implement_set_detail.description
	 *
	 * @return the value of implement_set_detail.description
	 *
	 * @mbg.generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column implement_set_detail.description
	 *
	 * @param description
	 *            the value for implement_set_detail.description
	 *
	 * @mbg.generated
	 */
	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column implement_set_detail.display_order
	 *
	 * @return the value of implement_set_detail.display_order
	 *
	 * @mbg.generated
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column implement_set_detail.display_order
	 *
	 * @param displayOrder
	 *            the value for implement_set_detail.display_order
	 *
	 * @mbg.generated
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column implement_set_detail.content_url
	 *
	 * @return the value of implement_set_detail.content_url
	 *
	 * @mbg.generated
	 */
	public String getContentUrl() {
		return contentUrl;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column implement_set_detail.content_url
	 *
	 * @param contentUrl
	 *            the value for implement_set_detail.content_url
	 *
	 * @mbg.generated
	 */
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl == null ? null : contentUrl.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column implement_set_detail.create_time
	 *
	 * @return the value of implement_set_detail.create_time
	 *
	 * @mbg.generated
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column implement_set_detail.create_time
	 *
	 * @param createTime
	 *            the value for implement_set_detail.create_time
	 *
	 * @mbg.generated
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column implement_set_detail.update_time
	 *
	 * @return the value of implement_set_detail.update_time
	 *
	 * @mbg.generated
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column implement_set_detail.update_time
	 *
	 * @param updateTime
	 *            the value for implement_set_detail.update_time
	 *
	 * @mbg.generated
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column implement_set_detail.in_use
	 *
	 * @return the value of implement_set_detail.in_use
	 *
	 * @mbg.generated
	 */
	public Integer getInUse() {
		return inUse;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column implement_set_detail.in_use
	 *
	 * @param inUse
	 *            the value for implement_set_detail.in_use
	 *
	 * @mbg.generated
	 */
	public void setInUse(Integer inUse) {
		this.inUse = inUse;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table implement_set_detail
	 *
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
		ImplementSetDetailVO other = (ImplementSetDetailVO) that;
		return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
				&& (this.getImplementId() == null ? other.getImplementId() == null
						: this.getImplementId().equals(other.getImplementId()))
				&& (this.getServiceType() == null ? other.getServiceType() == null
						: this.getServiceType().equals(other.getServiceType()))
				&& (this.getImgUrl() == null ? other.getImgUrl() == null
						: this.getImgUrl().equals(other.getImgUrl()))
				&& (this.getTitle() == null ? other.getTitle() == null
						: this.getTitle().equals(other.getTitle()))
				&& (this.getDescription() == null ? other.getDescription() == null
						: this.getDescription().equals(other.getDescription()))
				&& (this.getDisplayOrder() == null ? other.getDisplayOrder() == null
						: this.getDisplayOrder().equals(other.getDisplayOrder()))
				&& (this.getContentUrl() == null ? other.getContentUrl() == null
						: this.getContentUrl().equals(other.getContentUrl()))
				&& (this.getCreateTime() == null ? other.getCreateTime() == null
						: this.getCreateTime().equals(other.getCreateTime()))
				&& (this.getUpdateTime() == null ? other.getUpdateTime() == null
						: this.getUpdateTime().equals(other.getUpdateTime()))
				&& (this.getInUse() == null ? other.getInUse() == null
						: this.getInUse().equals(other.getInUse()));
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table implement_set_detail
	 *
	 * @mbg.generated
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getImplementId() == null) ? 0 : getImplementId().hashCode());
		result = prime * result + ((getServiceType() == null) ? 0 : getServiceType().hashCode());
		result = prime * result + ((getImgUrl() == null) ? 0 : getImgUrl().hashCode());
		result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
		result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
		result = prime * result + ((getDisplayOrder() == null) ? 0 : getDisplayOrder().hashCode());
		result = prime * result + ((getContentUrl() == null) ? 0 : getContentUrl().hashCode());
		result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
		result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
		result = prime * result + ((getInUse() == null) ? 0 : getInUse().hashCode());
		return result;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table implement_set_detail
	 *
	 * @mbg.generated
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", implementId=").append(implementId);
		sb.append(", serviceType=").append(serviceType);
		sb.append(", imgUrl=").append(imgUrl);
		sb.append(", title=").append(title);
		sb.append(", description=").append(description);
		sb.append(", displayOrder=").append(displayOrder);
		sb.append(", contentUrl=").append(contentUrl);
		sb.append(", createTime=").append(createTime);
		sb.append(", updateTime=").append(updateTime);
		sb.append(", inUse=").append(inUse);
		sb.append("]");
		return sb.toString();
	}
}