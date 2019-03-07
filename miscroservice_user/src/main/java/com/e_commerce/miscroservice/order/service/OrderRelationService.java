package com.e_commerce.miscroservice.order.service;


import com.e_commerce.miscroservice.commons.entity.application.TUser;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

/**
 * 
 * 功能描述:订单关系service层
 * 模块:订单关系
 * 项目:时间银行
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:姜修弘
 * 邮箱:414368243@qq.com
 * 创建时间:2019年3月2日 下午4:21:43
 */
public interface OrderRelationService {

    /**
     * 报名
     * @param orderId
     * @param userId
     * @param date
     * @param serviceId
     * @param orderRelationServiceId
     * @return
     * @throws ParseException
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    long enroll(Long orderId, Long userId , String date, Long serviceId , Long orderRelationServiceId) throws ParseException ;

    String test(Long userId , long orderRelationshipId);
}
