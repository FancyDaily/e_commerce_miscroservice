package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUserAuth;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.UserAuthDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAuthDaoImpl implements UserAuthDao {
    @Override
    public List<TUserAuth> findAllByCardId(String cardId) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserAuth(),new MybatisSqlWhereBuild(TUserAuth.class)
        .eq(TUserAuth::getCardId,cardId)
        .eq(TUserAuth::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int insert(TUserAuth userAuth) {
        return MybatisOperaterUtil.getInstance().save(userAuth);
    }
}
