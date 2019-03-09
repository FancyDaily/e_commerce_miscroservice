package com.e_commerce.miscroservice.order.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.application.TUserFreeze;
import com.e_commerce.miscroservice.commons.entity.application.TUserTimeRecord;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.SnowflakeIdWorker;
import com.e_commerce.miscroservice.message.po.TOrderRelationship;
import com.e_commerce.miscroservice.order.dao.OrderDao;
import com.e_commerce.miscroservice.order.dao.OrderRelationshipDao;
import com.e_commerce.miscroservice.commons.enums.application.OrderRelationshipEnum;
import com.e_commerce.miscroservice.order.service.OrderRelationService;
import com.e_commerce.miscroservice.order.vo.UserInfoView;
import com.e_commerce.miscroservice.user.controller.UserCommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



/**
 *
 * 功能描述:
 * 模块:
 * 项目:时间银行
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:姜修弘
 * 邮箱:414368243@qq.com
 * 创建时间:2019年3月2日 下午4:55:46
 */
@Service
public class OrderRelationServiceImpl implements OrderRelationService {


    Log logger = Log.getInstance(com.e_commerce.miscroservice.order.service.impl.OrderRelationServiceImpl.class);

    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();

    @Autowired
    private OrderRelationshipDao orderRelationshipDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserCommonController userCommonController;


