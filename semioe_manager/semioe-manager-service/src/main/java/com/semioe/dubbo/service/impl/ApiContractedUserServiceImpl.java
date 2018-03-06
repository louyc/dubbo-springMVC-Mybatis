package com.semioe.dubbo.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.ApiContractedUser;
import com.semioe.api.entity.ApiUserInfo;
import com.semioe.api.entity.BackstageUserInfo;
import com.semioe.api.entity.Dictionary;
import com.semioe.api.entity.JyDoctorInfo;
import com.semioe.api.entity.Message;
import com.semioe.api.entity.UserDoctorRel;
import com.semioe.api.vo.ApiContractedUserVO;
import com.semioe.api.vo.ApiUserInfoVO;
import com.semioe.api.vo.BackstageUserInfoVO;
import com.semioe.api.vo.JyDoctorInfoVO;
import com.semioe.api.vo.UserCountVO;
import com.semioe.api.vo.UserHealthyLivingVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.ApiContractedUserMapper;
import com.semioe.dubbo.dao.ApiUserInfoMapper;
import com.semioe.dubbo.dao.BackstageUserInfoMapper;
import com.semioe.dubbo.dao.DictionaryMapper;
import com.semioe.dubbo.dao.JyOrgDocRelMapper;
import com.semioe.dubbo.dao.MessageMapper;
import com.semioe.dubbo.dao.UserDoctorRelMapper;
import com.semioe.dubbo.dao.UserHealthyLivingMapper;
import com.semioe.dubbo.service.ApiContractedUserService;

@Service
public class ApiContractedUserServiceImpl implements ApiContractedUserService {

	private static final Logger logger = LoggerFactory
			.getLogger(ApiContractedUserServiceImpl.class);
	public static ResultCode MysqlWrong = new ResultCode("MYSQL_WRONG", "操作数据库出错");
	public static ResultCode UserInfoAddFail = new ResultCode("USERINFO_ADD_FAIL", "添加签约用户失败");
	public static ResultCode UserInfoDelFail = new ResultCode("USERINFO_DEL_FAIL", "修改签约用户信息失败");

	@Autowired
	private ApiContractedUserMapper apiContractedUserMapper;
	@Autowired
	private DictionaryMapper dictionaryMapper;
	@Autowired
	private UserHealthyLivingMapper userHealthyLivingMapper;
	@Autowired
	private UserDoctorRelMapper userDoctorRelMapper;
	@Autowired
	private JyOrgDocRelMapper jyOrgDocRelMapper;
	@Autowired
	private BackstageUserInfoMapper backUserMapper;
	@Autowired
	private ApiUserInfoMapper apiUserMapper;
	@Autowired
	private MessageMapper messageMapper;

