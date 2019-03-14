package com.e_commerce.miscroservice.order.controller;

import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.entity.colligate.MsgResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.order.dao.EvaluateDao;
import com.e_commerce.miscroservice.order.dao.OrderDao;
import com.e_commerce.miscroservice.order.dao.OrderRelationshipDao;
import com.e_commerce.miscroservice.order.vo.DetailOrderReturnView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单模块的公共订单
 * 订单信息
 */
@Component
public class OrderCommonController extends BaseController {


	@Autowired
	private OrderRelationshipDao orderRelationshipDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private EvaluateDao evaluateDao;

	/**
	 * 根据service派生订单 供其他模块调用
	 * @param service 商品
	 * @param type 派生的类型
	 * @param date 派生的日期
	 * @return
	 */
	public MsgResult produceOrder(TService service, Integer type, String date) {
//		logger.error("开始为serviceId为{}的商品派生订单>>>>>>", service.getId());
		//根据service生成出订单的属性
		MsgResult result = new MsgResult();
		try {
			orderService.produceOrder(service, type, date);
			result.setCode("200");
			result.setMessage("生成订单成功");
			return result;
		} catch (MessageException e) {
			result.setCode(e.getErrorCode());
			result.setMessage(e.getMessage());
			return result;
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage("生成订单错误");
			return result;
		}
	}

	/**
	 * 报名满人或者从满人的订单取消改变订单的可见和不可见状态
	 * @param orderId 订单ID
	 * @param type 类型
	 */
	public void changeOrderVisiableStatus(Long orderId, Integer type) {
		orderService.changeOrderVisiableStatus(orderId, type);
	}

    /**
     * 插入订单，供其他模块调用
     * @author 马晓晨
     * @return
     */
    public int saveOrder(TOrder order) {
        return orderService.saveOrder(order);
    }


    /**
     * @return int
     * @Author 姜修弘
     * 功能描述:插入订单
     * 创建时间:@Date 下午6:38 2019/3/6
     * @Param [orderRelationship]
     **/
    public int insertOrderRelationship(TOrderRelationship orderRelationship) {
        return orderRelationshipDao.insert(orderRelationship);
    }

    /**
     * @return int
     * @Author 姜修弘
     * 功能描述:更新订单
     * 创建时间:@Date 下午6:39 2019/3/6
     * @Param [orderRelationship]
     **/
    public int updateOrderRelationship(TOrderRelationship orderRelationship) {
        return orderRelationshipDao.updateByPrimaryKey(orderRelationship);
    }

    /**
     * @return com.e_commerce.miscroservice.commons.entity.application.TOrderRelationship
     * @Author 姜修弘
     * 功能描述:获取指定用户、订单的订单关系
     * 创建时间:@Date 下午6:39 2019/3/6
     * @Param [userId, orderId]
     **/
    public TOrderRelationship selectOrdertionshipByuserIdAndOrderId(Long userId, Long orderId) {
        return orderRelationshipDao.selectByOrderIdAndUserId(orderId, userId);
    }

    /**
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.TOrderRelationship>
     * @Author 姜修弘
     * 功能描述:获取指定用户所有的订单关系
     * 创建时间:@Date 下午6:39 2019/3/6
     * @Param [userId]
     **/
    public List<TOrderRelationship> selectOrdertionshipListByuserId(Long userId) {
        return orderRelationshipDao.selectByUserId(userId);
    }

    /**
     * 根据用户id获取订单列表
     * @param userId
     * @return
     */
    public List<TOrder> selectOdersByUserId(Long userId,boolean isService) {
        return orderDao.selectPublishedByUserId(userId,isService);
    }

    /**
     * 查询指定用户id过往订单记录
     * @param userId
     * @return
     */
    public List<TOrder> selectEndOrdersByUserId(Long userId) {
        return  orderDao.selectPastByUserId(userId);
    }

    /**
     * 查询指定订单Id集合的评价记录
     * @param orderIds
     * @return
     */
    public List<TEvaluate> selectEvaluateInOrderIds(List orderIds) {
        return evaluateDao.selectEvaluateInOrderIds(orderIds);
    }

    /**
     * 同步商品和订单的状态
     * @param productId 商品ID
     * @param status  要同步成的状态
     * @return 是否成功同步
     */
    public boolean synOrderServiceStatus(Long productId, Integer status) {
        try {
            orderService.synOrderServiceStatus(productId, status);
        } catch (Exception e) {
            logger.error("下架商品的订单错误");
            return false;
        }
        return true;
    }
    /**
     * 根据订单id集合和用户id返回评价记录
     * @param orderIds
     * @param userId
     * @return
     */
    public List<TEvaluate> selectEvaluateInOrderIdsAndByUserId(List orderIds,Long userId) {
        return  evaluateDao.selectEvaluateInOrderIdsAndByUserId(orderIds,userId);
    }

    /**
     * 根据订单id集合返回订单记录
     * @param orderIds
     * @return
     */
    public List<TOrder> selectOrdersInOrderIds(List orderIds) {
        return orderDao.selectOrdersInOrderIds(orderIds);
    }


    /**
     * 获取指定接单者的订单记录
     * @return
     */
    public List<TOrderRelationship> selectOrderrelationshipListByReceiptUserId(Long userId) {
        return orderRelationshipDao.selectOrderRelationshipByReceiptUserId(userId);
    }

    /**
     * 获取收藏列表
     * @param id
     * @return
     */
    public List<TOrderRelationship> selectCollectList(Long id) {
        return orderRelationshipDao.selectCollectList(id);
    }

    /**
     * 获取指定id，指定状态的所有订单记录
     * @param idList
     * @param collectionAvailableStatusArray
     * @return
     */
    public List<TOrder> selectOrdersInOrderIdsInStatus(List<Long> idList, Integer[] collectionAvailableStatusArray) {
        return orderRelationshipDao.selectOrdersInOrderIdsInStatus(idList,collectionAvailableStatusArray);
    }

    /**
     * 根据id查找订单
     * @param orderRelationshipId
     * @return
     */
    public TOrderRelationship selectOrderById(Long orderRelationshipId) {
        return orderRelationshipDao.selectByPrimaryKey(orderRelationshipId);
    }

    /**
     * 根据订单关系id、收藏状态更新收藏状态
     * @param orderRelationshipId
     * @param collectStatus
     */
    public int updateCollectStatus(Long orderRelationshipId, int collectStatus) {
        return orderRelationshipDao.updateCollectStatus(orderRelationshipId,collectStatus);
    }

	/**
	 * 订单详情
	 * @param orderId 订单ID
	 * @param token 用户token
	 * @return
	 */
	public DetailOrderReturnView detailIndexOrder(Long orderId, String token) {
		TUser user = (TUser) redisUtil.get(token);
		return orderService.orderDetail(orderId, user);
	}

	/**
	 * 为了模糊查询，用户修改名称同步订单表中创建人的名称
	 * @param userId
	 * @param userName
	 */
	public void synOrderCreateUserName(Long userId, String userName) {
		orderService.synOrderCreateUserName(userId, userName);
	}
}
