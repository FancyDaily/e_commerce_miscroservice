package com.e_commerce.miscroservice.message.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.e_commerce.miscroservice.commons.entity.application.TFormid;
import com.e_commerce.miscroservice.commons.entity.application.TMessage;
import com.e_commerce.miscroservice.commons.entity.application.TMessageNotice;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.MessageEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.SnowflakeIdWorker;
import com.e_commerce.miscroservice.message.dao.FormidDao;
import com.e_commerce.miscroservice.message.dao.MessageDao;
import com.e_commerce.miscroservice.message.dao.MessageNoticeDao;
import com.e_commerce.miscroservice.message.service.MessageService;
import com.e_commerce.miscroservice.message.vo.MessageDetailView;
import com.e_commerce.miscroservice.message.vo.MessageShowLIstView;
import com.e_commerce.miscroservice.message.vo.NoticesFirstView;
import com.e_commerce.miscroservice.user.controller.UserCommonController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;


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
public class MessageServiceImpl implements MessageService {


    Log logger = Log.getInstance(com.e_commerce.miscroservice.order.service.impl.OrderRelationServiceImpl.class);

    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();

    @Autowired
    private UserCommonController userCommonController;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private FormidDao formidDao;


    @Autowired
    private MessageNoticeDao messageNoticeDao;

    /**
     * 查看系统消息
     * @param lastTime 上次读取时间
     * @param nowUserId 当前用户id
     * @param pageSize 分页大小
     * @return
     */
    public QueryResult<TMessageNotice> notices(Long lastTime ,Long nowUserId,int pageSize ){
        long nowTime = System.currentTimeMillis();
        QueryResult<TMessageNotice> result = new QueryResult<>();
        Page<TMessageNotice> page = PageHelper.startPage(1, pageSize);
        List<TMessageNotice> messageNotices = messageNoticeDao.selectMessageNoticeByLastTime(lastTime,nowUserId);
        result.setTotalCount(page.getTotal());
        result.setResultList(messageNotices);
        //redisUtil.set("noticeUserId"+nowUserId, nowTime); TODO redis 上次读消息的时间
        return result;
    }

