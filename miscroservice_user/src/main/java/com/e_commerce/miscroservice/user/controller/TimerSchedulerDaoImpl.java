package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.config.TimerSchedulerPO;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 任务调度的Dao实现类
 */
@Repository
public class TimerSchedulerDaoImpl {

    private Log logger = Log.getInstance(TimerSchedulerDaoImpl.class);

    @Resource
    private ApplicationEventPublisher publisher;

    @Transactional
    public Boolean saveTimerScheduler(TimerSchedulerPO timerSchedulerPO) {
        Boolean saveFlag = Boolean.FALSE;
        timerSchedulerPO.setUuid("我爱你1");
        if (timerSchedulerPO.wasEmpty()) {
            logger.warn("存储定时任务所传参数有空 timerScheduler={}", timerSchedulerPO);
            return saveFlag;
        }

        if (MybatisOperaterUtil.getInstance().save(timerSchedulerPO) > 0) {
            logger.info("存储定时任务,定时任务UUID={} success", timerSchedulerPO.getUuid());
            saveFlag = Boolean.TRUE;
        }
        System.out.println(MybatisOperaterUtil.getInstance().findOne(new TimerSchedulerPO(), new
                MybatisSqlWhereBuild(TimerSchedulerPO.class).eq(TimerSchedulerPO::getUuid, "我爱你1")));

        return saveFlag;
    }
}
