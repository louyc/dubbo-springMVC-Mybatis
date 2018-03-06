/**
 * 基础排序查询对象
 */
package com.semioe.api.dto;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

public class BaseOrderByDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 排序列名称
	 */
	private String orderColumnName;
	/**
	 * 排序方式： DESC , ASC
	 */
	private String sortType;

	public String getOrderColumnName() {
		return orderColumnName;
	}

	public void setOrderColumnName(String orderColumnName) {
		this.orderColumnName = orderColumnName;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", orderColumnName=").append(orderColumnName);
		sb.append(", sortType=").append(sortType);
		sb.append("]");
		return sb.toString();
	}
	
	public void setJsonData(String jsonString){
		JSONObject json = JSONObject.parseObject(jsonString);
		
		if(json == null){
			return;
		}
		
		if(json != null && json.containsKey("orderColumnName")){
			this.orderColumnName = json.getString("orderColumnName");
		}
		if(json != null && json.containsKey("sortType")){
			this.sortType = json.getString("sortType");
		}
	}
	
}
