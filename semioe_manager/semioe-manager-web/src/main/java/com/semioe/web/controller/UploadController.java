package com.semioe.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.semioe.common.result.Result;
import com.semioe.common.result.ResultCode;
import com.semioe.common.result.StatusCodes;
import com.semioe.web.util.FileUtil;

@Controller
@RequestMapping("/file")
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@Value("#{settings['file.path']}")
	private String filePath;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upload(MultipartHttpServletRequest request,
			HttpServletResponse response) {
		Result<String> result = new Result<>(StatusCodes.OK, true);
		logger.info("UploadController.upload() start");
		try {
			// 在文件上传请求中获取文件，根据file的name
			List<MultipartFile> files = request.getFiles("file");
			String fileName = request.getParameter("fileName");
			String handOut = request.getParameter("handOut");
			String subPath = request.getParameter("subPath");
			if (StringUtils.isEmpty(subPath))
				subPath = "/upload/";
			logger.info("upload {} file to server", files.size());
			if (files.size() > 0) {

				for (int i = 0; i < files.size(); i++) {
					// 取得上传文件
					MultipartFile file = files.get(i);
					// 保存文件
					String returnPath = FileUtil.saveMultipartFile(file, subPath, fileName,
							handOut);
					if (returnPath != "-1") {
						if (StringUtils.isNotEmpty(result.getData())) {
							result.setData(result.getData() + ";" + returnPath);
						} else {
							result.setData(returnPath);
						}
						logger.info("return file path:{}", result.getData());
					}
				}
				result.setResultCode(new ResultCode("SUCCESS", "上传成功！"));
			} else {
				result.setSuccessful(false);
				result.setResultCode(new ResultCode("FILE_NULL", "上传文件为空！"));
			}
		} catch (IllegalStateException | IOException e) {
			logger.error("存储文件失败,{}", e);
			return new Result<>(StatusCodes.OK, false, new ResultCode("SAVE_FAILED", "上传失败！"));
		}
		return result;
	}

	@RequestMapping(value = "/upFile", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("files") String[] files) {
		Result<String> result = new Result<>(StatusCodes.OK, true);
		logger.info("UploadController.upFile() start:{}", files.toString());
		try {
			logger.info("upload {} file to server", files.length);
			if (files.length > 0) {
				for (String str : files) {
					// 取得上传文件
					File file = new File(filePath + str);
					FileInputStream input = new FileInputStream(file);
					MultipartFile multipartFile = new MockMultipartFile("file", file.getName(),
							Files.probeContentType(file.toPath()), IOUtils.toByteArray(input));
					// 保存文件
					String returnPath = FileUtil.saveMultipartFile(multipartFile,
							str.substring(0, 25), multipartFile.getOriginalFilename(), null);
					if (returnPath != "-1") {
						if (StringUtils.isNotEmpty(result.getData())) {
							result.setData(result.getData() + ";" + returnPath);
						} else {
							result.setData(returnPath);
						}
						logger.info("return result.data:{}", result.getData());
					}
				}
				result.setResultCode(new ResultCode("SUCCESS", "上传成功！"));
			} else {
				result.setSuccessful(false);
				result.setResultCode(new ResultCode("FILE_NULL", "上传文件为空！"));
			}
		} catch (IllegalStateException | IOException e) {
			logger.error("存储文件失败,{}", e);
			return new Result<>(StatusCodes.OK, false, new ResultCode("SAVE_FAILED", "上传失败！"));
		}
		return result;
	}

}
