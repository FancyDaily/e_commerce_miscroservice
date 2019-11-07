package com.e_commerce.miscroservice.sdx_proj.controller;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookAfterReadingNoteService;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* 书籍读后感的操作接口
*/
@RestController
@RequestMapping("tSdxBookAfterReadingNote")
public class TSdxBookAfterReadingNoteController {
    @Autowired
    private SdxBookAfterReadingNoteService sdxBookAfterReadingNoteService;

    /**
    * 添加或者修改书籍读后感
    *
    * @param id        书籍读后感的Id,修改或者查询的需要
    * @param bookId        书本编号
    * @param userId        创作者编号
    * @param bookInfoId        书籍信息编号
    * @param thumbUpNum        点赞数量
    * @param thumbDownNum        点踩数量
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
    @Consume(TSdxBookAfterReadingNoteVo.class)
    public Response modTSdxBookAfterReadingNote(@RequestParam(required = false) Long id,@RequestParam(required = false) Long bookId,@RequestParam(required = false) Long userId,@RequestParam(required = false) Long bookInfoId,@RequestParam(required = false) Integer thumbUpNum,@RequestParam(required = false) Integer thumbDownNum) {
        TSdxBookAfterReadingNoteVo tSdxBookAfterReadingNoteVo = (TSdxBookAfterReadingNoteVo) ConsumeHelper.getObj();
        if (tSdxBookAfterReadingNoteVo == null) {
            return Response.fail();
        }
        return Response.success(sdxBookAfterReadingNoteService.modTSdxBookAfterReadingNote(tSdxBookAfterReadingNoteVo.copyTSdxBookAfterReadingNotePo()));
    }

    /**
    * 删除书籍读后感
    *
    * @param ids 书籍读后感的Id的集合,例如1,2,3多个用英文,隔开
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
    public Response delTSdxBookAfterReadingNote(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return Response.fail();
        }
        return Response.success(sdxBookAfterReadingNoteService.delTSdxBookAfterReadingNoteByIds(ids));
    }

    /**
    * 查找书籍读后感
    *
    * @param page 页数默认第一页
    * @param size 每页返回的数量，默认10个
    * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
    * @param id        书籍读后感的Id,修改或者查询的需要
    * @param bookId        书本编号
    * @param userId        创作者编号
    * @param bookInfoId        书籍信息编号
    * @param thumbUpNum        点赞数量
    * @param thumbDownNum        点踩数量
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
    @Consume(TSdxBookAfterReadingNoteVo.class)
    public Response findTSdxBookAfterReadingNote(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size,@RequestParam(required = false) String openResponseExplainFlag,@RequestParam(required = false) Long id,@RequestParam(required = false) Long bookId,@RequestParam(required = false) Long userId,@RequestParam(required = false) Long bookInfoId,@RequestParam(required = false) Integer thumbUpNum,@RequestParam(required = false) Integer thumbDownNum) {

    TSdxBookAfterReadingNoteVo tSdxBookAfterReadingNoteVo = (TSdxBookAfterReadingNoteVo) ConsumeHelper.getObj();
        if (tSdxBookAfterReadingNoteVo == null) {
            return Response.fail();
        }
        if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
            return Response.success(JavaDocReader.read(TSdxBookAfterReadingNoteVo.class));
        }
        if(tSdxBookAfterReadingNoteVo.getId()!=null){
            return Response.success(sdxBookAfterReadingNoteService.findTSdxBookAfterReadingNoteById(tSdxBookAfterReadingNoteVo.getId()));
        }
        return Response.success(sdxBookAfterReadingNoteService.findTSdxBookAfterReadingNoteByAll(tSdxBookAfterReadingNoteVo.copyTSdxBookAfterReadingNotePo (),page,size), IdUtil.getTotal());
    }
}
