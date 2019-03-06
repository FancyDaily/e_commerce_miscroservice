package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.UserDao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

    /**
     * 根据主键查找
     *
     * @param id
     * @return
     */
    @Override
    public TUser selectByPrimaryKey(Long id) {
        return MybatisOperaterUtil.getInstance().findOne(new TUser(), new MybatisSqlWhereBuild(TUser.class)
                .eq(TUser::getId, id)
                .eq(TUser::getIsValid, AppConstant.IS_VALID_YES));
    }

    /**
     * 根据主键集合查找
     *
     * @param idList
     * @return
     */
    @Override
    public List<TUser> queryByIds(List<Long> idList) {
        return MybatisOperaterUtil.getInstance().finAll(new TUser(), new MybatisSqlWhereBuild(TUser.class)
                .in(TUser::getId, idList)
                .eq(TUser::getIsValid, AppConstant.IS_VALID_YES));
    }

}





