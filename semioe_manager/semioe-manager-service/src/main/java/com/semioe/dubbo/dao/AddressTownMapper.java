package com.semioe.dubbo.dao;

import com.semioe.api.entity.AddressTown;
import com.semioe.api.entity.AddressTownExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AddressTownMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_town
     *
     * @mbg.generated
     */
    long countByExample(AddressTownExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_town
     *
     * @mbg.generated
     */
    int deleteByExample(AddressTownExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_town
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_town
     *
     * @mbg.generated
     */
    int insert(AddressTown record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_town
     *
     * @mbg.generated
     */
    int insertSelective(AddressTown record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_town
     *
     * @mbg.generated
     */
    List<AddressTown> selectByExample(AddressTownExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_town
     *
     * @mbg.generated
     */
    AddressTown selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_town
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") AddressTown record, @Param("example") AddressTownExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_town
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") AddressTown record, @Param("example") AddressTownExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_town
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(AddressTown record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_town
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(AddressTown record);
}