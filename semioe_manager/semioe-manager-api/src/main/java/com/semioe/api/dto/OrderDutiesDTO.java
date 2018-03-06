/**
 * 履约情况查询 数据传输对象
 */
package com.semioe.api.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.semioe.common.pageUtil.PageInfo;

public class OrderDutiesDTO extends PageInfo {

	private static final long serialVersionUID = 1L;
	/**
	 * 医生ID
	 */
	private String doctorId ;
	/**
	 * 机构ID
	 */
	private String orgId ;
	/**
	 * 履约开始数量
	 */
	private Integer startCountNum ;
	/**
	 * 履约结束数量
	 */
	private Integer endCountNum ;
	/**
	 * 家医签约类型： 1.家庭医生  ， 2.孕产妇
	 */
	private Integer buildType ;
	/**
	 * 开始时间
	 */
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
	private Date startTime;
	/**
	 * 结束时间
	 */
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
	private Date endTime;
	/**
	 * 查询类型: 2.公卫服务  ， 1.其他服务
	 */
	private Integer keyWordsType;
	/**
	 * 排序数组
	 */
	private List<BaseOrderByDTO> orderColumn;
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public Integer getStartCountNum() {
		return startCountNum;
	}
	public void setStartCountNum(Integer startCountNum) {
		this.startCountNum = startCountNum;
	}
	public Integer getEndCountNum() {
		return endCountNum;
	}
	public void setEndCountNum(Integer endCountNum) {
		this.endCountNum = endCountNum;
	}
	public Integer getBuildType() {
		return buildType;
	}
	public void setBuildType(Integer buildType) {
		this.buildType = buildType;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getKeyWordsType() {
		return keyWordsType;
	}
	public void setKeyWordsType(Integer keyWordsType) {
		this.keyWordsType = keyWordsType;
	}
	public List<BaseOrderByDTO> getOrderColumn() {
		return orderColumn;
	}
	public void setOrderColumn(List<BaseOrderByDTO> orderColumn) {
		this.orderColumn = orderColumn;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public void setJsonData(String jsonString){
		// 方式二： OrderDutiesDTO dto = JSONObject.parseObject(jsonString,OrderDutiesDTO.class);
		
		JSONObject json = JSONObject.parseObject(jsonString);
		
		if(json == null){
			return;
		}
		
		if(json.containsKey("doctorId")){
			this.doctorId = json.getString("doctorId");
		}
		if(json.containsKey("orgId")){
			this.orgId = json.getString("orgId");
		}
		if(json.containsKey("startCountNum")){
			this.startCountNum = json.getInteger("startCountNum");
		}
		if(json.containsKey("endCountNum")){
			this.endCountNum = json.getInteger("endCountNum");
		}
		if(json.containsKey("buildType")){
			this.buildType = json.getInteger("buildType");
		}
		if(json.containsKey("startTime")){
			this.startTime = json.getDate("startTime");
		}
		if(json.containsKey("endTime")){
			setEndTime(json.getDate("endTime"));
		}
		if(json.containsKey("keyWordsType")){
			this.keyWordsType = json.getInteger("keyWordsType");
		}
		if(json.containsKey("orderColumn")){
			JSONArray array = JSONArray.parseArray(json.getString("orderColumn"));
			if(array != null && array.size() > 0){
				this.orderColumn = new ArrayList<>();
				for(int i=0; i<array.size(); i++){
					BaseOrderByDTO dto = new BaseOrderByDTO();
					dto.setJsonData(array.getString(i));
					this.orderColumn.add(dto);
				}
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", doctorId=").append(doctorId);
		sb.append(", orgId=").append(orgId);
		sb.append(", startCountNum=").append(startCountNum);
		sb.append(", endCountNum=").append(endCountNum);
		sb.append(", buildType=").append(buildType);
		sb.append(", startTime=").append(startTime);
		sb.append(", endTime=").append(endTime);
		sb.append(", keyWordsType=").append(keyWordsType);
		sb.append(", orderColumn=").append(orderColumn);
		sb.append("]");
		return sb.toString();
	}
	
}
