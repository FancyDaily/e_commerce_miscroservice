package com.e_commerce.miscroservice.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.user.StoreBusinessVo;
import com.e_commerce.miscroservice.commons.entity.user.MemberOperatorRequest;
import com.e_commerce.miscroservice.commons.entity.user.StoreShopVo;
import com.e_commerce.miscroservice.commons.entity.user.StoreWxaShopAuditDataQuery;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.current.ExecutorService;
import com.e_commerce.miscroservice.commons.helper.current.ExecutorTask;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.utils.*;
import com.e_commerce.miscroservice.user.dao.*;
import com.e_commerce.miscroservice.user.entity.*;
import com.e_commerce.miscroservice.user.entity.support.ValidTimeQueueHelper;
import com.e_commerce.miscroservice.user.mapper.StoreBusinessMapper;
import com.e_commerce.miscroservice.user.service.store.StoreBusinessService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 19:47
 * @Copyright 玖远网络
 */
@Service
public class StoreBusinessServiceImpl implements StoreBusinessService{

    @Value("${user.jfinal.sys.url}")
    private String weixinServiceUrl;
    @Value("${inShop.storeId}")
    private Long inShopStoreId;
    @Value("${page.url}")
    private String pageUrl;
    private ExecutorService executorService=new ExecutorService(1,"code");
    private static final String WXA_CODE_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";
    private static final String IN_SHOP_SHARE_QC_IMG = "inShopShareQcImg";
    private static final int YES = 1;
    private static final int NO = 0;
    private Log logger = Log.getInstance (StoreBusinessServiceImpl.class);

    private static final int SHARE_SHOP = 1;
    private static final int SELF_SHOP = 2;

    @Autowired
    private StoreWxaShopAuditDataDao storeWxaShopAuditDataDao;
    @Autowired
    private OssKit ossKit;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private StoreBusinessDao storeBusinessDao;
    @Autowired
    private StoreWxaDao storeWxaDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private ShopMemberDao shopMemberDao;
    @Autowired
    private StoreBusinessMapper storeMapper;
    @Autowired
    private WhitePhoneDao whitePhoneDao;


    /**
     * 更新微信小程序关闭时间
     *
     * @param storeId             门店id
     * @param newWxaCloseTime     新的时间
     * @param historyWxaCloseTime 老的时间
     * @return int
     * @author Charlie
     * @date 2018/8/16 14:24
     */
    @Override
    public int updateWxaCloseTime(Long storeId, Long newWxaCloseTime, Long historyWxaCloseTime) {
        return storeMapper.updateWxaCloseTime (storeId, newWxaCloseTime, historyWxaCloseTime, System.currentTimeMillis ());
    }

    @Override
    public StoreBusiness selectById(Long id) {
        return storeMapper.selectByPrimaryKey (id);
    }

    @Override
    public int updateByPrimaryKeySelective(StoreBusiness updInfo) {
        return storeMapper.updateByPrimaryKeySelective (updInfo);
    }

    @Override
    public StoreBusiness selectOne(StoreBusinessVo query) {
        return storeMapper.selectOne (query);
    }

    @Override
    public int insertSelective(StoreBusiness user) {
        return storeMapper.insertSelective (user);
    }


