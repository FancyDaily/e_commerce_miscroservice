package com.e_commerce.miscroservice.xiaoshi_proj.message.service;

import com.e_commerce.miscroservice.commons.entity.application.TMessageNotice;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.xiaoshi_proj.message.vo.MessageDetailView;
import com.e_commerce.miscroservice.xiaoshi_proj.message.vo.MessageShowLIstView;
import com.e_commerce.miscroservice.xiaoshi_proj.message.vo.NoticesFirstView;

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
public interface MessageService {

    /**
     * 查看系统消息
     * @param lastTime
     * @param nowUserId
     * @param pageSize
     * @return
     */
    public QueryResult<TMessageNotice> notices(Long lastTime , Long nowUserId, int pageSize );
    /**
     * 发送消息
     * @param nowUserId
     * @param messageUserId
     * @param specialId
     * @param type
     * @param message
     * @param url
     */
    void send(Long nowUserId , Long messageUserId ,  Long specialId , int type , String message , String url);
    /**
     * 查看消息详情
     *
     * @param toUserId
     * @param lastTime
     * @param pageSize
     * @param nowUserId
     * @return
     */
    QueryResult<MessageDetailView> detail (Long toUserId , Long lastTime , int pageSize , Long nowUserId);
    /**
     * 插入formid
     * @param formId
     * @param userId
     */
    void insertFormId(String formId, Long userId);
    /**
     * 消息列表
     *
     * @param nowUserId
     * @param lastTime
     * @param pageSize
     * @return
     */
    QueryResult<MessageShowLIstView> list (Long nowUserId , Long lastTime  , Integer pageSize);

    /**
     *
     * 功能描述:查看系统消息第一条日期
     * 作者:姜修弘
     * 创建时间:2018年11月16日 下午8:48:01
     * @param nowUserId
     * @return
     */
    NoticesFirstView noticesFirstInfo(Long nowUserId);

    /**
     *
     * 功能描述:是否有未读消息 有-1 没有-0
     * 作者:姜修弘
     * 创建时间:2018年12月28日 下午3:32:02
     * @param nowUserId
     * @return
     */
    int unReadMsg(Long nowUserId);


}
