package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxBookAfterReadingNoteService;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxBookInfoService;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxBookService;
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
	private TSdxBookService sdxBookService;

	@Autowired
	private TSdxBookInfoService sdxBookInfoService;

	@Autowired
	private TSdxBookAfterReadingNoteService sdxBookAfterReadingNoteService;

	/**
	 * 购买读后感
	 * @param id 书籍读后感编号
	 * @return
	 */
	@RequestMapping("bookinfo/detail")
	@Consume(TSdxBookAfterReadingNoteVo.class)
	public Response buy(Long id) {
		TSdxBookAfterReadingNoteVo vo = (TSdxBookAfterReadingNoteVo) ConsumeHelper.getObj();
		if (vo == null || vo.getId()==null) {
			return Response.fail();
		}
		sdxBookAfterReadingNoteService.buy(vo.getId(), IdUtil.getId());
		return Response.success();
	}

}
