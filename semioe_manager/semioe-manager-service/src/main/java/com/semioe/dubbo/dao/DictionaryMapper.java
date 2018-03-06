package com.semioe.dubbo.dao;

import com.semioe.api.entity.Dictionary;
import com.semioe.api.entity.DictionaryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DictionaryMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table dictionary
	 *
	 * @mbg.generated
	 */
	long countByExample(DictionaryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table dictionary
	 *
	 * @mbg.generated
	 */
	int deleteByExample(DictionaryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table dictionary
	 *
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table dictionary
	 *
	 * @mbg.generated
	 */
	int insert(Dictionary record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table dictionary
	 *
	 * @mbg.generated
	 */
	int insertSelective(Dictionary record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table dictionary
	 *
	 * @mbg.generated
	 */
	List<Dictionary> selectByExample(DictionaryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table dictionary
	 *
	 * @mbg.generated
	 */
	Dictionary selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table dictionary
	 *
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") Dictionary record,
			@Param("example") DictionaryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table dictionary
	 *
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") Dictionary record,
			@Param("example") DictionaryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table dictionary
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(Dictionary record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table dictionary
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKey(Dictionary record);

	List<Dictionary> selectByTypeId(Dictionary dic);
}