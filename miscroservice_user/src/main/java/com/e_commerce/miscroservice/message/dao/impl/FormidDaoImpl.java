package com.e_commerce.miscroservice.message.dao.impl;


import com.e_commerce.miscroservice.commons.entity.application.TFormid;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.message.dao.FormidDao;
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
        long save = MybatisOperaterUtil.getInstance().save(formid);
        return save;
    }
}
