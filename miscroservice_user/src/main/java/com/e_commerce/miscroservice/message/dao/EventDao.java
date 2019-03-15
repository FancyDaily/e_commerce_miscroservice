package com.e_commerce.miscroservice.message.dao;


import com.e_commerce.miscroservice.commons.entity.application.TEvent;

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
public interface EventDao {
    /**
     * 插入event
     *
     * @param event
     * @return
     */
    long insert(TEvent event);

    /**
     * 根据主键查询event
     * @param id
     * @return
     */
    TEvent selectById (long id);

    /**
     * 根据主键更新event
     * @param event
     * @return
     */
    long updateById(TEvent event);

    /**
     * 通过userId和触发id来查看所有的触发事件
     * @param userId
     * @param tiggerId
     * @return
     */
    List<TEvent> selectByUserIdAndTiggetId(Long userId , Long tiggerId);

}
