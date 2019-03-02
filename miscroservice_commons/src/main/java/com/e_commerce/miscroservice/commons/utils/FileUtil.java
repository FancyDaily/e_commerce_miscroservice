/**
 * 文件存储、上传、下载处理
 */
package com.e_commerce.miscroservice.commons.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author LWS
 *
 */
public interface FileUtil {

	public String uploadFile(final String basePath, File file) throws IOException;
	public String downloadFile(String filename);
	public String removeFile(final String basePath, String fileName);
	public List<String> listAllFile(String basePath);
}
