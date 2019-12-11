package com.e_commerce.miscroservice.sdx_proj.controller;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxThuService;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxThuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* 动态点赞的操作接口
*/
@RestController
@RequestMapping("tSdxThu")
public class TSdxThuController {
    @Autowired
    private TSdxThuService tSdxThuService;
    
    /**
    * 添加或者修改动态点赞 
    *
    * @param id        动态点赞的Id,修改或者查询的需要
    * @param type        点赞类型
    * @param userId        用户ID
    * @param trendsId        动态ID
    * @param typeName        类型名
    * @param userName        用户名 
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
    @Consume(TSdxThuVo.class)
    public Response modTSdxThu(@RequestParam(required = false) Long id,@RequestParam(required = false) Integer type,@RequestParam(required = false) Integer userId,@RequestParam(required = false) Integer trendsId,@RequestParam(required = false) String typeName,@RequestParam(required = false) String userName) {
        TSdxThuVo tSdxThuVo = (TSdxThuVo) ConsumeHelper.getObj();
        if (tSdxThuVo == null) {
            return Response.fail();
        }
        return Response.success(tSdxThuService.modTSdxThu(tSdxThuVo.copyTSdxThuPo()));
    }
    
    /**
    * 删除动态点赞 
    *
    * @param ids 动态点赞的Id的集合,例如1,2,3多个用英文,隔开 
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
    public Response delTSdxThu(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return Response.fail();
        }
        return Response.success(tSdxThuService.delTSdxThuByIds(ids));
    }
    
    /**
    * 查找动态点赞 
    *
    * @param page 页数默认第一页
    * @param size 每页返回的数量，默认10个
    * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
    * @param id        动态点赞的Id,修改或者查询的需要
    * @param type        点赞类型
    * @param userId        用户ID
    * @param trendsId        动态ID
    * @param typeName        类型名
    * @param userName        用户名 
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
    @Consume(TSdxThuVo.class)
    public Response findTSdxThu(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size,@RequestParam(required = false) String openResponseExplainFlag,@RequestParam(required = false) Long id,@RequestParam(required = false) Integer type,@RequestParam(required = false) Integer userId,@RequestParam(required = false) Integer trendsId,@RequestParam(required = false) String typeName,@RequestParam(required = false) String userName) {
        
    TSdxThuVo tSdxThuVo = (TSdxThuVo) ConsumeHelper.getObj();
        if (tSdxThuVo == null) {
            return Response.fail();
        }
        if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
            return Response.success(JavaDocReader.read(TSdxThuVo.class));
        }
        if(tSdxThuVo.getId()!=null){
            return Response.success(tSdxThuService.findTSdxThuById(tSdxThuVo.getId()));
        }
        return Response.success(tSdxThuService.findTSdxThuByAll(tSdxThuVo.copyTSdxThuPo (),page,size), IdUtil.getTotal());
    }
}
