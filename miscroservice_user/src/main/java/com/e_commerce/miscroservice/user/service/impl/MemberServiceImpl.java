package com.e_commerce.miscroservice.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.entity.application.user.Member;
import com.e_commerce.miscroservice.commons.entity.application.user.MemberLog;
import com.e_commerce.miscroservice.commons.entity.application.user.StoreBusinessVo;
import com.e_commerce.miscroservice.commons.entity.user.MemberOperatorRequest;
import com.e_commerce.miscroservice.commons.entity.user.StoreWxaShopAuditDataQuery;
import com.e_commerce.miscroservice.commons.enums.SystemPlatform;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.DebugUtils;
import com.e_commerce.miscroservice.commons.utils.HttpUtils;
import com.e_commerce.miscroservice.user.dao.MemberDao;
import com.e_commerce.miscroservice.user.dao.StoreBusinessDao;
import com.e_commerce.miscroservice.user.dao.StoreWxaShopAuditDataDao;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;
import com.e_commerce.miscroservice.user.entity.StoreWxaShopAuditData;
import com.e_commerce.miscroservice.user.entity.support.MemberPackageVo;
import com.e_commerce.miscroservice.user.entity.support.ValidTimeQueueHelper;
import com.e_commerce.miscroservice.user.mapper.MemberLogMapper;
import com.e_commerce.miscroservice.user.mapper.MemberMapper;
import com.e_commerce.miscroservice.user.mapper.StoreBusinessMapper;
import com.e_commerce.miscroservice.user.service.store.MemberService;
import com.e_commerce.miscroservice.user.service.store.StoreBusinessService;
import com.e_commerce.miscroservice.user.service.system.DataDictionaryService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.e_commerce.miscroservice.user.entity.support.ValidTimeQueueHelper.buildValidTimeQueue;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 19:47
 * @Copyright 玖远网络
 */
@Service
public class MemberServiceImpl implements MemberService {

    private static final String BUY_MEMBER_GROUP_CODE = "memberPackageType";
    private static final String BUY_MEMBER_TYPE = "\"memberType\":\"%s\"";
    private static Log logger = Log.getInstance(MemberServiceImpl.class);

    @Value( "${old.sys.jstore.url}" )
    private String jstoreUrl;

    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private StoreWxaShopAuditDataDao storeWxaShopAuditDataDao;
    /**
     * 未删除
     */
    private static final int NO_DELETE = 0;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private StoreBusinessMapper storeBusinessMapper;
    @Autowired
    private MemberLogMapper memberLogMapper;

    @Autowired
    private StoreBusinessService storeBusinessService;
    @Autowired
    private StoreBusinessDao storeBusinessDao;


    /**
     * 根据用户id查询 会员信息
     * <p>
     * 如果会员已过期,已删除,返回null
     * </p>
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public Member findValidMemberByUserId(SystemPlatform systemPlatform, Long userId) {
        logger.info("查询用户={} 会员信息", userId);
        Member member = findMemberByUserId(systemPlatform, userId);
        if (member == null || member.getDelState() != 0 || member.getEndTime() < System.currentTimeMillis()) {
            return null;
        }
        return member;
    }


    /**
     * 根据用户id查询 会员信息(统一入口)
     *
     * @param systemPlatform systemPlatform
     * @param userId userId
     * @return com.yujj.entity.product.Member
     * @author Charlie
     * @date 2018/8/16 10:45
     */
    @Override
    public Member findMemberByUserId(SystemPlatform systemPlatform, Long userId) {
        logger.info("查询用户={} 会员refreshExpirationTime信息", userId);
        Member member = memberDao.findMember(userId, systemPlatform);
        //查询会员是否过期,更新过期信息
        return refreshExpirationTime(member, 0);
    }


