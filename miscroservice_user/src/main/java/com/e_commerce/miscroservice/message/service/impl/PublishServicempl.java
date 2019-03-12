package com.e_commerce.miscroservice.message.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.*;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.SnowflakeIdWorker;

import com.e_commerce.miscroservice.message.dao.PublishDao;

import com.e_commerce.miscroservice.message.service.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



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
public class PublishServicempl implements PublishService {


    Log logger = Log.getInstance(com.e_commerce.miscroservice.order.service.impl.OrderRelationServiceImpl.class);

    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();

    @Autowired
    private PublishDao publishDao;

    /**
     * 插入key-value
     * @param id
     * @param key
     * @param value
     * @param extend
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void pulishIn( Long id , String key , String value , String extend ) {
        TPublish publish = new TPublish();
        if (id == null) {
            id = snowflakeIdWorker.nextId();
        }
        long nowTime = System.currentTimeMillis();
        publish.setId(id);
        publish.setMainKey(key);
        publish.setValue(value);
        publish.setExtend(extend);
        publish.setCreateTime(nowTime);
        publish.setCreateUser(9527l);
        publish.setCreateUserName("姜帅哥");
        publish.setUpdateTime(nowTime);
        publish.setUpdateUser(9527l);
        publish.setUpdateUserName("姜帅哥");
        publish.setIsValid("1");
        publishDao.insert(publish);
    }
    /**
     *
     * 功能描述:获取key—value
     * 作者:姜修弘
     * 创建时间:2018年11月12日 下午3:36:43
     * @param key
     * @return
     */
    public TPublish publishGet(String key) {
        return publishDao.selecePublish(key);
    }

}
