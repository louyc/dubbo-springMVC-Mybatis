package com.semioe.api.dto;

import java.io.Serializable;

public class SbOrderStatisticDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 统计类型ID
	 */
	private Integer keywordsId;
	
	/**
	 * 机构ID
	 */
	private String orgId;

	public Integer getKeywordsId() {
		return keywordsId;
	}

	public void setKeywordsId(Integer keywordsId) {
		this.keywordsId = keywordsId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
