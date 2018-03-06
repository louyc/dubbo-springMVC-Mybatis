/**
 * 订单履约情况数据展示对象
 */
package com.semioe.api.vo;

import java.io.Serializable;

public class SbOrderDutiesVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String dutiesKey;
	
	private String dutiesName;
	
	private Integer dutiesCount;


	public String getDutiesKey() {
		return dutiesKey;
	}

	public void setDutiesKey(String dutiesKey) {
		this.dutiesKey = dutiesKey;
	}

	public String getDutiesName() {
		return dutiesName;
	}

	public void setDutiesName(String dutiesName) {
		this.dutiesName = dutiesName;
	}

	public Integer getDutiesCount() {
		return dutiesCount;
	}

	public void setDutiesCount(Integer dutiesCount) {
		this.dutiesCount = dutiesCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
