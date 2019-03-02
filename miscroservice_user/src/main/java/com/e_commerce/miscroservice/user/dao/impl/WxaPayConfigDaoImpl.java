package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.WxaPayConfig;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.user.dao.WxaPayConfigDao;
import org.springframework.stereotype.Component;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 9:38
 * @Copyright 玖远网络
 */
@Component
public class WxaPayConfigDaoImpl implements WxaPayConfigDao{


    private Log logger = Log.getInstance (WxaPayConfigDaoImpl.class);


    /**
     * 根据wxaAppId查询
     *
     * @param wxaAppId wxaAppId
     * @return com.e_commerce.miscroservice.commons.entity.application.user.WxaPayConfig
     * @author Charlie
     * @date 2018/10/17 9:40
     */
    @Override
    public WxaPayConfig findByAppId(String wxaAppId) {
        logger.info ("根据wxaAppId查询 wxaAppId={}", wxaAppId);
        if (BeanKit.hasNull (wxaAppId)) {
            return null;
        }
        return MybatisOperaterUtil.getInstance ().findOne (
                new WxaPayConfig (),
                new MybatisSqlWhereBuild (WxaPayConfig.class)
                        .eq (WxaPayConfig::getAppId, wxaAppId)

        );
    }
}
