package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.YjjStoreBusinessAccountDao;
import com.e_commerce.miscroservice.user.mapper.YjjStoreBusinessAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Create by hyf on 2018/11/13
 */
@Repository
public class YjjStoreBusinessAccountDaoImpl implements YjjStoreBusinessAccountDao {
    @Autowired
    private YjjStoreBusinessAccountMapper yjjStoreBusinessAccountMapper;
    /**
     * 根据用户id查询 用户账户表
     * @param id
     * @return
     */
    @Override
    public YjjStoreBusinessAccount findOne(Long id) {
        YjjStoreBusinessAccount yjjStoreBusinessAccount = MybatisOperaterUtil.getInstance().findOne(new YjjStoreBusinessAccount(), new MybatisSqlWhereBuild(YjjStoreBusinessAccount.class)
                .eq(YjjStoreBusinessAccount::getUserId,id)
                .eq(YjjStoreBusinessAccount::getDelStatus, StateEnum.NORMAL)
                .eq(YjjStoreBusinessAccount::getStatus,StateEnum.NORMAL)
        );
        return yjjStoreBusinessAccount;
    }

    @Override
    public Integer addOne(YjjStoreBusinessAccount addNew) {
        Integer is = MybatisOperaterUtil.getInstance().save(addNew);
        return is;
    }

    @Override
    public Integer updateOne(YjjStoreBusinessAccount account) {
        Integer is = MybatisOperaterUtil.getInstance().update(account,new MybatisSqlWhereBuild(YjjStoreBusinessAccount.class).eq(YjjStoreBusinessAccount::getUserId,account.getUserId()));
        return is;
    }


    /**
     * 更新账户金额操作
     * @param userId
     * @param operMoney
     * @return
     */
    @Override
    public Integer upUseMoney(Long userId, Double operMoney) {
        return yjjStoreBusinessAccountMapper.upUseMoney(userId,operMoney);
    }
}
