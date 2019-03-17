package com.e_commerce.miscroservice.order.service;


import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TOrderRelationship;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.application.TUserTimeRecord;
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

    /**
     * 选择用户
     *
     * @param orderId
     * @param nowUserId
     * @param userIdList
     * @return
     */
    List<String> chooseUser(Long orderId, Long nowUserId, List<Long> userIdList);
    /**
     * 拒绝人选
     * @param orderId
     * @param userIdList
     * @param nowUserId
     * @param type 0-手动拒绝 1- 自动下架拒绝
     * @return
     */
    List<String> unChooseUser(Long orderId, List<Long> userIdList, Long nowUserId , int type);
    /**
     * 开始订单（签到）
     * @param orderId
     * @param nowUserId
     * @return
     */
    List<String> startOrder(Long orderId  , Long nowUserId , List<Long> userIdList);

    /**
     * 支付
     * @param orderId
     * @param userIdList
     * @param paymentList
     * @param nowUserId
     * @return
     */
    List<String> payOrder(Long orderId, List<Long> userIdList, List<Long> paymentList, Long nowUserId);
    /**
     * 新增发布者订单关系
     * @param order
     */
    int addTorderRelationship(TOrder order);

    /**
     * 批量投诉
     *
     * @param orderId
     * @param labelsId
     * @param message
     * @param voucherUrl
     * @param nowUserId
     * @param userIds
     * @return
     */
    List<String> repors (long orderId , long labelsId , String message ,   String voucherUrl , Long nowUserId , List<Long> userIds);
    /**
     * 评价订单
     * @param nowUserId
     * @param userIdList
     * @param orderId
     * @param credit
     * @param major
     * @param attitude
     * @param message
     * @param labels
     * @return
     */
    List<String> remarkOrder(Long nowUserId, List<Long> userIdList, Long orderId , int credit, int major, int attitude, String message, String labels);

    /**
     * 取消订单弹窗提醒每人扣除时间数
     * @param orderId
     * @return
     */
    long removeOrderTips(Long orderId , Long nowUserId);
    /**
     * 取消订单
     * @param orderId
     * @param userIdList
     * @param nowUserId
     * @return
     */
    List<String> removeOrder(Long orderId , List<Long> userIdList , Long nowUserId);
    /**
     * 接受时间赠礼
     * @param userTimeRecordId
     * @param eventId
     */
    void acceptGiftForRemove(Long userTimeRecordId , Long eventId);
    /**
     * 拒绝时间赠礼
     * @param userTimeRecordId
     * @param eventId
     */
    void unAcceptGiftForRemove( Long userTimeRecordId , Long eventId);

    /**
     * 举报详情
     * @param orderId
     * @param nowUserId
     * @return
     */
    void reoprtOrder(Long orderId , Long nowUserId);
    /**
     * 移除可报名日期
     * @param date
     * @param serviceId
     */
    void removeCanEnrollDate(String date , Long serviceId);
}
