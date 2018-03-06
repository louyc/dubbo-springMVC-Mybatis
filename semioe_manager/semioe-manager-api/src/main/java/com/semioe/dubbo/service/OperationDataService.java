package com.semioe.dubbo.service;

import com.semioe.api.entity.ApiUserInfo;
import com.semioe.api.vo.ApiContractedUserVO;
import com.semioe.api.vo.ApiUserInfoVO;
import com.semioe.api.vo.QrcodeInfoVO;
import com.semioe.api.vo.UserCountVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;

public interface OperationDataService {

	/* 用户注册统计 */
	PaginatedResult<UserCountVO> countRegist(ApiUserInfoVO userInfo);

	PaginatedResult<ApiUserInfo> registDetaile(ApiUserInfoVO userInfo);

	/* 用户分析 */
	Result userAnalysis(ApiUserInfoVO userInfo);

	Result queryProvince(String id);

	/* 转换率统计 */
	PaginatedResult<UserCountVO> countconversion(ApiUserInfoVO userInfo);

	/* 推广码统计 */
	PaginatedResult<QrcodeInfoVO> countQrcode(QrcodeInfoVO qrcodeInfo);
	
	/* 签约统计 */
	PaginatedResult<UserCountVO> signCount(ApiUserInfoVO qrcodeInfo);
	/* 签约统计 查看明细 */
	PaginatedResult<ApiContractedUserVO> signDetaile(ApiUserInfoVO userInfo);
	/* 签约统计 分析*/
	Result signAnalysis(ApiUserInfoVO userInfo);
	/* 签约统计 家庭组明细*/
	Result familyDetail(UserCountVO userInfo);
	/* 家庭组统计*/
	Result familyAnalysis(ApiUserInfoVO userInfo);
}
