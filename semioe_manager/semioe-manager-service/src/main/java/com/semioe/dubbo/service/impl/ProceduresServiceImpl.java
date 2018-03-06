package com.semioe.dubbo.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.common.tools.util.ConnUtil;
import com.semioe.common.tools.util.HttpClientUtil;
import com.semioe.dubbo.service.ProceduresService;
import com.semioe.util.MapToObject;

@Service
public class ProceduresServiceImpl implements ProceduresService {

	private static final Logger logger = LoggerFactory.getLogger(ProceduresServiceImpl.class);

	@Autowired
	private HttpClientUtil httpClientUtil;
	@Value("#{http['workflow-api.host']}")
	private String workflowAPI;

	@Override
	public PaginatedResult getProceduresByToken(String token, String title, String pageNumber,
			String pageSize) {
		logger.info("getProceduresByToken() start token={} title={} pageNumber={} pageSize={}",
				token, title, pageNumber, pageSize);
		PaginatedResult result = new PaginatedResult<>(0, 0);

		Map<String, String> header = new HashMap<String, String>();
		header.put("Authorization", "Basic " + MapToObject.byteToBase64(token + ":"));
		Map<String, String> param = new HashMap<String, String>();
		param.put("fields", "id,uid,title,created_at");
		param.put("ProcedureSearch[title]", title);
		if ("null".equals(pageNumber) || StringUtils.isEmpty(pageNumber))
			pageNumber = "1";
		param.put("page", pageNumber);
		if ("null".equals(pageSize) || StringUtils.isEmpty(pageSize))
			param.put("per-page", "50");
		else
			param.put("per-page", pageSize);

		try {
			String doGetReturnHeader = httpClientUtil.doGetReturnHeader(workflowAPI,
					"/v1/procedures", header, param, 60 * 1000);
			JSONObject pageInfo = JSONObject.parseObject(doGetReturnHeader);
			String doGet = httpClientUtil.doGet(workflowAPI, "/v1/procedures", header, param,
					60 * 1000);
			JSONArray dataArray = JSONArray.parseArray(doGet);

			// 提供方接口，分页大小只支持小于50，大于50的，多次请求，拼装返回结果
			int itemsCount = pageInfo.getInteger("X-Pagination-Total-Count");// 默认查询所有
			if (StringUtils.isNotEmpty(pageSize))
				itemsCount = Integer.parseInt(pageSize);
			else // 防止后续参数空指针，添加默认值
				pageSize = pageInfo.getString("X-Pagination-Total-Count");
			if (itemsCount > 50) {
				int pageCount = (int) Math.ceil((double) itemsCount / 50);
				for (int i = 2; i <= pageCount; i++) {
					param.put("page", String.valueOf(i));
					doGet = httpClientUtil.doGet(workflowAPI, "/v1/procedures", header, param,
							60 * 1000);
					dataArray.addAll(JSONArray.parseArray(doGet));
				}
			}

			result = new PaginatedResult<>(dataArray, Integer.parseInt(pageNumber),
					Integer.parseInt(pageSize), pageInfo.getInteger("X-Pagination-Total-Count"));
			result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} catch (Exception e) {
			logger.error("GET 请求失败，没有查询成功：{}", e);
			result.setSuccessful(false);
			result.setResultCode(new ResultCode("FAILED", "查询失败！"));
		}
		return result;
	}

	@Override
	public Result getProcedureDialog(String token, String userId, String doctorId, String orderId,
			String procedureId) {
		Result result = new Result<>(StatusCodes.OK, true);

		logger.info("调用流程列表：userId=" + userId + " doctorId=" + doctorId + " orderId=" + orderId
				+ " procedureId=" + procedureId);
		logger.info("传入token：" + token);

		Map<String, Object> header = new HashMap<String, Object>();
		header.put("Authorization", "Basic " + MapToObject.byteToBase64(token + ":"));
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("object_id", userId);
		param.put("doctor_id", doctorId);
		param.put("order_id", orderId);
		param.put("procedure_id", procedureId);

		try {
			String doPost = ConnUtil.sendPostStringWithHeader(workflowAPI + "/v1/procedure-dialog",
					header, param);
			logger.info("请求返回结果：" + doPost);
			JSONObject dataObject = JSONObject.parseObject(doPost);
			result.setData(dataObject);
			result.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} catch (Exception e) {
			logger.error("GET 请求失败，没有查询成功：{}", e);
			result.setSuccessful(false);
			result.setResultCode(new ResultCode("FAILED", "查询失败！"));
		}

		return result;
	}

}
