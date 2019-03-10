package com.e_commerce.miscroservice.message.dao.impl;


import com.e_commerce.miscroservice.commons.entity.application.TMessageNotice;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.message.dao.MessageDao;
import com.e_commerce.miscroservice.message.dao.MessageNoticeDao;
import org.springframework.stereotype.Repository;

import java.util.List;

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
public class MessageNoticeDaoImpl implements MessageNoticeDao {

    /**
     * 根据lastTIme和userId查看系统消息
     * @param lastTIme
     * @param userId
     * @return
     */
    public List<TMessageNotice> selectMessageNoticeByLastTime(Long lastTIme , Long userId){
        List<TMessageNotice> messageNoticeList = MybatisOperaterUtil.getInstance().finAll(new TMessageNotice(),
                new MybatisSqlWhereBuild(TMessageNotice.class)
                        .eq(TMessageNotice::getNoticeUserId , userId)
                        .lt(TMessageNotice::getCreateTime , lastTIme)
                        .eq(TMessageNotice::getIsValid , "1")
                        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TMessageNotice::getCreateTime)));
        return messageNoticeList;
    }
    /**
     * 根据lastTIme和userId查看系统消息数目
     * @param lastTIme
     * @param userId
     * @return
     */
    public long selectMessageNoticeCountByLastTime(Long lastTIme , Long userId){
        long count = MybatisOperaterUtil.getInstance().count(
                new MybatisSqlWhereBuild(TMessageNotice.class)
                        .eq(TMessageNotice::getNoticeUserId , userId)
                        .lt(TMessageNotice::getCreateTime , lastTIme)
                        .eq(TMessageNotice::getIsValid , "1")
                        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TMessageNotice::getCreateTime)));
        return count;
    }
}
