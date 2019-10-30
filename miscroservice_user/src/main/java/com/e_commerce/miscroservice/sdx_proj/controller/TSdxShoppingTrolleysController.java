package com.e_commerce.miscroservice.sdx_proj.controller;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxShoppingTrolleysService;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxShoppingTrolleysVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* 购物车的操作接口
*/
@RestController
@RequestMapping("tSdxShoppingTrolleys")
public class TSdxShoppingTrolleysController {
    @Autowired
    private TSdxShoppingTrolleysService tSdxShoppingTrolleysService;
    
    /**
    * 添加或者修改购物车 
    *
    * @param id        购物车的Id,修改或者查询的需要
    * @param userId        用户编号
    * @param bookInfoId        书籍信息编号 
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
    @Consume(TSdxShoppingTrolleysVo.class)
    public Response modTSdxShoppingTrolleys(@RequestParam(required = false) Long id,@RequestParam(required = false) Long userId,@RequestParam(required = false) Long bookInfoId) {
        TSdxShoppingTrolleysVo tSdxShoppingTrolleysVo = (TSdxShoppingTrolleysVo) ConsumeHelper.getObj();
        if (tSdxShoppingTrolleysVo == null) {
            return Response.fail();
        }
        return Response.success(tSdxShoppingTrolleysService.modTSdxShoppingTrolleys(tSdxShoppingTrolleysVo.copyTSdxShoppingTrolleysPo()));
    }
    
    /**
    * 删除购物车 
    *
    * @param ids 购物车的Id的集合,例如1,2,3多个用英文,隔开 
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
    public Response delTSdxShoppingTrolleys(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return Response.fail();
        }
        return Response.success(tSdxShoppingTrolleysService.delTSdxShoppingTrolleysByIds(ids));
    }
    
    /**
    * 查找购物车 
    *
    * @param page 页数默认第一页
    * @param size 每页返回的数量，默认10个
    * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
    * @param id        购物车的Id,修改或者查询的需要
    * @param userId        用户编号
    * @param bookInfoId        书籍信息编号 
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
    @Consume(TSdxShoppingTrolleysVo.class)
    public Response findTSdxShoppingTrolleys(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size,@RequestParam(required = false) String openResponseExplainFlag,@RequestParam(required = false) Long id,@RequestParam(required = false) Long userId,@RequestParam(required = false) Long bookInfoId) {
        
    TSdxShoppingTrolleysVo tSdxShoppingTrolleysVo = (TSdxShoppingTrolleysVo) ConsumeHelper.getObj();
        if (tSdxShoppingTrolleysVo == null) {
            return Response.fail();
        }
        if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
            return Response.success(JavaDocReader.read(TSdxShoppingTrolleysVo.class));
        }
        if(tSdxShoppingTrolleysVo.getId()!=null){
            return Response.success(tSdxShoppingTrolleysService.findTSdxShoppingTrolleysById(tSdxShoppingTrolleysVo.getId()));
        }
        return Response.success(tSdxShoppingTrolleysService.findTSdxShoppingTrolleysByAll(tSdxShoppingTrolleysVo.copyTSdxShoppingTrolleysPo (),page,size), IdUtil.getTotal());
    }
}
