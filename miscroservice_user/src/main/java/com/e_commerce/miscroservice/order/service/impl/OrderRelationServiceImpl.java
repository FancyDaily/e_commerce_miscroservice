package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.enums.application.SetTemplateIdEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.exception.colligate.NoEnoughCreditException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.view.RemarkLablesView;
import com.e_commerce.miscroservice.message.controller.MessageCommonController;
import com.e_commerce.miscroservice.order.controller.OrderCommonController;
import com.e_commerce.miscroservice.order.dao.*;
import com.e_commerce.miscroservice.order.service.OrderRelationService;
import com.e_commerce.miscroservice.order.vo.EnrollUserInfoView;
import com.e_commerce.miscroservice.order.vo.OrgEnrollUserView;
import com.e_commerce.miscroservice.order.vo.UserInfoView;
import com.e_commerce.miscroservice.product.controller.ProductCommonController;
import com.e_commerce.miscroservice.product.util.DateUtil;
import com.e_commerce.miscroservice.user.controller.UserCommonController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
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
public class OrderRelationServiceImpl extends BaseService implements OrderRelationService {


    Log logger = Log.getInstance(com.e_commerce.miscroservice.order.service.impl.OrderRelationServiceImpl.class);

    @Autowired
    private OrderRelationshipDao orderRelationshipDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserCommonController userCommonController;

    @Autowired
    private MessageCommonController messageCommonController;

    @Autowired
    private OrderCommonController orderCommonController;

    @Autowired
    private ProductCommonController productCommonController;


    @Autowired
    private OrderRecordDao orderRecordDao;

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private EvaluateDao evaluateDao;

