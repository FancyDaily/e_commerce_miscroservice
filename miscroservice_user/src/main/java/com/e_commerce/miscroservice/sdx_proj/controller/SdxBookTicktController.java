package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookTicktService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookTicktVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* 预定书券
*/
@RestController
@RequestMapping("sdxBookTickt")
public class SdxBookTicktController {
    @Autowired
    private SdxBookTicktService sdxBookTicktService;

    /**
    * 添加或者修改预定书券
    *
    * @param id        预定书券的Id,修改或者查询的需要
    * @param expire        过期时间点
    * @param userId        拥有者用户编号
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
    @Consume(TSdxBookTicktVo.class)
    public Response modTSdxBookTickt(@RequestParam(required = false) Long id,@RequestParam(required = false) Long expire,@RequestParam(required = false) Long userId) {
        TSdxBookTicktVo tSdxBookTicktVo = (TSdxBookTicktVo) ConsumeHelper.getObj();
        if (tSdxBookTicktVo == null) {
            return Response.fail();
        }
        return Response.success(sdxBookTicktService.modTSdxBookTickt(tSdxBookTicktVo.copyTSdxBookTicktPo()));
    }

    /**
    * 删除预定书券
    *
    * @param ids 预定书券的Id的集合,例如1,2,3多个用英文,隔开
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
    public Response delTSdxBookTickt(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return Response.fail();
        }
        return Response.success(sdxBookTicktService.delTSdxBookTicktByIds(ids));
    }

    /**
    * 查找预定书券
    *
    * @param page 页数默认第一页
    * @param size 每页返回的数量，默认10个
    * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
    * @param id        预定书券的Id,修改或者查询的需要
    * @param expire        过期时间点
    * @param userId        拥有者用户编号
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
    @Consume(TSdxBookTicktVo.class)
    public Response findTSdxBookTickt(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size,@RequestParam(required = false) String openResponseExplainFlag,@RequestParam(required = false) Long id,@RequestParam(required = false) Long expire,@RequestParam(required = false) Long userId) {

    TSdxBookTicktVo tSdxBookTicktVo = (TSdxBookTicktVo) ConsumeHelper.getObj();
        if (tSdxBookTicktVo == null) {
            return Response.fail();
        }
        if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
            return Response.success(JavaDocReader.read(TSdxBookTicktVo.class));
        }
        if(tSdxBookTicktVo.getId()!=null){
            return Response.success(sdxBookTicktService.findTSdxBookTicktById(tSdxBookTicktVo.getId()));
        }
        return Response.success(sdxBookTicktService.findTSdxBookTicktByAll(tSdxBookTicktVo.copyTSdxBookTicktPo (),page,size), IdUtil.getTotal());
    }
}
