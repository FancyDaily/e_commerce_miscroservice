package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.enums.SetTemplateIdEnum;
import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.SnowflakeIdWorker;
import com.e_commerce.miscroservice.message.controller.MessageCommonController;
import com.e_commerce.miscroservice.order.controller.OrderCommonController;
import com.e_commerce.miscroservice.order.dao.*;
import com.e_commerce.miscroservice.order.service.OrderRelationService;
import com.e_commerce.miscroservice.order.vo.UserInfoView;
import com.e_commerce.miscroservice.product.controller.ProductCommonController;
import com.e_commerce.miscroservice.product.util.DateUtil;
import com.e_commerce.miscroservice.user.controller.UserCommonController;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.ToDoubleBiFunction;


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

    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();

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
    public long enroll(Long orderId, Long userId, String date, Long serviceId) throws ParseException {
        TUser nowUser = userCommonController.getUserById(userId);
        long nowTime = System.currentTimeMillis();
        TOrder order = new TOrder();
        TService service = productCommonController.getProductById(serviceId);
        try {
            order = orderCommonController.produceOrder(service , OrderEnum.PRODUCE_TYPE_ENROLL.getValue() , date);
        } catch (Exception e){
            throw new MessageException("401", "对方余额不足");
        }
        if(order == null){
            throw new MessageException("401", "该日期已超出报名日期");
        }
        String errorMsg = enrollCantByOrder(order , nowTime);
        if (errorMsg != null){
            //如果错误消息不为空，说明该订单有部分问题不允许报名，抛出错误信息，并在外面接到这个异常后将报名日历移除
            throw new MessageException("401", errorMsg);
        }
        errorMsg = canEnroll(nowUser, date, order, orderId, nowTime);
        if (errorMsg != null) {
            //如果错误消息不为空，说明该用户有部分问题不允许报名，抛出错误信息
            throw new MessageException("499", errorMsg);
        }
        helpEnroll(order, nowUser, date, nowTime, serviceId);
        if (order.getType() == OrderRelationshipEnum.SERVICE_TYPE_SERV.getType()) {
            //如果是服务

            long canUseTime = nowUser.getSurplusTime() + nowUser.getCreditLimit() - nowUser.getFreezeTime();
            if (canUseTime < order.getCollectTime()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new MessageException("499", "对不起，余额不足 不可以报名");
            }
            userCommonController.freezeTimeCoin(nowUser.getId(), order.getCollectTime(), orderId, order.getServiceName());
            //修改用户表的冻结字段
            nowUser.setFreezeTime(nowUser.getFreezeTime() + order.getCollectTime());
            nowUser.setUpdateTime(nowTime);
            nowUser.setUpdateUser(nowUser.getId());
            nowUser.setUpdateUserName(nowUser.getName());
            userCommonController.updateByPrimaryKey(nowUser);

        } else {
            //如果是求助
            //TODO 如果未实名
        }

        order.setEnrollNum(order.getEnrollNum() + 1);
        orderDao.updateByPrimaryKey(order);

        //TODO 报名的通知（第一个报名的，或者第一个参与的（就看刘维怎么定，一个人报了取消又报了是不是还通知））

        if (order.getType() == ProductEnum.TYPE_SEEK_HELP.getValue()){//TODO 第一个报名的人
            //如果是求助
            if (orderRelationshipDao.selectJoinUser(orderId) == 0){
                //如果是首次报名
                String title = "您的求助收到报名啦";
                String content = new StringBuilder().append("您的求助“").append(order.getServiceName())
                        .append("”已经有小天使报名啦，快去看看吧！会有更多人报名的，记得及时选定您需要的人哦～").toString();
                messageCommonController.messageSave(orderId , nowUser , title , content , order.getCreateUser() , nowTime);

                TUser toUser = userCommonController.getUserById(order.getCreateUser());
                TFormid formid = findFormId(nowTime, toUser);
                if (formid != null) {
                    try {
                        List<String> msg = new ArrayList<>();
                        String orderType = "2";//服务通知的接收者是报名者
                        String parameter = "?orderId="+orderId+"&returnHome=true&orderType="+orderType;
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
                messageCommonController.messageSave(orderId , nowUser , title , content , order.getCreateUser() , nowTime);

                TUser toUser = userCommonController.getUserById(order.getCreateUser());
                TFormid formid = findFormId(nowTime, toUser);
                if (formid != null) {
                    try {
                        List<String> msg = new ArrayList<>();
                        String orderType = "2";//服务通知的接收者是报名者
                        String parameter = "?orderId="+orderId+"&returnHome=true&orderType="+orderType;
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
                if (orderRelationshipDao.selectJoinUser(orderId) == 0){
                    //如果是首次报名
                    String content = new StringBuilder().append("您发布的“").append(order.getServiceName())
                            .append("”服务，已有小伙伴报名了。记得关注并联系他哦").toString();
                    messageCommonController.messageSave(orderId , nowUser , title , content , order.getCreateUser() , nowTime);

                    TUser toUser = userCommonController.getUserById(order.getCreateUser());
                    TFormid formid = findFormId(nowTime, toUser);
                    if (formid != null) {
                        try {
                            List<String> msg = new ArrayList<>();
                            String orderType = "2";//服务通知的接收者是报名者
                            String parameter = "?orderId="+orderId+"&returnHome=true&orderType="+orderType;
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
        return serviceId;
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
                            .append("服务，小伙伴").append(nowUser.getName())
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
                        String orderType = "2";//服务通知的接收者是报名者
                        String parameter = "?orderId="+orderId+"&returnHome=true&orderType="+orderType;
                        if (order.getType() == ProductEnum.TYPE_SEEK_HELP.getValue()){
                            //如果是求助
                            msg.add("您已被求助者选定");
                            msg.add(order.getServiceName());
                            msg.add(changeTime(order.getStartTime()));
                            msg.add(changeAddress(order.getAddressName()));
                            msg.add("万物遇见了阳光，而我遇见了热心的你，真好");
                            messageCommonController.pushOneUserMsg(toUser.getVxOpenId() , formid.getFormId() , msg , SetTemplateIdEnum.help_setTemplate_7 , parameter);
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
        if ((canChooseUserSum - orderRelationshipIdList.size()) == 0) {
            //如果选满人了，那么判断是否有下周期的订单，如果有不处理，没有就派生订单
            TService service = productCommonController.getProductById(orderRelationshipList.get(0).getServiceId());
            String date = DateUtil.getDate(orderRelationshipList.get(0).getStartTime());
            //将该订单设置为可见，并调用方法修改订单可见状态
            orderCommonController.changeOrderVisiableStatus(orderId, 1);
            //派生下一张订单
            orderCommonController.produceOrder( service ,OrderEnum.PRODUCE_TYPE_ENOUGH.getValue(),date);
            //移除可报名日期
            removeCanEnrollDate(date , service.getId());
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
                //TODO 发送通知
                String title = "";
                String content = "";
                if (type == 0){
                    //如果是手动拒绝
                    TFormid formid = findFormId(nowTime, toUser);
                    if (order.getType() == ProductEnum.TYPE_SEEK_HELP.getValue()){
                        //如果是求助
                        if (formid != null) {
                            try {
                                List<String> msg = new ArrayList<>();
                                String orderType = "2";//服务通知的接收者是报名者
                                String parameter = "?orderId="+orderId+"&returnHome=true&orderType="+orderType;
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
                                List<String> msg = new ArrayList<>();
                                String orderType = "2";//服务通知的接收者是报名者
                                String parameter = "?orderId="+orderId+"&returnHome=true&orderType="+orderType;
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
                        //TODO 文本没有
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
    public List<String> payOrder(Long orderId, List<Long> userIdList, List<Long> paymentList, Long nowUserId) {
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
                    String msg = payOrderPri(orderRelationship, paymentList.get(i), nowUser, nowTime, toUser);
                    if (msg != null) {
                        //支付失败，添加错误信息
                        msgList.add(msg);
                    } else if (paymentList.get(i) > 0) {
                        //如果是有效支付，增加钱数，增加支付成功次数
                        if (order.getCollectType() == OrderEnum.COLLECT_TYPE_TIME.getValue()) {
                            //如果收取的是互助时
                            paymentSum += paymentList.get(i);
                        } else {
                            //如果是公益时，那么公益时数量要加
                            //paymentByWelfareSum += paymentList.get(i);
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
            if (seekHelpDoneNum > 3) {
                content = nowUser.getName() + " 支付了" + payUserName + seekHelpDoneNum + "人" + collectType + timeChange(paymentSum);
            } else {
                content = nowUser.getName() + " 支付了" + payUserName + collectType + timeChange(paymentSum);
            }

        } else {
            //如果是报名者支付，要修改自己的订单状态
            publishUserId = userIdList.get(0);
            TOrderRelationship orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId, nowUserId);
            String msg = payOrderPri(orderRelationship, paymentList.get(0), nowUser, nowTime, toUserList.get(0));
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
                content = nowUser.getName() + " 支付了" + toUserList.get(0).getName() + collectType + timeChange(paymentSum);
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
            //TODO 插入互助时方面的成长值（查看今天支付多少次，然后再看加不加）有最高次数限制
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
                if (nowUser.getPayNum() == 0) {
                    //如果是首次完成互助，增加成长值
                    //TODO 如果是首次完成，那么要增加成长值
                    logger.error("//TODO 如果是首次完成，那么要增加成长值");
                }
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
        changePublishOrderRela(order , order.getCreateUser());
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
            String msg = remark(orderRelationship, nowUser, toUser, nowTime, false);
            if (msg != null) {
                throw new MessageException("499", msg);
            }
        }
        //修改发布者订单关系状态
        changePublishOrderRela(order , order.getCreateUser());
        if (userIdList.size() == errorMsgList.size()){
            throw new MessageException("499", "所选用户中没有可评价用户");
        }
        //TODO 增加成长值，增加数量是userlist数量减去错误数量
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
                        String content = new StringBuilder().append("很遗憾，在“").append(orderRelationship.getServiceName())
                                .append("”的").append(typeName).append("事项中，").append(nowUser.getName())
                                .append("已经取消本次互助。您可以去报名其他互助，或者继续选定其他已报名的小伙伴～").toString();
                        if (payment > 0){
                            //插入赔付流水，并且插入弹窗事件
                            removeOrderPunishment(nowUser , toUser , payment , orderId , nowTime);
                            content = new StringBuilder().append("很遗憾，在“").append(orderRelationship.getServiceName())
                                    .append("”的").append(typeName).append("事项中，").append(nowUser.getName())
                                    .append("已经取消本次互助。临时取消实在抱歉，TA为您准备了一份致歉礼，请您前往本订单页面查收～").toString();
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
        changePublishOrderRela(order , order.getCreateUser());
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
     * @param nowUserId
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public void reoprtOrder(Long orderId , Long nowUserId){
    TOrder order = orderDao.selectByPrimaryKey(orderId);
    if(order.getCreateUser() == nowUserId.longValue()){
        throw new MessageException("499", "这是您自己的订单～");
    }
    TOrderRelationship orderRelationship = null;
    orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId ,nowUserId);
    if (orderRelationship == null){
        //如果没有订单，那么就创建一张无状态订单,并且将详情举报状态设置为举报
        orderRelationship = new TOrderRelationship();
        orderRelationship.setServiceId(order.getServiceId());
        orderRelationship.setOrderId(order.getId());
        orderRelationship.setServiceType(order.getType());
        orderRelationship.setFromUserId(order.getCreateUser());
        orderRelationship.setReceiptUserId(nowUserId);
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
        orderRelationship.setCreateUser(order.getCreateUser());
        orderRelationship.setCreateUserName(order.getCreateUserName());
        orderRelationship.setCreateTime(order.getCreateTime());
        orderRelationship.setUpdateUser(order.getCreateUser());
        orderRelationship.setUpdateUserName(order.getCreateUserName());
        orderRelationship.setUpdateTime(order.getCreateTime());
        orderRelationship.setIsValid(AppConstant.IS_VALID_YES);
        orderRelationshipDao.insert(orderRelationship);
    } else {
        if (orderRelationship.getServiceReportType() == OrderRelationshipEnum.SERVICE_REPORT_IS_TURE.getType()){
            throw new MessageException("499", "您已投诉，请勿多次投诉");
        }
        orderRelationship.setServiceReportType(OrderRelationshipEnum.SERVICE_REPORT_IS_TURE.getType());
        orderRelationshipDao.updateByPrimaryKey(orderRelationship);
    }

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
        //TODO 插入流水表

        userCommonController.insertUserTimeRecords(userTimeRecord);
        long userTimeRecordId = userTimeRecord.getId();//插入方法返回流水表主键
        //插入触发事件数据
        TEvent event = new TEvent();
        event.setUserId(toUser.getId());
        event.setTemplateId(EventEnum.TEMPLATE_ID_REMOVE_ORDER.getType());
        event.setTiggerId("orderId"+orderId);
        event.setParameter(""+userTimeRecordId);
        event.setPriority(2);
        event.setText("用户"+toUser.getName()+"已取消订单，并向你支付致歉礼：互助时"+timeChange(payment));
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

        if (msg != null) {
            //更行订单关系表
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
        evaluate.setId(snowflakeIdWorker.nextId());
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
        report.setId(snowflakeIdWorker.nextId());
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

        //TODO 服务通知
        //系统通知
        String title = "收到投诉提醒";
        String content = new StringBuilder().append("在“").append(orderRelationship.getServiceName())
                .append("”的互助事项中，").append(nowUser.getName()).append("已对您发起投诉，投诉理由：")
                .append(messageCommonController.getValue(labelsId, "complaint"))
                .append("。如果您对投诉有异议，请于2个工作日内联系平台在线客服。").toString();

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
        if (payment > 0) {
            //如果是有时间支付完成的
            orderRelationship.setStatus(OrderRelationshipEnum.STATUS_WAIT_REMARK.getType());
            if (orderRelationship.getCollectType() == OrderEnum.COLLECT_TYPE_TIME.getValue()) {
                //如果收取的是互助时，完成互助时相关事情
                if (toUser.getPayNum() == 0) {
                    //如果是首次完成互助
                    //TODO 奖励互助时 增加成长值
                    logger.error("//TODO 奖励互助时 增加成长值");
                }
                //TODO 插入成长值（查看今天被支付多少次，然后再看加不加）
                logger.error(" //TODO 插入成长值（查看今天被支付多少次，然后再看加不加）");
                //更新被支付用户的时间，并且将其完成求助数量+1
                toUser.setSurplusTime(toUser.getSurplusTime() + payment);
                toUser.setPayNum(toUser.getPayNum() + 1);
                //TODO 发送互助时系统通知
            } else {
                //如果收取的是公益时，完成公益时相关事情
                //TODO 看一下是不是第一次完成公益时 增加成长值
                //TODO 看一下是不是今天第一次完成公益时 增加成长值
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
            //TODO 插入 支付流水
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
    private String canEnroll(TUser nowUser, String date, TOrder order, Long orderId, long nowTime) throws ParseException {

        String msg = null;

        TOrderRelationship orderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(order.getId() , nowUser.getId());
        if (orderRelationship != null) {
            //如果有参加的订单关系，则不允许报名
            msg = "对不起，您已报名，请勿多次报名";
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
            throw new MessageException("499", "对不起，该订单已无法报名，请后退刷新重试");
        }
        if (order.getConfirmNum() == order.getServicePersonnel().longValue()) {
            throw new MessageException("499", "对不起，该订单已选满需要人数");
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
            orderRelationship = new TOrderRelationship();
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
        orderRecord.setId(snowflakeIdWorker.nextId());
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

    /**
     * 移除可报名日期
     * @param date
     * @param serviceId
     */
    public void removeCanEnrollDate(String date , Long serviceId){
        TService service = productCommonController.getProductById(serviceId);
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