    /**
     * 店中店登录
     *
     * @param inShopMemberId 店中店openId
     * @param phone          手机号
     * @param phoneCode      验证码
     * @author Charlie
     * @date 2018/12/10 14:16
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> inShopStoreLogin(Long inShopMemberId, String phone, String phoneCode) {

        if (BeanKit.hasNull (inShopMemberId, phone, phoneCode)) {
            ErrorHelper.declare (false, "请求参数为空");
        }

        if (whitePhoneDao.getWhitePhone (phone) == 0) {
            // 如果手机号不在白名单
            boolean phoneVerifySuccess = SmsUtils.verifyCode (phone, phoneCode);
            ErrorHelper.declare (phoneVerifySuccess, "手机验证码错误");
        }

        Map<String, Object> result = new HashMap<> (2);


        //查询用户是否已有绑定账号
        StoreBusiness store = storeBusinessDao.findByInMemberId (inShopMemberId);
        if (store != null) {
            logger.info ("店中店登录--openId已经绑定过 phone={},inShopMemberId={},storeId={}", phone, inShopMemberId, store.getId ());
        }
        else {

            //手机号对应账号已存在
            ShopMember inShopMember = shopMemberDao.findById (inShopMemberId);
            ErrorHelper.declareNull (inShopMember, "没有店中店用户信息");
            String inShopOpenId = inShopMember.getBindWeixin ();

            StoreBusinessVo query = new StoreBusinessVo ();
            query.setPhoneNumber (phone);
            store = selectOne (query);
            if (store != null) {
                logger.info ("店中店登录--账号已存在 phone={},inShopMemberId={},storeId={}", phone, inShopMemberId, store.getId ());
                Long historyInShopMemberId = store.getInShopMemberId ();
                ErrorHelper.declare (StringUtils.isBlank (store.getInShopOpenId ()) && (historyInShopMemberId == null || historyInShopMemberId<=0L), "该手机号已经绑定过一个微信号");
                //账号绑定openId
                StoreBusiness updInfo = new StoreBusiness ();
                updInfo.setId (store.getId ());
                updInfo.setInShopMemberId (inShopMemberId);
                updInfo.setInShopOpenId (inShopOpenId);
                int rec = storeMapper.updateByPrimaryKeySelective (updInfo);
                ErrorHelper.declare (rec == 1, "同步账户信息失败");
            }
            else {
                logger.info ("店中店登录--创建新账号 phone={},openId={}", phone, inShopMemberId);
                store = UserServiceImpl.buildDefaultStore ();
                store.setPhoneNumber (phone);
                store.setInShopMemberId (inShopMemberId);
                store.setInShopOpenId (inShopOpenId);
                userDao.addStoreBusiness (store);
                long bNo = store.getId().longValue() + 800000000;
                store.setBusinessNumber (bNo);
                userDao.updateStoreBusiness (store);
                //.... 派发优惠券
            }
        }
        result.put ("storeId", store.getId ());

        //是否可以开通店中店
        boolean isCanOpen = memberDao.isCanOpenInShop (store);
        if (! isCanOpen) {
            //不能开通
            result.put ("hasInShop", YES);
            return result;
        }
        result.put ("hasInShop", NO);


        //查询店铺资料
        result.put ("wxaData", buildInShopWxaData (store));
        return result;
    }


    /**
     * 组装共享店铺的资料
     *
     * @param store store
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/13 20:25
     */
    private Map<String, Object> buildInShopWxaData(StoreBusiness store) {
        Long storeId = store.getId ();

        StoreWxaShopAuditData history;

        StoreWxaShopAuditData draft = storeWxaShopAuditDataDao.findDraftByStoreId (storeId, SHARE_SHOP);
        //优先草稿,其次正式
        if (draft != null) {
            history = draft;
        }
        else {
            List<StoreWxaShopAuditData> dataList = storeWxaShopAuditDataDao.findAllNormal (storeId);
            if (dataList.isEmpty ()) {
                //没有
                history = new StoreWxaShopAuditData ();
            }
            else {
                history = dataList.get (0);
            }
        }

        Map<String, Object> wxaData = new HashMap<> (6);
        wxaData.put ("nickName", history.getBossName ());
        wxaData.put ("industry", history.getIndustry ());
        wxaData.put ("mainBusiness", history.getMainBusiness ());
        wxaData.put ("businessAddress", history.getAddress ());
        wxaData.put ("businessName", history.getShopName ());
        return wxaData;
    }


    /**
     * 提交共享小程序店铺资料
     *
     * @param query query
     * @return com.e_commerce.miscroservice.user.entity.StoreWxa
     * @author Charlie
     * @date 2018/12/10 15:56
     */
    @Transactional( rollbackFor = Exception.class )
    @Override
    public StoreWxaShopAuditData commitInShopWxaData(StoreWxaShopAuditDataQuery query) {
        Long inShopMemberId = query.getInShopMemberId ();
        logger.info ("提交店铺资料 inShopMemberId={}", inShopMemberId);

        //用户对应APP账号
        StoreBusinessVo userQuery = new StoreBusinessVo ();
        userQuery.setInShopMemberId (inShopMemberId);
        StoreBusiness store = selectOne (userQuery);
        ErrorHelper.declareNull (store, "没有用户信息");
        return doCommitShopWxaDataByStore (query, store);
    }



