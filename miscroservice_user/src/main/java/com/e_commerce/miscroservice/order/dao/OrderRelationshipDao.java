package com.e_commerce.miscroservice.order.dao;


import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TOrderRelationship;

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
	 * 根据主键查询订单关系表
	 *
	 * @param orderRelationshipId
	 * @return
	 */
	TOrderRelationship selectByPrimaryKey(Long orderRelationshipId);

	/**
	 * 插入订单关系表
	 *
	 * @param orderRelationship
	 * @return
	 */
	int insert(TOrderRelationship orderRelationship);

	/**
	 * 根据主键更新订单关系表
	 *
	 * @param orderRelationship
	 * @return
	 */
	int updateByPrimaryKey(TOrderRelationship orderRelationship);

	/**
	 * 根据日期找到报名者的订单关系
	 *
	 * @param startTime
	 * @param endTime
	 * @param serviceId
	 * @param userId
	 * @return
	 */
	TOrderRelationship selectByDateByEnrollUserId(Long startTime, Long endTime, Long serviceId, Long userId);

    /**
     * 根据订单id和用户id来查找订单关系
     * @param orderId
     * @param userId
     * @return
     */
    TOrderRelationship selectByOrderIdAndUserId(Long orderId , Long userId);
    /**
     * 根据订单id和报名用户idList来查询订单关系List
     * @param orderId
     * @param userIdList
     * @return
     */
    List<TOrderRelationship> selectByOrderIdAndEnrollUserIdList(Long orderId , List<Long> userIdList);
    /**
     * 根据orderId和status来查询未投诉报名者订单List
     * @param orderId
     * @param status
     * @return
     */
    List<TOrderRelationship> selectListByStatusForNotSignByEnroll(Long orderId , int status);
    /**
     * 根据orderId和status来查询报名者订单List
     * @param orderId
     * @param status
     * @return
     */
    List<TOrderRelationship> selectListByStatusByEnroll(Long orderId , int status);


    /**
     * 根据orderId和statusList来升序查询报名者订单List
     * @param orderId
     * @param statusList
     * @return
     */

	/**
	 * 根据orderId和statusList来升序查询报名者订单List
	 *
	 * @param orderId
	 * @param statusList
	 * @return
	 */

	List<TOrderRelationship> selectListByStatusListByEnroll(Long orderId, List<Integer> statusList);

	/**
	 * 根据statusList来查询参与者订单数量
	 *
	 * @param orderId
	 * @param status
	 * @return
	 */
	long selectCountByStatusByEnroll(Long orderId, Integer status);

	/**
	 * 根据用户id来查询订单关系List
	 *
	 * @param userId
	 * @return
	 */
	List<TOrderRelationship> selectByUserId(Long userId);

    List<TOrderRelationship> selectEndByUserId(Long userId);

    /**
	 * 分页报名和选人列表
	 *
	 * @param pageNum  分页页数
	 * @param pageSize 每页数量
	 * @param userId   当前用户id
	 * @return
	 */
	List<TOrderRelationship> pageEnrollAndChooseList(Integer pageNum, Integer pageSize, Long userId);

	/**
	 * @return com.e_commerce.miscroservice.order.po.TOrderRelationship
	 * @Author 姜修弘
	 * 功能描述:根据参与者id和orderId查询参与者订单
	 * 创建时间:@Date 下午3:33 2019/3/8
	 * @Param [orderId, userId]
	 **/
	TOrderRelationship selectOrderRelationshipByJoinIn(Long orderId, Long userId);

	/**
	 * 批量更新订单关系表
	 *
	 * @param orderRelationshipList
	 * @param orderRelationgshipIdList
	 * @return
	 */
	long updateOrderRelationshipByList(List<TOrderRelationship> orderRelationshipList, List<Long> orderRelationgshipIdList);

	/**
	 * 根据用户ID列出当前用户的订单关系列表
	 *
	 * @param userId
	 * @return
	 */
	List<TOrderRelationship> listRelationshipByUserId(Long userId);

	/**
	 * 获取该订单的所有接单者
	 *
	 * @param orderId 订单ID
	 * @return
	 */
	List<TOrderRelationship> getReceiver(Long orderId);

    /**
     * 查找指定接单者的订单记录
     * @param userId
     * @return
     */
    List<TOrderRelationship> selectOrderRelationshipByReceiptUserId(Long userId);

    /**
     * 根据用户id查找订单关系记录
     * @param id
     * @return
     */
    List<TOrderRelationship> selectCollectList(Long id);

    /**
     * 根据订单id集合、状态集合查找所有订单记录
     * @param idList
     * @param collectionAvailableStatusArray
     * @return
     */
    List<TOrder> selectOrdersInOrderIdsInStatus(List<Long> idList, Integer... collectionAvailableStatusArray);

	/**
	 * 更新收藏状态
	 * @param orderRelationshipId
	 * @param collectStatus
	 * @return
	 */
	int updateCollectStatus(Long orderRelationshipId, int collectStatus);
	/**
	 * 可以取消订单的报名者关系表
	 * @param orderId
	 * @return
	 */
	List<TOrderRelationship> selectCanRemoveEnrollUser(Long orderId);
	/**
	 * 根据orderId来查询未投诉参与者订单
	 * @param orderId
	 * @return
	 */
	List<TOrderRelationship> selectOrderRelaByStatusByEnrollNoReport(Long orderId);

	/**
	 * 查询被选择
	 * @param orderId
	 * @return
	 */
	long selectJoinUser(Long orderId);

	/**
	 * 查看完成的（支付的）用户的数量
	 *
	 * @param orderId
	 * @return
	 */
	long selectCompleteUserSum(Long orderId);
}
