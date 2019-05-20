package com.e_commerce.miscroservice.xiaoshi_proj.message.dao.impl;


import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TEvent;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.xiaoshi_proj.message.dao.EventDao;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 功能描述:事件表dao实现层
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
public class EventDaoImpl implements EventDao {

    /**
     * 插入event
     *
     * @param event
     * @return
     */
    public long insert(TEvent event){
        long save = MybatisPlus.getInstance().save(event);
        return save;
    }

    /**
     * 根据主键查询event
     * @param id
     * @return
     */
    public TEvent selectById (long id){
        return MybatisPlus.getInstance().findOne(new TEvent() ,
                new MybatisPlusBuild(TEvent.class)
                        .eq(TEvent::getId , id)
                        .eq(TEvent::getIsValid , AppConstant.ACCREDIT_STATUS_YES)
        );
    }

    /**
     * 根据主键更新event
     * @param event
     * @return
     */
    public long updateById(TEvent event){
        return MybatisPlus.getInstance().update(event,
                new MybatisPlusBuild(TEvent.class)
                        .eq(TEvent::getId, event.getId()));
    }

    /**
     * 通过userId和触发id来查看所有的触发事件
     * @param userId
     * @param tiggerId
     * @return
     */
    public List<TEvent> selectByUserIdAndTiggetId(Long userId , String tiggerId){
        List<TEvent> events = MybatisPlus.getInstance().finAll(new TEvent() ,
                new MybatisPlusBuild(TEvent.class)
                        .eq(TEvent::getUserId , userId)
                        .eq(TEvent::getTiggerId , tiggerId)
                        .eq(TEvent::getIsValid , AppConstant.ACCREDIT_STATUS_YES)
        );
        return events;
    }
}
