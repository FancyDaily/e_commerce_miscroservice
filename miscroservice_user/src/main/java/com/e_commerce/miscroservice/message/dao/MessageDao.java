package com.e_commerce.miscroservice.message.dao;


import com.e_commerce.miscroservice.commons.entity.application.TMessage;

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
 * 创建时间:2019/3/4 下午4:33
 * ************************************
 * ************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public interface MessageDao {

    /**
     * 根据两个用户查看最新的一条消息
     * @param fromUserId
     * @param toUserId
     * @return
     */
    TMessage selectNewMessageByTwoUserId(Long fromUserId , Long toUserId);
    /**
     * 根据分组id和用户id找到他发的最近一条消息的id
     * @param parent
     * @param userId
     * @return
     */
    TMessage selectNewMessageByOneUserId(Long parent , Long userId);
    /**
     * 插入消息
     * @param message
     * @return
     */
    long insert(TMessage message);

    /**
     * 根据两个用户查看全部消息，时间降序
     * @param fromUserId
     * @param toUserId
     * @return
     */
    List<TMessage> selectAllMessageByTwoUserId(Long fromUserId , Long toUserId , Long lastTime);
    /**
     * 消息列表
     * @param nowUser
     * @param lastTime
     * @return
     */
    List<TMessage> messageShowList(Long nowUser , Long lastTime);
    /**
     * 根据lastTime查看未读消息数量
     * @param parent
     * @param lastTime
     * @return
     */
    long selectCountByUnreade(Long parent , Long lastTime);
}
