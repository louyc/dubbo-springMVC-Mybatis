package com.semioe.api.dto;

import java.io.Serializable;

/**
 * @Description 运营数据统计
 * @author xuyuxing
 * @date 2017年7月29日
 */
public class Statistics implements Serializable{
	private String date; // 日期
	private int countNumber; // 统计数量

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getCountNumber() {
		return countNumber;
	}

	public void setCountNumber(int countNumber) {
		this.countNumber = countNumber;
	}
}
