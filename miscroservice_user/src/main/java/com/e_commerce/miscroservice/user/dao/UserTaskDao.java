package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUserTask;

import java.util.List;

public interface UserTaskDao {
    /**
     * 根据起止时间、用户id查询所有签到记录
     * @param id
     * @param beginTimeStamp
     * @param endTimeStamp
     * @return
     */
    List<TUserTask> queryOnesSignUpBetweenTime(Long id, Long beginTimeStamp, Long endTimeStamp);

    /**
     * 根据起止时间、用户id查询所有签到记录，倒序
     * @param id
     * @param thisBeginStamp
     * @param thisEndStamp
     * @return
     */
    List<TUserTask> queryOnessignUpBetweenTimeDesc(Long id, Long thisBeginStamp, Long thisEndStamp);

    /**
     * 根据用户id查询最近的所有签到记录
     * @param id
     * @return
     */
    List<TUserTask> findlatestSignUps(Long id);

    /**
     * 插入一条记录
     * @param userTask
     * @return
     */
    int insert(TUserTask userTask);

    /**
     * 根据用户id查询所有任务记录的类型
     * @param id
     * @return
     */
    List<TUserTask> findOnesTasks(Long id);
}
