package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TPublicWelfare;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.PublicWelfareDao;
import com.e_commerce.miscroservice.user.mapper.PublicWelfareMapper;
import com.e_commerce.miscroservice.user.vo.WelfareParamView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PublicWelfareDaoImpl implements PublicWelfareDao {

    @Autowired
    private PublicWelfareMapper mapper;

    @Override
    public Map<String, Object> selectPublicWelfare(WelfareParamView param, Long id, Long betLeft, Long betRight, Long lastTime, MybatisSqlWhereBuild.ORDER desc) {
        List<TPublicWelfare> publicWelfares = MybatisOperaterUtil.getInstance().finAll(new TPublicWelfare(), new MybatisSqlWhereBuild(TPublicWelfare.class)
                .lt(TPublicWelfare::getCreateTime, lastTime)
                .between(TPublicWelfare::getCreateTime, betLeft, betRight)
                .eq(TPublicWelfare::getIsValid, AppConstant.IS_VALID_YES)
                .orderBy(MybatisSqlWhereBuild.ORDER.DESC));
        Long yearWelfare = mapper.getYearWelfare(param);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("yearWelfare",yearWelfare);
        resultMap.put("publicWelfares",publicWelfares);
        return resultMap;
    }
}
