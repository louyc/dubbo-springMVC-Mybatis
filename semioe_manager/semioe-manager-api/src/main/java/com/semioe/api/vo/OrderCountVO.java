package com.semioe.api.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单统计页面对象
 * @author puanl
 *
 */
public class OrderCountVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 统计日期 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
	private Date countDate;
	
	/* 统计数量 */
	private Integer statisticCount;
	
	/** 统计结果 */
	private Double countNumber;
	
	/** 统计价格 */
	private Double countPrice;

	public Date getCountDate() {
		return countDate;
	}

	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}

	public Double getCountNumber() {
		return countNumber;
	}

	public void setCountNumber(Double countNumber) {
		this.countNumber = countNumber;
	}

	public Double getCountPrice() {
		return countPrice;
	}

	public void setCountPrice(Double countPrice) {
		this.countPrice = countPrice;
	}

	public Integer getStatisticCount() {
		return statisticCount;
	}

	public void setStatisticCount(Integer statisticCount) {
		this.statisticCount = statisticCount;
	}
	
	
}