    /**
     * 刷新会员过期时间
     *
     * @param member member
     * @return 是否更新了 true 更新
     * @author Charlie
     * @date 2018/8/16 7:53
     */
    private Member refreshExpirationTime(Member member, int max) {
        if (member == null) {
            return member;
        }
        if (max == 10) {
            //最大递归10次,绝对不能有无限循环
            return member;
        }

        ValidTimeQueueHelper helper = ValidTimeQueueHelper.instance(member);
        boolean hasDirtyEndTime = helper.isHasDirtyEndTime();
        if (hasDirtyEndTime) {
            final MemberLog log = MemberLog.createLog(member);
            helper.clearExpirationTime();
            Long endTime = helper.getEndTime();
            Integer type = helper.getMemberPackageType();
            String validTimeQueue = helper.getValidTimeQueue();
            Long id = member.getId();
            int rec = memberMapper.updateEndTime(id, type, endTime, 0D, validTimeQueue, member.getValidTimeQueue());
            if (rec != 1) {
                throw ErrorHelper.me("过期会员更新账户过期时间失败");
            }
            logger.info("过期会员更新账户过期时间成功 memberPackageType[{}].endTime[{}].totalMoney[{}].validTimeQueue[{}].historyValidTimeQueue[{}]", type, endTime, 0, validTimeQueue, member.getValidTimeQueue());

            member.setValidTimeQueue(validTimeQueue);
            member.setType(type);
            member.setEndTime(endTime);

            log.setSource("重设用户过期时间");
            memberLogMapper.insertSelective(log);
        }

        Long endTime = member.getEndTime();
        if (endTime < 1 || System.currentTimeMillis() < endTime) {
            return member;
        }
        else {
            //继续刷新
            return refreshExpirationTime(member, max+1);
        }
    }


    /**
     * 用户成功购买会员套餐
     *
     * @param systemPlatformCode 系统平台 暂时都只是1
     * @author Charlie
     * @date 2018/8/16 7:37
     */
    public Member buyMemberPackageOK(Integer systemPlatformCode, StoreBusiness store, MemberOperatorRequest request) {
        Long userId = store.getId();
        SystemPlatform sys = SystemPlatform.build(systemPlatformCode);
        Double totalMoney = request.getTotalMoney();
        Integer memberPackageType = request.getType();
        String orderNo = request.getOrderNo();
        logger.info("购买会员服务成功开始更新,更新用户会员和小程序信息start ===> systemPlatformCode = [" + systemPlatformCode + "], userId = [" + userId + "], totalMoney = [" + totalMoney + "], memberPackageType = [" + memberPackageType + "], orderNo = [" + orderNo + "]");
        if (sys == null) {
            throw ErrorHelper.me("目前不支持的其他系统购买会员");
        }

        MemberPackageVo memberPackageVo = new MemberPackageVo(memberPackageType, request.getTimeUnit(), request.getTimeValue());
        Member member = findMemberByUserId(SystemPlatform.STORE, userId);
        MemberLog log;
        if (member == null) {
            //没有就创建
            member = createMemberAccount(SystemPlatform.STORE, userId, totalMoney, memberPackageVo, 1);
            //记录日志
            log = MemberLog.createLog(member);
            log.setSource("用户成功购买会员套餐,创建一个新的会员账户");
        } else {
            //用户会员续费
            accountRenew(member, totalMoney, memberPackageVo);
            //记录日志
            log = MemberLog.createLog(member);
            log.setSource("用户成功购买会员套餐,用户会员账户续费");
        }

        //更新小程序服务到期时间
        if (sys == SystemPlatform.STORE) {
            logger.info("购买会员套餐,更新小程序关闭日期start....");
            Long wxaCloseTime = store.getWxaCloseTime();
            wxaCloseTime = wxaCloseTime == null ? 0 : wxaCloseTime;
            Calendar calendar = Calendar.getInstance();
            //是有已有小程序
            boolean firstBuyWeiChat = wxaCloseTime == 0 || wxaCloseTime < System.currentTimeMillis();
            if (firstBuyWeiChat) {
                //ignore 首次购买, 客服设置时间
            } else {
                //延时
                calendar.setTimeInMillis(wxaCloseTime);
                calendar.add(Calendar.YEAR, 1);
                int rec = storeBusinessMapper.updateWxaCloseTime(store.getId(), calendar.getTimeInMillis(), wxaCloseTime, System.currentTimeMillis());
                if (rec != 1) {
                    logger.error("更新用户小程序关闭时间失败 storeId[{}].预计设置时间[{}]", store.getId(), calendar.getTimeInMillis());
                    throw ErrorHelper.me("更新用户小程序关闭时间失败");
                }
                logger.info("更新用户小程序关闭时间成功 storeId[{}].预计设置时间[{}]", store.getId(), calendar.getTimeInMillis());
            }
        }


        log.setOrderNo(orderNo);
        memberLogMapper.insertSelective(log);
        return member;
    }


