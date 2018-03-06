package com.semioe.dubbo.dao;

import com.semioe.api.entity.QrcodeRel;
import com.semioe.api.entity.QrcodeRelExample;
import com.semioe.api.vo.QrcodeInfoVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface QrcodeRelMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_rel
	 * @mbg.generated
	 */
	long countByExample(QrcodeRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_rel
	 * @mbg.generated
	 */
	int deleteByExample(QrcodeRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_rel
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_rel
	 * @mbg.generated
	 */
	int insert(QrcodeRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_rel
	 * @mbg.generated
	 */
	int insertSelective(QrcodeRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_rel
	 * @mbg.generated
	 */
	List<QrcodeRel> selectByExample(QrcodeRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_rel
	 * @mbg.generated
	 */
	QrcodeRel selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_rel
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") QrcodeRel record, @Param("example") QrcodeRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_rel
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") QrcodeRel record, @Param("example") QrcodeRelExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_rel
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(QrcodeRel record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table qrcode_rel
	 * @mbg.generated
	 */
	int updateByPrimaryKey(QrcodeRel record);

	/* 查询openID 数量 */
	Integer selectOpenIdCount(String openId);

	/* 根据openid 查询关系数据 */
	QrcodeRel selectQrcodeRelByOpenId(String openId);

	/* 根据用户ID 查询关系数据 */
	QrcodeRel selectQrcodeRelByUserId(String userId);

	List<QrcodeInfoVO> selectQrcodeRelListPage(QrcodeInfoVO qrcodeInfo);

	List<QrcodeInfoVO> selectQrcodeRelByContion(QrcodeInfoVO qrcodeInfo);
}