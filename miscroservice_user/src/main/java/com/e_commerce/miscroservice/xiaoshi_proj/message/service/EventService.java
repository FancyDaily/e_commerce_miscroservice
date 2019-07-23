package com.e_commerce.miscroservice.xiaoshi_proj.message.service;

import com.e_commerce.miscroservice.commons.entity.application.TEvent;

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
public interface EventService {
    /**
     * 插入事件
     * @param event
     * @return
     */
    long insertTevent(TEvent event);

    /**
     * 更新事件
     * @param event
     * @return
     */
    long updateTevent(TEvent event);

    /**
     * 查找事件
     * @param id
     * @return
     */
    TEvent selectTeventById(Long id);

    /**
     * 批量查找事件
     * @param userId
     * @param tiggetId
     * @return
     */
    List<TEvent> selectTeventListByUserIdAndTiggetId(Long userId , String tiggetId);
}
