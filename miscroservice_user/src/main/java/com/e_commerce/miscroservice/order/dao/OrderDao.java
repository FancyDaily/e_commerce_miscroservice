package com.e_commerce.miscroservice.order.dao;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.order.vo.PageOrderParamView;

import java.util.List;

/**
 * 功能描述:订单dao层
 *
 * @author 马晓晨
 * @date 2019/3/5 21:15
 */
public interface OrderDao {
    /**
     * 保存一个Order订单
     *
     * @param order order
     * @return 保存的订单个数 本方法成功返回1
     */
    int saveOneOrder(TOrder order);

    /**
     * 根据id查找订单
     *
     * @param id 订单id
     * @return 订单PO
     */
    TOrder selectByPrimaryKey(Long id);

    /**
     * 根据主键更新订单信息
     *
     * @param order 订单PO
     * @return
     */
    int updateByPrimaryKey(TOrder order);

	/**
	 * 根据order的ids查询出多个order
	 * @param orderIds 要查询的订单IDs
	 * @return
	 */
	List<TOrder> selectOrderByOrderIds(List<Long> orderIds);
    /**
     * 分页查询首页订单列表
     *
     * @param param
     * @return
     */
    List<TOrder> pageOrder(PageOrderParamView param);

    /**
     * 根据userId查找订单列表
     * @param userId
     * @return
     */
    List<TOrder> selectPublishedByUserId(Long userId);

	List<TOrder> selectPublishedByUserId(Long userId, boolean isService);

	/**
     * 根据userId查找指定类型订单列表（发布服务/求助）
     *
     * @param userId
     * @param isService
     * @return
     */
    List<TOrder> selectPublishedByUserId(Long userId, boolean isService, TUser beenViewer);

    /**
     * 根据userId查找特定订单列表 (历史服务/求助)
     * @param userId
     * @return
     */
	List<TOrder> selectPastByUserId(Long userId, TUser user);

    List<TOrder> selectPastByUserId(Long userId);

    /**
     * 根据订单id查找订单记录
     * @param orderIds
     * @return
     */
    List<TOrder> selectOrdersInOrderIds(List orderIds);

    /**
     * 根据来源、状态、用户id查找订单记录
     * @param sourceType
     * @param userId
     * @param availableStatusArray
     * @return
     */
    List<TOrder> selectBySourceAndUserIdAndStatuses(Integer sourceType, Long userId, Integer[] availableStatusArray);
	/**
	 * 同步商品和订单的状态
	 * @param productId 商品ID
	 * @param status 商品状态  3、改为手动下架 4、改为自动下架
	 */
	void updateByServiceId(Long productId, Integer status);
	/**
	 * 功能描述:统计该商品派生了多少张订单
	 * @author 马晓晨
	 * @date 2019/3/10 13:50
	 * @param serviceId 商品ID
	 * @return
	 */
	Long countProductOrder(Long serviceId);

	/**
	 * 功能描述:统计该商品在固定时间派生的订单数
	 * @author 马晓晨
	 * @date 2019/3/10 13:50
	 * @param serviceId 商品ID
	 * @return
	 */
	TOrder findProductOrder(Long serviceId, Long startTime, Long endTime);

    /**
     * 根据来源、状态、用户id、订单id查找订单记录
     * @param source
     * @param userId
     * @param availableStatusArray
     * @param idList
     * @return
     */
    List<TOrder> selectBySourceAndUserIdAndStatusesInIds(Integer source, Long userId, Integer[] availableStatusArray, List<Long> idList);

	/**
	 * 获取该商品生成的最后一张订单
	 * @param serviceId
	 * @return
	 */
	TOrder findOneLatestOrderByServiceId(Long serviceId);

	/**
	 * 查找该商品下的唯一一条正常状态的可见订单
	 * @param serviceId
	 * @return
	 */
	TOrder selectVisiableOrder(Long serviceId);

	/**
	 * 查找最早的一条正常状态的不可见的订单
	 * @param serviceId
	 * @return
	 */
	TOrder selectNearNotVisiable(Long serviceId);

	/**
	 * 跟新order表中用户的名称
	 * @param userId 用户ID
	 * @param userName 用户名称
	 */
	void updateUserName(Long userId, String userName);

	/**
	 * 查找该时间段没满人的订单
	 * @param serviceId  商品ID
	 * @param tempStart 开始时间
	 * @param tempEnd 结束时间
	 * @return 找到的order 可能为null
	 */
	TOrder findProductOrderEnough(Long serviceId, long tempStart, long tempEnd);

    TOrder selectById(Long orderId);

	/**
	 * 根据订单id集合、观察者查找订单记录
	 * @param orderIds
	 * @param viewer
	 * @return
	 */
	List<TOrder> selectOrdersInOrderIdsByViewer(List<Long> orderIds, TUser viewer);

	List<TOrder> selectDailyOrders(Long userId);
}
