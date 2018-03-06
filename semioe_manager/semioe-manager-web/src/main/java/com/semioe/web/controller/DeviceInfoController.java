package com.semioe.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.ibm.icu.text.SimpleDateFormat;
import com.semioe.api.entity.BackstageUserInfo;
import com.semioe.api.entity.DeviceInfo;
import com.semioe.api.entity.DeviceUserRel;
import com.semioe.common.result.PaginatedResult;
import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.dubbo.service.BackstageUserInfoService;
import com.semioe.dubbo.service.DeviceBindingUserService;
import com.semioe.dubbo.service.DeviceBrandsService;
import com.semioe.dubbo.service.DeviceFirmsService;
import com.semioe.dubbo.service.DeviceInfoService;
import com.semioe.dubbo.service.DeviceThrowService;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Sheet;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/deviceInfo")
public class DeviceInfoController {

	@Reference
	private DeviceInfoService deviceInfoService;
	@Reference
	private DeviceFirmsService deviceFirmsService;
	@Reference
	private DeviceBrandsService deviceBrandsService;
	@Reference
	private DeviceBindingUserService deviceBindingService;
	@Reference
	private DeviceThrowService deviceThrowService;
	@Reference
	private BackstageUserInfoService backUserService;

	private static final Logger logger = LoggerFactory.getLogger(DeviceInfoController.class);

