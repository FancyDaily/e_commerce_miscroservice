package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.vo.WelfareParamView;

import java.util.Map;

public interface PublicWelfareDao {

    /**
     * 查询公益时
     *
     * @param param
     * @param id
     * @param betLeft 起时间戳
     * @param betRight 止时间戳
     * @param lastTime
     * @param desc
     * @return
     */
    Map<String, Object> selectPublicWelfare(WelfareParamView param, Long id, Long betLeft, Long betRight, Long lastTime, MybatisSqlWhereBuild.ORDER desc);

}
