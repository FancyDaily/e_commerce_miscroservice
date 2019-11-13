package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxScoreRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 书袋熊积分
*/
@RestController
@RequestMapping("tSdxScoreRecord")
public class SdxScoreRecordController {
    @Autowired
    private SdxScoreRecordService sdxScoreRecordService;

	/**
	 * 积分列表
	 * @param pageNum
	 * @param pageSize
	 * @param option
	 * @return
	 */
	@RequestMapping("list")
	public Object list(Integer pageNum, Integer pageSize, Integer option) {
		return Response.success(sdxScoreRecordService.list(IdUtil.getId(), pageNum, pageSize, option));
	}

}
