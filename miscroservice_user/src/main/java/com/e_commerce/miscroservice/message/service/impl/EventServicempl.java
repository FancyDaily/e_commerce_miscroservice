package com.e_commerce.miscroservice.message.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.TEvent;
import com.e_commerce.miscroservice.commons.entity.application.TPublish;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.SnowflakeIdWorker;
import com.e_commerce.miscroservice.message.dao.EventDao;
import com.e_commerce.miscroservice.message.dao.PublishDao;
import com.e_commerce.miscroservice.message.service.EventService;
import com.e_commerce.miscroservice.message.service.PublishService;
import com.e_commerce.miscroservice.message.vo.BroadcastView;
import com.e_commerce.miscroservice.message.vo.PublisValueView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
public class EventServicempl implements EventService {


    @Autowired
    private EventDao eventDao;

    /**
     * 插入事件
     * @param event
     * @return
     */
    public long insertTevent(TEvent event){
        return eventDao.insert(event);
    }

    /**
     * 更新事件
     * @param event
     * @return
     */
    public long updateTevent(TEvent event){
        return eventDao.updateById(event);
    }

    /**
     * 查找事件
     * @param id
     * @return
     */
    public TEvent selectTeventById(Long id){
        return eventDao.selectById(id);
    }

    /**
     * 批量查找事件
     * @param userId
     * @param tiggetId
     * @return
     */
    public List<TEvent> selectTeventListByUserIdAndTiggetId(Long userId , Long tiggetId){
        return eventDao.selectByUserIdAndTiggetId(userId , tiggetId);
    }
}
