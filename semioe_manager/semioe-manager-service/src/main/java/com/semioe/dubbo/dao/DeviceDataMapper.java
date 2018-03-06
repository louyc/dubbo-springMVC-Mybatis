package com.semioe.dubbo.dao;

import com.semioe.api.entity.DeviceData;
import com.semioe.api.entity.DeviceDataExample;
import com.semioe.api.vo.DeviceDataVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DeviceDataMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_data
	 *
	 * @mbg.generated
	 */
	long countByExample(DeviceDataExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_data
	 * 
	 * @mbg.generated
	 */
	int deleteByExample(DeviceDataExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_data
	 * 
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_data
	 * 
	 * @mbg.generated
	 */
	int insert(DeviceData record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_data
	 * 
	 * @mbg.generated
	 */
	int insertSelective(DeviceData record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_data
	 * 
	 * @mbg.generated
	 */
	List<DeviceData> selectByExampleWithBLOBs(DeviceDataExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_data
	 * 
	 * @mbg.generated
	 */
	List<DeviceData> selectByExample(DeviceDataExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_data
	 * 
	 * @mbg.generated
	 */
	DeviceData selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_data
	 * 
	 * @mbg.generated
	 */
	int updateByExampleSelective(@Param("record") DeviceData record,
			@Param("example") DeviceDataExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_data
	 * 
	 * @mbg.generated
	 */
	int updateByExampleWithBLOBs(@Param("record") DeviceData record,
			@Param("example") DeviceDataExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_data
	 * 
	 * @mbg.generated
	 */
	int updateByExample(@Param("record") DeviceData record,
			@Param("example") DeviceDataExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_data
	 * 
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(DeviceData record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_data
	 * 
	 * @mbg.generated
	 */
	int updateByPrimaryKeyWithBLOBs(DeviceData record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table device_data
	 * 
	 * @mbg.generated
	 */
	int updateByPrimaryKey(DeviceData record);

	DeviceData getDataByUserTime(String managerId, String time, String dataType);

	List<DeviceData> getDataByUserTimeList(String managerId, String time);

	List<DeviceData> getHistoryMeasureDataByDay(String managerId, String time);

	List<DeviceData> getHistoryMeasureData(String managerId, String time);

	List<DeviceData> getDataByType(String managerId, String from);

	List<DeviceDataVO> selectByCondition(DeviceDataVO deviceData);

	List<DeviceDataVO> selectByConditionListPage(DeviceDataVO deviceData);

	List<DeviceDataVO> selectMeasureInfo(DeviceDataVO deviceData);

	List<DeviceDataVO> selectMeasureInfoByConditionListPage(DeviceDataVO deviceData);

	List<DeviceDataVO> selectJYMeasureInfoByConditionListPage(DeviceDataVO deviceData);

	int updateHrData(String managerId, String time, String jos);

	DeviceData getDataByUserTimeByMin(String managerId, String time, String dataType);
}