/**
 * 订单统计数据传输对象
 * 查询条件
 */
package com.semioe.api.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.semioe.common.pageUtil.PageInfo;

public class OrderStatisticDTO extends PageInfo {

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
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 订单类型：0.商品 ， 1.服务
	 */
	private Integer type;
	/**
	 * 查询类型: 1.公卫服务  ， 2.其他服务
	 */
	private Integer keyWordsType;
	/**
	 * 统计字段类型：  0.relationId 名称合并 , 1. supplierId 创建者合并
	 */
	private Integer groupColumn;
	/**
	 * 排序数组
	 */
	private List<BaseOrderByDTO> orderColumn;
	
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
	public Integer getGroupColumn() {
		return groupColumn;
	}
	public void setGroupColumn(Integer groupColumn) {
		this.groupColumn = groupColumn;
	}
	public List<BaseOrderByDTO> getOrderColumn() {
		return orderColumn;
	}
	public void setOrderColumn(List<BaseOrderByDTO> orderColumn) {
		this.orderColumn = orderColumn;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	public void setJsonData(String jsonString){
		
		JSONObject json = JSONObject.parseObject(jsonString);
		
		if(json == null){
			return;
		}
		
		if(json.containsKey("relationId")){
			this.relationId = json.getString("relationId");
		}
		if(json.containsKey("relationName")){
			this.relationName = json.getString("relationName");
		}
		if(json.containsKey("relationSupplierId")){
			this.relationSupplierId = json.getString("relationSupplierId");
		}
		if(json.containsKey("relationSupplier")){
			this.relationSupplier = json.getString("relationSupplier");
		}
		if(json.containsKey("startTime")){
			this.startTime = json.getDate("startTime");
		}
		if(json.containsKey("endTime")){
			setEndTime(json.getDate("endTime"));
		}
		if(json.containsKey("type")){
			this.type = json.getInteger("type");
		}
		if(json.containsKey("keyWordsType")){
			this.keyWordsType = json.getInteger("keyWordsType");
		}
		if(json.containsKey("groupColumn")){
			this.groupColumn = json.getInteger("groupColumn");
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
		sb.append(", relationId=").append(relationId);
		sb.append(", relationName=").append(relationName);
		sb.append(", relationSupplierId=").append(relationSupplierId);
		sb.append(", relationSupplier=").append(relationSupplier);
		sb.append(", startTime=").append(startTime);
		sb.append(", endTime=").append(endTime);
		sb.append(", type=").append(type);
		sb.append(", keyWordsType=").append(keyWordsType);
		sb.append(", groupColumn=").append(groupColumn);
		sb.append(", orderColumn=").append(orderColumn);
		sb.append("]");
		return sb.toString();
	}
	
}
