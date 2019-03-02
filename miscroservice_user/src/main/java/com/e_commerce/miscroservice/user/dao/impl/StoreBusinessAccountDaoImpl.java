package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.StoreBusinessAccountDao;
import com.e_commerce.miscroservice.user.entity.ShopMember;
import com.e_commerce.miscroservice.user.mapper.StoreBusinessAccountMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 19:46
 * @Copyright 玖远网络
 */
@Component
public class StoreBusinessAccountDaoImpl implements StoreBusinessAccountDao{

    @Resource
    private StoreBusinessAccountMapper storeBusinessAccountMapper;

    /**
     * 查询账户金额
     * @param userId
     * @return
     */
    @Override
    public YjjStoreBusinessAccount findStoreBusinessAccount(Long userId) {
        YjjStoreBusinessAccount yjjStoreBusinessAccount = MybatisOperaterUtil.getInstance().findOne(new YjjStoreBusinessAccount(),new MybatisSqlWhereBuild(YjjStoreBusinessAccount.class).eq(YjjStoreBusinessAccount::getUserId,userId).eq(YjjStoreBusinessAccount::getDelStatus, StateEnum.NORMAL));
        return yjjStoreBusinessAccount;
    }
    /**
     * 添加 账户记录
     * @param yjjStoreBusinessAccountLog
     * @return
     */
    @Override
    public Integer insertStoreBusinessAccountLog(YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog) {
        Integer integer = MybatisOperaterUtil.getInstance().save(yjjStoreBusinessAccountLog);
        return integer;
    }

    /**
     * 账单记录
     *
     * @param userId
     * @param page
     * @return
     */
    @Override
    public List<YjjStoreBusinessAccountLog> findStoreBusinessAccountLog(Long userId, Integer page) {
        PageHelper.startPage(page,10);
        List<YjjStoreBusinessAccountLog> list = storeBusinessAccountMapper.findStoreBusinessAccountLog(userId);
        return list;
    }

    /**
     * 修改账户信息 根据账单记录
     * @param yjjStoreBusinessAccountLog
     * @return
     */
    @Override
    public Integer updateStoreBusinessAccount(YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog) {

//        暂时所有都 到待结账号中计算
//        if (yjjStoreBusinessAccountLog.getType().equals(GOODS_ORDER_SUCCESS.getCode())
//                ||yjjStoreBusinessAccountLog.getType().equals(StoreBillEnums.SETTLE_ACCOUNTS_SUCCESS.getCode())
//                ||yjjStoreBusinessAccountLog.getType().equals(StoreBillEnums.REFUND_MONEY_SUCCESS.getCode())){
            if (yjjStoreBusinessAccountLog.getInOutType()==0){
                return storeBusinessAccountMapper.updateStoreBusinessAccountWaitInMoneyInto(yjjStoreBusinessAccountLog);

            }else
            if (yjjStoreBusinessAccountLog.getInOutType()==1){
                return storeBusinessAccountMapper.updateStoreBusinessAccountWaitInMoney(yjjStoreBusinessAccountLog);
            }else {
                return 0;
            }
//        }
//        if (yjjStoreBusinessAccountLog.getInOutType()==0){
//            return storeBusinessAccountMapper.updateStoreBusinessAccountUseMoneyInto(yjjStoreBusinessAccountLog);
//
//
//        }else if (yjjStoreBusinessAccountLog.getInOutType()==1){
//            return storeBusinessAccountMapper.updateStoreBusinessAccountUseMoney(yjjStoreBusinessAccountLog);
//
//        }else {
//            return 0;
//        }

    }

    /**
     * 查询所有待结算金额日志
     * @return
     */
    @Override
    public List<YjjStoreBusinessAccountLog> findAllWaitInToUseMoneyLog() {

        return  storeBusinessAccountMapper.findAllWaitInToUseMoneyLog();
    }

    @Override
    public void updateAllWaitInToMoney() {

    }

    @Override
    public ShopMember findStoreBusinessAccountByMemberId(Long memberId) {

        return storeBusinessAccountMapper.findStoreBusinessAccountByMemberId(memberId);
    }

    @Override
    public Integer insertStoreBusinessAccount(YjjStoreBusinessAccount account) {
        Integer in = MybatisOperaterUtil.getInstance().save(account);
        return in;
    }

    @Override
    public YjjStoreBusinessAccountLog findByAboutOderAndType(String orderNo, Integer type) {
        List<YjjStoreBusinessAccountLog> ret = storeBusinessAccountMapper.findByAboutOrderAndType(orderNo, type);
        return ret.isEmpty() ? null : ret.get(0);
    }

    @Override
    public List<YjjStoreBusinessAccountLog> findByAboutOder(String shopMemberOrderNo) {
        return storeBusinessAccountMapper.findByAboutOrderAndType(shopMemberOrderNo, null);
    }

    @Override
    public int waitMoneyInUse(Long id, Double remainingWaitMoney) {
        return storeBusinessAccountMapper.waitMoneyInUse(id, remainingWaitMoney);
    }

    @Override
    public ShopMemberOrder findOrderByOrderNo(String aboutOrderNo) {
        return storeBusinessAccountMapper.findOrderByOrderNo(aboutOrderNo);
    }

}
