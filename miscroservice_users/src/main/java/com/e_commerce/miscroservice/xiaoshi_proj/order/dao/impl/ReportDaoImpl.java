package com.e_commerce.miscroservice.xiaoshi_proj.order.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.TReport;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.xiaoshi_proj.order.dao.ReportDao;
import org.springframework.stereotype.Repository;

/**
 * @author 马晓晨
 * @date 2019/3/5
 */
@Repository
public class ReportDaoImpl implements ReportDao {

    @Override
    public int saveOneOrder(TReport report) {
        return MybatisOperaterUtil.getInstance().save(report);
    }

}
