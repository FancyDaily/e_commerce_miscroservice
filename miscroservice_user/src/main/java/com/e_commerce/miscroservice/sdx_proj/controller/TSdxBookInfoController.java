package com.e_commerce.miscroservice.sdx_proj.controller;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookInfoService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* 书籍信息的操作接口
*/
@RestController
@RequestMapping("tSdxBookInfo")
public class TSdxBookInfoController {
    @Autowired
    private SdxBookInfoService sdxBookInfoService;

    /**
    * 添加或者修改书籍信息
    *
    * @param id        书籍信息的Id,修改或者查询的需要
    * @param name        书名
    * @param press        出版社
    * @param price        价格
    * @param author        作者
    * @param catalog        目录
    * @param soldNum        销量
    * @param introPic        图片
    * @param categoryId        类型编号
    * @param scoreDouban        豆瓣评分
    * @param bindingStyle        装帧风格
    * @param categoryName        类型名
    * @param introduction        简介
    * @param maximumReserve        最大预购接收数
    * @param maximumDiscount        最高可抵扣价格
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
    @Consume(TSdxBookInfoVo.class)
    public Response modTSdxBookInfo(@RequestParam(required = false) Long id,@RequestParam(required = false) String name,@RequestParam(required = false) String press,@RequestParam(required = false) Double price,@RequestParam(required = false) String author,@RequestParam(required = false) String catalog,@RequestParam(required = false) Integer soldNum,@RequestParam(required = false) String introPic,@RequestParam(required = false) Integer categoryId,@RequestParam(required = false) Double scoreDouban,@RequestParam(required = false) String bindingStyle,@RequestParam(required = false) String categoryName,@RequestParam(required = false) String introduction,@RequestParam(required = false) Integer maximumReserve,@RequestParam(required = false) Double maximumDiscount) {
        TSdxBookInfoVo tSdxBookInfoVo = (TSdxBookInfoVo) ConsumeHelper.getObj();
        if (tSdxBookInfoVo == null) {
            return Response.fail();
        }
        return Response.success(sdxBookInfoService.modTSdxBookInfo(tSdxBookInfoVo.copyTSdxBookInfoPo()));
    }

    /**
    * 删除书籍信息
    *
    * @param ids 书籍信息的Id的集合,例如1,2,3多个用英文,隔开
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
    public Response delTSdxBookInfo(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return Response.fail();
        }
        return Response.success(sdxBookInfoService.delTSdxBookInfoByIds(ids));
    }

   /**
    * 查找书籍信息
    *
    * @param page 页数默认第一页
    * @param size 每页返回的数量，默认10个
    * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
    * @param id        书籍信息的Id,修改或者查询的需要
    * @param name        书名
    * @param publisher        出版社
    * @param price        价格
    * @param author        作者
    * @param catalog        目录
    * @param soldNum        销量
    * @param coverPic        图片
    * @param categoryId        类型编号
    * @param scoreDouban        豆瓣评分
    * @param bindingStyle        装帧风格
    * @param categoryName        类型名
    * @param introduction        简介
    * @param maximumReserve        最大预购接收数
    * @param maximumDiscount        最高可抵扣价格
    *
    *                 code==503,代表服务器出错,请先检测参数类型是否正确
    *                 code==500,代表参数不正确
    *                 code==200,代表请求成功
    *                 count!=0,代表当前查询条件总的数量
    *                 data==0,代表操作不成功
    *                 data!=0,代表影响的数量
    *
    * @return
    *//*
    @RequestMapping("find")
    @Consume(TSdxBookInfoVo.class)
    public Response findTSdxBookInfo(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size,@RequestParam(required = false) String openResponseExplainFlag,@RequestParam(required = false) Long id,@RequestParam(required = false) String name,@RequestParam(required = false) String publisher,@RequestParam(required = false) Double price,@RequestParam(required = false) String author,@RequestParam(required = false) String catalog,@RequestParam(required = false) Integer soldNum,@RequestParam(required = false) String coverPic,@RequestParam(required = false) Integer categoryId,@RequestParam(required = false) Double scoreDouban,@RequestParam(required = false) String bindingStyle,@RequestParam(required = false) String categoryName,@RequestParam(required = false) String introduction,@RequestParam(required = false) Integer maximumReserve,@RequestParam(required = false) Double maximumDiscount) {

    TSdxBookInfoVo tSdxBookInfoVo = (TSdxBookInfoVo) ConsumeHelper.getObj();
        if (tSdxBookInfoVo == null) {
            return Response.fail();
        }
        if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
            return Response.success(JavaDocReader.read(TSdxBookInfoVo.class));
        }
        if(tSdxBookInfoVo.getId()!=null){
            return Response.success(sdxBookInfoService.findTSdxBookInfoById(tSdxBookInfoVo.getId()));
        }
        return Response.success(sdxBookInfoService.findTSdxBookInfoByAll(tSdxBookInfoVo.copyTSdxBookInfoPo (),page,size), IdUtil.getTotal());
    }*/
}
