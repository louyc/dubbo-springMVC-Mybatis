package com.semioe.api.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BackstageUserInfoExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	public BackstageUserInfoExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	protected abstract static class GeneratedCriteria {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<Criterion>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value, String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andManagerIdIsNull() {
			addCriterion("manager_id is null");
			return (Criteria) this;
		}

		public Criteria andManagerIdIsNotNull() {
			addCriterion("manager_id is not null");
			return (Criteria) this;
		}

		public Criteria andManagerIdEqualTo(String value) {
			addCriterion("manager_id =", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdNotEqualTo(String value) {
			addCriterion("manager_id <>", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdGreaterThan(String value) {
			addCriterion("manager_id >", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdGreaterThanOrEqualTo(String value) {
			addCriterion("manager_id >=", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdLessThan(String value) {
			addCriterion("manager_id <", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdLessThanOrEqualTo(String value) {
			addCriterion("manager_id <=", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdLike(String value) {
			addCriterion("manager_id like", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdNotLike(String value) {
			addCriterion("manager_id not like", value, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdIn(List<String> values) {
			addCriterion("manager_id in", values, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdNotIn(List<String> values) {
			addCriterion("manager_id not in", values, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdBetween(String value1, String value2) {
			addCriterion("manager_id between", value1, value2, "managerId");
			return (Criteria) this;
		}

		public Criteria andManagerIdNotBetween(String value1, String value2) {
			addCriterion("manager_id not between", value1, value2, "managerId");
			return (Criteria) this;
		}

		public Criteria andNameIsNull() {
			addCriterion("name is null");
			return (Criteria) this;
		}

		public Criteria andNameIsNotNull() {
			addCriterion("name is not null");
			return (Criteria) this;
		}

		public Criteria andNameEqualTo(String value) {
			addCriterion("name =", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameNotEqualTo(String value) {
			addCriterion("name <>", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameGreaterThan(String value) {
			addCriterion("name >", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameGreaterThanOrEqualTo(String value) {
			addCriterion("name >=", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameLessThan(String value) {
			addCriterion("name <", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameLessThanOrEqualTo(String value) {
			addCriterion("name <=", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameLike(String value) {
			addCriterion("name like", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameNotLike(String value) {
			addCriterion("name not like", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameIn(List<String> values) {
			addCriterion("name in", values, "name");
			return (Criteria) this;
		}

		public Criteria andNameNotIn(List<String> values) {
			addCriterion("name not in", values, "name");
			return (Criteria) this;
		}

		public Criteria andNameBetween(String value1, String value2) {
			addCriterion("name between", value1, value2, "name");
			return (Criteria) this;
		}

		public Criteria andNameNotBetween(String value1, String value2) {
			addCriterion("name not between", value1, value2, "name");
			return (Criteria) this;
		}

		public Criteria andImageUrlIsNull() {
			addCriterion("image_url is null");
			return (Criteria) this;
		}

		public Criteria andImageUrlIsNotNull() {
			addCriterion("image_url is not null");
			return (Criteria) this;
		}

		public Criteria andImageUrlEqualTo(String value) {
			addCriterion("image_url =", value, "imageUrl");
			return (Criteria) this;
		}

		public Criteria andImageUrlNotEqualTo(String value) {
			addCriterion("image_url <>", value, "imageUrl");
			return (Criteria) this;
		}

		public Criteria andImageUrlGreaterThan(String value) {
			addCriterion("image_url >", value, "imageUrl");
			return (Criteria) this;
		}

		public Criteria andImageUrlGreaterThanOrEqualTo(String value) {
			addCriterion("image_url >=", value, "imageUrl");
			return (Criteria) this;
		}

		public Criteria andImageUrlLessThan(String value) {
			addCriterion("image_url <", value, "imageUrl");
			return (Criteria) this;
		}

		public Criteria andImageUrlLessThanOrEqualTo(String value) {
			addCriterion("image_url <=", value, "imageUrl");
			return (Criteria) this;
		}

		public Criteria andImageUrlLike(String value) {
			addCriterion("image_url like", value, "imageUrl");
			return (Criteria) this;
		}

		public Criteria andImageUrlNotLike(String value) {
			addCriterion("image_url not like", value, "imageUrl");
			return (Criteria) this;
		}

		public Criteria andImageUrlIn(List<String> values) {
			addCriterion("image_url in", values, "imageUrl");
			return (Criteria) this;
		}

		public Criteria andImageUrlNotIn(List<String> values) {
			addCriterion("image_url not in", values, "imageUrl");
			return (Criteria) this;
		}

		public Criteria andImageUrlBetween(String value1, String value2) {
			addCriterion("image_url between", value1, value2, "imageUrl");
			return (Criteria) this;
		}

		public Criteria andImageUrlNotBetween(String value1, String value2) {
			addCriterion("image_url not between", value1, value2, "imageUrl");
			return (Criteria) this;
		}

		public Criteria andTypeIsNull() {
			addCriterion("type is null");
			return (Criteria) this;
		}

		public Criteria andTypeIsNotNull() {
			addCriterion("type is not null");
			return (Criteria) this;
		}

		public Criteria andTypeEqualTo(String value) {
			addCriterion("type =", value, "type");
			return (Criteria) this;
		}

		public Criteria andTypeNotEqualTo(String value) {
			addCriterion("type <>", value, "type");
			return (Criteria) this;
		}

		public Criteria andTypeGreaterThan(String value) {
			addCriterion("type >", value, "type");
			return (Criteria) this;
		}

		public Criteria andTypeGreaterThanOrEqualTo(String value) {
			addCriterion("type >=", value, "type");
			return (Criteria) this;
		}

		public Criteria andTypeLessThan(String value) {
			addCriterion("type <", value, "type");
			return (Criteria) this;
		}

		public Criteria andTypeLessThanOrEqualTo(String value) {
			addCriterion("type <=", value, "type");
			return (Criteria) this;
		}

		public Criteria andTypeLike(String value) {
			addCriterion("type like", value, "type");
			return (Criteria) this;
		}

		public Criteria andTypeNotLike(String value) {
			addCriterion("type not like", value, "type");
			return (Criteria) this;
		}

		public Criteria andTypeIn(List<String> values) {
			addCriterion("type in", values, "type");
			return (Criteria) this;
		}

		public Criteria andTypeNotIn(List<String> values) {
			addCriterion("type not in", values, "type");
			return (Criteria) this;
		}

		public Criteria andTypeBetween(String value1, String value2) {
			addCriterion("type between", value1, value2, "type");
			return (Criteria) this;
		}

		public Criteria andTypeNotBetween(String value1, String value2) {
			addCriterion("type not between", value1, value2, "type");
			return (Criteria) this;
		}

		public Criteria andAddressIsNull() {
			addCriterion("address is null");
			return (Criteria) this;
		}

		public Criteria andAddressIsNotNull() {
			addCriterion("address is not null");
			return (Criteria) this;
		}

		public Criteria andAddressEqualTo(String value) {
			addCriterion("address =", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressNotEqualTo(String value) {
			addCriterion("address <>", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressGreaterThan(String value) {
			addCriterion("address >", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressGreaterThanOrEqualTo(String value) {
			addCriterion("address >=", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressLessThan(String value) {
			addCriterion("address <", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressLessThanOrEqualTo(String value) {
			addCriterion("address <=", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressLike(String value) {
			addCriterion("address like", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressNotLike(String value) {
			addCriterion("address not like", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressIn(List<String> values) {
			addCriterion("address in", values, "address");
			return (Criteria) this;
		}

		public Criteria andAddressNotIn(List<String> values) {
			addCriterion("address not in", values, "address");
			return (Criteria) this;
		}

		public Criteria andAddressBetween(String value1, String value2) {
			addCriterion("address between", value1, value2, "address");
			return (Criteria) this;
		}

		public Criteria andAddressNotBetween(String value1, String value2) {
			addCriterion("address not between", value1, value2, "address");
			return (Criteria) this;
		}

		public Criteria andCompanyIsNull() {
			addCriterion("company is null");
			return (Criteria) this;
		}

		public Criteria andCompanyIsNotNull() {
			addCriterion("company is not null");
			return (Criteria) this;
		}

		public Criteria andCompanyEqualTo(String value) {
			addCriterion("company =", value, "company");
			return (Criteria) this;
		}

		public Criteria andCompanyNotEqualTo(String value) {
			addCriterion("company <>", value, "company");
			return (Criteria) this;
		}

		public Criteria andCompanyGreaterThan(String value) {
			addCriterion("company >", value, "company");
			return (Criteria) this;
		}

		public Criteria andCompanyGreaterThanOrEqualTo(String value) {
			addCriterion("company >=", value, "company");
			return (Criteria) this;
		}

		public Criteria andCompanyLessThan(String value) {
			addCriterion("company <", value, "company");
			return (Criteria) this;
		}

		public Criteria andCompanyLessThanOrEqualTo(String value) {
			addCriterion("company <=", value, "company");
			return (Criteria) this;
		}

		public Criteria andCompanyLike(String value) {
			addCriterion("company like", value, "company");
			return (Criteria) this;
		}

		public Criteria andCompanyNotLike(String value) {
			addCriterion("company not like", value, "company");
			return (Criteria) this;
		}

		public Criteria andCompanyIn(List<String> values) {
			addCriterion("company in", values, "company");
			return (Criteria) this;
		}

		public Criteria andCompanyNotIn(List<String> values) {
			addCriterion("company not in", values, "company");
			return (Criteria) this;
		}

		public Criteria andCompanyBetween(String value1, String value2) {
			addCriterion("company between", value1, value2, "company");
			return (Criteria) this;
		}

		public Criteria andCompanyNotBetween(String value1, String value2) {
			addCriterion("company not between", value1, value2, "company");
			return (Criteria) this;
		}

		public Criteria andSkillsIsNull() {
			addCriterion("skills is null");
			return (Criteria) this;
		}

		public Criteria andSkillsIsNotNull() {
			addCriterion("skills is not null");
			return (Criteria) this;
		}

		public Criteria andSkillsEqualTo(String value) {
			addCriterion("skills =", value, "skills");
			return (Criteria) this;
		}

		public Criteria andSkillsNotEqualTo(String value) {
			addCriterion("skills <>", value, "skills");
			return (Criteria) this;
		}

		public Criteria andSkillsGreaterThan(String value) {
			addCriterion("skills >", value, "skills");
			return (Criteria) this;
		}

		public Criteria andSkillsGreaterThanOrEqualTo(String value) {
			addCriterion("skills >=", value, "skills");
			return (Criteria) this;
		}

		public Criteria andSkillsLessThan(String value) {
			addCriterion("skills <", value, "skills");
			return (Criteria) this;
		}

		public Criteria andSkillsLessThanOrEqualTo(String value) {
			addCriterion("skills <=", value, "skills");
			return (Criteria) this;
		}

		public Criteria andSkillsLike(String value) {
			addCriterion("skills like", value, "skills");
			return (Criteria) this;
		}

		public Criteria andSkillsNotLike(String value) {
			addCriterion("skills not like", value, "skills");
			return (Criteria) this;
		}

		public Criteria andSkillsIn(List<String> values) {
			addCriterion("skills in", values, "skills");
			return (Criteria) this;
		}

		public Criteria andSkillsNotIn(List<String> values) {
			addCriterion("skills not in", values, "skills");
			return (Criteria) this;
		}

		public Criteria andSkillsBetween(String value1, String value2) {
			addCriterion("skills between", value1, value2, "skills");
			return (Criteria) this;
		}

		public Criteria andSkillsNotBetween(String value1, String value2) {
			addCriterion("skills not between", value1, value2, "skills");
			return (Criteria) this;
		}

		public Criteria andTitleIsNull() {
			addCriterion("title is null");
			return (Criteria) this;
		}

		public Criteria andTitleIsNotNull() {
			addCriterion("title is not null");
			return (Criteria) this;
		}

		public Criteria andTitleEqualTo(String value) {
			addCriterion("title =", value, "title");
			return (Criteria) this;
		}

		public Criteria andTitleNotEqualTo(String value) {
			addCriterion("title <>", value, "title");
			return (Criteria) this;
		}

		public Criteria andTitleGreaterThan(String value) {
			addCriterion("title >", value, "title");
			return (Criteria) this;
		}

		public Criteria andTitleGreaterThanOrEqualTo(String value) {
			addCriterion("title >=", value, "title");
			return (Criteria) this;
		}

		public Criteria andTitleLessThan(String value) {
			addCriterion("title <", value, "title");
			return (Criteria) this;
		}

		public Criteria andTitleLessThanOrEqualTo(String value) {
			addCriterion("title <=", value, "title");
			return (Criteria) this;
		}

		public Criteria andTitleLike(String value) {
			addCriterion("title like", value, "title");
			return (Criteria) this;
		}

		public Criteria andTitleNotLike(String value) {
			addCriterion("title not like", value, "title");
			return (Criteria) this;
		}

		public Criteria andTitleIn(List<String> values) {
			addCriterion("title in", values, "title");
			return (Criteria) this;
		}

		public Criteria andTitleNotIn(List<String> values) {
			addCriterion("title not in", values, "title");
			return (Criteria) this;
		}

		public Criteria andTitleBetween(String value1, String value2) {
			addCriterion("title between", value1, value2, "title");
			return (Criteria) this;
		}

		public Criteria andTitleNotBetween(String value1, String value2) {
			addCriterion("title not between", value1, value2, "title");
			return (Criteria) this;
		}

		public Criteria andCertificationsUrlIsNull() {
			addCriterion("certifications_url is null");
			return (Criteria) this;
		}

		public Criteria andCertificationsUrlIsNotNull() {
			addCriterion("certifications_url is not null");
			return (Criteria) this;
		}

		public Criteria andCertificationsUrlEqualTo(String value) {
			addCriterion("certifications_url =", value, "certificationsUrl");
			return (Criteria) this;
		}

		public Criteria andCertificationsUrlNotEqualTo(String value) {
			addCriterion("certifications_url <>", value, "certificationsUrl");
			return (Criteria) this;
		}

		public Criteria andCertificationsUrlGreaterThan(String value) {
			addCriterion("certifications_url >", value, "certificationsUrl");
			return (Criteria) this;
		}

		public Criteria andCertificationsUrlGreaterThanOrEqualTo(String value) {
			addCriterion("certifications_url >=", value, "certificationsUrl");
			return (Criteria) this;
		}

		public Criteria andCertificationsUrlLessThan(String value) {
			addCriterion("certifications_url <", value, "certificationsUrl");
			return (Criteria) this;
		}

		public Criteria andCertificationsUrlLessThanOrEqualTo(String value) {
			addCriterion("certifications_url <=", value, "certificationsUrl");
			return (Criteria) this;
		}

		public Criteria andCertificationsUrlLike(String value) {
			addCriterion("certifications_url like", value, "certificationsUrl");
			return (Criteria) this;
		}

		public Criteria andCertificationsUrlNotLike(String value) {
			addCriterion("certifications_url not like", value, "certificationsUrl");
			return (Criteria) this;
		}

		public Criteria andCertificationsUrlIn(List<String> values) {
			addCriterion("certifications_url in", values, "certificationsUrl");
			return (Criteria) this;
		}

		public Criteria andCertificationsUrlNotIn(List<String> values) {
			addCriterion("certifications_url not in", values, "certificationsUrl");
			return (Criteria) this;
		}

		public Criteria andCertificationsUrlBetween(String value1, String value2) {
			addCriterion("certifications_url between", value1, value2, "certificationsUrl");
			return (Criteria) this;
		}

		public Criteria andCertificationsUrlNotBetween(String value1, String value2) {
			addCriterion("certifications_url not between", value1, value2, "certificationsUrl");
			return (Criteria) this;
		}

		public Criteria andRoleIdIsNull() {
			addCriterion("role_id is null");
			return (Criteria) this;
		}

		public Criteria andRoleIdIsNotNull() {
			addCriterion("role_id is not null");
			return (Criteria) this;
		}

		public Criteria andRoleIdEqualTo(Integer value) {
			addCriterion("role_id =", value, "roleId");
			return (Criteria) this;
		}

		public Criteria andRoleIdNotEqualTo(Integer value) {
			addCriterion("role_id <>", value, "roleId");
			return (Criteria) this;
		}

		public Criteria andRoleIdGreaterThan(Integer value) {
			addCriterion("role_id >", value, "roleId");
			return (Criteria) this;
		}

		public Criteria andRoleIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("role_id >=", value, "roleId");
			return (Criteria) this;
		}

		public Criteria andRoleIdLessThan(Integer value) {
			addCriterion("role_id <", value, "roleId");
			return (Criteria) this;
		}

		public Criteria andRoleIdLessThanOrEqualTo(Integer value) {
			addCriterion("role_id <=", value, "roleId");
			return (Criteria) this;
		}

		public Criteria andRoleIdIn(List<Integer> values) {
			addCriterion("role_id in", values, "roleId");
			return (Criteria) this;
		}

		public Criteria andRoleIdNotIn(List<Integer> values) {
			addCriterion("role_id not in", values, "roleId");
			return (Criteria) this;
		}

		public Criteria andRoleIdBetween(Integer value1, Integer value2) {
			addCriterion("role_id between", value1, value2, "roleId");
			return (Criteria) this;
		}

		public Criteria andRoleIdNotBetween(Integer value1, Integer value2) {
			addCriterion("role_id not between", value1, value2, "roleId");
			return (Criteria) this;
		}

		public Criteria andUserStatusIsNull() {
			addCriterion("user_status is null");
			return (Criteria) this;
		}

		public Criteria andUserStatusIsNotNull() {
			addCriterion("user_status is not null");
			return (Criteria) this;
		}

		public Criteria andUserStatusEqualTo(Integer value) {
			addCriterion("user_status =", value, "userStatus");
			return (Criteria) this;
		}

		public Criteria andUserStatusNotEqualTo(Integer value) {
			addCriterion("user_status <>", value, "userStatus");
			return (Criteria) this;
		}

		public Criteria andUserStatusGreaterThan(Integer value) {
			addCriterion("user_status >", value, "userStatus");
			return (Criteria) this;
		}

		public Criteria andUserStatusGreaterThanOrEqualTo(Integer value) {
			addCriterion("user_status >=", value, "userStatus");
			return (Criteria) this;
		}

		public Criteria andUserStatusLessThan(Integer value) {
			addCriterion("user_status <", value, "userStatus");
			return (Criteria) this;
		}

		public Criteria andUserStatusLessThanOrEqualTo(Integer value) {
			addCriterion("user_status <=", value, "userStatus");
			return (Criteria) this;
		}

		public Criteria andUserStatusIn(List<Integer> values) {
			addCriterion("user_status in", values, "userStatus");
			return (Criteria) this;
		}

		public Criteria andUserStatusNotIn(List<Integer> values) {
			addCriterion("user_status not in", values, "userStatus");
			return (Criteria) this;
		}

		public Criteria andUserStatusBetween(Integer value1, Integer value2) {
			addCriterion("user_status between", value1, value2, "userStatus");
			return (Criteria) this;
		}

		public Criteria andUserStatusNotBetween(Integer value1, Integer value2) {
			addCriterion("user_status not between", value1, value2, "userStatus");
			return (Criteria) this;
		}

		public Criteria andEmailIsNull() {
			addCriterion("email is null");
			return (Criteria) this;
		}

		public Criteria andEmailIsNotNull() {
			addCriterion("email is not null");
			return (Criteria) this;
		}

		public Criteria andEmailEqualTo(String value) {
			addCriterion("email =", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailNotEqualTo(String value) {
			addCriterion("email <>", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailGreaterThan(String value) {
			addCriterion("email >", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailGreaterThanOrEqualTo(String value) {
			addCriterion("email >=", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailLessThan(String value) {
			addCriterion("email <", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailLessThanOrEqualTo(String value) {
			addCriterion("email <=", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailLike(String value) {
			addCriterion("email like", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailNotLike(String value) {
			addCriterion("email not like", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailIn(List<String> values) {
			addCriterion("email in", values, "email");
			return (Criteria) this;
		}

		public Criteria andEmailNotIn(List<String> values) {
			addCriterion("email not in", values, "email");
			return (Criteria) this;
		}

		public Criteria andEmailBetween(String value1, String value2) {
			addCriterion("email between", value1, value2, "email");
			return (Criteria) this;
		}

		public Criteria andEmailNotBetween(String value1, String value2) {
			addCriterion("email not between", value1, value2, "email");
			return (Criteria) this;
		}

		public Criteria andMobileIsNull() {
			addCriterion("mobile is null");
			return (Criteria) this;
		}

		public Criteria andMobileIsNotNull() {
			addCriterion("mobile is not null");
			return (Criteria) this;
		}

		public Criteria andMobileEqualTo(String value) {
			addCriterion("mobile =", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileNotEqualTo(String value) {
			addCriterion("mobile <>", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileGreaterThan(String value) {
			addCriterion("mobile >", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileGreaterThanOrEqualTo(String value) {
			addCriterion("mobile >=", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileLessThan(String value) {
			addCriterion("mobile <", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileLessThanOrEqualTo(String value) {
			addCriterion("mobile <=", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileLike(String value) {
			addCriterion("mobile like", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileNotLike(String value) {
			addCriterion("mobile not like", value, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileIn(List<String> values) {
			addCriterion("mobile in", values, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileNotIn(List<String> values) {
			addCriterion("mobile not in", values, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileBetween(String value1, String value2) {
			addCriterion("mobile between", value1, value2, "mobile");
			return (Criteria) this;
		}

		public Criteria andMobileNotBetween(String value1, String value2) {
			addCriterion("mobile not between", value1, value2, "mobile");
			return (Criteria) this;
		}

		public Criteria andCreateTimeIsNull() {
			addCriterion("create_time is null");
			return (Criteria) this;
		}

		public Criteria andCreateTimeIsNotNull() {
			addCriterion("create_time is not null");
			return (Criteria) this;
		}

		public Criteria andCreateTimeEqualTo(Date value) {
			addCriterion("create_time =", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeNotEqualTo(Date value) {
			addCriterion("create_time <>", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeGreaterThan(Date value) {
			addCriterion("create_time >", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
			addCriterion("create_time >=", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeLessThan(Date value) {
			addCriterion("create_time <", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
			addCriterion("create_time <=", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeIn(List<Date> values) {
			addCriterion("create_time in", values, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeNotIn(List<Date> values) {
			addCriterion("create_time not in", values, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeBetween(Date value1, Date value2) {
			addCriterion("create_time between", value1, value2, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
			addCriterion("create_time not between", value1, value2, "createTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeIsNull() {
			addCriterion("update_time is null");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeIsNotNull() {
			addCriterion("update_time is not null");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeEqualTo(Date value) {
			addCriterion("update_time =", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeNotEqualTo(Date value) {
			addCriterion("update_time <>", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeGreaterThan(Date value) {
			addCriterion("update_time >", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
			addCriterion("update_time >=", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeLessThan(Date value) {
			addCriterion("update_time <", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
			addCriterion("update_time <=", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeIn(List<Date> values) {
			addCriterion("update_time in", values, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeNotIn(List<Date> values) {
			addCriterion("update_time not in", values, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeBetween(Date value1, Date value2) {
			addCriterion("update_time between", value1, value2, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
			addCriterion("update_time not between", value1, value2, "updateTime");
			return (Criteria) this;
		}

		public Criteria andInUseIsNull() {
			addCriterion("in_use is null");
			return (Criteria) this;
		}

		public Criteria andInUseIsNotNull() {
			addCriterion("in_use is not null");
			return (Criteria) this;
		}

		public Criteria andInUseEqualTo(Integer value) {
			addCriterion("in_use =", value, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseNotEqualTo(Integer value) {
			addCriterion("in_use <>", value, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseGreaterThan(Integer value) {
			addCriterion("in_use >", value, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseGreaterThanOrEqualTo(Integer value) {
			addCriterion("in_use >=", value, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseLessThan(Integer value) {
			addCriterion("in_use <", value, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseLessThanOrEqualTo(Integer value) {
			addCriterion("in_use <=", value, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseIn(List<Integer> values) {
			addCriterion("in_use in", values, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseNotIn(List<Integer> values) {
			addCriterion("in_use not in", values, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseBetween(Integer value1, Integer value2) {
			addCriterion("in_use between", value1, value2, "inUse");
			return (Criteria) this;
		}

		public Criteria andInUseNotBetween(Integer value1, Integer value2) {
			addCriterion("in_use not between", value1, value2, "inUse");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table backstage_user_info
	 * @mbg.generated
	 */
	public static class Criterion {
		private String condition;
		private Object value;
		private Object secondValue;
		private boolean noValue;
		private boolean singleValue;
		private boolean betweenValue;
		private boolean listValue;
		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}

	/**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table backstage_user_info
     *
     * @mbg.generated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }
}