package com.semioe.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.vo.SourceVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.service.SourceService;
import com.semioe.util.MapToObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SourceServiceImpl implements SourceService {

	private static final Logger logger = LoggerFactory.getLogger(SourceServiceImpl.class);

	private static final String X_CURRENT_PAGE = "X-Pagination-Current-Page";
	private static final String X_PAGE_COUNT = "X-Pagination-Page-Count";
	private static final String X_TOTAL_COUNT = "X-Pagination-Total-Count";
	private static final String X_PER_PAGE = "X-Pagination-Per-Page";
	public static ResultCode MysqlWrong = new ResultCode("MYSQL_WRONG", "操作数据库出错");
	public static ResultCode SourceFail = new ResultCode("SOURCE_FAIL", "调用素材系统失败");

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Result getSourceInfoListPage(String url, String type, String token, String pageNumber,
			String pageSize) {
		return sourceSearch(url, token, type, pageNumber, pageSize);
	}

	/**
	 * 删除服务信息
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result sourceInfoRemove(String url, String token, String id) {
		return sourceDelete(url, token, id);
	}

	/**
	 * sourceSearch 查询素材
	 * 
	 * @param url
	 * @param token
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Result sourceSearch(String url, String token, String type, String page,
			String pageSize) {
		int pageNumber = Integer.parseInt(page);
		int totalItemsCount = 0;
		int pagesCount = 0;
		PaginatedResult<SourceVO> pa = null;
		BufferedReader in = null;
		if (StringUtil.isEmpty(url)) {
			return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "知识系统url为空"));
		}
		url = url + "search" + "?page=" + page + "&filemime=" + type + "&status=2" + "&per-page="
				+ pageSize;
		List<SourceVO> sourceList = new ArrayList<SourceVO>();
		try {
			URL urlObj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
			con.setRequestMethod("GET"); // 以Post方式提交表单，默认get方式
			con.setDoInput(true);
			con.setDoOutput(true);
			// 设置请求头信息
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Authorization",
					"Basic " + MapToObject.byteToBase64(token + ":"));
			con.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 获取所有响应头字段
			Map<String, List<String>> map = con.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				if (key == null || key.equals(X_CURRENT_PAGE)) {
					continue;
				} else if (key.equals(X_PAGE_COUNT)) {
					pagesCount = Integer.parseInt(map.get(key).get(0));
				} else if (key.equals(X_TOTAL_COUNT)) {
					totalItemsCount = Integer.parseInt(map.get(key).get(0));
				}
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				JSONArray array_news = new JSONArray();
				array_news = JSONArray.fromObject(line);
				sourceList = (List<SourceVO>) JSONArray.toCollection(array_news, SourceVO.class);
			}
			pa = new PaginatedResult<SourceVO>(sourceList, pageNumber, Integer.parseInt(pageSize),
					totalItemsCount);
			pa.setResultCode(new ResultCode("SUCCESS", "查询成功！"));
		} catch (Exception e) {
			logger.info("发送GET请求出现异常！" + e);
			pa = new PaginatedResult<SourceVO>(pageNumber, Integer.parseInt(pageSize));
			pa.setSuccessful(false);
			pa.setResultCode(SourceFail);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return pa;
	}

	/**
	 * sourceSearch 删除素材
	 * 
	 * @param url
	 * @param token
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Result sourceDelete(String url, String token, String id) {
		Result result = new Result<>(StatusCodes.OK, true);
		BufferedReader in = null;
		if (StringUtil.isEmpty(url)) {
			return new Result(StatusCodes.OK, false, new ResultCode("FAIL", "知识系统url为空"));
		}
		url = url + id;
		try {
			URL urlObj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
			con.setRequestMethod("PUT");
			con.setDoInput(true);
			con.setDoOutput(true);
			// 设置请求头信息
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Authorization",
					"Basic " + MapToObject.byteToBase64(token + ":"));
			con.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 获取所有响应头字段
			con.setRequestProperty("Content-Type",
					"multipart/x-www-form-urlencoded;status=" + "-1");
			// 获取所有的响应头字段
			// Map<String, List<String>> map = con.getHeaderFields();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			SourceVO source = null;
			while ((line = in.readLine()) != null) {
				JSONObject jsonobject = JSONObject.fromObject(line);
				source = (SourceVO) JSONObject.toBean(jsonobject, SourceVO.class);
			}
			if (source != null) {
				source.setStatus("-1");
			}
			result.setData(source);
			result.setResultCode(new ResultCode("SUCCESS", "删除素材成功！"));
		} catch (Exception e) {
			logger.info("发送GET请求出现异常！" + e);
			result = new Result(StatusCodes.OK, false, SourceFail);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

}
