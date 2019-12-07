package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookTransRecordService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookTransRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* 书籍漂流记录的操作接口
*/
@RestController
@RequestMapping("sdxBookTransRecord")
public class SdxBookTransRecordController {
    @Autowired
    private SdxBookTransRecordService sdxBookTransRecordService;

    /**
    * 添加或者修改书籍漂流记录
    *
    * @param id        书籍漂流记录的Id,修改或者查询的需要
    * @param type        类型1.xxx成为主人2.xxx的读后感3.存放在书籍驿站多少天etc.
    * @param bookId        书籍编号
    * @param userId        书籍主人编号
    * @param bookInfoId        书籍信息编号
    * @param description        描述
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
    @Consume(TSdxBookTransRecordVo.class)
	@UrlAuth
    public Response modTSdxBookTransRecord(@RequestParam(required = false) Long id,@RequestParam(required = false) Integer type,@RequestParam(required = false) Long bookId,@RequestParam(required = false) Long userId,@RequestParam(required = false) Long bookInfoId,@RequestParam(required = false) String description) {
        TSdxBookTransRecordVo tSdxBookTransRecordVo = (TSdxBookTransRecordVo) ConsumeHelper.getObj();
        if (tSdxBookTransRecordVo == null) {
            return Response.fail();
        }
        return Response.success(sdxBookTransRecordService.modTSdxBookTransRecord(tSdxBookTransRecordVo.copyTSdxBookTransRecordPo()));
    }

    /**
    * 删除书籍漂流记录
    *
    * @param ids 书籍漂流记录的Id的集合,例如1,2,3多个用英文,隔开
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
	@UrlAuth
    public Response delTSdxBookTransRecord(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return Response.fail();
        }
        return Response.success(sdxBookTransRecordService.delTSdxBookTransRecordByIds(ids));
    }

    /**
    * 查找书籍漂流记录
    *
    * @param page 页数默认第一页
    * @param size 每页返回的数量，默认10个
    * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
    * @param id        书籍漂流记录的Id,修改或者查询的需要
    * @param type        类型1.xxx成为主人2.xxx的读后感3.存放在书籍驿站多少天etc.
    * @param bookId        书籍编号
    * @param userId        书籍主人编号
    * @param bookInfoId        书籍信息编号
    * @param description        描述
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
    @Consume(TSdxBookTransRecordVo.class)
	@UrlAuth
    public Response findTSdxBookTransRecord(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size,@RequestParam(required = false) String openResponseExplainFlag,@RequestParam(required = false) Long id,@RequestParam(required = false) Integer type,@RequestParam(required = false) Long bookId,@RequestParam(required = false) Long userId,@RequestParam(required = false) Long bookInfoId,@RequestParam(required = false) String description) {

    TSdxBookTransRecordVo tSdxBookTransRecordVo = (TSdxBookTransRecordVo) ConsumeHelper.getObj();
        if (tSdxBookTransRecordVo == null) {
            return Response.fail();
        }
        if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
            return Response.success(JavaDocReader.read(TSdxBookTransRecordVo.class));
        }
        if(tSdxBookTransRecordVo.getId()!=null){
            return Response.success(sdxBookTransRecordService.findTSdxBookTransRecordById(tSdxBookTransRecordVo.getId()));
        }
        return Response.success(sdxBookTransRecordService.findTSdxBookTransRecordByAll(tSdxBookTransRecordVo.copyTSdxBookTransRecordPo (),page,size), IdUtil.getTotal());
    }
}
