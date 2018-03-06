package com.semioe.dubbo.dao;

import com.semioe.api.entity.UserShareService;
import com.semioe.api.entity.UserShareServiceExample;
import com.semioe.api.vo.UserShareServiceVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserShareServiceMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_share_service
	 *
	 * @mbg.generated
	 */
	long countByExample(UserShareServiceExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_share_service
	 *
	 * @mbg.generated
	 */
	int deleteByExample(UserShareServiceExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_share_service
	 *
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_share_service
	 *
	 * @mbg.generated
	 */
	int insert(UserShareService record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_share_service
	 *
	 * @mbg.generated
	 */
	int insertSelective(UserShareService record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_share_service
	 *
	 * @mbg.generated
	 */
	List<UserShareService> selectByExample(UserShareServiceExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_share_service
	 *
	 * @mbg.generated
	 */
	UserShareService selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_share_service
	 *
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") UserShareService record,
			@Param("example") UserShareServiceExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_share_service
	 *
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") UserShareService record,
			@Param("example") UserShareServiceExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_share_service
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(UserShareService record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table user_share_service
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKey(UserShareService record);

	List<UserShareService> selectByEntity(UserShareServiceVO record);

	List<UserShareService> selectByEntityListPage(UserShareServiceVO record);
	
	int updateInuseByPrimaryKey(UserShareServiceVO shareService);
}