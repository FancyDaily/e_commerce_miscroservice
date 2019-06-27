package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUserTimeRecord;
import com.e_commerce.miscroservice.commons.enums.application.PaymentEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.user.dao.UserTimeRecordDao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserTimeRecordDaoImpl implements UserTimeRecordDao {
    @Override
    public List<TUserTimeRecord> selectMonthlyTimeRecord(Long userId, Long begin, Long end) {
        return MybatisPlus.getInstance().findAll(new TUserTimeRecord(), new MybatisPlusBuild(TUserTimeRecord.class)
                .groupBefore()
                .eq(TUserTimeRecord::getUserId, userId)
                .or()
                .eq(TUserTimeRecord::getFromUserId,userId)
                .groupAfter()
                .between(TUserTimeRecord::getCreateTime, begin, end)
                .eq(TUserTimeRecord::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public Long insert(TUserTimeRecord record) {
        MybatisPlus.getInstance().save(record);
        return record.getId();
    }

    @Override
    public TUserTimeRecord selectById(Long id){
        return MybatisPlus.getInstance().findOne(new TUserTimeRecord() ,
                new MybatisPlusBuild(TUserTimeRecord.class)
                        .eq(TUserTimeRecord::getId,id));
    }

    /**
     * 查找一个订单的所有流水
     * @param orderId
     * @return
     */
    public List<TUserTimeRecord> selectGetTimeByOrder(Long orderId){
       return MybatisPlus.getInstance().findAll(new TUserTimeRecord() ,
                new MybatisPlusBuild(TUserTimeRecord.class)
                        .eq(TUserTimeRecord::getTargetId , orderId)
                        .eq(TUserTimeRecord::getType , PaymentEnum.PAYMENT_TYPE_ACEPT_SERV.getCode())
                        .eq(TUserTimeRecord::getIsValid , AppConstant.IS_ATTEN_YES));
    }

    /**
     * 查找流水记录 ASC
     * @param userId
     * @return
     */
    @Override
    public List<TUserTimeRecord> selectTimeRecordByUserIdBetweenASC(Long userId,Long begin,Long end) {
        return MybatisPlus.getInstance().findAll(new TUserTimeRecord(),new MybatisPlusBuild(TUserTimeRecord.class)
                .groupBefore()
        .eq(TUserTimeRecord::getUserId,userId)
                .or()
                .eq(TUserTimeRecord::getFromUserId,userId)
                .groupAfter()
        .eq(TUserTimeRecord::getType,PaymentEnum.PAYMENT_TYPE_ACEPT_SERV.getCode())
                .between(TUserTimeRecord::getCreateTime,begin,end)
        .eq(TUserTimeRecord::getIsValid,AppConstant.IS_VALID_YES)
        .orderBy(MybatisPlusBuild.OrderBuild.buildAsc(TUserTimeRecord::getCreateTime)));
    }

    /**
     * 根据用户id、订单ids查找所有流水记录
     * @param userId
     * @param orderIds
     * @return
     */
    @Override
    public List<TUserTimeRecord> selectByUserIdInOrderIds(Long userId, List<Long> orderIds) {
        return MybatisPlus.getInstance().findAll(new TUserTimeRecord(),new MybatisPlusBuild(TUserTimeRecord.class)
                .groupBefore()
                .eq(TUserTimeRecord::getUserId,userId)
                .or()
                .eq(TUserTimeRecord::getFromUserId,userId)
                .groupAfter()
                .in(TUserTimeRecord::getOrderId,orderIds)
                .eq(TUserTimeRecord::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 根据用户id、订单ids查找当天流水记录
     * @param userId
     * @param orderIds
     * @return
     */
    @Override
    public List<TUserTimeRecord> selectDailyByUserIdInOrderIds(Long userId,List<Long> orderIds) {
        long currentTimeMillis = System.currentTimeMillis();
        return MybatisPlus.getInstance().findAll(new TUserTimeRecord(),new MybatisPlusBuild(TUserTimeRecord.class)
                .groupBefore()
                .eq(TUserTimeRecord::getUserId,userId)
                .or()
                .eq(TUserTimeRecord::getFromUserId,userId)
                .groupAfter()
                .in(TUserTimeRecord::getTargetId,orderIds)
                .between(TUserTimeRecord::getCreateTime, DateUtil.getStartStamp(currentTimeMillis),DateUtil.getEndStamp(currentTimeMillis))
                .eq(TUserTimeRecord::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUserTimeRecord> selectByUserId(Long id) {
        return MybatisPlus.getInstance().findAll(new TUserTimeRecord(),new MybatisPlusBuild(TUserTimeRecord.class)
        .eq(TUserTimeRecord::getUserId,id)
        .eq(TUserTimeRecord::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUserTimeRecord> selectByUserIdOrFromUserIdAndTypeBetween(Long userId, int paymentType, long betLeft, long betRight) {
        return MybatisPlus.getInstance().findAll(new TUserTimeRecord(),new MybatisPlusBuild(TUserTimeRecord.class)
                .groupBefore()
                .eq(TUserTimeRecord::getUserId,userId)
                .or()
                .eq(TUserTimeRecord::getFromUserId,userId)
                .groupAfter()
                .eq(TUserTimeRecord::getType, paymentType)
                .between(TUserTimeRecord::getCreateTime,betLeft,betRight)
                .eq(TUserTimeRecord::getIsValid,AppConstant.IS_VALID_YES));
    }
}
