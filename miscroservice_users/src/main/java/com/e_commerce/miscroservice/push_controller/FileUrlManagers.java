package com.e_commerce.miscroservice.push_controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.e_commerce.miscroservice.commons.enums.colligate.ApplicationEnum;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl.GZLessonServiceImpl;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件url管理
 */
@Component
public class FileUrlManagers {

    @Autowired
    private RedisUtil redisUtil;

    private Upload upload = new Upload();
    private Dns dns = new Dns();


    /**
     * 推送
     *
     * @param fileName 文件名
     */
    public Boolean push(String fileName) {
        Boolean pushSuccessFlag=Boolean.FALSE;
        if(fileName==null||fileName.isEmpty()){
            return pushSuccessFlag;
        }
        try {
            upload.upload(fileName);
            pushSuccessFlag=Boolean.TRUE;

        } catch (QiniuException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pushSuccessFlag;
    }

    /**
     * 根据文件名 获取地址
     *
     * @param fileName 文件名称
     * @return
     */
    public String getUrl(String fileName) {
        String url = "";
        if(fileName==null||fileName.isEmpty()){
            return url;
        }
        try {
            String key = "url" + fileName;
            Long exist = (Long) redisUtil.get(key);
            boolean expired = exist==null? true:System.currentTimeMillis() > exist;
            //从LoadingCache获取
            url = (String) ApplicationEnum.loadingCache.getUnchecked(fileName);
            if(expired || "".equals(url)) {
                String dnsName = System.currentTimeMillis() + "";
                if (dns.addDnsByName(dnsName)) {
                    url = dns.getUrlByName(dnsName, fileName);
                    redisUtil.set(key, System.currentTimeMillis() + 1000 * 60 * 8, 1000 * 60 * 8);
                }
            }
        } catch (ClientException e) {

        }
        return url;
    }


    /**
     * dns
     */
    private static class Dns {

        private IAcsClient client;
        private final Long TASK_EXEC_INTERVAL_MILL_TIME = 1000 * 60L;
        private final Long TASK_INVALID_MILL_TIME = 1000 * 60 * 10L;
        private final String DNS_DOMAIN_NAME = "xiaoshitimebank.com";
        private volatile Boolean IS_START = Boolean.FALSE;
        private final String ACCESS_URL_PREFIX = "http://%s." + DNS_DOMAIN_NAME + "/%s";
        private final String DNS_VAL_PARSE_DOMAIN_NAME = "xiaoshitimebank.com.www.qiniudns.com";

        private static Map<String, Long> CDN_ID_CACHE = new ConcurrentHashMap<>();


        {
            init();
        }


        /**
         * 初始化任务
         */
        private synchronized void init() {

            if (IS_START) {
                return;
            } else {
                IS_START = Boolean.TRUE;
            }

            initClient();

            initTimer();
            clearAll();

        }

        /**
         * 初始化客户端参数
         */
        private void initClient() {
            String regionId = "cn-hangzhou";
            String accessKeyId = "LTAIugb9g6sfhKNs";
            String accessKeySecret = "gOqhLzaGYZ09nAi5SrTouN4to1fsU5";
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            client = new DefaultAcsClient(profile);
        }


        /**
         * 初始化定时任务
         */
        private void initTimer() {

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    try {
                        clearInvalidByCache();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }, TASK_EXEC_INTERVAL_MILL_TIME, TASK_EXEC_INTERVAL_MILL_TIME);
        }


        /**
         * 清理缓存中失效的key
         *
         * @throws ClientException
         */
        private void clearInvalidByCache() throws ClientException {
            for (Map.Entry<String, Long> entry : CDN_ID_CACHE.entrySet()) {
                if (System.currentTimeMillis() - entry.getValue() > TASK_INVALID_MILL_TIME) {
                    CDN_ID_CACHE.remove(entry.getKey());
                    removeDnsById(entry.getKey());

                }
            }

        }


        /**
         * 清除所有
         */
        private void clearAll() {
            try {
                DescribeDomainRecordsRequest allRecords = new DescribeDomainRecordsRequest();
                allRecords.setActionName("DescribeDomainRecords");
                allRecords.setDomainName(DNS_DOMAIN_NAME);
                allRecords.setPageSize(500L);
                allRecords.setValueKeyWord(DNS_VAL_PARSE_DOMAIN_NAME);
                DescribeDomainRecordsResponse recordsResponse = client.getAcsResponse(allRecords);
                for (DescribeDomainRecordsResponse.Record record : recordsResponse.getDomainRecords()) {
                    removeDnsById(record.getRecordId());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        /**
         * 根据Id删除dns记录值
         *
         * @param recordId Id
         * @throws ClientException
         */
        private void removeDnsById(String recordId) throws ClientException {
            DeleteDomainRecordRequest deleteDomainRecordRequest = new DeleteDomainRecordRequest();
            deleteDomainRecordRequest.setActionName("DeleteDomainRecord");
            deleteDomainRecordRequest.setRecordId(recordId);
            client.doAction(deleteDomainRecordRequest, true, 3);
        }


        /**
         * 根据名称添加dns记录值
         *
         * @param dnsName dns名称
         * @throws ClientException
         */
        public Boolean addDnsByName(String dnsName) throws ClientException {
            AddDomainRecordRequest addDomainRequest = new AddDomainRecordRequest();
            addDomainRequest.setActionName("AddDomainRecord");
            addDomainRequest.setDomainName(DNS_DOMAIN_NAME);
            addDomainRequest.setRR(dnsName);
            addDomainRequest.setType("CNAME");
            addDomainRequest.setValue(DNS_VAL_PARSE_DOMAIN_NAME);
            addDomainRequest.setTTL(600L);
            AddDomainRecordResponse acsResponse = client.getAcsResponse(addDomainRequest);

            if (acsResponse.getRecordId() != null) {
                CDN_ID_CACHE.put(acsResponse.getRecordId(), System.currentTimeMillis());
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }

        }

        /**
         * 根据dns名称和文件名称获取链接地址
         *
         * @param dnsName  dns名称
         * @param fileName 文件名称
         * @return
         */
        public String getUrlByName(String dnsName, String fileName) {
            return String.format(ACCESS_URL_PREFIX, dnsName, fileName);
        }


    }


    /**
     * 上传
     */

    private class Upload {


        private final String FILE_TOTAL_PATH = "/mnt/ftp/";
//        private final String FILE_TOTAL_PATH = "/Users/xufangyi/Downloads/";
        private final String accessKey = "OJ5ePYgOmXHSdi7Wb8cebmB0OwDUDlGzeTeWORdH";
        private final String secretKey = "ldPnyGLV5NeIwtzVLPTX-W-BXut7vMKtVXLglLao";
        private final String bucket = "ness";


        /**
         * 获取上传凭证
         *
         * @return
         */
        private String getUploadToken() {

            return Auth.create(accessKey, secretKey).uploadToken(bucket);

        }

        /**
         * 根据文件名称上传文件
         *
         * @param fileName
         */
        private void upload(String fileName) throws QiniuException {
            UploadManager uploadManager = new UploadManager(new Configuration(Zone.zone0()));

            uploadManager.put(FILE_TOTAL_PATH + fileName
                    , fileName, getUploadToken());


        }


    }


}