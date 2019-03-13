package com.e_commerce.miscroservice.order.dao;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TReport;
import com.e_commerce.miscroservice.order.vo.PageOrderParamView;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * 功能描述:订单dao层
 *
 * @author 马晓晨
 * @date 2019/3/5 21:15
 */
public interface ReportDao {
    /**
     * 保存一个Order订单
     *
     * @param order order
     * @return 保存的订单个数 本方法成功返回1
     */
    int saveOneOrder(TReport report);


}
