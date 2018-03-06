package com.semioe.api.entity;

import com.semioe.common.pageUtil.PageInfo;

import java.util.Date;

public class QrcodeInfo extends PageInfo{

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column qrcode_info.id
	 * @mbg.generated
	 */
	private Integer id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column qrcode_info.manager_id
	 * @mbg.generated
	 */
	private String managerId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column qrcode_info.is_promotion_name
	 * @mbg.generated
	 */
	private String isPromotionName;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column qrcode_info.ticket
	 * @mbg.generated
	 */
	private String ticket;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column qrcode_info.create_time
	 * @mbg.generated
	 */
	private Date createTime;


	private Integer qrType;

	public Integer getQrType() {
		return qrType;
	}

	public void setQrType(Integer qrType) {
		this.qrType = qrType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column qrcode_info.id
	 * @return  the value of qrcode_info.id
	 * @mbg.generated
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column qrcode_info.id
	 * @param id  the value for qrcode_info.id
	 * @mbg.generated
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column qrcode_info.manager_id
	 * @return  the value of qrcode_info.manager_id
	 * @mbg.generated
	 */
	public String getManagerId() {
		return managerId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column qrcode_info.manager_id
	 * @param managerId  the value for qrcode_info.manager_id
	 * @mbg.generated
	 */
	public void setManagerId(String managerId) {
		this.managerId = managerId == null ? null : managerId.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column qrcode_info.is_promotion_name
	 * @return  the value of qrcode_info.is_promotion_name
	 * @mbg.generated
	 */
	public String getIsPromotionName() {
		return isPromotionName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column qrcode_info.is_promotion_name
	 * @param isPromotionName  the value for qrcode_info.is_promotion_name
	 * @mbg.generated
	 */
	public void setIsPromotionName(String isPromotionName) {
		this.isPromotionName = isPromotionName == null ? null : isPromotionName.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column qrcode_info.ticket
	 * @return  the value of qrcode_info.ticket
	 * @mbg.generated
	 */
	public String getTicket() {
		return ticket;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column qrcode_info.ticket
	 * @param ticket  the value for qrcode_info.ticket
	 * @mbg.generated
	 */
	public void setTicket(String ticket) {
		this.ticket = ticket == null ? null : ticket.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column qrcode_info.create_time
	 * @return  the value of qrcode_info.create_time
	 * @mbg.generated
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column qrcode_info.create_time
	 * @param createTime  the value for qrcode_info.create_time
	 * @mbg.generated
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_info
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
		QrcodeInfo other = (QrcodeInfo) that;
		return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
				&& (this.getManagerId() == null ? other.getManagerId() == null
						: this.getManagerId().equals(other.getManagerId()))
				&& (this.getIsPromotionName() == null ? other.getIsPromotionName() == null
						: this.getIsPromotionName().equals(other.getIsPromotionName()))
				&& (this.getTicket() == null ? other.getTicket() == null : this.getTicket().equals(other.getTicket()))
				&& (this.getCreateTime() == null ? other.getCreateTime() == null
						: this.getCreateTime().equals(other.getCreateTime()));
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_info
	 * @mbg.generated
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getManagerId() == null) ? 0 : getManagerId().hashCode());
		result = prime * result + ((getIsPromotionName() == null) ? 0 : getIsPromotionName().hashCode());
		result = prime * result + ((getTicket() == null) ? 0 : getTicket().hashCode());
		result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
		return result;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_info
	 * @mbg.generated
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", managerId=").append(managerId);
		sb.append(", isPromotionName=").append(isPromotionName);
		sb.append(", ticket=").append(ticket);
		sb.append(", createTime=").append(createTime);
		sb.append("]");
		return sb.toString();
	}
}