package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.entity.user.StoreShopVo;
import com.e_commerce.miscroservice.commons.enums.user.StoreBillEnums;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.NumberUtils;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.user.dao.StoreBusinessAccountDao;
import com.e_commerce.miscroservice.user.entity.ShopMember;
import com.e_commerce.miscroservice.user.rpc.ShopMemberOrderRpcService;
import com.e_commerce.miscroservice.user.service.store.StoreBusinessAccountService;
import com.e_commerce.miscroservice.user.service.store.StoreBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 账户
 *
 * @author hyf
 * @date 2018年12月13日
 */
@Service
public class StoreBusinessAccountServiceImpl implements StoreBusinessAccountService {

    private Log logger = Log.getInstance(StoreBusinessAccountServiceImpl.class);

    @Resource
    private StoreBusinessAccountDao storeBusinessAccountDao;

    @Autowired
    private StoreBusinessService storeBusinessService;


    /**
     * 更新账户金额 账户日志
     *
     * @param yjjStoreBusinessAccountLog
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateStoreBusinessAccount(YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog) {
        logger.info("更新账号金额 账号日志={}", yjjStoreBusinessAccountLog);
        if (yjjStoreBusinessAccountLog != null && yjjStoreBusinessAccountLog.getUserId() == null) {
            ShopMember shopMember = storeBusinessAccountDao.findStoreBusinessAccountByMemberId(yjjStoreBusinessAccountLog.getMemberId());
            if (shopMember != null && shopMember.getStoreId() != null) {
                yjjStoreBusinessAccountLog.setUserId(shopMember.getStoreId());

            }
        }


        Long userId = null;
        ShopMemberOrder shopMemberOrder = null;
        if (yjjStoreBusinessAccountLog.getAboutOrderNo()!=null){
            logger.info("根据订单号进行查询={}", yjjStoreBusinessAccountLog);
            shopMemberOrder = storeBusinessAccountDao.findOrderByOrderNo(yjjStoreBusinessAccountLog.getAboutOrderNo());
            logger.info("根据订单号进行查询 = shopMemberOrder={}",shopMemberOrder);
            if (shopMemberOrder!=null){
                userId = shopMemberOrder.getStoreId();
            }
        }
        if (shopMemberOrder==null){
            userId = yjjStoreBusinessAccountLog.getUserId();
        }
        yjjStoreBusinessAccountLog.setUserId(userId);
        logger.info("根据订单号进行查询 = yjjStoreBusinessAccountLog={}",yjjStoreBusinessAccountLog);
        // 2018/12/14 3.8.5版本 判断是否为店中店用户 用与判断是否进行添加到流水表中
        StoreShopVo storeShopVo = storeBusinessService.storeShopStatus(userId);

        try {
            if (!storeShopVo.isShareShopOrSelfShopSafe()) {
                logger.warn("更新账单--独享版小程序");
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        YjjStoreBusinessAccount yjjStoreBusinessAccount = storeBusinessAccountDao.findStoreBusinessAccount(userId);
        if (yjjStoreBusinessAccount == null) {
            logger.info("用户账户不存在 新建用户账户");
            YjjStoreBusinessAccount account = new YjjStoreBusinessAccount();
            account.setUserId(userId);
            Integer in = storeBusinessAccountDao.insertStoreBusinessAccount(account);
            if (in<=0){
                logger.warn("创建用户账号失败", userId);
                return 0;
            }
        }


        Integer account = storeBusinessAccountDao.updateStoreBusinessAccount(yjjStoreBusinessAccountLog);
//        添加账户日志
        Integer inset = storeBusinessAccountDao.insertStoreBusinessAccountLog(yjjStoreBusinessAccountLog);
        logger.info("店中店用户小程序生成账单成功={}", yjjStoreBusinessAccountLog);
        return inset;
    }

    /**
     * 查询账户
     *
     * @param userId
     * @return
     */
    @Override
    public YjjStoreBusinessAccount selectStoreBusinessAccount(Long userId) {
        logger.info("查询账号={}", userId);
        YjjStoreBusinessAccount yjjStoreBusinessAccount = storeBusinessAccountDao.findStoreBusinessAccount(userId);
        if (yjjStoreBusinessAccount!=null){
            Double count =  NumberUtils.adds(2, yjjStoreBusinessAccount.getFrozenMoney(),yjjStoreBusinessAccount.getRealUseMoney(),yjjStoreBusinessAccount.getWaitInMoney());
            yjjStoreBusinessAccount.setCountMoney(count);
        }else {
            yjjStoreBusinessAccount = createDefaultAccount();
        }

        return yjjStoreBusinessAccount;
    }
    public YjjStoreBusinessAccount createDefaultAccount(){
        YjjStoreBusinessAccount yjjStoreBusinessAccount = new YjjStoreBusinessAccount();
        yjjStoreBusinessAccount.setCountMoney(0D);
        yjjStoreBusinessAccount.setFrozenMoney(0D);
        yjjStoreBusinessAccount.setUseMoney(0D);
        yjjStoreBusinessAccount.setRealUseMoney(0D);
        yjjStoreBusinessAccount.setWaitInMoney(0D);
        return yjjStoreBusinessAccount;
    }
    /**
     * 查询账户记录
     *
     * @param userId
     * @param page
     * @return
     */
    @Override
    public SimplePage selectStoreBusinessAccountLog(Long userId, Integer page) {
        logger.info("查询账户记录={},page={}", userId, page);

        List<YjjStoreBusinessAccountLog> li = storeBusinessAccountDao.findStoreBusinessAccountLog(userId, page);
        SimplePage<YjjStoreBusinessAccountLog> simplePage = new SimplePage<>(li);
        return simplePage;
    }