    /**
     * 报名
     *
     * @param orderId
     * @param userId
     * @param date
     * @param serviceId
     * @throws ParseException
     * @return¶
     */
    @Transactional(rollbackFor = Throwable.class)
    public long enroll(Long orderId, Long userId, String date, Long serviceId) {
        TUser nowUser = userCommonController.getUserById(userId);
        long nowTime = System.currentTimeMillis();
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        if (order.getTimeType() == ProductEnum.TIME_TYPE_REPEAT.getValue()){
            //如果是重复性的，根据日历来进行查找订单，如果没有就创建新订单
            try {
                TService service = productCommonController.getProductById(serviceId);
                order = orderCommonController.produceOrder(service , OrderEnum.PRODUCE_TYPE_ENROLL.getValue() , date);
            } catch (Exception e){
                throw new MessageException("401", "对方余额不足");
            }
            if(order == null){
                throw new MessageException("401", "该日期已超出可报名日期");
            }
        }

        String errorMsg = enrollCantByOrder(order , nowTime);
        if (errorMsg != null){
            //如果错误消息不为空，说明该订单有部分问题不允许报名，抛出错误信息，并在外面接到这个异常后将报名日历移除
            throw new MessageException("401", errorMsg);
        }
        enrollPri(order , nowTime , nowUser);

        //报名的通知

        if (order.getType() == ProductEnum.TYPE_SEEK_HELP.getValue()){
            //如果是求助
            if (orderRelationshipDao.selectJoinUser(order.getId()) == 0){
                //如果是首次报名
                String title = "您的求助收到报名啦";
                String content = new StringBuilder().append("您的求助“").append(order.getServiceName())
                        .append("”已经有小天使报名啦，快去看看吧！会有更多人报名的，记得及时选定您需要的人哦～").toString();
                messageCommonController.messageSave(order.getId() , nowUser , title , content , order.getCreateUser() , nowTime);

                TUser toUser = userCommonController.getUserById(order.getCreateUser());
                TFormid formid = findFormId(nowTime, toUser);
                if (formid != null) {
                    try {
                        List<String> msg = new ArrayList<>();
                        String parameter = "?orderId="+order.getId()+"&returnHome=true";
                        msg.add("已有一位小天使报名帮助你哦");
                        msg.add(order.getServiceName());
                        msg.add(nowUser.getName());
                        msg.add("会有更多人报名的，持续关注并及时选定您需要的人哦～");
                        messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , msg , SetTemplateIdEnum.help_setTemplate_3 , parameter);
                        formid.setIsValid("0");
                        messageCommonController.updateFormId(formid);
                    } catch (Exception e) {
                        logger.error("发送服务通知失败");
                    }
                }
            }
        } else {
            //如果是服务
            if (order.getServicePersonnel() == 1){
                //如果是一对一的服务
                String title = "服务有新订单了";
                String content = new StringBuilder().append("您发布的“").append(order.getServiceName())
                        .append("”服务，已有小伙伴").append(nowUser.getName()).append("对此感兴趣咯，赶快去看看吧！").toString();
                messageCommonController.messageSave(order.getId() , nowUser , title , content , order.getCreateUser() , nowTime);

                TUser toUser = userCommonController.getUserById(order.getCreateUser());
                TFormid formid = findFormId(nowTime, toUser);
                if (formid != null) {
                    try {
                        List<String> msg = new ArrayList<>();
                        String parameter = "?orderId="+order.getId()+"&returnHome=true";
                        msg.add(order.getServiceName());
                        msg.add(nowUser.getName());
                        msg.add(changeTime(nowTime));
                        msg.add("有人需要你的帮助！重复，有人需要你的帮助！");
                        messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , msg , SetTemplateIdEnum.serv_setTemplate_3 , parameter);
                        formid.setIsValid("0");
                        messageCommonController.updateFormId(formid);
                    } catch (Exception e) {
                        logger.error("发送服务通知失败");
                    }
                }
            } else {
                //如果是一对多的服务
                String title = "服务有新订单了";
                if (orderRelationshipDao.selectJoinUser(order.getId()) == 0){
                    //如果是首次报名
                    String content = new StringBuilder().append("您发布的“").append(order.getServiceName())
                            .append("”服务，已有小伙伴报名了。记得关注并联系他哦").toString();
                    messageCommonController.messageSave(order.getId() , nowUser , title , content , order.getCreateUser() , nowTime);

                    TUser toUser = userCommonController.getUserById(order.getCreateUser());
                    TFormid formid = findFormId(nowTime, toUser);
                    if (formid != null) {
                        try {
                            List<String> msg = new ArrayList<>();
                            String parameter = "?orderId="+order.getId()+"&returnHome=true";
                            msg.add(order.getServiceName());
                            msg.add(nowUser.getName());
                            msg.add(changeTime(nowTime));
                            msg.add("第一位有需要的小伙伴已就位，更多的正在路上，请持续关注哦。");
                            messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , msg , SetTemplateIdEnum.serv_setTemplate_4 , parameter);
                            formid.setIsValid("0");
                            messageCommonController.updateFormId(formid);
                        } catch (Exception e) {
                            logger.error("发送服务通知失败");
                        }
                    }
                }
            }
        }
        return 1l;
    }


    /**
     * 批量报名
     * @param userIdList
     * @param date
     * @param serviceId
     * @return
     */
    public List<String> orgEnroll(List<Long> userIdList  , String date , Long serviceId){
        List<String> msgList = new ArrayList<>();
        TOrder order = null;
        long nowTime = System.currentTimeMillis();
        if (order.getTimeType() == ProductEnum.TIME_TYPE_REPEAT.getValue()){
            //如果是重复性的，根据日历来进行查找订单，如果没有就创建新订单
            try {
                TService service = productCommonController.getProductById(serviceId);
                order = orderCommonController.produceOrder(service , OrderEnum.PRODUCE_TYPE_ENROLL.getValue() , date);
            } catch (Exception e){
                throw new MessageException("401", "您账户余额不足");
            }
            if(order == null){
                throw new MessageException("401", "该日期已超出可报名日期");
            }
        }
        String errorMsg = enrollCantByOrder(order , nowTime);
        if (errorMsg != null){
            //如果错误消息不为空，说明该订单有部分问题不允许报名，抛出错误信息，并在外面接到这个异常后将报名日历移除
            throw new MessageException("401", errorMsg);
        }

        List<TUser> userList = userCommonController.selectUserByIds(userIdList);

        for (int i = 0 ; i < userIdList.size() ; i++){
            TUser toUser = null;
            for (int j = 0 ; j < userList.size() ; j++){
                if (userIdList.get(i) == userList.get(j).getId().longValue()){
                    toUser = userList.get(j);
                    break;
                }
            }
            if (toUser == null){
                msgList.add("找不到用户编号："+userIdList.get(i)+"的用户信息");
            }

            try {
                enrollPri(order , nowTime , toUser);
            } catch (MessageException e){
                msgList.add("用户:"+toUser.getName()+"报名失败："+e.getMessage());
            }

        }
        if (msgList.size() == userIdList.size()){
            throw new MessageException("401", "所选用户中没有可操作用户");
        }
        return msgList;

    }


    /**
     * 取消报名
     *
     * @param orderId
     * @param nowUserId
     */
    @Transactional(rollbackFor = Throwable.class)
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
            unFreezeTime(orderRelationship.getCollectTime(), nowTime, nowUser, orderId);
            nowUser.setFreezeTime(nowUser.getFreezeTime() - orderRelationship.getCollectTime());
            nowUser.setUpdateTime(nowTime);
            nowUser.setUpdateUser(nowUser.getId());
            nowUser.setUpdateUserName(nowUser.getName());
            userCommonController.updateByPrimaryKey(nowUser);

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
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        if (order.getCreateUser() != nowUser.getId().longValue()) {
            throw new MessageException("499", "对不起，您不可以对非自己发布对互助进行操作");
        }
        List<TOrderRelationship> orderRelationshipList = new ArrayList<>();
        if (type == 1) {
            //状态1代表要查询选人列表
            orderRelationshipList = orderRelationshipDao.selectListByStatusByEnroll(orderId
                    , OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType());
        }
        if (type == 2) {
            //状态2代表投诉
            orderRelationshipList = orderRelationshipDao.selectListByStatusForNotSignByEnroll(orderId
                    , OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
        }
        if (type == 5){
            //状态为5代表取消
            long nowTime = System.currentTimeMillis();
            if (order.getStartTime() - nowTime < 0){
                throw new MessageException("499", "对不起，订单临近开始，不允许取消订单");
            }
            nowUser = userCommonController.getUserById(nowUserId);
            if (nowUser.getSurplusTime() + nowUser.getCreditLimit() - nowUser.getFreezeTime() < 0){
                //可用时间为负，说明该用户已经取消过一次，并且没有还清欠款
                throw new MessageException("499", "对不起，您可用余额不足，不允许取消订单");
            }
            if (order.getCollectType() == ProductEnum.COLLECT_TYPE_COMMONWEAL.getValue()){
                //如果是公益时
                throw new MessageException("499", "对不起，公益活动不可取消");
            }
            orderRelationshipList = orderRelationshipDao.selectCanRemoveEnrollUser(orderId);
        }
        if (type == 7) {
            //状态为7代表支付
            orderRelationshipList = orderRelationshipDao.selectListByStatusByEnroll(orderId
                    , OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
        }
        if (type == 9) {
            //默认是服务者发布者 找求助者评价服务者未评价
            statusList.add(OrderRelationshipEnum.STATUS_IS_REMARK.getType());
            statusList.add(OrderRelationshipEnum.STATUS_WAIT_REMARK.getType());
            orderRelationshipList = orderRelationshipDao.selectListByStatusListByEnroll(orderId, statusList);
        }
        List<Long> userIdList = new ArrayList<>();
        List<UserInfoView> userInfoViewList = new ArrayList<>();
        for (int i = 0; i < orderRelationshipList.size(); i++) {
            userIdList.add(orderRelationshipList.get(i).getReceiptUserId());
            UserInfoView userInfoView = new UserInfoView();
            userInfoView.setToStringId(orderRelationshipList.get(i).getReceiptUserId());
        }
        if (userIdList.size() == 0) {
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
                    userInfoView.setAuthStatus(userlist.get(i).getAuthenticationStatus());
                    if (type == 7 || type == 5) {
                        //如果是是支付，还要看一下未到人员
                        if (orderRelationshipList.get(i).getSignType() == OrderRelationshipEnum.SIGN_TYPE_NO.getType()) {
                            //如果是未签到，将状态置为未到
                            userInfoView.setStatus(3);
                        }
                    }
                    if (orderRelationshipList.get(i).getOrderReportType() == OrderRelationshipEnum.ORDER_REPORT_IS_BEREPORT.getType()
                            || orderRelationshipList.get(i).getOrderReportType() == OrderRelationshipEnum.ORDER_REPORT_IS_TURE.getType()) {
                        //如果订单内被举报了，状态置为异常
                        userInfoView.setStatus(2);
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
    @Transactional(rollbackFor = Throwable.class)
    public List<String> chooseUser(Long orderId, Long nowUserId, List<Long> userIdList) {
        TUser nowUser = userCommonController.getUserById(nowUserId);
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        if (order.getCreateUser() != nowUserId.longValue()) {
            throw new MessageException("499", "对不起，您不可以对非自己发布对互助进行操作");
        }
        List<Integer> statusList = new ArrayList<>();
        long canChooseUserSum = order.getServicePersonnel() - order.getConfirmNum();
        if (userIdList.size() > canChooseUserSum) {
            if (canChooseUserSum == 0) {
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
            TUser toUser = null;
            for (int j = 0; j < userList.size(); j++) {
                if (orderRelationshipList.get(i).getReceiptUserId() == userList.get(j).getId().longValue()) {
                    toUser = userList.get(i);
                    break;
                }
            }
            if (orderRelationshipList.get(i).getStatus() == OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType()) {
                orderRelationshipList.get(i).setStatus(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
                orderRelationshipList.get(i).setCreateTime(nowTime);
                orderRelationshipList.get(i).setCreateUser(nowUser.getId());
                orderRelationshipList.get(i).setCreateUserName(nowUser.getName());
                orderRelationshipIdList.add(orderRelationshipList.get(i).getId());

                //发送通知,默认为求助
                String title ="恭喜您已被求助者选定";

                String content = new StringBuilder().append("在“").append(order.getServiceName())
                        .append("的求助中，您已被求助者").append(nowUser.getName())
                        .append("选定为服务者啦！感谢您的热心，请与求助者保持联系并准时提供服务哦～").toString();
                if (order.getType() == ProductEnum.TYPE_SERVICE.getValue()){
                    //如果是服务
                    title = "服务者已经接单";
                    content = new StringBuilder().append("您报名的“").append(order.getServiceName())
                            .append("”服务，小伙伴").append(nowUser.getName())
                            .append("已确认接单咯。记得关注动态和保持与他的联系哦！").toString();
                }

                messageCommonController.messageSave(orderId ,nowUser , title , content , orderRelationshipList.get(i).getReceiptUserId() , nowTime);
                if (order.getCollectType() == ProductEnum.COLLECT_TYPE_EACHHELP.getValue()){
                    //TODO 如果是互助时，发送短信
                    String msgContent = "【壹晓时】"+content;
                }

                TFormid formid = findFormId(nowTime, toUser);
                if (formid != null) {
                    try {
                        List<String> msg = new ArrayList<>();
                        String parameter = "?orderId="+order.getId()+"&returnHome=true";
                        if (order.getType() == ProductEnum.TYPE_SEEK_HELP.getValue()){
                            //如果是求助
                            msg.add("您已被求助者选定");
                            msg.add(order.getServiceName());
                            msg.add(changeTime(order.getStartTime()));
                            msg.add(changeAddress(order.getAddressName()));
                            msg.add("万物遇见了阳光，而我遇见了热心的你，真好");
                            messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , msg ,  SetTemplateIdEnum.help_setTemplate_7 , parameter);
                        } else {
                            //如果是服务
                            msg.add("服务者确认接单啦！");
                            msg.add(order.getServiceName());
                            msg.add(nowUser.getName());
                            msg.add(changeTime(order.getStartTime()));
                            msg.add("Ta说了Yes！趁这一刻，还不主动出击保持联系？");
                            messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , msg , SetTemplateIdEnum.serv_setTemplate_5 , parameter);
                        }
                        formid.setIsValid("0");
                        messageCommonController.updateFormId(formid);
                    } catch (Exception e) {
                        logger.error("发送服务通知失败");
                    }
                }
            } else {
                errorMsg.add("用户" + toUser.getName() + "已被您操作");
            }
        }
        if (order.getTimeType() == ProductEnum.TIME_TYPE_REPEAT.getValue()){
            //如果是重复的互助，选满人要进行订单的派生
            if ((canChooseUserSum - orderRelationshipIdList.size()) == 0) {
                //如果选满人了，那么判断是否有下周期的订单，如果有不处理，没有就派生订单
                TService service = productCommonController.getProductById(orderRelationshipList.get(0).getServiceId());
                String date = DateUtil.getDate(orderRelationshipList.get(0).getStartTime());
                //将该订单设置为可见，并调用方法修改订单可见状态
                orderCommonController.changeOrderVisiableStatus(orderId, 1);
                //派生下一张订单
                try {
                    orderCommonController.produceOrder( service ,OrderEnum.PRODUCE_TYPE_ENOUGH.getValue(),date);
                } catch (NoEnoughCreditException e) {
                    logger.info("用户授信不足，无法派生订单");
                    errorMsg.add("因余额不足，您无法派生下一张订单");
                }
                //移除可报名日期
                removeCanEnrollDate(date , service.getId());
            }
        }

        if (orderRelationshipIdList.size() > 0) {
            //如果有更新的人批量更新
            orderRelationshipDao.updateOrderRelationshipByList(orderRelationshipList, orderRelationshipIdList);
        } else {
            throw new MessageException("499", "对不起，没有可供操作的用户");
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
     * 拒绝人选
     * @param orderId
     * @param userIdList
     * @param nowUserId
     * @param type 0-手动拒绝 1- 自动下架拒绝
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public List<String> unChooseUser(Long orderId, List<Long> userIdList, Long nowUserId , int type) {
        TUser nowUser = userCommonController.getUserById(nowUserId);
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        if (order.getCreateUser() != nowUserId.longValue()) {
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
                //发送通知
                String title = "";
                String content = "";
                if (type == 0){
                    //如果是手动拒绝
                    TFormid formid = findFormId(nowTime, toUser);
                    if (order.getType() == ProductEnum.TYPE_SEEK_HELP.getValue()){
                        //如果是求助
                        if (formid != null) {
                            try {
                                String parameter = "";
                                List<String> msg = new ArrayList<>();
                                msg.add("很遗憾，您未被求助者选中");
                                msg.add(order.getServiceName());
                                msg.add(toUser.getName());
                                msg.add("我们相信，这一次的错过，是为了彼此更好的相遇");
                                messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , msg , SetTemplateIdEnum.help_setTemplate_8 , parameter);
                                formid.setIsValid("0");
                                messageCommonController.updateFormId(formid);
                            } catch (Exception e) {
                                logger.error("发送服务通知失败");
                            }
                        }
                        title = "很遗憾，您未被求助者选中";
                        content = new StringBuilder().append("您报名的“").append(order.getServiceName())
                                .append("”求助，求助者").append(nowUser.getName())
                                .append("已选定其他人提供服务。不过没关系~还有更多晓主需要你的帮助，不如去看看吧～").toString();
                    } else {
                        //如果是服务
                        if (formid != null) {
                            try {
                                String parameter = "";
                                List<String> msg = new ArrayList<>();
                                msg.add("啊喔…TA似乎不太方便");
                                msg.add(order.getServiceName());
                                msg.add(nowUser.getName());
                                msg.add(changeTime(order.getStartTime()));
                                msg.add("很遗憾，您没有和TA成为小伙伴。但有更多人也发布了服务，下一个也许会更好呢？");
                                messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , msg , SetTemplateIdEnum.serv_setTemplate_6 , parameter);
                                formid.setIsValid("0");
                                messageCommonController.updateFormId(formid);
                            } catch (Exception e) {
                                logger.error("发送服务通知失败");
                            }
                        }
                        title = "很遗憾，服务者未接受交换";
                        content = new StringBuilder().append("您报名的“").append(order.getServiceName())
                                .append("”服务，小伙伴").append(nowUser.getName())
                                .append("未接受请求。您的互助时已解除冻结。错过是为了更好的相遇哦，不要灰心~").toString();
                    }
                } else {
                    //如果是自动拒绝（超时下架）
                    if (order.getType() == ProductEnum.TYPE_SEEK_HELP.getValue()){
                        //如果是求助
                        title = "求助下架通知";
                        content =new StringBuilder().append("您报名的求助“").append(order.getServiceName())
                                .append("”因求助者长时间未选定服务者，已默认下架处理啦~不过还有更多晓主等待您的帮助哦！").toString();
                    } else {
                        //如果是服务
                        title = "服务下架通知";
                        content =new StringBuilder().append("您报名的服务“").append(order.getServiceName())
                                .append("”，因超出结束时间已下架，您的互助时已解除冻结。错过是为了更好的相遇哦，不要灰心~").toString();
                    }

                }
                messageCommonController.messageSave(orderId ,nowUser , title , content , toUser.getId() , nowTime);
                if (order.getType() == 2) {
                    //如果是服务,解冻时间币
                    unFreezeTime(order.getCollectTime(), nowTime, toUser, orderId);
                }
            } else {
                errorMsg.add("用户" + toUser.getName() + "已被您操作");
            }
        }
        if (orderRelationshipIdList.size() > 0) {
            //如果有更新的人批量更新
            orderRelationshipDao.updateOrderRelationshipByList(orderRelationshipList, orderRelationshipIdList);
        } else {
            throw new MessageException("499", "对不起，没有可供操作的用户");
        }
        return errorMsg;
    }

    /**
     * 开始订单（签到）
     *
     * @param orderId
     * @param nowUserId
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public List<String> startOrder(Long orderId, Long nowUserId, List<Long> userIdList) {
        List<String> errorMsg = new ArrayList<>();
        TUser nowUser = userCommonController.getUserById(nowUserId);
        long nowTime = System.currentTimeMillis();
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        if (order.getCreateUser() == nowUser.getId().longValue()) {
            //如果是服务的发布者，改报名者状态
            List<Long> orderRelationshipIdList = new ArrayList<>();
            List<TUser> toUserList = userCommonController.selectUserByIds(userIdList);
            List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectByOrderIdAndEnrollUserIdList(orderId, userIdList);
            for (int i = 0; i < orderRelationshipList.size(); i++) {
                TUser toUser = new TUser();
                for (int j = 0; j < toUserList.size(); j++) {
                    if (toUserList.get(j).getId() == orderRelationshipList.get(i).getReceiptUserId().longValue()) {
                        toUser = toUserList.get(j);
                    }
                    break;
                }
                if (orderRelationshipList.get(i).getSignType() == OrderRelationshipEnum.SIGN_TYPE_YES.getType()) {
                    //如果用户已经被签到过了
                    errorMsg.add("您已确认为" + toUser.getName() + "开始服务～");
                } else {
                    //将该用户标记为签到状态
                    orderRelationshipList.get(i).setSignType(OrderRelationshipEnum.SIGN_TYPE_YES.getType());
                    orderRelationshipList.get(i).setUpdateTime(nowTime);
                    orderRelationshipList.get(i).setUpdateUser(nowUser.getId());
                    orderRelationshipList.get(i).setUpdateUserName(nowUser.getName());
                    orderRelationshipIdList.add(orderRelationshipList.get(i).getId());
                    //发送消息
                    String title = "服务者已开始服务";
                    String content = "";

                    //发送通知
                    TFormid formid = findFormId(nowTime, toUser);
                    if (order.getType() == ProductEnum.TYPE_SEEK_HELP.getValue()){
                        //如果是求助
                        content = new StringBuilder().append("在“").append(order.getServiceName())
                                .append("”的求助中，服务者已经确认开始服务。如有异常，您可以发起投诉。").toString();
                        if (formid != null) {
                            try {
                                List<String> msg = new ArrayList<>();
                                String parameter = "?orderId="+order.getId()+"&returnHome=true";
                                msg.add("报告，服务者已就位");
                                msg.add(order.getServiceName());
                                msg.add(changeTime(order.getStartTime()));
                                msg.add(changeAddress(order.getAddressName()));
                                msg.add("服务者已点击「确认开始」。如有异常，您可以发起投诉。");
                                messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , msg , SetTemplateIdEnum.help_setTemplate_14 , parameter);
                                formid.setIsValid("0");
                                messageCommonController.updateFormId(formid);
                            } catch (Exception e) {
                                logger.error("发送服务通知失败");
                            }
                        }
                    } else {
                        //如果是服务
                        content = new StringBuilder().append("您报名的“").append(order.getServiceName())
                                .append("”的服务中，服务者已经确认开始服务。如有异常，您可以发起投诉。").toString();
                        if (formid != null) {
                            try {
                                List<String> msg = new ArrayList<>();
                                String parameter = "?orderId="+order.getId()+"&returnHome=true";
                                msg.add("报告，服务者已就位");
                                msg.add(order.getServiceName());
                                msg.add(nowUser.getName());
                                msg.add(changeTime(order.getStartTime()));
                                msg.add("服务者已点击「确认开始」。如有异常，您可以发起投诉。");
                                messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , msg , SetTemplateIdEnum.serv_setTemplate_13 , parameter);
                                formid.setIsValid("0");
                                messageCommonController.updateFormId(formid);
                            } catch (Exception e) {
                                logger.error("发送服务通知失败");
                            }
                        }
                    }

                    messageCommonController.messageSave(orderId , nowUser , title , content , toUser.getId() , nowTime);

                }
            }
            orderRelationshipDao.updateOrderRelationshipByList(orderRelationshipList, orderRelationshipIdList);
        } else {
            //如果是报名者，改自己的开始状态，并且该状态的订单只有一个
            TOrderRelationship orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId, nowUser.getId());
            if (orderRelationship.getSignType() == OrderRelationshipEnum.SIGN_TYPE_YES.getType()) {
                //如果用户已经被签到过了
                throw new MessageException("499", "您已经签到过了～");
            }
            //将该用户标记为签到状态
            orderRelationship.setSignType(OrderRelationshipEnum.SIGN_TYPE_YES.getType());
            orderRelationship.setUpdateTime(nowTime);
            orderRelationship.setUpdateUser(nowUser.getId());
            orderRelationship.setUpdateUserName(nowUser.getName());
            orderRelationshipDao.updateByPrimaryKey(orderRelationship);
        }
        return errorMsg;
    }


    /**
     * @return java.util.List<java.lang.String>
     * @Author 姜修弘
     * 功能描述:订单支付
     * 创建时间:@Date 下午1:41 2019/3/8
     * @Param [orderId, userIdList, paymentList, nowUser]
     **/
    @Transactional(rollbackFor = Throwable.class)
    public List<String> payOrder(Long orderId, List<Long> userIdList, List<Long> paymentList, Long nowUserId , int type) {
        TUser nowUser = userCommonController.getUserById(nowUserId);
        List<String> msgList = new ArrayList<>();
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        long nowTime = System.currentTimeMillis();
        //支付时间总数默认为0
        long paymentSum = 0l;
        //支付公益时间数默认为0
        //long paymentByWelfareSum = 0l;
        //发布者id默认为0
        long publishUserId = 0l;
        //服务记录内容默认为空
        String content = "";
        //被支付人名字默认为空
        String payUserName = "";
        //支付成功数量初始默认为0
        int seekHelpDoneNum = 0;
        String collectType = "互助时";
        if (order.getCollectType() == OrderEnum.COLLECT_TYPE_WELFARE.getValue()) {
            //如果是公益时
            collectType = "公益时";
        }
        List<TUser> toUserList = userCommonController.selectUserByIds(userIdList);
        if (order.getCreateUser() == nowUser.getId().longValue()) {
            //如果当前用户是发布者，那么就查找报名者即可
            publishUserId = nowUserId;
            List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectByOrderIdAndEnrollUserIdList(orderId, userIdList);

            for (int i = 0; i < userIdList.size(); i++) {
                TOrderRelationship orderRelationship = null;
                TUser toUser = null;
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
                if (toUser == null) {
                    msgList.add("找不到用户" + userIdList.get(i) + "的账号信息");
                } else if (orderRelationship == null) {
                    msgList.add("找不到用户" + toUser.getName() + "的订单信息");
                } else {
                    String msg = payOrderPri(orderRelationship, paymentList.get(i), nowUser, nowTime, toUser , order);
                    if (msg != null) {
                        //支付失败，添加错误信息
                        msgList.add(msg);
                    } else if (paymentList.get(i) > 0) {
                        //如果是有效支付，增加钱数，增加支付成功次数
                        if (order.getCollectType() == OrderEnum.COLLECT_TYPE_TIME.getValue()) {
                            //如果收取的是互助时
                            paymentSum += paymentList.get(i);

                            //发送互助时系统通知
                            //发布者支付，只可能是求助
                            String title = "互助时已到账";
                            String msgContent = new StringBuilder().append("您已完成“").append(orderRelationship.getServiceName())
                                    .append("”的求助事项，获得的").append(paymentList.get(i))
                                    .append("分钟互助时已存入您的时间账户。真是棒棒哒！").toString();

                            TFormid formid = findFormId(nowTime, toUser);
                            if (formid != null) {
                                try {
                                    List<String> wxMsg = new ArrayList<>();
                                    String parameter = "?returnHome=true";
                                    wxMsg.add("叮！收到了一份时间谢礼");
                                    wxMsg.add(orderRelationship.getServiceName());
                                    wxMsg.add(nowUser.getName());
                                    wxMsg.add("发生的故事永远会被记住，如果您想回忆，就去「壹晓时」的时间账户看一看~");
                                    messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , wxMsg , SetTemplateIdEnum.help_setTemplate_17 , parameter);
                                    formid.setIsValid("0");
                                    messageCommonController.updateFormId(formid);
                                } catch (Exception e) {
                                    logger.error("发送服务通知失败");
                                }
                            }
                            messageCommonController.messageSave(orderRelationship.getId() , nowUser , title , msgContent , toUser.getId() , nowTime);
                        } else {
                            //如果是公益时，那么公益时数量要加
                            //paymentByWelfareSum += paymentList.get(i);

                            //只有发布者才可以支付公益时
                            String title = "公益时长又增加啦";
                            String msgContent = new StringBuilder().append("您已完成“").append(orderRelationship.getServiceName())
                                    .append("”公益服务，并获得").append(paymentList.get(i))
                                    .append("分钟的公益时长，您可以进入“我的-我的公益时”查看。").toString();

                            TFormid formid = findFormId(nowTime, toUser);
                            if (formid != null) {
                                try {
                                    List<String> wxMsg = new ArrayList<>();
                                    String parameter = "?returnHome=true";
                                    wxMsg.add("叮！你的公益时长有了新变化");
                                    wxMsg.add(paymentList.get(i)+"分钟");
                                    wxMsg.add("你做过最有意义的事情，已成功被时间记录，您可移步「壹晓时」查看。");
                                    messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , wxMsg , SetTemplateIdEnum.help_setTemplate_18 , parameter);
                                    formid.setIsValid("0");
                                    messageCommonController.updateFormId(formid);
                                } catch (Exception e) {
                                    logger.error("发送服务通知失败");
                                }
                            }
                            messageCommonController.messageSave(orderRelationship.getId() , nowUser , title , msgContent , toUser.getId() , nowTime);

                        }
                        seekHelpDoneNum++;
                        //对服务记录的人的名字进行合并处理
                        if (seekHelpDoneNum == 1) {
                            payUserName = payUserName + toUser.getName();
                        } else if (seekHelpDoneNum == 2 || seekHelpDoneNum == 3) {
                            payUserName = payUserName + "、" + toUser.getName();
                        } else if (seekHelpDoneNum == 4) {
                            payUserName = payUserName + "等";
                        }
                    }
                }
            }
            //服务记录内容
            if (type == 1){
                //如果类型为个人手动支付
                if (seekHelpDoneNum > 3) {
                    content = nowUser.getName() + " 支付了" + payUserName + seekHelpDoneNum + "人" + collectType + timeChange(paymentSum);
                } else {
                    content = nowUser.getName() + " 支付了" + payUserName + collectType + timeChange(paymentSum);
                }

            } else if (type == 2){
                //如果是自动
                if (seekHelpDoneNum > 3) {
                    content = "系统自动 支付了" + payUserName + seekHelpDoneNum + "人" + collectType + timeChange(paymentSum);
                } else {
                    content = "系统自动 支付了" + payUserName + collectType + timeChange(paymentSum);
                }

            }

        } else {
            //如果是报名者支付，要修改自己的订单状态
            publishUserId = userIdList.get(0);
            TOrderRelationship orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId, nowUserId);
            String msg = payOrderPri(orderRelationship, paymentList.get(0), nowUser, nowTime, toUserList.get(0) , order);
            if (msg != null) {
                throw new MessageException("499", msg);
            }
            if (paymentList.get(0) > 0) {
                //如果是有效支付
                if (order.getCollectType() == OrderEnum.COLLECT_TYPE_TIME.getValue()) {
                    //如果收取的是互助时
                    paymentSum += paymentList.get(0);
                }
                seekHelpDoneNum++;

                //服务记录内容
                if (type == 1){
                    //如果类型为个人手动支付
                    content = nowUser.getName() + " 支付了" + toUserList.get(0).getName() + collectType + timeChange(paymentSum);

                } else if (type == 2){
                    //如果是自动
                    content = "系统自动 支付了" + toUserList.get(0).getName() + collectType + timeChange(paymentSum);

                }

                //如果是报名者支付，只可能是服务
                if (order.getServicePersonnel() == 1){
                    //如果是一对一服务
                    String title = "互助时到账提醒";
                    String msgContent = new StringBuilder().append("您完成“").append(orderRelationship.getServiceName())
                            .append("”服务获得小伙伴").append(nowUser.getName()).append("的").append(paymentList.get(0))
                            .append("分钟互助时已经存入您的时间账户。快去查看吧~").toString();

                    TFormid formid = findFormId(nowTime, toUserList.get(0));
                    if (formid != null) {
                        try {
                            List<String> wxMsg = new ArrayList<>();
                            String parameter = "?returnHome=true";
                            wxMsg.add("叮！收到时间谢礼");
                            wxMsg.add(orderRelationship.getServiceName());
                            wxMsg.add(new StringBuilder().append(nowUser.getName()).append("等")
                                    .append(order.getServicePersonnel()).append("人").toString());
                            wxMsg.add("发生的故事永远会被记住，如果您想回忆，就去「壹晓时」的时间账户看一看~");
                            messageCommonController.pushOneUserMsg(toUserList.get(0).getVxOpenId() , formid.getFormId() , wxMsg , SetTemplateIdEnum.serv_setTemplate_15 , parameter);
                            formid.setIsValid("0");
                            messageCommonController.updateFormId(formid);
                        } catch (Exception e) {
                            logger.error("发送服务通知失败");
                        }
                    }
                    messageCommonController.messageSave(orderRelationship.getId() , nowUser , title , msgContent , toUserList.get(0).getId() , nowTime);

                }
            }

            if (order.getServicePersonnel() > 0){
                //如果是多人服务
                //看一下是不是最后一个人支付
                long complete = orderRelationshipDao.selectCompleteUserSum(orderId);
                if (complete == order.getServicePersonnel() - 1){
                    //如果是支付的最后一个人，如果是
                    List<TUserTimeRecord> userTimeRecordList = userCommonController.selectGetTimeByOrder(orderId);
                    long paySum = 0l;
                    for (int i = 0 ; i < userTimeRecordList.size() ; i++){
                        paySum = paySum + userTimeRecordList.get(i).getTime();
                    }
                    String title = "互助时到账提醒";
                    String msgContent = new StringBuilder().append("您完成“").append(orderRelationship.getServiceName())
                            .append("”服务获得的").append(paySum)
                            .append("分钟互助时已存入您的时间账户。").toString();

                    TFormid formid = findFormId(nowTime, toUserList.get(0));
                    if (formid != null) {
                        try {
                            List<String> wxMsg = new ArrayList<>();
                            String parameter = "?returnHome=true";
                            wxMsg.add("叮！收到了一份时间谢礼");
                            wxMsg.add(orderRelationship.getServiceName());
                            wxMsg.add(nowUser.getName());
                            wxMsg.add("发生的故事永远会被记住，如果您想回忆，就去「壹晓时」的时间账户看一看~");
                            messageCommonController.pushOneUserMsg(toUserList.get(0).getVxOpenId() , formid.getFormId() , wxMsg , SetTemplateIdEnum.serv_setTemplate_16 , parameter);
                            formid.setIsValid("0");
                            messageCommonController.updateFormId(formid);
                        } catch (Exception e) {
                            logger.error("发送服务通知失败");
                        }
                    }
                    messageCommonController.messageSave(orderRelationship.getId() , nowUser , title , msgContent , toUserList.get(0).getId() , nowTime);

                }
            }



        }

        if (userIdList.size() == msgList.size()) {
            //如果全错，返回错误消息，支付失败
            throw new MessageException("499", "所选用户中没有可支付用户");
        }
        if (order.getCollectType() == OrderEnum.COLLECT_TYPE_TIME.getValue()) {
            //解冻时间币，解冻数量是支付成功人数量(如果支付了0也算成功，虽然不显示，但是要解冻)*时间币单价
            unFreezeTime(order.getCollectTime() * (userIdList.size() - msgList.size()), nowTime, nowUser, orderId);
            //更新用户的信息
            nowUser.setFreezeTime(nowUser.getFreezeTime() - order.getCollectTime() * (userIdList.size() - msgList.size()));
            nowUser.setPayNum(nowUser.getPayNum() + Integer.parseInt(String.valueOf(seekHelpDoneNum)));
            nowUser.setSurplusTime(nowUser.getSurplusTime() - paymentSum);
            nowUser.setUpdateTime(nowTime);
            nowUser.setUpdateUser(nowUser.getId());
            nowUser.setUpdateUserName(nowUser.getName());
            userCommonController.updateByPrimaryKey(nowUser);
        }
        //查看待支付人数，如果和支付人数相等，将发布用户状态置为待评价或已无效
        long count = orderRelationshipDao.selectCountByStatusByEnroll(orderId, OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
        if (seekHelpDoneNum > 0) {
            //如果有有效支付人数，那么要改变发布者订单关系表，插入服务记录，判断是否首次完成

            if (count == (userIdList.size() - msgList.size())) {
                //没有要支付的人，就将发布者订单关系置为待评价
                TOrderRelationship publishOrderRela = orderRelationshipDao.selectByOrderIdAndUserId(orderId, publishUserId);
                publishOrderRela.setStatus(OrderRelationshipEnum.STATUS_WAIT_REMARK.getType());
                publishOrderRela.setUpdateTime(nowTime);
                publishOrderRela.setUpdateUser(nowUserId);
                publishOrderRela.setUpdateUserName(nowUser.getName());

                orderRelationshipDao.updateByPrimaryKey(publishOrderRela);
            }
            //插入服务记录
            recoreSave(orderId, content, nowUser, nowTime);

            if (order.getCollectType() == OrderEnum.COLLECT_TYPE_TIME.getValue()) {
                //如果是互助时，执行与公益时不同的操作
                //增加完成求助成长值
                userCommonController.taskComplete(nowUser , GrowthValueEnum.GROWTH_TYPE_REP_HELP_DONE , seekHelpDoneNum);
            }
        } else {
            //如果支付成功的人都是支付的0
            if (count == (userIdList.size() - msgList.size())) {
                //没有要支付的人，就将发布者订单关系置为无关系
                TOrderRelationship publishOrderRela = orderRelationshipDao.selectByOrderIdAndUserId(orderId, publishUserId);
                publishOrderRela.setStatus(OrderRelationshipEnum.STATUS_NOT_ESTABLISHED.getType());
                publishOrderRela.setUpdateTime(nowTime);
                publishOrderRela.setUpdateUser(nowUserId);
                publishOrderRela.setUpdateUserName(nowUser.getName());

                orderRelationshipDao.updateByPrimaryKey(publishOrderRela);
            }
        }


        return msgList;
    }

    /**
     * 新增发布者订单关系
     * @param order
     */
    public int addTorderRelationship(TOrder order){
        TOrderRelationship orderRelationship = new TOrderRelationship();
        orderRelationship.setServiceId(order.getServiceId());
        orderRelationship.setOrderId(order.getId());
        orderRelationship.setServiceType(order.getType());
        orderRelationship.setFromUserId(order.getCreateUser());
        orderRelationship.setSignType(OrderRelationshipEnum.SIGN_TYPE_NO.getType());
        orderRelationship.setStatus(OrderRelationshipEnum.STATUS_NO_STATE.getType());
        orderRelationship.setServiceReportType(OrderRelationshipEnum.SERVICE_REPORT_IS_NO.getType());
        orderRelationship.setOrderReportType(OrderRelationshipEnum.ORDER_REPORT_IS_NO.getType());
        orderRelationship.setServiceCollectionType(OrderRelationshipEnum.SERVICE_COLLECTION_IS_NO.getType());
        orderRelationship.setServiceName(order.getServiceName());
        orderRelationship.setStartTime(order.getStartTime());
        orderRelationship.setEndTime(order.getEndTime());
        orderRelationship.setTimeType(order.getTimeType());
        orderRelationship.setCollectTime(order.getCollectTime());
        orderRelationship.setCollectType(order.getCollectType());
        orderRelationship.setCreateUser(order.getCreateUser());
        orderRelationship.setCreateUserName(order.getCreateUserName());
        orderRelationship.setCreateTime(order.getCreateTime());
        orderRelationship.setUpdateUser(order.getCreateUser());
        orderRelationship.setUpdateUserName(order.getCreateUserName());
        orderRelationship.setUpdateTime(order.getCreateTime());
        orderRelationship.setIsValid(AppConstant.IS_VALID_YES);
        return orderRelationshipDao.insert(orderRelationship);
    }

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
    @Transactional(rollbackFor = Throwable.class)
    public List<String> repors(long orderId, long labelsId, String message, String voucherUrl, Long nowUserId, List<Long> userIds) {
        long nowTime = System.currentTimeMillis();
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        TUser nowUser = userCommonController.getUserById(nowUserId);
        List<String> errorMsgList = new ArrayList<>();
        if (order.getCreateUser() == nowUser.getId().longValue()) {
            //如果是发布者,投诉放到对方数据里
            List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectByOrderIdAndEnrollUserIdList(orderId, userIds);
            List<TUser> toUserList = userCommonController.selectUserByIds(userIds);
            for (int i = 0; i < orderRelationshipList.size(); i++) {
                TUser toUser = null;
                for (int j = 0; j < toUserList.size(); j++) {
                    if (orderRelationshipList.get(i).getReceiptUserId() == toUserList.get(j).getId().longValue()) {
                        toUser = toUserList.get(j);
                        break;
                    }
                }
                if (toUser == null) {
                    errorMsgList.add("找不到用户" + userIds.get(i) + "的数据");
                } else {
                    String msg = report(orderRelationshipList.get(i), labelsId, message, voucherUrl, nowUser, toUser, nowTime, false);
                    if (msg != null) {
                        errorMsgList.add(msg);
                    }
                }

            }
        } else {
            //如果不是发布者，那么只会投诉一个人，然后这个投诉记录在自己的订单关系上
            TOrderRelationship orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId, nowUser.getId());
            TUser touser = userCommonController.getUserById(userIds.get(0));
            String msg = report(orderRelationship, labelsId, message, voucherUrl, nowUser, touser, nowTime, true);
            if (msg != null) {
                throw new MessageException("499", msg);
            }
        }
        if (userIds.size() == errorMsgList.size()) {
            //如果全错，返回错误消息，支付失败
            throw new MessageException("499", "所选用户中没有可投诉用户");
        }
        //系统通知，投诉受理
        String title = "投诉受理通知";
        String content = "平台将尽快核实您的投诉事项，并在3-5个工作日内向您反馈处理结果。";
        messageCommonController.messageSave(orderId, nowUser, title, content, nowUser.getId(), nowTime);
        //修改发布者订单关系状态
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {

                if(status<0){
                    return;
                }
                changePublishOrderRela(order , order.getCreateUser());
                super.afterCommit();
            }
        });
        return errorMsgList;
    }

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
    @Transactional(rollbackFor = Throwable.class)
    public List<String> remarkOrder(Long nowUserId, List<Long> userIdList, Long orderId , int credit, int major, int attitude, String message, String labels) {
        List<String> errorMsgList = new ArrayList<>();
        TUser nowUser = userCommonController.getUserById(nowUserId);
        long nowTime = System.currentTimeMillis();
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        List<TUser> toUserList = userCommonController.selectUserByIds(userIdList);
        if (order.getCreateUser() == nowUser.getId().longValue()) {
            //如果是发布者，要评价很多人，查报名者，要改对方的状态
            List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectByOrderIdAndEnrollUserIdList(orderId, userIdList);
            for (int i = 0; i < userIdList.size(); i++) {
                TOrderRelationship orderRelationship = null;
                TUser toUser = null;
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
                if (toUser == null) {
                    errorMsgList.add("找不到用户" + userIdList.get(i) + "的账号信息");
                } else if (orderRelationship == null) {
                    errorMsgList.add("找不到用户" + toUser.getName() + "的订单信息");
                } else {
                    String msg = remark(orderRelationship, nowUser, toUser, nowTime, false);
                    if (msg != null) {
                        //更新状态表失败失败，添加错误信息
                        errorMsgList.add(msg);
                    } else {
                        //插入评价，并且更新用户评分数据
                        insertRemarkAndUpdateUser(nowUser , toUser , order , credit , major , attitude , message , labels , nowTime );
                    }
                }
            }
        } else {
            //如果是报名者，要修改自己的订单状态
            TUser toUser = userCommonController.getUserById(userIdList.get(0));
            TOrderRelationship orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId, nowUserId);
            String msg = remark(orderRelationship, nowUser, toUser, nowTime, true);
            if (msg != null) {
                throw new MessageException("499", msg);
            }
        }
        //修改发布者订单关系状态
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {

                if(status<0){
                    return;
                }
                changePublishOrderRela(order , order.getCreateUser());
                super.afterCommit();
            }
        });

        if (userIdList.size() == errorMsgList.size()){
            throw new MessageException("499", "所选用户中没有可评价用户");
        }
        userCommonController.taskComplete(nowUser , GrowthValueEnum.GROWTH_TYPE_REP_COMMENT , (userIdList.size() - errorMsgList.size()));
        return errorMsgList;
    }

    /**
     * 取消订单弹窗提醒每人扣除时间数
     * @param orderId
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public long removeOrderTips(Long orderId , Long nowUserId){
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        long nowTime = System.currentTimeMillis();
        if (order.getStartTime() - nowTime < 0){
            throw new MessageException("499", "对不起，订单已开始，不允许取消订单");
        }
        TUser nowUser = userCommonController.getUserById(nowUserId);
        if (nowUser.getSurplusTime() + nowUser.getCreditLimit() - nowUser.getFreezeTime() < 0){
            //可用时间为负，说明该用户已经取消过一次，并且没有还清欠款
            throw new MessageException("499", "对不起，您可用余额不足，不允许取消订单");
        }
        if (order.getCollectType() == ProductEnum.COLLECT_TYPE_COMMONWEAL.getValue()){
            //如果是公益时
            throw new MessageException("499", "对不起，公益活动不可取消");
        }
        return removeOrderTimeCoin(order , nowTime );

    }

    /**
     * 取消订单
     * @param orderId
     * @param userIdList
     * @param nowUserId
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public List<String> removeOrder(Long orderId , List<Long> userIdList , Long nowUserId){
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        long nowTime = System.currentTimeMillis();
        if (order.getStartTime() - nowTime < 0){
            throw new MessageException("499", "对不起，订单已开始，不允许取消订单");
        }
        long payment = removeOrderTimeCoin(order , nowTime);
        TUser nowUser = userCommonController.getUserById(nowUserId);
        if (nowUser.getSurplusTime() + nowUser.getCreditLimit() - nowUser.getFreezeTime() < 0){
            //可用时间为负，说明该用户已经取消过一次，并且没有还清欠款
            throw new MessageException("499", "对不起，您可用余额不足，不允许取消订单");
        }
        if (order.getCollectType() == ProductEnum.COLLECT_TYPE_COMMONWEAL.getValue()){
            //如果是公益时
            throw new MessageException("499", "对不起，公益活动不可取消");
        }
        List<TUser> toUserList = userCommonController.selectUserByIds(userIdList);
        List<String> errorMsgList = new ArrayList<>();
        if (order.getCreateUser() == nowUser.getId().longValue()) {
            //如果是发布者，要取消很多人，查报名者，要改对方的状态
            List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectByOrderIdAndEnrollUserIdList(orderId, userIdList);
            for (int i = 0; i < userIdList.size(); i++) {
                TOrderRelationship orderRelationship = null;
                TUser toUser = null;
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
                if (toUser == null) {
                    errorMsgList.add("找不到用户" + userIdList.get(i) + "的账号信息");
                } else if (orderRelationship == null) {
                    errorMsgList.add("找不到用户" + toUser.getName() + "的订单信息");
                } else {
                    String msg = removeOrderPri(orderRelationship, nowUser, toUser, nowTime, false);
                    if (msg != null) {
                        //更新状态表失败失败，添加错误信息
                        errorMsgList.add(msg);
                    } else {
                        String title = "TA取消了本次互助";
                        String typeName = ProductEnum.TYPE_SEEK_HELP.getDesc();
                        if (order.getType() == ProductEnum.TYPE_SERVICE.getValue()){
                            typeName = ProductEnum.TYPE_SERVICE.getDesc();
                        }
                        String content = "";
                        if (payment > 0){
                            //插入赔付流水，并且插入弹窗事件
                            removeOrderPunishment(nowUser , toUser , payment , orderId , nowTime);
                            content = new StringBuilder().append("很遗憾，在“").append(orderRelationship.getServiceName())
                                    .append("”的").append(typeName).append("事项中，").append(nowUser.getName())
                                    .append("已经取消本次互助。临时取消实在抱歉，TA为您准备了一份致歉礼，请您前往本订单页面查收～").toString();

                            TFormid formid = findFormId(nowTime, toUser);
                            if (formid != null) {
                                try {
                                    List<String> wxMsg = new ArrayList<>();
                                    String parameter = "?orderId="+order.getId()+"&returnHome=true";
                                    wxMsg.add("：很遗憾，TA因故取消了互助");
                                    wxMsg.add(orderRelationship.getServiceName());
                                    wxMsg.add(nowUser.getName());
                                    wxMsg.add("这一次的错过，是为了彼此更好的相遇");
                                    wxMsg.add("临时取消实在抱歉，TA为您准备了一份致歉礼，请点击查收～");
                                    messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , wxMsg , SetTemplateIdEnum.help_setTemplate_22 , parameter);
                                    formid.setIsValid("0");
                                    messageCommonController.updateFormId(formid);
                                } catch (Exception e) {
                                    logger.error("发送服务通知失败");
                                }
                            }

                        } else {
                            //如果是免费取消

                            content = new StringBuilder().append("很遗憾，在“").append(orderRelationship.getServiceName())
                                    .append("”的").append(typeName).append("事项中，").append(nowUser.getName())
                                    .append("已经取消本次互助。您可以去报名其他互助，或者继续选定其他已报名的小伙伴～").toString();

                            TFormid formid = findFormId(nowTime, toUser);
                            if (formid != null) {
                                try {
                                    List<String> wxMsg = new ArrayList<>();
                                    String orderType = "2";//服务通知的接收者是报名者
                                    String parameter = "";
                                    wxMsg.add("：很遗憾，TA因故取消了互助");
                                    wxMsg.add(orderRelationship.getServiceName());
                                    wxMsg.add(nowUser.getName());
                                    wxMsg.add("这一次的错过，是为了彼此更好的相遇");
                                    wxMsg.add("您可以去报名其他互助，或者继续选定其他已报名的小伙伴");
                                    messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , wxMsg , SetTemplateIdEnum.help_setTemplate_21 , parameter);
                                    formid.setIsValid("0");
                                    messageCommonController.updateFormId(formid);
                                } catch (Exception e) {
                                    logger.error("发送服务通知失败");
                                }
                            }
                        }
                        //发送消息
                        messageCommonController.messageSave(orderId , nowUser , title , content , toUser.getId() , nowTime);
                        if (order.getType() == ProductEnum.TYPE_SERVICE.getValue()){
                            //如果是服务，要将报名者时间解冻
                            unFreezeTime(order.getCollectTime() , nowTime , toUser , orderId);
                        }
                    }
                }
            }
            if (errorMsgList.size() == toUserList.size()){
                throw new MessageException("499", "对不起，没有可以操作的用户");
            }

            //将用户支付的钱支付出去
            nowUser.setSurplusTime(nowUser.getSurplusTime() - payment * (toUserList.size() - errorMsgList.size()));
            nowUser.setUpdateUser(nowUser.getId());
            nowUser.setUpdateUserName(nowUser.getName());
            nowUser.setUpdateTime(nowTime);
            userCommonController.updateByPrimaryKey(nowUser);
        } else {
            //如果是报名者，要修改自己的订单状态
            TUser toUser = userCommonController.getUserById(userIdList.get(0));
            TOrderRelationship orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId, nowUserId);
            String msg = removeOrderPri(orderRelationship, nowUser, toUser, nowTime, false);
            if (msg != null) {
                throw new MessageException("499", msg);
            }
            String title = "TA取消了本次互助";
            String typeName = ProductEnum.TYPE_SEEK_HELP.getDesc();
            if (order.getType() == ProductEnum.TYPE_SERVICE.getValue()){
                typeName = ProductEnum.TYPE_SERVICE.getDesc();
            }
            String content = new StringBuilder().append("很遗憾，在“").append(orderRelationship.getServiceName())
                    .append("”的").append(typeName).append("事项中，").append(nowUser.getName())
                    .append("已经取消本次互助。您可以去报名其他互助，或者继续选定其他已报名的小伙伴～").toString();
            if (payment > 0){
                //插入赔付流水，并且插入弹窗事件
                removeOrderPunishment(nowUser , toUser , payment , orderId , nowTime);
                //改变消息名字
                content = new StringBuilder().append("很遗憾，在“").append(orderRelationship.getServiceName())
                        .append("”的").append(typeName).append("事项中，").append(nowUser.getName())
                        .append("已经取消本次互助。临时取消实在抱歉，TA为您准备了一份致歉礼，请您前往本订单页面查收～").toString();
            }
            //发送消息
            messageCommonController.messageSave(orderId , nowUser , title , content , toUser.getId() , nowTime);
            //将用户支付的钱支付出去
            long surplusTime = nowUser.getSurplusTime();
            if (order.getType() == ProductEnum.TYPE_SERVICE.getValue()){
                //如果是服务，那么要将报名者时间币解冻
                unFreezeTime(order.getCollectTime() , nowTime , nowUser , orderId);
                surplusTime = surplusTime + order.getCollectTime();
                nowUser.setFreezeTime(nowUser.getFreezeTime() - order.getCollectTime());
            }
            nowUser.setSurplusTime(surplusTime - payment);
            nowUser.setUpdateUser(nowUser.getId());
            nowUser.setUpdateUserName(nowUser.getName());
            nowUser.setUpdateTime(nowTime);
            userCommonController.updateByPrimaryKey(nowUser);
        }
        if (userIdList.size() == errorMsgList.size()){
            throw new MessageException("499", "所选用户中没有可取消用户");
        }
        //修改发布者订单关系状态
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {

                if(status<0){
                    return;
                }
                changePublishOrderRela(order , order.getCreateUser());
                super.afterCommit();
            }
        });
        order.setConfirmNum(order.getConfirmNum() - toUserList.size() + errorMsgList.size());
        if (order.getConfirmNum() == order.getServicePersonnel()){
            //如果确认人选数量和需要人数量相等
            //改为可见，修改可报名日期
            TService service = productCommonController.getProductById(order.getServiceId());
            String date = DateUtil.getDate(order.getStartTime());
            service.setEnrollDate(service.getEnrollDate()+","+date);
            productCommonController.update(service);
            order.setVisiableStatus(OrderEnum.VISIABLE_NO.getStringValue());
            orderDao.updateByPrimaryKey(order);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    super.afterCommit();
                    orderCommonController.changeOrderVisiableStatus(orderId, 2);
                }
            });
        } else {
            orderDao.updateByPrimaryKey(order);
        }
        return errorMsgList;
    }


    /**
     * 接受时间赠礼
     * @param userTimeRecordId
     * @param eventId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void acceptGiftForRemove(Long userTimeRecordId , Long eventId){
        TUserTimeRecord userTimeRecord = userCommonController.selectUserTimeRecordById(userTimeRecordId);
        TUser getUser = userCommonController.getUserById(userTimeRecord.getUserId());
        getGiftForRemove(getUser , userTimeRecordId , eventId , userTimeRecord , PaymentEnum.PAYMENT_TYPE_REMOVE_ORDER_INDEMNITY_IN.getCode());
    }
    /**
     * 拒绝时间赠礼
     * @param userTimeRecordId
     * @param eventId
     */
    public void unAcceptGiftForRemove( Long userTimeRecordId , Long eventId){
        TUserTimeRecord userTimeRecord = userCommonController.selectUserTimeRecordById(userTimeRecordId);
        TUser getUser = userCommonController.getUserById(userTimeRecord.getFromUserId());
        getGiftForRemove(getUser , userTimeRecordId , eventId , userTimeRecord , PaymentEnum.PAYMENT_TYPE_REMOVE_ORDER_INDEMNITY_BACK.getCode());
    }

    /**
     * 举报详情
     * @param orderId
     * @param nowUser
     * @param labelsId
     * @param message
     * @param voucherUrl
     */
    @Transactional(rollbackFor = Throwable.class)
    public void reoprtOrder(Long orderId , TUser nowUser , long labelsId , String message , String voucherUrl){
    TOrder order = orderDao.selectByPrimaryKey(orderId);
    if(order.getCreateUser() == nowUser.getId().longValue()){
        throw new MessageException("499", "这是您自己的订单～");
    }
    long nowTime = System.currentTimeMillis();
    TOrderRelationship orderRelationship = null;
    orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId ,nowUser.getId());
    if (orderRelationship == null){
        //如果没有订单，那么就创建一张无状态订单,并且将详情举报状态设置为举报
        orderRelationship = new TOrderRelationship();
        orderRelationship.setServiceId(order.getServiceId());
        orderRelationship.setOrderId(order.getId());
        orderRelationship.setServiceType(order.getType());
        orderRelationship.setFromUserId(order.getCreateUser());
        orderRelationship.setReceiptUserId(nowUser.getId());
        orderRelationship.setSignType(OrderRelationshipEnum.SIGN_TYPE_NO.getType());
        orderRelationship.setStatus(OrderRelationshipEnum.STATUS_NO_STATE.getType());
        orderRelationship.setServiceReportType(OrderRelationshipEnum.SERVICE_REPORT_IS_TURE.getType());
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
        orderRelationship.setIsValid(AppConstant.IS_VALID_YES);
        orderRelationshipDao.insert(orderRelationship);
    } else {
        if (orderRelationship.getServiceReportType() == OrderRelationshipEnum.SERVICE_REPORT_IS_TURE.getType()){
            throw new MessageException("499", "您已投诉，请勿多次投诉");
        }
        orderRelationship.setServiceReportType(OrderRelationshipEnum.SERVICE_REPORT_IS_TURE.getType());
        orderRelationshipDao.updateByPrimaryKey(orderRelationship);
    }

        TReport report = new TReport();
        report.setReportUserId(0l);
        report.setType(ReportEnum.TPYE_SERVICE.getType());
        report.setAssociationId(orderId);
        report.setLabelsId(labelsId);
        report.setMessage(message);
        report.setVoucherUrl(voucherUrl);
        report.setStatus(ReportEnum.STATUS_PENDING_DISPOSAL.getType());
        report.setCreateTime(nowTime);
        report.setCreateUser(nowUser.getId());
        report.setCreateUserName(nowUser.getName());
        report.setUpdateTime(nowTime);
        report.setUpdateUser(nowUser.getId());
        report.setUpdateUserName(nowUser.getName());
        report.setIsValid(AppConstant.IS_VALID_YES);
        reportDao.saveOneOrder(report);


    }


    /**
     * 获取评价标签
     *
     * @param type
     * @param credit
     * @param major
     * @param attitude
     *
     *     "success": true,
     *     "errorCode": "",
     *     "msg": "报名成功",
     *     "data": "与描述不符,态度差,不守时,不专业,服务差" 评价标签逗号分割
     *
     * @return
     */
    public String getRemarkLabels(int type , int credit, int major, int attitude){
        RemarkLablesView remarkLablesView = null;
        String lables = "";
        if (type == 1){
            //如果是求助标签
            remarkLablesView = messageCommonController.getAllRemarkLables("seekHelpRemark");
        } else {
            remarkLablesView = messageCommonController.getAllRemarkLables("serviceRemark");
        }
        if (remarkLablesView == null){
            return lables;
        }
        if (credit > 3){
            //如果是4星及以上
            lables = new StringBuilder().append(remarkLablesView.getGoodCredit()).toString();
        } else {
            //如果是3星及以下
            lables = new StringBuilder().append(remarkLablesView.getBadCredit()).toString();
        }
        if (major > 3){
            //如果是4星及以上
            lables = new StringBuilder().append(lables).append(",").append(remarkLablesView.getGoodMajor()).toString();
        } else {
            //如果是3星及以下
            lables = new StringBuilder().append(lables).append(",").append(remarkLablesView.getBadMajor()).toString();
        }
        if (attitude > 3){
            //如果是4星及以上
            lables = new StringBuilder().append(lables).append(",").append(remarkLablesView.getGoodAttitude()).toString();
        } else {
            //如果是3星及以下
            lables = new StringBuilder().append(lables).append(",").append(remarkLablesView.getBadAttitude()).toString();
        }
        return lables;
    }
    /**
     * 获取时间赠礼（接受或者拒绝）
     * @param getUser
     * @param userTimeRecordId
     * @param eventId
     */
    private void getGiftForRemove(TUser getUser , Long userTimeRecordId , Long eventId , TUserTimeRecord userTimeRecord , int type){
        long nowTime = System.currentTimeMillis();
        //移除事件
        TEvent event = messageCommonController.selectTeventById(eventId);
        event.setIsValid(AppConstant.IS_VALID_NO);
        messageCommonController.updateTevent(event);
        //增加流水
        TUserTimeRecord myUserTimeRecord = new TUserTimeRecord();
        myUserTimeRecord.setUserId(getUser.getId());
        myUserTimeRecord.setFromUserId(userTimeRecord.getUserId());
        myUserTimeRecord.setType(type);
        myUserTimeRecord.setTargetId(userTimeRecord.getTargetId());
        myUserTimeRecord.setTime(userTimeRecord.getTime());
        myUserTimeRecord.setCreateTime(nowTime);
        myUserTimeRecord.setCreateUser(getUser.getId());
        myUserTimeRecord.setCreateUserName(getUser.getName());
        myUserTimeRecord.setUpdateTime(nowTime);
        myUserTimeRecord.setUpdateUserName(getUser.getName());
        myUserTimeRecord.setUpdateUser(getUser.getId());
        myUserTimeRecord.setIsValid(AppConstant.IS_VALID_YES);

        userCommonController.insertUserTimeRecords(myUserTimeRecord);

        //增加个人时间
        getUser.setSurplusTime(getUser.getSurplusTime() + userTimeRecord.getTime());
        getUser.setUpdateTime(nowTime);
        getUser.setUpdateUser(getUser.getId());
        getUser.setUpdateUserName(getUser.getName());

        userCommonController.updateByPrimaryKey(getUser);
    }


    /**
     * 取消订单私有方法
     * @param orderRelationship
     * @param nowUser
     * @param toUser
     * @param nowTiem
     * @param isPublish
     * @return
     */
    private String removeOrderPri(TOrderRelationship orderRelationship , TUser nowUser , TUser toUser , long nowTiem , boolean isPublish){
        String msg = null;
        if (orderRelationship.getStatus() == OrderRelationshipEnum.STATUS_ENROLL_CANCEL.getType()){
            //如果是报名者取消
            if (isPublish){
               //如果操作用户是发布者
                msg = "对不起，用户"+toUser.getName()+"已将该订单取消";
            } else {
                msg = "对不起，您已将"+toUser.getName()+"的订单取消";
            }
            return msg;
        } else if (orderRelationship.getStatus() == OrderRelationshipEnum.STATUS_PUBLISH_CANCEL.getType()){
            //如果是发布者取消
            if (isPublish){
                //如果操作用户是发布者
                msg = "对不起，您已将"+toUser.getName()+"的订单取消";
            } else {
                msg = "对不起，用户"+toUser.getName()+"已将该订单取消";
            }
            return msg;
        } else if (orderRelationship.getStatus() != OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType()){
            msg = "对不起，用户"+toUser.getName()+"的订单已完成，无法取消";
            return  msg;
        }
        if (isPublish){
            //如果操作用户是发布者，订单改为发布者取消
            orderRelationship.setStatus(OrderRelationshipEnum.STATUS_PUBLISH_CANCEL.getType());
        } else {
            //如果是报名者，订单改为报名者取消
            orderRelationship.setStatus(OrderRelationshipEnum.STATUS_ENROLL_CANCEL.getType());
        }
        orderRelationship.setSignType(OrderRelationshipEnum.SIGN_TYPE_NO.getType());
        orderRelationship.setUpdateUserName(nowUser.getName());
        orderRelationship.setUpdateUser(nowUser.getId());
        orderRelationship.setUpdateTime(nowTiem);
        orderRelationshipDao.updateByPrimaryKey(orderRelationship);
        return msg;
    }

    /**
     * 插入赔付流水，并且增加事件
     * @param nowUser
     * @param toUser
     * @param payment
     * @param orderId
     * @param nowTime
     */
    private void removeOrderPunishment(TUser nowUser , TUser toUser , Long payment , Long orderId , long nowTime){
        //插入支付流水
        TUserTimeRecord userTimeRecord = new TUserTimeRecord();
        userTimeRecord.setUserId(toUser.getId());
        userTimeRecord.setFromUserId(nowUser.getId());
        userTimeRecord.setType(PaymentEnum.PAYMENT_TYPE_REMOVE_ORDER_INDEMNITY_OUT.getCode());
        userTimeRecord.setTargetId(orderId);
        userTimeRecord.setTime(payment);
        userTimeRecord.setCreateUser(nowUser.getId());
        userTimeRecord.setCreateUserName(nowUser.getName());
        userTimeRecord.setUpdateTime(nowTime);
        userTimeRecord.setUpdateUserName(nowUser.getName());
        userTimeRecord.setUpdateTime(nowTime);
        userTimeRecord.setIsValid(AppConstant.IS_VALID_YES);
        //插入流水表

        userCommonController.insertUserTimeRecords(userTimeRecord);
        long userTimeRecordId = userTimeRecord.getId();//插入方法返回流水表主键
        //插入触发事件数据
        TEvent event = new TEvent();
        event.setUserId(toUser.getId());
        event.setTemplateId(EventEnum.TEMPLATE_ID_REMOVE_ORDER.getType());
        event.setTiggerId("orderId"+orderId);
        event.setParameter(""+userTimeRecordId);
        event.setPriority(2);
        event.setText("用户"+nowUser.getName()+"已取消订单，并向你支付致歉礼：互助时"+timeChange(payment));
        event.setCreateTime(nowTime);
        event.setCreateUser(nowUser.getId());
        event.setCreateUserName(nowUser.getName());
        event.setIsValid(AppConstant.IS_VALID_YES);

        messageCommonController.insertTevent(event);
    }
    /**
     * 判断限制时间
     * @param order
     * @param nowTime
     * @return
     */
    private long removeOrderLimitTime(TOrder order , long nowTime){
        if (order.getStartTime() - order.getCreateTime().longValue() < 60 * 60 * 1000){
            //如果开始时间距离发布时间不足一小时,返回限制时间为一小时的一半
            return (order.getStartTime() - order.getCreateTime().longValue())/2;
        }
        return 60 * 60 * 1000;
    }

    /**
     * 判断扣除时间数
     * @param order
     * @param nowTime
     * @return
     */
    private long removeOrderTimeCoin(TOrder order , long nowTime){
        long limitTime = removeOrderLimitTime(order , nowTime);
        int magnification = 100;
        if (order.getStartTime() - nowTime < limitTime){
            //如果开始时间距离现在的时间小于限制时间,返回扣除时间数为
            return order.getCollectTime() * magnification / 100;
        }
        return 0l;
    }
    /**
     * 修改订单状态表
     *
     * @param orderRelationship
     * @param nowUser
     * @param toUser
     * @param nowTime
     * @param isOwn
     * @return
     */
    private String remark(TOrderRelationship orderRelationship, TUser nowUser, TUser toUser, Long nowTime, boolean isOwn) {
        String msg = null;
        if (orderRelationship.getStatus() == OrderRelationshipEnum.STATUS_WAIT_REMARK.getType()) {
            //如果是双方未评价
            if (isOwn) {
                //如果是改的是自己的订单关系
                orderRelationship.setStatus(OrderRelationshipEnum.STATUS_IS_REMARK.getType());
            } else {
                orderRelationship.setStatus(OrderRelationshipEnum.STATUS_BE_REMARK.getType());
            }
        } else if (orderRelationship.getStatus() == OrderRelationshipEnum.STATUS_IS_REMARK.getType()) {
            //如果是已评价
            if (isOwn) {
                //如果是改的是自己的订单关系
                msg = "对不起，您已对" + toUser.getName() + "发表过评价";
            } else {
                //如果不是自己的，那么就完成了全部的评价
                orderRelationship.setStatus(OrderRelationshipEnum.STATUS_IS_COMPLETED.getType());
            }
        } else if (orderRelationship.getStatus() == OrderRelationshipEnum.STATUS_BE_REMARK.getType()) {
            //如果是被评价
            if (isOwn) {
                //如果是改的是自己的订单关系 ，那么就完成了全部的评价
                orderRelationship.setStatus(OrderRelationshipEnum.STATUS_IS_COMPLETED.getType());
            } else {
                //如果不是自己的，那么就完成了全部的评价
                msg = "对不起，您已发表过评价";
            }
        } else {
            msg = "对不起，您已发表过评价";
        }

        if (msg == null) {
            //更新订单关系表
            orderRelationship.setUpdateTime(nowTime);
            orderRelationship.setUpdateUser(nowUser.getId());
            orderRelationship.setUpdateUserName(nowUser.getName());
            orderRelationshipDao.updateByPrimaryKey(orderRelationship);
        }
        return msg;
    }

    /**
     * 插入评价 并且修改被评价人的用户表评分数据
     * @param nowUser
     * @param toUser
     * @param order
     * @param credit
     * @param major
     * @param attitude
     * @param message
     * @param labels
     * @param nowTime
     */
    private void insertRemarkAndUpdateUser(TUser nowUser, TUser toUser, TOrder order, int credit, int major, int attitude, String message, String labels, Long nowTime) {
        TEvaluate evaluate = new TEvaluate();
        evaluate.setEvaluateUserId(nowUser.getId());
        evaluate.setUserId(toUser.getId());
        evaluate.setOrderId(order.getId());
        evaluate.setCreditEvaluate(credit);
        evaluate.setMajorEvaluate(major);
        evaluate.setAttitudeEvaluate(attitude);
        evaluate.setMessage(message);
        evaluate.setLabels(labels);
        evaluate.setCreateTime(nowTime);
        evaluate.setCreateUser(nowUser.getId());
        evaluate.setCreateUserName(nowUser.getName());
        evaluate.setUpdateTime(nowTime);
        evaluate.setUpdateUser(nowUser.getId());
        evaluate.setUpdateUserName(nowUser.getName());
        evaluate.setIsValid(AppConstant.IS_VALID_YES);

        evaluateDao.save(evaluate);

        //更新用户表评分数据
        if ((order.getType() == ProductEnum.TYPE_SEEK_HELP.getValue()
                && order.getCreateUser() == toUser.getId().longValue())
                || (order.getType() == ProductEnum.TYPE_SERVICE.getValue()
                && order.getCreateUser() != toUser.getId().longValue())) {
            //如果是被评价用户求助的发布者, 或者服务的报名者，更行用户表求助的评分数据
            toUser.setSeekHelpNum(toUser.getSeekHelpNum()+1);
            toUser.setHelpAttitudeEvaluate(toUser.getHelpAttitudeEvaluate() + attitude);
            toUser.setHelpCreditEvaluate(toUser.getHelpCreditEvaluate() + credit);
            toUser.setHelpMajorEvaluate(toUser.getMasterStatus() + major);
            toUser.setHelpTotalEvaluate(toUser.getHelpTotalEvaluate() + attitude + major + credit);
            toUser.setUpdateTime(nowTime);
            toUser.setUpdateUserName(toUser.getName());
            toUser.setUpdateUser(toUser.getId());

            userCommonController.updateByPrimaryKey(toUser);
        } else {
            //更改用户表服务的评分数据
            toUser.setServeNum(toUser.getServeNum()+1);
            toUser.setServAttitudeEvaluate(toUser.getServAttitudeEvaluate() + attitude);
            toUser.setServCreditEvaluate(toUser.getServCreditEvaluate() + credit);
            toUser.setServMajorEvaluate(toUser.getServMajorEvaluate() + major);
            toUser.setServTotalEvaluate(toUser.getServTotalEvaluate() + attitude + major + credit);
            toUser.setUpdateTime(nowTime);
            toUser.setUpdateUserName(toUser.getName());
            toUser.setUpdateUser(toUser.getId());

            userCommonController.updateByPrimaryKey(toUser);

            //给被评价用户服务表里的数据进行更新
            List<TOrder> changeOrder = orderDao.selectUserServ(toUser.getId());
            List<Long> changeOrderId = new ArrayList<>();
            if (changeOrder != null && changeOrder.size() > 0) {
                double average = (double)(attitude + major + credit)/toUser.getServeNum();
                average = average * 10000;
                int round = (int) Math.round(average);
                for (int i = 0; i < changeOrder.size(); i++) {
                    changeOrder.get(i).setTotalEvaluate(round);
                    changeOrderId.add(changeOrder.get(i).getId());
                }
                orderDao.updateOrderByList(changeOrder , changeOrderId);
            }

            //给被评价用户批量更新商品
            List<TService> changeServiceList = productCommonController.selectServProByUser(toUser.getId());
            List<Long> changeServiceIdList = new ArrayList<>();
            if (changeServiceList != null && changeServiceList.size() > 0){
                double average = (double)(attitude + major + credit)/toUser.getServeNum();
                average = average * 10000;
                int round = (int) Math.round(average);
                for (int i = 0 ; i < changeServiceList.size() ; i++){
                    changeServiceList.get(i).setTotalEvaluate(round);
                    changeServiceIdList.add(changeServiceList.get(i).getId());
                }
                productCommonController.updateServiceByList(changeServiceList , changeServiceIdList);
            }
        }
    }

    /**
     * 投诉
     *
     * @param orderRelationship
     * @param labelsId
     * @param message
     * @param voucherUrl
     * @param nowUser
     * @param toUser
     * @param nowTime
     * @param isOwn
     * @return
     */
    private String report(TOrderRelationship orderRelationship, long labelsId, String message, String voucherUrl, TUser nowUser, TUser toUser, Long nowTime, boolean isOwn) {
        String msg = null;
        if (orderRelationship.getStatus() != OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType()) {
            msg = "对不起，" + orderRelationship.getServiceName() + "的订单已无法进行投诉";
        }
        if (orderRelationship.getOrderReportType() == OrderRelationshipEnum.ORDER_REPORT_IS_NO.getType()) {
            if (isOwn) {
                //如果订单关系是自己的
                orderRelationship.setOrderReportType(OrderRelationshipEnum.ORDER_REPORT_IS_TURE.getType());
            } else {
                orderRelationship.setOrderReportType(OrderRelationshipEnum.ORDER_REPORT_IS_BEREPORT.getType());
            }
        } else if (orderRelationship.getOrderReportType() == OrderRelationshipEnum.ORDER_REPORT_IS_TURE.getType()) {
            if (isOwn) {
                //如果订单关系是自己的
                msg = "您已对" + toUser.getName() + "发起过投诉，请勿多次投诉";
            } else {
                orderRelationship.setOrderReportType(OrderRelationshipEnum.ORDER_REPORT_EACH_OTHER.getType());
            }
            orderRelationship.setOrderReportType(OrderRelationshipEnum.ORDER_REPORT_EACH_OTHER.getType());
        } else if (orderRelationship.getOrderReportType() == OrderRelationshipEnum.ORDER_REPORT_IS_BEREPORT.getType()) {
            if (isOwn) {
                //如果订单关系是自己的
                orderRelationship.setOrderReportType(OrderRelationshipEnum.ORDER_REPORT_EACH_OTHER.getType());
            } else {
                msg = "您已对" + toUser.getName() + "发起过投诉，请勿多次投诉";
            }
        } else if (orderRelationship.getOrderReportType() == OrderRelationshipEnum.ORDER_REPORT_EACH_OTHER.getType()) {
            msg = "您已对" + toUser.getName() + "发起过投诉，请勿多次投诉";
        } else {
            msg = "您已无法发起对" + orderRelationship.getServiceName() + "订单发起投诉";
        }
        if (msg != null) {
            return msg;
        }
        orderRelationship.setUpdateUserName(nowUser.getName());
        orderRelationship.setUpdateUser(nowUser.getId());
        orderRelationship.setUpdateTime(nowTime);
        orderRelationshipDao.updateByPrimaryKey(orderRelationship);

        TReport report = new TReport();
        report.setReportUserId(toUser.getId());
        report.setType(ReportEnum.TYPE_ORDER.getType());
        report.setAssociationId(orderRelationship.getOrderId());
        report.setLabelsId(labelsId);
        report.setMessage(message);
        report.setVoucherUrl(voucherUrl);
        report.setStatus(ReportEnum.STATUS_PENDING_DISPOSAL.getType());
        report.setCreateTime(nowTime);
        report.setCreateUser(nowUser.getId());
        report.setCreateUserName(nowUser.getName());
        report.setUpdateTime(nowTime);
        report.setUpdateUser(nowUser.getId());
        report.setUpdateUserName(nowUser.getName());
        report.setIsValid(AppConstant.IS_VALID_YES);
        reportDao.saveOneOrder(report);

        //服务通知
        TFormid formid = findFormId(nowTime, toUser);
        if (formid != null) {
            try {
                List<String> msgList = new ArrayList<>();
                String parameter = "?orderId="+orderRelationship.getOrderId()+"&returnHome=true";
                msgList.add(messageCommonController.getValue(labelsId, "complaint"));
                msgList.add(nowUser.getName());
                msgList.add(changeTime(nowTime));
                msgList.add("如果您对该投诉有异议，请于2个工作日内联系平台在线客服。");
                messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , msgList , SetTemplateIdEnum.help_setTemplate_20 , parameter);
                formid.setIsValid("0");
                messageCommonController.updateFormId(formid);
            } catch (Exception e) {
                logger.error("发送服务通知失败");
            }
        }

        //系统通知
        String title = "收到投诉提醒";
        String content = new StringBuilder().append("在“").append(orderRelationship.getServiceName())
                .append("”的互助事项中，").append(nowUser.getName()).append("已对您发起投诉，投诉理由：")
                .append(messageCommonController.getValue(labelsId, "complaint"))
                .append("。如果您对投诉有异议，请于2个工作日内联系平台在线客服。").toString();

        String recordContent = new StringBuilder().append(nowUser.getName()).append(" ")
                .append("投诉了").append(toUser.getName())
                .append("，客服将在12小时内进行处理，如有疑问请联系客服").toString();

        recoreSave(orderRelationship.getOrderId(), recordContent, nowUser, nowTime);

        messageCommonController.messageSave(orderRelationship.getId(), nowUser, title, content, toUser.getId(), nowTime);
        return msg;
    }

    /**
     * 支付私有方法
     *
     * @param orderRelationship
     * @param payment
     * @param nowUser
     * @param nowTime
     * @param toUser
     * @return
     */
    private String payOrderPri(TOrderRelationship orderRelationship, Long payment, TUser nowUser, long nowTime, TUser toUser , TOrder order) {
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
        if (payment > 0) {
            //如果是有时间支付完成的
            orderRelationship.setStatus(OrderRelationshipEnum.STATUS_WAIT_REMARK.getType());
            if (orderRelationship.getCollectType() == OrderEnum.COLLECT_TYPE_TIME.getValue()) {
                //如果收取的是互助时，完成互助时相关事情
                //插入成长值
                userCommonController.taskComplete(toUser , GrowthValueEnum.GROWTH_TYPE_REP_SERV_DONE , 1);
                logger.error(" //TODO 插入成长值（查看今天被支付多少次，然后再看加不加）");
                //更新被支付用户的时间，并且将其完成求助数量+1
                toUser.setSurplusTime(toUser.getSurplusTime() + payment);
                toUser.setPayNum(toUser.getPayNum() + 1);
            } else {
                //如果收取的是公益时，完成公益时相关事情
                //完成公益时 增加成长值
                userCommonController.taskComplete(toUser , GrowthValueEnum.GROWTH_TYPE_REP_PUBLIC_WELFARE_ACTY_DONE , 1);
                //更新被支付用户的公益时间
                toUser.setPublicWelfareTime(toUser.getSurplusTime() + payment);
                //TODO 发送公益时系统通知
            }
            toUser.setUpdateUserName(nowUser.getName());
            toUser.setUpdateUser(nowUser.getId());
            toUser.setUpdateTime(nowTime);
            toUser.setServeNum(toUser.getServeNum() + 1);
            userCommonController.updateByPrimaryKey(toUser);

            //插入支付流水
            TUserTimeRecord userTimeRecord = new TUserTimeRecord();
            userTimeRecord.setUserId(toUser.getId());
            userTimeRecord.setFromUserId(nowUser.getId());
            userTimeRecord.setType(1);
            userTimeRecord.setTargetId(orderRelationship.getOrderId());
            userTimeRecord.setTime(payment);
            userTimeRecord.setCreateTime(nowTime);
            userTimeRecord.setCreateUser(nowUser.getId());
            userTimeRecord.setCreateUserName(nowUser.getName());
            userTimeRecord.setUpdateTime(nowTime);
            userTimeRecord.setUpdateUserName(nowUser.getName());
            userTimeRecord.setUpdateTime(nowTime);
            userTimeRecord.setIsValid("1");
            userCommonController.insertUserTimeRecords(userTimeRecord);
        } else {
            orderRelationship.setStatus(OrderRelationshipEnum.STATUS_NOT_ESTABLISHED.getType());
        }
        orderRelationship.setUpdateTime(nowTime);
        orderRelationship.setUpdateUser(nowUser.getId());
        orderRelationship.setUpdateUserName(nowUser.getName());
        orderRelationshipDao.updateByPrimaryKey(orderRelationship);


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
     * @param changeUser
     */
    private void unFreezeTime(long unfreezeTime, long nowTime, TUser changeUser, Long orderId) {
        TUserFreeze userFreeze = null;
        userFreeze = userCommonController.selectUserFreezeByUserIdAndOrderId(changeUser.getId(), orderId);
        if (userFreeze == null) {
            //如果没有冻结信息，说明订单有问题，取消失败
            throw new MessageException("499", "冻结信息表更新失败，请后退刷新重试");
        }
        userFreeze.setFreezeTime(userFreeze.getFreezeTime() - unfreezeTime);
        userFreeze.setUpdateTime(nowTime);
        userFreeze.setUpdateUser(changeUser.getId());
        userFreeze.setUpdateUserName(changeUser.getName());
        if (userFreeze.getFreezeTime() == 0) {
            userFreeze.setIsValid("0");
        }
        userCommonController.updateUserFreeze(userFreeze);
    }

    /**
     * 因为自己的关系无法报名
     * 作者:姜修弘
     * 创建时间:2019年2月25日 下午2:38:44
     */
    private String canEnroll(TUser nowUser,TOrder order) {

        String msg = null;

        TOrderRelationship orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(order.getId() , nowUser.getId());
        if (orderRelationship != null) {
            //如果有参加的订单关系，则不允许报名
            if (orderRelationship.getStatus() == OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType()
                    ||orderRelationship.getStatus() == OrderRelationshipEnum.STATUS_WAIT_REMARK.getType()
                    ||orderRelationship.getStatus() == OrderRelationshipEnum.STATUS_IS_COMPLETED.getType()
                    ||orderRelationship.getStatus() == OrderRelationshipEnum.STATUS_IS_REMARK.getType()
                    ||orderRelationship.getStatus() == OrderRelationshipEnum.STATUS_BE_REMARK.getType()
                    ||orderRelationship.getStatus() == OrderRelationshipEnum.STATUS_NOT_ESTABLISHED.getType()){
                msg = "对不起，您已报名，请勿多次报名";
                return msg;
            }
        }
        if (order.getCreateUser() == nowUser.getId().longValue()) {
            // 如果用户报名的是自己的需求
            msg = "对不起，您不能报名自己的求助";
            return msg;
        }
        if (order.getSource() == 2) {
            if (order.getOpenAuth() == 2) {
                // 如果是仅组织内成员可见
                boolean isCompany = false;
                String[] companIds = nowUser.getCompanyIds().split(",");
                for (int i =0 ;i < companIds.length ; i++){
                    if (order.getCompanyId().equals(companIds[i])) {
                        // 属于组织成员
                        isCompany = true;
                        break;
                    }
                }
                if (isCompany) {
                    // 不属于组织成员
                    msg = "对不起，该互助仅允许组织内成员报名，您非组织内成员，无法报名";
                    return msg;
                }
            }
        }
        return msg;

    }

    /**
     * 因为订单的关系无法报名
     * @param order
     * @param nowTime
     * @return
     */
    private String enrollCantByOrder(TOrder order , Long nowTime){
        String msg = null;
        if (order.getEndTime().longValue() < nowTime) {
            msg = "对不起，该项目已经结束，无法报名";
            return msg;
        }
        if (order.getServiceStatus() != ProductEnum.STATUS_UPPER_FRAME.getValue()) {
            throw new MessageException("401", "对不起，该订单已无法报名，请后退刷新重试");
        }
        if (order.getConfirmNum() == order.getServicePersonnel().longValue()) {
            throw new MessageException("401", "对不起，该订单已选满需要人数");
        }
        return msg;
    }

    /**
     * 求助报名
     *
     * @param order
     * @param nowUser
     * @param nowTime
     * @return
     */
    private long helpEnroll(TOrder order, TUser nowUser, long nowTime) {
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
            orderRelationship = new TOrderRelationship();
            //没有可以复用的订单关系，那么就新建订单关系
            orderRelationship.setServiceId(order.getServiceId());
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
    public String test(Long orderId, List<Long> userIdList) {
        List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectByOrderIdAndEnrollUserIdList(orderId, userIdList);
        List<Long> orderRelatingshipIdList = new ArrayList<>();
        for (int i = 0; i < orderRelationshipList.size(); i++) {
            if (orderRelationshipList.get(i).getReceiptUserId() == 68813259653775360l) {
                orderRelationshipList.get(i).setCreateUserName("测试");
            } else {
                orderRelationshipList.get(i).setCreateUserName("测试2");
            }
            orderRelatingshipIdList.add(orderRelationshipList.get(i).getId());
        }

        long count = orderRelationshipDao.updateOrderRelationshipByList(orderRelationshipList, orderRelatingshipIdList);
        return count + "";
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
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_IS_REMARK.getType());
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_BE_REMARK.getType());
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_NOT_ESTABLISHED.getType());
        return orderRelationshipStatusList;
    }

    /**
     * 插入服务记录
     *
     * @param orderId
     * @param content
     * @param nowUser
     * @param nowTime
     */
    private void recoreSave(Long orderId, String content, TUser nowUser, Long nowTime) {
        TOrderRecord orderRecord = new TOrderRecord();
        orderRecord.setOrderId(orderId);
        orderRecord.setContent(content);
        orderRecord.setCreatTime(nowTime);
        orderRecord.setCreatUserId(nowUser.getId());
        orderRecord.setCreatUserName(nowUser.getCreateUserName());
        orderRecord.setUpdateTime(nowTime);
        orderRecord.setUpdateUserId(nowUser.getId());
        orderRecord.setUpdateUserName(nowUser.getName());
        orderRecord.setIsValid(AppConstant.IS_VALID_YES);
        orderRecordDao.insert(orderRecord);
    }

    /**
     * 修改发布者状态
     * @param order
     * @param publishOrderRelaId
     */
    private void changePublishOrderRela(TOrder order , Long publishOrderRelaId){
        TOrderRelationship orderRelationshipForPublish = orderRelationshipDao.selectByOrderIdAndUserId(order.getId() , publishOrderRelaId);
        List<TOrderRelationship> orderRelationshipEnrollList = orderRelationshipDao.selectOrderRelaByStatusByEnrollNoReport(order.getId());
        int typeToPay = 0;
        int typeToRemark = 0;
        int typeNoPay = 0;
        int typeCompleted = 0;
        for (int i = 0 ; i < orderRelationshipEnrollList.size() ; i++ ){
            if (orderRelationshipEnrollList.get(i).getStatus() == OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType()){
                //如果有人还未支付，那么发布者状态就要变成待支付
                typeToPay++;
                break;
            }
            if (orderRelationshipEnrollList.get(i).getStatus() == OrderRelationshipEnum.STATUS_WAIT_REMARK.getType()){
                typeToRemark++;
            } else if (orderRelationshipEnrollList.get(i).getStatus() == OrderRelationshipEnum.STATUS_IS_REMARK.getType()){
                typeToRemark++;
            } else if (orderRelationshipEnrollList.get(i).getStatus() == OrderRelationshipEnum.STATUS_BE_REMARK.getType()){
                typeToRemark++;
            }
            if (orderRelationshipEnrollList.get(i).getStatus() == OrderRelationshipEnum.STATUS_IS_COMPLETED.getType()){
                typeCompleted++;
            }
            if (orderRelationshipEnrollList.get(i).getStatus() == OrderRelationshipEnum.STATUS_NOT_ESTABLISHED.getType()){
                typeNoPay++;
            }
        }
        if (typeToPay > 0){
            //如果有未支付的
            orderRelationshipForPublish.setStatus(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
        } else if (typeToRemark > 0 ){
            //如果有待评价的
            orderRelationshipForPublish.setStatus(OrderRelationshipEnum.STATUS_WAIT_REMARK.getType());
        } else if (typeCompleted > 0){
            //如果有已完成的
            orderRelationshipForPublish.setStatus(OrderRelationshipEnum.STATUS_IS_COMPLETED.getType());
        } else if (typeNoPay > 0){
            //如果有交易不成立的
            orderRelationshipForPublish.setStatus(OrderRelationshipEnum.STATUS_NOT_ESTABLISHED.getType());
        } else {
            //如果以上都没有，设为初始状态
            orderRelationshipForPublish.setStatus(OrderRelationshipEnum.STATUS_NO_STATE.getType());
        }
        //更新发布者状态
        orderRelationshipDao.updateByPrimaryKey(orderRelationshipForPublish);
    }

    private void enrollPri(TOrder order , long nowTime , TUser nowUser){
        String errorMsg = canEnroll(nowUser , order);
        if (errorMsg != null) {
            //如果错误消息不为空，说明该用户有部分问题不允许报名，抛出错误信息
            throw new MessageException("499", errorMsg);
        }
        helpEnroll(order, nowUser, nowTime);
        if (order.getType() == OrderRelationshipEnum.SERVICE_TYPE_SERV.getType()) {
            //如果是服务

            long canUseTime = nowUser.getSurplusTime() + nowUser.getCreditLimit() - nowUser.getFreezeTime();
            if (canUseTime < order.getCollectTime()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new MessageException("499", "对不起，余额不足 不可以报名");
            }
            userCommonController.freezeTimeCoin(nowUser.getId(), order.getCollectTime(), order.getId(), order.getServiceName());

        } else {
            //如果是求助
            if (nowUser.getAuthenticationStatus() != 2){
                throw new MessageException("9527", "对不起，您未实名，无法报名求助");
            }
        }

        order.setEnrollNum(order.getEnrollNum() + 1);
        orderDao.updateByPrimaryKey(order);


    }

    /**
     * 移除可报名日期
     * @param date
     * @param serviceId
     */
    public void removeCanEnrollDate(String date , Long serviceId){
        TService service = productCommonController.getProductById(serviceId);
        if (service.getTimeType() == ProductEnum.TIME_TYPE_REPEAT.getValue()){
            String enrollDate = service.getEnrollDate();
            List<String> canEnrollDateList = new ArrayList<>();
            String[] enrollDates = enrollDate.split(",");
            for (int i = 0; i < enrollDates.length; i++) {
                if (! enrollDates[i].equals(date)){
                    canEnrollDateList.add(enrollDates[i]);
                }
            }
            enrollDate = Joiner.on(",").join(canEnrollDateList);
            service.setEnrollDate(enrollDate);
            productCommonController.update(service);
        }
    }

    /**
     * 组织版的订单详情头部信息
     *
     * @param orderId
     * @param nowUserId
     * @return
     */
    public OrgEnrollUserView orgOrderInfo(Long orderId , Long nowUserId){
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        TOrderRelationship orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId , nowUserId);
        OrgEnrollUserView orgEnrollUserView = new OrgEnrollUserView();
        orgEnrollUserView.setOrderId(orderId);
        orgEnrollUserView.setTitle(order.getServiceName());
        orgEnrollUserView.setStatus(orderRelationship.getStatus());
        orgEnrollUserView.setStartTime(changeTime(order.getStartTime()));
        orgEnrollUserView.setEndTime(changeTime(order.getEndTime()));
        orgEnrollUserView.setIsRepeat(false);
        if (order.getTimeType() == ProductEnum.TIME_TYPE_REPEAT.getValue()){
            orgEnrollUserView.setIsRepeat(true);
        }
        orgEnrollUserView.setEnrollSum(order.getEnrollNum());

        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderRelationshipEnum.STATUS_WAIT_REMARK.getType());
        statusList.add(OrderRelationshipEnum.STATUS_IS_COMPLETED.getType());
        statusList.add(OrderRelationshipEnum.STATUS_IS_REMARK.getType());
        statusList.add(OrderRelationshipEnum.STATUS_BE_REMARK.getType());
        statusList.add(OrderRelationshipEnum.STATUS_NOT_ESTABLISHED.getType());
        List<TOrderRelationship> orderChooseRelationshipList = orderRelationshipDao.selectListByStatusListByEnroll(orderId , statusList);

        orgEnrollUserView.setChooseUserSum(orderChooseRelationshipList.size());

        orgEnrollUserView.setCanChooseSum(
                orderRelationshipDao.selectListByStatusForNotSignByEnroll(orderId ,
                        OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType()).size());

        return orgEnrollUserView;

    }

    /**
     * 组织报名选人列表
     *
     * @param orderId
     * @param status
     * @param type
     * @param value
     * @param pageSize
     * @param pageNum
     * @param nowUser
     * @return
     */
    public QueryResult<EnrollUserInfoView> enrollUserInfoList(Long orderId, int status, int type, String value,
                                                       int pageSize, int pageNum, TUser nowUser){

        QueryResult<EnrollUserInfoView> result = new QueryResult<>();
        List<EnrollUserInfoView> enrollUserInfoViewList = new ArrayList<>();
        List<Long> userIdFindList = new ArrayList<>();

        if (type == 1) {
            // 1是输入姓名
            List<TUser> userFindList = userCommonController.selectUserByName(value);
            for (int i = 0; i < userFindList.size(); i++) {
                userIdFindList.add(userFindList.get(i).getId());
            }
            if (userIdFindList.size() <= 0) {
                // 没数据
                result.setResultList(enrollUserInfoViewList);
                result.setTotalCount(0l);
                return result;
            }
        } else if (type == 2) {
            // 2 是输入手机号
            List<TUser> userFindList = userCommonController.selectUserByTelephone(value);
            for (int i = 0; i < userFindList.size(); i++) {
                userIdFindList.add(userFindList.get(i).getId());
            }
            if (userIdFindList.size() <= 0) {
                // 没数据
                result.setResultList(enrollUserInfoViewList);
                result.setTotalCount(0l);
                return result;
            }
        } else if (type == 3) {
            // 如果是组内姓名
            List<TUserCompany> userCompanies = userCommonController.selectUserCompanyByName(value);

            for (int i = 0; i < userCompanies.size(); i++) {
                userIdFindList.add(userCompanies.get(i).getUserId());
            }
            if (userIdFindList.size() <= 0) {
                // 没数据
                result.setResultList(enrollUserInfoViewList);
                result.setTotalCount(0l);
                return result;
            }
        }

        List<TOrderRelationship> orderRelationshipList = null;
        //分页插件
        Page<TService> page = PageHelper.startPage(pageNum, pageSize);
        if (status == 1) {
            // 1是可选择
            List<Integer> statusList = new ArrayList<>();
            statusList.add(OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType());
            if (userIdFindList.size() > 0){
                orderRelationshipList = orderRelationshipDao.selectListByStatusListByEnrollInUserList(orderId , statusList , userIdFindList);
            } else {
                orderRelationshipList = orderRelationshipDao.selectListByStatusListByEnroll(orderId , statusList);
            }

        } else if (status == 2) {
            // 2是被选择
            List<Integer> statusList = new ArrayList<>();
            statusList.add(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
            statusList.add(OrderRelationshipEnum.STATUS_WAIT_REMARK.getType());
            statusList.add(OrderRelationshipEnum.STATUS_IS_COMPLETED.getType());
            statusList.add(OrderRelationshipEnum.STATUS_IS_REMARK.getType());
            statusList.add(OrderRelationshipEnum.STATUS_BE_REMARK.getType());
            statusList.add(OrderRelationshipEnum.STATUS_NOT_ESTABLISHED.getType());
            if (userIdFindList.size() > 0){
                orderRelationshipList = orderRelationshipDao.selectListByStatusListByEnrollInUserList(orderId , statusList , userIdFindList);
            } else {
                orderRelationshipList = orderRelationshipDao.selectListByStatusListByEnroll(orderId , statusList);
            }

        } else if (status == 3) {
            // 3是被拒绝
            List<Integer> statusList = new ArrayList<>();
            statusList.add(OrderRelationshipEnum.STATUS_NOT_CHOOSE.getType());
            if (userIdFindList.size() > 0){
                orderRelationshipList = orderRelationshipDao.selectListByStatusListByEnrollInUserList(orderId , statusList , userIdFindList);
            } else {
                orderRelationshipList = orderRelationshipDao.selectListByStatusListByEnroll(orderId , statusList);
            }

        } else if (status == 4) {
            // 3是取消
            List<Integer> statusList = new ArrayList<>();
            statusList.add(OrderRelationshipEnum.STATUS_ENROLL_CANCEL.getType());
            statusList.add(OrderRelationshipEnum.STATUS_PUBLISH_CANCEL.getType());
            if (userIdFindList.size() > 0){
                orderRelationshipList = orderRelationshipDao.selectListByStatusListByEnrollInUserList(orderId , statusList , userIdFindList);
            } else {
                orderRelationshipList = orderRelationshipDao.selectListByStatusListByEnroll(orderId , statusList);
            }

        }

        if (orderRelationshipList == null || orderRelationshipList.size() <= 0) {
            // 没数据
            result.setResultList(enrollUserInfoViewList);
            result.setTotalCount(0l);
            return result;
        }

        List<Long> userIds = new ArrayList<>();

        for (int i = 0; i < orderRelationshipList.size(); i++) {
            userIds.add(orderRelationshipList.get(i).getUpdateUser());
        }

        List<TUser> userList = userCommonController.selectUserByIds(userIds);
        if (userList == null || userList.size() <= 0) {
            // 没数据
            result.setResultList(enrollUserInfoViewList);
            result.setTotalCount(0l);
            return result;
        }

        Long companyId = userCommonController.getOwnCompanyId(nowUser.getId());
        List<TUserCompany> userCompanieList = userCommonController.selectUserCompanyByIdAndUserIdList(userIdFindList , companyId);

        for (int i = 0; i < orderRelationshipList.size(); i++) {
            TUser thisUser = null;
            for (int j = 0; j < userList.size(); j++) {
                if (orderRelationshipList.get(i).getReceiptUserId() == userList.get(j).getId().longValue()) {
                    thisUser = userList.get(j);
                    break;
                }
            }
            if (thisUser != null) {
                TUserCompany userCompany = null;
                for (int j = 0; j < userCompanieList.size(); j++) {
                    if (orderRelationshipList.get(i).getReceiptUserId() == userCompanieList.get(j).getUserId().longValue()) {
                        userCompany = userCompanieList.get(j);
                        break;
                    }
                }
                EnrollUserInfoView enrollUserInfoView = new EnrollUserInfoView();

                if (userCompany != null) {
                    enrollUserInfoView.setIsGroup(true);
                    enrollUserInfoView.setUserNameForTeam(userCompany.getTeamName());
                } else {
                    enrollUserInfoView.setIsGroup(false);
                }

                enrollUserInfoView.setUserIdToString(thisUser.getId());
                enrollUserInfoView.setUserName(thisUser.getName());
                enrollUserInfoView.setUserUrl(thisUser.getUserHeadPortraitPath());
                if (thisUser.getAge() == null) {
                    enrollUserInfoView.setAge("未知");
                } else {
                    enrollUserInfoView.setAge(thisUser.getAge() + "");
                }
                if (thisUser.getOccupation() == null) {
                    enrollUserInfoView.setOccupation("无业游民");
                } else {
                    enrollUserInfoView.setOccupation(thisUser.getOccupation());
                }
                if (thisUser.getSkill() == null) {
                    enrollUserInfoView.setSkill("未设定");
                } else {
                    enrollUserInfoView.setSkill(thisUser.getSkill());
                }
                enrollUserInfoView.setServe_num(thisUser.getServeNum());
                enrollUserInfoView.setTotal_eva(thisUser.getServTotalEvaluate());
                enrollUserInfoView.setCreatTime(changeTime(orderRelationshipList.get(i).getCreateTime()));
                enrollUserInfoView.setStatus(orderRelationshipList.get(i).getStatus());
                enrollUserInfoView.setSex(thisUser.getSex());
                enrollUserInfoViewList.add(enrollUserInfoView);
            }

        }
        result.setResultList(enrollUserInfoViewList);
        result.setTotalCount(page.getTotal());



        return result;

    }

    public void timePayOrder(Long orderId){
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        if (order.getType() == ProductEnum.TYPE_SEEK_HELP.getValue()){
            //如果是求助，给一堆人付钱
            List<Long> userIdList = new ArrayList<>();
            List<Long> paymentList = new ArrayList<>();


        }
    }

    /**
     * 两个小时还未选人的通知方法
     * @param orderId
     */
    public void noChooseByTwoHour(Long orderId){
        TOrder order = orderDao.selectByPrimaryKey(orderId);
        long nowTime = System.currentTimeMillis();
        TUser publishUser = userCommonController.getUserById(order.getCreateUser());
        TOrderRelationship orderRelationshipByPublish = orderRelationshipDao.selectByOrderIdAndUserId(orderId , order.getCreateUser());
        if (orderRelationshipByPublish.getStatus() != OrderRelationshipEnum.STATUS_NO_STATE.getType()){
            //如果发布者状态不为为选人，说明选过人了，不用发送消息
            return;
        } else {
            List<TOrderRelationship> orderRelationshipList = orderRelationshipDao.selectListByStatusByEnroll(orderId , OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType());
            if (orderRelationshipList.size() == 0){
                //如果没有待选的人 不用发送消息
                return;
            } else {

                //发送消息
                String title = "请及时选定服务者";
                String msgContent = new StringBuilder().append("距离您的求助“").append(orderRelationshipByPublish.getServiceName())
                        .append("”开始时间只剩下2小时啦！已经有").append(orderRelationshipList.size())
                        .append("位小天使报名，快去选定您需要的人吧~“").toString();

                TFormid formid = findFormId(nowTime, publishUser);
                if (formid != null) {
                    try {
                        List<String> wxMsg = new ArrayList<>();
                        String parameter = "?orderId="+order.getId()+"&returnHome=true";
                        wxMsg.add("您还未选定ta哦");
                        wxMsg.add(orderRelationshipByPublish.getServiceName());
                        TUser toUser = userCommonController.getUserById(orderRelationshipList.get(0).getReceiptUserId());
                        if (orderRelationshipList.size() == 1){
                            wxMsg.add(toUser.getName());
                        } else {
                            wxMsg.add(new StringBuilder().append(toUser.getName()).append("等")
                                    .append(orderRelationshipList.size()).append("人").toString());
                        }
                        wxMsg.add("别拖啦，再不选定，ta就来不及上车啦！");
                        messageCommonController.pushOneUserMsg(publishUser.getVxOpenId() , formid.getFormId() , wxMsg , SetTemplateIdEnum.help_setTemplate_4 , parameter);
                        formid.setIsValid("0");
                        messageCommonController.updateFormId(formid);
                    } catch (Exception e) {
                        logger.error("发送服务通知失败");
                    }
                }
                TUser adminUser = new TUser();
                adminUser.setId(0l);
                adminUser.setName("系统管理员");
                messageCommonController.messageSave(order.getId() , adminUser , title , msgContent , publishUser.getId() , nowTime);

            }
        }
    }

   /* public noUserEnrollByStart(Long orderId){

    }*/
}
