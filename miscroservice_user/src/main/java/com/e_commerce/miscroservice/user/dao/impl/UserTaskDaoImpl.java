package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.user.po.TUserTask;
import com.e_commerce.miscroservice.commons.enums.application.TaskEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
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
        return MybatisPlus.getInstance().findAll(new TUserTask(), new MybatisPlusBuild(TUserTask.class)
                .eq(TUserTask::getUserId, id)
                .between(TUserTask::getCreateTime, beginTimeStamp, endTimeStamp)
                .eq(TUserTask::getType,TaskEnum.TASK_SIGN_UP.getType())
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
        return MybatisPlus.getInstance().findAll(new TUserTask(), new MybatisPlusBuild(TUserTask.class)
                .eq(TUserTask::getUserId, id)
                .between(TUserTask::getCreateTime, thisBeginStamp, thisEndStamp)
                .eq(TUserTask::getType,TaskEnum.TASK_SIGN_UP.getType())
                .eq(TUserTask::getIsValid, AppConstant.IS_VALID_YES)
                .orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TUserTask::getCreateTime)));
    }

    /**
     * 获取最近的签到记录,倒序
     * @param id
     * @return
     */
    @Override
    public List<TUserTask> findlatestSignUps(Long id) {
        return MybatisPlus.getInstance().findAll(new TUserTask(),new MybatisPlusBuild(TUserTask.class)
        .eq(TUserTask::getType, TaskEnum.TASK_SIGN_UP.getType())
        .eq(TUserTask::getUserId,id)
        .eq(TUserTask::getIsValid,AppConstant.IS_VALID_YES)
        .orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TUserTask::getCreateTime)));
    }

    @Override
    public int insert(TUserTask userTask) {
        return MybatisPlus.getInstance().save(userTask);
    }

    @Override
    public List<TUserTask> findOnesTasks(Long id) {
        TUserTask userTask = new TUserTask();
        return MybatisPlus.getInstance().findAll(userTask ,new MybatisPlusBuild(TUserTask.class)
                .eq(TUserTask::getUserId,id)
                .eq(TUserTask::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 根据用户id查询指定类型的任务记录
     * @param type
     * @param id
     * @return
     */
    @Override
    public List<TUserTask> findTasksByTypeAndUserId(Integer type, Long id) {
        return MybatisPlus.getInstance().findAll(new TUserTask(),new MybatisPlusBuild(TUserTask.class)
        .eq(TUserTask::getUserId,id)
        .eq(TUserTask::getType,type)
        .eq(TUserTask::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUserTask> findOnesTasksByType(Long id, int taskCode) {
        return MybatisPlus.getInstance().findAll(new TUserTask(),new MybatisPlusBuild(TUserTask.class)
        .eq(TUserTask::getUserId,id)
        .eq(TUserTask::getType,taskCode)
        .eq(TUserTask::getIsValid,AppConstant.IS_VALID_YES));
    }
}
