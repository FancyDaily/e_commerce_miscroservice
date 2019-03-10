package com.e_commerce.miscroservice.message.dao;


import com.e_commerce.miscroservice.commons.entity.application.TMessageNotice;

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
 * 创建时间:2019/3/4 下午4:33
 * ************************************
 * ************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public interface MessageNoticeDao {
    /**
     * 根据lastTIme和userId查看系统消息
     * @param lastTIme
     * @param userId
     * @return
     */
    List<TMessageNotice> selectMessageNoticeByLastTime(Long lastTIme, Long userId);
    /**
     * 根据lastTIme和userId查看系统消息数目
     * @param lastTIme
     * @param userId
     * @return
     */
    long selectMessageNoticeCountByLastTime(Long lastTIme , Long userId);

}