    /**
     * 提交共享小程序店铺资料
     *
     * @param query query
     * @param store store
     * @return com.e_commerce.miscroservice.user.entity.StoreWxaShopAuditData
     * @author Charlie
     * @date 2018/12/19 10:27
     */
    @Override
    public StoreWxaShopAuditData doCommitShopWxaDataByStore(StoreWxaShopAuditDataQuery query, StoreBusiness store) {
        logger.info ("提交店铺审核资料");
        Long storeId = store.getId ();

        String bossName = query.getBossName ();
        String shopName = query.getShopName ();
        String industry = query.getIndustry ();
        String address = query.getAddress ();
        String mainBusiness = query.getMainBusiness ();
        ErrorHelper.declare (! BeanKit.hasNull (bossName, shopName, industry, address, mainBusiness), "请求参数不可为空");

        //店铺名称唯一性
        List<StoreWxaShopAuditData> history = storeWxaShopAuditDataDao.findNormalAndDraftByShopName (shopName);
        if (! history.isEmpty ()) {
            for (StoreWxaShopAuditData audit : history) {
                if (! audit.getStoreId ().equals (storeId)) {
                    throw ErrorHelper.me ("该店铺名称已被注册");
                }
            }
        }

        //是否可以开通店中店
        boolean isCanOpen = memberDao.isCanOpenInShop (store);
        ErrorHelper.declare (isCanOpen, "用户已有小程序,不可重复开通小程序");

        StoreWxaShopAuditData draft = storeWxaShopAuditDataDao.findDraftByStoreId (storeId, SHARE_SHOP);
        //更新微信用户信息
        if (draft != null) {
            logger.info ("更新微信用户信息");
            draft.setId (draft.getId ());
            draft.setShopName (shopName);
            draft.setAddress (address);
            draft.setBossName (bossName);
            draft.setIndustry (industry);
            draft.setMainBusiness (mainBusiness);
            int rec = storeWxaShopAuditDataDao.updateById (draft);
            ErrorHelper.declare (rec == 1, "更新用户信息失败");
        }
        else {
            //创建
            logger.info ("新增微信用户信息");
            draft = new StoreWxaShopAuditData ();
            draft.setStoreId (storeId);
            draft.setShopName (shopName);
            draft.setIndustry (industry);
            draft.setBossName (bossName);
            draft.setAddress (address);
            draft.setMainBusiness (mainBusiness);
            draft.setDelStatus (StateEnum.DRAFT);
//            int rec = storeWxaShopAuditDataDao.safeInsertInShopWxaDraft (draft);
            int rec = MybatisOperaterUtil.getInstance().save(draft);
            ErrorHelper.declare (rec == 1, "新增小程序店铺信息失败");
        }
        return draft;
    }


    /**
     * 用户是否可以开通店中店
     *
     * @param storeId storeId
     * @return boolean
     * @author Charlie
     * @date 2018/12/11 15:58
     */
    @Override
    public boolean isCanOpenInShop(Long storeId) {
        logger.info ("用户是否可以开通店中店 storeId={}", storeId);
        StoreBusiness store = selectById (storeId);
        if (store == null) {
            logger.error ("判断用户是否可以购买店中店会员, 没有找到门店用户信息 storeId={}", storeId);
            return Boolean.FALSE;
        }
        return memberDao.isCanOpenInShop (store);
    }

    /**
     * @author Charlie
     * @date 2018/12/12 15:29
     */
    @Override
    public StoreBusiness findByInMemberId(Long inShopMemberId) {
        return storeBusinessDao.findByInMemberId (inShopMemberId);
    }