    /**
     * 购买会员
     *
     * @param oprRequest memberOperatorRequest
     * @return com.e_commerce.miscroservice.commons.entity.application.user.Member
     * @date 2018/9/30 16:51
     */
    public void buyMember(MemberOperatorRequest oprRequest, StoreBusiness user) {
        //购买
        StoreBusiness store = user;
        MemberOperatorRequest request = oprRequest;
        Long userId = store.getId();
        SystemPlatform sys = SystemPlatform.build(SystemPlatform.STORE.getCode());
        Double totalMoney = request.getTotalMoney();
        Integer memberPackageType = request.getType();
        String orderNo = request.getOrderNo();
        logger.info("购买会员服务成功开始更新,更新用户会员和小程序信息start ===> userId = [" + userId + "], totalMoney = [" + totalMoney + "], memberPackageType = [" + memberPackageType + "], orderNo = [" + orderNo + "]");
        if (sys == null) {
            throw ErrorHelper.me("目前不支持的其他系统购买会员");
        }

        MemberPackageVo memberPackageVo = new MemberPackageVo(memberPackageType, request.getTimeUnit(), request.getTimeValue());
        Member member = findMemberByUserId(SystemPlatform.STORE, userId);
        MemberLog log;
        if (member == null) {
            //没有就创建
            member = createMemberAccount(SystemPlatform.STORE, userId, totalMoney, memberPackageVo, 1);
            //记录日志
            log = MemberLog.createLog(member);
            log.setSource("用户成功购买会员套餐,创建一个新的会员账户");
        } else {
            //用户会员续费
            accountRenew(member, totalMoney, memberPackageVo);
            //记录日志
            log = MemberLog.createLog(member);
            log.setSource("用户成功购买会员套餐,用户会员账户续费");
        }

        //更新小程序服务到期时间 店中店的会员服务时间提到外层方法做,专享店铺的还没维护!!!
//        if (sys == SystemPlatform.STORE) {
//            logger.info("购买会员套餐,更新小程序关闭日期start....");
//            Long wxaCloseTime = store.getWxaCloseTime();
//            wxaCloseTime = wxaCloseTime == null ? 0 : wxaCloseTime;
//            Calendar calendar = Calendar.getInstance();
//            //是有已有小程序
//            boolean firstBuyWeiChat = wxaCloseTime == 0 || wxaCloseTime < System.currentTimeMillis();
//            if (firstBuyWeiChat) {
//                //ignore 首次购买, 客服设置时间
//            } else {
//                //延时, 9800,19800元延期1年,先写在这里(建议在字典表加字段)
//                if (memberPackageType.equals(2) || memberPackageType.equals(3) || memberPackageType.equals(7) || memberPackageType.equals(8)) {
//                    calendar.setTimeInMillis(wxaCloseTime);
//                    calendar.add(Calendar.YEAR, 1);
//                    int rec = storeBusinessMapper.updateWxaCloseTime(store.getId(), calendar.getTimeInMillis(), wxaCloseTime, System.currentTimeMillis());
//                    if (rec != 1) {
//                        logger.error("更新用户小程序关闭时间失败 storeId[{}].预计设置时间[{}]", store.getId(), calendar.getTimeInMillis());
//                        throw ErrorHelper.me("更新用户小程序关闭时间失败");
//                    }
//                    logger.info("更新用户小程序关闭时间成功 storeId[{}].预计设置时间[{}]", store.getId(), calendar.getTimeInMillis());
//                }
////                log.setBeforeWxClosedTime(wxaCloseTime);
////                log.setAfterWxClosedTime(calendar.getTimeInMillis());
//            }
//        }

        logger.info("修改会员 phone={}，memberId={}", oprRequest.getPhone(), member.getId());
        Member otherInfo = new Member();
        //购买后的一些会员记录更新
        if (StringUtils.isBlank(member.getCanal())) {
            //记录首次购买
            member.setCanal(oprRequest.getCanal());
        }
        otherInfo.setMemberLevel(oprRequest.getMemberLevel());
        otherInfo.setCity(oprRequest.getCity());
        otherInfo.setDistrict(oprRequest.getDistrict());
        otherInfo.setProvince(oprRequest.getProvince());
        otherInfo.setName(oprRequest.getName());
        MybatisOperaterUtil.getInstance().
                update(otherInfo, new MybatisSqlWhereBuild(Member.class).eq(Member::getId, member.getId()));
//        memberMapper.updateByPrimaryKeySelective(otherInfo);

        log.setOrderNo(orderNo);
        memberLogMapper.insertSelective(log);
    }


//    /**
//     * 购买会员
//     *
//     * @param oprRequest memberOperatorRequest
//     * @return com.e_commerce.miscroservice.commons.entity.application.user.Member
//     * @date 2018/9/30 16:51
//     */
//    @Transactional( rollbackFor = Exception.class )
//    @Override
//    public Member buyMember(MemberOperatorRequest oprRequest) {
//            StoreBusinessVo query = new StoreBusinessVo();
//            query.setPhoneNumber(oprRequest.getPhone());
//        StoreBusiness user = storeBusinessMapper.selectOne(query);
//
//        boolean isFirstBuy = true;
//        if (user != null) {
//            Member history = findMemberByUserId(SystemPlatform.STORE, user.getId());
//            isFirstBuy = history == null;
//            boolean isCanBuyMember = isCanBuyMember(history);
//            ErrorHelper.declare(isCanBuyMember, "不能多次购买会员");
//        } else {
//            logger.info("用户未注册 注册新用户");
//            user = StoreBusinessDao.buildDefaultStore();
//            user.setPhoneNumber(oprRequest.getPhone());
//            storeBusinessMapper.insertSelective(user);
//            user.setBusinessNumber(user.getId() + 800000000);
//            storeBusinessMapper.updateByPrimaryKeySelective(user);
//            logger.info("注册会员,创建一个新的用户 userId={}", user.getId());
//        }
//
//        //购买
//        Member member = buyMemberPackageOK(SystemPlatform.STORE.getCode(), user, oprRequest);
//        logger.info("修改会员 phone={}，memberId={}", oprRequest.getPhone(), member.getId());
//
//        //购买后的一些会员记录更新
//        if (isFirstBuy) {
//            //记录首次购买
//            member.setCanal(oprRequest.getCanal());
//        }
//        member.setMemberLevel(oprRequest.getMemberLevel());
//        member.setCity(oprRequest.getCity());
//        member.setDistrict(oprRequest.getDistrict());
//        member.setProvince(oprRequest.getProvince());
//        member.setName(oprRequest.getName());
//        memberMapper.updateByPrimaryKeySelective(member);
//        return member;
//    }


