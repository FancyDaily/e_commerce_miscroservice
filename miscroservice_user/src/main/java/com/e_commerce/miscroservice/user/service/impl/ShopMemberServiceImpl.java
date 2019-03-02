package com.e_commerce.miscroservice.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.user.ShopMemberQuery;
import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.wx.Base64;
import com.e_commerce.miscroservice.commons.utils.wx.WxAuthUtil;
import com.e_commerce.miscroservice.user.dao.ShopMemberDao;
import com.e_commerce.miscroservice.user.entity.ShopMember;
import com.e_commerce.miscroservice.user.service.shop.ShopMemberService;
import com.e_commerce.miscroservice.user.service.store.StoreBusinessService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;

import static com.e_commerce.miscroservice.commons.utils.wx.WxAuthUtil.acquireSessionkey;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/14 22:32
 * @Copyright 玖远网络
 */
@Service
public class ShopMemberServiceImpl implements ShopMemberService {

    private Log logger = Log.getInstance(ShopMemberServiceImpl.class);

    @Autowired
    private StoreBusinessService storeBusinessService;
    @Autowired
    private ShopMemberDao shopMemberDao;


    @Value("${user.jfinal.sys.url}")
    private String jfinalSysUrl;


    @Override
    public ShopMember selectOne(ShopMemberQuery query) {
        logger.info("selectOne 查询 query={}", query);
        ShopMember shopMember = shopMemberDao.selectOne(query);
        logger.info("查询会员结果={}", shopMember);
        return shopMember;
    }


