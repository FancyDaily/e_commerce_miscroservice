package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
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
@RequestMapping("sdxScoreRecord")
public class SdxScoreRecordController {
    @Autowired
    private SdxScoreRecordService sdxScoreRecordService;

	/**
	 * 积分列表
	 * @param pageNum
	 * @param pageSize
	 * @param option 全部-1收入0支出1
	 * @return
	 */
	@RequestMapping("list")
	@UrlAuth
	public Object list(Integer pageNum, Integer pageSize, Integer option) {
		return Response.success(sdxScoreRecordService.list(IdUtil.getId(), pageNum, pageSize, option));
	}

}
