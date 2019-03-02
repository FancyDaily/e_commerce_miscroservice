/**
 * OSS（阿里开放式云存储提供的文件上传、下载功能）
 */
package com.e_commerce.miscroservice.commons.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author LWS
 *
 */
@Component
public class OSSFileUtil implements FileUtil {

//	@Value("${oss.default.basepath.name}")
	public  String DEFAULT_BASE_PATH_NAME = "yjj-img-www";
//	@Value("${oss.access.key.id}")
	public  String ACCESS_KEY_ID="LTAII886vJs3n5ZQ";
//	@Value("${oss.access.key.secret}")
	public  String ACCESS_KEY_SECRET="RvluqO8ioAnpjRoTdABjbl6VQnxDwZ";
//	@Value("${oss.end.point}")
	public  String END_POINT="http://oss-cn-hangzhou.aliyuncs.com";
//	@Value("${oss.img.service}")
	public  String IMG_SERVICE="https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com";

	/*
	public static void setOssDefaultBasePathName(String ossDefaultBasePathName) {
		DEFAULT_BASE_PATH_NAME = ossDefaultBasePathName;
	}

	public static void setAccessKeyId(String accessKeyId) {
		ACCESS_KEY_ID = accessKeyId;
	}

	public static void setAccessKeySecret(String accessKeySecret) {
		ACCESS_KEY_SECRET = accessKeySecret;
	}

	public static void setEndPoint(String endPoint) {
		END_POINT = endPoint;
	}

	public static void setImgService(String imgService) {
		IMG_SERVICE = imgService;
	}
*/
	public OSSFileUtil(){
		/*// 创建ClientConfiguration实例
		ClientConfiguration conf = new ClientConfiguration();
		// 设置HTTP最大连接数为10
		conf.setMaxConnections(10);
		// 设置TCP连接超时为5000毫秒
		conf.setConnectionTimeout(5000);
		// 设置最大的重试次数为3
		conf.setMaxErrorRetry(3);
		// 设置Socket传输数据超时的时间为2000毫秒
		conf.setSocketTimeout(2000);*/
		// 使用OSSClient默认配置
		client = new OSSClient(END_POINT,ACCESS_KEY_ID, ACCESS_KEY_SECRET);
	}

	@Override
	public String uploadFile(String basePath, File file)
			throws IOException {
		String fileName = file.getName();
		if (fileName.contains("/")) {
			throw new RuntimeException(
					"get file name with error, please use other browser and try again");
		}
		String suffix = StringUtils.indexOf(fileName, '.') == -1 ? "": "."+  StringUtils.split(fileName, ".")[1];
		String storeFileName = String.valueOf(System.currentTimeMillis ()) + String.valueOf(System.currentTimeMillis()) +suffix ;
		// 获取Bucket的存在信息
		boolean exists = client.doesBucketExist(basePath);
		// 输出结果
		File dir = file.getParentFile();
		if (!dir.exists()&&dir.mkdirs()) {
			logger.info("目录创建成功");
		}
		if (!exists) {
			// 新建一个Bucket
			client.createBucket(basePath);
		}
		FileInputStream content = new FileInputStream(file);
//		InputStream content = file.getInputStream();
		// 创建上传Object的Metadata
		ObjectMetadata meta = new ObjectMetadata();
		// 必须设置ContentLength
		meta.setContentLength(file.length());
		// 上传Object.
		PutObjectResult result = client.putObject(basePath, storeFileName,
				content, meta);
		// 打印ETag
		logger.debug(result.getETag());
		// 生成访问链
		return IMG_SERVICE + SPLASH + storeFileName ;
	}

	@Override
	public String downloadFile(String filename) {
		return null;
	}

	@Override
	public List<String> listAllFile(String basePath) {
		return null;
	}

	@Override
	public String removeFile(String basePath, String fileName) {
		// 为安全考虑删除权限将不再提供， 定期统一处理
		// client.deleteObject(basePath, fileName);
		return fileName;
	}
	
	/*private String generateSignature(String source){
		final String algorithm = "HmacSHA1";
		final String charset = "US-ASCII";
		try {
			Mac mac = Mac.getInstance(algorithm);
			SecretKey secretKey = new SecretKeySpec(ACCESS_KEY_SECRET.getBytes(charset), algorithm);
			mac.init(secretKey);
			byte[] text = source.toString().getBytes(charset);
			byte[] finalText = mac.doFinal(text);
			return Base64Util.encode(new String(finalText));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
			logger.error(e);
		}
		return null;
	}*/
	
	/* 伟生接口
	private final String ACCESS_KEY_ID = "BbH5y38r89PDNLi1";
	private final String ACCESS_KEY_SECRET = "fiZUw81vGtOmb7JC0qZE6I6BfiOogw";
	private final String END_POINT = "http://oss-cn-shenzhen.aliyuncs.com";
	private final String IMG_SERVICE = "http://yjj-img-store-test.oss-cn-shenzhen.aliyuncs.com";
	*/



	// 初始化一个OSSClient
	private OSSClient client = null;
	private final String SPLASH = "/";
	
	private static Logger logger = Logger.getLogger(OSSFileUtil.class);
	
}
