package com.semioe.api.vo;

import com.semioe.api.entity.ApiMotherhoodUser;

/**
 * Created by kwinxu on 2017/12/19.
 */
public class ApiMotherhoodUserVO extends ApiMotherhoodUser {

	private String healthCareGuidanceDesc;

	private String permanentAddressDesc;

	private String husbandCardTypeDesc;
	private String orgId;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getHusbandCardTypeDesc() {
		return husbandCardTypeDesc;
	}

	public void setHusbandCardTypeDesc(String husbandCardTypeDesc) {
		this.husbandCardTypeDesc = husbandCardTypeDesc;
	}

	private String inOutSegmentDesc;

	private String gestationTimesDesc;

	private String medicineTypeDesc;

	private String medicineTimeDesc;

	private String culturalStatusDesc;

	private String nationalityDesc;

	private String nationDesc;

	private String occupationDesc;

	private String maritalStatusDesc;

	public String getHealthCareGuidanceDesc() {
		return healthCareGuidanceDesc;
	}

	public void setHealthCareGuidanceDesc(String healthCareGuidanceDesc) {
		this.healthCareGuidanceDesc = healthCareGuidanceDesc;
	}

	public String getPermanentAddressDesc() {
		return permanentAddressDesc;
	}

	public void setPermanentAddressDesc(String permanentAddressDesc) {
		this.permanentAddressDesc = permanentAddressDesc;
	}

	public String getInOutSegmentDesc() {
		return inOutSegmentDesc;
	}

	public void setInOutSegmentDesc(String inOutSegmentDesc) {
		this.inOutSegmentDesc = inOutSegmentDesc;
	}

	public String getGestationTimesDesc() {
		return gestationTimesDesc;
	}

	public void setGestationTimesDesc(String gestationTimesDesc) {
		this.gestationTimesDesc = gestationTimesDesc;
	}

	public String getMedicineTypeDesc() {
		return medicineTypeDesc;
	}

	public void setMedicineTypeDesc(String medicineTypeDesc) {
		this.medicineTypeDesc = medicineTypeDesc;
	}

	public String getMedicineTimeDesc() {
		return medicineTimeDesc;
	}

	public void setMedicineTimeDesc(String medicineTimeDesc) {
		this.medicineTimeDesc = medicineTimeDesc;
	}

	public String getCulturalStatusDesc() {
		return culturalStatusDesc;
	}

	public void setCulturalStatusDesc(String culturalStatusDesc) {
		this.culturalStatusDesc = culturalStatusDesc;
	}

	public String getNationalityDesc() {
		return nationalityDesc;
	}

	public void setNationalityDesc(String nationalityDesc) {
		this.nationalityDesc = nationalityDesc;
	}

	public String getNationDesc() {
		return nationDesc;
	}

	public void setNationDesc(String nationDesc) {
		this.nationDesc = nationDesc;
	}

	public String getOccupationDesc() {
		return occupationDesc;
	}

	public void setOccupationDesc(String occupationDesc) {
		this.occupationDesc = occupationDesc;
	}

	public String getMaritalStatusDesc() {
		return maritalStatusDesc;
	}

	public void setMaritalStatusDesc(String maritalStatusDesc) {
		this.maritalStatusDesc = maritalStatusDesc;
	}
}