	/**
	 * 查询字典表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result queryDictionary(String typeId) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<Dictionary> dictionaryList = new ArrayList<Dictionary>();
		Dictionary dic = new Dictionary();
		if (!StringUtil.isEmpty(typeId)) {
			dic.setItemType(Integer.parseInt(typeId));
		}
		dictionaryList = dictionaryMapper.selectByTypeId(dic);
		result.setData(dictionaryList);
		result.setSuccessful(true);
		return result;
	}

	/**
	 * 根据用户id或者主键id查询健康档案
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result queryUserArchives(String managerId, String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		ApiContractedUser user = new ApiContractedUser();
		if (!StringUtil.isEmpty(managerId)) {
			user.setManagerId(managerId);
		}
		if (!StringUtil.isEmpty(id)) {
			user.setId(Integer.parseInt(id));
		}
		if (StringUtil.isEmpty(id) && StringUtil.isEmpty(managerId)) {
			result.setSuccessful(false);
			result.setResultCode(new ResultCode("FAIL", "入参都为空"));
			return result;
		}
		ApiContractedUserVO userVO = apiContractedUserMapper.selectByObject(user);
		if (null == userVO) {
			result.setResultCode(new ResultCode("SUCCESS", "未查到相关数据"));
			return result;
		}
		String userId = String.valueOf(userVO.getId());
		// 查询健康信息和生活环境详细信息
		List<UserHealthyLivingVO> healthyList = userHealthyLivingMapper
				.selectByUserId(Integer.parseInt(userId));
		if (null != healthyList && healthyList.size() > 0) {
			userVO.setDataList(healthyList);
			Dictionary dic = new Dictionary();
			dic.setItemType(10); // 常住类型
			dic.setItemId(userVO.getResidentType());
			userVO.setResidentTypeDesc(queryDictionary(dic).getItemName());
			dic.setItemType(15); // 性别
			dic.setItemId(userVO.getSex());
			userVO.setSexDesc(queryDictionary(dic).getItemName());
			dic.setItemType(11); // 民族
			dic.setItemId(userVO.getNation());
			userVO.setNationDesc(queryDictionary(dic).getItemName());
			dic.setItemType(12); // 婚姻
			dic.setItemId(userVO.getMaritalStatus());
			userVO.setMaritalStatusDesc(queryDictionary(dic).getItemName());
			dic.setItemType(13); // 文化描述
			dic.setItemId(userVO.getCulturalStatus());
			userVO.setCulturalStatusDesc(queryDictionary(dic).getItemName());
			dic.setItemType(14); // 职业描述
			dic.setItemId(userVO.getOccupation());
			userVO.setOccupationDesc(queryDictionary(dic).getItemName());
			dic.setItemType(29); // 医疗类型
			dic.setItemId(userVO.getMedicalType());
			userVO.setMedicalTypeDesc(queryDictionary(dic).getItemName());
			dic.setItemType(16); // 血型描述
			dic.setItemId(userVO.getBloody());
			userVO.setBloodyDesc(queryDictionary(dic).getItemName());
			dic.setItemType(17); // RH描述
			dic.setItemId(userVO.getRh());
			userVO.setRhDesc(queryDictionary(dic).getItemName());
			result.setData(userVO);

		}
		return result;
	}

	public Dictionary queryDictionary(Dictionary dic) {
		if (null == dictionaryMapper.selectByTypeId(dic)) {
			dic.setItemName("");
			return dic;
		} else {
			return dictionaryMapper.selectByTypeId(dic).get(0);
		}
	}

	public List<UserDoctorRel> queryDocRel(String managerId, String doctorId) {
		UserDoctorRel userRel = new UserDoctorRel();
		userRel.setManagerId(managerId);
		userRel.setDoctorId(doctorId);
		return userDoctorRelMapper.selectByEntity(userRel);
	}

	/**
	 * 0.先判断用户是否已经签约 1.插入签约用户表 2.插入健康信息详情 3.插入用户、医生签约关系表
	 * 
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result addUserArchives(ApiContractedUserVO apiUserInfo) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			UserDoctorRel userRel = new UserDoctorRel();
			userRel.setManagerId(apiUserInfo.getManagerId());
			userRel.setDoctorId(apiUserInfo.getDoctorId());
			userRel.setInUse(1);
			if (!StringUtil.isEmpty(String.valueOf(apiUserInfo.getBuildType()))
					&& apiUserInfo.getBuildType() != 0 && apiUserInfo.getBuildType() == 2) { // 申请服务
				userRel.setCreateTime(new Date());
				userRel.setBuildType(2);
				userRel.setExpirationDate(apiUserInfo.getExpirationDate());
				userRel.setSymptom(apiUserInfo.getSymptom());
				List<UserDoctorRel> relList = userDoctorRelMapper.selectByEntity(userRel); // 只创建关系
				if (null != relList && relList.size() > 0) {
					return result = new Result<>(StatusCodes.OK, false,
							new ResultCode("FAIL", "当前用户已申请过该医生服务"));
				}
				ApiUserInfo user = apiUserMapper.selectByPrimaryKey(apiUserInfo.getManagerId());
				Message message = new Message();
				message.setMessageFrom(apiUserInfo.getManagerId());
				message.setMessageTo(apiUserInfo.getDoctorId());
				message.setType(Message.Type.INTERNAL.getValue());
				String auditMessage ="用户"+user.getMobile()+" "+user.getName()+
						" 申请您的服务，该用户的个人信息，可在用户管理中查看，以下是申请服务的内容："+apiUserInfo.getSymptom();
				message.setContent(auditMessage);
				message.setStatus(1);
				message.setId(UUID.randomUUID().toString());
				message.setCreateTime(new Date());
				message.setUpdateTime(new Date());
				int num = messageMapper.insertSelective(message);
				userDoctorRelMapper.insertSelective(userRel);
				result.setResultCode(new ResultCode("SUCCESS", "申请成功"));
				return result;
			}
			List<UserDoctorRel> relList = userDoctorRelMapper
					.selectByManagerId(apiUserInfo.getManagerId());
			if (null != relList && relList.size() > 0) {
				return result = new Result<>(StatusCodes.OK, false,
						new ResultCode("FAIL", "当前用户已经签约过"));
			}
			int num = apiContractedUserMapper.insertUserVO(apiUserInfo);
			if (num > 0) {
				if (null != apiUserInfo.getDataList() && apiUserInfo.getDataList().size() > 0) {
					for (UserHealthyLivingVO healthy : apiUserInfo.getDataList()) {
						healthy.setContractedUserId(apiUserInfo.getId());
						healthy.setCreateTime(new Date());
						healthy.setUpdateTime(new Date());
					}
					userHealthyLivingMapper.insertHealthyVOList(apiUserInfo.getDataList());
				}
				userRel.setCreateTime(apiUserInfo.getCreateTime());
				userRel.setContractedUserId(apiUserInfo.getId());
				userRel.setExpirationDate(apiUserInfo.getExpirationDate());
				userRel.setUpdateTime(apiUserInfo.getUpdateTime());
				userDoctorRelMapper.insert(userRel);
			} else {
				return result = new Result<>(StatusCodes.OK, false, UserInfoAddFail);
			}
			if (null != apiUserInfo.getManagerId()) {
				ApiUserInfoVO av = new ApiUserInfoVO();
				av.setManagerId(apiUserInfo.getManagerId());
				av.setBirthday(apiUserInfo.getBirthday());
				if (apiUserInfo.getSex() == 3) {
					av.setSex("0");
				} else {
					av.setSex(String.valueOf(apiUserInfo.getSex()));
				}
				apiUserMapper.updateByApiUserinfoVO(av);
			}
			result.setResultCode(new ResultCode("SUCCESS", "添加成功"));
			// 创建订单
		} catch (Exception e) {
			return result = new Result<>(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result deleteUserArchives(ApiContractedUserVO apiUserInfo) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			userDoctorRelMapper.deleteByUserId(apiUserInfo.getId());
			apiContractedUserMapper.deleteByPrimaryKey(apiUserInfo.getId());
			userHealthyLivingMapper.deleteByUserId(apiUserInfo.getId());
		} catch (Exception e) {
			return result = new Result<>(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result updateUserArchives(ApiContractedUserVO apiUserInfo) {
		Result result = new Result<>(StatusCodes.OK, true);
		try {
			ApiContractedUserVO user = apiContractedUserMapper
					.selectInfoByPrimaryKey(apiUserInfo.getId());
			apiUserInfo.setCreateTime(user.getCreateTime());
			apiUserInfo.setExpirationDate(user.getExpirationDate());
			apiUserInfo.setInUse(String.valueOf(user.getInUse()));
			apiUserInfo.setYears(user.getYears());
			apiUserInfo.setUpdateTime(new Date());
			apiUserInfo.setDoctorId(user.getDoctorId());
			result = deleteUserArchives(apiUserInfo);
			if (result.isSuccessful()) {
				result = addUserArchives(apiUserInfo);
			} else {
				return result = new Result<>(StatusCodes.OK, false, UserInfoAddFail);
			}
			result.setResultCode(new ResultCode("SUCCESS", "修改成功"));
		} catch (Exception e) {
			logger.info(e.getMessage());
			result = new Result(StatusCodes.OK, false, MysqlWrong);
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Result userAnalysis(ApiUserInfoVO userInfo) {
		Result result = new Result<>(StatusCodes.OK, true);
		if (userInfo.getType().equals("0")) {// 0:性别 1：疾病史 2：年龄
			List<UserCountVO> listUsers = apiContractedUserMapper.selectAllBySex(userInfo);
			for (UserCountVO user : listUsers) {
				user = sexSet(user);
			}
			result.setResultCode(new ResultCode("SUCCESS", "用户分析查询成功！"));
			result.setData(listUsers);
			return result;
		} else if (userInfo.getType().equals("1")) {
			List<UserCountVO> listUsers = apiContractedUserMapper.selectAllByDisease(userInfo);
			for (UserCountVO user : listUsers) {
				user = diseaseSet(user);
			}
			result.setResultCode(new ResultCode("SUCCESS", "用户分析查询成功！"));
			result.setData(listUsers);
			return result;
		} else if (userInfo.getType().equals("2")) {
			List<UserCountVO> listUsers = apiContractedUserMapper.selectAllByAge(userInfo);
			for (UserCountVO user : listUsers) {
				user = ageSet(user);
			}
			result.setResultCode(new ResultCode("SUCCESS", "用户分析查询成功！"));
			result.setData(listUsers);
			return result;
		}
		return result;
	}

	/**
	 * 用户管理
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result getAllContractedUser(ApiContractedUserVO apiUserInfo) {

		PaginatedResult<ApiContractedUserVO> pa = null;
		int currentResult = (apiUserInfo.getCurrentPage() - 1) * apiUserInfo.getShowCount();
		apiUserInfo.setCurrentResult(currentResult);
		try {
			Pattern pattern = Pattern.compile("[0-9]*");
			List<String> doctorIdList = new ArrayList<String>();
			// 判断医生id 是否all
			if (!StringUtil.isEmpty(apiUserInfo.getDoctorId())
					&& apiUserInfo.getDoctorId().equals("all")) {
				apiUserInfo.setDoctorId(null);
			}
			if (!StringUtil.isEmpty(apiUserInfo.getDoctorId())) {
				doctorIdList.add("'" + apiUserInfo.getDoctorId() + "'");
			}
			if (!StringUtil.isEmpty(apiUserInfo.getOrgId())
					&& StringUtil.isEmpty(apiUserInfo.getDoctorId())) {
				JyDoctorInfo entity = new JyDoctorInfo();
				entity.setSignOrgId(apiUserInfo.getOrgId());
				entity.setShowCount(10000);
				entity.setCurrentPage(1);
				entity.setSignType(1);
				List<JyDoctorInfoVO> doctorInfoArray = jyOrgDocRelMapper.selectJySignDoctor(entity);
				if (null != doctorInfoArray && doctorInfoArray.size() > 0) {
					for (JyDoctorInfoVO jy : doctorInfoArray) {
						doctorIdList.add("'" + jy.getManagerId() + "'");
					}
				}
				doctorIdList.add("'" + apiUserInfo.getOrgId() + "'");
			}
			if (doctorIdList.size() > 0) {
				apiUserInfo.setDoctorIdList(doctorIdList);
			}
			String ageBegin = "";
			String ageEnd = "";
			if (!pattern.matcher(String.valueOf(apiUserInfo.getAgeId())).matches()) {
				apiUserInfo.setAgeId(null);
			} else {
				String ageId = apiUserInfo.getAgeId();
				if (ageId.equals("1")) {
					ageBegin = "0";
					ageEnd = "6";
				} else if (ageId.equals("2")) {
					ageBegin = "7";
					ageEnd = "17";
				} else if (ageId.equals("3")) {
					ageBegin = "18";
					ageEnd = "40";
				} else if (ageId.equals("4")) {
					ageBegin = "41";
					ageEnd = "65";
				} else if (ageId.equals("5")) {
					ageBegin = "65";
				} else if (ageId.equals("6")) {
					ageBegin = "-1";
				}
			}
			apiUserInfo.setAgeBegin(ageBegin);
			apiUserInfo.setAgeEnd(ageEnd);
			if (!pattern.matcher(String.valueOf(apiUserInfo.getDiseaseId())).matches()) {
				apiUserInfo.setDiseaseId(null);
			}
			if (!pattern.matcher(String.valueOf(apiUserInfo.getInUse())).matches()) {
				apiUserInfo.setInUse(null);
			}
			if (!StringUtil.isEmpty(apiUserInfo.getName())) {
				apiUserInfo.setName(apiUserInfo.getName() + "%");
			}
			List<ApiContractedUserVO> listUser = new ArrayList<ApiContractedUserVO>();
			listUser = apiContractedUserMapper.getAllUserByConditionListPage(apiUserInfo);
			pa = new PaginatedResult<ApiContractedUserVO>(listUser, apiUserInfo.getCurrentPage(),
					apiUserInfo.getShowCount(), apiUserInfo.getTotalResult());
			pa.setSuccessful(true);
			pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} catch (Exception e) {
			logger.info(e.getMessage());
			pa = new PaginatedResult<>(apiUserInfo.getCurrentPage(), apiUserInfo.getShowCount());
			pa.setSuccessful(false);
			pa.setResultCode(MysqlWrong);
		}
		return pa;
	}

	/**
	 * 查询医生
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result queryDoctors(String managerId) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<BackstageUserInfoVO> listBuser = new ArrayList<BackstageUserInfoVO>();
		List<BackstageUserInfoVO> returnBuser = new ArrayList<BackstageUserInfoVO>();
		listBuser = userDoctorRelMapper.selectByMangerId(managerId);
		if (null != listBuser && listBuser.size() > 0) {
			for (BackstageUserInfoVO b : listBuser) {
				String nowTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				String outTime = new SimpleDateFormat("yyyy-MM-dd").format(b.getExpirationDate());
				if (nowTime.compareTo(outTime) >= 0) {
					userDoctorRelMapper.updateInuse(b.getId());
				} else {
					if (!StringUtil.isEmpty(b.getDesignOrgId())) {
						BackstageUserInfo user = backUserMapper
								.selectByPrimaryKey(b.getDesignOrgId());
						b.setDesignOrgName(user.getName());
					}
					returnBuser.add(b);
				}
			}
		}
		result.setData(returnBuser);
		return result;
	}

	/**
	 * 查询医生
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result queryRel(UserDoctorRel udRel) {
		Result result = new Result<>(StatusCodes.OK, true);
		udRel.setBuildType(2);// 查询申请服务的关系
		List<UserDoctorRel> rels = userDoctorRelMapper.selectByEntity(udRel);
		if (null != rels && rels.size() > 0) {
			result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		}
		result.setData(rels);
		return result;
	}

	public UserCountVO sexSet(UserCountVO user) {
		if (StringUtil.isEmpty(user.getSexs())) {
			return user;
		} else {
			String[] ss = user.getSexs().split(",");
			for (String s : ss) {
				if (s.split(":")[0].equals("1")) {
					user.setManCount(Double.valueOf(s.split(":")[1].toString()));
				} else if (s.split(":")[0].equals("2")) {
					user.setWomanCount(Double.valueOf(s.split(":")[1].toString()));
				} else if (s.split(":")[0].equals("0")) {
					user.setSecretCount(Double.valueOf(s.split(":")[1].toString()));
				} else {
					user.setNotExplainedCount(Double.valueOf(s.split(":")[1].toString()));
				}
			}
			return user;
		}
	}

	public UserCountVO ageSet(UserCountVO user) {
		if (StringUtil.isEmpty(user.getAges())) {
			return user;
		} else {
			String[] ss = user.getAges().split(",");
			for (String s : ss) {
				int age = Integer.parseInt(s);
				if (age >= 0 && age <= 6) {
					user.setChildrenCount(user.getChildrenCount() + 1);
				} else if (age >= 7 && age <= 17) {
					user.setJuvenileCount(user.getJuvenileCount() + 1);
				} else if (age >= 18 && age <= 40) {
					user.setYouthCount(user.getYouthCount() + 1);
				} else if (age >= 41 && age <= 65) {
					user.setMiddleAgeCount(user.getMiddleAgeCount() + 1);
				} else if (age > 65) {
					user.setOldAgeCount(user.getOldAgeCount() + 1);
				} else {
					user.setNoAgeCount(user.getNoAgeCount() + 1);
				}
			}
			return user;
		}
	}

	// 1 高血压 2糖尿病 3 冠心病 4 无 5 慢性阻塞性肺疾病 6恶性肿瘤 7 脑卒中 8 严重精神障碍
	// 9 结核病 10 肝炎 11其他法定传染病 12 职业病 13 其他
	public UserCountVO diseaseSet(UserCountVO user) {
		if (StringUtil.isEmpty(user.getDieases())) {
			return user;
		} else {
			String[] ss = user.getDieases().split(",");
			for (String s : ss) {
				int itemId = Integer.parseInt(s.split(":")[0]);
				if (itemId == 1) {
					user.setHbpCount(user.getHbpCount() + 1);
				} else if (itemId == 2) {
					user.setDmCount(user.getDmCount() + 1);
				} else if (itemId == 3) {
					user.setChdCount(user.getChdCount() + 1);
				} else if (itemId == 4) {
					user.setNoCount(user.getNoCount() + 1);
				} else if (itemId == 5) {
					user.setCopdCount(user.getCopdCount() + 1);
				} else if (itemId == 6) {
					user.setMtCount(user.getMtCount() + 1);
				} else if (itemId == 7) {
					user.setCerebralApoplexyCount(user.getCerebralApoplexyCount() + 1);
				} else if (itemId == 8) {
					user.setMentalDisordeCount(user.getMentalDisordeCount() + 1);
				} else if (itemId == 9) {
					user.setTbCount(user.getTbCount() + 1);
				} else if (itemId == 10) {
					user.setAihCount(user.getAihCount() + 1);
				} else if (itemId == 11) {
					user.setOtherNotifiableDiseasesCount(
							user.getOtherNotifiableDiseasesCount() + 1);
				} else if (itemId == 12) {
					user.setOhCount(user.getOhCount() + 1);
				} else {
					user.setOtherCount(user.getOtherCount() + 1);
				}
			}
			return user;
		}
	}

	/**
	 * 根据用户id或者主键id查询健康档案
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result querySignUserByEntity(ApiContractedUserVO apiUserInfo) {
		Result result = new Result<>(StatusCodes.OK, true);
		ApiUserInfoVO user = apiContractedUserMapper.selectApiUserByEntity(apiUserInfo);
		result.setData(user);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}

}