    /**
     * 开通店中店
     *
     * @param request request
     * @author Charlie
     * @date 2018/12/13 9:53
     */
    @Override
    public void openWxaInShop(MemberOperatorRequest request, StoreBusiness store) {
        logger.info ("开始开通店中店===>");
        Long storeId = request.getId ();
//        StoreBusiness store = storeBusinessDao.findById (storeId);
        //开通店中店
        Long draftId = request.getShopDraftId();
        logger.info ("开通店中店 start draftId={}", draftId);
        StoreBusiness updInfo = new StoreBusiness ();
        updInfo.setId (storeId);
        updInfo.setIsOpenWxa (1);
        updInfo.setWxaBusinessType (1);
        //开通时间
        Calendar cal = Calendar.getInstance ();
        if (store.getWxaOpenTime ()==0) {
            updInfo.setWxaOpenTime (cal.getTimeInMillis ());
        }
        //关闭时间
        Long historyCloseTime = store.getWxaCloseTime ();
        if (historyCloseTime > System.currentTimeMillis ()) {
            //如果有效期没过期,就延期
            cal.setTimeInMillis (historyCloseTime);
        }
        Calendar endTime = ValidTimeQueueHelper.addTime (cal, request.getTimeUnit (), request.getTimeValue ());
        updInfo.setWxaCloseTime (endTime.getTimeInMillis ());
        int rec = storeMapper.updateByPrimaryKeySelective (updInfo);
        ErrorHelper.declare (rec == 1, "更新");
        //店铺资料
        List<StoreWxaShopAuditData> allNormal = storeWxaShopAuditDataDao.findAllNormal (storeId);
        if (! allNormal.isEmpty ()) {
            for (StoreWxaShopAuditData history : allNormal) {
                Long historyId = history.getId ();
                logger.info ("删除以前的店铺记录 historyId={}", historyId);
                rec = storeWxaShopAuditDataDao.deleteById (historyId);
                ErrorHelper.declare (rec == 1, "更新店铺资料错误");
            }
        }
        StoreWxaShopAuditData updData = new StoreWxaShopAuditData ();
        updData.setId (draftId);
        updData.setDelStatus (StateEnum.NORMAL);
        rec = storeWxaShopAuditDataDao.updateById (updData);
        ErrorHelper.declare (rec == 1, "更新店铺状态失败");
        logger.info ("开通共享店中店成功!!!");

        /**
         * @Author:胡坤
         * @GreateTime:2019年1月15日09:22:14
         *
         * 开启线程去生成二维码
         */
        List list=new ArrayList();
        String fileName ="publicQrCode"+System.currentTimeMillis()+".jpg";
        executorService.addTask(
                new ExecutorTask() {

                    @Override
                    public void doJob() {
                        try {
                            String path="";
                            Map<String, Object> auditEntity = storeMapper.getShareShopLoginQr(storeId);
                            logger.info("店铺资料={}", auditEntity);
                            if (auditEntity == null) {
                                path = "";
                            }else {
                                path=(String) auditEntity.get("shareQcCodeUrl");
                                if (StringUtils.isBlank(path)) {
                                    logger.info("初始化共享店铺二维码start...storeId"+inShopStoreId);
                                    path= geneShareQrImg(fileName, storeId);
                                    logger.info("生成二维码返回的地址:"+path);
                                }
                            }
                            updData.setShareQrCodeUrl(path);
                            storeWxaShopAuditDataDao.updateById (updData);
                        } catch (Exception e) {
                            list.add("1");
                            logger.info("二维码生成失败,准备再次生成");
                            //如果生成二维码失败那么就继续生成
                            if (list.size()<=5){//很重要一定要进行判断!!!!!!!
                                updData.setShareQrCodeUrl(null);
                                storeWxaShopAuditDataDao.updateById (updData);
                                doJob();
                            }else {
                                logger.info("生成5次都还没有成功,进行短信通知");//发送短信还没做成功
                                ErrorHelper.declare (list.size()<=5, "门店二维码生成失败,请联系客服");
                                return;
                            }
                        }
                    }
                }
        );
    }




    private String geneShareQrImg(String fileName, Long storeId) {
        String path="";
        String url =weixinServiceUrl+"/third/findAccessTokenByAppId";
        Map<String, String> param = new HashMap<>(6);
        List<StoreWxa> storeWxas = storeMapper.selectStoreWxaAppidList(inShopStoreId);
        StoreWxa storeWxa = null;
        if (storeWxas.size()>0) {
            storeWxa = storeWxas.get(0);
            param.put("appId",storeWxa.getAppId());
            String token = HttpUtils.sendGet(url, param);
            logger.info("token = " + token);
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("scene", "storeId=" + storeId + "&type=1");
            paramMap.put("page", pageUrl);
            paramMap.put("width", "430");
            InputStream inputStream = HttpClientUtils.postInputStream(String.format(WXA_CODE_URL, token), JSONObject.toJSONString(paramMap));
            try{
                int inputSize = inputStream.available();
                //标准的是1600多 为了防止二维码生成失效 所以定义一个大小来进行判断
                if (inputSize<14000){
                    throw new Exception();
                }
                //上传
                path = ossKit.simpleUpload (
                        inputStream,
                        Stream.of("yjj-img-main", "customer/").collect(Collectors.toList()),
                        fileName
                );
            }catch (Exception e){
                logger.info("上传图片失败");
            }finally {
                IOUtils.close (inputStream);
                return path;
            }
        }else {
            return path;
        }
    }

