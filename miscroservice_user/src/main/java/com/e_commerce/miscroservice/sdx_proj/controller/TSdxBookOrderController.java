package com.e_commerce.miscroservice.sdx_proj.controller;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxBookOrderService;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* 订单的操作接口
*/
@RestController
@RequestMapping("tSdxBookOrder")
public class TSdxBookOrderController {
    @Autowired
    private TSdxBookOrderService tSdxBookOrderService;
    
    /**
    * 添加或者修改订单 
    *
    * @param id        订单的Id,修改或者查询的需要
    * @param type        订单类型(捐书、购书)
    * @param price        总价
    * @param status        状态
    * @param bookIds        多个书本编号
    * @param shipType        配送类型(邮寄、自送）
    * @param bookPrice        书本总价
    * @param shipPirce        运费
    * @param totalPrice        订单总价
    * @param bookInfoIds        书本信息编号
    * @param bookStationId        书籍驿站编号
    * @param scoreDiscount        积分抵扣总额
    * @param exactTotalScores        实际获得积分
    * @param shippingAddressId        邮寄地址编号
    * @param expectedTotalScores        预计获得总积分 
    *
    *                 code==503,代表服务器出错,请先检测参数类型是否正确
    *                 code==500,代表参数不正确
    *                 code==200,代表请求成功
    *                 data==0,代表操作不成功
    *                 data!=0,新增时候data返回的是自增id,修改的时候代表影响的数量
    *
    * @return
    */
    @PostMapping("mod")
    @Consume(TSdxBookOrderVo.class)
    public Response modTSdxBookOrder(@RequestParam(required = false) Long id,@RequestParam(required = false) Integer type,@RequestParam(required = false) Double price,@RequestParam(required = false) Integer status,@RequestParam(required = false) String bookIds,@RequestParam(required = false) Integer shipType,@RequestParam(required = false) Double bookPrice,@RequestParam(required = false) Double shipPirce,@RequestParam(required = false) Double totalPrice,@RequestParam(required = false) Long bookInfoIds,@RequestParam(required = false) Long bookStationId,@RequestParam(required = false) Integer scoreDiscount,@RequestParam(required = false) Integer exactTotalScores,@RequestParam(required = false) Long shippingAddressId,@RequestParam(required = false) Integer expectedTotalScores) {
        TSdxBookOrderVo tSdxBookOrderVo = (TSdxBookOrderVo) ConsumeHelper.getObj();
        if (tSdxBookOrderVo == null) {
            return Response.fail();
        }
        return Response.success(tSdxBookOrderService.modTSdxBookOrder(tSdxBookOrderVo.copyTSdxBookOrderPo()));
    }
    
    /**
    * 删除订单 
    *
    * @param ids 订单的Id的集合,例如1,2,3多个用英文,隔开 
    *
    *                 code==503,代表服务器出错,请先检测参数类型是否正确
    *                 code==500,代表参数不正确
    *                 code==200,代表请求成功
    *                 data==0,代表操作不成功
    *                 data!=0,代表影响的数量
    *    
    * @return
    */
    @RequestMapping("del")
    public Response delTSdxBookOrder(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return Response.fail();
        }
        return Response.success(tSdxBookOrderService.delTSdxBookOrderByIds(ids));
    }
    
    /**
    * 查找订单 
    *
    * @param page 页数默认第一页
    * @param size 每页返回的数量，默认10个
    * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
    * @param id        订单的Id,修改或者查询的需要
    * @param type        订单类型(捐书、购书)
    * @param price        总价
    * @param status        状态
    * @param bookIds        多个书本编号
    * @param shipType        配送类型(邮寄、自送）
    * @param bookPrice        书本总价
    * @param shipPirce        运费
    * @param totalPrice        订单总价
    * @param bookInfoIds        书本信息编号
    * @param bookStationId        书籍驿站编号
    * @param scoreDiscount        积分抵扣总额
    * @param exactTotalScores        实际获得积分
    * @param shippingAddressId        邮寄地址编号
    * @param expectedTotalScores        预计获得总积分 
    *
    *                 code==503,代表服务器出错,请先检测参数类型是否正确
    *                 code==500,代表参数不正确
    *                 code==200,代表请求成功
    *                 count!=0,代表当前查询条件总的数量
    *                 data==0,代表操作不成功
    *                 data!=0,代表影响的数量
    *
    * @return
    */
    @RequestMapping("find")
    @Consume(TSdxBookOrderVo.class)
    public Response findTSdxBookOrder(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size,@RequestParam(required = false) String openResponseExplainFlag,@RequestParam(required = false) Long id,@RequestParam(required = false) Integer type,@RequestParam(required = false) Double price,@RequestParam(required = false) Integer status,@RequestParam(required = false) String bookIds,@RequestParam(required = false) Integer shipType,@RequestParam(required = false) Double bookPrice,@RequestParam(required = false) Double shipPirce,@RequestParam(required = false) Double totalPrice,@RequestParam(required = false) Long bookInfoIds,@RequestParam(required = false) Long bookStationId,@RequestParam(required = false) Integer scoreDiscount,@RequestParam(required = false) Integer exactTotalScores,@RequestParam(required = false) Long shippingAddressId,@RequestParam(required = false) Integer expectedTotalScores) {
        
    TSdxBookOrderVo tSdxBookOrderVo = (TSdxBookOrderVo) ConsumeHelper.getObj();
        if (tSdxBookOrderVo == null) {
            return Response.fail();
        }
        if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
            return Response.success(JavaDocReader.read(TSdxBookOrderVo.class));
        }
        if(tSdxBookOrderVo.getId()!=null){
            return Response.success(tSdxBookOrderService.findTSdxBookOrderById(tSdxBookOrderVo.getId()));
        }
        return Response.success(tSdxBookOrderService.findTSdxBookOrderByAll(tSdxBookOrderVo.copyTSdxBookOrderPo (),page,size), IdUtil.getTotal());
    }
}
