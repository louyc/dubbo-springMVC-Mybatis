package com.semioe.dubbo.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.dto.ApiMotherhoodUserDTO;
import com.semioe.api.entity.ApiMotherhoodUser;
import com.semioe.api.entity.ApiMotherhoodUserExample;
import com.semioe.api.entity.Dictionary;
import com.semioe.api.entity.DictionaryExample;
import com.semioe.api.vo.ApiMotherhoodUserVO;
import com.semioe.api.vo.BackstageUserInfoVO;
import com.semioe.dubbo.dao.ApiMotherhoodUserMapper;
import com.semioe.dubbo.dao.DictionaryMapper;
import com.semioe.dubbo.dao.UserDoctorRelMapper;
import com.semioe.dubbo.service.ApiMotherhoodUserService;

/**
 * Created by kwinxu on 2017/12/18.
 */
@Service
public class ApiMotherhoodUserServiceImpl implements ApiMotherhoodUserService {

	@Autowired
	private ApiMotherhoodUserMapper apiMotherhoodUserMapper;

	@Autowired
	private DictionaryMapper dictionaryMapper;

	@Autowired
	private UserDoctorRelMapper userDoctorRelMapper;

	/**
	 * 添加孕产妇档案
	 *
	 * @param apiMotherhoodUser
	 * @return
	 * @throws Exception
	 */
	@Override
	public int add(ApiMotherhoodUserDTO apiMotherhoodUser) throws Exception {
		if (null == apiMotherhoodUser.getDocumentType()) {
			throw new Exception("证件类型不能为空！");
		}
		if (null == apiMotherhoodUser.getDocumentCode()
				|| StringUtil.isEmpty(apiMotherhoodUser.getDocumentCode())) {
			throw new Exception("证件号码不能为空！");
		}

		// 孕产期加一个月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(apiMotherhoodUser.getPregnancyBirth());
		calendar.add(Calendar.MONTH, 1);
		apiMotherhoodUser.setExpirationDate(calendar.getTime());
		// 建册孕周books_gestational_weeks
		// (建册时间created_date-末次月经last_menstruation)/7结果四舍五入
		// 初检孕周gestational_weeks
		// （确认日期 diagnosis_date-末次月经last_menstruation）/7=结果四舍五入
		int booksGestationalWeeks = (int) ((apiMotherhoodUser.getCreatedDate().getTime()
				- apiMotherhoodUser.getLastMenstruation().getTime()) / (1000 * 3600 * 24));
		int gestationalWeeks = (int) ((apiMotherhoodUser.getDiagnosisDate().getTime()
				- apiMotherhoodUser.getLastMenstruation().getTime()) / (1000 * 3600 * 24));
		apiMotherhoodUser
				.setBooksGestationalWeeks(String.valueOf(Math.round(booksGestationalWeeks / 7)));
		apiMotherhoodUser.setGestationalWeeks(String.valueOf(Math.round(gestationalWeeks / 7)));
		apiMotherhoodUser.setInUse(1);

		int row = apiMotherhoodUserMapper.insert(apiMotherhoodUser);
		if (row != 1) {
			throw new Exception("添加失败！");
		}
		int id = apiMotherhoodUser.getId();
		return id;
	}

