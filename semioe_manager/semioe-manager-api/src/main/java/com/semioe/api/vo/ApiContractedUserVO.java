package com.semioe.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.semioe.common.pageUtil.PageInfo;
import java.util.Date;
import java.util.List;

public class ApiContractedUserVO extends PageInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3628713229616595902L;

	private Integer id;

	private String managerId;

	private String name;

	private Integer sex;

	private String sexDesc;

	private String cardId;

	private Date birthday;

	private String contactPeople;

	private String contactMobile;

	private Integer residentType;

	private String residentTypeDesc;

	private String permanentAddress;

	private Integer nation;

	private String nationDesc;

	private Integer maritalStatus;

	private String maritalStatusDesc;

	private Integer culturalStatus;

	private String culturalStatusDesc;

	private Integer occupation;

	private String occupationDesc;

	private String workUnit;

	private String presentAddress;

	private Integer medicalType;

	private String medicalTypeDesc;

	private Integer bloody;

	private String bloodyDesc;

	private Integer rh;

	private String rhDesc;
	private Date expirationDate;

	private Integer years;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
	private Date createTime;

	private Date updateTime;

	private String inUse;

	List<UserHealthyLivingVO> dataList;

	private String age; // 年龄

	private String mobile;
	private String ageId; // 年龄段
	private String ageBegin;// 年龄段开始
	private String ageEnd; // 年龄段结束
	private String diseaseId;
	private String diseaseIds;

	private String contractedId;
	private String doctorId;
	private String doctorName;
	private List<String> doctorIdList;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
	private Date startTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
	private Date endTime;

	// 20171102
	private int buildType; // 1：签约用户 2：申请服务 3:孕产
	// 20171202
	private int roleId;
	private String roleName;
	private String parentId;
	private String parentName;
	private String parentMobile;
	private String symptom;
	// 20171216
	private String diseaseNames; // 疾病项
	private String orgId;
	private String orgName; // 机构名称
	private String apiName; // 微信帐号姓名
	private String signName; // 签约姓名

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getDiseaseNames() {
		return diseaseNames;
	}

	public void setDiseaseNames(String diseaseNames) {
		this.diseaseNames = diseaseNames;
	}

	public String getDiseaseIds() {
		return diseaseIds;
	}

	public void setDiseaseIds(String diseaseIds) {
		this.diseaseIds = diseaseIds;
	}

	public String getSymptom() {
		return symptom;
	}

	public void setSymptom(String symptom) {
		this.symptom = symptom;
	}

	public String getParentMobile() {
		return parentMobile;
	}

	public void setParentMobile(String parentMobile) {
		this.parentMobile = parentMobile;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public int getBuildType() {
		return buildType;
	}

	public void setBuildType(int buildType) {
		this.buildType = buildType;
	}

	public String getContractedId() {
		return contractedId;
	}

	public void setContractedId(String contractedId) {
		this.contractedId = contractedId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getAgeBegin() {
		return ageBegin;
	}

	public void setAgeBegin(String ageBegin) {
		this.ageBegin = ageBegin;
	}

	public String getAgeEnd() {
		return ageEnd;
	}

	public void setAgeEnd(String ageEnd) {
		this.ageEnd = ageEnd;
	}

	public List<String> getDoctorIdList() {
		return doctorIdList;
	}

	public void setDoctorIdList(List<String> doctorIdList) {
		this.doctorIdList = doctorIdList;
	}

	public List<UserHealthyLivingVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<UserHealthyLivingVO> dataList) {
		this.dataList = dataList;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAgeId() {
		return ageId;
	}

	public void setAgeId(String ageId) {
		this.ageId = ageId;
	}

	public String getDiseaseId() {
		return diseaseId;
	}

	public void setDiseaseId(String diseaseId) {
		this.diseaseId = diseaseId;
	}

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

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
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

	public String getSexDesc() {
		return sexDesc;
	}

	public void setSexDesc(String sexDesc) {
		this.sexDesc = sexDesc;
	}

	public String getResidentTypeDesc() {
		return residentTypeDesc;
	}

	public void setResidentTypeDesc(String residentTypeDesc) {
		this.residentTypeDesc = residentTypeDesc;
	}

	public String getNationDesc() {
		return nationDesc;
	}

	public void setNationDesc(String nationDesc) {
		this.nationDesc = nationDesc;
	}

	public String getMaritalStatusDesc() {
		return maritalStatusDesc;
	}

	public void setMaritalStatusDesc(String maritalStatusDesc) {
		this.maritalStatusDesc = maritalStatusDesc;
	}

	public String getCulturalStatusDesc() {
		return culturalStatusDesc;
	}

	public void setCulturalStatusDesc(String culturalStatusDesc) {
		this.culturalStatusDesc = culturalStatusDesc;
	}

	public String getOccupationDesc() {
		return occupationDesc;
	}

	public void setOccupationDesc(String occupationDesc) {
		this.occupationDesc = occupationDesc;
	}

	public String getMedicalTypeDesc() {
		return medicalTypeDesc;
	}

	public void setMedicalTypeDesc(String medicalTypeDesc) {
		this.medicalTypeDesc = medicalTypeDesc;
	}

	public String getBloodyDesc() {
		return bloodyDesc;
	}

	public void setBloodyDesc(String bloodyDesc) {
		this.bloodyDesc = bloodyDesc;
	}

	public String getRhDesc() {
		return rhDesc;
	}

	public void setRhDesc(String rhDesc) {
		this.rhDesc = rhDesc;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getContactPeople() {
		return contactPeople;
	}

	public void setContactPeople(String contactPeople) {
		this.contactPeople = contactPeople;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public Integer getResidentType() {
		return residentType;
	}

	public void setResidentType(Integer residentType) {
		this.residentType = residentType;
	}

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public Integer getNation() {
		return nation;
	}

	public void setNation(Integer nation) {
		this.nation = nation;
	}

	public Integer getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(Integer maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public Integer getCulturalStatus() {
		return culturalStatus;
	}

	public void setCulturalStatus(Integer culturalStatus) {
		this.culturalStatus = culturalStatus;
	}

	public Integer getOccupation() {
		return occupation;
	}

	public void setOccupation(Integer occupation) {
		this.occupation = occupation;
	}

	public String getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}

	public String getPresentAddress() {
		return presentAddress;
	}

	public void setPresentAddress(String presentAddress) {
		this.presentAddress = presentAddress;
	}

	public Integer getMedicalType() {
		return medicalType;
	}

	public void setMedicalType(Integer medicalType) {
		this.medicalType = medicalType;
	}

	public Integer getBloody() {
		return bloody;
	}

	public void setBloody(Integer bloody) {
		this.bloody = bloody;
	}

	public Integer getRh() {
		return rh;
	}

	public void setRh(Integer rh) {
		this.rh = rh;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Integer getYears() {
		return years;
	}

	public void setYears(Integer years) {
		this.years = years;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
	public Date getCreateTime() {
		return createTime;
	}
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getInUse() {
		return inUse;
	}

	public void setInUse(String inUse) {
		this.inUse = inUse;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}