    /**
     * 购买会员成功
     * <p>店中店购买</p>
     *
     * @param request request
     * @return boolean
     * @author Charlie
     * @date 2018/12/19 10:19
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public boolean buyMemberSuccess(MemberOperatorRequest request) {
        Long storeId = request.getId();
        StoreBusiness store = storeBusinessDao.findById(storeId);
        ErrorHelper.declareNull(store, "没有找到用户信息");

        StoreWxaShopAuditData wxaDraft = storeWxaShopAuditDataDao.findDraftByStoreId(storeId, 1);
        return doOpenMember(request, store, wxaDraft);
    }

    /**
     * 开通会员
     *
     * @param request request
     * @param store store
     * @param wxaDraft store
     * @return boolean
     * @author Charlie
     * @date 2019/1/24 13:35
     */
    private boolean doOpenMember(MemberOperatorRequest request, StoreBusiness store, StoreWxaShopAuditData wxaDraft) {
        logger.info("开通会员 request={}", request);
        Long storeId = store.getId();
        Integer task = request.getTask();
        if (task == null) {
            //扩展
        } else if (task == 1) {
            //=============== 购买会员成功,并且开通店中店 ===============
            //是否已有店铺资料
//            StoreWxaShopAuditData wxaDraft = storeWxaShopAuditDataDao.findDraftByStoreId(storeId, 1);
            ErrorHelper.declareNull(wxaDraft, "没有店中店资料信息");

            //是否可以开通
            boolean canOpenInShop = memberDao.isCanOpenInShop(store);
            ErrorHelper.declare(canOpenInShop, "用户不能开通店中店");

            //开通会员
            buyMember(request, store);
            logger.info("购买会员成功");

            //开通店中店
            request.setShopDraftId(wxaDraft.getId());
            storeBusinessService.openWxaInShop(request, store);
        }

        try {
            String clearMemberCache = jstoreUrl + "/mobile/product/clear";
            HashMap<String, String> param = new HashMap<String, String>(2) {{
                put("storeId", String.valueOf(storeId));
            }};
            logger.info("清空jstore 会员缓存 url={},param={}", clearMemberCache, param);
            new Thread(() -> HttpUtils.sendGet(clearMemberCache, param)).start();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("清空jstore 会员缓存 失败");
        }
        return Boolean.TRUE;
    }