    /**
     * 发送消息
     * @param nowUserId 当前用户id
     * @param messageUserId 对方用户id
     * @param specialId 特殊操作id
     * @param type 类型
     * @param message
     * @param url
     */
    @Transactional(rollbackFor = Throwable.class)
    public void send(Long nowUserId , Long messageUserId ,  Long specialId , int type , String message , String url) {
        TUser nowUser = userCommonController.getUserById(nowUserId);
        Integer statusForMsg = 1;//默认发送服务通知
        Long nowTime = System.currentTimeMillis();
        TMessage messageForParent = null;
        messageForParent = messageDao.selectNewMessageByTwoUserId(nowUserId , messageUserId);
        long parentId = 0l;
        if (messageForParent != null) {
            //如果有消息。证明不是第一次发送消息，那么就有分组id。
            parentId = messageForParent.getParent();
            TMessage firstMessage = null;
            firstMessage = messageDao.selectNewMessageByOneUserId(parentId ,nowUserId);
            if (firstMessage != null){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                String nowDay = simpleDateFormat.format(nowTime);
                String firstDay = simpleDateFormat.format(firstMessage.getCreateTime());
                if (nowDay.equals(firstDay)) {
                    //相等证明今天发过消息了（不可能发消息日期比当前时间早）就不发系统消息了
                    statusForMsg = 0 ;
                }
            }
        }
        TMessage sendMessge = new TMessage();
        sendMessge.setParent(parentId);
        sendMessge.setMessageUserId(messageUserId);
        sendMessge.setUserId(nowUser.getId());
        sendMessge.setType(type);
        sendMessge.setMessage(message);
        sendMessge.setUrl(url);
        sendMessge.setSpecialId(specialId);
        sendMessge.setCreateTime(nowTime);
        sendMessge.setCreateUser(nowUser.getId());
        sendMessge.setCreateUserName(nowUser.getName());
        sendMessge.setUpdateTime(nowTime);
        sendMessge.setUpdateUser(nowUser.getId());
        sendMessge.setUpdateUserName(statusForMsg+"");
        sendMessge.setIsValid("1");

        messageDao.insert(sendMessge);

        if (messageForParent == null){
            //如果是第一次发消息
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    super.afterCommit();
                    sendMessge.setParent(sendMessge.getId());
                    messageDao.
                }
            });
        }
        //TODO 发送通知

    }

    /**
     * 查看消息详情
     *
     * @param toUserId
     * @param lastTime
     * @param pageSize
     * @param nowUserId
     * @return
     */
    public QueryResult<MessageDetailView> detail (Long toUserId , Long lastTime , int pageSize , Long nowUserId){
        TUser nowUser = userCommonController.getUserById(nowUserId);
        long nowTime = System.currentTimeMillis();
        QueryResult<MessageDetailView> result = new QueryResult<>();
        List<MessageDetailView> messageDetailViews = new ArrayList<>();
        Page<TMessage> page = PageHelper.startPage(1, pageSize);
        List<TMessage> messageList = messageDao.selectAllMessageByTwoUserId(nowUserId , toUserId , lastTime);
        //没有消息返回空viewList
        if (messageList == null || messageList.size() <= 0) {
            result.setTotalCount(0l);
            result.setResultList(messageDetailViews);
            return result;
        }
        List<Long> usersInfoId = new ArrayList<>();
        usersInfoId.add(nowUser.getId());
        for (int i = 0; i < messageList.size(); i++) {
            if (messageList.get(i).getUserId() == nowUser.getId().longValue()) {
                usersInfoId.add(messageList.get(i).getMessageUserId());
            } else {
                usersInfoId.add(messageList.get(i).getUserId());
            }
        }
        List<TUser> userInfoList = userCommonController.selectUserByIds(usersInfoId);
        //循环填装数据
        for (int i = 0; i < messageList.size(); i++) {
            MessageDetailView messageDetailView = new MessageDetailView();
            messageDetailView.setId(messageList.get(i).getId());
            messageDetailView.setMessage(messageList.get(i).getMessage());
            messageDetailView.setUrl(messageList.get(i).getUrl());
            messageDetailView.setTime(messageList.get(i).getCreateTime());
            messageDetailView.setCreateTime(messageList.get(i).getCreateTime());
            messageDetailView.setType(messageList.get(i).getType());
            //如果发布用户是当前用户
            if (messageList.get(i).getUserId() == nowUser.getId().longValue()) {
                //当前用户，status 为0
                messageDetailView.setStatus(0);
            } else {
                messageDetailView.setStatus(1);
            }
            for (int j = 0; j < userInfoList.size(); j++) {
                if (messageList.get(i).getUserId() == userInfoList.get(j).getId().longValue()) {
                    messageDetailView.setUserId(userInfoList.get(j).getId());
                    messageDetailView.setUserUrl(userInfoList.get(j).getUserHeadPortraitPath());
                    messageDetailView.setUserName(userInfoList.get(j).getName());
                }
            }
            messageDetailViews.add(messageDetailView);
        }
        //倒叙
        List<MessageDetailView> messageDetailViews2 = new ArrayList<>();
        for (int i = messageDetailViews.size() - 1; i >= 0; i--) {
            messageDetailViews2.add(messageDetailViews.get(i));
        }
        result.setTotalCount(page.getTotal());
        result.setResultList(messageDetailViews2);
        //redisUtil.set("msgReadeLastTime"+parents.get(0)+nowUser.getId(), nowTime);TODO 插入上次阅读时间
        return result;
    }

    /**
     * 插入formid
     * @param formId
     * @param userId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void insertFormId(String formId, Long userId) {
        TUser user = userCommonController.getUserById(userId);
        //当前时间
        long currentTime = System.currentTimeMillis();
        //formid实体数据
        TFormid formid = new TFormid();
        formid.setIsValid("1");
        formid.setCreateUser(user.getId());
        formid.setCreateUserName(user.getName());
        formid.setCreateTime(currentTime);
        formid.setUpdateTime(currentTime);
        formid.setUpdateUser(user.getId());
        formid.setUpdateUserName(user.getName());
        formid.setFormId(formId);

        formidDao.insert(formid);
    }


    /**
     * 消息列表
     *
     * @param nowUserId
     * @param lastTime
     * @param pageSize
     * @return
     */
    public QueryResult<MessageShowLIstView> list (Long nowUserId , Long lastTime  , Integer pageSize){
        QueryResult<MessageShowLIstView> result = new QueryResult<>();
        TUser nowUser = userCommonController.getUserById(nowUserId);
        Page<TMessage> page = PageHelper.startPage(1, pageSize);
        List<TMessage> messageList = messageDao.messageShowList(nowUserId , lastTime);
        List<MessageShowLIstView> messageShowLIstViews = new ArrayList<>();
        //如果没有数据，返回空的list
        if (messageList == null || messageList.size() <= 0) {
            result.setTotalCount(0l);
            result.setResultList(messageShowLIstViews);
            return result;
        }
        List<Long> toUserIdList = new ArrayList<>();
        for (int i = 0; i < messageList.size(); i++) {
            if (messageList.get(i).getUserId() == nowUser.getId().longValue()) {
                toUserIdList.add(messageList.get(i).getMessageUserId());
            } else {
                toUserIdList.add(messageList.get(i).getUserId());
            }
        }
        List<TUser> toUsersInfoList = userCommonController.selectUserByIds(toUserIdList);
        for (int i = 0; i < messageList.size(); i++) {
            MessageShowLIstView messageShowLIstView = new MessageShowLIstView();
            messageShowLIstView.setParent(messageList.get(i).getParent());
            messageShowLIstView.setParent(messageList.get(i).getParent());
            messageShowLIstView.setTime(messageList.get(i).getUpdateTime());
            messageShowLIstView.setCreateTime(messageList.get(i).getCreateTime());
            messageShowLIstView.setUnReadSum(unReadMsgSum(messageList.get(i).getParent()));
            if (messageList.get(i).getType() == MessageEnum.TYPE_PHOTO.getType()) {
                //如果是图片
                messageShowLIstView.setContent("[图片]");
            } else {
                messageShowLIstView.setContent(messageList.get(i).getMessage());
            }
            if (messageList.get(i).getUserId() == nowUser.getId().longValue()) {
                //如果消息的发布者是当前用户
                messageShowLIstView.setUserName(nowUser.getName());
                messageShowLIstView.setUserUrl(nowUser.getUserHeadPortraitPath());
                messageShowLIstView.setToUserId(nowUser.getId());
            } else {
                for (int j = 0; j < toUsersInfoList.size(); j++) {
                    if (messageList.get(i).getUserId() == toUsersInfoList.get(j).getId().longValue()) {
                        messageShowLIstView.setUserName(toUsersInfoList.get(j).getName());
                        messageShowLIstView.setUserUrl(toUsersInfoList.get(j).getUserHeadPortraitPath());
                        messageShowLIstView.setToUserId(toUsersInfoList.get(j).getId());
                        break;
                    }
                }

            }
            messageShowLIstViews.add(messageShowLIstView);
        }
        result.setTotalCount(page.getTotal());
        result.setResultList(messageShowLIstViews);
        return result;
    }

    /**
     *
     * 功能描述:查看系统消息第一条日期
     * 作者:姜修弘
     * 创建时间:2018年11月16日 下午8:48:01
     * @param nowUserId
     * @return
     */
    public NoticesFirstView noticesFirstInfo(Long nowUserId){
        NoticesFirstView noticesFirstView = new NoticesFirstView();
        TMessageNotice messageNotice = messageNoticeDao.selectFirstMessageNotice(nowUserId);
        noticesFirstView.setSysTime(messageNotice.getCreateTime());
        noticesFirstView.setSysUnReadSum(unReadNoticesSum(nowUserId));
        return noticesFirstView;
    }


    /**
     *
     * 功能描述:是否有未读消息 有-1 没有-0
     * 作者:姜修弘
     * 创建时间:2018年12月28日 下午3:32:02
     * @param nowUserId
     * @return
     */
    public int unReadMsg(Long nowUserId) {
        if (unReadNoticesSum(nowUserId) > 0) {
            return 1;
        }
        //查看所有的消息分组的一条消息
        List<TMessage> messageList = messageDao.messageShowList(nowUserId , 9999999999999l);
        //对每个分组进行查看，是否有未读消息
        for (int i = 0; i < messageList.size(); i++) {
            if (unReadMsgSum(messageList.get(i).getParent()) > 0) {
                return 1 ;
            }
        }
        return 0;
    }


    /**
     * 未读消息数量
     *
     * @param parent
     * @return
     */
    private long unReadMsgSum(Long parent) {
        Long lastReadeTime = 0l;
        //Long lastReadeTime = (Long)redisUtil.get("msgReadeLastTime"+messageParent+nowUser.getId()); TODO 通过redis获取上次该分组读消息的时间
        if (lastReadeTime == null) {
            lastReadeTime = 0l;
        }
        Long unReadeSum = messageDao.selectCountByUnreade(parent , lastReadeTime);

        return unReadeSum ;
    }

    /**
     * 功能描述:未读系统消息条数
     * @param nowUserId
     * @return
     */
    private Long unReadNoticesSum(Long nowUserId) {
        Long lastReadeTime = 0l;
        //Long lastReadeTime = (Long)redisUtil.get("noticeUserId"+nowUser.getId());TODO 通过redis获取上次系统消息读取的时间
        if (lastReadeTime == null) {
            lastReadeTime = 0l;
        }
        long unReadNoticesSum = messageNoticeDao.selectMessageNoticeCountByLastTime(lastReadeTime , nowUserId);
        return unReadNoticesSum;
    }
}