    /**
     * 用户给是否可以开店
     *
     * @param inShopMemberId inShopMemberId
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/13 18:20
     */
    @Override
    public Map<String, Object> iWantOpenStore(Long inShopMemberId) {
        logger.info ("我要开店 inShopMemberId={}", inShopMemberId);
//        ShopMember shopMember = shopMemberDao.findById (inShopMemberId);
//        String bindWeixin = shopMember.getBindWeixin();
//        ErrorHelper.declareNull(bindWeixin, "没有微信信息");
//        StoreBusiness store = storeBusinessDao.findByInShopOpenId (bindWeixin);
        StoreBusiness store = findStoreByInShopMemberId (inShopMemberId);

        Map<String, Object> res = new HashMap<> (2);
        if (store == null) {
            logger.info ("需要登录绑定手机号");
            res.put ("needLoginBindApp", YES);
            res.put ("hasInShop", "");
            return res;
        }

        res.put ("needLoginBindApp", NO);
        boolean canOpenInShop = memberDao.isCanOpenInShop (store);
        logger.info ("canOpenInShop = {}", canOpenInShop);
        res.put ("hasInShop", canOpenInShop ? NO : YES);
        return res;
    }


    @Override
    public Map<String, Object> wxaDataHistoryList(Long inShopMemberId) {
        StoreBusiness store = findStoreByInShopMemberId (inShopMemberId);
        ErrorHelper.declareNull (store, "未找到用户");
        Map<String, Object> result = new HashMap<> (2);


        //是否可以开通店中店
        boolean isCanOpen = memberDao.isCanOpenInShop (store);
        if (! isCanOpen) {
            //不能开通
            result.put ("hasInShop", YES);
            return result;
        }
        result.put ("hasInShop", NO);


        //查询店铺资料
        result.put ("wxaData", buildInShopWxaData (store));
        return result;
    }


    /**
     * 打开店铺状态
     *
     * @param storeId   storeId
     * @param isOpenWxa 是否开通小程序,0未开通(正常)1已开通(正常),2冻结(手工关闭)
     * @author Charlie
     * @date 2018/12/17 20:17
     */
    @Override
    public void openWxaStatus(Long storeId, Integer isOpenWxa) {
        logger.info ("修改店铺状态 storeId={},isOpenWxa={}", storeId, isOpenWxa);
        StoreBusiness store = storeMapper.selectByPrimaryKey (storeId);
        ErrorHelper.declareNull (store, "没有用户信息");

        Integer historyStatus = store.getIsOpenWxa ();
        if (historyStatus.equals (isOpenWxa)) {
            logger.info ("一致");
            return;
        }

        StoreBusiness updInfo = new StoreBusiness ();
        updInfo.setId (storeId);
        updInfo.setIsOpenWxa (isOpenWxa);
        int rec = storeMapper.updateByPrimaryKeySelective (updInfo);
        ErrorHelper.declare (rec == 1, "修改店铺状态失败");
    }


    /**
     * 初始化小程序开通时间
     *
     * @param storeId      用户id
     * @param wxaOpenTime  开通时间
     * @param wxaCloseTime 结束时间
     * @param force        强制更新
     * @author Charlie
     * @date 2018/12/17 20:46
     */
    @Override
    public void initWxaOpenTime(Long storeId, Long wxaOpenTime, Long wxaCloseTime, Boolean force) {
        logger.info ("初始化小程序开通时间 storeId={},wxaOpenTime={},wxaCloseTime={},force={}");
        StoreBusiness store = storeMapper.selectByPrimaryKey (storeId);
        ErrorHelper.declareNull (store, "没有用户信息");

        if (! force) {
            Long closeTime = store.getWxaCloseTime ();
            Long openTime = store.getWxaOpenTime ();
            if (closeTime > 0 && openTime > 0) {
                logger.info ("账户已开通过,不允许多次初始化时间");
                return;
            }
        }

        StoreBusiness updInfo = new StoreBusiness ();
        updInfo.setId (storeId);
        updInfo.setWxaOpenTime (wxaOpenTime);
        updInfo.setWxaCloseTime (wxaCloseTime);
        storeMapper.updateByPrimaryKeySelective (updInfo);
    }


