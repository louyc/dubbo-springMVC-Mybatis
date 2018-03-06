package com.semioe.dubbo.dao;

import com.semioe.api.entity.Keywords;
import com.semioe.api.entity.KeywordsExample;
import com.semioe.api.vo.RelationVO;
import com.semioe.api.vo.ServiceInfoVO;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KeywordsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table keywords
     *
     * @mbg.generated
     */
    long countByExample(KeywordsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table keywords
     *
     * @mbg.generated
     */
    int deleteByExample(KeywordsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table keywords
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table keywords
     *
     * @mbg.generated
     */
    int insert(Keywords record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table keywords
     *
     * @mbg.generated
     */
    int insertSelective(Keywords record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table keywords
     *
     * @mbg.generated
     */
    List<Keywords> selectByExample(KeywordsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table keywords
     *
     * @mbg.generated
     */
    Keywords selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table keywords
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") Keywords record, @Param("example") KeywordsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table keywords
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") Keywords record, @Param("example") KeywordsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table keywords
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Keywords record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table keywords
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Keywords record);
    
    List<Keywords> selectKeywordsListPage(Keywords keywords);
    
    int getObjectCountByName(String name);
    
    Keywords getKeywordsBy(Keywords keywords);
    
    /**
     * 根据关键字，联合查询 商品&服务
     * @param serviceVO
     * @return
     */
    List<RelationVO> getRelationByKeywords(ServiceInfoVO serviceVO);
    
    /**
     * 根据关键字，统计 商品&服务 数量
     * @param serviceVO
     * @return
     */
    long countRelationByKeywords(ServiceInfoVO serviceVO);
    /**
     * 根据类型查询关键字
     * @param serviceVO
     * @return
     */
    List<Keywords> selectKeywordsByType(Keywords keywords);
}