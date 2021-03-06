package com.semioe.dubbo.dao;

import com.semioe.api.entity.AddressCity;
import com.semioe.api.entity.AddressCityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AddressCityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_city
     *
     * @mbg.generated
     */
    long countByExample(AddressCityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_city
     *
     * @mbg.generated
     */
    int deleteByExample(AddressCityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_city
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_city
     *
     * @mbg.generated
     */
    int insert(AddressCity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_city
     *
     * @mbg.generated
     */
    int insertSelective(AddressCity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_city
     *
     * @mbg.generated
     */
    List<AddressCity> selectByExample(AddressCityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_city
     *
     * @mbg.generated
     */
    AddressCity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_city
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") AddressCity record, @Param("example") AddressCityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_city
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") AddressCity record, @Param("example") AddressCityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_city
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(AddressCity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_address_city
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(AddressCity record);
}