    /**
     * 线下开通共享店铺
     *
     * @param memberReq memberReq
     * @param auditReq auditReq
     * @return boolean
     * @author Charlie
     * @date 2018/12/19 10:38
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public boolean openInShopMemberOffline(MemberOperatorRequest memberReq, StoreWxaShopAuditDataQuery auditReq) {
        String phone = memberReq.getPhone();
        ErrorHelper.declareNull(phone, "没有手机号");

        //用户对应APP账号
        StoreBusinessVo userQuery = new StoreBusinessVo();
        userQuery.setPhoneNumber(phone);
        StoreBusiness store = storeBusinessMapper.selectOne(userQuery);
        if (store == null) {
            logger.info("用户未注册 注册新用户");
            store = StoreBusinessDao.buildDefaultStore();
            store.setPhoneNumber(phone);
            storeBusinessMapper.insertSelective(store);

            Long storeId = store.getId();
            store.setBusinessNumber(storeId + 800000000);
            storeBusinessMapper.updateByPrimaryKeySelective(store);
            logger.info("注册会员,创建一个新的用户 userId={}", storeId);
        }

        //提交店铺资料
        StoreWxaShopAuditData auditData = storeBusinessService.doCommitShopWxaDataByStore(auditReq, store);

        //查询会员
        String memberTypeFilter = String.format(BUY_MEMBER_TYPE, memberReq.getType());
        List<DataDictionary> dictList = dataDictionaryService.getByGroupAndLikeComment(BUY_MEMBER_GROUP_CODE, memberTypeFilter);
        int size = dictList.size();
        logger.info("查询商品会员套餐 size={},memberTypeFilter={}", size, memberTypeFilter);
        ErrorHelper.declare(size == 1, "未找到会员套餐的支付信息");
        DataDictionary dict = dictList.get(0);

        BigDecimal totalFee = new BigDecimal(dict.getVal());
        logger.info("开通店中店会员,支付金额={}", totalFee);
        if (totalFee.compareTo(BigDecimal.ZERO) <= 0) {
            throw ErrorHelper.me("金额错误");
        }
        String memberConfig = dict.getComment();
        logger.info("购买的会员信息 {}", memberConfig);
        ErrorHelper.declareNull(memberConfig, "未找到会员信息");
        JSONObject memberJson = JSONObject.parseObject(memberConfig);
        Integer timeUnit = memberJson.getInteger("timeUnit");
        Integer timeValue = memberJson.getInteger("value");

        //开通会员
        memberReq.setId(store.getId());
        memberReq.setCanal("线下");
        memberReq.setName(auditData.getBossName());
        memberReq.setTimeValue(timeValue);
        memberReq.setTimeUnit(timeUnit);
        memberReq.setTask(1);
        memberReq.setTotalMoney(totalFee.doubleValue());
        return doOpenMember(memberReq, store, auditData);
    }


    /**
     * 更新用户会员状态
     *
     * @param memberId memberId
     * @param delStatus 0 正常  1 删除
     * @return java.lang.String
     * @author Charlie
     * @date 2018/12/19 11:32
     */
    @Override
    public boolean updMemberDelStatus(Long memberId, Integer delStatus) {
        Member upd = new Member();
        upd.setId(memberId);
        upd.setDelState(delStatus);
        int rec = memberDao.updateById(upd);
        ErrorHelper.declare(rec == 1, "更新失败");
        return true;
    }