    /**
     * 用户进店,获取memberId
     *
     * @param currentUserId 跳转的小程序用户id
     * @param storeId 跳转的门店id
     * @param inShopMemberId 店中店中端小程序用户id
     * @return com.e_commerce.miscroservice.user.entity.ShopMember
     * @author Charlie
     * @date 2018/12/12 9:35
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public Map<String, Object> findByInShopOpenIdIfNullCreateNew(Long currentUserId, Long storeId, Long inShopMemberId) {
        logger.info("用户进店 currentUserId={},storeId={},inShopMemberId={}", currentUserId, storeId, inShopMemberId);

        Map<String, Object> storeShop = storeBusinessService.checkStoreShop(storeId);
        Integer isCanUse = (Integer) storeShop.get("isCanUse");
        if (! ObjectUtils.nullSafeEquals(isCanUse, 1)) {
            throw ErrorHelper.me("店铺不可用");
        }

        ShopMember inShopMember = shopMemberDao.findById(inShopMemberId);
        ErrorHelper.declareNull(inShopMember, "没有店中店用户信息");

        //跳转的小程序用户
        ShopMember currentMember = shopMemberDao.findById(currentUserId);
        ErrorHelper.declareNull(currentMember, "没有找到用户信息");
        String currentOpenId = currentMember.getBindWeixin();
        //查找跳转小程序下对应storeId的小程序用户
        ShopMember history = shopMemberDao.findByInShopMemberIdAndStoreId(inShopMemberId, storeId);
        if (history == null) {
            logger.info("根据inShopMemberId查询 history={}", history);
            //其次根据openId和storeId查询
            history = shopMemberDao.findByCurrentOpenIdAndStoreId(currentOpenId, storeId);
            logger.info("根据openId好storeId查询, history={}", history);
            if (history != null) {
                Long historyInShopMemberId = history.getInShopMemberId();
                if (historyInShopMemberId == null || historyInShopMemberId <= 0) {
                    logger.info("同步店中店信息到用户信息");
                    ShopMember upd = new ShopMember();
                    upd.setInShopMemberId(inShopMemberId);
                    upd.setInShopOpenId(inShopMember.getBindWeixin());
                    upd.setId(history.getId());
                    int rec = shopMemberDao.updateById(upd);
                    ErrorHelper.declare(rec == 1, "更新账号店中店信息失败");
                }
            }
        }
        else {
            String inShopOpenId = history.getInShopOpenId();
            if (StringUtils.isBlank(inShopOpenId) || !inShopOpenId.equals(inShopMember.getBindWeixin()) ) {
                logger.info("更新openId");
                ShopMember upd = new ShopMember();
                upd.setInShopOpenId(inShopMember.getBindWeixin());
                upd.setId(history.getId());
                int rec = shopMemberDao.updateById(upd);
                ErrorHelper.declare(rec == 1, "更新账号店中店信息失败");
            }
        }

        //都没查到就创建新账号
        Long userId;
        if (history == null) {
            //查找跳转小程序下对应storeId的小程序用户
            logger.info("创建新的店中店用户");

            //创建该跳转小程序下的新账号
            long current = System.currentTimeMillis();
            ShopMember updShopMember = new ShopMember();
            updShopMember.setId(currentMember.getId());
            updShopMember.setUpdateTime(current);
            int upd = shopMemberDao.updateById(updShopMember);
            ErrorHelper.declare(upd == 1, "用户状态异常,请稍后再试");

            String inShopOpenId = currentMember.getBindWeixin();
            ErrorHelper.declareNull(inShopOpenId, "没有找到用户微信信息");

            ShopMember newUser = new ShopMember();
            newUser.setCreateTime(current);
            newUser.setUpdateTime(current);
            newUser.setBindPhone(currentMember.getBindPhone());
            newUser.setBindWeixin(currentMember.getBindWeixin());
            newUser.setInShopOpenId(inShopMember.getBindWeixin());
            newUser.setInShopMemberId(inShopMemberId);
            newUser.setSex(currentMember.getSex());
            newUser.setStoreId(storeId);
            newUser.setUserIcon(currentMember.getUserIcon());
            newUser.setUserNickname(currentMember.getUserNickname());
            userId = createShopMember(newUser, 1);
        } else {
            userId = history.getId();
        }
        HashMap<String, Object> res = new HashMap<>(2);
        res.put("memberId", userId);
        res.put("storeId", storeId);
        res.put("storeShop", storeShop);
        return res;
    }




    /**
     * 更细用户微信手机号
     *
     * @param appId appId
     * @param sessionId sessionId
     * @param encryptedData 加密数据
     * @param iv 加密数据
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/26 19:05
     */
    @Override
    public Map<String, Object> addUpdWxPhone(String appId, String sessionId, Long shopMemberId, String encryptedData, String iv) {
        logger.info("获取用户微信手机号,appId={},sessionId={},shopMemberId={},encryptedData={},iv={}", appId, sessionId, shopMemberId, encryptedData, iv);
        //获取sessionKey
        String skRes = acquireSessionkey(jfinalSysUrl+"/third/findSessionKeyBySessionId", appId, sessionId);
        logger.info("获取sessionKey={}", skRes);
        ErrorHelper.declareNull(skRes, "获取sessionKey是空");

        String wxPhoneJson;
        try {
            logger.info("开始解密密文");
            wxPhoneJson = WxAuthUtil.decrypt(skRes, iv, encryptedData);
        } catch (Exception e) {
            throw ErrorHelper.me("获取手机号解密失败");
        }
        logger.info("获取手机号={}", wxPhoneJson);
        /*
         * {
         * "phoneNumber":"18758196668", 用户绑定的手机号（国外手机号会有区号）
         * "purePhoneNumber":"18758196668", 没有区号的手机号
         * "countryCode":"86", 区号
         * "watermark":{"timestamp":1545897751,"appid":"wx23d3c43d2f0428c1"}
         * }
         */
        JSONObject phoneJsonObj = JSONObject.parseObject(wxPhoneJson);
        String phone = phoneJsonObj.getString("purePhoneNumber");

        //账号同步
        ShopMember user = shopMemberDao.findById(shopMemberId);
        ErrorHelper.declareNull(user, "没有用户信息");
        ShopMember upd = new ShopMember();
        upd.setWxPhone(phone);
        upd.setId(shopMemberId);
        int rec = shopMemberDao.updateById(upd);
        ErrorHelper.declare(rec == 1, "账号状态失败");

        return new HashMap<String, Object>(2){
            {
                put("wxPhone", phone);
            }
        };
    }



    /**
     * 获取用户信息
     *
     * @param shopMemberId shopMemberId
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/27 13:07
     */
    @Override
    public Map<String, Object> userInfo(Long shopMemberId) {
        ShopMember user = shopMemberDao.findById(shopMemberId);
        if (user == null) {
            return EmptyEnum.map();
        }

        return new HashMap<String, Object>(4) {
            {
                String wxPhone = user.getWxPhone();
                put("wxPhone", StringUtils.isBlank(wxPhone)?StateEnum.NORMAL:wxPhone);
                put("userId", user.getId());
                put("storeId", user.getStoreId());
                put("sex", user.getSex());
            }
        };
    }
    /**
     * 根据id查询会员
     * @param memberId
     * @return
     */
    @Override
    public ShopMember findShopMemberById(Long memberId) {
        ShopMember shopMember = shopMemberDao.findById(memberId);
        return shopMember;
    }


    /**
     * 所有创建新用户的DB操作,建议都重这里调用,方便同意管理
     *
     * @param newUser newUser
     * @param fromService 1,来源于店中店的,创建小程序用户
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/12/12 10:09
     */
    private Long createShopMember(ShopMember newUser, int fromService) {
        if (fromService == 1) {
            logger.info("来源于店中店的,创建小程序用户");
        }
        int rec = shopMemberDao.save(newUser);
        ErrorHelper.declare(rec == 1, "创建小程序账号失败");
        return newUser.getId();
    }
}
