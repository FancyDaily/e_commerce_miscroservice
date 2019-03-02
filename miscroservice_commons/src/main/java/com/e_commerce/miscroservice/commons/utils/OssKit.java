package com.e_commerce.miscroservice.commons.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * oss 工具类
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/11/5 9:21
 * @Copyright 玖远网络
 */
@Setter
public class OssKit implements InitializingBean{

    private static final String SEPARATOR = "/";

    private static final String ACCESS_URL = "https://%s.oss-cn-hangzhou.aliyuncs.com";

    private Log logger = Log.getInstance (OssKit.class);

    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private OSSClient ossClient;


    /**
     * 文件上传
     * <p>大于5G可能有问题</p>
     *
     * @param localFilePath 本地文件路径 /home/...
     * @param bucketInfos  阿里云上的文件目录
     * @param fileNameOnOss 存放在阿里云上的文件名
     * @author Charlie
     * @date 2018/11/5 10:39
     */
    public String simpleUpload(String localFilePath, List<String> bucketInfos, String fileNameOnOss) {
        String bucketName = bucketInfos.get (0);
        String subDirPath = bucketInfos.get (1);
        boolean isExist = ossClient.doesBucketExist (bucketName);
        logger.info ("上传文件 目录={}是否存在={}", bucketName, isExist);
        // 获取Bucket的存在信息
        if (! isExist) {
            // 新建一个Bucket
            ossClient.createBucket (bucketName);
        }

        // 上传文件流。
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream (localFilePath);
            PutObjectResult result = ossClient.putObject (bucketName, subDirPath + fileNameOnOss, inputStream);
            logger.info ("result = {}", result.getETag ());
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        } finally {
            IOUtils.close (inputStream);
            // 关闭OSSClient。
//            ossClient.shutdown ();
        }

        return new StringBuilder ()
                .append (String.format (ACCESS_URL, bucketName))
                .append (SEPARATOR)
                .append (subDirPath)
                .append (fileNameOnOss)
                .toString ();
    }

    /**
     * 文件上传
     * <p>大于5G可能有问题</p>
     *
     * @param inputStream   本地文件流
     * @param bucketInfos  阿里云上的文件目录
     * @param fileNameOnOss 存放在阿里云上的文件名
     * @author Charlie
     * @date 2018/11/5 10:39
     */
    public String simpleUpload(InputStream inputStream, List<String> bucketInfos, String fileNameOnOss) {
        String bucketName = bucketInfos.get (0);
        String subDirPath = bucketInfos.get (1);
        boolean isExist = ossClient.doesBucketExist (bucketName);
        logger.info ("上传文件 目录={}是否存在={}", bucketName, isExist);
        // 获取Bucket的存在信息
        if (! isExist) {
            // 新建一个Bucket
            ossClient.createBucket (bucketName);
        }

        // 上传文件流。
        ossClient.putObject (bucketName, subDirPath + fileNameOnOss, inputStream);
        // 关闭OSSClient。
//        ossClient.shutdown ();
        return new StringBuilder ()
                .append (String.format (ACCESS_URL, bucketName))
                .append (SEPARATOR)
                .append (subDirPath)
                .append (fileNameOnOss)
                .toString ();
    }


    @Override
    public void afterPropertiesSet() {
        ossClient = new OSSClient (endpoint, accessKeyId, accessKeySecret);
    }



//    public static void main(String[] args) {
//
//        String basePath = "store-img-test";
//        String filePath = "C:\\Users\\Think\\Desktop\\chaplin.jpg";
//        String fileNameOnOss = new SimpleDateFormat ("yyyyMMddHHmmss").format (new Date ())+new Random ().nextInt (100)+".jpg";
//        System.out.println (" 文件名 fileNameOnOss = " + fileNameOnOss);
//        String yourAccessKeyId = "LTAII886vJs3n5ZQ";
//        String yourAccessKeySecret = "RvluqO8ioAnpjRoTdABjbl6VQnxDwZ";
//
//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
//        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
//        String accessKeyId = yourAccessKeyId;
//        String accessKeySecret = yourAccessKeySecret;
//
//        // 创建OSSClient实例。
//        OSSClient ossClient = new OSSClient (endpoint, accessKeyId, accessKeySecret);
//
//        // 获取Bucket的存在信息
//        if (! ossClient.doesBucketExist (basePath)) {
//            // 新建一个Bucket
//            ossClient.createBucket (basePath);
//        }
//
//        // 上传文件流。
//        InputStream inputStream = null;
//        try {
//            inputStream = new FileInputStream (filePath);
//            ossClient.putObject (basePath, fileNameOnOss, inputStream);
//            // 关闭OSSClient。
//        } catch (FileNotFoundException e) {
//            e.printStackTrace ();
//        } finally {
//            IOUtils.close (inputStream);
//            ossClient.shutdown ();
//        }
//
//    }
}