    /**
     * 用户是否可以购买会员
     *
     * @param member member
     * @return true 可以
     * @author Charlie
     * @date 2018/9/30 17:20
     */
    public static boolean isCanBuyMember(Member member) {
        if (member == null) {
            logger.info("没有买过会员");
            return true;
        }
        if (member.getMemberLevel() == 0) {
            logger.info("是普通用户");
            return true;
        }
        if (member.getEndTime() < System.currentTimeMillis()) {
            logger.info("会员过期");
            return true;
        }
        if (! member.getDelState().equals(0)) {
            logger.info("会员账号被禁用");
            return false;
        }

        Integer type = member.getType();
        if (type == 4) {
            logger.info("购买0元会员");
            return true;
        }
        if (type == 6) {
            logger.info("980可以再次购买");
            return true;
        }
        return false;
    }

    /**
     * 账号续费
     *
     * @param history 账号当前状态
     * @param totalMoney 续费金额
     * @param memberPackageVo 会员套餐详情
     * @author Charlie
     * @date 2018/8/16 8:14
     */
    private void accountRenew(Member history, Double totalMoney, MemberPackageVo memberPackageVo) {
        logger.info("账号续费start...");
        boolean isUserIsMemberNow = history.getEndTime() > System.currentTimeMillis();

        ValidTimeQueueHelper helper;
        if (isUserIsMemberNow) {
            //当前会员期延长
            Integer historyType = history.getType();
            Integer memberType = memberPackageVo.getMemberType();
            boolean isChange = memberType - historyType != 0;
            logger.info("历史会员类型={},购买会员类型={}", historyType, memberType);

            Integer higherType = null;
            if (isChange) {
                String memberTypeFilter = String.format(BUY_MEMBER_TYPE, historyType);
                List<DataDictionary> dictList = dataDictionaryService.getByGroupAndLikeComment(BUY_MEMBER_GROUP_CODE, memberTypeFilter);
                int size = dictList.size();
                ErrorHelper.declare(size == 1, "未找到会员套餐的支付信息");
                BigDecimal historyPrice = new BigDecimal(dictList.get(0).getVal());
                higherType = historyPrice.compareTo(new BigDecimal(String.valueOf(totalMoney))) < 0 ? memberType : historyType;
                logger.info("历史未过期会员价格={},会员续费,购买价格更高的会员类型是={}", historyPrice, higherType);
            }

            helper = ValidTimeQueueHelper.instance(history)
                    .add(memberType, memberPackageVo.getTimeUnit(), memberPackageVo.getValue(), higherType);
            int rec = memberMapper.updateEndTime(history.getId(), helper.getMemberPackageType(), helper.getEndTime(), totalMoney, helper.getValidTimeQueue(), history.getValidTimeQueue());
            if (rec != 1) {
                throw ErrorHelper.me("当前会员期延长, 更新会员账号有效期失败");
            }
            logger.info("当前会员期延长, 更新会员账号有效期成功 memberPackageType[{}].endTime[{}].totalMoney[{}].validTimeQueue[{}].historyValidTimeQueue[{}]", memberPackageVo.getMemberType(), helper.getEndTime(), totalMoney, helper.getValidTimeQueue(), history.getValidTimeQueue());
        } else {
            //过期会员回归
            Integer mType = memberPackageVo.getMemberType();
            helper = ValidTimeQueueHelper.instance(history).add(mType, memberPackageVo.getTimeUnit(), memberPackageVo.getValue(), mType);
            int rec = memberMapper.updateEndTime(history.getId(), memberPackageVo.getMemberType(), helper.getEndTime(), totalMoney, helper.getValidTimeQueue(), history.getValidTimeQueue());
            if (rec != 1) {
                throw ErrorHelper.me("过期会员回归, 更新会员账号有效期失败");
            }
            logger.info("过期会员回归, 更新会员账号有效期成功 memberPackageType[{}].endTime[{}].totalMoney[{}].validTimeQueue[{}].historyValidTimeQueue[{}]", memberPackageVo.getMemberType(), helper.getEndTime(), totalMoney, helper.getValidTimeQueue(), history.getValidTimeQueue());
        }
    }


