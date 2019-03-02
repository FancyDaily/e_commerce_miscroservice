package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.enums.user.StoreBillEnums;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.YjjStoreBusinessAccountLogDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create by hyf on 2018/11/13
 */
@Repository
public class YjjStoreBusinessAccountLogDaoImpl implements YjjStoreBusinessAccountLogDao {

    /**
     * 添加流水日志
     * @param yjjStoreBusinessAccountLog
     * @return
     */
    @Override
    public Integer addOne(YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog) {
        return MybatisOperaterUtil.getInstance().save(yjjStoreBusinessAccountLog);
    }
    /**
     * 根据订单号查询订单 包含冻结状态
     * @param out_trade_no
     * @return
     */
    @Override
    public YjjStoreBusinessAccountLog findOneByOrderNo(String out_trade_no) {
        return MybatisOperaterUtil.getInstance().findOne(
                new YjjStoreBusinessAccountLog(),
                new MybatisSqlWhereBuild(YjjStoreBusinessAccountLog.class).
                        eq(YjjStoreBusinessAccountLog::getOrderNo,out_trade_no).eq(YjjStoreBusinessAccountLog::getDelStatus,StateEnum.NORMAL));
    }

    @Override
    public void upOne(YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog) {
        MybatisOperaterUtil.getInstance().update(yjjStoreBusinessAccountLog,new MybatisSqlWhereBuild(YjjStoreBusinessAccountLog.class).eq(YjjStoreBusinessAccountLog::getId,yjjStoreBusinessAccountLog.getId()));
    }

    /**
     * 根据页码收支查询
     * @param id
     * @param page
     * @param inOut
     * @param type
     * @param typeAdaptor
     * @return
     */
    @Override
    public List<YjjStoreBusinessAccountLog> findAll(Long id, Integer page, Integer inOut, Integer type, Integer typeAdaptor)
    {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(YjjStoreBusinessAccountLog.class);
        mybatisSqlWhereBuild.eq(YjjStoreBusinessAccountLog::getStatusType,0);
        mybatisSqlWhereBuild.eq(YjjStoreBusinessAccountLog::getUserId,id);
        if (inOut!=null){
            mybatisSqlWhereBuild.eq(YjjStoreBusinessAccountLog::getInOutType,inOut);
        }
        if (type!=null){
            mybatisSqlWhereBuild.eq(YjjStoreBusinessAccountLog::getType,type);
        }
        if (typeAdaptor != null) {
            if (typeAdaptor.equals(1)) {
                //可用
                mybatisSqlWhereBuild.in(YjjStoreBusinessAccountLog::getType,
                        //待结算->已结算
                        StoreBillEnums.SETTLE_ACCOUNTS_SUCCESS.getCode(),
                        StoreBillEnums.WITHDRAW_CASH_SUCCESS.getCode(),
                        StoreBillEnums.RECHARGE_SUCCESS.getCode(),
                        StoreBillEnums.REFUND_MONEY_SUCCESS.getCode(),
                        StoreBillEnums.CUT_PAYMENT_SUCCESS.getCode(),
                        StoreBillEnums.CONSUME_SUCCESS.getCode()
                );
            }
            else {
                //待结
                mybatisSqlWhereBuild.in(YjjStoreBusinessAccountLog::getType,
                        //平台代发货扣款
                        StoreBillEnums.PLATFORM_INSTEAD_OF_SEND_GOODS_SUCCESS.getCode(),
                        //下单成功
                        StoreBillEnums.GOODS_ORDER_SUCCESS.getCode(),
                        //发货退款
                        StoreBillEnums.APP_REFUND_MONEY_SUCCESS.getCode(),
                        StoreBillEnums.REFUND_MONEY_SUCCESS.getCode(),
                        //待结算->已结算
                        StoreBillEnums.SETTLE_ACCOUNTS_SUCCESS.getCode()
                );
            }
        }
        mybatisSqlWhereBuild.in(YjjStoreBusinessAccountLog::getType,0,1);
        mybatisSqlWhereBuild.orderBy(MybatisSqlWhereBuild.ORDER.DESC,YjjStoreBusinessAccountLog::getCreateTime);
        mybatisSqlWhereBuild.page(page,10);
        List<YjjStoreBusinessAccountLog> list = MybatisOperaterUtil.getInstance().finAll(new YjjStoreBusinessAccountLog(),mybatisSqlWhereBuild);
        return list;
    }

    @Override
    public YjjStoreBusinessAccountLog findLimitTimeOne(Integer id)
    {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(YjjStoreBusinessAccountLog.class);
        mybatisSqlWhereBuild.eq(YjjStoreBusinessAccountLog::getUserId,id).orderBy(MybatisSqlWhereBuild.ORDER.DESC,YjjStoreBusinessAccountLog::getCreateTime);
        YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = MybatisOperaterUtil.getInstance().findOne(new YjjStoreBusinessAccountLog(),mybatisSqlWhereBuild);
        return yjjStoreBusinessAccountLog;
    }

    @Override
    public YjjStoreBusinessAccountLog findOne(YjjStoreBusinessAccountLog req) {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(YjjStoreBusinessAccountLog.class);
        mybatisSqlWhereBuild.eq(YjjStoreBusinessAccountLog::getId,req.getId());
        mybatisSqlWhereBuild.eq(YjjStoreBusinessAccountLog::getUserId,req.getUserId());
        YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = MybatisOperaterUtil.getInstance().findOne(new YjjStoreBusinessAccountLog(),mybatisSqlWhereBuild);

        return yjjStoreBusinessAccountLog;
    }
}
