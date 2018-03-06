package com.semioe.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.semioe.api.entity.BackstageUserInfo;
import com.semioe.api.entity.DeviceBrands;
import com.semioe.api.entity.DeviceDefinition;
import com.semioe.api.entity.DeviceFirms;
import com.semioe.api.entity.DeviceInfo;
import com.semioe.api.entity.GoodsInfo;
import com.semioe.api.vo.ApiContractedUserVO;
import com.semioe.api.vo.ApiUserInfoVO;
import com.semioe.api.vo.BackstageUserInfoVO;
import com.semioe.api.vo.DeviceDefinitionVO;
import com.semioe.api.vo.GoodsInfoVO;
import com.semioe.api.vo.ServiceInfoVO;
import com.semioe.api.vo.UserCountVO;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.service.ApiUserInfoService;
import com.semioe.dubbo.service.BackstageUserInfoService;
import com.semioe.dubbo.service.DeviceBrandsService;
import com.semioe.dubbo.service.DeviceDefinitionService;
import com.semioe.dubbo.service.DeviceFirmsService;
import com.semioe.dubbo.service.DeviceInfoService;
import com.semioe.dubbo.service.GoodsInfoService;
import com.semioe.dubbo.service.ManagerService;
import com.semioe.dubbo.service.MessageService;
import com.semioe.dubbo.service.OperationDataService;
import com.semioe.dubbo.service.ServiceService;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Controller
@RequestMapping("/excelExport")
public class ExcelExportController {

	private static final Logger logger = LoggerFactory.getLogger(ExcelExportController.class);

	@Reference
	private BackstageUserInfoService backstageUserInfoService;

	@Reference
	private ManagerService managerService;
	@Reference
	private MessageService messageService;

	@Reference
	private ApiUserInfoService apiUserInfoService;
	@Reference
	private BackstageUserInfoService backUserService;
	@Reference
	private DeviceInfoService deviceInfoService;
	@Reference
	private DeviceBrandsService deviceBrandsService;
	@Reference
	private DeviceFirmsService deviceFirmsService;
	@Reference
	private DeviceDefinitionService deviceDefinitionService;
	@Reference
	private ServiceService serviceService;
	@Reference
	private GoodsInfoService goodsInfoService;
	@Reference
	private OperationDataService operationService;

