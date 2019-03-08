package com.e_commerce.miscroservice.order.service;


import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.order.vo.UserInfoView;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

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
     * @return
     * @throws ParseException
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    long enroll(Long orderId, Long userId , String date, Long serviceId ) throws ParseException;

    String test(Long orderId , List<Long> userIdList);
    /**
     * @Author 姜修弘
     * 功能描述:取消报名
     * 创建时间:@Date 下午3:06 2019/3/8
     * @Param [orderId, nowUser]
     * @return void
     **/
    void removeEnroll(Long orderId , Long nowUserId);
    /**
     * 根据状态来查询所有未被操作的人的列表
     *
     * @param orderId
     * @param type
     * @param nowUserId
     * @return
     */
    List<UserInfoView> userListByPperation(Long orderId, int type, Long nowUserId);
}
