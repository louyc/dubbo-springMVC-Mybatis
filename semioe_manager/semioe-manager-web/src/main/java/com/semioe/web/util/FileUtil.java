package com.semioe.web.util;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	private static String filePath;

	@Value("#{settings['file.path']}")
	public void setFilePath(String path) {
		FileUtil.filePath = path;
	}

	private static String fileHost;

	@Value("#{settings['file.host']}")
	public void setFileHost(String host) {
		FileUtil.fileHost = host;
	}

	public static String saveMultipartFile(MultipartFile file, String subPath, String fileName,
			String handOut) throws IllegalStateException, IOException {
		InetAddress address = InetAddress.getLocalHost();// 获取的是本地的IP地址
		logger.info("图片服务器ip:{}\t当前服务器ip:{}", fileHost, address.getHostAddress());

		if (!file.isEmpty() && StringUtils.isEmpty(fileName)) {
			// 取得当前上传文件的文件名称
			String myFileName = file.getOriginalFilename();
			logger.info("file name is {}", myFileName);
			// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
			if (myFileName.trim() != "") {
				// 重命名上传后的文件名
				fileName = UUID.randomUUID() + "."
						+ FilenameUtils.getExtension(file.getOriginalFilename());
			}
		}

		// 保存图片，返回路径
		if (!file.isEmpty()) {
			// 分发请求
			if (!"1".equals(handOut)) {
				String[] hosts = fileHost.split(",");
				for (String host : hosts) {
					if (!address.getHostAddress().equals(host)) {
						logger.info("向{}发送同步文件请求", host);
						httpClientUploadFile(host, fileName, subPath, file);
					}
				}
			}

			// 定义上传路径
			String path = filePath + subPath + fileName;
			logger.info("filePath={}", path);
			// 创建文件夹路径
			File directory = new File(filePath + subPath);
			directory.mkdirs();
			if (directory.exists()) {
				logger.info("文件路径{}确认存在，可以保存文件", filePath + subPath);
			}
			File localFile = new File(path);
			file.transferTo(localFile);

			String returnPath = "/upFile/" + fileName;
			logger.info("save file path:{}", returnPath);
			if (!"/upload/".equals(subPath))
				returnPath = subPath + fileName;

			return returnPath;
		}
		return "-1";
	}

	/**
	 * 中转文件
	 * 
	 * @param file
	 *            上传的文件
	 * @param subPath
	 *            子目录
	 * @return 响应结果
	 */
	public static String httpClientUploadFile(String fileHost, String fileName, String subPath,
			MultipartFile file) throws IOException {
		String remote_url = "http://" + fileHost + ":8080/file/upload";// 第三方服务器请求地址
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		try {
			HttpPost httpPost = new HttpPost(remote_url);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA,
					fileName);// 文件流
			builder.addTextBody("fileName", fileName);// 类似浏览器表单提交，对应input的name和value
			builder.addTextBody("handOut", "1");// 这是分发请求，接受方不再继续分发
			builder.addTextBody("subPath", subPath);// 子路径
			HttpEntity entity = builder.build();
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);// 执行提交
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				// 将响应内容转换为字符串
				result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
				logger.info("上传结果:{}", result);
			}
		} catch (Exception e) {
			logger.error("post 同步请求失败:{}", e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("httpClient.close失败:{}", e);
			}
		}
		return result;
	}

}
