package com.e_commerce.miscroservice.sdx_proj.controller;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookStationService;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookStationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* 书籍回收驿站的操作接口
*/
@RestController
@RequestMapping("tSdxBookStation")
public class TSdxBookStationController {
    @Autowired
    private SdxBookStationService sdxBookStationService;

    /**
    * 添加或者修改书籍回收驿站
    *
    * @param id        书籍回收驿站的Id,修改或者查询的需要
    * @param name        驿站名字
    * @param address        地址
    * @param latitude        纬度
    * @param longitude        经度
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
    @Consume(TSdxBookStationVo.class)
    public Response modTSdxBookStation(@RequestParam(required = false) Long id,@RequestParam(required = false) String name,@RequestParam(required = false) String address,@RequestParam(required = false) Double latitude,@RequestParam(required = false) Double longitude) {
        TSdxBookStationVo tSdxBookStationVo = (TSdxBookStationVo) ConsumeHelper.getObj();
        if (tSdxBookStationVo == null) {
            return Response.fail();
        }
        return Response.success(sdxBookStationService.modTSdxBookStation(tSdxBookStationVo.copyTSdxBookStationPo()));
    }

    /**
    * 删除书籍回收驿站
    *
    * @param ids 书籍回收驿站的Id的集合,例如1,2,3多个用英文,隔开
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
    public Response delTSdxBookStation(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return Response.fail();
        }
        return Response.success(sdxBookStationService.delTSdxBookStationByIds(ids));
    }

    /**
    * 查找书籍回收驿站
    *
    * @param page 页数默认第一页
    * @param size 每页返回的数量，默认10个
    * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
    * @param id        书籍回收驿站的Id,修改或者查询的需要
    * @param name        驿站名字
    * @param address        地址
    * @param latitude        纬度
    * @param longitude        经度
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
    @Consume(TSdxBookStationVo.class)
    public Response findTSdxBookStation(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size,@RequestParam(required = false) String openResponseExplainFlag,@RequestParam(required = false) Long id,@RequestParam(required = false) String name,@RequestParam(required = false) String address,@RequestParam(required = false) Double latitude,@RequestParam(required = false) Double longitude) {

    TSdxBookStationVo tSdxBookStationVo = (TSdxBookStationVo) ConsumeHelper.getObj();
        if (tSdxBookStationVo == null) {
            return Response.fail();
        }
        if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
            return Response.success(JavaDocReader.read(TSdxBookStationVo.class));
        }
        if(tSdxBookStationVo.getId()!=null){
            return Response.success(sdxBookStationService.findTSdxBookStationById(tSdxBookStationVo.getId()));
        }
        return Response.success(sdxBookStationService.findTSdxBookStationByAll(tSdxBookStationVo.copyTSdxBookStationPo (),page,size), IdUtil.getTotal());
    }
}
