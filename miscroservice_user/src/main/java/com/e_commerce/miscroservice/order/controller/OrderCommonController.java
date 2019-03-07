package com.e_commerce.miscroservice.order.controller;

import com.e_commerce.miscroservice.commons.entity.application.TEvaluate;
import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.BeanUtil;
import com.e_commerce.miscroservice.order.dao.EvaluateDao;
import com.e_commerce.miscroservice.order.dao.OrderDao;
import com.e_commerce.miscroservice.order.dao.OrderRelationshipDao;
import com.e_commerce.miscroservice.order.po.TOrder;
import com.e_commerce.miscroservice.order.po.TOrderRelationship;
import com.e_commerce.miscroservice.product.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
     *
     * @param service 商品
     */
    public boolean produceOrder(TService service) {
        logger.info("开始为serviceId为{}的商品派生订单>>>>>>", service.getId());
        //根据service生成出订单的属性
        TOrder order = BeanUtil.copy(service, TOrder.class);
        order.setId(snowflakeIdWorker.nextId());
        order.setConfirmNum(0);
        order.setEnrollNum(0);
        order.setServiceId(service.getId());
        //重复的订单的话  根据商品的重复时间生成第一张订单
        if (service.getTimeType().equals(ProductEnum.TIME_TYPE_REPEAT.getValue())) {
            //TODO 将int类型的星期几改为字符串型  逗号分隔数字  1是星期一  7是星期日
            String[] weekDayArray = service.getDateWeek().split(",");
            int[] WeekDayNumberArray = getIntArray(weekDayArray);
            //对星期进行升序排序
            Arrays.sort(WeekDayNumberArray);
            //获取商品开始时间的字符串形式 201803051434
            String serviceStartTimeString = service.getStartDateS() + service.getStartTimeS();
            //获取商品开始的时间是星期X
            int startWeekDay = DateUtil.getWeekDay(serviceStartTimeString);
            //订单开始的星期X
            int orderWeekDay = 0;
            //订单开始的下一个星期
            int orderNextWeekDay = 0;
            //获取最近可以发布的周
            for (int i = 0; i < WeekDayNumberArray.length; i++) {
                if (startWeekDay >= WeekDayNumberArray[i]) {
                    orderWeekDay = WeekDayNumberArray[i];
                    if (i + 1 >= WeekDayNumberArray.length) {
                        orderNextWeekDay = WeekDayNumberArray[0];
                    } else {
                        orderNextWeekDay = WeekDayNumberArray[i + 1];
                    }
                }
            }
            //需要增加的天数
            int addDays = orderWeekDay - startWeekDay;
            //订单开始的时间戳
            Long startTimestamp = DateUtil.parse(serviceStartTimeString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(startTimestamp));
            cal.add(Calendar.DAY_OF_YEAR, addDays);
            //第一天的结束时间
            String currentDayEndTime = service.getStartDateS() + service.getEndTimeS();
            //如果重复中包含当天的订单，查看结束时间是否小于当前时间，小于当前时间就是已经过了今天的，直接发下一个星期X的
            if (addDays == 0 && DateUtil.parse(currentDayEndTime) < System.currentTimeMillis()) {
                addDays = orderNextWeekDay - startWeekDay;
                cal.add(Calendar.DAY_OF_YEAR, addDays);
            }
            String endDateTime = service.getEndDateS() + service.getEndTimeS();
            if (DateUtil.parse(endDateTime) < cal.getTimeInMillis()) {
                throw new MessageException("订单生成超时，请修改时间后重新发布");
            }
            //订单开始的日期
            String orderStartDate = DateUtil.format(cal.getTimeInMillis()).substring(0, 8);
            //订单开始时间 = 订单开始的日期 + 商品的开始时间
            order.setStartTime(DateUtil.parse(orderStartDate + service.getStartTimeS()));
            //订单结束时间 = 订单开始的日期 + 商品的结束时间
            order.setEndTime(DateUtil.parse(orderStartDate + service.getEndTimeS()));
            try {
                orderService.saveOrder(order);
            } catch (Exception e) {
                logger.error(errInfo(e), e);
                return false;
            }
        }
        return true;
    }

    /**
     * 插入订单，供其他模块调用
     *
     * @return
     */
    public int saveOrder(TOrder order) {
        return orderService.saveOrder(order);
    }

    /**
     * 将字符串数组转换为int数组
     *
     * @param weekDayArray 字符串数值数组
     * @return int数组
     */
    private int[] getIntArray(String[] weekDayArray) {
        int[] WeekDayNumberArray = new int[weekDayArray.length];
        for (int i = 0; i < weekDayArray.length; i++) {
            Integer weekDay = Integer.parseInt(weekDayArray[i]);
            WeekDayNumberArray[i] = weekDay;
        }
        return WeekDayNumberArray;
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

}
