package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.ApiUserInfo;
import com.semioe.api.vo.ApiContractedUserVO;
import com.semioe.api.vo.ApiUserInfoVO;
import com.semioe.api.vo.ProvinceVO;
import com.semioe.api.vo.QrcodeInfoVO;
import com.semioe.api.vo.UserCountVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.dao.ApiContractedUserMapper;
import com.semioe.dubbo.dao.ApiUserInfoMapper;
import com.semioe.dubbo.dao.QrcodeRelMapper;
import com.semioe.dubbo.service.OperationDataService;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OperationDataServiceImpl implements OperationDataService {

	private static final Logger logger = LoggerFactory.getLogger(OperationDataServiceImpl.class);

	@Autowired
	private ApiUserInfoMapper apiUserInfoMapper;
	@Autowired
	private ApiContractedUserMapper apiContractedUserMapper;
	@Autowired
	private QrcodeRelMapper qrcodeMapper;

	@SuppressWarnings("unchecked")
	@Override
	public PaginatedResult<UserCountVO> countRegist(ApiUserInfoVO userInfo) {
		List<UserCountVO> listCoutVOs = new ArrayList<UserCountVO>();
		listCoutVOs = apiUserInfoMapper.countRegsitUser(userInfo);
		Double sum = 0.0;
		for (UserCountVO u : listCoutVOs) {
			sum = sum + u.getCountNumber();
		}
		int totalCount = listCoutVOs.size();
		List<UserCountVO> listCout = new ArrayList<UserCountVO>();
		listCout = apiUserInfoMapper.countRegsitUserListPage(userInfo);
		PaginatedResult<UserCountVO> pa = new PaginatedResult<UserCountVO>(listCout,
				userInfo.getCurrentPage(), userInfo.getShowCount(), totalCount);
		pa.setSuccessful(true);
		pa.setData(sum);
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

	@Override
	public PaginatedResult<ApiUserInfo> registDetaile(ApiUserInfoVO userInfo) {
		int totalCount = apiUserInfoMapper.regsitDetail(userInfo).size();
		List<ApiUserInfo> listCout = new ArrayList<ApiUserInfo>();
		listCout = apiUserInfoMapper.regsitDetailListPage(userInfo);
		PaginatedResult<ApiUserInfo> pa = new PaginatedResult<ApiUserInfo>(listCout,
				userInfo.getCurrentPage(), userInfo.getShowCount(), totalCount);
		pa.setSuccessful(true);
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result userAnalysis(ApiUserInfoVO userInfo) {
		Result result = new Result<>(StatusCodes.OK, true);
		if (userInfo.getType().equals("0")) {// 0:性别 1：区域 2：年龄
			List<UserCountVO> listUsers = apiUserInfoMapper.selectAllBySex(userInfo);
			for (UserCountVO user : listUsers) {
				user = sexSet(user);
			}
			result.setResultCode(new ResultCode("SUCCESS", "运营设置查询成功！"));
			result.setData(listUsers);
			return result;
		} else if (userInfo.getType().equals("1")) {
			List<UserCountVO> listUsers = apiUserInfoMapper.selectAllByArea(userInfo);
			UserCountVO userCount = areaSet(listUsers);
			result.setResultCode(new ResultCode("SUCCESS", "运营设置查询成功！"));
			result.setData(userCount);
			return result;
		} else if (userInfo.getType().equals("2")) {
			List<UserCountVO> listUsers = apiUserInfoMapper.selectAllByAge(userInfo);
			for (UserCountVO user : listUsers) {
				user = ageSet(user);
			}
			result.setResultCode(new ResultCode("SUCCESS", "运营设置查询成功！"));
			result.setData(listUsers);
			return result;
		}
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
				} else {
					if(user.getSecretCount()!=null && user.getSecretCount()>0) {
						user.setSecretCount(user.getSecretCount()+Double.valueOf(s.split(":")[1].toString()));
					}else {
						user.setSecretCount(Double.valueOf(s.split(":")[1].toString()));
					}
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

	public UserCountVO areaSet(List<UserCountVO> listUsers) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserCountVO user = new UserCountVO();
		user.setCountDateList(new ArrayList<String>());
		if (null == listUsers || listUsers.size() < 1) {
			return user;
		} else {
			for (UserCountVO userVO : listUsers) {
				user.getCountDateList().add(userVO.getCountDate());
				String[] ss = userVO.getProvinces().split(",");
				// Set<String> setMap = new HashSet<String>();
				// for (String s : ss) {
				// setMap.add(s);
				// }
				// Iterator<String> it = setMap.iterator();
				Map<String, Integer> mapProvince = new HashMap<String, Integer>();
				if (null != ss && ss.length > 0) {
					for (String str : ss) {
						if (StringUtil.isEmpty(str.split(":")[0])) {
							if (null != mapProvince.get("保密")) {
								if (Integer.parseInt(str.split(":")[1]) > 0) {
									mapProvince.put("保密", Integer.parseInt(str.split(":")[1]));
								}
							} else {
								mapProvince.put("保密", Integer.parseInt(str.split(":")[1]));
							}
							continue;
						}
						if (null != mapProvince.get(str.split(":")[0])) {
							if (Integer.parseInt(str.split(":")[1]) > 0) {
								mapProvince.put(str.split(":")[0],
										Integer.parseInt(str.split(":")[1]));
							}
							continue;
						}
						mapProvince.put(str.split(":")[0], Integer.parseInt(str.split(":")[1]));
					}
				}
				for (String str : mapProvince.keySet()) {
					String province = "";
					if (StringUtil.isEmpty(str)) {
						province = "保密";
					} else {
						province = str;
					}
					boolean b = false;
					for (String key : map.keySet()) {
						if (key.equals(province)) {
							map.put(key, map.get(key) + "," + mapProvince.get(str));
							b = true;
							break;
						}
					}
					if (!b) {
						map.put(province, mapProvince.get(str));
					}
				}
			}
			List<ProvinceVO> listPro = new ArrayList<ProvinceVO>();
			for (String key : map.keySet()) {
				ProvinceVO pro = new ProvinceVO();
				pro.setName(key);
				pro.setData(String.valueOf(map.get(key)));
				listPro.add(pro);
			}
			user.setProviceList(listPro);
		}
		map.clear();
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaginatedResult<UserCountVO> countconversion(ApiUserInfoVO userInfo) {
		List<UserCountVO> listCoutVOs = apiUserInfoMapper.countConversion(userInfo);
		Double visitorSum = 0.0;
		Double purchaserSum = 0.0;
		Double conversionSum = 0.0;
		UserCountVO userCount = new UserCountVO();
		if (null != listCoutVOs && listCoutVOs.size() > 0) {
			for (UserCountVO u : listCoutVOs) {
				visitorSum = visitorSum + u.getVisitorCount();
				purchaserSum = purchaserSum + u.getPurchaserCount();
				conversionSum = conversionSum + u.getConversionRate();
			}
			DecimalFormat df = new DecimalFormat("0.00");
			conversionSum = purchaserSum / visitorSum;
			conversionSum = new Double(df.format(conversionSum).toString());
		}
		userCount.setVisitorSum(visitorSum);
		userCount.setPurchaserSum(purchaserSum);
		userCount.setConversionSum(conversionSum);
		List<UserCountVO> listCout = new ArrayList<UserCountVO>();
		listCout = apiUserInfoMapper.countConversionListPage(userInfo);
		PaginatedResult<UserCountVO> pa = new PaginatedResult<UserCountVO>(listCout,
				userInfo.getCurrentPage(), userInfo.getShowCount(), listCoutVOs.size());
		pa.setSuccessful(true);
		pa.setData(userCount);
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

	@Override
	public Result queryProvince(String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		// Province province = provinceMapper.selectByProvinceId(id);
		// result.setData(province);
		result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaginatedResult<QrcodeInfoVO> countQrcode(QrcodeInfoVO qrcodeInfo) {
		List<QrcodeInfoVO> listCode = new ArrayList<QrcodeInfoVO>();
		listCode = qrcodeMapper.selectQrcodeRelByContion(qrcodeInfo);
		if (qrcodeInfo.getCurrentPage() < 0) {
			PaginatedResult<QrcodeInfoVO> pa = new PaginatedResult<QrcodeInfoVO>(listCode,
					qrcodeInfo.getCurrentPage(), qrcodeInfo.getShowCount(), listCode.size());
			return pa;
		}
		List<QrcodeInfoVO> listCout = new ArrayList<QrcodeInfoVO>();
		Double sumPrice = 0.0;
		Double sumPeople = Double.valueOf(listCode.size());
		for (QrcodeInfoVO code : listCode) {
			if (!StringUtil.isEmpty(code.getOrderPrice())) {
				sumPrice = sumPrice + Double.valueOf(code.getOrderPrice());
			}
		}
		UserCountVO userCount = new UserCountVO();
		userCount.setSumPrice(sumPrice);
		userCount.setSumPeople(sumPeople);
		listCout = qrcodeMapper.selectQrcodeRelListPage(qrcodeInfo);
		PaginatedResult<QrcodeInfoVO> pa = new PaginatedResult<QrcodeInfoVO>(listCout,
				qrcodeInfo.getCurrentPage(), qrcodeInfo.getShowCount(), listCode.size());
		pa.setSuccessful(true);
		pa.setData(userCount);
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;

	}

	/**
	 * signCount 20171216
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PaginatedResult<UserCountVO> signCount(ApiUserInfoVO userInfo) {
		Double signSum = 0.0;
		UserCountVO userCount = new UserCountVO();
		List<UserCountVO> listCout = new ArrayList<UserCountVO>();
		listCout = apiUserInfoMapper.countSignListPage(userInfo);
		PaginatedResult<UserCountVO> pa = new PaginatedResult<UserCountVO>(listCout,
				userInfo.getCurrentPage(), userInfo.getShowCount(), userInfo.getTotalResult());
		for (UserCountVO u : listCout) {
			signSum += u.getSignCount();
		}
		userCount.setSignSum(signSum);
		pa.setSuccessful(true);
		pa.setData(userCount);
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

	@Override
	public PaginatedResult<ApiContractedUserVO> signDetaile(ApiUserInfoVO userInfo) {
		List<ApiContractedUserVO> listCout = new ArrayList<ApiContractedUserVO>();
		listCout = apiUserInfoMapper.signDetaileListPage(userInfo);
		PaginatedResult<ApiContractedUserVO> pa = new PaginatedResult<ApiContractedUserVO>(listCout,
				userInfo.getCurrentPage(), userInfo.getShowCount(), userInfo.getTotalResult());
		pa.setSuccessful(true);
		pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		return pa;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Result signAnalysis(ApiUserInfoVO userInfo) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<UserCountVO> listUsers = new ArrayList<UserCountVO>();
		if (userInfo.getAnalysisType().equals("1")) {// 1:性别
			if (userInfo.getDisplayType().equals("3")) {// 1:天 2：月 3：汇总
				listUsers = apiContractedUserMapper
						.selectAllBySexSummary(userInfo);
				if(null!=listUsers && listUsers.get(0)==null) {
					result.setData(null);
					result.setResultCode(new ResultCode("SUCCESS", "性别统计查询为空！"));
					return result;
				}
			} else {
				listUsers = apiContractedUserMapper.selectAllBySex(userInfo);
			}
			for (UserCountVO user : listUsers) {
				user = sexSet(user);
			}
			result.setData(listUsers);
			result.setResultCode(new ResultCode("SUCCESS", "性别统计查询成功！"));
			return result;
		} else if (userInfo.getAnalysisType().equals("2")) {// 2：年龄
			if (userInfo.getDisplayType().equals("3")) {// 1:天 2：月 3：汇总
				listUsers = apiContractedUserMapper
						.selectAllByAgeSummary(userInfo);
				if(null!=listUsers && listUsers.get(0)==null) {
					result.setData(null);
					result.setResultCode(new ResultCode("SUCCESS", "用户年龄统计查询为空！"));
					return result;
				}
			} else {
				listUsers = apiContractedUserMapper.selectAllByAge(userInfo);
			}
			for (UserCountVO user : listUsers) {
				user = ageSet(user);
			}
			result.setData(listUsers);
			result.setResultCode(new ResultCode("SUCCESS", "用户年龄统计查询成功！"));
			return result;
		} else if (userInfo.getAnalysisType().equals("3")) {// 3：签约机构
			if (userInfo.getDisplayType().equals("3")) {// 1:天 2：月 3：汇总
				listUsers = apiContractedUserMapper
						.selectAllBySignOrgSummary(userInfo);
				if(null!=listUsers && listUsers.get(0)==null) {
					result.setData(null);
					result.setResultCode(new ResultCode("SUCCESS", "签约机构统计查询为空！"));
					return result;
				}
			} else {
				listUsers = apiContractedUserMapper.selectAllBySignOrg(userInfo);
			}
			result.setData(countDate(listUsers, "1"));
			result.setResultCode(new ResultCode("SUCCESS", "签约机构统计查询成功！"));
			return result;
		} else if (userInfo.getAnalysisType().equals("4")) {// 4：签约医生
			if (userInfo.getDisplayType().equals("3")) {// 1:天 2：月 3：汇总
				listUsers = apiContractedUserMapper
						.selectAllBySignDocSummary(userInfo);
				if(null!=listUsers && listUsers.get(0)==null) {
					result.setData(null);
					result.setResultCode(new ResultCode("SUCCESS", "签约医生统计查询为空！"));
					return result;
				}
			} else {
				listUsers = apiContractedUserMapper.selectAllBySignDoc(userInfo);
			}
			result.setData(countDate(listUsers, "2"));
			result.setResultCode(new ResultCode("SUCCESS", "签约医生统计查询成功！"));
			return result;
		} else if (userInfo.getAnalysisType().equals("5")) {// 5：疾病
			if (userInfo.getBuildType().equals("1") || userInfo.getBuildType().equals("0")) {
				if (userInfo.getDisplayType().equals("3")) {// 1:天 2：月 3：汇总
					listUsers = apiContractedUserMapper
						.selectAllByDiseaseSummary(userInfo);
					if(null!=listUsers && listUsers.get(0)==null) {
						result.setData(null);
						result.setResultCode(new ResultCode("SUCCESS", "疾病统计查询为空！"));
						return result;
					}
				} else {
					listUsers = apiContractedUserMapper.selectAllByDisease(userInfo);
				}
				for (UserCountVO user : listUsers) {
					user = sighDiseaseSet(user);
				}
			}
			result.setData(listUsers);
			result.setResultCode(new ResultCode("SUCCESS", "用户疾病统计查询成功！"));
			return result;
		} else if (userInfo.getAnalysisType().equals("6")) {// 6：家庭组
			if (userInfo.getDisplayType().equals("3")) {
				listUsers = apiContractedUserMapper
						.selectFamilySummaryCountListPage(userInfo);
			}else {
				listUsers = apiContractedUserMapper
					.selectFamilyCountListPage(userInfo);
			}
			PaginatedResult<UserCountVO> pa = new PaginatedResult<UserCountVO>(listUsers,
					userInfo.getCurrentPage(), userInfo.getShowCount(), userInfo.getTotalResult());
			pa.setSuccessful(true);
			pa.setResultCode(new ResultCode("SUCCESS", "家庭组统计查询成功！"));
			return pa;
		}
		return result;
	}

	@Override
	public Result familyAnalysis(ApiUserInfoVO userInfo) {
		List<UserCountVO> listCout = new ArrayList<UserCountVO>();
		listCout = apiUserInfoMapper.selectAllByFamilyListPage(userInfo);
		PaginatedResult<UserCountVO> pa = new PaginatedResult<UserCountVO>(listCout,
				userInfo.getCurrentPage(), userInfo.getShowCount(), userInfo.getTotalResult());
		pa.setSuccessful(true);
		pa.setResultCode(new ResultCode("SUCCESS", "家庭组（大）统计成功！"));
		return pa;

	}

	@Override
	public Result familyDetail(UserCountVO userInfo) {
		Result result = new Result<>(StatusCodes.OK, true);
		List<UserCountVO> listCout = new ArrayList<UserCountVO>();
		List<String> managerList = null;
		List<ApiContractedUserVO> returnList = null;
		Map<String,Integer> returnMap = null;
		for (String parentId : userInfo.getParentIds().split(",")) {
			returnMap = new HashMap<String,Integer>();
			managerList = new ArrayList<String>();
			returnList = new ArrayList<ApiContractedUserVO>();
			ApiUserInfo au = new ApiUserInfo();
			if (!StringUtil.isEmpty(userInfo.getParentMobile())) {
				au.setMobile(userInfo.getParentMobile());
			}
			if (!StringUtil.isEmpty(userInfo.getParentName())) {
				au.setName(userInfo.getParentName());
			}
			au.setManagerId(parentId);
			ApiUserInfo parentUser = apiUserInfoMapper.selectByEntity(au);
			if (null != parentUser) {
				UserCountVO uc = new UserCountVO();
				List<Map<String, Object>> listChildUser = apiUserInfoMapper
						.selectSubUserInfo(parentId);
				managerList.add("'" + parentId+"'" );
				for (Map<String, Object> child : listChildUser) {
					managerList.add("'" + child.get("managerId") + "'");
				}
				userInfo.setManagerList(managerList);
				List<ApiContractedUserVO> listUser = apiContractedUserMapper.selectAllByFamilyDetailListPage(userInfo);
				for(ApiContractedUserVO acu:listUser) {
					if(returnMap.get(acu.getManagerId())==null) {
						returnMap.put(acu.getManagerId(), acu.getBuildType());
						returnList.add(acu);
					}else if(returnMap.get(acu.getManagerId())==0) {
						if(acu.getBuildType()>0) {
							returnMap.put(acu.getManagerId(), acu.getBuildType());
						}
						continue;
					}else {
						if(acu.getBuildType()!=0) {
							returnList.add(acu);
						}
					}
				}
				uc.setListApiUser(returnList);
				uc.setParentName(parentUser.getName());
				uc.setParentMobile(parentUser.getMobile());
				listCout.add(uc);
			}
		}
		result.setResultCode(new ResultCode("SUCCESS", "家庭组明细查询成功！"));
		result.setData(listCout);
		return result;
	}
	
	/**
	 * 查询所有已和用户签约 的医生/机构
	 * 
	 * @param listUsers
	 * @param type
	 *            1：机构 2：医生
	 * @return
	 */
	public UserCountVO countDate(List<UserCountVO> listUsers, String type) {
		Map<String, String> data = new HashMap<String, String>();
		UserCountVO user = new UserCountVO();
		if (null != listUsers && listUsers.size() > 0) {
			user.setCountDateList(new ArrayList<String>());
			Map<String, Integer> map = new HashMap<String, Integer>();
			if (type.equals("1")) {
				for (UserCountVO uc : listUsers) {
					if (!StringUtil.isEmpty(uc.getOrgs()) || uc.getOrgs().split(",").length > 0) {
						for (String st : uc.getOrgs().split(",")) {
							map.put(st.split(":")[1], 0);
						}
					}
				}
			} else {
				for (UserCountVO uc : listUsers) {
					if (!StringUtil.isEmpty(uc.getDoctors())
							|| uc.getDoctors().split(",").length > 0) {
						for (String st : uc.getDoctors().split(",")) {
							map.put(st.split(":")[1], 0);
						}
					}
				}
			}
			for (UserCountVO uc : listUsers) {
				if(!StringUtil.isEmpty(uc.getCountDate())) {
					user.getCountDateList().add(uc.getCountDate());
				}
				String[] datas = null;
				if (type.equals("1")) {
					datas = uc.getOrgs().split(",");
				} else {
					datas = uc.getDoctors().split(",");
				}
				Map<String, String> m = new HashMap<>();
				if (null != datas && datas.length > 0) {
					for (String str : datas) {
						for (String s : map.keySet()) {
							if (str.split(":")[1].equals(s)) {
								if ((!StringUtil.isEmpty(m.get(s)) && m.get(s).equals("0"))
										|| StringUtil.isEmpty(m.get(s))) {
									m.put(s, str.split(":")[2]);
								}
							} else {
								if (StringUtil.isEmpty(m.get(s))) {
									m.put(s, "0");
								}
							}
						}
					}
					for (String s : m.keySet()) {
						data.put(s, null == data.get(s) ? m.get(s) : data.get(s) + "," + m.get(s));
					}
					m.clear();
				}
			}
			List<ProvinceVO> listPro = new ArrayList<ProvinceVO>();
			for (String key : data.keySet()) {
				ProvinceVO pro = new ProvinceVO();
				pro.setName(key);
				pro.setData(String.valueOf(data.get(key)));
				listPro.add(pro);
			}
			user.setProviceList(listPro);
		}
		return user;
	}

	// 1 高血压 2糖尿病 3 冠心病 0 无 5 慢性阻塞性肺疾病 6恶性肿瘤 7 脑卒中 8 严重精神障碍
	// 9 结核病 10 肝炎 11其他法定传染病 12 职业病 4职业病
	public UserCountVO sighDiseaseSet(UserCountVO user) {
		if (StringUtil.isEmpty(user.getDieases())) {
			return user;
		} else {
			String[] ss = user.getDieases().split(",");
			for (String s : ss) {
				int itemId = Integer.parseInt(s.split(":")[0]);
				if (itemId == 1) {
					user.setHbpCount(Integer.parseInt(s.split(":")[1]));
				} else if (itemId == 2) {
					user.setDmCount(Integer.parseInt(s.split(":")[1]));
				} else if (itemId == 3) {
					user.setChdCount(Integer.parseInt(s.split(":")[1]));
				} else if (itemId == 0) {
					user.setNoCount(Integer.parseInt(s.split(":")[1]));
				} else if (itemId == 5) {
					user.setCopdCount(Integer.parseInt(s.split(":")[1]));
				} else if (itemId == 6) {
					user.setMtCount(Integer.parseInt(s.split(":")[1]));
				} else if (itemId == 7) {
					user.setCerebralApoplexyCount(Integer.parseInt(s.split(":")[1]));
				} else if (itemId == 8) {
					user.setMentalDisordeCount(Integer.parseInt(s.split(":")[1]));
				} else if (itemId == 9) {
					user.setTbCount(Integer.parseInt(s.split(":")[1]));
				} else if (itemId == 10) {
					user.setAihCount(Integer.parseInt(s.split(":")[1]));
				} else if (itemId == 11) {
					user.setOtherNotifiableDiseasesCount(Integer.parseInt(s.split(":")[1]));
				} else if (itemId == 4) {
					user.setOhCount(Integer.parseInt(s.split(":")[1]));
				} else if (itemId == 12) {
					user.setOtherCount(Integer.parseInt(s.split(":")[1]));
				}
			}
			return user;
		}
	}

}
