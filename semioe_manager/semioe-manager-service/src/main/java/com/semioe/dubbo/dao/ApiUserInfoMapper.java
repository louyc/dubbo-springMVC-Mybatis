package com.semioe.dubbo.dao;

import com.semioe.api.entity.ApiUserInfo;
import com.semioe.api.entity.ApiUserInfoExample;
import com.semioe.api.vo.ApiContractedUserVO;
import com.semioe.api.vo.ApiUserInfoVO;
import com.semioe.api.vo.UserCountVO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface ApiUserInfoMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table api_user_info
	 *
	 * @mbg.generated
	 */
	long countByExample(ApiUserInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table api_user_info
	 *
	 * @mbg.generated
	 */
	int deleteByExample(ApiUserInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table api_user_info
	 *
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(String managerId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table api_user_info
	 *
	 * @mbg.generated
	 */
	int insert(ApiUserInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table api_user_info
	 *
	 * @mbg.generated
	 */
	int insertSelective(ApiUserInfo record);

	int insertInfoVoSelective(ApiUserInfoVO record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table api_user_info
	 *
	 * @mbg.generated
	 */
	List<ApiUserInfo> selectByExample(ApiUserInfoExample example);

	List<UserCountVO> selectAllBySex(ApiUserInfoVO userInfo);

	List<UserCountVO> selectAllByAge(ApiUserInfoVO userInfo);

	List<UserCountVO> selectAllByArea(ApiUserInfoVO userInfo);

	List<Map<String, Object>> selectSubUserInfo(String managerId);

	List<Map<String, Object>> selectParentUserInfo(String managerId);

	List<UserCountVO> countRegsitUserListPage(ApiUserInfoVO userInfo);

	List<UserCountVO> countRegsitUser(ApiUserInfoVO userInfo);

	List<UserCountVO> regsitDetail(ApiUserInfoVO userInfo);

	List<ApiUserInfo> regsitDetailListPage(ApiUserInfoVO userInfo);

	List<UserCountVO> countConversion(ApiUserInfoVO userInfo);

	List<UserCountVO> countConversionListPage(ApiUserInfoVO userInfo);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table api_user_info
	 *
	 * @mbg.generated
	 */
	ApiUserInfo selectByPrimaryKey(String managerId);

	ApiUserInfo selectByMoible(String moible);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table api_user_info
	 *
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") ApiUserInfo record,
			@Param("example") ApiUserInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table api_user_info
	 *
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") ApiUserInfo record,
			@Param("example") ApiUserInfoExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table api_user_info
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(ApiUserInfo record);

	int updateByApiUserinfoVO(ApiUserInfoVO record);

	int updateSubUser(ApiUserInfo record);

	int updateUserByMobile(ApiUserInfo record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table api_user_info
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKey(ApiUserInfo record);

	int updateInUse(Map updateMap);

	String getManagerIdByMobile(String mobile);

	ApiUserInfo getApiUserByOpenId(String openId);

	ApiUserInfo selectByEntity(ApiUserInfo record);
	
	/*签约统计*/
	List<UserCountVO> countSignListPage(ApiUserInfoVO userInfo);
	List<ApiContractedUserVO> signDetaileListPage(ApiUserInfoVO userInfo);
	List<UserCountVO> selectAllByFamilyListPage(ApiUserInfoVO userInfo);
}