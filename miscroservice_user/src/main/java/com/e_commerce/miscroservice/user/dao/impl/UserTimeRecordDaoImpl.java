package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUserTimeRecord;
import com.e_commerce.miscroservice.commons.enums.application.PaymentEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.UserTimeRecordDao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserTimeRecordDaoImpl implements UserTimeRecordDao {
    @Override
    public List<TUserTimeRecord> selectMonthlyTimeRecord(Long userId, Long begin, Long end) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserTimeRecord(), new MybatisSqlWhereBuild(TUserTimeRecord.class).eq(TUserTimeRecord::getUserId, userId).between(TUserTimeRecord::getCreateTime, begin, end).eq(TUserTimeRecord::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public Long insert(TUserTimeRecord record) {
        MybatisOperaterUtil.getInstance().save(record);
        return record.getId();
    }

    @Override
    public TUserTimeRecord selectById(Long id){
        return MybatisOperaterUtil.getInstance().findOne(new TUserTimeRecord() ,
                new MybatisSqlWhereBuild(TUserTimeRecord.class)
                        .eq(TUserTimeRecord::getId,id));
    }

    /**
     * 查找一个订单的所有流水
     * @param orderId
     * @return
     */
    public List<TUserTimeRecord> selectGetTimeByOrder(Long orderId){
       return MybatisOperaterUtil.getInstance().finAll(new TUserTimeRecord() ,
                new MybatisSqlWhereBuild(TUserTimeRecord.class)
                        .eq(TUserTimeRecord::getTargetId , orderId)
                        .eq(TUserTimeRecord::getType , PaymentEnum.PAYMENT_TYPE_ACEPT_SERV.getCode())
                        .eq(TUserTimeRecord::getIsValid , AppConstant.IS_ATTEN_YES));
    }
}
