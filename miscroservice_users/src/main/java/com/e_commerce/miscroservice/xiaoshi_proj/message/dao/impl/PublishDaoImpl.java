package com.e_commerce.miscroservice.xiaoshi_proj.message.dao.impl;


import com.e_commerce.miscroservice.commons.entity.application.TPublish;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.xiaoshi_proj.message.dao.PublishDao;
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
public class PublishDaoImpl implements PublishDao {


    /**
     * 插入key-value表
     * @param publish
     * @return
     */
    public long insert(TPublish publish){
        long save = MybatisOperaterUtil.getInstance().save(publish);
        return save;
    }

    /**
     * 根据key查询key-valud表
     * @param key
     * @return
     */
    public TPublish selecePublish(String key){
        return MybatisOperaterUtil.getInstance().findOne(new TPublish()
                , new MybatisSqlWhereBuild(TPublish.class)
                        .eq(TPublish::getMainKey , key));
    }
}
