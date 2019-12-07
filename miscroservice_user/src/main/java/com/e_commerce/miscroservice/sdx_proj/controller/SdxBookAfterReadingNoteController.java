package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookAfterReadingNoteService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookInfoService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 书袋熊书籍读后感
 * @Author: FangyiXu
 * @Date: 2019-10-25 11:57
 */
@RestController
@RequestMapping("sdx/book/afterReading")
@Log
public class SdxBookAfterReadingNoteController {

	@Autowired
	private SdxBookService sdxBookService;

	@Autowired
	private SdxBookInfoService sdxBookInfoService;

	@Autowired
	private SdxBookAfterReadingNoteService sdxBookAfterReadingNoteService;

	/**
	 * 购买读后感
	 * @param id 书籍读后感编号
	 * @return
	 */
	@RequestMapping("buy")
	@Consume(TSdxBookAfterReadingNoteVo.class)
	@UrlAuth
	public Response buy(Long id) {
		TSdxBookAfterReadingNoteVo vo = (TSdxBookAfterReadingNoteVo) ConsumeHelper.getObj();
		if (vo == null || vo.getId()==null) {
			return Response.fail();
		}
		try {
			sdxBookAfterReadingNoteService.buy(vo.getId(), IdUtil.getId());
		} catch (MessageException e) {
			Response.errorMsg(e.getMessage());
		}
		return Response.success();
	}

	/**
	 * 点赞/踩
	 * @param bookAfterReadingId 读后感编号
	 * @param option 操作，1点赞2点踩a
	 * @return
	 */
	@RequestMapping("thumb")
	@UrlAuth
	public Response thumb(Long bookAfterReadingId, Integer option) {
		sdxBookAfterReadingNoteService.thumb(bookAfterReadingId, IdUtil.getId(), option);
		return Response.success();
	}

	/**
	 * 第几任主人
	 *
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
	@PostMapping("getIndex")
	@Consume(TSdxBookAfterReadingNoteVo.class)
	@UrlAuth
	public Response getIndex(@RequestParam(required = false) Long bookInfoId) {
		TSdxBookAfterReadingNoteVo tSdxBookAfterReadingNoteVo = (TSdxBookAfterReadingNoteVo) ConsumeHelper.getObj();
		tSdxBookAfterReadingNoteVo.setUserId(IdUtil.getId());
		if (tSdxBookAfterReadingNoteVo == null) {
			return Response.fail();
		}
		return Response.success(sdxBookAfterReadingNoteService.getIndex(tSdxBookAfterReadingNoteVo.copyTSdxBookAfterReadingNotePo()));
	}

	/**
	 * 添加或者修改书籍读后感
	 *
	 * @param id        书籍读后感的Id,修改或者查询的需要
	 * @param bookId        书本编号
	 * @param userIds        创作者编号
	 * @param bookInfoId        书籍信息编号
	 * @param thumbUpNum        点赞数量
	 * @param thumbDownNum        点踩数量
	 * @param content			内容
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
	@UrlAuth
	public Response modTSdxBookAfterReadingNote(@RequestParam(required = false) Long id,@RequestParam(required = false) Long bookId,@RequestParam(required = false) Long userIds,@RequestParam(required = false) Long bookInfoId,@RequestParam(required = false) Integer thumbUpNum,@RequestParam(required = false) Integer thumbDownNum, @RequestParam(required = false) String content) {
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
	@UrlAuth
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
	@UrlAuth
	public Response findTSdxBookAfterReadingNote(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) String openResponseExplainFlag, @RequestParam(required = false) Long id, @RequestParam(required = false) Long bookId, @RequestParam(required = false) Long userId, @RequestParam(required = false) Long bookInfoId, @RequestParam(required = false) Integer thumbUpNum, @RequestParam(required = false) Integer thumbDownNum) {

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
