package com.semioe.dubbo.dao;

import com.semioe.api.entity.JyDoctorInfo;
import com.semioe.api.entity.JyOrgDocRel;
import com.semioe.api.entity.JyOrgDocRelExample;
import com.semioe.api.entity.JyOrgInfo;
import com.semioe.api.vo.JyDoctorInfoVO;
import com.semioe.api.vo.JyOrgInfoVO;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JyOrgDocRelMapper {
    /**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table jy_org_doc_rel
	 * @mbg.generated
	 */
	long countByExample(JyOrgDocRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table jy_org_doc_rel
	 * @mbg.generated
	 */
	int deleteByExample(JyOrgDocRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table jy_org_doc_rel
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table jy_org_doc_rel
	 * @mbg.generated
	 */
	int insert(JyOrgDocRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table jy_org_doc_rel
	 * @mbg.generated
	 */
	int insertSelective(JyOrgDocRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table jy_org_doc_rel
	 * @mbg.generated
	 */
	List<JyOrgDocRel> selectByExample(JyOrgDocRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table jy_org_doc_rel
	 * @mbg.generated
	 */
	JyOrgDocRel selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table jy_org_doc_rel
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") JyOrgDocRel record, @Param("example") JyOrgDocRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table jy_org_doc_rel
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") JyOrgDocRel record, @Param("example") JyOrgDocRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table jy_org_doc_rel
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(JyOrgDocRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table jy_org_doc_rel
	 * @mbg.generated
	 */
	int updateByPrimaryKey(JyOrgDocRel record);
	
	/**
     * 更新用户表冗余字段
     * @param record
     * @return
     */
    int updateUserInfoType(JyOrgDocRel record);
    
    /**
     * 查询邀请记录
     * @param entity
     * @return
     */
    List<JyOrgDocRel> selectJyOrgDocRel(JyOrgDocRel entity);
    
    /**
     * 按条件，查询医生信息及签约状态
     * @param entity
     * @return
     */
    List<JyDoctorInfoVO> findJyDoctorInfo(JyDoctorInfo entity);
    
    /**
     * 查询医生列表
     * @param entity
     * @return
     */
    long countJySignDoctor(JyDoctorInfo entity);
    
    List<JyDoctorInfoVO> selectJySignDoctor(JyDoctorInfo entity);
    
    /**
     * 按条件查询机构列表
     * @param entity
     * @return
     */
    long countJyOrgInfo(JyOrgInfo entity);
    
    List<JyOrgInfoVO> selectJyOrgInfo(JyOrgInfo entity);
    
    /**
     * 查询所有家庭医生机构
     * @param entity
     * @return
     */
    long countOrgInfoList(JyOrgInfo entity);
    
    List<JyOrgInfoVO> selectOrgInfoList(JyOrgInfo entity);
    
}