	/**
	 * 查询孕产妇档案
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiMotherhoodUser findById(String id) throws Exception {
		if (null == id) {
			throw new Exception("id 不能为空！");
		}
		ApiMotherhoodUserExample example = new ApiMotherhoodUserExample();
		example.createCriteria().andManagerIdEqualTo(id).andInUseEqualTo(1);
		List<ApiMotherhoodUserVO> apiMotherhoodUserList = apiMotherhoodUserMapper
				.selectByExample(example);
		if (apiMotherhoodUserList.size() == 1) {
			ApiMotherhoodUserVO motherhoodUserVO = apiMotherhoodUserList.get(0);
			List<BackstageUserInfoVO> listBuser = userDoctorRelMapper
					.selectByMangerId(motherhoodUserVO.getManagerId());
			for (BackstageUserInfoVO b : listBuser) {
				if (b.getBuildType() == 3) {
					motherhoodUserVO.setOrgId(b.getDesignOrgId());
				}
			}
			// 保健指导
			DictionaryExample dictionaryExample = new DictionaryExample();
			dictionaryExample.createCriteria().andItemTypeEqualTo(33);
			String hcgs = motherhoodUserVO.getHealthCareGuidance();
			if (null != hcgs && hcgs.length() > 0) {
				String[] hcgArr = hcgs.split(",");
				List<Integer> hcgList = new ArrayList<Integer>();
				for (int i = 0, length = hcgArr.length; i < length; i++) {
					hcgList.add(Integer.valueOf(hcgArr[i]));
				}
				dictionaryExample.createCriteria().andItemIdIn(hcgList);
				List<Dictionary> dictionaryList = dictionaryMapper
						.selectByExample(dictionaryExample);
				StringBuffer stringBuffer = new StringBuffer();
				for (int di = 0, length = dictionaryList.size(); di < length; di++) {
					if (di > 0) {
						stringBuffer.append("，");
					}
					stringBuffer.append(dictionaryList.get(di).getItemName());
				}
				motherhoodUserVO.setHealthCareGuidanceDesc(stringBuffer.toString());
			} else {
				motherhoodUserVO.setHealthCareGuidanceDesc("");
			}

			Dictionary dictionary = new Dictionary();
			// 户籍地址
			if (null != motherhoodUserVO.getPermanentAddress()) {
				dictionary.setItemType(34);
				dictionary.setItemId(Integer.valueOf(motherhoodUserVO.getPermanentAddress()));
				motherhoodUserVO.setPermanentAddressDesc(getDicName(dictionary));
			} else {
				motherhoodUserVO.setPermanentAddressDesc("");
			}
			// 证件类型
			if (null != motherhoodUserVO.getHusbandCardType()
					&& motherhoodUserVO.getHusbandCardType() > 0) {
				if (motherhoodUserVO.getHusbandCardType() == 1) {
					motherhoodUserVO.setHusbandCardTypeDesc("身份证");
				} else {
					motherhoodUserVO.setHusbandCardTypeDesc("护照");
				}
			} else {
				motherhoodUserVO.setHusbandCardTypeDesc("");
			}

			// 本段或外段
			if (null != motherhoodUserVO.getInOutSegment()) {
				dictionary.setItemType(35);
				dictionary.setItemId(Integer.valueOf(motherhoodUserVO.getInOutSegment()));
				motherhoodUserVO.setInOutSegmentDesc(getDicName(dictionary));
			} else {
				motherhoodUserVO.setInOutSegmentDesc("");
			}

			// 本地妊娠次数
			if (null != motherhoodUserVO.getGestationTimes()) {
				dictionary.setItemType(36);
				dictionary.setItemId(Integer.valueOf(motherhoodUserVO.getGestationTimes()));
				motherhoodUserVO.setGestationTimesDesc(getDicName(dictionary));
			} else {
				motherhoodUserVO.setGestationTimesDesc("");
			}

			// 服药种类
			// if (null != motherhoodUserVO.getMedicineType()) {
			// dictionary.setItemType(37);
			// dictionary.setItemId(motherhoodUserVO.getMedicineType());
			// motherhoodUserVO.setMedicineTypeDesc(getDicName(dictionary));
			// } else {
			// motherhoodUserVO.setMedicineTypeDesc("");
			// }

			// 服药开始时间
			if (null != motherhoodUserVO.getMedicineTime()) {
				dictionary.setItemType(38);
				dictionary.setItemId(motherhoodUserVO.getMedicineTime());
				motherhoodUserVO.setMedicineTimeDesc(getDicName(dictionary));
			} else {
				motherhoodUserVO.setMedicineTimeDesc("");
			}

			// 文化程度
			if (null != motherhoodUserVO.getCulturalStatus()) {
				dictionary.setItemType(13);
				dictionary.setItemId(motherhoodUserVO.getCulturalStatus());
				motherhoodUserVO.setCulturalStatusDesc(getDicName(dictionary));
			} else {
				motherhoodUserVO.setCulturalStatusDesc("");
			}

			// 国籍
			motherhoodUserVO.setNationalityDesc("中国");

			// 民族
			if (null != motherhoodUserVO.getNation()) {
				dictionary.setItemType(11);
				dictionary.setItemId(motherhoodUserVO.getNation());
				motherhoodUserVO.setNationDesc(getDicName(dictionary));
			} else {
				motherhoodUserVO.setNationDesc("");
			}

			// 职业
			if (null != motherhoodUserVO.getOccupation()) {
				dictionary.setItemType(14);
				dictionary.setItemId(motherhoodUserVO.getOccupation());
				motherhoodUserVO.setOccupationDesc(getDicName(dictionary));
			} else {
				motherhoodUserVO.setOccupationDesc("");
			}

			// 婚姻状况
			if (null != motherhoodUserVO.getMaritalStatus()) {
				dictionary.setItemType(12);
				dictionary.setItemId(motherhoodUserVO.getMaritalStatus());
				motherhoodUserVO.setMaritalStatusDesc(getDicName(dictionary));
			} else {
				motherhoodUserVO.setMaritalStatusDesc("");
			}

			return motherhoodUserVO;
		} else {
			throw new Exception("孕产妇档案错误！");
		}
	}

	private String getDicName(Dictionary dic) {
		List<Dictionary> dictionaryList = dictionaryMapper.selectByTypeId(dic);
		if (dictionaryList.size() == 1) {
			return dictionaryList.get(0).getItemName();
		} else {
			return "";
		}

	}

	/**
	 * 修改该孕产妇档案
	 *
	 * @param apiMotherhoodUser
	 * @return
	 * @throws Exception
	 */
	@Override
	public int modifyMotherhoodRecord(ApiMotherhoodUser apiMotherhoodUser) throws Exception {
		if (null == apiMotherhoodUser) {
			throw new Exception("孕产妇档案不能为空！");
		}
		if (null == apiMotherhoodUser.getId()) {
			throw new Exception("id 不能为空！");
		}
		// if (null == apiMotherhoodUser.getVerifyingPregnancy()) {
		// throw new Exception("核实孕产期不能为空！");
		// }

		// 孕产期加一个月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(apiMotherhoodUser.getPregnancyBirth());
		calendar.add(Calendar.MONTH, 1);
		apiMotherhoodUser.setExpirationDate(calendar.getTime());

		return apiMotherhoodUserMapper.updateByPrimaryKey(apiMotherhoodUser);
	}
}
