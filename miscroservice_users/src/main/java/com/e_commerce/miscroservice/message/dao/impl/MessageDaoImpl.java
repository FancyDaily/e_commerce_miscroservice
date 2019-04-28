package com.e_commerce.miscroservice.message.dao.impl;


import com.e_commerce.miscroservice.commons.entity.application.TMessage;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.message.dao.MessageDao;

import com.e_commerce.miscroservice.message.mapper.MessageMapper;
import com.e_commerce.miscroservice.message.vo.MessageShowLIstView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
 * 创建时间:2019/3/4 下午5:38
 * ************************************
 * ************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
@Repository
public class MessageDaoImpl implements MessageDao {

    @Autowired
    MessageMapper messageMapper;


    /**
     * 根据两个用户查看最新的一条消息
     * @param fromUserId
     * @param toUserId
     * @return
     */
    public TMessage selectNewMessageByTwoUserId(Long fromUserId , Long toUserId){
        TMessage message = MybatisOperaterUtil.getInstance().findOne(new TMessage(),
                new MybatisSqlWhereBuild(TMessage.class)
                        .groupBefore()
                            .eq(TMessage::getUserId ,toUserId)
                            .eq(TMessage::getMessageUserId , fromUserId)
                        .groupAfter().or()
                        .groupBefore()
                            .eq(TMessage::getUserId , fromUserId)
                            .eq(TMessage::getMessageUserId , toUserId)
                        .groupAfter()
                        .eq(TMessage::getIsValid , "1")
                        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TMessage::getCreateTime)));
        return message;
    }

    /**
     * 根据分组id和用户id找到他发的最近一条消息的id
     * @param parent
     * @param userId
     * @return
     */
    public TMessage selectNewMessageByOneUserId(Long parent , Long userId){
        TMessage message = MybatisOperaterUtil.getInstance().findOne(new TMessage(),
                new MybatisSqlWhereBuild(TMessage.class)
                        .eq(TMessage::getUserId , userId)
                        .eq(TMessage::getParent , parent)
                        .eq(TMessage::getIsValid , "1")
                        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TMessage::getCreateTime)));
        return message;
    }

    /**
     * 插入消息
     * @param message
     * @return
     */
    public TMessage insert(TMessage message){
        MybatisOperaterUtil.getInstance().save(message);
        return message;
    }

    /**
     * 更新消息
     * @param message
     * @return
     */
    public long updateUpdate(TMessage message){
        return MybatisOperaterUtil.getInstance().update(message,
                new MybatisSqlWhereBuild(TMessage.class)
                        .eq(TMessage::getId, message.getId()));
    }
    /**
     * 根据两个用户查看全部消息，时间降序
     * @param fromUserId
     * @param toUserId
     * @return
     */
    public List<TMessage> selectAllMessageByTwoUserId(Long fromUserId , Long toUserId , Long lastTime){
        List<TMessage> messageList = MybatisOperaterUtil.getInstance().finAll(new TMessage(),
                new MybatisSqlWhereBuild(TMessage.class)
                        .groupBefore()
                        .eq(TMessage::getUserId ,toUserId)
                        .eq(TMessage::getMessageUserId , fromUserId)
                        .groupAfter().or()
                        .groupBefore()
                        .eq(TMessage::getUserId , fromUserId)
                        .eq(TMessage::getMessageUserId , toUserId)
                        .groupAfter()
                        .lt(TMessage::getCreateTime , lastTime)
                        .eq(TMessage::getIsValid , "1")
                        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TMessage::getCreateTime)));
        return messageList;
    }

    /**
     * 消息列表
     * @param nowUser
     * @param lastTime
     * @return
     */
    public List<TMessage> messageShowList(Long nowUser , Long lastTime){
        List<TMessage> messageList = messageMapper.selectMessageList(nowUser , lastTime);
        return messageList;
    }

    /**
     * 根据lastTime查看未读消息数量
     * @param parent
     * @param lastTime
     * @return
     */
    public long selectCountByUnreade(Long parent , Long lastTime){
        long count = MybatisOperaterUtil.getInstance().count(
                new MybatisSqlWhereBuild(TMessage.class)
                        .eq(TMessage::getParent , parent)
                        .eq(TMessage::getIsValid , "1")
                        .gt(TMessage::getCreateTime , lastTime));
        return count;
    }
}
