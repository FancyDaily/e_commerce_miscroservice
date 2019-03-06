package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUserSkill;

import java.util.List;

public interface UserSkillDao {

    /**
     * 查看某个userId的技能记录
     * @param id
     * @return
     */
    List<TUserSkill> queryOnesSkills(Long userId);

    /**
     * 判断指定userId是否存在指定名字的技能记录
     * @param name
     * @param userId
     * @return
     */
    boolean isExist(String name, Long userId);

    /**
     * 新增一条技能记录
     * @param skill
     * @return
     */
    int insert(TUserSkill skill);

    int update(TUserSkill skill);
}
