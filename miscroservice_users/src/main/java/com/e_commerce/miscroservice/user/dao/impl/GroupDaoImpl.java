package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TGroup;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.user.dao.GroupDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupDaoImpl implements GroupDao {
    @Override
    public List<TGroup> selectByCompanyIdAndAuth(Long companyId, Integer groupAuthDefault) {
        return MybatisPlus.getInstance().findAll(new TGroup(),new MybatisPlusBuild(TGroup.class)
        .eq(TGroup::getCompanyId,companyId)
        .eq(TGroup::getAuth,groupAuthDefault)
        .eq(TGroup::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TGroup> selectByCompanyId(Long companyId) {
        return MybatisPlus.getInstance().findAll(new TGroup(),new MybatisPlusBuild(TGroup.class)
        .eq(TGroup::getCompanyId,companyId)
        .eq(TGroup::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TGroup> listGroup(Long companyId) {
        return MybatisPlus.getInstance().findAll(new TGroup(), new MybatisPlusBuild(TGroup.class)
                .eq(TGroup::getCompanyId, companyId).eq(TGroup::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int insert(TGroup group) {
        return MybatisPlus.getInstance().save(group);
    }

    @Override
    public TGroup selectByPrimaryKey(Long id) {
        return MybatisPlus.getInstance().findOne(new TGroup(), new MybatisPlusBuild(TGroup.class)
                .eq(TGroup::getId, id)
                .eq(TGroup::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int updateByPrimaryKeySelective(TGroup updateGroup) {
        return MybatisPlus.getInstance().update(updateGroup, new MybatisPlusBuild(TGroup.class)
                .eq(TGroup::getId, updateGroup.getId()));
    }
}
