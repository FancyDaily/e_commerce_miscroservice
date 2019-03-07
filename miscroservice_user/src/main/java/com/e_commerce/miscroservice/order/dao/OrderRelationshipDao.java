package com.e_commerce.miscroservice.order.dao;


import com.e_commerce.miscroservice.order.po.TOrderRelationship;
import com.github.pagehelper.Page;

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
public interface OrderRelationshipDao {

    /**
     *根据主键查询订单关系表
     * @param orderRelationshipId
     * @return
     */
    TOrderRelationship selectByPrimaryKey(Long orderRelationshipId);
    /**
     * 根据日期判断是否参加该订单
     * @param startTime
     * @param endTime
     * @param serviceId
     * @param userId
     * @return
     */
    TOrderRelationship selectByDate(Long startTime , Long endTime , Long serviceId , Long userId);
    /**
     * 根据订单id和用户id来查询订单关系
     * @param orderId
     * @param userId
     * @return
     */
    TOrderRelationship selectByOrderIdAndUserId(Long orderId , Long userId);
    /**
     *根据主键更新订单关系表
     * @param orderRelationship
     * @return
     */
    int updateByPrimaryKey(TOrderRelationship orderRelationship);

    /**
     *插入订单关系表
     * @param orderRelationship
     * @return
     */
    int insert(TOrderRelationship orderRelationship);
    /**
     * 根据orderId和statusList来查询订单List
     * @param orderId
     * @param statusList
     * @return
     */
    List<TOrderRelationship> selectListByStatusList(Long orderId , List<Integer> statusList);
    /**
     * 根据orderId和status来查询订单List
     * @param orderId
     * @param status
     * @return
     */
    List<TOrderRelationship> selectListByStatus(Long orderId , int status);
    /**
     * 根据订单id和用户idList来查询订单关系List
     * @param orderId
     * @param userIdList
     * @return
     */
    List<TOrderRelationship> selectByOrderIdAndUserIdList(Long orderId , List<Long> userIdList);
    /**
     * 根据statusList来查询订单数量
     * @param orderId
     * @param statusList
     * @return
     */
    long selectCountByStatusList(Long orderId , List<Integer> statusList);
    /**
     * 根据用户id来查询订单关系List
     * @param userId
     * @return
     */
    List<TOrderRelationship> selectByUserId(Long userId);

    /**
     * 分页报名和选人列表
     * @param pageNum 分页页数
     * @param pageSize 每页数量
     * @param userId 当前用户id
     * @return
     */
	Page<TOrderRelationship> pageEnrollAndChooseList(Integer pageNum, Integer pageSize, Long userId);
}