    /**
     * 将待结金额转换为可用金额
     *
     * @param userId
     */
    @Override
    public void waitInMoneyToUse(Long userId) {
        //查询出所有需要转换的日志
//        List<YjjStoreBusinessAccountLog> logList = storeBusinessAccountDao.findAllWaitInToUseMoneyLog(userId);

    }

    /**
     * 将所有的都转换
     */
    @Override
    public void waitInMoneyToUseAll() {
        logger.info("批量转换日期={}", TimeUtils.dateFormatString(new Date()));
//        查询所有的
        List<YjjStoreBusinessAccountLog> logList = storeBusinessAccountDao.findAllWaitInToUseMoneyLog();
        YjjStoreBusinessAccountLog log = new YjjStoreBusinessAccountLog();
        Integer monty = TimeUtils.getYearOrMonthOrDay(new Date(), 2);
        // TODO: 2018/12/18 验证 是否已添加过
        for (YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog : logList) {
            log.setUserId(yjjStoreBusinessAccountLog.getUserId());
            log.setOperMoney(yjjStoreBusinessAccountLog.getOperMoney());
            log.setInOutType(StoreBillEnums.PAY.getCode());
            log.setRemarks(StoreBillEnums.SETTLE_ACCOUNTS.getValue() + "-" + monty + "结算资金");
            log.setType(StoreBillEnums.SETTLE_ACCOUNTS_SUCCESS.getCode());
            updateStoreBusinessAccount(log);

            log.setInOutType(StoreBillEnums.INCOME.getCode());
            log.setType(StoreBillEnums.SETTLE_ACCOUNTS_USE_SUCCESS.getCode());
            updateStoreBusinessAccount(log);
        }
        storeBusinessAccountDao.updateAllWaitInToMoney();
    }

