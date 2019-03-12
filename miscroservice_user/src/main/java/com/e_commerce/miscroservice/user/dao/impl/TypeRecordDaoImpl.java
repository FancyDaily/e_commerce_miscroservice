package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TTypeRecord;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.TypeRecordDao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TypeRecordDaoImpl implements TypeRecordDao {
    @Override
    public List<TTypeRecord> selectByUserIdBetween(Long id, Long begin, Long end) {
        return MybatisOperaterUtil.getInstance().finAll(new TTypeRecord(),new MybatisSqlWhereBuild(TTypeRecord.class)
        .eq(TTypeRecord::getUserId,id)
        .between(TTypeRecord::getCreateTime,begin,end)
        .eq(TTypeRecord::getIsValid, AppConstant.IS_VALID_YES));
    }
}
