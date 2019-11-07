package com.e_commerce.miscroservice.sdx_proj.controller;

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

	public Object list(Integer pageNum, Integer pageSize, Integer option) {
//		return Response.success(sdxScoreRecordService.list(IdUtil.getId(), pageNum, pageSize, option));
		return null;
	}

}