    /**
     * 用户店铺状态
     *
     * @param storeId storeId
     * @return com.e_commerce.miscroservice.commons.entity.user.StoreShopVo
     * @author Charlie
     * @date 2018/12/18 10:23
     */
    @Override
    public StoreShopVo storeShopStatus(Long storeId) {
        StoreBusiness store = selectById (storeId);
        return StoreShopVo.create (storeId, store.getIsOpenWxa (), store.getWxaCloseTime (), store.getWxaOpenTime (), store.getWxaBusinessType ());
    }


    /**
     * 是否可开通专享店铺
     *
     * @param storeId storeId
     * @return boolean
     * @author Charlie
     * @date 2018/12/18 10:32
     */
    @Override
    public boolean isCanOpenSelfShop(Long storeId) {
        StoreBusiness store = selectById (storeId);
        return ! memberDao.cannotBuyWxaShop (store);
    }




    /**
     * 店铺是否可用
     *
     * @param storeId storeId
     * @return boolean true:可用
     * @author Charlie
     * @date 2018/12/19 16:17
     */
    @Override
    public Map<String, Object> checkStoreShop(Long storeId) {
        StoreBusiness store = storeMapper.selectByPrimaryKey (storeId);
        if (store == null) {
            logger.warn ("没有找到店铺 storeId={}", storeId);
            throw ErrorHelper.me ("未找到店铺信息");
        }

        Map<String, Object> shop = new HashMap<> (2);
        StoreShopVo status = StoreShopVo.create (storeId, store.getIsOpenWxa (), store.getWxaCloseTime (), store.getWxaOpenTime (), store.getWxaBusinessType ());
        try {
            //1共享店铺,2专项店铺
            boolean isShare = status.isShareShopOrSelfShopSafe ();
            shop.put ("shopType", isShare ? 1 : 2);
            //1可用
            shop.put ("isCanUse", 1);
        } catch (Exception e) {
            //0不可用
            shop.put ("isCanUse", 0);
        }
        return shop;
    }

    @Override
    public String demo(Long storeId) {
        logger.info ("开始生成二维码");
        /**
         * @Author:胡坤
         * @GreateTime:2019年1月15日09:22:14
         *
         * 开启线程去生成二维码
         */
        List list=new ArrayList();
        String fileName ="publicQrCode"+System.currentTimeMillis()+".jpg";
        executorService.addTask(
                new ExecutorTask() {

                    @Override
                    public void doJob() {
                        try {
                            String path="";
                            Map<String, Object> auditEntity = storeMapper.getShareShopLoginQr(storeId);
                            logger.info("店铺资料={}", auditEntity);
                            if (auditEntity == null) {
                                path = "";
                            }else {
                                path=(String) auditEntity.get("shareQcCodeUrl");
                                if (StringUtils.isBlank(path)) {
                                    logger.info("初始化共享店铺二维码start...storeId"+inShopStoreId);
                                    path= geneShareQrImgDemo(fileName, storeId);
                                    logger.info("生成二维码返回的地址:"+path);
                                }
                            }

                            storeMapper.updateUrlNew(storeId,path);

                        } catch (Exception e) {
                            list.add("1");
                            logger.info("二维码生成失败,准备再次生成");
                            //如果生成二维码失败那么就继续生成
                            if (list.size()<=5){//很重要一定要进行判断!!!!!!!
                                String path=null;
                                storeMapper.updateUrlNew(storeId,path);
                                doJob();
                            }else {
                                logger.info("生成5次都还没有成功,进行短信通知");//发送短信还没做成功
                                ErrorHelper.declare (list.size()<=5, "门店二维码生成失败,请联系客服");
                                return;
                            }
                        }
                    }
                }

        );

        return null;
    }

    @Override
    public void check() {
        List<Long> list = storeMapper.selectAllStoreId();
        for (Long storeId : list) {
            Map map=new HashMap();
            map.put("storeId",storeId);
            HttpClientUtils.get("https://online.yujiejie.com/user/user/shopInShop/demo",map);
        }
    }

    /**
     * 测试
     */
    @Override
    public void deleteUrl() {
        storeMapper.updeteUrl();
    }

