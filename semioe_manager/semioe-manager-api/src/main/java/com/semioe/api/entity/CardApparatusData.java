package com.semioe.api.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CardApparatusData {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column card_apparatus_data.id
	 * @mbg.generated
	 */
	private Integer id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column card_apparatus_data.device_id
	 * @mbg.generated
	 */
	private Integer deviceId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column card_apparatus_data.heart_rate_value
	 * @mbg.generated
	 */
	private Float heartRateValue;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column card_apparatus_data.ecg_result
	 * @mbg.generated
	 */
	private String ecgResult;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column card_apparatus_data.ecg_img_url
	 * @mbg.generated
	 */
	private String ecgImgUrl;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column card_apparatus_data.ecg_data
	 * @mbg.generated
	 */
	private String ecgData;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column card_apparatus_data.create_time
	 * @mbg.generated
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date createTime;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column card_apparatus_data.id
	 * @return  the value of card_apparatus_data.id
	 * @mbg.generated
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column card_apparatus_data.id
	 * @param id  the value for card_apparatus_data.id
	 * @mbg.generated
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column card_apparatus_data.device_id
	 * @return  the value of card_apparatus_data.device_id
	 * @mbg.generated
	 */
	public Integer getDeviceId() {
		return deviceId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column card_apparatus_data.device_id
	 * @param deviceId  the value for card_apparatus_data.device_id
	 * @mbg.generated
	 */
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column card_apparatus_data.heart_rate_value
	 * @return  the value of card_apparatus_data.heart_rate_value
	 * @mbg.generated
	 */
	public Float getHeartRateValue() {
		return heartRateValue;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column card_apparatus_data.heart_rate_value
	 * @param heartRateValue  the value for card_apparatus_data.heart_rate_value
	 * @mbg.generated
	 */
	public void setHeartRateValue(Float heartRateValue) {
		this.heartRateValue = heartRateValue;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column card_apparatus_data.ecg_result
	 * @return  the value of card_apparatus_data.ecg_result
	 * @mbg.generated
	 */
	public String getEcgResult() {
		return ecgResult;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column card_apparatus_data.ecg_result
	 * @param ecgResult  the value for card_apparatus_data.ecg_result
	 * @mbg.generated
	 */
	public void setEcgResult(String ecgResult) {
		this.ecgResult = ecgResult == null ? null : ecgResult.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column card_apparatus_data.ecg_img_url
	 * @return  the value of card_apparatus_data.ecg_img_url
	 * @mbg.generated
	 */
	public String getEcgImgUrl() {
		return ecgImgUrl;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column card_apparatus_data.ecg_img_url
	 * @param ecgImgUrl  the value for card_apparatus_data.ecg_img_url
	 * @mbg.generated
	 */
	public void setEcgImgUrl(String ecgImgUrl) {
		this.ecgImgUrl = ecgImgUrl == null ? null : ecgImgUrl.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column card_apparatus_data.ecg_data
	 * @return  the value of card_apparatus_data.ecg_data
	 * @mbg.generated
	 */
	public String getEcgData() {
		return ecgData;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column card_apparatus_data.ecg_data
	 * @param ecgData  the value for card_apparatus_data.ecg_data
	 * @mbg.generated
	 */
	public void setEcgData(String ecgData) {
		this.ecgData = ecgData == null ? null : ecgData.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column card_apparatus_data.create_time
	 * @return  the value of card_apparatus_data.create_time
	 * @mbg.generated
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column card_apparatus_data.create_time
	 * @param createTime  the value for card_apparatus_data.create_time
	 * @mbg.generated
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table card_apparatus_data
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
		CardApparatusData other = (CardApparatusData) that;
		return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
				&& (this.getDeviceId() == null ? other.getDeviceId() == null
						: this.getDeviceId().equals(other.getDeviceId()))
				&& (this.getHeartRateValue() == null ? other.getHeartRateValue() == null
						: this.getHeartRateValue().equals(other.getHeartRateValue()))
				&& (this.getEcgResult() == null ? other.getEcgResult() == null
						: this.getEcgResult().equals(other.getEcgResult()))
				&& (this.getEcgImgUrl() == null ? other.getEcgImgUrl() == null
						: this.getEcgImgUrl().equals(other.getEcgImgUrl()))
				&& (this.getEcgData() == null ? other.getEcgData() == null
						: this.getEcgData().equals(other.getEcgData()))
				&& (this.getCreateTime() == null ? other.getCreateTime() == null
						: this.getCreateTime().equals(other.getCreateTime()));
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table card_apparatus_data
	 * @mbg.generated
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getDeviceId() == null) ? 0 : getDeviceId().hashCode());
		result = prime * result + ((getHeartRateValue() == null) ? 0 : getHeartRateValue().hashCode());
		result = prime * result + ((getEcgResult() == null) ? 0 : getEcgResult().hashCode());
		result = prime * result + ((getEcgImgUrl() == null) ? 0 : getEcgImgUrl().hashCode());
		result = prime * result + ((getEcgData() == null) ? 0 : getEcgData().hashCode());
		result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
		return result;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table card_apparatus_data
	 * @mbg.generated
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", deviceId=").append(deviceId);
		sb.append(", heartRateValue=").append(heartRateValue);
		sb.append(", ecgResult=").append(ecgResult);
		sb.append(", ecgImgUrl=").append(ecgImgUrl);
		sb.append(", ecgData=").append(ecgData);
		sb.append(", createTime=").append(createTime);
		sb.append("]");
		return sb.toString();
	}
}