	@SuppressWarnings("rawtypes")
	@RequestMapping("/selectDeviceInfoListPage")
	@ResponseBody
	public Result selectDeviceInfoListPage(
			@RequestParam(value = "brandName", required = false) String brandName,
			@RequestParam(value = "brandId", required = false) String brandId,
			@RequestParam(value = "deviceType", required = false) String deviceType,
			@RequestParam(value = "firmName", required = false) String firmName,
			@RequestParam(value = "firmId", required = false) String firmId,
			@RequestParam(value = "pageSize", required = true) String pageSize,
			@RequestParam(value = "currentsPage", required = true) String currentsPage) {

		DeviceInfo deviceInfo = new DeviceInfo();

		if (pageSize != null && !pageSize.isEmpty()) {
			deviceInfo.setShowCount(Integer.parseInt(pageSize));
		}

		if (currentsPage != null && !currentsPage.isEmpty()) {
			int currentPage = Integer.parseInt(currentsPage);

			if (currentPage <= 0) {
				currentPage = 1;
			}

			int currentResult = (currentPage - 1) * Integer.parseInt(pageSize);

			deviceInfo.setCurrentPage(currentPage);
			deviceInfo.setCurrentResult(currentResult);
		}

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
			DeviceUserRel deviceRel = deviceBindingService.checkDeviceIsBinding(device.getId());
			if (null == deviceRel) {
				device.setIsBinding("F");
			} else {
				device.setIsBinding(deviceRel.getIsBinding());
			}
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
		return pa;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/insertDeviceInfo")
	@ResponseBody
	public Result insertDeviceInfo(@RequestBody DeviceInfo deviceInfo) {
		if (!StringUtil.isEmpty(deviceInfo.getManagerId())) {
			Result resultUser = backUserService.getOneUser(deviceInfo.getManagerId());
			BackstageUserInfo backUser = (BackstageUserInfo) resultUser.getData();
			if (null != backUser) {
				deviceInfo.setDeviceLocation(backUser.getMobile() + " " + backUser.getName() + " "
						+ backUser.getAddress());
			}
			deviceInfo.setDeviceLocationid(deviceInfo.getManagerId());
		}
		Result result = deviceInfoService.insertDeviceInfo(deviceInfo);
		if (result.isSuccessful() && !StringUtil.isEmpty(deviceInfo.getManagerId())) {
			deviceThrowService.insertDeviceThrow(deviceInfo.getManagerId(), deviceInfo.getId(),
					deviceInfo.getDeviceLocation());
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateDeviceInfo")
	@ResponseBody
	public Result updateDeviceInfo(@RequestBody DeviceInfo deviceInfo) {
		if (!StringUtil.isEmpty(deviceInfo.getManagerId())) {
			Result resultUser = backUserService.getOneUser(deviceInfo.getManagerId());
			BackstageUserInfo backUser = (BackstageUserInfo) resultUser.getData();
			if (null != backUser) {
				deviceInfo.setDeviceLocation(backUser.getMobile() + " " + backUser.getName() + " "
						+ backUser.getAddress());
			}
			deviceInfo.setDeviceLocationid(deviceInfo.getManagerId());
		} else {
			deviceInfo.setDeviceLocation("");
		}
		Result resultDevice = deviceInfoService.getDeviceInfoById(deviceInfo.getId());
		DeviceInfo oldDeviceInfo = (DeviceInfo) resultDevice.getData();
		Result result = deviceInfoService.updateDeviceInfo(deviceInfo);
		if (result.isSuccessful() && !StringUtil.isEmpty(deviceInfo.getManagerId())
				&& !oldDeviceInfo.getManagerId().equals(deviceInfo.getManagerId())) {
			deviceThrowService.insertDeviceThrow(deviceInfo.getManagerId(), deviceInfo.getId(),
					deviceInfo.getDeviceLocation());
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/fileImport")
	@ResponseBody
	public Result fileImport(@RequestParam(value = "file") MultipartFile file,
			@RequestParam("definitionId") String definitionId, HttpServletRequest request,
			@RequestParam(value = "managerId", required = false) String managerId,
			HttpServletResponse response) {
		List<DeviceInfo> listExcel = new ArrayList<DeviceInfo>(); // excel中的数据
		try {
			String rootPath = request.getSession().getServletContext().getRealPath(File.separator);
			String fileName = file.getOriginalFilename();
			String ym = new SimpleDateFormat("yyyy-MM-dd HHmmss").format(new Date());
			String filePath = "uploadFile/" + ym + "/" + fileName;
			File newFile = new File(rootPath + filePath);
			if (newFile.exists()) {
				newFile.delete();
				newFile.mkdirs();
			} else {
				newFile.mkdirs();
			}
			file.transferTo(newFile);

			Workbook book = Workbook.getWorkbook(new FileInputStream(newFile));
			Sheet sheet = book.getSheet(0);
			Set<String> setMap = new HashSet<String>();
			int row = sheet.getRows();
			int column = sheet.getColumns();
			for (int i = 1; i < row; i++) {
				DeviceInfo deviceInfo = new DeviceInfo();
				/*
				 * 不更新投放地信息 用户自己去修改 if (!StringUtil.isEmpty(managerId)) {
				 * deviceInfo.setManagerId(managerId); Result resultUser =
				 * backUserService.getOneUser(managerId); BackstageUserInfo
				 * backUser = (BackstageUserInfo) resultUser.getData(); if (null
				 * != backUser) {
				 * deviceInfo.setDeviceLocation(backUser.getMobile() + " " +
				 * backUser.getName() + " " + backUser.getAddress()); } }
				 */
				// 第一个是列数，第二个是行数
				String deviceCode = sheet.getCell(0, i).getContents();
				setMap.add(deviceCode);
				deviceInfo.setDefinitionId(Integer.parseInt(definitionId));
				deviceInfo.setDeviceCode(deviceCode);
				deviceInfo.setDeviceName(sheet.getCell(1, i).getContents());
				Pattern pattern = Pattern.compile("[0-9]*");
				String deviceStock = sheet.getCell(2, i).getContents();
				String deviceRental = sheet.getCell(3, i).getContents();
				if (pattern.matcher(deviceStock).matches()) {
					deviceInfo.setDeviceStock(Integer.parseInt(deviceStock));
				} else {
					return new Result<>(StatusCodes.FAILED_DEPENDENCY, false,
							new ResultCode("FALSE", "第" + (i + 1) + "行库存不为整数"));
				}
				if (pattern.matcher(deviceRental).matches()) {
					deviceInfo.setDeviceRental(Float.parseFloat(deviceRental));
				} else {
					return new Result<>(StatusCodes.FAILED_DEPENDENCY, false,
							new ResultCode("FALSE", "第" + (i + 1) + "行租金不为整数"));
				}
				listExcel.add(deviceInfo);
			}
			if (listExcel.size() != setMap.size()) {
				Result result = new Result<>(StatusCodes.FAILED_DEPENDENCY, false,
						new ResultCode("FALSE", "导入文档中机械码有重复"));
				return result;
			}
		} catch (Exception e) {
			logger.error("设备导入:" + e.getMessage());
			return new Result<>(StatusCodes.FAILED_DEPENDENCY, false,
					new ResultCode("FALSE", "导入设备文档报错"));
		}
		return deviceInfoService.insertListDeviceInfo(listExcel);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/deleteDeviceInfo")
	@ResponseBody
	public Result deleteDeviceInfo(@RequestParam("id") Integer id) {
		deviceBindingService.deleteBingingUser(id);
		return deviceInfoService.deleteDeviceInfo(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/recoverDeviceInfo")
	@ResponseBody
	public Result recoverDeviceInfo(@RequestParam("id") Integer id) {
		return deviceInfoService.recoverDeviceInfo(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/getDeviceInfoById")
	@ResponseBody
	public Result getDeviceInfoById(@RequestParam("id") Integer id) {
		Result result = deviceInfoService.getDeviceInfoById(id);
		if (result.isSuccessful()) {
			DeviceInfo device = (DeviceInfo) (result.getData());
			Result resultUser = backUserService.getOneUser(device.getManagerId());
			BackstageUserInfo backUser = (BackstageUserInfo) resultUser.getData();
			if (null != backUser) {
				device.setUserMobile(backUser.getMobile());
				device.setUserName(backUser.getName());
				device.setDeviceLocation(backUser.getAddress());
			}
		}
		return result;
	}
}