    private synchronized  String geneShareQrImgDemo(String fileName, Long storeId) {
        String path="";
        String url =weixinServiceUrl+"/third/findAccessTokenByAppId";
        Map<String, String> param = new HashMap<>(6);
        List<StoreWxa> storeWxas = storeMapper.selectStoreWxaAppidList(inShopStoreId);
        StoreWxa storeWxa = null;
        if (storeWxas.size()>0) {
            storeWxa = storeWxas.get(0);
            param.put("appId",storeWxa.getAppId());
            logger.info("appId="+storeWxa.getAppId());
            logger.info("url="+url);
            logger.info("parm="+param);
            String token = HttpUtils.sendGet(url, param);
            logger.info("token = " + token);
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("scene", "storeId=" + storeId + "&type=1");
            paramMap.put("page", "page/index/index");
            paramMap.put("width", "430");
            InputStream inputStream = HttpClientUtils.postInputStream(String.format(WXA_CODE_URL, token), JSONObject.toJSONString(paramMap));
            try{
                int inputSize = inputStream.available();
                //标准的是1600多 为了防止二维码生成失效 所以定义一个大小来进行判断
                if (inputSize<14000){
                    throw new Exception();
                }
                //上传
                path = ossKit.simpleUpload (
                        inputStream,
                        Stream.of("yjj-img-main", "customer/").collect(Collectors.toList()),
                        fileName
                );
            }catch (Exception e){
                logger.info("上传图片失败");
            }finally {
                IOUtils.close (inputStream);
                return path;
            }
        }else {
            return path;
        }
    }

    /**
     * 根据店中店的用户id,查询门店的用户id
     *
     * @param inShopMemberId inShopMemberId
     * @author Charlie
     * @date 2018/12/13 19:55
     */
    private StoreBusiness findStoreByInShopMemberId(Long inShopMemberId) {
        //查询openId  店中店用户id-->店中店用户的openId-->用openId查询用户的store
        ShopMember inShopMember = shopMemberDao.findById (inShopMemberId);
        ErrorHelper.declareNull (inShopMember, "没有店中店用户信息");

        //店中店openId
        String inShopOpenId = inShopMember.getBindWeixin ();

        //用户对应APP账号
        StoreBusinessVo userQuery = new StoreBusinessVo ();
        userQuery.setInShopOpenId (inShopOpenId);
        return selectOne (userQuery);
    }


//    /**
//     * 生成店中店二维码<>写在老系统了</>
//     *
//     * @param storeId storeId
//     * @author Charlie
//     * @date 2018/12/13 11:39
//     */
//    public String generateInShopQcImg(Long storeId, boolean isForce) {
//
//        StoreWxaShopAuditData auditData = storeWxaShopAuditDataDao.findNormal (storeId, 1);
//        ErrorHelper.declareNull (auditData, "没有店中店资料信息");
//
//        StoreBusiness store = storeMapper.selectByPrimaryKey (storeId);
//        ErrorHelper.declareNull (store, "没有店铺资料");
//
//        String realPath = webApplicationContext.getServletContext ().getRealPath (IN_SHOP_SHARE_QC_IMG);
//        FileUtils.createDir (realPath);
//        String img = realPath + File.separator + System.currentTimeMillis () + ".jpg";
//
//        //生成磁盘文件
//        File file = null;
//        String qrCodeUrl;
//        try {
//            ImgQrUtil.createSimpleQr ("http://www.baidu.com/s?word=china", 300, 300, img);
//            file = new File (img);
//            ErrorHelper.declare (file.exists (), "生成二维码失败");
//            qrCodeUrl = ossKit.simpleUpload (file.getAbsolutePath (), inShopWxaQcImgOssPath, generateFileNameOnOss (store.getPhoneNumber (), 400, "inviteInShopLogin"));
//            logger.info ("阿里云图片地址 ossImgUrl={}", qrCodeUrl);
//        } finally {
//            if (file != null) {
//                file.delete ();
//            }
//        }
//
//        StoreWxaShopAuditData updData = new StoreWxaShopAuditData ();
//        updData.setId (auditData.getId ());
//        updData.setShareQrCodeUrl (qrCodeUrl);
//        int rec = storeWxaShopAuditDataDao.updateById (updData);
//        ErrorHelper.declare (rec == 1, "更新二维码失败");
//        return qrCodeUrl;
//    }

}