    /**
     * 报名
     *
     * @param orderId
     * @param userId
     * @param date
     * @param serviceId
     * @return¶
     * @throws ParseException
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public long enroll(Long orderId, Long userId, String date, Long serviceId) throws ParseException {
        TUser nowUser = userCommonController.getUserById(userId);
        long nowTime = System.currentTimeMillis();
        String errorMsg = canEnroll(nowUser, date, serviceId, orderId, nowTime);
        if (errorMsg != null) {
            //如果错误消息不为空，说明该用户有部分问题不允许报名，抛出错误信息
            throw new MessageException("499", errorMsg);
        }
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        helpEnroll(order, nowUser, date, nowTime, serviceId);
        if (order.getType() == OrderRelationshipEnum.SERVICE_TYPE_SERV.getType()) {
            //如果是服务
            long canUseTime = nowUser.getSurplusTime() + nowUser.getCreditLimit() - nowUser.getFreezeTime();
            if (canUseTime < order.getCollectTime()) {
                throw new MessageException("499", "对不起，余额不足 不可以报名");
            }
            //TODO 冻结时间（要看一下是不是有可以复用的，，没有新增）
            //修改用户表的冻结字段
            nowUser.setFreezeTime(nowUser.getFreezeTime() + order.getCollectTime());
            nowUser.setUpdateTime(nowTime);
            nowUser.setUpdateUser(nowUser.getId());
            nowUser.setUpdateUserName(nowUser.getName());
            userCommonController.updateByPrimaryKey(nowUser);

        }
        order.setEnrollNum(order.getEnrollNum() + 1);
        orderDao.updateByPrimaryKey(order);
        //TODO 报名的通知（第一个报名的，或者第一个参与的（就看刘维怎么定，一个人报了取消又报了是不是还通知））
        return serviceId;
    }

    /**
     * 取消报名
     *
     * @param orderId
     * @param nowUserId
     */
    public void removeEnroll(Long orderId, Long nowUserId) {
        long nowTime = System.currentTimeMillis();
        TUser nowUser = userCommonController.getUserById(nowUserId);
        TOrderRelationship orderRelationship = null;
        orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId, nowUser.getId());
        if (orderRelationship == null) {
            //如果订单关系表为空
            throw new MessageException("499", "异常，该订单没有您参与信息，请后退重试");
        }
        if (!orderRelationship.getStatus().equals(OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType())) {
            //如果订单关系表状态不为待确认
            throw new MessageException("499", "异常，该订单没有您报名信息，请后退重试");
        }
        orderRelationship.setStatus(OrderRelationshipEnum.STATUS_REMOVE_ENROLL.getType());
        orderRelationship.setUpdateTime(nowTime);
        orderRelationshipDao.updateByPrimaryKey(orderRelationship);
        if (orderRelationship.getServiceType() == OrderRelationshipEnum.SERVICE_TYPE_SERV.getType()) {
            //如果是服务，那么取消报名要解冻时间币
            //TODO 解冻时间币（根据用户和订单编号找出该冻结信息，然后修改更新）
            unFreezeTime(orderRelationship.getCollectTime(), nowTime, nowUser);

        }

    }

    /**
     * 根据状态来查询所有未被操作的人的列表
     *
     * @param orderId
     * @param type
     * @param nowUserId
     * @return
     */
    public List<UserInfoView> userListByPperation(Long orderId, int type, Long nowUserId) {
        List<Integer> statusList = new ArrayList<>();
        TUser nowUser = userCommonController.getUserById(nowUserId);
        List<TOrderRelationship> orderRelationshipList = new ArrayList<>();
        if (type == 1) {
            //状态1代表要查询选人列表
            orderRelationshipList = orderRelationshipDao.selectListByStatusByEnroll(orderId
                    , OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType());
        }
        if (type == 2) {
            //状态2代表开始
            orderRelationshipList = orderRelationshipDao.selectListByStatusForNotSignByEnroll(orderId
                    , OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
        }
        if (type == 7) {
            //状态为7代表支付
            orderRelationshipList = orderRelationshipDao.selectListByStatusByEnroll(orderId
                    , OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
        }
        if (type == 9) {
            TOrder order = orderDao.selectByPrimaryKey(orderId);
            //默认是服务者 找求助者评价服务者未评价
            statusList.add(OrderRelationshipEnum.STATUS_HELPER_REMARK.getType());
            statusList.add(OrderRelationshipEnum.STATUS_WAIT_REMARK.getType());
            boolean isPublish = false;
            if (order.getCreateUser() == nowUser.getId().longValue()) {
                isPublish = true;
            }
            if ((order.getType() == 1 && isPublish) || (order.getType() == 2 && !isPublish)) {
                //改为求助者-如果是求助的发布者 或者 服务的非发布者
                statusList.add(OrderRelationshipEnum.STATUS_SERVER_REMARK.getType());
                statusList.add(OrderRelationshipEnum.STATUS_WAIT_REMARK.getType());
            }
            //orderRelationshipList = orderRelationshipDao.selectListByStatusListByEnroll(orderId , statusList);
        }
        List<Long> userIdList = new ArrayList<>();
        List<UserInfoView> userInfoViewList = new ArrayList<>();
        for (int i = 0; i < orderRelationshipList.size(); i++) {
            userIdList.add(orderRelationshipList.get(i).getReceiptUserId());
            UserInfoView userInfoView = new UserInfoView();
            userInfoView.setToStringId(orderRelationshipList.get(i).getReceiptUserId());
        }
        if (userIdList.size() == 0){
            return userInfoViewList;
        }
        List<TUser> userlist = userCommonController.selectUserByIds(userIdList);
        for (int i = 0; i < orderRelationshipList.size(); i++) {
            for (int j = 0; j < userlist.size(); j++) {
                if (orderRelationshipList.get(i).getReceiptUserId() == userlist.get(j).getId().longValue()) {
                    //匹配到对应用户，数据填充，跳出循环 查找下一个
                    UserInfoView userInfoView = new UserInfoView();
                    userInfoView.setToStringId(userlist.get(j).getId());
                    userInfoView.setName(userlist.get(j).getName());
                    userInfoView.setUserHeadPortraitPath(userlist.get(j).getUserHeadPortraitPath());
                    userInfoView.setStatus(1);//默认为已到
                    if (orderRelationshipList.get(i).getOrderReportType() == OrderRelationshipEnum.ORDER_REPORT_IS_BEREPORT.getType()
                            || orderRelationshipList.get(i).getOrderReportType() == OrderRelationshipEnum.ORDER_REPORT_IS_TURE.getType()) {
                        //如果订单内被举报了，状态置为异常
                        userInfoView.setStatus(2);
                    }
                    if (type == 7) {
                        //如果是是支付，还要看一下未到人员
                        if (orderRelationshipList.get(i).getSignType() == OrderRelationshipEnum.SIGN_TYPE_NO.getType()) {
                            //如果是未签到，将状态置为未到
                            userInfoView.setStatus(3);
                        }
                    }
                    userInfoViewList.add(userInfoView);
                    break;
                }
            }
        }
        return userInfoViewList;
    }

    /*   *//**
     * 选人的用户列表
     * @param orderId
     * @return
     *//*
    public List<UserInfoView> userListByChoose(Long orderId){
        List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectListByStatus(orderId
                , OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType());
        List<Long> userIdList = new ArrayList<>();
        List<UserInfoView> userInfoViewList = new ArrayList<>();
        for (int i = 0 ; i < orderRelationshipList.size() ; i++){
            userIdList.add(orderRelationshipList.get(i).getReceiptUserId());
        }
        List<TUser> userlist = new ArrayList<TUser>();//TODO 之后根据userdao来查
        for (int i = 0 ; i < orderRelationshipList.size() ; i++){
            for (int j = 0 ; j < userlist.size() ; j++){
                if (orderRelationshipList.get(i).getReceiptUserId() == userlist.get(j).getId().longValue()){
                    //匹配到对应用户，数据填充，跳出循环 查找下一个
                    UserInfoView userInfoView = new UserInfoView();
                    userInfoView.setToStringId(userlist.get(j).getId());
                    userInfoView.setUserHeadPortraitPath(userlist.get(j).getUserHeadPortraitPath());
                    userInfoView.setName(userlist.get(j).getName());
                    userInfoViewList.add(userInfoView);
                    break;
                }
            }
        }
        return userInfoViewList;
    }*/


    /**
     * 选择用户
     *
     * @param orderId
     * @param nowUserId
     * @param userIdList
     * @return
     */
    public List<String> chooseUser(Long orderId, Long nowUserId, List<Long> userIdList) {
        TUser nowUser = userCommonController.getUserById(nowUserId);
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        if (order.getCreateUser() != nowUserId.longValue()){
            throw new MessageException("499", "对不起，您不可以对非自己发布对互助进行操作");
        }
        List<Integer> statusList = new ArrayList<>();
        long canChooseUserSum = order.getServeNum() - order.getConfirmNum();
        if (userIdList.size() > canChooseUserSum) {
            if (canChooseUserSum == 0){
                throw new MessageException("499", "对不起，您已选满所需人数");
            }
            throw new MessageException("499", "对不起，您最多可选" + canChooseUserSum + "人");
        }
        List<String> errorMsg = new ArrayList<>();
        List<TUser> userList = userCommonController.selectUserByIds(userIdList);
        long nowTime = System.currentTimeMillis();
        List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectByOrderIdAndEnrollUserIdList(orderId, userIdList);
        List<Long> orderRelationshipIdList = new ArrayList<>();
        for (int i = 0; i < orderRelationshipList.size(); i++) {
            if (orderRelationshipList.get(i).getStatus() == OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType()) {
                orderRelationshipList.get(i).setStatus(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
                orderRelationshipList.get(i).setCreateTime(nowTime);
                orderRelationshipList.get(i).setCreateUser(nowUser.getId());
                orderRelationshipList.get(i).setCreateUserName(nowUser.getName());
                orderRelationshipIdList.add(orderRelationshipList.get(i).getId());
                //TODO 发送通知
            } else {
                for (int j = 0; j < userList.size(); j++) {
                    if (orderRelationshipList.get(i).getReceiptUserId() == userList.get(j).getId().longValue()) {
                        errorMsg.add("用户" + userList.get(j).getName() + "已被您操作");
                        break;
                    }
                }
            }
        }
        if ((canChooseUserSum - orderRelationshipIdList.size()) == 0) {
            //如果选满人了，那么判断是否有下周期的订单，如果有不处理，没有就派生订单
            //TODO 派生订单
        } else {
            throw new MessageException("499", "对不起，没有可供操作的用户");
        }
        if (orderRelationshipIdList.size() > 0){
            //如果有更新的人批量更新
            orderRelationshipDao.updateOrderRelationshipByList(orderRelationshipList ,orderRelationshipIdList );
        }
        order.setConfirmNum(order.getConfirmNum() + orderRelationshipIdList.size());
        orderDao.updateByPrimaryKey(order);
        //选人了就要吧发布者状态改成待支付
        TOrderRelationship publisherOrderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId, nowUser.getId());
        publisherOrderRelationship.setStatus(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
        orderRelationshipDao.updateByPrimaryKey(publisherOrderRelationship);
        return errorMsg;
    }

    /**
     * @return java.util.List<java.lang.String>
     * @Author 姜修弘
     * 功能描述:拒绝人选
     * 创建时间:@Date 下午7:48 2019/3/6
     * @Param [orderId, nowUserId]
     **/
    public List<String> unChooseUser(Long orderId, List<Long> userIdList, Long nowUserId) {
        TUser nowUser = userCommonController.getUserById(nowUserId);
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        if (order.getCreateUser() != nowUserId.longValue()){
            throw new MessageException("499", "对不起，您不可以对非自己发布对互助进行操作");
        }
        List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectByOrderIdAndEnrollUserIdList(orderId, userIdList);
        List<String> errorMsg = new ArrayList<>();
        List<TUser> userList = userCommonController.selectUserByIds(userIdList);
        long nowTime = System.currentTimeMillis();
        List<Long> orderRelationshipIdList = new ArrayList<>();
        for (int i = 0; i < orderRelationshipList.size(); i++) {
            TUser toUser = new TUser();
            for (int j = 0; j < userList.size(); j++) {
                if (orderRelationshipList.get(i).getReceiptUserId() == userList.get(j).getId().longValue()) {
                    toUser = userList.get(j);
                    break;
                }
            }
            if (orderRelationshipList.get(i).getStatus() == OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType()) {
                orderRelationshipList.get(i).setStatus(OrderRelationshipEnum.STATUS_NOT_CHOOSE.getType());
                orderRelationshipList.get(i).setCreateTime(nowTime);
                orderRelationshipList.get(i).setCreateUser(nowUser.getId());
                orderRelationshipList.get(i).setCreateUserName(nowUser.getName());
                orderRelationshipIdList.add(orderRelationshipList.get(i).getId());
                //TODO 发送通知
                if (order.getType() == 2) {
                    //如果是服务,解冻时间币
                    unFreezeTime(order.getCollectTime(), nowTime, toUser);
                }
            } else {
                errorMsg.add("用户" + toUser.getName() + "已被您操作");
            }
        }
        if (orderRelationshipIdList.size() > 0){
            //如果有更新的人批量更新
            orderRelationshipDao.updateOrderRelationshipByList(orderRelationshipList ,orderRelationshipIdList );
        } else {
            throw new MessageException("499", "对不起，没有可供操作的用户");
        }
        return errorMsg;
    }
//TODO 标记开始 等订单关系表更新上来再写
 /*   public List<String> startOrder(Long orderId , List<Long> userIdList){
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        if (userIdList.size() == 1){
            TOrderRelationship orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId , userIdList.get(0));
            if (orderRelationship.get)
        }
        List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectByOrderIdAndEnrollUserIdList()
    }*/


    /**
     * @return java.util.List<java.lang.String>
     * @Author 姜修弘
     * 功能描述:订单支付
     * 创建时间:@Date 下午1:41 2019/3/8
     * @Param [orderId, userIdList, paymentList, nowUser]
     **/
    public List<String> payOrder(Long orderId, List<Long> userIdList, List<Long> paymentList, Long nowUserId) {
        TUser nowUser = userCommonController.getUserById(nowUserId);
        List<String> msgList = new ArrayList<>();
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        long nowTime = System.currentTimeMillis();
        long paymentSum = 0l;
        long publishUserId = 0l;
        List<TUser> toUserList = userCommonController.selectUserByIds(userIdList);
        if (order.getCreateUser() == nowUserId.longValue()){
            //如果当前用户是发布者，那么就查找报名者即可
            publishUserId = nowUserId;
            List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectByOrderIdAndEnrollUserIdList(orderId, userIdList);

            for (int i = 0; i < userIdList.size(); i++) {
                TOrderRelationship orderRelationship = new TOrderRelationship();
                TUser toUser = new TUser();
                for (int j = 0; j < orderRelationshipList.size(); j++) {
                    if (userIdList.get(i) == orderRelationshipList.get(j).getReceiptUserId().longValue()) {
                        orderRelationship = orderRelationshipList.get(j);
                        break;
                    }
                }
                for (int j = 0; j < toUserList.size(); j++) {
                    if (userIdList.get(i) == toUserList.get(j).getId().longValue()) {
                        toUser = toUserList.get(j);
                        break;
                    }
                }
                String msg = payOrderPri(orderRelationship, paymentList.get(i), nowUser, nowTime, toUser);
                if (msg != null) {
                    msgList.add(msg);
                } else {
                    paymentSum += paymentList.get(i);
                }
            }
        } else {
            //如果是报名者支付，要修改自己的订单状态
            publishUserId = userIdList.get(0);
            TOrderRelationship orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId, nowUserId);
            String msg = payOrderPri(orderRelationship, paymentList.get(0), nowUser, nowTime, toUserList.get(0));
            if (msg != null) {
                throw new MessageException("499", "支付失败，"+msg);
            }
            paymentSum += paymentList.get(0);

        }

        //支付数量为传入人数量减去错误消息数量
        long seekHelpDoneNum = userIdList.size() - msgList.size();
        //查看是否还有待支付的人，没有则将发布用户状态置为待评价
        long count = orderRelationshipDao.selectCountByStatusByEnroll(orderId, OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
        if ( count == 0){
            //如果没有要支付的人，就将发布者订单关系置为待支付
            TOrderRelationship publishOrderRela = orderRelationshipDao.selectByOrderIdAndUserId(orderId,publishUserId);
            publishOrderRela.setStatus(OrderRelationshipEnum.STATUS_WAIT_REMARK.getType());
            publishOrderRela.setUpdateTime(nowTime);
            publishOrderRela.setUpdateUser(nowUserId);
            publishOrderRela.setUpdateUserName(nowUser.getName());

            orderRelationshipDao.updateByPrimaryKey(publishOrderRela);
        }

        //解冻时间币，解冻数量是支付成功人数量*时间币单价
        //unFreezeTime(orderRelationship.getCollectTime() , nowTime , nowUser); TODO 暂时标注


        return msgList;
    }

    private String payOrderPri(TOrderRelationship orderRelationship, Long payment, TUser nowUser, long nowTime, TUser toUser) {
        String msg = null;
        if (orderRelationship.getStatus() != OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType()) {
            msg = "支付失败：用户" + toUser.getName() + "状态无法支付";
            return msg;
        }
        if (orderRelationship.getOrderReportType() == OrderRelationshipEnum.ORDER_REPORT_IS_TURE.getType()) {
            //如果用户被投诉
            msg = "支付失败：该订单已被投诉";
            return msg;
        }
        if (orderRelationship.getOrderReportType() == OrderRelationshipEnum.ORDER_REPORT_IS_BEREPORT.getType()) {
            //如果用户发起投诉
            msg = "支付失败：该订单已被投诉";
            return msg;
        }
        orderRelationship.setStatus(OrderRelationshipEnum.STATUS_WAIT_REMARK.getType());
        //被支付用户增加时间币
        if (toUser.getServeNum() + toUser.getSeekHelpNum() == 0) {
            //如果是首次完成互助
            //TODO 奖励互助时 增加成长值
        }
        toUser.setSurplusTime(toUser.getSurplusTime() + payment);
        toUser.setUpdateUserName(nowUser.getName());
        toUser.setUpdateUser(nowUser.getId());
        toUser.setUpdateTime(nowTime);
        toUser.setServeNum(toUser.getServeNum() + 1);
        userCommonController.updateByPrimaryKey(toUser);

        //插入支付流水
        TUserTimeRecord userTimeRecord = new TUserTimeRecord();
        userTimeRecord.setId(snowflakeIdWorker.nextId());
        userTimeRecord.setUserId(toUser.getId());
        userTimeRecord.setFromUserId(nowUser.getId());
        userTimeRecord.setType(1);
        userTimeRecord.setTargetId(orderRelationship.getOrderId());
        userTimeRecord.setTime(payment);
        userTimeRecord.setCreateUser(nowUser.getId());
        userTimeRecord.setCreateUserName(nowUser.getName());
        userTimeRecord.setUpdateTime(nowTime);
        userTimeRecord.setUpdateUserName(nowUser.getName());
        userTimeRecord.setUpdateTime(nowTime);
        userTimeRecord.setIsValid("1");
        //TODO insert 支付表
        //TODO 发送系统通知
        //TODO 插入成长值
        //TODO 插入服务记录


        return msg;
    }
/*    public List<UserInfoView> startUserList(Long orderId){
        List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectListByStatus(orderId
            , OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
        List<Long> userIdList = new ArrayList<>();
        List<UserInfoView> userInfoViewList = new ArrayList<>();
        for (int i = 0 ; i < orderRelationshipList.size() ; i++){
            userIdList.add(orderRelationshipList.get(i).getReceiptUserId());
        }
        List<TUser> userlist = new ArrayList<TUser>();//TODO 之后根据userdao来查
        for (int i = 0 ; i < orderRelationshipList.size() ; i++){
            for (int j = 0 ; j < userlist.size() ; j++){
                if (orderRelationshipList.get(i).getReceiptUserId() == userlist.get(j).getId().longValue()){
                    //匹配到对应用户，数据填充，跳出循环 查找下一个
                    UserInfoView userInfoView = new UserInfoView();
                    userInfoView.setToStringId(userlist.get(j).getId());
                    userInfoView.setUserHeadPortraitPath(userlist.get(j).getUserHeadPortraitPath());
                    userInfoView.setName(userlist.get(j).getName());
                    userInfoViewList.add(userInfoView);
                    break;
                }
            }
        }
        return userInfoViewList;
    }*/
    /* *//**
     * @Author 姜修弘
     * 功能描述:支付列表
     * 创建时间:@Date 下午9:45 2019/3/6
     * @Param [orderId]
     * @return java.util.List<com.e_commerce.miscroservice.order.vo.UserInfoView>
     **//*
    public List<UserInfoView> orderPayUserList(Long orderId){
        List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectListByStatus(orderId
            , OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
        List<Long> userIdList = new ArrayList<>();
        List<UserInfoView> userInfoViewList = new ArrayList<>();
        for (int i = 0 ; i < orderRelationshipList.size() ; i++){
            userIdList.add(orderRelationshipList.get(i).getReceiptUserId());
        }
        List<TUser> userlist = new ArrayList<TUser>();//TODO 之后根据userdao来查
        for (int i = 0 ; i < orderRelationshipList.size() ; i++){
            for (int j = 0 ; j < userlist.size() ; j++){
                if (orderRelationshipList.get(i).getReceiptUserId() == userlist.get(j).getId().longValue()){
                    //匹配到对应用户，数据填充，跳出循环 查找下一个
                    UserInfoView userInfoView = new UserInfoView();
                    userInfoView.setToStringId(userlist.get(j).getId());
                    userInfoView.setUserHeadPortraitPath(userlist.get(j).getUserHeadPortraitPath());
                    userInfoView.setName(userlist.get(j).getName());
                    userInfoView.setStatus(1);//默认为已到
                    if (orderRelationshipList.get(i).getOrderReportType() == OrderRelationshipEnum.ORDER_REPORT_IS_BEREPORT.getType()){
                        //如果订单内被举报了，状态置为异常
                        userInfoView.setStatus(2);
                    } else if (orderRelationshipList.get(i).getSignType() == OrderRelationshipEnum.SIGN_TYPE_NO.getType()){
                        //如果是未签到，将状态置为未到
                        userInfoView.setStatus(3);
                    }
                    userInfoViewList.add(userInfoView);
                    break;
                }
            }
        }
        return userInfoViewList;
    }*/

    /**
     * 解冻时间币
     *
     * @param unfreezeTime
     * @param nowTime
     * @param nowUser
     */
    private void unFreezeTime(long unfreezeTime, long nowTime, TUser nowUser) {
        TUserFreeze userFreeze = null;//TODO 调用冻结dao来查询冻结表
        if (userFreeze == null) {
            //如果没有冻结信息，说明订单有问题，取消失败
            throw new MessageException("499", "冻结信息表更新失败，请后退刷新重试");
        }
        userFreeze.setFreezeTime(userFreeze.getFreezeTime() - unfreezeTime);
        userFreeze.setUpdateTime(nowTime);
        userFreeze.setUpdateUser(nowUser.getId());
        userFreeze.setUpdateUserName(nowUser.getName());
        if (userFreeze.getFreezeTime() == 0) {
            userFreeze.setIsValid("0");
        }
        //TODO 更新冻结表
        nowUser.setFreezeTime(nowUser.getFreezeTime() - unfreezeTime);
        nowUser.setUpdateTime(nowTime);
        nowUser.setUpdateUser(nowUser.getId());
        nowUser.setUpdateUserName(nowUser.getName());
        //TODO 更新用户表
    }

    /**
     * 功能描述:报名的各种限制
     * 作者:姜修弘
     * 创建时间:2019年2月25日 下午2:38:44
     *
     */
    private String canEnroll(TUser nowUser, String date, Long serviceId, Long orderId, long nowTime) throws ParseException {

        String msg = null;
        TOrder order = orderDao.selectByPrimaryKey(orderId);//TODO 之后用马晓晨的方法根据serviceId配合日期查出，如果没有就新建一个订单，然后拿过来
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = simpleDateFormat.parse(date + " 00:00:00");
        Date endDate = simpleDateFormat.parse(date + " 23:59:59");
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();

        TOrderRelationship orderRelationship = null;
        //查询出传入日期是否有参与的订单关系
        orderRelationship = orderRelationshipDao.selectOrderRelationshipByJoinIn(orderId , nowUser.getId());

        if (orderRelationship != null) {
            //如果有参加的订单关系，则不允许报名
            msg = "对不起，您已报名，请勿多次报名";
            return msg;
        }
        if (order.getEndTime().longValue() < nowTime) {
            msg = "对不起，该项目已经结束，无法报名";
            return msg;
        }
        if (order.getCreateUser() == nowUser.getId().longValue()) {
            // 如果用户报名的是自己的需求
            msg = "对不起，您不能报名自己的求助";
            return msg;
        }
        if (order.getSource() == 2) {
            if (false/*order.getOpenAuth() == 2*/) {//TODO 马晓晨的字段
                // 如果是仅组织内成员可见
                //TODO 调用许方毅 是否是组内成员
                if (false) {
                    // 不属于组织成员
                    msg = "对不起，该互助仅允许组织内成员报名，您非组织内成员，无法报名";
                    return msg;
                }
            }
        }
        return msg;

    }

    /**
     * 求助报名
     *
     * @param order
     * @param nowUser
     * @param date
     * @param nowTime
     * @param serviceId
     * @return
     */
    private long helpEnroll(TOrder order, TUser nowUser, String date, long nowTime, Long serviceId) {
        TOrderRelationship orderRelationship = null;
        orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(order.getId(), nowUser.getId());
        if (orderRelationship != null) {
            //如果订单ID非空，说明以前有取消被取消等等状态，将原订单复用
            orderRelationship.setUpdateTime(nowTime);
            orderRelationship.setStatus(OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType());
            orderRelationship.setUpdateUser(nowUser.getId());
            orderRelationship.setUpdateUserName(nowUser.getName());
            orderRelationshipDao.updateByPrimaryKey(orderRelationship);
        } else {
            orderRelationship=new TOrderRelationship();
            //没有可以复用的订单关系，那么就新建订单关系
            orderRelationship.setId(snowflakeIdWorker.nextId());
            orderRelationship.setServiceId(serviceId);
            orderRelationship.setOrderId(order.getId());
            orderRelationship.setServiceType(order.getType());
            orderRelationship.setFromUserId(order.getCreateUser());
            orderRelationship.setReceiptUserId(nowUser.getId());
            orderRelationship.setSignType(OrderRelationshipEnum.SIGN_TYPE_NO.getType());
            orderRelationship.setStatus(OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType());
            orderRelationship.setServiceReportType(OrderRelationshipEnum.SERVICE_REPORT_IS_NO.getType());
            orderRelationship.setOrderReportType(OrderRelationshipEnum.ORDER_REPORT_IS_NO.getType());
            orderRelationship.setServiceCollectionType(OrderRelationshipEnum.SERVICE_COLLECTION_IS_NO.getType());
            orderRelationship.setServiceName(order.getServiceName());
            orderRelationship.setStartTime(order.getStartTime());
            orderRelationship.setEndTime(order.getEndTime());
            orderRelationship.setTimeType(order.getTimeType());
            orderRelationship.setCollectTime(order.getCollectTime());
            orderRelationship.setCollectType(order.getCollectType());
            orderRelationship.setCreateUser(nowUser.getId());
            orderRelationship.setCreateUserName(nowUser.getName());
            orderRelationship.setCreateTime(nowTime);
            orderRelationship.setUpdateUser(nowUser.getId());
            orderRelationship.setUpdateUserName(nowUser.getName());
            orderRelationship.setUpdateTime(nowTime);
            orderRelationship.setIsValid("1");
            orderRelationshipDao.insert(orderRelationship);
        }
        return orderRelationship.getId();
    }

    /**
     * @return java.lang.String
     * @Author 姜修弘
     * 功能描述:
     * 创建时间:@Date 下午5:04 2019/3/6
     * @Param [userId, orderRelationshipId]
     **/
    public String test(Long orderId ,List<Long> userIdList) {
        List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectByOrderIdAndEnrollUserIdList(orderId ,userIdList);
        List<Long> orderRelatingshipIdList = new ArrayList<>();
        for (int i = 0 ;i < orderRelationshipList.size() ; i++){
            if (orderRelationshipList.get(i).getReceiptUserId() == 68813259653775360l) {
                orderRelationshipList.get(i).setCreateUserName("测试");
            } else {
                orderRelationshipList.get(i).setCreateUserName("测试2");
            }
            orderRelatingshipIdList.add(orderRelationshipList.get(i).getId());
        }

        long count = orderRelationshipDao.updateOrderRelationshipByList(orderRelationshipList ,orderRelatingshipIdList);
        return count+"";
    }

    /**
     * 参与的订单的状态
     *
     * @return
     */
    private List<Integer> participationStatusList() {
        List<Integer> orderRelationshipStatusList = new ArrayList<>();
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType());
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_WAIT_REMARK.getType());
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_IS_COMPLETED.getType());
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_SERVER_REMARK.getType());
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_HELPER_REMARK.getType());
        return orderRelationshipStatusList;
    }
}
