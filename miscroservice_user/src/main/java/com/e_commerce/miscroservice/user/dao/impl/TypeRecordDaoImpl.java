package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TTypeRecord;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.user.dao.TypeRecordDao;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TypeRecordDaoImpl implements TypeRecordDao {
    @Override
    public List<TTypeRecord> selectByUserIdBetween(Long id, Long begin, Long end) {
        return MybatisOperaterUtil.getInstance().finAll(new TTypeRecord(),new MybatisSqlWhereBuild(TTypeRecord.class)
        .eq(TTypeRecord::getUserId,id)
        .between(TTypeRecord::getCreateTime,begin,end)
        .eq(TTypeRecord::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TTypeRecord> selectIncomeByUserIdBetween(Long id, Long beginStamp, Long endStamp) {
        return MybatisOperaterUtil.getInstance().finAll(new TTypeRecord(),new MybatisSqlWhereBuild(TTypeRecord.class)
        .eq(TTypeRecord::getUserId,id)
                .between(TTypeRecord::getCreateTime,beginStamp,endStamp)
                .gt(TTypeRecord::getNum,0l)
                .eq(TTypeRecord::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public int insert(TTypeRecord record) {
        return MybatisOperaterUtil.getInstance().save(record);
    }

    @Override
    public List<TTypeRecord> selectByType(int code) {
        return MybatisOperaterUtil.getInstance().finAll(new TTypeRecord(),new MybatisSqlWhereBuild(TTypeRecord.class)
        .eq(TTypeRecord::getType,code)
        .eq(TTypeRecord::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 根据用户id、类型查找成长值记录
     * @param code
     * @param userId
     * @return
     */
    @Override
    public List<TTypeRecord> selectByTypeAndUserId(int code, Long userId) {
        return MybatisOperaterUtil.getInstance().finAll(new TTypeRecord(),new MybatisSqlWhereBuild(TTypeRecord.class)
                .eq(TTypeRecord::getType,code)
                .eq(TTypeRecord::getUserId,userId)
                .eq(TTypeRecord::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TTypeRecord> selectByTypeAndUserId(int code, int code1, Long id) {
        return MybatisOperaterUtil.getInstance().finAll(new TTypeRecord(),new MybatisSqlWhereBuild(TTypeRecord.class)
                .groupBefore()
        .eq(TTypeRecord::getType,code)
        .or().eq(TTypeRecord::getType,code1)
                .groupAfter()
                .eq(TTypeRecord::getUserId,id)
                .eq(TTypeRecord::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TTypeRecord> selectDailyGrowthRecords(Long id) {
        String today = DateUtil.timeStamp2Date(System.currentTimeMillis());
        Map<String, Object> ym2BetweenStamp = DateUtil.ym2BetweenStamp(today);
        Long beginStamp = Long.valueOf((String) ym2BetweenStamp.get("begin"));
        Long endStamp = Long.valueOf((String) ym2BetweenStamp.get("end"));

        return MybatisOperaterUtil.getInstance().finAll(new TTypeRecord(), new MybatisSqlWhereBuild(TTypeRecord.class)
        .eq(TTypeRecord::getUserId,id)
                .between(TTypeRecord::getCreateTime,beginStamp,endStamp)
        .eq(TTypeRecord::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TTypeRecord> selectGrowthRecords(Long id) {
        return MybatisOperaterUtil.getInstance().finAll(new TTypeRecord(), new MybatisSqlWhereBuild(TTypeRecord.class)
                .eq(TTypeRecord::getUserId,id)
                .eq(TTypeRecord::getIsValid,AppConstant.IS_VALID_YES));
    }

}
