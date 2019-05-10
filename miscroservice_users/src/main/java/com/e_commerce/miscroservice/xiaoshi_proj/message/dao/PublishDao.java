package com.e_commerce.miscroservice.xiaoshi_proj.message.dao;


import com.e_commerce.miscroservice.commons.entity.application.TPublish;

/**
 * 功能描述:
 * 模块:
 * 项目:时间银行
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:姜修弘
 * 邮箱:414368243@qq.com
 * 创建时间:2019/3/4 下午4:33
 * ************************************
 * ************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public interface PublishDao {
    /**
     * 插入key-value表
     * @param publish
     * @return
     */
    long insert(TPublish publish);

    /**
     * 根据key查询key-valud表
     * @param key
     * @return
     */
    TPublish selecePublish(String key);

}
