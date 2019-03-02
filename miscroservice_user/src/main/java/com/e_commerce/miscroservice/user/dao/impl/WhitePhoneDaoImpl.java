package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.user.dao.WhitePhoneDao;
import com.e_commerce.miscroservice.user.mapper.WhitePhoneMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/6 15:15
 * @Copyright 玖远网络
 */
@Repository
public class WhitePhoneDaoImpl implements WhitePhoneDao {
    @Resource
    private WhitePhoneMapper whitePhoneMapper;
    /**
     * 查询白名单
     *
     * @param phone
     * @return
     */
    @Override
    public int getWhitePhone(String phone) {

        return whitePhoneMapper.getWhitePhone(phone);
    }
}
