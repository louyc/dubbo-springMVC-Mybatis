package com.semioe.api.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class UserDoctorRelExample implements Serializable {
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	protected String orderByClause;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	protected boolean distinct;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	public UserDoctorRelExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
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
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_doctor_rel
	 *
	 * @mbg.generated
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to
	 * the database table user_doctor_rel
	 *
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

		protected void addCriterion(String condition, Object value1, Object value2,
				String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		protected void addCriterionForJDBCDate(String condition, Date value, String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property + " cannot be null");
			}
			addCriterion(condition, new java.sql.Date(value.getTime()), property);
		}

		protected void addCriterionForJDBCDate(String condition, List<Date> values,
				String property) {
			if (values == null || values.size() == 0) {
				throw new RuntimeException(
						"Value list for " + property + " cannot be null or empty");
			}
			List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
			Iterator<Date> iter = values.iterator();
			while (iter.hasNext()) {
				dateList.add(new java.sql.Date(iter.next().getTime()));
			}
			addCriterion(condition, dateList, property);
		}

		protected void addCriterionForJDBCDate(String condition, Date value1, Date value2,
				String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			addCriterion(condition, new java.sql.Date(value1.getTime()),
					new java.sql.Date(value2.getTime()), property);
		}

		public Criteria andIdIsNull() {
			addCriterion("id is null");
			return (Criteria) this;
		}

		public Criteria andIdIsNotNull() {
			addCriterion("id is not null");
			return (Criteria) this;
		}

		public Criteria andIdEqualTo(Integer value) {
			addCriterion("id =", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotEqualTo(Integer value) {
			addCriterion("id <>", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThan(Integer value) {
			addCriterion("id >", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("id >=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThan(Integer value) {
			addCriterion("id <", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThanOrEqualTo(Integer value) {
			addCriterion("id <=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdIn(List<Integer> values) {
			addCriterion("id in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotIn(List<Integer> values) {
			addCriterion("id not in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdBetween(Integer value1, Integer value2) {
			addCriterion("id between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotBetween(Integer value1, Integer value2) {
			addCriterion("id not between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andContractedUserIdIsNull() {
			addCriterion("contracted_user_id is null");
			return (Criteria) this;
		}

		public Criteria andContractedUserIdIsNotNull() {
			addCriterion("contracted_user_id is not null");
			return (Criteria) this;
		}

		public Criteria andContractedUserIdEqualTo(Integer value) {
			addCriterion("contracted_user_id =", value, "contractedUserId");
			return (Criteria) this;
		}

		public Criteria andContractedUserIdNotEqualTo(Integer value) {
			addCriterion("contracted_user_id <>", value, "contractedUserId");
			return (Criteria) this;
		}

		public Criteria andContractedUserIdGreaterThan(Integer value) {
			addCriterion("contracted_user_id >", value, "contractedUserId");
			return (Criteria) this;
		}

		public Criteria andContractedUserIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("contracted_user_id >=", value, "contractedUserId");
			return (Criteria) this;
		}

		public Criteria andContractedUserIdLessThan(Integer value) {
			addCriterion("contracted_user_id <", value, "contractedUserId");
			return (Criteria) this;
		}

		public Criteria andContractedUserIdLessThanOrEqualTo(Integer value) {
			addCriterion("contracted_user_id <=", value, "contractedUserId");
			return (Criteria) this;
		}

		public Criteria andContractedUserIdIn(List<Integer> values) {
			addCriterion("contracted_user_id in", values, "contractedUserId");
			return (Criteria) this;
		}

		public Criteria andContractedUserIdNotIn(List<Integer> values) {
			addCriterion("contracted_user_id not in", values, "contractedUserId");
			return (Criteria) this;
		}

		public Criteria andContractedUserIdBetween(Integer value1, Integer value2) {
			addCriterion("contracted_user_id between", value1, value2, "contractedUserId");
			return (Criteria) this;
		}

		public Criteria andContractedUserIdNotBetween(Integer value1, Integer value2) {
			addCriterion("contracted_user_id not between", value1, value2, "contractedUserId");
			return (Criteria) this;
		}

		public Criteria andDoctorIdIsNull() {
			addCriterion("doctor_id is null");
			return (Criteria) this;
		}

		public Criteria andDoctorIdIsNotNull() {
			addCriterion("doctor_id is not null");
			return (Criteria) this;
		}

		public Criteria andDoctorIdEqualTo(Integer value) {
			addCriterion("doctor_id =", value, "doctorId");
			return (Criteria) this;
		}

		public Criteria andDoctorIdNotEqualTo(Integer value) {
			addCriterion("doctor_id <>", value, "doctorId");
			return (Criteria) this;
		}

		public Criteria andDoctorIdGreaterThan(Integer value) {
			addCriterion("doctor_id >", value, "doctorId");
			return (Criteria) this;
		}

		public Criteria andDoctorIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("doctor_id >=", value, "doctorId");
			return (Criteria) this;
		}

		public Criteria andDoctorIdLessThan(Integer value) {
			addCriterion("doctor_id <", value, "doctorId");
			return (Criteria) this;
		}

		public Criteria andDoctorIdLessThanOrEqualTo(Integer value) {
			addCriterion("doctor_id <=", value, "doctorId");
			return (Criteria) this;
		}

		public Criteria andDoctorIdIn(List<Integer> values) {
			addCriterion("doctor_id in", values, "doctorId");
			return (Criteria) this;
		}

		public Criteria andDoctorIdNotIn(List<Integer> values) {
			addCriterion("doctor_id not in", values, "doctorId");
			return (Criteria) this;
		}

		public Criteria andDoctorIdBetween(Integer value1, Integer value2) {
			addCriterion("doctor_id between", value1, value2, "doctorId");
			return (Criteria) this;
		}

		public Criteria andDoctorIdNotBetween(Integer value1, Integer value2) {
			addCriterion("doctor_id not between", value1, value2, "doctorId");
			return (Criteria) this;
		}

		public Criteria andExpirationDateIsNull() {
			addCriterion("expiration_date is null");
			return (Criteria) this;
		}

		public Criteria andExpirationDateIsNotNull() {
			addCriterion("expiration_date is not null");
			return (Criteria) this;
		}

		public Criteria andExpirationDateEqualTo(Date value) {
			addCriterionForJDBCDate("expiration_date =", value, "expirationDate");
			return (Criteria) this;
		}

		public Criteria andExpirationDateNotEqualTo(Date value) {
			addCriterionForJDBCDate("expiration_date <>", value, "expirationDate");
			return (Criteria) this;
		}

		public Criteria andExpirationDateGreaterThan(Date value) {
			addCriterionForJDBCDate("expiration_date >", value, "expirationDate");
			return (Criteria) this;
		}

		public Criteria andExpirationDateGreaterThanOrEqualTo(Date value) {
			addCriterionForJDBCDate("expiration_date >=", value, "expirationDate");
			return (Criteria) this;
		}

		public Criteria andExpirationDateLessThan(Date value) {
			addCriterionForJDBCDate("expiration_date <", value, "expirationDate");
			return (Criteria) this;
		}

		public Criteria andExpirationDateLessThanOrEqualTo(Date value) {
			addCriterionForJDBCDate("expiration_date <=", value, "expirationDate");
			return (Criteria) this;
		}

		public Criteria andExpirationDateIn(List<Date> values) {
			addCriterionForJDBCDate("expiration_date in", values, "expirationDate");
			return (Criteria) this;
		}

		public Criteria andExpirationDateNotIn(List<Date> values) {
			addCriterionForJDBCDate("expiration_date not in", values, "expirationDate");
			return (Criteria) this;
		}

		public Criteria andExpirationDateBetween(Date value1, Date value2) {
			addCriterionForJDBCDate("expiration_date between", value1, value2, "expirationDate");
			return (Criteria) this;
		}

		public Criteria andExpirationDateNotBetween(Date value1, Date value2) {
			addCriterionForJDBCDate("expiration_date not between", value1, value2,
					"expirationDate");
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
			addCriterionForJDBCDate("create_time =", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeNotEqualTo(Date value) {
			addCriterionForJDBCDate("create_time <>", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeGreaterThan(Date value) {
			addCriterionForJDBCDate("create_time >", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
			addCriterionForJDBCDate("create_time >=", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeLessThan(Date value) {
			addCriterionForJDBCDate("create_time <", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
			addCriterionForJDBCDate("create_time <=", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeIn(List<Date> values) {
			addCriterionForJDBCDate("create_time in", values, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeNotIn(List<Date> values) {
			addCriterionForJDBCDate("create_time not in", values, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeBetween(Date value1, Date value2) {
			addCriterionForJDBCDate("create_time between", value1, value2, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
			addCriterionForJDBCDate("create_time not between", value1, value2, "createTime");
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
			addCriterionForJDBCDate("update_time =", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeNotEqualTo(Date value) {
			addCriterionForJDBCDate("update_time <>", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeGreaterThan(Date value) {
			addCriterionForJDBCDate("update_time >", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
			addCriterionForJDBCDate("update_time >=", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeLessThan(Date value) {
			addCriterionForJDBCDate("update_time <", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
			addCriterionForJDBCDate("update_time <=", value, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeIn(List<Date> values) {
			addCriterionForJDBCDate("update_time in", values, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeNotIn(List<Date> values) {
			addCriterionForJDBCDate("update_time not in", values, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeBetween(Date value1, Date value2) {
			addCriterionForJDBCDate("update_time between", value1, value2, "updateTime");
			return (Criteria) this;
		}

		public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
			addCriterionForJDBCDate("update_time not between", value1, value2, "updateTime");
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
	 * This class was generated by MyBatis Generator. This class corresponds to
	 * the database table user_doctor_rel
	 *
	 * @mbg.generated do_not_delete_during_merge
	 */
	public static class Criteria extends GeneratedCriteria {

		protected Criteria() {
			super();
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to
	 * the database table user_doctor_rel
	 *
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

		protected Criterion(String condition, Object value, Object secondValue,
				String typeHandler) {
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
}