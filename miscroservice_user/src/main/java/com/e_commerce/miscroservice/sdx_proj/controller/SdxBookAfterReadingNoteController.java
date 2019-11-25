package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookAfterReadingNoteService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookInfoService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
		sdxBookAfterReadingNoteService.buy(vo.getId(), IdUtil.getId());
		return Response.success();
	}

	/**
	 * 点赞/踩
	 * @param bookAfterReadingId
	 * @param option
	 * @return
	 */
	@RequestMapping("thumb")
	@UrlAuth
	public Response thumb(Long bookAfterReadingId, Integer option) {
		sdxBookAfterReadingNoteService.thumb(bookAfterReadingId, IdUtil.getId(), option);
		return Response.success();
	}

}
