package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.user.po.TUserAuth;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.user.dao.UserAuthDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAuthDaoImpl implements UserAuthDao {
    @Override
    public List<TUserAuth> findAllByCardId(String cardId) {
        return MybatisPlus.getInstance().findAll(new TUserAuth(),new MybatisPlusBuild(TUserAuth.class)
        .eq(TUserAuth::getCardId,cardId)
        .eq(TUserAuth::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int insert(TUserAuth userAuth) {
        return MybatisPlus.getInstance().save(userAuth);
    }

    @Override
    public List<TUserAuth> selectByUserId(Long id) {
        return MybatisPlus.getInstance().findAll(new TUserAuth(),new MybatisPlusBuild(TUserAuth.class)
        .eq(TUserAuth::getUserId,id)
        .eq(TUserAuth::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUserAuth> selectAll() {
        return MybatisPlus.getInstance().findAll(new TUserAuth(),new MybatisPlusBuild(TUserAuth.class)
        .eq(TUserAuth::getIsValid,AppConstant.IS_VALID_YES));
    }
}