    @Override
    public Response checkShopInShop(Long userId) {
        StoreShopVo storeShopVo = storeBusinessService.storeShopStatus(userId);
        // 2018/12/14 3.8.5版本 判断是否为店中店用户 用与判断是否进行添加到流水表中
        try {
            if (!storeShopVo.isShareShopOrSelfShopSafe()) {
                logger.warn("独享版小程序");
                return Response.success(Boolean.FALSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.success(Boolean.TRUE);
    }

    @Override
    public boolean sendGoodsAfter15DaysWaitMoneyIn(String shopMemberOrderNo, Long storeId, Long storeOrderNo) {
        logger.info("发货15天更新账户 shopMemberOrderNo={},storeId={},storeOrderNo={}", shopMemberOrderNo, storeId, storeOrderNo);
        YjjStoreBusinessAccount account = storeBusinessAccountDao.findStoreBusinessAccount(storeId);
        if (account == null) {
            logger.warn("账户不存在");
            return false;
        }

        YjjStoreBusinessAccountLog history = storeBusinessAccountDao.findByAboutOderAndType(shopMemberOrderNo, StoreBillEnums.SETTLE_ACCOUNTS_USE_SUCCESS.getCode());
        if (history != null) {
            logger.warn("已入过账,不可重复入账");
            return false;
        }

        List<YjjStoreBusinessAccountLog> logs = storeBusinessAccountDao.findByAboutOder(shopMemberOrderNo);
        if (logs.isEmpty()) {
            logger.warn("没有入账信息");
            return false;
        }

        //平台退款
        BigDecimal inMoneyOfPlatformRefund = BigDecimal.ZERO;
        YjjStoreBusinessAccountLog platformRefund = storeBusinessAccountDao.findByAboutOderAndType(storeOrderNo + "", StoreBillEnums.APP_REFUND_MONEY_SUCCESS.getCode());
        if (platformRefund != null) {
            inMoneyOfPlatformRefund = BigDecimal.valueOf(platformRefund.getOperMoney());
        }
        logger.info("收入--平台退款给商家的金额={}", inMoneyOfPlatformRefund);

        //收入
        BigDecimal inMoney = logs.stream()
                .filter(log -> {
                    Integer type = log.getType();
                    //C付款
                    if (StoreBillEnums.GOODS_ORDER_SUCCESS.isThis(type)) {
                        logger.info("收入--C端用户下单收入金额={}", log.getOperMoney());
                        return true;
                    }
                    return false;
                }).map(log -> BigDecimal.valueOf(log.getOperMoney())).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal outMoney = logs.stream()
                .filter(log -> {
                    Integer type = log.getType();
                    //平台代发货扣钱
                    if (StoreBillEnums.PLATFORM_INSTEAD_OF_SEND_GOODS_SUCCESS.isThis(type)) {
                        logger.info("支出--平台代发货扣款={}", log.getOperMoney());
                        return true;
                    }
                    if (StoreBillEnums.REFUND_MONEY_SUCCESS.isThis(type) && log.getInOutType().equals(1)) {
                        logger.info("支出--店家退款给C端用户,金额={}", log.getOperMoney());
                        return true;
                    }
                    return false;
                }).map(log -> BigDecimal.valueOf(log.getOperMoney())).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal allWaitMoney = inMoney.add(inMoneyOfPlatformRefund).subtract(outMoney);
        logger.info("这笔订单总共收益={}",allWaitMoney);
        if (allWaitMoney.compareTo(BigDecimal.ZERO) <=0 ) {
            logger.warn("待结算入账,收入未<0 money={}", account);
            return true;
        }

        //记录流水
        YjjStoreBusinessAccountLog newLog = new YjjStoreBusinessAccountLog();
        newLog.setInOutType(0);
        newLog.setRemarks(StoreBillEnums.SETTLE_ACCOUNTS_USE_SUCCESS.getValue());
        newLog.setUserId(storeId);
        newLog.setAboutOrderNo(shopMemberOrderNo);
        newLog.setType(StoreBillEnums.SETTLE_ACCOUNTS_USE_SUCCESS.getCode());
        newLog.setOperMoney(allWaitMoney.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        newLog.setRemainderMoney(BigDecimal.valueOf(account.getWaitInMoney()).subtract(allWaitMoney).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        Integer addRec = storeBusinessAccountDao.insertStoreBusinessAccountLog(newLog);
        if (addRec != 1) {
            logger.warn("记录流水失败");
            return false;
        }

        int updRec = storeBusinessAccountDao.waitMoneyInUse(account.getId(), allWaitMoney.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        if (updRec != 1) {
            logger.warn("更新");
            return false;
        }
        return true;
    }


}
