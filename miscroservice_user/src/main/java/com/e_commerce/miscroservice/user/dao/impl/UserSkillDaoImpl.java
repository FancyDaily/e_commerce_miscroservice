package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.user.po.TUserSkill;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.UserSkillDao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserSkillDaoImpl implements UserSkillDao {

    /**
     * 查看某个userId的技能记录
     * @param userId
     * @return
     */
    @Override
    public List<TUserSkill> queryOnesSkills(Long userId) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserSkill(),new MybatisSqlWhereBuild(TUserSkill.class)
                .eq(TUserSkill::getUserId,userId)
                .eq(TUserSkill::getIsValid, AppConstant.IS_VALID_YES).
                        orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TUserSkill::getCreateTime)));
    }

    /**
     * 判断指定userId是否存在指定名字的技能记录
     * @param name
     * @param userId
     * @return
     */
    @Override
    public boolean isExist(String name, Long userId) {
        return !MybatisOperaterUtil.getInstance().finAll(new TUserSkill(),new MybatisSqlWhereBuild(TUserSkill.class)
                .eq(TUserSkill::getName,name)
                .eq(TUserSkill::getUserId,userId)
                .eq(TUserSkill::getIsValid,AppConstant.IS_VALID_YES)).isEmpty();
    }

    /**
     * 新增一条技能记录
     * @param skill
     * @return
     */
    @Override
    public int insert(TUserSkill skill) {
        return MybatisOperaterUtil.getInstance().save(skill);
    }

    /**
     * 更新一条技能记录
     * @param skill
     * @return
     */
    @Override
    public int update(TUserSkill skill) {
        return MybatisOperaterUtil.getInstance().update(skill,new MybatisSqlWhereBuild(TUserSkill.class).eq(TUserSkill::getId,skill.getId()));
    }

    /**
     * 删除技能
     * @param id
     * @return
     */
    @Override
    public int delete(Long id) {
        TUserSkill skill = new TUserSkill();
        skill.setId(id);
        skill.setIsValid(AppConstant.IS_VALID_NO);
        return MybatisOperaterUtil.getInstance().update(skill,new MybatisSqlWhereBuild(TUserSkill.class)
        .eq(TUserSkill::getId,id)
        .eq(TUserSkill::getIsValid,AppConstant.IS_VALID_YES));
    }
}
