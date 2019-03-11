package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUserTask;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.UserTaskDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserTaskDaoImpl implements UserTaskDao {
    /**
     * 根据时间区间获取某个人id的签到记录
     * @param id
     * @param beginTimeStamp
     * @param endTimeStamp
     * @return
     */
    @Override
    public List<TUserTask> queryOnesSignUpBetweenTime(Long id, Long beginTimeStamp, Long endTimeStamp) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserTask(), new MybatisSqlWhereBuild(TUserTask.class)
                .eq(TUserTask::getUserId, id)
                .between(TUserTask::getCreateTime, beginTimeStamp, endTimeStamp)
                .eq(TUserTask::getIsValid, AppConstant.IS_VALID_YES));
    }

    /**
     * 根据时间区间获取某个人id的签到记录,倒序
     * @param id
     * @param thisBeginStamp
     * @param thisEndStamp
     * @return
     */
    @Override
    public List<TUserTask> queryOnessignUpBetweenTimeDesc(Long id, Long thisBeginStamp, Long thisEndStamp) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserTask(), new MybatisSqlWhereBuild(TUserTask.class)
                .eq(TUserTask::getUserId, id)
                .between(TUserTask::getCreateTime, thisBeginStamp, thisEndStamp)
                .eq(TUserTask::getIsValid, AppConstant.IS_VALID_YES)
                .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TUserTask::getCreateTime)));
    }

    /**
     * 获取最近的签到记录,倒序
     * @param id
     * @param type
     * @return
     */
    @Override
    public List<TUserTask> findlatestSignUps(Long id, Integer type) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserTask(),new MybatisSqlWhereBuild(TUserTask.class)
        .eq(TUserTask::getType,type)
        .eq(TUserTask::getUserId,id)
        .eq(TUserTask::getIsValid,AppConstant.IS_VALID_YES)
        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TUserTask::getCreateTime)));
    }
}