    /**
     * 创建一个会员账号
     *
     * @param systemPlatform 系统平台
     * @param userId 用户id
     * @param totalMoney 支付(支付金额用实际支付金额)
     * @param memberPackageVo 套餐详情(用户等级会员的类型和会员的有效期)
     * @param level 用户等级
     * @author Charlie
     * @date 2018/8/16 8:05
     */
    private Member createMemberAccount(SystemPlatform systemPlatform, Long userId, Double totalMoney, MemberPackageVo memberPackageVo, Integer level) {
        logger.info("创建一个会员账号 ==> systemPlatform = [" + systemPlatform + "], userId = [" + userId + "], totalMoney = [" + totalMoney + "], memberPackageVo = [" + memberPackageVo.toString() + "], level = [" + level + "]");
        Member member = new Member();
        member.setPlatformType(systemPlatform.getCode());
        member.setUserId(userId);
        member.setMemberLevel(level);
        member.setMoneyTotal(new BigDecimal(String.valueOf(totalMoney)));
        member.setDelState(NO_DELETE);
        member.setType(memberPackageVo.getMemberType());
        Calendar calendar = Calendar.getInstance();
        calendar = ValidTimeQueueHelper.addTime(calendar, memberPackageVo.getTimeUnit(), memberPackageVo.getValue());
        long endTime = calendar.getTimeInMillis();
        member.setEndTime(endTime);
        member.setValidTimeQueue(buildValidTimeQueue(endTime, memberPackageVo.getMemberType()));
        int rec = memberMapper.insertSelective(member);
        if (rec != 1) {
            logger.error("创建会员账号失败 systemPlatform = [" + systemPlatform + "], userId = [" + userId + "]");
            throw ErrorHelper.me("创建会员账号失败");
        }
        logger.info("创建一个新的会员账号成功 systemPlatform = [" + systemPlatform + "], userId = [" + userId + "]");
        return member;
    }


}
