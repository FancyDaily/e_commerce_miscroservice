package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.user.po.TUserSkill;

import java.util.List;

public interface UserSkillDao {

    /**
     * 查看某个userId的技能记录
     * @param userId
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

    /**
     * 更新指定id的技能记录
     * @param skill
     * @return
     */
    int update(TUserSkill skill);

    /**
     * 将指定id的技能记录修改为不可用
     * @param id
     * @return
     */
    int delete(Long id);
}
