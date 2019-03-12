package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TGroup;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.GroupDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupDaoImpl implements GroupDao {
    @Override
    public List<TGroup> selectByCompanyIdAndAuth(Long companyId, Integer groupAuthDefault) {
        return MybatisOperaterUtil.getInstance().finAll(new TGroup(),new MybatisSqlWhereBuild(TGroup.class)
        .eq(TGroup::getCompanyId,companyId)
        .eq(TGroup::getAuth,groupAuthDefault)
        .eq(TGroup::getIsValid, AppConstant.IS_VALID_YES));
    }
}
