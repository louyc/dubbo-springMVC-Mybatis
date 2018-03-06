package com.semioe.dubbo.dao;

import com.semioe.api.entity.ApiMotherhoodUser;
import com.semioe.api.entity.ApiMotherhoodUserExample;
import com.semioe.api.vo.ApiMotherhoodUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiMotherhoodUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_motherhood_user
     *
     * @mbg.generated
     */
    long countByExample(ApiMotherhoodUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_motherhood_user
     *
     * @mbg.generated
     */
    int deleteByExample(ApiMotherhoodUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_motherhood_user
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_motherhood_user
     *
     * @mbg.generated
     */
    int insert(ApiMotherhoodUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_motherhood_user
     *
     * @mbg.generated
     */
    int insertSelective(ApiMotherhoodUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_motherhood_user
     *
     * @mbg.generated
     */
    List<ApiMotherhoodUserVO> selectByExample(ApiMotherhoodUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_motherhood_user
     *
     * @mbg.generated
     */
    ApiMotherhoodUser selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_motherhood_user
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") ApiMotherhoodUser record, @Param("example") ApiMotherhoodUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_motherhood_user
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") ApiMotherhoodUser record, @Param("example") ApiMotherhoodUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_motherhood_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ApiMotherhoodUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_motherhood_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ApiMotherhoodUser record);
}