package com.e_commerce.miscroservice.xiaoshi_proj.message.dao.impl;


import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TFormid;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.xiaoshi_proj.message.dao.FormidDao;
import org.springframework.stereotype.Repository;


/**
 * 功能描述:
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
public class FormidDaoImpl implements FormidDao {

    /**
     * 插入formid
     *
     * @param formid
     * @return
     */
    public long insert(TFormid formid){
        long save = MybatisPlus.getInstance().save(formid);
        return save;
    }

    /**
     * 查找可用的formId
     * @param findTime
     * @param userId
     * @return
     */
    public TFormid selectAllFormIdCanUse(Long findTime , Long userId){
        return MybatisPlus.getInstance().findOne(new TFormid() ,
                new MybatisPlusBuild(TFormid.class)
                        .eq(TFormid::getCreateUser , userId)
                        .eq(TFormid::getIsValid , AppConstant.ACCREDIT_STATUS_YES)
                        .gt(TFormid::getUpdateTime , findTime)
                        .neq(TFormid::getFormId , "undefined")
                        .neq(TFormid::getFormId , "the formId is a mock one")
                        .orderBy(MybatisPlusBuild.OrderBuild.buildAsc(TFormid::getUpdateTime)));
    }

    /**
     * 更新formId
     * @param formid
     * @return
     */
    public long updateFormId(TFormid formid){
        return MybatisPlus.getInstance().update(formid ,
                new MybatisPlusBuild(TFormid.class)
                        .eq(TFormid::getId , formid.getId()));
    }
}