	/**
	 * exportBackstageUser 用户管理导出
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param roleId
	 * @param userStatus
	 * @param inUse
	 * @param pageSize
	 *            每页条数
	 * @param pageNumber
	 *            页数
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/backstageUser")
	public Result exportBackstageUser(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "managerId", required = false) String managerId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "roleId", required = false) String roleId,
			@RequestParam(value = "userStatus", required = false) String userStatus,
			@RequestParam(value = "inUse", required = false) String inUse,
			@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {
		logger.info("ExcelExportController.exportBackstageUser start,name={}", name);
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != name && !name.isEmpty()) {
			map.put("name", name + "%");
		}
		if (null != roleId && !roleId.isEmpty()) {
			map.put("roleId", roleId);
		}
		if (null != userStatus && !userStatus.isEmpty()) {
			map.put("userStatus", userStatus);
		}
		if (null != inUse && !inUse.isEmpty()) {
			map.put("inUse", inUse);
		}
		if (null != managerId && !managerId.isEmpty()) {
			map.put("managerId", managerId);
		}
		if (null != mobile && !mobile.isEmpty()) {
			map.put("mobile", mobile);
		}
		pageSize = "100000";
		pageNumber = "1";
		int size = Integer.parseInt(pageSize);
		int num = Integer.parseInt(pageNumber);
		map.put("pageSize", size);
		map.put("pageNumber", pageNumber);
		map.put("beginSize", (num - 1) * size);
		Result result = new Result<>(StatusCodes.OK, true);
		// 文件名
		String fileName = "USER_" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
				+ ".xls";
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名
		result = backstageUserInfoService.getAllBackstageUserInfo(map);
		List<BackstageUserInfoVO> users = (List<BackstageUserInfoVO>) ((Map) result.getData())
				.get("list");
		String[] cells = { "姓名", "手机号", "角色", "标签", "审核状态", "是否可用" };
		boolean b = creatExcel(response, "用户管理", cells, users, null, null, null, null, null, null);
		if (!b) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("FAIL", "生成excel文档失败"));
		}
		return result;
	}

	/**
	 * 设备管理
	 * 
	 * @param request
	 * @param response
	 * @param brandName
	 * @param brandId
	 * @param deviceType
	 * @param firmName
	 * @param firmId
	 * @param pageSize
	 * @param currentsPage
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/device")
	public Result ExportDevice(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "brandName", required = false) String brandName,
			@RequestParam(value = "brandId", required = false) String brandId,
			@RequestParam(value = "deviceType", required = false) String deviceType,
			@RequestParam(value = "firmName", required = false) String firmName,
			@RequestParam(value = "firmId", required = false) String firmId,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "currentsPage", required = false) String currentsPage) {
		DeviceInfo deviceInfo = new DeviceInfo();
		pageSize = "100000";
		currentsPage = "1";
		deviceInfo.setShowCount(Integer.parseInt(pageSize));
		int currentPage = Integer.parseInt(currentsPage);
		if (currentPage <= 0) {
			currentPage = 1;
		}
		int currentResult = (currentPage - 1) * Integer.parseInt(pageSize);
		deviceInfo.setCurrentPage(currentPage);
		deviceInfo.setCurrentResult(currentResult);
		// 厂商id 坑没法了 name就是传的厂商id
		if (!StringUtil.isEmpty(firmName)) {
			deviceInfo.setFirmId(Integer.parseInt(firmName));
		}
		if (!StringUtil.isEmpty(firmId)) {
			deviceInfo.setFirmId(Integer.parseInt(firmId));
		}
		// 品牌id 同上
		if (!StringUtil.isEmpty(brandName)) {
			deviceInfo.setBrandId(Integer.parseInt(brandName));
		}
		if (!StringUtil.isEmpty(brandId)) {
			deviceInfo.setBrandId(Integer.parseInt(brandId));
		}
		// 设备类型 即 定义id
		if (!StringUtil.isEmpty(deviceType)) {
			deviceInfo.setDefinitionId(Integer.parseInt(deviceType));
		}
		PaginatedResult<DeviceInfo> pa = (PaginatedResult) deviceInfoService
				.selectDeviceInfoListPage(deviceInfo);
		for (DeviceInfo device : pa.getItems()) {
			if (!StringUtil.isEmpty(device.getManagerId())) {
				Result resultUser = backUserService.getOneUser(device.getManagerId());
				BackstageUserInfo backUser = (BackstageUserInfo) resultUser.getData();
				if (null != backUser) {
					device.setUserMobile(backUser.getMobile());
					device.setUserName(backUser.getName());
					device.setDeviceLocation(backUser.getAddress());
				}
			}
		}
		List<DeviceInfo> listDeviceInfos = pa.getItems();
		Result result = new Result<>(StatusCodes.OK, true);
		// 文件名
		String fileName = "DEVICE_" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
				+ ".xls";
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名
		String[] cells = { "设备名称", "设备描述", "厂商", "品牌", "设备类别", "设备库存", "机械号", "设备租金", "设备投放",
				"是否上架", "是否租赁" };
		boolean b = creatExcel(response, "设备管理", cells, null, listDeviceInfos, null, null, null,
				null, null);
		if (!b) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("FAIL", "生成excel文档失败"));
		}
		return result;
	}

	/**
	 * 品牌管理导出
	 * 
	 * @param request
	 * @param response
	 * @param brandName
	 * @param firmName
	 * @param firmId
	 * @param pageSize
	 * @param currentsPage
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/brands")
	public Result exportBrands(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "brandName", required = false) String brandName,
			@RequestParam(value = "firmName", required = false) String firmName,
			@RequestParam(value = "firmId", required = false) Integer firmId,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "currentsPage", required = false) String currentsPage) {
		pageSize = "100000";
		currentsPage = "1";
		DeviceBrands deviceBrands = new DeviceBrands();
		deviceBrands.setShowCount(Integer.parseInt(pageSize));
		int currentResult = (Integer.parseInt(currentsPage) - 1) * Integer.parseInt(pageSize);
		deviceBrands.setCurrentPage(Integer.parseInt(currentsPage));
		deviceBrands.setCurrentResult(currentResult);
		if (brandName != null) {
			deviceBrands.setBrandName(brandName);
		}
		if (firmName != null) {
			deviceBrands.setFirmName(firmName);
		}
		if (firmId != null) {
			deviceBrands.setDeviceFirmid(firmId);
		}
		PaginatedResult<DeviceBrands> pa = (PaginatedResult<DeviceBrands>) deviceBrandsService
				.selectDeviceBrandsListPage(deviceBrands);
		List<DeviceBrands> brands = pa.getItems();
		Result result = new Result<>(StatusCodes.OK, true);
		// 文件名
		String fileName = "BRAND_" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
				+ ".xls";
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名
		String[] cells = { "品牌名称", "品牌描述", "厂商", "是否删除" };
		boolean b = creatExcel(response, "品牌管理", cells, null, null, brands, null, null, null, null);
		if (!b) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("FAIL", "生成excel文档失败"));
		}
		return result;
	}

	/**
	 * 厂商导出
	 * 
	 * @param request
	 * @param response
	 * @param firmName
	 * @param pageSize
	 * @param currentsPage
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/firm")
	public Result exportFirm(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "firmName", required = false) String firmName,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "currentsPage", required = false) String currentsPage) {
		DeviceFirms deviceFirms = new DeviceFirms();
		pageSize = "100000";
		currentsPage = "1";
		deviceFirms.setShowCount(Integer.parseInt(pageSize));
		int currentResult = (Integer.parseInt(currentsPage) - 1) * Integer.parseInt(pageSize);
		deviceFirms.setCurrentPage(Integer.parseInt(currentsPage));
		deviceFirms.setCurrentResult(currentResult);
		deviceFirms.setFirmName(firmName);
		PaginatedResult<DeviceFirms> pa = (PaginatedResult<DeviceFirms>) deviceFirmsService
				.selectDeviceFirmsListPage(deviceFirms);
		List<DeviceFirms> firms = pa.getItems();
		Result result = new Result<>(StatusCodes.OK, true);
		// 文件名
		String fileName = "FIRM_" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
				+ ".xls";
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名
		String[] cells = { "厂商名称", "厂商描述", "厂商地址", "厂商邮编", "厂商联系人", "厂商联系方式", "本部联系厂商负责人",
				"本部联系厂商负责人联系方式", "是否删除" };
		boolean b = creatExcel(response, "厂商管理", cells, null, null, null, firms, null, null, null);
		if (!b) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("FAIL", "生成excel文档失败"));
		}
		return result;
	}

	/**
	 * 设备定义导出
	 * 
	 * @param request
	 * @param response
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/deviceDefinition")
	public Result exportDeviceDefinition(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "definitionName", required = false) String definitionName,
			@RequestParam(value = "definitionCode", required = false) String definitionCode,
			@RequestParam(value = "definitionSpec", required = false) String definitionSpec,
			@RequestParam(value = "deviceFirmid", required = false) String deviceFirmid,
			@RequestParam(value = "type", required = false) String type) {
		DeviceDefinition entity = new DeviceDefinition();
		if (!StringUtil.isEmpty(definitionName)) {
			entity.setDefinitionName(definitionName);
		}
		if (!StringUtil.isEmpty(definitionCode)) {
			entity.setDefinitionCode(definitionCode);
		}
		if (!StringUtil.isEmpty(definitionSpec)) {
			entity.setDefinitionSpec(definitionSpec);
		}
		if (!StringUtil.isEmpty(deviceFirmid)) {
			entity.setDeviceFirmid(Integer.parseInt(deviceFirmid));
		}
		if (!StringUtil.isEmpty(type)) {
			entity.setType(Integer.parseInt(type));
		}
		PaginatedResult<DeviceDefinitionVO> pa = (PaginatedResult<DeviceDefinitionVO>) deviceDefinitionService
				.getDeviceDefinitionList(entity);
		List<DeviceDefinitionVO> devinitions = pa.getItems();
		Result result = new Result<>(StatusCodes.OK, true);
		// 文件名
		String fileName = "DEVICE_DEFINITION_"
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ".xls";
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名
		String[] cells = { "设备名称", "设备编码", "品牌", "厂商", "是否上架" };
		boolean b = creatExcel(response, "设备定义", cells, null, null, null, null, devinitions, null,
				null);
		if (!b) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("FAIL", "生成excel文档失败"));
		}
		return result;
	}

	/**
	 * 服务审核导出
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param status
	 * @param createName
	 * @param pageNumber
	 * @param pageSize
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/service")
	public Result getServiceInfoByType(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "createName", required = false) String createName,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "id", required = false) String id) {
		ServiceInfoVO serviceVO = new ServiceInfoVO();
		if (name != null && !name.isEmpty()) {
			serviceVO.setName(name + "%");
		}
		if (id != null && !id.isEmpty()) {
			serviceVO.setCreateUserId(id);
		}
		if (status != null && !status.isEmpty()) {
			List<Integer> listStatus = new ArrayList<Integer>();
			for (String s : status.split(",")) {
				listStatus.add(Integer.parseInt(s));
			}
			serviceVO.setStatusList(listStatus);
		}
		if (createName != null && !createName.isEmpty()) {
			serviceVO.setCreateName(createName + "%");
		}
		pageSize = "100000";
		pageNumber = "1";
		int currentPage = Integer.parseInt(pageNumber);
		if (currentPage <= 0) {
			currentPage = 1;
		}
		int currentResult = (currentPage - 1) * Integer.parseInt(pageSize);
		serviceVO.setShowCount(Integer.parseInt(pageSize));
		serviceVO.setCurrentResult(currentResult);
		serviceVO.setCurrentPage(currentPage);
		serviceVO.setInUse(1);
		PaginatedResult<ServiceInfoVO> pa = (PaginatedResult<ServiceInfoVO>) serviceService
				.getServiceInfoListPage(serviceVO);
		List<ServiceInfoVO> services = pa.getItems();
		Result result = new Result<>(StatusCodes.OK, true);
		// 文件名
		String fileName = "SERVICE_"
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ".xls";
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名
		String[] cells = { "服务名称", "创建单位", "服务描述", "标签", "审核意见" };
		boolean b = creatExcel(response, "服务审核", cells, null, null, null, null, null, services,
				null);
		if (!b) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("FAIL", "生成excel文档失败"));
		}
		return result;
	}

	/**
	 * 商品管理导出
	 * 
	 * @param request
	 * @param response
	 * @param goodsInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/goods")
	public Result getGoodsInfoArray(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "goodsName", required = false) String goodsName,
			@RequestParam(value = "supplierName", required = false) String supplierName,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "keywordsNames", required = false) String keywordsNames) {
		GoodsInfo goodsInfo = new GoodsInfo();
		if (!StringUtil.isEmpty(goodsName)) {
			goodsInfo.setGoodsName(goodsName);
		}
		if (!StringUtil.isEmpty(supplierName)) {
			goodsInfo.setSupplierName(supplierName);
		}
		if (!StringUtil.isEmpty(type)) {
			goodsInfo.setType(Integer.parseInt(type));
		}
		if (!StringUtil.isEmpty(keywordsNames)) {
			goodsInfo.setKeywordsNames(keywordsNames);
		}
		goodsInfo.setShowCount(1000000);
		logger.info("GoodsInfoController.getGoodsInfoArray start, select");
		PaginatedResult<GoodsInfoVO> pa = goodsInfoService.getGoodsInfoArray(goodsInfo);
		List<GoodsInfoVO> goods = pa.getItems();
		Result result = new Result<>(StatusCodes.OK, true);
		// 文件名
		String fileName = "GOODS_" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
				+ ".xls";
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名
		String[] cells = { "商品名称", "商品标签", "单价", "库存", "供应商" };
		boolean b = creatExcel(response, "商品管理", cells, null, null, null, null, null, null, goods);
		if (!b) {
			return new Result<>(StatusCodes.OK, false, new ResultCode("FAIL", "生成excel文档失败"));
		}
		return result;
	}

	/**
	 * creatExcel 导出excel
	 * 
	 * @param response
	 * @param sheetName
	 *            sheet页名称
	 * @param cells
	 *            表单首行
	 * @param listUsers
	 *            用户数据
	 * @param listDeviceInfos
	 *            设备数据
	 * @param brands
	 *            品牌数据
	 * @param firms
	 *            厂商管理
	 * @param definitiona
	 *            设备定时
	 * @param services
	 *            服务审核
	 * @param goods
	 *            商品数据
	 * @return
	 */
	public boolean creatExcel(HttpServletResponse response, String sheetName, String[] cells,
			List<BackstageUserInfoVO> listUsers, List<DeviceInfo> listDeviceInfos,
			List<DeviceBrands> brands, List<DeviceFirms> firms,
			List<DeviceDefinitionVO> definitiona, List<ServiceInfoVO> services,
			List<GoodsInfoVO> goods) {
		boolean b = false;
		try {
			// 1.创建excel文件
			WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
			// 居中样式
			WritableCellFormat wf = new WritableCellFormat();
			wf.setAlignment(Alignment.CENTRE);
			WritableSheet sheet = book.createSheet(sheetName, 0);
			// 2.添加表头数据
			for (int i = 0; i < cells.length; i++) {
				sheet.addCell(new Label(i, 0, cells[i], wf));
			}
			// 用户管理 "姓名", "手机号", "角色","标签", "审核状态", "是否可用"
			if (null != listUsers && listUsers.size() > 0) {
				for (int i = 0; i < listUsers.size(); i++) {
					BackstageUserInfoVO user = listUsers.get(i);
					if (user != null && user.getName() != null) {
						sheet.addCell(new Label(0, i + 1, user.getName() + "", wf));
					} else {
						sheet.addCell(new Label(0, i + 1, "", wf));
					}
					if (user != null && user.getMobile() != null) {
						sheet.addCell(new Label(1, i + 1, user.getMobile() + "", wf));
					} else {
						sheet.addCell(new Label(1, i + 1, "", wf));
					}
					if (user != null && user.getRoleName() != null) {
						sheet.addCell(new Label(2, i + 1, user.getRoleName() + "", wf));
					} else {
						sheet.addCell(new Label(2, i + 1, "", wf));
					}
					if (user != null && user.getTagNames() != null) {
						sheet.addCell(new Label(3, i + 1, user.getTagNames() + "", wf));
					} else {
						sheet.addCell(new Label(3, i + 1, "", wf));
					}
					if (user != null && user.getUserStatus() != null) {
						String status = "";
						if (user.getUserStatus() == 0) { // 0.未完善，1.审核通过，2.待审核，3.驳回申请，4.禁用
							status = "未完善";
						} else if (user.getUserStatus() == 1) {
							status = "审核通过";
						} else if (user.getUserStatus() == 2) {
							status = "待审核";
						} else if (user.getUserStatus() == 3) {
							status = "驳回申请";
						} else {
							status = "禁用";
						}
						sheet.addCell(new Label(4, i + 1, status + "", wf));
					} else {
						sheet.addCell(new Label(4, i + 1, "", wf));
					}
					if (user != null && user.getInUse() != null) {
						if (user.getInUse() == 1) {
							sheet.addCell(new Label(5, i + 1, "可用" + "", wf));
						} else {
							sheet.addCell(new Label(5, i + 1, "不可用" + "", wf));
						}
					} else {
						sheet.addCell(new Label(5, i + 1, "", wf));
					}
				}
			}
			// 设备管理 "设备名称", "设备描述", "厂商", "品牌", "设备类别", "设备库存", "机械号", "设备租金",
			// "设备投放","是否上架", "是否租赁"
			if (null != listDeviceInfos && listDeviceInfos.size() > 0) {
				for (int i = 0; i < listDeviceInfos.size(); i++) {
					DeviceInfo device = listDeviceInfos.get(i);
					if (device != null && device.getDeviceName() != null) {
						sheet.addCell(new Label(0, i + 1, device.getDeviceName() + "", wf));
					} else {
						sheet.addCell(new Label(0, i + 1, "", wf));
					}
					if (device != null && device.getDeviceDesc() != null) {
						sheet.addCell(new Label(1, i + 1, device.getDeviceDesc() + "", wf));
					} else {
						sheet.addCell(new Label(1, i + 1, "", wf));
					}
					if (device != null && device.getFirmName() != null) {
						sheet.addCell(new Label(2, i + 1, device.getFirmName() + "", wf));
					} else {
						sheet.addCell(new Label(2, i + 1, "", wf));
					}
					if (device != null && device.getBrandName() != null) {
						sheet.addCell(new Label(3, i + 1, device.getBrandName() + "", wf));
					} else {
						sheet.addCell(new Label(3, i + 1, "", wf));
					}
					if (device != null && device.getDefinitionName() != null) {
						sheet.addCell(new Label(4, i + 1, device.getDefinitionName() + "", wf));
					} else {
						sheet.addCell(new Label(4, i + 1, "", wf));
					}
					// 设备库存
					if (device.getDeviceStock() != null && device.getDeviceStock() > 0) {
						sheet.addCell(new Label(5, i + 1, device.getDeviceStock() + "", wf));
					} else {
						sheet.addCell(new Label(5, i + 1, "0", wf));
					}
					if (device != null && device.getDeviceCode() != null) {
						sheet.addCell(new Label(6, i + 1, device.getDeviceCode() + "", wf));
					} else {
						sheet.addCell(new Label(6, i + 1, "", wf));
					}
					if (device.getDeviceRental() != null && device.getDeviceRental() > 0) {
						sheet.addCell(new Label(7, i + 1, device.getDeviceRental() + "", wf));
					} else {
						sheet.addCell(new Label(7, i + 1, "0", wf));
					}
					String deviceLocation = "";
					if (device.getUserMobile() != null) {
						deviceLocation = deviceLocation + device.getUserMobile();
					}
					if (device.getUserName() != null) {
						deviceLocation = deviceLocation + " " + device.getUserName();
					}
					if (device.getDeviceLocation() != null) {
						deviceLocation = deviceLocation + " " + device.getDeviceLocation();
					}
					sheet.addCell(new Label(8, i + 1, deviceLocation, wf));
					if (device != null && device.getIsPutaway() != null) {// T：已上架
																			// F：未上架
						if (device.getIsPutaway().equals("T")) {
							sheet.addCell(new Label(9, i + 1, "已上架", wf));
						} else {
							sheet.addCell(new Label(9, i + 1, "未上架", wf));
						}
					} else {
						sheet.addCell(new Label(9, i + 1, "未上架", wf));
					}
					if (device != null && device.getIsHired() != null) { // T：已租赁
																			// F：
						if (device.getIsHired().equals("T")) {
							sheet.addCell(new Label(10, i + 1, "已租赁", wf));
						} else {
							sheet.addCell(new Label(10, i + 1, "未租赁", wf));
						}
					} else {
						sheet.addCell(new Label(10, i + 1, "未租赁", wf));
					}
				}
			}
			// 品牌管理 "品牌名称", "品牌描述", "厂商", "是否删除"
			if (null != brands && brands.size() > 0) {
				for (int i = 0; i < brands.size(); i++) {
					DeviceBrands brand = brands.get(i);
					if (brand != null && brand.getBrandName() != null) {
						sheet.addCell(new Label(0, i + 1, brand.getBrandName() + "", wf));
					} else {
						sheet.addCell(new Label(0, i + 1, "", wf));
					}
					if (brand != null && brand.getBrandDesc() != null) {
						sheet.addCell(new Label(1, i + 1, brand.getBrandDesc() + "", wf));
					} else {
						sheet.addCell(new Label(1, i + 1, "", wf));
					}
					if (brand != null && brand.getFirmName() != null) {
						sheet.addCell(new Label(2, i + 1, brand.getFirmName() + "", wf));
					} else {
						sheet.addCell(new Label(2, i + 1, "", wf));
					}
					if (brand != null && brand.getIsdel() != null) {// F 否 T 是
						if (brand.getIsdel().equals("T")) {
							sheet.addCell(new Label(3, i + 1, "已删除", wf));
						} else {
							sheet.addCell(new Label(3, i + 1, "未删除", wf));
						}
					} else {
						sheet.addCell(new Label(3, i + 1, "", wf));
					}
				}
			}
			// 厂商管理 "厂商名称", "厂商描述", "厂商地址", "厂商邮编", "厂商联系人", "厂商联系方式",
			// "本部联系厂商负责人",
			// "本部联系厂商负责人联系方式", "是否删除" }
			if (null != firms && firms.size() > 0) {
				for (int i = 0; i < firms.size(); i++) {
					DeviceFirms firm = firms.get(i);
					if (firm != null && firm.getFirmName() != null) {
						sheet.addCell(new Label(0, i + 1, firm.getFirmName() + "", wf));
					} else {
						sheet.addCell(new Label(0, i + 1, "", wf));
					}
					if (firm != null && firm.getFirmDesc() != null) {
						sheet.addCell(new Label(1, i + 1, firm.getFirmDesc() + "", wf));
					} else {
						sheet.addCell(new Label(1, i + 1, "", wf));
					}
					if (firm != null && firm.getFirmAdress() != null) {
						sheet.addCell(new Label(2, i + 1, firm.getFirmAdress() + "", wf));
					} else {
						sheet.addCell(new Label(2, i + 1, "", wf));
					}
					if (firm != null && firm.getFirmPostcode() != null) {
						sheet.addCell(new Label(3, i + 1, firm.getFirmPostcode() + "", wf));
					} else {
						sheet.addCell(new Label(3, i + 1, "", wf));
					}
					if (firm != null && firm.getFirmContact() != null) {
						sheet.addCell(new Label(4, i + 1, firm.getFirmContact() + "", wf));
					} else {
						sheet.addCell(new Label(4, i + 1, "", wf));
					}
					if (firm != null && firm.getFirmPhone() != null) {
						sheet.addCell(new Label(5, i + 1, firm.getFirmPhone() + "", wf));
					} else {
						sheet.addCell(new Label(5, i + 1, "", wf));
					}
					if (firm != null && firm.getFirmResponsible() != null) {
						sheet.addCell(new Label(6, i + 1, firm.getFirmResponsible() + "", wf));
					} else {
						sheet.addCell(new Label(6, i + 1, "", wf));
					}
					if (firm != null && firm.getFirmResponsiblePhone() != null) {
						sheet.addCell(new Label(7, i + 1, firm.getFirmResponsiblePhone() + "", wf));
					} else {
						sheet.addCell(new Label(7, i + 1, "", wf));
					}
					if (firm != null && firm.getIsdel() != null) {
						if (firm.getIsdel().equals("T")) {
							sheet.addCell(new Label(8, i + 1, "已删除", wf));
						} else {
							sheet.addCell(new Label(8, i + 1, "未删除", wf));
						}
					} else {
						sheet.addCell(new Label(8, i + 1, "", wf));
					}
				}
			}
			// 设备定义 "设备名称", "设备编码", "品牌", "厂商", "是否上架"
			if (null != definitiona && definitiona.size() > 0) {
				for (int i = 0; i < definitiona.size(); i++) {
					DeviceDefinitionVO definition = definitiona.get(i);
					if (definition != null && definition.getDefinitionName() != null) {
						sheet.addCell(new Label(0, i + 1, definition.getDefinitionName() + "", wf));
					} else {
						sheet.addCell(new Label(0, i + 1, "", wf));
					}
					if (definition != null && definition.getDefinitionCode() != null) {
						sheet.addCell(new Label(1, i + 1, definition.getDefinitionCode() + "", wf));
					} else {
						sheet.addCell(new Label(1, i + 1, "", wf));
					}
					if (definition != null && definition.getDeviceBrandName() != null) {
						sheet.addCell(
								new Label(2, i + 1, definition.getDeviceBrandName() + "", wf));
					} else {
						sheet.addCell(new Label(2, i + 1, "", wf));
					}
					if (definition != null && definition.getDeviceFirmName() != null) {
						sheet.addCell(new Label(3, i + 1, definition.getDeviceFirmName() + "", wf));
					} else {
						sheet.addCell(new Label(3, i + 1, "", wf));
					}
					if (definition != null && definition.getType() != null) {// 0：下架，1：上架
						if (definition.getType() == 1) {
							sheet.addCell(new Label(4, i + 1, "已上架", wf));
						} else {
							sheet.addCell(new Label(4, i + 1, "已下架", wf));
						}
					} else {
						sheet.addCell(new Label(4, i + 1, "已下架", wf));
					}
				}
			}
			// 服务审核 "服务名称", "创建单位", "服务描述", "标签", "审核意见"
			if (null != services && services.size() > 0) {
				for (int i = 0; i < services.size(); i++) {
					ServiceInfoVO service = services.get(i);
					if (service != null && service.getName() != null) {
						sheet.addCell(new Label(0, i + 1, service.getName() + "", wf));
					} else {
						sheet.addCell(new Label(0, i + 1, "", wf));
					}
					if (service != null && service.getCreateName() != null) {
						sheet.addCell(new Label(1, i + 1, service.getCreateName() + "", wf));
					} else {
						sheet.addCell(new Label(1, i + 1, "", wf));
					}
					if (service != null && service.getDescription() != null) {
						sheet.addCell(new Label(2, i + 1, service.getDescription() + "", wf));
					} else {
						sheet.addCell(new Label(2, i + 1, "", wf));
					}
					if (service != null && service.getKeywordsName() != null) {
						sheet.addCell(new Label(3, i + 1, service.getKeywordsName() + "", wf));
					} else {
						sheet.addCell(new Label(3, i + 1, "", wf));
					}
					if (service != null && service.getOpinion() != null) {
						sheet.addCell(new Label(4, i + 1, service.getOpinion() + "", wf));
					} else {
						sheet.addCell(new Label(4, i + 1, "", wf));
					}
				}
			}
			// 商品管理 "商品名称", "商品标签", "单价", "库存", "供应商"
			if (null != goods && goods.size() > 0) {
				for (int i = 0; i < goods.size(); i++) {
					GoodsInfoVO good = goods.get(i);
					if (good != null && good.getGoodsName() != null) {
						sheet.addCell(new Label(0, i + 1, good.getGoodsName() + "", wf));
					} else {
						sheet.addCell(new Label(0, i + 1, "", wf));
					}
					if (good != null && good.getKeywordsNames() != null) {
						sheet.addCell(new Label(1, i + 1, good.getKeywordsNames() + "", wf));
					} else {
						sheet.addCell(new Label(1, i + 1, "", wf));
					}
					if (good != null && good.getPrice() != null) {
						sheet.addCell(new Label(2, i + 1, good.getPrice() + "", wf));
					} else {
						sheet.addCell(new Label(2, i + 1, "0", wf));
					}
					if (good != null && good.getStock() != null) {
						sheet.addCell(new Label(3, i + 1, good.getStock() + "", wf));
					} else {
						sheet.addCell(new Label(3, i + 1, "0", wf));
					}
					if (good != null && good.getSupplierName() != null) {
						sheet.addCell(new Label(4, i + 1, good.getSupplierName() + "", wf));
					} else {
						sheet.addCell(new Label(4, i + 1, "", wf));
					}
				}

			}
			book.write();
			book.close();
			b = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return b;
		}
		return b;
	}
	
	
	/**
	 * 签约统计导出
	 * @param userInfo
	 * @return
	 */
	@RequestMapping("/sign")
	@ResponseBody
	public void signExport(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "buildType", required = false) String buildType,
			HttpServletResponse response) {
		logger.info("OperationDataController.signExport start, userInfo = {}",
				buildType.toString());
		ApiUserInfoVO userInfo  = new ApiUserInfoVO();
		
		userInfo.setShowCount(1000);
		userInfo.setCurrentPage(1);
		if (!StringUtil.isEmpty(startDate)) {
			userInfo.setStartDate(startDate);
		}
		if (!StringUtil.isEmpty(endDate)) {
			userInfo.setEndDate(endDate);
		}
		if (!StringUtil.isEmpty(buildType)) {
			userInfo.setBuildType(buildType);
		}
		PaginatedResult<UserCountVO> pa = operationService.signCount(userInfo);
		List<UserCountVO> itemList = pa.getItems();

		CellView navCellView = new CellView();
		navCellView.setAutosize(true); // 设置自动大小
		// 文件名
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "SIGN_COUNT_" + sdf.format(new Date()) + ".xls";
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名

		try {
			// 1.创建excel文件
			WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
			// 居中样式
			WritableCellFormat wf = new WritableCellFormat();
			wf.setAlignment(Alignment.CENTRE);

			WritableSheet sheet = book.createSheet("签约统计", 0);
			// 2.添加表头数据
			sheet.addCell(new Label(0, 0, "日期", wf));
			sheet.addCell(new Label(1, 0, "签约数", wf));
			int count=0;
			for (int i = 0; i < itemList.size(); i++) {
				sheet.addCell(new Label(0, i + 1, itemList.get(i).getCountDate()+ "", wf));
				count = count + itemList.get(i).getSignCount().intValue();
				sheet.addCell(new Label(1, i + 1, itemList.get(i).getSignCount().intValue() + "", wf));
			}
			sheet.addCell(new Label(0, itemList.size() + 1, "合计", wf));
			sheet.addCell(new Label(1, itemList.size() + 1, count + "", wf));
			// 4.写入excel并关闭
			book.write();
			book.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * 签约统计 查看明细 导出
	 * @param userInfo
	 * @return
	 */
	@RequestMapping("/signDetaile")
	@ResponseBody
	public void signDetaile(
			@RequestParam(value = "countDate", required = false) String countDate,
			@RequestParam(value = "buildType", required = false) String buildType,
			HttpServletResponse response) {
		logger.info("OperationDataController.signDetaile start, userInfo = {}",
				countDate.toString());
		ApiUserInfoVO userInfo  = new ApiUserInfoVO();
		
		userInfo.setShowCount(1000);
		userInfo.setCurrentPage(1);
		userInfo.setBuildType(buildType);
		userInfo.setCountDate(countDate);
		PaginatedResult<ApiContractedUserVO> pa =  operationService.signDetaile(userInfo);
		
		List<ApiContractedUserVO> itemList = pa.getItems();

		CellView navCellView = new CellView();
		navCellView.setAutosize(true); // 设置自动大小
		// 文件名
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fileName = "SIGN_DETAIL_" + sdf.format(new Date()) + ".xls";
		response.setContentType("application/x-excel");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// excel文件名

		try {
			// 1.创建excel文件
			WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
			// 居中样式
			WritableCellFormat wf = new WritableCellFormat();
			wf.setAlignment(Alignment.CENTRE);

			WritableSheet sheet = book.createSheet("签约统计", 0);
			// 2.添加表头数据
			sheet.addCell(new Label(0, 0, "签约帐号", wf));
			sheet.addCell(new Label(1, 0, "签约人", wf));
			sheet.addCell(new Label(2, 0, "身份证", wf));
			sheet.addCell(new Label(3, 0, "年龄", wf));
			sheet.addCell(new Label(4, 0, "性别", wf));
			sheet.addCell(new Label(5, 0, "居住地址", wf));
			sheet.addCell(new Label(6, 0, "疾病", wf));
			sheet.addCell(new Label(7, 0, "签约医生", wf));
			sheet.addCell(new Label(8, 0, "签约机构", wf));
			sheet.addCell(new Label(9, 0, "签约时间", wf));
			for (int i = 0; i < itemList.size(); i++) {
				sheet.addCell(new Label(0, i + 1, itemList.get(i).getMobile()+ "", wf));
				sheet.addCell(new Label(1, i + 1, itemList.get(i).getSignName() + "", wf));
				sheet.addCell(new Label(2, i + 1, itemList.get(i).getCardId() + "", wf));
				sheet.addCell(new Label(3, i + 1, itemList.get(i).getAge() + "", wf));
				if(itemList.get(i).getSex()==1) {
					sheet.addCell(new Label(4, i + 1, "男" + "", wf));
				}else if(itemList.get(i).getSex()==2) {
					sheet.addCell(new Label(4, i + 1, "女" + "", wf));
				}else {
					sheet.addCell(new Label(4, i + 1, "保密" + "", wf));
				}
				sheet.addCell(new Label(5, i + 1, itemList.get(i).getPresentAddress() + "", wf));
				sheet.addCell(new Label(6, i + 1, itemList.get(i).getDiseaseNames() + "", wf));
				sheet.addCell(new Label(7, i + 1, itemList.get(i).getDoctorName() + "", wf));
				sheet.addCell(new Label(8, i + 1, itemList.get(i).getOrgName() + "", wf));
				sheet.addCell(new Label(9, i + 1, new SimpleDateFormat("yyyy-MM-dd").format(itemList.get(i).getCreateTime())+ "", wf));
			}
			// 4.写入excel并关闭
			book.write();
			book.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

}
