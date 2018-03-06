package com.semioe.api.vo;

import java.io.Serializable;

public class ProvinceVO implements Serializable {

	private String data;

	private String name;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", data=").append(data);
		sb.append(", name=").append(name);
		sb.append("]");
		return sb.toString();
	}
}