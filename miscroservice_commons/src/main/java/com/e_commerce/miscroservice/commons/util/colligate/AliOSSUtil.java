package com.e_commerce.miscroservice.commons.util.colligate;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.e_commerce.miscroservice.commons.enums.application.UploadPathEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


/**
 * 功能描述:进行oss的简单上传
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月17日 下午5:38:35
 */
@Component
public class AliOSSUtil {

	private static Logger logger = LoggerFactory.getLogger(AliOSSUtil.class);

	private static OSSClient ossClient;

	private static String bucket;

	private static String endpoint;

	@Autowired 
	public void setOssClient(OSSClient ossClient) {
		AliOSSUtil.ossClient = ossClient;
	}

	@Value("${bucket}")
	public void setBucket(String bucket) {
		AliOSSUtil.bucket = bucket;
	}

	@Value("${endpoint}")
	public void setEndpoint(String endpoint) {
		AliOSSUtil.endpoint = endpoint;
	}

	/**
	 * 
	 * 功能描述:文件上传
	 * 作者:马晓晨
	 * 创建时间:2018年11月17日 下午5:40:25
	 * @throws Exception 
	 */
	public static String uploadFile(MultipartFile file, String savePath) throws Exception {
		if (savePath == null) {
			savePath = "";
		}
		// 获取图片后缀名
		String fileOriginalFilename = file.getOriginalFilename();
		String suffix = fileOriginalFilename.substring(fileOriginalFilename.lastIndexOf("."));
		String fileName = UUID.randomUUID().toString().replaceAll("-", "");
		if (savePath.equals("")) {
			fileName = fileName + suffix;
		} else {
			fileName = savePath + "/" + fileName + suffix;
		}
		try {
			InputStream inputStream = file.getInputStream();
			ossClient.putObject(bucket, fileName, inputStream);
			String imgPath = "https://" + bucket + "." + endpoint + "/" + fileName;
			return imgPath;
		} catch (OSSException | ClientException | IOException e) {
			logger.error("图片上传oss发生错误");
			e.printStackTrace();
			throw new Exception("图片上传oss发生错误");
		}
	}

	public static String uploadFile(InputStream inputStream, String savePath, String fileName) throws Exception {
		if (savePath == null) {
			savePath = "";
		}
		// 获取图片后缀名
		fileName = savePath + "/" + fileName;
		try {
			ossClient.putObject(bucket, fileName, inputStream);
			String imgPath = "https://" + bucket + "." + endpoint + "/" + fileName;
			return imgPath;
		} catch (OSSException | ClientException e) {
			logger.error("图片上传oss发生错误");
			e.printStackTrace();
			throw new Exception("图片上传oss发生错误");
		}
	}

	public static String uploadQrImg(InputStream in, String secen) throws Exception {
		// 判空
		String savePath = UploadPathEnum.innerEnum.PERSON.getPath();
		
		// 获取图片后缀名
		String fileName = savePath + "/" + "QR" + secen+".jpg";
		try {
			ossClient.putObject(bucket, fileName, in);
			String imgPath = "https://" + bucket + "." + endpoint + "/" + fileName;
			return imgPath;
		} catch (OSSException | ClientException e) {
			logger.error("图片上传oss发生错误");
			e.printStackTrace();
			throw new Exception("图片上传oss发生错误");
		}
	}

}
