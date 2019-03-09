package com.e_commerce.miscroservice.order.controller;

import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.order.dao.OrderRelationshipDao;
import com.e_commerce.miscroservice.order.po.TOrderRelationship;
import com.e_commerce.miscroservice.order.service.OrderRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 功能描述:
 */
//@RestController
@RequestMapping("/api/v2/orderRelation")
public class OrderRelationController {

    @Autowired
    private OrderRelationService orderRelationService;

    @Autowired
    private OrderRelationshipDao orderRelationshipDao;

    @RequestMapping("/id")
    public Object notices(Long orderId, long userId , String date, Long serviceId , Long orderRelationServiceId) {
        AjaxResult result = new AjaxResult();
        try {
            long msg = orderRelationService.enroll(orderId , userId , date , serviceId ,orderRelationServiceId);
            result.setSuccess(true);
            result.setMsg("查看成功");
            result.setData(msg);;
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查看失败");
            e.printStackTrace();
            return result;
        }
    }
    @RequestMapping("/test")
    public Object notices(Long orderRelationshipId, long userId) {
        AjaxResult result = new AjaxResult();
        try {
            String msg = orderRelationService.test(userId , orderRelationshipId);
            result.setSuccess(true);
            result.setMsg("查看成功");
            result.setData(msg);;
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("失败");
            e.printStackTrace();
            return result;
        }
    }

    /**
     * @Author 姜修弘
     * 功能描述:插入订单
     * 创建时间:@Date 下午6:38 2019/3/6
     * @Param [orderRelationship]
     * @return int
     **/
    public int insertOrderRelationship(TOrderRelationship orderRelationship){return orderRelationshipDao.insert(orderRelationship);}

    /**
     * @Author 姜修弘
     * 功能描述:更新订单
     * 创建时间:@Date 下午6:39 2019/3/6
     * @Param [orderRelationship]
     * @return int
     **/
    public int updateOrderRelationship(TOrderRelationship orderRelationship){return  orderRelationshipDao.updateByPrimaryKey(orderRelationship);}

    /**
     * @Author 姜修弘
     * 功能描述:获取指定用户、订单的订单关系
     * 创建时间:@Date 下午6:39 2019/3/6
     * @Param [userId, orderId]
     * @return com.e_commerce.miscroservice.commons.entity.application.TOrderRelationship
     **/
    public TOrderRelationship selectOrdertionshipByuserIdAndOrderId(Long userId , Long orderId){return orderRelationshipDao.selectByOrderIdAndUserId(orderId,userId);}

    /**
     * @Author 姜修弘
     * 功能描述:获取指定用户所有的订单关系
     * 创建时间:@Date 下午6:39 2019/3/6
     * @Param [userId]
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.TOrderRelationship>
     **/
    public List<TOrderRelationship> selectOrdertionshipListByuserId(Long userId){return  orderRelationshipDao.selectByUserId(userId);}
}
