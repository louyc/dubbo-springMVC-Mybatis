package com.semioe.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.semioe.common.pageUtil.PageInfo;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户相关统计
 * 
 * @author
 *
 */
public class UserCountVO extends PageInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6476898525893041610L;
	// 性别
	private Double manCount = 0.0;
	private Double womanCount = 0.0;
	private Double secretCount = 0.0;
	private Double notExplainedCount = 0.0;
	// 年龄
	private Double childrenCount = 0.0;
	private Double juvenileCount = 0.0;
	private Double youthCount = 0.0;
	private Double middleAgeCount = 0.0;
	private Double oldAgeCount = 0.0;
	private Double noAgeCount = 0.0;
	// 疾病史
	private int hbpCount = 0; // 高血压
	private int dmCount = 0; // 糖尿病
	private int chdCount = 0; // 冠心病
	private int copdCount = 0; // 慢性阻塞性肺疾病
	private int mtCount = 0;// 恶性肿瘤
	private int cerebralApoplexyCount = 0;// 脑卒中
	private int mentalDisordeCount = 0;// 严重精神障碍
	private int tbCount = 0;// 结核病
	private int aihCount = 0;// 肝炎
	private int otherNotifiableDiseasesCount = 0;// 其他法定传染病
	private int ohCount = 0;// 职业病
	private int otherCount = 0;// 其他
	private int noCount = 0;// 无
	// 省份
	private String provinceId;
	private Double provinceCount = 0.0;;

	/** 统计日期 */
	private String countDate;

	private List<String> countDateList;
	private List<Map<String, Double>> dataList;
	private List<ProvinceVO> proviceList;

	private Double visitorSum = 0.0;
	private Double purchaserSum = 0.0;
	private Double conversionSum = 0.0;
	// 推广码 总额
	private Double sumPrice = 0.0;
	// 推广码 总人
	private Double sumPeople = 0.0;

	/** 访客数 */
	private Double visitorCount = 0.0;
	/** 购买数 */
	private Double purchaserCount = 0.0;
	/** 转化率 */
	private Double conversionRate = 0.0;
	/** 统计结果 */
	private Double countNumber = 0.0;

	private String sexs;

	private String ages;

	private String provinces;

	private String dieases; // 疾病史

	/** 签约总数 */
	private Double signSum = 0.0;
	/** 签约数 */
	private Double signCount = 0.0;

	private String orgs; // 多个机构
	private String doctors;// 多个医生

	private String doctorId;
	private String doctorName;
	private String orgName;
	private String signDate;

	private String buildType; // 0 全部 1：家医 3：孕产
	private String parentId; // 主账户id
	private String parentName;// 主账户名称
	private String parentMobile;// 主帐号帐号 手机号
	private String childs; // 家庭子女成员合集
	private String parentIds; // 父账户合集
	private Date createTime;// 签约时间
	private List<String> managerList; // 用户id集合
	private List<ApiContractedUserVO> listApiUser;// 家庭组成员信息

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getBuildType() {
		return buildType;
	}

	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}

	public List<String> getManagerList() {
		return managerList;
	}

	public void setManagerList(List<String> managerList) {
		this.managerList = managerList;
	}

	public String getCountDate() {
		return countDate;
	}

	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}

	public List<ApiContractedUserVO> getListApiUser() {
		return listApiUser;
	}

	public void setListApiUser(List<ApiContractedUserVO> listApiUser) {
		this.listApiUser = listApiUser;
	}

	public String getParentMobile() {
		return parentMobile;
	}

	public void setParentMobile(String parentMobile) {
		this.parentMobile = parentMobile;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
	public Date getCreateTime() {
		return createTime;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getChilds() {
		return childs;
	}

	public void setChilds(String childs) {
		this.childs = childs;
	}

	public String getOrgs() {
		return orgs;
	}

	public void setOrgs(String orgs) {
		this.orgs = orgs;
	}

	public String getDoctors() {
		return doctors;
	}

	public void setDoctors(String doctors) {
		this.doctors = doctors;
	}

	public Double getSignSum() {
		return signSum;
	}

	public void setSignSum(Double signSum) {
		this.signSum = signSum;
	}

	public Double getSignCount() {
		return signCount;
	}

	public void setSignCount(Double signCount) {
		this.signCount = signCount;
	}

	public Double getNotExplainedCount() {
		return notExplainedCount;
	}

	public void setNotExplainedCount(Double notExplainedCount) {
		this.notExplainedCount = notExplainedCount;
	}

	public String getDieases() {
		return dieases;
	}

	public void setDieases(String dieases) {
		this.dieases = dieases;
	}

	public int getHbpCount() {
		return hbpCount;
	}

	public void setHbpCount(int hbpCount) {
		this.hbpCount = hbpCount;
	}

	public int getDmCount() {
		return dmCount;
	}

	public void setDmCount(int dmCount) {
		this.dmCount = dmCount;
	}

	public int getChdCount() {
		return chdCount;
	}

	public void setChdCount(int chdCount) {
		this.chdCount = chdCount;
	}

	public int getCopdCount() {
		return copdCount;
	}

	public void setCopdCount(int copdCount) {
		this.copdCount = copdCount;
	}

	public int getMtCount() {
		return mtCount;
	}

	public void setMtCount(int mtCount) {
		this.mtCount = mtCount;
	}

	public int getCerebralApoplexyCount() {
		return cerebralApoplexyCount;
	}

	public void setCerebralApoplexyCount(int cerebralApoplexyCount) {
		this.cerebralApoplexyCount = cerebralApoplexyCount;
	}

	public int getMentalDisordeCount() {
		return mentalDisordeCount;
	}

	public void setMentalDisordeCount(int mentalDisordeCount) {
		this.mentalDisordeCount = mentalDisordeCount;
	}

	public int getTbCount() {
		return tbCount;
	}

	public void setTbCount(int tbCount) {
		this.tbCount = tbCount;
	}

	public int getAihCount() {
		return aihCount;
	}

	public void setAihCount(int aihCount) {
		this.aihCount = aihCount;
	}

	public int getOtherNotifiableDiseasesCount() {
		return otherNotifiableDiseasesCount;
	}

	public void setOtherNotifiableDiseasesCount(int otherNotifiableDiseasesCount) {
		this.otherNotifiableDiseasesCount = otherNotifiableDiseasesCount;
	}

	public int getOhCount() {
		return ohCount;
	}

	public void setOhCount(int ohCount) {
		this.ohCount = ohCount;
	}

	public int getOtherCount() {
		return otherCount;
	}

	public void setOtherCount(int otherCount) {
		this.otherCount = otherCount;
	}

	public int getNoCount() {
		return noCount;
	}

	public void setNoCount(int noCount) {
		this.noCount = noCount;
	}

	public List<ProvinceVO> getProviceList() {
		return proviceList;
	}

	public void setProviceList(List<ProvinceVO> proviceList) {
		this.proviceList = proviceList;
	}

	public List<String> getCountDateList() {
		return countDateList;
	}

	public void setCountDateList(List<String> countDateList) {
		this.countDateList = countDateList;
	}

	public String getProvinces() {
		return provinces;
	}

	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}

	public Double getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(Double sumPrice) {
		this.sumPrice = sumPrice;
	}

	public Double getSumPeople() {
		return sumPeople;
	}

	public void setSumPeople(Double sumPeople) {
		this.sumPeople = sumPeople;
	}

	/** 统计价格 */
	private Double countPrice;

	public String getProvinceId() {
		return provinceId;
	}

	public Double getVisitorSum() {
		return visitorSum;
	}

	public void setVisitorSum(Double visitorSum) {
		this.visitorSum = visitorSum;
	}

	public Double getPurchaserSum() {
		return purchaserSum;
	}

	public void setPurchaserSum(Double purchaserSum) {
		this.purchaserSum = purchaserSum;
	}

	public Double getConversionSum() {
		return conversionSum;
	}

	public void setConversionSum(Double conversionSum) {
		this.conversionSum = conversionSum;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public Double getProvinceCount() {
		return provinceCount;
	}

	public void setProvinceCount(Double provinceCount) {
		this.provinceCount = provinceCount;
	}

	public String getAges() {
		return ages;
	}

	public void setAges(String ages) {
		this.ages = ages;
	}

	public Double getNoAgeCount() {
		return noAgeCount;
	}

	public void setNoAgeCount(Double noAgeCount) {
		this.noAgeCount = noAgeCount;
	}

	public String getSexs() {
		return sexs;
	}

	public void setSexs(String sexs) {
		this.sexs = sexs;
	}

	public Double getVisitorCount() {
		return visitorCount;
	}

	public void setVisitorCount(Double visitorCount) {
		this.visitorCount = visitorCount;
	}

	public Double getPurchaserCount() {
		return purchaserCount;
	}

	public void setPurchaserCount(Double purchaserCount) {
		this.purchaserCount = purchaserCount;
	}

	public Double getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(Double conversionRate) {
		this.conversionRate = conversionRate;
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

	public Double getManCount() {
		return manCount;
	}

	public void setManCount(Double manCount) {
		this.manCount = manCount;
	}

	public Double getWomanCount() {
		return womanCount;
	}

	public void setWomanCount(Double womanCount) {
		this.womanCount = womanCount;
	}

	public Double getSecretCount() {
		return secretCount;
	}

	public void setSecretCount(Double secretCount) {
		this.secretCount = secretCount;
	}

	public Double getChildrenCount() {
		return childrenCount;
	}

	public void setChildrenCount(Double childrenCount) {
		this.childrenCount = childrenCount;
	}

	public Double getJuvenileCount() {
		return juvenileCount;
	}

	public void setJuvenileCount(Double juvenileCount) {
		this.juvenileCount = juvenileCount;
	}

	public Double getYouthCount() {
		return youthCount;
	}

	public void setYouthCount(Double youthCount) {
		this.youthCount = youthCount;
	}

	public Double getMiddleAgeCount() {
		return middleAgeCount;
	}

	public void setMiddleAgeCount(Double middleAgeCount) {
		this.middleAgeCount = middleAgeCount;
	}

	public Double getOldAgeCount() {
		return oldAgeCount;
	}

	public void setOldAgeCount(Double oldAgeCount) {
		this.oldAgeCount = oldAgeCount;
	}

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",
	// timezone = "PRC")
	// public Date getCountDate() {
	// return countDate;
	// }
	//
	//// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",
	// timezone = "PRC")
	// public void setCountDate(Date countDate) {
	// this.countDate = countDate;
	// }

	public List<Map<String, Double>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String, Double>> dataList) {
		this.dataList = dataList;
	}

}
