package com.e_commerce.miscroservice.sdx_proj.controller;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookAfterReadingNoteUserService;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* 书籍读后感用户相关的操作接口
*/
@RestController
@RequestMapping("tSdxBookAfterReadingNoteUser")
public class TSdxBookAfterReadingNoteUserController {
    @Autowired
    private SdxBookAfterReadingNoteUserService sdxBookAfterReadingNoteUserService;

    /**
    * 添加或者修改书籍读后感用户相关
    *
    * @param id        书籍读后感用户相关的Id,修改或者查询的需要
    * @param type        类型(1.点赞与购买2.留言)
    * @param bookId        书籍编号
    * @param userId        用户编号
    * @param isThumb        是否点赞
    * @param bookInfoId        书籍信息编号
    * @param isPurchase        是否购买
    * @param bookAfterReadingNoteId        读后感编号
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
    @Consume(TSdxBookAfterReadingNoteUserVo.class)
    public Response modTSdxBookAfterReadingNoteUser(@RequestParam(required = false) Long id,@RequestParam(required = false) Integer type,@RequestParam(required = false) Long bookId,@RequestParam(required = false) Long userId,@RequestParam(required = false) Integer isThumb,@RequestParam(required = false) Long bookInfoId,@RequestParam(required = false) Integer isPurchase,@RequestParam(required = false) Long bookAfterReadingNoteId) {
        TSdxBookAfterReadingNoteUserVo tSdxBookAfterReadingNoteUserVo = (TSdxBookAfterReadingNoteUserVo) ConsumeHelper.getObj();
        if (tSdxBookAfterReadingNoteUserVo == null) {
            return Response.fail();
        }
        return Response.success(sdxBookAfterReadingNoteUserService.modTSdxBookAfterReadingNoteUser(tSdxBookAfterReadingNoteUserVo.copyTSdxBookAfterReadingNoteUserPo()));
    }

    /**
    * 删除书籍读后感用户相关
    *
    * @param ids 书籍读后感用户相关的Id的集合,例如1,2,3多个用英文,隔开
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
    public Response delTSdxBookAfterReadingNoteUser(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return Response.fail();
        }
        return Response.success(sdxBookAfterReadingNoteUserService.delTSdxBookAfterReadingNoteUserByIds(ids));
    }

    /**
    * 查找书籍读后感用户相关
    *
    * @param page 页数默认第一页
    * @param size 每页返回的数量，默认10个
    * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
    * @param id        书籍读后感用户相关的Id,修改或者查询的需要
    * @param type        类型(1.点赞与购买2.留言)
    * @param bookId        书籍编号
    * @param userId        用户编号
    * @param isThumb        是否点赞
    * @param bookInfoId        书籍信息编号
    * @param isPurchase        是否购买
    * @param bookAfterReadingNoteId        读后感编号
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
    @Consume(TSdxBookAfterReadingNoteUserVo.class)
    public Response findTSdxBookAfterReadingNoteUser(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size,@RequestParam(required = false) String openResponseExplainFlag,@RequestParam(required = false) Long id,@RequestParam(required = false) Integer type,@RequestParam(required = false) Long bookId,@RequestParam(required = false) Long userId,@RequestParam(required = false) Integer isThumb,@RequestParam(required = false) Long bookInfoId,@RequestParam(required = false) Integer isPurchase,@RequestParam(required = false) Long bookAfterReadingNoteId) {

    TSdxBookAfterReadingNoteUserVo tSdxBookAfterReadingNoteUserVo = (TSdxBookAfterReadingNoteUserVo) ConsumeHelper.getObj();
        if (tSdxBookAfterReadingNoteUserVo == null) {
            return Response.fail();
        }
        if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
            return Response.success(JavaDocReader.read(TSdxBookAfterReadingNoteUserVo.class));
        }
        if(tSdxBookAfterReadingNoteUserVo.getId()!=null){
            return Response.success(sdxBookAfterReadingNoteUserService.findTSdxBookAfterReadingNoteUserById(tSdxBookAfterReadingNoteUserVo.getId()));
        }
        return Response.success(sdxBookAfterReadingNoteUserService.findTSdxBookAfterReadingNoteUserByAll(tSdxBookAfterReadingNoteUserVo.copyTSdxBookAfterReadingNoteUserPo (),page,size), IdUtil.getTotal());
    }
}
